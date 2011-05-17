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
import java.util.List;

import org.argouml.model.CommonBehaviorHelper;
import org.eclipse.uml2.uml.InstanceSpecification;

/**
 * Eclipse UML2 implementation of CommonBehaviorHelper.
 */
class CommonBehaviorHelperEUMLImpl implements CommonBehaviorHelper {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param implementation The ModelImplementation.
     */
    public CommonBehaviorHelperEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public void addAction(Object handle, Object action) {
        // TODO: Auto-generated method stub
        
    }

    public void addAction(Object handle, int position, Object action) {
        // TODO: Auto-generated method stub
        
    }

    public void addActualArgument(Object handle, Object argument) {
        // TODO: Auto-generated method stub
        
    }

    public void addActualArgument(Object handle, int position, Object argument) {
        // TODO: Auto-generated method stub
        
    }

    public void addClassifier(Object handle, Object classifier) {
        // TODO: Auto-generated method stub
        
    }

    public void addStimulus(Object handle, Object stimulus) {
        // TODO: Auto-generated method stub
        
    }

    public Object getActionOwner(Object handle) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object getDestination(Object link) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object getInstantiation(Object createaction) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object getSource(Object link) {
        // TODO: Auto-generated method stub
        return null;
    }

    public void removeAction(Object handle, Object action) {
        // TODO: Auto-generated method stub
        
    }

    public void removeActualArgument(Object handle, Object argument) {
        // TODO: Auto-generated method stub
        
    }

    public void removeClassifier(Object handle, Object classifier) {
        // TODO: Auto-generated method stub
        
    }

    public void removeContext(Object handle, Object context) {
        // TODO: Auto-generated method stub
        
    }

    public void removeReception(Object handle, Object reception) {
        // TODO: Auto-generated method stub
        
    }

    public void setActualArguments(Object action, List arguments) {
        // TODO: Auto-generated method stub
        
    }

    public void setAsynchronous(Object handle, boolean value) {
        // TODO: Auto-generated method stub
        
    }


    public void setClassifiers(Object handle, Collection classifiers) {
        ((InstanceSpecification) handle).getClassifiers().retainAll(classifiers);
        classifiers.removeAll(((InstanceSpecification) handle).getClassifiers());
        ((InstanceSpecification) handle).getClassifiers().addAll(classifiers);
    }
    
    public void setCommunicationLink(Object handle, Object c) {
        // TODO: Auto-generated method stub
        
    }

    public void setComponentInstance(Object handle, Object c) {
        // TODO: Auto-generated method stub
        
    }

    public void setContexts(Object handle, Collection c) {
        // TODO: Auto-generated method stub
        
    }

    public void setDispatchAction(Object handle, Object value) {
        // TODO: Auto-generated method stub
        
    }

    public void setInstance(Object handle, Object inst) {
        // TODO: Auto-generated method stub
        
    }

    public void setInstantiation(Object createaction, Object instantiation) {
        // TODO: Auto-generated method stub
        
    }

    public void setNodeInstance(Object handle, Object nodeInstance) {
        // TODO: Auto-generated method stub
        
    }

    public void setOperation(Object handle, Object operation) {
        // TODO: Auto-generated method stub
        
    }

    public void setReceiver(Object handle, Object receiver) {
        // TODO: Auto-generated method stub
        
    }

    public void setReception(Object handle, Collection receptions) {
        // TODO: Auto-generated method stub
        
    }

    public void setRecurrence(Object handle, Object expr) {
        // TODO: Auto-generated method stub
        
    }

    public void setScript(Object handle, Object expr) {
        // TODO: Auto-generated method stub
        
    }

    public void setSender(Object handle, Object sender) {
        // TODO: Auto-generated method stub
        
    }

    public void setSignal(Object handle, Object signal) {
        // TODO: Auto-generated method stub
        
    }

    public void setSpecification(Object handle, String specification) {
        // TODO: Auto-generated method stub
        
    }

    public void setTarget(Object handle, Object element) {
        // TODO: Auto-generated method stub
        
    }

    public void setTransition(Object handle, Object trans) {
        // TODO: Auto-generated method stub
        
    }

    public void setValue(Object handle, Object value) {
        // TODO: Auto-generated method stub
        
    }


}
