/* $Id: 
 *****************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *    Michiel van der Wulp
 *****************************************************************************/

package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Action;

import org.argouml.model.Model;
import org.argouml.ui.ActionCreateContainedModelElement;
import org.argouml.ui.UndoableAction;


public class UMLTransitionTriggerComboBoxModel extends UMLComboBoxModel {

    public UMLTransitionTriggerComboBoxModel(
            final String propertyName,
            final Object target) {
        super(target, propertyName, false);
        Model.getPump().addClassModelEventListener(this,
                Model.getMetaTypes().getNamespace(), "ownedElement");
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void buildMinimalModelList() {
        Collection list = new ArrayList(1);
        Object element = getSelectedModelElement();
        if (element != null) {
            list.add(element);
        }
        setElements(list);
    }
    
    @Override
    protected boolean isLazy() {
        return true;
    }
    
    /**
     * For the list of possible items include all events under the first package
     * above the target transition
     * 
     * @see org.argouml.core.propertypanels.ui.UMLComboBoxModel#buildModelList()
     */
    protected void buildModelList() {
        final Object transition = getTarget();
        removeAllElements();

        Object parent =
            Model.getStateMachinesHelper()
                .findNamespaceForEvent(transition, null);
        final Collection list =
            Model.getModelManagementHelper().getAllModelElementsOfKind(
                parent,
                Model.getMetaTypes().getEvent());
        
        Object selectedElement = getSelectedModelElement();
        if (selectedElement != null && !list.contains(selectedElement)) {
            // Just in case the existing selected element is from elsewhere
            // make sure it is in the list
            list.add(selectedElement);
        }
        setElements(list);
    }

    protected boolean isValidElement(Object m) {
        return Model.getFacade().isAEvent(m);
    }

    protected Object getSelectedModelElement() {
        if (getTarget() != null) {
            return Model.getFacade().getTrigger(getTarget());
        }
        return null;
    }
    
    public List<Action> getActions() {
        final ArrayList<Action> actions = new ArrayList<Action>();
        actions.add(new ActionCreateContainedModelElement(
                Model.getMetaTypes().getCallEvent(), getTarget()));
        actions.add(new ActionCreateContainedModelElement(
                Model.getMetaTypes().getChangeEvent(), getTarget()));
        actions.add(new ActionCreateContainedModelElement(
                Model.getMetaTypes().getSignalEvent(), getTarget()));
        actions.add(new ActionCreateContainedModelElement(
                Model.getMetaTypes().getTimeEvent(), getTarget()));
        return actions;
    }

    public Action getAction() {
        
        return new SetAction();
    }
    
    class SetAction extends UndoableAction {
        
        /**
         * Constructor for ActionSetModelElementNamespace.
         */
        public SetAction() {
            super();
        }

        public void actionPerformed(ActionEvent e) {
            final Object source = e.getSource();
            final UMLComboBox box = (UMLComboBox) source;
            final Object trigger = box.getSelectedItem();
            if (trigger != Model.getFacade().getTrigger(getTarget())) {
                super.actionPerformed(e);
                Model.getStateMachinesHelper().setTrigger(getTarget(), trigger);
            }
        }
    }
}
