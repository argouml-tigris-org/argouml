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
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.argouml.application.ArgoVersion;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;



/**
 * Action object for handling Argo settings.
 *
 * @author Thierry Lach
 * @since  0.9.4
 */
public class SettingsTabEnvironment extends SettingsTabHelper
    implements SettingsTabPanel {

    private String userDir;

    private JTextField fieldArgoRoot;
    private JTextField fieldArgoHome;
    private JTextField fieldArgoExtDir;
    private JTextField fieldJavaHome;
    private JTextField fieldUserHome;
    private JTextField fieldUserDir;
    private JTextField fieldStartupDir;

    private JButton userDirButton;

    /**
     * The constructor.
     *
     */
    public SettingsTabEnvironment() {
        super();
        setLayout(new BorderLayout());
	JPanel top = new JPanel();
    	top.setLayout(new GridBagLayout());

	GridBagConstraints labelConstraints = new GridBagConstraints();
	labelConstraints.anchor = GridBagConstraints.WEST;
	labelConstraints.gridy = 0;
	labelConstraints.gridx = 0;
	labelConstraints.gridwidth = 1;
	labelConstraints.gridheight = 1;
	labelConstraints.insets = new Insets(2, 20, 2, 4);

	GridBagConstraints fieldConstraints = new GridBagConstraints();
	fieldConstraints.anchor = GridBagConstraints.EAST;
	fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
	fieldConstraints.gridy = 0;
	fieldConstraints.gridx = 1;
	fieldConstraints.gridwidth = 3;
	fieldConstraints.gridheight = 1;
	fieldConstraints.weightx = 1.0;
	fieldConstraints.insets = new Insets(2, 4, 2, 20);

	labelConstraints.gridy = 0;
	fieldConstraints.gridy = 0;
	// This string is NOT to be translated! See issue 2381.
	//top.add(createLabel("${argo.root}"), labelConstraints);
	top.add(new JLabel("${argo.root}"), labelConstraints);
	fieldArgoRoot = createTextField();
	fieldArgoRoot.setEnabled(false);
	top.add(fieldArgoRoot, fieldConstraints);

	labelConstraints.gridy = 1;
	fieldConstraints.gridy = 1;
	// This string is NOT to be translated! See issue 2381.
	top.add(new JLabel("${argo.home}"), labelConstraints);
        fieldArgoHome = createTextField();
	fieldArgoHome.setEnabled(false);
	top.add(fieldArgoHome, fieldConstraints);

	labelConstraints.gridy = 2;
	fieldConstraints.gridy = 2;
 	// This string is NOT to be translated! See issue 2381.
	top.add(new JLabel("${argo.ext.dir}"), labelConstraints);
        fieldArgoExtDir = createTextField();
	fieldArgoExtDir.setEnabled(false);
	top.add(fieldArgoExtDir, fieldConstraints);

	labelConstraints.gridy = 3;
	fieldConstraints.gridy = 3;
  	// This string is NOT to be translated! See issue 2381.
	top.add(new JLabel("${java.home}"), labelConstraints);
        fieldJavaHome = createTextField();
	fieldJavaHome.setEnabled(false);
	top.add(fieldJavaHome, fieldConstraints);

	labelConstraints.gridy = 4;
	fieldConstraints.gridy = 4;
  	// This string is NOT to be translated! See issue 2381.
	top.add(new JLabel("${user.home}"), labelConstraints);
        fieldUserHome = createTextField();
	fieldUserHome.setEnabled(false);
	top.add(fieldUserHome, fieldConstraints);

	labelConstraints.gridy = 5;
	fieldConstraints.gridy = 5;
	// This string is NOT to be translated! See issue 2381.
	top.add(new JLabel("${user.dir}"), labelConstraints);
        fieldUserDir = createTextField();
	fieldUserDir.setEnabled(false);
	top.add(fieldUserDir, fieldConstraints);

	labelConstraints.gridy = 6;
	fieldConstraints.gridy = 6;
  	top.add(createLabel("label.startup-directory"), labelConstraints);
        fieldStartupDir = createTextField();
	fieldStartupDir.setEnabled(false);
	top.add(fieldStartupDir, fieldConstraints);

	add(top, BorderLayout.NORTH);
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabRefresh()
     */
    public void handleSettingsTabRefresh() {
        fieldArgoRoot.setText(Argo.getArgoRoot());
        fieldArgoHome.setText(Argo.getArgoHome());
        fieldArgoExtDir.setText(Argo.getArgoHome() + File.separator + "ext");
        fieldJavaHome.setText(System.getProperty("java.home"));
        fieldUserHome.setText(System.getProperty("user.home"));
        fieldUserDir.setText(Configuration.getString(Argo.KEY_STARTUP_DIR,
		System.getProperty("user.dir")));
        fieldStartupDir.setText(Argo.getDirectory());
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabSave()
     */
    public void handleSettingsTabSave() {
        Configuration.setString(Argo.KEY_STARTUP_DIR, fieldUserDir.getText());
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
    public String getModuleName() { return "SettingsTabEnvironment"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() {
	return "Settings Tab for Environment";
    }

    /**
     * Use of Module is curious. Does this mean the
     * author of a particular zargo?
     * This means the author of this extension to ArgoUML.
     * this information is not stored in the .argo xml
     * in zargo
     *
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() { return "ArgoUML Core"; }

    /** This should call on a global config file somewhere
     * .9.4 is the last version of argo
     *
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() { return ArgoVersion.getVersion(); }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() { return "module.settings.environment"; }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#getTabKey()
     */
    public String getTabKey() { return "tab.environment"; }

}

