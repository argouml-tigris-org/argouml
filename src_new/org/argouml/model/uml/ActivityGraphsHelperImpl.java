// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.model.uml;

import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.ActivityGraphsHelper;

import ru.novosoft.uml.behavior.activity_graphs.MClassifierInState;
import ru.novosoft.uml.behavior.activity_graphs.MObjectFlowState;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.model_management.MPackage;

/**
 * Helper class for UML BehavioralElements::ActivityGraphs Package.
 *
 * Current implementation is a placeholder.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
class ActivityGraphsHelperImpl implements ActivityGraphsHelper {

    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    ActivityGraphsHelperImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * Finds the Classifier to which a given ObjectFlowState
     * refers by its given name. This function may be used for when the user
     * types the name of a classifier in the diagram, in an ObjectFlowState.
     *
     * @author MVW
     * @param ofs the given ObjectFlowState
     * @param s   the given String that represents
     *            the name of the "type" Classifier
     * @return    the found classifier or null
     */
    public Object findClassifierByName(Object ofs, String s) {
        if (!(ofs instanceof MObjectFlowState)) {
            throw new IllegalArgumentException();
        }

        MCompositeState cs = ((MObjectFlowState) ofs).getContainer();
        MStateMachine sm = cs.getStateMachine();
        MModelElement ns = sm.getContext();
        if (!(ns instanceof MNamespace)) {
            ns = ns.getNamespace();
        }
        if (ns != null) {
            Collection c =
                nsmodel.getModelManagementHelper()
                	.getAllModelElementsOfKind(ns,
                	        nsmodel.getMetaTypes().getClassifier());
            Iterator i = c.iterator();
            while (i.hasNext()) {
                MModelElement classifier = (MModelElement) i.next();
                String cn = classifier.getName();
                if (cn.equals(s)) {
                    return classifier;
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
        return null;
    }

    /**
     * Find a state of a Classifier by its name.
     * This routine is used to make the connection between
     * a ClassifierInState and its State.
     *
     * @author mvw
     * @param c the Classifier. If this is not a Classifier, then
     *          IllegalArgumentException is thrown.
     * @param s the string that represents the name of
     *          the state we are looking for. If "" or null, then
     *          null is returned straight away.
     * @return  the State (as Object) or null, if not found.
     */
    public Object findStateByName(Object c, String s) {
        if (!(c instanceof MClassifier)) {
            throw new IllegalArgumentException();
        }

        if ((s == "") || (s == null)) { // TODO: Shouldn't it be "".equals(s)?
            return null;
        }
        Collection allStatemachines = ((MClassifier) c).getBehaviors();
        Iterator i = allStatemachines.iterator();
        while (i.hasNext()) {
            MStateMachine statemachine = (MStateMachine) i.next();
            MState top = statemachine.getTop();
            Collection allStates =
                nsmodel.getStateMachinesHelper().getAllSubStates(top);
            Iterator ii = allStates.iterator();
            while (ii.hasNext()) {
                MState state = (MState) ii.next();

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

    /**
     * Returns true if an activitygraph may be added to the given
     * context. To decouple ArgoUML as much as possible from the NSUML
     * model, the parameter of the method is of type Object.<p>
     *
     * An ActivityGraph specifies the dynamics of<ol>
     * <li> a Package, or
     * <li> a Classifier (including UseCase), or
     * <li> a BehavioralFeature.
     * </ol>
     *
     * @param context the given context
     * @return boolean true if an activitygraph may be added
     */
    public boolean isAddingActivityGraphAllowed(Object context) {
        return context instanceof MBehavioralFeature
            || context instanceof MClassifier
            || context instanceof MPackage;
    }

    /**
     * @author mvw
     * @param classifierInState the classifierInState
     * @param state the state that will be linked
     */
    public void addInState(Object classifierInState, Object state) {
        if (classifierInState instanceof MClassifierInState
                && state instanceof MState) {
            ((MClassifierInState) classifierInState)
            	.addInState((MState) state);
        } else {
            throw new IllegalArgumentException(
                    "classifierInState: " + classifierInState
                    + " or state: " + state);
        }
    }
}

