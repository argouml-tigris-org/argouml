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

package org.argouml.kernel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Manages a stacks of Commands to undo and redo.
 * UndoManager is only temporarily singleton until changes are made to GEF.
 * @author Bob Tarling
 */
public class UndoManager {

    private int undoMax = 100;
    private int undoChainCount = 0;
    private int redoChainCount = 0;
    
    private Collection<PropertyChangeListener> listeners =
        new ArrayList<PropertyChangeListener>();
    
    private boolean newChain = true;
    
    // TODO: A UndoChainStack may produce some reasuable code for
    // the undoStack and the redoStack/
    private List<Command> undoStack = new ArrayList<Command>();
    private List<Command> redoStack = new ArrayList<Command>();
    
    private static final UndoManager INSTANCE = new UndoManager();

    private UndoManager() {
        super();
    }
    
    /**
     * Get the UndoManager singleton instance.
     * UndoManager is only temporarily singleton until changes are made to GEF.
     * @return the singleton undo manager
     */
    public static UndoManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * Adds a new command to the undo stack.
     * @param command the command.
     */
    public void addMemento(Command command) {
        if (undoMax == 0) {
            return;
        }
        // Flag the command as to whether it is first in a chain
        if (newChain) {
            ((AbstractCommand) command).startChain();
            // If the command is the first then consider that
            // there is a new chain being received and clear
            // the redos
            emptyRedo();
            incrementUndoChainCount();
            newChain = false;
            if (undoChainCount > undoMax) {
                // TODO The undo stack is full, dispose
                // of the oldest chain.
            }
        }
        undoStack.add(command);
    }
    
    /**
     * Set the maximum number of command chains the stack can hold.
     * @param max the maximum chain count
     */
    public void setUndoMax(int max) {
        undoMax = max;
    }

    /**
     * Undo the most recent chain of mementos received by the undo stack
     */
    public void undo() {
        AbstractUndoableCommand command;
        boolean startChain = false;
        do {
            command = (AbstractUndoableCommand) pop(undoStack);
            startChain = command.isStartChain();
            undo(command);
        } while (!startChain);
        decrementUndoChainCount();
        incrementRedoChainCount();
    }
    
    /**
     * Undo a single command
     * @param command the command to undo
     */
    protected void undo(UndoableCommand command) {
        command.undo();
        redoStack.add(command);
    }
    
    /**
     * Redo the most recent chain of mementos received by the undo stack
     */
    public void redo() {
        do {
            Command command = pop(redoStack);
            redo(command);
        } while(redoStack.size() > 0
                && !((AbstractCommand)
                        (redoStack.get(redoStack.size() - 1))).isStartChain());
        incrementUndoChainCount();
        decrementRedoChainCount();
    }
    
    /**
     * Undo a single command
     * @param command the command to redo
     */
    protected void redo(Command command) {
        command.execute();
        undoStack.add(command);
    }
    
    /**
     * Empty all undoable items from the UndoManager
     */
    public void emptyUndo() {
        if (undoChainCount > 0) {
            emptyStack(undoStack);
            undoChainCount = 0;
            fireCanUndo();
        }
    }
    
    /**
     * Empty all redoable items from the UndoManager
     */
    private void emptyRedo() {
        if (redoChainCount > 0) {
            emptyStack(redoStack);
            redoChainCount = 0;
            fireCanRedo();
        }
    }
    
    /**
     * Empty all undoable and redoable items from the UndoManager
     */
    public void empty() {
        emptyUndo();
        emptyRedo();
    }
    
    /**
     * Instructs the UndoManager that the sequence of mementos recieved up
     * until the next call to newChain all represent one chain of mementos
     * (ie one undoable user interaction).
     */
    public void startChain() {
        newChain = true;
    }
 
    /**
     * Empty a list stack disposing of all mementos.
     * @param list the list of mementos
     */
    private void emptyStack(List<Command> list) {
        // Lets only introduce dispose if we find it's required
        //        for (int i = 0; i < list.size(); ++i) {
        //            list.get(i).dispose();
        //        }
        list.clear();
    }
    
    private Command pop(List<Command> stack) {
        return stack.remove(stack.size() - 1);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }
    
    private void fireCanUndo() {
        Iterator i = listeners.iterator();
        while (i.hasNext()) {
            PropertyChangeListener listener = (PropertyChangeListener) i.next();
            listener.propertyChange(
                    new PropertyChangeEvent(
                            this,
                            "canUndo",
                            "",
                            Boolean.toString(undoChainCount > 0)));
        }
    }
    
    private void fireCanRedo() {
        Iterator i = listeners.iterator();
        while (i.hasNext()) {
            PropertyChangeListener listener = (PropertyChangeListener) i.next();
            listener.propertyChange(
                    new PropertyChangeEvent(
                            this,
                            "canRedo",
                            "",
                            Boolean.toString(redoChainCount > 0)));
        }
    }
    
    private void incrementUndoChainCount() {
        if (++undoChainCount == 1) {
            fireCanUndo();
        }
    }
    
    private void decrementUndoChainCount() {
        if (--undoChainCount == 0) {
            fireCanUndo();
        }
    }
    
    private void incrementRedoChainCount() {
        if (++redoChainCount == 1) {
            fireCanRedo();
        }
    }
    
    private void decrementRedoChainCount() {
        if (--redoChainCount == 0) {
            fireCanRedo();
        }
    }
}
