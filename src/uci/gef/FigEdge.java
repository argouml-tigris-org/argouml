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
import java.beans.*;

import uci.util.*;
import uci.graph.*;

/** A Fig that paints arcs between ports.
 *  <A HREF="../features.html#graph_visualization_arcs">
 *  <TT>FEATURE: graph_visualization_arcs</TT></A>
 *  <A HREF="../bugs.html#arc_translate">
 *  <FONT COLOR=660000><B>BUG: arc_translate</B></FONT></A>
 */

public abstract class FigEdge extends Fig implements PropertyChangeListener {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Fig presenting the edge's from port . */
  protected Fig _sourcePortFig;
  /** Fig presenting the edge's to port. */
  protected Fig _destPortFig;
  /** FigNode presenting the edge's from port's parent node. */
  protected FigNode _sourceFigNode;
  /** FigNode presenting the edge's to port's parent node. */
  protected FigNode _destFigNode;
  /** Fig that presents the edge. */
  protected Fig _fig;
  protected boolean _useNearest = false;
  protected boolean _highlight = false;
  /** The ArrowHead at the start of the line */
  ArrowHead _arrowHeadStart = new ArrowHeadNone();
  /** The ArrowHead at the end of the line */
  ArrowHead _arrowHeadEnd = new ArrowHeadNone();
  /** The items that are accumulated along the path, a vector. */
  Vector _pathItems = new Vector();

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigEdge(Fig s, Fig d, FigNode sfn, FigNode dfn, Object edge) {
    _sourcePortFig = s;
    _destPortFig = d;
    sourceFigNode(sfn);
    destFigNode(dfn);
    setOwner(edge);
    _fig = makeEdgeFig();
  }

  public FigEdge() { _fig = makeEdgeFig(); }

  protected abstract Fig makeEdgeFig();

  ////////////////////////////////////////////////////////////////
  // accessors

  public void sourcePortFig(Fig fig) { _sourcePortFig = fig; }

  public void destPortFig(Fig fig) { _destPortFig = fig; }

  public void sourceFigNode(FigNode fn) {
    // assert fn != null
    if (_sourceFigNode != null) _sourceFigNode.removeFigEdge(this);
    _sourceFigNode = fn;
    fn.addFigEdge(this);
  }

  public void destFigNode(FigNode fn) {
    // assert fn != null
    if (_destFigNode != null) _destFigNode.removeFigEdge(this);
    _destFigNode = fn;
    fn.addFigEdge(this);
  }

  public void setOwner(Object own) {
    Object oldOwner = getOwner();
    if (oldOwner != null && oldOwner instanceof GraphEdgeHooks) {
      ((GraphEdgeHooks)oldOwner).removePropertyChangeListener(this);
    }
    if (own instanceof GraphEdgeHooks) {
      ((GraphEdgeHooks)own).addPropertyChangeListener(this);
    }
    super.setOwner(own);
  }

  public ArrowHead getSourceArrowHead() { return _arrowHeadStart; }

  public ArrowHead getDestArrowHead() { return _arrowHeadEnd; }

  public void setSourceArrowHead(ArrowHead newArrow) {
    _arrowHeadStart = newArrow;
  }

  public void setDestArrowHead(ArrowHead newArrow) {
    _arrowHeadEnd = newArrow;
  }

  public Vector getPathItemsRaw() { return _pathItems; }

  public Fig getPathItem(PathConv pointOnPath) { 
    // needs-more-work: Find the closest Fig to this point
    return null;
  }

  public void addPathItem(Fig newFig, PathConv newPath) {
    _pathItems.addElement(new PathItem(newFig, newPath));
  }

  public void removePathItem(PathItem goneItem) {
    _pathItems.removeElement(goneItem);    
  }

  public boolean getBetweenNearestPoints() { return _useNearest; }
  public void setBetweenNearestPoints(boolean un) { _useNearest = un; }

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

  public boolean isResizable() { return _fig.isResizable(); }
  public boolean isReshapable() { return _fig.isReshapable(); }
  public boolean isRotatable() { return _fig.isRotatable(); }

  ////////////////////////////////////////////////////////////////
  // display methods

  protected void drawArrowHead(Graphics g) {
    _arrowHeadStart.setFillColor(Color.white);
    //System.out.println("p0= " + pointAlongPerimeter(0) + " len= " + getPerimeterLength() + " pE= " + pointAlongPerimeter(getPerimeterLength()));
    _arrowHeadStart.paint(g, pointAlongPerimeter(5), pointAlongPerimeter(0));
    _arrowHeadEnd.paint(g, pointAlongPerimeter(getPerimeterLength() - 6),
			pointAlongPerimeter(getPerimeterLength() - 1));
  }

  protected void drawPathItems(Graphics g) {
    Vector pathVec = getPathItemsRaw();

    for (int i = 0; i < pathVec.size(); i++) {
      PathItem element = (PathItem) pathVec.elementAt(i);
      PathConv path = element.getPath();
      Fig f = element.getFig();
      f.setLocation(path.getPoint().x, path.getPoint().y);
      f.paint(g);
    } 
  }

  /** Paint this object. */
  // needs-more-work: take Highlight into account
  public void paint(Graphics g) {
    computeRoute();
    _fig.paint(g);
    drawArrowHead(g);
    drawPathItems(g);
  }



  
  ////////////////////////////////////////////////////////////////
  // notifications and updates

  public void propertyChange(PropertyChangeEvent pce) {
    System.out.println("FigEdge got a PropertyChangeEvent");
    String pName = pce.getPropertyName();
    Object src = pce.getSource();
    if (pName.equals("Dispose") && src == getOwner()) { delete(); }
    if (pName.equals("Highlight") && src == getOwner()) {
      _highlight = ((Boolean)pce.getNewValue()).booleanValue();
      damage();
    }
  }

  
  ////////////////////////////////////////////////////////////////
  // inner classes
  protected class PathItem {
    Fig _fig;
    PathConv _path;

    PathItem(Fig f, PathConv pc) {
      _fig = f;
      _path = pc;
    }

    PathConv getPath() { return _path; }
    Fig getFig() { return _fig; }
  }

} /* end class ArcFigNode */
