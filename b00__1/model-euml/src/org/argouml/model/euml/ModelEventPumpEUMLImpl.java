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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.argouml.model.AbstractModelEventPump;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
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
    
    // Access should be fast
    private Map<Object,List<Listener>> registerForElements = new HashMap<Object,List<Listener>>();
    
    // Iteration should be fast
    private Map<Object,List<Listener>> registerForClasses = new LinkedHashMap<Object,List<Listener>>();

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
	if (!(modelClass instanceof Class && EObject.class
		.isAssignableFrom((Class) modelClass))) {
	    throw new IllegalArgumentException();
	}
	registerListener(modelClass, listener, propertyNames, registerForClasses);
    }

    public void addModelEventListener(PropertyChangeListener listener,
	    Object modelelement, String[] propertyNames) {
	if (!(modelelement instanceof EObject)) {
	    throw new IllegalArgumentException();
	}
	registerListener((EObject) modelelement, listener, propertyNames, registerForElements);
    }

    public void addModelEventListener(PropertyChangeListener listener,
            Object modelelement) {
	addModelEventListener(listener, modelelement, (String []) null);
    }
    
    private void registerListener(Object notifier,
	    PropertyChangeListener listener, String[] propertyNames,
	    Map<Object, List<Listener>> register) {
	if (notifier == null || listener == null) {
	    throw new NullPointerException();
	}
	List<Listener> list = register.get(notifier);
	boolean new_ = false;
	boolean found = false;
	if (list == null) {
	    new_ = true;
	    list = new ArrayList<Listener>();
	} else {
	    for (Listener l : list) {
		if (l.getListener() == listener) {
		    // TODO: Do we really want to add new properties to the already
		    // registered listener or we want to replace the old properties
		    l.addProperties(propertyNames);
		    found = true;
		    break;
		}
	    }
	}
	if (new_ || !found) {
	    list.add(new Listener(listener, propertyNames));
	    register.put(notifier, list);
	}
    }

    public void flushModelEvents() {
        // TODO Auto-generated method stub

    }

    public void removeClassModelEventListener(PropertyChangeListener listener,
            Object modelClass, String[] propertyNames) {
	if (!(modelClass instanceof Class && EObject.class
		.isAssignableFrom((Class) modelClass))) {
	    throw new IllegalArgumentException();
	}
	unregisterListener(modelClass, listener, propertyNames, registerForClasses);
    }

    public void removeModelEventListener(PropertyChangeListener listener,
            Object modelelement, String[] propertyNames) {
	if (!(modelelement instanceof EObject)) {
	    throw new IllegalArgumentException();
	}
	unregisterListener(modelelement, listener, propertyNames, registerForElements);
    }

    public void removeModelEventListener(PropertyChangeListener listener,
            Object modelelement) {
	removeModelEventListener(listener, modelelement, (String []) null);
    }
    
    private void unregisterListener(Object notifier,
	    PropertyChangeListener listener, String[] propertyNames,
	    Map<Object, List<Listener>> register) {
	if (notifier == null || listener == null) {
	    throw new NullPointerException();
	}
	List<Listener> list = register.get(notifier);
	if (list == null) {
	    return;
	}
	Iterator<Listener> iter = list.iterator();
	while (iter.hasNext()) {
	    Listener l = iter.next();
	    if (l.getListener() == listener) {
		if (propertyNames == null) {
		    l.removeProperties(propertyNames);
		} else {
		    iter.remove();
		}
		break;
	    }
	}
    }
    
    /**
     * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(Notification)
     * @param notification The notification event
     */
    @SuppressWarnings("unchecked")
    public void notifyChanged(Notification notification) {
	Object notifier = notification.getNotifier();
	List<Listener> list = registerForElements.get(notifier);
	if (list != null) {
	    for (Listener l : list) {
		fireNotification(notification, l);
	    }
	}
	
	for (Object o : registerForClasses.keySet()) {
	    if (o instanceof Class) {
		Class type = (Class) o;
		if (type.isAssignableFrom(notifier.getClass())) {
		    for (Listener l : registerForClasses.get(o)) {
			fireNotification(notification, l);
		    }
		}
	    }
	}
    }
    
    private void fireNotification(Notification n, Listener l) {
	// TODO: verify the property names && fire the notification
    }

    public void startPumpingEvents() {
	rootContainerAdapter.setDeliverEvents(true);
    }

    public void stopPumpingEvents() {
	rootContainerAdapter.setDeliverEvents(false);
    }
    
}
