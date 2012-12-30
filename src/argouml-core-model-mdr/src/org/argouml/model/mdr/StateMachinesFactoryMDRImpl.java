/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.StateMachinesFactory;
import org.omg.uml.behavioralelements.statemachines.CallEvent;
import org.omg.uml.behavioralelements.statemachines.ChangeEvent;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.Event;
import org.omg.uml.behavioralelements.statemachines.FinalState;
import org.omg.uml.behavioralelements.statemachines.Guard;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.SignalEvent;
import org.omg.uml.behavioralelements.statemachines.SimpleState;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.StateMachinesPackage;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.behavioralelements.statemachines.StubState;
import org.omg.uml.behavioralelements.statemachines.SubmachineState;
import org.omg.uml.behavioralelements.statemachines.SynchState;
import org.omg.uml.behavioralelements.statemachines.TimeEvent;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.foundation.core.BehavioralFeature;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Operation;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.foundation.datatypes.BooleanExpression;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;
import org.omg.uml.foundation.datatypes.TimeExpression;

/**
 * Factory to create UML classes for the UML BehaviorialElements::StateMachines
 * package.<p>
 *
 * Abstract metatypes from the UML metamodel do not have create methods.<p>
 *
 * @since ARGO0.19.3
 * @author Bob Tarling
 * @author Ludovic Ma&icirc;tre
 * @author Tom Morris
 */
