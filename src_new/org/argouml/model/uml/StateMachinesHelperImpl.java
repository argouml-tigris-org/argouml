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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.model.StateMachinesHelper;

import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.state_machines.MChangeEvent;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MEvent;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MSubmachineState;
import ru.novosoft.uml.behavior.state_machines.MSynchState;
import ru.novosoft.uml.behavior.state_machines.MTimeEvent;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.data_types.MBooleanExpression;
import ru.novosoft.uml.foundation.data_types.MTimeExpression;

/**
 * Helper class for UML BehavioralElements::StateMachines Package.
 *
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
class StateMachinesHelperImpl implements StateMachinesHelper {

    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    StateMachinesHelperImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * Returns the source of the given transition. This operation is here to
     * give a full implementation of all getSource and getDestination methods
     * on the uml helpers.
     *
     * @param trans the given transition
     * @return MStateVertex the source statevertex
     */
    public Object getSource(Object trans) {
        return ((MTransition) trans).getSource();
    }

    /**
     * Returns the destination of the given transition. This operation is here
     * to give a full implementation of all getSource and getDestination methods
     * on the uml helpers.<p>
     *
     * @param trans the given transition
     * @return MStateVertex the destination statevertex
     */
    public Object getDestination(Object trans) {
        return ((MTransition) trans).getTarget();
    }

    /**
     * Gets the statemachine that contains the given Object
     * Traverses the state hierarchy of the statemachine
     * untill the statemachine is reached.  To decouple ArgoUML as
     * much as possible from the NSUML model, the parameter of the
     * method is of type Object, and the result, too.<p>
     *
     * @param handle The state for which we want to know the
     * statemachine
     * @return Object MStateMachine The statemachine the state belongs too or
     * null if the given parameter is not a state or null itself.
     */
    public Object getStateMachine(Object handle) {
        if (handle instanceof MStateVertex) {
            MStateVertex state = (MStateVertex) handle;
            if (state instanceof MState
		&& ((MState) state).getStateMachine() != null) {
                return ((MState) state).getStateMachine();
            } else {
                return getStateMachine(state.getContainer());
            }
        }
        if (handle instanceof MTransition) {
            Object sm = ((MTransition) handle).getStateMachine();
            if (sm != null) {
                return sm;
            }
            // the next statement is for internal transitions
            return getStateMachine(((MTransition) handle).getSource());
        }
        throw new IllegalArgumentException("null argument to "
					   + "getStateMachine()");
    }

    /**
     * Couples a given event to the given transition as being trigger
     * event. To decouple ArgoUML as much as possible from the NSUML
     * model, the parameters of the method are of type Object.<p>
     *
     * @param transition the given transition
     * @param event the given event
     */
    public void setEventAsTrigger(Object transition, Object event) {
        if (transition == null || !(transition instanceof MTransition)) {
            throw new IllegalArgumentException("Transition either null or not "
					       + "an instance of MTransition");
        }
        if (event == null || !(event instanceof MEvent)) {
            throw new IllegalArgumentException("Event either null or not an "
					       + "instance of MEvent");
        }
        ((MTransition) transition).setTrigger((MEvent) event);
    }

    /**
     * Returns true if a statemachine may be added to the given
     * context. To decouple ArgoUML as much as possible from the NSUML
     * model, the parameter of the method is of type Object.<p>
     *
     * @param context the given context
     * @return boolean true if a statemachine may be added
     */
    public boolean isAddingStatemachineAllowed(Object context) {
        if (context instanceof MBehavioralFeature
	    || context instanceof MClassifier) {
            return true;
	}
        return false;
    }

    /**
     * Returns true is the given state is the top state.
     *
     * @author MVW
     * @param o CompositeState
     * @return boolean
     */
    public boolean isTopState(Object o) {
        if (o instanceof MCompositeState) {
            if (Model.getFacade().getContainer(o) == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all statemachines that can be the statemachine the given
     * submachinestate represents. To decouple ArgoUML as much as possible from
     * the NSUML model, the parameter of the method is of type Object.
     *
     * @param model The model where we search for state machines.
     * @param oSubmachineState The submachinestate we are searching the
     * statemachines for.
     * @return Collection The collection with found statemachines.
     */
    public Collection getAllPossibleStatemachines(
            Object model,
            Object oSubmachineState) {
        if (oSubmachineState instanceof MSubmachineState) {
            Collection statemachines =
		nsmodel.getModelManagementHelper()
		    .getAllModelElementsOfKind(model, MStateMachine.class);
            statemachines.remove(getStateMachine(oSubmachineState));
            return statemachines;
        }
        return null;
    }

    /**
     * Connects a given statemachine to a submachinestate as being the
     * statemachine the submachinestate represents. To decouple ArgoUML as much
     * as possible from the NSUML model, the parameters of the method are of
     * type Object.
     * @param oSubmachineState The submachinestate for which we want to set the
     * property submachine
     * @param oStatemachine The statemachine
     */
    public void setStatemachineAsSubmachine(Object oSubmachineState,
					    Object oStatemachine) {
        if (oSubmachineState instanceof MSubmachineState
	    && oStatemachine instanceof MStateMachine) {
	    MSubmachineState mss = (MSubmachineState) oSubmachineState;
	    mss.setSubmachine((MStateMachine) oStatemachine);
        }
    }

    /**
     * Get the "top" composite state of a statemachine. <p>
     *
     * @param sm the given statemachine
     * @return the top composite state
     */
    public Object getTop(Object sm) {
        if (!(sm instanceof MStateMachine)) {
            throw new IllegalArgumentException();
        }

        if (sm == null) {
            return null;
        }
        return ((MStateMachine) sm).getTop();
    }

    /**
     * Gets all statevertices that are a target to transitions
     * outgoing from the given statevertex.<p>
     *
     * @param ostatevertex  the given statevertex
     * @return Collection   all statevertices that are a target
     */
    public Collection getOutgoingStates(Object ostatevertex) {
        if (ostatevertex instanceof MStateVertex) {
            MStateVertex statevertex = (MStateVertex) ostatevertex;
            Collection col = new ArrayList();
            Iterator it = statevertex.getOutgoings().iterator();
            while (it.hasNext()) {
                col.add(((MTransition) it.next()).getTarget());
            }
            return col;
        }
        return null;
    }

    /**
     * Finds the operation to which a CallEvent refers.
     * TODO: This function works for the most normal cases,
     * but needs some testing for rare cases, e.g. internal transitions,...
     *
     * @author MVW
     * @param trans Object of type MTransition
     * @param opname the name of the operation sought
     * @return Object The operation with the given name, or null.
     */
    public Object findOperationByName(Object trans, String opname) {
        if (!(trans instanceof MTransition)) {
            throw new IllegalArgumentException();
        }
        Object sm = getStateMachine(trans);
        Object ns = nsmodel.getFacade().getNamespace(sm);
        if (ns instanceof MClassifier) {
            Collection c = nsmodel.getFacade().getOperations(ns);
            Iterator i = c.iterator();
            while (i.hasNext()) {
                Object op = i.next();
                String on = ((MModelElement) op).getName();
                if (on.equals(opname)) {
                    return op;
                }
            }
        }
        return null;
    }

    /**
     * Returns all substates some composite state contains.
     * @param compState the given compositestate
     * @return all substates
     */
    public Collection getAllSubStates(Object compState) {
        if (compState instanceof MCompositeState) {
            List retList = new ArrayList();
            Iterator it =
                nsmodel.getFacade().getSubvertices(compState).iterator();
            while (it.hasNext()) {
                Object subState = it.next();
                if (subState instanceof MCompositeState) {
                    retList.addAll(getAllSubStates(subState));
                }
                retList.add(subState);
            }
            return retList;
        } else {
            throw new IllegalArgumentException(
                    "Argument is not a composite state");
        }
    }

    /**
     * Remove a given subvertex from a given composite state.
     *
     * @param handle the composite state
     * @param subvertex the StateVertex
     */
    public void removeSubvertex(Object handle, Object subvertex) {
        if (handle instanceof MCompositeState
            && subvertex instanceof MStateVertex) {
            ((MCompositeState) handle).removeSubvertex(
                    (MStateVertex) subvertex);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or subvertex: " + subvertex);
    }

    /**
     * Add a subvertex to a composite state.
     *
     * @param handle the CompositeState
     * @param subvertex the StateVertex
     */
    public void addSubvertex(Object handle, Object subvertex) {
        if (handle instanceof MCompositeState
            && subvertex instanceof MStateVertex) {
            ((MCompositeState) handle).addSubvertex((MStateVertex) subvertex);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or subvertex: " + subvertex);
    }

    /**
     * Sets the Bound of some SynchState.
     *
     * @param handle Synch State
     * @param bound A positive integer or the value “unlimited” specifying
     *              the maximal count of the SynchState. The count is the
     *              difference between the number of times the incoming
     *              and outgoing transitions of the synch state are fired.
     */
    public void setBound(Object handle, int bound) {
        if (handle instanceof MSynchState) {
            ((MSynchState) handle).setBound(bound);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or bound: " + bound);
    }

    /**
     * Makes a Composite State concurrent.
     *
     * @param handle the CompositState
     * @param concurrent boolean
     */
    public void setConcurrent(Object handle, boolean concurrent) {
        if (handle instanceof MCompositeState) {
            ((MCompositeState) handle).setConcurent(concurrent);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Set the container of a statevertex.
     *
     * @param handle is the stateVertex
     * @param compositeState is the container. Can be <code>null</code>.
     */
    public void setContainer(Object handle, Object compositeState) {
        if (handle instanceof MStateVertex
            && (compositeState == null
                || compositeState instanceof MCompositeState)) {
            ((MStateVertex) handle).setContainer(
                (MCompositeState) compositeState);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or compositeState: " + compositeState);
    }

    /**
     * Sets the do activity of a state.
     *
     * @param handle is the state
     * @param value the activity. Can be <code>null</code>.
     */
    public void setDoActivity(Object handle, Object value) {
        if (handle instanceof MState
            && (value == null || value instanceof MAction)) {
            ((MState) handle).setDoActivity((MAction) value);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or value: " + value);
    }

    /**
     * Sets the effect of some transition.
     *
     * @param handle is the transition
     * @param value is the effect. Can be <code>null</code>.
     */
    public void setEffect(Object handle, Object value) {
        if (handle instanceof MTransition
            && (value == null || value instanceof MAction)) {
            ((MTransition) handle).setEffect((MAction) value);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or value: " + value);
    }

    /**
     * Sets the entry action of some state.
     *
     * @param handle is the state
     * @param value is the action. Can be <code>null</code>.
     */
    public void setEntry(Object handle, Object value) {
        if (handle instanceof MState
            && (value == null || value instanceof MAction)) {
            ((MState) handle).setEntry((MAction) value);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or value: " + value);
    }

    /**
     * Sets the exit action of some state.
     *
     * @param handle is the state
     * @param value is the action. Can be <code>null</code>.
     */
    public void setExit(Object handle, Object value) {
        if (handle instanceof MState
            && (value == null || value instanceof MAction)) {
            ((MState) handle).setExit((MAction) value);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or value: " + value);
    }

    /**
     * Set the Expression of a Guard or ChangeEvent.
     *
     * @param handle Guard or ChangeEvent
     * @param value BooleanExpression or null
     */
    public void setExpression(Object handle, Object value) {
        if (handle instanceof MGuard
                && (value == null || value instanceof MBooleanExpression)) {
            ((MGuard) handle).setExpression((MBooleanExpression) value);
            return;
        }
        if (handle instanceof MChangeEvent
                && (value == null || value instanceof MBooleanExpression)) {
            MChangeEvent ce = (MChangeEvent) handle;
            ce.setChangeExpression((MBooleanExpression) value);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or value: " + value);
    }

    /**
     * Sets the guard of a transition.
     *
     * @param handle to the transition
     * @param guard to be set. Can be null.
     */
    public void setGuard(Object handle, Object guard) {
        if (handle instanceof MTransition
                && (guard == null || guard instanceof MGuard)) {
            ((MTransition) handle).setGuard((MGuard) guard);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or guard: " + guard);
    }

    /**
     * @param handle is the target.
     * @param intTrans is a collection of transitions.
     */
    public void setInternalTransitions(
        Object handle,
        Collection intTrans) {
        if (handle instanceof MState) {
            ((MState) handle).setInternalTransitions(intTrans);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Sets the source state of some message.
     *
     * @param handle the message
     * @param state the source state
     */
    public void setSource(Object handle, Object state) {
        if (handle instanceof MTransition && state instanceof MStateVertex) {
            ((MTransition) handle).setSource((MStateVertex) state);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or state: " + state);
    }

    /**
     * Sets the state of an internal transition.
     *
     * @param handle the internal transition
     * @param element the state that contains this transition
     */
    public void setState(Object handle, Object element) {
        if (handle instanceof MTransition
            && element instanceof MState) {
            ((MTransition) handle).setState((MState) element);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or element: " + element);
    }

    /**
     * Sets a state machine of some state or transition.
     *
     * @param handle is the state or transition
     * @param stm is the state machine
     */
    public void setStateMachine(Object handle, Object stm) {
        if (handle instanceof MState
            && (stm == null || stm instanceof MStateMachine)) {
            ((MState) handle).setStateMachine((MStateMachine) stm);
            return;
        }
        if (handle instanceof MTransition
            && (stm == null || stm instanceof MStateMachine)) {
            ((MTransition) handle).setStateMachine((MStateMachine) stm);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle + " or stm: " + stm);
    }

    /**
     * Set the collection of substates for a CompositeState.
     *
     * @param handle CompositeState
     * @param subvertices collection of sub-StateVertexes
     */
    public void setSubvertices(Object handle, Collection subvertices) {
        if (handle instanceof MCompositeState) {
            ((MCompositeState) handle).setSubvertices(subvertices);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or subvertices: " + subvertices);
    }

    /**
     * Sets the trigger event of a transition.
     *
     * @param handle is the transition
     * @param event is the trigger event
     */
    public void setTrigger(Object handle, Object event) {
        if (handle instanceof MTransition
                && (event == null || event instanceof MEvent)) {
            ((MTransition) handle).setTrigger((MEvent) event);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or event: " + event);
    }

    /**
     * Sets the time-expression for a TimeEvent.
     *
     * @param handle Object (MTimeEvent)
     * @param value Object (MTimeExpression)
     */
    public void setWhen(Object handle, Object value) {
        if (handle instanceof MTimeEvent
            && (value == null || value instanceof MTimeExpression)) {
            ((MTimeEvent) handle).setWhen((MTimeExpression) value);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or value: " + value);
    }

}
