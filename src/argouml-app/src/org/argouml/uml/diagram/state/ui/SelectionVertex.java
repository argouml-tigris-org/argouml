/* $Id$
 *****************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.uml.diagram.state.ui;

import java.awt.Graphics;

import org.tigris.gef.presentation.Fig;

/**
 * A selection for a vertex (which can be an orthogonal state).
 * This acts the same as its superclass unless the vertex is in fact an
 * orthogonal state in which case extra handles are painted for moving
 * the region separators.
 *
 * @author Bob Tarling
 */
public class SelectionVertex extends SelectionState {

    /**
     * Construct a new SelectionState for the given Fig.
     *
     * @param f The given Fig.
     */
    public SelectionVertex(Fig f) {
	super(f);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}
