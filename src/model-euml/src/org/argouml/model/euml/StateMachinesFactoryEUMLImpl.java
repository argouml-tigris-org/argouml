// $Id$
// Copyright (c) 2007, The ArgoUML Project
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//     * Neither the name of the ArgoUML Project nor the
//       names of its contributors may be used to endorse or promote products
//       derived from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE ArgoUML PROJECT ``AS IS'' AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE ArgoUML PROJECT BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.argouml.model.euml;

import org.argouml.model.StateMachinesFactory;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * The implementation of the StateMachinesFactory for EUML2.
 */
class StateMachinesFactoryEUMLImpl implements StateMachinesFactory {

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
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

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
        // TODO: Auto-generated method stub
        throw new NotYetImplementedException();

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

    public Object createTransition() {
        return UMLFactory.eINSTANCE.createTransition();
    }

}
