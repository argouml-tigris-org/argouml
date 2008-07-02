// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.util.Collection;

import org.argouml.model.ActivityGraphsFactory;
import org.omg.uml.behavioralelements.activitygraphs.ActionState;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.activitygraphs.CallState;
import org.omg.uml.behavioralelements.activitygraphs.ClassifierInState;
import org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState;
import org.omg.uml.behavioralelements.activitygraphs.Partition;
import org.omg.uml.behavioralelements.activitygraphs.SubactivityState;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.foundation.core.BehavioralFeature;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;

/**
 * Factory to create UML classes for the UML BehaviorialElements::ActivityGraphs
 * package.
 * <p>
 * @since ARGO0.19.5
 * @author Ludovic Ma&icirc;tre
 * Derived from NSUML implementation by:
 * @author Thierry Lach
 */
class ActivityGraphsFactoryMDRImpl extends AbstractUmlModelFactoryMDR
        implements ActivityGraphsFactory {

    /**
     * The model implementation.
     */
    private MDRModelImplementation modelImpl;

    /**
     * Don't allow instantiation.
     * 
     * @param implementation
     *            To get other helpers and factories.
     */
    ActivityGraphsFactoryMDRImpl(MDRModelImplementation implementation) {
        modelImpl = implementation;
    }


    public ActionState createActionState() {
        ActionState myActionState = modelImpl.getUmlPackage()
                .getActivityGraphs().getActionState().createActionState();
        super.initialize(myActionState);
        return myActionState;
    }


    public ActivityGraph createActivityGraph() {
        ActivityGraph myActivityGraph = modelImpl.getUmlPackage().
            getActivityGraphs().getActivityGraph().createActivityGraph();
        super.initialize(myActivityGraph);
        return myActivityGraph;
    }


    public CallState createCallState() {
        CallState myCallState = modelImpl.getUmlPackage().getActivityGraphs().
            getCallState().createCallState();
        super.initialize(myCallState);
        return myCallState;
    }


    public ClassifierInState createClassifierInState() {
        ClassifierInState myClassifierInState = modelImpl.getUmlPackage().
            getActivityGraphs().getClassifierInState().
                createClassifierInState();
        super.initialize(myClassifierInState);
        return myClassifierInState;
    }


    public ObjectFlowState createObjectFlowState() {
        ObjectFlowState myObjectFlowState = modelImpl.getUmlPackage().
            getActivityGraphs().getObjectFlowState().
                createObjectFlowState();
        super.initialize(myObjectFlowState);
        return myObjectFlowState;
    }


    public Partition createPartition() {
        Partition myPartition = modelImpl.getUmlPackage().getActivityGraphs().
            getPartition().createPartition();
        super.initialize(myPartition);
        return myPartition;
    }


    public SubactivityState createSubactivityState() {
        SubactivityState mySubactivityState =
                modelImpl.getUmlPackage().getActivityGraphs()
                        .getSubactivityState().createSubactivityState();
        super.initialize(mySubactivityState);
        return mySubactivityState;
    }


    public ActivityGraph buildActivityGraph(Object theContext) {
        if (theContext instanceof ModelElement) {
            ActivityGraph myActivityGraph = createActivityGraph();
            myActivityGraph.setContext((ModelElement) theContext);
            if (theContext instanceof Namespace) {
                myActivityGraph.setNamespace((Namespace) theContext);
            } else if (theContext instanceof BehavioralFeature) {
                myActivityGraph.setNamespace(((BehavioralFeature) theContext).
                        getOwner());
            }
            State top =
                    (CompositeState) modelImpl.getStateMachinesFactory()
                            .buildCompositeStateOnStateMachine(myActivityGraph);
            myActivityGraph.setTop(top);
            return myActivityGraph;
        }
        throw new IllegalArgumentException(
                "Cannot create an ActivityGraph with context " + theContext);
    }


    public ObjectFlowState buildObjectFlowState(Object compositeState) {
        if (!(compositeState instanceof CompositeState)) {
            throw new IllegalArgumentException();
        }

        ObjectFlowState state = createObjectFlowState();
        state.setContainer((CompositeState) compositeState);
        return state;
    }


    public ClassifierInState buildClassifierInState(Object classifier,
            Collection state) {
        if (!(classifier instanceof Classifier)) {
            throw new IllegalArgumentException();
        }
        if (state.size() < 1) {
            throw new IllegalArgumentException(
                    "Collection of states must have at least one element");
        }

        ClassifierInState c = createClassifierInState();
        c.setType((Classifier) classifier);
        c.getInState().addAll(state);
        c.setNamespace(((Classifier) classifier).getNamespace());
        // this doesn't support I18N or multiple states, 
        // but it's just a default
        c.setName(((Classifier) classifier).getName() 
                + "inState[" 
                + ((State) state.iterator().next()).getName() 
                + "]");
        return c;
    }

    /**
     * @param elem
     *            the ActionState to be deleted
     */
    void deleteActionState(Object elem) {
        if (!(elem instanceof ActionState)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param elem
     *            the ActivityGraph to be deleted
     */
    void deleteActivityGraph(Object elem) {
        if (!(elem instanceof ActivityGraph)) {
            throw new IllegalArgumentException();
        }
        // Partitions are composite elements and will get deleted implicitly
        // Partition contents don't need to be deleted or checked
    }

    /**
     * @param elem
     *            the CallState to be deleted
     */
    void deleteCallState(Object elem) {
        if (!(elem instanceof CallState)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param elem
     *            the ClassifierInState to be deleted
     */
    void deleteClassifierInState(Object elem) {
        if (!(elem instanceof ClassifierInState)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param elem
     *            ObjectFlowState
     */
    void deleteObjectFlowState(Object elem) {
        if (!(elem instanceof ObjectFlowState)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param elem
     *            Partition
     */
    void deletePartition(Object elem) {
        if (!(elem instanceof Partition)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param elem
     *            SubactivityState
     */
    void deleteSubactivityState(Object elem) {
        if (!(elem instanceof SubactivityState)) {
            throw new IllegalArgumentException();
        }
    }

}
