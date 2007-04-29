// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;

/**
 * Tab Panel for setting the default user attributes: name and email.
 *
 * @author Thierry Lach
 * @since  0.9.4
 */
class SettingsTabUser extends JPanel
    implements GUISettingsTabInterface {

    /**
     * This is where the user enters full name in settings tab.
     * This information is stored
     * in the argo.user.properties file.
     */
    private JTextField userFullname;

    /**
     * This is where the user enters email in settings tab.
     * This information is stored
     * in the argo.user.properties file.
     */
    private JTextField userEmail;

    /**
     * The constructor.
     *
     */
    SettingsTabUser() {
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
	top.add(new JLabel(Translator.localize("label.user")),
                labelConstraints);
	JTextField j = new JTextField();
        userFullname = j;
	top.add(userFullname, fieldConstraints);

	labelConstraints.gridy = 1;
	fieldConstraints.gridy = 1;
 	top.add(new JLabel(Translator.localize("label.email")),
                labelConstraints);
 	JTextField j1 = new JTextField();
        userEmail = j1;
	top.add(userEmail, fieldConstraints);

	add(top, BorderLayout.NORTH);
    }

    /*
     * @see GUISettingsTabInterface#handleSettingsTabRefresh()
     */
    public void handleSettingsTabRefresh() {
        userFullname.setText(Configuration.getString(Argo.KEY_USER_FULLNAME));
        userEmail.setText(Configuration.getString(Argo.KEY_USER_EMAIL));
    }

    /*
     * @see GUISettingsTabInterface#handleSettingsTabSave()
     */
    public void handleSettingsTabSave() {
        Configuration.setString(Argo.KEY_USER_FULLNAME, userFullname.getText());
        Configuration.setString(Argo.KEY_USER_EMAIL, userEmail.getText());
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
     * @see GUISettingsTabInterface#getTabKey()
     */
    public String getTabKey() { return "tab.user"; }

    /*
     * @see GUISettingsTabInterface#getTabPanel()
     */
    public JPanel getTabPanel() { return this; }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -742258688091914619L;
}
