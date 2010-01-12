/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPopupMenu;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionNewModelElement;

/**
 * @author MarkusK
 */
class UMLSignalEventSignalList extends UMLMutableLinkedList {

    /**
     * The class uid
     */
    private static final long serialVersionUID = -1557658052001738064L;

    /**
     * Constructor for UMLTransitionTriggerList.
     * @param dataModel the model
     */
    public UMLSignalEventSignalList(UMLModelElementListModel dataModel) {
        super(dataModel, (AbstractActionAddModelElement2) null, null, null);
    }

    /*
     * @see org.argouml.uml.ui.UMLMutableLinkedList#getPopupMenu()
     */
    public JPopupMenu getPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.add(new ActionAddSignalsToSignalEvent(getTarget()));
        menu.add(new ActionNewSignal());
        return menu;
    }
    
    /**
     * Provide a dialog which helps the user to select one event
     * out of an existing list,
     * which will be used as the trigger of the transition.
     *
     * @author MarkusK
     *
     */
    private static class ActionAddSignalsToSignalEvent extends AbstractActionAddModelElement2 {

        /**
         * Constructor for ActionAddClassifierRoleBase.
         */
        public ActionAddSignalsToSignalEvent(Object target) {
            super();
            setMultiSelect(false);
            setTarget(target);
        }


        protected List getChoices() {
            List vec = new ArrayList();

            vec.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(
                    Model.getFacade().getRoot(getTarget()),
                    Model.getMetaTypes().getSignal()));

            return vec;
        }


        protected List getSelected() {
            List vec = new ArrayList();
            Object signal = Model.getFacade().getSignal(getTarget());
            if (signal != null) {
                vec.add(signal);
            }
            return vec;
        }


        protected String getDialogTitle() {
            return Translator.localize("dialog.title.add-signal");
        }


        @Override
        protected void doIt(Collection selected) {
            Object event = getTarget();
            if (selected == null || selected.size() == 0) {
                Model.getCommonBehaviorHelper().setSignal(event, null);
            } else {
                Model.getCommonBehaviorHelper().setSignal(event,
                        selected.iterator().next());
            }
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = 6890869588365483936L;
    }
    
    /**
     * Create a new Signal.
     */
    public class ActionNewSignal extends AbstractActionNewModelElement {

        /**
         * The class uid
         */
        private static final long serialVersionUID = -1905204858078372670L;

        /**
         * The constructor.
         */
        public ActionNewSignal() {
            super("button.new-signal");
            putValue(Action.NAME, Translator.localize("button.new-signal"));
            Icon icon = ResourceLoaderWrapper.lookupIcon("SignalSending");
            putValue(Action.SMALL_ICON, icon);
        }

        /**
         * Creates a new signal and in case of a SignalEvent as target also set the
         * Signal for this event.<p>
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent e) {
            Object target = TargetManager.getInstance().getModelTarget();
            if (Model.getFacade().isASignalEvent(target)
                    || Model.getFacade().isASendAction(target)
                    || Model.getFacade().isAReception(target)
                    || Model.getFacade().isABehavioralFeature(target)) {
                Object newSig = 
                    Model.getCommonBehaviorFactory().buildSignal(target);
                TargetManager.getInstance().setTarget(newSig);
            } else {
                Object ns = null;
                if (Model.getFacade().isANamespace(target)) {
                    ns = target;
                } else {
                    ns = Model.getFacade().getNamespace(target);
                }
                Object newElement = Model.getCommonBehaviorFactory().createSignal();
                TargetManager.getInstance().setTarget(newElement);
                Model.getCoreHelper().setNamespace(newElement, ns);
            }
            super.actionPerformed(e);
        }
    }
}
