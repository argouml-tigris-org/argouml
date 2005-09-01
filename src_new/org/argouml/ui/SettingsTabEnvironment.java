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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.argouml.application.ArgoVersion;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.SaveGraphicsManager;
import org.argouml.util.SuffixFilter;
import org.tigris.swidgets.LabelledLayout;



/**
 * Settings panel for handling ArgoUML environment related settings.
 *
 * @author Thierry Lach
 * @since  0.9.4
 */
public class SettingsTabEnvironment extends SettingsTabHelper
    implements SettingsTabPanel {

    private JTextField fieldArgoRoot;
    private JTextField fieldArgoHome;
    private JTextField fieldArgoExtDir;
    private JTextField fieldJavaHome;
    private JTextField fieldUserHome;
    private JTextField fieldUserDir;
    private JTextField fieldStartupDir;
    private JComboBox fieldGraphicsFormat;
    private JComboBox fieldGraphicsResolution;
    private Collection theResolutions;

    /**
     * The constructor.
     */
    public SettingsTabEnvironment() {
        super();
        setLayout(new BorderLayout());
        int labelGap = 10;
        int componentGap = 5;
        JPanel top = new JPanel(new LabelledLayout(labelGap, componentGap));

        JLabel label = createLabel("label.default.graphics-format");
        fieldGraphicsFormat = new JComboBox();
        label.setLabelFor(fieldGraphicsFormat);
        top.add(label);
        top.add(fieldGraphicsFormat);
        
        label = createLabel("label.default.graphics-resolution");
        theResolutions = new ArrayList();
        theResolutions.add(new GResolution(1, "combobox.item.resolution-1"));
        theResolutions.add(new GResolution(2, "combobox.item.resolution-2"));
        theResolutions.add(new GResolution(4, "combobox.item.resolution-4"));
        fieldGraphicsResolution = new JComboBox(); //filled in later
        label.setLabelFor(fieldGraphicsResolution);
        top.add(label);
        top.add(fieldGraphicsResolution);

        // This string is NOT to be translated! See issue 2381.
	label = new JLabel("${argo.root}");
	fieldArgoRoot = createTextField();
	fieldArgoRoot.setEnabled(false);
        label.setLabelFor(fieldArgoRoot);
        top.add(label);
	top.add(fieldArgoRoot);

	// This string is NOT to be translated! See issue 2381.
	label = new JLabel("${argo.home}");
        fieldArgoHome = createTextField();
	fieldArgoHome.setEnabled(false);
        label.setLabelFor(fieldArgoHome);
        top.add(label);
	top.add(fieldArgoHome);

 	// This string is NOT to be translated! See issue 2381.
	label = new JLabel("${argo.ext.dir}");
        fieldArgoExtDir = createTextField();
	fieldArgoExtDir.setEnabled(false);
        label.setLabelFor(fieldArgoExtDir);
        top.add(label);
        top.add(fieldArgoExtDir);

  	// This string is NOT to be translated! See issue 2381.
	label = new JLabel("${java.home}");
        fieldJavaHome = createTextField();
	fieldJavaHome.setEnabled(false);
        label.setLabelFor(fieldJavaHome);
        top.add(label);
        top.add(fieldJavaHome);

  	// This string is NOT to be translated! See issue 2381.
	label = new JLabel("${user.home}");
        fieldUserHome = createTextField();
	fieldUserHome.setEnabled(false);
        label.setLabelFor(fieldUserHome);
        top.add(label);
        top.add(fieldUserHome);

	// This string is NOT to be translated! See issue 2381.
	label = new JLabel("${user.dir}");
        fieldUserDir = createTextField();
	fieldUserDir.setEnabled(false);
        label.setLabelFor(fieldUserDir);
        top.add(label);
        top.add(fieldUserDir);

  	label = createLabel("label.startup-directory");
        fieldStartupDir = createTextField();
	fieldStartupDir.setEnabled(false);
        label.setLabelFor(fieldStartupDir);
        top.add(label);
        top.add(fieldStartupDir);

        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	add(top, BorderLayout.NORTH);
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabRefresh()
     */
    public void handleSettingsTabRefresh() {
        fieldArgoRoot.setText(Argo.getArgoRoot());
        fieldArgoHome.setText(Argo.getArgoHome());
        fieldArgoExtDir.setText(Argo.getArgoHome() + File.separator + "ext");
        fieldJavaHome.setText(System.getProperty("java.home"));
        fieldUserHome.setText(System.getProperty("user.home"));
        fieldUserDir.setText(Configuration.getString(Argo.KEY_STARTUP_DIR,
		System.getProperty("user.dir")));
        fieldStartupDir.setText(Argo.getDirectory());
        
        fieldGraphicsFormat.removeAllItems();
        Collection c = SaveGraphicsManager.getInstance().getSettingsList();
        fieldGraphicsFormat.setModel(new DefaultComboBoxModel(c.toArray()));
        
        fieldGraphicsResolution.removeAllItems();
        fieldGraphicsResolution.setModel(new DefaultComboBoxModel(
                theResolutions.toArray()));
        int defaultResolution = Configuration.getInteger(
                SaveGraphicsManager.KEY_GRAPHICS_RESOLUTION, 1);
        Iterator i = theResolutions.iterator();
        while (i.hasNext()) {
            GResolution gr = (GResolution) i.next();
            if (defaultResolution == gr.getResolution()) {
                fieldGraphicsResolution.setSelectedItem(gr);
                break;
            }
        }
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabSave()
     */
    public void handleSettingsTabSave() {
        Configuration.setString(Argo.KEY_STARTUP_DIR, fieldUserDir.getText());
        
        GResolution r = (GResolution) fieldGraphicsResolution.getSelectedItem();
        Configuration.setInteger(SaveGraphicsManager.KEY_GRAPHICS_RESOLUTION, 
                r.getResolution());
        
        SaveGraphicsManager.getInstance().setDefaultFilter( 
                (SuffixFilter) fieldGraphicsFormat.getSelectedItem());
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabCancel()
     */
    public void handleSettingsTabCancel() {
	handleSettingsTabRefresh();
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() { return "SettingsTabEnvironment"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() {
	return "Settings Tab for Environment";
    }

    /**
     * Use of Module is curious. Does this mean the
     * author of a particular zargo?
     * This means the author of this extension to ArgoUML.
     * this information is not stored in the .argo xml
     * in zargo
     *
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() { return "ArgoUML Core"; }

    /** This should call on a global config file somewhere
     * .9.4 is the last version of argo
     *
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() { return ArgoVersion.getVersion(); }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() { return "module.settings.environment"; }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#getTabKey()
     */
    public String getTabKey() { return "tab.environment"; }
    
}


class GResolution {
    private int resolution;
    private String label;
    
    GResolution(int r, String name) {
        resolution = r;
        label = Translator.localize(name);
    }
    
    int getResolution() {
        return resolution;
    }
    
    public String toString() {
        return label;
    }
}

