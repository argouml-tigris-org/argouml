// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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
import org.argouml.model.uml.UmlFactory;
import org.argouml.uml.*;
import org.argouml.uml.generator.*;
import org.argouml.application.api.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import org.argouml.model.ModelFacade;

import ru.novosoft.uml.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.foundation.data_types.MExpression;

/** handles communication between the initial value JComboBox and the Collection.
 *  This class also causes NSUML to refresh and so keeps the diagram in synch
 *  with the model.
 *
 *  Method PropertySet listens for MElementEvent events and updates the other
 *  elements, such as type, visibility and changeability.
 *      Modified psager@tigris.org
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             to be replaced by something similar to {@link org.argouml.uml.ui.foundation.core.UMLAttributeInitialValueListModel},
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLInitialValueComboBox extends JComboBox 
             implements ActionListener, UMLUserInterfaceComponent {

    private UMLUserInterfaceContainer _container;

    /** Prevent event storms through not generating unnecessary events */
    private boolean _isUpdating = false;
    
/** items in the initial value combobox that are available for selection.*/    
    String[] listItems = {"", "0", "1", "2", "null"};

    /** Creates new BooleanChangeListener */
    public UMLInitialValueComboBox(UMLUserInterfaceContainer container) {
        super();
        _container = container;
        
        for (int i = 0; i < listItems.length; i++) {
            addItem(listItems[i]);
        }
	setEditable(true);

/** handles ActionEvents from the combobox. The action listener was not handling
 *  events from the combo box properly, so I moved it up to the constructor, 
 *  where it works fine. I guess the best alternative would be to create an
 *  inner class for the listener. The listener sets the value from the combo
 *  box into the collection, and then calls update() to refresh the drawing so
 *  that it stays in synch with the model.
 * 
 *      Modified psager@tigris.org Aug.6, 2001 to handle method parameter
 *      expressions.
 */
        addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
		if (_isUpdating) {
		    return;
		}

                String item = (String) getSelectedItem();
                Object target = _container.getTarget();
                if (org.argouml.model.ModelFacade.isAAttribute(target)) {
                    Object/*MExpression*/ itemExpr = UmlFactory.getFactory().getDataTypes().createExpression("Java", item);
                    ModelFacade.setInitialValue(target, itemExpr);
                    update();
                }
                else if (org.argouml.model.ModelFacade.isAParameter(target)) {
                    Object/*MExpression*/ itemExpr = UmlFactory.getFactory().getDataTypes().createExpression("Java", item);
                    ModelFacade.setDefaultValue(target, itemExpr);
                    update();
                }
            }
        }); //...end of action listener...
        
    }   //...end of constructor...
    
/** 
 *  On change of target (when we display the Parameter or Attribute property panel)
 *  we set the initial value of the attribute into the UMLInitialValueComboBox.
 *
 *  If the attribute or the parameter has no value, then clear the
 *  initialValue combobox of residual junk..this is also done to keep from setting
 *  residual values into the return parameter.
 *  @author psager@tigris.org Aug. 31, 2001. Modified Sept. 05, 2001
 */ 
    public void targetChanged() {
        Object target = _container.getTarget();
	_isUpdating = true;
        if (org.argouml.model.ModelFacade.isAAttribute(target)) {
            Object/*MExpression*/ initExpr = (MExpression) ModelFacade.getInitialValue(target);
            if (initExpr != null) {
                Object init = ModelFacade.getBody(initExpr);
                setSelectedItem(init);
                //update();
            }
            else if (initExpr == null) {
                setSelectedItem(null); // clear residual junk from the combo box.
            }
        } 
        else if (org.argouml.model.ModelFacade.isAParameter(target)) {
            Object/*MExpression*/ initExpr = (MExpression) ModelFacade.getDefaultValue(target);
            if (initExpr != null) {
                Object init = ModelFacade.getBody(initExpr);
                setSelectedItem(init);
                //update();
            }
            else if (initExpr == null) {
                setSelectedItem(null); // clear the previous value from the combo box.
            }
        }
	_isUpdating = false;
    }
    

    public void targetReasserted() {
    }

    public void roleAdded(final MElementEvent p1) {
    }
    public void recovered(final MElementEvent p1) {
    }
    public void roleRemoved(final MElementEvent p1) {
    }
    public void listRoleItemSet(final MElementEvent p1) {
    }
    public void removed(final MElementEvent p1) {
    }

/**  event handler for MElement events generated by the attribute/parameter 
 *   property panels and the diagram. 
 *
 *   The event name is used to identify whether the event should be handled here
 *   or not. eventProp "owner" and "name" are handled elsewhere while 
 *   eventProp "type" and "initValue" are handled here.
  *          modified Aug. 29, 2001 psager@tigris.org
 *          modified Dec. 13, 2001 psager@tigris.org cleaned up the code.
 *          modified Aug. 21, 2002 d00mst@tigris.org catch changes to the initial value
 *   @param  MElementEvent event the event object that identifies the event
 */
    public void propertySet(final MElementEvent event) {
        String eventProp = event.getName();
//        Argo.log.info("Event Property = " + eventProp);
        if (eventProp.equals("owner") || eventProp.equals("name")) {
            return;
        }
	if ("initialValue".equals(eventProp) 
	    || "defaultValue".equals(eventProp))
	    targetChanged();
//	else
//	    update();
    } 
    
/** updates the diagram. It first has to locate it's target element and then
 *  causes the update to take place so that the diagram stays in synch with
 *  the model. 
 *  @author psager@tigris.org   Aug. 30, 2001
 */    
    private void update() {
        Object target = _container.getTarget();
        if (ModelFacade.isAAttribute(target)) {
            Object classifier = ModelFacade.getOwner(target);
            if (classifier == null) {
                return;
            }
            ModelFacade.setFeatures(classifier, ModelFacade.getFeatures(classifier));
        }
        else if (ModelFacade.isAParameter(target)) {
            if (ModelFacade.isACallEvent(target)) {
                return;
            }
            Object feature = ModelFacade.getBehavioralFeature(target);
            if (feature != null) {
                Object classifier = ModelFacade.getOwner(feature);
                if (classifier == null) {
                    return;
                }
                ModelFacade.setFeatures(classifier, ModelFacade.getFeatures(classifier));
            }
        }
    }   // ...end of update() method...
    

} //...end of class...