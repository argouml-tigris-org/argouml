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

// File: Selection.java
// Classes: Selection
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;
import uci.util.*;

/** This class represents the "selection" object that is used when you
 *  select one or more Figures in the drawing window. Selection's
 *  (should) handle the display of handles or whatever graphics
 *  indicate that something is selected, and they process events to
 *  manipulate the selected Fig. Needs-More-Work: in a
 *  earlier version of this design Fig's did all of that
 *  work themselves which led to much code duplication. Unfortuanately
 *  some of that code is still left over. Specificially,
 *  Fig's still have methods to draw their own handles.<p>
 *  <A HREF="../features.html#selections">
 *  <TT>FEATURE: selections</TT></A>
 *
 * @see Fig
 * @see Fig#drawSelected */

public abstract class Selection extends EventHandler
implements java.io.Serializable {

  ////////////////////////////////////////////////////////////////
  // constants

  public static final int HAND_SIZE = 6;

  /** The margin between the contents bbox and the frame */
  public static final int BORDER_WIDTH = 4;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** _content refers to the DiagramElement that is selected.
   * Needs-more-work:  this could be a blank-final. */ 
  protected Fig _content; // could be blank final? no...

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new selection. Empty, subclases can override */
  public Selection(Fig f) {
    if (null == f) throw new java.lang.NullPointerException();
    _content = f;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Reply the Fig that was selected */
  public Fig getContent() { return _content; }
  public void setcontent(Fig f) {  _content = f; }

  public boolean getLocked() { return getContent().getLocked(); }

  /** Reply true if this selection contains the given Fig */
  public boolean contains(Fig f) { return f == _content; }


  ////////////////////////////////////////////////////////////////
  // display methods

  /** Paint something that indicates a selection to the user */
  public void print(Graphics g) { paint(g); }
  public abstract void paint(Graphics g);

  /** Tell the content to start a transaction that causes damage */
  public void startTrans() { getContent().startTrans(); }

  /** Tell the content to end a transaction that causes damage */
  public void endTrans() { getContent().endTrans(); }

  /** Reply the position of the Selection. That is defined to be the
   *  upper left corner of my bounding box. */
  public Point getLocation() {
    return _content.getLocation();
  }

  /** This selection object needs to be redrawn, register its damaged
   * area within the given Editor */
  public void damagedIn(Editor ed) { _content.damagedIn(ed); }

  /** reply true if the given point is inside this selection */
  public boolean contains(int x, int y) {
    return _content.contains(x, y) || hitHandle(x, y, 0, 0) != -1;
  }
  public boolean hit(Rectangle r) {
    return _content.hit(r) || hitHandle(r) != -1;
  }

  public final boolean contains(Point pnt) { return contains(pnt.x, pnt.y); }

  /** Find which handle the user clicked on, or return -1 if none. */
  public abstract int hitHandle(Rectangle r);
  public final int hitHandle(int x, int y, int w, int h) {
    return hitHandle(new Rectangle(x, y, w, h));
  }

    /** Tell the selected Fig to move to front or back, etc. */
  public void reorder(int func, Layer lay) { lay.reorder(_content, func); }

  /** Do nothing because alignment only makes sense for multiple
   * selections */
  public void align(int direction) { /* do nothing */ }

  public void align(Rectangle r, int direction, Editor ed) {
    _content.align(r, direction, ed);
  }

  /** When the selection is told to move, move the selected
   * Fig */
  public void translate(int dx,int dy) { _content.translate(dx, dy); }

  /** The bounding box of the selection is the bbox of the contained
   * Fig. */
  public Rectangle getBounds() {
    return new Rectangle(_content.getX() - HAND_SIZE/2,
			 _content.getY() - HAND_SIZE/2,
			 _content.getWidth() + HAND_SIZE,
			 _content.getHeight() + HAND_SIZE);
  }

  public void stuffBounds(Rectangle r) {
    r.reshape(_content.getX() - HAND_SIZE/2,
	      _content.getY() - HAND_SIZE/2,
	      _content.getWidth() + HAND_SIZE,
	      _content.getHeight() + HAND_SIZE);
  }

  /** If the selection is being deleted, the selected object is
   * deleted also. This is different from just deselecting the
   * selected Fig, to do that use one of the deselect
   * operations in Editor
   * @see Editor#deselect
   */
  public void removeFrom(Editor ed) { _content.removeFrom(ed); }

  /** If the selection is being disposed, the selected object is
   * disposed also. This is different from just deselecting the
   * selected Fig, to do that use one of the deselect
   * operations in Editor
   * @see Editor#deselect
   */
  public void dispose(Editor ed) { _content.dispose(ed); }

  /** Move one of the handles of a selected Fig. */
  public abstract void dragHandle(int mx, int my, int an_x,int an_y, Handle h);

  /** Reply the bounding box of the selected Fig's, does not
   *  include space used by handles. */
  public Rectangle getContentBounds() { return _content.getBounds(); }

  ////////////////////////////////////////////////////////////////
  // event handlers

    /** Pass any events along to the selected Fig.
   * Subclasses of Selection may reimplement this to add
   * functionality.
   */
  public boolean keyDown(Event e,int key) { return _content.keyDown(e, key); }

  public boolean mouseMove(Event e,int x,int y) {
    return _content.mouseMove(e, x, y);
  }

  public boolean mouseDrag(Event e,int x,int y) {
    return _content.mouseDrag(e, x, y);
  }

  public boolean mouseDown(Event e,int x,int y) {
    return _content.mouseDown(e, x, y);
  }

  public boolean mouseUp(Event e,int x,int y) {
    return _content.mouseUp(e, x, y);
  }

} /* end class Selection */
