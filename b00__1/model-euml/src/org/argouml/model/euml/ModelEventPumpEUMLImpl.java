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

import java.beans.PropertyChangeListener;

import org.argouml.model.ModelEventPump;
import org.eclipse.emf.common.notify.Notifier;

/**
 * The implementation of the ModelEventPump for EUML2.
 */
class ModelEventPumpEUMLImpl implements ModelEventPump {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;
    
    private Notifier rootContainer;
    
    private RootContainerAdapter rootContainerAdapter;

    /**
     * Constructor.
     * 
     * @param implementation
     *            The ModelImplementation.
     */
    public ModelEventPumpEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }
    
    public void setRootContainer(Notifier container) {
	if (rootContainerAdapter == null) {
	    rootContainerAdapter = new RootContainerAdapter();
	}
	rootContainerAdapter.setRootContainer(container);
    }

    public void addClassModelEventListener(PropertyChangeListener listener,
            Object modelClass, String[] propertyNames) {
        // TODO Auto-generated method stub

    }

    public void addClassModelEventListener(PropertyChangeListener listener,
            Object modelClass, String propertyName) {
        // TODO Auto-generated method stub

    }

    public void addModelEventListener(PropertyChangeListener listener,
            Object modelelement, String[] propertyNames) {
        // TODO Auto-generated method stub

    }

    public void addModelEventListener(PropertyChangeListener listener,
            Object modelelement, String propertyName) {
        // TODO Auto-generated method stub

    }

    public void addModelEventListener(PropertyChangeListener listener,
            Object modelelement) {
        // TODO Auto-generated method stub

    }

    public void flushModelEvents() {
        // TODO Auto-generated method stub

    }

    public void removeClassModelEventListener(PropertyChangeListener listener,
            Object modelClass, String[] propertyNames) {
        // TODO Auto-generated method stub

    }

    public void removeClassModelEventListener(PropertyChangeListener listener,
            Object modelClass, String propertyName) {
        // TODO Auto-generated method stub

    }

    public void removeModelEventListener(PropertyChangeListener listener,
            Object modelelement, String[] propertyNames) {
        // TODO Auto-generated method stub

    }

    public void removeModelEventListener(PropertyChangeListener listener,
            Object modelelement, String propertyName) {
        // TODO Auto-generated method stub

    }

    public void removeModelEventListener(PropertyChangeListener listener,
            Object modelelement) {
        // TODO Auto-generated method stub

    }

    public void startPumpingEvents() {
        // TODO Auto-generated method stub

    }

    public void stopPumpingEvents() {
        // TODO Auto-generated method stub

    }

}
