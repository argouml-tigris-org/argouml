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




// File: ModeModify.java
// Classes: ModeModify
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.event.*;
import java.awt.*;
import java.util.*;

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

  protected Rectangle _highlightTrap = null;

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
    if (me.isConsumed()) return;
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
      if (legal(dx, dy, sm, me)) {
	sm.translate(dx, dy);
	_lastX = snapX; _lastY = snapY;
      }
    }
    else if (_curHandle.index >= 0) {
      // needs-more-work: check if legal
      setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
      sm.dragHandle(snapX, snapY, _anchor.x, _anchor.y, _curHandle);
      _lastX = snapX; _lastY = snapY;
    }
    //Note: if _curHandle.index == -2 then do nothing

    sm.endTrans();
    //_editor.scrollToShow(snapX, snapY);
    me.consume();
  }

  /** When the user presses the mouse button on a Fig, this Mode
   *  starts preparing for future drag events by finding if a handle
   *  was clicked on.  This event is passed from ModeSelect. */
  public void mousePressed(MouseEvent me) {
    if (me.isConsumed()) return;
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
    sm.hitHandle(new Rectangle(x-4, y-4, 8, 8), _curHandle);
    Globals.showStatus(_curHandle.instructions);
    sm.endTrans();
    synchronized (snapPt) {
      snapPt.setLocation(x, y);
      getEditor().snap(snapPt);
      _startX = _lastX = snapPt.x;
      _startY = _lastY = snapPt.y;
    }
    //System.out.println("consuming in ModeModify");
    //me.consume();
  }

  /** On mouse up the modification interaction is done. */
  public void mouseReleased(MouseEvent me) {
    if (me.isConsumed()) return;
    done();
    me.consume();
    SelectionManager sm = getEditor().getSelectionManager();
    Vector figs = sm.getFigs();
    Enumeration sels = figs.elements();
    while (sels.hasMoreElements()) {
      Fig selectedFig = (Fig) sels.nextElement();
      if (!(selectedFig instanceof FigNode)) continue;
      Rectangle bbox = selectedFig.getBounds();
//       Fig oldEncloser = selectedFig.getEnclosingFig();
//       if (oldEncloser != null && oldEncloser.contains(bbox))
// 	continue;
      Layer lay = selectedFig.getLayer();
      Vector otherFigs = lay.getContents();
      Enumeration others = otherFigs.elements();
      Fig encloser = null;
      while (others.hasMoreElements()) {
        Fig otherFig = (Fig) others.nextElement();
        if (!(otherFig instanceof FigNode)) continue;
	if (!(otherFig.getUseTrapRect())) continue;
        //if (figs.contains(otherFig)) continue;
        Rectangle trap = otherFig.getTrapRect();
	if (trap == null) continue;
        // now bbox is where the fig _will_ be
        if ((trap.contains(bbox.x, bbox.y) &&
             trap.contains(bbox.x + bbox.width, bbox.y + bbox.height))) {
           encloser = otherFig;
           }
      }
      selectedFig.setEnclosingFig(encloser);
    }
  }

  public void start() {
    _minDeltaAchieved = false;
    super.start();
  }

  public void done() {
    super.done();
    SelectionManager sm = getEditor().getSelectionManager();
    sm.cleanUp();
    if (_highlightTrap != null) {
      _editor.damaged(_highlightTrap);
      _highlightTrap = null;
    }
  }

  public void paint(Graphics g) {
    super.paint(g);
    if (_highlightTrap != null) {
      Color selectRectColor = (Color) Globals.getPrefs().getRubberbandColor();
      g.setColor(selectRectColor);
      g.drawRect(_highlightTrap.x-1, _highlightTrap.y-1,
		 _highlightTrap.width+1, _highlightTrap.height+1);
      g.drawRect(_highlightTrap.x-2, _highlightTrap.y-2,
		 _highlightTrap.width+3, _highlightTrap.height+3);
    }
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

  protected boolean legal(int dx, int dy, SelectionManager sm, MouseEvent me) {
    if (_highlightTrap != null) _editor.damaged(_highlightTrap);
    _highlightTrap = null;
    Vector figs = sm.getFigs();
    Enumeration sels = figs.elements();
    while (sels.hasMoreElements()) {
      Fig selectedFig = (Fig) sels.nextElement();
      boolean selectedUseTrap = selectedFig.getUseTrapRect();
      if (!(selectedFig instanceof FigNode)) continue;
      Rectangle bbox = selectedFig.getBounds();
      bbox.x += dx; bbox.y += dy;
      Layer lay = selectedFig.getLayer();
      Vector otherFigs = lay.getContents();
      Enumeration others = otherFigs.elements();
      while (others.hasMoreElements()) {
        Fig otherFig = (Fig) others.nextElement();
        if (!(otherFig instanceof FigNode)) continue;
	if (!selectedUseTrap && !otherFig.getUseTrapRect()) continue;
        if (figs.contains(otherFig)) continue;
        Rectangle trap = otherFig.getTrapRect();
	if (trap == null) continue;
        // now bbox is where the fig _will_ be
        int cornersHit = 0;
        if (trap.contains(bbox.x, bbox.y)) cornersHit++;
        if (trap.contains(bbox.x + bbox.width, bbox.y)) cornersHit++;
        if (trap.contains(bbox.x, bbox.y + bbox.height)) cornersHit++;
        if (trap.contains(bbox.x + bbox.width, bbox.y + bbox.height)) cornersHit++;
        if (!trap.intersects(bbox)) continue;
        if ((trap.contains(bbox.x, bbox.y) &&
	     trap.contains(bbox.x + bbox.width, bbox.y + bbox.height))) continue;
        if ((bbox.contains(trap.x, trap.y) &&
	     bbox.contains(trap.x + trap.width, trap.y + trap.height))) continue;
	_highlightTrap = trap;
	_editor.damaged(_highlightTrap);
        return false;
      }
    }
    return true;
  }

  static final long serialVersionUID = -5048296582544436022L;
} /* end class ModeModify */

