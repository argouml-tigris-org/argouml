// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

package org.argouml.model;

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

/**
 * The interface for the factory for StateMachines.<p>
 *
 * Created from the old StateMachinesFactory.
 */
public interface StateMachinesFactory {
    /**
     * Create an empty but initialized instance of a UML CallEvent.
     *
     * @return an initialized UML CallEvent instance.
     */
    MCallEvent createCallEvent();

    /**
     * Create an empty but initialized instance of a UML ChangeEvent.
     *
     * @return an initialized UML ChangeEvent instance.
     */
    MChangeEvent createChangeEvent();

    /**
     * Create an empty but initialized instance of a UML CompositeState.
     *
     * @return an initialized UML CompositeState instance.
     */
    MCompositeState createCompositeState();

    /**
     * Create an empty but initialized instance of a UML FinalState.
     *
     * @return an initialized UML FinalState instance.
     */
    MFinalState createFinalState();

    /**
     * Create an empty but initialized instance of a UML Guard.
     *
     * @return an initialized UML Guard instance.
     */
    Object/*MGuard*/createGuard();

    /**
     * Create an empty but initialized instance of a UML Pseudostate.
     *
     * @return an initialized UML Pseudostate instance.
     */
    MPseudostate createPseudostate();

    /**
     * Create an empty but initialized instance of a UML SignalEvent.
     *
     * @return an initialized UML SignalEvent instance.
     */
    MSignalEvent createSignalEvent();

    /**
     * Create an empty but initialized instance of a UML SimpleState.
     *
     * @return an initialized UML SimpleState instance.
     */
    MSimpleState createSimpleState();

    /**
     * Create an empty but initialized instance of a UML State.
     *
     * @return an initialized UML State instance.
     */
    MState createState();

    /**
     * Create an empty but initialized instance of a UML StateMachine.
     *
     * @return an initialized UML StateMachine instance.
     */
    MStateMachine createStateMachine();

    /**
     * Create an empty but initialized instance of a UML StubState.
     *
     * @return an initialized UML StubState instance.
     */
    MStubState createStubState();

    /**
     * Create an empty but initialized instance of a UML SubmachineState.
     *
     * @return an initialized UML SubmachineState instance.
     */
    MSubmachineState createSubmachineState();

    /**
     * Create an empty but initialized instance of a UML SynchState.
     *
     * @return an initialized UML SynchState instance.
     */
    MSynchState createSynchState();

    /**
     * Create an empty but initialized instance of a UML TimeEvent.
     *
     * @return an initialized UML TimeEvent instance.
     */
    MTimeEvent createTimeEvent();

    /**
     * Create an empty but initialized instance of a UML Transition.
     *
     * @return an initialized UML Transition instance.
     */
    Object createTransition();

    /**
     * Builds a compositestate as top for some statemachine.<p>
     *
     * @param statemachine the given statemachine
     * @return MCompositeState the newly build top state
     * @see #buildCompositeState(Object)
     */
    MCompositeState buildCompositeState(MStateMachine statemachine);

    /**
     * Builds a state machine owned by the given context.
     *
     * @param oContext the given context
     * @return MStateMachine the newly build statemachine
     */
    MStateMachine buildStateMachine(Object oContext);

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
    Object buildTransition(MCompositeState owningState, MStateVertex source,
            MStateVertex dest);

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
    MPseudostate buildPseudoState(Object compositeState);

    /**
     * Builds a synchstate initalized with bound 0. The synchstate
     * will be a subvertix of the given compositestate. The parameter
     * compositeState is of type Object to decouple the factory and
     * NSUML as much as possible from the rest of ArgoUML.
     *
     * @param compositeState the given compositestate
     * @return MSynchState the newly created SynchState
     */
    MSynchState buildSynchState(Object compositeState);

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
    MStubState buildStubState(Object compositeState);

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
    MCompositeState buildCompositeState(Object compositeState);

    /**
     * Builds a simplestate. The simplestate will be a subvertix of
     * the given compositestate. The parameter compositeState is of
     * type Object to decouple the factory and NSUML as much as
     * possible.  from the rest of ArgoUML.
     *
     * @param compositeState the given compositestate
     * @return MSimpleState the newly build simple state
     */
    MSimpleState buildSimpleState(Object compositeState);

