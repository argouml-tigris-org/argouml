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

class CheckBox extends JCheckBox 
    implements PropertyChangeListener {

    /**
     * The class uid
     */
    private static final long serialVersionUID = 2654856740168885592L;

    private Object target;
    private String propertyName;
    
    /**
     * The action that will be called when the checkbox changes
     */
    private Action action;

    private final GetterSetter getterSetter;
    
    /**
     * Constructor for UMLCheckBox.
     * @param text the text of the check box
     * @param a the action we're going to listen to
     * @param name the property set name
     */
    public CheckBox(final String propertyName, final Object target, GetterSetter getterSetter) {
        super(Translator.localize("label." + propertyName));
        
        this.getterSetter = getterSetter;
        this.propertyName = propertyName;
        this.target = target;
        
        setFont(LookAndFeelMgr.getInstance().getStandardFont());
        
        build();
        
        action = new SetAction(getterSetter, target, propertyName);
        setActionCommand((String) action.getValue(Action.ACTION_COMMAND_KEY));
    }
    
    /**
     * Add listeners when the component is placed on its parent
     */
    public void addNotify() {
        addActionListener(action);
        Model.getPump().addModelEventListener(
                this, target, propertyName);
    }
    
    /**
     * Remove all listeners when the component is removed from its parent
     */
    public void removeNotify() {
        removeActionListener(action);
        Model.getPump().removeModelEventListener(
                this, target, propertyName);
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
            setSelected((Boolean) getterSetter.get(target, propertyName));
        }
    }
    
    private static class SetAction extends UndoableAction {

        /**
         * The class uid
         */
        private static final long serialVersionUID = -2708077474004286682L;
        
        private final GetterSetter getterSetter;
        private final String propertyName;
        private Object target;
        
        /**
         * Constructor for ActionSetElementOwnershipSpecification.
         */
        protected SetAction(
                final GetterSetter getterSetter, 
                final Object target,
                final String propertyName) {
            super(Translator.localize("Set"), null);
            this.target = target;
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
            this.getterSetter.set(target, source.isSelected(), propertyName);
        }
    }
}
