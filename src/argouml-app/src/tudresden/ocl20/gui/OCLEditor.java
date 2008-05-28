/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * OCL Editor                                                        *
 * Copyright (C) 2001 Steffen Zschaler.                              *
 *                                                                   *
 * This work is free software; you can redistribute it and/or        *
 * modify it under the terms of the GNU Library General Public       *
 * License as published by the Free Software Foundation; either      *
 * version 2 of the License, or (at your option) any later version.  *
 *                                                                   *
 * This work is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU *
 * Library General Public License for more details.                  *
 *                                                                   *
 * You should have received a copy of the GNU Library General Public *
 * License along with this library; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * See CREDITS file for further details.                             *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

// OCLEditor.java -- new version of the ocl editor intented for practical use
//
// 08/08/2002  [sz9 ]  Fixed issue #511143 by hardcoding line breaking at column 50.
// 10/16/2001  [sz9 ]  Added support for editor instances that whish to prevent
//                     a constraint's context from being edited.
// 10/16/2001  [sz9 ]  Added support for replacing the table model in subclasses.
// 10/16/2001  [sz9 ]  Added support for deleting unedited, freshly created
//                     constraints.
// 03/12/2001  [sz9 ]  Added handling of line and column numbers in parser error
//                     messages.
// 02/23/2001  [sz9 ]  Added helper function to syntax check constraints.
// 02/15/2001  [sz9 ]  Created.
//
package tudresden.ocl20.gui;

//import tudresden.ocl.OclTree;
import tudresden.ocl20.core.MetaModelConst;
import tudresden.ocl20.core.OclModel;
import tudresden.ocl20.core.OclModelException;
import tudresden.ocl20.core.parser.astgen.Heritage;
import tudresden.ocl20.core.parser.astgen.LAttrAstGenerator;
import tudresden.ocl20.core.parser.sablecc.analysis.AttrEvalException;
import tudresden.ocl20.core.parser.sablecc.lexer.Lexer;
import tudresden.ocl20.core.parser.sablecc.lexer.LexerException;
import tudresden.ocl20.core.parser.sablecc.parser.Parser;
import tudresden.ocl20.core.parser.sablecc.parser.ParserException;
import tudresden.ocl20.gui.events.*;
import tudresden.ocl20.integration.OCLChecker;

import javax.jmi.xmi.XmiWriter;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.*;

import org.apache.log4j.Logger;
import org.argouml.model.Facade;
import org.argouml.model.Model;
import org.argouml.model.ModelManagementFactory;
import org.argouml.model.UmlException;
import org.argouml.ocl.ArgoUMLModelFacade;
import org.netbeans.api.xmi.XMIOutputConfig;
import org.netbeans.api.xmi.XMIWriterFactory;
import org.netbeans.lib.jmi.xmi.OutputConfig;



/**
  * An editor for a list of OCL Constraints. The editor allows editing
  * of a list of {@link ConstraintRepresentation constraint representations}
  * as specified by its {@link OCLEditorModel model}.
  *
  * @author  sz9
  * @author Raphael Schmid
  */
