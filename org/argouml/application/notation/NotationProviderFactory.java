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
// $Id$

// 8 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support
// extension points.


package org.argouml.application.notation;
import org.argouml.application.api.*;
import org.argouml.application.events.*;

import java.util.*;
// import java.beans.*;

// import javax.swing.*;

// import ru.novosoft.uml.foundation.core.*;
// import ru.novosoft.uml.foundation.data_types.*;
// import ru.novosoft.uml.foundation.extension_mechanisms.*;
// import ru.novosoft.uml.behavior.common_behavior.*;
// import ru.novosoft.uml.behavior.activity_graphs.*;
// import ru.novosoft.uml.behavior.state_machines.*;
// import ru.novosoft.uml.behavior.use_cases.*;
// import ru.novosoft.uml.behavior.collaborations.*;
// import ru.novosoft.uml.model_management.*;

// import org.apache.log4j.*;

/** Provides a factory for handling notation providers.
 *
 *  @author Thierry Lach
 *  @since 0.9.4
 */

public class NotationProviderFactory
implements ArgoModuleEventListener {

  static NotationProviderFactory SINGLETON = new NotationProviderFactory();

  public static NotationProviderFactory getInstance() { return SINGLETON; }

  private ArrayList _providers = new ArrayList();
  private NotationProvider _defaultProvider = null;

  private NotationProviderFactory() {
      _providers = new ArrayList();
      ListIterator iterator = Argo.getPlugins(PluggableNotation.class).listIterator();
      while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof NotationProvider) {
	        NotationProvider np = (NotationProvider)o;
                Notation.cat.debug ("added provider:" + np);
                _providers.add(np);
                fireEvent(ArgoEventTypes.NOTATION_PROVIDER_ADDED, np);
	    }
      }
      ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
  }

  /** Remove the notation change listener.
   *  <code>finalize</code> should never happen, but play it safe.
   */
  public void finalize() {
      ArgoEventPump.removeListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
  }


    public NotationProvider getProvider(NotationName nn) {
        NotationName n = (nn == null) ? Notation.getDefaultNotation() : nn;

	Notation.cat.debug ("looking for " + n);
        ListIterator iterator = _providers.listIterator();
        while (iterator.hasNext()) {
            NotationProvider np = (NotationProvider)iterator.next();
	    Notation.cat.debug ("Checking " + np + ", " + np.getNotation());
	    if (np.getNotation().equals(n)) {
	        Notation.cat.debug ("found provider " + np);
	        return np;
	    }
	}
        return getDefaultProvider();
    }

    public ArrayList getProviders() { return _providers; }

    public ArrayList getNotations() {
        ArrayList _notations = new ArrayList();
        ListIterator iterator = _providers.listIterator();
        while (iterator.hasNext()) {
            NotationProvider np = (NotationProvider)iterator.next();
	    _notations.add(np.getNotation());
	}
        return _notations;
    }

    public NotationProvider getDefaultProvider() {
      if (_defaultProvider == null) {
          _defaultProvider = (NotationProvider)org.argouml.uml.generator.GeneratorDisplay.getInstance();
          // TODO:  This must be the provider pointed to by the configuration,
	  // or UML 13 if none.
	  // 
      }
      return _defaultProvider;
    }
 
  public void moduleLoaded(ArgoModuleEvent event) {
    Notation.cat.debug (event);
    if(event.getSource() instanceof NotationProvider) {
        NotationProvider np = (NotationProvider)event.getSource();
        Notation.cat.debug ("added:" + np);
        _providers.add(np);
        fireEvent(ArgoEventTypes.NOTATION_PROVIDER_ADDED, np);
    }
  }

  public void moduleUnloaded(ArgoModuleEvent event) {
  }

  public void moduleEnabled(ArgoModuleEvent event) {
  }

  public void moduleDisabled(ArgoModuleEvent event) {
  }

  private void fireEvent(int eventType,  NotationProvider provider) {
      ArgoEventPump.getInstance().fireEvent(new ArgoNotationEvent(eventType,
                                                                provider));
  }

}
