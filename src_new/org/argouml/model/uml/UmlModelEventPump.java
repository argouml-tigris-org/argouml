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

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.model_management.MModel;

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

    private Category _cat = Category.getInstance(UmlModelEventPump.class);

    public final static String REMOVE = "remove";

    private static UmlModelEventPump _instance = new UmlModelEventPump();

    /**
     * The 'map' with the eventlistenerlists per modelelement
     */
    private EventListenerHashMap _listenerMap = new EventListenerHashMap();

    private ClassListenerHashMap _classListenerMap = new ClassListenerHashMap();

    private EventTreeDefinition _definition = new EventTreeDefinition();

    /**
     * Singleton access method
     * @return UmlModelEventPump
     */
    public synchronized static UmlModelEventPump getPump() {
        return _instance;
    }

    /**
     * Constructor for UmlModelEventPump.
     */
    private UmlModelEventPump() {
        super();
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
    public void addClassModelEventListener(
        MElementListener listener,
        Class modelClass,
        String[] eventNames) {
        if (listener == null
            || modelClass == null
            || eventNames == null
            || !MBase.class.isAssignableFrom(modelClass)
            || eventNames.length == 0)
            throw new IllegalArgumentException("Tried to add illegal class" +
                " modeleventlistener to possible null class");
        for (int i = 0; i < eventNames.length; i++) {
            executeAddClassModelEventListener(
                listener,
                modelClass,
                eventNames[i]);
        }
    }

    /**
     * Convinience method to add a listener that only listens to one specific 
     * event
     * @param listener The listener to add
     * @param modelClass The listener should listen to instances of this class
     * @param eventName The name of the event the listener wants to listen 
     * too.
     */
    public void addClassModelEventListener(
        MElementListener listener,
        Class modelClass,
        String eventName) {
        if (listener == null
            || modelClass == null
            || eventName == null
            || !MBase.class.isAssignableFrom(modelClass)
            || eventName.equals(""))
            throw new IllegalArgumentException("Illegal argument to " +
                "addClassModelEventListener");
        executeAddClassModelEventListener(listener, modelClass, eventName);
    }

    /**
     * Does the actual adding
     * @param listener The listener to add
     * @param modelClass The listener should listen to instances of this class
     * @param eventName The name of the event the listener wants to listen 
     */
    private synchronized void executeAddClassModelEventListener(
        MElementListener listener,
        Class modelClass,
        String eventName) {
        // first register the listener for all elements allready in the model
        if (_cat.isDebugEnabled())
            _cat.debug(
                "Registring listener "
                    + listener
                    + " to class "
                    + modelClass.getName()
                    + " and event "
                    + eventName);

        //modelClass = formatClass(modelClass);
        Collection col =
            ModelManagementHelper.getHelper().getAllModelElementsOfKind(
                modelClass);
        if (modelClass.isAssignableFrom(MModel.class)) {
            MModel root =
                ProjectManager.getManager().getCurrentProject().getRoot();
            if (root != null)
                col.add(root);
        }
        modelClass = formatClass(modelClass);
        EventKey[] keys = _definition.getEventTypes(modelClass, eventName);
        Iterator it = col.iterator();
        while (it.hasNext()) {
            MBase base = (MBase) it.next();
            for (int i = 0; i < keys.length; i++) {
                _listenerMap.put(base, keys[i], (MElementListener) listener);
            }
        }
        // add the class to the 'interested classes list' so the listener is 
        // added on creation of a modelelement
        for (int i = 0; i < keys.length; i++) {
            _classListenerMap.put(modelClass, keys[i], listener);
        }
    }
    /**
     * Retrieves the  implementation class belonging to some given class. For 
     * example, retrieves ClassImpl.class if the input was Class.class or 
     * ClassImpl.class.
     * @param inputClass An interface or implementation class from NSUML
     * @return The implementation class from NSUML
     */
    private Class formatClass(Class inputClass) {
        String name = inputClass.getName();
        if (name.endsWith("Impl"))
            return inputClass;
        else {
            try {
                Class returnClass = Class.forName(name + "Impl");
                return returnClass;
            } catch (ClassNotFoundException ignorable) {
                // cannot happen
            }
        }
        return null;

    }

    /**
     * Removes a listener that listens to all modelevents fired by instances of
     * modelClass and that have the original name eventNames.
     * @param listener The listener to remove
     * @param modelClass The class the listener does not want to listen to 
     * instances anymore
     * @param eventNames The eventnames the listener does not want to listen to
     * anymore
     */
    public void removeClassModelEventListener(
        MElementListener listener,
        Class modelClass,
        String[] eventNames) {
        if (listener == null
            || modelClass == null
            || eventNames == null
            || !MBase.class.isAssignableFrom(modelClass)
            || eventNames.length == 0)
            throw new IllegalArgumentException("Illegal argument to " +
                "removeClassModelEventListener");
        for (int i = 0; i < eventNames.length; i++) {
            executeRemoveClassModelEventListener(
                listener,
                modelClass,
                eventNames[i]);
        }
    }

    /**
     * Convinience method to remove a listener that listens to events named 
     * eventName that are fired by instances of modelClass
     * @param listener The listener to remove
     * @param modelClass The class the listener does not want to listen to 
     * instances anymore
     * @param eventName The eventname the listener does not want to listen to
     * anymore
     */
    public void removeClassModelEventListener(
        MElementListener listener,
        Class modelClass,
        String eventName) {
        if (listener == null
            || modelClass == null
            || eventName == null
            || !MBase.class.isAssignableFrom(modelClass))
            throw new IllegalArgumentException("Illegal argument to " +
                "removeClassModelEventListener");
        executeRemoveClassModelEventListener(listener, modelClass, eventName);
    }

    /**
     * Removes a listener that listens to all modelevents fired by instances of
     * modelClass.
     * @param listener The listener to remove
     * @param modelClass The class the listener does not want to listen to 
     * instances anymore
     * @param eventName The eventname the listener does not want to listen to
     * anymore
     */
    public void removeClassModelEventListener(
        MElementListener listener,
        Class modelClass) {
        if (listener == null
            || modelClass == null
            || !MBase.class.isAssignableFrom(modelClass))
            throw new IllegalArgumentException("Tried to remove null listener " +
                "from null class");
        executeRemoveClassModelEventListener(listener, modelClass, null);
    }

    /**
     * Executes the removal of a listener to a class
     * @param listener The listener to remove
     * @param modelClass The class the listener does not want to listen to 
     * instances anymore
     * @param eventName The eventname the listener does not want to listen to
     * anymore
     */
    private synchronized void executeRemoveClassModelEventListener(
        MElementListener listener,
        Class modelClass,
        String eventName) {
        // remove all registrations of this listener with all instances of 
        // modelClass
        if (_cat.isDebugEnabled())
            _cat.debug(
                "Removing listener "
                    + listener.toString()
                    + " from modelclass "
                    + modelClass.getName()
                    + ". It was listening to event "
                    + eventName);
        //modelClass = formatClass(modelClass);
        Iterator it =
            ModelManagementHelper
                .getHelper()
                .getAllModelElementsOfKind(modelClass)
                .iterator();
        while (it.hasNext()) {
            MBase base = (MBase) it.next();
            removeModelEventListener(listener, base, eventName);
            if (_cat.isDebugEnabled())
                _cat.debug(
                    "Removed the listener "
                        + listener
                        + " that was registred for modelelement "
                        + base
                        + " and event "
                        + eventName);
        }
        // remove the listener from the registry
        EventKey[] keys = _definition.getEventTypes(modelClass, eventName);
        for (int i = 0; i < keys.length; i++) {
            _classListenerMap.remove(modelClass, keys[i], listener);
        }
    }

    /**
     * Adds a listener to modelevents that are fired by some given modelelement 
     * and that have the name eventNames.
     * <p>
     * If you want the listener to be registred for remove events (that is: the
     * instance the listener is listening too is removed), then you have to
     * register for the eventname "remove"
     * </p>
     * @param listener The listener to add
     * @param modelelement The modelelement the listener should be added too
     * @param eventNames The array of eventnames the listener should listen 
     * to
     */
    public void addModelEventListener(
        Object listener,
        Object modelelement,
        String[] eventNames) {
        if (listener == null
            || modelelement == null
            || eventNames == null
            || eventNames.length == 0
            || !(listener instanceof MElementListener)
            || !(modelelement instanceof MBase))
            throw new IllegalArgumentException("Wrong argument types while " +
                "adding a modelelement listener");
        for (int i = 0; i < eventNames.length; i++) {
            EventKey[] keys =
                _definition.getEventTypes(
                    modelelement.getClass(),
                    eventNames[i]);
            for (int j = 0; j < keys.length; j++) {
                _listenerMap.put(
                    (MBase) modelelement,
                    keys[j],
                    (MElementListener) listener);
            }
        }
    }

    /**
     * Convinience method to add a listener that only listens to one specific 
     * event
     * @param listener The listener to add
     * @param modelelement The modelelement the listener should be added too
     * @param eventNames The array of eventnames the listener should listen 
     */
    public void addModelEventListener(
        Object listener,
        Object modelelement,
        String eventName) {
        if (listener == null
            || modelelement == null
            || eventName == null
            || !(listener instanceof MElementListener)
            || !(modelelement instanceof MBase))
            throw new IllegalArgumentException("Wrong argument types while " +
                "adding a modelelement listener");
        EventKey[] keys =
            _definition.getEventTypes(modelelement.getClass(), eventName);
        for (int i = 0; i < keys.length; i++) {
            _listenerMap.put(
                (MBase) modelelement,
                keys[i],
                (MElementListener) listener);
        }
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
    public void addModelEventListener(Object listener, Object modelelement) {
        if (listener == null
            || modelelement == null
            || !(listener instanceof MElementListener)
            || !(modelelement instanceof MBase))
            throw new IllegalArgumentException("Wrong argument types while adding a modelelement listener");
        EventKey[] keys = _definition.getEventTypes(modelelement.getClass());
        for (int i = 0; i < keys.length; i++) {
            _listenerMap.put(
                (MBase) modelelement,
                keys[i],
                (MElementListener) listener);
        }

    }

    /**
     * Removes a listener that listens to modelevents with name eventNames that
     * are fired by the given modelelement.
     * @param listener The listener to remove
     * @param modelElement The modelelement that fires the events the listener is listening to
     * @param eventNames The list of event names the listener is interested in
     */
    public void removeModelEventListener(
        MElementListener listener,
        Object handle,
        String[] eventNames) {
        if (!(handle instanceof MBase))
            throw new IllegalArgumentException("Handle is not a valid modelelement");
        MBase modelElement = (MBase) handle;
        if (listener == null
            || modelElement == null
            || eventNames == null
            || eventNames.length == 0)
            throw new IllegalArgumentException("Tried to remove null listener from null modelelement");
        for (int i = 0; i < eventNames.length; i++) {
            EventKey[] keys =
                _definition.getEventTypes(
                    modelElement.getClass(),
                    eventNames[i]);
            for (int j = 0; j < keys.length; j++) {
                _listenerMap.remove(
                    (MBase) modelElement,
                    keys[j],
                    (MElementListener) listener);
            }
        }
    }

    /**
     * Removes a listener that listens to all events fired by the given modelelement.
     * @param listener
     * @param modelElement
     */
    public void removeModelEventListener(
        MElementListener listener,
        Object handle) {
        if (!(handle instanceof MBase))
            throw new IllegalArgumentException("Handle is not a valid modelelement");
        MBase modelElement = (MBase) handle;
        if (listener == null || modelElement == null)
            throw new IllegalArgumentException("Tried to remove null listener from null modelelement");
        EventKey[] keys = _definition.getEventTypes(modelElement.getClass());
        for (int i = 0; i < keys.length; i++) {
            _listenerMap.remove(
                modelElement,
                keys[i],
                (MElementListener) listener);
        }
    }

    /**
     * Convinience method to remove a listener to some event.
     * @param listener
     * @param modelElement
     * @param eventName
     */
    public void removeModelEventListener(
        MElementListener listener,
        Object handle,
        String eventName) {
        if (!(handle instanceof MBase))
            throw new IllegalArgumentException("Handle is not a valid modelelement");
        MBase modelElement = (MBase) handle;
        if (listener == null || modelElement == null || eventName == null)
            throw new IllegalArgumentException("Tried to remove null listener from null modelelement");
        EventKey[] keys =
            _definition.getEventTypes(modelElement.getClass(), eventName);
        for (int j = 0; j < keys.length; j++) {
            _listenerMap.remove(
                (MBase) modelElement,
                keys[j],
                (MElementListener) listener);
        }

    }

    /**
     * Method to remove some element from the listenerObjectMap. Used by
     * delete on UmlFactory to make sure all listeners are removed.
     * @param element
     */
    synchronized void cleanUp(MBase element) {
        _listenerMap.remove(element);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) {
        if (_cat.isDebugEnabled())
            _cat.debug(
                "Pumping listRoleItemSet event with name "
                    + e.getName()
                    + " and source "
                    + e.getSource());
        MElementListener[] listeners = getListenerList(e);
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].listRoleItemSet(e);
        }
    }

    private MElementListener[] getListenerList(MElementEvent e) {
        return _listenerMap.getListeners(
            (MBase) e.getSource(),
            new EventKey(e.getType(), e.getName()));
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent e) {
        if (_cat.isDebugEnabled())
            _cat.debug(
                "Pumping propertySet event with name "
                    + e.getName()
                    + " and source "
                    + e.getSource());
        if (e.getNewValue() == null
            || !(e.getNewValue().equals(e.getOldValue()))) {
            MElementListener[] listeners = getListenerList(e);
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].propertySet(e);
            }
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent e) {
        if (_cat.isDebugEnabled())
            _cat.debug(
                "Pumping recoverd event with name "
                    + e.getName()
                    + " and source "
                    + e.getSource());
        MElementListener[] listeners = getListenerList(e);
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].recovered(e);
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent e) {
        if (_cat.isDebugEnabled())
            _cat.debug(
                "Pumping removed event with name "
                    + e.getName()
                    + " and source "
                    + e.getSource());
        MElementListener[] listeners = getListenerList(e);
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].removed(e);
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
        if (_cat.isDebugEnabled())
            _cat.debug(
                "Pumping roleAdded event with name "
                    + e.getName()
                    + " and source "
                    + e.getSource());
        MElementListener[] listeners = getListenerList(e);
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].roleAdded(e);
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
        if (_cat.isDebugEnabled())
            _cat.debug(
                "Pumping roleRemoved event with name "
                    + e.getName()
                    + " and source "
                    + e.getSource());
        MElementListener[] listeners = getListenerList(e);
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].roleRemoved(e);
        }
    }

    /**
     * Clears the hashmaps with listeners. This is only needed by the JUnit tests.
     * Therefore the visibility is 'default'.
     */
    void cleanUp() {
        _listenerMap = null;
        _listenerMap = new EventListenerHashMap();
        _classListenerMap = null;
        _classListenerMap = new ClassListenerHashMap();
    }

    ClassListenerHashMap getClassListenerMap() {
        return _classListenerMap;
    }

    EventListenerHashMap getEventListenerMap() {
        return _listenerMap;
    }

}

