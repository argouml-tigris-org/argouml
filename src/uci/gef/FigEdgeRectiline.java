// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: FigEdgeRectiline.java
// Classes: FigEdgeRectiline
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;

/** A Fig that paints edges between ports. This version
 *  automatically routes a rectilinear edge. The routing is not very
 *  good. It avoids the source and sink nodes and no other nodes. It is
 *  basically case-analysis, and some of the cases are wrong or
 *  missing. In any case, the user can edit the edge by dragging
 *  handles. The 0th and last handles are fixed in position so that
 *  they stay connected to ports. If the user drags a handle next to a
 *  fixed handle, a new vertex is automatically inserted.
 *
 * @see FigPoly */

public class FigEdgeRectiline extends FigEdge {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** True if the edge has been laid out automatically once. It will
   *  not be done automatically again since the user may have edited the
   *  edge and I dont want to undo that work. */
  protected boolean _initiallyLaidOut = false;

  ////////////////////////////////////////////////////////////////
  // FigEdge API

  /** Instanciate a FigPoly with its rectilinear flag set. */
  protected Fig makeEdgeFig() {
    FigPoly res = new FigPoly(Color.black);
    res.rectilinear(true);
    res.fixedHandles(1);
    res.setFilled(false);
    return res;
  }


  ////////////////////////////////////////////////////////////////
  // routing methods

  /** Find the route that the edge should follow.  Basically case
   *  analysis to route around source and destination nodes.
   *  Needs-More-Work: A better algorithm would really be useful.
   *  Needs-More-Work: Sometimes the edge can get non-rectilinear. */
  protected void computeRoute() {
    if (!_initiallyLaidOut) {
      layoutEdge();
      _initiallyLaidOut = true;
    }
    FigPoly p = ((FigPoly) _fig);

    Point srcPt, dstPt;
    
    if (_useNearest) {
      srcPt = _sourcePortFig.connectionPoint(p.getPoints(1));
      dstPt = _destPortFig.connectionPoint(p.getPoints(p.getNumPoints()-2));
    }
    else {
      srcPt = _sourcePortFig.center();
      dstPt = _destPortFig.center();
    }
    
    p.setEndPoints(srcPt, dstPt);
    calcBounds();
  } /* end computeRoute */

  /** Internal function to actually compute the layout of the line if
   *  it has never been done on that line before.
   *  <A HREF="../bugs.html#rectilinear_arc_constraints">
   *  <FONT COLOR=660000><B>BUG: rectilinear_arc_constraints</B></FONT></A>
   *  <A HREF="../bugs.html#rectilinear_arc_points">
   *  <FONT COLOR=660000><B>BUG: rectilinear_arc_points</B></FONT></A> */
  protected void layoutEdge() {
    int npoints = 0;
    int xpoints[] = new int[16];
    int ypoints[] = new int[16];
    Point srcPt = _sourcePortFig.center();
    Point dstPt = _destPortFig.center();

    if (_useNearest) {
      srcPt = _sourcePortFig.connectionPoint(dstPt);
      dstPt = _destPortFig.connectionPoint(srcPt);
      srcPt = _sourcePortFig.connectionPoint(dstPt);
      dstPt = _destPortFig.connectionPoint(srcPt);
    }
    
    Rectangle srcRR = _sourceFigNode.routingRect();
    Rectangle dstRR = _destFigNode.routingRect();

    Object srcPort = _sourcePortFig.getOwner();
    Object dstPort = _destPortFig.getOwner();
    int srcSector = _sourceFigNode.getPortSector(_sourcePortFig);
    int dstSector = _destFigNode.getPortSector(_destPortFig);

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
    ((FigPoly)_fig).polygon(routePoly);
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
