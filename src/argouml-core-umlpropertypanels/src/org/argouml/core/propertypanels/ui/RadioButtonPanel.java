/* $Id$
 *******************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
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

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import org.argouml.core.propertypanels.model.GetterSetterManager;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.UndoableAction;
import org.tigris.swidgets.FlexiGridLayout;

/**
 * A collection of radio buttons representing the state of some UML property
 * on a UML element.
 * @author Bob Tarling
 */
public class RadioButtonPanel extends JPanel
        implements PropertyChangeListener {

    /**
     * The class uid
     */
    private static final long serialVersionUID = -3786246432442765208L;

    /**
     * The UML element this panel represents
     */
    private final Object umlElement;

    /**
     * The UML property this panel represents
     */
    private final String propertyName;

    /**
     * The group of buttons
     */
    private final ButtonGroup buttonGroup = new ButtonGroup();
    
    /**
     * The getter/setter facade for accessing the model subsystem
     */
    private final GetterSetterManager getterSetterManager;
    
    /**
     * Constructor for RadioButtonPanel.
     * @param umlElement the UML element this radio panel represents and is
     * listening to
     * @param propertyName the property name of the UML element this radio
     * panel represents and is listening to
     * @param horizontal determines the orientation
     * @param getterSetterManager the manager for getting and setting model
     * element values
     */
    public RadioButtonPanel(
            final Object umlElement,
            final String propertyName,
            final boolean horizontal,
            final GetterSetterManager getterSetterManager) {
        super(true);
        setDoubleBuffered(true);
        
        final Collection options =
            getterSetterManager.getOptions(umlElement, propertyName, null);
        
        setLayout(horizontal
                ? new BoxLayout(this, BoxLayout.X_AXIS)
                : new FlexiGridLayout(0, options.size()));

        this.propertyName = propertyName;
        this.getterSetterManager = getterSetterManager;
        this.umlElement = umlElement;
        
        final Font font = LookAndFeelMgr.getInstance().getStandardFont();
        final String label = Translator.localize("label." + propertyName);
        if (label != null) {
            TitledBorder border = new TitledBorder(label);
            border.setTitleFont(font);
            setBorder(border);
        }
        
        Action action = new SetAction(getterSetterManager, umlElement, propertyName);
        
        buttonGroup.add(new JRadioButton());
        
        for (Object option : options) {
            final String optionLabel =
                Translator.localize("label." + propertyName + "-" + option);
            final JRadioButton button = new JRadioButton(optionLabel);
            button.addActionListener(action);
            button.setActionCommand((String) option);
            button.setFont(font);
            button.setName((String) option);
            buttonGroup.add(button);
            add(button);
        }
        
        build();
        
        Model.getPump().addModelEventListener(
                this, umlElement, propertyName);
    }
    
    /**
     * Remove listeners when this component is removed
     */
    public void removeNotify() {
        Model.getPump().removeModelEventListener(
                this, umlElement, propertyName);
        
        final Enumeration<AbstractButton> en =
            buttonGroup.getElements();
        en.nextElement();
        while (en.hasMoreElements()) {
            JRadioButton b = (JRadioButton) en.nextElement();
            b.removeActionListener(b.getAction());
        }
    }
    
    private GetterSetterManager getGetterSetter() {
        return getterSetterManager;
    }
    
    public void setEnabled(boolean enabled) {
        for (final Component component : getComponents()) {
            component.setEnabled(enabled);
        }
    }

    /*
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals(propertyName)) {
            build();
        }
    }

    private void build() {
        final String value =
            (String) getGetterSetter().get(umlElement, propertyName, null);
        final Enumeration<AbstractButton> en =
            buttonGroup.getElements();
        if (value == null) {
            en.nextElement().setSelected(true);
            return;
        }
        while (en.hasMoreElements()) {
            AbstractButton b = en.nextElement();
            if (value.equals(b.getActionCommand())) {
                b.setSelected(true);
                break;
            }
        }
    }
    
    /**
     * This action sets the Visibility of a ModelElement. 
     * Next to a ModelElement, this also works for an 
     * ElementResidence and ElementImport.
     *
     * @author jaap.branderhorst@xs4all.nl
     * @since Jan 4, 2003
     */
    private static class SetAction extends UndoableAction {

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
            this.modelElement = modelElement;
            this.getterSetter = getterSetter;
            this.propertyName = propertyName;
        }

        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            JRadioButton source = (JRadioButton) e.getSource();
            getterSetter.set(
                    modelElement, 
                    source.getActionCommand(), 
                    propertyName);
        }
    }
}
