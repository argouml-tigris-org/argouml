// $Id: Checkbox.java 15920 2008-10-14 18:03:33Z bobtarling $

package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JCheckBox;

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

    private final GetterSetter getterSetter;
    
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
            final GetterSetter getterSetter) {
        super(text);
        
        this.getterSetter = getterSetter;
        this.propertyName = propertyName;
        this.modelElement = modelElement;
        
        setFont(LookAndFeelMgr.getInstance().getStandardFont());
        
        build();
        
        action = new SetAction(getterSetter, modelElement, propertyName);
        setActionCommand((String) action.getValue(Action.ACTION_COMMAND_KEY));
    }
    
    private String propertyToLabel(String propertyName) {
        if (propertyName.startsWith("is")) {
            return "checkbox." + propertyName.substring(2).toLowerCase();
        } else {
            return "checkbox." + propertyName.toLowerCase();
        }
    }
    
    /**
     * Add listeners when the component is placed on its parent
     */
    public void addNotify() {
        addActionListener(action);
        Model.getPump().addModelEventListener(
                this, modelElement, propertyName);
    }
    
    /**
     * Remove all listeners when the component is removed from its parent
     */
    public void removeNotify() {
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
            setSelected((Boolean) getterSetter.get(modelElement, propertyName));
        }
    }
    
    private static class SetAction extends UndoableAction {

        /**
         * The class uid
         */
        private static final long serialVersionUID = -2708077474004286682L;
        
        private final GetterSetter getterSetter;
        private final String propertyName;
        private Object modelElement;
        
        /**
         * Constructor for ActionSetElementOwnershipSpecification.
         */
        protected SetAction(
                final GetterSetter getterSetter, 
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
            this.getterSetter.set(modelElement, source.isSelected(), propertyName);
        }
    }
}
