/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2006 The Regents of the University of California. All
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

// $header$
package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionNewModelElement;

/**
 * @since Dec 15, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
class UMLTransitionTriggerListModel extends UMLModelElementListModel {

    /**
     * Constructor for UMLTransitionTriggerListModel.
     */
    public UMLTransitionTriggerListModel(Object modelElement, String propertyName) {
        super(propertyName);
        setTarget(modelElement);
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        removeAllElements();
        addElement(Model.getFacade().getTrigger(getTarget()));
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return element == Model.getFacade().getTrigger(getTarget());
    }

    @Override
    public boolean buildPopup(JPopupMenu popup, int index) {
        
        JMenu subMenu = new JMenu(Translator.localize("action.new"));
        subMenu.add(new ActionNewEvent(getTarget(), Model.getMetaTypes().getCallEvent(), "button.new-callevent"));
        subMenu.add(new ActionNewEvent(getTarget(), Model.getMetaTypes().getChangeEvent(), "button.new-changeevent"));
        subMenu.add(new ActionNewEvent(getTarget(), Model.getMetaTypes().getSignalEvent(), "button.new-signalevent"));
        subMenu.add(new ActionNewEvent(getTarget(), Model.getMetaTypes().getTimeEvent(), "button.new-timeevent"));
        
        popup.add(subMenu);
        
        return true;
    }
    
    public AbstractActionAddModelElement2 getAddAction() {
        return new ActionAddEvent();
    }

    @Override
    protected boolean hasPopup() {
        return true;
    }
    
    private class ActionAddEvent extends AbstractActionAddModelElement2 {

        /**
         * The uid
         */
        private static final long serialVersionUID = -5468330543866735509L;


        /**
         * Constructor for ActionAddEvent.
         */
        public ActionAddEvent() {
            super();
            setMultiSelect(false);
        }


        protected List getChoices() {
            List vec = new ArrayList();
            // TODO: the namespace of enlisted events is currently the model. 
            // I think this is wrong, they should be
            // in the namespace of the activitygraph!
//            vec.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(
//                    Model.getFacade().getNamespace(getTarget()),
//                    Model.getMetaTypes().getEvent()));
            vec.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(
                    Model.getFacade().getRoot(getTarget()),
                    Model.getMetaTypes().getEvent()));

            return vec;
        }


        protected List getSelected() {
            List vec = new ArrayList();
            Object trigger = Model.getFacade().getTrigger(getTarget());
            if (trigger != null)
                vec.add(trigger);
            return vec;
        }


        protected String getDialogTitle() {
            return Translator.localize("dialog.title.add-events");
        }


        @Override
        protected void doIt(Collection selected) {
            Object trans = getTarget();
            if (selected == null || selected.size() == 0) {
                Model.getStateMachinesHelper().setEventAsTrigger(trans, null);
            } else {
                Model.getStateMachinesHelper().setEventAsTrigger(trans,
                        selected.iterator().next());
            }
        }
    }

    private class ActionNewEvent extends AbstractActionNewModelElement {

        private Object eventMetaType;
        
        /**
         * Constructor for ActionNewEvent.
         */
        public ActionNewEvent(Object transition, Object eventMetaType, String name) {
            super(name);
            setTarget(transition);
            this.eventMetaType = eventMetaType;
        }

        /**
         * Implementors should create a concrete event like an instance of
         * SignalEvent in this method.
         * @param ns the namespace
         * @return Object
         */
        private Object createEvent(Object ns) {
            if (eventMetaType.equals(Model.getMetaTypes().getCallEvent())) {
                return Model.getStateMachinesFactory().buildCallEvent(ns);
            }
            if (eventMetaType.equals(Model.getMetaTypes().getChangeEvent())) {
                return Model.getStateMachinesFactory().buildChangeEvent(ns);
            }
            if (eventMetaType.equals(Model.getMetaTypes().getSignalEvent())) {
                return Model.getStateMachinesFactory().buildSignalEvent(ns);
            }
            if (eventMetaType.equals(Model.getMetaTypes().getTimeEvent())) {
                return Model.getStateMachinesFactory().buildTimeEvent(ns);
            }
            throw new IllegalStateException("We expect a metatype which is an Event");
        }

        /**
         * Creates the event, sets its role and namespace,
         * and navigates towards it.
         *
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            Object target = getTarget();
            Object model =
                    ProjectManager.getManager().getCurrentProject().getModel();
            Object ns = Model.getStateMachinesHelper()
                            .findNamespaceForEvent(target, model);
            Object event = createEvent(ns);
            Model.getStateMachinesHelper()
                        .setEventAsTrigger(target, event);
            TargetManager.getInstance().setTarget(event);
        }
    }
}
