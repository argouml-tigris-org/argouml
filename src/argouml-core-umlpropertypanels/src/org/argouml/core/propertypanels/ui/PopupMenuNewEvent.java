/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
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

package org.argouml.core.propertypanels.ui;

import javax.swing.Action;
import javax.swing.JPopupMenu;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionRemoveModelElement;
import org.argouml.uml.ui.behavior.activity_graphs.ActionAddEventAsTrigger;
import org.argouml.uml.ui.behavior.state_machines.ActionAddEventAsDeferrableEvent;
import org.argouml.uml.ui.behavior.state_machines.ActionNewCallEvent;
import org.argouml.uml.ui.behavior.state_machines.ActionNewChangeEvent;
import org.argouml.uml.ui.behavior.state_machines.ActionNewEvent;
import org.argouml.uml.ui.behavior.state_machines.ActionNewSignalEvent;
import org.argouml.uml.ui.behavior.state_machines.ActionNewTimeEvent;

/**
 * @since Dec 15, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
class PopupMenuNewEvent extends ActionBuilder {
    
    /**
     * The UID.
     */
    private static final long serialVersionUID = -7624618103144695448L;

    private static final Object[] dtactions = new Object[] {
        "action.select",
        new Object[] {
            ActionAddEventAsDeferrableEvent.SINGLETON,
            ActionAddEventAsTrigger.SINGLETON,
        },
        "action.new",
        new Object[] {
            ActionNewCallEvent.getSingleton(),
            ActionNewChangeEvent.getSingleton(),
            ActionNewSignalEvent.getSingleton(),
            ActionNewTimeEvent.getSingleton(),
        },
        null,
        ActionRemoveModelElement.SINGLETON
    };
    
    private static final Object[] actions = new Object[] {
        "action.new",
        new Object[] {
            ActionNewCallEvent.getSingleton(),
            ActionNewChangeEvent.getSingleton(),
            ActionNewSignalEvent.getSingleton(),
            ActionNewTimeEvent.getSingleton(),
        },
        null,
        ActionRemoveModelElement.SINGLETON
    };
    
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
    PopupMenuNewEvent(String role, Object target) {
        super();

        buildMenu(this, role, target, "action.new");
    }
    
    void buildMenu(
            final JPopupMenu pmenu,
            final String role,
            final Object target,
            String label) {
        
        assert role != null;
        assert target != null;
        
        init(role, target);

        if (role.equals(ActionNewEvent.Roles.DEFERRABLE_EVENT)
                || role.equals(ActionNewEvent.Roles.TRIGGER)) {
            buildMenu(pmenu, role, target, label, dtactions);
        } else {
            buildMenu(pmenu, role, target, label, actions);
        }
    }
    
    private static void init(
            final String role,
            final Object target) {
        
        if (role.equals(ActionNewEvent.Roles.DEFERRABLE_EVENT)
                || role.equals(ActionNewEvent.Roles.TRIGGER)) {
            if (role.equals(ActionNewEvent.Roles.DEFERRABLE_EVENT)) {
                ActionAddEventAsDeferrableEvent.SINGLETON.setTarget(target);
            } else if (role.equals(ActionNewEvent.Roles.TRIGGER)) {
                ActionAddEventAsTrigger.SINGLETON.setTarget(target);
            }
        }

        ActionNewCallEvent.getSingleton().setTarget(target);
        ActionNewCallEvent.getSingleton().putValue(ActionNewEvent.ROLE, role);
        ActionNewChangeEvent.getSingleton().setTarget(target);
        ActionNewChangeEvent.getSingleton().putValue(ActionNewEvent.ROLE, role);
        ActionNewSignalEvent.getSingleton().setTarget(target);
        ActionNewSignalEvent.getSingleton().putValue(ActionNewEvent.ROLE, role);
        ActionNewTimeEvent.getSingleton().setTarget(target);
        ActionNewTimeEvent.getSingleton().putValue(ActionNewEvent.ROLE, role);

        ActionRemoveModelElement.SINGLETON.setObjectToRemove(
                ActionNewEvent.getAction(role, target));
        ActionRemoveModelElement.SINGLETON.putValue(Action.NAME, 
                Translator.localize("action.delete-from-model"));
    }
}
