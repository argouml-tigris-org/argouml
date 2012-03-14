/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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
import javax.swing.JPanel;

import org.argouml.application.api.Argo;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.persistence.PersistenceManager;

/**
 * Settings tab panel for handling ArgoUML application related settings.
 *
 * @author Thierry Lach
 * @since  0.9.4
 */
class SettingsTabPreferences extends JPanel
    implements GUISettingsTabInterface {

    private JPanel topPanel;
    private JCheckBox chkSplash;
    private JCheckBox chkReloadRecent;
    private JCheckBox chkStripDiagrams;
    private JCheckBox chkUseSafeSaves;
    
    /**
     * The constructor.
     *
     */
    SettingsTabPreferences() {
    }

    private void buildPanel() {
        setLayout(new BorderLayout());
	topPanel = new JPanel();
    	topPanel.setLayout(new GridBagLayout());

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
	topPanel.add(chkSplash, checkConstraints);

	checkConstraints.gridy++;
        JCheckBox j2 =
            new JCheckBox(Translator.localize("label.reload-recent"));
        chkReloadRecent = j2;
 	topPanel.add(chkReloadRecent, checkConstraints);

        checkConstraints.gridy++;
        JCheckBox j3 =
            new JCheckBox(Translator.localize("label.strip-diagrams"));
        chkStripDiagrams = j3;
        topPanel.add(chkStripDiagrams, checkConstraints);

        checkConstraints.gridy++;
        JCheckBox j4 =
            new JCheckBox(Translator.localize("label.use-safe-saves"));
        chkUseSafeSaves = j4;
        topPanel.add(chkUseSafeSaves, checkConstraints);
        
        checkConstraints.fill = GridBagConstraints.HORIZONTAL;

	add(topPanel, BorderLayout.NORTH);
    }

    /*
     * @see GUISettingsTabInterface#handleSettingsTabRefresh()
     */
    public void handleSettingsTabRefresh() {
        chkSplash.setSelected(Configuration.getBoolean(Argo.KEY_SPLASH, true));
        chkReloadRecent.setSelected(
		Configuration.getBoolean(Argo.KEY_RELOAD_RECENT_PROJECT,
					 false));
        chkStripDiagrams.setSelected(
                Configuration.getBoolean(Argo.KEY_XMI_STRIP_DIAGRAMS,
                                         false));
        chkUseSafeSaves.setSelected(
            Configuration.getBoolean(PersistenceManager.USE_SAFE_SAVES,
                true)); 
    }

    /*
     * @see GUISettingsTabInterface#handleSettingsTabSave()
     */
    public void handleSettingsTabSave() {
        Configuration.setBoolean(Argo.KEY_SPLASH, chkSplash.isSelected());
        Configuration.setBoolean(Argo.KEY_RELOAD_RECENT_PROJECT,
				 chkReloadRecent.isSelected());
        Configuration.setBoolean(Argo.KEY_XMI_STRIP_DIAGRAMS,
                 chkStripDiagrams.isSelected());
        Configuration.setBoolean(PersistenceManager.USE_SAFE_SAVES,
                chkUseSafeSaves.isSelected());
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
    public JPanel getTabPanel() {
        if (topPanel == null) {
            buildPanel();
        }
        return this;
    }

    /*
     * @see GUISettingsTabInterface#getTabKey()
     */
    public String getTabKey() {
        return "tab.preferences";
    }

}

