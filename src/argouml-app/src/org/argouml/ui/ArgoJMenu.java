/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

package org.argouml.ui;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingConstants;

import org.argouml.i18n.Translator;

/**
 * An extension of the standard swing JMenu class which provides
 * additional Argo support.
 *
 */
public class ArgoJMenu extends JMenu {

    /**
     * Constructs a new ArgoJMenu with the key to localize.
     *
     * @param key The key to localize.
     */
    public ArgoJMenu(String key) {
        super();
        localize(this, key);
    }

    /**
     * Sets a menu item's text and mnemonic values using the specified resource
     * key.
     *
     * @param   menuItem    the menu or menu item to localize
     * @param   key         the resource string to find
     */
    public static final void localize(JMenuItem menuItem, String key) {
        menuItem.setText(Translator.localize(key));

        String localMnemonic = Translator.localize(key + ".mnemonic");
        if (localMnemonic != null && localMnemonic.length() == 1) {
            menuItem.setMnemonic(localMnemonic.charAt(0));
        }
    }

    /**
     * Creates a new checkbox menu item attached to the specified
     * action object and appends it to the end of this menu.
     *
     * @param     a     the Action for the checkbox menu item to be added
     * @return          the new checkbox menu item
     */
    public JCheckBoxMenuItem addCheckItem(Action a) {
	String name = (String) a.getValue(Action.NAME);
	Icon icon = (Icon) a.getValue(Action.SMALL_ICON);
	// Block added by BobTarling 8-Jan-2002 Set the checkbox on or
	// off according to the SELECTED value of the action.  If no
	// SELECTED value is found then this defaults to true in order
	// to remain compatible with previous versions of this code.
	Boolean selected = (Boolean) a.getValue("SELECTED");
	JCheckBoxMenuItem mi =
	    new JCheckBoxMenuItem(name, icon,
				  (selected == null
				   || selected.booleanValue()));
	// End of block
	mi.setHorizontalTextPosition(SwingConstants.RIGHT);
	mi.setVerticalTextPosition(SwingConstants.CENTER);
	mi.setEnabled(a.isEnabled());
	mi.addActionListener(a);
	add(mi);
	a.addPropertyChangeListener(createActionChangeListener(mi));
	return mi;
    }

    /**
     * Creates a new radiobutton menu item attached to the specified
     * action object and appends it to the end of this menu.
     *
     * @param     a     the Action for the radiobutton menu item to be added
     * @return          the new radiobutton menu item
     */
    public JRadioButtonMenuItem addRadioItem(Action a) {
        String name = (String) a.getValue(Action.NAME);
        Icon icon = (Icon) a.getValue(Action.SMALL_ICON);
        // Set the checkbox on or
        // off according to the SELECTED value of the action.  If no
        // SELECTED value is found then this defaults to true.
        Boolean selected = (Boolean) a.getValue("SELECTED");
        JRadioButtonMenuItem mi =
            new JRadioButtonMenuItem(name, icon,
                                  (selected == null
                                   || selected.booleanValue()));
        mi.setHorizontalTextPosition(SwingConstants.RIGHT);
        mi.setVerticalTextPosition(SwingConstants.CENTER);
        mi.setEnabled(a.isEnabled());
        mi.addActionListener(a);
        add(mi);
        a.addPropertyChangeListener(createActionChangeListener(mi));
        return mi;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 8318663502924796474L;
} /* end class ArgoJMenu */
