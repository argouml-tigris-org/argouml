// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.argouml.application.api.GUISettingsTabInterface;

/**
 * This is the "main" class for the GUI subsystem.<p>
 *
 * Users of the GUI subsystem, i.e. components that are to place themselves
 * in any of the GUI components, register themselves in this class.<p>
 *
 * TODO: Add the rest of the registers to this.
 *
 * @author Linus Tolke
 * @since 0.21.3
 */
public final class GUI {

    /**
     * Constructor.
     */
    private GUI() {
        // Add GUI-internal stuff.
        // GUI-internal stuff is panes, tabs, menu items that are
        // part of the GUI subsystem i.e. a class in the
        // org.argouml.ui-package.
        // Things that are not part of the GUI, like everything that
        // has any knowledge about UML, Diagrams, Code Generation, 
        // Reverse Engineering, creates and registers itself
        // when that subsystem or module is loaded.
        addSettingsTab(new SettingsTabPreferences());
        addSettingsTab(new SettingsTabEnvironment());
        addSettingsTab(new SettingsTabUser());
        addSettingsTab(new SettingsTabAppearance());

        addProjectSettingsTab(new ProjectSettingsTabProperties());
    }

    /**
     * The instance.
     */
    private static GUI instance = new GUI();

    /**
     * @return the instance.
     */
    public static GUI getInstance() {
        return instance;
    }

    /**
     * A List of {@link GUISettingsTabInterface}.
     */
    private List<GUISettingsTabInterface> settingsTabs = 
        new ArrayList<GUISettingsTabInterface>();

    /**
     * Register a new SettingsTab.
     *
     * @param panel The GUISettingsTabInterface to add.
     */
    public void addSettingsTab(GUISettingsTabInterface panel) {
        settingsTabs.add(panel);
    }

    /**
     * Get the components for the settings tab.
     *
     * @return A List of {@link GUISettingsTabInterface}.
     */
    public List getSettingsTabs() {
        return Collections.unmodifiableList(settingsTabs);
    }

    /**
     * A List of {@link GUISettingsTabInterface}.
     */
    private List<GUISettingsTabInterface> projectSettingsTabs = 
        new ArrayList<GUISettingsTabInterface>();

    /**
     * Register a new ProjectSettingsTab.
     *
     * @param panel The GUISettingsTabInterface to add.
     */
    public void addProjectSettingsTab(GUISettingsTabInterface panel) {
        projectSettingsTabs.add(panel);
    }

    /**
     * Get the components for the project settings tab.
     *
     * @return A List of {@link GUISettingsTabInterface}.
     */
    public List getProjectSettingsTabs() {
        return Collections.unmodifiableList(projectSettingsTabs);
    }
}
