/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007-2009 The Regents of the University of California. All
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
package org.argouml.uml.diagram.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectSettings;
import org.argouml.swingext.JLinkButton;
import org.argouml.ui.ActionProjectSettings;
import org.argouml.ui.ArgoJFontChooser;
import org.argouml.ui.GUIProjectSettingsTabInterface;
import org.argouml.ui.ShadowComboBox;
import org.argouml.uml.diagram.DiagramAppearance;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.util.ArgoFrame;
import org.tigris.gef.undo.Memento;

/**
 * Settings tab panel for handling diagram appearance settings.
 * <p>
 * It supports different scopes: application and project. The former is stored
 * in the properties file in the user-directory, the latter in the project file
 * (.zargo,...).
 * <p>
 * This class is written in a way that supports adding more scopes easily.
 *
 * @author Aleksandar
 */
public class SettingsTabDiagramAppearance extends JPanel implements
        GUIProjectSettingsTabInterface {

    private Project p;
    
    private JButton jbtnDiagramFont;
    private JCheckBox showBoldNames;
    private JCheckBox hideBidirectionalArrows;
    private ShadowComboBox defaultShadowWidth;

    private String selectedDiagramFontName;

    private int selectedDiagramFontSize;

    private int scope;

    private JLabel jlblDiagramFont = null;

    /**
     * The constructor. We currently support 2 scopes, but this class is written
     * in a way to easily extend that.
     *
     * @param settingsScope the scope of the settings
     */
    public SettingsTabDiagramAppearance(int settingsScope) {
        super();
        scope = settingsScope;
        initialize();
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        
        this.setLayout(new BorderLayout());
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        
        if (scope == Argo.SCOPE_APPLICATION) {
            JPanel warning = new JPanel();
            warning.setLayout(new BoxLayout(warning, BoxLayout.PAGE_AXIS));
            JLabel warningLabel = new JLabel(
                Translator.localize("label.warning"));
            warningLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            warning.add(warningLabel);

            JLinkButton projectSettings = new JLinkButton();
            projectSettings.setAction(new ActionProjectSettings());
            projectSettings.setText(
                Translator.localize("button.project-settings"));
            projectSettings.setIcon(null);
            projectSettings.setAlignmentX(Component.RIGHT_ALIGNMENT);
            warning.add(projectSettings);
            
            top.add(warning, BorderLayout.NORTH);
        }
        
        JPanel settings = new JPanel();
        settings.setLayout(new GridBagLayout());

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

        showBoldNames = createCheckBox("label.show-bold-names");
        settings.add(showBoldNames, constraints);

        hideBidirectionalArrows = 
            createCheckBox("label.hide-bidirectional-arrows");
        settings.add(hideBidirectionalArrows, constraints);        

        constraints.insets = new Insets(5, 30, 0, 4);
        JPanel defaultShadowWidthPanel = new JPanel(new FlowLayout(
            FlowLayout.LEFT, 5, 0));
        JLabel defaultShadowWidthLabel = createLabel(
            "label.default-shadow-width");
        defaultShadowWidth = new ShadowComboBox();
        defaultShadowWidthLabel.setLabelFor(defaultShadowWidth);
        defaultShadowWidthPanel.add(defaultShadowWidthLabel);
        defaultShadowWidthPanel.add(defaultShadowWidth);
        settings.add(defaultShadowWidthPanel, constraints);

        jlblDiagramFont = new JLabel();
        jlblDiagramFont.setText(Translator
                .localize("label.diagramappearance.diagramfont"));       
        JPanel fontButtonPanel = new JPanel(new FlowLayout(
                FlowLayout.LEFT, 5, 0));
        JButton fontButton = getJbtnDiagramFont();
        jlblDiagramFont.setLabelFor(fontButton);
        fontButtonPanel.add(jlblDiagramFont);
        fontButtonPanel.add(fontButton);
        settings.add(fontButtonPanel, constraints);

        top.add(settings, BorderLayout.CENTER);
        
        this.add(top, BorderLayout.NORTH);
        this.setSize(new Dimension(296, 169));

    }

    private JButton getJbtnDiagramFont() {
        if (jbtnDiagramFont == null) {
            jbtnDiagramFont = new JButton(
                    Translator.localize("label.diagramappearance.changefont"));

            jbtnDiagramFont.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ArgoJFontChooser jFontChooser = new ArgoJFontChooser(
                            ArgoFrame.getFrame(), jbtnDiagramFont,
                            selectedDiagramFontName, selectedDiagramFontSize);
                    jFontChooser.setVisible(true);

                    if (jFontChooser.isOk()) {
                        selectedDiagramFontName = jFontChooser.getResultName();
                        selectedDiagramFontSize = jFontChooser.getResultSize();
                    }
                }
            });

        }
        return jbtnDiagramFont;
    }

    /*
     * @see org.argouml.ui.GUISettingsTabInterface#handleSettingsTabRefresh()
     */
    public void handleSettingsTabRefresh() {
        if (scope == Argo.SCOPE_APPLICATION) {
            showBoldNames.setSelected(getBoolean(
                    DiagramAppearance.KEY_SHOW_BOLD_NAMES));
            selectedDiagramFontName = DiagramAppearance.getInstance()
                    .getConfiguredFontName();
            selectedDiagramFontSize = Configuration
                    .getInteger(DiagramAppearance.KEY_FONT_SIZE);
            /*
             * The next one defaults to TRUE, despite that this is
             * NOT compatible with older ArgoUML versions
             * (before 0.28?) that did
             * not have this setting - see issue 535
             */
            hideBidirectionalArrows.setSelected(Configuration.getBoolean(
                    DiagramAppearance.KEY_HIDE_BIDIRECTIONAL_ARROWS, true));
            defaultShadowWidth.setSelectedIndex(Configuration.getInteger(
                    DiagramAppearance.KEY_DEFAULT_SHADOW_WIDTH, 1));
        }
        if (scope == Argo.SCOPE_PROJECT) {
            assert p != null;
            ProjectSettings ps = p.getProjectSettings();
            DiagramSettings ds = ps.getDefaultDiagramSettings();

            showBoldNames.setSelected(ds.isShowBoldNames());
            selectedDiagramFontName = ds.getFontName();
            selectedDiagramFontSize = ds.getFontSize();
            hideBidirectionalArrows.setSelected(!
                    ds.isShowBidirectionalArrows());
            defaultShadowWidth.setSelectedIndex(
                    ds.getDefaultShadowWidth());
        }
    }

    /**
     * Get a boolean from the configuration.
     *
     * @param key a notation key.
     * @return a boolean
     */
    protected static boolean getBoolean(ConfigurationKey key) {
        return Configuration.getBoolean(key, false);
    }

    /*
     * @see org.argouml.ui.GUISettingsTabInterface#handleSettingsTabSave()
     */
    public void handleSettingsTabSave() {
        if (scope == Argo.SCOPE_APPLICATION) {
            Configuration.setBoolean(DiagramAppearance.KEY_SHOW_BOLD_NAMES,
                    showBoldNames.isSelected());
            Configuration.setString(DiagramAppearance.KEY_FONT_NAME,
                    selectedDiagramFontName);
            Configuration.setInteger(DiagramAppearance.KEY_FONT_SIZE,
                    selectedDiagramFontSize);
            Configuration.setBoolean(
                    DiagramAppearance.KEY_HIDE_BIDIRECTIONAL_ARROWS,
                    hideBidirectionalArrows.isSelected());            
            Configuration.setInteger(DiagramAppearance.KEY_DEFAULT_SHADOW_WIDTH,
                    defaultShadowWidth.getSelectedIndex());
        }
        if (scope == Argo.SCOPE_PROJECT) {
            assert p != null;
            ProjectSettings ps = p.getProjectSettings();
            final DiagramSettings ds = ps.getDefaultDiagramSettings();

            ds.setShowBoldNames(showBoldNames.isSelected());
            ds.setFontName(selectedDiagramFontName);
            ds.setFontSize(selectedDiagramFontSize);
            ds.setDefaultShadowWidth(defaultShadowWidth.getSelectedIndex());
            ds.setShowBidirectionalArrows(
                    !hideBidirectionalArrows.isSelected());

            /* Update the diagram, and other users of these settings: */
            Memento memento = new Memento() {
                public void redo() {
                    ds.notifyOfChangedSettings();
                }

                public void undo() {
                    ds.notifyOfChangedSettings();
                }
            };
            // TODO: Undo should be managed externally or we should be given 
            // an Undo manager to use (the project's) rather than using a 
            // global one
//            if (DiagramUndoManager.getInstance().isGenerateMementos()) {
//                DiagramUndoManager.getInstance().addMemento(memento);
//            }
            memento.redo();
        }
    }

    /*
     * @see org.argouml.ui.GUISettingsTabInterface#handleSettingsTabCancel()
     */
    public void handleSettingsTabCancel() {
        handleSettingsTabRefresh();
    }

    /*
     * @see org.argouml.ui.GUISettingsTabInterface#handleResetToDefault()
     */
    public void handleResetToDefault() {
        if (scope == Argo.SCOPE_PROJECT) {
            showBoldNames.setSelected(getBoolean(
                    DiagramAppearance.KEY_SHOW_BOLD_NAMES));
            selectedDiagramFontName = DiagramAppearance.getInstance()
                    .getConfiguredFontName();
            selectedDiagramFontSize = Configuration
                    .getInteger(DiagramAppearance.KEY_FONT_SIZE);
            hideBidirectionalArrows.setSelected(Configuration.getBoolean(
                    DiagramAppearance.KEY_HIDE_BIDIRECTIONAL_ARROWS, true));
            defaultShadowWidth.setSelectedIndex(Configuration.getInteger(
                    DiagramAppearance.KEY_DEFAULT_SHADOW_WIDTH, 1)); 
        }
    }

    /*
     * @see org.argouml.ui.GUISettingsTabInterface#getTabKey()
     */
    public String getTabKey() {
        return "tab.diagramappearance";
    }

    /*
     * @see org.argouml.ui.GUISettingsTabInterface#getTabPanel()
     */
    public JPanel getTabPanel() {
        return this;
    }

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

    /**
     * Create a localized JButton.
     *
     * @param key the key for the string to be localized
     * @return a new checkbox with localized text
     */
    protected JButton createButton(String key) {
        return new JButton(Translator.localize(key));
    }

    /*
     * @see javax.swing.JComponent#setVisible(boolean)
     */
    public void setVisible(boolean arg0) {
        super.setVisible(arg0);
        if (arg0) {
            handleSettingsTabRefresh();
        }
    }

    public void setProject(Project project) {
        assert project != null;
        p = project;
    }
} // @jve:decl-index=0:visual-constraint="10,10"
