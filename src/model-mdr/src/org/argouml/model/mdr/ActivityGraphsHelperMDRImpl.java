// $Id$
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.ActivityGraphsHelper;
import org.argouml.model.Model;
import org.omg.uml.behavioralelements.activitygraphs.ClassifierInState;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.activitygraphs.Partition;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.foundation.core.BehavioralFeature;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.modelmanagement.UmlPackage;

/**
 * Class to implement ActivityGraphsHelper.
 * @since ARGO0.19.5
 * @author Ludovic Ma&icirc;tre
 * Derived from NSUML implementation
 */
class ActivityGraphsHelperMDRImpl implements ActivityGraphsHelper {

    /**
     * Constructor.
     */
    public ActivityGraphsHelperMDRImpl() {
        super();
    }

    /*
     * @see org.argouml.model.ActivityGraphsHelper#findClassifierByName(
     *         java.lang.Object, java.lang.String)
     */
    public Object findClassifierByName(Object ofs, String s) {
        if (!(ofs instanceof ObjectFlowState)) {
            throw new IllegalArgumentException();
        }

        CompositeState cs = ((ObjectFlowState) ofs).getContainer();
        StateMachine sm = cs.getStateMachine();
        ModelElement ns = sm.getContext();
        if (ns == null) {
            return null;
        }
        if (!(ns instanceof Namespace)) {
            ns = ns.getNamespace();
        }
        if (ns != null) {
            Collection c =
                Model.getModelManagementHelper().getAllModelElementsOfKind(
                    ns, Model.getMetaTypes().getClassifier());
            Iterator i = c.iterator();
            while (i.hasNext()) {
                ModelElement classifier = (ModelElement) i.next();
                String cn = classifier.getName();
                if (cn != null && cn.equals(s)) {
                    return classifier;
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
        return null;
    }


    public Object findStateByName(Object c, String s) {
        if (!(c instanceof Classifier)) {
            throw new IllegalArgumentException();
        }

        if ((s == null) || (s.equals(""))) {
            return null;
        }

        Collection allStatemachines = Model.getFacade().getBehaviors(c);
        Iterator i = allStatemachines.iterator();
        while (i.hasNext()) {
            StateMachine statemachine = (StateMachine) i.next();
            State top = statemachine.getTop();
            Collection allStates =
                Model.getStateMachinesHelper().getAllSubStates(top);
            Iterator ii = allStates.iterator();
            while (ii.hasNext()) {
                StateVertex state = (StateVertex) ii.next();

                String statename = state.getName();
                if (statename != null) {
                    if (statename.equals(s)) {
                        return state;
                    }
                }
            }
        }
        return null;
    }


    public boolean isAddingActivityGraphAllowed(Object context) {
        return context instanceof BehavioralFeature
            || context instanceof Classifier
            || context instanceof UmlPackage;
    }


    public void addInState(Object classifierInState, Object state) {
        if (classifierInState instanceof ClassifierInState
                && state instanceof State) {
            ((ClassifierInState) classifierInState).getInState().add(state);
        } else {
            throw new IllegalArgumentException(
                    "classifierInState: " + classifierInState
                    + " or state: " + state);
        }
    }


    public void setInStates(Object classifierInState, Collection newStates) {
        if (classifierInState instanceof ClassifierInState) {
            ClassifierInState cis = (ClassifierInState) classifierInState;
            CollectionHelper.update(cis.getInState(), newStates);
        } else {
            throw new IllegalArgumentException(
                    "classifierInState: " + classifierInState);
        }
    }
    

    public void setContents(Object partition, Collection contents) {
        if (partition instanceof Partition) {
            Partition p = (Partition) partition;
            CollectionHelper.update(p.getContents(), contents);
        } else {
            throw new IllegalArgumentException(
                    "Partition: " + partition);
        }
    }

    /**
     * @deprecated Use {@link #setSynch(Object,boolean)} instead
     */
    public void setIsSynch(Object objectFlowState, boolean isSynch) {
        setSynch(objectFlowState, isSynch);
    }

    public void setSynch(Object objectFlowState, boolean isSynch) {
        ((ObjectFlowState) objectFlowState).setSynch(isSynch);
    }


}

