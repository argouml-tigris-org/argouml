// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.model.uml.behavioralelements.statemachines;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.ModelFacade;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;

import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MEvent;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MStubState;
import ru.novosoft.uml.behavior.state_machines.MSubmachineState;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MClassifier;

/**
 * Helper class for UML BehavioralElements::StateMachines Package.
 *
 * Current implementation is a placeholder.
 * 
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */
public class StateMachinesHelper {

    /** Don't allow instantiation.
     */
    private StateMachinesHelper() {
    }

    /** Singleton instance.
    */
    private static StateMachinesHelper SINGLETON = new StateMachinesHelper();

    /** Singleton instance access method.
     */
    public static StateMachinesHelper getHelper() {
        return SINGLETON;
    }

    /**
     * Returns the source of the given transition. This operation is here to 
     * give a full implementation of all getSource and getDestination methods
     * on the uml helpers.
     * @param trans
     * @return MStateVertex
     */
    public MStateVertex getSource(MTransition trans) {
        return trans.getSource();
    }

    /**
     * Returns the destination of the given transition. This operation is here 
     * to give a full implementation of all getSource and getDestination methods
     * on the uml helpers.
     * @param trans
     * @return MStateVertex
     */
    public MStateVertex getDestination(MTransition trans) {
        return trans.getTarget();
    }

    /**
     * Couples a given action to the given state as being the exit action. To 
     * decouple ArgoUML as much as possible from the NSUML model, the parameters
     * of the method are of type Object.
     * @param state
     * @param action
     */
    public void setActionAsExit(Object state, Object action) {
        if (state == null || !(state instanceof MState)) {
            throw new IllegalArgumentException("State either null or not an instance of MState");
        }
        if (action == null || !(action instanceof MAction)) {
            throw new IllegalArgumentException("Action either null or not an instance of MAction");
        }
        ((MState) state).setExit((MAction) action);
    }

    /**
     * Couples a given action to the given state as being the entry action. To 
     * decouple ArgoUML as much as possible from the NSUML model, the parameters
     * of the method are of type Object.
     * @param state
     * @param action
     */
    public void setActionAsEntry(Object state, Object action) {
        if (state == null || !(state instanceof MState)) {
            throw new IllegalArgumentException("State either null or not an instance of MState");
        }
        if (action == null || !(action instanceof MAction)) {
            throw new IllegalArgumentException("Action either null or not an instance of MAction");
        }
        ((MState) state).setEntry((MAction) action);
    }

    /**
     * Couples a given action to the given state as being the do activity action. To 
     * decouple ArgoUML as much as possible from the NSUML model, the parameters
     * of the method are of type Object.
     * @param state
     * @param action
     */
    public void setActionAsDoActivity(Object state, Object action) {
        if (state == null || !(state instanceof MState)) {
            throw new IllegalArgumentException("State either null or not an instance of MState");
        }
        if (action == null || !(action instanceof MAction)) {
            throw new IllegalArgumentException("Action either null or not an instance of MAction");
        }
        ((MState) state).setDoActivity((MAction) action);
    }

    /**
     * Couples a given action to the given transition as being the effect
     * of the transition. To decouple ArgoUML as much as possible from the NSUML
     * model, the parameters of the method are of type Object.
     * @param state
     * @param action
     */
    public void setActionAsEffect(Object transition, Object action) {
        if (transition == null || !(transition instanceof MTransition)) {
            throw new IllegalArgumentException("Transition either null or not an instance of MTransition");
        }
        if (action == null || !(action instanceof MAction)) {
            throw new IllegalArgumentException("Action either null or not an instance of MAction");
        }
        ((MTransition) transition).setEffect((MAction) action);
    }

    /**
     * Gets the statemachine that contains the given parameter oState. Traverses
     * the state hierarchy of the statemachine untill the statemachine is reached.
     * To decouple ArgoUML as much as possible from the NSUML model, the parameter
     * of the method is of type Object.
     * @param oState The state for which we want to know the statemachine
     * @return MStateMachine The statemachine the state belongs too or null if 
     *  the given parameter is not a state or null itself.
     */
    public MStateMachine getStateMachine(Object oStateVertex) {
        if (oStateVertex instanceof MStateVertex) {
            MStateVertex state = (MStateVertex) oStateVertex;
            if (state instanceof MState && ((MState)state).getStateMachine() != null) {
                return ((MState)state).getStateMachine();
            } else
                return getStateMachine(state.getContainer());
        }
        return null;
    }

    /**
     * Couples a given event to the given transition as being trigger event. To
     * decouple ArgoUML as much as possible from the NSUML model, the parameters
     * of the method are of type Object.
     * @param state
     * @param action
     */
    public void setEventAsTrigger(Object transition, Object event) {
        if (transition == null || !(transition instanceof MTransition)) {
            throw new IllegalArgumentException("Transition either null or not an instance of MTransition");
        }
        if (event == null || !(event instanceof MEvent)) {
            throw new IllegalArgumentException("Event either null or not an instance of MEvent");
        }
        ((MTransition) transition).setTrigger((MEvent) event);
    }
    
    /**
     * Returns true if a statemachine may be added to the given context. To
     * decouple ArgoUML as much as possible from the NSUML model, the parameter
     * of the method is of type Object.
     * @param context
     * @return boolean
     */
    public boolean isAddingStatemachineAllowed(Object context) {
        if (context instanceof MBehavioralFeature || context instanceof MClassifier)
            return true;
        return false;
    }
    
    /**
     * Returns all statemachines that can be the statemachine the given
     * submachinestate represents. To decouple ArgoUML as much as possible from
     * the NSUML model, the parameter of the method is of type Object.
     * @param oSubmachineState The submachinestate we are searching the
     * statemachines for.
     * @return Collection The collection with found statemachines.
     */
    public Collection getAllPossibleStatemachines(Object oSubmachineState) {
        if (oSubmachineState instanceof MSubmachineState) {
            Collection statemachines = ModelManagementHelper.getHelper().getAllModelElementsOfKind(MStateMachine.class);
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
    public void setStatemachineAsSubmachine(Object oSubmachineState, Object oStatemachine) {
        if (oSubmachineState instanceof MSubmachineState && oStatemachine instanceof MStateMachine) {
            ((MSubmachineState)oSubmachineState).setSubmachine((MStateMachine)oStatemachine);
        }
    }
    
    public MState getTop(MStateMachine sm) {
        if (sm == null)
        	return null;
        return sm.getTop();
    }
    
    /**
     * Gets all statevertices that are a target to transitions outgoing from the
     * given statevertex.
     * @param transition
     * @return Collection
     */
    public Collection getOutgoingStates(Object ostatevertex) {
        if (ModelFacade.getInstance().isAStateVertex(ostatevertex)) {
            MStateVertex statevertex = (MStateVertex)ostatevertex;
            Collection col = new ArrayList();
            Iterator it = statevertex.getOutgoings().iterator();
            while (it.hasNext()) {
                col.add(((MTransition)it.next()).getTarget());           
            }
            return col;
        }
        return null;
    }
    
    

}
