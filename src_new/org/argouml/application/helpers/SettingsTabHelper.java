// $Id$
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

package org.argouml.application.helpers;
import org.argouml.application.api.*;
import org.argouml.ui.*;
import org.argouml.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import java.util.*;
import org.tigris.gef.util.*;

/** Helper object for Settings Tabs
 *
 *  @author Thierry Lach
 *  @since  0.9.4
 */

public abstract class SettingsTabHelper extends JPanel
    implements SettingsTabPanel, PluggableSettingsTab 
{

    /** Default localization key for Settings
     */
    public static final String SETTINGS_BUNDLE = "CoreSettings";

    /** String naming the resource bundle to use for localization.
     */
    protected String _bundle = "";

    /** Constructor gets passed the localizer bundle name and
     *  a property change listener.
     */
    public SettingsTabHelper() {
        _bundle = getTabResourceBundleKey();
    }

    /** Helper for localization, localizes using the bundle passed in
     *  the constructor.
     */
    public String localize(String key) {
        return Argo.localize(_bundle, key);
    }

    /** Create a localized JLabel.
     */
    protected JLabel createLabel(String key) {
    	return new JLabel(localize(key));
    }

    /** Create a localized JCheckBox.
     */
    protected JCheckBox createCheckBox(String key) {
    	JCheckBox j = new JCheckBox(localize(key));
	return j;
    }

    /** Create a localized JRadioButton.
     */
    protected JRadioButton createRadioButton(ButtonGroup bg, String key, 
					     boolean selected) {
	JRadioButton j = new JRadioButton(localize(key), selected);
	bg.add(j);
	return j;
    }

    /** Create a JTextField.
     */
    protected JTextField createTextField() {
    	JTextField j = new JTextField();
	return j;
    }

    public void setModuleEnabled(boolean v) { }
    public boolean initializeModule() { return true; }
    public boolean inContext(Object[] o) { return true; }
    public boolean isModuleEnabled() { return true; }
    public Vector getModulePopUpActions(Vector v, Object o) { return null; }
    public boolean shutdownModule() { return true; }
    public SettingsTabPanel getSettingsTabPanel() { return this; }
    public JPanel getTabPanel() { return this; }

    public String getTabResourceBundleKey() {
        return SETTINGS_BUNDLE;
    }
}

