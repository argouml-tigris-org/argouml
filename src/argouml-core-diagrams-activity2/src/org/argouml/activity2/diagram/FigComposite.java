package org.argouml.activity2.diagram;

import java.awt.Rectangle;

import org.tigris.gef.presentation.FigGroup;

public class FigComposite extends FigGroup {
    
    private Rectangle bounds;
    
    private static final int MARGIN = 0;
    
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
    
    protected Rectangle getBoundsImpl() {
        return bounds;
    }
}
