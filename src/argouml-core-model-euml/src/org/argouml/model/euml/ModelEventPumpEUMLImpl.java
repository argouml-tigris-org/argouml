// $Id$
/***************************************************************************
 * Copyright (c) 2007-2012 Bogdan Pistol and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bogdan Pistol - initial implementation
 *    Thomas Neustupny
 *    Bob Tarling
 *    Laurent Braud
 ***************************************************************************/
package org.argouml.model.euml;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.AbstractModelEventPump;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AssociationChangeListener;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeListener;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.uml2.uml.Element;

/**
 * The implementation of the ModelEventPump for eUML.
 */
class ModelEventPumpEUMLImpl extends AbstractModelEventPump {

    /**
     * A list of model elements that when removed should not create delete
     * events. See issue

     */
    private final List<Element> deleteEventIgnoreList =
        new ArrayList<Element>();

    /**
     * A listener attached to a UML element.
     */
    private class Listener {

        private EventListener listener;

        private Set<String> props;

        Listener(EventListener theListener, String[] properties) {
            listener = theListener;
            if (properties != null) {
                setProperties(properties);
            }
        }

        void setProperties(String[] properties) {
            if (properties == null) {
                props = null;
            } else {
                if (props == null) {
                    props = new HashSet<String>();
                }
                for (String s : properties) {
                    props.add(s);
                }
            }
        }

        void removeProperties(String[] properties) {
            if (props == null) {
                return;
            }
            for (String s : properties) {
                props.remove(s);
            }
        }

        EventListener getListener() {
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

    private RootContainerAdapter rootContainerAdapter =
            new RootContainerAdapter(this);

    // Access should be fast
    private Map<Object, List<Listener>> registerForElements =
            new HashMap<Object, List<Listener>>();

    // Iteration should be fast
    private Map<Object, List<Listener>> registerForClasses =
            new LinkedHashMap<Object, List<Listener>>();

    private Object mutex;

    private static final Logger LOG =
            Logger.getLogger(ModelEventPumpEUMLImpl.class.getName());

    public static final int COMMAND_STACK_UPDATE =
            Notification.EVENT_TYPE_COUNT + 1;

    /**
     * Constructor.
     *
     * @param implementation
     *                The ModelImplementation.
     */
    public ModelEventPumpEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
        mutex = this;
        implementation.getEditingDomain().getCommandStack()
                .addCommandStackListener(new CommandStackListener() {

                    public void commandStackChanged(EventObject event) {
                        notifyChanged(new NotificationImpl(
                                COMMAND_STACK_UPDATE, false, false));
                    }

                });
    }

    /**
     * Setter for the root container
     *
     * @param container
     */
    public void setRootContainer(Notifier container) {
        rootContainerAdapter.setRootContainer(container);
    }

    public RootContainerAdapter getRootContainer() {
        return rootContainerAdapter;
    }

    public void addClassModelEventListener(PropertyChangeListener listener,
            Object modelClass, String[] propertyNames) {
        if (!(modelClass instanceof Class
                && EObject.class.isAssignableFrom((Class) modelClass))) {
            throw new IllegalArgumentException(
                    "The model class must be instance of " //$NON-NLS-1$
                            + "java.lang.Class<EObject>"); //$NON-NLS-1$
        }
        registerListener(
                modelClass, listener, propertyNames, registerForClasses);
    }

    public void addModelEventListener(PropertyChangeListener listener,
            Object modelElement, String[] propertyNames) {
        LOG.log(Level.FINE,
                "Adding a listener {0} to {1} for {2}", //$NON-NLS-1$
                new Object[]{listener, modelElement, propertyNames});
        if (!(modelElement instanceof EObject)) {
            throw new IllegalArgumentException(
                    "The modelelement must be instance " //$NON-NLS-1$
                            + "of EObject. We got " //$NON-NLS-1$
                            + modelElement);
        }
        registerListener(
                modelElement, listener, propertyNames, registerForElements);
    }

