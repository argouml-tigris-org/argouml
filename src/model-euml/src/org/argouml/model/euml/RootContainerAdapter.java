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
    
    private Notifier rootContainer;
    
    private ModelEventPumpEUMLImpl pump;
    
    private boolean deliverEvents = true;
    
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
        for (Notifier n : notifiers) {
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
            pump.notifyChanged(notification);
        }
    }

}
