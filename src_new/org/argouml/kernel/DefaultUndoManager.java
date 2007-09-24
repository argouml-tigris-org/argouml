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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;

/**
 * Manages a stacks of Commands to undo and redo. This DefaultUndoManager is
 * only temporarily a singleton until changes are made to GEF.
 * 
 * @author Bob Tarling
 */
class DefaultUndoManager implements UndoManager {
    
    private static final Logger LOG =
        Logger.getLogger(DefaultUndoManager.class);

    private int undoMax = 100;
    
    private ArrayList<PropertyChangeListener> listeners =
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
     * 
     * @deprecated The DefaultUndoManager is only temporarily a singleton until
     *             changes are made to GEF at which point there will be one undo
     *             manager per ArgoUML project and this method will disappear.
     * @return the singleton undo manager
     */
    @Deprecated
    public static UndoManager getInstance() {
        return INSTANCE;
    }
    
    public synchronized Object execute(Command command) {
        addCommand(command);
        return command.execute();
    }
    
    public synchronized void addCommand(Command command) {

        ProjectManager.getManager().setSaveEnabled(true);
        
        if (undoMax == 0) {
            return;
        }
        
        if (!command.isUndoable()) {
            undoStack.clear();
            newInteraction = true;
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


    public synchronized void undo() {
        final Interaction command = undoStack.pop();
        command.undo();
        if (!command.isRedoable()) {
            redoStack.clear();
        }
        redoStack.push(command);
    }
    

    public synchronized void redo() {
        final Interaction command = redoStack.pop();
        command.execute();
        undoStack.push(command);
    }
    
    public synchronized void startInteraction(String label) {
        LOG.info("Starting interaction " + label);
        this.newInteractionLabel = label;
        newInteraction = true;
    }
 
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void fire(final String property, final Object value) {
        
        final ArrayList<PropertyChangeListener> list =
            new ArrayList<PropertyChangeListener>(listeners);
        
        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    Iterator<PropertyChangeListener> i = list.iterator();
                    while (i.hasNext()) {
                        PropertyChangeListener listener = i.next();
                        listener.propertyChange(
                                new PropertyChangeEvent(
                                        this, property, "", value));
                    }
                }
            }
        );
        
    }
    
    /**
     * An Interact is a Command the contains a list of sub-commands.
     * It represents a single user interaction and contains all the commands
     * executed as part of that interaction.
     *
     * @author Bob
     */
    class Interaction extends AbstractCommand {
        
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
        
        public Object execute() {
            final Iterator<Command> it = commands.iterator();
            while (it.hasNext()) {
                it.next().execute();
            }
            return null;
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
        
        public boolean isRedoable() {
            final Iterator<Command> it = commands.iterator();
            while (it.hasNext()) {
                final Command command = it.next();
                if (!command.isRedoable()) {
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
            if (isRedoable()) {
                return "Redo " + label;
            } else {
                return "Can't Redo " + label;
            }
        }
        
        List<Command> getCommands() {
            return new ArrayList<Command> (commands);
        }
    }
    
    private abstract class InteractionStack extends Stack<Interaction> {
        
        private String labelProperty;
        private String addedProperty;
        private String removedProperty;
        private String sizeProperty;
        
        public InteractionStack(
                String labelProperty,
                String addedProperty,
                String removedProperty,
                String sizeProperty) {
            this.labelProperty = labelProperty;
            this.addedProperty = addedProperty;
            this.removedProperty = removedProperty;
            this.sizeProperty = sizeProperty;
        }
        
        public Interaction push(Interaction item) {
            super.push(item);
            fireLabel();
            fire(addedProperty, item);
            fire(sizeProperty, size());
            return item;
        }
        
        public Interaction pop() {
            Interaction item = super.pop();
            fireLabel();
            fire(removedProperty, item);
            fire(sizeProperty, size());
            return item;
        }
        
        private void fireLabel() {
            fire(labelProperty, getLabel());
        }
        
        protected abstract String getLabel();
    }
    
    private class UndoStack extends InteractionStack {
        
        public UndoStack() {
            super(
                    "undoLabel",
                    "undoAdded",
                    "undoRemoved",
                    "undoSize");
        }
        
        public Interaction push(Interaction item) {
            super.push(item);
            if (item.isUndoable()) {
                fire("undoable", true);
            }
            return item;
        }
        
        public Interaction pop() {
            Interaction item = super.pop();
            if (size() == 0 || !peek().isUndoable()) {
                fire("undoable", false);
            }
            return item;
        }
        
        public void clear() {
            super.clear();
            fire("undoSize", size());
            fire("undoable", false);
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
            super(
                    "redoLabel", 
                    "redoAdded", 
                    "redoRemoved", 
                    "redoSize");
        }
        
        
        public Interaction push(Interaction item) {
            super.push(item);
            if (item.isRedoable()) {
                fire("redoable", true);
            }
            return item;
        }
        
        public Interaction pop() {
            Interaction item = super.pop();
            if (size() == 0 || !peek().isRedoable()) {
                fire("redoable", false);
            }
            return item;
        }
        
        public void clear() {
            super.clear();
            fire("redoSize", size());
            fire("redoable", false);
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