/**
 * Value object class to find the correct eventlistenerlist in the eventhashmap.
 * @author jaap.branderhorst@xs4all.nl
 */
class EventKey {
    private Integer _type;
    private String _name;

    static final EventKey EMPTY_KEY = new EventKey(null, null);

    public EventKey(int type, String name) {
        setType(type);
        setName(name);
    }

    public EventKey(Integer type, String name) {
        _type = type;
        setName(name);
    }

    private void setType(int type) {
        if (type < 0 || type > 10)
            throw new IllegalArgumentException(
                "This is not a legal eventtype: " + type);
        _type = new Integer(type);
    }

    public Integer getType() {
        return _type;
    }

    private void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public boolean equals(Object o) {
        if (o instanceof EventKey) {
            EventKey key = (EventKey) o;
            if ((key.getType() == null && getType() == null)
                || (key.getType() != null && key.getType().equals(getType()))
                && (key.getName() != null
                    && key.getName().equals(getName())
                    || (key.getName() == null && getName() == null)))
                return true;
        }
        return false;
    }

}

/**
 * A wrapped object array that contains triples of eventtype, eventname, interested
 * listener. I used an Object array for this purpose since it performs fast. Copied
 * a lot of the code from javax.swing.EventListenerList.
 * @author jaap.branderhorst@xs4all.nl
 */
