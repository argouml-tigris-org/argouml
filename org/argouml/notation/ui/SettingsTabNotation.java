// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.notation.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.argouml.application.api.Configuration;
import org.argouml.application.api.ConfigurationKey;
import org.argouml.i18n.Translator;
import org.argouml.notation.Notation;
import org.argouml.ui.GUI;
import org.argouml.ui.GUISettingsTabInterface;
import org.argouml.ui.ShadowComboBox;

/**
 * Settings tab panel for handling Notation settings.
 *
 * @author Thierry Lach
 * @since  0.9.4
 */
public class SettingsTabNotation     
    extends JPanel
    implements GUISettingsTabInterface {

    private JCheckBox allowNotations;
    private JCheckBox useGuillemots;
    private JCheckBox showVisibility;
    private JCheckBox showMultiplicity;
    private JCheckBox showInitialValue;
    private JCheckBox showProperties;
    private JCheckBox showStereotypes;
    private JCheckBox showTypes;
    private ShadowComboBox defaultShadowWidth;

    /**
     * The constructor.
     */
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

        constraints.gridy = GridBagConstraints.RELATIVE;
        allowNotations = createCheckBox("label.uml-notation-only");
        top.add(allowNotations, constraints);

        useGuillemots = createCheckBox("label.use-guillemots");
        top.add(useGuillemots, constraints);

        // 2002-07-31
        // Jaap Branderhorst
        // from here made visibility etc. configurable

        showVisibility = createCheckBox("label.show-visibility");
        top.add(showVisibility, constraints);

        showMultiplicity = createCheckBox("label.show-multiplicity");
        top.add(showMultiplicity, constraints);

        showInitialValue = createCheckBox("label.show-initialvalue");
        top.add(showInitialValue, constraints);

        showProperties = createCheckBox("label.show-properties");
        top.add(showProperties, constraints);

        showTypes = createCheckBox("label.show-types");
        top.add(showTypes, constraints);

        showStereotypes = createCheckBox("label.show-stereotypes");
        top.add(showStereotypes, constraints);

        constraints.insets = new Insets(5, 30, 0, 4);
        JPanel defaultShadowWidthPanel =
            new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel defaultShadowWidthLabel =
            createLabel("label.default-shadow-width");
        defaultShadowWidth = new ShadowComboBox();
        defaultShadowWidthLabel.setLabelFor(defaultShadowWidth);
        defaultShadowWidthPanel.add(defaultShadowWidthLabel);
        defaultShadowWidthPanel.add(defaultShadowWidth);
        top.add(defaultShadowWidthPanel, constraints);

        add(top, BorderLayout.NORTH);

        GUI.getInstance().addSettingsTab(this);
    }

    /**
     * @see org.argouml.ui.GUISettingsTabInterface#handleSettingsTabRefresh()
     */
    public void handleSettingsTabRefresh() {
        useGuillemots.setSelected(Notation.getUseGuillemots());
        allowNotations.setSelected(getBoolean(Notation.KEY_UML_NOTATION_ONLY));
        showVisibility.setSelected(getBoolean(Notation.KEY_SHOW_VISIBILITY));
        showInitialValue.setSelected(
            getBoolean(Notation.KEY_SHOW_INITIAL_VALUE));
        showProperties.setSelected(getBoolean(Notation.KEY_SHOW_PROPERTIES));
        /*
         * The next one defaults to TRUE, to stay compatible with older
         * ArgoUML versions that did not have this setting:
         */
        showTypes.setSelected(Configuration.getBoolean(
                Notation.KEY_SHOW_TYPES, true));
        showMultiplicity.setSelected(
            getBoolean(Notation.KEY_SHOW_MULTIPLICITY));
        showStereotypes.setSelected(getBoolean(Notation.KEY_SHOW_STEREOTYPES));
        defaultShadowWidth.setSelectedIndex(
                Configuration.getInteger(Notation.KEY_DEFAULT_SHADOW_WIDTH, 1));
    }

    /**
     * Get a boolean from the configuration.
     *
     * @param key a notation key.
     * @return a boolean
     */
    private static boolean getBoolean(ConfigurationKey key) {
        return Configuration.getBoolean(key, false);
    }

    /**
     * @see org.argouml.ui.GUISettingsTabInterface#handleSettingsTabSave()
     */
    public void handleSettingsTabSave() {
        Notation.setUseGuillemots(useGuillemots.isSelected());
        Configuration.setBoolean(Notation.KEY_UML_NOTATION_ONLY,
                 allowNotations.isSelected());
        Configuration.setBoolean(Notation.KEY_SHOW_VISIBILITY,
                 showVisibility.isSelected());
        Configuration.setBoolean(Notation.KEY_SHOW_MULTIPLICITY,
                 showMultiplicity.isSelected());
        Configuration.setBoolean(Notation.KEY_SHOW_PROPERTIES,
                 showProperties.isSelected());
        Configuration.setBoolean(Notation.KEY_SHOW_TYPES,
                showTypes.isSelected());
        Configuration.setBoolean(Notation.KEY_SHOW_INITIAL_VALUE,
                 showInitialValue.isSelected());
        Configuration.setBoolean(Notation.KEY_SHOW_STEREOTYPES,
                 showStereotypes.isSelected());
        Configuration.setInteger(Notation.KEY_DEFAULT_SHADOW_WIDTH,
                defaultShadowWidth.getSelectedIndex());
    }

    /**
     * @see org.argouml.ui.GUISettingsTabInterface#handleSettingsTabCancel()
     */
    public void handleSettingsTabCancel() {
        handleSettingsTabRefresh();
    }

    /**
     * @see org.argouml.ui.GUISettingsTabInterface#getTabKey()
     */
    public String getTabKey() { return "tab.notation"; }

    /**
     * @see org.argouml.ui.GUISettingsTabInterface#getTabPanel()
     */
    public JPanel getTabPanel() { return this; }

    /**
     * Create a localized JCheckBox.
     *
     * @param key the key for the string to be localized
     * @return a new checkbox with localized text
     */
    protected JCheckBox createCheckBox(String key) {
        JCheckBox j = new JCheckBox(Translator.localize(key));
        return j;
    }

    /**
     * Create a localized JLabel.
     *
     * @param key the key of the text for the label
     * @return a new label with a localized text for the given key
     */
    protected JLabel createLabel(String key) {
        return new JLabel(Translator.localize(key));
    }
}
