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

import java.text.*;

import javax.swing.event.*;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyVetoException;
import java.lang.reflect.*;

import org.argouml.application.api.*;

import org.argouml.kernel.*;

import org.argouml.ui.*;

import ru.novosoft.uml.*;

import ru.novosoft.uml.foundation.core.*; //--pjs-- added for event.

import ru.novosoft.uml.behavior.use_cases.*;

import ru.novosoft.uml.behavior.state_machines.*;

/**
 *  This class handles the updating of text as it is typed into the text field on one
 *  of the many property panels. By catching the MElementEvent dispatched from NSUML it
 *  updates the diagram as each character is typed. */

public class UMLTextField
    extends JTextField
    implements DocumentListener, UMLUserInterfaceComponent, ActionListener, FocusListener {

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
    
    protected boolean _firstException = true;

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

        update(); 

    }

    public void targetChanged() {

        _property.targetChanged();

        update();

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

            if (eventSource == null || eventSource == _target) {

                update();

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
     *   redraw.</p>
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
            try {
            	setText(newText);
            }
            catch (IllegalStateException il) {}

        }

        // Now look at the associated NSUML element and see if we need to do

        // anything special. Discard if we are null. As a start we need to mark

        // this for saving.

        _target = _container.getTarget();

        if (_target == null) {

            return;

        }

        // Commented out for now, because this triggers from all over the

        // place.

        Project p = ProjectBrowser.TheInstance.getProject();

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

        //        else

        //            Argo.log.info("UMLTextField.update()else...target = " + _target);

    }

    public void changedUpdate(final DocumentEvent p1) {
		// never happens
        // handleEvent();

        //        Argo.log.info("UMLTextField.changedUpdate: DocumentEvent p1 " );       

    }

    public void removeUpdate(final DocumentEvent p1) {
    	_textChanged = true;

        handleEvent();

    }

    public void insertUpdate(final DocumentEvent p1) {
    	// this is hit after focusgained
    	// we must check wether the text is really changed
    	_textChanged = (_oldPropertyValue != null) && !getText().equals(_oldPropertyValue);

        handleEvent();

    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        handleEvent();
    }

    protected void handleEvent() {
        try {
        	if ((_oldPropertyValue == null || _oldPropertyValue.length() == 0) ||  
        	  (_oldPropertyValue != null && !(_oldPropertyValue.equals(getText())))) {
            	_property.setProperty(_container, getText());
        	}     			
        }
        catch (Exception ex) {
           showException(ex);
            if (_firstException) {
            	try {
            		_property.setProperty(_container, _oldPropertyValue);
            	}
            	catch (Exception ex2) {
            		Argo.log.fatal("FATAL: Repeating exception");
            		Argo.log.fatal(ex2);
            		System.exit(-1);
            	}
            	_firstException = false;
            }
            else {
            	Argo.log.fatal("FATAL: Repeating exception");
            	Argo.log.fatal(ex);
            }
        }
        update();
    }

    /**
     * @see java.awt.event.FocusListener#focusGained(FocusEvent)
     */
    public void focusGained(FocusEvent arg0) {
    	_oldPropertyValue = _property.getProperty(_container);
    }

    /**
     * @see java.awt.event.FocusListener#focusLost(FocusEvent)
     */
    public void focusLost(FocusEvent arg0) {
    	if (_textChanged) {
    		try {
    	 		// check if the new property is legal
        		if ((_oldPropertyValue == null || _oldPropertyValue.length() == 0) ||  
        	  		(_oldPropertyValue != null && !(_oldPropertyValue.equals(getText())))) {
        	  		// next line dirty hack to enable the continuous updating 
        	  		// of the textfields in the figs and in the navigator
        	  		_property.setProperty(_container, _oldPropertyValue);
            		_property.setProperty(_container, getText(), true);
            		_oldPropertyValue = getText();
        		}      			
        	}
        	catch (PropertyVetoException pv) {
        		try {
        			setText(_oldPropertyValue);     			
        		}
        		catch (IllegalStateException i) {} // will allways throw but set the text correctly
        		try {
        			_property.setProperty(_container, _oldPropertyValue);
        		}
        		catch (Exception ex) {
        			Argo.log.fatal("FATAL: Repeating exception");
            		Argo.log.fatal(ex);
            		System.exit(-1);
        		}
        		showException(pv);
        	}
        	catch (Exception ex) { // a setProperty method throws a PropertyVetoException if it is an illegal change
            	showException(ex);
            	if (_firstException) {
            		try {
                    	_property.setProperty(_container, _oldPropertyValue);
                    	setText(_oldPropertyValue);
                	}
                	catch (Exception e) {
                		Argo.log.fatal("FATAL: Repeating exception");
            			Argo.log.fatal(e);
            			System.exit(-1);
                	}
            		_firstException = false;
            	} else {
            		Argo.log.fatal("FATAL: Repeating exception");
            		Argo.log.fatal(ex);
            	}
        	}
        	finally {
        		_textChanged = false;
        	}
    	}
        // update();
        // _oldPropertyValue = null;
    }
    
    protected void showException(Exception ex) {
    	String message = ex.getMessage();
        // cant show the messagebox in this container
        JOptionPane.showMessageDialog(
            ProjectBrowser.TheInstance,
            message,
            "error",
            JOptionPane.ERROR_MESSAGE);
    }    

} //...end of class UMLTextField...
