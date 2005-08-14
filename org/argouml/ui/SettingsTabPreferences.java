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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.argouml.application.ArgoVersion;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;

/** Action object for handling Argo settings
 *
 *  @author Thierry Lach
 *  @since  0.9.4
 */
public class SettingsTabPreferences extends SettingsTabHelper
    implements SettingsTabPanel {

    private JCheckBox chkSplash = null;
    private JCheckBox chkPreload = null;
    private JCheckBox chkReloadRecent = null;

    /**
     * The constructor.
     *
     */
    public SettingsTabPreferences() {
        super();
        setLayout(new BorderLayout());
	JPanel top = new JPanel();
    	top.setLayout(new GridBagLayout());

	GridBagConstraints checkConstraints = new GridBagConstraints();
	checkConstraints.anchor = GridBagConstraints.WEST;
	checkConstraints.gridy = 0;
	checkConstraints.gridx = 0;
	checkConstraints.gridwidth = 1;
	checkConstraints.gridheight = 1;
	checkConstraints.insets = new Insets(0, 30, 0, 4);

	GridBagConstraints labelConstraints = new GridBagConstraints();
	labelConstraints.anchor = GridBagConstraints.EAST;
	labelConstraints.gridy = 0;
	labelConstraints.gridx = 0;
	labelConstraints.gridwidth = 1;
	labelConstraints.gridheight = 1;
	labelConstraints.insets = new Insets(2, 10, 2, 4);

	GridBagConstraints fieldConstraints = new GridBagConstraints();
	fieldConstraints.anchor = GridBagConstraints.WEST;
	fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
	fieldConstraints.gridy = 0;
	fieldConstraints.gridx = 1;
	fieldConstraints.gridwidth = 3;
	fieldConstraints.gridheight = 1;
	fieldConstraints.weightx = 1.0;
	fieldConstraints.insets = new Insets(0, 4, 0, 20);

	checkConstraints.gridy = 0;
	labelConstraints.gridy = 0;
	fieldConstraints.gridy = 0;
        chkSplash = createCheckBox("label.splash");
	top.add(chkSplash, checkConstraints);
	top.add(new JLabel(""), labelConstraints);
	top.add(new JLabel(""), fieldConstraints);

	checkConstraints.gridy = 1;
        chkPreload = createCheckBox("label.preload");
 	top.add(chkPreload, checkConstraints);

	checkConstraints.gridy = 3;
        chkReloadRecent = createCheckBox("label.reload-recent");
 	top.add(chkReloadRecent, checkConstraints);

	add(top, BorderLayout.NORTH);
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabRefresh()
     */
    public void handleSettingsTabRefresh() {
        chkSplash.setSelected(Configuration.getBoolean(Argo.KEY_SPLASH, true));
        chkPreload.setSelected(Configuration.getBoolean(Argo.KEY_PRELOAD,
                true));
        chkReloadRecent.setSelected(
		Configuration.getBoolean(Argo.KEY_RELOAD_RECENT_PROJECT,
					 false));
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabSave()
     */
    public void handleSettingsTabSave() {
        Configuration.setBoolean(Argo.KEY_SPLASH, chkSplash.isSelected());
        Configuration.setBoolean(Argo.KEY_PRELOAD, chkPreload.isSelected());
        Configuration.setBoolean(Argo.KEY_RELOAD_RECENT_PROJECT,
				 chkReloadRecent.isSelected());
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabCancel()
     */
    public void handleSettingsTabCancel() {
        handleSettingsTabRefresh();
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() { return "SettingsTabPreferences"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() {
	return "Settings Tab for Preferences";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() { return "ArgoUML Core"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() { return ArgoVersion.getVersion(); }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() { return "module.settings.preferences"; }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#getTabKey()
     */
    public String getTabKey() { return "tab.preferences"; }
}

