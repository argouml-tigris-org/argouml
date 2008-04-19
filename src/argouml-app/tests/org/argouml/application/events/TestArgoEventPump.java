// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.application.events;

import junit.framework.TestCase;

/**
 * Tests initialization of ArgoEventPump.
 * 
 * @author Scott Roberts
 */
public class TestArgoEventPump extends TestCase {

    // initialize the event listener trackers which
    // verify that all of the proper events were fired
    private TArgoHelpEventListener helpTracker = 
        new TArgoHelpEventListener();

    private TArgoStatusEventListener statusTracker = 
        new TArgoStatusEventListener();

    private TArgoProfileEventListener profileTracker = 
        new TArgoProfileEventListener();

    private TArgoNotationEventListener notationTracker = 
        new TArgoNotationEventListener();

    private TArgoGeneratorEventListener generatorTracker = 
        new TArgoGeneratorEventListener();

    private TArgoDiagramAppearanceEventListener diagramTracker = 
        new TArgoDiagramAppearanceEventListener();

    /**
     * The constructor.
     * 
     * @param name the name of the test.
     */
    public TestArgoEventPump(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // register all of the event listener trackers
        ArgoEventPump.addListener(ArgoEventTypes.HELP_CHANGED, helpTracker);
        ArgoEventPump.addListener(ArgoEventTypes.HELP_REMOVED, helpTracker);
        ArgoEventPump.addListener(ArgoEventTypes.STATUS_TEXT, statusTracker);
        ArgoEventPump.addListener(ArgoEventTypes.STATUS_CLEARED, statusTracker);
        ArgoEventPump.addListener(ArgoEventTypes.STATUS_PROJECT_SAVED,
                statusTracker);
        ArgoEventPump.addListener(ArgoEventTypes.STATUS_PROJECT_LOADED,
                statusTracker);
        ArgoEventPump.addListener(ArgoEventTypes.STATUS_PROJECT_MODIFIED,
                statusTracker);
        ArgoEventPump.addListener(ArgoEventTypes.PROFILE_ADDED, profileTracker);
        ArgoEventPump.addListener(ArgoEventTypes.PROFILE_REMOVED,
                profileTracker);
        ArgoEventPump.addListener(ArgoEventTypes.NOTATION_CHANGED,
                notationTracker);
        ArgoEventPump.addListener(ArgoEventTypes.NOTATION_ADDED,
                notationTracker);
        ArgoEventPump.addListener(ArgoEventTypes.NOTATION_REMOVED,
                notationTracker);
        ArgoEventPump.addListener(ArgoEventTypes.NOTATION_PROVIDER_ADDED,
                notationTracker);
        ArgoEventPump.addListener(ArgoEventTypes.NOTATION_PROVIDER_REMOVED,
                notationTracker);
        ArgoEventPump.addListener(ArgoEventTypes.GENERATOR_CHANGED,
                generatorTracker);
        ArgoEventPump.addListener(ArgoEventTypes.GENERATOR_ADDED,
                generatorTracker);
        ArgoEventPump.addListener(ArgoEventTypes.GENERATOR_REMOVED,
                generatorTracker);
        ArgoEventPump.addListener(ArgoEventTypes.DIAGRAM_FONT_CHANGED,
                diagramTracker);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // remove all of the listeners
        ArgoEventPump.removeListener(ArgoEventTypes.HELP_CHANGED, helpTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.HELP_REMOVED, helpTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.STATUS_TEXT, statusTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.STATUS_CLEARED,
                statusTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.STATUS_PROJECT_SAVED,
                statusTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.STATUS_PROJECT_LOADED,
                statusTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.STATUS_PROJECT_MODIFIED,
                statusTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.PROFILE_ADDED,
                profileTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.PROFILE_REMOVED,
                profileTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.NOTATION_CHANGED,
                notationTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.NOTATION_ADDED,
                notationTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.NOTATION_REMOVED,
                notationTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.NOTATION_PROVIDER_ADDED,
                notationTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.NOTATION_PROVIDER_REMOVED,
                notationTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.GENERATOR_CHANGED,
                generatorTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.GENERATOR_ADDED,
                generatorTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.GENERATOR_REMOVED,
                generatorTracker);
        ArgoEventPump.removeListener(ArgoEventTypes.DIAGRAM_FONT_CHANGED,
                diagramTracker);
    }

