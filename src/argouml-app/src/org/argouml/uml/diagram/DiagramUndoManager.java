/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007-2008 The Regents of the University of California. All
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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.tigris.gef.undo.Memento;
import org.tigris.gef.undo.UndoManager;

/**
 * This class is a temporary wrapper around the GEF UndoManager.
 * This will be changed when GEF is modified to create commands and
 * provide an observer interface for ArgoUML to receive them.
 * <p>
 * TODO: How does this relate to {@link org.argouml.kernel.DefaultUndoManager}?
 *
 * @author Bob Tarling
 */
public class DiagramUndoManager extends UndoManager {

    private static final Logger LOG =
        Logger.getLogger(UndoManager.class.getName());

    private boolean startChain;

    /**
     * Called when a new user interaction starts
     * @see org.tigris.gef.undo.UndoManager#startChain()
     */
    @Override
    public void startChain() {
        startChain = true;
    }


    @Override
    public boolean isGenerateMementos() {
        // TODO: This shouldn't depend on the current project, but for now
        // just make sure it's defined and that we have an undo manager
        Project p = ProjectManager.getManager().getCurrentProject();
        return super.isGenerateMementos() && p != null
                && p.getUndoManager() != null;
    }

    /**
     * @param memento the GEF memento
     * @see org.tigris.gef.undo.UndoManager#addMemento(org.tigris.gef.undo.Memento)
     */
    @Override
    public void addMemento(final Memento memento) {
        // TODO: This shouldn't be referencing the current project.  Instead
        // the appropriate UndoManager should have already been retrieved from
        // the correct project.
        Project p = ProjectManager.getManager().getCurrentProject();
        if (p != null) {
            org.argouml.kernel.UndoManager undo = p.getUndoManager();
            if (undo != null) {
                if (startChain) {
                    //TODO i18n: GEF needs to pass us back the description
                    // of what is being done.
                    undo.startInteraction("Diagram Interaction");
                }
                // TODO: I presume this would fix issue 5250 - but
                // GEF would need to be adapted:
//                if (!(memento instanceof SelectionMemento))
                undo.addCommand(new DiagramCommand(memento));

                startChain = false;
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        LOG.log(Level.INFO, "Adding property listener {0}", listener);

        super.addPropertyChangeListener(listener);
    }


    private class DiagramCommand
            extends org.argouml.kernel.AbstractCommand {

        private final Memento memento;

        DiagramCommand(final Memento theMemento) {
            this.memento = theMemento;
        }

        @Override
        public Object execute() {
            memento.redo();
            return null;
        }

        @Override
        public void undo() {
            memento.undo();
        }

        @Override
        public String toString() {
            return memento.toString();
        }
    }
}
