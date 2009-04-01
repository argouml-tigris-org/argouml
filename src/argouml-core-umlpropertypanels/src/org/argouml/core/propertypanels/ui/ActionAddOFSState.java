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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;

/**
 * @author mkl
 */
class ActionAddOFSState extends AbstractActionAddModelElement2 {
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