class EventListenerList {
    /**
     *  A null array to be shared by all empty listener lists
     */
    private final static Object[] NULL_ARRAY = new Object[0];
    /**
     *  The list of EventKey - Listener pairs. It's visibility is default since
     * it's used in AbstractUmlModelFactory in a performant but quite awkward
     * way from an encapsulation point of view. 
     */
    Object[] _listenerList = NULL_ARRAY;

    /**
     * Returns an array of listeners that are interested in an event that is
     * typed by the given EventKey. If the name is null of the EventKey, all listeners
     * are returned that have the corresponding name filled or null. Same is true
     * for the type.
     * @param key
     * @return An array of listeners that are interested in the event typed by
     * the given EventKey
     */
    public synchronized MElementListener[] getListeners(EventKey key) {
        Object[] lList = _listenerList;
        int n = getListenerCount(lList, key);
        MElementListener[] result =
            (MElementListener[]) Array.newInstance(MElementListener.class, n);
        int j = 0;
        // if the event name is not set we should return all listeners interested
        if (key.getType().intValue() == 0) {
            Integer type = key.getType();
            for (int i = lList.length - 3; i >= 0; i -= 3) {
                if (type.equals(lList[i]))
                    result[j++] = (MElementListener) lList[i + 2];
            }
        } else if (key.getName() != null && key.getType() != null) {
            Integer type = key.getType();
            String name = key.getName();
            for (int i = lList.length - 3; i >= 0; i -= 3) {
                if (name.equals(lList[i + 1]) && type.equals(lList[i]))
                    result[j++] = (MElementListener) lList[i + 2];
            }
        } else
            throw new IllegalArgumentException("Illegal eventkey!");
        return result;
    }

