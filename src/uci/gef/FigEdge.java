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

// File: FigEdge.java
// Classes: FigEdge
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;
import java.io.*;
import uci.util.*;

/** A Fig that paints arcs between ports.
 *  <A HREF="../features.html#graph_visualization_arcs">
 *  <TT>FEATURE: graph_visualization_arcs</TT></A>
 *  <A HREF="../bugs.html#arc_translate">
 *  <FONT COLOR=660000><B>BUG: arc_translate</B></FONT></A>
 */

public abstract class FigEdge extends Fig {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Fig presenting the NetEdge's from NetPort . */
  protected Fig _sourcePortFig;
  /** Fig presenting the NetEdge's to NetPort. */
  protected Fig _destPortFig;
  /** FigNode presenting the NetEdge's from NetPort's parent NetNode. */
  protected FigNode _sourceFigNode;
  /** FigNode presenting the NetEdge's to NetPort's parent NetNode. */
  protected FigNode _destFigNode;
  /** Fig that presents the NetEdge. */
  protected Fig _fig;
  /** The ArrowHead at the start of the line */
  ArrowHead ArrowHeadStart = new ArrowHeadTriangle();
  /** The ArrowHead at the end of the line */
  ArrowHead ArrowHeadEnd = new ArrowHeadTriangle();


  ////////////////////////////////////////////////////////////////
  // constructors

  public FigEdge(Fig s, Fig d, FigNode sp, FigNode dp, NetEdge a) {
    _sourcePortFig = s;
    _destPortFig = d;
    _sourceFigNode = sp;
    _destFigNode = dp;
    setOwner(a);
    _fig = makeEdgeFig();
  }

  public FigEdge() { _fig = makeEdgeFig(); }

  protected abstract Fig makeEdgeFig();

  ////////////////////////////////////////////////////////////////
  // accessors

  public void sourcePortFig(Fig fig) { _sourcePortFig = fig; }

  public void destPortFig(Fig fig) { _destPortFig = fig; }

  public void sourceFigNode(FigNode pers) {_sourceFigNode = pers; }

  public void destFigNode(FigNode pers) { _destFigNode = pers; }

  public void setOwner(Object own) {
    Object oldOwner = getOwner();
    if (oldOwner != null && oldOwner instanceof Observable) {
      ((Observable)oldOwner).deleteObserver(this);
    }
    if (own instanceof Observable) {
      ((NetEdge)own).addPersistantObserver(this);
    }
    super.setOwner(own);
  }

  public void setSourceArrowHead(ArrowHead newArrow)
  {
    ArrowHeadStart = newArrow;
  }

  public void setDestArrowHead(ArrowHead newArrow)
  {
    ArrowHeadEnd = newArrow;
  }


  ////////////////////////////////////////////////////////////////
  // Routing related methods

  protected void computeRoute() { }

  ////////////////////////////////////////////////////////////////
  // Fig API

  /** Reply the bounding box for this edge. */
  public Rectangle getBounds() { return _fig.getBounds(); }

  //needed?
  protected void calcBounds() {
    _fig.calcBounds();
    _x = _fig.getX();
    _y = _fig.getY();
    _w = _fig.getWidth();
    _h = _fig.getHeight();
  }

  /** Reply true iff this edge contains the given point.
   * @see Fig#contains */
  public boolean contains(int x, int y) { return _fig.contains(x, y); }

  /** Reply true iff this arc intersects the given rectangle. */
  public boolean intersects(Rectangle r) { return _fig.intersects(r); }

  /** Reply true iff this edge  contains the given point.
   * @see Fig#contains */
  public boolean hit(Rectangle r) { return _fig.hit(r); }

  public int getPerimeterLength() { return _fig.getPerimeterLength(); }

  public Point pointAlongPerimeter(int dist) {
    return _fig.pointAlongPerimeter(dist);
  }

