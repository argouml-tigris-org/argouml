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

// File: FigPoly.java
// Classes: FigPoly
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;
import uci.ui.*;
import uci.util.*;

/** Primitive Fig to paint Polygons on a LayerDiagram. FigPolys contain
 *  a set of points that define the polygon, the index of one point
 *  that was selected by the user most recently, a boolean to
 *  determine if the polygon should be constrained to rectilinear
 *  (strict horizontal and vertical) segments, and a number of handles
 *  that cannot be moved by user dragging. A FigPoly is not closed
 *  unless the last point equals the first point. Thus, FigPolys can
 *  be used to represent polylines such as FigEdgeRectilinear.
 *  <A HREF="../features.html#basic_shapes_polygon">
 *  <TT>FEATURE: basic_shapes_polygon</TT></A>
 *
 * @see ActionRemoveVertex
 * @see ActionInsertVertex
 * @see FigEdgeRectilinear */

public class FigPoly extends Fig {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The index of the last handle that the user touched. Pressing
   *  delete while a handle is selected will remove one point from the
   *  polygon. A value of -1 means that no handle is selected. */

  /** The total number of points. */
  protected int _npoints = 0;

  /** The array of x coordinates. */
  protected int _xpoints[] = new int[4];

  /** The array of y coordinates. */
  protected int _ypoints[] = new int[4];

  /** flag to control how the polygon is drawn */
  protected boolean _rectilinear = false;

  /** The number of handles at each end of the polygon that cannot be
   *  dragged by the user. -1 indicates that any point can be
   *  dragged. 0 indicates that the endpoints cannot be dragged. 1
   *  would indicate that the first 2 points and the last 2 points
   *  cannot be dragged. */
  protected int _fixedHandles = -1;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new FigPoly w/ the given line color. */
  public FigPoly(Color lineColor) {
    super();
    setLineColor(lineColor);
  }

  public FigPoly(Color lineColor, Color fillColor) {
    super();
    setLineColor(lineColor);
    setFillColor(fillColor);
  }

  /** Construct a new FigPoly w/ the given attributes. */
  public FigPoly(Hashtable gAttrs) {
    super();
    //put(gAttrs);
  }

  /** Construct a new FigPoly w/ the given point and attributes. */
  public FigPoly(int x, int y, Hashtable gAttrs) {
    this(gAttrs);
    addPoint(x, y);
  }

  ////////////////////////////////////////////////////////////////
  // invariant

  public boolean OK() {
    return super.OK() && _npoints > 0 &&
      _xpoints != null && _ypoints != null;
    // and bounds are up to date
  }

  ////////////////////////////////////////////////////////////////
  // accessors



  /** Get the current vector of points as a java.awt.Polygon. */
  public Polygon polygon() {
    return new Polygon(_xpoints, _ypoints, _npoints);
  }

  /** Set the current vector of points. */
  public void polygon(Polygon p) {
    _npoints = p.npoints;
    _xpoints = new int[_npoints];
    _ypoints = new int[_npoints];
    System.arraycopy(p.xpoints, 0, _xpoints, 0, _npoints);
    System.arraycopy(p.ypoints, 0, _ypoints, 0, _npoints);
    calcBounds();
  }

  /** Return the number of points in this polygon */
  public int npoints() { return _npoints; }
  public int getNumPoints() { return _npoints; }

  /** Return true if the polygon should be constrained to rectilinear
   *  segments.  */
  public boolean rectilinear() { return _rectilinear; }

  /** Set the rectilinear flag. Setting this flag to true will not
   *  change the current shape of the polygon, instead future dragging
   *  by the user will move near-by points to be rectilinear. */
  public void rectilinear(boolean r) { _rectilinear = r; }

  /** Reply the number of fixed handles. 0 indicates that the end
   *  points of the polygon cannot be dragged by the user. */
  public int fixedHandles() { return _fixedHandles; }

  /** Set the number of points near each end of the polygon that
   *  cannot be dragged by the user. */
  public void fixedHandles(int n) { _fixedHandles = n; }

