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
import org.argouml.application.api.*;
import org.argouml.kernel.*;
import org.argouml.uml.ui.UMLAction;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import java.util.*;
import org.tigris.gef.util.*;

/** Action object for handling Argo settings
 *
 *  @author Thomas N
 *  @author Thierry Lach
 *  @since  0.9.4
 */
public class ActionSettings extends UMLAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    /** Localization key for Settings
     */
    public final static String SETTINGS_BUNDLE = "CoreSettings";

    /** One and only instance.
     */
    private static ActionSettings SINGLETON = new ActionSettings();

    /** Get the instance.
     */
    public static ActionSettings getInstance() {
        return SINGLETON;
    }

    ////////////////////////////////////////////////////////////////
    // constructors
    protected JButton buttonOk = null;
    protected JButton buttonCancel = null;
    protected JButton buttonApply = null;
    protected JTabbedPane tabs = null;
    protected JDialog dlg = null;

    protected ActionSettings() {
        super(Argo.localize(Argo.MENU_BUNDLE,"Settings..."), false);
    }

    /** Helper for localization.
     */
    protected String localize(String key) {
        return Argo.localize(SETTINGS_BUNDLE, key);
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent event) {
	Object source = event.getSource();

	if (source.equals(buttonOk) && tabs != null) {
	   for (int i = 0; i < tabs.getComponentCount(); i++) {
	       Object o = tabs.getComponent(i);
	       if (o instanceof SettingsTabPanel) {
		   ((SettingsTabPanel)o).handleSettingsTabSave();
	       }
	   }
	   dlg.setVisible(false);
	} else if (source.equals(buttonApply) && tabs != null) {
	   // Same as ok but don't hide it.
	   for (int i = 0; i < tabs.getComponentCount(); i++) {
	       Object o = tabs.getComponent(i);
	       if (o instanceof SettingsTabPanel) {
		   ((SettingsTabPanel)o).handleSettingsTabSave();
	       }
	   }

	} else if (source.equals(buttonCancel) && tabs != null) {
	   for (int i = 0; i < tabs.getComponentCount(); i++) {
	       Object o = tabs.getComponent(i);
	       if (o instanceof SettingsTabPanel) {
		   ((SettingsTabPanel)o).handleSettingsTabCancel();
	       }
	   }
	   dlg.setVisible(false);
	}
	else if (source instanceof JMenuItem) {
            ProjectBrowser pb = ProjectBrowser.TheInstance;
	    if (dlg == null) {
                try {
	            dlg = new JDialog(pb, localize("caption_settings"), true);

	            dlg.getContentPane().setLayout(new BorderLayout());
                    tabs = new JTabbedPane();
	            dlg.getContentPane().add(tabs, BorderLayout.CENTER);
     
	            JPanel buttons = new JPanel();
	            buttons.setLayout(new FlowLayout());
        
	            buttonOk = new JButton(localize("button_ok"));
	            buttonOk.addActionListener(this);
	            buttons.add (buttonOk);
        
	            buttonCancel = new JButton(localize("button_cancel"));
	            buttonCancel.addActionListener(this);
	            buttons.add (buttonCancel);
        
	            buttonApply = new JButton(localize("button_apply"));
	            buttonApply.addActionListener(this);
	            buttons.add (buttonApply);
        
	            dlg.getContentPane().add(buttons, BorderLayout.SOUTH);
        
	            tabs.addTab(localize("tab_preferences"),
		                new PreferencesTab(SETTINGS_BUNDLE));
	            tabs.addTab(localize("tab_user"),
		                new UserTab(SETTINGS_BUNDLE));
        
                } catch (Exception exception) {
                    Argo.log.error("got an Exception in ActionSettings");
	            Argo.log.error(exception);
                }
	    }
	    dlg.setSize(500, 300);
	    dlg.setLocation(pb.getLocation().x + 100, pb.getLocation().y + 100);
	    // Refresh all the tab data
	    for (int i = 0; i < tabs.getComponentCount(); i++) {
	        Object o = tabs.getComponent(i);
	        if (o instanceof SettingsTabPanel) {
		    ((SettingsTabPanel)o).handleSettingsTabRefresh();
	        }
	    } 
	    dlg.toFront();
	    dlg.setVisible(true);
	}
    }
}
/* end class ActionSettings */

