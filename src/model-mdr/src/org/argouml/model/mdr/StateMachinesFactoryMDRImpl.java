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
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
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
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Operation;
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
    private Logger LOG = Logger.getLogger(StateMachinesFactoryMDRImpl.class);
	
    /**
     * The model implementation.
     */
    private MDRModelImplementation nsmodel;
    
    /**
     * The State Machine Package proxy 
     */
    private StateMachinesPackage smPackage;
    
    /**
     * Don't allow instantiation.
     *
     * @param implementation
     *            To get other helpers and factories.
     */
    StateMachinesFactoryMDRImpl(MDRModelImplementation implementation) {
        nsmodel = implementation;
        smPackage = nsmodel.getUmlPackage().getStateMachines();

    }

    /*
     * @see org.argouml.model.StateMachinesFactory#createCallEvent()
     */
    public Object createCallEvent() {
        CallEvent myCallEvent = smPackage.getCallEvent().createCallEvent();
        super.initialize(myCallEvent);
        return myCallEvent;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#createChangeEvent()
     */
    public Object createChangeEvent() {
        ChangeEvent myChangeEvent = smPackage.getChangeEvent()
                .createChangeEvent();
        super.initialize(myChangeEvent);
        return myChangeEvent;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#createCompositeState()
     */
    public Object createCompositeState() {
        CompositeState myCompositeState = smPackage.getCompositeState()
                .createCompositeState();
        super.initialize(myCompositeState);
        return myCompositeState;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#createFinalState()
     */
    public Object createFinalState() {
        FinalState myFinalState = smPackage.getFinalState().createFinalState();
        super.initialize(myFinalState);
        return myFinalState;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#createGuard()
     */
    public Object createGuard() {
        Guard myGuard = smPackage.getGuard().createGuard();
        super.initialize(myGuard);
        return myGuard;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#createPseudostate()
     */
    public Object createPseudostate() {
        Pseudostate myPseudostate = smPackage.getPseudostate()
                .createPseudostate();
        super.initialize(myPseudostate);
        return myPseudostate;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#createSignalEvent()
     */
    public Object createSignalEvent() {
        SignalEvent mySignalEvent = smPackage.getSignalEvent()
                .createSignalEvent();
        super.initialize(mySignalEvent);
        return mySignalEvent;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#createSimpleState()
     */
    public Object createSimpleState() {
        SimpleState mySimpleState = smPackage.getSimpleState()
                .createSimpleState();
        super.initialize(mySimpleState);
        return mySimpleState;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#createStateMachine()
     */
    public Object createStateMachine() {
        StateMachine myStateMachine = smPackage.getStateMachine()
                .createStateMachine();
        super.initialize(myStateMachine);
        return myStateMachine;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#createStubState()
     */
    public Object createStubState() {
        StubState myStubState = smPackage.getStubState().createStubState();
        super.initialize(myStubState);
        return myStubState;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#createSubmachineState()
     */
    public Object createSubmachineState() {
        SubmachineState mySubmachineState = smPackage.getSubmachineState()
                .createSubmachineState();
        super.initialize(mySubmachineState);
        return mySubmachineState;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#createSynchState()
     */
    public Object createSynchState() {
        SynchState mySynchState = smPackage.getSynchState().createSynchState();
        super.initialize(mySynchState);
        return mySynchState;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#createTimeEvent()
     */
    public Object createTimeEvent() {
        TimeEvent myTimeEvent = smPackage.getTimeEvent().createTimeEvent();
        super.initialize(myTimeEvent);
        return myTimeEvent;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#createTransition()
     */
    public Object createTransition() {
        Transition myTransition = smPackage.getTransition().createTransition();
        super.initialize(myTransition);
        return myTransition;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildCompositeStateOnStateMachine(java.lang.Object)
     */
    public Object buildCompositeStateOnStateMachine(Object statemachine) {
        if (statemachine instanceof StateMachine) {
            CompositeState state = (CompositeState) createCompositeState();
            state.setStateMachine((StateMachine) statemachine);
            state.setName("top");
            return state;
        }
        throw new IllegalArgumentException("statemachine");
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildStateMachine(java.lang.Object)
     */
    public Object buildStateMachine(Object oContext) {
        if (oContext != null
                && (nsmodel.getStateMachinesHelper().
                        isAddingStatemachineAllowed(oContext))) {
            
            StateMachine machine = (StateMachine) createStateMachine();
            ModelElement context = (ModelElement) oContext;
            machine.setContext(context);
            if (context instanceof Classifier) {
                machine.setNamespace((Classifier) context);
            } else if (context instanceof BehavioralFeature) {
                BehavioralFeature feature = (BehavioralFeature) context;
                machine.setNamespace(feature.getOwner());
            }
            Object top = buildCompositeStateOnStateMachine(machine);
            machine.setTop((State) top);
            return machine;
        }
        throw new IllegalArgumentException("In buildStateMachine: "
                + "context null or not legal");
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildTransition(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object buildTransition(Object owningState, Object source, 
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
    	    Transition trans = (Transition) createTransition();
    	    compositeState.getInternalTransition().add(trans);
    	    trans.setSource((StateVertex) source);
    	    trans.setTarget((StateVertex) dest);
    	    return trans;

      	}
        throw new IllegalArgumentException("In buildTransition: "
                + "arguments not legal");
    }


    /*
     * @see org.argouml.model.StateMachinesFactory#buildPseudoState(java.lang.Object)
     */
    public Object buildPseudoState(Object compositeState) {
        if (compositeState instanceof CompositeState) {
            Pseudostate state = (Pseudostate) createPseudostate();
            state.setKind(PseudostateKindEnum.PK_CHOICE);
            state.setContainer((CompositeState) compositeState);
            ((CompositeState) compositeState).getSubvertex().add(state);
            return state;
        }
        throw new IllegalArgumentException(
                "Argument must be a CompositeState");
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildSynchState(java.lang.Object)
     */
    public Object buildSynchState(Object compositeState) {
        if (compositeState instanceof CompositeState) {
            SynchState state = (SynchState) createSynchState();
            state.setBound(0);
            state.setContainer((CompositeState) compositeState);
            return state;
        }
        throw new IllegalArgumentException(
                "Argument must be a CompositeState");
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildStubState(java.lang.Object)
     */
    public Object buildStubState(Object compositeState) {
        if (compositeState instanceof CompositeState) {
            StubState state = (StubState) createStubState();
            state.setReferenceState("");
            state.setContainer((CompositeState) compositeState);
            return state;
        }
        throw new IllegalArgumentException(
                "Argument must be a CompositeState");
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildCompositeState(java.lang.Object)
     */
    public Object buildCompositeState(Object compositeState) {
        if (compositeState instanceof CompositeState) {
            CompositeState state = (CompositeState) createCompositeState();
            state.setConcurrent(false);
            state.setContainer((CompositeState) compositeState);
            return state;
        }
        throw new IllegalArgumentException(
                "Argument must be a CompositeState");
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildSimpleState(java.lang.Object)
     */
    public Object buildSimpleState(Object compositeState) {
        if (compositeState instanceof CompositeState) {
            SimpleState state = (SimpleState) createSimpleState();
            state.setContainer((CompositeState) compositeState);
            return state;
        }
        throw new IllegalArgumentException(
                "Argument must be a CompositeState");
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildFinalState(java.lang.Object)
     */
    public Object buildFinalState(Object compositeState) {
        if (compositeState instanceof CompositeState) {
            FinalState state = (FinalState) createFinalState();
            state.setContainer((CompositeState) compositeState);
            return state;
        }
        throw new IllegalArgumentException(
                "Argument must be a CompositeState");
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildSubmachineState(java.lang.Object)
     */
    public Object buildSubmachineState(Object compositeState) {
        if (compositeState instanceof CompositeState) {
            SubmachineState state = (SubmachineState) createSubmachineState();
            state.setContainer((CompositeState) compositeState);
            return state;
        }
        throw new IllegalArgumentException(
                "Argument must be a CompositeState");
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildInternalTransition(java.lang.Object)
     */
    public Object buildInternalTransition(Object state) {
        if (state instanceof State) {
            Transition trans = (Transition) createTransition();
            ((State) state).getInternalTransition().add(trans);
            trans.setSource((State) state);
            trans.setTarget((State) state);
            return trans;
        }
        throw new IllegalArgumentException("Argument must be a State");    
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildTransition(java.lang.Object, java.lang.Object)
     */
    public Object buildTransition(Object source, Object target) {
        if (source instanceof StateVertex && target instanceof StateVertex) {
            Transition trans = (Transition) createTransition();
            trans.setSource((StateVertex) source);
            trans.setTarget((StateVertex) target);
            trans.setStateMachine((StateMachine) nsmodel.
                    getStateMachinesHelper().getStateMachine(source));
            return trans;
        }
        throw new IllegalArgumentException();
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildCallEvent(java.lang.Object)
     */
    public Object buildCallEvent(Object ns) {
        CallEvent event = (CallEvent) createCallEvent();
        event.setNamespace((Namespace) ns);
        event.setName("");
        return event;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildCallEvent(java.lang.Object, java.lang.String, java.lang.Object)
     */
    public Object buildCallEvent(Object trans, String name, Object ns) {
        if (!(trans instanceof Transition)) {
            throw new IllegalArgumentException();
        }
        CallEvent evt = (CallEvent) createCallEvent();
        evt.setNamespace((Namespace) ns);

        String operationName = (name.indexOf("(") > 0) ? name.substring(0,
                name.indexOf("(")).trim() : name.trim();
        evt.setName(operationName);
        Object op = nsmodel.getStateMachinesHelper().findOperationByName(trans,
                operationName);
        if (op != null) {
            evt.setOperation((Operation) op);
        }
        return evt;        
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildSignalEvent(java.lang.Object)
     */
    public Object buildSignalEvent(Object ns) {
        SignalEvent event = (SignalEvent) createSignalEvent();
        event.setNamespace((Namespace) ns);
        event.setName("");
        return event;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildSignalEvent(java.lang.String, java.lang.Object)
     */
    public Object buildSignalEvent(String name, Object ns) {
        SignalEvent event = (SignalEvent) createSignalEvent();
        event.setNamespace((Namespace) ns);
        event.setName(name);
        return event;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildTimeEvent(java.lang.Object)
     */
    public Object buildTimeEvent(Object ns) {
        TimeEvent event = (TimeEvent) createTimeEvent();
        event.setNamespace((Namespace) ns);
        event.setName("");
        return event;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildTimeEvent(java.lang.String, java.lang.Object)
     */
    public Object buildTimeEvent(String s, Object ns) {
        TimeEvent event = (TimeEvent) createTimeEvent();
        event.setNamespace((Namespace) ns);
        event.setName("");
        Object te = nsmodel.getDataTypesFactory().createTimeExpression("", s);
        event.setWhen((TimeExpression) te);
        return event;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildChangeEvent(java.lang.Object)
     */
    public Object buildChangeEvent(Object ns) {
        ChangeEvent event = (ChangeEvent) createChangeEvent();
        event.setNamespace((Namespace) ns);
        event.setName("");
        return event;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildChangeEvent(java.lang.String, java.lang.Object)
     */
    public Object buildChangeEvent(String s, Object ns) {
        ChangeEvent event = (ChangeEvent) createChangeEvent();
        event.setNamespace((Namespace) ns);
        event.setName("");
        Object ce = nsmodel.getDataTypesFactory()
                .createBooleanExpression("", s);
        event.setChangeExpression((BooleanExpression) ce);
        return event;
    }

    /*
     * @see org.argouml.model.StateMachinesFactory#buildGuard(java.lang.Object)
     */
    public Object/*Guard*/ buildGuard(Object transition) {
        if (transition instanceof Transition) {
            Transition t = (Transition) transition;
            if (t.getGuard() != null) {
                LOG.warn("Replacing Guard " + t.getGuard().getName() 
                        + " on Transition " + t.getName());
            }
            Guard guard = (Guard) createGuard();
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
     * @param elem
     *            the UML element to be deleted
     */
    void deleteCompositeState(Object elem) {
        if (!(elem instanceof CompositeState)) {
            throw new IllegalArgumentException();
        }

        Collection vertices = ((CompositeState) elem).getSubvertex();
        Iterator it = vertices.iterator();
        while (it.hasNext()) {
            StateVertex vertex = (StateVertex) it.next();
            nsmodel.getUmlFactory().delete(vertex);
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

        // This code is probably unnecessary since the top is
        // associated by composition
        State top = (State) Model.getFacade().getTop(elem);
        if (top != null) {
            nsmodel.getUmlFactory().delete(top);
        }
        
        nsmodel.getUmlHelper().deleteCollection(
                ((StateMachine) elem).getSubmachineState());
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

        nsmodel.getUmlHelper().deleteCollection(
                ((StateVertex) elem).getIncoming());
        nsmodel.getUmlHelper().deleteCollection(
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
    }

}

