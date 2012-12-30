/* $Id$
 *****************************************************************************
 * Copyright (c) 2011-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.uml.diagram.static_structure.ui;

import javax.swing.Icon;

import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.presentation.Fig;

/**
 * @author Bob Tarling
 */
public class SelectionCompartmentText extends SelectionNodeClarifiers2 {

    /**
     * Construct a new SelectionClass for the given Fig.
     *
     * @param f The given Fig.
     */
    public SelectionCompartmentText(Fig f) { 
        super(f);
    }

    @Override
    protected Icon[] getIcons() {
        return null;
    }
    
    @Override
    protected String getInstructions(int index) {
        return "";
    }

    @Override
    protected Object getNewNodeType(int i) {
        return null;
    }

    @Override
    protected Object getNewEdgeType(int i) {
        return null;
    }

    @Override
    protected boolean isDraggableHandle(int index) {
        return false;
    }
}