  public void setPoints(Point[] ps) { _fig.setPoints(ps); calcBounds(); }
  public Point[] getPoints() { return _fig.getPoints(); }
  public void setPoints(int i, int x, int y) {
    _fig.setPoints(i, x, y);
    calcBounds(); }
  public void setPoints(Handle h, int x, int y) {
    _fig.setPoints(h, x, y);
    calcBounds();
  }
  public Point getPoints(int i) { return _fig.getPoints(i); }
  public int getNumPoints() { return _fig.getNumPoints(); }
  public void setNumPoints(int npoints) {
    _fig.setNumPoints(npoints);
    calcBounds();
  }
  public int[] getXs() { return _fig.getXs(); }
  public void setXs(int[] xs) { _fig.setXs(xs); calcBounds(); }
  public int[] getYs() { return _fig.getYs(); }
  public void setYs(int[] ys) { _fig.setYs(ys); calcBounds(); }


  public void dispose(Editor ed) {
    System.out.println("disposing: " + toString());
    ((NetEdge)getOwner()).dispose();
    super.dispose(ed);
  }

  public void removeFrom(Editor ed) {
    super.removeFrom(ed);
    setOwner(null);
  }

  public boolean isResizable() { return _fig.isResizable(); }
  public boolean isReshapable() { return _fig.isReshapable(); }
  public boolean isRotatable() { return _fig.isRotatable(); }

  ////////////////////////////////////////////////////////////////
  // display methods

  protected void drawArrowHead(Graphics g) {
    ArrowHeadStart.setFillColor(Color.white);
    System.out.println("p0= " + pointAlongPerimeter(0) + " len= " + getPerimeterLength() + " pE= " + pointAlongPerimeter(getPerimeterLength()));
    ArrowHeadStart.paint(g, pointAlongPerimeter(20), pointAlongPerimeter(0));
    ArrowHeadEnd.paint(g, pointAlongPerimeter(getPerimeterLength() - 21), pointAlongPerimeter(getPerimeterLength() - 1));
  }


  PathConvPercent[] labelPos = {
    new PathConvPercent(this, (float) .2, 0),
    new PathConvPercent(this, (float) .5, 0),
    new PathConvPercent(this, (float) .8, 0) };
  FigText[] labelText = {
    new FigText(0, 0, 100, 10, Color.black, "TimesRoman", 8),
    new FigText(0, 0, 100, 10, Color.black, "TimesRoman", 8),
    new FigText(0, 0, 100, 10, Color.black, "TimesRoman", 8), };

  boolean initedFigs = false;

  /** Paint this object. */
  public void paint(Graphics g) {
    computeRoute();
    _fig.paint(g);
    drawArrowHead(g);

    if (!initedFigs)
    {
      getLayer().add(labelText[0]);
      getLayer().add(labelText[1]);
      getLayer().add(labelText[2]);
      initedFigs = true;
    }

    labelText[0].setText("x= " + labelPos[0].getPoint().x + " y= " + labelPos[0].getPoint().y);
    labelText[1].setText("x= " + labelPos[1].getPoint().x + " y= " + labelPos[1].getPoint().y);
    labelText[2].setText("x= " + labelPos[2].getPoint().x + " y= " + labelPos[2].getPoint().y);

    labelText[0].setLocation(labelPos[0].getPoint().x, labelPos[0].getPoint().y);
    labelText[1].setLocation(labelPos[1].getPoint().x, labelPos[1].getPoint().y);
    labelText[2].setLocation(labelPos[2].getPoint().x, labelPos[2].getPoint().y);

    labelText[0].paint(g);
    labelText[1].paint(g);
    labelText[2].paint(g);
  }

  ////////////////////////////////////////////////////////////////
  // notifications and updates

  public void update(Observable o, Object arg) {
    if (arg instanceof Vector) {
      Vector v = (Vector) arg;
      String s = (String) v.elementAt(0);
      Object obj = v.elementAt(1);
      if (s.equals(Globals.REMOVE) && obj == getOwner()) {
	removeFrom(null);
      }
    }
  }

} /* end class ArcFigNode */
