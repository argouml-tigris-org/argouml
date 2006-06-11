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

package org.argouml.uml.ui.behavior.common_behavior;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionRemoveModelElement;
import org.argouml.uml.ui.UMLMutableLinkedList;

/**
 * The popupmenu shown by several lists on the proppanels when the user wants
 * to add or delete an action.
 * @since Dec 15, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class PopupMenuNewAction extends JPopupMenu {


    /**
     * Constructs a new popupmenu. The given parameter role determines what
     * the purpose is of the actions that can be created via this popupmenu.
     * The parameter must comply to the interface Roles
     * defined on ActionNewAction.
     * @param role the role
     * @param list the list
     */
    public PopupMenuNewAction(String role, UMLMutableLinkedList list) {
        super();

        JMenu newMenu = new JMenu();
        newMenu.setText(Translator.localize("action.new"));

        newMenu.add(ActionNewCallAction.getInstance());
        ActionNewCallAction.getInstance().setTarget(list.getTarget());
        ActionNewCallAction.getInstance().putValue(ActionNewAction.ROLE, role);

        newMenu.add(ActionNewCreateAction.getInstance());
        ActionNewCreateAction.getInstance().setTarget(list.getTarget());
        ActionNewCreateAction.getInstance()
            .putValue(ActionNewAction.ROLE, role);

        newMenu.add(ActionNewDestroyAction.getiNSTANCE());
        ActionNewDestroyAction.getiNSTANCE().setTarget(list.getTarget());
        ActionNewDestroyAction.getiNSTANCE()
            .putValue(ActionNewAction.ROLE, role);

        newMenu.add(ActionNewReturnAction.getInstance());
        ActionNewReturnAction.getInstance().setTarget(list.getTarget());
        ActionNewReturnAction.getInstance()
            .putValue(ActionNewAction.ROLE, role);

        newMenu.add(ActionNewSendAction.getInstance());
        ActionNewSendAction.getInstance().setTarget(list.getTarget());
        ActionNewSendAction.getInstance().putValue(ActionNewAction.ROLE, role);

        newMenu.add(ActionNewTerminateAction.getInstance());
        ActionNewTerminateAction.getInstance().setTarget(list.getTarget());
        ActionNewTerminateAction.getInstance()
            .putValue(ActionNewAction.ROLE, role);

        newMenu.add(ActionNewUninterpretedAction.getInstance());
        ActionNewUninterpretedAction.getInstance().setTarget(list.getTarget());
        ActionNewUninterpretedAction.getInstance()
            .putValue(ActionNewAction.ROLE, role);

        add(newMenu);

        addSeparator();

        ActionRemoveModelElement.SINGLETON.setObjectToRemove(ActionNewAction
             .getAction(role, list.getTarget()));
        add(ActionRemoveModelElement.SINGLETON);
    }
}