    /**
     * <p>
     * Registers the given listeners for the event typed by EventKey. If the name 
     * is null (of the EventKey), the listener is registred for all events that
     * have a type corresponding to the type in the given key, no matter what the
     * name of the event is. Vice versa for the type.
     * </p>  
     * <p>
     * <strong>A listener that has been added twice will get the events for which
     * it registred twice. Be carefull with registring listeners!</strong>
     * </p>
     * @param key
     * @param listener
     */
    public synchronized void add(EventKey key, MElementListener listener) {
        if (listener == null || key == null)
            throw new IllegalArgumentException("Null key or null listener");
        // check if there allready is a listener
        // if (!Arrays.asList(getListeners(key)).contains(listener)) {

        if (_listenerList == NULL_ARRAY) {
            // if this is the first listener added, 
            // initialize the lists
            _listenerList =
                new Object[] { key.getType(), key.getName(), listener };
        } else {
            // Otherwise copy the array and add the new listener
            int i = _listenerList.length;
            Object[] tmp = new Object[i + 3];
            System.arraycopy(_listenerList, 0, tmp, 0, i);

            tmp[i] = key.getType();
            tmp[i + 1] = key.getName();
            tmp[i + 2] = listener;

            _listenerList = tmp;
        }
        // }
    }

    /**
     * Removes a listener from the list.
     * @param key
     * @param listener
     */
    public void remove(EventKey key, MElementListener listener) {
        if (listener == null || key == null)
            throw new IllegalArgumentException("Null key or null listener");
        if (key.getName() != null && key.getType() != null)
            for (int i = _listenerList.length - 3; i >= 0; i -= 3) {
                if (_listenerList[i + 2] == listener
                    && (
                        new EventKey(
                            (Integer) _listenerList[i],
                            (String) _listenerList[i + 1])).equals(
                        key)) {
                    removeElement(i);
                    break;
                }
            } else if (key.equals(EventKey.EMPTY_KEY)) {
            for (int i = _listenerList.length - 1; i >= 0; i -= 3) {
                if (_listenerList[i] == listener) {
                    removeElement(i - 2);
                }
            }
        } else if (key.getName() != null) {
            String name = key.getName();
            for (int i = _listenerList.length - 1; i >= 0; i -= 3) {
                if (_listenerList[i] == listener
                    && name.equals(_listenerList[i - 1])) {
                    removeElement(i - 2);
                }
            }
        } else {
            Integer type = key.getType();
            for (int i = _listenerList.length - 1; i >= 0; i -= 3) {
                if (_listenerList[i] == listener
                    && type.equals(_listenerList[i - 2])) {
                    removeElement(i - 2);
                }
            }
        }

    }

