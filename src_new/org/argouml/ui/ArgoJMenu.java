// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import org.argouml.uml.ui.*;

/**
 * An extension of the standard swing JMenu class which provides
 * additional Argo support.
 *
 */
public class ArgoJMenu extends JMenu {

    /**
     * Constructs a new ArgoJMenu with the supplied string as its text.
     *
     * @param     s      the text for the menu label
     */
    public ArgoJMenu(String s) { super(s); }

    /**
     * Creates a new checkbox menu item attached to the specified
     * Action object and appends it to the end of this menu.
     *
     * @param     a      the Action for the checkbox menu item to be added
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
				  (selected == null || selected.booleanValue()));
	// End of block
	mi.setHorizontalTextPosition(JButton.RIGHT);
	mi.setVerticalTextPosition(JButton.CENTER);
	mi.setEnabled(a.isEnabled());
	mi.addActionListener(a);
	add(mi);
	a.addPropertyChangeListener(createActionChangeListener(mi));
	return mi;
    }

} /* end class ArgoJMenu */
