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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.argouml.application.ArgoVersion;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;
import org.argouml.swingext.LabelledLayout;

/* This is copied from SettingsTabPreferences */
/** Action object for handling Argo settings
 *
 *  @author Linus Tolke
 *  @author Jeremy Jones
 *  @since  0.9.7
 */
public class SettingsTabFonts extends SettingsTabHelper implements SettingsTabPanel {

    private JComboBox	_lookAndFeel;
    private JComboBox	_metalTheme;
    private JLabel	_metalLabel;

    public SettingsTabFonts() {
        super();

        setLayout(new BorderLayout());

        int labelGap = 10;
        int componentGap = 10;
        JPanel top = new JPanel(new LabelledLayout(labelGap, componentGap));

        JLabel label = createLabel("label.look-and-feel");
        _lookAndFeel = new JComboBox(LookAndFeelMgr.SINGLETON.getAvailableLookAndFeelNames());
        _lookAndFeel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                setMetalThemeState();
            }
        });
        label.setLabelFor(_lookAndFeel);
        top.add(label);
        top.add(_lookAndFeel);

        _metalLabel = createLabel("label.metal-theme");

        _metalTheme = new JComboBox(LookAndFeelMgr.SINGLETON.getAvailableThemeNames());
        _metalLabel.setLabelFor(_metalTheme);
        top.add(_metalLabel);
        top.add(_metalTheme);

        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));		
        add(top, BorderLayout.CENTER);

        JLabel restart = createLabel("label.restart-application");
        restart.setHorizontalAlignment(SwingConstants.CENTER);
        restart.setVerticalAlignment(SwingConstants.CENTER);
        restart.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 2));		
        add(restart, BorderLayout.SOUTH);

        setMetalThemeState();
    }
    
    /**
     * Enables or disables the metal theme controls depending on whether
     * or not themes are supported by the selected look and feel.
    **/
    private void setMetalThemeState()
    {
        String lafName = (String) _lookAndFeel.getSelectedItem();
        boolean enabled = LookAndFeelMgr.SINGLETON.isThemeCompatibleLookAndFeel(
                        LookAndFeelMgr.SINGLETON.getLookAndFeelFromName(lafName));

        _metalLabel.setEnabled(enabled);
        _metalTheme.setEnabled(enabled);
    }

    public void handleSettingsTabRefresh() {
        String laf = LookAndFeelMgr.SINGLETON.getCurrentLookAndFeelName();
    	String theme = LookAndFeelMgr.SINGLETON.getCurrentThemeName();

        _lookAndFeel.setSelectedItem(laf);
        _metalTheme.setSelectedItem(theme);		
    }

    public void handleSettingsTabSave() {
	LookAndFeelMgr.SINGLETON.setCurrentLookAndFeel(
			LookAndFeelMgr.SINGLETON.getLookAndFeelFromName(
			(String) _lookAndFeel.getSelectedItem()));
		
	LookAndFeelMgr.SINGLETON.setCurrentTheme(
			LookAndFeelMgr.SINGLETON.getThemeFromName(
			(String) _metalTheme.getSelectedItem()));
    }

    public void handleSettingsTabCancel() { }
    public String getModuleName() { return "SettingsTabFonts"; }
    public String getModuleDescription() { return "Settings of font"; }
    public String getModuleAuthor() { return "ArgoUML Core"; }
    public String getModuleVersion() { return ArgoVersion.getVersion(); }
    public String getModuleKey() { return "module.settings.fonts"; }
    public String getTabKey() { return "tab.fonts"; }
}
