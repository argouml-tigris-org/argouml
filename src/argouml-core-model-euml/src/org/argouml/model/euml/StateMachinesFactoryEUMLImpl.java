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

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.StateMachinesFactory;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.Vertex;

/**
 * The implementation of the StateMachinesFactory for EUML2.
 */
class StateMachinesFactoryEUMLImpl implements StateMachinesFactory,
        AbstractModelFactory {

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
    public StateMachinesFactoryEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public Object buildCallEvent(Object ns) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildCallEvent(Object trans, String name, Object ns) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildChangeEvent(Object ns) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildChangeEvent(String s, Object ns) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildCompositeState(Object compositeState) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildCompositeStateOnStateMachine(Object statemachine) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildFinalState(Object compositeState) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildGuard(Object transition) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildInternalTransition(Object state) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildPseudoState(Object compositeState) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildSignalEvent(Object ns) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildSignalEvent(String name, Object ns) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildSimpleState(Object compositeState) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildStateMachine(Object oContext) {
        BehavioredClassifier bc = (BehavioredClassifier) oContext;
        StateMachine machine = (StateMachine) createStateMachine();
        bc.setClassifierBehavior(machine);
        
        return machine;
    }

    public Object buildStubState(Object compositeState) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildSubmachineState(Object compositeState) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildSynchState(Object compositeState) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildTimeEvent(Object ns) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildTimeEvent(String s, Object ns) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildTransition(Object owningState, Object source, Object dest) {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object buildTransition(Object source, Object target) {
        if (source instanceof Vertex && target instanceof Vertex) {
            Transition trans = createTransition();
            trans.setSource((Vertex) source);
            trans.setTarget((Vertex) target);
            return trans;
        }
        throw new IllegalArgumentException();
    }

    public Object createCallEvent() {
        return UMLFactory.eINSTANCE.createCallEvent();
    }

    public Object createChangeEvent() {
        return UMLFactory.eINSTANCE.createChangeEvent();
    }

    public Object createCompositeState() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object createFinalState() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object createGuard() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object createPseudostate() {
        return UMLFactory.eINSTANCE.createPseudostate();
    }

    public Object createSignalEvent() {
        return UMLFactory.eINSTANCE.createSignalEvent();
    }

    public Object createSimpleState() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object createStateMachine() {
        return UMLFactory.eINSTANCE.createStateMachine();
    }

    public Object createStubState() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object createSubmachineState() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object createSynchState() {
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

    }

    public Object createTimeEvent() {
        return UMLFactory.eINSTANCE.createTimeEvent();
    }

    public Transition createTransition() {
        return UMLFactory.eINSTANCE.createTransition();
    }

}
