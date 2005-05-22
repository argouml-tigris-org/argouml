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

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.Handle;


/** A Fig that paints edges between ports. This version
 *  automatically routes a rectilinear edge. The routing is not very
 *  good. It avoids the source and sink nodes and no other nodes. It is
 *  basically case-analysis, and some of the cases are wrong or
 *  missing. Anyway, the user can edit the edge by dragging
 *  handles. The 0th and last handles are fixed in position so that
 *  they stay connected to ports. If the user drags a handle next to a
 *  fixed handle, a new vertex is automatically inserted.
 */

public class FigEdge extends org.tigris.gef.presentation.FigEdge {

    private RoutingStrategy routingStrategy;
    
    /** True if the edge has been laid out automatically once. It will
     *  not be done automatically again since the user may have edited the
     *  edge and I dont want to undo that work.
     */
    private boolean _initiallyLaidOut = false;

    ////////////////////////////////////////////////////////////////
    // FigEdge API
    public FigEdge(RoutingStrategy routingStrategy) {
        this.routingStrategy = routingStrategy;
    }
    
    /** Instantiate a FigPoly with its rectilinear flag set. By default
     *  the FigPoly is black and the FigEdge has no ArrowHeads. */
    protected Fig makeEdgeFig() {
        return routingStrategy.makeEdgeFig();
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public void setInitiallyLaidOut(boolean b) {
        _initiallyLaidOut = b;
    }

    ////////////////////////////////////////////////////////////////
    // routing methods

    /** Find the route that the edge should follow.  Basically case
     *  analysis to route around source and destination nodes.
     *  Needs-More-Work: A better algorithm would really be useful.
     *  Needs-More-Work: Sometimes the edge can get non-rectilinear. */
    public void computeRoute() {
        routingStrategy.computeRoute(this);
    } /* end computeRoute */

    /** When the user drags the handles, move individual points */
    public void setPoint(Handle h, int mX, int mY) {
        routingStrategy.setPoint(this, h, mX, mY);
    }

    /** Add a point to this polygon. Fires PropertyChange with "bounds". */
    public void insertPoint(int i, int x, int y) {
        FigPoly p = ((FigPoly) _fig);
        p.insertPoint(i, x, y);
    }

    private static Handle _TempHandle = new Handle(0);

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
