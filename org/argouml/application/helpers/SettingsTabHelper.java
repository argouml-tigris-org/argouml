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

package org.argouml.application.helpers;

import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.argouml.application.api.PluggableSettingsTab;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.i18n.Translator;

/**
 * Helper object for Settings Tabs.
 * 
 * @author Thierry Lach
 * @since 0.9.4
 * @deprecated as of 0.21.3 by Linus Tolke. Replace by explicit calls to Swing
 *             and copy the {@link #getTabPanel()} into your class. Instead of
 *             extending this class, implement {@link GUISettingsTabInterface}
 *             and {@link ModuleInterface} directly and register your module
 *             with
 *             {@link GUI#addSettingsTab(org.argouml.ui.GUISettingsTabInterface)}
 *             when your module is enabled. See {@link SettingsTabCpp} for an
 *             example which has been converted.
 */
public abstract class SettingsTabHelper extends JPanel
    implements SettingsTabPanel, PluggableSettingsTab {

    /**
     * Constructor.
     */
    public SettingsTabHelper() {
    }

    /**
     * Helper for localization, localizes using the bundle passed in
     * the constructor.
     *
     * @param key the key for the string to localize
     * @return the localized string
     */
    public String localize(String key) {
        return Translator.localize(key);
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
     * Create a localized JRadioButton.
     *
     * @param bg the buttongroup
     * @param key the key for the string to be localized
     * @param selected true if selected
     * @return a new radiobutton with localized string
     */
    protected JRadioButton createRadioButton(ButtonGroup bg, String key,
					     boolean selected) {
	JRadioButton j = new JRadioButton(Translator.localize(key), selected);
	bg.add(j);
	return j;
    }

    /**
     * Create a JTextField.
     *
     * @return a new textfield
     */
    protected JTextField createTextField() {
    	JTextField j = new JTextField();
	return j;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#setModuleEnabled(boolean)
     */
    public void setModuleEnabled(boolean v) { }

    /**
     * @see org.argouml.application.api.ArgoModule#initializeModule()
     */
    public boolean initializeModule() { return true; }

    /**
     * @see org.argouml.application.api.Pluggable#inContext(java.lang.Object[])
     */
    public boolean inContext(Object[] o) { return true; }

    /**
     * @see org.argouml.application.api.ArgoModule#isModuleEnabled()
     */
    public boolean isModuleEnabled() { return true; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModulePopUpActions(
     * java.util.Vector, java.lang.Object)
     */
    public Vector getModulePopUpActions(Vector v, Object o) { return null; }

    /**
     * @see org.argouml.application.api.ArgoModule#shutdownModule()
     */
    public boolean shutdownModule() { return true; }

    /**
     * @see org.argouml.application.api.PluggableSettingsTab#getSettingsTabPanel()
     */
    public SettingsTabPanel getSettingsTabPanel() { return this; }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#getTabPanel()
     */
    public JPanel getTabPanel() { return this; }
}

