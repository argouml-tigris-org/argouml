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

// File: Geometry.java
// Classes: Geometry
// Orginal Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.lang.Math;
import java.awt.*;

/** A library of functions that do geometric opeations.
 *  Needs-More-Work: many of these are not done yet or not used
 *  yet. */

public class Geometry {

  public static void ptClosestTo(Rectangle r, Point p, Point res) {
    int x1 = Math.min(r.x, r.x + r.width);
    int y1 = Math.min(r.y, r.y + r.height);
    int x2 = Math.max(r.x, r.x + r.width);
    int y2 = Math.max(r.y, r.y + r.height);
    int c = 0;
    if (p.x < x1) c = 0; else if (p.x > x2) c = 2; else c = 1;
    if (p.y < y1) c += 0; else if (p.y > y2) c += 6; else c += 3;
    System.out.println("Case " + c);
    switch (c) {
    case 0: res.x = x1;  res.y = y1;  return; // above, left
    case 1: res.x = p.x; res.y = y1;  return; // above
    case 2: res.x = x2;  res.y = y1;  return; // above, right
    case 3: res.x = x1;  res.y = p.y; return; // left
    case 4: res.x = p.x; res.y = p.y; return; // inside rect
    case 5: res.x = x2;  res.y = p.y; return; // right
    case 6: res.x = x1;  res.y = y2;  return; // below, left
    case 7: res.x = p.x; res.y = y2;  return; // below
    case 8: res.x = x2;  res.y = y2;  return; // below right
    }
  }

  public static Point ptClosestTo(Rectangle r, Point p) {
    Point res = new Point(0,0);
    ptClosestTo(r, p, res);
    return res;
  }

  public static double segmentAngle(Point p1, Point p2) {
    if (p2.x == p1.x && p2.y <= p1.y) return 90;
    if (p2.x == p1.x && p2.y > p1.y) return 270;
    if (p2.y == p1.y && p2.x > p1.x) return 0;
    if (p2.y == p1.y) return 180;
    int dx = p2.x - p1.x; int dy = p2.y = p1.y;
    double m = dy / dx;
    double a = Math.atan(m);
    if (dx > 0) return a; else return -a;
  }

  public static void ptClosestTo(int x1, int y1, int x2, int y2,
				 Point p, Point res) {
    // segment is a point
    if (y1 == y2 && x1 == x1) { res.x = x1; res.y = y1; return; }
    // segment is horizontal
    if (y1 == y2) { res.y = y1; res.x = mid(x1, x2, p.x); return; }
    // segment is vertical
    if (x1 == x2) { res.x = x1; res.y = mid(y1, y2, p.y); return; }
    int dx = x2 - x1; int dy = y2 - y1;
    res.x = dy * (dy * x1 - dx * (y1 + p.y)) + dx * p.x;
    res.x = res.x / (dx*dx + dy*dy);
    res.y = (dx * (p.x - res.x)) / dy + p.y;
  }

  public static int mid(int a, int b, int c) {
    if (a <= b) {
      if (b <= c) return b;
      else if (c <= a) return a;
      else return c;
    }
    else {
      if (b >= c) return b;
      else if (c >= a) return a;
      else return c;
    }
  }

  public static Point ptClosestTo(int x1, int y1, int x2, int y2, Point p) {
    Point res = new Point(0,0);
    ptClosestTo(x1, y1, x2, y2, p, res);
    return res;
  }

  public static Point ptClosestTo(Point p1, Point p2, Point p) {
    return ptClosestTo(p1.x, p1.y, p2.x, p2.y, p);
  }

  private static Point tempPoint = new Point(0, 0);

  public static synchronized void ptClosestTo(int xs[], int ys[],
					      int n, Point p, Point res) {
    res.x = xs[0]; res.y = ys[0];
    int bestDist = (res.x - p.x)*(res.x - p.x) + (res.y - p.y)*(res.y - p.y);
    int tDist;
    tempPoint.x = 0; tempPoint.y = 0;
    for (int i = 0; i < n - 1; ++i) {
      ptClosestTo(xs[i], ys[i], xs[i+1], ys[i+1], p, tempPoint);
      tDist = (tempPoint.x-res.x)*(tempPoint.x-res.x) + (tempPoint.y-res.y)*(tempPoint.y-res.y);
      if (bestDist > tDist) {
	bestDist = tDist;
	res.x = tempPoint.x; res.y = tempPoint.y;
      }
    }
    // dont check segment xs[n-1],ys[n-1] to xs[0],ys[0] because I assume
    // xs[n-1] == xs[0] && ys[n-1] == ys[0], if it is a closed polygon
  }

  public static Point ptClosestTo(int xs[], int ys[], int n, Point p) {
    Point res = new Point(0,0);
    ptClosestTo(xs, ys, n, p, res);
    return res;
  }

  public static synchronized boolean
  nearPolySegment(int xs[], int ys[], int n, int x, int y, int grip) {
    for (int i = 0; i < n - 1; ++i) {
      int x1 = xs[i], y1 = ys[i];
      int x2 = xs[i+1], y2 = ys[i+1];
      if (Geometry.nearSegment(x1, y1, x2, y2, x, y, grip))
        return true;
    }
    return false;
  }

  private static Rectangle tempRect1 = new Rectangle();

  public static synchronized
  boolean nearSegment(int x1, int y1, int x2, int y2, int x, int y, int grip) {
    tempRect1.reshape(x - grip, y - grip, 2 * grip, 2 * grip);
    return intersects(tempRect1, x1, y1, x2, y2);
  }

  private static Rectangle tempRect2 = new Rectangle();
  public static synchronized
  boolean intersects(Rectangle r, int x1, int y1, int x2, int y2) {
    tempRect2.reshape(Math.min(x1, x2), Math.min(y1, y2),
		      Math.abs(x2 - x1), Math.abs(y2 - y1));
    if (! r.intersects(tempRect2)) return false;

    int ccw1 = counterClockWise(x1, y1, x2, y2, r.x, r.y);
    int ccw2 = counterClockWise(x1, y1, x2, y2, r.x, r.y + r.height);
    int ccw3 = counterClockWise(x1, y1, x2, y2, r.x + r.width, r.y);
    int ccw4 = counterClockWise(x1, y1, x2, y2, r.x + r.width, r.y + r.height);

    // reply true iff any of the points are on opposite sides of the
    // line, or if any of them are on the line

    return  ((ccw1 ==  1 || ccw2 ==  1 || ccw3 ==  1 || ccw4 ==  1) &&
	     (ccw1 == -1 || ccw2 == -1 || ccw3 == -1 || ccw4 == -1) ||
	     ccw1 ==  0 || ccw2 ==  0 || ccw3 ==  0 || ccw4 ==  0);
  }

  /** Reply true if the given point is counter-clockwise from the
   * vector defined by the position of the given line. This
   * is used as in determining intersection between lines and
   * rectangles. Taken from Algorithms in C by Sedgewick, page
   * 350. */
  public static
  int counterClockWise(int x1, int y1, int x2, int y2, int x, int y) {
    int dx1 = x2 - x1;
    int dy1 = y2 - y1;
    int dx2 = x - x1;
    int dy2 = y - y1;
    if (dx1*dy2 > dy1*dx2) return +1;
    if (dx1*dy2 < dy1*dx2) return -1;
    if ((dx1*dx2 < 0) || (dy1*dy2 < 0)) return -1;
    if ((dx1*dx1 + dy1*dy1) < (dx2*dx2 + dy2*dy2)) return +1;
    return 0;
  }

} /* end class Geometry */
