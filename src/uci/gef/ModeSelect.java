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

// File: ModeSelect.java
// Classes: ModeSelect
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;

/** This class implements a Mode that interperts user input as
 *  selecting one or more Fig's. Clicking on a
 *  Fig will select it. Shift-clicking will toggle whether
 *  it is selected. Dragging in open space will draw a selection
 *  rectangle. Dragging on a Fig will switch to
 *  ModeModify. Dragging from a port will switch to
 *  ModeCreateArc. ModeSelect paints itself by displaying its selection
 *  rectangle if any. <p>
 *
 *  Needs-More-Work: this mode has more responsibility than just making
 *  selections, it has become the "main mode" of the editor and it has
 *  taken resposibility for switching to other modes. I shuold probably
 *  implement a "UIDialog" class that would have a state machine that
 *  describes the various transitions between UI modes. <p>
 *
 *  Needs-More-Work: There is currently a bug in shift clicking, you
 *  cannot unselect an individual item by shift-clicking on it. <p>
 *  <A HREF="../features.html#editing_modes_select">
 *  <TT>FEATURE: editing_modes_select</TT></A>
 *
 * @see ModeCreateArc
 * @see ModeModify
 * @see Fig
 * @see Editor */

public class ModeSelect extends Mode {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** If the user drags a selection rectangle, this is the first corner. */
  private Point	selectAnchor  = new Point(0, 0);

  /** This is the seclection rectangle. */
  private Rectangle selectRect = new Rectangle(0,0,0,0);

  /** True when the selection rectangle should be paintn. */
  private boolean showSelectRect = false;

  /** True when the user holds the shift key to toggle selections. */
  private boolean toggleSelection = false;

  ////////////////////////////////////////////////////////////////
  // constructors and related methods

  /** Construct a new ModeSelect with the given parent. */
  public ModeSelect(Editor par) { super(par); }

  /** Construct a new ModeSelect instance. Its parent must be set
   *  before this instance can be used.  */
  public ModeSelect() { }

  /** Always false because I never want to get out of selection mode. */
  public boolean canExit() { return false; }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** Handle mouse down events by preparing for a drag. If the mouse
   *  down event happens on a handle or an already selected object, and
   *  the shift key is not down, then go to ModeModify. If the mouse
   *  down event happens on a port, to to ModeCreateArc.
   *  <A HREF="../features.html#select_by_click">
   *  <TT>FEATURE: select_by_click</TT></A>
   *  <A HREF="../features.html#select_none">
   *  <TT>FEATURE: select_none</TT></A>
   *  <A HREF="../bugs.html#shift_click">
   *  <FONT COLOR=660000><B>BUG: shift_click</B></FONT></A>
   */
  public void mousePressed(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    /* If multiple things are selected and the user clicked one of them. */
    selectAnchor = new Point(x, y);
    selectRect.reshape(x, y, 0, 0);
    toggleSelection = me.isShiftDown();
    SelectionManager sm = _editor.getSelectionManager();
    Rectangle hitRect = new Rectangle(x - 4, y - 4, 8, 8);

    Fig underMouse = _editor.hit(selectAnchor);
    if (underMouse == null && !sm.hit(hitRect)) return;

    if (underMouse != null) {
      if (toggleSelection)
	_editor.getSelectionManager().toggle(underMouse);
      else if (!_editor.getSelectionManager().containsFig(underMouse))
	_editor.getSelectionManager().select(underMouse);
    }

    if (sm.hit(hitRect)) {
      gotoModifyMode(me);
      // do not consume?
      return;
    }
    me.consume();
  }

  /** On mouse dragging, modify the selection rectangle.
   *  <A HREF="../features.html#select_by_area">
   *  <TT>FEATURE: select_by_area</TT></A>
   */
  public void mouseDragged(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    showSelectRect = true;
    int bound_x = Math.min(selectAnchor.x, x);
    int bound_y = Math.min(selectAnchor.y, y);
    int bound_w = Math.max(selectAnchor.x, x) - bound_x;
    int bound_h = Math.max(selectAnchor.y, y) - bound_y;

    _editor.damaged(selectRect);
    selectRect.reshape(bound_x, bound_y, bound_w, bound_h);
    _editor.damaged(selectRect);
    me.consume();
  }

  /** On mouse up, select or toggle the selection of items under the
   *  mouse or in the selection rectangle. */
  public void mouseReleased(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    showSelectRect = false;
    Vector selectList = new Vector();

    //Enumeration figs = _editor.getLayerManager().contents().elements();
    Enumeration figs = _editor.figs();
    Rectangle hitRect = new Rectangle(x - 4, y - 4, 8, 8);
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if ((selectRect.isEmpty() && f.hit(hitRect)) ||
	  (!selectRect.isEmpty() && f.intersects(selectRect))) {
	selectList.addElement(f);
      }
    }
    if (toggleSelection) _editor.getSelectionManager().toggle(selectList);
    else _editor.getSelectionManager().select(selectList);

    selectRect.grow(1,1); /* make sure it is not empty for redraw */
    _editor.damaged(selectRect);
    me.consume();
  }

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paint this mode by painting the selection rectangle if appropriate. */
  public void paint(Graphics g) {
    if (showSelectRect) {
      Color selectRectColor =
	(Color) Globals.getPrefs().rubberbandAttrs().get("LineColor");
      g.setColor(selectRectColor);
      g.drawRect(selectRect.x, selectRect.y,
		 selectRect.width, selectRect.height);
    }
  }

  ////////////////////////////////////////////////////////////////
  // methods related to transitions among modes

  /** Set the Editor's Mode to ModeModify.  Needs-More-Work: This
   *  should not be in ModeSelect, I wanted to move it to ModeModify,
   *  but it is too tighly integrated with ModeSelect. */
  protected void gotoModifyMode(MouseEvent me) {
    Mode nextMode = new ModeModify(_editor);
    _editor.mode(nextMode);
    nextMode.mousePressed(me);
  }

} /* end class ModeSelect */

