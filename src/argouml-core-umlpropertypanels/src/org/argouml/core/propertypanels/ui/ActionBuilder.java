// $Id$
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
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionRemoveModelElement;
import org.argouml.uml.ui.behavior.activity_graphs.ActionAddEventAsTrigger;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewActionSequence;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewCallAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewCreateAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewDestroyAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewReturnAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewSendAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewTerminateAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewUninterpretedAction;
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
class ActionBuilder extends JPopupMenu {
    
    void buildMenu(
            final JPopupMenu pmenu, 
            final String role,
            final Object target,
            String label,
            Object[] actions) {
      
        JMenu newMenu = new JMenu();
        newMenu.setText(Translator.localize(label));

        for (Object action : actions) {
            if (action == null) {
                pmenu.addSeparator();
            } else if (action instanceof String) {
                label = (String) action;
            } else if (action instanceof Action) {
                pmenu.add((Action) action);
            } else {
                JMenu innerMenu = new JMenu(Translator.localize(label));
                for (Object innerAction : (Object[]) action) {
                    if (innerAction == null) {
                        innerMenu.addSeparator();
                    } else if (innerAction instanceof Action) {
                        innerMenu.add((Action) innerAction);
                    }
                }
                pmenu.add(innerMenu);
            }
        }
    }
}
