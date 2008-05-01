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

// Original Author: agauthie

package org.argouml.cognitive.ui;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.argouml.cognitive.ToDoItem;
import org.argouml.i18n.Translator;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.UndoableAction;

/**
 * Action to display the todo pane.
 *
 */
public class ActionGoToCritique extends UndoableAction {
    private ToDoItem item = null;

    /**
     * Constructor.
     *
     * @param theItem The item that we go to.
     */
    public ActionGoToCritique(ToDoItem theItem) {
        super(Translator.localize(theItem.getHeadline()), 
	            null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize(theItem.getHeadline()));
	item = theItem;
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        super.actionPerformed(ae);
        // TODO: ProjectBrowser doesn't need to mediate this conversation
        // Use an event listener in the ToDoPane to communicate instead. - tfm
        ((ToDoPane) ProjectBrowser.getInstance().getTodoPane())
                .selectItem(item);
    }

} /* end class ActionGoToCritique */
