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

import java.util.Collection;

/**
 * The interface for the helper for StateMachines.<p>
 *
 * Created from the old StateMachinesHelper.
 */
public interface StateMachinesHelper {
    /**
     * Returns the source of the given transition. This operation is here to
     * give a full implementation of all getSource and getDestination methods
     * on the uml helpers.
     *
     * @param trans the given transition
     * @return MStateVertex the source statevertex
     */
    Object getSource(Object trans);

    /**
     * Returns the destination of the given transition. This operation is here
     * to give a full implementation of all getSource and getDestination methods
     * on the uml helpers.<p>
     *
     * @param trans the given transition
     * @return MStateVertex the destination statevertex
     */
    Object getDestination(Object trans);

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
    Object getStateMachine(Object handle);

    /**
     * Couples a given event to the given transition as being trigger
     * event. To decouple ArgoUML as much as possible from the NSUML
     * model, the parameters of the method are of type Object.<p>
     *
     * @param transition the given transition
     * @param event the given event
     */
    void setEventAsTrigger(Object transition, Object event);

    /**
     * Returns true if a statemachine may be added to the given
     * context. To decouple ArgoUML as much as possible from the NSUML
     * model, the parameter of the method is of type Object.<p>
     *
     * @param context the given context
     * @return boolean true if a statemachine may be added
     */
    boolean isAddingStatemachineAllowed(Object context);

    /**
     * Returns true is the given state is the top state.
     *
     * @author MVW
     * @param o CompositeState
     * @return boolean
     */
    boolean isTopState(Object o);

    /**
     * Returns all statemachines that can be the statemachine the given
     * submachinestate represents. To decouple ArgoUML as much as possible from
     * the NSUML model, the parameter of the method is of type Object.
     * @param oSubmachineState The submachinestate we are searching the
     * statemachines for.
     * @return Collection The collection with found statemachines.
     */
    Collection getAllPossibleStatemachines(Object oSubmachineState);

    /**
     * Connects a given statemachine to a submachinestate as being the
     * statemachine the submachinestate represents. To decouple ArgoUML as much
     * as possible from the NSUML model, the parameters of the method are of
     * type Object.
     * @param oSubmachineState The submachinestate for which we want to set the
     * property submachine
     * @param oStatemachine The statemachine
     */
    void setStatemachineAsSubmachine(Object oSubmachineState,
            Object oStatemachine);

    /**
     * Get the "top" composite state of a statemachine. <p>
     *
     * The difference with the equally named function in the ModelFacade,
     * is that this one swallows null without causing an exception.
     *
     * @param sm the given statemachine
     * @return the top composite state
     */
    Object getTop(Object sm);

    /**
     * Gets all statevertices that are a target to transitions
     * outgoing from the given statevertex.<p>
     *
     * @param ostatevertex  the given statevertex
     * @return Collection   all statevertices that are a target
     */
    Collection getOutgoingStates(Object ostatevertex);

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
    Object findOperationByName(Object trans, String opname);

    /**
     * Returns all substates some composite state contains.
     * @param compState the given compositestate
     * @return all substates
     */
    Collection getAllSubStates(Object compState);
}
