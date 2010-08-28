/* $Id$
 *******************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */

package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.ui.UndoableAction;
import org.argouml.uml.ui.UMLComboBox2;

class UMLMethodSpecificationComboBoxModel
extends UMLComboBoxModel {
    /**
     * Constructor.
     */
    public UMLMethodSpecificationComboBoxModel(
            final String propertyName,
            final Object target) {
        super(target, "specification", false);
        Model.getPump().addClassModelEventListener(this,
                Model.getMetaTypes().getOperation(), "method");
    }
    
    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(
     *         java.lang.Object)
     */
    protected boolean isValidElement(Object element) {
        Object specification =
            Model.getCoreHelper().getSpecification(getTarget());
        return specification == element;
    }
    
    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        if (getTarget() != null) {
            removeAllElements();
            Object classifier = Model.getFacade().getOwner(getTarget());
            addAll(Model.getFacade().getOperations(classifier));
        }
    }
    
    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        return Model.getCoreHelper().getSpecification(getTarget());
    }
    
    /*
     * @see java.beans.PropertyChangeListener#propertyChange(
     *         java.beans.PropertyChangeEvent)
     */
    public void modelChanged(UmlChangeEvent evt) {
        if (evt instanceof AttributeChangeEvent) {
            if (evt.getPropertyName().equals("specification")) {
                if (evt.getSource() == getTarget()
                        && (getChangedElement(evt) != null)) {
                    Object elem = getChangedElement(evt);
                    setSelectedItem(elem);
                }
            }
        }
    }
    
    public Action getAction() {
        return new ActionSetMethodSpecification();
    }

    private static class ActionSetMethodSpecification extends UndoableAction {

        /**
         * Constructor for ActionSetStructuralFeatureType.
         */
        protected ActionSetMethodSpecification() {
            super(Translator.localize("Set"), null);
            // Set the tooltip string:
            putValue(Action.SHORT_DESCRIPTION, 
                    Translator.localize("Set"));
        }
        
        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            Object source = e.getSource();
            Object oldOperation = null;
            Object newOperation = null;
            Object method = null;
            if (source instanceof UMLComboBox2) {
                UMLComboBox2 box = (UMLComboBox2) source;
                Object o = box.getTarget(); // the method
                if (Model.getFacade().isAMethod(o)) {
                    method = o;
                    oldOperation =
                        Model.getCoreHelper().getSpecification(method);
                }
                o = box.getSelectedItem(); // the selected operation
                if (Model.getFacade().isAOperation(o)) {
                    newOperation = o;
                }
            }
            if (newOperation != oldOperation && method != null) {
                Model.getCoreHelper().setSpecification(method, newOperation);
            }
        }
    }
}
