// $Id$
// Copyright (c) 2006-2008 The Regents of the University of California. All
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

package org.argouml.kernel;

import java.beans.PropertyChangeEvent;

import junit.framework.TestCase;

import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;
import org.argouml.configuration.Configuration;
import org.argouml.model.InitializeModel;
import org.argouml.notation.InitNotation;
import org.argouml.notation.Notation;
import org.argouml.notation.providers.java.InitNotationJava;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.profile.init.InitProfileSubsystem;

/**
 * Tests for the ProjectSettings.
 *
 * @author michiel
 */
public class TestProjectSettings extends TestCase {

    private ArgoNotationEvent rxdEvent;

    /**
     * Constructor.
     *
     * @param arg0 is the name of the test case.
     */
    public TestProjectSettings(String arg0) {
        super(arg0);
    }

    /**
     * Test if the settings are copied correctly
     * from the default into the project. <p>
     *
     * For this test, only the shadow-width is used.
     */
    public void testProjectSettingsCreation() {
        Configuration.setInteger(
                Notation.KEY_DEFAULT_SHADOW_WIDTH, 2);
        Project p1 = ProjectManager.getManager().makeEmptyProject();
        assertTrue("Default Setting is not copied",
                p1.getProjectSettings().getDefaultShadowWidthValue() == 2);
        Configuration.setInteger(
                Notation.KEY_DEFAULT_SHADOW_WIDTH, 3);
        assertTrue("Project Setting is altered",
                p1.getProjectSettings().getDefaultShadowWidth().equals("2"));
        ProjectManager.getManager().removeProject(p1);

        /* In the next line, replacing makeEmptyProject
         * by getCurrentProject fails the test,
         * except when run in Eclipse.
         * MVW: I have no idea why.*/
        Project p2 = ProjectManager.getManager().makeEmptyProject();
        assertTrue("New project does not get Default Setting",
                p2.getProjectSettings().getDefaultShadowWidthValue() == 3);
        p2.getProjectSettings().setDefaultShadowWidth(4);
        assertTrue("Default is altered by project-setting",
                Configuration.getInteger(
                        Notation.KEY_DEFAULT_SHADOW_WIDTH) == 3);
    }

    /**
     * Test the use of Guillemots.
     */
    public void testGuillemots() {
        Configuration.setBoolean(Notation.KEY_USE_GUILLEMOTS, false);
        Project p = ProjectManager.getManager().makeEmptyProject();
        assertTrue("Guillemots not correct",
                !p.getProjectSettings().getUseGuillemotsValue());
        assertTrue("Guillemots string not set correctly",
                "<<".equals(p.getProjectSettings().getLeftGuillemot()));
        assertTrue("Guillemots string not set correctly",
                ">>".equals(p.getProjectSettings().getRightGuillemot()));

        p.getProjectSettings().setUseGuillemots(true);
        assertTrue("Guillemots not correct",
                p.getProjectSettings().getUseGuillemotsValue());
        assertTrue("Guillemots string not set correctly",
                "\u00ab".equals(p.getProjectSettings().getLeftGuillemot()));
        assertTrue("Guillemots string not set correctly",
                "\u00bb".equals(p.getProjectSettings().getRightGuillemot()));

        p.getProjectSettings().setUseGuillemots(Boolean.toString(false));
        assertTrue("Guillemots not correct",
                !p.getProjectSettings().getUseGuillemotsValue());
    }

    /**
     * Test the project setting for showing Association names.
     */
    public void testAssociationNames() {
        Configuration.setBoolean(Notation.KEY_SHOW_ASSOCIATION_NAMES, false);
        Project p = ProjectManager.getManager().makeEmptyProject();

        assertTrue("Association names not correct",
                !p.getProjectSettings().getShowAssociationNamesValue());
        String showem = p.getProjectSettings().getShowAssociationNames();
        assertTrue("Association names string not set correctly",
                !Boolean.valueOf(showem).booleanValue());

        p.getProjectSettings().setShowAssociationNames(true);
        assertTrue("Association names not correct",
                p.getProjectSettings().getShowAssociationNamesValue());
        showem = p.getProjectSettings().getShowAssociationNames();
        assertTrue("Association names string not set correctly",
                Boolean.valueOf(showem).booleanValue());

        p.getProjectSettings().setShowAssociationNames(Boolean.toString(false));
        assertTrue("Association names not correct",
                !p.getProjectSettings().getShowAssociationNamesValue());
    }

