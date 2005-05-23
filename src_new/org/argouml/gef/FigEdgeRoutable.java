// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

// File: FigEdgePoly.java
// Classes: FigEdgePoly
// Original Author: agauthie@ics.uci.edu
// $Id$

package org.argouml.gef;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.Handle;



/**
 * A Fig that paints edges between ports. The user can edit the edge by
 * dragging handles. The rerouting caused by such dragging is managed
 * by the RoutingStrategy resistered in the constructor.
 */

public class FigEdgeRoutable extends org.tigris.gef.presentation.FigEdge {

    private RoutingStrategy routingStrategy;
    
    /** True if the edge has been laid out automatically once. It will
     *  not be done automatically again since the user may have edited the
     *  edge and I dont want to undo that work.
     */
    private boolean _initiallyLaidOut = false;

    ////////////////////////////////////////////////////////////////
    // FigEdge API
    public FigEdgeRoutable(RoutingStrategy routingStrategy) {
        this.routingStrategy = routingStrategy;
        _fig = routingStrategy.makeEdgeFig();
    }
    
    public RoutingStrategy getRoutingStrategy() {
        return routingStrategy;
    }
    
    /**
     * @deprecated This method has been introduced temporarily while FigEdgePoly and RoutingStrategyPoly
     * exist at the same time. It will be removed in future and should be considereed deprecated.
     */
    public boolean isPolyRoutingStrategy() {
        return (routingStrategy instanceof RoutingStrategyPoly);
    }
    
    ////////////////////////////////////////////////////////////////
    // FigEdge API

    /** Instantiate a FigPoly with its rectilinear flag set. By default
     *  the FigPoly is black and the FigEdge has no ArrowHeads. */
    protected Fig makeEdgeFig() {
        if (routingStrategy == null) {
            return null;
        }
        return routingStrategy.makeEdgeFig();
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public boolean getInitiallyLaidOut() {
        return _initiallyLaidOut;
    }
    
    public boolean getUseNearest() {
        return _useNearest;
    }

    public void setUseNearest(boolean b) {
        _useNearest = b;
    }

    public void setInitiallyLaidOut(boolean b) {
        _initiallyLaidOut = b;
    }

    ////////////////////////////////////////////////////////////////
    // routing methods

    /** 
     * Compute the route that the edge should follow.
     * Delegated to the routing strategy.
     */
    public void computeRoute() {
        routingStrategy.computeRoute(this);
    } /* end computeRoute */

    public void moveVertex(Handle h, int x, int y, boolean ov) {
        routingStrategy.moveVertex(this, h, x, y, ov);
    }

    /** When the user drags the handles, move individual points */
    public void setPoint(Handle h, int mX, int mY) {
        moveVertex(h, mX, mY, false);
        calcBounds();
    }

    /** Add a point to this polygon. Fires PropertyChange with "bounds". */
    public void insertPoint(int i, int x, int y) {
        FigPoly p = ((FigPoly) _fig);
        p.insertPoint(i, x, y);
    }

    private static Handle _TempHandle = new Handle(0);

    /** 
     * Set the end points of this edge, regardless of the number of
     * fixed handles. This is used when nodes move.
     */
    public void setEndPoints(Point start, Point end) {
        FigPoly p = ((FigPoly) _fig);
        while (p.getNumPoints() < 2) {
            p.addPoint(start);
        }
        synchronized (_TempHandle) {
            _TempHandle.index = 0;
            moveVertex(_TempHandle, start.x, start.y, true);
            _TempHandle.index = p.getNumPoints() - 1;
            moveVertex(_TempHandle, end.x, end.y, true);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (_highlight) {
            FigPoly f = (FigPoly) getFig();
            int nPoints = f.getNumPoints();
            int xs[] = f.getXs();
            int ys[] = f.getYs();
            for (int i = 1; i < nPoints; i++) {
                paintHighlightLine(g, xs[i - 1], ys[i - 1], xs[i], ys[i]);
            }
        }
    }
} /* end class FigEdgePoly */