    /**
     * Does the actual removal of an element at the given index in the list.
     * @param index
     */
    private synchronized void removeElement(int index) {
        Object[] tmp = new Object[_listenerList.length - 3];
        // Copy the list up to index
        System.arraycopy(_listenerList, 0, tmp, 0, index);
        // Copy from two past the index, up to
        // the end of tmp (which is three elements
        // shorter than the old list)
        if (index < tmp.length)
            System.arraycopy(
                _listenerList,
                index + 3,
                tmp,
                index,
                tmp.length - index);
        // set the listener array to the new array or null
        _listenerList = (tmp.length == 0) ? NULL_ARRAY : tmp;
    }

    /**
     * Returns the number of listeners that are interested in the given key and 
     * exist in the given object array.
     * @param list
     * @param key
     * @return
     */
    private int getListenerCount(Object[] list, EventKey key) {
        int count = 0;
        String name = key.getName();
        Integer type = key.getType();
        if (type.intValue() == 0) {
            for (int i = 0; i < list.length; i += 3) {
                if (type.equals(list[i])) {
                    count++;
                }
            }
        } else if (name != null && type != null) {
            for (int i = 0; i < list.length; i += 3) {
                if (type.equals(list[i]) && name.equals(list[i + 1]))
                    count++;
            }
        } else
            throw new IllegalArgumentException("Illegal eventkey!");
        return count;
    }

