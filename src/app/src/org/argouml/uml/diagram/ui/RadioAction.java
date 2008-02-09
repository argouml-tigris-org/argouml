// $Id$
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

// Created on 20 July 2003, 02:12

package org.argouml.uml.diagram.ui;

import javax.swing.Action;
import javax.swing.Icon;
import org.argouml.kernel.ProjectManager;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.toolbar.toolbutton.AbstractButtonAction;

/**
 * A wrapper around a standard action to indicate that any buttons created
 * from this actions should act like radio buttons, i.e. that when the
 * toolbar buttons are double-clicked, they remain active, and every click
 * on the diagram will place a new modelelement.
 *
 * @author Bob Tarling
 */
public class RadioAction extends AbstractButtonAction {

    private Action realAction;

    /**
     * @param action the action
     */
    public RadioAction(Action action) {
        super((String) action.getValue(Action.NAME),
		(Icon) action.getValue(Action.SMALL_ICON));
        putValue(Action.SHORT_DESCRIPTION,
                action.getValue(Action.SHORT_DESCRIPTION));
        realAction = action;
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        UMLDiagram diagram = (UMLDiagram)
            ProjectManager.getManager().getCurrentProject().getActiveDiagram();
        if (Globals.getSticky() && diagram.getSelectedAction() == this) {
            // If the user selects an Action that is already selected in sticky
            // mode (double clicked) then we turn off sticky mode and make sure
            // no action is selected.
            Globals.setSticky(false);
            diagram.deselectAllTools();
            Editor ce = Globals.curEditor();
            if (ce != null) {
                ce.finishMode();
            }
            return;
        }
        super.actionPerformed(actionEvent);
        realAction.actionPerformed(actionEvent);
        diagram.setSelectedAction(this);
        Globals.setSticky(isDoubleClick());
        if (!isDoubleClick()) {
            Editor ce = Globals.curEditor();
            if (ce != null) {
                ce.finishMode();
            }
        }
    }

    /**
     * @return the action
     */
    public Action getAction() {
        return realAction;
    }
}
