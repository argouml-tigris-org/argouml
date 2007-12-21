// $Id: SubsystemUtility.java 13902 2007-12-11 07:53:28Z tfmorris $
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.application;

import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.application.api.InitSubsystem;
import org.argouml.ui.DetailsPane;
import org.argouml.ui.GUI;
import org.argouml.ui.ProjectBrowser;

/**
 * Utility class for subsystem management.
 *
 * @author Michiel
 */
public class SubsystemUtility {

    /**
     * The use of this method in the top-level package 
     * prevents that the subsystem would depend on the GUI.
     * 
     * @param subsystem the subsystem to be initialised
     */
    static void initSubsystem(InitSubsystem subsystem) {
        subsystem.init();
        for (GUISettingsTabInterface tab : subsystem.getSettingsTabs()) {
            GUI.getInstance().addSettingsTab(tab);
        }
        for (GUISettingsTabInterface tab : subsystem.getProjectSettingsTabs()) {
            GUI.getInstance().addProjectSettingsTab(tab);
        }
        for (AbstractArgoJPanel tab : subsystem.getDetailsTabs()) {
            ((DetailsPane) ProjectBrowser.getInstance().getDetailsPane())
                .addTab(tab, true);
        }
    }

}