    /**
    * Returns the total number of listeners of the supplied type 
    * for this listener list.
    */
    public synchronized int getListenerCount(EventKey key) {
        return getListenerCount(_listenerList, key);
    }

    /**
     * Returns the total number of listeners for this listener list.
     */
    public synchronized int getListenerCount() {
        return _listenerList.length / 3;
    }

}

/**
 * A map containing instances of meta-classes (modelelements) as keys and 
 * EventListenerLists as values. The class is a wrapper around an underlying
 * java.util.HashMap and provides some custom methods for easy access to the 
 * underlying data structure. 
 * @author jaap.branderhorst@xs4all.nl
 */
class EventListenerHashMap {

    /**
     *  A null array to be shared by all empty listener lists
     */
    private final static MElementListener[] NULL_ARRAY =
        new MElementListener[0];

    /**
     *  The list of ListenerType - Listener pairs 
     */
    private transient Map _listenerMap = new HashMap();

    /**
     * Puts the given listener as listener to the given modelelement and given eventKey 
     * in the map.
     * @param element
     * @param key
     * @param listener
     */
    public synchronized void put(
        MBase element,
        EventKey key,
        MElementListener listener) {
        if (element == null || listener == null)
            throw new IllegalArgumentException("Modelelement or listener null");
        EventListenerList list = (EventListenerList) _listenerMap.get(element);
        if (list == null) {
            list = new EventListenerList();
            _listenerMap.put(element, list);
        }
        list.add(key, listener);
    }

    /**
     * Removes a listener for a given eventkey and a given modelelement.
     * @param element
     * @param key
     * @param listener
     */
    public synchronized void remove(
        MBase element,
        EventKey key,
        MElementListener listener) {
        if (element == null || listener == null)
            throw new IllegalArgumentException("Modelelement or listener null");
        EventListenerList list = (EventListenerList) _listenerMap.get(element);
        if (list != null) {
            list.remove(key, listener);
        }
    }

    /**
     * Removes the complete EventListenerList for the given element.
     * @param element
     */
    public synchronized void remove(MBase element) {
        _listenerMap.remove(element);
    }

