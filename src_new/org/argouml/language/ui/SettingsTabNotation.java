// Copyright (c) 1996-2002 The Regents of the University of California. All
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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.argouml.application.ArgoVersion;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.Notation;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;

/** Action object for handling Argo settings
 *
 *  @author Thierry Lach
 *  @since  0.9.4
 */

public class SettingsTabNotation extends SettingsTabHelper

implements SettingsTabPanel {

    JCheckBox _allowNotations = null;
    JCheckBox _useGuillemots = null;
    JCheckBox _showVisibility = null;
    JCheckBox _showMultiplicity = null;
    JCheckBox _showInitialValue = null;
    JCheckBox _showProperties = null;
    JCheckBox _showStereotypes = null;
    JTextField _defaultShadowWidth = null;

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
    
    // 2002-07-31
    // Jaap Branderhorst
    // from here made visibility etc. configurable
    top.add(new JLabel(""), labelConstraints);
    top.add(new JLabel(""), fieldConstraints);
    
    checkConstraints.gridy = 2;
    labelConstraints.gridy = 2;
    fieldConstraints.gridy = 2;
    _showVisibility = createCheckBox("label.show-visibility");
    top.add(_showVisibility, checkConstraints);
    top.add(new JLabel(""), labelConstraints);
    top.add(new JLabel(""), fieldConstraints);
    
    checkConstraints.gridy = 3;
    labelConstraints.gridy = 3;
    fieldConstraints.gridy = 3;
    _showMultiplicity = createCheckBox("label.show-multiplicity");
    top.add(_showMultiplicity, checkConstraints);
    top.add(new JLabel(""), labelConstraints);
    top.add(new JLabel(""), fieldConstraints);

    checkConstraints.gridy = 4;
    labelConstraints.gridy = 4;
    fieldConstraints.gridy = 4;
    _showInitialValue = createCheckBox("label.show-initialvalue");
    top.add(_showInitialValue, checkConstraints);
    top.add(new JLabel(""), labelConstraints);
    top.add(new JLabel(""), fieldConstraints);
    
    checkConstraints.gridy = 5;
    labelConstraints.gridy = 5;
    fieldConstraints.gridy = 5;
    _showProperties = createCheckBox("label.show-properties");
    top.add(_showProperties, checkConstraints);
    top.add(new JLabel(""), labelConstraints);
    top.add(new JLabel(""), fieldConstraints);
	
    checkConstraints.gridy = 6;
    labelConstraints.gridy = 6;
    fieldConstraints.gridy = 6;
    _showStereotypes = createCheckBox("label.show-stereotypes");
    top.add(_showStereotypes, checkConstraints);
    top.add(new JLabel(""), labelConstraints);
    top.add(new JLabel(""), fieldConstraints);

    checkConstraints.gridy = 7;
    labelConstraints.gridy = 7;
    fieldConstraints.gridy = 7;
    _defaultShadowWidth = createTextField();
    top.add(_defaultShadowWidth, checkConstraints);
    top.add(createLabel("label.default-shadow-width"), labelConstraints);
    top.add(new JLabel(""), fieldConstraints);
	
	add(top, BorderLayout.NORTH);
    }

    public void handleSettingsTabRefresh() {
        _useGuillemots.setSelected(Notation.getUseGuillemots());
        _allowNotations.setSelected(Configuration.getBoolean(Notation.KEY_UML_NOTATION_ONLY, false));
        _showVisibility.setSelected(Configuration.getBoolean(Notation.KEY_SHOW_VISIBILITY, false));
        _showInitialValue.setSelected(Configuration.getBoolean(Notation.KEY_SHOW_INITIAL_VALUE, false));
        _showProperties.setSelected(Configuration.getBoolean(Notation.KEY_SHOW_PROPERTIES, false));
        _showMultiplicity.setSelected(Configuration.getBoolean(Notation.KEY_SHOW_MULTIPLICITY, false));
        _showStereotypes.setSelected(Configuration.getBoolean(Notation.KEY_SHOW_STEREOTYPES, false));
        _defaultShadowWidth.setText(Configuration.getString(Notation.KEY_DEFAULT_SHADOW_WIDTH,"1"));
    }

    public void handleSettingsTabSave() {
        Notation.setUseGuillemots(_useGuillemots.isSelected());
        Configuration.setBoolean(Notation.KEY_UML_NOTATION_ONLY, _allowNotations.isSelected());
        Configuration.setBoolean(Notation.KEY_SHOW_VISIBILITY, _showVisibility.isSelected());
        Configuration.setBoolean(Notation.KEY_SHOW_MULTIPLICITY, _showMultiplicity.isSelected());
        Configuration.setBoolean(Notation.KEY_SHOW_PROPERTIES, _showProperties.isSelected());
        Configuration.setBoolean(Notation.KEY_SHOW_INITIAL_VALUE, _showInitialValue.isSelected());
        Configuration.setBoolean(Notation.KEY_SHOW_STEREOTYPES, _showStereotypes.isSelected());
	Configuration.setString(Notation.KEY_DEFAULT_SHADOW_WIDTH, _defaultShadowWidth.getText());
    }

    public void handleSettingsTabCancel() {
        handleSettingsTabRefresh();
    }

  public String getModuleName() { return "SettingsTabNotation"; }
  public String getModuleDescription() { return "Settings Tab for Notation"; }
  public String getModuleAuthor() { return "ArgoUML Core"; }
  public String getModuleVersion() { return ArgoVersion.getVersion(); }
  public String getModuleKey() { return "module.settings.notation"; }
  public String getTabKey() { return "tab.notation"; }
  public String getTabResourceBundleKey() { return "CoreSettings"; }



}



