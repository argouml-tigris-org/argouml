// $Id: ChangeCommand.java 13359 2007-08-15 08:23:10Z b00__1 $
// Copyright (c) 2007, ArgoUML Project
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

import org.eclipse.emf.common.notify.impl.NotificationImpl;

/**
 * A ChangeCommand that updates its label.
 * <p>
 * The label of this command can contain the character '#' that will be replaced
 * with information about an object when the label is returned.
 * 
 * @author Bogdan Pistol
 */
public class ChangeCommand extends
        org.eclipse.uml2.common.edit.command.ChangeCommand {

    private Object objects[];

    private EUMLModelImplementation modelImpl;

    public ChangeCommand(EUMLModelImplementation modelImplementation,
            Runnable runnable, String label) {
        super(modelImplementation.getEditingDomain(), runnable, label);
        modelImpl = modelImplementation;
    }

    public ChangeCommand(EUMLModelImplementation modelImplementation,
            Runnable runnable, String label, Object... objects) {
        super(modelImplementation.getEditingDomain(), runnable, label);
        if (!isValid(label, objects)) {
            throw new IllegalArgumentException(
                    "The label is not compatible with the objects"); //$NON-NLS-1$
        }
        this.objects = objects;
        modelImpl = modelImplementation;
    }

    public void setObjects(Object... objects) {
        if (!isValid(label, objects)) {
            throw new IllegalArgumentException(
                    "The label is not compatible with the objects"); //$NON-NLS-1$
        }
        this.objects = objects;
        modelImpl.getModelEventPump().notifyChanged(
                new NotificationImpl(
                        ModelEventPumpEUMLImpl.COMMAND_STACK_UPDATE, false,
                        false));
    }

    @Override
    public String getLabel() {
        if (objects == null) {
            return label;
        }
        StringBuilder sb = new StringBuilder();
        int j = 0;
        for (int i = 0; i < label.length(); i++) {
            if (label.charAt(i) == '#') {
                sb.append(UMLUtil.toString(objects[j]));
                j++;
            } else {
                sb.append(label.charAt(i));
            }
        }
        return sb.toString();
    }

    private boolean isValid(String label, Object objects[]) {
        if (objects == null) {
            return true;
        }
        int i = 0;
        for (int j = 0; j < label.length(); j++) {
            if (label.charAt(j) == '#') {
                i++;
            }
        }
        return i == objects.length;
    }

}
