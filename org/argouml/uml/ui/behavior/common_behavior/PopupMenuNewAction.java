// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

import org.argouml.application.api.Argo;
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
     * The parameter must comply to the interface Roles defined on ActionNewAction.
     * @param role 
     */
    public PopupMenuNewAction(String role, UMLMutableLinkedList list) {
        super();

        JMenu newMenu = new JMenu();
        newMenu.setText(Translator.localize("action.new"));
        newMenu.add(ActionNewCallAction.SINGLETON);
        ActionNewCallAction.SINGLETON.setTarget(list.getTarget());
        ActionNewCallAction.SINGLETON.putValue(ActionNewAction.ROLE, role);
        newMenu.add(ActionNewCreateAction.SINGLETON);
        ActionNewCreateAction.SINGLETON.setTarget(list.getTarget());
        ActionNewCreateAction.SINGLETON.putValue(ActionNewAction.ROLE, role);
        newMenu.add(ActionNewDestroyAction.SINGLETON);
        ActionNewDestroyAction.SINGLETON.setTarget(list.getTarget());
        ActionNewDestroyAction.SINGLETON.putValue(ActionNewAction.ROLE, role);
        newMenu.add(ActionNewReturnAction.SINGLETON);
        ActionNewReturnAction.SINGLETON.setTarget(list.getTarget());
        ActionNewReturnAction.SINGLETON.putValue(ActionNewAction.ROLE, role);
        newMenu.add(ActionNewSendAction.SINGLETON);
        ActionNewSendAction.SINGLETON.setTarget(list.getTarget());
        ActionNewSendAction.SINGLETON.putValue(ActionNewAction.ROLE, role);
        newMenu.add(ActionNewTerminateAction.SINGLETON);
        ActionNewTerminateAction.SINGLETON.setTarget(list.getTarget());
        ActionNewTerminateAction.SINGLETON.putValue(ActionNewAction.ROLE, role);
        newMenu.add(ActionNewUninterpretedAction.SINGLETON);
        ActionNewUninterpretedAction.SINGLETON.setTarget(list.getTarget());
        ActionNewUninterpretedAction.SINGLETON.putValue(
							ActionNewAction.ROLE,
							role);
        add(newMenu);

        addSeparator();

        ActionRemoveModelElement.SINGLETON.setTarget(
						     list.getSelectedValue());
        add(ActionRemoveModelElement.SINGLETON);
    }
}
