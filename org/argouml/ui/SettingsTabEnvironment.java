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
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.argouml.application.ArgoVersion;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;



/** Action object for handling Argo settings
 *
 *  @author Thierry Lach
 *  @since  0.9.4
 */
public class SettingsTabEnvironment extends SettingsTabHelper
    implements SettingsTabPanel 
{

    public String USER_DIR;
    
    JTextField _argoRoot;
    JTextField _argoHome;
    JTextField _argoExtDir;
    JTextField _javaHome;
    JTextField _userHome;
    JTextField _userDir;
    JTextField _startupDir;
    
    JButton _userDirButton;

    public SettingsTabEnvironment() {
        super();
        setLayout(new BorderLayout());
	JPanel top = new JPanel();
    	top.setLayout(new GridBagLayout()); 

        // TODO: I18N

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
	// This string is NOT to be translated! See issue 2381.
  //top.add(createLabel("${argo.root}"), labelConstraints);
  top.add(new JLabel("${argo.root}"), labelConstraints);
      _argoRoot = createTextField();
	_argoRoot.setEnabled(false);
	top.add(_argoRoot, fieldConstraints);

	labelConstraints.gridy = 1;
	fieldConstraints.gridy = 1;
  // This string is NOT to be translated! See issue 2381.
  top.add(new JLabel("${argo.home}"), labelConstraints);
        _argoHome = createTextField();
	_argoHome.setEnabled(false);
	top.add(_argoHome, fieldConstraints);

	labelConstraints.gridy = 2;
	fieldConstraints.gridy = 2;
 	// This string is NOT to be translated! See issue 2381.
  top.add(new JLabel("${argo.ext.dir}"), labelConstraints);
        _argoExtDir = createTextField();
	_argoExtDir.setEnabled(false);
	top.add(_argoExtDir, fieldConstraints);

	labelConstraints.gridy = 3;
	fieldConstraints.gridy = 3;
  	// This string is NOT to be translated! See issue 2381.
    top.add(new JLabel("${java.home}"), labelConstraints);
        _javaHome = createTextField();
	_javaHome.setEnabled(false);
	top.add(_javaHome, fieldConstraints);

	labelConstraints.gridy = 4;
	fieldConstraints.gridy = 4;
  	// This string is NOT to be translated! See issue 2381.
    top.add(new JLabel("${user.home}"), labelConstraints);
        _userHome = createTextField();
	_userHome.setEnabled(false);
	top.add(_userHome, fieldConstraints);

	labelConstraints.gridy = 5;
	fieldConstraints.gridy = 5;
    // This string is NOT to be translated! See issue 2381.
    top.add(new JLabel("${user.dir}"), labelConstraints);
        _userDir = createTextField();
	_userDir.setEnabled(false);
	top.add(_userDir, fieldConstraints);

	labelConstraints.gridy = 6;
	fieldConstraints.gridy = 6;
  	top.add(createLabel("label.startup-directory"), labelConstraints);
        _startupDir = createTextField();
	_startupDir.setEnabled(false);
	top.add(_startupDir, fieldConstraints);

	add(top, BorderLayout.NORTH);
    }

    public void handleSettingsTabRefresh() {
        _argoRoot.setText(Argo.getArgoRoot());
        _argoHome.setText(Argo.getArgoHome());
        _argoExtDir.setText(Argo.getArgoHome() + File.separator + "ext");
        _javaHome.setText(System.getProperty("java.home"));
        _userHome.setText(System.getProperty("user.home"));
        _userDir.setText(Configuration.getString(Argo.KEY_STARTUP_DIR, System.getProperty("user.dir")));
        _startupDir.setText(Argo.getDirectory());
    }

    public void handleSettingsTabSave() {
        Configuration.setString(Argo.KEY_STARTUP_DIR, _userDir.getText());
    }

    public void handleSettingsTabCancel() {
	handleSettingsTabRefresh();
    }

    public String getModuleName() { return "SettingsTabEnvironment"; }

    public String getModuleDescription() {
	return "Settings Tab for Environment";
    }

    /** Use of Module is curious. Does this mean the
     * author of a particular zargo?
     * this information is not stored in the .argo xml
     * in zargo
     */    
    public String getModuleAuthor() { return "ArgoUML Core"; }

    /** This should call on a global config file somewhere
     * .9.4 is the last version of argo
     */    
    public String getModuleVersion() { return ArgoVersion.getVersion(); }

    public String getModuleKey() { return "module.settings.environment"; }

    public String getTabKey() { return "tab.environment"; }

}