    public void addModelEventListener(UmlChangeListener listener,
            Object modelElement, String[] propertyNames) {
        LOG.log(Level.FINE,
                "Adding a listener {0} to {1} for {2}", //$NON-NLS-1$
                new Object[]{listener, modelElement, propertyNames});
        if (!(modelElement instanceof EObject)) {
            throw new IllegalArgumentException(
                    "The modelelement must be instance " //$NON-NLS-1$
                            + "of EObject. We got " //$NON-NLS-1$
                            + modelElement);
        }
        registerListener(
                modelElement, listener, propertyNames, registerForElements);
    }

    public void addModelEventListener(PropertyChangeListener listener,
            Object modelelement) {
        addModelEventListener(listener, modelelement, (String[]) null);
    }

    private void registerListener(Object notifier,
            EventListener listener, String[] propertyNames,
            Map<Object, List<Listener>> register) {
        if (notifier == null || listener == null) {
            throw new NullPointerException(
                    "The model element/class and the " //$NON-NLS-1$
                    + "listener must be non-null."); //$NON-NLS-1$
        }
        synchronized (mutex) {
            List<Listener> list = register.get(notifier);
            boolean found = false;
            if (list == null) {
                list = new ArrayList<Listener>();
                register.put(notifier, list);
            } else {
                for (Listener l : list) {
                    if (l.getListener() == listener) {
                        l.setProperties(propertyNames);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                list.add(new Listener(listener, propertyNames));
            }
        }
    }

    public void flushModelEvents() {
        // TODO: Auto-generated method stub
    }

    public void removeClassModelEventListener(PropertyChangeListener listener,
            Object modelClass, String[] propertyNames) {
        if (!(modelClass instanceof Class && EObject.class
                .isAssignableFrom((Class) modelClass))) {
            throw new IllegalArgumentException();
        }
        unregisterListener(
                modelClass, listener, propertyNames, registerForClasses);
    }

    public void removeModelEventListener(PropertyChangeListener listener,
            Object modelelement, String[] propertyNames) {
        unregisterListener(
                modelelement, listener, propertyNames, registerForElements);
    }

    public void removeModelEventListener(UmlChangeListener listener,
            Object modelelement, String[] propertyNames) {
        unregisterListener(
                modelelement, listener, propertyNames, registerForElements);
    }

    public void removeModelEventListener(PropertyChangeListener listener,
            Object modelelement) {
        removeModelEventListener(listener, modelelement, (String[]) null);
    }

    private void unregisterListener(Object notifier,
            EventListener listener, String[] propertyNames,
            Map<Object, List<Listener>> register) {
        if (notifier == null || listener == null) {
            throw new NullPointerException(
                    "The model element/class and the "  //$NON-NLS-1$
                    + "listener must be non-null."); //$NON-NLS-1$
        }
        synchronized (mutex) {
            List<Listener> list = register.get(notifier);
            if (list == null) {
                return;
            }
            Iterator<Listener> iter = list.iterator();
            while (iter.hasNext()) {
                Listener l = iter.next();
                if (l.getListener() == listener) {
                    if (propertyNames != null) {
                        l.removeProperties(propertyNames);
                        if (l.getProperties().isEmpty()) {
                            iter.remove();
                        }
                    } else {
                        iter.remove();
                    }
                    break;
                }
            }
            if (list.isEmpty()) {
                register.remove(notifier);
            }
        }
    }

    /**
     * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(Notification)
     * @param notification
     *                The notification event
     */
    public void notifyChanged(Notification notification) {

        if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
            return;
        }

        final ENamedElement feature = (ENamedElement) notification.getFeature();

        final String featureName =
            feature == null ? "" : feature.getName(); //$NON-NLS-1$

        final EReference oppositeRef;
        if (feature instanceof EReference) {
            oppositeRef = ((EReference) feature).getEOpposite();
        } else {
            oppositeRef = null;
        }

        fireEvent(
                notification.getNotifier(),
                notification.getOldValue(),
                notification.getNewValue(),
                notification.getEventType(),
                featureName,
                oppositeRef);
    }

    /**
     * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(Notification)
     * @param notification
     *                The notification event
     */
    void fireEvent(
            Object notifier,
            Object oldValue,
            Object newValue,
            int eventType,
            String featureName,
            EReference oppositeRef) {

        LOG.log(Level.FINE,
                "event  - Property: {0} Type : {1} Old: {2} " //$NON-NLS-1$
                + "New: {3} From: {4}", //$NON-NLS-1$
                new Object[] {
                    featureName,
                    eventType,
                    oldValue,
                    newValue,
                    notifier});

        class EventAndListeners {
            public EventAndListeners(PropertyChangeEvent e,
                    List<EventListener> l) {
                event = e;
                listeners = l;
            }

            private PropertyChangeEvent event;

            private List<EventListener> listeners;
        }

        List<EventAndListeners> events = new ArrayList<EventAndListeners>();

        if (eventType == Notification.SET) {
            String propName =
                    mapPropertyName(featureName);
            events.add(new EventAndListeners(new AttributeChangeEvent(
                    notifier, propName,
                    oldValue, newValue,
                    null), getListeners(
                            notifier, propName)));
        } else if (eventType == Notification.ADD
                || eventType == Notification.REMOVE) {
            String propName = mapPropertyName(featureName);
            if (eventType == Notification.ADD) {
                events.add(new EventAndListeners(new AddAssociationEvent(
                        notifier, propName, null,
                        newValue,
                        newValue, null), getListeners(
                                notifier, propName)));
                events.add(new EventAndListeners(new AttributeChangeEvent(
                        notifier, propName, null,
                        newValue, null), getListeners(
                                notifier, propName)));
            } else {
                if (isDeleteEventRequired(oldValue)) {
                    // Changing of a property can result in the property
                    // being removed and added again (eclipse behaviour)
                    // we don't want to mistake this for deletion of the
                    // property. See issue 5853
                    events.add(new EventAndListeners(
                            new DeleteInstanceEvent(
                                    oldValue,
                                    "remove",  //$NON-NLS-1$
                                    null, null, null),
                                    getListeners(
                                        oldValue)));
                } else {
                    LOG.log(Level.INFO,
                            "Not sending delete event - assume remove");
                }
                events.add(new EventAndListeners(
                        new RemoveAssociationEvent(
                                notifier, propName,
                                oldValue, null,
                                oldValue, null),
                        getListeners(
                                notifier, propName)));
                events.add(new EventAndListeners(
                        new AttributeChangeEvent(
                                notifier, propName,
                                oldValue, null, null),
                        getListeners(
                                notifier, propName)));
            }

            if (oppositeRef != null) {
                propName = mapPropertyName(oppositeRef.getName());
                if (eventType == Notification.ADD) {
                    events.add(new EventAndListeners(
                            new AddAssociationEvent(
                                    newValue,
                                    propName, null,
                                    notifier,
                                    notifier, null),
                            getListeners(
                                    newValue,
                                    propName)));
                    events.add(new EventAndListeners(
                            new AttributeChangeEvent(
                                    newValue,
                                    propName, null,
                                    notifier, null),
                            getListeners(
                                    newValue,
                                    propName)));
                } else { // i.e. eventType == Notification.REMOVE
                    events.add(new EventAndListeners(
                            new AddAssociationEvent(
                                    oldValue,
                                    propName,
                                    notifier, null,
                                    notifier, null),
                            getListeners(
                                    oldValue,
                                    propName)));
// Bob says - I don't understand the purpose of sending an attribute change
// event when a delete event has already happened. With this code in place
// it causes the feedback loop mentioned in
// http://argouml.tigris.org/issues/show_bug.cgi?id=6270
// If this code must be re-introduced to solve some other issue then please give
// a full explanation here of what this solves for us.
//                    events.add(new EventAndListeners(
//                            new AttributeChangeEvent(
//                                    oldValue,
//                                    propName,
//                                    notifier, null, null),
//                            getListeners(
//                                    oldValue,
//                                    propName)));
                }
            }
        }

        for (EventAndListeners e : events) {
            if (e.listeners != null) {
                for (EventListener l : e.listeners) {
                    if (l instanceof AssociationChangeListener) {
                        if (e.event instanceof AddAssociationEvent) {
                            ((AssociationChangeListener) l).elementAdded(
                                    (AddAssociationEvent) e.event);
                        } else if (e.event instanceof RemoveAssociationEvent) {
                            ((AssociationChangeListener) l).elementRemoved(
                                    (RemoveAssociationEvent) e.event);
                        }
                    } else if (l instanceof PropertyChangeListener) {
                        ((PropertyChangeListener) l).propertyChange(e.event);
                    }
                }
            }
        }
    }



    /**
     * Determine if we should create a delete event for the given property
     * when EMF tells us it has been removed. This is currently used to
     * work around the problem discussed in issue 5853.
     *
     * @param element
     * @return true if
     */
    private boolean isDeleteEventRequired(
            final Object element) {
        synchronized (deleteEventIgnoreList) {
            if (deleteEventIgnoreList.contains(element)) {
                deleteEventIgnoreList.remove(element);
                return false;
            }
        }
        return true;
    }

    /**
     * Add Element to list which will cause the next delete event to
     * be ignored.
     *
     * @param property
     */
    void addElementForDeleteEventIgnore(Element element) {
        synchronized (deleteEventIgnoreList) {
            deleteEventIgnoreList.add(element);
        }
    }

    private List<EventListener> getListeners(Object element) {
        return getListeners(element, null);
    }

    @SuppressWarnings("unchecked")
    private List<EventListener> getListeners(Object element,
            String propName) {
        List<EventListener> returnedList =
                new ArrayList<EventListener>();

        synchronized (mutex) {
            addListeners(returnedList, element, propName, registerForElements);
            for (Object o : registerForClasses.keySet()) {
                if (o instanceof Class) {
                    Class type = (Class) o;
                    if (type.isAssignableFrom(element.getClass())) {
                        addListeners(
                                returnedList, o, propName, registerForClasses);
                    }
                }
            }
        }
        return returnedList.isEmpty() ? null : returnedList;
    }

    private void addListeners(List<EventListener> listeners,
            Object element, String propName,
            Map<Object, List<Listener>> register) {
        List<Listener> list = register.get(element);
        if (list != null) {
            for (Listener l : list) {
                if (propName == null || l.getProperties() == null
                        || l.getProperties().contains(propName)) {
                    listeners.add(l.getListener());
                }
            }
        }
    }

    public void startPumpingEvents() {
        rootContainerAdapter.setDeliverEvents(true);
    }

    public void stopPumpingEvents() {
        rootContainerAdapter.setDeliverEvents(false);
    }

    private String mapPropertyName(String name) {
        // TODO: map UML2 names to UML1.x names
        if (name.equals("ownedAttribute")) { //$NON-NLS-1$
            return "feature"; //$NON-NLS-1$
        }
        return name;
    }

    @SuppressWarnings("unchecked")
    public List getDebugInfo() {
        List info = new ArrayList();
        info.add("Event Listeners"); //$NON-NLS-1$
        for (Iterator it = registerForElements.entrySet().iterator();
                it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();

            String item = entry.getKey().toString();
            List modelElementNode = newDebugNode(item);
            info.add(modelElementNode);
            List listenerList = (List) entry.getValue();

            Map<String, List<String>> map = new HashMap<String, List<String>>();

            for (Iterator listIt = listenerList.iterator(); listIt.hasNext();) {
                Listener listener = (Listener) listIt.next();

                if (listener.getProperties() != null) {
                    for (String eventName : listener.getProperties()) {
                        if (!map.containsKey(eventName)) {
                            map.put(eventName, new LinkedList<String>());
                        }
                        map.get(eventName).add(
                                listener.getListener().getClass().getName());
                    }
                } else {
                    if (!map.containsKey("")) { //$NON-NLS-1$
                        map.put("", new LinkedList<String>()); //$NON-NLS-1$
                    }
                    map.get("") //$NON-NLS-1$
                            .add(listener.getListener().getClass().getName());
                }
            }
            for (Map.Entry o : map.entrySet()) {
                modelElementNode.add((String) o.getKey());
                modelElementNode.add((List<String>) o.getValue());
            }
        }
        return info;
    }

    private List<String> newDebugNode(String name) {
        List<String> list = new ArrayList<String>();
        list.add(name);
        return list;
    }

}
