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



// File: ModeSelect.java
// Classes: ModeSelect
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/** This class implements a Mode that interperts user input as
 *  selecting one or more Figs. Clicking on a Fig will select
 *  it.  Shift-clicking will toggle whether it is selected. Dragging in
 *  open space will draw a selection rectangle.  Dragging on a Fig will
 *  switch to ModeModify.  Dragging from a port will switch to
 *  ModeCreateEdge.  ModeSelect paints itself by displaying its
 *  selection rectangle if any. <p>
 *
 *  Needs-More-Work: this mode has more responsibility than just making
 *  selections, it has become the "main mode" of the editor and it has
 *  taken resposibility for switching to other modes.  I shuold probably
 *  implement a "UIDialog" class that would have a state machine that
 *  describes the various transitions between UI modes. <p>
 *
 *  Needs-More-Work: There is currently a bug in shift clicking, you
 *  cannot unselect an individual item by shift-clicking on it. <p>
 *
 * @see ModeCreateEdge
 * @see ModeModify
 * @see Fig
 * @see Editor */

public class ModeSelect extends Mode {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** If the user drags a selection rectangle, this is the first corner. */
  private Point	_selectAnchor  = new Point(0, 0);

  /** This is the seclection rectangle. */
  private Rectangle _selectRect = new Rectangle(0,0,0,0);

  /** True when the selection rectangle should be paintn. */
  private boolean _showSelectRect = false;

  /** True when the user holds the shift key to toggle selections. */
  private boolean _toggleSelection = false;

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
   *  down event happens on a port, to to ModeCreateEdge.   */
  public void mousePressed(MouseEvent me) {
    if (me.isConsumed()) {
      //System.out.println("ModeSelect consumed");
      return;
    }
    if (me.getModifiers() == InputEvent.BUTTON3_MASK) { return; }
    if (me.isControlDown()) { gotoBroomMode(me); return; }
    if (me.isAltDown()) return;
    int x = me.getX(), y = me.getY();

    _selectAnchor = new Point(x, y);
    _selectRect.setBounds(x, y, 0, 0);
    _toggleSelection = me.isShiftDown();
    SelectionManager sm = _editor.getSelectionManager();
    Rectangle hitRect = new Rectangle(x - 4, y - 4, 8, 8);

    /* Check if multiple things are selected and user clicked one of them. */
    Fig underMouse = _editor.hit(_selectAnchor);
    if (underMouse == null && !sm.hit(hitRect)) return;

    Handle h = new Handle(-1);
    sm.hitHandle(new Rectangle(x-4, y-4, 8, 8), h);
    if (h.index >= 0) {
      gotoModifyMode(me);
      me.consume();
      return;
    }

    if (underMouse != null) {
      if (_toggleSelection) sm.toggle(underMouse);
      else if (!sm.containsFig(underMouse)) sm.select(underMouse);
    }
    if (sm.hit(hitRect)) {
      //System.out.println("gotoModifyMode");
      gotoModifyMode(me);
    }
    me.consume();
  }

  /** On mouse dragging, stretch the selection rectangle. */
  public void mouseDragged(MouseEvent me) {
    if (me.getModifiers() == InputEvent.BUTTON3_MASK) { return; }
    if (me.isConsumed()) return;
    if (me.isAltDown()) return;
    int x = me.getX(), y = me.getY();
    _showSelectRect = true;
    int bound_x = Math.min(_selectAnchor.x, x);
    int bound_y = Math.min(_selectAnchor.y, y);
    int bound_w = Math.max(_selectAnchor.x, x) - bound_x;
    int bound_h = Math.max(_selectAnchor.y, y) - bound_y;

    _editor.damaged(_selectRect);
    _selectRect.setBounds(bound_x, bound_y, bound_w, bound_h);
    _editor.damaged(_selectRect);
    _editor.scrollToShow(x, y);

    me.consume();
  }

  /** On mouse up, select or toggle the selection of items under the
   *  mouse or in the selection rectangle. */
  public void mouseReleased(MouseEvent me) {
    if (me.isConsumed()) return;
    int x = me.getX(), y = me.getY();
    _showSelectRect = false;
    Vector selectList = new Vector();

    Rectangle hitRect = new Rectangle(x - 4, y - 4, 8, 8);
    Enumeration figs = _editor.figs();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if ((!_toggleSelection && _selectRect.isEmpty() && f.hit(hitRect)) ||
	  (!_selectRect.isEmpty() && f.within(_selectRect))) {
	selectList.addElement(f);
      }
    }
    if (!_selectRect.isEmpty() && selectList.isEmpty()) {
       figs = _editor.figs();
       while (figs.hasMoreElements()) {
	 Fig f = (Fig) figs.nextElement();
	 if (f.intersects(_selectRect)) selectList.addElement(f);
       }
    }
    if (_toggleSelection) _editor.getSelectionManager().toggle(selectList);
    else _editor.getSelectionManager().select(selectList);

    _selectRect.grow(1,1); /* make sure it is not empty for redraw */
    _editor.damaged(_selectRect);
    if (me.getModifiers() == InputEvent.BUTTON3_MASK) return;
    me.consume();
  }


  ////////////////////////////////////////////////////////////////
  // user feedback methods

  /** Reply a string of instructions that should be shown in the
   *  statusbar when this mode starts. */
  public String instructions() { return "  "; }

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paint this mode by painting the selection rectangle if appropriate. */
  public void paint(Graphics g) {
    if (_showSelectRect) {
      Color selectRectColor = (Color) Globals.getPrefs().getRubberbandColor();
      g.setColor(selectRectColor);
      g.drawRect(_selectRect.x, _selectRect.y,
		 _selectRect.width, _selectRect.height);
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

  protected void gotoBroomMode(MouseEvent me) {
    Mode nextMode = new ModeBroom(_editor);
    _editor.mode(nextMode);
    nextMode.mousePressed(me);
  }

  static final long serialVersionUID = -8847591655429149135L;
} /* end class ModeSelect */

