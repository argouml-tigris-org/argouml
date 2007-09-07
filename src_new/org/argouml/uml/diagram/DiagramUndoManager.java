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

package org.argouml.uml.diagram;

import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.tigris.gef.undo.Memento;
import org.tigris.gef.undo.UndoManager;

/**
 * This class is a temporary wrapper around the GEF UndoManager.
 * This will be changed when GEF is modified to fire mementos and
 * provide an observer interface for ArgoUML to receive them.
 *
 * @author Bob Tarling
 */
public class DiagramUndoManager extends UndoManager {
    
    private static final Logger LOG = Logger.getLogger(UndoManager.class);
    
    private boolean startChain;
    
    /**
     * Called when a new user interaction starts
     * @see org.tigris.gef.undo.UndoManager#startChain()
     */
    @Override
    public void startChain() {
        startChain = true;
    }
    
    /**
     * @param memento the GEF memento
     * @see org.tigris.gef.undo.UndoManager#addMemento(org.tigris.gef.undo.Memento)
     */
    @Override
    public void addMemento(final Memento memento) {
        Project p = ProjectManager.getManager().getCurrentProject();
        org.argouml.kernel.UndoManager undo = p.getUndoManager();

        if (startChain) {
            //TODO i18n: GEF needs to pass us back the description of what is
            // being done.
            undo.startInteraction("Diagram Interaction");
        }
        
        undo.addCommand(new DiagramCommand(memento));
        
        startChain = false;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        LOG.info("Adding property listener " + listener);
        super.addPropertyChangeListener(listener);
    }
    
    
    private class DiagramCommand
            extends org.argouml.kernel.AbstractCommand {
        
        private final Memento memento;
        
        DiagramCommand(final Memento memento) {
            this.memento = memento;
        }

        @Override
        public void execute() {
            memento.redo();
        }

        @Override
        public void undo() {
            memento.undo();
        }
    }
}
