// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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
import javax.swing.JPanel;
import javax.swing.JTextField;
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
public class SettingsTabUser extends SettingsTabHelper
    implements SettingsTabPanel 
{
    /** 
     * This is where the user enters full name in settings tab.
     * This information is stored
     * in the argo.user.properties file.
     */    
    private JTextField userFullname = null;

    /** 
     * This is where the user enters email in settings tab.
     * This information is stored
     * in the argo.user.properties file.
     */    
    private JTextField userEmail = null;

    /**
     * The constructor.
     * 
     */
    public SettingsTabUser() {
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
	top.add(createLabel("label.user"), labelConstraints);
        userFullname = createTextField();
	top.add(userFullname, fieldConstraints);

	labelConstraints.gridy = 1;
	fieldConstraints.gridy = 1;
 	top.add(createLabel("label.email"), labelConstraints);
        userEmail = createTextField();
	top.add(userEmail, fieldConstraints);

	add(top, BorderLayout.NORTH);
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabRefresh()
     */
    public void handleSettingsTabRefresh() {
        userFullname.setText(Configuration.getString(Argo.KEY_USER_FULLNAME));
        userEmail.setText(Configuration.getString(Argo.KEY_USER_EMAIL));
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabSave()
     */
    public void handleSettingsTabSave() {
        Configuration.setString(Argo.KEY_USER_FULLNAME, userFullname.getText());
        Configuration.setString(Argo.KEY_USER_EMAIL, userEmail.getText());
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
    public String getModuleName() { return "SettingsTabUser"; }
    
    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() { return "Settings Tab for User"; }
    
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
    public String getModuleKey() { return "module.settings.user"; }
    
    /**
     * @see org.argouml.application.api.SettingsTabPanel#getTabKey()
     */
    public String getTabKey() { return "tab.user"; }
}
