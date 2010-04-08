// $Id$
/*******************************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial framework 
 *******************************************************************************/
package org.argouml.model.euml;

import java.util.Collection;

import org.argouml.model.StateMachinesHelper;
import org.eclipse.uml2.uml.State;

/**
 * The implementation of the StateMachinesHelper for EUML2.
 */
class StateMachinesHelperEUMLImpl implements StateMachinesHelper {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     * 
     * @param implementation
     *            The ModelImplementation.
     */
    public StateMachinesHelperEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public void addDeferrableEvent(Object state, Object deferrableEvent) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void addSubvertex(Object handle, Object subvertex) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object findNamespaceForEvent(Object trans, Object model) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object findOperationByName(Object trans, String opname) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Collection getAllPossibleStatemachines(Object model,
            Object oSubmachineState) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Collection getAllPossibleSubvertices(Object oState) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Collection getAllSubStates(Object compState) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object getDestination(Object trans) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Collection getOutgoingStates(Object ostatevertex) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public String getPath(Object o) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object getSource(Object trans) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object getStateMachine(Object handle) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object getStatebyName(String path, Object container) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object getTop(Object sm) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public boolean isAddingStatemachineAllowed(Object context) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public boolean isTopState(Object o) {
        // TODO: This needs to be double checked. - tfm
        return o instanceof State && ((State) o).getOwner() == null;
    }

    public void removeDeferrableEvent(Object state, Object deferrableEvent) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void removeSubvertex(Object handle, Object subvertex) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setBound(Object handle, int bound) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setChangeExpression(Object handle, Object value) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setConcurrent(Object handle, boolean concurrent) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setContainer(Object handle, Object compositeState) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setContext(Object statemachine, Object modelElement) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setDoActivity(Object handle, Object value) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setEffect(Object handle, Object value) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setEntry(Object handle, Object value) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setEventAsTrigger(Object transition, Object event) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setExit(Object handle, Object value) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setExpression(Object handle, Object value) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setGuard(Object handle, Object guard) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setInternalTransitions(Object handle, Collection intTrans) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setReferenceState(Object o, String referenced) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setSource(Object handle, Object state) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setState(Object handle, Object element) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setStateMachine(Object handle, Object stm) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setStatemachineAsSubmachine(Object oSubmachineState,
            Object oStatemachine) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setSubvertices(Object handle, Collection subvertices) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setTrigger(Object handle, Object event) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public void setWhen(Object handle, Object value) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

}
