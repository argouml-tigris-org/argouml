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
import java.util.ListIterator;
import java.util.Stack;

/**
 * Manages a stacks of Commands to undo and redo.
 * UndoManager is only temporarily singleton until changes are made to GEF.
 * @author Bob Tarling
 */
public class UndoManager {

    private int undoMax = 100;
    
    private Collection<PropertyChangeListener> listeners =
        new ArrayList<PropertyChangeListener>();
    
    private boolean newChain = true;
    
    // TODO: A UndoChainStack may produce some reasuable code for
    // the undoStack and the redoStack/
    private Stack<MacroCommand> undoStack = new Stack<MacroCommand>();
    private Stack<MacroCommand> redoStack = new Stack<MacroCommand>();
    
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
    public void addCommand(Command command) {
        if (undoMax == 0) {
            return;
        }
        // Flag the command as to whether it is first in a chain
        final MacroCommand macroCommand;
        if (newChain || undoStack.isEmpty()) {
            redoStack.clear();
            newChain = false;
            if (undoStack.size() > undoMax) {
                undoStack.remove(0);
            }
            macroCommand = new MacroCommand();
            undoStack.push(macroCommand);
        } else {
            macroCommand = undoStack.peek();
        }
        macroCommand.addCommand(command);
    }
    
    /**
     * Set the maximum number of command chains the stack can hold.
     * @param max the maximum chain count
     */
    public void setUndoMax(int max) {
        undoMax = max;
    }

    /**
     * Undo the top MacroCommand on the undo stack and move
     * it to the redo stack
     */
    public void undo() {
        final MacroCommand command = undoStack.pop();
        command.undo();
        redoStack.push(command);
    }
    
    /**
     * Redo the top MacroCommand on the undo stack and move
     * it to the undo stack
     */
    public void redo() {
        final MacroCommand command = redoStack.pop();
        command.execute();
        undoStack.push(command);
    }
    
    /**
     * Empty all undoable and redoable items from the UndoManager
     */
    public void empty() {
        undoStack.clear();
        redoStack.clear();
    }
    
    /**
     * Instructs the UndoManager that a new user interaction is about to take
     * place. All commands received until this is called once more will make
     * a single undoable unit.
     */
    public void startInteraction() {
        newChain = true;
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
                            Boolean.toString(undoStack.size() > 0)));
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
                            Boolean.toString(redoStack.size() > 0)));
        }
    }
    
    /**
     * A MacroCommand is a Command the executes a list of sub-commands.
     * It represents a single user interaction and contains all the commands
     * executed as part of that interaction.
     *
     * @author Bob
     */
    private class MacroCommand extends AbstractUndoableCommand {
        
        private List<Command> commands = new ArrayList<Command>();
        
        public void undo() {
            final ListIterator<Command> it =
                commands.listIterator(commands.size());
            while (it.hasPrevious()) {
                ((UndoableCommand) it.previous()).undo();
            }
        }
        
        public void execute() {
            final Iterator<Command> it = commands.iterator();
            while (it.hasNext()) {
                it.next().execute();
            }
        }
        
        public boolean isUndoable() {
            final Iterator<Command> it = commands.iterator();
            while (it.hasNext()) {
                final Command command = it.next();
                if (!(command instanceof UndoableCommand)) {
                    return false;
                }
                if (!((UndoableCommand) command).isUndoable()) {
                    return false;
                }
            }
            return true;
        }
        
        private void addCommand(Command command) {
            commands.add(command);
        }
        
        private String getUndoLabel() {
            return "Undo";
        }
        
        private String getRedoLabel() {
            return "Redo";
        }
    }
}
