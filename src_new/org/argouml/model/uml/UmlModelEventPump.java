// Copyright (c) 1996-99 The Regents of the University of California. All
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

// $header$
package org.argouml.model.uml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
public class UmlModelEventPump implements MElementListener {
    
    private static UmlModelEventPump _instance = null;
    
    private Map _listenerClassModelEventsMap = Collections.synchronizedMap(new HashMap());
    private Map _listenerModelEventsMap = Collections.synchronizedMap(new HashMap());
    
    /**
     * Singleton access method
     * @return UmlModelEventPump
     */
    public static UmlModelEventPump getPump() {
        if (_instance == null) {
            _instance = new UmlModelEventPump();
        }
        return _instance;
    }

    /**
     * Constructor for UmlModelEventPump.
     */
    protected UmlModelEventPump() {
        super();
    }
    
    
    
    /**
     * Makes the key for the hashmap where the listeners are stored.
     * @param modelClass
     * @param eventName
     * @return String
     */
    private String getKey(Class modelClass, String eventName) {
        return modelClass.getName() + eventName;
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
    public void addClassModelEventsListener(MElementListener listener, Class modelClass, String[] eventNames) {
        if (listener == null || modelClass == null || eventNames == null) 
            throw new IllegalArgumentException("Tried to add null listener to null class");
        if (!MBase.class.isAssignableFrom(modelClass)) 
            throw new IllegalArgumentException("Tried to add illegal class modeleventlistener.");
        if (eventNames.length == 0)
            throw new IllegalArgumentException("Tried to add an empty eventName list");
        for (int i = 0; i < eventNames.length; i++) {
            List listenerList = (List)_listenerClassModelEventsMap.get(getKey(modelClass, eventNames[i]));
            if (listenerList == null) {
                listenerList = new ArrayList();
                _listenerClassModelEventsMap.put(getKey(modelClass, eventNames[i]), listenerList);
            } else
            if (listenerList.contains(listener))
                throw new IllegalStateException("Tried to add a listener twice");
            listenerList.add(listener);
        }
    }
    
    /**
     * Removes a listener that listens to all modelevents fired by instances of 
     * modelClass and that have the original name eventNames.
     * @param listener
     * @param modelClass
     * @param eventNames
     */
    public void removeClassModelEventsListener(MElementListener listener, Class modelClass, String[] eventNames) {
        if (listener == null || modelClass == null || eventNames == null) 
            throw new IllegalArgumentException("Tried to remove null listener from null class");
        if (!MBase.class.isAssignableFrom(modelClass)) 
            throw new IllegalArgumentException("Tried to remove illegal class modeleventlistener.");
        if (eventNames.length == 0)
            throw new IllegalArgumentException("Tried to remove an empty eventName list");
        for (int i = 0; i < eventNames.length; i++) {
            List listenerList = (List)_listenerClassModelEventsMap.get(getKey(modelClass, eventNames[i]));
            if (listenerList == null) 
                throw new IllegalStateException("No class listener for this class registred");
            listenerList.remove(listener);
        }
    }
    
    /**
     * Gets the key for the map with the listeners to modelevents.
     * @param modelelement
     * @param eventName
     * @return String
     */
    private String getKey(MBase modelelement, String eventName) {
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
    public void addModelEventsListener(MElementListener listener, MBase modelelement, String[] eventNames) {
        if (listener == null || modelelement == null || eventNames == null || eventNames.length == 0) 
            throw new IllegalArgumentException("Null or empty arguments while adding a modelelement listener");
        for (int i = 0; i < eventNames.length; i++) {
            List listenerList = (List)_listenerModelEventsMap.get(getKey(modelelement, eventNames[i]));
            if (listenerList == null) {
                listenerList = new ArrayList();
                _listenerModelEventsMap.put(getKey(modelelement, eventNames[i]), listenerList);
            } else
            if (listenerList.contains(listener)) 
                throw new IllegalStateException("Tried to add listener twice");
            listenerList.add(listener);
        }
    }
        
    
    
    
    /**
     * Method to remove some element from the listenerObjectMap. Used by
     * delete on UmlFactory to make sure all listeners are removed.
     * @param element
     */
    void cleanUp(MBase element) {
        _listenerModelEventsMap.remove(element);
    }
            
    
    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) {
        fireClassRoleItemSet(e);
        fireObjectRoleItemSet(e);
    }
    
    private void fireClassRoleItemSet(MElementEvent e) {
        List listenerList = (List)_listenerClassModelEventsMap.get(getKey(e.getSource().getClass(), e.getName()));
        if (listenerList == null) return;
        Iterator it = listenerList.iterator();
        while(it.hasNext()) {
            ((MElementListener)it.next()).listRoleItemSet(e);
        }
    }
    
    private void fireObjectRoleItemSet(MElementEvent e) {
        List listenerList = (List)_listenerModelEventsMap.get(getKey((MBase)e.getSource(), e.getName()));
        if (listenerList == null) return;
        Iterator it = listenerList.iterator();
        while(it.hasNext()) {
            ((MElementListener)it.next()).listRoleItemSet(e);
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent e) {
        fireClassPropertySet(e);
        fireObjectPropertySet(e);
    }
    
    private void fireClassPropertySet(MElementEvent e) {
        List listenerList = (List)_listenerClassModelEventsMap.get(getKey(e.getSource().getClass(), e.getName()));
        if (listenerList == null) return;
        Iterator it = listenerList.iterator();
        while(it.hasNext()) {
            ((MElementListener)it.next()).propertySet(e);
        }
    }
    
    private void fireObjectPropertySet(MElementEvent e) {
        List listenerList = (List)_listenerModelEventsMap.get(getKey((MBase)e.getSource(), e.getName()));
        if (listenerList == null) return;
        Iterator it = listenerList.iterator();
        while(it.hasNext()) {
            ((MElementListener)it.next()).propertySet(e);
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent e) {
        fireObjectRecovered(e);
        fireClassRecovered(e);
    }
    
    private void fireClassRecovered(MElementEvent e) {
        List listenerList = (List)_listenerClassModelEventsMap.get(getKey(e.getSource().getClass(), e.getName()));
        if (listenerList == null) return;
        Iterator it = listenerList.iterator();
        while(it.hasNext()) {
            ((MElementListener)it.next()).recovered(e);
        }
    }
    
    private void fireObjectRecovered(MElementEvent e) {
        List listenerList = (List)_listenerModelEventsMap.get(getKey((MBase)e.getSource(), e.getName()));
        if (listenerList == null) return;
        Iterator it = listenerList.iterator();
        while(it.hasNext()) {
            ((MElementListener)it.next()).recovered(e);
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent e) {
        fireObjectRemoved(e);
        fireClassRemoved(e);
    }
    
    private void fireClassRemoved(MElementEvent e) {
        List listenerList = (List)_listenerClassModelEventsMap.get(getKey(e.getSource().getClass(), "remove"));
        if (listenerList == null) return;
        Iterator it = listenerList.iterator();
        while(it.hasNext()) {
            ((MElementListener)it.next()).removed(e);
        }
    }
    
    private void fireObjectRemoved(MElementEvent e) {
        List listenerList = (List)_listenerModelEventsMap.get(getKey((MBase)e.getSource(), "remove"));
        if (listenerList == null) return;
        Iterator it = listenerList.iterator();
        while(it.hasNext()) {
            ((MElementListener)it.next()).removed(e);
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
        fireObjectRoleAdded(e);
        fireClassRoleAdded(e);
    }
    
    private void fireClassRoleAdded(MElementEvent e) {
        List listenerList = (List)_listenerClassModelEventsMap.get(getKey(e.getSource().getClass(), e.getName()));
        if (listenerList == null) return;
        Iterator it = listenerList.iterator();
        while(it.hasNext()) {
            ((MElementListener)it.next()).roleAdded(e);
        }
    }
    
    private void fireObjectRoleAdded(MElementEvent e) {
        List listenerList = (List)_listenerModelEventsMap.get(getKey((MBase)e.getSource(), e.getName()));
        if (listenerList == null) return;
        Iterator it = listenerList.iterator();
        while(it.hasNext()) {
            ((MElementListener)it.next()).roleAdded(e);
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
        fireObjectRoleRemoved(e);
        fireClassRoleRemoved(e);
    }
    
    private void fireClassRoleRemoved(MElementEvent e) {
        List listenerList = (List)_listenerClassModelEventsMap.get(getKey(e.getSource().getClass(), e.getName()));
        if (listenerList == null) return;
        Iterator it = listenerList.iterator();
        while(it.hasNext()) {
            ((MElementListener)it.next()).roleRemoved(e);
        }
    }
    
    private void fireObjectRoleRemoved(MElementEvent e) {
        List listenerList = (List)_listenerModelEventsMap.get(getKey((MBase)e.getSource(), e.getName()));
        if (listenerList == null) return;
        Iterator it = listenerList.iterator();
        while(it.hasNext()) {
            ((MElementListener)it.next()).roleRemoved(e);
        }
    }

    /**
     * Clears the hasmaps with listeners. This is only needed by the JUnit tests.
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

}
