// $Id$
// Copyright (c) 2002 The Regents of the University of California. All
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

/**
 * This class implements an event pump for all modelevents (MEvents with the current
 * NSUML model). Two kinds of listeners can be registred to the pump: listeners
 * to class events and listeners to object events. The pump dispatches all
 * events fired by objects of a certain class to the class listeners (listeners 
 * that are registred via addClassModelEventListener). Furthermore, it dispatches
 * all events to listeners that are registered for a certain object if this object
 * fired the original event.
 * <p>
 * Maybe this class should dispatch a thread to handle the incoming event in the 
 * future.</p>
 * @since Oct 14, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public final class UmlModelEventPump implements MElementListener {

    private Category cat = Category.getInstance(UmlModelEventPump.class);

    public final static String REMOVE = "remove";

    private static UmlModelEventPump _instance = null;

    private Map _listenerClassModelEventsMap = new HashMap();
    private Map _listenerModelEventsMap = new HashMap();

    /**
     * Singleton access method
     * @return UmlModelEventPump
     */
    public synchronized static UmlModelEventPump getPump() {
        if (_instance == null) {
            _instance = new UmlModelEventPump();
        }
        return _instance;
    }

    /**
     * Constructor for UmlModelEventPump.
     */
    private UmlModelEventPump() {
        super();
    }

    /**
     * Makes the key for the hashmap where the listeners are stored. For test
     * purposes this method has a 'default' visibility
     * @param modelClass
     * @param eventName
     * @return String
     */
    String getKey(Class modelClass, String eventName) {
        String className = modelClass.getName();
        if (className.endsWith("Impl")) {
            className = className.substring(0, className.lastIndexOf("Impl"));
        }
        return className + eventName;
    }

    /**
     * Adds a listener that listens to all modelevents that are named eventNames
     * and that occur to instances of a given modelClass.
     * 
     * <p>
     * If you want the listener to be registred for remove events (that is: an 
     * instance of the class the listener is listening too is removed), then you 
     * have to register for the eventname "remove"
     * </p>
     * @param listener
     * @param modelClass
     * @param eventNames
     * @throws IllegalArgumentException if one of the arguments is null or if
     *  the modelClass is not a subclass of MBase.
     * @throws IllegalStateException if the listener is allready registred
     */
    public void addClassModelEventListener(MElementListener listener, Class modelClass, String[] eventNames) {
        if (listener == null || modelClass == null || eventNames == null)
            throw new IllegalArgumentException("Tried to add null listener to null class");
        if (!MBase.class.isAssignableFrom(modelClass))
            throw new IllegalArgumentException("Tried to add illegal class modeleventlistener.");
        if (eventNames.length == 0)
            throw new IllegalArgumentException("Tried to add an empty eventName list");
        for (int i = 0; i < eventNames.length; i++) {
            executeAddClassModelEventListener(listener, modelClass, eventNames[i]);
        }
    }

    /**
     * Convinience method to add a listener that only listens to one specific event
     * @param listener
     * @param modelClass
     * @param eventName
     */
    public void addClassModelEventListener(MElementListener listener, Class modelClass, String eventName) {
        if (listener == null || modelClass == null || eventName == null)
            throw new IllegalArgumentException("Tried to add null listener to null class");
        if (!MBase.class.isAssignableFrom(modelClass))
            throw new IllegalArgumentException("Tried to add illegal class modeleventlistener.");
        if (eventName.equals(""))
            throw new IllegalArgumentException("Tried to add an empty eventname");
        executeAddClassModelEventListener(listener, modelClass, eventName);
    }

    /**
     * Adds a listener to events fired by metaClass modelClass. All events fired
     * by instances of modelClass or instances of its subclasses will be pumped
     * to the listener.
     * 
     * <p><em>Note:</em> Due to the fact that ALL events are pumped for some 
     * metaclass and it's children, this is a very powerfull method but also
     * one that can hog performance. Use this with care!</p>
     * @param listener
     * @param modelClass
     */
    public void addClassModelEventListener(MElementListener listener, Class modelClass) {
        if (listener == null || modelClass == null)
            throw new IllegalArgumentException("Tried to add null listener to null class");
        if (!MBase.class.isAssignableFrom(modelClass))
            throw new IllegalArgumentException("Tried to add illegal class modeleventlistener.");
        executeAddClassModelEventListener(listener, modelClass, null);
    }

    /**
     * Does the actual adding
     * @param listener
     * @param modelClass
     * @param eventName
     */
    private synchronized void executeAddClassModelEventListener(MElementListener listener, Class modelClass, String eventName) {
        // first register the listener for all elements allready in the model
        Iterator it = ModelManagementHelper.getHelper().getAllModelElementsOfKind(modelClass).iterator();
        while (it.hasNext()) {
            executeAddModelEventListener(listener, (MBase) it.next(), eventName);
        }
        // add the class to the 'interested classes list' so the listener is added on creation
        // of a modelelement
        Set listenerList = (Set) _listenerClassModelEventsMap.get(modelClass);
        if (listenerList == null) {
            listenerList = new HashSet();
            _listenerClassModelEventsMap.put(modelClass, listenerList);
        }
        ListenerEventName couple = new ListenerEventName(listener, eventName);
        it = listenerList.iterator();
        boolean addNeeded = true;
        while (it.hasNext()) {
            if (couple.equals(it.next())) {
                return;
            }
        }
        listenerList.add(couple);
    }

    /**
     * Removes a listener that listens to all modelevents fired by instances of 
     * modelClass and that have the original name eventNames.
     * @param listener
     * @param modelClass
     * @param eventNames
     */
    public void removeClassModelEventListener(MElementListener listener, Class modelClass, String[] eventNames) {
        if (listener == null || modelClass == null || eventNames == null)
            throw new IllegalArgumentException("Tried to remove null listener from null class");
        if (!MBase.class.isAssignableFrom(modelClass))
            throw new IllegalArgumentException("Tried to remove illegal class modeleventlistener.");
        if (eventNames.length == 0)
            throw new IllegalArgumentException("Tried to remove an empty eventName list");
        for (int i = 0; i < eventNames.length; i++) {
            executeRemoveClassModelEventListener(listener, modelClass, eventNames[i]);
        }
    }

    /**
     * Convinience method to remove a listener that listens to events named eventName
     * that are fired by instances of modelClass
     * @param listener
     * @param modelClass
     * @param eventName
     */
    public void removeClassModelEventListener(MElementListener listener, Class modelClass, String eventName) {
        if (listener == null || modelClass == null || eventName == null)
            throw new IllegalArgumentException("Tried to remove null listener from null class");
        if (!MBase.class.isAssignableFrom(modelClass))
            throw new IllegalArgumentException("Tried to remove illegal class modeleventlistener.");
        executeRemoveClassModelEventListener(listener, modelClass, eventName);
    }

    /**
     * Removes a listener that listens to all modelevents fired by instances of 
     * modelClass.
     * @param listener
     * @param modelClass
     * @param eventName
     */
    public void removeClassModelEventListener(MElementListener listener, Class modelClass) {
        if (listener == null || modelClass == null)
            throw new IllegalArgumentException("Tried to remove null listener from null class");
        if (!MBase.class.isAssignableFrom(modelClass))
            throw new IllegalArgumentException("Tried to remove illegal class modeleventlistener.");
        executeRemoveClassModelEventListener(listener, modelClass, null);
    }

    private synchronized void executeRemoveClassModelEventListener(MElementListener listener, Class modelClass, String eventName) {
        // remove all registrations of this listener with all instances of modelClass
        Iterator it = ModelManagementHelper.getHelper().getAllModelElementsOfKind(modelClass).iterator();
        while (it.hasNext()) {
            removeModelEventListener(listener, (MBase) it.next(), eventName);
        }
        // remove the listener from the registry
        Set listenerList = (Set) _listenerClassModelEventsMap.get(modelClass);
        Object[] asArray = listenerList.toArray();
        for (int i = 0; i < asArray.length; i++) {
            ListenerEventName couple = (ListenerEventName) asArray[i];
            if (couple.getListener() == listener && (couple.getEventName().equals(eventName) || eventName == null)) {
                listenerList.remove(couple);
            }
        }
        if (listenerList.isEmpty()) {
            _listenerClassModelEventsMap.remove(modelClass);
        }
    }

    /**
     * Gets the key for the map with the listeners to modelevents.
     * @param modelelement
     * @param eventName
     * @return String
     */
    String getKey(MBase modelelement, String eventName) {
        return modelelement.hashCode() + eventName;
    }

    /**
     * Adds a listener to modelevents that are fired by some given modelelement and that 
     * have the name eventNames.
     * 
     * <p>
     * If you want the listener to be registred for remove events (that is: the 
     * instance the listener is listening too is removed), then you have to 
     * register for the eventname "remove"
     * </p>
     * @param listener
     * @param modelelement
     * @param eventNames
     */
    public void addModelEventListener(MElementListener listener, MBase modelelement, String[] eventNames) {
        if (listener == null || modelelement == null || eventNames == null || eventNames.length == 0)
            throw new IllegalArgumentException("Null or empty arguments while adding a modelelement listener");
        for (int i = 0; i < eventNames.length; i++) {
            executeAddModelEventListener(listener, modelelement, eventNames[i]);
        }
    }

    /**
     * Convinience method to add a listener that only listens to one specific event
     * @param listener
     * @param modelClass
     * @param eventName
     */
    public void addModelEventListener(MElementListener listener, MBase modelelement, String eventName) {
        if (listener == null || modelelement == null || eventName == null)
            throw new IllegalArgumentException("Tried to add null listener to null class");
        if (eventName.equals(""))
            throw new IllegalArgumentException("Tried to add an empty eventname");
        executeAddModelEventListener(listener, modelelement, eventName);
    }

    /**
     * Adds a listener to all events fired by some modelelement.
     * 
     * <p><em>Note:</em> Due to the fact that ALL events are pumped for some 
     * modelelement, this is a rather powerfull method but also
     * one that can hog performance. Use this with care!</p>
     * @param listener
     * @param modelElement
     */
    public void addModelEventListener(MElementListener listener, MBase modelelement) {
        if (listener == null || modelelement == null)
            throw new IllegalArgumentException("Tried to add null listener to null class");
        executeAddModelEventListener(listener, modelelement, null);
    }

    private synchronized void executeAddModelEventListener(MElementListener listener, MBase modelelement, String eventName) {
        String key = getKey(modelelement, eventName);
        Set listenerList = (Set) _listenerModelEventsMap.get(key);
        if (listenerList == null) {
            listenerList = new HashSet();
            _listenerModelEventsMap.put(key, listenerList);
        }
        listenerList.add(listener);
        modelelement.removeMElementListener(this);
        modelelement.addMElementListener(this);
    }

    /**
     * Removes a listener that listens to modelevents with name eventNames that
     * are fired by the given modelelement. 
     * @param listener The listener to remove
     * @param modelElement The modelelement that fires the events the listener is listening to
     * @param eventNames The list of event names the listener is interested in
     */
    public void removeModelEventListener(MElementListener listener, MBase modelElement, String[] eventNames) {
        if (listener == null || modelElement == null || eventNames == null)
            throw new IllegalArgumentException("Tried to remove null listener from null modelelement");
        if (eventNames.length == 0)
            throw new IllegalArgumentException("Tried to remove an empty eventName list");
        for (int i = 0; i < eventNames.length; i++) {
            executeRemoveModelEventListener(listener, modelElement, eventNames[i]);
        }
    }

    /**
     * Removes a listener that listens to all events fired by the given modelelement.
     * @param listener
     * @param modelElement
     */
    public void removeModelEventListener(MElementListener listener, MBase modelElement) {
        if (listener == null || modelElement == null)
            throw new IllegalArgumentException("Tried to remove null listener from null modelelement");
        executeRemoveModelEventListener(listener, modelElement, null);
    }

    /**
     * Convinience method to remove a listener to some event.
     * @param listener
     * @param modelElement
     * @param eventName
     */
    public void removeModelEventListener(MElementListener listener, MBase modelElement, String eventName) {
        if (listener == null || modelElement == null || eventName == null)
            throw new IllegalArgumentException("Tried to remove null listener from null modelelement");
        executeRemoveModelEventListener(listener, modelElement, eventName);
    }

    private synchronized void executeRemoveModelEventListener(MElementListener listener, MBase elem, String eventName) {
        String key = getKey(elem, eventName);
        Set listenerList = (Set) _listenerModelEventsMap.get(key);
        if (listenerList != null) {
            listenerList.remove(listener);
            if (listenerList.isEmpty())
                _listenerModelEventsMap.remove(key);
        }
    }

    /**
     * Method to remove some element from the listenerObjectMap. Used by
     * delete on UmlFactory to make sure all listeners are removed.
     * @param element
     */
    synchronized void cleanUp(MBase element) {
        String hash = element.hashCode() + "";
        Iterator it = _listenerModelEventsMap.keySet().iterator();
        List cleanUplist = new ArrayList();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key.startsWith(hash)) {
                cleanUplist.add(key);
            }
        }
        it = cleanUplist.iterator();
        while (it.hasNext()) {
            _listenerModelEventsMap.remove(it.next());
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) {
        Iterator it = getListenerList(e).iterator();
        while (it.hasNext()) {
            ((MElementListener) it.next()).listRoleItemSet(e);
        }
    }

    private Set getListenerList(MElementEvent e) {
        MBase source = (MBase) e.getSource();
        String eventName = e.getName();
        if (e.getType() == MElementEvent.ELEMENT_REMOVED)
            eventName = REMOVE;
        Set listenerList = (Set) _listenerModelEventsMap.get(getKey(source, eventName));
        if (_listenerModelEventsMap.get(getKey(source, null)) != null) {
            if (listenerList == null)
                listenerList = (Set) _listenerModelEventsMap.get(getKey(source, null));
            else
                listenerList.addAll((Set) _listenerModelEventsMap.get(getKey(source, null)));
        }
        return listenerList != null ? listenerList : new HashSet();
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent e) {
        if (e.getNewValue() != null && !(e.getNewValue().equals(e.getOldValue()))) {
            Iterator it = getListenerList(e).iterator();

            while (it.hasNext()) {
                ((MElementListener) it.next()).propertySet(e);
            }
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent e) {
        Iterator it = getListenerList(e).iterator();
        while (it.hasNext()) {
            ((MElementListener) it.next()).recovered(e);
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent e) {
        Iterator it = getListenerList(e).iterator();
        while (it.hasNext()) {
            ((MElementListener) it.next()).removed(e);
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
        Iterator it = getListenerList(e).iterator();
        while (it.hasNext()) {
            ((MElementListener) it.next()).roleAdded(e);
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
        Iterator it = getListenerList(e).iterator();
        while (it.hasNext()) {
            ((MElementListener) it.next()).roleRemoved(e);
        }
    }

    /**
     * Clears the hashmaps with listeners. This is only needed by the JUnit tests.
     * Therefore the visibility is 'default'.
     */
    void cleanUp() {
        _listenerClassModelEventsMap.clear();
        _listenerModelEventsMap.clear();
    }

    /**
     * Returns the listenerClassModelEventsMap.
     * @return Map
     */
    public Map getListenerClassModelEventsMap() {
        return _listenerClassModelEventsMap;
    }

    /**
     * Returns the listenerModelEventsMap.
     * @return Map
     */
    public Map getListenerModelEventsMap() {
        return _listenerModelEventsMap;
    }

    /**
     * Returns a set of listenerEventName objects. Each object is a pair of 
     * a listener that is interested in certain events defined by a given eventname
     * This method is called by AbstractUmlFactory when initializing a new 
     * modelelement.
     * @param modelClass
     * @return Set
     */
    Set getInterestedListeners(Class modelClass) {
        Set returnSet = new HashSet();
        Set interfaces = getInterfaces(modelClass);
        Iterator it = interfaces.iterator();
        while (it.hasNext()) {
            Set listeners = (Set) _listenerClassModelEventsMap.get(it.next());
            if (listeners != null)
                returnSet.addAll(listeners);
        }
        return returnSet;
    }

    /**
     * An utility method to get all interfaces realized by some class, not only
     * the interfaces directly realized by the class but also all interfaces
     * realized by the interfaces themselves.
     * @param modelClass. The class for which we are searching for interfaces
     * @return Set the set with all interfaces. It is empty if there are no 
     * interfaces
     */
    private Set getInterfaces(Class modelClass) {
        Set returnSet = new HashSet();
        Class[] interfaces = modelClass.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            returnSet.add(interfaces[i]);
            returnSet.addAll(getInterfaces(interfaces[i]));
        }
        return returnSet;
    }

    class ListenerEventName {
        private final String _eventName;
        private final MElementListener _listener;

        /**
         * Constructor for ListenerEventName.
         */
        public ListenerEventName(MElementListener listener, String eventName) {
            _eventName = eventName;
            _listener = listener;
        }

        /**
         * Returns the eventName.
         * @return String
         */
        public String getEventName() {
            return _eventName;
        }

        /**
         * Returns the listener.
         * @return MElementListener
         */
        public MElementListener getListener() {
            return _listener;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object obj) {
            if (obj instanceof ListenerEventName) {
                ListenerEventName couple = (ListenerEventName) obj;
                if (couple.getEventName().equals(_eventName) && couple.getListener().equals(_listener))
                    return true;
            }
            return super.equals(obj);
        }

    }

}
