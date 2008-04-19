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

package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionRemoveModelElement;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.behavior.activity_graphs.ActionAddEventAsTrigger;

/**
 * @since Dec 15, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class PopupMenuNewEvent extends JPopupMenu {

    /**
     * Constructor for PopupMenuNewEvent.<p>
     *
     * Constructs a new popupmenu. The given parameter role determines what
     * the purpose is of the events that can be created via this popupmenu.
     * The parameter must comply to the interface Roles
     * defined on ActionNewEvent.
     *
     * @param role the role
     * @param list the list
     */
    public PopupMenuNewEvent(String role, UMLMutableLinkedList list) {
        super();

        assert role != null;

        if (role.equals(ActionNewEvent.Roles.DEFERRABLE_EVENT)
                || role.equals(ActionNewEvent.Roles.TRIGGER)) {
            JMenu select = new JMenu();
            select.setText(Translator.localize("action.select"));
            if (role.equals(ActionNewEvent.Roles.DEFERRABLE_EVENT)) {
                ActionAddEventAsDeferrableEvent.SINGLETON.setTarget(
                        list.getTarget());
                select.add(ActionAddEventAsDeferrableEvent.SINGLETON);
            } else if (role.equals(ActionNewEvent.Roles.TRIGGER)) {
                ActionAddEventAsTrigger.SINGLETON.setTarget(list.getTarget());
                select.add(ActionAddEventAsTrigger.SINGLETON);
            }
            add(select);
        }

        JMenu newMenu = new JMenu();
        newMenu.setText(Translator.localize("action.new"));
        newMenu.add(ActionNewCallEvent.getSingleton());
        ActionNewCallEvent.getSingleton().setTarget(list.getTarget());
        ActionNewCallEvent.getSingleton().putValue(ActionNewEvent.ROLE, role);
        newMenu.add(ActionNewChangeEvent.getSingleton());
        ActionNewChangeEvent.getSingleton().setTarget(list.getTarget());
        ActionNewChangeEvent.getSingleton().putValue(ActionNewEvent.ROLE, role);
        newMenu.add(ActionNewSignalEvent.getSingleton());
        ActionNewSignalEvent.getSingleton().setTarget(list.getTarget());
        ActionNewSignalEvent.getSingleton().putValue(ActionNewEvent.ROLE, role);
        newMenu.add(ActionNewTimeEvent.getSingleton());
        ActionNewTimeEvent.getSingleton().setTarget(list.getTarget());
        ActionNewTimeEvent.getSingleton().putValue(ActionNewEvent.ROLE, role);
        add(newMenu);

        addSeparator();

        ActionRemoveModelElement.SINGLETON.setObjectToRemove(
                ActionNewEvent.getAction(role, list.getTarget()));
        add(ActionRemoveModelElement.SINGLETON);

    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -7624618103144695448L;
}