public class OCLEditor extends javax.swing.JPanel
                        implements javax.swing.event.ListSelectionListener,
                                   ConstraintChangeListener,
                                   EditingUtilities {

    private static final Logger LOG = Logger.getLogger(OCLEditor.class);
    
    private static final long serialVersionUID = -5645723403437695527L;

    /**
     * Attributes used to denote fields that need to be replaced.
     */
  private static SimpleAttributeSet s_sasField
      = new SimpleAttributeSet();
  /**
    * Attributes used to denote normal OCL constraint text.
    */
  private static SimpleAttributeSet s_sasNormalText
      = new SimpleAttributeSet();

  /**
    * Attributes used to denote uneditable text.
    */
  private static SimpleAttributeSet s_sasNoEditText
      = new SimpleAttributeSet();
  
  static {
    if (StyleConstants.Alignment != null) {
      // Just a dummy to properly initialize StyleConstants...
      // for some reason this seems to be necessary...
    }
    
    s_sasField.addAttribute (
        StyleConstants.CharacterConstants.Underline,
        Boolean.TRUE);
    s_sasField.addAttribute (
        "isField",
        Boolean.TRUE);

    s_sasNoEditText.addAttribute (
        StyleConstants.CharacterConstants.Italic,
        Boolean.TRUE);
    s_sasNoEditText.addAttribute (
        "allowEdit",
        Boolean.FALSE);
    // provoke automatic selection of the whole un-editable text
    // when the cursor enters it
    s_sasNoEditText.addAttribute (
        "isField",
        Boolean.TRUE);
  }
  
  private static String imagePath = "/tudresden/ocl20/images/";

  /**
   * A specialized styled document that will prevent editing of portions of text
   * which have been specially marked "allowEdit"==FALSE.
   */
  protected static class OCLEditorDocument extends DefaultStyledDocument {
    
    /**
     * If >= 0, {@link #checkEditLocation} will throw a
     * {@link BadLocationException} on attempts to remove or modify elements
     * which are marked "allowEdit"==FALSE.
     */
    private int m_nDoCheckUneditable = 0;
    
    public void insertString (final int offset,
                              final String str,
                              final AttributeSet a)
        throws BadLocationException {
      checkEditLocation (offset, 0);
      
      super.insertString (offset, str, a);
    }
    
    public void remove (int offset,
                        int length)
        throws BadLocationException {
      checkEditLocation (offset, length);
      
      super.remove (offset, length);
    }
    
    /**
     * Stop checking for uneditable elements. Must be paired with calls to 
     * {@link #restartChecking}.
     */
    public void stopChecking() {
      m_nDoCheckUneditable --;
    }
    
    /**
     * Restart checking for uneditable elements. Must be paired with calls to 
     * {@link #stopChecking}.
     */
    public void restartChecking() {
      m_nDoCheckUneditable ++;
    }
    
    /**
     * Check the edit range for intersection with any elements that are marked
     * uneditable.
     */
    protected void checkEditLocation (int offset,
                                      int length)
        throws BadLocationException {
      if (m_nDoCheckUneditable < 0) {
        return;
      }
       
      //LOG.debug ("OCLEditor$OCLEditorDocument.checkEditLocation: invoked for (" + offset + ", " + length + ")");
      // Get first affected element    
      Element eAffected = getDefaultRootElement();
      while (! eAffected.isLeaf()) {
        eAffected = eAffected.getElement (
            eAffected.getElementIndex (offset));
      }

      // check all affected elements
      while (
             (eAffected != null) &&
             (
              (eAffected.getStartOffset() <= offset) ||
              (eAffected.getEndOffset() >= offset + length)
             )
            ) {
        if (eAffected.getAttributes().getAttribute ("allowEdit") == Boolean.FALSE) {
          // do not allow edit
          //LOG.debug ("OCLEditor$OCLEditorDocument.checkEditLocation: denying edit for (" + offset + ", " + length + ")");
          throw new BadLocationException (
              "Editing not allowed.",
              Math.max (offset, eAffected.getStartOffset())
            );
        }
        else {
          eAffected = getNeighbouringElement (eAffected);
        }
      }
    }
    
    protected Element getNeighbouringElement (Element e) {
      Element eTemp = e.getParentElement();
      
      // move up in the tree
      while ((eTemp != null) &&
             (eTemp.getEndOffset() <= e.getEndOffset())) {
        e = eTemp;
        eTemp = eTemp.getParentElement();
      }
      
      // and down again
      while ((eTemp != null) &&
             (! eTemp.isLeaf())) {
        eTemp = eTemp.getElement (eTemp.getElementIndex (e.getEndOffset()));
      }
      
      return eTemp;
    }
  }
  
  /**
    * Table model for the table of constraints.
    *
    * @author sz9
    */
  protected static class ConstraintTableModel extends javax.swing.table.AbstractTableModel
                                                 implements ConstraintChangeListener {
    /**
      * The OCLEditorModel on which this table model is based.
      */
    protected OCLEditorModel m_oclemModel;
    
    /**
     * The OCLEditor for which this table model is used.
     */
    protected java.lang.ref.WeakReference m_wrocle;
    
    public ConstraintTableModel (OCLEditor ocle) {
      super();
      
      m_wrocle = new java.lang.ref.WeakReference (ocle);
    }
    
    /**
      * Set the OCLEditorModel to base this table model on.
      */
    public void setOCLModel (OCLEditorModel oclemModel) {
      if (m_oclemModel != null) {
        m_oclemModel.removeConstraintChangeListener (this);
      }
      
      m_oclemModel = oclemModel;
      
      if (m_oclemModel != null) {
        m_oclemModel.addConstraintChangeListener (this);
      }
      
      fireTableDataChanged();
    }
    
    /**
      * Return the number of rows in the table. This returns the number of
      * constraints in the underlying model.
      */
    public int getRowCount() {
      if (m_oclemModel != null) {
        return m_oclemModel.getConstraintCount();
      }
      else {
        return 0;
      }
    }
    
    /**
      * This table model has one column.
      */
    public int getColumnCount() {
      return 1;
    }

    /**
      * This table model has one column: &quot;Constraint name&quot;.
      */
    public String getColumnName (int nIdx) {
      return "Constraint Name";
    }
    
    /**
      * This table model has one column: &quot;String.class&quot;.
      */
    public Class getColumnClass (int nIdx) {
      return String.class;
    }
    
    /**
      * Get the constraint name of the specified constraint.
      */
    public Object getValueAt (int row, int column) {
      if (m_oclemModel != null) {
        ConstraintRepresentation cr = m_oclemModel.getConstraintAt (row);
        
        if (cr != null) {
          return cr.getName();
        }
        else {
          return null;
        }
      }
      else {
        return null;
      }
    }
    
    /**
      * Constraint names can be edited.
      */
    public boolean isCellEditable (int row, int column) {
      return column == 0;
    }
    
    /**
      * Set the edited constraint name.
      */
    public void setValueAt (Object value, int row, int column) {
      if (column == 0) {
        if (m_oclemModel != null) {
          ConstraintRepresentation cr = m_oclemModel.getConstraintAt (row);

          if (cr != null) {
            try {
              cr.setName (value.toString(), (OCLEditor) m_wrocle.get());
            }
            catch (IllegalArgumentException iae) {
              JOptionPane.showMessageDialog (null,
                                               "Invalid name: " +
                                                   iae.getMessage(),
                                               "Error",
                                               JOptionPane.ERROR_MESSAGE);
            }
            catch (IllegalStateException ise) {
              JOptionPane.showMessageDialog (null,
                                               "Couldn't set name: " +
                                                   ise.getMessage(),
                                               "Error",
                                               JOptionPane.ERROR_MESSAGE);
            }
          }
        }
      }
    }
    
    // ConstraintChangeListener interface methods
    
    /**
      * Relay the event to the table indicating a row was added.
      */
    public void constraintAdded (ConstraintChangeEvent cce) {
      fireTableRowsInserted (cce.getIndex(), cce.getIndex());
    }
    
    /**
      * Relay the event to the table indicating a row was deleted.
      */
    public void constraintRemoved(ConstraintChangeEvent cce) {
      fireTableRowsDeleted (cce.getIndex(), cce.getIndex());
    }
    
    /**
      * Relay the event to the table indicating the data in the cell changed.
      */
    public void constraintNameChanged(ConstraintChangeEvent cce) {
      fireTableCellUpdated (cce.getIndex(), 0);
    }
    
    /**
      * Ignored.
      */
    public void constraintDataChanged(ConstraintChangeEvent cce) {
    }
  }
  
  /**
    * The underlying model.
    */
  private OCLEditorModel m_oclemModel;
  
  /**
    * The currently selected constraint, if any.
    */
  private ConstraintRepresentation m_crCurrent;
  
  /**
    * The table model used by the list of constraints table.
    */
  private ConstraintTableModel m_ctmTableModel;

  /**
   * Does {@link #parseAndCheckConstraint} perform type checking?
   */
  private boolean m_fDoTypeCheck = true;
  
  /**
   * Should multi-constraint inputs be automatically split into individual
   * constraints?
   */
  private boolean m_fDoAutoSplit = true;

  /**
   * Should the constraints context (everything up to the first 'inv', 'pre',
   * or 'post') be un-editable?
   */
  private boolean m_fNoContextEdit = false;
  
  public static final int OPTIONMASK_TYPECHECK = 1;
  public static final int OPTIONMASK_AUTOSPLIT = 2;

  /**
    * The options supported by this instance of the editor. I.e., the options
    * which will show up in the options dialog.
    */
  private int m_nOptionMask = OPTIONMASK_TYPECHECK | OPTIONMASK_AUTOSPLIT;

  /**
   * If true, we're in edit mode.
   */
  private boolean m_fInEditMode = false;

  /**
   * Set to false when beginning edit mode, evaluated at the end of editing a
   * newly created constraint to determine whether to remove it again. When a
   * {@link #constraintDataChanged} event is received for the currently selected
   * constraint, this is set to true.
   */
  private boolean m_fConstraintChanged;

  /**
   * Indicates the constraint currently being edited is a newly created one.
   * This means, it will be removed if no changes where made to it.
   */
  private boolean m_fCreatedFreshConstraint = false;
  
  /** Creates new form OCLEditor */
  public OCLEditor() {
    m_ctmTableModel = createConstraintTableModel();
    
    initComponents ();
    
    /*
     * Remove editor panel, which was added only to be able to manipulate it
     * from the IDE.
     */
    m_jpToolbarWrapper.remove (m_jpEditorPanel);
    
    m_jtConstraintList.getSelectionModel()
        .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    m_jtConstraintList.getSelectionModel()
        .addListSelectionListener (this);
    
    m_ocltbQuickBar.setVisible (false);
  }

  /**
   * Create and return an instance of ConstraintTableModel that will be used
   * to represent the list of constraints currently being edited.
   *
   * <p><strong>Attention!</strong> This method is called from the constructor
   * and should therefore be overridden with great care. When referencing
   * <code>this</code> from within this method care must be taken to recognize
   * that <code>this</code> will not have been fully initialized yet. In
   * particular, any field introduced by subclasses of {@link OCLEditor}
   * will not have been initialized, and any code specified in constructors of
   * subclasses of {@link OCLEditor} will not have been executed.
   *
   * @return an instance of ConstraintTableModel to be used to display the list
   *   of constraints being edited
   */
  protected ConstraintTableModel createConstraintTableModel() {
     return new ConstraintTableModel (this);
  }
  
  /**
    * Retrieve the underlying model.
    */
  public OCLEditorModel getModel() {
    return m_oclemModel;
  }
  
  /**
    * Set the underlying model and update the display.
    */
  public void setModel (OCLEditorModel oclemModel) {
    setEditMode (false);
    
    if (m_oclemModel != null) {
      m_oclemModel.removeConstraintChangeListener (this);
    }
    
    m_oclemModel = oclemModel;
    
    m_ctmTableModel.setOCLModel (oclemModel);
    
    if (m_oclemModel != null) {
      m_oclemModel.addConstraintChangeListener (this);
    }
    
    m_jtConstraintList.clearSelection();
  }

  /**
   * Return true if the OCL editor is currently in edit mode.
   */
  public boolean isInEditMode() {
    return m_fInEditMode;
  }
  
  /**
   * Set the edit mode of the OCL editor. Setting the edit mode to false cancels
   * any current edit.
   */
  public void setEditMode (boolean fEditMode) {
    if (fEditMode == m_fInEditMode) {
      return;
    }
    
    // From here it's basically a toggling of edit mode
    if (! m_fInEditMode) {
      // Only switch to edit mode if current constraint exists
      if (m_crCurrent == null) {
        return;
      }
      
      m_fInEditMode = true;
      m_fConstraintChanged = false;
      
      m_jpToolbarWrapper.remove (m_jspMainPane);
      m_jpToolbarWrapper.add (m_jpEditorPanel, java.awt.BorderLayout.CENTER);

      /*m_jbNew.setEnabled (false);
      m_jbRemove.setEnabled (false);*/
      m_jbNew.setVisible (false);
      m_jbRemove.setVisible (false);

      m_jbEdit.setIcon (new javax.swing.ImageIcon (getClass ().getResource (imagePath + "Cancel16.gif")));
      m_jbEdit.setToolTipText ("Click to cancel editing without saving changes");
      //m_jbEdit.setText ("Stop Edit");

      /*m_jbSaveEditResult.setEnabled (true);*/
      m_jbSaveEditResult.setVisible (true);
      
      m_jtpConstraintEditor.requestFocus();      
      
      m_ocltbQuickBar.setVisible (m_jcbQuickBar.isSelected());
      /*m_jcbQuickBar.setEnabled (true);*/
      m_jcbQuickBar.setVisible (true);
    }
    else {
      m_fInEditMode = false;
    
      if (m_fCreatedFreshConstraint &&
          ! m_fConstraintChanged) {
        int nIdx = m_jtConstraintList.getSelectedRow();

        if (nIdx != -1) {
          m_oclemModel.removeConstraintAt (nIdx);
        }
      }
      
      // reset flags
      m_fCreatedFreshConstraint = false;
      m_fConstraintChanged = false;
      
      m_jpToolbarWrapper.remove (m_jpEditorPanel);
      m_jpToolbarWrapper.add (m_jspMainPane, java.awt.BorderLayout.CENTER);

      setEditorText (m_crCurrent);
      
      /*m_jbNew.setEnabled (true);
      m_jbRemove.setEnabled (true);*/
      m_jbNew.setVisible (true);
      m_jbRemove.setVisible (true);

      m_jbEdit.setIcon (new javax.swing.ImageIcon (getClass ().getResource (imagePath + "Edit16.gif")));
      m_jbEdit.setToolTipText ("Click to edit the currently selected constraint");
      //m_jbEdit.setText ("Edit");

      /*m_jbSaveEditResult.setEnabled (false);*/
      m_jbSaveEditResult.setVisible (false);

      m_ocltbQuickBar.setVisible (false);
      /*m_jcbQuickBar.setEnabled (false);*/
      m_jcbQuickBar.setVisible (false);
    }
    
    m_jpToolbarWrapper.revalidate();
    m_jpToolbarWrapper.repaint();
  }
    
  /**
   * Checks the specified name and returns true if it is a valid OCL name.
   */
  public boolean isValidConstraintName (String sName) {
    if ((sName == null) ||
         (sName.length() == 0)) {
      return false;
    }
    if ((! Character.isLetter (sName.charAt (0))) ||
         (! Character.isLowerCase (sName.charAt (0)))) {
      return false;
    }
    for (int i = 1; i < sName.length(); i++) {
      if ((! Character.isLetterOrDigit (sName.charAt (i))) &&
          (sName.charAt (i) != '_')) {
        return false;
      }
    }
    
    return true;
  }
  
  /**
   * Specify which user option check boxes should be displayed.
   */
  public void setOptionMask (int nOptionMask) {
    m_nOptionMask = nOptionMask;
    
    if (m_nOptionMask == 0) {
      return;
    }
  }

  public int getOptionMask() {
    return m_nOptionMask;
  }
  
  /**
   * Specify whether or not to allow editing the context of a constraint.
   */
  public void setNoContextEdit (boolean fNoContextEdit) {
    m_fNoContextEdit = fNoContextEdit;
  }
  
  /**
   * Return whether context editing is currently prohibited.
   */
  public boolean getNoContextEdit() {
    return m_fNoContextEdit;
  }
  
  /**
    * Set the type checking mode.
    */
  public void setDoTypeCheck (boolean fDoTypeCheck) {
    m_fDoTypeCheck = fDoTypeCheck;
  }
  
  /**
   * Return whether type checking mode is on. If this returns true,
   * {@link #parseAndCheckConstraint} will also check type conformance of
   * constraints.
   */
  public boolean getDoTypeCheck() {
    return m_fDoTypeCheck;
  }
  
  /**
   * Check the specified constraint.
   * 
   * @exception OclModelException thrown by the methods of {@link OclModel OclModel} in case of an error.
   * @exception ParserException if a syntax error occurred.
   * @exception LexerException .
   * @exception AttrEvalException if a type checking error occurred.
   * @exception IOException if an I/O operation failed.
   */
  public void parseAndCheckConstraint (final String sConstraint)
  // Updated by Raphael Schmid
    throws OclModelException, ParserException, LexerException, AttrEvalException, IOException {
		if (this.getDoTypeCheck()) {
	  		String constraint = m_jtpConstraintEditor.getText();
	  	  
		    // Project p = ProjectManager.getManager().getCurrentProject();
		  	// Object o = p.getRoot();
		  	
		  	ModelManagementFactory mmf = Model.getModelManagementFactory();
		  	Object o = mmf.getRootModel();
		  	
		    //LOG.debug("Root-package: " + o.getClass().getName());
			OCLChecker oclChecker = OCLChecker.getInstance(o);
			try {
				oclChecker.setModelFacade(new ArgoUMLModelFacade());
				// oclChecker.initModel(); // not necessary, already called by getInstance()!
			} catch (Exception e) {
				// TODO
				e.printStackTrace();
			}
			//LOG.debug("check the following constraint: " + constraint);
				oclChecker.validate(constraint);
	  	} else {
	  		// don't perform OCL type checking
	  		
	  	}
  }
  
//  
//  /**
//   * Check the specified constraint using the specified model facade for model
//   * information. Return the parse tree for the constraint.
//   * 
//   * <p>This is a short-cut helper function for editor models that want to parse
//   * constraints before adding them to the model base.</p>
//   * 
//   * @exception OclParserException if a syntax error occurred.
//   * @exception IOException if an I/O operation failed.
//   * @exception OclTypeException if a type checking error occurred.
//   */
//  public OclTree parseAndCheckConstraint (final String sConstraint,
//                                              final ModelFacade mfFacade)
//    throws OclParserException,
//            java.io.IOException,
//            OclTypeException {
//    // Parse and syntax check
//    final OclTree tree = OclTree.createTree (sConstraint,
//                                               mfFacade);
//    
//    // Type check
//    if ((tree != null) &&
//         (m_fDoTypeCheck)) {
//       DepthFirstAdapter dfaTypeChecker = new DepthFirstAdapter() {
//        private RuntimeException m_rteException = null;
//        
//        public void inStart (Start node) {
//          defaultIn(node);
//        }
//
//        public void outStart(Start s) {
//          if (m_rteException != null) {
//            throw m_rteException;
//          }
//        }
//        
//        public void defaultIn(Node node) {
//          try {
//            Object o = tree.getNodeType(node);
//          }
//          catch (RuntimeException e) {
//            if (m_rteException == null) {
//              m_rteException = e;
//            }
//          }
//        }
//      };
//
//      try {
//        tree.apply (dfaTypeChecker);
//
//        try {
//          tree.applyGeneratedTests();
//        }
//        catch (java.security.AccessControlException ace) {
//          // SecurityManager denies access to non-public fields
//          // Ignore and assume all is correct
//        }
//      }
//      catch (Exception e) {
//        e.printStackTrace();
//        throw new OclTypeException (e.getMessage());
//      }
//    }
//
//    return tree;
//  }

  /**
    * Set the auto split mode.
    */
  public void setDoAutoSplit (boolean fDoAutoSplit) {
    m_fDoAutoSplit = fDoAutoSplit;
  }
  
  /**
   * Return whether auto split mode is on. If this returns true, constraint
   * representations should call {@link #splitConstraint} and create one
   * constraint representation per actual constraint.
   */
  public boolean getDoAutoSplit() {
    return m_fDoAutoSplit;
  }
  
//  /**
//   * Split the specified constraint into its constituting constraints. E.g.
//   * <pre>
//   * context Test
//   * inv: a > 0
//   * inv: a < 10
//   * inv: a * 100 = 900
//   * </pre>
//   * would be split into three constraints:
//   * <pre>
//   * context Test
//   * inv: a > 0
//   *
//   * context Test
//   * inv: a < 10
//   *
//   * context Test
//   * inv: a * 100 = 900
//   * </pre>
//   */
//  public List splitConstraint (OclTree ocltConstraint) {
//    final List lResult = new LinkedList();
//    
//    class Splitter extends DepthFirstAdapter {
//      public void caseAConstraint(AConstraint node) {
//        PContextDeclaration pcd = node.getContextDeclaration();
//        
//        for (Iterator i = node.getConstraintBody().iterator(); i.hasNext();) {
//          PConstraintBody pcbCurrent = (PConstraintBody) i.next();
//          
//          List lTempBody = new LinkedList();
//          lTempBody.add (pcbCurrent.clone());
//
//          lResult.add (new OclTree (
//              new Start (
//                new AConstraint ((PContextDeclaration) pcd.clone(),
//                                   lTempBody),
//                new EOF()
//              )
//          ));
//        }
//      }
//    }
//    
//    ocltConstraint.apply (new Splitter());
//    
//    return lResult;
//  }

  /**
   * Add the given text to the edit pane, if a constraint is currently being
   * edited. The text will replace the current selection. If saBefore/saAfter
   * are not <code>null</code> and contain elements, these will be added as
   * items to be replaced. The first such item will be selected.
   */
  void addConstraintText (String[] saBefore,
                             String sText,
                             String[] saAfter) {
    if ((m_crCurrent != null) && isInEditMode()) {
      int nSelStart = -1;
      int nSelEnd = -1;
      
      this.m_fHandleCaretUpdates = false;
      
      m_jtpConstraintEditor.replaceSelection ("");
      
      if (saBefore != null) {
        m_jtpConstraintEditor.setCharacterAttributes (s_sasField, true);
        for (int i = 0; i < saBefore.length; i++) {
          if (i == 0) {
            nSelStart = m_jtpConstraintEditor.getSelectionStart();
            nSelEnd = nSelStart + saBefore[i].length();
          }
          
          m_jtpConstraintEditor.replaceSelection (saBefore[i]);
        }
        m_jtpConstraintEditor.setCharacterAttributes (s_sasNormalText, true);
      }
      
      m_jtpConstraintEditor.replaceSelection (sText);
      
      if (saAfter != null) {
        m_jtpConstraintEditor.setCharacterAttributes (s_sasField, true);
        for (int i = 0; i < saAfter.length; i++) {
          if ((i == 0) &&
               (nSelStart == -1)) {
            nSelStart = m_jtpConstraintEditor.getSelectionStart();
            nSelEnd = nSelStart + saAfter[i].length();
          }
          
          m_jtpConstraintEditor.replaceSelection (saAfter[i]);
        }
        m_jtpConstraintEditor.setCharacterAttributes (s_sasNormalText, true);
      }

      if (nSelStart != -1) {
        m_jtpConstraintEditor.paintImmediately (
            0, 0,
            m_jtpConstraintEditor.getWidth(),
            m_jtpConstraintEditor.getHeight());
        m_jtpConstraintEditor.setSelectionStart (nSelStart);
        m_jtpConstraintEditor.setSelectionEnd (nSelEnd);
      }
      this.m_fHandleCaretUpdates = true;
      
/*      java.awt.Frame fTop = (java.awt.Frame) getTopLevelAncestor();
      fTop.toFront();
      fTop.requestFocus();
      m_jtpConstraintEditor.requestFocus();*/
    }
  }

  /**
   * Replace the contents of the edior by the data of the specified constraint.
   */
  protected void setEditorText (ConstraintRepresentation cr) {
    m_jtpConstraintEditor.setCharacterAttributes (s_sasNormalText, true);
    
    if (cr != null) {
      if (m_fNoContextEdit) {
        ((OCLEditorDocument) m_jtpConstraintEditor.getDocument()).stopChecking();
        m_jtpConstraintEditor.setText ("");
        ((OCLEditorDocument) m_jtpConstraintEditor.getDocument()).restartChecking();
        
        String s = cr.getData();
        
        int nInvPos = s.indexOf ("inv"); if (nInvPos == -1) { nInvPos = s.length(); }
        int nPrePos = s.indexOf ("pre"); if (nPrePos == -1) { nPrePos = s.length(); }
        int nPostPos = s.indexOf ("post"); if (nPostPos == -1) { nPostPos = s.length(); }
        
        int nContextEnd =
            Math.min (
              Math.min (
                nInvPos,
                nPrePos
              ),
              nPostPos
            );
        
        m_jtpConstraintEditor.setCharacterAttributes (
            s_sasNoEditText,
            true
          );
        m_jtpConstraintEditor.replaceSelection (s.substring (0, nContextEnd));

        m_jtpConstraintEditor.setCharacterAttributes (
            s_sasNormalText,
            true
          );
        m_jtpConstraintEditor.replaceSelection (s.substring (nContextEnd));
      }
      else {
        // TODO: Deal with fields.
        ((OCLEditorDocument) m_jtpConstraintEditor.getDocument()).stopChecking();
        m_jtpConstraintEditor.setText (cr.getData());
        ((OCLEditorDocument) m_jtpConstraintEditor.getDocument()).restartChecking();
      }
    }
    else {
      ((OCLEditorDocument) m_jtpConstraintEditor.getDocument()).stopChecking();
      m_jtpConstraintEditor.setText ("");
      ((OCLEditorDocument) m_jtpConstraintEditor.getDocument()).restartChecking();
    }
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the FormEditor.
   */
  private void initComponents () {//GEN-BEGIN:initComponents
    m_jtbTools = new javax.swing.JToolBar ();
    m_jbNew = new javax.swing.JButton ();
    m_jbRemove = new javax.swing.JButton ();
    m_jbEdit = new javax.swing.JButton ();
    m_jbSaveEditResult = new javax.swing.JButton ();
    m_jbSaveEditResult.setVisible (false);
    pad1 = new javax.swing.JPanel ();
    m_jbPreferences = new javax.swing.JButton ();
    xmiExportButton = new javax.swing.JButton();
    pad2 = new javax.swing.JPanel ();
    m_jcbQuickBar = new javax.swing.JCheckBox ();
    m_jcbQuickBar.setVisible (false);
    m_jpToolbarWrapper = new javax.swing.JPanel ();
    m_ocltbQuickBar = new tudresden.ocl20.gui.OCLToolbar ();
    m_jpEditorPanel = new javax.swing.JPanel ();
    m_jspConstraintEditorScroller = new javax.swing.JScrollPane ();
    m_jtpConstraintEditor = new javax.swing.JTextPane ();
    m_jspMainPane = new javax.swing.JSplitPane ();
    m_jpConstraintListPane = new javax.swing.JPanel ();
    m_jspConstraintListScroller = new javax.swing.JScrollPane ();
    m_jtConstraintList = new javax.swing.JTable ();
    m_jpPreviewPane = new javax.swing.JPanel ();
    m_jpPreviewGroup = new javax.swing.JPanel ();
    m_jspConstraintPreviewScroller = new javax.swing.JScrollPane ();
    m_jtpConstraintPreview = new javax.swing.JTextPane ();
    
    setLayout (new java.awt.BorderLayout ());
    
    setPreferredSize (new java.awt.Dimension (500, 300));
    setMinimumSize (new java.awt.Dimension (500, 300));
    m_jbNew.setIcon (new javax.swing.ImageIcon (getClass ().getResource (imagePath + "New16.gif")));
    m_jbNew.setToolTipText ("Click to create a new constraint");
    m_jbNew.setMargin (new java.awt.Insets (0, 0, 0, 0));
    m_jbNew.addActionListener (new java.awt.event.ActionListener () {
      public void actionPerformed (java.awt.event.ActionEvent evt) {
        onNewConstraintButton (evt);
      }
    });
    
    m_jtbTools.add (m_jbNew);
    
    m_jbRemove.setIcon (new javax.swing.ImageIcon (getClass ().getResource (imagePath + "Delete16.gif")));
    m_jbRemove.setToolTipText ("Click to remove the currently selected constraint");
    m_jbRemove.setMargin (new java.awt.Insets (0, 0, 0, 0));
    m_jbRemove.addActionListener (new java.awt.event.ActionListener () {
      public void actionPerformed (java.awt.event.ActionEvent evt) {
        onRemoveConstraintButton (evt);
      }
    });
    
    m_jtbTools.add (m_jbRemove);
    
    m_jbEdit.setIcon (new javax.swing.ImageIcon (getClass ().getResource (imagePath + "Edit16.gif")));
    m_jbEdit.setToolTipText ("Click to edit the currently selected constraint");
    m_jbEdit.setMargin (new java.awt.Insets (0, 0, 0, 0));
    m_jbEdit.addActionListener (new java.awt.event.ActionListener () {
      public void actionPerformed (java.awt.event.ActionEvent evt) {
        onEditButton (evt);
      }
    });
    
    m_jtbTools.add (m_jbEdit);
    
    m_jbSaveEditResult.setIcon (new javax.swing.ImageIcon (getClass ().getResource (imagePath + "Ok16.gif")));
    m_jbSaveEditResult.setToolTipText ("Check OCL syntax and save constraint into model");
    m_jbSaveEditResult.setMargin (new java.awt.Insets (0, 0, 0, 0));
    m_jbSaveEditResult.addActionListener (new java.awt.event.ActionListener () {
      public void actionPerformed (java.awt.event.ActionEvent evt) {
        onSubmitConstraintButton (evt);
      }
    });
    
    m_jtbTools.add (m_jbSaveEditResult);
    
    pad1.setMaximumSize (new java.awt.Dimension (10, 10));
    m_jtbTools.add (pad1);
    
    m_jbPreferences.setIcon (new javax.swing.ImageIcon (getClass ().getResource (imagePath + "Preferences16.gif")));
    m_jbPreferences.setToolTipText ("Click to inspect and modify OCL editor preferences.");
    m_jbPreferences.addActionListener (new java.awt.event.ActionListener () {
      public void actionPerformed (java.awt.event.ActionEvent evt) {
        onPreferencesButton (evt);
      }
    });
    
    m_jtbTools.add (m_jbPreferences);
    
    xmiExportButton.setIcon (new javax.swing.ImageIcon (getClass ().getResource (imagePath + "save_icon.gif")));
    xmiExportButton.setToolTipText ("Click to export the model and constraints in XMI.");
    xmiExportButton.addActionListener (new java.awt.event.ActionListener () {
      public void actionPerformed (java.awt.event.ActionEvent evt) {
        onXMIExportButton (evt);
      }
    });
       
    m_jtbTools.add (xmiExportButton);
    
    pad2.setPreferredSize (new java.awt.Dimension (5, 10));
    pad2.setMinimumSize (new java.awt.Dimension (5, 10));
    pad2.setMaximumSize (new java.awt.Dimension (5, 10));
    m_jtbTools.add (pad2);
    
    m_jcbQuickBar.setToolTipText ("Check to see the syntax assistant toolbar");
    m_jcbQuickBar.setText ("Syntax Assistant");
    m_jcbQuickBar.addActionListener (new java.awt.event.ActionListener () {
      public void actionPerformed (java.awt.event.ActionEvent evt) {
        onQuickBarButton (evt);
      }
    });
    
    m_jtbTools.add (m_jcbQuickBar);
    
    add (m_jtbTools, java.awt.BorderLayout.NORTH);
    
    m_jpToolbarWrapper.setLayout (new java.awt.BorderLayout ());
    
    m_ocltbQuickBar.setEditor (this);
    m_jpToolbarWrapper.add (m_ocltbQuickBar, java.awt.BorderLayout.NORTH);
    
    m_jpEditorPanel.setLayout (new java.awt.GridBagLayout ());
    java.awt.GridBagConstraints gridBagConstraints1;
    
    m_jpEditorPanel.setBorder (new javax.swing.border.TitledBorder ("Edit constraint"));
    m_jtpConstraintEditor.setToolTipText ("Edit the constraint expression");
    m_jtpConstraintEditor.setDocument (new OCLEditorDocument ());
    m_jtpConstraintEditor.addCaretListener (new javax.swing.event.CaretListener () {
      public void caretUpdate (javax.swing.event.CaretEvent evt) {
        onCaretUpdate (evt);
      }
    });
    
    m_jspConstraintEditorScroller.setViewportView (m_jtpConstraintEditor);
    
    gridBagConstraints1 = new java.awt.GridBagConstraints ();
    gridBagConstraints1.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints1.gridheight = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints1.insets = new java.awt.Insets (5, 5, 10, 5);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints1.weightx = 1.0;
    gridBagConstraints1.weighty = 1.0;
    m_jpEditorPanel.add (m_jspConstraintEditorScroller, gridBagConstraints1);
    
    m_jpToolbarWrapper.add (m_jpEditorPanel, java.awt.BorderLayout.CENTER);
    
    m_jspMainPane.setOneTouchExpandable (true);
    m_jpConstraintListPane.setLayout (new java.awt.GridBagLayout ());
    java.awt.GridBagConstraints gridBagConstraints2;
    
    m_jpConstraintListPane.setPreferredSize (new java.awt.Dimension (100, 37));
    m_jtConstraintList.setToolTipText ("Lists all constraints. Selecting a constraint, shows its body in the right hand preview pane. Clicking twice on a constraint allows editing the constraint's name.");
    m_jtConstraintList.setModel (m_ctmTableModel);
    m_jspConstraintListScroller.setViewportView (m_jtConstraintList);
    
    gridBagConstraints2 = new java.awt.GridBagConstraints ();
    gridBagConstraints2.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints2.insets = new java.awt.Insets (10, 5, 5, 5);
    gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints2.weightx = 1.0;
    gridBagConstraints2.weighty = 1.0;
    m_jpConstraintListPane.add (m_jspConstraintListScroller, gridBagConstraints2);
    
    m_jspMainPane.setLeftComponent (m_jpConstraintListPane);
    
    m_jpPreviewPane.setLayout (new java.awt.GridBagLayout ());
    java.awt.GridBagConstraints gridBagConstraints3;
    
    m_jpPreviewPane.setPreferredSize (new java.awt.Dimension (100, 80));
    m_jpPreviewGroup.setLayout (new java.awt.GridBagLayout ());
    java.awt.GridBagConstraints gridBagConstraints4;
    
    m_jpPreviewGroup.setBorder (new javax.swing.border.TitledBorder ("Preview"));
    m_jtpConstraintPreview.setToolTipText ("Constraint expression preview");
    m_jtpConstraintPreview.setEditable (false);
    m_jspConstraintPreviewScroller.setViewportView (m_jtpConstraintPreview);
    
    gridBagConstraints4 = new java.awt.GridBagConstraints ();
    gridBagConstraints4.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints4.insets = new java.awt.Insets (5, 5, 5, 5);
    gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints4.weightx = 1.0;
    gridBagConstraints4.weighty = 1.0;
    m_jpPreviewGroup.add (m_jspConstraintPreviewScroller, gridBagConstraints4);
    
    gridBagConstraints3 = new java.awt.GridBagConstraints ();
    gridBagConstraints3.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints3.gridheight = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints3.insets = new java.awt.Insets (0, 5, 20, 5);
    gridBagConstraints3.weightx = 1.0;
    gridBagConstraints3.weighty = 1.0;
    m_jpPreviewPane.add (m_jpPreviewGroup, gridBagConstraints3);
    
    m_jspMainPane.setRightComponent (m_jpPreviewPane);
    
    m_jpToolbarWrapper.add (m_jspMainPane, java.awt.BorderLayout.CENTER);
    
    add (m_jpToolbarWrapper, java.awt.BorderLayout.CENTER);
    
  }//GEN-END:initComponents
  
  private void onEditButton (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onEditButton
    // Toggle edit mode
    setEditMode (! isInEditMode());
  }//GEN-LAST:event_onEditButton

  private void onNewConstraintButton (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onNewConstraintButton
    if (m_oclemModel != null) {
      int nOldCount = m_oclemModel.getConstraintCount();
      m_oclemModel.addConstraint();
      
      if (m_oclemModel.getConstraintCount() > nOldCount) {
        // New constraint was added, it should now also be selected because of
        // our reaction to the constraintAdded event.
        m_fCreatedFreshConstraint = true; // mark as new constraint
        setEditMode (true);
      }
    }
  }//GEN-LAST:event_onNewConstraintButton

  private void onPreferencesButton (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onPreferencesButton
    new OCLEditorPreferences (new JFrame(), this).setVisible (true);
  }//GEN-LAST:event_onPreferencesButton

  private boolean m_fHandleCaretUpdates = true;
  
  private int m_nOldDot = 0;
  private int m_nOldMark = 0;
  
  private void onCaretUpdate (javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_onCaretUpdate
    if (! m_fHandleCaretUpdates) {
      return;
    }
    
    final CaretEvent e = evt;
    SwingUtilities.invokeLater(new Runnable () {
      public void run() {
        // Get affected element
        Element eAffected = m_jtpConstraintEditor.
            getDocument().getDefaultRootElement();
        while (! eAffected.isLeaf()) {
          eAffected = eAffected.getElement (
              eAffected.getElementIndex (e.getDot()));
        }

        if (eAffected.getAttributes().getAttribute("isField") == Boolean.TRUE) {
          // enhance selection to end of field
          int nFieldStart = eAffected.getStartOffset();
          int nFieldEnd = eAffected.getEndOffset();

          int nMark = e.getMark();
          int nDot = e.getDot();
          
          int nSelStart = 0;
          int nSelEnd = 0;
          
          if (nMark == nDot) {
            // Cursor set
            if (m_nOldDot > nDot) {
              // coming in from the right
              nSelStart = nFieldEnd;
              nSelEnd = nFieldStart;
            }
            else {
              // coming in from the left
              nSelStart = nFieldStart;
              nSelEnd = nFieldEnd;
            }
          }
          else if (nMark < nDot) {
            // Selection spanning toward the right
            if (m_nOldDot > nDot) {
              // making selection smaller...
              nSelStart = Math.min (nMark, nFieldStart);
              nSelEnd = Math.min (nDot, nFieldStart);
            }
            else {
              if (nMark > nFieldStart) {
                nSelStart = nFieldStart;
              }
              else {
                nSelStart = nMark;
              }

              if (nDot <= nFieldEnd) {
                nSelEnd = nFieldEnd;
              }
              else {
                nSelEnd = nDot;
              }
            }
          }
          else {
            if (m_nOldDot < nDot) {
              // making selection smaller...
              nSelStart = Math.max (nMark, nFieldEnd);
              nSelEnd = Math.max (nDot, nFieldEnd);
            }
            else {
              if (nMark < nFieldEnd) {
                nSelStart = nFieldEnd;
              }
              else {
                nSelStart = nMark;
              }

              if (nDot >= nFieldStart) {
                nSelEnd = nFieldStart;
              }
              else {
                nSelEnd = nDot;
              }
            }
          }
          
          Caret c = m_jtpConstraintEditor.getCaret();
          m_fHandleCaretUpdates = false;
          c.setDot (nSelStart);
          c.moveDot (nSelEnd);
          m_fHandleCaretUpdates = true;
          
          m_nOldMark = nSelEnd;
          m_nOldDot = nSelEnd;
        }
        else {
          m_nOldDot = e.getDot();
          m_nOldMark = e.getMark();
        }
      }
    });
  }//GEN-LAST:event_onCaretUpdate
  
  private ArrayList getConstraints(Object element) {
	  ArrayList constraints = new ArrayList();
	  Facade argoFacade = Model.getFacade();
	  
	  // the constraints of the sub-namespaces
	  Collection ownedElements = argoFacade.getOwnedElements(element);
	  Iterator i = ownedElements.iterator();
	  while (i.hasNext()) {
		  Object e = i.next();
		  if (e instanceof org.omg.uml.foundation.core.Namespace) {
			  Collection c = this.getConstraints(e);
			  constraints.addAll(c);  
		  }
	  }
	  
	  // the constraints of the features
	  if (element instanceof org.omg.uml.foundation.core.Classifier) {
		  Collection features = argoFacade.getFeatures(element);
		  i = features.iterator();
		  while (i.hasNext()) {
			  Object e = i.next();
			  constraints.addAll(argoFacade.getConstraints(e));
		  }
	  }
	  
	  // the constraints of the element itself
	  constraints.addAll(argoFacade.getConstraints(element));
	  return constraints;
  }
  
  private String convertConstraints(ArrayList constraints) {
	  String constraintsText = new String();
	  for (Object o : constraints) {
			org.omg.uml.foundation.core.Constraint c = (org.omg.uml.foundation.core.Constraint) o;
			String text = c.getBody().getBody();
			constraintsText = constraintsText.concat(text);
			constraintsText = constraintsText.concat(" "); // to prevent endpackagepackage !
		}
	  //LOG.debug(constraintsText);
	  return constraintsText;
  }
  
  private void onXMIExportButton(java.awt.event.ActionEvent evt) {
		ModelManagementFactory mmf = Model.getModelManagementFactory();
		Object argoModel = mmf.getRootModel();

		// gather all constraints
		ArrayList constraints = this.getConstraints(argoModel);
		String constraintText = this.convertConstraints(constraints);

		LOG.debug("Exporting the following constraints:");
		LOG.debug(constraintText);
		
		try {
			javax.swing.JFileChooser fileChooser =  new javax.swing.JFileChooser();
            if (fileChooser.showSaveDialog(this) != JFileChooser.CANCEL_OPTION) {
                // write the xmi model from argoUML
    			String tmppath = fileChooser.getSelectedFile().getParent() + "/tmp.xmi";
    			Model.getXmiWriter(argoModel, new FileWriter(tmppath), "1.4").write();
    			
    			// load the xmi model into the dresden ocl2 toolkit
    			String modelUrl = "file:" + tmppath;              
                OclModel oclModel = new OclModel(MetaModelConst.UML15, modelUrl);
                            
                // generate concrete syntax tree
                // create lexer
                Lexer lexer = new Lexer (new PushbackReader(new StringReader(constraintText),1024));
                // parser program
                Parser parser = new Parser(lexer);
                tudresden.ocl20.core.parser.sablecc.node.Start cst = parser.parse(); // start symbol of cst
                
                LAttrAstGenerator astgen = new LAttrAstGenerator(oclModel);
    			//astgen.setMessageSink(this);
    			Heritage hrtg = new Heritage();
    			oclModel.beginTrans(true);
    			cst.apply(astgen, hrtg);
    			oclModel.endTrans(false);
    			
                // write the xmi
                //oclModel.save(fileChooser.getSelectedFile().getAbsolutePath());
    			//tudresden.ocl20.core.Repository r = RepositoryManager.getRepository();
    			//XmiWriter xmiWriter = r.createXMIWriter();
    			XMIOutputConfig config = new OutputConfig();
    			config.setEncoding("UTF-8");
    			XmiWriter xmiWriter = XMIWriterFactory.getDefault().createXMIWriter(config);
                xmiWriter.write(new FileOutputStream(fileChooser.getSelectedFile().getAbsolutePath()), oclModel.getModel(), "1.2");
                
                // delete the tmp file
                new File(tmppath).delete();
            }
		} catch (UmlException e) {
			String sWrappedMessage = wrapMessage(e.getMessage(), 100);
			JOptionPane.showMessageDialog(null,
					"XMI Export failed:\n" + sWrappedMessage,
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			String sWrappedMessage = wrapMessage(e.getMessage(), 100);
			JOptionPane.showMessageDialog(null,
					"XMI Export failed:\n" + sWrappedMessage,
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (OclModelException e) {
			String sWrappedMessage = wrapMessage(e.getMessage(), 100);
			JOptionPane.showMessageDialog(null,
					"XMI Export failed:\n" + sWrappedMessage,
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ParserException e) {
			String sWrappedMessage = wrapMessage(e.getMessage(), 100);
			JOptionPane.showMessageDialog(null,
					"XMI Export failed:\n" + sWrappedMessage,
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (LexerException e) {
			String sWrappedMessage = wrapMessage(e.getMessage(), 100);
			JOptionPane.showMessageDialog(null,
					"XMI Export failed:\n" + sWrappedMessage,
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (AttrEvalException e) {
			String sWrappedMessage = wrapMessage(e.getMessage(), 100);
			JOptionPane.showMessageDialog(null,
					"XMI Export failed:\n" + sWrappedMessage,
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} 
	}

  private void onQuickBarButton (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onQuickBarButton
    /*if (m_ocltQuickBar == null) {
      m_ocltQuickBar = new OCLToolbar (this);
    }

    if (m_jcbQuickBar.isSelected()) {
      m_ocltQuickBar.setVisible (true);
      m_ocltQuickBar.toFront();
    }
    else {
      m_ocltQuickBar.setVisible (false);
    }*/

    m_ocltbQuickBar.setVisible (m_jcbQuickBar.isSelected());
    revalidate();
    repaint();
  }//GEN-LAST:event_onQuickBarButton

  // Updated by Raphael Schmid.
  protected String wrapMessage(String sMessage, int maxLineLen) {
		StringBuffer sbResult = new StringBuffer(sMessage.length());
		int sMessagePointer = 0;
		int currentLinePointer = 0;

		while (sMessagePointer < sMessage.length()) {
			int lengthOfNextWord = 0;
			
			while ((sMessagePointer+lengthOfNextWord < sMessage.length()) && (sMessage.charAt (sMessagePointer+lengthOfNextWord) != ' ')) {
				lengthOfNextWord++;
			}
			
			if (currentLinePointer + lengthOfNextWord < maxLineLen) {
				sbResult.append(sMessage.substring(sMessagePointer,sMessagePointer + lengthOfNextWord));
				sbResult.append(' ');
				currentLinePointer += lengthOfNextWord;
			} else {
				sbResult.append('\n');
				sbResult.append(sMessage.substring(sMessagePointer,sMessagePointer + lengthOfNextWord));
				sbResult.append(' ');
				currentLinePointer = lengthOfNextWord;
			}
			sMessagePointer++;
			sMessagePointer += lengthOfNextWord;
		}
		return sbResult.toString();
	}
	
// protected String wrapMessage (String sMessage,
//                                int nMaxLineLen) {
//    StringBuffer sbResult = new StringBuffer (sMessage.length());
//    int i = 0;
//    
//    while (i < sMessage.length()) {
//      int nEndOfLine = i + nMaxLineLen;
//      
//      if (nEndOfLine > sMessage.length()) {
//        nEndOfLine = sMessage.length();
//      }
//      
//      while ((nEndOfLine > i + 1) &&
//             (sMessage.charAt (nEndOfLine - 1) != ' ')) {
//        nEndOfLine--;
//      }
//      
//      sbResult.append (sMessage.substring (i, nEndOfLine - 1))
//              .append ('\n');
//      
//      i = nEndOfLine;
//    }
//    
//    return sbResult.toString();
//  }
  
  
  /**
    * React to the submit button.
    */
  private void onSubmitConstraintButton(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_onSubmitConstraintButton
	  // Updated by Raphael Schmid
		if (m_oclemModel != null) {
			int nIdx = m_jtConstraintList.getSelectedRow();

			if (nIdx != -1) {
				try {
					m_oclemModel.getConstraintAt(nIdx).setData(
							m_jtpConstraintEditor.getText(), this);

					// Stop editing if successful
					m_fConstraintChanged = true; // somehow wasn't set
													// correctly before...
					setEditMode(false);
				} catch (ParserException ope) {
					// int nCaretPos = getCaretPositionFromLineAndColumn (
					// ope.getErrorLine(),
					// ope.getErrorCol()
					// );

					String sWrappedMessage = wrapMessage(ope.getMessage(), 100);

					JOptionPane.showMessageDialog(null, "Syntax error:\n"
							+ sWrappedMessage, "Error",
							JOptionPane.ERROR_MESSAGE);

					// m_jtpConstraintEditor.select (nCaretPos, nCaretPos);
					 m_jtpConstraintEditor.requestFocus();

					LOG.debug(ope);

				} catch (IllegalStateException ise) {
					String sWrappedMessage = wrapMessage(ise.getMessage(), 50);

					JOptionPane.showMessageDialog(null,
							"Couldn't set constraint:\n" + sWrappedMessage,
							"Error", JOptionPane.ERROR_MESSAGE);
					LOG.debug(ise);
				} catch (OclModelException e) {
					// TODO
				        LOG.debug(e);
				} catch (IOException e) {
					// TODO
				        LOG.debug(e);
				} catch (LexerException e) {
					// TODO
				        LOG.debug(e);
				} catch (AttrEvalException e) {
					String sWrappedMessage = wrapMessage(e.getMessage(), 100);
					JOptionPane.showMessageDialog(null,
							"Type checking failed:\n" + sWrappedMessage,
							"Error", JOptionPane.ERROR_MESSAGE);
					LOG.debug(e);
				}
			}
		}
  }

  /**
   * Compute caret position for the given line and column in the editor pane's
   * text.
   */
  protected int getCaretPositionFromLineAndColumn (int nLine, int nCol) {
    int nCurLine = 1; int nCurCol = 1;
    String sText = m_jtpConstraintEditor.getText();
    int nCaret = 0;
    while ((nCurLine != nLine) || (nCurCol != nCol)) {
      if (nCaret >= sText.length()) {
        return 0;
      }
      
      if (sText.charAt (nCaret) == '\n') {
        // new line
        nCurLine++;
        nCurCol = 1;
      }
      else {
        nCurCol++;
      }
      
      nCaret++;
    }
    
    return nCaret;
  }
  
  /**
    * React to the remove button.
    */
  private void onRemoveConstraintButton (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveConstraintButton
    if (m_oclemModel != null) {
      int nIdx = m_jtConstraintList.getSelectedRow();

      if (nIdx != -1) {
        m_oclemModel.removeConstraintAt (nIdx);
      }
    }
  }//GEN-LAST:event_onRemoveConstraintButton

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JToolBar m_jtbTools;
  private javax.swing.JButton m_jbNew;
  private javax.swing.JButton m_jbRemove;
  private javax.swing.JButton m_jbEdit;
  private javax.swing.JButton m_jbSaveEditResult;
  private javax.swing.JPanel pad1;
  private javax.swing.JButton m_jbPreferences;
  private javax.swing.JButton xmiExportButton;
  private javax.swing.JPanel pad2;
  private javax.swing.JCheckBox m_jcbQuickBar;
  private javax.swing.JPanel m_jpToolbarWrapper;
  private tudresden.ocl20.gui.OCLToolbar m_ocltbQuickBar;
  private javax.swing.JPanel m_jpEditorPanel;
  private javax.swing.JScrollPane m_jspConstraintEditorScroller;
  private javax.swing.JTextPane m_jtpConstraintEditor;
  private javax.swing.JSplitPane m_jspMainPane;
  private javax.swing.JPanel m_jpConstraintListPane;
  private javax.swing.JScrollPane m_jspConstraintListScroller;
  private javax.swing.JTable m_jtConstraintList;
  private javax.swing.JPanel m_jpPreviewPane;
  private javax.swing.JPanel m_jpPreviewGroup;
  private javax.swing.JScrollPane m_jspConstraintPreviewScroller;
  private javax.swing.JTextPane m_jtpConstraintPreview;
  // End of variables declaration//GEN-END:variables

  // ListSelectionListener interface method
  
  /**
    * The selection in the table changed.
    */
  public void valueChanged(final javax.swing.event.ListSelectionEvent p1) {
    // Selected row changed in table.
    int newIndex = m_jtConstraintList.getSelectedRow();
    
    if (newIndex != -1) {
      if (m_oclemModel != null) {
        m_crCurrent = m_oclemModel.getConstraintAt (newIndex);
      }
      else {
        m_crCurrent = null;
      }
    }
    else {
      m_crCurrent = null;
    }

    setEditorText (m_crCurrent);
    if (m_crCurrent != null) {
      // TODO: Deal with fields.
      m_jtpConstraintPreview.setText (m_crCurrent.getData());
    }
    else {
      m_jtpConstraintPreview.setText ("");
    }
  }

  // ConstraintChangeListener interface methods
  
  /**
    * Select the newly added constraint.
    */  
  public void constraintAdded(ConstraintChangeEvent cce) {
    m_jtConstraintList.setRowSelectionInterval (cce.getIndex(),
                                                   cce.getIndex());
  }
  
  /**
    * If the current constraint was deleted, clear the selection.
    */
  public void constraintRemoved(ConstraintChangeEvent cce) {
    if (cce.getIndex() == m_jtConstraintList.getSelectedRow()) {
      m_jtConstraintList.clearSelection();
    }
  }
  
  /**
    * Ignored.
    */
  public void constraintNameChanged(ConstraintChangeEvent cce) { }
  
  /**
    * Update the editor if the currently selected constraint changed.
    */
  public void constraintDataChanged(ConstraintChangeEvent cce) {
    // TODO: Deal with fields.
    if (cce.getIndex() == m_jtConstraintList.getSelectedRow()) {
      m_jtpConstraintEditor.setText (cce.getNew().getData());
      m_jtpConstraintPreview.setText (cce.getNew().getData());
      
      m_fConstraintChanged = true;
    }
  }
}
