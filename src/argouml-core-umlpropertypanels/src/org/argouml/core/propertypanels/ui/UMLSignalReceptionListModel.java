// $Id$
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
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionRemoveElement;

/**
 * The model for the listbox showing the receptions of a signal.
 * 
 * @author Michiel
 */
class UMLSignalReceptionListModel extends UMLModelElementListModel {

    /**
     * The class uid
     */
    private static final long serialVersionUID = 1045014158284951739L;

    /**
     * Construct a list model showing the receptions of a signal.
     */
    public UMLSignalReceptionListModel(Object modelElement) {
        /*
         * The event to listen to is "reception", so that model updates
         * get shown in the list. Reproduce this by adding a new reception,
         * and see the result displayed in the list.
         */
        super("reception",
                modelElement.getClass(),
            new ActionAddReceptionSignal(), 
            new ActionRemoveReceptionSignal());
        setTarget(modelElement);
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        if (getTarget() != null) {
            setAllElements(Model.getFacade().getReceptions(getTarget()));
        }
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(java.lang.Object)
     */
    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAReception(element)
            && Model.getFacade().getReceptions(getTarget()).contains(element);
    }

    /**
     * This Action adds a Reception to a Signal.
     * 
     * @author Michiel
     */
    private static class ActionAddReceptionSignal extends AbstractActionAddModelElement2 {

        /**
         * Construct an Action which adds a Reception to a Signal.
         */
        public ActionAddReceptionSignal() {
            super();
        }


        protected List getChoices() {
            List ret = new ArrayList();
            Object model =
                ProjectManager.getManager().getCurrentProject().getModel();
            if (getTarget() != null) {
                ret.addAll(Model.getModelManagementHelper()
                    .getAllModelElementsOfKind(model, 
                        Model.getMetaTypes().getReception()));
            }
            return ret;
        }


        protected List getSelected() {
            List ret = new ArrayList();
            ret.addAll(Model.getFacade().getReceptions(getTarget()));
            return ret;
        }


        protected String getDialogTitle() {
            return Translator.localize("dialog.title.add-receptions");
        }


        @Override
        protected void doIt(Collection selected) {
            Model.getCommonBehaviorHelper().setReception(getTarget(), selected);
        }

    }
    
    /**
     * This Action removes a Reception from a Signal.
     * 
     * @author Michiel
     */
    private static class ActionRemoveReceptionSignal extends AbstractActionRemoveElement {

        /**
         * The class uid
         */
        private static final long serialVersionUID = 8213584329453727152L;

        /**
         * Construct an Action which removes a Reception from a Signal.
         */
        public ActionRemoveReceptionSignal() {
            super(Translator.localize("menu.popup.remove"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            Object reception = getObjectToRemove(); 
            if (reception != null) {
                Object signal = getTarget();
                if (Model.getFacade().isASignal(signal)) {
                    // TODO: Should we delete the Reception?  A Reception
                    // without a Signal violates the cardinality of 1 in
                    // the metamodel - tfm - 20070308
                    Model.getCommonBehaviorHelper().removeReception(signal, 
                            reception);
                }
            }
        }
    }
}