class StateMachinesFactoryMDRImpl extends AbstractUmlModelFactoryMDR
        implements StateMachinesFactory {

    /**
     * Logger
     */
    private static final Logger LOG =
        Logger.getLogger(StateMachinesFactoryMDRImpl.class.getName());

    /**
     * The model implementation.
     */
    private MDRModelImplementation modelImpl;

    /**
     * Package-private constructor.
     *
     * @param implementation
     *            To get other helpers and factories.
     */
    StateMachinesFactoryMDRImpl(MDRModelImplementation implementation) {
        modelImpl = implementation;
    }

    private StateMachinesPackage getSmPackage() {
        return modelImpl.getUmlPackage().getStateMachines();
    }


    public CallEvent createCallEvent() {
        CallEvent myCallEvent = getSmPackage().getCallEvent().createCallEvent();
        super.initialize(myCallEvent);
        return myCallEvent;
    }


    public ChangeEvent createChangeEvent() {
        ChangeEvent myChangeEvent = getSmPackage().getChangeEvent()
                .createChangeEvent();
        super.initialize(myChangeEvent);
        return myChangeEvent;
    }


    public CompositeState createCompositeState() {
        CompositeState myCompositeState = getSmPackage().getCompositeState()
                .createCompositeState();
        super.initialize(myCompositeState);
        return myCompositeState;
    }


    public FinalState createFinalState() {
        FinalState myFinalState = getSmPackage().getFinalState().createFinalState();
        super.initialize(myFinalState);
        return myFinalState;
    }


    public Guard createGuard() {
        Guard myGuard = getSmPackage().getGuard().createGuard();
        super.initialize(myGuard);
        return myGuard;
    }


    public Pseudostate createPseudostate() {
        Pseudostate myPseudostate = getSmPackage().getPseudostate()
                .createPseudostate();
        super.initialize(myPseudostate);
        return myPseudostate;
    }


    public SignalEvent createSignalEvent() {
        SignalEvent mySignalEvent = getSmPackage().getSignalEvent()
                .createSignalEvent();
        super.initialize(mySignalEvent);
        return mySignalEvent;
    }


    public SimpleState createSimpleState() {
        SimpleState mySimpleState = getSmPackage().getSimpleState()
                .createSimpleState();
        super.initialize(mySimpleState);
        return mySimpleState;
    }


    public StateMachine createStateMachine() {
        StateMachine myStateMachine = getSmPackage().getStateMachine()
                .createStateMachine();
        super.initialize(myStateMachine);
        return myStateMachine;
    }


    public StubState createStubState() {
        StubState myStubState = getSmPackage().getStubState().createStubState();
        super.initialize(myStubState);
        return myStubState;
    }


    public SubmachineState createSubmachineState() {
        SubmachineState mySubmachineState = getSmPackage().getSubmachineState()
                .createSubmachineState();
        super.initialize(mySubmachineState);
        return mySubmachineState;
    }


    public SynchState createSynchState() {
        SynchState mySynchState = getSmPackage().getSynchState().createSynchState();
        super.initialize(mySynchState);
        return mySynchState;
    }


    public TimeEvent createTimeEvent() {
        TimeEvent myTimeEvent = getSmPackage().getTimeEvent().createTimeEvent();
        super.initialize(myTimeEvent);
        return myTimeEvent;
    }


    public Transition createTransition() {
        Transition myTransition = getSmPackage().getTransition().createTransition();
        super.initialize(myTransition);
        return myTransition;
    }


    public CompositeState buildCompositeStateOnStateMachine(
            Object statemachine) {
        if (statemachine instanceof StateMachine) {
            StateMachine sm = (StateMachine) statemachine;
            CompositeState top = createCompositeState();
            top.setStateMachine(sm);
            top.setName("top");
            sm.setTop(top);
            assert top.equals(sm.getTop());
            return top;
        }
        throw new IllegalArgumentException("statemachine");
    }


    public StateMachine buildStateMachine(Object oContext) {
        if (oContext != null
                && (modelImpl.getStateMachinesHelper().
                        isAddingStatemachineAllowed(oContext))) {

            StateMachine machine = createStateMachine();
            ModelElement modelelement = (ModelElement) oContext;
            machine.setContext(modelelement);

            if (modelelement instanceof BehavioralFeature) {
                modelelement = ((BehavioralFeature) modelelement).getOwner();
            }
            if (modelelement instanceof Namespace) {
                Namespace namespace = (Namespace) modelelement;
                /* Follow well-formedness rule for a Class [2].
                 * See issue 4282. Do not use a class
                 * as the namespace for a statemachine: */
                while (namespace instanceof UmlClass) {
                    Namespace pns = namespace.getNamespace();
                    if (pns == null) break;
                    namespace = pns;
                }
                machine.setNamespace(namespace);
            }

            State top = buildCompositeStateOnStateMachine(machine);
            assert top.equals(machine.getTop());
            return machine;
        }
        throw new IllegalArgumentException("In buildStateMachine: "
                + "context null or not legal");
    }


    public Transition buildTransition(Object owningState, Object source,
            Object dest) {
        if (!(owningState instanceof CompositeState)) {
            throw new IllegalArgumentException("owningState");
        }
        if (!(source instanceof StateVertex)) {
            throw new IllegalArgumentException("source");
        }
        if (!(dest instanceof StateVertex)) {
            throw new IllegalArgumentException("dest");
        }

        CompositeState compositeState = (CompositeState) owningState;
        if (compositeState.getSubvertex().contains(source)
                && compositeState.getSubvertex().contains(dest)) {
    	    Transition trans = createTransition();
    	    compositeState.getInternalTransition().add(trans);
    	    trans.setSource((StateVertex) source);
    	    trans.setTarget((StateVertex) dest);
    	    return trans;

      	}
        throw new IllegalArgumentException("In buildTransition: "
                + "arguments not legal");
    }


    public Pseudostate buildPseudoState(Object compositeState) {
        if (compositeState instanceof CompositeState) {
            Pseudostate state = createPseudostate();
            state.setKind(PseudostateKindEnum.PK_CHOICE);
            state.setContainer((CompositeState) compositeState);
            ((CompositeState) compositeState).getSubvertex().add(state);
            return state;
        }
        throw new IllegalArgumentException(
                "Argument must be a CompositeState");
    }


    public SynchState buildSynchState(Object compositeState) {
        if (compositeState instanceof CompositeState) {
            SynchState state = createSynchState();
            state.setBound(0);
            state.setContainer((CompositeState) compositeState);
            return state;
        }
        throw new IllegalArgumentException(
                "Argument must be a CompositeState");
    }


    public StubState buildStubState(Object compositeState) {
        if (compositeState instanceof CompositeState) {
            StubState state = createStubState();
            state.setReferenceState("");
            state.setContainer((CompositeState) compositeState);
            return state;
        }
        throw new IllegalArgumentException(
                "Argument must be a CompositeState");
    }


    public CompositeState buildCompositeState(Object compositeState) {
        if (compositeState instanceof CompositeState) {
            CompositeState state = createCompositeState();
            state.setConcurrent(false);
            state.setContainer((CompositeState) compositeState);
            return state;
        }
        throw new IllegalArgumentException(
                "Argument must be a CompositeState");
    }


    public SimpleState buildSimpleState(Object compositeState) {
        if (compositeState instanceof CompositeState) {
            SimpleState state = createSimpleState();
            state.setContainer((CompositeState) compositeState);
            return state;
        }
        throw new IllegalArgumentException(
                "Argument must be a CompositeState");
    }


    public FinalState buildFinalState(Object compositeState) {
        if (compositeState instanceof CompositeState) {
            FinalState state = createFinalState();
            state.setContainer((CompositeState) compositeState);
            return state;
        }
        throw new IllegalArgumentException(
                "Argument must be a CompositeState");
    }


    public SubmachineState buildSubmachineState(Object compositeState) {
        if (compositeState instanceof CompositeState) {
            SubmachineState state = createSubmachineState();
            state.setContainer((CompositeState) compositeState);
            return state;
        }
        throw new IllegalArgumentException(
                "Argument must be a CompositeState");
    }


    public Transition buildInternalTransition(Object state) {
        if (state instanceof State) {
            Transition trans = createTransition();
            ((State) state).getInternalTransition().add(trans);
            trans.setSource((State) state);
            trans.setTarget((State) state);
            return trans;
        }
        throw new IllegalArgumentException("Argument must be a State");
    }


    public Transition buildTransition(Object source, Object target) {
        if (source instanceof StateVertex && target instanceof StateVertex) {
            Transition trans = createTransition();
            trans.setSource((StateVertex) source);
            trans.setTarget((StateVertex) target);
            trans.setStateMachine((StateMachine) modelImpl.
                    getStateMachinesHelper().getStateMachine(source));
            return trans;
        }
        throw new IllegalArgumentException();
    }


    public CallEvent buildCallEvent(Object ns) {
        CallEvent event = createCallEvent();
        event.setNamespace((Namespace) ns);
        event.setName("");
        return event;
    }


    public CallEvent buildCallEvent(Object trans, String name, Object ns) {
        if (!(trans instanceof Transition)) {
            throw new IllegalArgumentException();
        }
        CallEvent evt = createCallEvent();
        evt.setNamespace((Namespace) ns);

        String operationName = (name.indexOf("(") > 0) ? name.substring(0,
                name.indexOf("(")).trim() : name.trim();
        evt.setName(operationName);
        Object op = modelImpl.getStateMachinesHelper().findOperationByName(
                trans, operationName);
        if (op != null) {
            evt.setOperation((Operation) op);
        }
        return evt;
    }


    public SignalEvent buildSignalEvent(Object ns) {
        SignalEvent event = createSignalEvent();
        event.setNamespace((Namespace) ns);
        event.setName("");
        return event;
    }


    public SignalEvent buildSignalEvent(String name, Object ns) {
        SignalEvent event = createSignalEvent();
        event.setNamespace((Namespace) ns);
        event.setName(name);
        return event;
    }


    public TimeEvent buildTimeEvent(Object ns) {
        TimeEvent event = createTimeEvent();
        event.setNamespace((Namespace) ns);
        event.setName("");
        return event;
    }


    public TimeEvent buildTimeEvent(String s, Object ns) {
        TimeEvent event = createTimeEvent();
        event.setNamespace((Namespace) ns);
        event.setName("");
        Object te = modelImpl.getDataTypesFactory().createTimeExpression("", s);
        event.setWhen((TimeExpression) te);
        return event;
    }


    public ChangeEvent buildChangeEvent(Object ns) {
        ChangeEvent event =  createChangeEvent();
        event.setNamespace((Namespace) ns);
        event.setName("");
        return event;
    }


    public ChangeEvent buildChangeEvent(String expression, Object ns) {
        ChangeEvent event = buildChangeEvent(ns);
        Object ce = modelImpl.getDataTypesFactory()
                .createBooleanExpression("", expression);
        event.setChangeExpression((BooleanExpression) ce);
        return event;
    }


    public Guard buildGuard(Object transition) {
        if (transition instanceof Transition) {
            Transition t = (Transition) transition;
            if (t.getGuard() != null) {
                LOG.log(Level.WARNING, "Replacing Guard " + t.getGuard().getName()
                        + " on Transition " + t.getName());
            }
            Guard guard = createGuard();
            guard.setTransition((Transition) transition);
            return guard;
        }
        throw new IllegalArgumentException("transition: " + transition);
    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteCallEvent(Object elem) {
        if (!(elem instanceof CallEvent)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteChangeEvent(Object elem) {
        if (!(elem instanceof ChangeEvent)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Deletes any associated subVertices.
     *
     * This also enforces the following well-formedness rule.
     * <p>Well formedness rule 4.12.3.1 CompositeState
     * [4] There have to be at least two composite substates in a
     * concurrent composite state.<p>
     * If this is broken by deletion of substate then we delete the other
     * remaining substate and convert the composite state to non-concurrent
     *
     * @param elem
     *            the UML element to be deleted
     */
    void deleteCompositeState(Object elem) {
        if (!(elem instanceof CompositeState)) {
            throw new IllegalArgumentException();
        }

        final CompositeState compositeState = (CompositeState) elem;
        for (StateVertex vertex : compositeState.getSubvertex()) {
            modelImpl.getUmlFactory().delete(vertex);
        }

        final CompositeState containingCompositeState =
            compositeState.getContainer();

        // Well formedness rule 4.12.3.1 CompositeState
        // [4] There have to be at least two composite substates in a
        // concurrent composite state.
        // If this is broken by deletion of substate then we delete the other
        // remaining substates.
        if (containingCompositeState != null
                && containingCompositeState.isConcurrent()) {
            final Collection<StateVertex> siblings =
                containingCompositeState.getSubvertex();

            final int substatesRemaining = siblings.size();
            if (substatesRemaining == 2) {
                for (StateVertex sibling : siblings) {
                    if (sibling != compositeState) {
                        modelImpl.getUmlFactory().delete(sibling);
                    }
                }
            }
        }
    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteEvent(Object elem) {
        if (!(elem instanceof Event)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteFinalState(Object elem) {
        if (!(elem instanceof FinalState)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteGuard(Object elem) {
        if (!(elem instanceof Guard)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deletePseudostate(Object elem) {
        if (!(elem instanceof Pseudostate)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteSignalEvent(Object elem) {
        if (!(elem instanceof SignalEvent)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteState(Object elem) {
        if (!(elem instanceof State)) {
            throw new IllegalArgumentException();
        }
        State state = (State) elem;
        deleteNonNull(state.getDoActivity());
        deleteNonNull(state.getEntry());
        deleteNonNull(state.getExit());
        modelImpl.getUmlHelper().deleteCollection(
                state.getInternalTransition());

    }

    private void deleteNonNull(ModelElement action) {
        if (action != null) {
            modelImpl.getUmlFactory().delete(action);
        }
    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteSimpleState(Object elem) {
        if (!(elem instanceof SimpleState)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * deletes its top state, which is a composite state (state vertex).
     * Delete any submachine states which depend on this StateMachine.
     *
     * @param elem
     *            the state machine to be removed.
     */
    void deleteStateMachine(Object elem) {
        if (!(elem instanceof StateMachine)) {
            throw new IllegalArgumentException();
        }
        StateMachine stateMachine = (StateMachine) elem;

        // This shouldn't be required since it's a composite, but there's
        // a bug in the version of MDR that we use (20050711) that causes
        // it to fail to delete aggregate elements which are single valued
        // and where the aggregate end is listed second in the association
        // defined in the metamodel. - tfm 20080713
        State top = stateMachine.getTop();
        if (top != null) {
            modelImpl.getUmlFactory().delete(top);
        }

        modelImpl.getUmlHelper().deleteCollection(
                stateMachine.getSubmachineState());
    }

    /**
     * Deletes the outgoing and incoming transitions of a statevertex.
     * <p>
     *
     * @param elem
     *            the UML element to be deleted
     */
    void deleteStateVertex(Object elem) {
        if (!(elem instanceof StateVertex)) {
            throw new IllegalArgumentException();
        }

        modelImpl.getUmlHelper().deleteCollection(
                ((StateVertex) elem).getIncoming());
        modelImpl.getUmlHelper().deleteCollection(
                ((StateVertex) elem).getOutgoing());

    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteStubState(Object elem) {
        if (!(elem instanceof StubState)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteSubmachineState(Object elem) {
        if (!(elem instanceof SubmachineState)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteSynchState(Object elem) {
        if (!(elem instanceof SynchState)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteTimeEvent(Object elem) {
        if (!(elem instanceof TimeEvent)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param elem
     *            the UML element to be deleted
     */
    void deleteTransition(Object elem) {
        if (!(elem instanceof Transition)) {
            throw new IllegalArgumentException();
        }

        final Transition transition = (Transition) elem;
        final Guard guard = transition.getGuard();
        if (guard != null) {
            // This shouldn't be required since it's a composite, but there's
            // a bug in the version of MDR that we use (20050711) that causes
            // it to fail to delete aggregate elements which are single valued
            // and where the aggregate end is listed second in the association
            // defined in the metamodel. - tfm 20080713
            modelImpl.getUmlFactory().delete(guard);
        }

        // The effect will get deleted automatically by MDR, unlike the Guard.
    }

}
