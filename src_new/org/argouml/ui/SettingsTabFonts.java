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

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.argouml.application.ArgoVersion;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;

/* This is copied from SettingsTabPreferences */
/** Action object for handling Argo settings
 *
 *  @author Linus Tolke
 *  @since  0.9.7
 */

public class SettingsTabFonts extends SettingsTabHelper implements SettingsTabPanel {

    ButtonGroup _bg = null;
    JRadioButton _normal = null;
    JRadioButton _big = null;
    JRadioButton _huge = null;

    public SettingsTabFonts() {
        super();
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

	labelConstraints.gridy = 0;
	fieldConstraints.gridy = 0;

	_bg = new ButtonGroup();
	ProjectBrowser pb = ProjectBrowser.getInstance();

	checkConstraints.gridy = 0;
	_normal = createRadioButton(_bg, "label.fonts.normal",
				    LookAndFeelMgr.SINGLETON.isCurrentTheme("normal"));
	top.add(_normal, checkConstraints);
	top.add(new JLabel(""), labelConstraints);
	top.add(new JLabel(""), fieldConstraints);

	checkConstraints.gridy = 1;
        _big = createRadioButton(_bg, "label.fonts.big",
				 LookAndFeelMgr.SINGLETON.isCurrentTheme("big"));
 	top.add(_big, checkConstraints);

	checkConstraints.gridy = 2;
        _huge = createRadioButton(_bg, "label.fonts.huge",
				  LookAndFeelMgr.SINGLETON.isCurrentTheme("huge"));
 	top.add(_huge, checkConstraints);

	add(top, BorderLayout.NORTH);
    }

    public void handleSettingsTabRefresh() {
    }

    public void handleSettingsTabSave() {
	ProjectBrowser pb = ProjectBrowser.getInstance();
	if (_normal.isSelected()) {
	    LookAndFeelMgr.SINGLETON.setCurrentTheme("normal");
	}
	else if (_big.isSelected()) {
	    LookAndFeelMgr.SINGLETON.setCurrentTheme("big");
	}
	else if (_huge.isSelected()) {
	    LookAndFeelMgr.SINGLETON.setCurrentTheme("huge");
	}
    }

    public void handleSettingsTabCancel() {}
    public String getModuleName() { return "SettingsTabFonts"; }
    public String getModuleDescription() { return "Settings of font"; }
    public String getModuleAuthor() { return "ArgoUML Core"; }
    public String getModuleVersion() { return ArgoVersion.getVersion(); }
    public String getModuleKey() { return "module.settings.fonts"; }
    public String getTabKey() { return "tab.fonts"; }
}
