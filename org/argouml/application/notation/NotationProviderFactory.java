// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

// File: NotationProviderFactory.java
// Classes: NotationProviderFactory
// Original Author: Thierry Lach

package org.argouml.application.notation;

import java.util.ArrayList;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Notation;
import org.argouml.application.api.NotationName;
import org.argouml.application.api.NotationProvider2;
import org.argouml.application.api.PluggableNotation;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoModuleEvent;
import org.argouml.application.events.ArgoModuleEventListener;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.uml.generator.GeneratorDisplay;

/** Provides a factory for handling notation providers.
 *
 *  @author Thierry Lach
 *  @since 0.9.4
 */
public class NotationProviderFactory
    implements ArgoModuleEventListener
{
	/** logger */
    private static final Logger LOG =
		Logger.getLogger(NotationProviderFactory.class);

    private static NotationProviderFactory singleton = 
        new NotationProviderFactory();

    /**
     * @return the singleton
     */
    public static NotationProviderFactory getInstance() { return singleton; }

    private ArrayList providers = new ArrayList();
    private NotationProvider2 defaultProvider = null;

    private NotationProviderFactory() {
	providers = new ArrayList();
	ListIterator iterator =
	    Argo.getPlugins(PluggableNotation.class).listIterator();
	while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof NotationProvider2) {
	        NotationProvider2 np = (NotationProvider2) o;
                LOG.debug ("added provider:" + np);
                providers.add(np);
                fireEvent(ArgoEventTypes.NOTATION_PROVIDER_ADDED, np);
	    }
	}
	ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
	ArgoEventPump.addListener(ArgoEventTypes.ANY_MODULE_EVENT, this);
    }

    /** Remove the notation change listener.
     *  <code>finalize</code> should never happen, but play it safe.
     */
    public void finalize() {
	ArgoEventPump.removeListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
    }


    /**
     * @param nn the name of the notation
     * @return the notation provider class
     */
    public NotationProvider2 getProvider(NotationName nn) {
        NotationName n = (nn == null) ? Notation.getDefaultNotation() : nn;

	LOG.debug ("looking for " + n);
        ListIterator iterator = providers.listIterator();
        while (iterator.hasNext()) {
            NotationProvider2 np = (NotationProvider2) iterator.next();
	    LOG.debug ("Checking " + np + ", " + np.getNotation());
	    if (np.getNotation().equals(n)) {
	        LOG.debug ("found provider " + np);
	        return np;
	    }
	}
        return getDefaultProvider();
    }

    /**
     * @return the list of all providers
     */
    public ArrayList getProviders() { return providers; }

    /**
     * @return the list of all notations
     */
    public ArrayList getNotations() {
        ArrayList nots = new ArrayList();
        ListIterator iterator = providers.listIterator();
        while (iterator.hasNext()) {
            NotationProvider2 np = (NotationProvider2) iterator.next();
	    nots.add(np.getNotation());
	}
        return nots;
    }

    /**
     * @return the default notation provider
     */
    public NotationProvider2 getDefaultProvider() {
	if (defaultProvider == null) {
	    defaultProvider =
		(NotationProvider2) GeneratorDisplay.getInstance();
	    // TODO:  This must be the provider pointed to by the configuration,
	    // or UML 13 if none.
	    //
	}
	return defaultProvider;
    }

    /**
     * @see org.argouml.application.events.ArgoModuleEventListener#moduleLoaded(org.argouml.application.events.ArgoModuleEvent)
     */
    public void moduleLoaded(ArgoModuleEvent event) {
	LOG.debug (event);
	if (event.getSource() instanceof NotationProvider2) {
	    NotationProvider2 np = (NotationProvider2) event.getSource();
	    LOG.debug ("added:" + np);
	    providers.add(np);
	    fireEvent(ArgoEventTypes.NOTATION_PROVIDER_ADDED, np);
	}
    }

    /**
     * @see org.argouml.application.events.ArgoModuleEventListener#moduleUnloaded(org.argouml.application.events.ArgoModuleEvent)
     */
    public void moduleUnloaded(ArgoModuleEvent event) {
    }

    /**
     * @see org.argouml.application.events.ArgoModuleEventListener#moduleEnabled(org.argouml.application.events.ArgoModuleEvent)
     */
    public void moduleEnabled(ArgoModuleEvent event) {
    }

    /**
     * @see org.argouml.application.events.ArgoModuleEventListener#moduleDisabled(org.argouml.application.events.ArgoModuleEvent)
     */
    public void moduleDisabled(ArgoModuleEvent event) {
    }

    private void fireEvent(int eventType,  NotationProvider2 provider) {
	ArgoEventPump.fireEvent(new ArgoNotationEvent(eventType, provider));
    }

}
