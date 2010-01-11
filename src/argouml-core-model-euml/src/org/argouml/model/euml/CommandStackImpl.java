/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007,2008 Bogdan Pistol and other contributors
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//     * Neither the name of the project or its contributors may be used 
//       to endorse or promote products derived from this software without
//       specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE CONTRIBUTORS ``AS IS'' AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE CONTRIBUTORS BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

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
