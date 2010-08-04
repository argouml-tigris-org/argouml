/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2006-2009 The Regents of the University of California. All
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
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.argouml.application.events.ArgoDiagramAppearanceEvent;
import org.argouml.application.events.ArgoDiagramAppearanceEventListener;
import org.argouml.application.events.ArgoEvent;
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
import org.argouml.uml.diagram.DiagramAppearance;
import org.argouml.uml.diagram.DiagramSettings;

/**
 * Tests for the ProjectSettings.
 *
 * @author michiel
 */
public class TestProjectSettings extends TestCase {

    private ArgoEvent rxdEvent;

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
                DiagramAppearance.KEY_DEFAULT_SHADOW_WIDTH, 2);
        Project p1 = ProjectManager.getManager().makeEmptyProject();
        assertTrue("Default Setting is not copied",
                p1.getProjectSettings().getDefaultShadowWidthValue() == 2);
        Configuration.setInteger(
                DiagramAppearance.KEY_DEFAULT_SHADOW_WIDTH, 3);
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
                        DiagramAppearance.KEY_DEFAULT_SHADOW_WIDTH) == 3);
    }

    /**
     * Test the use of Guillemots.
     */
    public void testGuillemots() {
        Configuration.setBoolean(Notation.KEY_USE_GUILLEMOTS, false);
        Project p = ProjectManager.getManager().makeEmptyProject();
        assertTrue("Guillemots not correct",
                !p.getProjectSettings().getUseGuillemotsValue());

        p.getProjectSettings().setUseGuillemots(true);
        assertTrue("Guillemots not correct",
                p.getProjectSettings().getUseGuillemotsValue());

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
        final Project p = ProjectManager.getManager().makeEmptyProject();

        ArgoEventPump.addListener(new EventCatcher());

        rxdEvent = null;
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    p.getProjectSettings().setShowVisibility(
                            Boolean.toString(true));
                }
            });
            assertTrue("Got no notation event", rxdEvent != null);

            rxdEvent = null;
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    p.getProjectSettings().setShowVisibility(
                            Boolean.toString(false));
                }
            });
            assertTrue("Got no notation event", rxdEvent != null);

            PropertyChangeEvent pce = (PropertyChangeEvent) rxdEvent
                    .getSource();
            assertTrue("Wrong event name",
                    pce.getPropertyName().equals(
                            Notation.KEY_SHOW_VISIBILITY.getKey()));
            assertTrue("Wrong old event value", Boolean.valueOf(
                    (String) pce.getOldValue()).booleanValue());
            assertTrue("Wrong new event value", !Boolean.valueOf(
                    (String) pce.getNewValue()).booleanValue());
        } catch (InterruptedException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        }
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
        Configuration.setInteger(DiagramAppearance.KEY_DEFAULT_SHADOW_WIDTH, 4);
        Configuration.setString(Notation.KEY_DEFAULT_NOTATION, "UML 1.4");

        final Project p = ProjectManager.getManager().makeEmptyProject();

        ArgoEventPump.addListener(new EventCatcher());

        rxdEvent = null; 
        try {

            // Because the notation events get dispatched on the Swing
            // event thread, we need to use invokeAndWait to make sure that we
            // don't return until they've been delivered
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    p.getProjectSettings().setShowInitialValue(true);
                }
            });
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
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    p.getProjectSettings().setShowProperties(true);
                }
            });
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
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    p.getProjectSettings().setShowTypes(true);
                }
            });
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
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    p.getProjectSettings().setShowStereotypes(true);
                }
            });
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
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    ProjectSettings ps = p.getProjectSettings();
                    DiagramSettings ds = ps.getDefaultDiagramSettings();
                    ds.setDefaultShadowWidth(2);
                    ds.notifyOfChangedSettings();
                }
            });
            assertTrue("Got no diagram settings event", rxdEvent != null);
            /* We no longer send individual events,
             *  so next code is obsolete: */
//            pce = (PropertyChangeEvent) rxdEvent.getSource();
//            assertTrue("Wrong event name",
//                    pce.getPropertyName().equals(
//                            DiagramAppearance.KEY_DEFAULT_SHADOW_WIDTH.getKey()));
//            String value = (String) pce.getOldValue();
//            int i = Integer.parseInt(value);
//            assertTrue("Wrong old event value", i == 4);
//            assertTrue("Wrong new event value",
//                    ((String) pce.getNewValue()).equals("2"));

            rxdEvent = null;
            /* We initialised Java Notation, so let's activate it: */
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    assertTrue(p.getProjectSettings().setNotationLanguage(
                            "Java"));
                }
            });
            assertTrue("Got no notation event", rxdEvent != null);
            pce = (PropertyChangeEvent) rxdEvent.getSource();
            assertTrue("Wrong event name",
                    pce.getPropertyName().equals(
                            Notation.KEY_DEFAULT_NOTATION.getKey()));
            String value = (String) pce.getOldValue();
            assertTrue("Wrong old event value", "UML 1.4".equals(value));
            value = (String) pce.getNewValue();
            assertTrue("Wrong new event value", "Java".equals(value));
        } catch (InterruptedException e) {
            fail();
        } catch (InvocationTargetException e) {
            fail();
        }    
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
        ProjectManager.getManager().makeEmptyProject();
    }

    /**
     * Catcher of Events.
     *
     * @author michiel
     */
    protected class EventCatcher implements ArgoNotationEventListener,
    ArgoDiagramAppearanceEventListener {
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

        public void diagramFontChanged(ArgoDiagramAppearanceEvent e) {
            rxdEvent = e;
        }
    }
}
