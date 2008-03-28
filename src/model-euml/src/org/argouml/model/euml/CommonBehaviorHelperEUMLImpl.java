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

import java.util.Collection;
import java.util.List;

import org.argouml.model.CommonBehaviorHelper;

/**
 * Eclipse UML2 implementatoin of CommonBehaviorHelper.
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
        // TODO Auto-generated method stub
        
    }

    public void addAction(Object handle, int position, Object action) {
        // TODO Auto-generated method stub
        
    }

    public void addActualArgument(Object handle, Object argument) {
        // TODO Auto-generated method stub
        
    }

    public void addActualArgument(Object handle, int position, Object argument) {
        // TODO Auto-generated method stub
        
    }

    public void addClassifier(Object handle, Object classifier) {
        // TODO Auto-generated method stub
        
    }

    public void addStimulus(Object handle, Object stimulus) {
        // TODO Auto-generated method stub
        
    }

    public Object getActionOwner(Object handle) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getDestination(Object link) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getInstantiation(Object createaction) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getSource(Object link) {
        // TODO Auto-generated method stub
        return null;
    }

    public void removeAction(Object handle, Object action) {
        // TODO Auto-generated method stub
        
    }

    public void removeActualArgument(Object handle, Object argument) {
        // TODO Auto-generated method stub
        
    }

    public void removeClassifier(Object handle, Object classifier) {
        // TODO Auto-generated method stub
        
    }

    public void removeContext(Object handle, Object context) {
        // TODO Auto-generated method stub
        
    }

    public void removeReception(Object handle, Object reception) {
        // TODO Auto-generated method stub
        
    }

    public void setActualArguments(Object action, List arguments) {
        // TODO Auto-generated method stub
        
    }

    public void setAsynchronous(Object handle, boolean value) {
        // TODO Auto-generated method stub
        
    }


    public void setClassifiers(Object handle, Collection classifiers) {
        // TODO Auto-generated method stub
    }
    
    public void setCommunicationLink(Object handle, Object c) {
        // TODO Auto-generated method stub
        
    }

    public void setComponentInstance(Object handle, Object c) {
        // TODO Auto-generated method stub
        
    }

    public void setContexts(Object handle, Collection c) {
        // TODO Auto-generated method stub
        
    }

    public void setDispatchAction(Object handle, Object value) {
        // TODO Auto-generated method stub
        
    }

    public void setInstance(Object handle, Object inst) {
        // TODO Auto-generated method stub
        
    }

    public void setInstantiation(Object createaction, Object instantiation) {
        // TODO Auto-generated method stub
        
    }

    public void setNodeInstance(Object handle, Object nodeInstance) {
        // TODO Auto-generated method stub
        
    }

    public void setOperation(Object handle, Object operation) {
        // TODO Auto-generated method stub
        
    }

    public void setReceiver(Object handle, Object receiver) {
        // TODO Auto-generated method stub
        
    }

    public void setReception(Object handle, Collection receptions) {
        // TODO Auto-generated method stub
        
    }

    public void setRecurrence(Object handle, Object expr) {
        // TODO Auto-generated method stub
        
    }

    public void setScript(Object handle, Object expr) {
        // TODO Auto-generated method stub
        
    }

    public void setSender(Object handle, Object sender) {
        // TODO Auto-generated method stub
        
    }

    public void setSignal(Object handle, Object signal) {
        // TODO Auto-generated method stub
        
    }

    public void setSpecification(Object handle, String specification) {
        // TODO Auto-generated method stub
        
    }

    public void setTarget(Object handle, Object element) {
        // TODO Auto-generated method stub
        
    }

    public void setTransition(Object handle, Object trans) {
        // TODO Auto-generated method stub
        
    }

    public void setValue(Object handle, Object value) {
        // TODO Auto-generated method stub
        
    }


}
