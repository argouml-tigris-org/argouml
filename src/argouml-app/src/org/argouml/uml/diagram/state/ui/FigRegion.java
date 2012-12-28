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

import java.awt.Dimension;
import org.tigris.gef.presentation.FigRect;

/**
 * Displays a region within a composite state
 */
public class FigRegion extends FigRect {

    /**
     * Construct a fig representing a region.
     * 
     * @param region owning UML element
     */
    public FigRegion(
            final Object region) {
        super(0, 0, 0, 0);
        setLineWidth(0);
        setOwner(region);
    }
    
    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(180, 80);
    }
}
