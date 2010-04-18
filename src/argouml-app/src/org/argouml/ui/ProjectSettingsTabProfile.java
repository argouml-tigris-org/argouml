/* $Id$
 *****************************************************************************
 * Copyright (c) 2007-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marcos Aurelio - design and initial implementation
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

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProfileConfiguration;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectSettings;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.UserDefinedProfile;
import org.argouml.uml.diagram.DiagramAppearance;
import org.argouml.uml.diagram.DiagramSettings;

/**
 * The Tab where new profiles can be added and the registered ones can be
 * activated or deactivated on current project
 * 
 * @author Marcos Aurelio
 */
public class ProjectSettingsTabProfile extends JPanel implements
        GUIProjectSettingsTabInterface, ActionListener {

    private Project p;

    private JButton loadFromFile;

    private JButton unregisterProfile;

    private JButton addButton;

    private JButton removeButton;

    private JList availableList;

    private JList usedList;

    // //////

    private JLabel stereoLabel = new JLabel(Translator
            .localize("menu.popup.stereotype-view")
            + ": ");

    private JComboBox stereoField = new JComboBox();

    private JFileChooser fileChooser;

    private boolean initialized = false;
    
    /**
     * The default constructor for this class
     */
    public ProjectSettingsTabProfile() {
        // Defer all work until we're actually needed
    }

    private void buildDialog() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // ////////////

        JPanel setDefStereoV = new JPanel();
        setDefStereoV.setLayout(new FlowLayout());

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
                if (p == null) {
                    return;
                }
                ProjectSettings ps = p.getProjectSettings();
                DiagramSettings ds = ps.getDefaultDiagramSettings();
                Object src = e.getSource();

                if (src == stereoField) {
                    Object item = e.getItem();
                    DefaultComboBoxModel model = 
                        (DefaultComboBoxModel) stereoField.getModel();
                    int idx = model.getIndexOf(item);

                    switch (idx) {
                    case 0:
                        ds.setDefaultStereotypeView(
                                DiagramAppearance.STEREOTYPE_VIEW_TEXTUAL);
                        break;
                    case 1:
                        ds.setDefaultStereotypeView(
                                DiagramAppearance.STEREOTYPE_VIEW_BIG_ICON);
                        break;
                    case 2:
                        ds.setDefaultStereotypeView(
                                DiagramAppearance.STEREOTYPE_VIEW_SMALL_ICON);
                        break;
                    }
                }
            }

        });

        add(setDefStereoV);

        // //////////

        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));

        availableList = createProfileList();
        usedList = createProfileList();

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
        centerButtons.add(addButton);
        removeButton = new JButton("<<");
        centerButtons.add(removeButton);
        configPanel.add(centerButtons);

        JPanel rightList = new JPanel();
        rightList.setLayout(new BorderLayout());
        rightList.add(new JLabel(Translator
                .localize("tab.profiles.userdefined.active")),
                BorderLayout.NORTH);
        rightList.add(new JScrollPane(usedList), BorderLayout.CENTER);
        configPanel.add(rightList);


        addButton.addActionListener(this);
        removeButton.addActionListener(this);

        add(configPanel);

        JPanel lffPanel = new JPanel();
        lffPanel.setLayout(new FlowLayout());

        loadFromFile = new JButton(Translator
                .localize("tab.profiles.userdefined.load"));
        loadFromFile.addActionListener(this);
        lffPanel.add(loadFromFile);

        unregisterProfile = new JButton(Translator
                .localize("tab.profiles.userdefined.unload"));
        unregisterProfile.addActionListener(this);
        lffPanel.add(unregisterProfile);

        add(lffPanel);
        
        initialized = true;
    }
    
   
    @Override
    public void setVisible(boolean flag) {
        if (flag && !initialized) {
            buildDialog();
        }
        super.setVisible(flag);
    }
    
    private JList createProfileList() {
        JList list = new JList();
        list.setMinimumSize(new Dimension(50, 50));
        // TODO: Add double click listener 
        return list;
    }
    private void refreshLists() {
        availableList.setModel(new DefaultComboBoxModel(getAvailableProfiles()
                .toArray()));
        usedList.setModel(
                new DefaultComboBoxModel(getUsedProfiles().toArray()));
    }

    private List<Profile> getUsedProfiles() {
        return new ArrayList<Profile>(
                p.getProfileConfiguration().getProfiles());
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
        MutableComboBoxModel modelAvailable = 
            ((MutableComboBoxModel) availableList.getModel());
        MutableComboBoxModel modelUsed = 
            ((MutableComboBoxModel) usedList.getModel());

        if (arg0.getSource() == addButton) {
            Object[] selections = availableList.getSelectedValues();
            for (Object s : selections) {
                Profile selected = (Profile) s;
                modelUsed.addElement(selected);
                modelAvailable.removeElement(selected);

                for (Profile profile : getAvailableDependents(selected)) {
                    modelUsed.addElement(profile);
                    modelAvailable.removeElement(profile);
                }
            }
        } else if (arg0.getSource() == removeButton) {
            Object[] selections = usedList.getSelectedValues();
            for (Object s : selections) {
                Profile selected = (Profile) s;
                Collection<Profile> dependents = getActiveDependents(selected);
                boolean remove = true;

                if (!dependents.isEmpty()) {
                    String message = Translator.localize(
                            "tab.profiles.confirmdeletewithdependencies",
                            new Object[] {dependents});
                    String title = Translator.localize(
                            "tab.profiles.confirmdeletewithdependencies.title");
                    remove = (JOptionPane.showConfirmDialog(
                            this, message, title, JOptionPane.YES_NO_OPTION) 
                            == JOptionPane.YES_OPTION);
                }

                if (remove) {
                    if (!ProfileFacade.getManager().getRegisteredProfiles()
                            .contains(selected)
                            && !ProfileFacade.getManager().getDefaultProfiles()
                                    .contains(selected)) {
                        remove = (JOptionPane
                                .showConfirmDialog(
                                        this,
                                        Translator.localize(
                                 "tab.profiles.confirmdeleteunregistered"),
                                        Translator.localize(
                                "tab.profiles.confirmdeleteunregistered.title"),
                                        JOptionPane.YES_NO_OPTION) 
                                        == JOptionPane.YES_OPTION);
                    }

                    if (remove) {
                        modelUsed.removeElement(selected);
                        modelAvailable.addElement(selected);

                        for (Profile profile : dependents) {
                            modelUsed.removeElement(profile);
                            modelAvailable.addElement(profile);
                        }
                    }
                }
            }
        } else if (arg0.getSource() == unregisterProfile) {
            Object[] selections = availableList.getSelectedValues();
            for (Object s : selections) {
                Profile selected = (Profile) s;
                if (selected instanceof UserDefinedProfile) {
                    ProfileFacade.getManager().removeProfile(selected);
                    modelAvailable.removeElement(selected);
                } else {
                    JOptionPane.showMessageDialog(this, Translator
                            .localize("tab.profiles.cannotdelete"));
                }
            }
        } else if (arg0.getSource() == loadFromFile) {
            JFileChooser chooser = getFileChooser();

            int ret = chooser.showOpenDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                chooser.setCurrentDirectory(file.getParentFile());
                try {
                    UserDefinedProfile profile = new UserDefinedProfile(file,
                        ProfileFacade.getManager());
                    ProfileFacade.getManager().registerProfile(profile);

                    modelAvailable.addElement(profile);
                } catch (ProfileException e) {
                    JOptionPane.showMessageDialog(this, Translator
                            .localize("tab.profiles.userdefined.errorloading"));
                }
            }
        }

        availableList.validate();
        usedList.validate();
    }

    private JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new ProfileFileFilter());
        }
        return fileChooser;
    }

    private List<Profile> getAvailableDependents(Profile selected) {
        MutableComboBoxModel modelAvl = ((MutableComboBoxModel) availableList
                .getModel());

        List<Profile> ret = new ArrayList<Profile>();
        for (int i = 0; i < modelAvl.getSize(); ++i) {
            Profile profile = (Profile) modelAvl.getElementAt(i);

            if (!profile.equals(selected) 
                    && selected.getDependencies().contains(profile)) {
                ret.add(profile);
            }
        }

        return ret;
    }

    private List<Profile> getActiveDependents(Profile selected) {
        MutableComboBoxModel modelUsd = ((MutableComboBoxModel) usedList
                .getModel());

        List<Profile> ret = new ArrayList<Profile>();
        for (int i = 0; i < modelUsd.getSize(); ++i) {
            Profile profile = (Profile) modelUsd.getElementAt(i);

            if (!profile.equals(selected) 
                    && profile.getDependencies().contains(selected)) {
                ret.add(profile);
            }
        }

        return ret;
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
        if (!initialized) {
            buildDialog();
        }
        return this;
    }

    public void handleResetToDefault() {
        if (!initialized) {
            buildDialog();
        }
        refreshLists();
    }

    public void handleSettingsTabCancel() {

    }

    public void handleSettingsTabRefresh() {
        if (!initialized) {
            buildDialog();
        }
        assert p != null;
        ProjectSettings ps = p.getProjectSettings();
        DiagramSettings ds = ps.getDefaultDiagramSettings();
        
        switch (ds.getDefaultStereotypeViewInt()) {
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

        refreshLists();
    }

    public void handleSettingsTabSave() {
        if (!initialized) {
            return;
        }
        assert p != null;
        List<Profile> toRemove = new ArrayList<Profile>();
        ProfileConfiguration pc = p.getProfileConfiguration();
        Object m = p.getUserDefinedModelList().get(0);

        List<Profile> usedItens = new ArrayList<Profile>();

        MutableComboBoxModel modelUsd = ((MutableComboBoxModel) usedList
                .getModel());

        for (int i = 0; i < modelUsd.getSize(); ++i) {
            usedItens.add((Profile) modelUsd.getElementAt(i));
        }

        for (Profile profile : pc.getProfiles()) {
            if (!usedItens.contains(profile)) {
                toRemove.add(profile);
            }
        }

        for (Profile profile : toRemove) {
            pc.removeProfile(profile, m);
        }

        for (Profile profile : usedItens) {
            if (!pc.getProfiles().contains(profile)) {
                pc.addProfile(profile, m);
            }
        }

        p.setProfileConfiguration(pc);
    }

    public void setProject(Project project) {
        assert project != null;
        p = project;
    }
    
    private class ProfileFileFilter extends FileFilter {
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            if (file.isFile()) {
                String filename = file.getName().toLowerCase();
                String[] validEndings = {".xmi", ".xml", ".xmi.zip", 
                                         ".xml.zip"};
                for (String ending : validEndings) {
                    if (filename.endsWith(ending)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public String getDescription() {
            return "*.xmi *.xml *.xmi.zip *.xml.zip";
        }
    }
}
