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

import org.argouml.notation2.NotationType;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;

abstract class BaseDisplayState extends FigGroup
        implements StereotypeDisplayer, NameDisplayer, PropertyChangeListener {
    
    private final DiagramElement bigPort;
    private final DiagramElement nameDisplay;
    private Rectangle bounds;
    private static final int PADDING = 2;
    private static final int MIN_WIDTH = 90;
    
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
        if (pce.getSource() == getNameDisplay()
                && pce.getPropertyName().equals("bounds")) {
            // The size of the name has changed. Check if we need to make the
            // node bigger so that it contains all its children.
            Rectangle textBounds = (Rectangle) pce.getNewValue();
            Rectangle thisBounds = getBounds();
            int notationEndX =
                textBounds.x + textBounds.width + getRightMargin();
            int thisEndX = thisBounds.x + thisBounds.width;
            if (notationEndX > thisEndX) {
                thisBounds.width =
                    getLeftMargin() + textBounds.width + getRightMargin();
                setBounds(thisBounds);
                // TODO: We have noticed that the child fig inside for notation
                // has grown beyond our bounds so we change the size of ourself
                // to encompass it. We should now inform our own parent that we
                // have changed size so that it can fit us.
                // Maybe we need a separate event mechanism for this as
                // property change with bounds can happen for many reasons and
                // may cause problems with cycles (e.g. our parent changes
                // size so we change, we tell our parent we changed so it
                // redraws and tells us to change, we change and tell our
                // parent we changed.....) Events are maybe OTT - we could just
                // call our parent directly.
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
        
        final int nx = bounds.x + getLeftMargin()
            + (bounds.width - (nameWidth + getLeftMargin() + getRightMargin()))
            / 2;
        final int ny = bounds.y + getTopMargin()
            + (bounds.height - nameHeight - getTopMargin() - getBottomMargin())
            / 2;
        getNameDisplay().setLocation(nx, ny);
    }
    
    @Override
    public Dimension getMinimumSize() {
        
        final Dimension nameDim = getNameDisplay().getMinimumSize();
        int width = nameDim.width;
        int height = nameDim.height;
        if (getStereotypeDisplay() != null) {
            final Dimension stereoDim = getStereotypeDisplay().getMinimumSize();
            width += Math.max(stereoDim.width, nameDim.width);
            height += (stereoDim.height - 2);
        }
        
        int w = width + getRightMargin() + getLeftMargin();
        final int h = height + getTopMargin() + getBottomMargin();
        w = Math.max(w, MIN_WIDTH); // the width needs to be > the height
        return new Dimension(w, h);
    }
    
    protected int getRightMargin() {
        return PADDING;
    }
    
    protected int getLeftMargin() {
        return PADDING;
    }
    
    protected int getTopMargin() {
        return PADDING;
    }
    
    protected int getBottomMargin() {
        return PADDING;
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

        final int ww;
        // Refuse to set bounds below the minimum width
        final int minWidth = getMinimumSize().width;
        if (w < minWidth) {
            ww = minWidth;
        } else {
            ww = w;
        }
        final int hh;
        final int minHeight = getMinimumSize().height;
        if (h < minHeight) {
            hh = minHeight;
        } else {
            hh = h;
        }
        
        final Rectangle oldBounds = getBounds();
        bounds = new Rectangle(x, y, ww, hh);
        
        if (oldBounds.equals(bounds)) {
            return;
        }
        
        _x = x;
        _y = y;
        _w = ww;
        _h = hh;
        
        positionChildren();
        
        firePropChange("bounds", oldBounds, bounds);
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