    /**
     * Test the project setting for showing Visibility.
     */
    public void testVisibility() {
        Configuration.setBoolean(Notation.KEY_SHOW_VISIBILITY, false);
        Project p = ProjectManager.getManager().makeEmptyProject();

        assertTrue("Visibility not correct",
                !p.getProjectSettings().getShowVisibilityValue());
        String showem = p.getProjectSettings().getShowVisibility();
        assertTrue("Visibility string not set correctly",
                !Boolean.valueOf(showem).booleanValue());

        p.getProjectSettings().setShowVisibility(true);
        assertTrue("Visibility not correct",
                p.getProjectSettings().getShowVisibilityValue());
        showem = p.getProjectSettings().getShowVisibility();
        assertTrue("Visibility string not set correctly",
                Boolean.valueOf(showem).booleanValue());

        p.getProjectSettings().setShowVisibility(Boolean.toString(false));
        assertTrue("Visibility not correct",
                !p.getProjectSettings().getShowVisibilityValue());
    }

    /**
     * Test the project setting for showing Multiplicity.
     */
    public void testMultiplicity() {
        Configuration.setBoolean(Notation.KEY_SHOW_MULTIPLICITY, false);
        Project p = ProjectManager.getManager().makeEmptyProject();

        assertTrue("Multiplicity not correct",
                !p.getProjectSettings().getShowMultiplicityValue());
        String showem = p.getProjectSettings().getShowMultiplicity();
        assertTrue("Multiplicity string not set correctly",
                !Boolean.valueOf(showem).booleanValue());

        p.getProjectSettings().setShowMultiplicity(true);
        assertTrue("Multiplicity not correct",
                p.getProjectSettings().getShowMultiplicityValue());
        showem = p.getProjectSettings().getShowMultiplicity();
        assertTrue("Multiplicity string not set correctly",
                Boolean.valueOf(showem).booleanValue());

        p.getProjectSettings().setShowMultiplicity(Boolean.toString(false));
        assertTrue("Multiplicity not correct",
                !p.getProjectSettings().getShowMultiplicityValue());
    }

    /**
     * Test the project setting for showing Visibility.
     */
    public void testVisibilityEvents() {
        Configuration.setBoolean(Notation.KEY_SHOW_VISIBILITY, false);
        Project p = ProjectManager.getManager().makeEmptyProject();

        ArgoEventPump.addListener(new EventCatcher());

        rxdEvent = null;
        p.getProjectSettings().setShowVisibility(Boolean.toString(true));
        /* This assumes events are dispatched on the same thread. */
        assertTrue("Got no notation event", rxdEvent != null);

        rxdEvent = null;
        p.getProjectSettings().setShowVisibility(Boolean.toString(false));
        /* This assumes events are dispatched on the same thread. */
        assertTrue("Got no notation event", rxdEvent != null);

        PropertyChangeEvent pce = (PropertyChangeEvent) rxdEvent.getSource();
        assertTrue("Wrong event name",
                pce.getPropertyName().equals(
                        Notation.KEY_SHOW_VISIBILITY.getKey()));
        assertTrue("Wrong old event value", Boolean.valueOf(
                (String) pce.getOldValue()).booleanValue());
        assertTrue("Wrong new event value", !Boolean.valueOf(
                (String) pce.getNewValue()).booleanValue());
    }

