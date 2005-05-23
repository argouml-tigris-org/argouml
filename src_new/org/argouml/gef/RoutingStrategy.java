// %1035290364839:org.tigris.gef.presentation%
package org.argouml.gef;

import java.awt.Graphics;

import org.tigris.gef.base.Globals;
import org.tigris.gef.base.PathConv;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.Handle;

/**
 * Abstract strategy for routing edge paths between ports.
 */
public abstract class RoutingStrategy {
    ////////////////////////////////////////////////////////////////
    // instance variables

    /** Fig presenting the edge's from-port . */
    private Fig _sourcePortFig;

    /** Fig presenting the edge's to-port. */
    private Fig _destPortFig;

    /** FigNode presenting the edge's from-port's parent node. */
    protected FigNode _sourceFigNode;

    /** FigNode presenting the edge's to-port's parent node. */
    protected FigNode _destFigNode;

    /** Fig that presents the edge. */
    protected Fig _fig;

    /** True when the FigEdgde should be drawn highlighted. */
    protected boolean _highlight = false;

    ////////////////////////////////////////////////////////////////
    // inner classes
    private class PathItem implements java.io.Serializable {
        Fig _fig;
        PathConv _path;

        PathItem(Fig f, PathConv pc) {
            _fig = f;
            _path = pc;
        }

        public PathConv getPath() {
            return _path;
        }

        public Fig getFig() {
            return _fig;
        }
    }

    /** Contruct a new FigEdge without any underlying edge. */
    public RoutingStrategy() {
        _fig = makeEdgeFig();
    }

    ////////////////////////////////////////////////////////////////
    // Routing related methods

    /**
     * Method to compute the route a FigEdge should follow.  By defualt
     * this does nothing. Sublcasses, like FigEdgeRectiline override
     * this method.
     */
    abstract public void computeRoute(FigEdgeRoutable edge);

    /** Abstract method to make the Fig that will be drawn for this
     *  FigEdge. In FigEdgeLine this method constructs a FigLine. In
     *  FigEdgeRectiline, this method constructs a FigPoly. */
    protected abstract Fig makeEdgeFig();


    public void paintHighlightLine(Graphics g, int x1, int y1, int x2, int y2) {
        g.setColor(Globals.getPrefs().getHighlightColor());    /* needs-more-work */
        double dx = (x2 - x1);
        double dy = (y2 - y1);
        double denom = Math.sqrt(dx * dx + dy * dy);
        if(denom == 0) {
            return;
        }

        double orthoX = dy / denom;
        double orthoY = -dx / denom;
        // needs-more-work: should fill poly instead
        for(double i = 2.0; i < 5.0; i += 0.27) {
            int hx1 = (int)(x1 + i * orthoX);
            int hy1 = (int)(y1 + i * orthoY);
            int hx2 = (int)(x2 + i * orthoX);
            int hy2 = (int)(y2 + i * orthoY);
            g.drawLine(hx1, hy1, hx2, hy2);
        }
    }
    
    /** When the user drags the handles, move individual points */
    public void setPoint(FigEdgeRoutable edge, Handle h, int mX, int mY) {
        edge.setPoint(h, mX, mY);
    }
    
    /**
     * Return true if the given Fig as an edge following some poly routing strategy.
     * @param f
     * @return
     */
    public static boolean isPolyStrategy(Fig f) {
        return (f instanceof FigEdgeRoutable && ((FigEdgeRoutable)f).isPolyRoutingStrategy());
    }

}    /* end class FigEdge */
