// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import javax.swing.Action;
import javax.swing.event.EventListenerList;

import org.argouml.model.Model;

/**
 * This class captures the information about the "change flag" of
 * the current project. Initially, the "change flag" is clear,
 * which means that there is nothing in the project to save.
 * The "change flag" is set by every change made to its UML model,
 * which means that the project becomes 'dirty', i.e. it needs saving. <p>
 *
 * The UmlModelListener is a single listener that listens to all
 * UML ModelElement events and on its turn, sends
 * events out that indicate that the "change flag" of the Save action
 * needs to be set. <p>
 *
 * This class only keeps an eye on changes to the UML model - i.e.
 * it does not notice changes done to e.g. the graph like moving an edge
 * (when they do not also change the UML model), settings, todo items
 * or the diagrams. <p>
 *
 * The UmlModelListener only transfers information when the flag needs
 * to be set - it does not know its current status. That is a task for the
 * listeners in the <code>listenerList</code>.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */
public final class UmlModelListener implements PropertyChangeListener {

    /**
     * Singleton instance.
     */
    private static final UmlModelListener INSTANCE = new UmlModelListener();

    /**
     * The listener list.
     */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * The name of the property that defines the save state.
     */
    public static final String SAVE_STATE_PROPERTY_NAME = "saveState";

    /**
     * The action to enable when the model changes.
     */
    private Action saveAction;

    /**
     * Singleton instance access method.
     *
     * @return the singleton instance.
     */
    public static UmlModelListener getInstance() {
        return INSTANCE;
    }

    /**
     * Don't allow instantiation.
     */
    private UmlModelListener() {
    }

    /**
     * Register the Action that will be enabled whenever a model
     * change takes place.
     * @param action the action to be enabled on model change.
     */
    public void setSaveAction(Action action) {
        saveAction = action;
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getNewValue() != null
            && !pce.getNewValue().equals(pce.getOldValue())) {
            fireNeedsSavePropertyChanged();
        }
    }

    /**
     * For every new ModelElement that has been created, we want
     * to register for updation events.
     *
     * @param elm the UML modelelement that has been created
     */
    public void newElement(Object elm) {
        Model.getPump().addModelEventListener(this, elm);
    }

    /**
     * For every ModelElement that has been deleted, we want to
     * remove its listener.
     *
     * @param elm the UML modelelement that has been deleted
     */
    public void deleteElement(Object elm) {
        Model.getPump().removeModelEventListener(this, elm);
    }


    /**
     * Adds a listener to the listener list.
     *
     * @param listener The listener to add.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listenerList.add(PropertyChangeListener.class, listener);
    }

    /**
     * Removes a listener from the listener list.
     *
     * @param listener The listener to remove.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listenerList.remove(PropertyChangeListener.class, listener);
    }

    /**
     * Fire an event to all members of the listener list.
     */
    private void fireNeedsSavePropertyChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PropertyChangeListener.class) {
                // Lazily create the event:
                PropertyChangeEvent event =
		    new PropertyChangeEvent(
                            this,
                            SAVE_STATE_PROPERTY_NAME,
                            new Boolean(false),
                            new Boolean(true));
                ((PropertyChangeListener) listeners[i + 1]).propertyChange(
                    event);
            }
        }
    }
}
