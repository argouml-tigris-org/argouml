// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.argouml.application.api.Argo;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;

/**
 * Settings tab panel for handling ArgoUML application related settings.
 *
 * @author Thierry Lach
 * @since  0.9.4
 */
class SettingsTabPreferences extends JPanel
    implements GUISettingsTabInterface {

    private JCheckBox chkSplash;
    private JCheckBox chkPreload;
    private JCheckBox chkReloadRecent;
    private JCheckBox chkStripDiagrams;

    /**
     * The constructor.
     *
     */
    SettingsTabPreferences() {
        setLayout(new BorderLayout());
	JPanel top = new JPanel();
    	top.setLayout(new GridBagLayout());

	GridBagConstraints checkConstraints = new GridBagConstraints();
	checkConstraints.anchor = GridBagConstraints.LINE_START;
	checkConstraints.gridy = 0;
	checkConstraints.gridx = 0;
	checkConstraints.gridwidth = 1;
	checkConstraints.gridheight = 1;
	checkConstraints.insets = new Insets(4, 10, 0, 10);

	checkConstraints.gridy = 2;
	JCheckBox j = new JCheckBox(Translator.localize("label.splash"));
        chkSplash = j;
	top.add(chkSplash, checkConstraints);

	checkConstraints.gridy++;
        JCheckBox j1 = new JCheckBox(Translator.localize("label.preload"));
        chkPreload = j1;
 	top.add(chkPreload, checkConstraints);

	checkConstraints.gridy++;
        JCheckBox j2 =
            new JCheckBox(Translator.localize("label.reload-recent"));
        chkReloadRecent = j2;
 	top.add(chkReloadRecent, checkConstraints);

        checkConstraints.gridy++;
        JCheckBox j3 =
            new JCheckBox(Translator.localize("label.strip-diagrams"));
        chkStripDiagrams = j3;
        top.add(chkStripDiagrams, checkConstraints);

        // TODO: Profile field is currently read-only, need a selector
        checkConstraints.gridy++;
        top.add(new JLabel(Translator.localize("label.default-profile")),
                checkConstraints);
        checkConstraints.fill = GridBagConstraints.HORIZONTAL;

	add(top, BorderLayout.NORTH);
    }

    /*
     * @see GUISettingsTabInterface#handleSettingsTabRefresh()
     */
    public void handleSettingsTabRefresh() {
        chkSplash.setSelected(Configuration.getBoolean(Argo.KEY_SPLASH, true));
        chkPreload.setSelected(Configuration.getBoolean(Argo.KEY_PRELOAD,
                true));
        chkReloadRecent.setSelected(
		Configuration.getBoolean(Argo.KEY_RELOAD_RECENT_PROJECT,
					 false));
        chkStripDiagrams.setSelected(
                Configuration.getBoolean(Argo.KEY_XMI_STRIP_DIAGRAMS,
                                         false));
    }

    /*
     * @see GUISettingsTabInterface#handleSettingsTabSave()
     */
    public void handleSettingsTabSave() {
        Configuration.setBoolean(Argo.KEY_SPLASH, chkSplash.isSelected());
        Configuration.setBoolean(Argo.KEY_PRELOAD, chkPreload.isSelected());
        Configuration.setBoolean(Argo.KEY_RELOAD_RECENT_PROJECT,
				 chkReloadRecent.isSelected());
        Configuration.setBoolean(Argo.KEY_XMI_STRIP_DIAGRAMS,
                 chkStripDiagrams.isSelected());
    }

    /*
     * @see GUISettingsTabInterface#handleSettingsTabCancel()
     */
    public void handleSettingsTabCancel() {
        handleSettingsTabRefresh();
    }

    /*
     * @see org.argouml.ui.GUISettingsTabInterface#handleResetToDefault()
     */
    public void handleResetToDefault() {
        // Do nothing - these buttons are not shown.
    }

    /*
     * @see GUISettingsTabInterface#getTabPanel()
     */
    public JPanel getTabPanel() { return this; }

    /*
     * @see GUISettingsTabInterface#getTabKey()
     */
    public String getTabKey() { return "tab.preferences"; }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -340220974967836979L;
}

