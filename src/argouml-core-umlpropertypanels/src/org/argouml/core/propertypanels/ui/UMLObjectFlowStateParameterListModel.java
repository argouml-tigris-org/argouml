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
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.AbstractActionRemoveElement;

/**
 * @author mkl
 */
class UMLObjectFlowStateParameterListModel extends UMLModelElementListModel {

    /**
     * The class uid
     */
    private static final long serialVersionUID = -6095531893213309195L;

    /**
     * Constructor for UMLObjectFlowStateParameterListModel.
     */
    public UMLObjectFlowStateParameterListModel(Object modelElement, String propertyName) {
        super(propertyName,
              modelElement.getClass(),
              new ActionAddOFSParameter(),
              new ActionNewOFSParameter(),
              new ActionRemoveOFSParameter());
        setTarget(modelElement);
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        if (getTarget() != null) {
            setAllElements(Model.getFacade().getParameters(getTarget()));
        }
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return Model.getFacade().getParameters(getTarget()).contains(
                element);
    }
    
    /**
     * @author mkl
     */
    private static class ActionAddOFSParameter extends AbstractActionAddModelElement2 {
        private Object choiceClass = Model.getMetaTypes().getParameter();

        /**
         * The constructor.
         */
        public ActionAddOFSParameter() {
            super();
            setMultiSelect(true);
        }


        protected void doIt(Collection selected) {
            Object t = getTarget();
            if (Model.getFacade().isAObjectFlowState(t)) {
                Model.getActivityGraphsHelper().setParameters(t, selected);
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

                // TODO: We may want to restrict the list to parameters which 
                // conform to the following WFR:
//              parameter.type = ofstype
//              or (parameter.kind = #in
//              and ofstype.allSupertypes->includes(type))
//              or ((parameter.kind = #out or parameter.kind = #return)
//              and type.allSupertypes->includes(ofstype))
//              or (parameter.kind = #inout
//              and ( ofstype.allSupertypes->includes(type)
//              or type.allSupertypes->includes(ofstype))))

            }
            return ret;
        }


        protected String getDialogTitle() {
            return Translator.localize("dialog.title.add-state");
        }


        protected List getSelected() {
            Object t = getTarget();
            if (Model.getFacade().isAObjectFlowState(t)) {
                return new ArrayList(Model.getFacade().getParameters(t));
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
    }
    
    /**
     * @author mkl
     */
    private static class ActionNewOFSParameter extends AbstractActionNewModelElement {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object target = getTarget();
            if (Model.getFacade().isAObjectFlowState(target)) {
                Object type = getType(target);
                Object parameter = Model.getCoreFactory().createParameter();
                Model.getCoreHelper().setType(parameter, type);
                Model.getActivityGraphsHelper().addParameter(target, parameter);
            }
        }
        private static Object getType(Object target) {
            Object type = Model.getFacade().getType(target);
            if (Model.getFacade().isAClassifierInState(type)) {
                type = Model.getFacade().getType(type);
            }
            return type;
        }
    }
    
    /**
     * @author mkl
     */
    private static class ActionRemoveOFSParameter extends AbstractActionRemoveElement {
        /**
         * Constructor.
         */
        public ActionRemoveOFSParameter() {
            super(Translator.localize("menu.popup.remove"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            Object param = getObjectToRemove();
            if (param != null) {
                Object t = getTarget();
                if (Model.getFacade().isAObjectFlowState(t)) {
                    Model.getActivityGraphsHelper().removeParameter(t, param);
                }
            }
        }
    }
}
