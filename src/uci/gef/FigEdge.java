// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


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
import uci.ui.*;
import uci.graph.*;

/** Abastract Fig class for representing edges between ports.
 *
 *  @see FigEdgeLine
 *  @see FigEdgeRectiline
 */

public abstract class FigEdge extends Fig
implements PropertyChangeListener {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Fig presenting the edge's from-port . */
  protected Fig _sourcePortFig;
  /** Fig presenting the edge's to-port. */
  protected Fig _destPortFig;
  /** FigNode presenting the edge's from-port's parent node. */
  protected FigNode _sourceFigNode;
  /** FigNode presenting the edge's to-port's parent node. */
  protected FigNode _destFigNode;
  /** Fig that presents the edge. */
  protected Fig _fig;
  /** True if the FigEdge should be drawn from the nearest point of
   *  each port Fig. */
  protected boolean _useNearest = false;
  /** True when the FigEdgde should be drawn highlighted. */
  protected boolean _highlight = false;
  /** The ArrowHead at the start of the line */
  protected ArrowHead _arrowHeadStart = new ArrowHeadNone();
  /** The ArrowHead at the end of the line */
  protected ArrowHead _arrowHeadEnd = new ArrowHeadNone();
  /** The items that are accumulated along the path, a vector. */
  protected Vector _pathItems = new Vector();

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Contruct a new FigEdge with the given source and destination
   *  port figs and FigNodes.  The new FigEdge will represent the
   *  given edge (an object from some underlying model). */
  public FigEdge(Fig s, Fig d, FigNode sfn, FigNode dfn, Object edge) {
    _sourcePortFig = s;
    _destPortFig = d;
    sourceFigNode(sfn);
    destFigNode(dfn);
    setOwner(edge);
    _fig = makeEdgeFig();
  }

  /** Contruct a new FigEdge without any underlying edge. */
  public FigEdge() { _fig = makeEdgeFig(); }

  /** Abstract method to make the Fig that will be drawn for this
   *  FigEdge. In FigEdgeLine this method constructs a FigLine. In
   *  FigEdgeRectiline, this method constructs a FigPoly. */
  protected abstract Fig makeEdgeFig();

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Return the Fig that will be drawn. */
  public Fig getFig() { return _fig; }

  /** Get the Fig reprenting this FigEdge's from-port. */ 
  public void sourcePortFig(Fig fig) { _sourcePortFig = fig; }

  /** Get the Fig reprenting this FigEdge's to-port. */ 
  public void destPortFig(Fig fig) { _destPortFig = fig; }

  /** Set the FigNode reprenting this FigEdge's from-node. */ 
  public void sourceFigNode(FigNode fn) {
    // assert fn != null
    if (_sourceFigNode != null) _sourceFigNode.removeFigEdge(this);
    _sourceFigNode = fn;
    fn.addFigEdge(this);
  }

  /** Set the FigNode reprenting this FigEdge's to-node. */ 
  public void destFigNode(FigNode fn) {
    // assert fn != null
    if (_destFigNode != null) _destFigNode.removeFigEdge(this);
    _destFigNode = fn;
    fn.addFigEdge(this);
  }

  /** Set the edge (some object in an underlying model) that this
   *  FigEdge should represent. */
  public void setOwner(Object own) {
    Object oldOwner = getOwner();

    if (oldOwner != null && oldOwner instanceof GraphEdgeHooks) 
      ((GraphEdgeHooks)oldOwner).removePropertyChangeListener(this);
    else if (oldOwner != null && oldOwner instanceof Highlightable) 
      ((Highlightable)oldOwner).removePropertyChangeListener(this);

    if (own instanceof GraphEdgeHooks)
      ((GraphEdgeHooks)own).addPropertyChangeListener(this);
    else if (own instanceof Highlightable)
      ((Highlightable)own).addPropertyChangeListener(this);

    super.setOwner(own);
  }


  /** Get the ArrowHead at the start of this FigEdge. */
  public ArrowHead getSourceArrowHead() { return _arrowHeadStart; }

  /** Get the ArrowHead at the end of this FigEdge. */
  public ArrowHead getDestArrowHead() { return _arrowHeadEnd; }

  /** Set the ArrowHead at the start of this FigEdge. */
  public void setSourceArrowHead(ArrowHead newArrow) {
    _arrowHeadStart = newArrow;
  }

  /** Set the ArrowHead at the end of this FigEdge. */
  public void setDestArrowHead(ArrowHead newArrow) {
    _arrowHeadEnd = newArrow;
  }

  /** Return the vector of path items on this FigEdge. */
  public Vector getPathItemsRaw() { return _pathItems; }

  /** Return the path item on this FigEdge closest to the given
   *  location. needs-more-work: not implemented yet. */
  public Fig getPathItem(PathConv pointOnPath) { 
    // needs-more-work: Find the closest Fig to this point
    return null;
  }

  /** Add a new path item to this FigEdge. newPath indicates both the
   *  location and the Fig (usually FigText) that should be drawn. */
  public void addPathItem(Fig newFig, PathConv newPath) {
    _pathItems.addElement(new PathItem(newFig, newPath));
  }

  /** Removes the given path item. */
  public void removePathItem(PathItem goneItem) {
    _pathItems.removeElement(goneItem);    
  }

  /** Get and set the flag about using Fig connection points rather
   *  than centers. */
  public boolean getBetweenNearestPoints() { return _useNearest; }
  public void setBetweenNearestPoints(boolean un) { _useNearest = un; }

  ////////////////////////////////////////////////////////////////
  // Routing related methods

  /** Method to compute the route a FigEdge should follow.  By defualt
   *  this does nothing. Sublcasses, like FigEdgeRectiline override
   *  this method. */
  protected void computeRoute() { }

  ////////////////////////////////////////////////////////////////
  // Fig API

  /** Reply the bounding box for this FigEdge. */
  public Rectangle getBounds() {
    Rectangle res = _fig.getBounds();
    Enumeration enum = _pathItems.elements();
    while (enum.hasMoreElements()) {
      Fig f = ((PathItem) enum.nextElement()).getFig();
      res = res.union(f.getBounds());
    }
    return res;
  }

  /** Update my bounding box */
  protected void calcBounds() {
    _fig.calcBounds();
    Rectangle res = _fig.getBounds();
    Vector pathVec = getPathItemsRaw();

    Point loc = new Point();
    for (int i = 0; i < pathVec.size(); i++) {
      PathItem element = (PathItem) pathVec.elementAt(i);
      PathConv path = element.getPath();
      Fig f = element.getFig();
      int halfWidth = f.getWidth() / 2;
      int halfHeight = f.getHeight() / 2;
      path.stuffPoint(loc);
      f.setLocation(loc.x - halfWidth, loc.y - halfHeight);
      res = res.union(f.getBounds());
    }


    _x = res.x;
    _y = res.y;
    _w = res.width;
    _h = res.height;
  }

  public boolean contains(int x, int y) {
    if (_fig.contains(x, y)) return true;
    Enumeration enum = _pathItems.elements();
    while (enum.hasMoreElements()) {
      Fig f = ((PathItem) enum.nextElement()).getFig();
      if (f.contains(x, y)) return true;
    }
    return false;
  }

  public boolean intersects(Rectangle r) {
    if (_fig.intersects(r)) return true;
    Enumeration enum = _pathItems.elements();
    while (enum.hasMoreElements()) {
      Fig f = ((PathItem) enum.nextElement()).getFig();
      if (f.intersects(r)) return true;
    }
    return false;
  }

  public boolean hit(Rectangle r) {
    if (_fig.hit(r)) return true;
    Enumeration enum = _pathItems.elements();
    while (enum.hasMoreElements()) {
      Fig f = ((PathItem) enum.nextElement()).getFig();
      if (f.hit(r)) return true;
    }
    return false;
  }

  public int getPerimeterLength() { return _fig.getPerimeterLength(); }

  public void stuffPointAlongPerimeter(int dist, Point res) {
    _fig.stuffPointAlongPerimeter(dist, res);
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

  /** Paint ArrowHeads on this FigEdge. Called from paint().
   *  Determines placement and orientation by using
   *  pointAlongPerimeter(). */
  protected void paintArrowHeads(Graphics g) {
    _arrowHeadStart.paint(g, pointAlongPerimeter(5), pointAlongPerimeter(0));
    _arrowHeadEnd.paint(g, pointAlongPerimeter(getPerimeterLength() - 6),
			pointAlongPerimeter(getPerimeterLength() - 1));
  }


  /** Paint any labels that are located relative to this FigEdge. */
  protected void paintPathItems(Graphics g) {
    Vector pathVec = getPathItemsRaw();

    for (int i = 0; i < pathVec.size(); i++) {
      PathItem element = (PathItem) pathVec.elementAt(i);
//       PathConv path = element.getPath();
      Fig f = element.getFig();
//       int halfWidth = f.getWidth() / 2;
//       int halfHeight = f.getHeight() / 2;
//       Point loc = path.getPoint();
//       f.setLocation(loc.x - halfWidth, loc.y - halfHeight);
      f.paint(g);
    }
  }

  /** Paint this FigEdge.  Needs-more-work: take Highlight into account */
  public void paint(Graphics g) {
    //computeRoute();
    _fig.paint(g);
    paintArrowHeads(g);
    paintPathItems(g);
  }

  
  ////////////////////////////////////////////////////////////////
  // notifications and updates

  public void propertyChange(PropertyChangeEvent pce) {
    //System.out.println("FigEdge got a PropertyChangeEvent");
    String pName = pce.getPropertyName();
    Object src = pce.getSource();
    if (pName.equals("dispose") && src == getOwner()) { delete(); }
    if (pName.equals("highlight") && src == getOwner()) {
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
