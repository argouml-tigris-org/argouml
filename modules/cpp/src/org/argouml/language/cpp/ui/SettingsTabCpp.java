// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.language.cpp.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.Box;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.apache.log4j.Logger;
import org.argouml.application.ArgoVersion;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;
import org.argouml.language.cpp.generator.GeneratorCpp;
import org.argouml.language.cpp.generator.Section;


/**
 * Settings tab for the C++ code generator.
 */
public class SettingsTabCpp extends SettingsTabHelper
    implements SettingsTabPanel
{
    private static final Logger LOG = Logger.getLogger(SettingsTabCpp.class);

    private JSpinner indent;
    private JCheckBox verboseDocs;
    private JCheckBox lfBeforeCurly;
    private JComboBox useSect;

    /**
     * Creates the widgets but do not initialize them with the values
     * (this is handleSettingsTabRefresh() duty).
     */
    public SettingsTabCpp() {
        super();
        LOG.debug("SettingsTabCpp being created...");

        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        top.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(0, 30, 0, 4);

        // adds indent width spinner
        JLabel label = createLabel("cpp.indent");
        // The actual value is loaded in handleSettingsTabRefresh()
        Integer spinVal = new Integer(4); 
        Integer spinMin = new Integer(0);
        Integer spinStep = new Integer(1);
        indent = new JSpinner(
                new SpinnerNumberModel(spinVal, spinMin, null, spinStep));
        label.setLabelFor(indent);
        
        JPanel indentPanel = new JPanel();
        indentPanel.setLayout(new BoxLayout(indentPanel, BoxLayout.LINE_AXIS));
        indentPanel.add(label);
        indentPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        indentPanel.add(indent);
        indentPanel.add(Box.createHorizontalGlue());
        top.add(indentPanel, constraints);
        
        verboseDocs = createCheckBox("cpp.verbose-docs");
        top.add(verboseDocs, constraints);

        lfBeforeCurly = createCheckBox("cpp.lf-before-curly");
        top.add(lfBeforeCurly, constraints);
        
        // adds section combobox
        String[] sectOpts = new String[3];
        sectOpts[Section.SECT_NONE] = localize("cpp.sections.none"); 
        sectOpts[Section.SECT_NORMAL] = localize("cpp.sections.normal"); 
        sectOpts[Section.SECT_BRIEF] = localize("cpp.sections.brief"); 
        useSect = new JComboBox(sectOpts);
        label = createLabel("cpp.sections");
        label.setLabelFor(useSect);
        JPanel sectPanel =
            new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        sectPanel.add(label);
        sectPanel.add(useSect);
        top.add(sectPanel, constraints);
        
	// TODO: add more options

        add(top, BorderLayout.NORTH);

        LOG.debug("SettingsTabCpp created!");
    }
    
    /*** implements SettingsTabPanel ***/

    /**
     * Save any fields changed.
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabSave
     */
    public void handleSettingsTabSave() {
        GeneratorCpp cpp = GeneratorCpp.getInstance();
        int indWidth = ((Integer) indent.getValue()).intValue();
        cpp.setIndent(indWidth);
        cpp.setLfBeforeCurly(lfBeforeCurly.isSelected());
        cpp.setVerboseDocs(verboseDocs.isSelected());
        cpp.setUseSect(useSect.getSelectedIndex());
    }

    /**
     * Cancel any changes.
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabCancel
     */
    public void handleSettingsTabCancel() {

    }

    /**
     * Load or reload field settings.
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabRefresh
     */
    public void handleSettingsTabRefresh() {
        GeneratorCpp cpp = GeneratorCpp.getInstance();
        lfBeforeCurly.setSelected(cpp.isLfBeforeCurly());
        verboseDocs.setSelected(cpp.isVerboseDocs());
        indent.setValue(new Integer(cpp.getIndent()));
        useSect.setSelectedIndex(cpp.getUseSect());
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#getTabKey
     */
    public String getTabKey() { return "cpp.tabname"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() { return "SettingsTabCpp"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() { return "C++ Settings"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() { return ArgoVersion.getVersion(); }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() { return "Daniele Tamino"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() { return "cpp.module"; }
}
