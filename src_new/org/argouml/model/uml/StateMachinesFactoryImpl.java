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

import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.argouml.model.StateMachinesFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.state_machines.MCallEvent;
import ru.novosoft.uml.behavior.state_machines.MChangeEvent;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MEvent;
import ru.novosoft.uml.behavior.state_machines.MFinalState;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MPseudostate;
import ru.novosoft.uml.behavior.state_machines.MSignalEvent;
import ru.novosoft.uml.behavior.state_machines.MSimpleState;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MStubState;
import ru.novosoft.uml.behavior.state_machines.MSubmachineState;
import ru.novosoft.uml.behavior.state_machines.MSynchState;
import ru.novosoft.uml.behavior.state_machines.MTimeEvent;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;
import ru.novosoft.uml.model_management.MModel;

/**
 * Factory to create UML classes for the UML
 * BehaviorialElements::StateMachines package.<p>
 *
 * MEvent and MStateVertex do not have create methods
 * since they are abstract classes in the NSUML model.<p>
 *
 * TODO: Change visibility to package after reflection problem solved.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
public class StateMachinesFactoryImpl
	extends AbstractUmlModelFactory
	implements StateMachinesFactory {

    /**
     * Don't allow instantiation.
     */
    StateMachinesFactoryImpl() {
    }

    /**
     * Create an empty but initialized instance of a UML CallEvent.
     *
     * @return an initialized UML CallEvent instance.
     */
    public MCallEvent createCallEvent() {
        MCallEvent modelElement =
	    MFactory.getDefaultFactory().createCallEvent();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML ChangeEvent.
     *
     * @return an initialized UML ChangeEvent instance.
     */
    public MChangeEvent createChangeEvent() {
        MChangeEvent modelElement =
	    MFactory.getDefaultFactory().createChangeEvent();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML CompositeState.
     *
     * @return an initialized UML CompositeState instance.
     */
    public MCompositeState createCompositeState() {
        MCompositeState modelElement =
	    MFactory.getDefaultFactory().createCompositeState();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML FinalState.
     *
     * @return an initialized UML FinalState instance.
     */
    public MFinalState createFinalState() {
        MFinalState modelElement =
	    MFactory.getDefaultFactory().createFinalState();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Guard.
     *
     * @return an initialized UML Guard instance.
     */
    public Object/*MGuard*/ createGuard() {
        MGuard modelElement = MFactory.getDefaultFactory().createGuard();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Pseudostate.
     *
     * @return an initialized UML Pseudostate instance.
     */
    public MPseudostate createPseudostate() {
        MPseudostate modelElement =
	    MFactory.getDefaultFactory().createPseudostate();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML SignalEvent.
     *
     * @return an initialized UML SignalEvent instance.
     */
    public MSignalEvent createSignalEvent() {
        MSignalEvent modelElement =
	    MFactory.getDefaultFactory().createSignalEvent();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML SimpleState.
     *
     * @return an initialized UML SimpleState instance.
     */
    public MSimpleState createSimpleState() {
        MSimpleState modelElement =
	    MFactory.getDefaultFactory().createSimpleState();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML State.
     *
     * @return an initialized UML State instance.
     */
    public MState createState() {
        MState modelElement = MFactory.getDefaultFactory().createState();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML StateMachine.
     *
     * @return an initialized UML StateMachine instance.
     */
    public MStateMachine createStateMachine() {
        MStateMachine modelElement =
	    MFactory.getDefaultFactory().createStateMachine();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML StubState.
     *
     * @return an initialized UML StubState instance.
     */
    public MStubState createStubState() {
        MStubState modelElement =
	    MFactory.getDefaultFactory().createStubState();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML SubmachineState.
     *
     * @return an initialized UML SubmachineState instance.
     */
    public MSubmachineState createSubmachineState() {
        MSubmachineState modelElement =
	    MFactory.getDefaultFactory().createSubmachineState();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML SynchState.
     *
     * @return an initialized UML SynchState instance.
     */
    public MSynchState createSynchState() {
        MSynchState modelElement =
	    MFactory.getDefaultFactory().createSynchState();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML TimeEvent.
     *
     * @return an initialized UML TimeEvent instance.
     */
    public MTimeEvent createTimeEvent() {
        MTimeEvent modelElement =
	    MFactory.getDefaultFactory().createTimeEvent();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Transition.
     *
     * @return an initialized UML Transition instance.
     */
    public Object createTransition() {
        MTransition modelElement =
	    MFactory.getDefaultFactory().createTransition();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Builds a compositestate as top for some statemachine.<p>
     *
     * @param statemachine the given statemachine
     * @return MCompositeState the newly build top state
     * @see #buildCompositeState(Object)
     */
    public MCompositeState buildCompositeState(MStateMachine statemachine) {
    	if (statemachine != null) {
            MCompositeState state = createCompositeState();
            state.setStateMachine(statemachine);
            state.setName("top");
            return state;
    	}
	    throw new IllegalArgumentException(
                "In buildCompositeState: "
		       + "statemachine is null");
    }

    /**
     * Builds a state machine owned by the given context.
     *
     * @param oContext the given context
     * @return MStateMachine the newly build statemachine
     */
    public MStateMachine buildStateMachine(Object oContext) {
    	if (oContext != null
        	    && (Model.getStateMachinesHelper()
        		.isAddingStatemachineAllowed(oContext))) {

    	    MStateMachine machine = createStateMachine();
            MModelElement context = (MModelElement) oContext;
    	    machine.setContext(context);
    	    if (context instanceof MClassifier) {
    	        machine.setNamespace((MClassifier) context);
    	    } else if (context instanceof MBehavioralFeature) {
    	        MBehavioralFeature feature = (MBehavioralFeature) context;
    	        machine.setNamespace(feature.getOwner());
    	    }
    	    Model.getStateMachinesFactory().buildCompositeState(machine);
    	    return machine;

    	}
	    throw new IllegalArgumentException("In buildStateMachine: "
					       + "context null or not legal");
    }

    /**
     * Builds a complete transition including all associations
     * (composite state the transition belongs to, source the
     * transition is coming from, destination the transition is going
     * to). The transition is owned by the compositestate.<p>
     *
     * @param owningState the composite state that owns the transition
     * @param source the source of the transition
     * @param dest the destination of the transition
     * @return MTransition the newly build transition
     */
    public Object buildTransition(MCompositeState owningState,
				  MStateVertex source, MStateVertex dest) {
      	if (owningState != null && source != null && dest != null
        	    && owningState.getSubvertices().contains(source)
        	    && owningState.getSubvertices().contains(dest)) {

    	    MTransition trans = (MTransition) createTransition();
    	    owningState.addInternalTransition(trans);
    	    trans.setSource(source);
    	    trans.setTarget(dest);
    	    return trans;

      	}
	    throw new IllegalArgumentException("In buildTransition: "
					       + "arguments not legal");
    }

    /**
     * Builds a pseudostate initialized as a choice pseudostate. The
     * pseudostate will be a subvertix of the given
     * compositestate. The parameter compositeState is of type Object
     * to decouple the factory and NSUML as much as possible from the
     * rest of ArgoUML.<p>
     *
     * @param compositeState the parent
     * @return MPseudostate
     */
    public MPseudostate buildPseudoState(Object compositeState) {
        if (compositeState instanceof MCompositeState) {
            MPseudostate state = createPseudostate();
            state.setKind(MPseudostateKind.BRANCH);
            state.setContainer((MCompositeState) compositeState);
            ((MCompositeState) compositeState).addSubvertex(state);
            return state;
        }
        return null;
    }

    /**
     * Builds a synchstate initalized with bound 0. The synchstate
     * will be a subvertix of the given compositestate. The parameter
     * compositeState is of type Object to decouple the factory and
     * NSUML as much as possible from the rest of ArgoUML.
     *
     * @param compositeState the given compositestate
     * @return MSynchState the newly created SynchState
     */
    public MSynchState buildSynchState(Object compositeState) {
        if (compositeState instanceof MCompositeState) {
            MSynchState state = createSynchState();
            state.setBound(0);
            state.setContainer((MCompositeState) compositeState);
            return state;
        }
        return null;
    }

    /**
     * Builds a stubstate initalized with an empty referenced
     * state. The stubstate will be a subvertix of the given
     * compositestate. The parameter compositeState is of type Object
     * to decouple the factory and NSUML as much as possible from the
     * rest of ArgoUML.
     *
     * @param compositeState the given composite state
     * @return MSynchState the newly build stubstate
     */
    public MStubState buildStubState(Object compositeState) {
        if (compositeState instanceof MCompositeState) {
            MStubState state = createStubState();
            state.setReferenceState("");
            state.setContainer((MCompositeState) compositeState);
            return state;
        }
        return null;
    }

    /**
     * Builds a compositestate initalized as a non-concurrent
     * composite state.  The compositestate will be a subvertix of the
     * given compositestate. The parameter compositeState is of type
     * Object to decouple the factory and NSUML as much as possible
     * from the rest of ArgoUML.
     *
     * @param compositeState the given compositestate
     * @return MSynchState the newly build synchstate
     * @see #buildCompositeState(MStateMachine)
     */
    public MCompositeState buildCompositeState(Object compositeState) {
        if (compositeState instanceof MCompositeState) {
            MCompositeState state = createCompositeState();
            state.setConcurent(false);
            state.setContainer((MCompositeState) compositeState);
            return state;
        }
        return null;
    }

    /**
     * Builds a simplestate. The simplestate will be a subvertix of
     * the given compositestate. The parameter compositeState is of
     * type Object to decouple the factory and NSUML as much as
     * possible.  from the rest of ArgoUML.
     *
     * @param compositeState the given compositestate
     * @return MSimpleState the newly build simple state
     */
    public MSimpleState buildSimpleState(Object compositeState) {
        if (compositeState instanceof MCompositeState) {
            MSimpleState state = createSimpleState();
            state.setContainer((MCompositeState) compositeState);
            return state;
        }
        return null;
    }

    /**
     * Builds a finalstate. The finalstate will be a subvertix of the
     * given compositestate. The parameter compositeState is of type
     * Object to decouple the factory and NSUML as much as possible.
     * from the rest of ArgoUML.
     *
     * @param compositeState the given compositestate
     * @return MFinalState the given compositestate
     */
    public MFinalState buildFinalState(Object compositeState) {
        if (compositeState instanceof MCompositeState) {
            MFinalState state = createFinalState();
            state.setContainer((MCompositeState) compositeState);
            return state;
        }
        return null;
    }

    /**
     * Builds a submachinestate. The submachinestate will be a
     * subvertix of the given compositestate. The parameter
     * compositeState is of type Object to decouple the factory and
     * NSUML as much as possible.  from the rest of ArgoUML.
     *
     * @param compositeState the given compositestate
     * @return MSubmachineState the given submachinestate
     */
    public MSubmachineState buildSubmachineState(Object compositeState) {
        if (compositeState instanceof MCompositeState) {
            MSubmachineState state = createSubmachineState();
            state.setStateMachine(null);
            state.setContainer((MCompositeState) compositeState);
            return state;
        }
        return null;
    }

    /**
     * Builds an internal transition for a given state. The parameter state is
     * of type Object to decouple the factory and NSUML as much as possible.
     *
     * @param state The state the internal transition should belong to
     * @return MTransition The internal transition constructed
     */
    public MTransition buildInternalTransition(Object state) {
        if (state instanceof MState) {
            MTransition trans = (MTransition) createTransition();
            trans.setState((MState) state);
            trans.setSource((MState) state);
            trans.setTarget((MState) state);
            return trans;
        }
        return null;
    }

    /**
     * Build a transition between a source state and a target state. The
     * parameters are of type Object to decouple the factory and NSUML as much
     * as possible.
     * This should not be used for internal transitions!
     * @param source The source state
     * @param target The target state
     * @return MTransition The resulting transition between source an state
     */
    public MTransition buildTransition(Object source, Object target) {
        if (source instanceof MStateVertex && target instanceof MStateVertex) {
            MTransition trans = (MTransition) createTransition();
            trans.setSource((MStateVertex) source);
            trans.setTarget((MStateVertex) target);
            trans.setStateMachine((MStateMachine) Model
                    .getStateMachinesHelper().getStateMachine(source));
            return trans;
        }
        return null;
    }

    /**
     * Builds a callevent whose namespace (and therefore the ownership) is the
     * rootmodel.
     * @param model the model
     * @return MCallEvent
     */
    public MCallEvent buildCallEvent(Object model) {
        MCallEvent event = createCallEvent();
        event.setNamespace((MModel) model);
        event.setName("");
        return event;
    }

    /**
     * Create a initialized instance of a CallEvent with a name
     * as a trigger for a Transition.
     * If an operation with corresponding name can be found, it is linked.
     *
     * @param trans Object MTransition for which the CallEvent is a trigger
     * @param name String with the trigger name - should not include "()"
     * @param model the model
     * @return an initialized UML CallEvent instance.
     */
    public MCallEvent buildCallEvent(Object trans, String name, Object model) {
        if (!(trans instanceof MTransition)) {
            throw new IllegalArgumentException();
        }
        MCallEvent evt = MFactory.getDefaultFactory().createCallEvent();
        evt.setNamespace((MModel) model);

        String operationName = name.indexOf("(") > 0
            ? name.substring(0, name.indexOf("(")).trim()
            : name.trim();
        ModelFacade.setName(evt, operationName);
        Object op = Model.getStateMachinesHelper()
                                .findOperationByName(trans, operationName);
        if (op != null) {
            ModelFacade.setOperation(evt, op);
        }
        return evt;
    }

    /**
     * Builds a signalevent whose namespace (and therefore the
     * ownership) is the rootmodel.<p>
     * @param model the model
     * @return MSignalEvent
     */
    public MSignalEvent buildSignalEvent(Object model) {
        MSignalEvent event = createSignalEvent();
        event.setNamespace((MModel) model);
        event.setName("");
        return event;
    }

    /**
     * Builds a named signalevent whose namespace (and therefore the
     * ownership) is the rootmodel.<p>
     * @param model the model
     * @param name String the name of the SignalEvent
     * @return MSignalEvent
     */
    public MSignalEvent buildSignalEvent(String name, Object model) {
        MSignalEvent event = createSignalEvent();
        event.setNamespace((MModel) model);
        event.setName(name);
        return event;
    }

    /**
     * Builds a timeevent whose namespace (and therefore the
     * ownership) is the rootmodel.<p>
     * @param model the Model
     * @return MTimeEvent
     */
    public MTimeEvent buildTimeEvent(Object model) {
        MTimeEvent event = createTimeEvent();
        event.setNamespace((MModel) model);
        event.setName("");
        return event;
    }

    /**
     * Builds a timeevent whose namespace (and therefore the
     * ownership) is the rootmodel.<p>
     *
     * @param s String for creating the TimeExpression
     * @param model the model
     * @return MTimeEvent
     */
    public MTimeEvent buildTimeEvent(String s, Object model) {
        MTimeEvent event = createTimeEvent();
        event.setNamespace((MModel) model);
        event.setName("");
        Object te = Model.getUmlFactory().getDataTypes()
                        .createTimeExpression("", s);
        ModelFacade.setWhen(event, te);
        return event;
    }

    /**
     * Builds a changeevent whose namespace (and therefore the
     * ownership) is the rootmodel.<p>
     * @param model the model
     * @return MChangeEvent
     */
    public MChangeEvent buildChangeEvent(Object model) {
        MChangeEvent event = createChangeEvent();
        event.setNamespace((MModel) model);
        event.setName("");
        return event;
    }

    /**
     * Builds a changeevent whose namespace (and therefore the
     * ownership) is the rootmodel.<p>
     * @param model the model
     * @param s String for creating the BooleanExpression
     * @return MChangeEvent
     */
    public MChangeEvent buildChangeEvent(String s, Object model) {
        MChangeEvent event = createChangeEvent();
        event.setNamespace((MModel) model);
        event.setName("");
        Object ce = Model.getUmlFactory().getDataTypes()
                            .createBooleanExpression("", s);
        ModelFacade.setExpression(event, ce);

        return event;
    }

    /**
     * Builds a guard condition with a given transition. The guard condition is
     * empty by default. The parameter is of type Object to decouple the factory
     * and NSUML as much as possible.
     * @param transition The transition that owns the resulting guard condition
     * @return MGuard The resulting guard condition
     */
    public Object/*MGuard*/ buildGuard(Object transition) {
        if (transition instanceof MTransition) {
            Object guard = createGuard();
            ModelFacade.setTransition(guard, transition);
            return guard;
        }
        return null;
    }

    /**
     * @param elem the UML element to be deleted
     */
    public void deleteCallEvent(MCallEvent elem) { }

    /**
     * @param elem the UML element to be deleted
     */
    public void deleteChangeEvent(MChangeEvent elem) { }

    /**
     * Deletes any associated subVertices.
     *
     * @param elem the UML element to be deleted
     */
    public void deleteCompositeState(MCompositeState elem) {

        Collection vertices = elem.getSubvertices();
        Iterator it = vertices.iterator();
        while (it.hasNext()) {
            MStateVertex vertex = (MStateVertex) it.next();
            Model.getUmlFactory().delete(vertex);
        }
    }

    /**
     * @param elem the UML element to be deleted
     */
    public void deleteEvent(MEvent elem) { }

    /**
     * @param elem the UML element to be deleted
     */
    public void deleteFinalState(MFinalState elem) { }

    /**
     * @param elem the UML element to be deleted
     */
    public void deleteGuard(MGuard elem) { }

    /**
     * @param elem the UML element to be deleted
     */
    public void deletePseudostate(MPseudostate elem) { }

    /**
     * @param elem the UML element to be deleted
     */
    public void deleteSignalEvent(MSignalEvent elem) { }

    /**
     * @param elem the UML element to be deleted
     */
    public void deleteSimpleState(MSimpleState elem) { }

    /**
     * @param elem the UML element to be deleted
     */
    public void deleteState(MState elem) { }

    /**
     * deletes its top state, which is a composite state (state vertex).
     *
     * @param elem the state machine to be removed.
     */
    public void deleteStateMachine(MStateMachine elem) {
        MState top = elem.getTop();
	if (top != null) {
	    Model.getUmlFactory().delete(top);
	}
    }

    /**
     * Deletes the outgoing and incoming transitions of a
     * statevertex.<p>
     *
     * @param elem the UML element to be deleted
     */
    public void deleteStateVertex(MStateVertex elem) {
        Collection col = elem.getIncomings();
        Iterator it = col.iterator();
        while (it.hasNext()) {
            Model.getUmlFactory().delete(it.next());
        }
        col = elem.getOutgoings();
        it = col.iterator();
        while (it.hasNext()) {
            Model.getUmlFactory().delete(it.next());
        }
    }

    /**
     * @param elem the UML element to be deleted
     */
    public void deleteStubState(MStubState elem) { }

    /**
     * @param elem the UML element to be deleted
     */
    public void deleteSubmachineState(MSubmachineState elem) { }

    /**
     * @param elem the UML element to be deleted
     */
    public void deleteSynchState(MSynchState elem) { }

    /**
     * @param elem the UML element to be deleted
     */
    public void deleteTimeEvent(MTimeEvent elem) { }

    /**
     * @param elem the UML element to be deleted
     */
    public void deleteTransition(MTransition elem) { }




}