    /**
     * Builds a finalstate. The finalstate will be a subvertix of the
     * given compositestate. The parameter compositeState is of type
     * Object to decouple the factory and NSUML as much as possible.
     * from the rest of ArgoUML.
     *
     * @param compositeState the given compositestate
     * @return MFinalState the given compositestate
     */
    MFinalState buildFinalState(Object compositeState);

    /**
     * Builds a submachinestate. The submachinestate will be a
     * subvertix of the given compositestate. The parameter
     * compositeState is of type Object to decouple the factory and
     * NSUML as much as possible.  from the rest of ArgoUML.
     *
     * @param compositeState the given compositestate
     * @return MSubmachineState the given submachinestate
     */
    MSubmachineState buildSubmachineState(Object compositeState);

    /**
     * Builds an internal transition for a given state. The parameter state is
     * of type Object to decouple the factory and NSUML as much as possible.
     *
     * @param state The state the internal transition should belong to
     * @return MTransition The internal transition constructed
     */
    MTransition buildInternalTransition(Object state);

    /**
     * Build a transition between a source state and a target state. The
     * parameters are of type Object to decouple the factory and NSUML as much
     * as possible.
     * This should not be used for internal transitions!
     * @param source The source state
     * @param target The target state
     * @return MTransition The resulting transition between source an state
     */
    MTransition buildTransition(Object source, Object target);

    /**
     * Builds a callevent whose namespace (and therefore the ownership) is the
     * rootmodel.
     * @param model the model
     * @return MCallEvent
     */
    MCallEvent buildCallEvent(Object model);

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
    MCallEvent buildCallEvent(Object trans, String name, Object model);

    /**
     * Builds a signalevent whose namespace (and therefore the
     * ownership) is the rootmodel.<p>
     * @param model the model
     * @return MSignalEvent
     */
    MSignalEvent buildSignalEvent(Object model);

    /**
     * Builds a named signalevent whose namespace (and therefore the
     * ownership) is the rootmodel.<p>
     * @param model the model
     * @param name String the name of the SignalEvent
     * @return MSignalEvent
     */
    MSignalEvent buildSignalEvent(String name, Object model);

    /**
     * Builds a timeevent whose namespace (and therefore the
     * ownership) is the rootmodel.<p>
     * @param model the Model
     * @return MTimeEvent
     */
    MTimeEvent buildTimeEvent(Object model);

    /**
     * Builds a timeevent whose namespace (and therefore the
     * ownership) is the rootmodel.<p>
     *
     * @param s String for creating the TimeExpression
     * @param model the model
     * @return MTimeEvent
     */
    MTimeEvent buildTimeEvent(String s, Object model);

    /**
     * Builds a changeevent whose namespace (and therefore the
     * ownership) is the rootmodel.<p>
     * @param model the model
     * @return MChangeEvent
     */
    MChangeEvent buildChangeEvent(Object model);

    /**
     * Builds a changeevent whose namespace (and therefore the
     * ownership) is the rootmodel.<p>
     * @param model the model
     * @param s String for creating the BooleanExpression
     * @return MChangeEvent
     */
    MChangeEvent buildChangeEvent(String s, Object model);

    /**
     * Builds a guard condition with a given transition. The guard condition is
     * empty by default. The parameter is of type Object to decouple the factory
     * and NSUML as much as possible.
     * @param transition The transition that owns the resulting guard condition
     * @return MGuard The resulting guard condition
     */
    Object/*MGuard*/buildGuard(Object transition);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteCallEvent(MCallEvent elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteChangeEvent(MChangeEvent elem);

    /**
     * Deletes any associated subVertices.
     *
     * @param elem the UML element to be deleted
     */
    void deleteCompositeState(MCompositeState elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteEvent(MEvent elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteFinalState(MFinalState elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteGuard(MGuard elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deletePseudostate(MPseudostate elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteSignalEvent(MSignalEvent elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteSimpleState(MSimpleState elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteState(MState elem);

    /**
     * deletes its top state, which is a composite state (state vertex).
     *
     * @param elem the state machine to be removed.
     */
    void deleteStateMachine(MStateMachine elem);

    /**
     * Deletes the outgoing and incoming transitions of a
     * statevertex.<p>
     *
     * @param elem the UML element to be deleted
     */
    void deleteStateVertex(MStateVertex elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteStubState(MStubState elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteSubmachineState(MSubmachineState elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteSynchState(MSynchState elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteTimeEvent(MTimeEvent elem);

    /**
     * @param elem the UML element to be deleted
     */
    void deleteTransition(MTransition elem);
}
