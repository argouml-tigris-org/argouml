// $Id: NotationProvider.java 12546 2007-05-05 16:54:40Z linus $
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.notation.providers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.argouml.model.Model;

/**
 * A class that implements this abstract class manages a text
 * shown on a diagram. This means it is able to generate
 * text that represents one or more UML objects.
 * And when the user has edited this text, the model may be adapted
 * by parsing the text.
 * Additionally, a help text for the parsing is provided,
 * so that the user knows the syntax.
 * 
 * @author mvw@tigris.org
 */
public abstract class NotationProvider {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(NotationProvider.class);
    
    /**
     * A collection of listeners registered for this notation. 
     * This facilitates easy removal of a complex set of listeners. 
     */
    private Collection listeners = new ArrayList();

    /**
     * @return a i18 key that represents a help string
     *         giving an explanation to the user of the syntax
     */
    public abstract String getParsingHelp();

    
    /**
     * Utility function to determine the presence of a key. 
     * The default is false.
     * 
     * @param key the string for the key
     * @param map the hashmap to check for the presence 
     * and value of the key
     * @return true if the value for the key is true, otherwise false
     */
    public static boolean isValue(String key, HashMap map) {
        if (map == null) return false;
        Object o = map.get(key);
        if (!(o instanceof Boolean)) {
            return false;
        }
        return ((Boolean) o).booleanValue();
    }

    /**
     * Parses the given text, and adapts the modelElement and
     * maybe related elements accordingly.
     * 
     * @param modelElement the modelelement to adapt
     * @param text the string given by the user to be parsed
     * to adapt the model
     */
    public abstract void parse(Object modelElement, String text);

    /**
     * Generates a string representation for the given model element.
     * 
     * @param modelElement the base UML modelelement
     * @param args arguments that may determine the notation
     * @return the string written in the correct notation
     */
    public abstract String toString(Object modelElement, HashMap args);
    
    /**
     * Initialise the appropriate model change listeners 
     * for the given modelelement to the given listener.
     * Overrule this when you need more than 
     * listening to all events from the base modelelement.
     * 
     * @param listener the given listener
     * @param modelElement the modelelement that we provide 
     * notation for
     */
    public void initialiseListener(PropertyChangeListener listener, 
            Object modelElement) {
        addElementListener(listener, modelElement);
    }
    
    /**
     * Clean out the listeners registered before.
     * The default implementation is to remove all listeners 
     * that were remembered by the utility functions below.
     * 
     * @param listener the given listener
     * @param modelElement the modelelement that we provide 
     * notation for
     */
    public void cleanListener(PropertyChangeListener listener, 
            Object modelElement) {
        removeAllElementListeners(listener);
    }
    
    /**
     * Update the set of listeners based on the given event. <p>
     * 
     * The default implementation just removes all listeners, and then 
     * re-initialises completely - this is method 1. 
     * A more efficient way would be to dissect 
     * the propertyChangeEvent, and only adapt the listeners
     * that need to be adapted - this is method 2. <p>
     * 
     * Method 2 is explained by the code below that is commented out.
     * Method 1 is the easiest to implement, since at every arrival of an event,
     * we just remove all old listeners, and then inspect the current model, 
     * and add listeners where we need them. I.e. the advantage is 
     * that we only need to traverse the model structure in one location, i.e. 
     * the initialiseListener() method.
     * 
     * @param listener the given listener
     * @param modelElement the modelelement that we provide 
     * notation for
     * @param pce the received event, that we base the changes on
     */
    public void updateListener(PropertyChangeListener listener, 
            Object modelElement,
            PropertyChangeEvent pce) {
        // e.g. for an operation:
        // if pce.getSource() == modelElement
        // && event.propertyName = "parameter"
        //     if event instanceof AddAssociationEvent
        //         Get the parameter instance from event.newValue
        //         Call model to add listener on parameter on change 
        //             of "name", "type"
        //     else if event instanceof RemoveAssociationEvent
        //         Get the parameter instance from event.oldValue
        //         Call model to remove listener on parameter on change 
        //             of "name", "type"
        //     end if
        // end if 
        if (Model.getUmlFactory().isRemoved(modelElement)) {
            LOG.warn("Encountered deleted object during delete of " 
                    + modelElement);
            return;
        }
        cleanListener(listener, modelElement);
        initialiseListener(listener, modelElement);
    }
    
    /*
     * Add an element listener and remember the registration.
     * 
     * @param element
     *            element to listen for changes on
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener, Object, String)
     */
    protected final void addElementListener(PropertyChangeListener listener, 
            Object element) {
        if (Model.getUmlFactory().isRemoved(element)) {
            LOG.warn("Encountered deleted object during delete of " + element);
            return;
        }
        listeners.add(new Object[] {element, null});
        Model.getPump().addModelEventListener(listener, element);
    }
    
    /*
     * Utility function to add a listener for a given property name 
     * and remember the registration.
     * 
     * @param element
     *            element to listen for changes on
     * @param property
     *            name of property to listen for changes of
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener,
     *      Object, String)
     */
    protected final void addElementListener(PropertyChangeListener listener, 
            Object element, String property) {
        if (Model.getUmlFactory().isRemoved(element)) {
            LOG.warn("Encountered deleted object during delete of " + element);
            return;
        }
        listeners.add(new Object[] {element, property});
        Model.getPump().addModelEventListener(listener, element, property);
    }

    /*
     * Utility function to add a listener for an array of property names 
     * and remember the registration.
     * 
     * @param element
     *            element to listen for changes on
     * @param property
     *            array of property names (Strings) to listen for changes of
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener,
     *      Object, String)
     */
    protected final void addElementListener(PropertyChangeListener listener, 
            Object element, String[] property) {
        if (Model.getUmlFactory().isRemoved(element)) {
            LOG.warn("Encountered deleted object during delete of " + element);
            return;
        }
        listeners.add(new Object[] {element, property});
        Model.getPump().addModelEventListener(listener, element, property);
    }
    
    /*
     * Utility function to remove an element listener 
     * and adapt the remembered list of registration.
     * 
     * @param element
     *            element to listen for changes on
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener, Object, String)
     */
    protected final void removeElementListener(PropertyChangeListener listener, 
            Object element) {
        listeners.remove(new Object[] {element, null});
        Model.getPump().removeModelEventListener(listener, element);
    }
    
    /*
     * Utility function to unregister all listeners 
     * registered through addElementListener.
     * 
     * @see #addElementListener(Object, String)
     */
    protected final void removeAllElementListeners(
            PropertyChangeListener listener) {
        for (Iterator iter = listeners.iterator(); iter.hasNext();) {
            Object[] lis = (Object[]) iter.next();
            Object property = lis[1];
            if (property == null) {
                Model.getPump().removeModelEventListener(listener, lis[0]);
            } else if (property instanceof String[]) {
                Model.getPump().removeModelEventListener(listener, lis[0],
                        (String[]) property);
            } else if (property instanceof String) {
                Model.getPump().removeModelEventListener(listener, lis[0],
                        (String) property);
            } else {
                throw new RuntimeException(
                        "Internal error in removeAllElementListeners");
            }
        }
        listeners.clear();
    }

}
