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




// File: Geometry.java
// Classes: Geometry
// Orginal Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.lang.Math;
import java.awt.*;

/** A library of functions that do geometric opeations.  These are all
 *  static methods, so you never need to make an instance of this
 *  class.  Needs-More-Work: many of these are not done yet or not
 *  used yet. */

public class Geometry {

  /** Given a Rectangle and a point, set res to be the point on or in
   *  the Rectangle that is closest to the given point. */
  public static void ptClosestTo(Rectangle r, Point p, Point res) {
    int x1 = Math.min(r.x, r.x + r.width);
    int y1 = Math.min(r.y, r.y + r.height);
    int x2 = Math.max(r.x, r.x + r.width);
    int y2 = Math.max(r.y, r.y + r.height);
    int c = 0;
    if (p.x < x1) c = 0; else if (p.x > x2) c = 2; else c = 1;
    if (p.y < y1) c += 0; else if (p.y > y2) c += 6; else c += 3;
    //System.out.println("Case " + c);
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

  /** Given a Rectangle and a point, return a new Point on or in the
   *  Rectangle that is closest to the given Point. */
  public static Point ptClosestTo(Rectangle r, Point p) {
    Point res = new Point(0,0);
    ptClosestTo(r, p, res);
    return res;
  }

  /** Return the angle of the given line segment. */
  public static double segmentAngle(Point p1, Point p2) {
    if (p2.x == p1.x && p2.y <= p1.y) return 90;
    if (p2.x == p1.x && p2.y > p1.y) return 270;
    if (p2.y == p1.y && p2.x > p1.x) return 0;
    if (p2.y == p1.y) return 180;
    double dx = p2.x - p1.x;
    double dy = p2.y - p1.y;
    double m = dy / dx;
    double a = Math.atan(m);
    if (dx > 0) return a; else return -a;
  }

  /** Given the coordinates of the endpoints of a line segment, and a
   *  point, set res to be the closest point on the segement to the
   *  given point. */
  public static void ptClosestTo(int x1, int y1, int x2, int y2,
				 Point p, Point res) {
    // segment is a point
    if (y1 == y2 && x1 == x2) { res.x = x1; res.y = y1; return; }
    // segment is horizontal
    if (y1 == y2) { res.y = y1; res.x = mid(x1, x2, p.x); return; }
    // segment is vertical
    if (x1 == x2) { res.x = x1; res.y = mid(y1, y2, p.y); return; }
    int dx = x2 - x1; int dy = y2 - y1;
    res.x = dy * (dy * x1 - dx * (y1 + p.y)) + dx * p.x;
    res.x = res.x / (dx*dx + dy*dy);
    res.y = (dx * (p.x - res.x)) / dy + p.y;
  }

  /** Given three ints, return the one with the middle value. I.e., it
   *  is not the single largest or the single smallest. */
  private static int mid(int a, int b, int c) {
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

  /** Given the coordinates of the endpoints of a line segment, and a
   *  point, return a new point that is the closest point on the
   *  segement to the given point. */
  public static Point ptClosestTo(int x1, int y1, int x2, int y2, Point p) {
    Point res = new Point(0,0);
    ptClosestTo(x1, y1, x2, y2, p, res);
    return res;
  }

  /** Given the endpoints of a line segment, and a point, return a new
   *  point that is the closest point on the segement to the given
   *  point. */
  public static Point ptClosestTo(Point p1, Point p2, Point p) {
    return ptClosestTo(p1.x, p1.y, p2.x, p2.y, p);
  }

  private static Point tempPoint = new Point(0, 0);

  /** Given a polygon and a point, set res to be the point on 
   *  the perimiter of the polygon that is closest to to the given
   *  point. */
  public static synchronized void ptClosestTo(int xs[], int ys[],
					      int n, Point p, Point res) {
    System.out.println("a");
    res.x = xs[0]; res.y = ys[0];
    int bestDist = (res.x - p.x)*(res.x - p.x) + (res.y - p.y)*(res.y - p.y);
    int tDist;
    tempPoint.x = 0; tempPoint.y = 0;
    for (int i = 0; i < n - 1; ++i) {
      ptClosestTo(xs[i], ys[i], xs[i+1], ys[i+1], p, tempPoint);
      tDist = (tempPoint.x-p.x)*(tempPoint.x-p.x) + (tempPoint.y-p.y)*(tempPoint.y-p.y);
      System.out.println("b:" + tDist);
      if (bestDist > tDist) {
	bestDist = tDist;
	res.x = tempPoint.x; res.y = tempPoint.y;
      }
    }
    // dont check segment xs[n-1],ys[n-1] to xs[0],ys[0] because I assume
    // xs[n-1] == xs[0] && ys[n-1] == ys[0], if it is a closed polygon
  }

  /** Given a polygon and a point, return a new point on the perimiter
   *  of the polygon that is closest to to the given point. */
  public static Point ptClosestTo(int xs[], int ys[], int n, Point p) {
    Point res = new Point(0,0);
    ptClosestTo(xs, ys, n, p, res);
    return res;
  }

  /** Reply true iff the given point is within grip pixels of one of
   *  the segments of the given polygon. Needs-more-work: this is
   *  never used, I don't know that it is needed now that I use hit
   *  rectangles instead. */
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

  /** Reply true iff the given point is within grip pixels of the
   *  given segment. Needs-more-work: this is never used, I don't know
   *  that it is needed now that I use hit rectangles instead. */
  public static synchronized
  boolean nearSegment(int x1, int y1, int x2, int y2, int x, int y, int grip) {
    tempRect1.setBounds(x - grip, y - grip, 2 * grip, 2 * grip);
    return intersects(tempRect1, x1, y1, x2, y2);
  }

  private static Rectangle tempRect2 = new Rectangle();

  /** Reply true iff the given Rectangle intersects the given line
   *  segment. */
  public static synchronized
  boolean intersects(Rectangle r, int x1, int y1, int x2, int y2) {
    tempRect2.setBounds(Math.min(x1, x2), Math.min(y1, y2),
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