    /**
     * Returns all listeners that are registered for the given modelElement and
     * the given EventKey.
     * @param element
     * @param key
     * @return
     */
    public MElementListener[] getListeners(MBase element, EventKey key) {
        EventListenerList list = (EventListenerList) _listenerMap.get(element);
        return list == null ? NULL_ARRAY : list.getListeners(key);
    }

    /**
     * Tests wether there are any listeners registred for any modelelements and eventkeys.
     * @return
     */
    public boolean isEmpty() {
        return _listenerMap.isEmpty();
    }
}

/**
 * A map that holds ElementListenerLists with lists of listeners that are interested
 * in each and every event of a certain type of the instances of a certain meta-class.
 * @author jaap.branderhorst@xs4all.nl
 */
class ClassListenerHashMap {

    /**
     *  A null array to be shared by all empty listener lists
     */
    private final static MElementListener[] NULL_ARRAY =
        new MElementListener[0];

    /**
     *  The list of ListenerType - Listener pairs 
     */
    private transient Map _listenerMap = new HashMap();

    /**
     * Puts a listener that is interested in a certain event that will be send
     * by instances of the given meta-class.
     * @param element The meta-class the listener is interested in
     * @param key The type/name pair designating the event type
     * @param listener The listener that's interested in the given event type
     */
    public synchronized void put(
        Class element,
        EventKey key,
        MElementListener listener) {
        if (element == null || listener == null)
            throw new IllegalArgumentException("Modelelement or listener null");
        EventListenerList list = (EventListenerList) _listenerMap.get(element);
        if (list == null) {
            list = new EventListenerList();
            _listenerMap.put(element, list);
        }
        list.add(key, listener);
    }

    /**
     * Removes a listener as being interested in the given event.
     * @param element
     * @param key
     * @param listener
     */
    public synchronized void remove(
        Class element,
        EventKey key,
        MElementListener listener) {
        if (element == null || listener == null)
            throw new IllegalArgumentException("Modelelement or listener null");
        EventListenerList list = (EventListenerList) _listenerMap.get(element);
        if (list != null) {
            list.remove(key, listener);
        }
    }

    /**
     * Removes the list of listeners for the given meta-class.
     * @param element
     */
    public synchronized void remove(Class element) {
        _listenerMap.remove(element);
    }

    /**
     * Returns all listeners that are interested in the given event.
     * @param element
     * @param key
     * @return
     */
    public MElementListener[] getListeners(Class element, EventKey key) {
        EventListenerList list = (EventListenerList) _listenerMap.get(element);
        return list == null ? NULL_ARRAY : list.getListeners(key);
    }

    /** 
     * Returns an EventListenerList with listeners that are interested in certain
     * events of the given meta-class.
     * @param element
     * @return
     */
    public EventListenerList[] getListenerList(Class element) {
        // element = formatClass(element);
        Class[] hierarchy = getHierarchy(element);
        EventListenerList[] lists = new EventListenerList[hierarchy.length];
        EventListenerList list = null;
        for (int i = 0; i < lists.length; i++) {
            list = (EventListenerList) _listenerMap.get(hierarchy[i]);
            lists[i] = list == null ? new EventListenerList() : list;
        }
        return lists;
    }

    private Class[] getHierarchy(Class clazz) {
        Class[] returnClass = null;
        if (clazz != Object.class) {
            Class[] tmp = getHierarchy(clazz.getSuperclass());
            returnClass =
                (Class[]) Array.newInstance(Class.class, tmp.length + 1);
            System.arraycopy(tmp, 0, returnClass, 0, tmp.length);
            returnClass[tmp.length] = clazz;
        } else {
            returnClass = new Class[] { clazz };
        }
        return returnClass;
    }

    /**
     * Tests if the hashmap is empty.
     * @return
     */
    public boolean isEmpty() {
        return _listenerMap.isEmpty();
    }

}

/**
 * Class containing the definitions of all events in NSUML. The file eventtree.xml
 * is loaded by this class and converted to a hashmap containing the classes firing
 * events as keys and hashmaps containing eventnames as keys and eventtypes as values 
 * as values.  
 * @author jaap.branderhorst@xs4all.nl
 */
