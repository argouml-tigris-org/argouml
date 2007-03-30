// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.model.mdr;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.jmi.model.Association;
import javax.jmi.model.AssociationEnd;
import javax.jmi.model.GeneralizableElement;
import javax.jmi.model.ModelElement;
import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofClass;
import javax.jmi.model.NameNotFoundException;
import javax.jmi.reflect.InvalidObjectException;
import javax.jmi.reflect.RefAssociation;
import javax.jmi.reflect.RefObject;

import org.apache.log4j.Logger;
import org.argouml.model.AbstractModelEventPump;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRObject;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.mdr.events.AssociationEvent;
import org.netbeans.api.mdr.events.AttributeEvent;
import org.netbeans.api.mdr.events.InstanceEvent;
import org.netbeans.api.mdr.events.MDRChangeEvent;
import org.netbeans.api.mdr.events.MDRPreChangeListener;
import org.netbeans.api.mdr.events.TransactionEvent;

/**
 * The ModelEventPump for the MDR implementation.<p>
 *
 * This implements three different event dispatching interfaces
 * which support a variety of different types of listener registration.
 * We keep a single event listener registered with the repository
 * for all events and then re-dispatch events to those listeners
 * who have requested them.<p>
 *
 * @since ARGO0.19.5
 * @author Ludovic Ma&icirc;tre
 * @author Tom Morris
 */
