// $Id$
// Copyright (c) 2007, The ArgoUML Project
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//     * Neither the name of the ArgoUML Project nor the
//       names of its contributors may be used to endorse or promote products
//       derived from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE ArgoUML PROJECT ``AS IS'' AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE ArgoUML PROJECT BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.argouml.model.euml;

import org.argouml.model.ModelMemento;

public class CommandStackImpl extends ModelMemento {

    private EUMLModelImplementation modelImplementation;
    
    private static CommandStackImpl instance;
    
    public static final String COMMAND_STACK_UPDATE_EVENT = "COMMAND_STACK_CHANGED"; //$NON-NLS-1$

    private CommandStackImpl(EUMLModelImplementation modelImplementation) {
        this.modelImplementation = modelImplementation;
        modelImplementation.getEditingDomain().getCommandStack().flush();
    }
    
    public static ModelMemento getInstance(EUMLModelImplementation modelImplementation) {
        if (instance == null) {
            instance = new CommandStackImpl(modelImplementation);
        }
        return instance;
    }

    public boolean canRedo() {
        return modelImplementation.getEditingDomain().getCommandStack().canRedo();
    }

    public boolean canUndo() {
        return modelImplementation.getEditingDomain().getCommandStack().canUndo();
    }

    public String getRedoLabel() {
        return canRedo() ? modelImplementation.getEditingDomain().getCommandStack().getRedoCommand().getLabel()
                : null;
    }

    public String getUndoLabel() {
        return canUndo() ? modelImplementation.getEditingDomain().getCommandStack().getUndoCommand().getLabel()
                : null;
    }

    public void redo() {
        modelImplementation.getEditingDomain().getCommandStack().redo();
    }

    public void undo() {
        modelImplementation.getEditingDomain().getCommandStack().undo();
    }

}
