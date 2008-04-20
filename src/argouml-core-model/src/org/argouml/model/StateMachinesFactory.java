// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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


/**
 * The interface for the factory for StateMachines.
 */
public interface StateMachinesFactory extends Factory {
    /**
     * Create an empty but initialized instance of a UML CallEvent.
     *
     * @return an initialized UML CallEvent instance.
     */
    Object createCallEvent();

    /**
     * Create an empty but initialized instance of a UML ChangeEvent.
     *
     * @return an initialized UML ChangeEvent instance.
     */
    Object createChangeEvent();

    /**
     * Create an empty but initialized instance of a UML CompositeState.
     *
     * @return an initialized UML CompositeState instance.
     */
    Object createCompositeState();

    /**
     * Create an empty but initialized instance of a UML FinalState.
     *
     * @return an initialized UML FinalState instance.
     */
    Object createFinalState();

    /**
     * Create an empty but initialized instance of a UML Guard.
     *
     * @return an initialized UML Guard instance.
     */
    Object createGuard();

    /**
     * Create an empty but initialized instance of a UML Pseudostate.
     *
     * @return an initialized UML Pseudostate instance.
     */
    Object createPseudostate();

    /**
     * Create an empty but initialized instance of a UML SignalEvent.
     *
     * @return an initialized UML SignalEvent instance.
     */
    Object createSignalEvent();

    /**
     * Create an empty but initialized instance of a UML SimpleState.
     *
     * @return an initialized UML SimpleState instance.
     */
    Object createSimpleState();

    /**
     * Create an empty but initialized instance of a UML StateMachine.
     *
     * @return an initialized UML StateMachine instance.
     */
    Object createStateMachine();

    /**
     * Create an empty but initialized instance of a UML StubState.
     *
     * @return an initialized UML StubState instance.
     */
    Object createStubState();

    /**
     * Create an empty but initialized instance of a UML SubmachineState.
     *
     * @return an initialized UML SubmachineState instance.
     */
    Object createSubmachineState();

    /**
     * Create an empty but initialized instance of a UML SynchState.
     *
     * @return an initialized UML SynchState instance.
     */
    Object createSynchState();

    /**
     * Create an empty but initialized instance of a UML TimeEvent.
     *
     * @return an initialized UML TimeEvent instance.
     */
    Object createTimeEvent();

    /**
     * Create an empty but initialized instance of a UML Transition.
     *
     * @return an initialized UML Transition instance.
     */
    Object createTransition();

    /**
     * Builds a compositestate as top for some statemachine.<p>
     * TODO: Confusing name: this method should better
     * be named buildTopStateOnStateMachine.
     *
     * @param statemachine The given statemachine
     * @return MCompositeState The newly build top state
     * @see #buildCompositeState(Object)
     */
    Object buildCompositeStateOnStateMachine(Object statemachine);

    /**
     * Builds a state machine owned by the given context.
     *
     * @param oContext the given context
     * @return MStateMachine the newly build statemachine
     */
    Object buildStateMachine(Object oContext);

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
    Object buildTransition(Object owningState, Object source,
            Object dest);

    /**
     * Builds a pseudostate initialized as a choice pseudostate. The
     * pseudostate will be a subvertix of the given
     * compositestate. The parameter compositeState is of type Object
     * to decouple the factory and model implementation as much as
     * possible from the rest of ArgoUML.<p>
     *
     * @param compositeState the parent
     * @return MPseudostate
     */
    Object buildPseudoState(Object compositeState);

    /**
     * Builds a synchstate initalized with bound 0. The synchstate
     * will be a subvertix of the given compositestate. The parameter
     * compositeState is of type Object to decouple the factory and
     * model implementation as much as possible from the rest of ArgoUML.
     *
     * @param compositeState the given compositestate
     * @return MSynchState the newly created SynchState
     */
    Object buildSynchState(Object compositeState);

    /**
     * Builds a stubstate initalized with an empty referenced
     * state. The stubstate will be a subvertix of the given
     * compositestate. The parameter compositeState is of type Object
     * to decouple the factory and model implementation as much as
     * possible from the rest of ArgoUML.
     *
     * @param compositeState the given composite state
     * @return MSynchState the newly build stubstate
     */
    Object buildStubState(Object compositeState);

