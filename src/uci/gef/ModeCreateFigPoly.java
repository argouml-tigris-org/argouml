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

// File: ModeCreateFigPoly.java
// Classes: ModeCreateFigPoly
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.MouseEvent;

/** A Mode to interpert user input while creating a FigPoly. All of
 *  the actual event handling is inherited from ModeCreate. This class
 *  just implements the differences needed to make it specific to
 *  Polys.
 *  <A HREF="../features.html#basic_shapes_polygon">
 *  <TT>FEATURE: basic_shapes_polygon</TT></A>
 */

public class ModeCreateFigPoly extends ModeCreate {
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected int _npoints = 0;
  protected int _lastX, _lastY, _startX, _startY;
  protected Handle _handle = new Handle(-1);

  ////////////////////////////////////////////////////////////////
  // Mode API
  public String instructions() {
    return "Click to add a point; Double-click to finish";
  }

  ////////////////////////////////////////////////////////////////
  // ModeCreate API

  /** Create a new FigRect instance based on the given mouse down
   * event and the state of the parent Editor. */
  public Fig createNewItem(MouseEvent me, int snapX, int snapY) {
    FigPoly p = new FigPoly(snapX, snapY, _editor.graphAttrs());
    p.addPoint(snapX, snapY); // add the first point twice
    _startX = _lastX = snapX; _startY = _lastY = snapY;
    _npoints = 2;
    return p;
  }

  ////////////////////////////////////////////////////////////////
  // Event handlers

  public void mousePressed(MouseEvent me) {
    int x = me.getX(), y = me.getY();    
    if (_npoints == 0) { super.mousePressed(me); }
    if (!nearLast(x, y)) {
      _editor.damaged(_newItem);
      Point snapPt = new Point(x, y);
      _editor.snap(snapPt);
      ((FigPoly)_newItem).addPoint(snapPt.x, snapPt.y);
      _npoints++;
      _editor.damaged(_newItem);
    }
    me.consume();
  }

  public void mouseReleased(MouseEvent me) {
    int x = me.getX(), y = me.getY();    
    if (_npoints > 2 && nearLast(x, y)) {
      FigPoly p = (FigPoly) _newItem;
      _editor.damaged(_newItem);
      _handle.index = p.npoints() - 1;
      p.moveVertex(_handle, _startX, _startY, true);
      _npoints = 0;
      _editor.damaged(p);
      _editor.add(p);
      _editor.getSelectionManager().select(p);
      _newItem = null;
      done();
      me.consume();
      return;
    }
    _lastX = x; _lastY = y;
    me.consume();
  }

  public void mouseMoved(MouseEvent me) {
    mouseDragged(me);
  }

  public void mouseDragged(MouseEvent me) {
    int x = me.getX(), y = me.getY();    
    if (_npoints == 0) { me.consume(); return; }
    FigPoly p = (FigPoly)_newItem;
    _editor.damaged(_newItem); // startTrans?
    Point snapPt = new Point(x, y);
    _editor.snap(snapPt);
    _handle.index = p.npoints() - 1;
    p.moveVertex(_handle, snapPt.x, snapPt.y, true);
    _editor.damaged(_newItem); // endTrans?
    me.consume();
  }

  /** Internal function to see if the user clicked twice on the same spot. */
  protected boolean nearLast(int x, int y) {
    return x > _lastX - Editor.GRIP_SIZE &&
      x < _lastX + Editor.GRIP_SIZE &&
      y > _lastY - Editor.GRIP_SIZE &&
      y < _lastY + Editor.GRIP_SIZE;
  }

} /* end class ModeCreateFigPoly */

