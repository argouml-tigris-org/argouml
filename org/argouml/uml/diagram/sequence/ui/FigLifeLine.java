/**
 * 
 */
package org.argouml.uml.diagram.sequence.ui;

import java.awt.Color;
import java.awt.Dimension;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;

class FigLifeLine extends FigGroup {

    final static int WIDTH = 20;
    final static int HEIGHT = 1000;
    
    private FigRect rect;
    private FigLine line;
    
    FigLifeLine(int x, int y) {
        super();
        rect = new FigRect(x, y, WIDTH, HEIGHT);
        rect.setFilled(false);
        rect.setLineWidth(0);
        line = new FigLine(x + WIDTH / 2, y, x+ WIDTH / 2, HEIGHT, Color.black);
        line.setDashed(true);
        addFig(rect);
        addFig(line);
    }
    
    public Dimension getMinimumSize() {
        return new Dimension(20, 100);
    }
    
    public void setBoundsImpl(int x, int y, int w, int h) {
        rect.setBounds(x, y, WIDTH, h);
        line.setX(x + w / 2);
        calcBounds();
    }
    
    /**
     * @see org.tigris.gef.presentation.Fig#calcBounds()
     */
    public void calcBounds() {
        _x = rect.getX();
        _y = rect.getY();
        _w = rect.getWidth();
        _h = rect.getHeight();
        firePropChange("bounds", null, null);
    }

}