class EventTreeDefinition {
    private Logger _log = Logger.getLogger(this.getClass());
    private final static String FILE_NAME = "org/argouml/eventtree.xml";
    private Map _definition = new HashMap();

    public EventTreeDefinition() {
        Document doc = loadDocument();
        synchronized (doc) {
            NodeList sources = doc.getChildNodes().item(0).getChildNodes();
            for (int i = 0; i < sources.getLength(); i++) {
                Element source = (Element) sources.item(i);
                String className = source.getAttribute("classname");
                Class sourceClass = null;
                try {
                    sourceClass = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    _log.error(e);
                }
                Map nameMap = new HashMap();
                NodeList eventTypes = source.getChildNodes();
                for (int j = 0; j < eventTypes.getLength(); j++) {
                    Element eventType = (Element) eventTypes.item(j);
                    String name = eventType.getAttribute("name");
                    NodeList typeNodes = eventType.getChildNodes();
                    int typeLength = typeNodes.getLength();
                    int[] types = new int[typeLength];
                    for (int k = 0; k < typeLength; k++) {
                        Element typeNode = (Element) typeNodes.item(k);
                        types[k] =
                            Integer.parseInt(
                                typeNode.getFirstChild().getNodeValue());
                    }
                    nameMap.put(name, types);
                }
                // remove case
                nameMap.put(UmlModelEventPump.REMOVE, new int[] { 0 });
                _definition.put(sourceClass, nameMap);
            }
        }
        System.out.println();
    }

    /**
     * Returns all eventkeys that an instance of the given modelClass could possibly
     * fire.
     * @param modelClass
     * @return
     */
    public EventKey[] getEventTypes(Class modelClass) {
        modelClass = formatClass(modelClass);
        Map nameMap = (Map) _definition.get(modelClass);
        Iterator it = nameMap.keySet().iterator();
        int size = 0;
        while (it.hasNext()) {
            size += ((int[]) nameMap.get(it.next())).length;
        }
        EventKey[] result = new EventKey[size + 1];
        int counter = 0;
        it = nameMap.keySet().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            int[] types = (int[]) nameMap.get(name);
            EventKey[] keys = new EventKey[types.length];
            for (int i = 0; i < types.length; i++) {
                keys[i] = new EventKey(types[i], name);
            }
            System.arraycopy(keys, 0, result, counter, keys.length);
            counter += keys.length;
        }
        // remove event
        System.arraycopy(
            new EventKey[] { new EventKey(0, null)},
            0,
            result,
            counter,
            1);
        return result;
    }

    /**
     * Returns all EventKeys (eventdefinitions) with the given eventName 
     * that the given modelClass can fire.
     * @param modelClass
     * @param name
     * @return
     */
    public EventKey[] getEventTypes(Class modelClass, String name) {
        modelClass = formatClass(modelClass);
        Map nameMap = (Map) _definition.get(modelClass);
        if (nameMap != null) {
            int[] types = (int[]) nameMap.get(name);
            if (types != null) {

                EventKey[] keys = new EventKey[types.length];
                for (int i = 0; i < types.length; i++) {
                    keys[i] = new EventKey(types[i], name);
                }
                return keys;
            }
        }
        return new EventKey[0];
    }

    private synchronized Document loadDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document eventNamesDoc =
                builder.parse(
                    getClass().getClassLoader().getResourceAsStream(FILE_NAME));
            return eventNamesDoc;
        } catch (ParserConfigurationException e) {
            _log.fatal(e);
            System.exit(-1);
        } catch (SAXException e) {
            _log.fatal(e);
            System.exit(-1);
        } catch (IOException e) {
            _log.fatal(e);
            System.exit(-1);
        }
        return null;
    }

    private Class formatClass(Class inputClass) {
        String name = inputClass.getName();
        if (name.endsWith("Impl"))
            return inputClass;
        else {
            try {
                Class returnClass = Class.forName(name + "Impl");
                return returnClass;
            } catch (ClassNotFoundException ignorable) {
                // cannot happen
            }
        }
        return null;

    }

}