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
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectSettings;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.profile.Profile;
import org.argouml.uml.profile.ProfileConfiguration;
import org.argouml.uml.profile.ProfileException;
import org.argouml.uml.profile.ProfileManagerImpl;
import org.argouml.uml.profile.UserDefinedProfile;

/**
 * The Tab where new profiles can be added and the registered
 * ones can be activated or deactivated on current project
 *
 * @author Marcos Aurélio
 */
public class ProjectSettingsTabProfile extends JPanel implements
	GUISettingsTabInterface, ActionListener {

    private JButton loadFromFile = new JButton(Translator
	    .localize("tab.profiles.userdefined.load"));

    private JButton unregisterProfile = new JButton(Translator
	    .localize("tab.profiles.userdefined.unload"));

    private JButton addButton = new JButton(">>");

    private JButton removeButton = new JButton("<<");

    private JList availableList = new JList();

    private JList usedList = new JList();
    
    ////////
    
    private JLabel stereoLabel =
        new JLabel(Translator.localize("menu.popup.stereotype-view") + ": ");

    private JComboBox stereoField = new JComboBox();

    /**
     * The default constructor for this class
     */
    public ProjectSettingsTabProfile() {
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
                ProjectSettings ps = ProjectManager.getManager()
                        .getCurrentProject().getProjectSettings();
                Object src = e.getSource();
                
                if (src == stereoField) {
                    Object item = e.getItem();
                    DefaultComboBoxModel model = 
                        (DefaultComboBoxModel) stereoField.getModel();
                    int idx = model.getIndexOf(item);

                    switch (idx) {
                    case 0:
                        ps.setDefaultStereotypeView(
                                FigNodeModelElement.STEREOTYPE_VIEW_TEXTUAL);
                        break;
                    case 1:
                        ps.setDefaultStereotypeView(
                                FigNodeModelElement.STEREOTYPE_VIEW_BIG_ICON);
                        break;
                    case 2:
                        ps.setDefaultStereotypeView(
                                FigNodeModelElement.STEREOTYPE_VIEW_SMALL_ICON);
                        break;
                    }
                }
            }
            
        });

        add(setDefStereoV);
                
        ////////////
        
        
	JPanel configPanel = new JPanel();
	configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));

	availableList.setPrototypeCellValue("12345678901234567890");
	usedList.setPrototypeCellValue("12345678901234567890");

	availableList.setMinimumSize(new Dimension(50, 50));
	usedList.setMinimumSize(new Dimension(50, 50));

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
		.localize("tab.profiles.userdefined.active")),
		BorderLayout.NORTH);
	rightList.add(new JScrollPane(usedList), BorderLayout.CENTER);
	configPanel.add(rightList);

	addButton.addActionListener(this);
	removeButton.addActionListener(this);

	add(configPanel);

	JPanel lffPanel = new JPanel();
	lffPanel.setLayout(new FlowLayout());
	lffPanel.add(loadFromFile);
	lffPanel.add(unregisterProfile);

	loadFromFile.addActionListener(this);
	unregisterProfile.addActionListener(this);

	add(lffPanel);
    }

    private void refreshLists() {
        availableList.setModel(
                new DefaultComboBoxModel(getAvailableProfiles().toArray()));
        usedList.setModel(
                new DefaultComboBoxModel(getUsedProfiles().toArray()));
    }

    private List<Profile> getUsedProfiles() {
        return new ArrayList<Profile>(ProjectManager.getManager()
                .getCurrentProject().getProfileConfiguration().getProfiles());
    }

    private List<Profile> getAvailableProfiles() {
        List<Profile> used = getUsedProfiles();
        List<Profile> ret = new ArrayList<Profile>();
        
        for (Profile profile : ProfileManagerImpl.getInstance()
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
            if (availableList.getSelectedIndex() != -1) {
                Profile selected = (Profile) modelAvailable
                        .getElementAt(availableList.getSelectedIndex());
                modelUsed.addElement(selected);
                modelAvailable.removeElement(selected);
                
                for (Profile profile : getAvailableDependents(selected)) {
                    modelUsed.addElement(profile);
                    modelAvailable.removeElement(profile);                    
                }
            }
	} else if (arg0.getSource() == removeButton) {
            if (usedList.getSelectedIndex() != -1) {
                Profile selected = (Profile) modelUsed.getElementAt(usedList
                        .getSelectedIndex());
     
                List<Profile> dependents = getActiveDependents(selected); 
                boolean remove = true;

                if (!dependents.isEmpty()) {
                    String message = Translator.localize(
                            "tab.profiles.confirmdeletewithdependencies",
                            new Object[] {dependents});
                    String title = Translator.localize(
                            "tab.profiles.confirmdeletewithdependencies.title");
                    remove = (JOptionPane
                            .showConfirmDialog(
                                    this,
                                    message,
                                    title,
                                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
                }
                
                if (remove) {
                    if (!ProfileManagerImpl.getInstance()
                            .getRegisteredProfiles().contains(selected)
                            && !ProfileManagerImpl.getInstance()
                                    .getDefaultProfiles().contains(selected)) {
                        remove = (JOptionPane
                                .showConfirmDialog(
                                        this,
                                        Translator
                                                .localize("tab.profiles.confirmdeleteunregistered"),
                                        Translator
                                                .localize("tab.profiles.confirmdeleteunregistered.title"),
                                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
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
	    if (availableList.getSelectedIndex() != -1) {
                Profile selected = (Profile) modelAvailable
                        .getElementAt(availableList.getSelectedIndex());
                if (selected instanceof UserDefinedProfile) {
                    ProfileManagerImpl.getInstance().removeProfile(selected);
                    modelAvailable.removeElement(selected);
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
			    || (file.isFile() 
				    && (file.getName().endsWith(".xml") 
					  || file.getName().endsWith(".xmi")));
		}

		public String getDescription() {
		    return "XMI Files";
		}

	    });

	    int ret = fileChooser.showOpenDialog(this);
	    if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                try {
                    UserDefinedProfile profile = new UserDefinedProfile(file);
                    ProfileManagerImpl.getInstance().registerProfile(profile);

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

    private List<Profile> getAvailableDependents(Profile selected) {
        MutableComboBoxModel modelAvl = ((MutableComboBoxModel) availableList
                .getModel());
        
        List<Profile> ret = new ArrayList<Profile>();
        for (int i = 0; i < modelAvl.getSize(); ++i) {
            Profile p = (Profile) modelAvl.getElementAt(i);
            
            if (!p.equals(selected) && selected.getDependencies().contains(p)) {
                ret.add(p);
            }
        }
        
        return ret;
    }

    private List<Profile> getActiveDependents(Profile selected) {
        MutableComboBoxModel modelUsd = ((MutableComboBoxModel) usedList
                .getModel());
        
        List<Profile> ret = new ArrayList<Profile>();
        for (int i = 0; i < modelUsd.getSize(); ++i) {
            Profile p = (Profile) modelUsd.getElementAt(i);
            
            if (!p.equals(selected) && p.getDependencies().contains(selected)) {
                ret.add(p);
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
	return this;
    }

    public void handleResetToDefault() {
        refreshLists();
    }

    public void handleSettingsTabCancel() {

    }

    public void handleSettingsTabRefresh() {
        ProjectSettings ps = ProjectManager.getManager().getCurrentProject()
                .getProjectSettings();
        
        switch (ps.getDefaultStereotypeViewValue()) {
        case FigNodeModelElement.STEREOTYPE_VIEW_TEXTUAL:
            stereoField.setSelectedIndex(0);                
            break;
        case FigNodeModelElement.STEREOTYPE_VIEW_BIG_ICON:
            stereoField.setSelectedIndex(1);                
            break;
        case FigNodeModelElement.STEREOTYPE_VIEW_SMALL_ICON:
            stereoField.setSelectedIndex(2);                
            break;
        }
        
        refreshLists();
    }

    public void handleSettingsTabSave() {
        List<Profile> toRemove = new ArrayList<Profile>();
        ProfileConfiguration pc = ProjectManager.getManager()
                .getCurrentProject().getProfileConfiguration();
        
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
            pc.removeProfile(profile);            
        }
                
        for (Profile profile : usedItens) {
            if (!pc.getProfiles().contains(profile)) {
                pc.addProfile(profile);
            }
        }
        
        ExplorerEventAdaptor.getInstance().structureChanged();        
    }

}
