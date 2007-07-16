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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.argouml.model.AbstractModelEventPump;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * The implementation of the ModelEventPump for EUML2.
 */
class ModelEventPumpEUMLImpl extends AbstractModelEventPump {

    /**
     * A listener attached to a UML element
     */
    class Listener {
	
	private PropertyChangeListener listener;
	
	private Set<String> props;
	
	Listener(PropertyChangeListener listener, String[] properties) {
	    this.listener = listener;
	    if (properties != null) {
		props = null;
		addProperties(properties);
	    }
	}
	
	void addProperties(String[] properties) {
	    if (properties == null) {
		return;
	    }
	    if (props == null) {
		props = new HashSet<String>();
	    }
	    for (String s : properties) {
		props.add(s);
	    }
	}
	
	void removeProperties(String[] properties) {
	    for (String s : properties) {
		props.remove(s);
	    }
	}
	
	PropertyChangeListener getListener() {
	    return listener;
	}
	
	Set<String> getProperties() {
	    return props;
	}
	
    }
    
    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;
    
    private RootContainerAdapter rootContainerAdapter = new RootContainerAdapter(this);
    
    private Map<Object,List<Listener>> register = new HashMap<Object,List<Listener>>();

    /**
     * Constructor.
     * 
     * @param implementation
     *            The ModelImplementation.
     */
    public ModelEventPumpEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }
    
    /**
     * Setter for the root container
     * @param container
     */
    public void setRootContainer(Notifier container) {
	rootContainerAdapter.setRootContainer(container);
    }

    public void addClassModelEventListener(PropertyChangeListener listener,
            Object modelClass, String[] propertyNames) {
	registerListener(modelClass, listener, propertyNames);
    }

    public void addModelEventListener(PropertyChangeListener listener,
            Object modelelement, String[] propertyNames) {
	registerListener(modelelement, listener, propertyNames);
    }

    public void addModelEventListener(PropertyChangeListener listener,
            Object modelelement) {
	registerListener(modelelement, listener, null);
    }
    
    private void registerListener(Object notifier,
	    PropertyChangeListener listener, String[] propertyNames) {
	if (notifier == null || listener == null) {
	    throw new NullPointerException();
	}
	if (!(notifier instanceof EObject || (notifier instanceof Class && EObject.class
		.isAssignableFrom((Class) notifier)))) {
	    throw new IllegalArgumentException();
	}
	List<Listener> array = register.get(notifier);
	boolean put = false;
	if (array == null) {
	    put = true;
	    array = new ArrayList<Listener>();
	}
	int i = array.indexOf(listener);
	if (i != -1) {
	    // TODO: Do we really want to add new properties to the already
	    // registered listener or we want to replace the old properties
	    array.get(i).addProperties(propertyNames);
	} else {
	    array.add(new Listener(listener, propertyNames));
	}
	if (put) {
	    register.put(notifier, array);
	}
    }

    public void flushModelEvents() {
        // TODO Auto-generated method stub

    }

    public void removeClassModelEventListener(PropertyChangeListener listener,
            Object modelClass, String[] propertyNames) {
	unregisterListener(modelClass, listener, propertyNames);
    }

    public void removeModelEventListener(PropertyChangeListener listener,
            Object modelelement, String[] propertyNames) {
	unregisterListener(modelelement, listener, propertyNames);
    }

    public void removeModelEventListener(PropertyChangeListener listener,
            Object modelelement) {
	unregisterListener(modelelement, listener, null);
    }
    
    private void unregisterListener(Object notifier, PropertyChangeListener listener, String[] propertyNames) {
	if (notifier == null || listener == null) {
	    throw new NullPointerException();
	}
	if (!(notifier instanceof EObject || notifier instanceof EClass)) {
	    throw new IllegalArgumentException();
	}
	List<Listener> array = register.get(notifier);
	if (array == null) {
	    return;
	}
	int i = array.indexOf(listener);
	if (i == -1) {
	    return;
	}
	if (propertyNames == null) {
	    array.remove(i);
	} else {
	    array.get(i).removeProperties(propertyNames);
	}
    }
    
    /**
     * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(Notification)
     * @param notification The notification event
     */
    public void notifyChanged(Notification notification) {
	
    }

    public void startPumpingEvents() {
	rootContainerAdapter.setDeliverEvents(true);
    }

    public void stopPumpingEvents() {
	rootContainerAdapter.setDeliverEvents(false);
    }
    
}
