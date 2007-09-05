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

import org.argouml.i18n.Translator;

/**
 * Manages a stacks of Commands to undo and redo.
 * UndoManager is only temporarily singleton until changes are made to GEF.
 * @author Bob Tarling
 */
public class DefaultUndoManager implements UndoManager {

    private int undoMax = 100;
    
    private Collection<PropertyChangeListener> listeners =
        new ArrayList<PropertyChangeListener>();
    
    /**
     * Set when a new user interaction begins
     */
    private boolean newInteraction = true;
    
    /**
     * A description of the user interaction taking place.
     * Often this is the label of an Action.
     */
    private String newInteractionLabel;
    
    private UndoStack undoStack = new UndoStack();
    private RedoStack redoStack = new RedoStack();
    
    private static final UndoManager INSTANCE = new DefaultUndoManager();

    private DefaultUndoManager() {
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
    
    public void addCommand(Command command) {
        if (undoMax == 0) {
            return;
        }
        // Flag the command as to whether it is first in a chain
        final Interaction macroCommand;
        if (newInteraction || undoStack.isEmpty()) {
            redoStack.clear();
            newInteraction = false;
            if (undoStack.size() > undoMax) {
                undoStack.remove(0);
            }
            macroCommand = new Interaction(newInteractionLabel);
            undoStack.push(macroCommand);
        } else {
            macroCommand = undoStack.peek();
        }
        macroCommand.addCommand(command);
    }
    
    public void setUndoMax(int max) {
        undoMax = max;
    }

    /**
     * 
     * @see org.argouml.kernel.UndoManager#undo()
     */
    public void undo() {
        final Interaction command = undoStack.pop();
        command.undo();
        redoStack.push(command);
    }
    
    /**
     * 
     * @see org.argouml.kernel.UndoManager#redo()
     */
    public void redo() {
        final Interaction command = redoStack.pop();
        command.execute();
        undoStack.push(command);
    }
    
    public void startInteraction(String label) {
        if (!newInteraction) {
            this.newInteractionLabel = label;
            newInteraction = true;
        }
    }
 
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void fire(String property, Object value) {
        Iterator i = listeners.iterator();
        while (i.hasNext()) {
            PropertyChangeListener listener = (PropertyChangeListener) i.next();
            listener.propertyChange(
                    new PropertyChangeEvent(this, property, "", value));
        }
    }
    
    /**
     * An Interact is a Command the contains a list of sub-commands.
     * It represents a single user interaction and contains all the commands
     * executed as part of that interaction.
     *
     * @author Bob
     */
    private class Interaction extends AbstractCommand {
        
        private List<Command> commands = new ArrayList<Command>();
        
        private String label;
        
        Interaction(String label) {
            this.label = label;
        }
        
        public void undo() {
            final ListIterator<Command> it =
                commands.listIterator(commands.size());
            while (it.hasPrevious()) {
                it.previous().undo();
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
                if (!command.isUndoable()) {
                    return false;
                }
            }
            return true;
        }
        
        private void addCommand(Command command) {
            commands.add(command);
        }
        
        // TODO: i18n
        private String getUndoLabel() {
            if (isUndoable()) {
                return "Undo " + label;
            } else {
                return "Can't Undo " + label;
            }
        }
        
        // TODO: i18n
        private String getRedoLabel() {
            return "Redo " + label;
        }
    }
    
    private abstract class InteractionStack extends Stack<Interaction> {
        
        private String enabledProperty;
        private String labelProperty;
        
        public InteractionStack(
                String enabledProperty,
                String labelProperty) {
            this.enabledProperty = enabledProperty;
            this.labelProperty = labelProperty;
        }
        
        public Interaction push(Interaction item) {
            super.push(item);
            fireLabel();
            if (size() == 1) {
                fire(enabledProperty, true);
            }
            return item;
        }
        
        public Interaction pop() {
            Interaction item = super.pop();
            fireLabel();
            if (size() == 0) {
                fire(enabledProperty, false);
            }
            return item;
        }
        
        private void fireLabel() {
            fire(labelProperty, getLabel());
        }
        
        protected abstract String getLabel();
    }
    
    private class UndoStack extends InteractionStack {
        
        public UndoStack() {
            super("undoable", "undoLabel");
        }
        
        protected String getLabel() {
            if (empty()) {
                return Translator.localize("action.undo");
            } else {
                return peek().getUndoLabel();
            }
        }
    }
    
    private class RedoStack extends InteractionStack {
        
        public RedoStack() {
            super("redoable", "redoLabel");
        }
        
        protected String getLabel() {
            if (empty()) {
                return Translator.localize("action.redo");
            } else {
                return peek().getRedoLabel();
            }
        }
    }
}
