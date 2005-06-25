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
import javax.swing.BoxLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;

//import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
//import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
//import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.argouml.application.ArgoVersion;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;
import org.argouml.language.cpp.generator.GeneratorCpp;


/**
 * Settings tab for the C++ code generator.
 */
public class SettingsTabCpp extends SettingsTabHelper
    implements SettingsTabPanel
{
    private static final Logger LOG = Logger.getLogger(GeneratorCpp.class);

    private JSpinner indent;
    private JCheckBox verboseDocs;
    private JCheckBox lfBeforeCurly;

    /**
     * Creates the widgets in a JScrollPanel (there may be many options),
     * but do not initialize them with the values (this is
     * handleSettingsTabRefresh() duty).
     */
    public SettingsTabCpp() {
        super();
        LOG.debug("SettingsTabCpp being created...");

        setLayout(new BorderLayout());

        JPanel opts = new JPanel();
        opts.setLayout(new BoxLayout(opts, BoxLayout.Y_AXIS));
        
	// adds option widgets
        JPanel indentPanel = new JPanel(new FlowLayout());
        
	JLabel label = createLabel("cpp.indent");
	// The actual value is loaded in handleSettingsTabRefresh()
        Integer spinVal = new Integer(4); 
        Integer spinMin = new Integer(0);
        Integer spinStep = new Integer(1);
        indent = new JSpinner(
                new SpinnerNumberModel(spinVal, spinMin, null, spinStep));
        // let the minimum size be a little wider than the default
	// FIXME: this doesn't seem to do anything at all :-(
        Dimension minSize = indent.getMinimumSize();
        minSize.setSize(minSize.getWidth() + 50, minSize.getHeight());
        LOG.debug("setting spinner minimum size to " + minSize);
        indent.setMinimumSize(minSize);
        
	label.setLabelFor(indent);
	indentPanel.add(label);
	indentPanel.add(indent);
        // force the minimum size
        indentPanel.setMaximumSize(indentPanel.getMinimumSize());
	opts.add(indentPanel);

	verboseDocs = createCheckBox("cpp.verbose-docs");
	opts.add(verboseDocs);

	lfBeforeCurly = createCheckBox("cpp.lf-before-curly");
	opts.add(lfBeforeCurly);

	opts.add(new JLabel("TODO: add more options!!!"));

	// ... more?

        // put all the options in a scroll pane
        //JScrollPane sp = new JScrollPane();
        //sp.add(opts);
	//add(sp, BorderLayout.CENTER);
        add(opts, BorderLayout.CENTER);

	// TODO: add a text field to show a preview of what the generated
	// code will look like (like in eclipse)
	
        LOG.debug("SettingsTabCpp created!");
    }

    /*** implements SettingsTabPanel ***/

    /**
     * Save any fields changed.
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabSave
     */
    public void handleSettingsTabSave() {
        GeneratorCpp cpp = GeneratorCpp.getInstance();
        cpp.setLfBeforeCurly(lfBeforeCurly.isSelected());
        cpp.setVerboseDocs(verboseDocs.isSelected());
        cpp.setIndent(((Integer)indent.getValue()).intValue());
        // TODO: save to disk!
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
