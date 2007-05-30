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

import org.argouml.model.CommonBehaviorFactory;

/**
 * The implementation of the CommonBehaviorFactory for EUML2.
 */
class CommonBehaviorFactoryEUMLImpl implements CommonBehaviorFactory {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param implementation The ModelImplementation.
     */
    public CommonBehaviorFactoryEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public Object buildAction(Object message) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildCallAction(Object oper, String name) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildLink(Object fromInstance, Object toInstance) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildReception(Object aClassifier) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildSignal(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildStimulus(Object link) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object buildUninterpretedAction(Object actionState) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createActionSequence() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createArgument() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createAttributeLink() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createCallAction() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createComponentInstance() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createCreateAction() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createDataValue() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createDestroyAction() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createException() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createLink() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createLinkEnd() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createLinkObject() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createNodeInstance() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createObject() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createReception() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createReturnAction() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createSendAction() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createSignal() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createStimulus() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createSubsystemInstance() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createTerminateAction() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object createUninterpretedAction() {
        // TODO Auto-generated method stub
        return null;
    }


}
