/* $Id$
 *****************************************************************************
 * Copyright (c) 2007-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marcos Aur�lio - Design and initial implementation
 *    thn
 *    euluis
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007-2009 The Regents of the University of California. All
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.MutableComboBoxModel;
import javax.swing.filechooser.FileFilter;

import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProfileConfiguration;
import org.argouml.model.Model;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.UserDefinedProfile;
import org.argouml.profile.UserDefinedProfileHelper;
import org.argouml.swingext.JLinkButton;
import org.argouml.uml.diagram.DiagramAppearance;

/**
 * The Tab containing the global settings for profiles
 * 
 * @author Marcos Aur�lio
 */
public class SettingsTabProfile extends JPanel implements
        GUISettingsTabInterface, ActionListener {

    private JButton loadFromFile;

    private JButton addButton;

    private JButton removeButton;

    private JList availableList;

    private JList defaultList;

    // //////

    private JList directoryList;

    private JButton addDirectory;

    private JButton removeDirectory;

    private JButton refreshProfiles = new JButton(Translator
            .localize("tab.profiles.directories.refresh"));

    // /////

    private JLabel stereoLabel;

    private JComboBox stereoField;

    private boolean initialized = false;

    /**
     * Construct the Profile settings tab
     */
    public SettingsTabProfile() {
    }

    private void buildPanel() {
        setLayout(new BorderLayout());

        JPanel warning = new JPanel();
        warning.setLayout(new BoxLayout(warning, BoxLayout.PAGE_AXIS));
        JLabel warningLabel = new JLabel(Translator.localize("label.warning"));
        warningLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        warning.add(warningLabel);

        JLinkButton projectSettings = new JLinkButton();
        projectSettings.setAction(new ActionProjectSettings());
        projectSettings.setText(Translator.localize("button.project-settings"));
        projectSettings.setIcon(null);
        projectSettings.setAlignmentX(Component.RIGHT_ALIGNMENT);
        warning.add(projectSettings);

        add(warning, BorderLayout.NORTH);

        JPanel profileSettings = new JPanel();
        profileSettings.setLayout(new BoxLayout(profileSettings,
                BoxLayout.Y_AXIS));

        profileSettings.add(initDefaultStereotypeViewSelector());

        JPanel sdirPanel = new JPanel();
        sdirPanel.setLayout(new BoxLayout(sdirPanel, BoxLayout.Y_AXIS));


        JPanel lcb = new JPanel();
        lcb.setLayout(new BoxLayout(lcb, BoxLayout.Y_AXIS));

        addDirectory = new JButton(Translator
                .localize("tab.profiles.directories.add"));
        removeDirectory = new JButton(Translator
                .localize("tab.profiles.directories.remove"));
        
        lcb.add(addDirectory);
        lcb.add(removeDirectory);

        addDirectory.addActionListener(this);
        removeDirectory.addActionListener(this);

        directoryList = new JList();
        directoryList.setMinimumSize(new Dimension(50, 50));

        JPanel dlist = new JPanel();
        dlist.setLayout(new BorderLayout());        
        dlist.add(new JScrollPane(directoryList), BorderLayout.CENTER);
        dlist.add(lcb, BorderLayout.EAST);

        sdirPanel.add(new JLabel(Translator
                .localize("tab.profiles.directories.desc")));
        sdirPanel.add(dlist);

        profileSettings.add(sdirPanel);

        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));

        availableList = createProfileList();
        defaultList = createProfileList();

        refreshLists();

        JPanel leftList = new JPanel();
        leftList.setLayout(new BorderLayout());
        leftList.add(new JLabel(Translator
                .localize("tab.profiles.userdefined.available")),
                BorderLayout.NORTH);
        leftList.add(new JScrollPane(availableList), BorderLayout.CENTER);
        configPanel.add(leftList);

        JPanel centerButtons = new JPanel();
        centerButtons.setLayout(new BoxLayout(centerButtons, BoxLayout.Y_AXIS));
        addButton = new JButton(">>");
        removeButton = new JButton("<<");
        centerButtons.add(addButton);
        centerButtons.add(removeButton);
        configPanel.add(centerButtons);

        JPanel rightList = new JPanel();
        rightList.setLayout(new BorderLayout());
        rightList.add(new JLabel(Translator
                .localize("tab.profiles.userdefined.default")),
                BorderLayout.NORTH);

        rightList.add(new JScrollPane(defaultList), BorderLayout.CENTER);
        configPanel.add(rightList);

        addButton.addActionListener(this);
        removeButton.addActionListener(this);

        profileSettings.add(configPanel);

        JPanel lffPanel = new JPanel();
        lffPanel.setLayout(new FlowLayout());
        loadFromFile = new JButton(Translator
                .localize("tab.profiles.userdefined.load"));
        lffPanel.add(loadFromFile);
        lffPanel.add(refreshProfiles);

        loadFromFile.addActionListener(this);
        refreshProfiles.addActionListener(this);

        profileSettings.add(lffPanel);

        add(profileSettings, BorderLayout.CENTER);

        initialized = true;
    }

    
    @Override
    public void setVisible(boolean flag) {
        if (flag && !initialized) {
            buildPanel();
        }
        super.setVisible(flag);
    }
    
    private JList createProfileList() {
        JList list = new JList();
        list.setMinimumSize(new Dimension(50, 50));
        return list;
    }

    private JPanel initDefaultStereotypeViewSelector() {
        JPanel setDefStereoV = new JPanel();
        setDefStereoV.setLayout(new FlowLayout());
        stereoField = new JComboBox();
        stereoLabel = new JLabel(Translator
                .localize("menu.popup.stereotype-view")
                + ": ");
        stereoLabel.setLabelFor(stereoField);
        setDefStereoV.add(stereoLabel);
        setDefStereoV.add(stereoField);

        DefaultComboBoxModel cmodel = new DefaultComboBoxModel();
        stereoField.setModel(cmodel);

        cmodel.addElement(Translator
                .localize("menu.popup.stereotype-view.textual"));
        cmodel.addElement(Translator
                .localize("menu.popup.stereotype-view.big-icon"));
        cmodel.addElement(Translator
                .localize("menu.popup.stereotype-view.small-icon"));

        stereoField.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                Object src = e.getSource();

                if (src == stereoField) {
                    Object item = e.getItem();
                    DefaultComboBoxModel model = 
                        (DefaultComboBoxModel) stereoField.getModel();
                    int idx = model.getIndexOf(item);

                    switch (idx) {
                    case 0:
                        Configuration
                                .setInteger(
                                        ProfileConfiguration.KEY_DEFAULT_STEREOTYPE_VIEW,
                                        DiagramAppearance.STEREOTYPE_VIEW_TEXTUAL);
                        break;
                    case 1:
                        Configuration
                                .setInteger(
                                        ProfileConfiguration.KEY_DEFAULT_STEREOTYPE_VIEW,
                                        DiagramAppearance.STEREOTYPE_VIEW_BIG_ICON);
                        break;
                    case 2:
                        Configuration
                                .setInteger(
                                        ProfileConfiguration.KEY_DEFAULT_STEREOTYPE_VIEW,
                                        DiagramAppearance.STEREOTYPE_VIEW_SMALL_ICON);
                        break;
                    }
                }
            }

        });
        return setDefStereoV;
    }

    private void refreshLists() {
        availableList.setModel(new DefaultComboBoxModel(getAvailableProfiles()
                .toArray()));
        defaultList.setModel(new DefaultComboBoxModel(getUsedProfiles()
                .toArray()));
        directoryList.setModel(new DefaultComboBoxModel(ProfileFacade
                .getManager().getSearchPathDirectories().toArray()));
    }

    private List<Profile> getUsedProfiles() {
        return new ArrayList<Profile>(ProfileFacade.getManager()
                .getDefaultProfiles());
    }

    private List<Profile> getAvailableProfiles() {
        List<Profile> used = getUsedProfiles();
        List<Profile> ret = new ArrayList<Profile>();

        for (Profile profile : ProfileFacade.getManager()
                .getRegisteredProfiles()) {
            if (!used.contains(profile)) {
                ret.add(profile);
            }
        }

        return ret;
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        MutableComboBoxModel modelAvl = ((MutableComboBoxModel) availableList
                .getModel());
        MutableComboBoxModel modelUsd = ((MutableComboBoxModel) defaultList
                .getModel());

        if (arg0.getSource() == addButton) {
            Object[] selections = availableList.getSelectedValues();
            for (Object s : selections) {
                Profile selected = (Profile) s;
                modelUsd.addElement(selected);
                modelAvl.removeElement(selected);
            }
        } else if (arg0.getSource() == removeButton) {
            Object[] selections = defaultList.getSelectedValues();
            for (Object s : selections) {
                Profile selected = (Profile) s;
                if (selected == ProfileFacade.getManager().getUMLProfile()
                        && Model.getFacade().getUmlVersion().charAt(0) != '1') {
                    JOptionPane.showMessageDialog(this, Translator
                            .localize("tab.profiles.cantremoveuml"));
                } else {
                    modelUsd.removeElement(selected);
                    modelAvl.addElement(selected);
                }
            }
        } else if (arg0.getSource() == loadFromFile) {
            JFileChooser fileChooser =
                UserDefinedProfileHelper.createUserDefinedProfileFileChooser();
            int ret = fileChooser.showOpenDialog(this);
            List<File> files = null;
            if (ret == JFileChooser.APPROVE_OPTION) {
                files = UserDefinedProfileHelper.getFileList(
                    fileChooser.getSelectedFiles());
            }
            if (files != null && files.size() > 0) {
                for (File file : files) {
                    try {
                        UserDefinedProfile profile =
                            new UserDefinedProfile(file,
                                ProfileFacade.getManager());
                        ProfileFacade.getManager().registerProfile(profile);
                        modelAvl.addElement(profile);
                    } catch (ProfileException e) {
                        JOptionPane.showMessageDialog(this, Translator
                            .localize("tab.profiles.userdefined.errorloading")
                            + ": " + file.getAbsolutePath());
                    }
                }
            }

        } else if (arg0.getSource() == removeDirectory) {
            MutableComboBoxModel model = 
                ((MutableComboBoxModel) directoryList.getModel()); 
            Object[] selections = directoryList.getSelectedValues();
            for (Object o : selections) {
                model.removeElement(o);
            }
        } else if (arg0.getSource() == refreshProfiles) {
            boolean refresh = JOptionPane.showConfirmDialog(this, Translator
                    .localize("tab.profiles.confirmrefresh"), Translator
                    .localize("tab.profiles.confirmrefresh.title"),
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
            if (refresh) {
                handleSettingsTabSave();
                ProfileFacade.getManager().refreshRegisteredProfiles();
                refreshLists();
            }
        } else if (arg0.getSource() == addDirectory) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileFilter() {

                public boolean accept(File file) {
                    return file.isDirectory();
                }

                public String getDescription() {
                    return "Directories";
                }

            });

            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int ret = fileChooser.showOpenDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                // find and add all subdirectories
                Collection<File> dirs = new ArrayList<File>();
                collectSubdirs(file, dirs);
                
                MutableComboBoxModel cbModel = 
                    ((MutableComboBoxModel) directoryList.getModel());
                for (File dir : dirs) {
                    cbModel.addElement(dir.getAbsolutePath());
                }
            }
        }

        availableList.validate();
        defaultList.validate();
    }

    private void collectSubdirs(File file, Collection<File> subdirs) {
        subdirs.add(file);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory() & !f.isHidden()) {
                    collectSubdirs(f, subdirs);
                }
            }
        }
    }

    /**
     * @return the internationalization key that containing the name of this tab
     * @see org.argouml.application.api.GUISettingsTabInterface#getTabKey()
     */
    public String getTabKey() {
        return "tab.profiles";
    }

    /**
     * @return the panel containing this tab
     * @see org.argouml.application.api.GUISettingsTabInterface#getTabPanel()
     */
    public JPanel getTabPanel() {
        return this;
    }

    public void handleResetToDefault() {
        if (!initialized) {
            buildPanel();
        }
        refreshLists();
    }

    public void handleSettingsTabCancel() {
    }

    public void handleSettingsTabRefresh() {
        if (!initialized) {
            buildPanel();
        }
        refreshLists();

        switch (Configuration.getInteger(
                ProfileConfiguration.KEY_DEFAULT_STEREOTYPE_VIEW,
                DiagramAppearance.STEREOTYPE_VIEW_TEXTUAL)) {
        case DiagramAppearance.STEREOTYPE_VIEW_TEXTUAL:
            stereoField.setSelectedIndex(0);
            break;
        case DiagramAppearance.STEREOTYPE_VIEW_BIG_ICON:
            stereoField.setSelectedIndex(1);
            break;
        case DiagramAppearance.STEREOTYPE_VIEW_SMALL_ICON:
            stereoField.setSelectedIndex(2);
            break;
        }
    }

    public void handleSettingsTabSave() {
        List<Profile> toRemove = new ArrayList<Profile>();
        List<Profile> usedItens = new ArrayList<Profile>();

        MutableComboBoxModel modelUsd = ((MutableComboBoxModel) defaultList
                .getModel());
        MutableComboBoxModel modelDir = ((MutableComboBoxModel) directoryList
                .getModel());

        for (int i = 0; i < modelUsd.getSize(); ++i) {
            usedItens.add((Profile) modelUsd.getElementAt(i));
        }

        for (Profile profile 
                : ProfileFacade.getManager().getDefaultProfiles()) {
            if (!usedItens.contains(profile)) {
                toRemove.add(profile);
            }
        }

        for (Profile profile : toRemove) {
            ProfileFacade.getManager().removeFromDefaultProfiles(profile);
        }

        for (Profile profile : usedItens) {
            if (!ProfileFacade.getManager().getDefaultProfiles().contains(
                    profile)) {
                ProfileFacade.getManager().addToDefaultProfiles(profile);
            }
        }

        List<String> toRemoveDir = new ArrayList<String>();
        List<String> usedItensDir = new ArrayList<String>();

        for (int i = 0; i < modelDir.getSize(); ++i) {
            usedItensDir.add((String) modelDir.getElementAt(i));
        }

        for (String dirEntry : ProfileFacade.getManager()
                .getSearchPathDirectories()) {
            if (!usedItensDir.contains(dirEntry)) {
                toRemoveDir.add(dirEntry);
            }
        }

        for (String dirEntry : toRemoveDir) {
            ProfileFacade.getManager().removeSearchPathDirectory(dirEntry);
        }

        for (String dirEntry : usedItensDir) {
            if (!ProfileFacade.getManager().getSearchPathDirectories()
                    .contains(dirEntry)) {
                ProfileFacade.getManager().addSearchPathDirectory(dirEntry);
            }
        }

    }

}
