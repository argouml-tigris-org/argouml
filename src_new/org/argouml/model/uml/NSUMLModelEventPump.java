// $Id$
// Copyright (c) 2004-2005 The Regents of the University of California. All
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

package org.argouml.model.uml;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;
import org.argouml.model.AbstractModelEventPump;
import org.argouml.model.ModelEventPump;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;


/**
 * This is the ModelEventPump implementation for NSUML. It relies heavily
 * on the {@link UmlModelEventPump}.<p>
 *
 * The default visibility is to guarantee that it is not seen outside the
 * model component.
 *
 * @author Linus Tolke
 */
class NSUMLModelEventPump
	extends AbstractModelEventPump
	implements ModelEventPump {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(NSUMLModelEventPump.class);

    private Map modelEventListeners;
    private Map classEventListeners;

    /**
     * Constructor for the NSUMLModelEventPump.<p>
     *
     * The default visibility is to guarantee that it is not seen outside the
     * model component.
     *
     * TODO: Don't make available.
     */
    public NSUMLModelEventPump() {
        super();
        modelEventListeners = new WeakHashMap();
        classEventListeners = new WeakHashMap();
    }


    /**
     * @see org.argouml.model.ModelEventPump#addModelEventListener(
     * 		java.beans.PropertyChangeListener, java.lang.Object,
     * 		java.lang.String[])
     */
    public void addModelEventListener(PropertyChangeListener listener,
            			      Object modelelement,
            			      String[] eventNames) {
        LOG.debug("addModelEventListener("
                  + listener + ", "
                  + modelelement + ", "
                  + eventNames + ")");

        register(modelEventListeners,
                 new NSUMLModelEventListener(listener,
					     modelelement, eventNames));
    }

    /**
     * @param listeners
     * @param relay
     */
    private void register(Map listeners, NSUMLEventListener relay) {
        PropertyChangeListener listener = relay.getListener();
        List list = (List) listeners.get(listener);
        if (list == null) {
            list = new ArrayList();
        }
        list.add(relay);
        listeners.put(listener, list);
    }


    /**
     * @see org.argouml.model.ModelEventPump#addModelEventListener(
     * 		java.beans.PropertyChangeListener, java.lang.Object)
     */
    public void addModelEventListener(PropertyChangeListener listener,
            			      Object modelelement) {
        LOG.debug("addModelEventListener("
                  + listener + ", "
                  + modelelement + ")");
        register(modelEventListeners,
                 new NSUMLModelEventListener(listener, modelelement));
    }

    /**
     * @see org.argouml.model.ModelEventPump#removeModelEventListener(
     * 		java.beans.PropertyChangeListener, java.lang.Object,
     * 		java.lang.String[])
     */
    public void removeModelEventListener(PropertyChangeListener listener,
            				 Object modelelement,
            				 String[] eventNames) {
        LOG.debug("removeModelEventListener("
                  + listener + ", "
                  + modelelement + ", "
                  + eventNames + ")");
        NSUMLEventListener relay = find(modelEventListeners,
                                        listener, modelelement, eventNames);
        if (relay != null) {
            unregister(modelEventListeners, relay);
            relay.delete();
        }
    }

    /**
     * @param listeners
     * @param listener
     * @param modelelement
     * @param eventNames
     * @return the found relay.
     */
    private NSUMLEventListener find(Map listeners,
				    PropertyChangeListener listener,
				    Object modelelement,
				    String[] eventNames) {
        List list = (List) listeners.get(listener);
        if (list == null) {
            return null;
        }
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            NSUMLEventListener theListener = (NSUMLEventListener) iter.next();

            if (theListener.match(modelelement, eventNames)) {
                return theListener;
            }
        }
        return null;
    }

    /**
     * @see org.argouml.model.ModelEventPump#removeModelEventListener(
     * 		java.beans.PropertyChangeListener, java.lang.Object)
     */
    public void removeModelEventListener(PropertyChangeListener listener,
            				 Object modelelement) {
        LOG.debug("removeModelEventListener("
                  + listener + ", "
                  + modelelement + ")");
        NSUMLEventListener relay = find(modelEventListeners,
                			listener, modelelement, null);
        if (relay != null) {
            unregister(modelEventListeners, relay);
            relay.delete();
        }
    }

    /**
     * @param listeners
     * @param relay
     */
    private void unregister(Map listeners, NSUMLEventListener relay) {
        List list = (List) listeners.get(relay.getListener());
        if (list == null) {
            return;
        }
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            NSUMLEventListener theListener = (NSUMLEventListener) iter.next();

            if (theListener == relay) {
                iter.remove();
                return;
            }
        }
    }


    /**
     * @see org.argouml.model.ModelEventPump#addClassModelEventListener(
     * 		java.beans.PropertyChangeListener, java.lang.Object,
     * 		java.lang.String[])
     */
    public void addClassModelEventListener(PropertyChangeListener listener,
            				   Object modelClass,
            				   String[] eventNames) {
        LOG.debug("addClassModelEventListener("
                  + listener + ", "
                  + modelClass + ", "
                  + eventNames + ")");
        register(classEventListeners,
                 new NSUMLClassEventListener(listener, modelClass, eventNames));
    }

    /**
     * @see org.argouml.model.ModelEventPump#removeClassModelEventListener(
     * 		java.beans.PropertyChangeListener, java.lang.Object,
     * 		java.lang.String[])
     */
    public void removeClassModelEventListener(PropertyChangeListener listener,
            				      Object modelClass,
            				      String[] eventNames) {
        LOG.debug("removeClassModelEventListener("
                  + listener + ", "
                  + modelClass + ", "
                  + eventNames + ")");
        NSUMLEventListener relay = find(classEventListeners,
		listener, modelClass, null);
        if (relay != null) {
            unregister(classEventListeners, relay);
            relay.delete();
        }
    }
}


