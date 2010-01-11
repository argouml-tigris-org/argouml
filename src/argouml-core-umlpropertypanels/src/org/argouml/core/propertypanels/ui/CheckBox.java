/* $Id$
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling - Original implementation
 *******************************************************************************
 */

package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JCheckBox;

import org.argouml.core.propertypanels.model.GetterSetterManager;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.UndoableAction;

/**
 * A check box representing a boolean property in the UML model
 * @author Bob Tarling
 * @since 0.29.2 19th Dec 2009
 */
class CheckBox extends JCheckBox 
    implements PropertyChangeListener {

    /**
     * The class uid
     */
    private static final long serialVersionUID = 2654856740168885592L;

    private final Object modelElement;
    
    private final String propertyName;
    
    /**
     * The action that will be called when the checkbox changes
     */
    private final Action action;

    private final GetterSetterManager getterSetter;
    
    /**
     * Constructor for UMLCheckBox.
     * @param text the text of the check box
     * @param modelElement the model element the check box represents, updates and
     * is listening for changes to
     * @param propertyName the property of the target that the checkbox is listening
     * @param getterSetter the facade used to get and set properties on the model
     * for and updating
     */
    public CheckBox(
            final String text,
            final Object modelElement,
            final String propertyName, 
            final GetterSetterManager getterSetter) {
        super(text);
        
        this.getterSetter = getterSetter;
        this.propertyName = propertyName;
        this.modelElement = modelElement;
        
        setFont(LookAndFeelMgr.getInstance().getStandardFont());
        
        build();
        
        action = new SetAction(getterSetter, modelElement, propertyName);
        setActionCommand((String) action.getValue(Action.ACTION_COMMAND_KEY));
        addActionListener(action);
        Model.getPump().addModelEventListener(
                this, modelElement, propertyName);
    }
    
    private String propertyToLabel(String propertyName) {
        if (propertyName.startsWith("is")) {
            return "checkbox." + propertyName.substring(2).toLowerCase();
        } else {
            return "checkbox." + propertyName.toLowerCase();
        }
    }
    
    /**
     * Remove all listeners when the component is removed from its parent
     */
    public void removeNotify() {
        super.removeNotify();
        removeActionListener(action);
        Model.getPump().removeModelEventListener(
                this, modelElement, propertyName);
    }
    
    /*
     * The property value has changed so rebuild our view.
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        build();
    }

    /*
     * @see org.argouml.uml.ui.UMLCheckBox#buildModel()
     */
    private void build() {
        if (getterSetter != null) {
            setSelected((Boolean) getterSetter.get(modelElement, propertyName, null));
        }
    }
    
    private static class SetAction extends UndoableAction {

        /**
         * The class uid
         */
        private static final long serialVersionUID = -2708077474004286682L;
        
        private final GetterSetterManager getterSetter;
        private final String propertyName;
        private Object modelElement;
        
        /**
         * Constructor for ActionSetElementOwnershipSpecification.
         */
        protected SetAction(
                final GetterSetterManager getterSetter, 
                final Object modelElement,
                final String propertyName) {
            super(Translator.localize("Set"), null);
            this.modelElement = modelElement;
            this.getterSetter = getterSetter;
            this.propertyName = propertyName;
            // Set the tooltip string:
            putValue(Action.SHORT_DESCRIPTION, 
                    Translator.localize("Set"));
        }
 
        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            CheckBox source = (CheckBox) e.getSource();
            getterSetter.set(modelElement, source.isSelected(), propertyName);
        }
    }
}
