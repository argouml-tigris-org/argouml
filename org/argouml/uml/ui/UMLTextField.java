// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS. 

package org.argouml.uml.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyVetoException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ProjectBrowser;
import org.tigris.gef.presentation.Fig;

import ru.novosoft.uml.MElementEvent;

/**
 * This class handles the updating of text as it is typed into the
 * text field on one of the many property panels. By catching the
 * MElementEvent dispatched from NSUML it updates the diagram as each
 * character is typed.
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 * replaced by {@link org.argouml.uml.ui.UMLTextField2},
 * this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 * that used reflection a lot.
 */
public class UMLTextField
    extends JTextField
    implements DocumentListener, UMLUserInterfaceComponent, FocusListener {

    /** logger */
    private static final Logger LOG = Logger.getLogger(UMLTextField.class);

    private UMLUserInterfaceContainer theContainer;
    private UMLTextProperty theProperty;

    /**
     * value of property when focus is gained
     */
    private String oldPropertyValue;

    /**
     * true if text has changed since last focusgained
     */
    private boolean textChanged = false;

    /**
     * true if changed via userinput
     */
    private boolean viaUserInput = false;

    /**
     * true if it's the first exception in the handling of events,
     * prevents exception loops and thus hanging argouml
     */
    private boolean firstException = true;

    private class TextSetter implements Runnable {
        private String text = null;
        private JTextField field = null;
        /**
         * @see java.lang.Runnable#run()
         */
        public void run() {
            field.setText(text);
        }
        
        public TextSetter(String textToSet, JTextField f) {
	    text = textToSet;
	    field = f;
        }
    }

    private Object target;
    private Object/*MClassifier*/ classifier;

    /**
     * Creates new BooleanChangeListener.<p>
     * 
     * @param container the container of UML user interface components
     * @param property the property
     */
    public UMLTextField(UMLUserInterfaceContainer container,
			UMLTextProperty property) {
        theContainer = container;
        theProperty = property;
        getDocument().addDocumentListener(this);
        // addActionListener(this);
        addFocusListener(this);
        // update();
    }
    
	/**
	 * Called when an UMLTextField is left.
	 * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetChanged()
	 */
    public void targetChanged() {
        theProperty.targetChanged();
        // update();
        
        target = theContainer.getTarget();
        String newText = theProperty.getProperty(theContainer);
        TextSetter textSetter = new TextSetter(newText, this);
        SwingUtilities.invokeLater(textSetter);
        
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetReasserted()
     */
    public void targetReasserted() {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(final MElementEvent p1) {
        //        LOG.info("UMLTextField.roleAdded: event p1 happened...");
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(final MElementEvent p1) {
        //        LOG.info("UMLTextField.recovered: event p1 happened...");
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(final MElementEvent p1) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(final MElementEvent p1) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(final MElementEvent p1) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(final MElementEvent event) {
        //
        //   check the possibility that this is a promiscuous event
        Object eventSource = event.getSource();
        target = theContainer.getTarget();
        if (target == null) {
            return;
        }
        if (theProperty.isAffected(event)) {      
            //
            //    if event source is unknown or 
            //       the event source is the container's target
            //          then update the field
            if ((eventSource == null || eventSource == target)
		&& ((event.getOldValue() == null
		     && event.getNewValue() != null)
		    || (event.getNewValue() == null
			&& event.getOldValue() != null)
		    || (event.getOldValue() != null
			&& !event.getOldValue().equals(event.getNewValue())))) {
                update();
            }
        }
    }
    
    /**
     * Updates both the Collection (by setText()) and the drawing (using 
     * the if statements and code blocks).<p>
     *
     * The code forces {@link
     * org.argouml.uml.diagram.static_structure.ui.FigClass} and
     * {@link org.argouml.uml.diagram.use_case.ui.FigUseCase} to
     * update the drawing as information is typed into the text boxes
     * in the property panes. This is done by getting component parts
     * (features or extension points) from the NSUML object and
     * setting them back again to force a redraw. The setting back
     * costs VERY much.<p>
     *
     * @author modified by psager@tigris.org Aug. 27, 2001
     *
     * @author 16 Apr, 2002. Jeremy Bennett (mail@jeremybennett.com).
     *         Modified to support
     *         {@link org.argouml.uml.diagram.use_case.ui.FigUseCase}.
     */
    private void update() {
        String oldText = getText();
        String newText = theProperty.getProperty(theContainer);
        // Update the text if we have changed from or to nothing, or if the old
        // and new text are different.
        
        if ((oldText == null)
            || (newText == null)
            || (!(oldText.equals(newText)))) {
            
            TextSetter textSetter = new TextSetter(newText, this);
            SwingUtilities.invokeLater(textSetter);
           
        }
        
        // Now look at the associated NSUML element and see if we need to do
        // anything special. Discard if we are null. As a start we need to mark
        // this for saving.
        // 2002-08-02
        // Jaap Branderhorst
        // the only reason to do this next bit is to update the diagrams. 
        // (according to the javadoc and what i see). This can be done in a 
        // better way, by searching the appropriate figs and update them
        target = theContainer.getTarget();
        if (target == null) {
            return;
        }  
       
    
        Project p = ProjectManager.getManager().getCurrentProject();

        Iterator it = p.findFigsForMember(target).iterator();
        while (it.hasNext()) {
            Fig o = (Fig) it.next();
           	
            
            Vector figs = o.getEnclosedFigs();
            if (figs != null) {
		for (int i = 0; i < figs.size(); i++) {
		    ((Fig) figs.get(i)).damage();
		}
            }
            
	    o.damage();
            
            
            
        }
    }
    
    /**
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
     */
    public void changedUpdate(final DocumentEvent p1) {
        // never happens since UMLTextFields don't support non-plain documents
        // handleEvent();
    }
    
    /**
     * Event handler for the removal of the content of the Document
     * this textfield is managing.<p>
     *
     * @see javax.swing.event.DocumentListener#removeUpdate(DocumentEvent)
     */
    public void removeUpdate(final DocumentEvent p1) {
        if (viaUserInput) {	
	    textChanged = p1.getLength() > 0;
        }
        handleEvent();
    }
    
    /**
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    public void insertUpdate(final DocumentEvent p1) {
        // this is hit after focusgained
        // we must check wether the text is really changed
        if (viaUserInput) {
            textChanged =
                // (_oldPropertyValue != null) &&
                    p1.getLength() > 0;
        }
        handleEvent();
    }
    
    /**
     * Handle the event.
     */
    protected void handleEvent() {
        try {
            if (viaUserInput) {
            	if (textChanged) {
		    // next line dirty hack to enable the continuous updating 
		    // of the textfields in the figs and in the navigator
		    textChanged = false;
		    // _property.setProperty(_container, _oldPropertyValue);
		    theProperty.setProperty(theContainer, getText(), true);
            	}
            }
            else {
            	String proptext = theProperty.getProperty(theContainer);
            	String fieldtext = getText();
            	if (proptext != null && !proptext.equals(fieldtext)) {
		    theProperty.setProperty(theContainer, proptext, false);
            	}	
            }
        }
        catch (PropertyVetoException pv) {
            showException(pv);
            TextSetter textSetter = new TextSetter(oldPropertyValue, this);
            SwingUtilities.invokeLater(textSetter);
        }
        catch (Exception ex) { 
            showException(ex);
            if (firstException) {
                try {
                    theProperty.setProperty(theContainer, oldPropertyValue);
                    TextSetter textSetter =
			new TextSetter(oldPropertyValue, this);
		    SwingUtilities.invokeLater(textSetter);
                }
                catch (Exception e) {
                    LOG.fatal("Repeating exception");
                    LOG.fatal(e);
                    System.exit(-1);
                }
                firstException = false;
            }
            else {
                LOG.fatal("Repeating exception");
                LOG.fatal(ex);
                System.exit(-1);
            }
        }
    }
    
    /**
     * @see java.awt.event.FocusListener#focusGained(FocusEvent)
     */
    public void focusGained(FocusEvent arg0) {
        oldPropertyValue = theProperty.getProperty(theContainer);
        textChanged = false;
        viaUserInput = true;
    }
    /**
     * @see java.awt.event.FocusListener#focusLost(FocusEvent)
     */
    public void focusLost(FocusEvent arg0) {
        handleEvent();
        viaUserInput = false;
        textChanged = false;
    }
    
    /**
     * @param ex the exception to be shown
     */
    protected void showException(Exception ex) {
        String message = ex.getMessage();
        // cant show the messagebox in this container
        JOptionPane.showMessageDialog(ProjectBrowser.getInstance(),
				      message,
				      "error",
				      JOptionPane.ERROR_MESSAGE);
    }
} //...end of class UMLTextField...