  ////////////////////////////////////////////////////////////////
  // geomertric manipulations

  private static Handle _TempHandle = new Handle(0);

  /** Set the end points of this polygon, regardless of the number of
   *  fixed handles. This is used when nodes move. */
  public void setEndPoints(Point start, Point end) {
    while (_npoints < 2) addPoint(start);
    synchronized (_TempHandle) {
      _TempHandle.index = 0;
      moveVertex(_TempHandle, start.x, start.y, true);
      _TempHandle.index = _npoints - 1;
      moveVertex(_TempHandle, end.x, end.y, true);
    }
  }

  /** Change the position of the object from were it is to were it is
   *  plus dx or dy. Often called when an object is dragged. This could
   *  be very useful if local-coordinate systems are used because
   *  deltas need less transforming... maybe.  */
  public void translate(int dx, int dy) {
    Rectangle oldBounds = getBounds();
    for (int i = 0; i < _npoints; ++i) {
      _xpoints[i] += dx; _ypoints[i] += dy;
    }
    // dont call calcBounds because width and height are unchanged
    _x += dx; _y += dy;
    firePropChange("bounds", oldBounds, getBounds());
  }

  /** Add a point to this polygon. */
  public void addPoint(int x, int y) {
    growIfNeeded();
    _xpoints[_npoints] = x;
    _ypoints[_npoints] = y;
    _npoints++;
    Rectangle oldBounds = getBounds();
    calcBounds(); // a special case could be faster
    firePropChange("bounds", oldBounds, getBounds());
  }

  /** Add a point to this polygon. */
  public final void addPoint(Point p) { addPoint(p.x, p.y); }

  /** Reply true if the point at the given index can be moved. The
   *  index must be valid, and the number of fixed handles must not *
   *  include this index, unless ov is true to override the fixed *
   *  handles. */
  protected boolean canMoveVertex(int i, boolean ov) {
    return (i >= 0 && i < _npoints &&
	    (ov || i >= _fixedHandles && i < _npoints - _fixedHandles));
  }

  /** Move the point indicated by the given Handle object to the given
   *  location. Fixed handles cannot be moved, unless ov is set to true
   *  to override the fixed handle constaint. */
  public void moveVertex(Handle h, int x, int y, boolean ov) {
    int i = h.index;
    if (!_rectilinear) { _xpoints[i] = x; _ypoints[i] = y; }
    else {
      if (ov) { _xpoints[i] = x; _ypoints[i] = y; }
      if (i == _fixedHandles) { prependTwoPoints(); h.index += 2; i += 2;}
      if (i == _npoints - _fixedHandles - 1) { appendTwoPoints(); }
      if (i % 2 == 0) {
        if (canMoveVertex(i-1, ov)) { _xpoints[i-1] = x; _xpoints[i] = x; }
        if (canMoveVertex(i+1, ov)) { _ypoints[i+1] = y; _ypoints[i] = y; }
      }
      else {
        if (canMoveVertex(i-1, ov)) { _ypoints[i-1] = y; _ypoints[i] = y; }
        if (canMoveVertex(i+1, ov)) { _xpoints[i+1] = x; _xpoints[i] = x; }
      }
    }
    Rectangle oldBounds = getBounds();
    calcBounds();
    firePropChange("bounds", oldBounds, getBounds());
  }

  /** Add two points to the front of the list of points. Needed to
   *  introduce new handles at the front of the polygon when the user
   *  drags a point just after a fixed handle. */
  protected void prependTwoPoints() {
    int tmp[];
    tmp = new int[_npoints + 2];
    System.arraycopy(_xpoints, 0, tmp, 2, _npoints);
    _xpoints = tmp;

    tmp = new int[_npoints + 2];
    System.arraycopy(_ypoints, 0, tmp, 2, _npoints);
    _ypoints = tmp;

    _xpoints[0] = _xpoints[1] = _xpoints[2];
    _ypoints[0] = _ypoints[1] = _ypoints[2];
    _npoints += 2;
  }

