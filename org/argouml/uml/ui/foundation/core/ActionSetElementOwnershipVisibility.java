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

// $Header$
package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JRadioButton;

import org.argouml.application.api.Argo;
import org.argouml.uml.ui.UMLButtonGroup;
import org.argouml.uml.ui.UMLChangeAction;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;

/**
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class ActionSetElementOwnershipVisibility extends UMLChangeAction {

    public final static String PUBLIC_ACTION_COMMAND = "public";
    public final static String PROTECTED_ACTION_COMMAND = "protected";
    public final static String PRIVATE_ACTION_COMMAND = "private";

    public static final ActionSetElementOwnershipVisibility SINGLETON = new ActionSetElementOwnershipVisibility();

    /**
     * Constructor for ActionSetElementOwnershipVisibility.
     * @param s
     */
    protected ActionSetElementOwnershipVisibility() {
        super(Argo.localize("CoreMenu", "Set"), true, NO_ICON);
    }
    
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() instanceof UMLButtonGroup) {
            UMLButtonGroup group = (UMLButtonGroup)e.getSource();
            Object target = group.getTarget();
            if (target instanceof MModelElement) {
                MModelElement m = (MModelElement)target;
                String command = e.getActionCommand();
                if (command.equals(PUBLIC_ACTION_COMMAND)) {
                    m.setVisibility(MVisibilityKind.PUBLIC);
                } else
                if (command.equals(PRIVATE_ACTION_COMMAND)) {
                    m.setVisibility(MVisibilityKind.PRIVATE);
                } else
                if (command.equals(PROTECTED_ACTION_COMMAND)) {
                    m.setVisibility(MVisibilityKind.PROTECTED);
                } else
                    throw new IllegalArgumentException("Illegal action. Actioncommand was not correct. It was " + command);
            }
        }
    }

}
