// $Id$
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

package org.argouml.dev;

/**
 * By using this wrapper we are able to show the contents of the Undo stack.
 */
//public class UndoManagerWrapper extends UndoManager {
public class UndoManagerWrapper {

    /**
     * The Constructor.
     */
    public UndoManagerWrapper() {
    }

//    /*
//     * @see org.tigris.gef.undo.UndoManager#addMemento(org.tigris.gef.undo.Memento)
//     */
//    public void addMemento(Memento memento) {
//        super.addMemento(memento);
//        UndoLogPanel.getInstance().addMemento(memento);
//    }
//
//    /**
//     * @see org.tigris.gef.undo.UndoManager#empty()
//     */
//    public void empty() {
//        super.empty();
//        UndoLogPanel.getInstance().removeAll();
//    }
//
//    /**
//     * @see org.tigris.gef.undo.UndoManager#emptyUndo()
//     */
//    public void emptyUndo() {
//        super.emptyUndo();
//    }
//
//    /*
//     * @see org.tigris.gef.undo.UndoManager#redo(org.tigris.gef.undo.Memento)
//     */
//    protected void redo(Memento memento) {
//        UndoLogPanel.getInstance().addMemento(memento);
//        super.redo(memento);
//    }
//
//    /*
//     * @see org.tigris.gef.undo.UndoManager#undo(org.tigris.gef.undo.Memento)
//     */
//    protected void undo(Memento memento) {
//        UndoLogPanel.getInstance().removeMemento(memento);
//        super.undo(memento);
//    }
//

}
