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

// File: ModeModify.java
// Classes: ModeModify
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.Rectangle;

/** A Mode to process events from the Editor when the user is
 *  modifying a Fig. Right now users can drag one or more
 *  Fig's around the drawing area, or they can move a handle
 *  on a single Fig. The Fig is responsible for
 *  reacting to the movement of a handle, but so far they all resize
 *  themselves. In the future, some Fig's may have handles
 *  that set properties other than size (e.g., spline curvature).
 *  <A HREF="../features.html#editing_modes_modify">
 *  <TT>FEATURE: editing_modes_modify</TT></A>
 */

public class ModeModify extends Mode {
  /** The point relative to the original postition of the
   *  Fig where the dragging started. Keeping this point
   *  allows the user to "grip" any part of the Fig, rather
   *  than just dragging its position (or "pin") around. */
  protected Point _anchor;

  /** the mouse location for the most recent drag event */
  protected int _lastX = 1000, _lastY = 1000;

  /** the mouse location for the first mouse down event*/
  protected int _startX, _startY;

  /** has the mouse moved enough to indicate that the user really
   *  wants to modify somthing? */
  protected boolean _minDeltaAchieved;

  /** Minimum amoun that the user must move the mouse to indicate that he
   *  really wants to modify something. */
  public static final int MIN_DELTA = 4;

  /** the ID of the handle that the user is dragging */
  protected Handle _curHandle = new Handle(-1);

  public static final int NO_CONSTRAINT = 0;
  public static final int HORIZONTAL_CONSTRAINT = 1;
  public static final int VERTICAL_CONSTRAINT = 2;
  protected int _constraint = NO_CONSTRAINT;

  /** Construct a new ModeModify with the given parent, and set the
   *  Anchor point to a default location (the _anchor's proper position
   *  will be determioned on mouse down). */
  ModeModify(Editor par) {
    super(par);
    _anchor = new Point(0,0);
  }

  private static Point snapPt = new Point(0, 0);

  /** When the user drags the mouse two things can happen: (1) if the
   *  user is dragging the body of one or more Fig's then
   *  they are all moved around the drawing area, or (2) if the user
   *  started dragging on a handle of one Fig then the user
   *  can drag the handle around the drawing area and the
   *  Fig reacts to that.
   *  <A HREF="../features.html#drag_object">
   *  <TT>FEATURE: drag_object</TT></A>
   *  <A HREF="../features.html#drag_object_constrained">
   *  <TT>FEATURE: drag_object_constrained</TT></A>
   *  <A HREF="../features.html#drag_handle">
   *  <TT>FEATURE: drag_handle</TT></A>
   *  <A HREF="../features.html#drag_handle_constrained">
   *  <TT>FEATURE: drag_handle_constrained</TT></A>
   *  <A HREF="../features.html#locked_objects">
   *  <TT>FEATURE: locked_objects</TT></A>
   *  <A HREF="../bugs.html#arc_translate">
   *  <FONT COLOR=660000><B>BUG: arc_translate</B></FONT></A>
   */
  public void mouseDragged(MouseEvent me) {
    int x = me.getX(), y = me.getY();    
    int dx, dy, snapX, snapY;
    if (!checkMinDelta(x, y)) { me.consume(); return; }
    SelectionManager sm = getEditor().getSelectionManager();
    if (sm.getLocked()) {
      Globals.showStatus("Cannot Modify Locked Objects");
      me.consume();
      return;
    }
    synchronized (snapPt) {
      snapPt.move(x, y);
      getEditor().snap(snapPt);
      snapX = snapPt.x;
      snapY = snapPt.y;
    }
    dx = snapX - _lastX;
    dy = snapY - _lastY;
    if (me.isControlDown() && _constraint == NO_CONSTRAINT) {
      if (dx != 0) _constraint = HORIZONTAL_CONSTRAINT;
      if (dy != 0) _constraint = VERTICAL_CONSTRAINT;
    }
    if (_constraint == HORIZONTAL_CONSTRAINT)  dy = 0;
    if (_constraint == VERTICAL_CONSTRAINT)  dx = 0;
    if (dx == 0 && dy == 0) { me.consume(); return; }

    sm.startTrans();
    if (_curHandle.index == -1)
      sm.translate(dx, dy);
    else if (_curHandle.index >= 0)
      sm.dragHandle(snapX, snapY, _anchor.x, _anchor.y, _curHandle);
    //if _curHandle.index == -2 then do nothing

    sm.endTrans();

    _lastX = snapX; _lastY = snapY;
    me.consume();
  }

  /** When the user presses the mouse button on a Fig,
   *  this Mode starts preparing for future drag events by finding if
   *  a handle was clicked on. This event is passed from ModeSelect. */
  public void mousePressed(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    start();
    SelectionManager sm = getEditor().getSelectionManager();
    if (sm.size() == 0) { done(); }
    if (sm.getLocked()) {
      Globals.showStatus("Cannot Modify Locked Objects");
      me.consume();
      return;
    }
    /* needs-more-work: _anchor point sign convention is backwards */
    _anchor.x = sm.getBounds().x - x;
    _anchor.y = sm.getBounds().y - y;
    _curHandle.index = sm.hitHandle(new Rectangle(x-4, y-4, 8, 8));
    sm.endTrans();
    synchronized (snapPt) {
      snapPt.move(x, y);
      getEditor().snap(snapPt);
      _startX = _lastX = snapPt.x;
      _startY = _lastY = snapPt.y;
    }
    me.consume();
  }

  /** On mouse up the modification interaction is done. */
  public void mouseReleased(MouseEvent me) {
    done();
    me.consume();
  }

  public void start() {
    _minDeltaAchieved = false;
    super.start();
  }

  /** Reply true if the user had moved the mouse enough to signify
   * that (s)he really wants to make a change.
   *  <A HREF="../features.html#minimum_modify_delta">
   *  <TT>FEATURE: minimum_modify_delta</TT></A>
   */
  public boolean checkMinDelta(int x, int y) {
    if (x > _startX + MIN_DELTA ||
        x < _startX - MIN_DELTA ||
        y > _startY + MIN_DELTA ||
        y < _startY - MIN_DELTA)
      _minDeltaAchieved = true;
    return _minDeltaAchieved;
  }

} /* end class ModeModify */

