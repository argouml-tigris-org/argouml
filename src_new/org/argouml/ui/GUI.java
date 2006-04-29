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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        addSettingsTab(new SettingsTabPreferences());
        addSettingsTab(new SettingsTabEnvironment());
        addSettingsTab(new SettingsTabUser());
        addSettingsTab(new SettingsTabAppearance());
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
    private List settingsTabs = new ArrayList();

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
     * Register a file in the menubar.
     *
     * @param file The File.
     * @throws IOException if we cannot get the filename.
     */
    public void addFileSaved(File file) throws IOException {
        ProjectBrowser.getInstance().addFileSaved(file);
    }
}
