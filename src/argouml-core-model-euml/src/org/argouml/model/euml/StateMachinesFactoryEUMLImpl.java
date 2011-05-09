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

import java.util.List;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.Model;
import org.argouml.model.StateMachinesFactory;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
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

    public Object buildTransition(
            Object owningState, Object source, Object dest) {
        
        if (!(source instanceof Vertex) || !(dest instanceof Vertex)) {
            throw new IllegalArgumentException(
                    "The source and dest must both be vertices. Source=" //$NON-NLS-1$
                    + source + " dest=" + dest); //$NON-NLS-1$
        }
        
        if (!(owningState instanceof StateMachine)
                && !(owningState instanceof State)
                && !(owningState instanceof Region)) {
            throw new IllegalArgumentException(
                    "Did not expect a " + owningState); //$NON-NLS-1$
        }
        
        final Object region;
        if (owningState instanceof StateMachine || owningState instanceof State) {
            List regions = Model.getStateMachinesHelper().getRegions(owningState);
            if (regions.isEmpty()) {
                region = Model.getUmlFactory().buildNode(
                        Model.getMetaTypes().getRegion(), owningState);
            } else {
                region = regions.get(0);
            }
        } else {
            region = (Region) owningState;
        }
        
        Transition transition = createTransition();
        transition.setSource((Vertex) source);
        transition.setTarget((Vertex) dest);
        transition.setContainer((Region) region);
        return transition;
    }

    public Object buildTransition(Object source, Object target) {
        if (!(source instanceof Vertex) || !(target instanceof Vertex)) {
            throw new IllegalArgumentException();
        }
        
        Region region = ((Vertex) source).getContainer();
        return buildTransition(region, source, target);
    }

    public Object createCallEvent() {
        return UMLFactory.eINSTANCE.createCallEvent();
    }

    public Object createChangeEvent() {
        return UMLFactory.eINSTANCE.createChangeEvent();
    }

    public Object createCompositeState() {
        // A composite state contains at least one region
        State state = UMLFactory.eINSTANCE.createState();
        Region region = UMLFactory.eINSTANCE.createRegion();
        state.getRegions().add(region);
        return state;
    }

    public Object createFinalState() {
        return UMLFactory.eINSTANCE.createFinalState();
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
        return UMLFactory.eINSTANCE.createState();
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
