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



// File: ModeBroom.java
// Classes: ModeBroom
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ModeBroom extends Mode {

  ////////////////////////////////////////////////////////////////
  // constants
  public final int UNDEFINED = 0;
  public final int UPWARD = 1;
  public final int DOWNWARD = 2;
  public final int RIGHTWARD = 3;
  public final int LEFTWARD = 4;
  public final int BROOM_WIDTH = 200;
  public final int SMALL_BROOM_WIDTH = 30;
  public final int FUDGE = 10;
  public final int MAX_TOUCHED = 1000;

  public final int EVEN_SPACE = 0;
  public final int PACK = 1;
  public final int SPREAD = 2;
  public final int ORIG = 3;

  public final Font HINT_FONT = new Font("Dialog", Font.PLAIN, 9);

  ////////////////////////////////////////////////////////////////
  // instance variables

  private Point	_start  = new Point(0, 0);
  private Vector _LastTouched = new Vector();

  private int x1, y1, x2, y2;
  private int _lastX1, _lastY1, _lastX2, _lastY2;
  private int _lastMX, _lastMY;

  private int _dir = UNDEFINED;
  private boolean _magnetic = false;
  private boolean _movable = true;
  private boolean _draw = false;

  private Fig _touched[] = new Fig[MAX_TOUCHED];
  private int _origX[] = new int[MAX_TOUCHED];
  private int _origY[] = new int[MAX_TOUCHED];
  private int _offX[] = new int[MAX_TOUCHED];
  private int _offY[] = new int[MAX_TOUCHED];
  private int _nTouched = 0;

  private int _broomMargin = 0;
  private int _distributeMode = 0;

  private Rectangle _addRect = new Rectangle();
  private Rectangle _selectRect = new Rectangle();
  private Rectangle _bigDamageRect = new Rectangle(0, 0, 400, 400);
  private Rectangle _origBBox = null;

  private String _hint = null;

  ////////////////////////////////////////////////////////////////
  // constructors and related methods

  /** Construct a new ModeBroom with the given parent. */
  public ModeBroom(Editor par) {
    super(par);
  }

  /** Construct a new ModeBroom instance. Its parent must be set
   *  before this instance can be used.  */
  public ModeBroom() { }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** Handle mouse down events by preparing for a drag. If the mouse
   *  down event happens on a handle or an already selected object, and
   *  the shift key is not down, then go to ModeModify. If the mouse
   *  down event happens on a port, to to ModeCreateEdge.   */
  public void mousePressed(MouseEvent me) {
    if (me.isConsumed()) return;
    _touched = new Fig[MAX_TOUCHED];
    _origX = new int[MAX_TOUCHED];
    _origY = new int[MAX_TOUCHED];
    _offX = new int[MAX_TOUCHED];
    _offY = new int[MAX_TOUCHED];
    _nTouched = 0;
    _dir = UNDEFINED;
    _magnetic = false;
    _draw = true;
    x1 = x2 = _start.x = me.getX();
    y1 = y2 = _start.y = me.getY();
    _lastX1 = x1;  _lastY1 = y1;
    _lastX2 = x2;  _lastY2 = y2;
    _selectRect.setBounds(x1 - 14, y1 - 14, x2 - x1 + 28, y2 - y1 + 28);
    _editor.damaged(_selectRect);
    _editor.getSelectionManager().deselectAll();
    me.consume();
    _hint = null;
    start();
  }

  /** On mouse dragging, stretch the selection rectangle. */
  public void mouseDragged(MouseEvent me) {
    if (me.isConsumed()) return;
    me.consume();
    Enumeration figs;
    _lastMX = me.getX();
    _lastMY = me.getY();
    Point snapPt = new Point(me.getX(), me.getY());
    _editor.snap(snapPt);
    int x = snapPt.x;
    int y = snapPt.y;
    int i;
    _selectRect.setBounds(x1 - 4, y1 - 4, x2 - x1 + 8, y2 - y1 + 8);
    _bigDamageRect.setLocation(x1 - 200, y1 - 200);
    _editor.damaged(_bigDamageRect);
    _editor.damaged(_selectRect);

    if (_dir == UNDEFINED) {
      if (me.isShiftDown()) _broomMargin = SMALL_BROOM_WIDTH;
      else _broomMargin = BROOM_WIDTH;

      int dx = me.getX() - _start.x;
      int dy = me.getY() - _start.y;
      if (Math.abs(dx) < FUDGE && Math.abs(dy) < FUDGE) return;
      if (Math.abs(dx) > Math.abs(dy)) {
	_dir = (dx > 0) ? RIGHTWARD : LEFTWARD;
	x1 = x2 = x;
	y1 = y - _broomMargin /2;
	y2 = y + _broomMargin /2;
	if (me.isShiftDown()) {
	  y1 = y - _broomMargin /2;
	  y2 = y + _broomMargin /2;
	}
      }
      else {
	_dir = (dy > 0) ? DOWNWARD : UPWARD;
	y1 = y2 = y;
	x1 = x - _broomMargin /2;
	x2 = x + _broomMargin /2;
	if (me.isShiftDown()) {
	  x1 = x - _broomMargin /2;
	  x2 = x + _broomMargin /2;
	}
      }
    }


    if (!_magnetic) addNewItems();
    _lastX1 = x1;     _lastY1 = y1;
    _lastX2 = x2;     _lastY2 = y2;

    switch (_dir) {
    case UPWARD:
      if (_movable) {
	y1 = y2 = Math.min(y, _start.y);
	if (_magnetic) y1 = y2 = y;
      }
      x1 = Math.min(x1, _lastMX - _broomMargin / 2);
      x2 = Math.max(x2, _lastMX + _broomMargin / 2);
      break;
    case DOWNWARD:
      if (_movable) {
	y1 = y2 = Math.max(y, _start.y);
	if (_magnetic) y1 = y2 = y;
      }
      x1 = Math.min(x1, _lastMX - _broomMargin / 2);
      x2 = Math.max(x2, _lastMX + _broomMargin / 2);
      break;
    case RIGHTWARD:
      if (_movable) {
	x1 = x2 = Math.max(x, _start.x);
	if (_magnetic) x1 = x2 = x;
      }
      y1 = Math.min(y1, _lastMY - _broomMargin / 2);
      y2 = Math.max(y2, _lastMY + _broomMargin / 2);
      break;
    case LEFTWARD:
      if (_movable) {
	x1 = x2 = Math.min(x, _start.x);
	if (_magnetic) x1 = x2 = x;
      }
      y1 = Math.min(y1, _lastMY - _broomMargin / 2);
      y2 = Math.max(y2, _lastMY + _broomMargin / 2);
      break;
    }

    if (_movable) {
    Vector nonMovingEdges = new Vector();
    Vector movingEdges = new Vector();
    for (i = 0; i < _nTouched; i++) {
      Fig f = _touched[i];
      int newX = x;
      int newY = y;
      int dx = 0, dy = 0;
      switch (_dir) {
      case UPWARD:
	if (!_magnetic) newY = Math.min(y, _origY[i] + _offY[i]);
	dy = newY - f.getY() - _offY[i];
	break;
      case DOWNWARD:
	if (!_magnetic) newY = Math.max(y, _origY[i] + _offY[i]);
	dy = newY - f.getY() - _offY[i];
	break;
      case RIGHTWARD:
	if (!_magnetic) newX = Math.max(x, _origX[i] + _offX[i]);
	dx = newX - f.getX() - _offX[i];
	break;
      case LEFTWARD:
	if (!_magnetic) newX = Math.min(x, _origX[i] + _offX[i]);
	dx = newX - f.getX() - _offX[i];
	break;
      }
      f.startTrans();
      if (!(f instanceof FigNode)) {
	f.translate(dx, dy);
      }
      else {
	FigNode fn = (FigNode) f;
	fn.superTranslate(dx, dy);
	Vector figEdges = fn.getFigEdges();
	int feSize = figEdges.size();
	for (int j = 0; j < feSize; j++) {
	  FigEdge fe = (FigEdge) figEdges.elementAt(j);
	  if (nonMovingEdges.contains(fe) && !movingEdges.contains(fe)) {
	    movingEdges.addElement(fe);
	    fe.translateEdge(dx, dy);
	  }
	  else nonMovingEdges.addElement(fe);
	}
      }
      f.endTrans();
    }
    for (i = 0; i < _nTouched; i++) {
      Fig f = _touched[i];
      if (f instanceof FigNode) ((FigNode) f).updateEdges();
    }
    }
    _selectRect.setBounds(x1 - 4, y1 - 4, x2 - x1 + 8, y2 - y1 + 8);
    _editor.damaged(_selectRect);
    _hint = null;
    touching();
  }

  /** On mouse up, select or toggle the selection of items under the
   *  mouse or in the selection rectangle. */
  public void mouseReleased(MouseEvent me) {
    if (me.isConsumed()) return;
    _selectRect.setBounds(x1 - 1, y1 - 1, x2 - x1 + 2, y2 - y1 + 20);
    _bigDamageRect.setLocation(x1 - 200, y1 - 200);
    _editor.damaged(_bigDamageRect);
    _editor.damaged(_selectRect);
    _editor.getSelectionManager().select(_LastTouched);
    _draw = false;
    done();
    me.consume();
    _hint = null;
  }

  public void addNewItems() {
    if (_nTouched >= MAX_TOUCHED) return;
    int i;
    _addRect.setBounds(_lastX1, _lastY1, _lastX2 - _lastX1,
		       _lastY2 - _lastY1);
    _addRect.add(_selectRect);
    Enumeration figs = _editor.figs();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if (_addRect.intersects(f.getBounds())) {
	for (i = 0; i < _nTouched; i++) {
	  Fig ft = _touched[i];
	  if (ft == f) break;
	}
	if (i < _nTouched) continue;
	_touched[_nTouched] = f;
	_origX[_nTouched] = f.getX();
	_origY[_nTouched] = f.getY();
	_offX[_nTouched] = (_dir == LEFTWARD) ? f.getWidth() : 0;
	_offY[_nTouched] = (_dir == UPWARD) ? f.getHeight() : 0;
	_nTouched++;
	_origBBox = null;
      }
      // use different points depending on _dir
    }
  }

  public void keyPressed(KeyEvent ke) {
    super.keyPressed(ke);
    if (ke.isConsumed()) return;
    if (KeyEvent.VK_ENTER == ke.getKeyCode() ||
	KeyEvent.VK_TAB == ke.getKeyCode()) {
      _magnetic = !_magnetic;
    }
    else if (KeyEvent.VK_SPACE == ke.getKeyCode()) {
      doDistibute(false, ke.isShiftDown());
      ke.consume();
    }
    else {
      //System.out.println("key code is " + ke.getKeyCode());
      return;
    }
    _bigDamageRect.setLocation(x1 - 200, y1 - 200);
    _editor.damaged(_bigDamageRect);
    _editor.damaged(_selectRect);
  }

  ////////////////////////////////////////////////////////////////
  // actions

  public void doDistibute(boolean alignToGrid, boolean doCentering) {
    _movable = false;
    Vector figs = _LastTouched;
    if (figs == null) figs = touching();
    int request = 0;
    int size = figs.size();

    if (_distributeMode == EVEN_SPACE || _distributeMode == SPREAD) {
      request = CmdDistribute.V_SPACING;
      if (_dir == UPWARD || _dir == DOWNWARD)
	request = CmdDistribute.H_SPACING;
    }
    else if (_distributeMode == PACK) {
      request = CmdDistribute.V_PACK;
      if (_dir == UPWARD || _dir == DOWNWARD)
	request = CmdDistribute.H_PACK;
    }

//     if (_distributeMode == EVEN_SPACE && _origBBox == null) {
//       for (int i = 0; i < size; i++) {
// 	Fig f = (Fig) figs.elementAt(i);
// 	_origLocation[i] = f.getLocation();
//       }
//     }
    if (_distributeMode == ORIG) {
      for (int i = 0; i < size; i++) {
	Fig f = (Fig) figs.elementAt(i);
	if (_dir == UPWARD || _dir == DOWNWARD)
	  f.setLocation(_origX[i], f.getY());
	else
	  f.setLocation(f.getX(), _origY[i]);
      }
    }
    else {
      CmdDistribute d = new CmdDistribute(request);
      d.setArg("figs", figs);
      if (_distributeMode == SPREAD) d.setArg("bbox", _selectRect);
      else if (_distributeMode == EVEN_SPACE && _origBBox != null)
	d.setArg("bbox", _origBBox);
      d.doIt();

      if (doCentering) {
	int centerRequest = CmdAlign.ALIGN_H_CENTERS;
	if (_dir == UPWARD || _dir == DOWNWARD)
	  centerRequest = CmdAlign.ALIGN_V_CENTERS;
	CmdAlign a = new CmdAlign(centerRequest);
	a.setArg("figs", figs);
	a.doIt();
      }

      if (alignToGrid) {
	CmdAlign a = new CmdAlign(CmdAlign.ALIGN_TO_GRID);
	a.setArg("figs", figs);
	a.doIt();
      }
      if (_distributeMode == EVEN_SPACE && _origBBox == null)
	_origBBox = d.getLastBBox();
    }

    if (_distributeMode == EVEN_SPACE) _hint =  "Space evenly";
    else if (_distributeMode == PACK) _hint =   "Pack tightly";
    else if (_distributeMode == SPREAD) _hint = "Spread out";
    else if (_distributeMode == ORIG) _hint =   "Original";
    else _hint = "(internal prog error)";
    if (doCentering) _hint += " + center";
    if (alignToGrid) _hint += " + snap";
    _distributeMode = (_distributeMode + 1) % 4;
  }

  public Vector touching() {
    Vector figs = new Vector(_nTouched);
    for (int i = 0; i < _nTouched; i++)
      if (_touched[i].getBounds().intersects(_selectRect)) //needs-more-work
	if (!(_touched[i] instanceof FigEdge))
	  figs.addElement(_touched[i]);
    _LastTouched = figs;
    return figs;
  }

  ////////////////////////////////////////////////////////////////
  // user feedback methods

  /** Reply a string of instructions that should be shown in the
   *  statusbar when this mode starts. */
  public String instructions() {
    return "Push objects around. Retrun toggles pulling. Space key distributes.";
  }

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paint this mode by painting the selection rectangle if appropriate. */
  public void paint(Graphics g) {
    if (!_draw) return;
    Color selectRectColor = (Color) Globals.getPrefs().getRubberbandColor();
    if (_magnetic) g.setColor(Color.red);
    else g.setColor(selectRectColor);

    if (_hint != null) g.setFont(HINT_FONT);
    int bm = _broomMargin / 2;
    switch (_dir) {
    case UNDEFINED:
      g.fillRect(x1 - 10, (y1 + y2)/2 - 2, 20, 4);
      g.fillRect((x1 + x2)/2 -2, y1-10, 4, 20);
      break;
    case UPWARD:
      g.fillRect(x1, y1, x2-x1, y2-y1+4);
      g.drawLine(_lastMX - bm, y2+4, _lastMX - bm, y2+8);
      g.drawLine(_lastMX + bm-1, y2+4, _lastMX + bm-1, y2+8);
      if (_movable) g.fillRect((x1 + x2)/2 -2, y1, 4, 14);
      if (_hint != null) g.drawString(_hint, (x1 + x2)/2 + 5, y1 + 15);
      break;
    case DOWNWARD:
      g.fillRect(x1, y1-4, x2-x1, y2-y1+4);
      if (_movable) g.fillRect((x1 + x2)/2 -2, y1-14, 4, 14);
      g.drawLine(_lastMX - bm, y1-4, _lastMX - bm, y1-8);
      g.drawLine(_lastMX + bm-1, y1-4, _lastMX + bm-1, y1-8);
      if (_hint != null) g.drawString(_hint, (x1 + x2)/2 + 5, y1 - 8);
      break;
    case RIGHTWARD:
      g.fillRect(x1-4, y1, x2-x1+4, y2-y1);
      g.drawLine(x1-4, _lastMY - bm, x1-8, _lastMY - bm);
      g.drawLine(x1-4, _lastMY + bm-1, x1-8, _lastMY + bm-1);
      if (_movable) g.fillRect(x1 - 14, (y1 + y2)/2 - 2, 14, 4);
      if (_hint != null) g.drawString(_hint, x1 - 70, (y1 + y2)/2 - 10);
      break;
    case LEFTWARD:
      g.fillRect(x1, y1, x2-x1+4, y2-y1);
      g.drawLine(x2+4, _lastMY - bm, x2+8, _lastMY - bm);
      g.drawLine(x2+4, _lastMY + bm-1, x2+8, _lastMY + bm-1);
      if (_movable) g.fillRect(x1, (y1 + y2)/2 - 2, 14, 4);
      if (_hint != null) g.drawString(_hint, x2 + 5, (y1 + y2)/2 - 10);
      break;
    }

  }

  static final long serialVersionUID = 8477764856706223037L;
} /* end class ModeBroom */

