// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ListIterator;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.tigris.gef.undo.UndoableAction;


/**
 * Allows selection of the notation used in the current project.
 *
 * @author Thierry Lach
 * @since  ARGO0.9.4
 */
public class ActionNotation extends UndoableAction
    implements MenuListener {

    /**
     * The popup menu with all notations. It gets filled
     * every time the menu is opened 
     * (see {@link #menuSelected(MenuEvent me)}),
     * since it depends on the current project,
     * and notation languages may be added or removed
     * by plugins.
     */
    private JMenu menu;

    /**
     * Constructor - adds the Notation menu.
     */
    public ActionNotation() {
	    super(Translator.localize("menu.notation"), 
	            null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("menu.notation"));
        menu = new JMenu(Translator.localize("menu.notation"));
        menu.add(this);
        menu.addMenuListener(this);
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
    	super.actionPerformed(ae);
        String key = ae.getActionCommand();
        List list = Notation.getAvailableNotations();
        ListIterator iterator = list.listIterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof NotationName) {
                NotationName nn = (NotationName) o;
                if (key.equals(nn.getTitle())) {
                    Project p = ProjectManager.getManager().getCurrentProject();
                    p.getProjectSettings().setNotationLanguage(nn);
                    break;
                }
            }
        }
    }

    /**
     * @return The menu for the notation.
     */
    public JMenu getMenu() { return menu; }

    /*
     * @see javax.swing.event.MenuListener#menuSelected(javax.swing.event.MenuEvent)
     */
    public void menuSelected(MenuEvent me) {
        Project p = ProjectManager.getManager().getCurrentProject();
        NotationName current = p.getProjectSettings().getNotationName();
        menu.removeAll();
        List list = Notation.getAvailableNotations();
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
                mi.setSelected(current.sameNotationAs(nn));
                menu.add(mi);
            }
        }
    }

    /*
     * @see javax.swing.event.MenuListener#menuDeselected(javax.swing.event.MenuEvent)
     */
    public void menuDeselected(MenuEvent me) { }

    /*
     * @see javax.swing.event.MenuListener#menuCanceled(javax.swing.event.MenuEvent)
     */
    public void menuCanceled(MenuEvent me) { }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 1364283215100616618L;
}
