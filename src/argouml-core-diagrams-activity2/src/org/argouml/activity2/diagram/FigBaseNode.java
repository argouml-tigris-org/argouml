/* $Id: $
 *****************************************************************************
 * Copyright (c) 2010-2011 Contributors - see below
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
import org.tigris.gef.presentation.FigNode;

/**
 * The Fig for all node diagram elements. All specialist diagram elements
 * decorate this to get specialist behaviour 
 * @author Bob Tarling
 */
class FigBaseNode extends FigNode implements DiagramNode {

    private BaseDisplayState displayState;
    private final DiagramSettings settings;
    private DiagramElement nameDiagramElement;
    
    private Rectangle bounds;
    
    /**
     * Constructor a new FigBaseNode
     * 
     * @param owner the owning UML element
     * @param bounds rectangle describing bounds
     * @param settings rendering settings
     */
    FigBaseNode(final Object owner, final Rectangle bounds,
            final DiagramSettings settings) {
        super(owner);
        this.bounds = bounds;
        this.settings = settings;
    }
    
    void setDisplayState(BaseDisplayState displayState) {
        this.displayState = displayState;
        displayState.setOwner(getOwner());
        addFig(displayState);
    }
    
    @Override
    public boolean isDragConnectable() {
        return false;
    }
    
    @Override
    public Dimension getMinimumSize() {
        return displayState.getMinimumSize();
    }

    public void setNameDiagramElement(DiagramElement name) {
    }

    // TODO: Move an empty implementation to FigGroup in GEF
    protected void positionChildren() {
        Rectangle myBounds = getBounds();
        displayState.setBounds(myBounds);
//      calcBounds();
        updateEdges();
    }
    
    @Override
    protected void setBoundsImpl(
            final int x,
            final int y,
            final int w,
            final int h) {

        final Rectangle oldBounds = getBounds();
        
        bounds = new Rectangle(x, y, w, h);
        _x = x;
        _y = y;
        _w = w;
        _h = h;
        
        positionChildren();
        
        if (!oldBounds.equals(getBounds())) {
            firePropChange("bounds", oldBounds, bounds);
        }
    }

    protected Rectangle getBoundsImpl() {
        return bounds;
    }
    
    /**
     * Change the position of the object from where it is to where it is plus dx
     * and dy. Often called when an object is dragged. This could be very useful
     * if local-coordinate systems are used because deltas need less
     * transforming... maybe. Fires property "bounds".
     */
    protected void translateImpl(int dx, int dy) {
        if (dx ==0 || dy == 0) {
            return;
        }
        Rectangle oldBounds = getBounds();
        Rectangle newBounds = new Rectangle(
                oldBounds.x + dx,
                oldBounds.y + dy,
                oldBounds.width,
                oldBounds.height);
        setBounds(newBounds);
    }
}
