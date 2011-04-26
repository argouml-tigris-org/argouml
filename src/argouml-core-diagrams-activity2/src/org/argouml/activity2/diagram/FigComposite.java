package org.argouml.activity2.diagram;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigGroup;

public class FigComposite extends FigGroup {
    
    private Rectangle bounds;
    
    private static final int MARGIN = 0;
    
    public FigComposite(
            final Object owner,
            final Rectangle bounds,
            final DiagramSettings settings) {
        this.bounds = bounds;
        setOwner(owner);
    }
    
    
    int getRightMargin() {
        return MARGIN;
    }

    int getLeftMargin() {
        return MARGIN;
    }
    
    int getTopMargin() {
        return MARGIN;
    }
    
    int getBottomMargin() {
        return MARGIN;
    }
    
    protected void positionChildren() {
        
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
    
    
    /**
     * This is called to rearrange the contents of the Fig when a childs
     * minimum size means it will no longer fit. If this group also has
     * a parent and it will no longer fit that parent then control is
     * delegated to that parent.
     */
    public void calcBounds() {
        final Dimension min = getMinimumSize();
        if (getGroup() != null
                && (getBounds().height < min.height
                        || getBounds().width < min.width)) {
            ((FigGroup) getGroup()).calcBounds();
        } else {
            int maxw = Math.max(getWidth(), min.width);
            int maxh = Math.max(getHeight(), min.height);
            setSize(maxw, maxh);
        }
    }
}
