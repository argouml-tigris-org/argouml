package org.tigris.gef.undo;

import org.tigris.gef.undo.Memento;
import org.tigris.gef.undo.UndoManager;

public class UndoManagerWrapper extends UndoManager {

    public UndoManagerWrapper() {
    }

    public void addMemento(Memento memento) {
        super.addMemento(memento);
        UndoLogPanel.getInstance().addMemento(memento);
    }

    public void empty() {
        super.empty();
    }

    public void emptyUndo() {
        super.emptyUndo();
    }

    protected void redo(Memento memento) {
        UndoLogPanel.getInstance().addMemento(memento);
        super.redo(memento);
    }

    protected void undo(Memento memento) {
        UndoLogPanel.getInstance().removeMemento(memento);
        super.undo(memento);
    }
    
    
}
