// $Id$
/***********************************************************************
 * Copyright (c) 2007,2010 Bogdan Pistol and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bogdan Pistol - initial API and implementation
 ***********************************************************************/
package org.argouml.model.euml;

//import org.argouml.model.CommandStack;

/**
 * CommandStack implementation to support Undo/Redo.
 * 
 */
public class CommandStackImpl /*implements CommandStack*/ {

    private EUMLModelImplementation modelImplementation;

    public CommandStackImpl(EUMLModelImplementation implementation) {
        modelImplementation = implementation;
        implementation.getEditingDomain().getCommandStack().flush();
    }

    public boolean canRedo() {
        return modelImplementation.getEditingDomain().getCommandStack()
                .canRedo();
    }

    public boolean canUndo() {
        return modelImplementation.getEditingDomain().getCommandStack()
                .canUndo();
    }

    public String getRedoLabel() {
        return canRedo() ? modelImplementation.getEditingDomain()
                        .getCommandStack().getRedoCommand().getLabel()
                : null;
    }

    public String getUndoLabel() {
        return canUndo() ? modelImplementation.getEditingDomain()
                        .getCommandStack().getUndoCommand().getLabel()
                : null;
    }

    public boolean isCommandStackCapabilityAvailable() {
        return true;
    }

    public void redo() {
        modelImplementation.getEditingDomain().getCommandStack().redo();
    }

    public void undo() {
        modelImplementation.getEditingDomain().getCommandStack().undo();
    }

}
