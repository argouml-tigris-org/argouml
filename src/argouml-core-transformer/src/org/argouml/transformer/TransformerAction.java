/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *******************************************************************************
 */

package org.argouml.transformer;


import org.argouml.kernel.Project;
import org.argouml.ui.UndoableAction;

/**
 * This class guarantees a similar layout for all transformer menu items.
 *
 * @author mvw
 */
class TransformerAction extends UndoableAction {
    private Project project;
    private Object source;
    
    /**
     * Constructor.
     */
    TransformerAction(String s) {
        super(s);
//        Object icon = ResourceLoaderWrapper.lookupIcon(s);
//        putValue(Action.NAME, Translator.localize(s));
//        putValue(Action.SMALL_ICON, icon);
//        // Set the tooltip string:
//        putValue(Action.SHORT_DESCRIPTION, Translator.localize(s));
    }
    
    TransformerAction(String nameKey, Project project, Object sourceModelElement) {
        this(nameKey);
        this.project = project;
        source = sourceModelElement;
    }
    
    protected Object getSource() {
        return source;
    }

    protected Project getProject() {
        return project;
    }

}
