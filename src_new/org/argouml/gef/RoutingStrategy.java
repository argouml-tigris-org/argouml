// %1035290364839:org.tigris.gef.presentation%
package org.argouml.gef;

import java.awt.Graphics;
import java.util.Vector;

import org.tigris.gef.base.Globals;
import org.tigris.gef.base.PathConv;
import org.tigris.gef.presentation.ArrowHead;
import org.tigris.gef.presentation.ArrowHeadNone;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
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

    /** True if the FigEdge should be drawn from the nearest point of
     *  each port Fig. */
    protected boolean _useNearest = false;

    /** True when the FigEdgde should be drawn highlighted. */
    protected boolean _highlight = false;

    /** The ArrowHead at the start of the line */
    protected ArrowHead _arrowHeadStart = ArrowHeadNone.TheInstance;

    /** The ArrowHead at the end of the line */
    protected ArrowHead _arrowHeadEnd = ArrowHeadNone.TheInstance;

    /** The items that are accumulated along the path, a vector. */
    protected Vector _pathItems = new Vector();

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
    abstract public void computeRoute(FigEdge edge);

    /** Abstract method to make the Fig that will be drawn for this
     *  FigEdge. In FigEdgeLine this method constructs a FigLine. In
     *  FigEdgeRectiline, this method constructs a FigPoly. */
    protected abstract Fig makeEdgeFig();

    /** Paint this FigEdge.  Needs-more-work: take Highlight into account */
    public void paint(FigEdge edge, Graphics g) {
        _fig.paint(g);
        paintArrowHeads(g);
        paintPathItems(g);
    }
    
    /** Paint any labels that are located relative to this FigEdge. */
    protected void paintPathItems(Graphics g) {
        Vector pathVec = getPathItemsRaw();
        for(int i = 0; i < pathVec.size(); i++) {
            PathItem element = (PathItem)pathVec.elementAt(i);
            Fig f = element.getFig();
            f.paint(g);
        }
    }

    /** Return the vector of path items on this FigEdge. */
    public Vector getPathItemsRaw() {
        return _pathItems;
    }

    ////////////////////////////////////////////////////////////////
    // display methods

    /** Paint ArrowHeads on this FigEdge. Called from paint().
     *  Determines placement and orientation by using
     *  pointAlongPerimeter(). */
    protected void paintArrowHeads(Graphics g) {
        _arrowHeadStart.paintAtHead(g, _fig);
        _arrowHeadEnd.paintAtTail(g, _fig);
        //     _arrowHeadStart.paint(g, pointAlongPerimeter(5), pointAlongPerimeter(0));
        //     _arrowHeadEnd.paint(g, pointAlongPerimeter(getPerimeterLength() - 6),
        //          pointAlongPerimeter(getPerimeterLength() - 1));
    }

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
    public void setPoint(FigEdge edge, Handle h, int mX, int mY) {
        edge.setPoint(h, mX, mY);
    }

}    /* end class FigEdge */