  /** Add two points at the end of the polygon. Needed if the user
   *  drags a point just before a fixed handle. */
  protected void appendTwoPoints() {
    int tmp[];
    tmp = new int[_npoints + 2];
    System.arraycopy(_xpoints, 0, tmp, 0, _npoints);
    _xpoints = tmp;

    tmp = new int[_npoints + 2];
    System.arraycopy(_ypoints, 0, tmp, 0, _npoints);
    _ypoints = tmp;

    _xpoints[_npoints+1] = _xpoints[_npoints] = _xpoints[_npoints-1];
    _ypoints[_npoints+1] = _ypoints[_npoints] = _ypoints[_npoints-1];
    _npoints += 2;
  }

  public void removePoint(int i) {
    // needs-more-work: this assertion has been violated, track it down
    if (Dbg.on) Dbg.assert(i >= 0 && i < _npoints, "point not found");
    if (_npoints < 3) return;
    int tmp[] = new int[_npoints];
    if (_rectilinear && i != 0 && i != _npoints - 1) {
      if (i%2 == 0) {_xpoints[i] = _xpoints[i+1]; _ypoints[i] = _ypoints[i-1];}
      else { _xpoints[i] = _xpoints[i-1]; _ypoints[i] = _ypoints[i+1];}
    }
    else {
      System.arraycopy(_xpoints, i+1, tmp, 0, _npoints - i - 1);
      System.arraycopy(tmp, 0, _xpoints, i, _npoints - i - 1);
      System.arraycopy(_ypoints, i+1, tmp, 0, _npoints - i - 1);
      System.arraycopy(tmp, 0, _ypoints, i, _npoints - i - 1);
      --_npoints;
    }
    Rectangle oldBounds = getBounds();
    calcBounds();
    firePropChange("bounds", oldBounds, getBounds());
  }

  public void insertPoint(int i, int x, int y) {
    growIfNeeded();
    int tmp[] = new int[_npoints];
    System.arraycopy(_xpoints, i + 1, tmp, 0, _npoints - i - 1);
    _xpoints[i+1] = x;
    System.arraycopy(tmp, 0, _xpoints, i + 2, _npoints - i - 1);
    System.arraycopy(_ypoints, i + 1, tmp, 0, _npoints - i - 1);
    _ypoints[i+1] = y;
    System.arraycopy(tmp, 0, _ypoints, i + 2, _npoints - i - 1);
    ++_npoints;
    calcBounds(); // needed? //?
  }

  /** Increase the memory used to store polygon points, if needed. */
  protected void growIfNeeded() {
    if (_npoints >= _xpoints.length) {
      int tmp[];

      tmp = new int[_npoints * 2];
      System.arraycopy(_xpoints, 0, tmp, 0, _npoints);
      _xpoints = tmp;

      tmp = new int[_npoints * 2];
      System.arraycopy(_ypoints, 0, tmp, 0, _npoints);
      _ypoints = tmp;
    }
  }


  /** When the user drags the handles, move individual points */
  public void setPoints(Handle h, int mX, int mY) {
    moveVertex(h, mX, mY, false);
  }

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paint the FigPoly on the given Graphics */
  public void paint(Graphics g) {
    if (_filled  && _fillColor != null) {
      g.setColor(_fillColor);
      g.fillPolygon(_xpoints, _ypoints, _npoints);
    }
    if (_lineWidth > 0  && _lineColor != null) {
      g.setColor(_lineColor);
      g.drawPolyline(_xpoints, _ypoints, _npoints);
      drawArrowHead(g);
    }
  }


  ////////////////////////////////////////////////////////////////
  // selection methods


