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
import org.argouml.application.ArgoVersion;
import org.argouml.application.api.*;
import org.argouml.application.helpers.*;
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
 *  @author Thierry Lach
 *  @since  0.9.4
 */
public class SettingsTabUser extends SettingsTabHelper
implements SettingsTabPanel {

    /** This is where the user enters full name in settings tab
     * unknown where this information is stored
     * it is NOT stored in myproject.argo
     */    
    JTextField _fullname = null;

    /** This is where the user enters email in settings tab
     * unknown where this information is stored
     * it is NOT stored in myproject.argo
     */    
    JTextField _email = null;

    public SettingsTabUser() {
        super();
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
	top.add(createLabel("label.user"), labelConstraints);
        _fullname = createTextField();
	top.add(_fullname, fieldConstraints);

	labelConstraints.gridy = 1;
	fieldConstraints.gridy = 1;
 	top.add(createLabel("label.email"), labelConstraints);
        _email = createTextField();
	top.add(_email, fieldConstraints);

	add(top, BorderLayout.NORTH);
    }

    public void handleSettingsTabRefresh() {
        _fullname.setText(Configuration.getString(Argo.KEY_USER_FULLNAME));
        _email.setText(Configuration.getString(Argo.KEY_USER_EMAIL));
    }

    public void handleSettingsTabSave() {
        Configuration.setString(Argo.KEY_USER_FULLNAME, _fullname.getText());
        Configuration.setString(Argo.KEY_USER_EMAIL, _email.getText());
    }

    public void handleSettingsTabCancel() {
	handleSettingsTabRefresh();
    }

    public String getModuleName() { return "SettingsTabUser"; }
    public String getModuleDescription() { return "Settings Tab for User"; }
    public String getModuleAuthor() { return "ArgoUML Core"; }
    public String getModuleVersion() { return ArgoVersion.VERSION; }
    public String getModuleKey() { return "module.settings.user"; }
    public String getTabKey() { return "tab.user"; }
}



