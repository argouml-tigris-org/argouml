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

import org.argouml.model.ActivityGraphsFactory;
import org.argouml.model.ModelFacade;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.activity_graphs.MActivityGraph;
import ru.novosoft.uml.behavior.activity_graphs.MCallState;
import ru.novosoft.uml.behavior.activity_graphs.MClassifierInState;
import ru.novosoft.uml.behavior.activity_graphs.MObjectFlowState;
import ru.novosoft.uml.behavior.activity_graphs.MPartition;
import ru.novosoft.uml.behavior.activity_graphs.MSubactivityState;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.model_management.MPackage;

/**
 * Factory to create UML classes for the UML
 * BehaviorialElements::ActivityGraphs package.
 *
 * TODO: Change visibility to package after reflection problem solved.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
public class ActivityGraphsFactoryImpl
    	extends AbstractUmlModelFactory
    	implements ActivityGraphsFactory {

    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    ActivityGraphsFactoryImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * Create an empty but initialized instance of a UML ActionState.
     *
     * @return an initialized UML ActionState instance.
     */
    public Object createActionState() {
        Object modelElement = MFactory.getDefaultFactory().createActionState();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML ActivityGraph.
     *
     * @return an initialized UML ActivityGraph instance.
     */
    public Object createActivityGraph() {
        MActivityGraph modelElement =
	    MFactory.getDefaultFactory().createActivityGraph();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML CallState.
     *
     * @return an initialized UML CallState instance.
     */
    public Object createCallState() {
        MCallState modelElement =
	    MFactory.getDefaultFactory().createCallState();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML ClassifierInState.
     *
     * @return an initialized UML ClassifierInState instance.
     */
    public Object createClassifierInState() {
        MClassifierInState modelElement =
	    MFactory.getDefaultFactory().createClassifierInState();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML ObjectFlowState.
     *
     * @return an initialized UML ObjectFlowState instance.
     */
    public Object createObjectFlowState() {
        MObjectFlowState modelElement =
	    MFactory.getDefaultFactory().createObjectFlowState();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Partition.
     *
     * @return an initialized UML Partition instance.
     */
    public Object createPartition() {
        MPartition modelElement =
	    MFactory.getDefaultFactory().createPartition();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML SubactivityState.
     *
     * @return an initialized UML SubactivityState instance.
     */
    public Object createSubactivityState() {
        MSubactivityState modelElement =
	    MFactory.getDefaultFactory().createSubactivityState();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Builds an activity graph owned by the given context.<p>
     *
     * @param theContext is a ModelElement that will own the graph.
     * @return the new MActivityGraph as Object
     */
    public Object buildActivityGraph(Object theContext) {
        MModelElement context = (MModelElement) theContext;
    	if (context != null
	    && (context instanceof MBehavioralFeature
		|| context instanceof MClassifier
		|| context instanceof MPackage)) {
	    MActivityGraph graph = (MActivityGraph) createActivityGraph();
	    graph.setContext(context);
	    if (context instanceof MNamespace) {
		graph.setNamespace((MNamespace) context);
	    } else
    		if (context instanceof MBehavioralFeature) {
		    graph.setNamespace(context.getNamespace());
    		}
	    nsmodel.getStateMachinesFactory()
	    	.buildCompositeStateOnStateMachine(graph);
	    return graph;
    	} else {
	    throw new IllegalArgumentException("In buildActivityGraph: "
					       + "context null or not legal");
    	}
    }

    /**
     * Builds an objectflowstate. The objectflowstate will be a subvertix of
     * the given compositestate. The parameter compositeState is of
     * type Object to decouple the factory and NSUML as much as
     * possible from the rest of ArgoUML.
     *
     * @author MVW
     * @param compositeState the given compositestate
     * @return Object the newly build objectflow state
     */
    public Object buildObjectFlowState(Object compositeState) {
        if (compositeState instanceof MCompositeState) {
            Object state = createObjectFlowState();
            ((MObjectFlowState) state).setContainer(
                    (MCompositeState) compositeState);
            return state;
        }
        return null;
    }

    /**
     * Builds a ClassifierInState. Links it to the 2 required objects:
     * the classifier that forms the type of this classifierInState,
     * and the state.
     * @param classifier the classifier (type)
     * @param state the state (inState)
     * @return the newly build classifierInState
     */
    public Object buildClassifierInState(Object classifier, Object state) {
        if (classifier instanceof MClassifier && state instanceof MState) {
            Object c = createClassifierInState();
            ModelFacade.setType(c, classifier);
            ModelFacade.addInState(c, state);
        }
        return null;
    }


    /**
     * @param elem the ActionState to be deleted
     */
    public void deleteActionState(Object elem) { }

    /**
     * @param elem the ActivityGraph to be deleted
     */
    public void deleteActivityGraph(Object elem) { }

    /**
     * @param elem the CallState to be deleted
     */
    public void deleteCallState(Object elem) { }

    /**
     * @param elem the ClassifierInState to be deleted
     */
    public void deleteClassifierInState(Object elem) { }

    /**
     * @param elem ObjectFlowState
     */
    public void deleteObjectFlowState(Object elem) { }

    /**
     * @param elem Partition
     */
    public void deletePartition(Object elem) { }

    /**
     * @param elem SubactivityState
     */
    public void deleteSubactivityState(Object elem) { }

}

