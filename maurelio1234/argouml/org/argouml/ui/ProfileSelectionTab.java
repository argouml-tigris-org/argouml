// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
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
import java.io.File;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileFilter;

import org.argouml.kernel.ProjectManager;
import org.argouml.uml.profile.FileModelLoader;
import org.argouml.uml.profile.Profile;
import org.argouml.uml.profile.ProfileConfiguration;
import org.argouml.uml.profile.ProfileManager;
import org.argouml.uml.profile.ProfileManagerImpl;
import org.argouml.uml.profile.UserDefinedProfile;

/**
 * The Tab where new profiles can be added and the registered
 * ones can be activated or deactivated on current project
 *
 * @author Marcos Aurélio
 */
public class ProfileSelectionTab extends JPanel implements
	GUISettingsTabInterface, ActionListener {

    private JButton loadFromFile = new JButton("Load profile from file...");

    private JButton unregisterProfile = new JButton(
	    "Unregister Profile loaded from file");

    private JButton addButton = new JButton(">>");

    private JButton removeButton = new JButton("<<");

    private JList availableList = new JList(new AvailableProfilesListModel());

    private JList usedList = new JList(new UsedProfilesListModel());

    private class AvailableProfilesListModel implements ListModel {
	private ProfileManager profileManager = ProfileManagerImpl
		.getInstance();

	private Vector listeners = new Vector();

	public void addListDataListener(ListDataListener arg0) {
	    listeners.add(arg0);
	}

	public void fireListeners() {
	    ListDataEvent evt = new ListDataEvent(this,
		    ListDataEvent.CONTENTS_CHANGED, 0, getSize());
	    for (int i = 0; i < listeners.size(); ++i) {
		((ListDataListener) listeners.elementAt(i))
			.contentsChanged(evt);
	    }
	}

	public Profile getProfileAt(int arg0) {
	    ProfileConfiguration config = ProjectManager.getManager()
		    .getCurrentProject().getProfileConfiguration();
	    Vector registeredProfiles = profileManager.getRegisteredProfiles();
	    int count = 0;
	    for (int i = 0; i < registeredProfiles.size(); ++i) {
		if (!config.getProfiles().contains(
			registeredProfiles.elementAt(i))) {

		    if (count == arg0) {
			return ((Profile) registeredProfiles.elementAt(i));
		    }
		    ++count;
		}
	    }
	    return null;
	}

	public Object getElementAt(int arg0) {
	    Profile p = getProfileAt(arg0);
	    if (p != null) {
		return p.getDisplayName();
	    } else {
		return null;
	    }
	}

	public int getSize() {
	    ProfileConfiguration config = ProjectManager.getManager()
		    .getCurrentProject().getProfileConfiguration();
	    Vector registeredProfiles = profileManager.getRegisteredProfiles();
	    int count = 0;
	    for (int i = 0; i < registeredProfiles.size(); ++i) {
		if (!config.getProfiles().contains(
			registeredProfiles.elementAt(i))) {
		    ++count;
		}
	    }
	    return count;
	}

	public void removeListDataListener(ListDataListener arg0) {
	    listeners.remove(arg0);
	}
    }

    private class UsedProfilesListModel implements ListModel {
	private Vector listeners = new Vector();

	public void addListDataListener(ListDataListener arg0) {
	    listeners.add(arg0);
	}

	public void fireListeners() {
	    ListDataEvent evt = new ListDataEvent(this,
		    ListDataEvent.CONTENTS_CHANGED, 0, getSize());
	    for (int i = 0; i < listeners.size(); ++i) {
		((ListDataListener) listeners.elementAt(i))
			.contentsChanged(evt);
	    }
	}

	public Profile getProfileAt(int arg0) {
	    ProfileConfiguration config = ProjectManager.getManager()
		    .getCurrentProject().getProfileConfiguration();
	    return ((Profile) config.getProfiles().elementAt(arg0));
	}

	public Object getElementAt(int arg0) {
	    ProfileConfiguration config = ProjectManager.getManager()
		    .getCurrentProject().getProfileConfiguration();
	    return ((Profile) config.getProfiles().elementAt(arg0))
		    .getDisplayName();
	}

	public int getSize() {
	    ProfileConfiguration config = ProjectManager.getManager()
		    .getCurrentProject().getProfileConfiguration();
	    return config.getProfiles().size();
	}

	public void removeListDataListener(ListDataListener arg0) {
	    listeners.remove(arg0);
	}
    }

    public ProfileSelectionTab() {
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

	JPanel configPanel = new JPanel();
	configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));

	availableList.setPrototypeCellValue("12345678901234567890");
	usedList.setPrototypeCellValue("12345678901234567890");

	availableList.setMinimumSize(new Dimension(50, 50));
	usedList.setMinimumSize(new Dimension(50, 50));

	JPanel leftList = new JPanel();
	leftList.setLayout(new BorderLayout());
	leftList.add(new JLabel("Available Profiles:"), BorderLayout.NORTH);
	leftList.add(availableList, BorderLayout.CENTER);
	configPanel.add(leftList);

	JPanel centerButtons = new JPanel();
	centerButtons.setLayout(new BoxLayout(centerButtons, BoxLayout.Y_AXIS));
	centerButtons.add(addButton);
	centerButtons.add(removeButton);
	configPanel.add(centerButtons);

	JPanel rightList = new JPanel();
	rightList.setLayout(new BorderLayout());
	rightList.add(new JLabel("Active Profiles:"), BorderLayout.NORTH);
	rightList.add(usedList, BorderLayout.CENTER);
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

    public void actionPerformed(ActionEvent arg0) {
	if (arg0.getSource() == addButton) {
	    AvailableProfilesListModel model = 
		((AvailableProfilesListModel) availableList.getModel());
	    Profile selected = model.getProfileAt(availableList
		    .getSelectedIndex());
	    ProjectManager.getManager().getCurrentProject()
		    .getProfileConfiguration().addProfile(selected);
	    model.fireListeners();
	} else if (arg0.getSource() == removeButton) {
	    UsedProfilesListModel model = ((UsedProfilesListModel) usedList
		    .getModel());
	    Profile selected = model.getProfileAt(usedList.getSelectedIndex());
	    ProjectManager.getManager().getCurrentProject()
		    .getProfileConfiguration().removeProfile(selected);
	    model.fireListeners();
	} else if (arg0.getSource() == unregisterProfile) {
	    AvailableProfilesListModel model = 
		((AvailableProfilesListModel) availableList
		    .getModel());
	    Profile selected = model.getProfileAt(availableList
		    .getSelectedIndex());
	    if (selected instanceof UserDefinedProfile) {
		ProfileManagerImpl.getInstance().removeProfile(selected);
		model.fireListeners();
	    } else {
		JOptionPane.showMessageDialog(this,
			"Only user defined profiles can be removed");
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

		Profile profile = new UserDefinedProfile(file);

		if (profile.getModel() != null) {
		    ProfileManagerImpl.getInstance().registerProfile(profile);

		    ProjectManager.getManager().getCurrentProject()
			    .getProfileConfiguration().addProfile(profile);

		    UsedProfilesListModel model = 
			((UsedProfilesListModel) usedList.getModel());
		    model.fireListeners();
		} else {
		    JOptionPane.showMessageDialog(this,
			    "Error opening profile!");
		}
	    }
	}

	availableList.validate();
	usedList.validate();
    }

    public String getTabKey() {
	return "tab.profiles";
    }

    public JPanel getTabPanel() {
	return this;
    }

    public void handleResetToDefault() {
	// TODO: Auto-generated method stub

    }

    public void handleSettingsTabCancel() {
	// TODO: Auto-generated method stub

    }

    public void handleSettingsTabRefresh() {
	// TODO: Auto-generated method stub

    }

    public void handleSettingsTabSave() {
	// TODO: Auto-generated method stub

    }

}
