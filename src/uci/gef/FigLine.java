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

// File: FigLine.java
// Classes: FigLine
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/** Class to implement lines in diagrams.
 *  <A HREF="../features.html#basic_shapes_line">
 *  <TT>FEATURE: basic_shapes_line</TT></A>
 */

public class FigLine extends Fig {

  ////////////////////////////////////////////////////////////////
  // instance variables

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
  public FigLine(int x1, int y1, int x2, int y2, Hashtable gAttrs) {
    super();
    setX1(x1); setY1(y1); setX2(x2); setY2(y2);
    //put(gAttrs);
    calcBounds();
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public final void setShape(Point p1, Point p2) {
    setShape(p1.x, p1.y, p2.x, p2.y);
  }

  public void setShape(int x1, int y1, int x2, int y2) {
    _x1 = x1; _y1 = y1; _x2 = x2; _y2 = y2;
    calcBounds();
  }

  public void setX1(int x1) { _x1 = x1; calcBounds(); }
  public void setY1(int y1) { _y1 = y1; calcBounds(); }
  public void setX2(int x2) { _x2 = x2; calcBounds(); }
  public void setY2(int y2) { _y2 = y2; calcBounds(); }

  public boolean isResizable() { return false; }
  public boolean isReshapable() { return true; }
  public boolean isRotatable() { return false; }


  ////////////////////////////////////////////////////////////////
  // Fig API

  public void setPoints(Point[] ps) {
    if (ps.length != 2)
      throw new IllegalArgumentException("FigLine must have exactly 2 points");
    _x1 = ps[0].x; _y1 = ps[0].y; _x2 = ps[1].x; _y2 = ps[1].y;
    calcBounds();
  }
  public Point[] getPoints() {
    Point[] ps = new Point[2];
    ps[0] = new Point(_x1, _y1);
    ps[1] = new Point(_x2, _y2);
    return ps;
  }
  public void setPoints(int i, int x, int y) {
    if (i == 0) { _x1 = x; _y1 = y; }
    else if (i == 1) { _x2 = x; _y2 = y; }
    else throw new IndexOutOfBoundsException("FigLine has exactly 2 points");
    calcBounds();
  }
  public Point getPoints(int i) {
    if (i == 0) { return new Point(_x1, _y1); }
    else if (i == 1)  { return new Point(_x2, _y2); }
    throw new IndexOutOfBoundsException("FigLine has exactly 2 points");
  }

  public int getNumPoints() { return 2; }
  public int[] getXs() {
    int[] xs = new int[2];
    xs[0] = _x1;
    xs[1] = _x2;
    return xs;
  }
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
  public Point pointAlongPerimeter(int dist) {
    int len = getPerimeterLength();
    int p = Math.min(dist, len);
    return new Point(_x1 + ((_x2 - _x1) * p) / len, _y1 + ((_y2 - _y1) * p) / len);
  }

  public void setBounds(int x, int y, int w, int h) {
    _x1 = (_w == 0) ? x : x + ((_x1 - _x) * w) / _w;
    _y1 = (_h == 0) ? y : y + ((_y1 - _y) * h) / _h;
    _x2 = (_w == 0) ? x : x + ((_x2 - _x) * w) / _w;
    _y2 = (_h == 0) ? y : y + ((_y2 - _y) * h) / _h;
    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
  }


  ////////////////////////////////////////////////////////////////
  // Fig API

  /** Translate this Fig. */
  public void translate(int dx, int dy) {
    _x1 += dx; _y1 += dy; _x2 += dx; _y2 += dy;
    _x += dx; _y += dy; // dont calcBounds because _w and _h are unchanged
  }

  public void calcBounds() {
    if (_x1 < _x2) { _x = _x1; _w = _x2 - _x1; }
    else  { _x = _x2; _w = _x1 - _x2; }
    if (_y1 < _y2) { _y = _y1; _h = _y2 - _y1; }
    else  { _y = _y2; _h = _y1 - _y2; }
  }

  /** Paint this line object. */
  public void paint(Graphics g) {
    if (_lineWidth > 0) {
      g.setColor(_lineColor);
      g.drawLine(_x1, _y1, _x2, _y2);
	  drawArrowHead(g);
   }
  }


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
   *  the anchor point. Needs-More-Work: do I really need this
   *  function?
   *
   * @see FigLine#drag */
  public void createDrag(int anchorX, int anchorY, int x, int y,
			 int snapX, int snapY) {
    _x2 = snapX;
    _y2 = snapY;
    calcBounds();
  }

  ArrowHead ArrowHeadStart = new ArrowHeadTriangle();
  ArrowHead ArrowHeadEnd = new ArrowHeadTriangle();

	protected void drawArrowHead(Graphics g) {
		ArrowHeadStart.setFillColor(Color.white);
		//System.out.println("pointalongat0 = " + pointAlongPerimeter(0) + " pointAtEnd = " + getPerimeterLength() + " pointatEnd = " + pointAlongPerimeter(getPerimeterLength()));
		ArrowHeadStart.paint(g, pointAlongPerimeter(0), pointAlongPerimeter(getPerimeterLength()));
		ArrowHeadEnd.paint(g, pointAlongPerimeter(getPerimeterLength()), pointAlongPerimeter(0));
	}

} /* end class FigLine */

