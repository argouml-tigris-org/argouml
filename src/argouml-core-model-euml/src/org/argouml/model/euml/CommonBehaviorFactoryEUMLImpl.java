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
import org.argouml.model.CommonBehaviorFactory;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * The implementation of the CommonBehaviorFactory for EUML2.
 */
class CommonBehaviorFactoryEUMLImpl implements CommonBehaviorFactory,
        AbstractModelFactory {

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
        // TODO: Auto-generated method stub
        return null;
    }

    public Object buildCallAction(Object oper, String name) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object buildLink(Object fromInstance, Object toInstance) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object buildReception(Object aClassifier) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object buildSignal(Object element) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object buildStimulus(Object link) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object buildUninterpretedAction(Object actionState) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createActionSequence() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createArgument() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createAttributeLink() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createCallAction() {

        // TODO: Auto-generated method stub
        return null;
    }

    public Object createComponentInstance() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createCreateAction() {
        return UMLFactory.eINSTANCE.createCreateObjectAction();
    }

    public Object createDataValue() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createDestroyAction() {
        return UMLFactory.eINSTANCE.createDestroyObjectAction();
    }

    public Object createException() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createLink() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createLinkEnd() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createLinkObject() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createNodeInstance() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createObject() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createReception() {
        return UMLFactory.eINSTANCE.createReception();
    }

    public Object createReturnAction() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createSendAction() {
        return UMLFactory.eINSTANCE.createSendObjectAction();
    }

    public Signal createSignal() {
        return UMLFactory.eINSTANCE.createSignal();
    }

    public Object createStimulus() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createSubsystemInstance() {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object createTerminateAction() {
        // TODO: Auto-generated method stub
        return null;
    }

    public OpaqueAction createUninterpretedAction() {
        // TODO: Auto-generated method stub
        return null;
    }


}
