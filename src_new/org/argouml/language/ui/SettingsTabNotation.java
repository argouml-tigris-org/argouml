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

package org.argouml.language.ui;
import org.argouml.application.api.*;
import org.argouml.application.helpers.*;
import org.argouml.kernel.*;
import org.argouml.uml.ui.UMLAction;
import org.argouml.ui.*;
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
public class SettingsTabNotation extends SettingsTabHelper
implements SettingsTabPanel {

    JCheckBox _allowNotations = null;
    JCheckBox _useGuillemots = null;

    public SettingsTabNotation() {
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

	checkConstraints.gridy = 0;
	labelConstraints.gridy = 0;
	fieldConstraints.gridy = 0;
        _allowNotations = createCheckBox("label.uml-notation-only");
	top.add(_allowNotations, checkConstraints);
	top.add(new JLabel(""), labelConstraints);
	top.add(new JLabel(""), fieldConstraints);

	checkConstraints.gridy = 1;
	labelConstraints.gridy = 1;
	fieldConstraints.gridy = 1;
        _useGuillemots = createCheckBox("label.use-guillemots");
	top.add(_useGuillemots, checkConstraints);

	// checkConstraints.gridy = 1;
        // _preload = createCheckBox("label.preload");
 	// top.add(_preload, checkConstraints);

	// checkConstraints.gridy = 2;
        // _edem = createCheckBox("label.edem");
 	// top.add(_edem, checkConstraints);

	// checkConstraints.gridy = 3;
        // _profile = createCheckBox("label.profile");
 	// top.add(_profile, checkConstraints);

	add(top, BorderLayout.NORTH);
    }

    public void handleSettingsTabRefresh() {
        _useGuillemots.setSelected(Notation.getUseGuillemots());
        _allowNotations.setSelected(Configuration.getBoolean(Notation.KEY_UML_NOTATION_ONLY, false));
    }

    public void handleSettingsTabSave() {
        Notation.setUseGuillemots(_useGuillemots.isSelected());
        Configuration.setBoolean(Notation.KEY_UML_NOTATION_ONLY, _allowNotations.isSelected());
    }

    public void handleSettingsTabCancel() {
        handleSettingsTabRefresh();
    }

  public String getModuleName() { return "SettingsTabNotation"; }
  public String getModuleDescription() { return "Settings Tab for Notation"; }
  public String getModuleAuthor() { return "ArgoUML Core"; }
  public String getModuleVersion() { return "0.9.4"; }
  public String getModuleKey() { return "module.settings.notation"; }
  public String getTabKey() { return "tab.notation"; }
  public String getTabResourceBundleKey() { return "CoreSettings"; }

}