class PreferencesTab extends SettingsTabHelper
implements SettingsTabPanel {

    JCheckBox _splash = null;
    JCheckBox _preload = null;
    JCheckBox _edem = null;
    JCheckBox _profile = null;

    PreferencesTab(String bundle) {
        super(bundle);
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
        _splash = createCheckBox("label_splash");
	top.add(_splash, checkConstraints);
	top.add(new JLabel(""), labelConstraints);
	top.add(new JLabel(""), fieldConstraints);

	checkConstraints.gridy = 1;
        _preload = createCheckBox("label_preload");
 	top.add(_preload, checkConstraints);

	checkConstraints.gridy = 2;
        _edem = createCheckBox("label_edem");
 	top.add(_edem, checkConstraints);

	checkConstraints.gridy = 3;
        _profile = createCheckBox("label_profile");
 	top.add(_profile, checkConstraints);

	add(top, BorderLayout.NORTH);
    }

    public void handleSettingsTabRefresh() {
        _splash.setSelected(Configuration.getBoolean(Argo.KEY_SPLASH, true));
        _preload.setSelected(Configuration.getBoolean(Argo.KEY_PRELOAD, true));
        _edem.setSelected(Configuration.getBoolean(Argo.KEY_EDEM, true));
        _profile.setSelected(Configuration.getBoolean(Argo.KEY_PROFILE, false));
    }

    public void handleSettingsTabSave() {
        Configuration.setBoolean(Argo.KEY_SPLASH, _splash.isSelected());
        Configuration.setBoolean(Argo.KEY_PRELOAD, _preload.isSelected());
        Configuration.setBoolean(Argo.KEY_EDEM, _edem.isSelected());
        Configuration.setBoolean(Argo.KEY_PROFILE, _profile.isSelected());
    }

    public void handleSettingsTabCancel() {
        handleSettingsTabRefresh();
    }
}

class UserTab extends SettingsTabHelper
implements SettingsTabPanel {

    JTextField _fullname = null;
    JTextField _email = null;
    // JTextField _smtp = new JTextField();

    UserTab(String bundle) {
        super(bundle);
        setLayout(new BorderLayout());
	JPanel top = new JPanel();
    	top.setLayout(new GridBagLayout()); 

	GridBagConstraints labelConstraints = new GridBagConstraints();
	labelConstraints.anchor = GridBagConstraints.WEST;
	labelConstraints.gridy = 0;
	labelConstraints.gridx = 0;
	// labelConstraints.ipadx = 16;
	// labelConstraints.ipady = 4;
	labelConstraints.gridwidth = 1;
	labelConstraints.gridheight = 1;
	labelConstraints.insets = new Insets(2, 20, 2, 4);
	// labelConstraints.weightx = 0.3;

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
	top.add(createLabel("label_user"), labelConstraints);
        _fullname = createTextField();
	top.add(_fullname, fieldConstraints);

	labelConstraints.gridy = 1;
	fieldConstraints.gridy = 1;
 	top.add(createLabel("label_email"), labelConstraints);
        _email = createTextField();
	top.add(_email, fieldConstraints);

	// labelConstraints.gridy = 2;
	// fieldConstraints.gridy = 2;
  	// top.add(createLabel("label_smtp"), labelConstraints);
	// top.add(_smtp, fieldConstraints);

	add(top, BorderLayout.NORTH);
    }

    public void handleSettingsTabRefresh() {
        _fullname.setText(Configuration.getString(Argo.KEY_USER_FULLNAME));
        _email.setText(Configuration.getString(Argo.KEY_USER_EMAIL));
    }

    public void handleSettingsTabSave() {
        Configuration.setString(Argo.KEY_USER_FULLNAME, _fullname.getText());
        Configuration.setString(Argo.KEY_USER_EMAIL, _email.getText());
        // Configuration.setString(Argo.KEY_USER_NAME);
    }

    public void handleSettingsTabCancel() {
	handleSettingsTabRefresh();
    }
}

