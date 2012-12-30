/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
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

package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;

/**
 * @author pepargouml@yahoo.es
 */
class UMLStubStateComboBoxModel extends UMLComboBoxModel {

    /**
     * The class uid
     */
    private static final long serialVersionUID = -3567856233571414072L;

    /**
     * Constructor for UMLStubStateComboBoxModel.
     */
    public UMLStubStateComboBoxModel(
            final String propertyName,
            final Object target) {
        super(target, propertyName, true);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return (Model.getFacade().isAStateVertex(element)
                && !Model.getFacade().isAConcurrentRegion(element)
                && Model.getFacade().getName(element) != null);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel#buildModelList()
     */
    protected void buildModelList() {
        removeAllElements();
        Object stateMachine = null;
        if (Model.getFacade().isASubmachineState(
                Model.getFacade().getContainer(getTarget()))) {
            stateMachine = Model.getFacade().getSubmachine(
                    Model.getFacade().getContainer(getTarget()));
        }
        if (stateMachine != null) {
            ArrayList v = (ArrayList) Model.getStateMachinesHelper()
                .getAllPossibleSubvertices(
                        Model.getFacade().getTop(stateMachine));
            ArrayList v2 = (ArrayList) v.clone();
            Iterator it = v2.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (!isValidElement(o)) {
                    v.remove(o);
                }
            }
            setElements(v);
        }
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        String objectName = null;
        Object container = null;
        if (getTarget() != null) {
            objectName = Model.getFacade().getReferenceState(getTarget());
            container = Model.getFacade().getContainer(getTarget());
            if (container != null
                    && Model.getFacade().isASubmachineState(container)
                    && Model.getFacade().getSubmachine(container) != null) {

                return Model.getStateMachinesHelper()
                        .getStatebyName(objectName,
                                Model.getFacade().getTop(Model.getFacade()
                                        .getSubmachine(container)));
            }
        }
        return null;
    }
    
    public Action getAction() {
        return new ActionSetStubStateReferenceState();
    }

    /**
     * Action to set the reference state of a stubstate.
     *
     * @author pepargouml@yahoo.es
     */
    class ActionSetStubStateReferenceState extends UndoableAction {

        /**
         * The class uid
         */
        private static final long serialVersionUID = 4412688175035654097L;

        /**
         * The constructor.
         */
        protected ActionSetStubStateReferenceState() {
            super(Translator.localize("action.set"), null);
            // Set the tooltip string:
            putValue(Action.SHORT_DESCRIPTION, 
                    Translator.localize("action.set"));
        }

        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            UMLComboBox box = (UMLComboBox) e.getSource();
            Object o = box.getSelectedItem();
            if (o != null) {
                String name = Model.getStateMachinesHelper().getPath(o);
                if (name != null)
                    Model.getStateMachinesHelper()
                            .setReferenceState(box.getTarget(), name);
            }
        }
    }
}
