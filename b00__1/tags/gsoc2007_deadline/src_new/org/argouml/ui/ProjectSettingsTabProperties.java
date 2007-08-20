// $Id:ProjectSettingsTabProperties.java 12883 2007-06-19 21:04:47Z mvw $
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.argouml.application.api.Argo;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;

/**
 * Tab Panel for setting the project attributes: 
 * author name and email, project description. 
 * These are stored in the project file.
 *
 * @author michiel
 */
public class ProjectSettingsTabProperties extends JPanel implements
        GUISettingsTabInterface {
    /**
     * This is where the user enters full name in settings tab.
     * This information is stored in the zargo file.
     */
    private JTextField userFullname;

    /**
     * This is where the user enters email in settings tab.
     * This information is stored in the zargo file.
     */
    private JTextField userEmail;

    /**
     * This is where the user enters a description of the project
     * in the settings tab.
     * This information is stored in the zargo file.
     */
    private JTextField description;

    /**
     * This is where the ArgoUML version that last saved this project
     * is shown in the settings tab.
     * This information is stored in the zargo file.
     */
    private JTextField version;

    /**
     * The constructor.
     */
    ProjectSettingsTabProperties() {
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
        userFullname = new JTextField();
        top.add(userFullname, fieldConstraints);

        labelConstraints.gridy = 1;
        fieldConstraints.gridy = 1;
        top.add(new JLabel(Translator.localize("label.email")),
                labelConstraints);
        userEmail = new JTextField();
        top.add(userEmail, fieldConstraints);

        labelConstraints.gridy = 2;
        fieldConstraints.gridy = 2;
        top.add(new JLabel(Translator.localize("label.project.description")),
                labelConstraints);
        description = new JTextField();
        top.add(description, fieldConstraints);

        labelConstraints.gridy = 3;
        fieldConstraints.gridy = 3;
        top.add(new JLabel(Translator.localize("label.argouml.version")),
                labelConstraints);
        version = new JTextField();
        version.setEditable(false);
        top.add(version, fieldConstraints);

        add(top, BorderLayout.NORTH);
    }

    /*
     * @see org.argouml.ui.GUISettingsTabInterface#handleSettingsTabRefresh()
     */
    public void handleSettingsTabRefresh() {
        Project p = ProjectManager.getManager().getCurrentProject();
        userFullname.setText(p.getAuthorname());
        userEmail.setText(p.getAuthoremail());
        description.setText(p.getDescription());
        version.setText(p.getVersion());
    }

    /*
     * @see org.argouml.ui.GUISettingsTabInterface#handleSettingsTabSave()
     */
    public void handleSettingsTabSave() {
        Project p = ProjectManager.getManager().getCurrentProject();
        p.setAuthorname(userFullname.getText());
        p.setAuthoremail(userEmail.getText());
        p.setDescription(description.getText());
    }

    /*
     * @see org.argouml.ui.GUISettingsTabInterface#handleSettingsTabCancel()
     */
    public void handleSettingsTabCancel() {
        handleSettingsTabRefresh();
    }

    /*
     * @see org.argouml.ui.GUISettingsTabInterface#handleResetToDefault()
     */
    public void handleResetToDefault() {
        userFullname.setText(Configuration.getString(Argo.KEY_USER_FULLNAME));
        userEmail.setText(Configuration.getString(Argo.KEY_USER_EMAIL));
        // There is no default description.
    }

    /*
     * @see org.argouml.ui.GUISettingsTabInterface#getTabKey()
     */
    public String getTabKey() { return "tab.user"; }

    /*
     * @see org.argouml.ui.GUISettingsTabInterface#getTabPanel()
     */
    public JPanel getTabPanel() { return this; }

}