    private void resetTrackers() {
        helpTracker.resetEventStatus();
        statusTracker.resetEventStatus();
        profileTracker.resetEventStatus();
        notationTracker.resetEventStatus();
        generatorTracker.resetEventStatus();
        diagramTracker.resetEventStatus();
    }

    /**
     * Test firing of all events.
     */
    public void testAllEvents() {

        resetTrackers();

        // fire all of the status events
        ArgoEvent evt = new ArgoStatusEvent(ArgoEventTypes.STATUS_TEXT, this,
                "Test Event");
        ArgoEventPump.fireEvent(evt);
        evt = new ArgoStatusEvent(ArgoEventTypes.STATUS_CLEARED, this,
                "Test Event");
        ArgoEventPump.fireEvent(evt);
        evt = new ArgoStatusEvent(ArgoEventTypes.STATUS_PROJECT_SAVED, this,
                "Test Event");
        ArgoEventPump.fireEvent(evt);
        evt = new ArgoStatusEvent(ArgoEventTypes.STATUS_PROJECT_LOADED, this,
                "Test Event");
        ArgoEventPump.fireEvent(evt);
        evt = new ArgoStatusEvent(ArgoEventTypes.STATUS_PROJECT_MODIFIED, this,
                "Test Event");
        ArgoEventPump.fireEvent(evt);

        // fire all of the diagram events
        evt = new ArgoDiagramAppearanceEvent(
                ArgoEventTypes.DIAGRAM_FONT_CHANGED, this);
        ArgoEventPump.fireEvent(evt);

        // fire all of the generator events
        evt = new ArgoGeneratorEvent(ArgoEventTypes.GENERATOR_CHANGED, this);
        ArgoEventPump.fireEvent(evt);
        evt = new ArgoGeneratorEvent(ArgoEventTypes.GENERATOR_ADDED, this);
        ArgoEventPump.fireEvent(evt);
        evt = new ArgoGeneratorEvent(ArgoEventTypes.GENERATOR_REMOVED, this);
        ArgoEventPump.fireEvent(evt);

        // fire all of the help events
        evt = new ArgoHelpEvent(ArgoEventTypes.HELP_CHANGED, this, 
                "Test Event");
        ArgoEventPump.fireEvent(evt);
        evt = new ArgoHelpEvent(ArgoEventTypes.HELP_REMOVED, this, 
                "Test Event");
        ArgoEventPump.fireEvent(evt);

        // fire all of the notation events
        evt = new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, this);
        ArgoEventPump.fireEvent(evt);
        evt = new ArgoNotationEvent(ArgoEventTypes.NOTATION_ADDED, this);
        ArgoEventPump.fireEvent(evt);
        evt = new ArgoNotationEvent(ArgoEventTypes.NOTATION_REMOVED, this);
        ArgoEventPump.fireEvent(evt);
        evt = new ArgoNotationEvent(ArgoEventTypes.NOTATION_PROVIDER_ADDED,
                this);
        ArgoEventPump.fireEvent(evt);
        evt = new ArgoNotationEvent(ArgoEventTypes.NOTATION_PROVIDER_REMOVED,
                this);
        ArgoEventPump.fireEvent(evt);

        // fire all of the profile events
        evt = new ArgoProfileEvent(ArgoEventTypes.PROFILE_ADDED, this);
        ArgoEventPump.fireEvent(evt);
        evt = new ArgoProfileEvent(ArgoEventTypes.PROFILE_REMOVED, this);
        ArgoEventPump.fireEvent(evt);

        // verify all events were fired correctly
        assertTrue("ArgoEventPump did not fire all " 
                + "ArgoStatusEvents",
                statusTracker.allEventsFired());

        assertTrue("ArgoEventPump incorrectly fired " 
                + "ArgoStatusEvents",
                statusTracker.allEventsFiredCorrectly());

        assertTrue("ArgoEventPump did not fire all "
                + "ArgoDiagramAppearanceEvents", 
                diagramTracker.allEventsFired());

        assertTrue("ArgoEventPump incorrectly fired "
                + "ArgoDiagramAppearanceEvents", 
                diagramTracker.allEventsFiredCorrectly());

        assertTrue("ArgoEventPump did not fire all " 
                + "ArgoGeneratorEvents",
                generatorTracker.allEventsFired());

        assertTrue("ArgoEventPump incorrectly fired " 
                + "ArgoGeneratorEvents",
                generatorTracker.allEventsFiredCorrectly());

        assertTrue("ArgoEventPump did not fire all " 
                + "ArgoHelpEvents",
                helpTracker.allEventsFired());

