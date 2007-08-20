// $Id:AbstractStateMachinesHelperDecorator.java 13374 2007-08-15 22:03:43Z bobtarling $
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
    protected AbstractStateMachinesHelperDecorator(StateMachinesHelper component) {
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

    /*
     * @see org.argouml.model.StateMachinesHelper#getSource(java.lang.Object)
     */
    public Object getSource(Object trans) {
        return impl.getSource(trans);
    }

    public Object getDestination(Object trans) {
        return impl.getDestination(trans);
    }

    public Object getStateMachine(Object handle) {
        return impl.getStateMachine(handle);
    }

    public void setEventAsTrigger(Object transition, Object event) {
        impl.setEventAsTrigger(transition, event);
    }

    public boolean isAddingStatemachineAllowed(Object context) {
        return impl.isAddingStatemachineAllowed(context);
    }

    public boolean isTopState(Object o) {
        return impl.isTopState(o);
    }

    public Collection getAllPossibleStatemachines(Object model,
            Object oSubmachineState) {
        return impl.getAllPossibleStatemachines(model, oSubmachineState);
    }

    public Collection getAllPossibleSubvertices(Object oState) {
        return impl.getAllPossibleSubvertices(oState);
    }

    public void setStatemachineAsSubmachine(Object oSubmachineState,
            Object oStatemachine) {
        impl.setStatemachineAsSubmachine(oSubmachineState, oStatemachine);
    }

    public Object getTop(Object sm) {
        return impl.getTop(sm);
    }

    public Collection getOutgoingStates(Object ostatevertex) {
        return impl.getOutgoingStates(ostatevertex);
    }

    public Object findOperationByName(Object trans, String opname) {
        return impl.findOperationByName(trans, opname);
    }

    public Collection getAllSubStates(Object compState) {
        return impl.getAllSubStates(compState);
    }

    public void removeSubvertex(Object handle, Object subvertex) {
        impl.removeSubvertex(handle, subvertex);
    }

    public void addSubvertex(Object handle, Object subvertex) {
        impl.addSubvertex(handle, subvertex);
    }

    public void setBound(Object handle, int bound) {
        impl.setBound(handle, bound);
    }

    public void setConcurrent(Object handle, boolean concurrent) {
        impl.setConcurrent(handle, concurrent);
    }

    public void setContainer(Object handle, Object compositeState) {
        impl.setContainer(handle, compositeState);
    }

    public void setDoActivity(Object handle, Object value) {
        impl.setDoActivity(handle, value);
    }

    public void setEffect(Object handle, Object value) {
        impl.setEffect(handle, value);
    }

    public void setEntry(Object handle, Object value) {
        impl.setEntry(handle, value);
    }

    public void setExit(Object handle, Object value) {
        impl.setExit(handle, value);
    }

    public void setExpression(Object handle, Object value) {
        impl.setExpression(handle, value);
    }

    public void setGuard(Object handle, Object guard) {
        impl.setGuard(handle, guard);
    }

    public void setInternalTransitions(Object handle, Collection intTrans) {
        impl.setInternalTransitions(handle, intTrans);
    }

    public void setSource(Object handle, Object state) {
        impl.setSource(handle, state);
    }

    public void setState(Object handle, Object element) {
        impl.setState(handle, element);
    }

    public void setStateMachine(Object handle, Object stm) {
        impl.setStateMachine(handle, stm);
    }

    public void setSubvertices(Object handle, Collection subvertices) {
        impl.setSubvertices(handle, subvertices);
    }

    public void setTrigger(Object handle, Object event) {
        impl.setTrigger(handle, event);
    }

    public void setWhen(Object handle, Object value) {
        impl.setWhen(handle, value);
    }

    public void setChangeExpression(Object handle, Object value) {
        impl.setChangeExpression(handle, value);
    }

    public String getPath(Object o) {
        return impl.getPath(o);
    }

    public Object getStatebyName(String path, Object container) {
        return impl.getStatebyName(path, container);
    }

    public void setReferenceState(Object o, String referenced) {
        impl.setReferenceState(o, referenced);
    }

    public Object findNamespaceForEvent(Object trans, Object model) {
        return impl.findNamespaceForEvent(trans, model);
    }

    public void addDeferrableEvent(Object state, Object deferrableEvent) {
        impl.addDeferrableEvent(state, deferrableEvent);
    }

    public void removeDeferrableEvent(Object state, Object deferrableEvent) {
        impl.removeDeferrableEvent(state, deferrableEvent);
    }

    public void setContext(Object statemachine, Object modelElement) {
        impl.setContext(statemachine, modelElement);
    }

}
