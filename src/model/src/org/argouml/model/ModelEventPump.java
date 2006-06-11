// $Id$
// Copyright (c) 2004-2006 The Regents of the University of California. All
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

package org.argouml.model;

import java.beans.PropertyChangeListener;

import javax.swing.Action;

/**
 * This is the interface for registering interest in events sent from
 * the model component.<p>
 *
 * Neither the registration or unregistration, nor the events themselves
 * contain any reference to the model implementation.  Instead they
 * use the {@link java.beans.PropertyChangeListener},
 * {@link java.beans.PropertyChangeEvent} to
 * deliver events.<p>
 *
 * The reference to the listener is a WeakReference so you don't need to
 * call removeWHATEVERListener, you can just forget about your listener
 * and it is eventually finalized and removed. This also means that you
 * will have to keep a reference to your listener while it is active.
 * Since the garbage collecting mechanism is not really deterministic
 * a forgotten about listener might still receive events. Unless it can
 * handle them in a harmless way, this approach should not be used.
 *
 * TODO: (Is this still true or does it refer to the NSUML
 * implementation? - tfm 20051109)
 * (This is part of the contract that is established between the Model
 * subsystem and its users. If that is not fulfilled by the current
 * implementation, then the current implementation is incorrect.
 * Linus 20060411).<p>
 *
 * TODO: What event names?
 * The event names generated are {@link String}s and their values and
 * meanings are not really well documented. In general they are the
 * name of an an association end or attribute in the UML metamodel.<p>
 *
 * Here are some highlights:<ul>
 * <li>"remove" - event sent when the element is removed.
 * </ul>
 *
 * @author Linus Tolke
 */
public interface ModelEventPump {
    // Operations that work on elements:

    /**
     * Adds a listener to modelevents that are fired by some given modelelement
     * and that have any of the names in eventNames.<p>
     *
     * @param listener The listener to add
     * @param modelelement The modelelement the listener should be added too
     * @param propertyNames The array of property names the listener wishes to
     * receive events for
     */
    void addModelEventListener(PropertyChangeListener listener,
            		       Object modelelement,
			       String[] propertyNames);

    /**
     * Adds a listener to modelevents that are fired by some given modelelement
     * and that have the name in eventName.<p>
     *
     * @param listener The listener to add
     * @param modelelement The modelelement the listener should be added too
     * @param propertyName The property name the listener wishes to
     * receive events for
     */
    void addModelEventListener(PropertyChangeListener listener,
			       Object modelelement,
			       String propertyName);

    /**
     * Adds a listener to all events fired by some modelelement.
     *
     * @param listener is the listener to add
     * @param modelelement is the model element
     */
    void addModelEventListener(PropertyChangeListener listener,
            		       Object modelelement);

    /**
     * Removes a listener that listens to modelevents with name
     * eventNames that are fired by the given modelelement.<p>
     *
     * @param listener The listener to remove
     * @param modelelement The modelelement that fires the events the
     * listener is listening to.
     * @param propertyNames The property names the listener no longer wishes to
     * receive events for
     */
    void removeModelEventListener(PropertyChangeListener listener,
				  Object modelelement,
				  String[] propertyNames);

    /**
     * Removes a listener that listens to modelevents with name
     * eventName that are fired by the given modelelement.<p>
     *
     * @param listener The listener to remove.
     * @param modelelement The modelelement that fires the events the
     * listener is listening to.
     * @param propertyName The property name the listener no longer wishes to
     * receive events for
     */
    void removeModelEventListener(PropertyChangeListener listener,
				  Object modelelement,
				  String propertyName);

    /**
     * Removes a listener that listens to all events fired by the
     * given modelelement.<p>
     *
     * @param listener is the listener
     * @param modelelement is the model element
     */
    void removeModelEventListener(PropertyChangeListener listener,
				  Object modelelement);

    // Operations that work on classes of objects:

    /**
     * Adds a listener that listens to all events that are named eventNames and
     * that occur to instances of the given modelClass.
     * <p>
     * 
     * @param listener
     *            is the listener to add.
     * @param modelClass
     *            is the given model class
     * @param propertyNames
     *            The property names the listener wishes to receive events for.
     *            If null or an empty array, all events will be returned.
     * @throws IllegalArgumentException
     *             if one of the arguments is null or if the modelClass is not a
     *             valid metamodel class.
     * @throws IllegalStateException
     *             if the listener is already registred.
     */
    void addClassModelEventListener(PropertyChangeListener listener,
				    Object modelClass,
				    String[] propertyNames);

    /**
     * Adds a listener that listens to the event that is named eventName and
     * that occur to instances of the given modelClass.
     * <p>
     * 
     * @param listener
     *            is the listener to add.
     * @param modelClass
     *            is the given model class
     * @param propertyName
     *            The property name the listener wishes to receive events for.
     * @throws IllegalArgumentException
     *             if one of the arguments is null or if the modelClass is not a
     *             valid metamodel class.
     * @throws IllegalStateException
     *             if the listener is already registred.
     */
    void addClassModelEventListener(PropertyChangeListener listener,
				    Object modelClass,
				    String propertyName);

    /**
     * Removes a listener that listens to all modelevents fired by instances of
     * modelClass and that have the original name eventNames.
     *
     * @param listener The listener to remove
     * @param modelClass The class the listener does not want to listen to
     * instances anymore
     * @param propertyNames The property names the listener no longer wishes to
     * receive events for
     */
    void removeClassModelEventListener(PropertyChangeListener listener,
				       Object modelClass,
				       String[] propertyNames);

    /**
     * Removes a listener that listens to all modelevents fired by instances of
     * modelClass and that have the name eventName.
     *
     * @param listener The listener to remove
     * @param modelClass The class the listener does not want to listen to
     * instances anymore.
     * @param propertyName The property name the listener no longer wishes to
     * receive events for
     */
    void removeClassModelEventListener(PropertyChangeListener listener,
				       Object modelClass,
				       String propertyName);

    /**
     * Register an Action with the pump that is used to perform saving.
     * This action will be enabled by any change to the model.
     *
     * @param saveAction the action to enable on change to model.
     */
    void setSaveAction(Action saveAction);

    /**
     * Start the ModelEventPump firing events.
     */
    void startPumpingEvents();

    /**
     * Stop the ModelEventPump firing events.
     */
    void stopPumpingEvents();

    /**
     * Flush events from the ModelEventPump.
     * Blocks until all events have been delivered.
     */
    void flushModelEvents();
}
