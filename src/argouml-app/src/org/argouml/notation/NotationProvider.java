/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2009 The Regents of the University of California. All
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

package org.argouml.notation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;

/**
 * A class that implements this abstract class manages a text
 * shown on a diagram. This means it is able to generate
 * text that represents one or more UML objects.
 * And when the user has edited this text, the model may be adapted
 * by parsing the text.
 * Additionally, a help text for the parsing is provided,
 * so that the user knows the syntax.
 *
 * @author Michiel van der Wulp
 */
public abstract class NotationProvider implements PropertyChangeListener {

    private static final Logger LOG =
        Logger.getLogger(NotationProvider.class.getName());
    private NotationRenderer renderer;

    /**
     * A collection of properties of listeners registered for this notation.
     * Each entry is a 2 element array containing the element and the property
     * name(s) for which a listener is registered. This facilitates easy removal
     * of a complex set of listeners.
     */
    private final Collection<Object[]> listeners = new ArrayList<Object[]>();

    /**
     * @return a i18 key that represents a help string
     *         giving an explanation to the user of the syntax
     */
    public abstract String getParsingHelp();

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
     * Generate a string representation for the given model element.
     *
     * @param modelElement the base UML element
     * @param settings settings that control rendering of the text
     * @return the string written in the correct notation
     */
    public abstract String toString(Object modelElement,
            NotationSettings settings);

    /**
     * Initialize the appropriate model change listeners
     * for the given modelelement to the given listener.
     * Overrule this when you need more than
     * listening to all events from the base modelelement.
     *
     * @param modelElement the modelelement that we provide
     * notation for
     */
    public void initialiseListener(Object modelElement) {
        addElementListener(modelElement);
    }

    /**
     * Clean out the listeners registered before.
     * <p>
     * The default implementation is to remove all listeners
     * that were remembered by the utility functions below.
     */
    public void cleanListener() {
        removeAllElementListeners();
    }

    /**
     * Update the set of listeners based on the given event. <p>
     *
     * The default implementation just removes all listeners, and then
     * re-initializes completely - this is method 1.
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
     * @param modelElement the modelelement that we provide
     * notation for
     * @param pce the received event, that we base the changes on
     */
    public void updateListener(Object modelElement, PropertyChangeEvent pce) {
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
            LOG.log(Level.WARNING, "Encountered deleted object during delete of "
                    + modelElement);
            return;
        }
        cleanListener();
        initialiseListener(modelElement);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (renderer != null) {
            Object owner = renderer.getOwner(this);
            if ((owner == evt.getSource())
                    && (evt instanceof DeleteInstanceEvent)) {
                return;
            }
            if (owner != null) {
                if (Model.getUmlFactory().isRemoved(owner)) {
                    LOG.log(Level.WARNING, "Encountered deleted object during delete of "
                            + owner);
                    return;
                }
                renderer.notationRenderingChanged(this,
                        toString(owner, renderer.getNotationSettings(this)));
                if (evt instanceof AddAssociationEvent
                        || evt instanceof RemoveAssociationEvent) {
                    initialiseListener(owner);
                }
            }
        }
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
            LOG.log(Level.WARNING, "Encountered deleted object during delete of " + element);
            return;
        }
        Object[] entry = new Object[] {element, null};
        if (!listeners.contains(entry)) {
            listeners.add(entry);
            Model.getPump().addModelEventListener(listener, element);
        } else {
            LOG.log(Level.WARNING, "Attempted duplicate registration of event listener"
                    + " - Element: " + element + " Listener: " + listener);
        }
    }

    /**
     * Utility function to add a listener for an array of property names
     * and remember the registration.
     *
     * @param element element to listen for changes on
     */
    public final void addElementListener(Object element) {
        addElementListener(this, element);
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
            LOG.log(Level.WARNING, "Encountered deleted object during delete of " + element);
            return;
        }
        Object[] entry = new Object[] {element, property};
        if (!listeners.contains(entry)) {
            listeners.add(entry);
            Model.getPump().addModelEventListener(listener, element, property);
        } else {
            LOG.log(Level.FINE, "Attempted duplicate registration of event listener"
                    + " - Element: {0} Listener: {1}", new Object[]{element, listener});
        }
    }

    /**
     * Utility function to add a listener for an array of property names
     * and remember the registration.
     *
     * @param element element to listen for changes on
     * @param property name of property to listen for changes of
     */
    public final void addElementListener(Object element, String property) {
        addElementListener(this, element, property);
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
            LOG.log(Level.WARNING, "Encountered deleted object during delete of " + element);
            return;
        }
        Object[] entry = new Object[] {element, property};
        if (!listeners.contains(entry)) {
            listeners.add(entry);
            Model.getPump().addModelEventListener(listener, element, property);
        } else {
            LOG.log(Level.FINE,
                    "Attempted duplicate registration of event listener"
                    + " - Element: " + element + " Listener: " + listener);
        }
    }

    /**
     * Utility function to add a listener for an array of property names
     * and remember the registration.
     *
     * @param element element to listen for changes on
     * @param property array of property names (Strings)
     * to listen for changes of
     */
    public final void addElementListener(Object element, String[] property) {
        addElementListener(this, element, property);
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

    /**
     * Utility function to remove an element listener
     * and adapt the remembered list of registration.
     *
     * @param element element to listen for changes on
     */
    public final void removeElementListener(Object element) {
        removeElementListener(this, element);
    }

    /*
     * Utility function to unregister all listeners
     * registered through addElementListener.
     *
     * @see #addElementListener(Object, String)
     */
    protected final void removeAllElementListeners(
            PropertyChangeListener listener) {
        for (Object[] lis : listeners) {
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

    /**
     * Utility function to unregister all listeners
     * registered through addElementListener.
     */
    public final void removeAllElementListeners() {
        removeAllElementListeners(this);
    }

    /**
     * @param nr the NotationRenderer
     */
    void setRenderer(NotationRenderer nr) {
        renderer = nr;
    }
}
