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

// File: FigLine.java
// Classes: FigLine
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/** Class to display lines in diagrams. */

public class FigLine extends Fig {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Coordinates of the start and end points of the line. Note: _x,
   *  _y, _w, and _h from class Fig are always updated by calcBounds()
   *  whenever _x1, _y1, _x2, or _y2 change.*/
  protected int _x1;
  protected int _y1;
  protected int _x2;
  protected int _y2;


  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new FigLine with the given coordinates and color. */
  public FigLine(int x1, int y1, int x2, int y2, Color lineColor) {
    super();
    setX1(x1); setY1(y1); setX2(x2); setY2(y2);
    setLineColor(lineColor);
    calcBounds();
  }

  /** Construct a new FigLine with the given coordinates and attributes. */
  public FigLine(int x1, int y1, int x2, int y2) {
    super();
    setX1(x1); setY1(y1); setX2(x2); setY2(y2);
    calcBounds();
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Set both end points. Fires PropertyChange with "bounds". */
  public final void setShape(Point p1, Point p2) {
    setShape(p1.x, p1.y, p2.x, p2.y);
  }

  /** Set both end points. Fires PropertyChange with "bounds". */
  public void setShape(int x1, int y1, int x2, int y2) {
    _x1 = x1; _y1 = y1; _x2 = x2; _y2 = y2;
    calcBounds();
    firePropChange("bounds", null, null);
  }

  public int getX1() { return _x1; }
  public int getY1() { return _y1; }
  public int getX2() { return _x2; }
  public int getY2() { return _y2; }

  /** Set one of the end point coordinates. Each of these methods
   *  fires PropertyChange with "bounds". */
  public void setX1(int x1) {
    _x1 = x1;
    calcBounds();
    firePropChange("bounds", null, null);
  }
  public void setY1(int y1) {
    _y1 = y1;
    calcBounds(); 
    firePropChange("bounds", null, null);
  }
  public void setX2(int x2) {
    _x2 = x2;
    calcBounds(); 
    firePropChange("bounds", null, null);
  }
  public void setY2(int y2) {
    _y2 = y2;
    calcBounds(); 
    firePropChange("bounds", null, null);
  }

  ////////////////////////////////////////////////////////////////
  // Fig API

  /** Lines can be reshaped, but not resized or rotated (for now). */
  public boolean isResizable() { return false; }
  public boolean isReshapable() { return true; }
  public boolean isRotatable() { return false; }

  /** Sets both endpoints of a line. Length of array must be 2.
   *  Fires PropertyChange with "bounds". */
  public void setPoints(Point[] ps) {
    if (ps.length != 2)
      throw new IllegalArgumentException("FigLine must have exactly 2 points");
    _x1 = ps[0].x; _y1 = ps[0].y; _x2 = ps[1].x; _y2 = ps[1].y;
    calcBounds();
    firePropChange("bounds", null, null);
  }
  
  /** returns an array of lenfth 2 that has the line's endpoints. */
  public Point[] getPoints() {
    Point[] ps = new Point[2];
    ps[0] = new Point(_x1, _y1);
    ps[1] = new Point(_x2, _y2);
    return ps;
  }
  
  /** Move point i to location (x, y). Argument i must be 0 or 1.
   *  Fires PropertyChange with "bounds". */
  public void setPoints(int i, int x, int y) {
    if (i == 0) { _x1 = x; _y1 = y; }
    else if (i == 1) { _x2 = x; _y2 = y; }
    else throw new IndexOutOfBoundsException("FigLine has exactly 2 points");
    calcBounds();
    firePropChange("bounds", null, null);    
  }
  
  /** returns the ith point. Argument i must be 0 or 1. */
  public Point getPoints(int i) {
    if (i == 0) { return new Point(_x1, _y1); }
    else if (i == 1)  { return new Point(_x2, _y2); }
    throw new IndexOutOfBoundsException("FigLine has exactly 2 points");
  }

  /** Returns the number of points. Lines always have 2 points. */
  public int getNumPoints() { return 2; }
  
  /** Returns an array of the X coordinates of all (2) points. */
  public int[] getXs() {
    int[] xs = new int[2];
    xs[0] = _x1;
    xs[1] = _x2;
    return xs;
  }

  /** Returns an array of the Y coordinates of all (2) points. */
  public int[] getYs() {
    int[] ys = new int[2];
    ys[0] = _y1;
    ys[1] = _y2;
    return ys;
  }

  /** return the approximate arc length of the path in pixel units */
  public int getPerimeterLength() {
    int dxdx = (_x2 - _x1) * (_x2 - _x1);
    int dydy = (_y2 - _y1) * (_y2 - _y1);
    return (int) Math.sqrt(dxdx + dydy);
  }

  /** return a point that is dist pixels along the path */
  public void stuffPointAlongPerimeter(int dist, Point res) {
    int len = getPerimeterLength();
    if (dist <= 0) { res.x = _x1; res.y = _y1; return; }
    if (dist >= len) { res.x = _x2; res.y = _y2; return; }
    res.x = _x1 + ((_x2 - _x1) * dist) / len;
    res.y = _y1 + ((_y2 - _y1) * dist) / len;
  }

  /** Sets the bounds of the line.  The line is scaled to fit within
   *  the new bounding box. Fires PropertyChange with "bounds". */
  public void setBounds(int x, int y, int w, int h) {
    _x1 = (_w == 0) ? x : x + ((_x1 - _x) * w) / _w;
    _y1 = (_h == 0) ? y : y + ((_y1 - _y) * h) / _h;
    _x2 = (_w == 0) ? x : x + ((_x2 - _x) * w) / _w;
    _y2 = (_h == 0) ? y : y + ((_y2 - _y) * h) / _h;
    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
    firePropChange("bounds", null, null);    
  }

  /** Replys the point that other connected Figs should attach
   *  to. Currently replys the point on the this line that is closest
   *  to the given point. */
  public Point connectionPoint(Point anotherPt) {
    return Geometry.ptClosestTo(_x1, _y1, _x2, _y2, anotherPt);
  }

  /** Translate (move) this Fig. Fires PropertyChange with "bounds". */
  public void translate(int dx, int dy) {
    _x1 += dx; _y1 += dy; _x2 += dx; _y2 += dy;
    _x += dx; _y += dy; // dont calcBounds because _w and _h are unchanged
    firePropChange("bounds", null, null);    
  }

  /** Update the bounding box so that it encloses (_x1, _y1)--(_x2, _y2). */
  public void calcBounds() {
    if (_x1 < _x2) { _x = _x1; _w = _x2 - _x1; }
    else  { _x = _x2; _w = _x1 - _x2; }
    if (_y1 < _y2) { _y = _y1; _h = _y2 - _y1; }
    else  { _y = _y2; _h = _y1 - _y2; }
  }

  /** Paint this line object. */
  public void paint(Graphics g) {
    if (_lineWidth > 0) {
      if (getDashed()) {
        g.setColor(_lineColor);
	drawDashedLine(g, 0, _x1, _y1, _x2, _y2);
      }
      else {
        g.setColor(_lineColor);
        g.drawLine(_x1, _y1, _x2, _y2);
      }
   }
 }

/*
 protected Point getOffsetAmount(Point p1, Point p2, int offset) {
   // slope of the line we're finding the normal to
   // is slope, and the normal is the negative reciprocal
   // slope is (p1.y - p2.y) / (p1.x - p2.x)
   // so recip is - (p1.x - p2.x) / (p1.y - p2.y)

   int recipnumerator = (p1.x - p2.x) * -1;
   int recipdenominator = (p1.y - p2.y);

   // find the point offset on the line that gives a 
   // correct offset

   double a = offset / Math.sqrt(recipnumerator * recipnumerator +
		  recipdenominator * recipdenominator);
   Point newPoint = new Point((int) (recipdenominator * a),
       (int) (recipnumerator * a));

   //System.out.println("p1=" + p1 + " p2=" + p2 + " off=" + offset);
   //System.out.println("a=" + a + " rn=" + recipnumerator +
   //" rd=" + recipdenominator + " nP=" + newPoint);

   return newPoint;
 }

 public Point getPoint() {
   int figLength = _pathFigure.getPerimeterLength();
   int pointToGet = (int) (figLength * percent);

   Point linePoint = _pathFigure.pointAlongPerimeter(pointToGet);

   //System.out.println("lP=" + linePoint + " ptG=" + pointToGet +
   //" figLen=" + figLength);

   Point offsetAmount =
     getOffsetAmount(_pathFigure.pointAlongPerimeter(pointToGet + 5),
	      _pathFigure.pointAlongPerimeter(pointToGet - 5), offset);

   return new Point(linePoint.x + offsetAmount.x,
	     linePoint.y + offsetAmount.y);
  }
*/
  /** Reply true iff the given point is "near" the line. Nearness
   *  allows the user to more easily select the line with the
   *  mouse. Needs-More-Work: I should probably have two functions
   *  contains() which gives a strict geometric version, and hit() which
   *  is for selection by mouse clicks. */
   public boolean hit(Rectangle r) {
     return Geometry.intersects(r, _x1, _y1, _x2, _y2);
   }

  /** Reply true iff this line passes through, or is even partly
   *  inside the given rectangle, or if any corner of the rect is on
   *  the line. What happens if the line runs along one edge of the
   *  rect, but not all the way to either corner? */
   public boolean intersects(Rectangle r) {
     return Geometry.intersects(r, _x1, _y1, _x2, _y2);
   }


  /** Resize the object for drag on creation. It bypasses the things
   *  done in resize so that the position of the object can be kept as
   *  the anchor point. Fires PropertyChange with "bounds".
   *
   * @see FigLine#drag */
  public void createDrag(int anchorX, int anchorY, int x, int y,
			 int snapX, int snapY) {
    _x2 = snapX;
    _y2 = snapY;
    calcBounds();
    firePropChange("bounds", null, null);    
  }

  static final long serialVersionUID = -1388823280311397581L;

} /* end class FigLine */