    /**
     * Build a CompositeState initalized as a non-concurrent
     * composite state.  The CompositeState will be a subvertex of the
     * given CompositeState.
     *
     * @param compositeState the given compositestate
     * @return the newly built CompositeState
     * @see #buildCompositeStateOnStateMachine(Object)
     */
    Object buildCompositeState(Object compositeState);

    /**
     * Builds a simplestate. The simplestate will be a subvertix of
     * the given compositestate.
     *
     * @param compositeState the given compositestate
     * @return MSimpleState the newly build simple state
     */
    Object buildSimpleState(Object compositeState);

    /**
     * Builds a finalstate. The finalstate will be a subvertix of the
     * given compositestate. The parameter compositeState is of type
     * Object to decouple the factory and model implementation as much
     * as possible from the rest of ArgoUML.
     *
     * @param compositeState the given compositestate
     * @return MFinalState the given compositestate
     */
    Object buildFinalState(Object compositeState);

    /**
     * Builds a submachinestate. The submachinestate will be a
     * subvertix of the given compositestate. The parameter
     * compositeState is of type Object to decouple the factory and
     * model implementation as much as possible.  from the rest of ArgoUML.
     *
     * @param compositeState the given compositestate
     * @return MSubmachineState the given submachinestate
     */
    Object buildSubmachineState(Object compositeState);

    /**
     * Builds an internal transition for a given state. The parameter state is
     * of type Object to decouple the factory and model implementation as
     * much as possible.
     *
     * @param state The state the internal transition should belong to
     * @return MTransition The internal transition constructed
     */
    Object buildInternalTransition(Object state);

    /**
     * Build a transition between a source state and a target state. The
     * parameters are of type Object to decouple the factory and model
     * implementation as much as possible.
     * This should not be used for internal transitions!
     * @param source The source state
     * @param target The target state
     * @return MTransition The resulting transition between source an state
     */
    Object buildTransition(Object source, Object target);

    /**
     * Builds a callevent with given namespace
     * (and therefore the ownership).
     *
     * @param ns the namespace
     * @return MCallEvent
     */
    Object buildCallEvent(Object ns);

    /**
     * Create a initialized instance of a CallEvent with a name
     * as a trigger for a Transition, within a given namespace.
     * If an operation with corresponding name can be found, it is linked.
     *
     * @param trans Object MTransition for which the CallEvent is a trigger
     * @param name String with the trigger name - should not include "()"
     * @param ns the namespace
     * @return an initialized UML CallEvent instance.
     */
    Object buildCallEvent(Object trans, String name, Object ns);

    /**
     * Builds a signalevent within a given namespace.
     *
     * @param ns the Namespace
     * @return MSignalEvent
     */
    Object buildSignalEvent(Object ns);

    /**
     * Builds a named signalevent within a given namespace.
     *
     * @param ns the Namespace
     * @param name String the name of the SignalEvent
     * @return MSignalEvent
     */
    Object buildSignalEvent(String name, Object ns);

    /**
     * Builds a timeevent within a given namespace.
     *
     * @param ns the Namespace
     * @return MTimeEvent
     */
    Object buildTimeEvent(Object ns);

    /**
     * Builds a timeevent within a given namespace.
     *
     * @param s String for creating the TimeExpression
     * @param ns the Namespace
     * @return MTimeEvent
     */
    Object buildTimeEvent(String s, Object ns);

    /**
     * Builds a changeevent within a given namespace.
     *
     * @param ns the Namespace
     * @return MChangeEvent
     */
    Object buildChangeEvent(Object ns);

    /**
     * Create a initialized instance of a ChangeEvent,
     * within a given namespace.
     *
     * @param ns the Namespace
     * @param s String for creating the BooleanExpression
     * @return MChangeEvent
     */
    Object buildChangeEvent(String s, Object ns);

    /**
     * Builds a guard condition with a given transition. The guard condition is
     * empty by default. The parameter is of type Object to decouple the factory
     * and model implementation as much as possible.
     *
     * @param transition The transition that owns the resulting guard condition
     * @return MGuard The resulting guard condition
     */
    Object buildGuard(Object transition);
}
