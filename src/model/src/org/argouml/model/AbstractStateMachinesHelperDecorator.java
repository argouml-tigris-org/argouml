// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
 * An abstract Decorator for the {@link StateMachinesHelper}.
 *
 * @author Bob Tarling
 */
public abstract class AbstractStateMachinesHelperDecorator
	implements StateMachinesHelper {

    /**
     * The component.
     */
    private StateMachinesHelper impl;

    /**
     * @param component The component to decorate.
     */
    AbstractStateMachinesHelperDecorator(StateMachinesHelper component) {
        impl = component;
    }

    /**
     * The component we are decorating.
     *
     * @return Returns the component.
     */
    protected StateMachinesHelper getComponent() {
        return impl;
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#getSource(java.lang.Object)
     */
    public Object getSource(Object trans) {
        return impl.getSource(trans);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#getDestination(
     *         java.lang.Object)
     */
    public Object getDestination(Object trans) {
        return impl.getDestination(trans);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#getStateMachine(
     *         java.lang.Object)
     */
    public Object getStateMachine(Object handle) {
        return impl.getStateMachine(handle);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setEventAsTrigger(
     *         java.lang.Object,
     *      java.lang.Object)
     */
    public void setEventAsTrigger(Object transition, Object event) {
        impl.setEventAsTrigger(transition, event);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#isAddingStatemachineAllowed(
     *         java.lang.Object)
     */
    public boolean isAddingStatemachineAllowed(Object context) {
        return impl.isAddingStatemachineAllowed(context);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#isTopState(java.lang.Object)
     */
    public boolean isTopState(Object o) {
        return impl.isTopState(o);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#getAllPossibleStatemachines(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public Collection getAllPossibleStatemachines(Object model,
            Object oSubmachineState) {
        return impl.getAllPossibleStatemachines(model, oSubmachineState);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#getAllPossibleSubvertices(
     *         java.lang.Object)
     */
    public Collection getAllPossibleSubvertices(Object oState) {
        return impl.getAllPossibleSubvertices(oState);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setStatemachineAsSubmachine(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setStatemachineAsSubmachine(Object oSubmachineState,
            Object oStatemachine) {
        impl.setStatemachineAsSubmachine(oSubmachineState, oStatemachine);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#getTop(java.lang.Object)
     */
    public Object getTop(Object sm) {
        return impl.getTop(sm);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#getOutgoingStates(
     *         java.lang.Object)
     */
    public Collection getOutgoingStates(Object ostatevertex) {
        return impl.getOutgoingStates(ostatevertex);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#findOperationByName(
     *         java.lang.Object,
     *         java.lang.String)
     */
    public Object findOperationByName(Object trans, String opname) {
        return impl.findOperationByName(trans, opname);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#getAllSubStates(
     *         java.lang.Object)
     */
    public Collection getAllSubStates(Object compState) {
        return impl.getAllSubStates(compState);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#removeSubvertex(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void removeSubvertex(Object handle, Object subvertex) {
        impl.removeSubvertex(handle, subvertex);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#addSubvertex(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void addSubvertex(Object handle, Object subvertex) {
        impl.addSubvertex(handle, subvertex);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setBound(
     *         java.lang.Object,
     *         int)
     */
    public void setBound(Object handle, int bound) {
        impl.setBound(handle, bound);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setConcurrent(
     *         java.lang.Object,
     *         boolean)
     */
    public void setConcurrent(Object handle, boolean concurrent) {
        impl.setConcurrent(handle, concurrent);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setContainer(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setContainer(Object handle, Object compositeState) {
        impl.setContainer(handle, compositeState);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setDoActivity(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setDoActivity(Object handle, Object value) {
        impl.setDoActivity(handle, value);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setEffect(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setEffect(Object handle, Object value) {
        impl.setEffect(handle, value);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setEntry(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setEntry(Object handle, Object value) {
        impl.setEntry(handle, value);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setExit(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setExit(Object handle, Object value) {
        impl.setExit(handle, value);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setExpression(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setExpression(Object handle, Object value) {
        impl.setExpression(handle, value);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setGuard(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setGuard(Object handle, Object guard) {
        impl.setGuard(handle, guard);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setInternalTransitions(
     *         java.lang.Object,
     *         java.util.Collection)
     */
    public void setInternalTransitions(Object handle, Collection intTrans) {
        impl.setInternalTransitions(handle, intTrans);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setSource(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setSource(Object handle, Object state) {
        impl.setSource(handle, state);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setState(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setState(Object handle, Object element) {
        impl.setState(handle, element);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setStateMachine(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setStateMachine(Object handle, Object stm) {
        impl.setStateMachine(handle, stm);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setSubvertices(
     *         java.lang.Object,
     *         java.util.Collection)
     */
    public void setSubvertices(Object handle, Collection subvertices) {
        impl.setSubvertices(handle, subvertices);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setTrigger(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setTrigger(Object handle, Object event) {
        impl.setTrigger(handle, event);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setWhen(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setWhen(Object handle, Object value) {
        impl.setWhen(handle, value);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setChangeExpression(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setChangeExpression(Object handle, Object value) {
        impl.setChangeExpression(handle, value);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#getPath(
     *         java.lang.Object)
     */
    public String getPath(Object o) {
        return impl.getPath(o);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#getStatebyName(
     *         java.lang.String, java.lang.Object)
     */
    public Object getStatebyName(String path, Object container) {
        return impl.getStatebyName(path, container);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setReferenceState(
     *         java.lang.Object,
     *         java.lang.String)
     */
    public void setReferenceState(Object o, String referenced) {
        impl.setReferenceState(o, referenced);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#findNamespaceForEvent(
     *         java.lang.Object, java.lang.Object)
     */
    public Object findNamespaceForEvent(Object trans, Object model) {
        return impl.findNamespaceForEvent(trans, model);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#addDeferrableEvent(java.lang.Object, java.lang.Object)
     */
    public void addDeferrableEvent(Object state, Object deferrableEvent) {
        impl.addDeferrableEvent(state, deferrableEvent);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#removeDeferrableEvent(java.lang.Object, java.lang.Object)
     */
    public void removeDeferrableEvent(Object state, Object deferrableEvent) {
        impl.removeDeferrableEvent(state, deferrableEvent);
    }

    /**
     * @see org.argouml.model.StateMachinesHelper#setContext(java.lang.Object, java.lang.Object)
     */
    public void setContext(Object statemachine, Object modelElement) {
        impl.setContext(statemachine, modelElement);
    }

}
