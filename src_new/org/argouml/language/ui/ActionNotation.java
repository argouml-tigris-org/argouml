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

package org.argouml.language.ui;

import org.argouml.application.api.*;
import org.argouml.uml.ui.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;


/** Allows selection of a default notation.
 *
 *  @author Thierry Lach
 *  @since  ARGO0.9.4
 */
public class ActionNotation extends UMLAction
    implements MenuListener
{

    ////////////////////////////////////////////////////////////////
    // constructors

    private static final ActionNotation SINGLETON = new ActionNotation();

    private JMenu menu = null;

    /**
     * @return the singleton
     */
    public static final ActionNotation getInstance() { return SINGLETON; }

    /**
     * Constructor - adds the Notation menu.
     */
    public ActionNotation() {
        super("Notation", NO_ICON);
        menu = new JMenu("Notation");
        menu.add(this);
        menu.addMenuListener(this);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        String key = ae.getActionCommand();
        ArrayList list = Notation.getAvailableNotations();
        ListIterator iterator = list.listIterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof NotationName) {
                NotationName nn = (NotationName) o;
                if (key.equals(nn.getTitle())) {
                    Notation.setDefaultNotation(nn);
                    break;
                }
            }
        }
    }

    /**
     * @return the menu for the natation
     */
    public JMenu getMenu() { return menu; }

    /**
     * @see javax.swing.event.MenuListener#menuSelected(javax.swing.event.MenuEvent)
     */
    public void menuSelected(MenuEvent me) {
        NotationName dflt = Notation.getDefaultNotation();
        menu.removeAll();
        ArrayList list = Notation.getAvailableNotations();
        ListIterator iterator = list.listIterator();
        ButtonGroup b = new ButtonGroup();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof NotationName) {
                NotationName nn = (NotationName) o;
                JRadioButtonMenuItem mi =
                    new JRadioButtonMenuItem(nn.getTitle());
                if (nn.getIcon() != null) {
                    mi.setIcon(nn.getIcon());
                }
                mi.addActionListener(this);
                b.add(mi);
                mi.setSelected(dflt.equals(nn));
                menu.add(mi);
            }
        }
    }

    /**
     * @see javax.swing.event.MenuListener#menuDeselected(javax.swing.event.MenuEvent)
     */
    public void menuDeselected(MenuEvent me) { }

    /**
     * @see javax.swing.event.MenuListener#menuCanceled(javax.swing.event.MenuEvent)
     */
    public void menuCanceled(MenuEvent me) { }

}
