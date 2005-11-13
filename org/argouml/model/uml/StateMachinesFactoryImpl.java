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
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;

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
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    StateMachinesFactoryImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * Create an empty but initialized instance of a UML CallEvent.
     *
     * @return an initialized UML CallEvent instance.
     */
    public Object createCallEvent() {
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
    public Object createChangeEvent() {
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
    public Object createCompositeState() {
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
    public Object createFinalState() {
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
    public Object createPseudostate() {
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
    public Object createSignalEvent() {
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
    public Object createSimpleState() {
        MSimpleState modelElement =
	    MFactory.getDefaultFactory().createSimpleState();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML StateMachine.
     *
     * @return an initialized UML StateMachine instance.
     */
    public Object createStateMachine() {
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
    public Object createStubState() {
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
    public Object createSubmachineState() {
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
    public Object createSynchState() {
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
    public Object createTimeEvent() {
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
    public Object buildCompositeStateOnStateMachine(Object statemachine) {
    	if (statemachine instanceof MStateMachine) {
            MCompositeState state = (MCompositeState) createCompositeState();
            state.setStateMachine((MStateMachine) statemachine);
            state.setName("top");
            return state;
    	}
    	throw new IllegalArgumentException("statemachine");
    }

    /**
     * Builds a state machine owned by the given context.
     *
     * @param oContext the given context
     * @return MStateMachine the newly build statemachine
     */
    public Object buildStateMachine(Object oContext) {
    	if (oContext != null
        	    && (nsmodel.getStateMachinesHelper()
        		.isAddingStatemachineAllowed(oContext))) {

    	    MStateMachine machine = (MStateMachine) createStateMachine();
            MModelElement context = (MModelElement) oContext;
    	    machine.setContext(context);
    	    if (context instanceof MClassifier) {
    	        machine.setNamespace((MClassifier) context);
    	    } else if (context instanceof MBehavioralFeature) {
    	        MBehavioralFeature feature = (MBehavioralFeature) context;
    	        machine.setNamespace(feature.getOwner());
    	    }
    	    nsmodel.getStateMachinesFactory()
    	    	.buildCompositeStateOnStateMachine(machine);
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
     * @param source the source of the transition (a StateVertex)
     * @param dest the destination of the transition (a StateVertex)
     * @return The newly build Transition.
     */
    public Object buildTransition(Object owningState,
				  Object source, Object dest) {
        if (!(owningState instanceof MCompositeState)) {
            throw new IllegalArgumentException("owningState");
        }
        if (!(source instanceof MStateVertex)) {
            throw new IllegalArgumentException("source");
        }
        if (!(dest instanceof MStateVertex)) {
            throw new IllegalArgumentException("dest");
        }

        MCompositeState compositeState = (MCompositeState) owningState;
        if (compositeState.getSubvertices().contains(source)
                && compositeState.getSubvertices().contains(dest)) {
    	    MTransition trans = (MTransition) createTransition();
    	    compositeState.addInternalTransition(trans);
    	    trans.setSource((MStateVertex) source);
    	    trans.setTarget((MStateVertex) dest);
    	    return trans;

      	}
        throw new IllegalArgumentException("In buildTransition: "
                + "arguments not legal");
    }

    /**
     * Builds a pseudostate initialized as a choice pseudostate. The
     * pseudostate will be a subvertex of the given
     * compositestate. The parameter compositeState is of type Object
     * to decouple the factory and NSUML as much as possible from the
     * rest of ArgoUML.<p>
     *
     * @param compositeState the parent
     * @return MPseudostate
     */
    public Object buildPseudoState(Object compositeState) {
        if (compositeState instanceof MCompositeState) {
            MPseudostate state = (MPseudostate) createPseudostate();

	    // equivalent to CHOICE in UML 1.4
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
    public Object buildSynchState(Object compositeState) {
        if (compositeState instanceof MCompositeState) {
            MSynchState state = (MSynchState) createSynchState();
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
    public Object buildStubState(Object compositeState) {
        if (compositeState instanceof MCompositeState) {
            MStubState state = (MStubState) createStubState();
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
     * @see #buildCompositeStateOnStateMachine(Object)
     */
    public Object buildCompositeState(Object compositeState) {
        if (compositeState instanceof MCompositeState) {
            MCompositeState state = (MCompositeState) createCompositeState();
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
    public Object buildSimpleState(Object compositeState) {
        if (compositeState instanceof MCompositeState) {
            MSimpleState state = (MSimpleState) createSimpleState();
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
    public Object buildFinalState(Object compositeState) {
        if (compositeState instanceof MCompositeState) {
            MFinalState state = (MFinalState) createFinalState();
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
    public Object buildSubmachineState(Object compositeState) {
        if (compositeState instanceof MCompositeState) {
            MSubmachineState state = (MSubmachineState) createSubmachineState();
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
    public Object buildInternalTransition(Object state) {
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
     * Build a transition between a source state and a target state.
     *
     * This should not be used for internal transitions!
     *
     * @param source The source state
     * @param target The target state
     * @return MTransition The resulting transition between source an state
     */
    public Object buildTransition(Object source, Object target) {
        if (source instanceof MStateVertex && target instanceof MStateVertex) {
            MTransition trans = (MTransition) createTransition();
            trans.setSource((MStateVertex) source);
            trans.setTarget((MStateVertex) target);
            trans.setStateMachine((MStateMachine) nsmodel
                    .getStateMachinesHelper().getStateMachine(source));
            return trans;
        }
        return null;
    }

    /**
     * Builds a callevent with given namespace
     * (and therefore the ownership).
     *
     * @param ns the namespace
     * @return MCallEvent
     */
    public Object buildCallEvent(Object ns) {
        MCallEvent event = (MCallEvent) createCallEvent();
        event.setNamespace((MNamespace) ns);
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
     * @param ns the namespace
     * @return an initialized UML CallEvent instance.
     */
    public Object buildCallEvent(Object trans, String name, Object ns) {
        if (!(trans instanceof MTransition)) {
            throw new IllegalArgumentException();
        }
        MCallEvent evt = MFactory.getDefaultFactory().createCallEvent();
        evt.setNamespace((MNamespace) ns);

        String operationName =
            (name.indexOf("(") > 0)
            ? name.substring(0, name.indexOf("(")).trim()
            : name.trim();
        nsmodel.getCoreHelper().setName(evt, operationName);
        Object op =
            nsmodel.getStateMachinesHelper()
            	.findOperationByName(trans, operationName);
        if (op != null) {
            nsmodel.getCommonBehaviorHelper().setOperation(evt, op);
        }
        return evt;
    }

    /**
     * Builds a signalevent whose namespace (and therefore the
     * ownership) is given.
     *
     * @param ns the Namespace
     * @return MSignalEvent
     */
    public Object buildSignalEvent(Object ns) {
        MSignalEvent event = (MSignalEvent) createSignalEvent();
        event.setNamespace((MNamespace) ns);
        event.setName("");
        return event;
    }

    /**
     * Builds a named signalevent whose namespace (and therefore the
     * ownership) is given.
     *
     * @param ns the Namespace
     * @param name String the name of the SignalEvent
     * @return MSignalEvent
     */
    public Object buildSignalEvent(String name, Object ns) {
        MSignalEvent event = (MSignalEvent) createSignalEvent();
        event.setNamespace((MNamespace) ns);
        event.setName(name);
        return event;
    }

    /**
     * Builds a timeevent whose namespace (and therefore the
     * ownership) is given.
     *
     * @param ns the Namespace
     * @return MTimeEvent
     */
    public Object buildTimeEvent(Object ns) {
        MTimeEvent event = (MTimeEvent) createTimeEvent();
        event.setNamespace((MNamespace) ns);
        event.setName("");
        return event;
    }

    /**
     * Builds a timeevent whose namespace (and therefore the
     * ownership) is given.
     *
     * @param s String for creating the TimeExpression
     * @param ns the Namespace
     * @return MTimeEvent
     */
    public Object buildTimeEvent(String s, Object ns) {
        MTimeEvent event = (MTimeEvent) createTimeEvent();
        event.setNamespace((MNamespace) ns);
        event.setName("");
        Object te =
            nsmodel.getDataTypesFactory()
            	.createTimeExpression("", s);
        nsmodel.getStateMachinesHelper().setWhen(event, te);
        return event;
    }

    /**
     * Builds a changeevent whose namespace (and therefore the
     * ownership) is given.
     *
     * @param ns the Namespace
     * @return MChangeEvent
     */
    public Object buildChangeEvent(Object ns) {
        MChangeEvent event = (MChangeEvent) createChangeEvent();
        event.setNamespace((MNamespace) ns);
        event.setName("");
        return event;
    }

    /**
     * Builds a changeevent whose namespace (and therefore the
     * ownership) is given.
     *
     * @param ns the Namespace
     * @param s String for creating the BooleanExpression
     * @return MChangeEvent
     */
    public Object buildChangeEvent(String s, Object ns) {
        MChangeEvent event = (MChangeEvent) createChangeEvent();
        event.setNamespace((MNamespace) ns);
        event.setName("");
        Object ce =
            nsmodel.getDataTypesFactory()
            	.createBooleanExpression("", s);
        nsmodel.getStateMachinesHelper().setExpression(event, ce);

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
            nsmodel.getCommonBehaviorHelper().setTransition(guard, transition);
            return guard;
        }
        return null;
    }

    /**
     * @param elem the UML element to be deleted
     */
    void deleteCallEvent(Object elem) {
        if (!(elem instanceof MCallEvent)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the UML element to be deleted
     */
    void deleteChangeEvent(Object elem) {
        if (!(elem instanceof MChangeEvent)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Deletes any associated subVertices.
     *
     * @param elem the UML element to be deleted
     */
    void deleteCompositeState(Object elem) {
        if (!(elem instanceof MCompositeState)) {
            throw new IllegalArgumentException();
        }

        Collection vertices = ((MCompositeState) elem).getSubvertices();
        Iterator it = vertices.iterator();
        while (it.hasNext()) {
            MStateVertex vertex = (MStateVertex) it.next();
            nsmodel.getUmlFactory().delete(vertex);
        }
    }

    /**
     * @param elem the UML element to be deleted
     */
    void deleteEvent(Object elem) {
        if (!(elem instanceof MEvent)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the UML element to be deleted
     */
    void deleteFinalState(Object elem) {
        if (!(elem instanceof MFinalState)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the UML element to be deleted
     */
    void deleteGuard(Object elem) {
        if (!(elem instanceof MGuard)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the UML element to be deleted
     */
    void deletePseudostate(Object elem) {
        if (!(elem instanceof MPseudostate)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the UML element to be deleted
     */
    void deleteSignalEvent(Object elem) {
        if (!(elem instanceof MSignalEvent)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the UML element to be deleted
     */
    void deleteSimpleState(Object elem) {
        if (!(elem instanceof MSimpleState)) {
            throw new IllegalArgumentException();
        }


    }

    /**
     * @param elem the UML element to be deleted
     */
    void deleteState(Object elem) {
        if (!(elem instanceof MState)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * deletes its top state, which is a composite state (state vertex).
     *
     * @param elem the state machine to be removed.
     */
    void deleteStateMachine(Object elem) {
        if (!(elem instanceof MStateMachine)) {
            throw new IllegalArgumentException();
        }

        MState top = ((MStateMachine) elem).getTop();
	if (top != null) {
	    nsmodel.getUmlFactory().delete(top);
	}
    }

    /**
     * Deletes the outgoing and incoming transitions of a
     * statevertex.<p>
     *
     * @param elem the UML element to be deleted
     */
    void deleteStateVertex(Object elem) {
        if (!(elem instanceof MStateVertex)) {
            throw new IllegalArgumentException();
        }

        Collection col = ((MStateVertex) elem).getIncomings();
        Iterator it = col.iterator();
        while (it.hasNext()) {
            nsmodel.getUmlFactory().delete(it.next());
        }
        col = ((MStateVertex) elem).getOutgoings();
        it = col.iterator();
        while (it.hasNext()) {
            nsmodel.getUmlFactory().delete(it.next());
        }
    }

    /**
     * @param elem the UML element to be deleted
     */
    void deleteStubState(Object elem) {
        if (!(elem instanceof MStubState)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the UML element to be deleted
     */
    void deleteSubmachineState(Object elem) {
        if (!(elem instanceof MSubmachineState)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the UML element to be deleted
     */
    void deleteSynchState(Object elem) {
        if (!(elem instanceof MSynchState)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the UML element to be deleted
     */
    void deleteTimeEvent(Object elem) {
        if (!(elem instanceof MTimeEvent)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the UML element to be deleted
     */
    void deleteTransition(Object elem) {
        if (!(elem instanceof MTransition)) {
            throw new IllegalArgumentException();
        }

    }




}

