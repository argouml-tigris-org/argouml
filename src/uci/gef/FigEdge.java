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




// File: FigEdge.java
// Classes: FigEdge
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import com.sun.java.util.collections.*;
import java.util.Enumeration;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.beans.*;
import javax.swing.*;

import uci.util.*;
import uci.ui.*;
import uci.graph.*;
import uci.argo.kernel.*;

/** Abastract Fig class for representing edges between ports.
 *
 *  @see FigEdgeLine
 *  @see FigEdgeRectiline
 */

public abstract class FigEdge extends Fig
implements PropertyChangeListener, Highlightable {

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
  protected ArrowHead _arrowHeadStart = ArrowHeadNone.TheInstance;
  /** The ArrowHead at the end of the line */
  protected ArrowHead _arrowHeadEnd = ArrowHeadNone.TheInstance;
  /** The items that are accumulated along the path, a vector. */
  protected Vector _pathItems = new Vector();

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Contruct a new FigEdge with the given source and destination
   *  port figs and FigNodes.  The new FigEdge will represent the
   *  given edge (an object from some underlying model). */
  public FigEdge(Fig s, Fig d, FigNode sfn, FigNode dfn, Object edge) {
    setSourcePortFig(s);
    setDestPortFig(d);
    setSourceFigNode(sfn);
    setDestFigNode(dfn);
    setOwner(edge);
    _fig = makeEdgeFig();
    _fig.setGroup(this);
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

  public void setFig(Fig f) {
    if (_fig != null && _fig.getGroup() == this) _fig.setGroup(null);
    _fig = f;
    _fig.setGroup(this);
  }

  /** Get the Fig reprenting this FigEdge's from-port. */ 
  public void setSourcePortFig(Fig fig) { _sourcePortFig = fig; }
  public Fig getSourcePortFig() { return _sourcePortFig; }

  /** Get the Fig reprenting this FigEdge's to-port. */ 
  public void setDestPortFig(Fig fig) { _destPortFig = fig; }
  public Fig getDestPortFig() { return _destPortFig; }

  public Fig getSourceFigNode() { return _sourceFigNode; }
  public Fig getDestFigNode() { return _destFigNode; }

  /** Set the FigNode reprenting this FigEdge's from-node. */ 
  public void setSourceFigNode(FigNode fn) {
    // assert fn != null
    if (_sourceFigNode != null) _sourceFigNode.removeFigEdge(this);
    _sourceFigNode = fn;
    fn.addFigEdge(this);
  }

  /** Set the FigNode reprenting this FigEdge's to-node. */ 
  public void setDestFigNode(FigNode fn) {
    // assert fn != null
    if (_destFigNode != null) _destFigNode.removeFigEdge(this);
    _destFigNode = fn;
    fn.addFigEdge(this);
  }

  public Fig hitFig(Rectangle r) {
    Enumeration enum = _pathItems.elements();
    Fig res = null;
    if (_fig.hit(r)) res = _fig;
    while (enum.hasMoreElements()) {
      PathItem pi = (PathItem) enum.nextElement();
      Fig f = pi.getFig();
      if (f.hit(r)) res = f;
    }
    return res;
  }


  /** Set the edge (some object in an underlying model) that this
   *  FigEdge should represent. */
  public void setOwner(Object own) {
    Object oldOwner = getOwner();

    if (oldOwner instanceof GraphEdgeHooks) 
      ((GraphEdgeHooks)oldOwner).removePropertyChangeListener(this);
    else if (oldOwner instanceof Highlightable) 
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
    newFig.setGroup(this);
  }

  /** Removes the given path item. */
  public void removePathItem(PathItem goneItem) {
    _pathItems.removeElement(goneItem);
    goneItem.getFig().setGroup(null);
  }

  /** Get and set the flag about using Fig connection points rather
   *  than centers. */
  public boolean getBetweenNearestPoints() { return _useNearest; }
  public void setBetweenNearestPoints(boolean un) { _useNearest = un; }

  ////////////////////////////////////////////////////////////////
  // Routing related methods

  /** MMethod to compute the route a FigEdge should follow.  By defualt
   *  this does nothing. Sublcasses, like FigEdgeRectiline override
   *  this method. */
  protected void computeRoute() { }

  ////////////////////////////////////////////////////////////////
  // Fig API

//   public void startTrans() {
//     super.startTrans();
//     int size = _pathItems.size();
//     for (int i = 0; i < size; i+) {
//       Fig f = ((PathItem) _pathItems.elementAt(i)).getFig();
//       f.startTrans();
//     }    
//   }
  
  /** Reply the bounding box for this FigEdge. */
  public Rectangle getBounds() {
    Rectangle res = _fig.getBounds();
    int size = _pathItems.size();
    for (int i = 0; i < size; i++) {
      Fig f = ((PathItem) _pathItems.elementAt(i)).getFig();
      res.add(f.getBounds());
    }
    return res;
  }

  /** Update my bounding box */
  public void calcBounds() {
    _fig.calcBounds();
    Rectangle res = _fig.getBounds();

    Point loc = new Point();
    int size = _pathItems.size();
    for (int i = 0; i < size; i++) {
      PathItem element = (PathItem) _pathItems.elementAt(i);
      PathConv pc = element.getPath();
      Fig f = element.getFig();
      int oldX = f.getX();
      int oldY = f.getY();
      int halfWidth = f.getWidth() / 2;
      int halfHeight = f.getHeight() / 2;
      pc.stuffPoint(loc);
      if (oldX != loc.x || oldY != loc.y) {
	f.damage();
	f.setLocation(loc.x - halfWidth, loc.y - halfHeight);
      }
      res.add(f.getBounds());
    }

    _x = res.x;
    _y = res.y;
    _w = res.width;
    _h = res.height;
  }

  public void updatePathItemLocations() { calcBounds(); }

  public boolean contains(int x, int y) {
    if (_fig.contains(x, y)) return true;
    int size = _pathItems.size();
    for (int i = 0; i < size; i++) {
      Fig f = ((PathItem) _pathItems.elementAt(i)).getFig();
      if (f.contains(x, y)) return true;
    }
    return false;
  }

  public boolean intersects(Rectangle r) {
    if (_fig.intersects(r)) return true;
    int size = _pathItems.size();
    for (int i = 0; i < size; i++) {
      Fig f = ((PathItem) _pathItems.elementAt(i)).getFig();
      if (f.intersects(r)) return true;
    }
    return false;
  }

  public boolean hit(Rectangle r) {
    if (_fig.hit(r)) return true;
    int size = _pathItems.size();
    for (int i = 0; i < size; i++) {
      Fig f = ((PathItem) _pathItems.elementAt(i)).getFig();
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

  public Point getFirstPoint() { return _fig.getFirstPoint(); }
  public Point getLastPoint() { return _fig.getLastPoint(); }

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

  public void translateEdge(int dx, int dy) {
    _fig.translate(dx, dy);
    calcBounds();
  }

  public void setLineColor(Color c) { _fig.setLineColor(c); }
  public Color getLineColor() { return _fig.getLineColor(); }

  public void setLineWidth(int w) { _fig.setLineWidth(w); }
  public int getLineWidth() { return _fig.getLineWidth(); }

  public void setDashed(boolean d) { _fig.setDashed(d); }
  public boolean getDashed() { return _fig.getDashed(); }

  public boolean isResizable() { return _fig.isResizable(); }
  public boolean isReshapable() { return _fig.isReshapable(); }
  public boolean isRotatable() { return _fig.isRotatable(); }

  ////////////////////////////////////////////////////////////////
  // Highlightable implementation
  public void setHighlight(boolean b) {
    _highlight = b;
    damage();
  }
  public boolean getHighlight() { return _highlight; }

  ////////////////////////////////////////////////////////////////
  // display methods

  /** Paint ArrowHeads on this FigEdge. Called from paint().
   *  Determines placement and orientation by using
   *  pointAlongPerimeter(). */
  protected void paintArrowHeads(Graphics g) {
    _arrowHeadStart.paintAtHead(g, _fig);
    _arrowHeadEnd.paintAtTail(g, _fig);
    //     _arrowHeadStart.paint(g, pointAlongPerimeter(5), pointAlongPerimeter(0));
    //     _arrowHeadEnd.paint(g, pointAlongPerimeter(getPerimeterLength() - 6),
    // 			pointAlongPerimeter(getPerimeterLength() - 1));
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

  public void paintHighlightLine(Graphics g, int x1, int y1,
				 int x2, int y2) {
    g.setColor(Globals.getPrefs().getHighlightColor()); /* needs-more-work */

    double dx = (double)(x2 - x1);
    double dy = (double)(y2 - y1);
    double denom = Math.sqrt(dx*dx+dy*dy);
    if (denom == 0) return;
    double orthoX =  dy / denom;
    double orthoY = -dx / denom;

    // needs-more-work: should fill poly instead
    for (double i = 2.0; i < 5.0; i += 0.27) {
      int hx1  = (int)(x1 + i * orthoX);
      int hy1  = (int)(y1 + i * orthoY);
      int hx2  = (int)(x2 + i * orthoX);
      int hy2  = (int)(y2 + i * orthoY);
      g.drawLine(hx1, hy1, hx2, hy2);
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

  /** After the file is loaded, re-establish any connections from the
   * model to the Figs */
  public void postLoad() { setOwner(getOwner()); }

  public void cleanUp() { _fig.cleanUp(); }

  public void delete() {
    if (_sourceFigNode != null) _sourceFigNode.removeFigEdge(this);
    if (_destFigNode != null) _destFigNode.removeFigEdge(this);
    super.delete();
  }

  ////////////////////////////////////////////////////////////////
  // inner classes
  protected class PathItem implements java.io.Serializable {
    Fig _fig;
    PathConv _path;

    PathItem(Fig f, PathConv pc) {
      _fig = f;
      _path = pc;
    }

    PathConv getPath() { return _path; }
    Fig getFig() { return _fig; }
  }

  static final long serialVersionUID = 5276706932173784022L;

} /* end class FigEdge */
