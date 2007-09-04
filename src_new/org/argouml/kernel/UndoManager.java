// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.kernel;

import java.beans.PropertyChangeListener;

/**
 * Stores Commands that have been executed and allows them to be undone
 * and redone. Commands represent single operations on the ArgoUML model.
 * A single user interaction may generate several Commands. Undo/redo
 * works an a user interaction and so can undo/redo several commands in one
 * call.
 * 
 * @author Bob Tarling
 */
public interface UndoManager {

    /**
     * Adds a new command to the undo stack.
     * @param command the command.
     */
    public abstract void addCommand(Command command);

    /**
     * Set the maximum number of interactions the stack can hold.
     * @param max the maximum chain count
     */
    public abstract void setUndoMax(int max);

    /**
     * Undo the top user interaction on the undo stack and move
     * it to the redo stack
     */
    public abstract void undo();

    /**
     * Redo the top user interaction on the undo stack and move
     * it to the undo stack
     */
    public abstract void redo();

    /**
     * Empty all undoable and redoable items from the UndoManager
     */
    public abstract void empty();

    /**
     * Instructs the UndoManager that a new user interaction is about to take
     * place. All commands received until the next call to startInteraction
     * will form a single undoable unit.
     */
    public abstract void startInteraction();

    /**
     * Allow a listener to detect when the undo or redo stack
     * changes availability
     * @param listener a PropertyChangeListener
     */
    public abstract void addPropertyChangeListener(
            PropertyChangeListener listener);
}