        assertTrue("ArgoEventPump incorrectly fired " 
                + "ArgoHelpEvents",
                helpTracker.allEventsFiredCorrectly());

        assertTrue("ArgoEventPump did not fire all " 
                + "ArgoNotationEvents",
                notationTracker.allEventsFired());

        assertTrue("ArgoEventPump incorrectly fired " 
                + "ArgoNotationEvents",
                notationTracker.allEventsFiredCorrectly());

        assertTrue("ArgoEventPump did not fire all " 
                + "ArgoProfileEvents",
                profileTracker.allEventsFired());

        assertTrue("ArgoEventPump incorrectly fired " 
                + "ArgoProfileEvents",
                profileTracker.allEventsFiredCorrectly());
    }

    /**
     * Used to track the status of events that have fired.
     */
    public abstract class EventTracker {
        private int numEvents = 0;

        private int totalEventsFired = 0;

        private int eventStatus = 0;

        /**
         * Initialize a new EventTest
         * 
         * @param numberOfEvents the number of events to be represented
         */
        public EventTracker(int numberOfEvents) {
            numEvents = numberOfEvents;
        }

        /**
         * Reset the event status.
         */
        public void resetEventStatus() {
            eventStatus = 0;
        }

        /**
         * Update the status of the event of an event as fired.
         * 
         * @param event the an id that represents the event
         */
        protected void setEvent(int event) {
            // keep track of total events fired
            totalEventsFired++;

            // keep track of which events fired
            eventStatus |= (0x01 << event);
        }

        /**
         * Gets the total number of events that have been fired
         * 
         * @return returns true if all events have been fired
         */
        public int getTotalNumberOfEventsFired() {
            return totalEventsFired;
        }

        /**
         * Gets the individual number of events that have been fired
         * 
         * @return returns true if all events have been fired
         */
        public int getNumberOfEventsFired() {
            int eventsFired = 0;

            // counts each bit that has been fired
            for (int events = eventStatus; events > 0; ++eventsFired)
                events &= events - 1;

            return eventsFired;
        }

        /**
         * Determines if all events have been fired
         * 
         * @return returns true if all events have been fired
         */
        public boolean allEventsFired() {
            return (getNumberOfEventsFired() == numEvents);
        }

        /**
         * Determines if the events were called the right number of times
         * 
         * @return returns true if the total number of events fired is the same
         *         as all event types
         */
        public boolean allEventsFiredCorrectly() {
            return (getTotalNumberOfEventsFired() == numEvents);
        }
    }

    /**
     * Represents a ArgoDiagramAppearanceEventListener and tracks the firing of
     * its events
     */
    public class TArgoDiagramAppearanceEventListener extends EventTracker
            implements ArgoDiagramAppearanceEventListener {
        /**
         * Initialize the listener
         */
        public TArgoDiagramAppearanceEventListener() {
            super(1);
        }

        /**
         * Invoked when any aspect of the notation has been changed.
         * 
         * @param e <code>ArgoNotationEvent</code> describing the change.
         */
        public void diagramFontChanged(ArgoDiagramAppearanceEvent e) {
            setEvent(0);
        }
    }

    /**
     * Represents a ArgoGeneratorEventListener and tracks the firing of its
     * events.
     */
    public class TArgoGeneratorEventListener extends EventTracker implements
            ArgoGeneratorEventListener {
        /**
         * Initialize the listener
         */
        public TArgoGeneratorEventListener() {
            super(3);
        }

        /**
         * Invoked when any aspect of the generator has been changed.
         * 
         * @param e <code>ArgoGeneratorEvent</code> describing the change.
         */
        public void generatorChanged(ArgoGeneratorEvent e) {
            setEvent(0);
        }

        /**
         * Invoked when a CodeGenerator has been added.
         * 
         * @param e <code>ArgoGeneratorEvent</code> describing the added
         *                notation.
         */
        public void generatorAdded(ArgoGeneratorEvent e) {
            setEvent(1);
        }

        /**
         * Invoked when a CodeGenerator has been removed.
         * 
         * @param e <code>ArgoGeneratorEvent</code> describing the removed
         *                notation.
         */
        public void generatorRemoved(ArgoGeneratorEvent e) {
            setEvent(2);
        }
    }

    /**
     * Represents a ArgoDiagramAppearanceEventListener and tracks the firing of
     * its events
     */
    public class TArgoHelpEventListener extends EventTracker implements
            ArgoHelpEventListener {
        /**
         * Initialize the listener
         */
        public TArgoHelpEventListener() {
            super(2);
        }

        /**
         * Invoked when there is a new help text to be shown, that should
         * replace any previous one.
         * 
         * @param e <code>ArgoHelpEvent</code> describing the changed text
         */
        public void helpChanged(ArgoHelpEvent e) {
            setEvent(0);
        }

        /**
         * Invoked when a previously shown help text has to be removed.
         * 
         * @param e <code>ArgoHelpEvent</code> describing the removed event
         */
        public void helpRemoved(ArgoHelpEvent e) {
            setEvent(1);
        }
    }

    /**
     * Represents a ArgoNotationEventListener and tracks the firing of its
     * events
     */
    public class TArgoNotationEventListener extends EventTracker implements
            ArgoNotationEventListener {
        /**
         * Initialize the listener
         */
        public TArgoNotationEventListener() {
            super(5);
        }

        /**
         * Invoked when any aspect of the notation has been changed.
         * 
         * @param e <code>ArgoNotationEvent</code> describing the change.
         */
        public void notationChanged(ArgoNotationEvent e) {
            setEvent(0);
        }

        /**
         * Invoked when a notation has been added.
         * 
         * @param e <code>ArgoNotationEvent</code> describing the added
         *                notation.
         */
        public void notationAdded(ArgoNotationEvent e) {
            setEvent(1);
        }

        /**
         * Invoked when a notation has been removed.
         * 
         * @param e <code>ArgoNotationEvent</code> describing the removed
         *                notation.
         */
        public void notationRemoved(ArgoNotationEvent e) {
            setEvent(2);
        }

        /**
         * Invoked when a notation provider has been added.
         * 
         * @param e <code>ArgoNotationEvent</code> describing the added
         *                notation provider.
         */
        public void notationProviderAdded(ArgoNotationEvent e) {
            setEvent(3);
        }

        /**
         * Invoked when a notation provider has been removed.
         * 
         * @param e <code>ArgoNotationEvent</code> describing the removed
         *                notation provider.
         */
        public void notationProviderRemoved(ArgoNotationEvent e) {
            setEvent(4);
        }
    }

    /**
     * Represents a ArgoProfileEventListener and tracks the firing of its events
     */
    public class TArgoProfileEventListener extends EventTracker implements
            ArgoProfileEventListener {
        /**
         * Initialize the listener
         */
        public TArgoProfileEventListener() {
            super(2);
        }

        /**
         * Invoked when a profile has been added.
         * 
         * @param e <code>ArgoProfileEvent</code> describing the added
         *                notation.
         */
        public void profileAdded(ArgoProfileEvent e) {
            setEvent(0);
        }

        /**
         * Invoked when a profile has been removed.
         * 
         * @param e <code>ArgoProfileEvent</code> describing the removed
         *                notation.
         */
        public void profileRemoved(ArgoProfileEvent e) {
            setEvent(1);
        }
    }

    /**
     * Tracker for ArgoStatusEventListener.
     */
    public class TArgoStatusEventListener extends EventTracker implements
            ArgoStatusEventListener {
        /**
         * Initialize the listener
         */
        public TArgoStatusEventListener() {
            super(5);
        }

        /**
         * Invoked when there is a new status text to be shown, that should
         * replace any previous one.
         * 
         * @param e <code>ArgoStatusEvent</code> describing the changed text
         */
        public void statusText(ArgoStatusEvent e) {
            setEvent(0);
        }

        /**
         * Invoked when a previously shown status text has to be removed.
         * 
         * @param e <code>ArgoStatusEvent</code> describing the removed event
         */
        public void statusCleared(ArgoStatusEvent e) {
            setEvent(1);
        }

        /**
         * A project has been saved.
         * 
         * @param e <code>ArgoStatusEvent</code> with the name of the project
         *                that was saved.
         */
        public void projectSaved(ArgoStatusEvent e) {
            setEvent(2);
        }

        /**
         * A project has been loaded.
         * 
         * @param e <code>ArgoStatusEvent</code> with the name of the project
         *                that was loaded.
         */
        public void projectLoaded(ArgoStatusEvent e) {
            setEvent(3);
        }

        /**
         * A project has been modified.
         * 
         * @param e <code>ArgoStatusEvent</code> with the name of the project
         *                that was modified (ignored for current ArgoUML
         *                implementation where there is only a single project
         *                open at a time).
         */
        public void projectModified(ArgoStatusEvent e) {
            setEvent(4);
        }
    }
}
