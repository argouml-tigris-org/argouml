// $Id:ActionAutoCritique.java 12684 2007-05-27 07:25:08Z mvw $
// Copyright (c) 2004-2006 The Regents of the University of California. All
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

package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.argouml.cognitive.Designer;
import org.argouml.i18n.Translator;
import org.tigris.gef.undo.UndoableAction;



/**
 * Stops critiquing and the TodoList validity checking thread in ToDoList.
 */
public class ActionAutoCritique extends UndoableAction {

    /**
     * The constructor.
     */
    public ActionAutoCritique() {
        super(Translator.localize("action.toggle-auto-critique"),
                null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("action.toggle-auto-critique"));
        putValue("SELECTED",
                    Boolean.valueOf(Designer.theDesigner().getAutoCritique()));
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        super.actionPerformed(ae);
    	
        // stop/start creating more critics
	Designer d = Designer.theDesigner();
	boolean b = d.getAutoCritique();
	d.setAutoCritique(!b);

        // stop/start cleaning up invalid TodoItems.
        Designer.theDesigner().getToDoList().setPaused(
                !Designer.theDesigner().getToDoList().isPaused());
    }


    /**
     * The UID.
     */
    private static final long serialVersionUID = 9057306108717070004L;
} /* end class ActionAutoCritique */

