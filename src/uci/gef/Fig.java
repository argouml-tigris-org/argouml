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

// File: Fig.java
// Classes: Fig
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.JMenu;

import uci.util.*;
import uci.ui.*;
import uci.graph.*;

/** This class is the base class for basic drawing objects such as
 *  rectangles, lines, text, circles, etc. Also, class FigGroup
 *  implements a composite figure. Fig's are Diagram elements that can
 *  be placed in any LayerDiagram. Fig's are also used to define the
 *  look of FigNodes on NetNodes. */

public class Fig
implements Cloneable, java.io.Serializable, PropertyChangeListener, PopupGenerator  {

  ////////////////////////////////////////////////////////////////
  // constants

  /** The smallest size that the user can drag this Fig. */
  public final int MIN_SIZE = 4;

  /** The size of the dashes drawn when the Fig is dashed. */
  //public final int DASH_LENGTH = 5;

  public static String DASHED_CHOICES[] = { "Solid", "Dashed" };
  public static int DASH_ARRAYS[][] = { null, {5}, {15, 5},
					{3, 10},  {3, 6, 10, 6} };


  ////////////////////////////////////////////////////////
  // instance variables

  /** The Layer that this Fig is in.  Each Fig can be in exactly one
   *  Layer, but there can be multiple Editors on a given Layer. */
  protected Layer _layer = null;

  /** True if this object is locked and cannot be moved by the user. */
  protected boolean _locked = false;

  /** Owners are underlying objects that "own" the graphical Fig's
   *  that represent them. For example, a FigNode and FigEdge keep a
   *  pointer to the net-level object that they represent. Also, any
   *  Fig can have NetPort as an owner.
   *
   * @see FigNode#setOwner
   * @see FigNode#bindPort
   */
  private Object _owner;

  /** Coordinates of the Fig's bounding box. It is the responsibility
   *  of subclasses to make sure that these values are ALWAYS up-to-date. */
  protected int _x;
  protected int _y;
  protected int _w;
  protected int _h;

  /** Outline color of fig object. */
  protected Color _lineColor = Color.black;

  /** Fill color of fig object. */
  protected Color _fillColor = Color.white;

  /** Thickness of line around object, for now limited to 0 or 1. */
  protected int _lineWidth = 1;

  protected int[] _dashes = null;

  /** True if the object should fill in its area. */
  protected boolean _filled = true;

  protected Fig _group = null;

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

  /** Construct a new Fig with the given bounds, colors, and owner. */
  public Fig(int x, int y, int w, int h,
		 Color lineColor, Color fillColor, Object own) {
    _x = x; _y = y; _w = w; _h = h;
    if (_lineColor != null) _lineColor = lineColor; else _lineWidth = 0;
    if (_fillColor != null) _fillColor = fillColor; else _filled = false;
    setOwner(own);
  }

  /** Construct a new Fig with the given bounds and colors. */
  public Fig(int x, int y, int w, int h, Color lineColor, Color fillColor) {
    this(x, y, w, h, lineColor, fillColor, null);
  }

  /** Construct a new Fig with the given bounds. */
  public Fig(int x, int y, int w, int h) {
    this(x, y, w, h, Color.black, Color.white, null);
  }

  /** Most subclasses will not use this constructor, it is only useful
   *  for subclasses that redefine most of the infrastructure provided
   *  by class Fig. */
  public Fig() { }


  public Object clone() {
    try { return super.clone(); }
    catch (CloneNotSupportedException e) { return null; }
  }

  ////////////////////////////////////////////////////////////////
  // invariant

  /** Check class invariants to make sure the Fig is in a valid state.
   *  This is useful for debugging. needs-more-work. */
  public boolean OK() {
    // super.OK() /
    return _lineWidth >= 0 && _lineColor != null && _fillColor != null;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  //public void assignLayer(Layer lay) { _layer = lay; } //?
  /** Sets the Layer that this Fig belongs to. Fires PropertyChangeEvent
   *  "layer". */
  public void setLayer(Layer lay) {
    firePropChange("layer", _layer, lay);
    _layer = lay;
  }
  public Layer getLayer() { return _layer; }

  //public void assignLocked(boolean b) { _locked = b; } //?

  /** Sets whether this Fig is locked or not.  Most Cmds check to see
   *  if Figs are locked and will not request modifications to locked
   *  Figs. Fires PropertyChangeEvent
   *  "locked". */
  public void setLocked(boolean b) {
    firePropChange("locked", _locked, b);
    _locked = b;
  }
  public boolean getLocked() { return _locked; }

  public Rectangle getTrapRect() { return getBounds(); }
  public boolean getUseTrapRect() { return false; }

  public Fig getEnclosingFig() { return null; }

  public void setEnclosingFig(Fig f) {
    if (f != null && f != getEnclosingFig() && _layer != null) {
        _layer.bringInFrontOf(this,f);
        damage();
    }
    //System.out.println("enclosing fig has been set");
  }

  public Vector getEnclosedFigs() { return null; }

  /** Sets the owner object of this Fig. Fires PropertyChangeEvent
   *  "owner" */
  public void setOwner(Object own) {
    firePropChange("owner", _owner, own);
    _owner = own;
  }
  public Object getOwner() { return _owner; }

  public String getId() {
    if (getGroup() != null) {
      String gID = getGroup().getId();
      if (getGroup() instanceof FigGroup)
	return gID + "." + ((FigGroup)getGroup()).getFigs().indexOf(this);
      else return gID + ".1";
    }
    int index = getLayer().getContents().indexOf(this);
    return "Fig" + index;
  }

  /** Sets the enclosing FigGroup of this Fig.  The enclosing group is
   * always notified of property changes, without need to add a listener. */
  public void setGroup(Fig f) { _group = f; }
  public Fig getGroup() { return _group; }

  /** Internal function to change the line color attribute. Other code
   *  should use put(String key, Object value). */
  //public void assignLineColor(Color col) { _lineColor = col; } //?

  /** Sets the color to be used if the lineWidth is > 0. If col is
   *  null, sets the lineWidth to 0.  Fires PropertyChangeEvent
   *  "lineColor", or "lineWidth".*/
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
  //public void assignFillColor(Color col) { _fillColor = col; } //?

  /** Sets the color that will be used if the Fig is filled.  If col
   *  is null, turns off filling. Fires PropertyChangeEvent
   *  "fillColor", or "filled".*/
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
  //public void assignFilled(boolean f) { _filled = f; } //?

  /** Sets a flag to either fill the Fig with its fillColor or
   *  not. Fires PropertyChangeEvent "filled". */
  public void setFilled(boolean f) {
    firePropChange("filled", _filled, f);
    _filled = f;
  }
  public boolean getFilled() { return _filled; }
  public int getFilled01() { return _filled ? 1 : 0; }

  /** Internal function to change the line width attribute is used.
   *  Other code should use put(String key, Object value). */
  //public void assignLineWidth(int w) { //?
  //  _lineWidth = Math.max(0, Math.min(1, w));
  // }

  /** Set the line width. Zero means lines are not draw. One draws
   *  them one pixel wide. Larger widths are not yet supported. Fires
   *  PropertyChangeEvent "lineWidth". */
  public void setLineWidth(int w) {
    int newLW = Math.max(0, Math.min(1, w));
    firePropChange("lineWidth", _lineWidth, newLW);
    _lineWidth = newLW;
  }
  public int getLineWidth() { return _lineWidth; }

  public void setDashedString(String dashString) {
    if (dashString.equalsIgnoreCase("solid"))
      _dashes = null;
    else
      _dashes = DASH_ARRAYS[1];
  }

  public String getDashedString() {
    return (_dashes == null) ? DASHED_CHOICES[0] : DASHED_CHOICES[1];
  }

  /** Set line to be dashed or not **/
  public void setDashed(boolean now_dashed) {
    if (now_dashed) _dashes = DASH_ARRAYS[1];
    else _dashes = null;
  }

  /** Get the dashed attribute **/
  public boolean getDashed() { return (_dashes != null); }
  public int getDashed01() { return getDashed() ? 1 : 0; }

  public String getTipString(MouseEvent me) {
    return toString();
  }

//   public void assignShadowColor(Color c) { _shadowColor = c; } //?
//   public void setShadowColor(Color c) { _shadowColor = c; }
//   public Color getShadowColor() { return _shadowColor; }

//   public void assignShadowOffset(Point p) { _shadowOffset = p; } //?
//   public void setShadowOffset(Point p) { _shadowOffset = p; }
//   public Point getShadowOffset() { return _shadowOffset; }

  /** Draw the Fig on a PrintGraphics. This just calls paint. */
  public void print(Graphics g) { paint(g); }

  /** Method to paint this Fig.  By default it paints an "empty"
   *  space, subclasses should override this method. */
  public void paint(Graphics g) {
    g.setColor(Color.pink);
    g.fillRect(_x, _y, _w, _h);
    g.setColor(Color.black);
    g.drawString("(undefined)", _x + _w/2, _y + _h/2);
  }

  protected void drawDashedPerimeter(Graphics g) {
    Point segStart = new Point();
    Point segEnd = new Point();
    int numDashes = _dashes.length;
    int length = getPerimeterLength();
    int i = 0, d = 0;
    while (i < length) {
      stuffPointAlongPerimeter(i, segStart);
      i += _dashes[d];
      d = (d + 1) % numDashes;
      stuffPointAlongPerimeter(i, segEnd);
      g.drawLine(segStart.x, segStart.y, segEnd.x, segEnd.y );
      i += _dashes[d];
      d = (d + 1) % numDashes;
    }
  }

  protected int drawDashedLine(Graphics g, int phase,
			       int x1, int y1, int x2, int y2) {
    int segStartX, segStartY;
    int segEndX, segEndY;
    int dxdx = (x2 - x1) * (x2 - x1);
    int dydy = (y2 - y1) * (y2 - y1);
    int length = (int) Math.sqrt(dxdx + dydy);
    int numDashes = _dashes.length;
    int d, dashesDist = 0;
    for (d = 0; d < numDashes; d++) {
      dashesDist += _dashes[d];
      // find first partial dash?
    }
    d = 0;
    int i= 0;
    while (i < length) {
      segStartX = x1 + ((x2 - x1) * i) / length;
      segStartY = y1 + ((y2 - y1) * i) / length;
      i += _dashes[d];
      d = (d + 1) % numDashes;
      if (i >= length) { segEndX = x2; segEndY = y2; }
      else {
	segEndX = x1 + ((x2 - x1) * i) / length;
	segEndY = y1 + ((y2 - y1) * i) / length;
      }
      g.drawLine(segStartX, segStartY, segEndX, segEndY );
      i += _dashes[d];
      d = (d + 1) % numDashes;
    }
    // needs-more-work: phase not taken into account
    return (length + phase) % dashesDist;
  }

  public String  classNameAndBounds() {
    return getClass().getName() + "[" +
      getX() + ", " + getY() + ", " +
      getWidth() + ", " + getHeight() + "]";
  }

  /** Return a Rectangle that completely encloses this Fig. */
  public Rectangle getBounds() { return new Rectangle(_x, _y, _w, _h); }

  /** Reshape the given rectangle to be my bounding box. */
  public void stuffBounds(Rectangle r) { r.setBounds(_x, _y, _w, _h); }

  /** Change my bounding box to the given Rectangle. Just calls
   *  setBounds(x, y, w, h). */
  public final void setBounds(Rectangle r) {
    setBounds(r.x, r.y, r.width, r.height);
  }

  /** Set the bounds of this Fig. Fires PropertyChangeEvent "bounds". */
  public void setBounds(int x, int y, int w, int h) {
    Rectangle oldBounds = getBounds();
    _x = x; _y = y; _w = w; _h = h;
    firePropChange("bounds", oldBounds, getBounds());
  }


  ////////////////////////////////////////////////////////////////
  // Editor API


  /** Remove this Fig from the Layer being edited by the
   *  given editor. */
  public void delete() {
    if (_layer != null) { _layer.deleted(this); }
    setOwner(null);
  }

  /** Delete whatever application object this Fig is representing, the
   *  Fig itself should automatically be deleted as a side-effect. Simple
   *  Figs have no underlying model, so they are just deleted. Figs
   *  that graphically present some part of an underlying model should
   *  NOT delete themselves, instead they should ask the model to
   *  dispose, and IF it does then the figs will be notified. */
  public void dispose() {
    Object own = getOwner();
    if (own instanceof GraphNodeHooks) ((GraphNodeHooks)own).dispose();
    if (own instanceof GraphEdgeHooks) ((GraphEdgeHooks)own).dispose();
    if (own instanceof GraphPortHooks) ((GraphPortHooks)own).dispose();
    else delete();
  }

  /** Returns a custom Selection object for use when this Fig is
   *  selected by the user.  Normally SelectionManger makes the
   *  Selection using its own rules.  This is for special cases. */
  public Selection makeSelection() { return null; }

  /** Returns true if this Fig can be moved around by the user. */
  public boolean isMovable() { return true; }

  /** Returns true if this Fig can be resized by the user. */
  public boolean isResizable() { return true; }

  /** Returns true if this Fig can be resized by the user. */
  public boolean isLowerRightResizable() { return false; }

  /** Returns true if this Fig can be reshaped by the user. */
  public boolean isReshapable() { return false; }

  /** Returns true if this Fig can be rotated by the user. */
  public boolean isRotatable() { return false; }

  /** This Fig has changed in some way, tell its Layer to record my
   *  bounding box as a damaged region so that I will eventualy be
   *  redrawn. This method can be called directly, but using
   *  startTrans() and endTrans() instead is generally faster,
   *  simpler, and better avoids "screen dirt". */
  public void damage() { if (_layer != null) _layer.damaged(this); }


  /** This indicates that some Cmd is starting a manipulation on the
   *  receiving Fig and that redrawing must take place at the objects
   *  old location. This adds a damage region to all editors that are
   *  displaying this Fig. This method also locks the RedrawManager so
   *  that no redraws will take place during the transaction.  Locking
   *  the RedrawManager is key to avoiding "screen dirt". Each call to
   *  startTrans() MUST be matched with a call to endTrans(). */
  public void startTrans() {
    damage();
    //RedrawManager.lock(); // helps avoid dirt
  }

  /** This is called after an Cmd mondifies a Fig and the Fig needs to
   * be redrawn in its new position. This also unlocks the
   * RedrawManager. In general, endTrans() should be * paired with a
   * startTrans(), although it is alright to have extra calls to
   * endTrans(). */
  public void endTrans() {
    damage();
    //RedrawManager.unlock();  // helps avoid dirt
  }


  ////////////////////////////////////////////////////////////////
  // geometric manipulations

  /** Margin between this Fig and automatically routed arcs. */
  public final int BORDER = 8;

  /** Reply a rectangle that arcs should not route through. Basically
   *  this is the bounding box plus some margin around all egdes. */
  public Rectangle routingRect() {
    return new Rectangle(_x-BORDER, _y-BORDER, _w+BORDER*2, _h+BORDER*2);
  }

  /** Change the back-to-front ordering of a Fig in
   *  LayerDiagram. Should the Fig have any say in it?
   *
   * @see LayerDiagram#reorder
   * @see CmdReorder */
  public void reorder(int func, Layer lay) { lay.reorder(this, func); }

  /** Change the position of the object from were it is to were it is
   *  plus dx and dy. Often called when an object is dragged. This
   *  could be very useful if local-coordinate systems are used
   *  because deltas need less transforming... maybe. Fires property
   *  "bounds". */
  public void translate(int dx, int dy) {
    Rectangle oldBounds = getBounds();
    _x += dx; _y += dy;
    firePropChange("bounds", oldBounds, getBounds());
  }

  /** Move the Fig to the given position. */
  public final void setLocation(Point p) { setLocation(p.x, p.y); }

  /** Move the Fig to the given position. By default translates the
   *  Fig so that the upper left corner of its bounding box is at the
   *  location. Fires property "bounds".*/
  public void setLocation(int x, int y) { translate(x - _x, y - _y); }

  /** Returns a point that is the upper left corner of the Fig's
   *  bounding box. */
  public Point getLocation() { return new Point(_x, _y); }

  /** Returns the size of the Fig. */
  public Dimension getSize() { return new Dimension(_w, _h); }

  /** Sets the size of the Fig. Fires property "bounds". */
  public final void setSize(Dimension d) { setSize(d.width, d.height); }

  /** Sets the size of the Fig. Fires property "bounds". */
  public void setSize(int w, int h) {
    setBounds(_x, _y, w, h);
  }

  /** Returns the minimum size of the Fig.  This is the smallest size
   *  that the user can make this Fig by dragging. You can ignore this
   *  and make Figs smaller programmitically if you must. */
  public Dimension getMinimumSize() { return new Dimension(MIN_SIZE, MIN_SIZE); }

  /** Returns the prefered size of the Fig. This will be useful for
   *  automated layout. By default just uses the current
   *  size. Subclasses must override to return something useful. */
  public Dimension getPreferedSize() { return new Dimension(_w, _h); }

  public Vector getPopUpActions(MouseEvent me) {
    Vector popUpActions = new Vector();
    JMenu orderMenu = new JMenu("Ordering");
    orderMenu.add(CmdReorder.BringForward);
    orderMenu.add(CmdReorder.SendBackward);
    orderMenu.add(CmdReorder.BringToFront);
    orderMenu.add(CmdReorder.SendToBack);
    popUpActions.addElement(orderMenu);
    return popUpActions;
  }

  // needs-more-work: property change events?
  public void setX(int x) { setBounds(x, _y, _w, _h); }
  public int getX() { return _x; }

  public void setY(int y) { setBounds(_x, y, _w, _h); }
  public int getY() { return _y; }

  public void setWidth(int w) { setBounds(_x, _y, w, _h); }
  public int getWidth() { return _w; }
  public int getHalfWidth() { return _w / 2; }

  public void setHeight(int h) { setBounds(_x, _y, _w, h); }
  public int getHeight() { return _h; }
  public int getHalfHeight() { return _h / 2; }

  /** Get and set the points along a path for Figs that are path-like. */
  public void setPoints(Point[] ps) { }
  public Point getFirstPoint() { return new Point(); }
  public Point getLastPoint() { return new Point();}
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

  public void addPoint(int x, int y) { }
  public void insertPoint(int i, int x, int y) { }
  public void removePoint(int i) { }

  /** Return the length of the path around this Fig. By default,
   *  returns the perimeter of the Fig's bounding box.  Subclasses
   *  like FigPoly have more specific logic. */
  public int getPerimeterLength() { return _w + _w + _h + _h; }

  /** Return a point at the given distance along the path around this
   *  Fig. By default, uses perimeter of the Fig's bounding
   *  box. Subclasses like FigPoly have more specific logic. */
  public Point pointAlongPerimeter(int dist) {
    Point res = new Point();
    stuffPointAlongPerimeter(dist, res);
    return res;
  }

  public void stuffPointAlongPerimeter(int dist, Point res) {
    if (dist < _w && dist >= 0) {
      res.x = _x + (dist);
      res.y = _y;
    }
    else if (dist < _w + _h) {
      res.x = _x + _w;
      res.y = _y + (dist - _w);
    }
    else if (dist < _w + _h + _w) {
      res.x = _x + _w - (dist - _w - _h);
      res.y = _y + _h;
    }
    else if (dist < _w + _h + _w + _h) {
      res.x = _x;
      res.y = _y + (_w + _h + _w + _h - dist);
    }
    else {
      res.x = _x;
      res.y = _y;
    }
  }

  /** Align this Fig with the given rectangle. Some subclasses may
   *  need to know the editor that initiated this action.  */
  public void align(Rectangle r, int direction, Editor ed) {
    Rectangle bbox = getBounds();
    int dx = 0, dy = 0;
    switch (direction) {
    case CmdAlign.ALIGN_TOPS:
      dy = r.y - bbox.y;
      break;
    case CmdAlign.ALIGN_BOTTOMS:
      dy = r.y + r.height - (bbox.y + bbox.height);
      break;
    case CmdAlign.ALIGN_LEFTS:
      dx = r.x - bbox.x;
      break;
    case CmdAlign.ALIGN_RIGHTS:
      dx = r.x + r.width - (bbox.x + bbox.width);
      break;
    case CmdAlign.ALIGN_CENTERS:
      dx = r.x + r.width/2 - (bbox.x + bbox.width/2);
      dy = r.y + r.height/2 - (bbox.y + bbox.height/2);
      break;
    case CmdAlign.ALIGN_H_CENTERS:
      dx = r.x + r.width/2 - (bbox.x + bbox.width/2);
      break;
    case CmdAlign.ALIGN_V_CENTERS:
      dy = r.y + r.height/2 - (bbox.y + bbox.height/2);
      break;
    case CmdAlign.ALIGN_TO_GRID:
      Point loc = getLocation();
      Point snapPt = new Point(loc.x, loc.y);
      ed.snap(snapPt);
      dx = snapPt.x - loc.x;
      dy = snapPt.y - loc.y;
      break;
    }
    translate(dx, dy);
  }


  /** Reply true if the given point is inside the given Fig. By
   *  default reply true if the point is in my bounding
   *  box. Subclasses like FigCircle and FigEdge do more specific
   *  checks.
   *
   * @see FigCircle
   * @see FigEdge */
  public boolean contains(int x, int y) {
    return (_x <= x) && (x <= _x + _w) && (_y <= y) && (y <= _y + _h);
  }

  /** Reply true if the given point is inside this Fig by
   *  calling contains(int x, int y). */
  public final boolean contains(Point p) { return contains(p.x, p.y); }

  /** Reply true if the all four corners of the given rectangle are
   *  inside this Fig, as determined by contains(int x, int y). */
  public boolean contains(Rectangle r) {
    return countCornersContained(r.x, r.y, r.width, r.height) == 4;
  }

  /** Reply true if the entire Fig is contained within the given
   *  Rectangle. This can be used by ModeSelect to select Figs that
   *  are totally within the selection rectangle. */
  public boolean within(Rectangle r) {
    return r.contains(_x, _y) && r.contains(_x + _w, _y + _h);
  }

  /** Reply true if the given rectangle contains some pixels of the
   *  Fig. This is used to determine if the user is trying to select
   *  this Fig. Rather than ask if the mouse point is in the Fig, I
   *  use a small rectangle around the mouse point so that small
   *  objects and lines are easier to select. */
  public boolean hit(Rectangle r) {
    int cornersHit = countCornersContained(r.x, r.y, r.width, r.height);
    if (_filled) return cornersHit > 0;
    else return cornersHit > 0 && cornersHit < 4;
  }

  /** Reply the number of corners of the given rectangle that are
   *  inside this Fig, as determined by contains(int x, int y). */
  protected int countCornersContained(int x, int y, int w, int h) {
    int cornersHit = 0;
    if (contains(x, y)) cornersHit++;
    if (contains(x + w, y)) cornersHit++;
    if (contains(x, y + h)) cornersHit++;
    if (contains(x + w, y + h)) cornersHit++;
    return cornersHit;
  }

  /** Reply true if the object intersects the given rectangle. Used
   *  for selective redrawing and by ModeSelect to select all Figs
   *  that are partly within the selection rectangle. */
  public boolean intersects(Rectangle r) {
    return !((r.x + r.width <= _x) ||
	     (r.y + r.height <= _y) ||
	     (r.x >= _x + _w) ||
	     (r.y >= _y + _h));
  }
  // note: computing non-intersection is faster on average.  Maybe I
  // should structure the API to allow clients to take advantage of that?

  /** Return the center of the given Fig. By default the center is the
   *  center of its bounding box. Subclasses may want to define
   *  something else. */
  public Point center() {
    Rectangle bbox = getBounds();
    return new Point(bbox.x + bbox.width/2, bbox.y + bbox.height/2);
  }

  /** Return a point that should be used for arcs that to toward the
   *  given point. By default, this makes arcs end on the edge that is
   *  nearest the given point.
   *
   * needs-more-work: define gravity points, berths
   */
  public Point connectionPoint(Point anotherPt) {
    Vector grav = getGravityPoints();
    if (grav != null && grav.size() > 0) {
      int ax = anotherPt.x;
      int ay = anotherPt.y;
      Point bestPoint = (Point) grav.elementAt(0);
      int bestDist = Integer.MAX_VALUE;
      int size = grav.size();
      for (int i = 0; i < size; i++) {
	Point gp = (Point) grav.elementAt(i);
	int dx = gp.x - ax;
	int dy = gp.y - ay;
	int dist = dx*dx + dy*dy;
	if (dist < bestDist) {
	  bestDist = dist;
	  bestPoint = gp;
	}
      }
      return new Point(bestPoint.x, bestPoint.y);
    }
    return getClosestPoint(anotherPt);
  }

  public Vector getGravityPoints() { return null; }

  public Point getClosestPoint(Point anotherPt) {
    return Geometry.ptClosestTo(getBounds(), anotherPt);
  }

  /** Resize the object for drag on creation. It bypasses the things
   *  done in resize so that the position of the object can be kept as
   *  the anchor point. Needs-More-Work: do I really need this
   *  function?
   *
   * @see FigLine#createDrag */
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
   * @see FigText#calcBounds */
  public void calcBounds() { }


  ////////////////////////////////////////////////////////////////
  // updates

  /** The specified PropertyChangeListeners <b>propertyChange</b>
   *  method will be called each time the value of any bound property
   *  is changed.  Note: the JavaBeans specification does not require
   *  PropertyChangeListeners to run in any particular order. <p>
   *
   *  Since most Fig's will never have any listeners, and I want Figs
   *  to be fairly light-weight objects, listeners are kept in a
   *  global Hashtable, keyed by Fig.  NOTE: It is important that all
   *  listeners eventually remove themselves, otherwise this will
   *  prevent garbage collection. */
  public void addPropertyChangeListener(PropertyChangeListener l) {
    Globals.addPropertyChangeListener(this, l);
  }

  /** Remove this PropertyChangeListener from the JellyBeans internal
   *  list.  If the PropertyChangeListener isn't on the list, silently
   *  do nothing.
   */
  public void removePropertyChangeListener(PropertyChangeListener l) {
    Globals.removePropertyChangeListener(this, l);
  }

  /** Creates a PropertyChangeEvent and calls all registered listeners
   *  propertyChanged() method. */
  protected void firePropChange(String propName, Object oldV, Object newV) {
    Globals.firePropChange(this, propName, oldV, newV);
    if (_group != null) {
      PropertyChangeEvent pce =
	new PropertyChangeEvent(this, propName, oldV, newV);
      _group.propertyChange(pce);
    }
  }

  protected void firePropChange(String propName, int oldV, int newV) {
    firePropChange(propName, new Integer(oldV), new Integer(newV));
  }

  protected void firePropChange(String propName, boolean oldV, boolean newV) {
    firePropChange(propName, new Boolean(oldV), new Boolean(newV));
  }

  ////////////////////////////////////////////////////////////////
  // property change handling

  /** By default just pass it up to enclosing groups.  Subclasses of
   *  FigNode may want to override this method. */
  public void propertyChange(PropertyChangeEvent pce) {
    if (_group != null) _group.propertyChange(pce);
  }

  public void preSave() { }
  public void postSave() { }
  public void postLoad() { }

  public void cleanUp() { }

  static final long serialVersionUID = 8658160363557344358L;

} /* end class Fig */