    /**
     * Test the events generated by project settings changes.
     */
    public void testMoreEvents() {
        PropertyChangeEvent pce;

        Configuration.setBoolean(Notation.KEY_SHOW_INITIAL_VALUE, false);
        Configuration.setBoolean(Notation.KEY_SHOW_PROPERTIES, false);
        Configuration.setBoolean(Notation.KEY_SHOW_TYPES, false);
        Configuration.setBoolean(Notation.KEY_SHOW_STEREOTYPES, false);
        Configuration.setInteger(Notation.KEY_DEFAULT_SHADOW_WIDTH, 4);
        Configuration.setString(Notation.KEY_DEFAULT_NOTATION, "UML 1.4");

        Project p = ProjectManager.getManager().makeEmptyProject();
        ArgoEventPump.addListener(new EventCatcher());

        rxdEvent = null;
        p.getProjectSettings().setShowInitialValue(true);
        /* This assumes events are dispatched on the same thread. */
        assertTrue("Got no notation event", rxdEvent != null);
        pce = (PropertyChangeEvent) rxdEvent.getSource();
        assertTrue("Wrong event name",
                pce.getPropertyName().equals(
                        Notation.KEY_SHOW_INITIAL_VALUE.getKey()));
        assertTrue("Wrong old event value", !Boolean.valueOf(
                (String) pce.getOldValue()).booleanValue());
        assertTrue("Wrong new event value", Boolean.valueOf(
                (String) pce.getNewValue()).booleanValue());

        rxdEvent = null;
        p.getProjectSettings().setShowProperties(true);
        /* This assumes events are dispatched on the same thread. */
        assertTrue("Got no notation event", rxdEvent != null);
        pce = (PropertyChangeEvent) rxdEvent.getSource();
        assertTrue("Wrong event name",
                pce.getPropertyName().equals(
                        Notation.KEY_SHOW_PROPERTIES.getKey()));
        assertTrue("Wrong old event value", !Boolean.valueOf(
                (String) pce.getOldValue()).booleanValue());
        assertTrue("Wrong new event value", Boolean.valueOf(
                (String) pce.getNewValue()).booleanValue());

        rxdEvent = null;
        p.getProjectSettings().setShowTypes(true);
        /* This assumes events are dispatched on the same thread. */
        assertTrue("Got no notation event", rxdEvent != null);
        pce = (PropertyChangeEvent) rxdEvent.getSource();
        assertTrue("Wrong event name",
                pce.getPropertyName().equals(
                        Notation.KEY_SHOW_TYPES.getKey()));
        assertTrue("Wrong old event value", !Boolean.valueOf(
                (String) pce.getOldValue()).booleanValue());
        assertTrue("Wrong new event value", Boolean.valueOf(
                (String) pce.getNewValue()).booleanValue());

        rxdEvent = null;
        p.getProjectSettings().setShowStereotypes(true);
        /* This assumes events are dispatched on the same thread. */
        assertTrue("Got no notation event", rxdEvent != null);
        pce = (PropertyChangeEvent) rxdEvent.getSource();
        assertTrue("Wrong event name",
                pce.getPropertyName().equals(
                        Notation.KEY_SHOW_STEREOTYPES.getKey()));
        assertTrue("Wrong old event value", !Boolean.valueOf(
                (String) pce.getOldValue()).booleanValue());
        assertTrue("Wrong new event value", Boolean.valueOf(
                (String) pce.getNewValue()).booleanValue());

        rxdEvent = null;
        p.getProjectSettings().setDefaultShadowWidth(2);
        /* This assumes events are dispatched on the same thread. */
        assertTrue("Got no notation event", rxdEvent != null);
        pce = (PropertyChangeEvent) rxdEvent.getSource();
        assertTrue("Wrong event name",
                pce.getPropertyName().equals(
                        Notation.KEY_DEFAULT_SHADOW_WIDTH.getKey()));
        String value = (String) pce.getOldValue();
        int i = Integer.parseInt(value);
        assertTrue("Wrong old event value", i == 4);
        assertTrue("Wrong new event value",
                ((String) pce.getNewValue()).equals("2"));

        rxdEvent = null;
        /* We initialised Java Notation, so let's activate it: */
        assertTrue(p.getProjectSettings().setNotationLanguage("Java"));
        /* This assumes events are dispatched on the same thread. */
        assertTrue("Got no notation event", rxdEvent != null);
        pce = (PropertyChangeEvent) rxdEvent.getSource();
        assertTrue("Wrong event name",
                pce.getPropertyName().equals(
                        Notation.KEY_DEFAULT_NOTATION.getKey()));
        value = (String) pce.getOldValue();
        assertTrue("Wrong old event value", "UML 1.4".equals(value));
        value = (String) pce.getNewValue();
        assertTrue("Wrong new event value", "Java".equals(value));
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        (new InitProfileSubsystem()).init();
        (new InitNotation()).init();
        (new InitNotationUml()).init();
        (new InitNotationJava()).init();
        /* Needed for initialisations: */
        ProjectManager.getManager().getCurrentProject();
    }

    /**
     * Catcher of Events.
     *
     * @author michiel
     */
    protected class EventCatcher implements ArgoNotationEventListener {
        public void notationChanged(ArgoNotationEvent e) {
            rxdEvent = e;
        }

        public void notationAdded(ArgoNotationEvent e) {
            rxdEvent = e;
        }

        public void notationRemoved(ArgoNotationEvent e) {
            rxdEvent = e;
        }

        public void notationProviderAdded(ArgoNotationEvent e) {
            rxdEvent = e;
        }

        public void notationProviderRemoved(ArgoNotationEvent e) {
            rxdEvent = e;
        }
    }
}
