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

package org.argouml.application.events;
import org.argouml.application.api.*;
import java.util.*;
import org.apache.commons.logging.*;

public class ArgoEventPump {

    Log logger = org.apache.commons.logging.LogFactory.getLog(ArgoEventPump.class.getName());

    private ArrayList _listeners = null;

    final static ArgoEventPump SINGLETON = new ArgoEventPump();

    public static ArgoEventPump getInstance() { return SINGLETON; }

    private ArgoEventPump() {
    }

    public static void addListener(ArgoEventListener listener) {
        SINGLETON.doAddListener(ArgoEvent.ANY_EVENT, listener);
    }

    public static void addListener(int event, ArgoEventListener listener) {
        SINGLETON.doAddListener(event, listener);
    }

    public static void removeListener(ArgoEventListener listener) {
        SINGLETON.doRemoveListener(ArgoEvent.ANY_EVENT, listener);
    }

    public static void removeListener(int event, ArgoEventListener listener) {
        SINGLETON.doRemoveListener(event, listener);
    }

    protected void doAddListener(int event, ArgoEventListener listener) {
        if (_listeners == null) _listeners = new ArrayList();
	_listeners.add(new Pair(event, listener));
    }

    protected void doRemoveListener(int event, ArgoEventListener listener) {
        if (_listeners == null) return;
    }


    private void handleFireModuleEvent(ArgoModuleEvent event,
                                       ArgoModuleEventListener listener) {
	switch (event.getEventType()) {
	    case ArgoEvent.MODULE_LOADED:
	        listener.moduleLoaded(event);
		break;

	    case ArgoEvent.MODULE_UNLOADED:
	        listener.moduleUnloaded(event);
		break;

	    case ArgoEvent.MODULE_ENABLED:
	        listener.moduleEnabled(event);
		break;

	    case ArgoEvent.MODULE_DISABLED:
	        listener.moduleDisabled(event);
		break;

	    default:
	        Argo.logger.error("Invalid event:" + event.getEventType());
		break;
	}
    }

    private void handleFireNotationEvent(ArgoNotationEvent event,
                                       ArgoNotationEventListener listener) {
	switch (event.getEventType()) {
	    case ArgoEvent.NOTATION_CHANGED:
	        listener.notationChanged(event);
		break;

	    case ArgoEvent.NOTATION_ADDED:
	        listener.notationAdded(event);
		break;

	    case ArgoEvent.NOTATION_REMOVED:
	        listener.notationRemoved(event);
		break;

	    case ArgoEvent.NOTATION_PROVIDER_ADDED:
	        listener.notationProviderAdded(event);
		break;

	    case ArgoEvent.NOTATION_PROVIDER_REMOVED:
	        listener.notationProviderRemoved(event);
		break;

	    default:
	        Argo.logger.error("Invalid event:" + event.getEventType());
		break;
	}
    }

    private void handleFireEvent(ArgoEvent event,
                                 ArgoEventListener listener) {
	if (event.getEventType() == ArgoEvent.ANY_EVENT) {
            if (listener instanceof ArgoModuleEventListener) {
	        handleFireModuleEvent((ArgoModuleEvent)event,
	                              (ArgoModuleEventListener)listener);
            }
            if (listener instanceof ArgoNotationEventListener) {
	        handleFireNotationEvent((ArgoNotationEvent)event,
	                              (ArgoNotationEventListener)listener);
            }
	}
	else {
	    if (event.getEventType() >= ArgoEvent.ANY_MODULE_EVENT &&
	            event.getEventType() < ArgoEvent.ANY_MODULE_EVENT + 100) {
                if (listener instanceof ArgoModuleEventListener) {
	            handleFireModuleEvent((ArgoModuleEvent)event,
	                                  (ArgoModuleEventListener)listener);
                }
	    }
	    if (event.getEventType() >= ArgoEvent.ANY_NOTATION_EVENT &&
	            event.getEventType() < ArgoEvent.ANY_NOTATION_EVENT + 100) {
                if (listener instanceof ArgoNotationEventListener) {
	            handleFireNotationEvent((ArgoNotationEvent)event,
	                                  (ArgoNotationEventListener)listener);
                }
	    }
	}
    }

    public static void fireEvent(ArgoEvent event) {
        SINGLETON.doFireEvent(event);
    }

    protected void doFireEvent(ArgoEvent event) {

        if (_listeners == null) {
	    return;
	}

        ListIterator iterator = _listeners.listIterator();
        while (iterator.hasNext()) {
	     Pair pair = (Pair)iterator.next();
	     if (pair.getEventType() == ArgoEvent.ANY_EVENT) {
		 handleFireEvent(event, pair.getListener());
	     }
	     else if ((pair.getEventType() >= event.getEventStartRange()) &&
	             (pair.getEventType() <= event.getEventEndRange())) {
		 handleFireEvent(event, pair.getListener());
	     }
        }

    }

    class Pair {
        int _eventType;
	ArgoEventListener _listener;
        Pair(int eventType, ArgoEventListener listener) {
	    _eventType = eventType;
	    _listener = listener;
	}

	int getEventType() { return _eventType; }
	ArgoEventListener getListener() { return _listener; }

	public String toString() {
	    return "{Pair(" + _eventType + "," + _listener + ")}";
	}
    }
}