class ModelEventPumpMDRImpl extends AbstractModelEventPump implements
        MDRPreChangeListener {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ModelEventPumpMDRImpl.class);

    private MDRModelImplementation modelImplementation;

    private Object lock = new Byte[0];

    private MDRepository repository;

    private int pendingEvents;

    /**
     * Map of Element/attribute tuples and the listeners they have registered.
     */
    private Map elements = Collections.synchronizedMap(new HashMap());

    /**
     * Map of Class/attribute tupes and the listeners they have registered.
     */
    private Map listenedClasses = Collections.synchronizedMap(new HashMap());

    /**
     * Map of subtypes for all types in our metamodel.
     */
    private Map subtypeMap;

    /**
     * Constructor.
     *
     * @param implementation The implementation.
     */
    public ModelEventPumpMDRImpl(MDRModelImplementation implementation) {
        this(implementation, MDRManager.getDefault().getDefaultRepository());
    }

    /**
     * Constructor.
     *
     * @param implementation The implementation.
     * @param repo The repository.
     */
    public ModelEventPumpMDRImpl(MDRModelImplementation implementation,
            MDRepository repo) {
        super();
        modelImplementation = implementation;
        repository = repo;
        subtypeMap = buildTypeMap(modelImplementation.getModelPackage());
    }
    
    /*
     * @see org.argouml.model.AbstractModelEventPump#addModelEventListener(java.beans.PropertyChangeListener,
     *      java.lang.Object, java.lang.String[])
     */
    public void addModelEventListener(PropertyChangeListener listener,
            Object modelElement, String[] propertyNames) {
        if (listener == null) {
            throw new IllegalArgumentException("A listener must be supplied");
        }

        if (modelElement == null) {
            throw new IllegalArgumentException(
                    "A model element must be supplied");
        }

        registerModelEvent(listener, modelElement, propertyNames);
    }

    /*
     * @see org.argouml.model.AbstractModelEventPump#addModelEventListener(java.beans.PropertyChangeListener,
     *      java.lang.Object)
     */
    public void addModelEventListener(PropertyChangeListener listener,
            Object modelElement) {
        if (listener == null) {
            throw new IllegalArgumentException("A listener must be supplied");
        }

        if (modelElement == null) {
            throw new IllegalArgumentException(
                    "A model element must be supplied");
        }

        registerModelEvent(listener, modelElement, null);
    }

    /*
     * @see org.argouml.model.AbstractModelEventPump#removeModelEventListener(java.beans.PropertyChangeListener,
     *      java.lang.Object, java.lang.String[])
     */
    public void removeModelEventListener(PropertyChangeListener listener,
            Object modelelement, String[] propertyNames) {
        unregisterModelEvent(listener, modelelement, propertyNames);
    }

    /*
     * @see org.argouml.model.AbstractModelEventPump#removeModelEventListener(java.beans.PropertyChangeListener,
     *      java.lang.Object)
     */
    public void removeModelEventListener(PropertyChangeListener listener,
            Object modelelement) {
        unregisterModelEvent(listener, modelelement, null);
    }

    /*
     * @see org.argouml.model.AbstractModelEventPump#addClassModelEventListener(java.beans.PropertyChangeListener,
     *      java.lang.Object, java.lang.String[])
     */
    public void addClassModelEventListener(PropertyChangeListener listener,
            Object modelClass, String[] propertyNames) {
        registerClassEvent(listener, modelClass, propertyNames);
    }

    /*
     * @see org.argouml.model.AbstractModelEventPump#removeClassModelEventListener(java.beans.PropertyChangeListener,
     *      java.lang.Object, java.lang.String[])
     */
    public void removeClassModelEventListener(PropertyChangeListener listener,
            Object modelClass, String[] propertyNames) {
        unregisterClassEvent(listener, modelClass, propertyNames);
    }

    /**
     * Detect a change event in MDR and convert this to a change event from the
     * model interface.  We also keep track of the number of pending changes so
     * that we can implement a simple flush interface.<p>
     *
     * The conversions are according to this table.
     * <pre>
     * MDR Event         MDR Event Type            Propogated Event
     *
     * InstanceEvent     EVENT_INSTANCE_DELETE     DeleteInstanceEvent
     * AttributeEvent    EVENT_ATTRIBUTE_SET       AttributeChangeEvent
     * AssociationEvent  EVENT_ASSOCIATION_ADD     AddAssociationEvent
     * AssociationEvent  EVENT_ASSOCIATION_REMOVE  RemoveAssociationEvent
     * </pre>
     * Any other events are ignored and not propogated beyond the model
     * subsystem.
     *
     * @param mdrEvent Change event from MDR
     * @see org.netbeans.api.mdr.events.MDRChangeListener#change
     */
    public void change(MDRChangeEvent mdrEvent) {
        // TODO: This should be done after all events are delivered, but leave
        // it here for now to avoid last minute synchronization problems
        decrementEvents();

        // Quick exit if it's a transaction event
        // (we get a lot of them and they are all ignored)
        if (mdrEvent instanceof TransactionEvent) {
            return;
        }

        Vector events = new Vector();

        if (mdrEvent instanceof AttributeEvent) {
            AttributeEvent ae = (AttributeEvent) mdrEvent;
            events.add(new AttributeChangeEvent(ae.getSource(),
                    ae.getAttributeName(), ae.getOldElement(),
                    ae.getNewElement(), mdrEvent));
        } else if (mdrEvent instanceof InstanceEvent
                && mdrEvent.isOfType(InstanceEvent.EVENT_INSTANCE_DELETE)) {
            InstanceEvent ie = (InstanceEvent) mdrEvent;
            events.add(new DeleteInstanceEvent(ie.getSource(),
                    "remove", null, null, mdrEvent));
        } else if (mdrEvent instanceof AssociationEvent) {
            AssociationEvent ae = (AssociationEvent) mdrEvent;
            if (ae.isOfType(AssociationEvent.EVENT_ASSOCIATION_ADD)) {
                events.add(new AddAssociationEvent(
                        ae.getNewElement(),
                        mapPropertyName(ae.getEndName()),
                        ae.getOldElement(), // will always be null
                        ae.getFixedElement(),
                        ae.getFixedElement(),
                        mdrEvent));
                // Create a change event for the corresponding property
                events.add(new AttributeChangeEvent(
                        ae.getNewElement(),
                        mapPropertyName(ae.getEndName()),
                        ae.getOldElement(), // will always be null
                        ae.getFixedElement(),
                        mdrEvent));
                // Create an event for the other end of the association
                events.add(new AddAssociationEvent(
                        ae.getFixedElement(),
                        otherAssocEnd(ae),
                        ae.getOldElement(), // will always be null
                        ae.getNewElement(),
                        ae.getNewElement(),
                        mdrEvent));
                // and a change event for that end
                events.add(new AttributeChangeEvent(
                        ae.getFixedElement(),
                        otherAssocEnd(ae),
                        ae.getOldElement(), // will always be null
                        ae.getNewElement(),
                        mdrEvent));
            } else if (ae.isOfType(AssociationEvent.EVENT_ASSOCIATION_REMOVE)) {
                events.add(new RemoveAssociationEvent(
                        ae.getOldElement(),
                        mapPropertyName(ae.getEndName()),
                        ae.getFixedElement(),
                        ae.getNewElement(), // will always be null
                        ae.getFixedElement(),
                        mdrEvent));
                // Create a change event for the associated property
                events.add(new AttributeChangeEvent(
                        ae.getOldElement(),
                        mapPropertyName(ae.getEndName()),
                        ae.getFixedElement(),
                        ae.getNewElement(), // will always be null
                        mdrEvent));
                // Create an event for the other end of the association
                events.add(new RemoveAssociationEvent(
                        ae.getFixedElement(),
                        otherAssocEnd(ae),
                        ae.getOldElement(),
                        ae.getNewElement(), // will always be null
                        ae.getOldElement(),
                        mdrEvent));
                // Create a change event for the associated property
                events.add(new AttributeChangeEvent(
                        ae.getFixedElement(),
                        otherAssocEnd(ae),
                        ae.getOldElement(),
                        ae.getNewElement(), // will always be null
                        mdrEvent));
            } else if (ae.isOfType(AssociationEvent.EVENT_ASSOCIATION_SET)) {
                LOG.error("Unexpected EVENT_ASSOCIATION_SET received");
            } else {
                LOG.error("Unknown association event type " + ae.getType());
            }
        } else {
            if (LOG.isDebugEnabled()) {
                String name = mdrEvent.getClass().getName();
                // Cut down on debugging noise
                if (!name.endsWith("CreateInstanceEvent")) {
                    LOG.debug("Ignoring MDR event " + mdrEvent);
                }
            }
        }

        for (int i = 0; i < events.size(); i++) {
            UmlChangeEvent event = (UmlChangeEvent) events.get(i);

            fire(event);
            // Unregister deleted instances after all events have been delivered
            if (event instanceof DeleteInstanceEvent) {
                unregister(elements, null, ((MDRObject) event.getSource())
                        .refMofId(), null);
            }
        }
    }

    /**
     * @param e Event from MDR indicating a planned change.
     * @see org.netbeans.api.mdr.events.MDRPreChangeListener#plannedChange
     */
    public synchronized void plannedChange(MDRChangeEvent e) {
        pendingEvents++;
    }

    /**
     * @param e
     *            MDR event which was announced to plannedChange then
     *            subsequently cancelled.
     * @see org.netbeans.api.mdr.events.MDRPreChangeListener#changeCancelled
     */
    public void changeCancelled(MDRChangeEvent e) {
        decrementEvents();
    }

    /**
     * Decrement count of outstanding events and wake
     * any waiters when it becomes zero.
     */
    private synchronized void decrementEvents() {
        pendingEvents--;
        if (pendingEvents == 0) {
            notifyAll();
        }
    }

    /**
     * Fire an event to any registered listeners.
     */
    private void fire(UmlChangeEvent event) {
        String mofId = ((MDRObject) event.getSource()).refMofId();
        String className  = getClassName(event.getSource());

        // Any given listener is only called once even if it is
        // registered for multiple relevant matches
        Set listeners = new HashSet();
        synchronized (lock) {
            listeners.addAll(getMatches(elements, mofId, event
                    .getPropertyName()));

            // This will include all subtypes registered
            listeners.addAll(getMatches(listenedClasses, className, event
                    .getPropertyName()));
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Firing "
                    + modelImplementation.getMetaTypes().getName(event)
                    + " source "
                    + modelImplementation.getMetaTypes().getName(
                            event.getSource())
                    + " [" + ((MDRObject) event.getSource()).refMofId()
                    + "]."  + event.getPropertyName()
                    + "," + formatElement(event.getOldValue())
                    + "->" + formatElement(event.getNewValue()));
        }

        if (!listeners.isEmpty()) {
            Iterator it = listeners.iterator();
            PropertyChangeListener pcl = null;
            while (it.hasNext()) {
                pcl = (PropertyChangeListener) it.next();
                if (false /*(LOG.isDebugEnabled()*/) {
                    LOG.debug("Firing event on " + pcl.getClass().getName()
                            + "[" + pcl + "]");
                }
                pcl.propertyChange(event);
            }
        } else {
            // For debugging you probably want either this
            // OR the logging for every event which is fired - not both
            if (false/*LOG.isDebugEnabled()*/) {
                LOG.debug("No listener for "
                        + modelImplementation.getMetaTypes().getName(event)
                        + " source "
                        + modelImplementation.getMetaTypes().getName(
                                event.getSource())
                        + " ["
                        + ((MDRObject) event.getSource()).refMofId() + "]."
                        + event.getPropertyName() + "," + event.getOldValue()
                        + "->" + event.getNewValue());
            }
        }
    }


    /**
     * Register a listener for a Model Event.  The ModelElement's
     * MofID is used as the string to match against.
     */
    private void registerModelEvent(PropertyChangeListener listener,
            Object modelElement, String[] propertyNames) {
        if (listener == null || modelElement == null) {
            throw new IllegalArgumentException("Neither listener (" + listener
                    + ") or modelElement (" + modelElement
                    + ") can be null! [Property names: " + propertyNames + "]");
        }

        // Fetch the key before going in synchronized mode
        String mofId = ((MDRObject) modelElement).refMofId();
        verifyAttributeNames(((MDRObject) modelElement).refMetaObject(),
                propertyNames);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Register ["
                    + " element:" + formatElement(modelElement)
                    + ", properties:" + formatArray(propertyNames)
                    + ", listener:" + listener
                    + "]");
        }
        synchronized (lock) {
            register(elements, listener, mofId, propertyNames);
        }
    }

    /**
     * Unregister a listener for a Model Event.
     */
    private void unregisterModelEvent(PropertyChangeListener listener,
            Object modelElement, String[] propertyNames) {
        if (listener == null || modelElement == null) {
            LOG.error("Attempt to unregister null listener(" + listener
                    + ") or modelElement (" + modelElement
                    + ")! [Property names: " + propertyNames + "]");
            return;
        }
        if (!(modelElement instanceof MDRObject)) {
            LOG.error("Ignoring non-MDRObject received by "
                    + "unregisterModelEvent - " + modelElement);
            return;
        }
        String mofId = ((MDRObject) modelElement).refMofId();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Unregister ["
                    + " element:" + formatElement(modelElement)
                    + ", properties:" + formatArray(propertyNames)
                    + ", listener:" + listener
                    + "]");
        }
        synchronized (lock) {
            unregister(elements, listener, mofId, propertyNames);
        }
    }

    /**
     * Register a listener for metamodel Class (and all its
     * subclasses), optionally qualified by a list of
     * property names.
     *
     * TODO: verify that property/event names are legal for
     * this class in the metamodel
     */
    private void registerClassEvent(PropertyChangeListener listener,
            Object modelClass, String[] propertyNames) {

        if (modelClass instanceof Class) {
            String className = getClassName(modelClass);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Register class ["
                        + modelImplementation.getMetaTypes().getName(modelClass)
                        + "properties:" + formatArray(propertyNames)
                        + ", listener:" + listener + "]");
            }
            Collection subtypes = (Collection) subtypeMap.get(className);
            verifyAttributeNames(className, propertyNames);
            synchronized (lock) {
                register(listenedClasses, listener, className, propertyNames);
                for (Iterator i = subtypes.iterator(); i.hasNext();) {
                    register(listenedClasses, listener, (String) i.next(),
                            propertyNames);
                }
            }
            return;
        }
        throw new IllegalArgumentException(
                "Don't know how to register class event for object "
                        + modelClass);
    }


    /**
     * Unregister a listener for a class and its subclasses.
     */
    private void unregisterClassEvent(PropertyChangeListener listener,
            Object modelClass, String[] propertyNames) {
        if (modelClass instanceof Class) {
            String className = getClassName(modelClass);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Unregister class [" + className
                        + ", properties:" + formatArray(propertyNames)
                        + ", listener:" + listener + "]");
            }
            Collection subtypes = (Collection) subtypeMap.get(className);
            synchronized (lock) {
                unregister(listenedClasses, listener, className, propertyNames);
                for (Iterator i = subtypes.iterator(); i.hasNext();) {
                    unregister(listenedClasses, listener, (String) i.next(),
                            propertyNames);
                }
            }
            return;
        }
        throw new IllegalArgumentException(
                "Don't know how to unregister class event for object "
                        + modelClass);
    }

    private String getClassName(Object elementOrClass) {
        return modelImplementation.getMetaTypes().getName(elementOrClass);
    }

    /*
     * @see org.argouml.model.ModelEventPump#startPumpingEvents()
     */
    public void startPumpingEvents() {
        LOG.debug("Start pumping events");
        repository.addListener(this);
    }

    /*
     * @see org.argouml.model.ModelEventPump#stopPumpingEvents()
     */
    public void stopPumpingEvents() {
        LOG.debug("Stop pumping events");
        repository.removeListener(this);
    }

    /*
     * @see org.argouml.model.ModelEventPump#flushModelEvents()
     */
    public synchronized void flushModelEvents() {
        try {
            while (pendingEvents > 0) {
                wait();
            }
        } catch (InterruptedException e) {
            LOG.error("Interrupted while waiting in flushModelEvents");
        }
    }

    /**
     * Register an object with given keys(s) in the registry. The object is
     * registered in multiple locations for quick lookup. During matching an
     * object registered without subkeys will match any subkey. Multiple calls
     * with the same item and key pair will only result in a single registration
     * being made.
     *
     * @param registry
     *            registry to use
     * @param item
     *            object to be registered
     * @param key
     *            primary key for registration
     * @param subkeys
     *            array of subkeys. If null, register under primary key only.
     *            The special value of the empty string ("") must not be used as
     *            a subkey by the caller.
     */
    static void register(Map registry, Object item, String key,
            String[] subkeys) {

        // Lookup primary key, creating new entry if needed
        Map entry = (Map) registry.get(key);
        if (entry == null) {
            entry = new HashMap();
            registry.put(key, entry);
        }

        // If there are no subkeys, register using our special value
        // to indicate that this is a primary key only registration
        if (subkeys == null || subkeys.length < 1) {
            subkeys =
                new String[] {
                    "",
                };
        }

        for (int i = 0; i < subkeys.length; i++) {
            List list = (ArrayList) entry.get(subkeys[i]);
            if (list == null) {
                list = new ArrayList();
                entry.put(subkeys[i], list);
            }
            if (!list.contains(item)) {
                list.add(item);
            } else {
                LOG.debug("Duplicate registration attempt for " + key + ":"
                        + subkeys + " Listener: " + item);
            }
        }
    }

    /**
     * Unregister an item or all items which match key set.
     *
     * @param registry registry to use
     * @param item object to be unregistered.  If null, unregister all
     * matching objects.
     * @param key primary key for registration
     * @param subkeys array of subkeys.  If null, unregister under primary
     * key only.
     */
    static void unregister(Map registry, Object item, String key,
            String[] subkeys) {

        Map entry = (HashMap) registry.get(key);
        if (entry == null) {
            return;
        }

        if (subkeys != null && subkeys.length > 0) {
            for (int i = 0; i < subkeys.length; i++) {
                lookupRemoveItem(entry, subkeys[i], item);
            }
        } else {
            if (item == null) {
                registry.remove(key);
            } else {
                lookupRemoveItem(entry, "", item);
            }
        }
    }

    private static void lookupRemoveItem(Map map, String key, Object item) {
        List list = (ArrayList) map.get(key);
        if (list == null) {
            return;
        }
        if (item == null) {
            map.remove(key);
            return;
        }
        if (!list.contains(item)) {
            LOG.debug("Attempt to unregister non-existant registration" + key
                    + " Listener: " + item);
        }
        while (list.contains(item)) {
            list.remove(item);
        }
        if (list.isEmpty()) {
            map.remove(key);
        }
    }

    /**
     * Return a list of items which have been registered for given key(s).
     * Returns items registered both for the key/subkey pair as well as
     * those registered just for the primary key.
     * @param registry
     * @param key
     * @param subkey
     * @return collection of items previously registered.
     */
    static Collection getMatches(Map registry, String key, String subkey) {

        List results = new ArrayList();
        Map entry = (HashMap) registry.get(key);
        if (entry != null) {
            if (entry.containsKey(subkey)) {
                results.addAll((ArrayList) entry.get(subkey));
            }
            if (entry.containsKey("")) {
                results.addAll((ArrayList) entry.get(""));
            }
        }
        return results;
    }

    /**
     * Get name of opposite end of association using
     * reflection on metamodel.
     */
    private String otherAssocEnd(AssociationEvent ae) {
        RefAssociation ra = (RefAssociation) ae.getSource();
        Association a = (Association) ra.refMetaObject();
        AssociationEnd aend = null;
        try {
            aend = (AssociationEnd) a.lookupElementExtended(ae.getEndName());
        } catch (NameNotFoundException e) {
            LOG.error("Failed to find other end of association : "
                    + ae.getSource() + " -> " + ae.getEndName());
            return null;
        }
        return aend.otherEnd().getName();
    }


    /**
     * Map from UML 1.4 names to UML 1.3 names
     * expected by ArgoUML.<p>
     *
     * Note: It would have less performance impact to do the
     * mapping during listener registration, but ArgoUML
     * depends on the value in the event.
     */
    private static String mapPropertyName(String name) {

        // TODO: We don't want to do this once we have dropped UML1.3
        // Map UML 1.4 names to UML 1.3 equivalents
        if ("typedParameter".equals(name)) {
            return "parameter";
        }
        if ("typedFeature".equals(name)) {
            return "feature";
        }
        return name;
    }

    /**
     * Formatters for debug output.
     */
    private String formatArray(String[] array) {
        if (array == null) {
            return null;
        }
        String result = "[";
        for (int i = 0; i < array.length; i++) {
            result = result + array[i] + ", ";
        }
        return result.substring(0, result.length() - 2) + "]";
    }

    private String formatElement(Object element) {
        try {
            if (element instanceof MDRObject) {
                return modelImplementation.getMetaTypes().getName(element)
                        + "<" + ((MDRObject) element).refMofId() + ">";
            } else if (element != null) {
                return element.toString();
            }
        } catch (InvalidObjectException e) {
            return modelImplementation.getMetaTypes().getName(element)
                    + "<deleted>";
        }
        return null;
    }


    /**
     * Traverse metamodel and build list of subtypes for every metatype.
     */
    private HashMap buildTypeMap(ModelPackage extent) {
        HashMap names = new HashMap();
        for (Iterator iter = extent.getMofClass().refAllOfClass().iterator();
                iter.hasNext();) {
            ModelElement element = (ModelElement) iter.next();
            String name = element.getName();
            if (names.containsKey(name)) {
                LOG.error("Found duplicate class '" + name + "' in metamodel");
            } else {
                names.put(name, getSubtypes(extent, element));
                // LOG.debug(" Class " + name + " has subtypes : "
                // + names.get(name));
            }
        }
        return names;
    }

    /**
     * Recursive method to get all subtypes.
     */
    private Collection getSubtypes(ModelPackage extent, ModelElement me) {
        Collection allSubtypes = new HashSet();
        if (me instanceof GeneralizableElement) {
            GeneralizableElement ge = (GeneralizableElement) me;
            Collection subtypes = extent.getGeneralizes().getSubtype(ge);
            for (Iterator i = subtypes.iterator(); i.hasNext();) {
                ModelElement st = (ModelElement) i.next();
                allSubtypes.add(st.getName());
                allSubtypes.addAll(getSubtypes(extent, st));
            }
        }
        return allSubtypes;
    }

    /**
     * Check whether given attribute names exist for this
     * metatype in the metamodel.  Throw exception if not found.
     */
    private void verifyAttributeNames(String className, String[] attributes) {
        // convert classname to RefObject
        RefObject ro = null;
        verifyAttributeNames(ro, attributes);
    }

    /**
     * Check whether given attribute names exist for this
     * metatype in the metamodel.  Throw exception if not found.
     */
    private void verifyAttributeNames(RefObject metaobject,
            String[] attributes) {
        // Only do verification if debug level logging is on
        // TODO: Should we leave this on always? - tfm
        if (LOG.isDebugEnabled()) {
            if (metaobject == null || attributes == null) {
                return;
            }

            HashSet names = new HashSet();
            // If we don't have a MofClass, see if we can get one from the
            // instance
            if (!(metaobject instanceof MofClass)) {
                metaobject = metaobject.refMetaObject();
            }

            // If we still don't have a MofClass, something's wrong
            if (!(metaobject instanceof MofClass)) {
                throw new IllegalArgumentException(
                        "Argument must be MofClass or instance of MofClass");
            }

            MofClass metaclass = (MofClass) metaobject;
            for (Iterator it = metaclass.allSupertypes().iterator(); it
                    .hasNext();) {
                MofClass superclass = (MofClass) it.next();
                // TODO: This won't find associations which aren't navigable in
                // this direction
                List contents = superclass.getContents();
                for (Iterator i2 = contents.iterator(); i2.hasNext();) {
                    Object o = i2.next();
                    if (o instanceof javax.jmi.model.Reference
                            || o instanceof javax.jmi.model.Attribute) {
                        names.add(((javax.jmi.model.ModelElement) o).getName());
                    }
                }
            }

            for (int i = 0; i < attributes.length; i++) {
                if (!names.contains(attributes[i])) {
                    /*
                     * TODO: For any names not found in the class definition,
                     * see if we can find an association with an end of the
                     * right name where the opposite end has the correct type .
                     *
                     * Perhaps instead of working from the class we should get
                     * all associations, check the types of their association
                     * ends for one which matches our class, then get the name
                     */
                    LOG.error("Property '" + attributes[i]
                             + "' for class '"
                             + metaclass.getName()
                             + "' doesn't exist in metamodel"
                             + " (possible false warning)");
//                  throw new IllegalArgumentException("Property '"
//                            + attributes[i] + "' doesn't exist in metamodel");
                }
            }
        }
    }
    
    /**
     * Getter provided for the dev module to allow it to discover the
     * listeners contained by the event pump.
     * @return The structure of listeners
     */
    Map getElements() {
        return elements;
    }
    
    /**
     * Getter provided for the dev module to allow it to determine the model
     * element from a MOF ID returned within the getElements() structure.
     * @param mofId The MOF ID of the model element
     * @return The model element
     */
    Object getByMofId(String mofId) {
        return repository.getByMofId(mofId);
    }
}
