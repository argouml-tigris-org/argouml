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

// File: Fig.java
// Classes: Fig
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.beans.*;
import java.util.*;
import uci.util.*;
import uci.ui.*;

/** This class is the base class for basic drawing objects
 *  such as rectangles, lines, text, circles, etc. Also, class FigGroup
 *  implements a composite figure. Fig's are Diagram elements that can
 *  be placed in any LayerDiagram. Fig's are also used to define the
 *  look of FigNode's on NetNode's.<p>
 *
 *  <A HREF="../features.html#basic_shapes">
 *  <TT>FEATURE: basic_shapes</TT></A>.
 *
 * @see FigRect
 * @see FigLine
 * @see FigText
 * @see FigCircle */

public class Fig extends EventHandler
implements Observer, java.io.Serializable  {

  ////////////////////////////////////////////////////////
  // instance variables

  /** The Layer that this Fig is in.  Each
   *  Fig can be in exactly one Layer, but there can be
   *  multiple Editors on a given Layer. */
  protected Layer _layer = null;

  /** True if this object is locked and cannot be moved by the user.
   *  Needs-More-Work: not implemented yet.
   *  <A HREF="../features.html#locked_objects">
   *  <TT>FEATURE: locked_objects</TT></A>
   */
  protected boolean _locked = false;

  /** owner of fig object. Owners are underlying objects that "own"
   *  the graphical Fig's that represent them. For example,
   *  a FigNode and FigEdge keep a pointer to the net-level
   *  object that they represent. Also, any Fig can have NetPort as an
   *  owner. */
  private Object _owner;

// //   transient protected PropertyChangeSupport _changes =
// //   new PropertyChangeSupport(this);

  protected int _x;
  protected int _y;
  protected int _w;
  protected int _h;

  /** Graphical attributes of figures. Other code that is accessing
   *  these attruibutes should use the get and set functions for
   *  graphical attributes.
   *
   * @see Fig#get */

  /** Outline color of fig object. */
  protected Color _lineColor = Color.black;
  /** Fill color of fig object. */
  protected Color _fillColor = Color.white;
  /** Thickness of line around object, for now limited to 0 or 1. */
  protected int _lineWidth = 1;
  /** True if the object should fill in its area. */
  protected boolean _filled = true;
//   /** Future color of shadows. */
//   protected Color _shadowColor = Color.gray;
//   /** Future offset of shadows. */
//   protected Point _shadowOffset = new Point(0, 0);

  ////////////////////////////////////////////////////////////////
  // static initializer

  static {
    //needs-more-work: get rect editor to work
    //PropCategoryManager.categorizeProperty("Geometry", "bounds");
    PropCategoryManager.categorizeProperty("Geometry", "x");
    PropCategoryManager.categorizeProperty("Geometry", "y");
    PropCategoryManager.categorizeProperty("Geometry", "width");
    PropCategoryManager.categorizeProperty("Geometry", "height");
    PropCategoryManager.categorizeProperty("Geometry", "filled");
    PropCategoryManager.categorizeProperty("Geometry", "locked");
    PropCategoryManager.categorizeProperty("Style", "lineWidth");
    PropCategoryManager.categorizeProperty("Style", "fillColor");
    PropCategoryManager.categorizeProperty("Style", "lineColor");
    PropCategoryManager.categorizeProperty("Style", "filled");
  }

  ////////////////////////////////////////////////////////////////
  // constuctors

  /** Construct a new Fig with the given attributes and number of handles. */
  public Fig(int x, int y, int w, int h,
		 Color lineColor, Color fillColor, Object own) {
    _x = x; _y = y; _w = w; _h = h;
    if (_lineColor != null) _lineColor = lineColor; else _lineWidth = 0;
    if (_fillColor != null) _fillColor = fillColor; else _filled = false;
    setOwner(own);
  }

  /** Construct a new Fig with the given attributes and number of handles. */
  public Fig(int x, int y, int w, int h, Color lineColor, Color fillColor) {
    this(x, y, w, h, lineColor, fillColor, null);
  }

  public Fig(int x, int y, int w, int h) {
    this(x, y, w, h, Color.black, Color.white, null);
  }

  /** Most subclasses will not use this constructor, it is only useful
   *  for subclasses that redefine most of the infrastructure provided
   *  by class Fig. */
  public Fig() { } // needs-more-work: needed?

  ////////////////////////////////////////////////////////////////
  // invariant

  public boolean OK() {
    // super.OK() //?
    return _lineWidth >= 0 && _lineColor != null && _fillColor != null;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void assignLayer(Layer lay) { _layer = lay; } //?
  public void setLayer(Layer lay) {
    firePropChange("layer", _layer, lay);
    _layer = lay;
  }
  public Layer getLayer() { return _layer; }

  public void assignLocked(boolean b) { _locked = b; } //?
  public void setLocked(boolean b) {
    firePropChange("locked", _locked, b);
    _locked = b;
  }
  public boolean getLocked() { return _locked; }


  /** Get and set the owner object of this Fig */
  public void setOwner(Object own) {
    firePropChange("owner", _owner, own);
    _owner = own;
  }
  public Object getOwner() { return _owner; }

  /** Internal function to change the line color attribute. Other code
   *  should use put(String key, Object value). */
  public void assignLineColor(Color col) { _lineColor = col; } //?
  public void setLineColor(Color col) {
    if (col != null) {
      firePropChange("lineColor", _lineColor, col);
      _lineColor = col;
    }
    else {
      firePropChange("lineWidth", _lineWidth, 0);
      _lineWidth = 0;
    }
  }
  public Color getLineColor() { return _lineColor; }

  /** Internal function to change the fill color attribute. Other code
   *  should use put(String key, Object value). */
  public void assignFillColor(Color col) { _fillColor = col; } //?
  public void setFillColor(Color col) {
    if (col != null) {
      firePropChange("fillColor", _fillColor, col);
      _fillColor = col;
    }
    else {
      firePropChange("filled", _filled, false);
      _filled = false;
    }
  }
  public Color getFillColor() { return _fillColor; }

  /** Internal function to change the whether fill color attribute is used.
   *  Other code should use put(String key, Object value). */
  public void assignFilled(boolean f) { _filled = f; } //?
  public void setFilled(boolean f) {
    firePropChange("filled", _filled, f);
    _filled = f;
  }
  public boolean getFilled() { return _filled; }

  /** Internal function to change the line width attribute is used.
   *  Other code should use put(String key, Object value). */
  public void assignLineWidth(int w) { //?
    _lineWidth = Math.max(0, Math.min(1, w));
  }
  public void setLineWidth(int w) {
    int newLW = Math.max(0, Math.min(1, w));
    firePropChange("lineWidth", _lineWidth, newLW);
    _lineWidth = newLW;
  }
  public int getLineWidth() { return _lineWidth; }

//   public void assignShadowColor(Color c) { _shadowColor = c; } //?
//   public void setShadowColor(Color c) { _shadowColor = c; }
//   public Color getShadowColor() { return _shadowColor; }

//   public void assignShadowOffset(Point p) { _shadowOffset = p; } //?
//   public void setShadowOffset(Point p) { _shadowOffset = p; }
//   public Point getShadowOffset() { return _shadowOffset; }

  public void print(Graphics g) { paint(g); }

  /** Method to paint this Fig.  By default it paints an "empty" space. */
  public void paint(Graphics g) {
    g.setColor(Color.pink);
    g.fillRect(_x, _y, _w, _h);
    g.setColor(Color.black);
    g.drawString("(undefined)", _x + _w/2, _y + _h/2);
  }

  /** Return a Rectangle that completely encloses this Fig. */
  public Rectangle getBounds() { return new Rectangle(_x, _y, _w, _h); }

  public void stuffBounds(Rectangle r) { r.reshape(_x, _y, _w, _h); }

  public final void setBounds(Rectangle r) {
    setBounds(r.x, r.y, r.width, r.height);
  }

  // make this a bottle-neck method?
  public void setBounds(int x, int y, int w, int h) {
    Rectangle oldBounds = getBounds();
    _x = x; _y = y; _w = w; _h = h;
    firePropChange("bounds", oldBounds, getBounds());
  }


  ////////////////////////////////////////////////////////////////
  // Editor API

  /** When a graphical object needs to be redraw it is said to be
   *  "damaged". This informs the editor that this object is damaged.
   *  Subclasses may implement this method differently.  For example
   *  FigNode damages each of its connected FigEdges. */
  public void damagedIn(Editor ed) { ed.damaged(this); }

  private static Vector _removeVector = null;
  /** Remove this Fig from the document being edited by the
   *  given editor.
   *  <A HREF="../features.html#removing_objects_delete">
   *  <TT>FEATURE: removing_objects_delete</TT></A>
   */
  public void removeFrom(Editor ed) {
    if (_removeVector == null) {
      _removeVector = new Vector(2);
      _removeVector.addElement(Globals.REMOVE);
      _removeVector.addElement(this);
    }
    else _removeVector.setElementAt(this, 1);
    setChanged();
    notifyObservers(_removeVector);
    if (_layer != null) _layer.notifyRemoved(this);
  }

  /** Remove this object from view and from all underlying models. By
   *  default it assumed that there are no underlying
   *  models. Subclasses like FigNode and FigEdge make the
   *  opposite assumption.
   *  <A HREF="../features.html#removing_objects_dispose">
   *  <TT>FEATURE: removing_objects_dispose</TT></A>
   *
   * @see FigNode
   * @see FigEdge */
  public void dispose(Editor ed) { removeFrom(ed); }

  public boolean isMovable() { return true; }
  public boolean isResizable() { return true; }
  public boolean isReshapable() { return false; }
  public boolean isRotatable() { return false; }

  /** This indicates that some Action is starting a manipulation on
   *  the receiving Fig and that redrawing must take place
   *  at the objects old location. This is implemented by notifying the
   *  Observer's of this object that it has changed. That will eventually
   *  result in damage regions being added to all editors that are
   *  displaying this object.<p>
   *
   *  <A HREF="../features.html#visual_updates">
   *  <TT>FEATURE: visual_updates</TT></A>
   *  <A HREF="../features.html#viewable_properties">
   *  <TT>FEATURE: viewable_properties</TT></A>
   */
  public void startTrans() {
    if (_layer != null) _layer.notifyChanged(this);
    RedrawManager.lock(); // helps avoid dirt
  }

  /** This is called after an Action mondifies a Fig and
   *  the Fig needs to be redrawn in its new position. Each
   *  endTrans shuold be paired with one startTrans(). <p>
   *  <A HREF="../features.html#visual_updates">
   *  <TT>FEATURE: visual_updates</TT></A>
   */
  public void endTrans() {
    if (_layer != null) _layer.notifyChanged(this);
    RedrawManager.unlock();  // helps avoid dirt
  }


  ////////////////////////////////////////////////////////////////
  // geometric manipulations

  /** Margin between this Fig and automatically routed arcs. */
  public final int BORDER = 8;

  /** Reply a rectangle that arcs shoulc not route through. Basically
   *  this is the bounding box plus some margin around all egdes. */
  public Rectangle routingRect() {
    return new Rectangle(_x-BORDER, _y-BORDER, _w+BORDER*2, _h+BORDER*2);
  }

  /** change the back-to-front ordering of a Fig in
   *  LayerDiagram. Should the Fig have any say in it?
   *
   * @see LayerDiagram#reorder
   * @see ActionReorder */
  public void reorder(int func, Layer lay) { lay.reorder(this, func); }

  /** Change the position of the object from were it is
   *  to were it is plus dx or dy. Often called when an object is
   *  dragged. This could be very useful if local-coordinate systems are
   *  used because deltas need less transforming... maybe. */
  // should this call setBounds?
  public void translate(int dx, int dy) {
    Rectangle oldBounds = getBounds();
    _x += dx; _y += dy;
    firePropChange("bounds", oldBounds, getBounds());
  }


  /** Move the FigNode to the given position. */
  public final void setLocation(Point p) { setLocation(p.x, p.y); }
  public Point getLocation() { return new Point(_x, _y); }

  public void setLocation(int x, int y) { translate(x - _x, y - _y); }


  public Dimension getSize() { return new Dimension(_w, _h); }
  public final void setSize(Dimension d) { setSize(d.width, d.height); }
  public void setSize(int w, int h) {
    setBounds(_x, _y, w, h);
  }


  // needs-more-work: property change events?
  public void setX(int x) { setBounds(x, _y, _w, _h); }
  public int getX() { return _x; }

  public void setY(int y) { setBounds(_x, y, _w, _h); }
  public int getY() { return _y; }

  public void setWidth(int w) { setBounds(_x, _y, w, _h); }
  public int getWidth() { return _w; }

  public void setHeight(int h) { setBounds(_x, _y, _w, h); }
  public int getHeight() { return _h; }

  /** Get and set the points along the path */
  public void setPoints(Point[] ps) { }
  public Point[] getPoints() { return new Point[0]; }
  public final void setPoints(int i, Point p) { setPoints(i, p.x, p.y); }
  public final void setPoints(Handle h, Point p) { setPoints(h, p.x, p.y); }
  public void setPoints(int i, int x, int y) { }
  public void setPoints(Handle h, int x, int y) { setPoints(h.index, x, y); }
  public Point getPoints(int i) { return null; }
  public int getNumPoints() { return 0; }
  public void setNumPoints(int npoints) { }
  public int[] getXs() { return new int[0]; }
  public void setXs(int[] xs) { }
  public int[] getYs() { return new int[0]; }
  public void setYs(int[] ys) { }

  public void addPoint(int x, int y) { } //@
  public void insertPoint(int i, int x, int y) { } //@
  public void removePoint(int i) { } //@


  public int getPerimeterLength() { return _w + _w + _h + _h; }

  public Point pointAlongPerimeter(int dist) {
    if (dist < _w && dist >= 0)
      return new Point(_x + (dist), _y);
    else if (dist < _w + _h)
      return new Point(_x + _w, _y + (dist - _w));
    else if (dist < _w + _h + _w)
      return new Point(_x + _w - (dist - _w - _h), _y + _h);
    else if (dist < _w + _h + _w + _h)
      return new Point(_x, _y + (_w + _h + _w + _h - dist));
    else
      return new Point(_x, _y);
  }

  /** Align this Fig with the given rectangle. Some
   *  subclasses may need to know the editor that initiated this action.
   *  <A HREF="../features.html#align_objects">
   *  <TT>FEATURE: align_objects</TT></A>
   */
  public void align(Rectangle r, int direction, Editor ed) {
    Rectangle bbox = getBounds();
    int dx = 0, dy = 0;
    switch (direction) {
    case ActionAlign.ALIGN_TOPS:
      dy = r.y - bbox.y;
      break;
    case ActionAlign.ALIGN_BOTTOMS:
      dy = r.y + r.height - (bbox.y + bbox.height);
      break;
    case ActionAlign.ALIGN_LEFTS:
      dx = r.x - bbox.x;
      break;
    case ActionAlign.ALIGN_RIGHTS:
      dx = r.x + r.width - (bbox.x + bbox.width);
      break;
    case ActionAlign.ALIGN_CENTERS:
      dx = r.x + r.width/2 - (bbox.x + bbox.width/2);
      dy = r.y + r.height/2 - (bbox.y + bbox.height/2);
      break;
    case ActionAlign.ALIGN_H_CENTERS:
      dx = r.x + r.width/2 - (bbox.x + bbox.width/2);
      break;
    case ActionAlign.ALIGN_V_CENTERS:
      dy = r.y + r.height/2 - (bbox.y + bbox.height/2);
      break;
    case ActionAlign.ALIGN_TO_GRID:
      Point loc = getLocation();
      Point snapPt = new Point(loc.x, loc.y);
      ed.snap(snapPt);
      dx = snapPt.x - loc.x;
      dy = snapPt.y - loc.y;
      break;
    }
    translate(dx, dy);
  }


  /** Reply true if the given point is inside the given
   *  Fig. By default reply true if the pint is in my
   *  bounding box. Subclasses like FigCircle and FigEdge do
   *  more specific checks.
   *
   * @see FigCircle
   * @see FigEdge */
  public boolean contains(int x, int y) {
    return (_x <= x) && (x <= _x + _w) && (_y <= y) && (y <= _y + _h);
  }

  /** Reply true if the given point is inside this Fig by
   *  calling contains(int x, int y). */
  public final boolean contains(Point p) { return contains(p.x, p.y); }

  public boolean contains(Rectangle r) {
    return countCornersContained(r.x, r.y, r.width, r.height) == 4;
  }

  public boolean within(Rectangle r) {
    return r.contains(_x, _y) && r.contains(_x + _w, _y + _h);
  }

  public boolean hit(Rectangle r) {
    int cornersHit = countCornersContained(r.x, r.y, r.width, r.height);
    if (_filled) return cornersHit > 0;
    else return cornersHit > 0 && cornersHit < 4;
  }

  protected int countCornersContained(int x, int y, int w, int h) {
    int cornersHit = 0;
    if (contains(x, y)) cornersHit++;
    if (contains(x + w, y)) cornersHit++;
    if (contains(x, y + h)) cornersHit++;
    if (contains(x + w, y + h)) cornersHit++;
    return cornersHit;
  }


  /** Reply true if the object intersects the given rectangle. Used
   *  for selective redrawing and for multiple selections. <p>
   *  needs-more-work: we probably need a within(Rectangle) operation
   *  to do marquee selection properly. */
  public boolean intersects(Rectangle r) { return getBounds().intersects(r); }

  /** return the center of the given figure. By default the center is
   *  the center of the bounding box. Subclasses may want to define
   *  something else. */
  public Point center() {
    Rectangle bbox = getBounds();
    return new Point(bbox.x + bbox.width/2, bbox.y + bbox.height/2);
  }

  /** return a point that should be used for arcs that to toward the
   *  given point. For example, you may want the arc to end on the edge
   *  of a port that is nearest the given point. By default this is
   *  center(). */
  public Point connectionPoint(Point anotherPt) { return center(); }

  /** Resize the object for drag on creation. It bypasses the things
   *  done in resize so that the position of the object can be kept as
   *  the anchor point. Needs-More-Work: do I really need this
   *  function?
   *
   * @see FigLine#drag */
  public void createDrag(int anchorX, int anchorY, int x, int y,
			 int snapX, int snapY) {
    int newX = Math.min(anchorX, snapX);
    int newY = Math.min(anchorY, snapY);
    int newW = Math.max(anchorX, snapX) - newX;
    int newH = Math.max(anchorY, snapY) - newY;
    setBounds(newX, newY, newW, newH);
  }

  /** Update the bounds of this Fig.  By default it is assumed that
   *  the bounds have already been updated, so this does nothing.
   *
   * @see FigText */
  protected void calcBounds() { }


  ////////////////////////////////////////////////////////////////
  // updates

  /** The specified PropertyChangeListeners <b>propertyChange</b>
   * method will be called each time the value of any bound property
   * is changed.  The PropertyListener object is addded to a list of
   * PropertyChangeListeners managed by the JellyBean, it can be
   * removed with removePropertyChangeListener.  Note: the JavaBeans
   * specification does not require PropertyChangeListeners to run in
   * any particular order.
   *
   * @see #removePropertyChangeListener
   * @param l the PropertyChangeListener */
  public void addPropertyChangeListener(PropertyChangeListener l) {
    // _changes.addPropertyChangeListener(l);
    Globals.addPropertyChangeListener(this, l);
  }

  /** Remove this PropertyChangeListener from the JellyBeans internal
   * list.  If the PropertyChangeListener isn't on the list, silently
   * do nothing.
   * 
   * @see #addPropertyChangeListener
   * @param l the PropertyChangeListener */
  public void removePropertyChangeListener(PropertyChangeListener l) {
    //_changes.removePropertyChangeListener(l);
    Globals.removePropertyChangeListener(this, l);
  }

  protected void firePropChange(String propName, Object oldV, Object newV) {
    Globals.firePropChange(this, propName, oldV, newV);
  }

  protected void firePropChange(String propName, int oldV, int newV) {
    Globals.firePropChange(this, propName,
			   new Integer(oldV), new Integer(newV));
  }

  protected void firePropChange(String propName, boolean oldV, boolean newV) {
    Globals.firePropChange(this, propName,
			   new Boolean(oldV), new Boolean(newV));
  }

  public void update(Observable o, Object arg) {
    /* If I dont handle it, maybe my observers do. */
    setChanged();
    notifyObservers(arg);
    if (_layer != null) _layer.notifyChanged(this);
  }

} /* end class Fig */
