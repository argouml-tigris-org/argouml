// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
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




// File: ModeModify.java
// Classes: ModeModify
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.event.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Cursor;

/** A Mode to process events from the Editor when the user is
 *  modifying a Fig.  Right now users can drag one or more
 *  Figs around the drawing area, or they can move a handle
 *  on a single Fig.
 *
 * @see Fig
 * @see Seleciton
 */

public class ModeModify extends Mode {
  ////////////////////////////////////////////////////////////////
  // constants
  
  /** Minimum amoun that the user must move the mouse to indicate that he
   *  really wants to modify something. */
  public static final int MIN_DELTA = 4;

  /** When _constraint == NO_CONSTRAINT, user can move selections any way*/
  public static final int NO_CONSTRAINT = 0;

  /** When _constraint == HORIZONTAL_CONSTRAINT, the user can only
   *  move selections left and right. */
  public static final int HORIZONTAL_CONSTRAINT = 1;

  /** When _constraint == VERTICAL_CONSTRAINT, the user can only
   *  move selections up and down. */
  public static final int VERTICAL_CONSTRAINT = 2;

  ////////////////////////////////////////////////////////////////
  // instance variables
  
  /** The point relative to the original postition of the
   *  Fig where the dragging started.  Keeping this point
   *  allows the user to "grip" any part of the Fig, rather
   *  than just dragging its position (or "pin") around. */
  protected Point _anchor;

  /** the mouse location for the most recent drag event */
  protected int _lastX = 1000, _lastY = 1000;

  /** the mouse location for the first mouse down event*/
  protected int _startX, _startY;

  /** Has the mouse moved enough to indicate that the user really
   *  wants to modify somthing? */
  protected boolean _minDeltaAchieved;

  /** The index of the handle that the user is dragging */
  protected Handle _curHandle = new Handle(-1);

  /** Indicates the kind of movement constraint being
   *  applied. Possible values are NO_CONSTRAINT,
   *  HORIZONTAL_CONSTRAINT, or VERTICAL_CONSTRAINT. */
  protected int _constraint = NO_CONSTRAINT;

  /** Construct a new ModeModify with the given parent, and set the
   *  Anchor point to a default location (the _anchor's proper position
   *  will be determioned on mouse down). */
  public ModeModify(Editor par) {
    super(par);
    _anchor = new Point(0,0);
  }


  ////////////////////////////////////////////////////////////////
  // user feedback

  /** Reply a string of instructions that should be shown in the
   *  statusbar when this mode starts. */
  public String instructions() { return "Modify selected objects"; }

  ////////////////////////////////////////////////////////////////
  // event handlers
  
  private static Point snapPt = new Point(0, 0);

  /** When the user drags the mouse two things can happen: (1) if the
   *  user is dragging the body of one or more Figs then they are all
   *  moved around the drawing area, or (2) if the user started
   *  dragging on a handle of one Fig then the user can drag the
   *  handle around the drawing area and the Fig reacts to that.  */
  public void mouseDragged(MouseEvent me) {
    if (me.getModifiers() == InputEvent.BUTTON3_MASK) return;
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
      snapPt.setLocation(x, y);
      getEditor().snap(snapPt);
      snapX = snapPt.x;
      snapY = snapPt.y;
    }
    dx = snapX - _lastX;
    dy = snapY - _lastY;
    if (me.isControlDown() && _constraint == NO_CONSTRAINT) {
      if (dx != 0) {
	_constraint = HORIZONTAL_CONSTRAINT;
	Globals.showStatus("Moving objects horizontally");
      }
      if (dy != 0) {
	_constraint = VERTICAL_CONSTRAINT;
	Globals.showStatus("Moving objects vertically");
      }
    }
    if (_constraint == HORIZONTAL_CONSTRAINT)  {
      dy = 0;
      snapY = _anchor.y;
    }
    if (_constraint == VERTICAL_CONSTRAINT)  {
      dx = 0;
      snapX = _anchor.x;
    }
    if (dx == 0 && dy == 0) { me.consume(); return; }

    sm.startTrans();
    if (_curHandle.index == -1) {
      setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));      
      sm.translate(dx, dy);
    }
    else if (_curHandle.index >= 0) {
      setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
      sm.dragHandle(snapX, snapY, _anchor.x, _anchor.y, _curHandle);
    }
    //Note: if _curHandle.index == -2 then do nothing

    sm.endTrans();

    _editor.scrollToShow(snapX, snapY);
	
    _lastX = snapX; _lastY = snapY;
    me.consume();
  }

  /** When the user presses the mouse button on a Fig, this Mode
   *  starts preparing for future drag events by finding if a handle
   *  was clicked on.  This event is passed from ModeSelect. */
  public void mousePressed(MouseEvent me) {
    if (me.getModifiers() == InputEvent.BUTTON3_MASK) return;
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
    _anchor.x = x;
    _anchor.y = y;
    _curHandle.index = sm.hitHandle(new Rectangle(x-4, y-4, 8, 8));
    sm.endTrans();
    synchronized (snapPt) {
      snapPt.setLocation(x, y);
      getEditor().snap(snapPt);
      _startX = _lastX = snapPt.x;
      _startY = _lastY = snapPt.y;
    }
    me.consume();
  }

  /** On mouse up the modification interaction is done. */
  public void mouseReleased(MouseEvent me) {
    if (me.getModifiers() == InputEvent.BUTTON3_MASK) return;
    done();
    me.consume();
  }

  public void start() {
    _minDeltaAchieved = false;
    super.start();
  }

  public void done() {
    super.done();
    SelectionManager sm = getEditor().getSelectionManager();
    sm.cleanUp();
  }

  /** Reply true if the user had moved the mouse enough to signify
   * that (s)he really wants to make a change. */
  protected boolean checkMinDelta(int x, int y) {
    if (x > _startX + MIN_DELTA ||
        x < _startX - MIN_DELTA ||
        y > _startY + MIN_DELTA ||
        y < _startY - MIN_DELTA)
      _minDeltaAchieved = true;
    return _minDeltaAchieved;
  }

  static final long serialVersionUID = -5048296582544436022L;
} /* end class ModeModify */

