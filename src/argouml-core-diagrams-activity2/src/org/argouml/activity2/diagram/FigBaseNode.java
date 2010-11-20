/* $Id: $
 *****************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.activity2.diagram;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigNodeModelElement;

/**
 * The Fig for all node diagram elements. All specialist diagram elements
 * decorate this to get specialist behaviour 
 * @author Bob Tarling
 */
class FigBaseNode extends FigNodeModelElement {

    private BaseDisplayState displayState;
    
    /**
     * Constructor a new FigBaseNode
     * 
     * @param owner the owning UML element
     * @param bounds rectangle describing bounds
     * @param settings rendering settings
     */
    FigBaseNode(final Object owner, final Rectangle bounds,
            final DiagramSettings settings) {
        super(owner, bounds, settings);
        addFig(getBigPort());
    }
    
    void setDisplayState(BaseDisplayState displayState) {
        this.displayState = displayState;
        if (getBigPort() != null) {
            removeFig(getBigPort());
        }
        setBigPort(displayState.getBigPort());
        addFig(displayState.getBigPort());
    }
    
    @Override
    public Dimension getMinimumSize() {
        return displayState.getMinimumSize();
    }
}
