// $Id$
/*******************************************************************************
 * Copyright (c) 2007,2010 Bogdan Pistol and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bogdan Pistol - initial API and implementation
 *******************************************************************************/
package org.argouml.model.euml;

import org.eclipse.emf.common.notify.impl.NotificationImpl;

/**
 * A ChangeCommand that updates its label.
 * <p>
 * The label of this command can contain the character '#' that will be replaced
 * with information about an object when the label is returned.
 * <p>
 * TODO: Switch this to use Java string formatting instead of private # notation?
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
                    "The label is not compatible with the # of objects"); //$NON-NLS-1$
        }
        this.objects = objects;
        modelImpl = modelImplementation;
    }

    /**
     * TODO: Document! What does this do?  Interacts with event pump in some manner
     * 
     * @param objects new objeccts
     */
    public void setObjects(Object... objects) {
        if (!isValid(label, objects)) {
            throw new IllegalArgumentException(
                    "The label is not compatible with the # of objects"); //$NON-NLS-1$
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
