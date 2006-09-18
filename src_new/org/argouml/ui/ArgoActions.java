// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

package org.argouml.ui;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.argouml.i18n.Translator;
//import org.argouml.kernel.ProjectManager;
import org.argouml.uml.diagram.ui.ActionRemoveFromDiagram;
import org.argouml.uml.ui.ActionSaveProject;
import org.tigris.gef.undo.RedoAction;
import org.tigris.gef.undo.UndoAction;

/**
 * Singleton to hold actions refactored out of ProjectBrowser.
 * 
 * @author Tom Morris
 *
 */
public class ArgoActions {
    
    /**
     * The action to save the current project.
     */
    private static AbstractAction saveAction = new ActionSaveProject();

    /**
     * The action to redo the last undone action.
     */
    private static AbstractAction redoAction =
        new RedoAction(Translator.localize("action.redo"));

    /**
     * The action to undo the last user interaction.
     */
    private static UndoAction undoAction =
        new UndoAction(Translator.localize("action.undo"));

    /**
     * The action to remove the current selected Figs from the diagram.
     */
    private static ActionRemoveFromDiagram removeFromDiagram =
        new ActionRemoveFromDiagram(
                Translator.localize("action.remove-from-diagram"));


    static void initialize() {
        //ProjectManager.getManager().setSaveAction(saveAction);
                
    }
    

    /**
     * Get the action that can undo the last user interaction on this project.
     * @return the undo action.
     */
    public static AbstractAction getUndoAction() {
        return undoAction;
    }

    /**
     * Get the action that can redo the last undone action.
     * @return the redo action.
     */
    public static AbstractAction getRedoAction() {
        return redoAction;
    }

    /**
     * Get the action that can save the current project.
     * @return the save action.
     */
    public static AbstractAction getSaveAction() {
        return saveAction;
    }

    /**
     * Get the action that removes selected figs from the diagram.
     * @return the remove from diagram action.
     */
    public static Action getRemoveFromDiagramAction() {
        return removeFromDiagram;
    }

}
