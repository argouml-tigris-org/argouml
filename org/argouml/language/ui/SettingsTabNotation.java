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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.argouml.application.ArgoVersion;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.Notation;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;
import org.argouml.swingext.LabelledLayout;

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

	GridBagConstraints constraints = new GridBagConstraints();
	constraints.anchor = GridBagConstraints.WEST;
    constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.gridy = 0;
	constraints.gridx = 0;
	constraints.gridwidth = 1;
	constraints.gridheight = 1;
    constraints.weightx = 1.0;
	constraints.insets = new Insets(0, 30, 0, 4);

	constraints.gridy = 0;
    _allowNotations = createCheckBox("label.uml-notation-only");
	top.add(_allowNotations, constraints);

	constraints.gridy = 1;
    _useGuillemots = createCheckBox("label.use-guillemots");
	top.add(_useGuillemots, constraints);
    
    // 2002-07-31
    // Jaap Branderhorst
    // from here made visibility etc. configurable
    
    constraints.gridy = 2;
    _showVisibility = createCheckBox("label.show-visibility");
    top.add(_showVisibility, constraints);
    
    constraints.gridy = 3;
    _showMultiplicity = createCheckBox("label.show-multiplicity");
    top.add(_showMultiplicity, constraints);

    constraints.gridy = 4;
    _showInitialValue = createCheckBox("label.show-initialvalue");
    top.add(_showInitialValue, constraints);
    
    constraints.gridy = 5;
    _showProperties = createCheckBox("label.show-properties");
    top.add(_showProperties, constraints);
	
    constraints.gridy = 6;
    _showStereotypes = createCheckBox("label.show-stereotypes");
    top.add(_showStereotypes, constraints);

    constraints.gridy = 7;
    constraints.insets = new Insets(5, 30, 0, 4);
    JPanel defaultShadowWidthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    JLabel defaultShadowWidthLabel = createLabel("label.default-shadow-width");
    _defaultShadowWidth = new JTextField(5);
    defaultShadowWidthLabel.setLabelFor(_defaultShadowWidth);
    defaultShadowWidthPanel.add(defaultShadowWidthLabel);
    defaultShadowWidthPanel.add(_defaultShadowWidth);
    top.add(defaultShadowWidthPanel, constraints);
	
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



