// $Id: ProfileSelectionTab.java 13040 2007-07-10 20:00:25Z linus $
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
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.UserDefinedProfile;
import org.argouml.uml.diagram.DiagramAppearance;

/**
 * The Tab containing the global settings for profiles
 *
 * @author Marcos Aurélio
 */
public class SettingsTabProfile extends JPanel implements
	GUISettingsTabInterface, ActionListener {

    private JButton loadFromFile = new JButton(Translator
	    .localize("tab.profiles.userdefined.load"));

    private JButton unregisterProfile = new JButton(Translator
	    .localize("tab.profiles.userdefined.unload"));

    private JButton addButton = new JButton(">>");

    private JButton removeButton = new JButton("<<");

    private JList availableList = new JList();

    private JList defaultList = new JList();

    ////////
    
    private JList directoryList = new JList();

    private JButton addDirectory = new JButton(Translator
            .localize("tab.profiles.directories.add"));

    private JButton removeDirectory = new JButton(Translator
            .localize("tab.profiles.directories.remove"));

    private JButton refreshProfiles = new JButton(Translator
            .localize("tab.profiles.directories.refresh"));    

    ///////

    private JLabel stereoLabel =
        new JLabel(Translator.localize("menu.popup.stereotype-view") + ": ");

    private JComboBox stereoField = new JComboBox();
    
    /**
     * The default constructor for this class
     */
    public SettingsTabProfile() {
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //////////////
        
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
                Object src = e.getSource();
                
                if (src == stereoField) {
                    Object item = e.getItem();
                    DefaultComboBoxModel model = 
                        (DefaultComboBoxModel) stereoField.getModel();
                    int idx = model.getIndexOf(item);

                    switch (idx) {
                    case 0:
                        Configuration.setInteger(
                            ProfileConfiguration.KEY_DEFAULT_STEREOTYPE_VIEW,
                            DiagramAppearance.STEREOTYPE_VIEW_TEXTUAL);
                        break;
                    case 1:
                        Configuration.setInteger(
                            ProfileConfiguration.KEY_DEFAULT_STEREOTYPE_VIEW,
                            DiagramAppearance.STEREOTYPE_VIEW_BIG_ICON);
                        break;
                    case 2:
                        Configuration.setInteger(
                            ProfileConfiguration.KEY_DEFAULT_STEREOTYPE_VIEW,
                            DiagramAppearance.STEREOTYPE_VIEW_SMALL_ICON);
                        break;
                    }
                }
            }
            
        });

        add(setDefStereoV);
                
        ////////////
        
        directoryList.setPrototypeCellValue(
                "123456789012345678901234567890123456789012345678901234567890");
        directoryList.setMinimumSize(new Dimension(50, 50));
        
        JPanel sdirPanel = new JPanel();
        sdirPanel.setLayout(new BoxLayout(sdirPanel, BoxLayout.Y_AXIS));
        
        JPanel dlist = new JPanel();
        dlist.setLayout(new BorderLayout());
                
        JPanel lcb = new JPanel();
        lcb.setLayout(new BoxLayout(lcb, BoxLayout.Y_AXIS));
        
        lcb.add(addDirectory);
        lcb.add(removeDirectory);

        addDirectory.addActionListener(this);
        removeDirectory.addActionListener(this);        
        
        dlist.add(new JScrollPane(directoryList), BorderLayout.CENTER);
        dlist.add(lcb, BorderLayout.EAST);
        
        sdirPanel.add(new JLabel(Translator
                .localize("tab.profiles.directories.desc")));
        sdirPanel.add(dlist);

        add(sdirPanel);
        
        /////
        
	JPanel configPanel = new JPanel();
	configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));

	availableList.setPrototypeCellValue("12345678901234567890");
	defaultList.setPrototypeCellValue("12345678901234567890");

	availableList.setMinimumSize(new Dimension(50, 50));
	defaultList.setMinimumSize(new Dimension(50, 50));

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

	add(configPanel);

	JPanel lffPanel = new JPanel();
	lffPanel.setLayout(new FlowLayout());
	lffPanel.add(loadFromFile);
	lffPanel.add(unregisterProfile);
	lffPanel.add(refreshProfiles);        
        
	loadFromFile.addActionListener(this);
	unregisterProfile.addActionListener(this);
	refreshProfiles.addActionListener(this); //ADDED
        
	add(lffPanel);                       
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
            if (availableList.getSelectedIndex() != -1) {
                Profile selected = (Profile) modelAvl.getElementAt(availableList
                        .getSelectedIndex());
                modelUsd.addElement(selected);
                modelAvl.removeElement(selected);
            }
	} else if (arg0.getSource() == removeButton) {
            if (defaultList.getSelectedIndex() != -1) {
                Profile selected = (Profile) modelUsd.getElementAt(defaultList
                        .getSelectedIndex());
                modelUsd.removeElement(selected);
                modelAvl.addElement(selected);                        
            }
	} else if (arg0.getSource() == unregisterProfile) {
	    if (availableList.getSelectedIndex() != -1) {
                Profile selected = (Profile) modelAvl.getElementAt(availableList
                        .getSelectedIndex());
                if (selected instanceof UserDefinedProfile) {
                    ProfileFacade.getManager().removeProfile(selected);
                    modelAvl.removeElement(selected);
                } else {
                    JOptionPane.showMessageDialog(this, Translator
                            .localize("tab.profiles.cannotdelete"));
                }
            }
	} else if (arg0.getSource() == loadFromFile) {
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setFileFilter(new FileFilter() {

		public boolean accept(File file) {
                    return file.isDirectory()
                            || (file.isFile() && (file.getName().toLowerCase()
                                    .endsWith(".xml") || file.getName()
                                        .toLowerCase().endsWith(".xmi")));
                }

		public String getDescription() {
		    return "*.XMI";
		}

	    });

	    int ret = fileChooser.showOpenDialog(this);
	    if (ret == JFileChooser.APPROVE_OPTION) {
		File file = fileChooser.getSelectedFile();

                try {
                    UserDefinedProfile profile = new UserDefinedProfile(file);

                    ProfileFacade.getManager().registerProfile(profile);

                    modelAvl.addElement(profile);
                } catch (ProfileException e) {
                    JOptionPane.showMessageDialog(this, Translator
                            .localize("tab.profiles.userdefined.errorloading"));
                }
            }

        } else if (arg0.getSource() == removeDirectory) { 
            if (directoryList.getSelectedIndex() != -1) {
                int idx = directoryList.getSelectedIndex();
                ((MutableComboBoxModel) directoryList.getModel())
                        .removeElementAt(idx);
            }
        } else if (arg0.getSource() == refreshProfiles) { 
            boolean refresh = (JOptionPane
                    .showConfirmDialog(
                            this,
                            Translator
                                    .localize("tab.profiles.confirmrefresh"),
                            Translator
                                    .localize("tab.profiles.confirmrefresh.title"),
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);

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

                String path = file.getAbsolutePath();

                ((MutableComboBoxModel) directoryList.getModel())
                        .addElement(path);
            }
            
        }
        
	availableList.validate();
	defaultList.validate();
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
        refreshLists();
    }

    public void handleSettingsTabCancel() {

    }

    public void handleSettingsTabRefresh() {
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

        for (Profile profile : ProfileFacade.getManager()
                .getDefaultProfiles()) {
            if (!usedItens.contains(profile)) {
                toRemove.add(profile);
            }
        }

        for (Profile profile : toRemove) {
            ProfileFacade.getManager().removeFromDefaultProfiles(profile);
        }

        for (Profile profile : usedItens) {
            if (!ProfileFacade.getManager().getDefaultProfiles()
                    .contains(profile)) {
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
            ProfileFacade.getManager()
                    .removeSearchPathDirectory(dirEntry);
        }

        for (String dirEntry : usedItensDir) {
            if (!ProfileFacade.getManager().getSearchPathDirectories()
                    .contains(dirEntry)) {
                ProfileFacade.getManager().addSearchPathDirectory(
                        dirEntry);
            }
        }

    }

}
