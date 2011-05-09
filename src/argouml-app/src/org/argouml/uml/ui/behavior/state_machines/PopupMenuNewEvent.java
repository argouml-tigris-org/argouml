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

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionRemoveModelElement;
import org.argouml.uml.ui.behavior.activity_graphs.ActionAddEventAsTrigger;

/**
 * @since Dec 15, 2002
 * @author jaap.branderhorst@xs4all.nl
 * @deprecated by Bob Tarling in 0.33.4. This is no longer used
 */
@Deprecated
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
     * @param target the target
     */
    public PopupMenuNewEvent(String role, Object target) {
        super();

        buildMenu(this, role, target);
    }
    
    static void buildMenu(JPopupMenu pmenu, String role, Object target) {
        
        assert role != null;
        assert target != null;

        if (role.equals(ActionNewEvent.Roles.DEFERRABLE_EVENT)
                || role.equals(ActionNewEvent.Roles.TRIGGER)) {
            JMenu select = new JMenu(Translator.localize("action.select"));
//            select.setText(Translator.localize("action.select"));
            if (role.equals(ActionNewEvent.Roles.DEFERRABLE_EVENT)) {
                ActionAddEventAsDeferrableEvent.SINGLETON.setTarget(target);
                JMenuItem menuItem = new JMenuItem(
                        ActionAddEventAsDeferrableEvent.SINGLETON);
//                select.add(ActionAddEventAsDeferrableEvent.SINGLETON);
                select.add(menuItem);
            } else if (role.equals(ActionNewEvent.Roles.TRIGGER)) {
                ActionAddEventAsTrigger.SINGLETON.setTarget(target);
                select.add(ActionAddEventAsTrigger.SINGLETON);
            }
            pmenu.add(select);
        }

        JMenu newMenu = new JMenu(Translator.localize("action.new"));
//        newMenu.setText(Translator.localize("action.new"));
        newMenu.add(ActionNewCallEvent.getSingleton());
        ActionNewCallEvent.getSingleton().setTarget(target);
        ActionNewCallEvent.getSingleton().putValue(ActionNewEvent.ROLE, role);
        newMenu.add(ActionNewChangeEvent.getSingleton());
        ActionNewChangeEvent.getSingleton().setTarget(target);
        ActionNewChangeEvent.getSingleton().putValue(ActionNewEvent.ROLE, role);
        newMenu.add(ActionNewSignalEvent.getSingleton());
        ActionNewSignalEvent.getSingleton().setTarget(target);
        ActionNewSignalEvent.getSingleton().putValue(ActionNewEvent.ROLE, role);
        newMenu.add(ActionNewTimeEvent.getSingleton());
        ActionNewTimeEvent.getSingleton().setTarget(target);
        ActionNewTimeEvent.getSingleton().putValue(ActionNewEvent.ROLE, role);
        pmenu.add(newMenu);

        pmenu.addSeparator();

        ActionRemoveModelElement.SINGLETON.setObjectToRemove(
                ActionNewEvent.getAction(role, target));
        ActionRemoveModelElement.SINGLETON.putValue(Action.NAME, 
                Translator.localize("action.delete-from-model"));
        pmenu.add(ActionRemoveModelElement.SINGLETON);
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -7624618103144695448L;
}