/**
 * This is the base class of relays of events.
 *
 * @author Linus Tolke
 */
abstract class NSUMLEventListener implements MElementListener {
    /**
     * Contains a WeakReference.
     */
    private Reference listenerRef;
    private Object element;
    private String[] events;

    /**
     * Constructor for the NSUMLEventListener.
     *
     * @param l The PropertyChangeListener.
     * @param e The object we are monitoring, can be a class.
     * @param evs The strings that we are interested in.
     */
    NSUMLEventListener(PropertyChangeListener l, Object e, String[] evs) {
        listenerRef = new WeakReference(l);
        element = e;
        events = evs;
    }

    /**
     * Returns the listener or <code>null</code> if the listener is
     * already Garbage collected.
     *
     * @return Returns the listener.
     */
    PropertyChangeListener getListener() {
        PropertyChangeListener pcl = (PropertyChangeListener) listenerRef.get();
        if (pcl == null) {
            delete();
        }
        return pcl;
    }

    /**
     * @return Returns the element.
     */
    Object getElement() {
        return element;
    }
    /**
     * @return Returns the events.
     */
    String[] getEvents() {
        return events;
    }

    /**
     * Unregister this event listener and remove all information about it.
     */
    public abstract void delete();

    /**
     * Returns <code>true</code> if this event listener matches the object
     * and event names.
     *
     * @param e The object to match.
     * @param evs The event names to match.
     * @return true if matching.
     */
    public boolean match(Object e, String[] evs) {
        if (getElement() != e) {
            return false;
        }
        if (evs == getEvents()) {
            return true;
        }
        if (evs == null) {
            return false;
        }
        if (getEvents() == null) {
            return false;
        }
        if (evs.length != getEvents().length) {
            return false;
        }
        for (int i = 0; i < evs.length; i++) {
            int j;
            for (j = 0; j < getEvents().length; j++) {
                if (evs[i].equals(getEvents()[j])) {
                    break; // Found!
                }
            }
            if (!(j < getEvents().length)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Send an event to the listener (if any).
     *
     * @param pce The event to send.
     */
    private void fire(PropertyChangeEvent pce) {
        PropertyChangeListener pcl = getListener();
        if (pcl != null) {
            pcl.propertyChange(pce);
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(
     *         ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent arg0) {
        fire(new PropertyChangeEvent(arg0.getSource(), arg0.getName(),
                                     arg0.getOldValue(), arg0.getNewValue()));
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(
     *         ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent arg0) {
        fire(new PropertyChangeEvent(arg0.getSource(), arg0.getName(),
                		     arg0.getOldValue(), arg0.getNewValue()));
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(
     *         ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent arg0) {
        fire(new PropertyChangeEvent(arg0.getSource(), arg0.getName(),
                		     arg0.getOldValue(), arg0.getNewValue()));
    }

    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(
     *         ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent arg0) {
        fire(new PropertyChangeEvent(arg0.getSource(), arg0.getName(),
                		     arg0.getOldValue(), arg0.getNewValue()));
    }

    /**
     * @see ru.novosoft.uml.MElementListener#removed(
     *         ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent arg0) {
        fire(new PropertyChangeEvent(arg0.getSource(), arg0.getName(),
                		     arg0.getOldValue(), arg0.getNewValue()));
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(
     *         ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent arg0) {
        fire(new PropertyChangeEvent(arg0.getSource(), arg0.getName(),
                		     arg0.getOldValue(), arg0.getNewValue()));
    }
}

/**
 * The adapter listener for model element events.
 */
class NSUMLModelEventListener extends NSUMLEventListener {
    /**
     * @see NSUMLEventListener#NSUMLEventListener(
     *         PropertyChangeListener, Object, String[])
     */
    public NSUMLModelEventListener(PropertyChangeListener l,
				   Object e,
				   String[] ev) {
        super(l, e, ev);

        if (getEvents() == null) {
            UmlModelEventPump.getPump()
		.addModelEventListener(this, getElement());
        } else {
            UmlModelEventPump.getPump()
		.addModelEventListener(this, getElement(), getEvents());
        }
    }

    /**
     * @see NSUMLModelEventListener#NSUMLModelEventListener(
     *         PropertyChangeListener, Object, String[])
     */
    public NSUMLModelEventListener(PropertyChangeListener l, Object e) {
        this(l, e, null);
    }

    /**
     * @see org.argouml.model.uml.NSUMLEventListener#delete()
     */
    public void delete() {
        UmlModelEventPump.getPump()
	    .removeModelEventListener(this, getElement());
    }
}

/**
 * The adapter listener for class events.
 */
class NSUMLClassEventListener extends NSUMLEventListener {
    /**
     * @param l
     * @param modelClass
     */
    public NSUMLClassEventListener(PropertyChangeListener l,
				   Object modelClass,
				   String[] ev) {
        super(l, modelClass, ev);

        UmlModelEventPump.getPump()
	    .addClassModelEventListener(this, getElement(),
					getEvents());
    }

    /**
     * @see org.argouml.model.uml.NSUMLEventListener#delete()
     */
    public void delete() {
        UmlModelEventPump.getPump()
	    .removeClassModelEventListener(this, getElement(),
	            			   getEvents());
    }
}
