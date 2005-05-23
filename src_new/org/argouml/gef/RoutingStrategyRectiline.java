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



// File: FigEdgeRectiline.java
// Classes: FigEdgeRectiline
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.gef;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;


/** 
 * A Fig that paints edges between ports. This version
 * automatically routes a rectilinear edge. The routing is not very
 * good. It avoids the source and sink nodes and no other nodes. It is
 * basically case-analysis, and some of the cases are wrong or
 * missing. Anyway, the user can edit the edge by dragging
 * handles. The 0th and last handles are fixed in position so that
 * they stay connected to ports. If the user drags a handle next to a
 * fixed handle, a new vertex is automatically inserted.
 */

public class RoutingStrategyRectiline extends RoutingStrategy {

    /**
     * Instanciate a FigPoly with its rectilinear flag set. By default
     * the FigPoly is black and the FigEdge has now ArrowHeads.
     */
    protected Fig makeEdgeFig() {
        FigPoly res = new FigPoly(Color.black);
        res.setRectilinear(true);
        res.setFixedHandles(1);
        res.setFilled(false);
        return res;
    }


    /** Find the route that the edge should follow.  Basically case
     *  analysis to route around source and destination nodes.
     *  Needs-More-Work: A better algorithm would really be useful.
     *  Needs-More-Work: Sometimes the edge can get non-rectilinear. */
    public void computeRoute(FigEdgeRoutable edge) {
        if (!edge.getInitiallyLaidOut()) {
            layoutEdge(edge);
            edge.setInitiallyLaidOut(true);
        }
        FigPoly p = ((FigPoly) edge.getFig());
        
        Point srcPt, dstPt;
        
        Fig sourcePortFig = edge.getSourcePortFig();
        Fig destPortFig = edge.getDestPortFig();
        
        if (((FigEdgeRoutable)edge).getUseNearest()) {
            srcPt = sourcePortFig.connectionPoint(p.getPoint(1));
            dstPt = destPortFig.connectionPoint(p.getPoint(p.getNumPoints()-2));
        } else {
            srcPt = sourcePortFig.getCenter();
            dstPt = destPortFig.getCenter();
        }
        
        p.setEndPoints(srcPt, dstPt);
        edge.calcBounds();
    } /* end computeRoute */

  /** Internal function to actually compute the layout of the line if
   *  it has never been done on that line before. */
  protected void layoutEdge(FigEdgeRoutable edge) {
    int npoints = 0;
    int xpoints[] = new int[16];
    int ypoints[] = new int[16];
    
    Fig sourcePortFig = edge.getSourcePortFig();
    Fig destPortFig = edge.getDestPortFig();
    
    Point srcPt = sourcePortFig.getCenter();
    Point dstPt = destPortFig.getCenter();

    if (((FigEdgeRoutable)edge).getUseNearest()) {
      srcPt = sourcePortFig.connectionPoint(dstPt);
      dstPt = destPortFig.connectionPoint(srcPt);
      srcPt = sourcePortFig.connectionPoint(dstPt);
      dstPt = destPortFig.connectionPoint(srcPt);
    }
    
    Rectangle srcRR = edge.getSourceFigNode().routingRect();
    Rectangle dstRR = edge.getDestFigNode().routingRect();

    Object srcPort = sourcePortFig.getOwner();
    Object dstPort = destPortFig.getOwner();
    int srcSector = ((FigNode)edge.getSourceFigNode()).getPortSector(sourcePortFig);
    int dstSector = ((FigNode)edge.getDestFigNode()).getPortSector(destPortFig);

    // first decide what layout case we have
    Point srcRRPt = routingRectPoint(srcPt, srcRR, srcSector);
    Point dstRRPt = routingRectPoint(dstPt, dstRR, dstSector);

    if (srcSector == 1 || srcSector == -1) {
      xpoints[npoints] = srcPt.x; ypoints[npoints++] = srcPt.y; }
    xpoints[npoints] = srcPt.x; ypoints[npoints++] = srcPt.y;
    xpoints[npoints] = srcRRPt.x; ypoints[npoints++] = srcRRPt.y;

    npoints = npoints + tryRoute(dstRRPt.x, dstRRPt.y, npoints,
				 xpoints, ypoints,
				 srcRR, dstRR, srcSector, dstSector);

    xpoints[npoints] = dstRRPt.x; ypoints[npoints++] = dstRRPt.y;
    xpoints[npoints] = dstPt.x; ypoints[npoints++] = dstPt.y;
    //   if (dstSector == 1 || dstSector == -1) {
    // xpoints[npoints] = dstPt.x; ypoints[npoints++] = dstPt.y; }
    Polygon routePoly = new Polygon(xpoints, ypoints, npoints);
    ((FigPoly)edge.getFig()).setPolygon(routePoly);
  }

