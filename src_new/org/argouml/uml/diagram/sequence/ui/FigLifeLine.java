/**
 * 
 */
package org.argouml.uml.diagram.sequence.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;

class FigLifeLine extends FigGroup {

    final static int WIDTH = 20;
    final static int HEIGHT = 1000;
    
    private FigRect rect;
    private FigLine line;
    
    /**
     * The set of activation figs.
     */
    private Set activationFigs;
    
    FigLifeLine(int x, int y) {
        super();
        rect = new FigRect(x, y, WIDTH, HEIGHT);
        rect.setFilled(false);
        rect.setLineWidth(0);
        line = new FigLine(x + WIDTH / 2, y, x+ WIDTH / 2, HEIGHT, Color.black);
        line.setDashed(true);
        addFig(rect);
        addFig(line);
        activationFigs = new HashSet();
    }
    
    public Dimension getMinimumSize() {
        return new Dimension(20, 100);
    }
    
    public void setBoundsImpl(int x, int y, int w, int h) {
        rect.setBounds(x, y, WIDTH, h);
        line.setLocation(x + w / 2, y);
        for (Iterator figIt = getFigs().iterator(); figIt.hasNext();) {
            Fig fig = (Fig) figIt.next();
            if (activationFigs.contains(fig)) {
                fig.setLocation(getX(), y - getY() + fig.getY());
            }
        }
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

    final void removeActivations() {
        List activations = new ArrayList(activationFigs);
        activationFigs.clear();
        for (Iterator it = activations.iterator();
              it.hasNext();
         ) {
            removeFig((Fig) it.next());
    }
        calcBounds();
    }

    final void addActivationFig(Fig f) {
        addFig(f);
        activationFigs.add(f);
    }

    /**
     * Removes the fig from both the figs list as from the
     * activationFigs set.  This insures
     * that removal will indeed remove all 'pointers' to the object.<p>
     *
     * @see org.tigris.gef.presentation.FigGroup#removeFig(Fig)
     */
    public void removeFig(Fig f) {
        super.removeFig(f);
        activationFigs.remove(f);
    }
}