  /** Reply the index of the vertex that the given point is near. */
  protected int findHandle(int x, int y) {
    int HAND_SIZE = Selection.HAND_SIZE;
    int xs[] = _xpoints;
    int ys[] = _ypoints;
    for (int i = 0; i < _npoints; ++i) {
      if (x >= xs[i] - HAND_SIZE/2 && y >= ys[i] - HAND_SIZE/2 &&
	  x <= xs[i] + HAND_SIZE/2 && y <= ys[i] + HAND_SIZE/2) {
	//@ _selectedHandle = i;
	return i;
      }
    }
    //@ _selectedHandle = -1;
    return -1;
  }

  /** Reply true iff the given point is inside a filled FigPoly, or it
   *  is near this FigPoly, or it is near a vertex of this FigPoly, or
   *  it is near a line segment of this FigPoly (regardless of line
   *  width). */

  //? does polygon.contains rewally work? need to call Geometry?
  public boolean contains(int x, int y) {
    Polygon p = polygon();
    return p.contains(x, y);
  }



  public int[] getXs() { return _xpoints; }
  public int[] getYs() { return _ypoints; }

  public void setBounds(int x, int y, int w, int h) {
    if (w > 0 && h > 0) {
      for (int i = 0; i < _npoints; ++i) {
	_xpoints[i] = x + ((_xpoints[i] - _x) * w) / _w;
	_ypoints[i] = y + ((_ypoints[i] - _y) * h) / _h;
      }
      _x = x; _y = y; _w = w; _h = h;
    }
  }


  public int getPerimeterLength() {
    // needs-more-work: should I cache this value?
    int len = 0;
    int dx, dy;
    for (int i = 0; i < _npoints - 1; ++i) {
      dx = _xpoints[i+1] - _xpoints[i];
      dy = _ypoints[i+1] - _ypoints[i];
      len += (int)Math.sqrt(dx*dx + dy*dy);
    }
    return len;
  }

  public Point pointAlongPerimeter(int dist) {
    for (int i = 0; i < _npoints - 1; ++i) {
      int dx = _xpoints[i+1] - _xpoints[i];
      int dy = _ypoints[i+1] - _ypoints[i];
      int segLen = (int)Math.sqrt(dx*dx + dy*dy);
      if (dist < segLen)
	  {
        if (segLen != 0)
		  return new Point(_xpoints[i] + (dx * dist) / segLen, _ypoints[i] + (dy * dist) / segLen);
		else
		  return new Point(_xpoints[i], _ypoints[i]);
      }
      else
        dist -= segLen;
    }
    // what if there are 0 points?
    return new Point(_xpoints[0], _ypoints[0]);
  }


  public boolean isResizable() { return true; }
  public boolean isReshapable() { return true; }
  public boolean isRotatable() { return false; }


  ////////////////////////////////////////////////////////////////
  // internal utility functions

  protected int countCornersContained(int x, int y, int w, int h) {
    Polygon p = polygon();
    int cornersHit = 0;
    if (p.contains(x, y)) cornersHit++;
    if (p.contains(x + w, y)) cornersHit++;
    if (p.contains(x, y + h)) cornersHit++;
    if (p.contains(x + w, y + h)) cornersHit++;
    return cornersHit;
  }

  protected void calcBounds() {
    //needs-more-work: could be faster, dont alloc polygon
    Rectangle polyBounds = polygon().getBounds();
    _x = polyBounds.x;
    _y = polyBounds.y;
    _w = polyBounds.width;
    _h = polyBounds.height;
  }

  ArrowHead ArrowHeadStart = new ArrowHeadTriangle();
  ArrowHead ArrowHeadEnd = new ArrowHeadTriangle();

	protected void drawArrowHead(Graphics g) {
		ArrowHeadStart.setFillColor(Color.white);
		System.out.println("pointalongat0 = " + pointAlongPerimeter(0) + " pointAtEnd = " + getPerimeterLength() + " pointatEnd = " + pointAlongPerimeter(20));
		ArrowHeadStart.paint(g, pointAlongPerimeter(20), pointAlongPerimeter(0));
		ArrowHeadEnd.paint(g, pointAlongPerimeter(getPerimeterLength() - 21), pointAlongPerimeter(getPerimeterLength() - 1));
	}

} /* end class FigPoly */