  /** Reply a point on the given routing rect that is "straight out"
   *  from the connection point in the proper direction. */
  protected Point routingRectPoint(Point p, Rectangle r, int sector) {
    switch (sector) {
    case -1: return new Point(p.x, r.y);
    case 2: return new Point(r.x, p.y);
    case 1: return new Point(p.x, r.y + r.height);
    case -2: return new Point(r.x + r.width, p.y);
    default: System.out.println("error, undefined sector!");
      return p;
    }
  }

  /** Try to find a route from the last point in (xs, ys) to point (x,
   *  y).  Try to avoid the given rectangles along the
   *  way. Needs-More-Work: should allow a vector of rectangles to
   *  avoid. */
  protected int tryRoute(int x, int y, int np, int xs[], int ys[],
			 Rectangle avoid1, Rectangle avoid2,
			 int srcSector, int dstSector) {
    if (np > 12 ) return 0;
    int fx = xs[np-1], fy = ys[np-1];
    if ((fx == x || fy == y) && segOK(fx, fy, x, y, avoid1, avoid2)) {
      xs[np] = x; ys[np++] = y;
      xs[np] = x; ys[np++] = y;
      return 1;
    }
    if (segOK(fx,fy,fx,y,avoid1,avoid2) && segOK(fx,y,x,y,avoid1,avoid2)) {
      xs[np] = fx; ys[np++] = y;
      xs[np] = x; ys[np++] = y;
      return 2;
    }
    if (segOK(fx,fy,x,fy,avoid1,avoid2) && segOK(x,fy,x,y,avoid1,avoid2)) {
      xs[np] = x; ys[np++] = fy;
      xs[np] = x; ys[np++] = y;
      return 2;
    }

    Point avoidPt = findAvoidPt(fx, fy, x, y, avoid1, avoid2);
    if ((srcSector==1 || srcSector==-1 || fy==avoidPt.y) && fx!=avoidPt.x) {
      xs[np] = avoidPt.x;
      ys[np++] = fy;
      return tryRoute(x,y,np,xs,ys,avoid1, avoid2, srcSector, dstSector) + 1;
    }
    if ((srcSector==2 || srcSector==-2 || fx==avoidPt.x) && fy != avoidPt.y) {
      xs[np] = fx;
      ys[np++] = avoidPt.y;
      return tryRoute(x,y,np,xs,ys,avoid1,avoid2,srcSector,dstSector) + 1;
    }
    return 0;
  }

  /** Find a point on the corner of one of the avoid rectangles that
   *  is a good intermediate point for the edge layout. */
  protected Point findAvoidPt(int fx, int fy, int x, int y,
			      Rectangle avoid1, Rectangle avoid2) {
    Point res = new Point(fx, fy);
    if (avoid1.x + avoid1.width < avoid2.x) res.x = avoid2.x;
    else if (avoid2.x + avoid2.width < avoid1.x) res.x = avoid1.x;
    else res.x = Math.min(avoid1.x, avoid2.x);

    if (avoid1.y + avoid1.height < avoid2.y) res.y = avoid2.y;
    else if (avoid2.y + avoid2.height < avoid1.y) res.y = avoid1.y;
    else res.y = Math.min(avoid1.y, avoid2.y);

    return res;
  }

  /** Reply true if the line segment from (x1, y1) to (x2, y2) does
   *  not intersect the given avoid rectangles. */
  protected boolean segOK(int x1, int y1, int x2, int y2,
			  Rectangle avoid1, Rectangle avoid2) {
    int xmin = Math.min(x1, x2);
    int xmax = Math.max(x1, x2);
    int ymin = Math.min(y1, y2);
    int ymax = Math.max(y1, y2);
    int rright = avoid1.x + avoid1.width;
    int rbot = avoid1.y + avoid1.height;
    if (x1 == x2) { // vertical
      if (x1 > avoid1.x && x1 < rright &&
	  ((ymin < avoid1.y && ymax > avoid1.y) ||
	   (ymin < rbot && ymax > rbot)))
	return false;
    }
    if (y1 == y2) { // horizontal
      if (y1 > avoid1.y && y1 < rbot &&
	  ((xmin < avoid1.x && xmax > avoid1.x) ||
	   (xmin < rright && xmax > rright)))
	return false;
    }
    rright = avoid2.x + avoid2.width;
    rbot = avoid2.y + avoid2.height;
    if (x1 == x2) { // vertical
      if (x1 > avoid2.x && x1 < rright &&
	  ((ymin < avoid2.y && ymax > avoid2.y) ||
	   (ymin < rbot && ymax > rbot)))
	return false;
    }
    if (y1 == y2) { // horizontal
      if (y1 > avoid2.y && y1 < rbot &&
	  ((xmin < avoid2.x && xmax > avoid2.x) ||
	   (xmin < rright && xmax > rright)))
	return false;
    }
    return true;
  }
} /* end class FigEdgeRectiline */
