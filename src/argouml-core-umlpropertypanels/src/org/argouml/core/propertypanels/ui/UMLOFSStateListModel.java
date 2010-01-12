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

// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionRemoveElement;

/**
 * @author mkl
 */
class UMLOFSStateListModel extends UMLModelElementListModel {

    /**
     * The class uid
     */
    private static final long serialVersionUID = -9214579555300746872L;

    /**
     * Constructor for UMLOFSStateListModel.
     */
    public UMLOFSStateListModel(Object modelElement) {
        /* TODO: This needs work...
         * We also need to listen to addition/removal
         * of states to/from a ClassifierInState.
         */
        super("type", modelElement.getClass(),
            new ActionAddOFSState(),
            new ActionRemoveOFSState());
        setTarget(modelElement);
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        if (getTarget() != null) {
            Object classifier = Model.getFacade().getType(getTarget());
            if (Model.getFacade().isAClassifierInState(classifier)) {
                Collection c = Model.getFacade().getInStates(classifier);
                setAllElements(c);
            }
        }
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(java.lang.Object)
     */
    protected boolean isValidElement(Object elem) {
        Object t = getTarget();
        if (Model.getFacade().isAState(elem)
                && Model.getFacade().isAObjectFlowState(t)) {
            Object type = Model.getFacade().getType(t);
            if (Model.getFacade().isAClassifierInState(type)) {
                Collection c = Model.getFacade().getInStates(type);
                if (c.contains(elem)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * @author mkl
     */
    private static class ActionAddOFSState extends AbstractActionAddModelElement2 {
        private Object choiceClass = Model.getMetaTypes().getState();


        /**
         * The constructor.
         */
        public ActionAddOFSState() {
            super();
            setMultiSelect(true);
        }


        protected void doIt(Collection selected) {
            Object t = getTarget();
            if (Model.getFacade().isAObjectFlowState(t)) {
                Object type = Model.getFacade().getType(t);
                if (Model.getFacade().isAClassifierInState(type)) {
                    Model.getActivityGraphsHelper().setInStates(type, selected);
                } else if (Model.getFacade().isAClassifier(type)
                        && (selected != null)
                        && (selected.size() > 0)) {
                    /* So, we found a Classifier
                     * that is not a ClassifierInState.
                     * And at least one state has been selected.
                     * Well, let's correct that:
                     */
                    Object cis =
                        Model.getActivityGraphsFactory()
                            .buildClassifierInState(type, selected);
                    Model.getCoreHelper().setType(t, cis);
                }
            }
        }


        protected List getChoices() {
            List ret = new ArrayList();
            Object t = getTarget();
            if (Model.getFacade().isAObjectFlowState(t)) {
                Object classifier = getType(t);
                if (Model.getFacade().isAClassifier(classifier)) {
                    ret.addAll(Model.getModelManagementHelper()
                            .getAllModelElementsOfKindWithModel(classifier,
                                    choiceClass));
                }
                removeTopStateFrom(ret);
            }
            return ret;
        }


        protected String getDialogTitle() {
            return Translator.localize("dialog.title.add-state");
        }


        protected List getSelected() {
            Object t = getTarget();
            if (Model.getFacade().isAObjectFlowState(t)) {
                Object type = Model.getFacade().getType(t);
                if (Model.getFacade().isAClassifierInState(type)) {
                    return new ArrayList(Model.getFacade().getInStates(type));
                }
            }
            return new ArrayList();
        }
        
        private static Object getType(Object target) {
            Object type = Model.getFacade().getType(target);
            if (Model.getFacade().isAClassifierInState(type)) {
                type = Model.getFacade().getType(type);
            }
            return type;
        }
        /**
         * Utility function to remove the top states
         * from a given collection of states.
         *
         * @param ret a collection of states
         */
        static void removeTopStateFrom(Collection ret) {
            Collection tops = new ArrayList();
            for (Object state : ret) {
                if (Model.getFacade().isACompositeState(state)
                        && Model.getFacade().isTop(state)) {
                    tops.add(state);
                }
            }
            ret.removeAll(tops);
        }
    }
    
    /**
     * @author mkl
     */
    private static class ActionRemoveOFSState extends AbstractActionRemoveElement {

        /**
         * The class uid
         */
        private static final long serialVersionUID = 4745674604611374936L;

        /**
         * Constructor.
         */
        public ActionRemoveOFSState() {
            super(Translator.localize("menu.popup.remove"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            Object state = getObjectToRemove();
            if (state != null) {
                Object t = getTarget();
                if (Model.getFacade().isAObjectFlowState(t)) {
                    Object type = Model.getFacade().getType(t);
                    if (Model.getFacade().isAClassifierInState(type)) {
                        Collection states =
                            new ArrayList(
                                Model.getFacade().getInStates(type));
                        states.remove(state);
                        Model.getActivityGraphsHelper()
                                .setInStates(type, states);
                    }
                }
            }
        }
    }
}
