// $Id$
/*******************************************************************************
 * Copyright (c) 2007,2010 Bogdan Pistol and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bogdan Pistol - initial API and implementation
 *******************************************************************************/
package org.argouml.model.euml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.util.EContentAdapter;

/**
 * Maintains a list of notifiers descendants of the root container that are
 * firing events to this adapter.
 * 
 * @author Bogdan Pistol
 */
public class RootContainerAdapter extends EContentAdapter {

    private List<Notifier> notifiers = new ArrayList<Notifier>();
    
    private List<Notification> events = new ArrayList<Notification>();

    private Notifier rootContainer;
    
    private ModelEventPumpEUMLImpl pump;
    
    private boolean deliverEvents = true;
    
    private boolean holdEvents = false;
    
    /**
     * Constructor
     * @param pump The ModelEventPump instance
     */
    public RootContainerAdapter(ModelEventPumpEUMLImpl pump) {
        super();
        this.pump = pump;
    }
    
    /**
     * Stop or start the firing of events.
     * @param value True for delivering events, false otherwise
     */
    public void setDeliverEvents(boolean value) {
        deliverEvents = value;
    }
    
    /**
     * Setter for the root container
     * @param n the new root container
     */
    public void setRootContainer(Notifier n) {
        if (n == rootContainer) {
            return;
        }

        removeAllAdapters();
        if (n != null) {
            rootContainer = n;
            rootContainer.eAdapters().add(this);
        }
    }

    @Override
    protected void addAdapter(Notifier notifier) {
        notifiers.add(notifier);
        super.addAdapter(notifier);
    }

    @Override
    protected void removeAdapter(Notifier notifier) {
        notifiers.remove(notifier);
        super.removeAdapter(notifier);
    }

    /**
     * Removes this listener from all the notifiers' eAdapters list.
     */
    public void removeAllAdapters() {
        List<Notifier> notifiersToRemove = new ArrayList<Notifier>(notifiers);
        for (Notifier n : notifiersToRemove) {
            super.removeAdapter(n);
        }
        if (rootContainer != null) {
            super.removeAdapter(rootContainer);
            rootContainer = null;
        }
        notifiers.clear();
    }

    @Override
    public void notifyChanged(Notification notification) {
        super.notifyChanged(notification);
        if (deliverEvents) {
            if (holdEvents) {
                events.add(notification);                
            } else {
                pump.notifyChanged(notification);
            }
        }
    }
    
    /**
     * Clears all the events held until now
     */
    public void clearHeldEvents() {
        events.clear();
    }
    
    /**
     * Determine if the events should be delivered when they arrive or to wait until holdEvents is false
     * @param value the holdEvents value
     */
    public void setHoldEvents(boolean value) {
        if (value == false) {
            if (deliverEvents) {
                for (Notification n : events) {
                    pump.notifyChanged(n);
                }
            }
            events.clear();
        }
        holdEvents = value;
    }

}
