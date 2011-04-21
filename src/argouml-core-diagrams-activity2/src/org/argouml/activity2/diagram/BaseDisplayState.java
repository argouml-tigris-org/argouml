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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;
import org.argouml.notation2.NotationType;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;

abstract class BaseDisplayState extends FigGroup
        implements StereotypeDisplayer, NameDisplayer, PropertyChangeListener {
    
    private static final Logger LOG = Logger.getLogger(BaseDisplayState.class);

    private final DiagramElement bigPort;
    private final DiagramElement nameDisplay;
    private Rectangle bounds;
    
    public BaseDisplayState(
            final Rectangle rect,
            final Color lineColor,
            final Color fillColor,
            final Object modelElement,
            final DiagramSettings settings) {
        nameDisplay = new FigNotation(
                modelElement,
                new Rectangle(0, 0, 0, 0),
                settings,
                NotationType.NAME);
        bigPort = createBigPort(rect, lineColor, fillColor);
        addFig((Fig) bigPort);
        addFig((Fig) getNameDisplay());
        ((Fig) nameDisplay).addPropertyChangeListener(this);
        setBounds(rect);
    }
    
    public DiagramElement getStereotypeDisplay() {
        return null;
    }

    public DiagramElement getNameDisplay() {
        return nameDisplay;
    }

    abstract DiagramElement createBigPort(
            final Rectangle rect,
            final Color lineColor,
            final Color fillColor);
    
    DiagramElement getPort() {
        return bigPort;
    }
    
    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getSource() == getNameDisplay() && pce.getPropertyName().equals("bounds")) {
            // The size of the name has changed. Check if we need to make the
            // node bigger so that it contains all its children.
            Rectangle textBounds = (Rectangle) pce.getNewValue();
            int notationEndX = textBounds.x + textBounds.width;
            int thisEndX = getBounds().x + getBounds().width;
            LOG.debug("Got the event notation right = " + notationEndX + " container right = " + thisEndX);
            if (notationEndX > thisEndX) {
                // TODO: Why do we not get here?
                // The container seems to be growing by itself but we don't see that visibly.
                LOG.info("text has grown too wide so redrawing parent");
                // TODO: layout our children and calculate our bounds.
                calcBounds();
            }
        }
        super.propertyChange(pce);
    }
    
    // TODO: Move an empty implementation to FigGroup in GEF
    protected void positionChildren() {
        Rectangle myBounds = getBounds();
        getPort().setBounds(myBounds);
        
        final Dimension nameDim = getNameDisplay().getMinimumSize();
        final int nameWidth = nameDim.width;
        final int nameHeight = nameDim.height;
        
        final int nx = bounds.x + (bounds.width - nameWidth) /2;
        final int ny = bounds.y + (bounds.height - nameHeight) /2;
        getNameDisplay().setLocation(nx, ny);
    }
    
    
    //
    // !! TODO: All code below here is duplicated in FigBaseNode. The reason
    // is the GEF defect - http://gef.tigris.org/issues/show_bug.cgi?id=358
    // Once we have taken a release of GEF with that fix we can remove this
    // code.
    //
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

