// Copyright (c) 1996-99 The Regents of the University of California. All
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
// File: UMLTextField.java
// Classes: UMLTextField
// Original Author: not known
// $Id$
// 25 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support
// the FigUseCase.
// 3 May 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to mark the
// project as needing saving if a text field is changed.
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

import org.apache.log4j.Category;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ProjectBrowser;
import org.tigris.gef.presentation.Fig;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.foundation.core.MClassifier;
/**
 *  This class handles the updating of text as it is typed into the text field on one
 *  of the many property panels. By catching the MElementEvent dispatched from NSUML it
 *  updates the diagram as each character is typed. */
public class UMLTextField
    extends JTextField
    implements DocumentListener, UMLUserInterfaceComponent, FocusListener {

    protected final static Category cat =
        Category.getInstance("org.argouml.uml.ui.UMLTextField");

    private UMLUserInterfaceContainer _container;
    private UMLTextProperty _property;
    /**
     * value of property when focus is gained
     */
    protected String _oldPropertyValue;
    /**
     * true if text has changed since last focusgained
     */
    protected boolean _textChanged = false;
    /**
     * true if changed via userinput
     */
    protected boolean _viaUserInput = false;
    /**
     * true if it's the first exception in the handling of events, prevents exception loops 
     * and thus hanging argouml
     */
    protected boolean _firstException = true;

    private class TextSetter implements Runnable {
        String _text = null;
        JTextField _field = null;
        /**
         * @see java.lang.Runnable#run()
         */
        public void run() {
            _field.setText(_text);
        }
        
        public TextSetter(String textToSet, JTextField field) {
        	_text = textToSet;
        	_field = field;
        }
    }

    Object _target;
    MClassifier _classifier;
    /** Creates new BooleanChangeListener */
    public UMLTextField(
        UMLUserInterfaceContainer container,
        UMLTextProperty property) {
        _container = container;
        _property = property;
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
        _property.targetChanged();
        // update();
        
        _target = _container.getTarget();
        String oldText = getText();
        String newText = _property.getProperty(_container);
        TextSetter textSetter = new TextSetter(newText, this);
        SwingUtilities.invokeLater(textSetter);
        
    }
    public void targetReasserted() {
    }
    public void roleAdded(final MElementEvent p1) {
        //        Argo.log.info("UMLTextField.roleAdded: event p1 happened...");
    }
    public void recovered(final MElementEvent p1) {
        //        Argo.log.info("UMLTextField.recovered: event p1 happened...");
    }
    public void roleRemoved(final MElementEvent p1) {
        //        Argo.log.info("UMLTextField.roleRemoved: event p1 happened...");        
    }
    public void listRoleItemSet(final MElementEvent p1) {
        //        Argo.log.info("UMLTextField.listRoleItemSet: event p1 happened...");        
    }
    public void removed(final MElementEvent p1) {
    }
    public void propertySet(final MElementEvent event) {
        //
        //   check the possibility that this is a promiscuous event
        Object eventSource = event.getSource();
        _target = _container.getTarget();
        if (_target == null) {
            return;
        }
        if (_property.isAffected(event)) {      
            //
            //    if event source is unknown or 
            //       the event source is the container's target
            //          then update the field
            if ((eventSource == null || eventSource == _target) && 
            	((event.getOldValue() == null && event.getNewValue() != null) ||
            	(event.getNewValue() == null && event.getOldValue() != null) ||
            	(event.getOldValue()!= null && !event.getOldValue().equals(event.getNewValue())))) {
                update();
                // TextSetter textSetter = new TextSetter((String)event.getNewValue(), this);
	            // SwingUtilities.invokeLater(textSetter);
                
            }
        }
        //        else if(_target instanceof MDataType)
        //            Argo.log.info("UMLTextField.propertySet: else :Target = " + _target);
    }
    
    /**
     * <p>Updates both the Collection (by setText()) and the drawing (using 
     *   the if statements and code blocks).</p>
     *
     * <p>The code forces {@link FigClass} and {@link FigUseCase} to update the
     *   drawing as information is typed into the text boxes in the property
     *   panes. This is done by getting component parts (features or extension
     *   points) from the NSUML object and setting them back again to force a
     *   redraw. The setting back costs VERY much.</p>
     *
     * @author modified by psager@tigris.org Aug. 27, 2001
     *
     * @author 16 Apr, 2002. Jeremy Bennett (mail@jeremybennett.com). Modified
     *         to support {@link FigUseCase}.
     */
    private void update() {
        String oldText = getText();
        String newText = _property.getProperty(_container);
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
        _target = _container.getTarget();
        if (_target == null) {
            return;
        }  
       
    
        Project p = ProjectManager.getManager().getCurrentProject();
        // start new code
        Iterator it = p.findFigsForMember(_target).iterator();
        while (it.hasNext()) {
            Fig o = (Fig)it.next();
           	
            
            Vector figs = o.getEnclosedFigs();
            if (figs != null) {
            for (int i = 0; i < figs.size(); i++) {
            	((Fig)figs.get(i)).damage();
            }
            }
            
             o.damage();
            
            
            
        }
        // end new code
        // commented out rest of update
        /*    
        // Commented out for now, because this triggers from all over the
        // place.
        //p.setNeedsSave(true);
        // If we are a use case update all our extension points.
        if (_target instanceof MUseCase) {
            MUseCase useCase = (MUseCase) _target;
            useCase.setExtensionPoints(useCase.getExtensionPoints());
        }
        // If we are an extension point update the extension points of our
        // owning use case. This could be null of course.
        else
            if (_target instanceof MExtensionPoint) {
                MUseCase useCase = ((MExtensionPoint) _target).getUseCase();
                if (useCase != null) {
                    useCase.setExtensionPoints(useCase.getExtensionPoints());
                }
            }
        // If we are any other (non-use case) sort of classifier update all our
        // features.
        else
            if (_target instanceof MClassifier) {
                _classifier = (MClassifier) _target;
                if (_classifier == null) {
                    return;
                }
                _classifier.setFeatures(_classifier.getFeatures());
            }
            else
                if (_target instanceof MOperation) {
                    _classifier =
                        (MClassifier) ((MOperation) _target).getOwner();
                    if (_classifier == null) {
                        return;
                    }
                    _classifier.setFeatures(_classifier.getFeatures());
                }
                else
                    if (_target instanceof MAttribute) {
                        _classifier =
                            (MClassifier) ((MAttribute) _target).getOwner();
                        //            Argo.log.info("UMLTextField.update()..._classifier = " + _classifier);
                        if (_classifier == null) {
                            return;
                        }
                        _classifier.setFeatures(_classifier.getFeatures());
                    }
                    else
                        if (_target instanceof MParameter) {
                            MBehavioralFeature feature =
                                ((MParameter) _target).getBehavioralFeature();
                            //
                            // check if we are dealing with a valid parameter...
                            // 
                            if (feature == null) {
                                return;
                            }
                            _classifier = (MClassifier) feature.getOwner();
                            if (_classifier == null) {
                                return;
                            }
                            _classifier.setFeatures(_classifier.getFeatures());
                        }
                        else
                            if (_target instanceof MCallEvent) {
                                //            Argo.log.info("UMLTextField.update()...target = " + _target);
                            }
          */
            
    }
    
    public void changedUpdate(final DocumentEvent p1) {
        // never happens since UMLTextFields don't support non-plain documents
        // handleEvent();
        //        Argo.log.info("UMLTextField.changedUpdate: DocumentEvent p1 " );       
    }
    
    /**
     * Event handler for the removal of the content of the Document this textfield is managing.
     * @see javax.swing.event.DocumentListener#removeUpdate(DocumentEvent)
     */
    public void removeUpdate(final DocumentEvent p1) {
        if (_viaUserInput) {	
            	_textChanged =
                	p1.getLength() > 0;
        }
        handleEvent();
    }
    
    public void insertUpdate(final DocumentEvent p1) {
        // this is hit after focusgained
        // we must check wether the text is really changed
        if (_viaUserInput) {
            _textChanged =
                // (_oldPropertyValue != null) &&
                    p1.getLength() > 0;
        }
        handleEvent();
    }
    
    protected void handleEvent() {
        try {
            if (_viaUserInput) {
            	if (_textChanged) {
                	// next line dirty hack to enable the continuous updating 
                	// of the textfields in the figs and in the navigator
                	_textChanged = false;
                	// _property.setProperty(_container, _oldPropertyValue);
                	_property.setProperty(_container, getText(), true);
            	}
            }
            else {
            	String proptext = _property.getProperty(_container);
            	String fieldtext = getText();
            	if (proptext != null && !proptext.equals(fieldtext)) {
                	_property.setProperty(_container, proptext, false);
            	}	
            }
        }
        catch (PropertyVetoException pv) {
            showException(pv);
            TextSetter textSetter = new TextSetter(_oldPropertyValue, this);
            SwingUtilities.invokeLater(textSetter);
        }
        catch (Exception ex) { 
            showException(ex);
            if (_firstException) {
                try {
                    _property.setProperty(_container, _oldPropertyValue);
                    TextSetter textSetter = new TextSetter(_oldPropertyValue, this);
            		SwingUtilities.invokeLater(textSetter);
                }
                catch (Exception e) {
                    cat.fatal("Repeating exception");
                    cat.fatal(e);
                    System.exit(-1);
                }
                _firstException = false;
            }
            else {
                cat.fatal("Repeating exception");
                cat.fatal(ex);
                System.exit(-1);
            }
        }
    }
    
    /**
     * @see java.awt.event.FocusListener#focusGained(FocusEvent)
     */
    public void focusGained(FocusEvent arg0) {
        _oldPropertyValue = _property.getProperty(_container);
        _textChanged = false;
        _viaUserInput = true;
    }
    /**
     * @see java.awt.event.FocusListener#focusLost(FocusEvent)
     */
    public void focusLost(FocusEvent arg0) {
        handleEvent();
        _viaUserInput = false;
        _textChanged = false;
    }
    
    protected void showException(Exception ex) {
        String message = ex.getMessage();
        // cant show the messagebox in this container
        JOptionPane.showMessageDialog(
            ProjectBrowser.getInstance(),
            message,
            "error",
            JOptionPane.ERROR_MESSAGE);
    }
} //...end of class UMLTextField...
