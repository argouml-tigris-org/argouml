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

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.StrictCompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.CopyToClipboardCommand;
import org.eclipse.emf.edit.command.PasteFromClipboardCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

/**
 * This class exposes protected methods from
 * {@link org.eclipse.uml2.uml.UMLUtil}
 * and adds additional util methods
 * 
 * @author Bogdan Pistol
 */
public class UMLUtil extends org.eclipse.uml2.uml.util.UMLUtil {

    public static EList<Property> getOwnedAttributes(Type type) {
        if (type instanceof AssociationClass) {
            return ((AssociationClass) type).getOwnedAttributes();
        } else if (type instanceof Association) {
            return ((Association) type).getOwnedEnds();
        } else {
            return org.eclipse.uml2.uml.util.UMLUtil.getOwnedAttributes(type);
        }
    }

    public static EList<Operation> getOwnedOperations(Type type) {
        return org.eclipse.uml2.uml.util.UMLUtil.getOwnedOperations(type);
    }
    
    public static boolean copy(EditingDomain editingDomain, Element source, Element destination) {
        Command copyToClipboard = CopyToClipboardCommand.create(editingDomain, source);
        Command pasteFromClipboard = PasteFromClipboardCommand.create(editingDomain, destination, null);
        StrictCompoundCommand copyCommand = new StrictCompoundCommand() {
            {
                isPessimistic = true;
            }
        };
        copyCommand.append(copyToClipboard);
        copyCommand.append(pasteFromClipboard);
        if (!copyCommand.canExecute()) {
            return false;
        } else {
            editingDomain.getCommandStack().execute(copyCommand);
            return true;
        }
    }

}
