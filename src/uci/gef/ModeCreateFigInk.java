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

// File: ModeCreateFigInk.java
// Classes: ModeCreateFigInk
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.event.MouseEvent;

/** A Mode to interpert user input while creating a FigInk.
 *  <A HREF="../features.html#basic_shapes_ink">
 *  <TT>FEATURE: basic_shapes_ink</TT></A>
 */

public class ModeCreateFigInk extends ModeCreate {

  ////////////////////////////////////////////////////////////////
  // instance variables

  public static final int MIN_DELTA = 4;

  protected int _lastX, _lastY;

  ////////////////////////////////////////////////////////////////
  // Mode API

  public String instructions() {
    return "Drag to draw a stream of ink";
  }

  ////////////////////////////////////////////////////////////////
  // ModeCreate API

  /** Create a new FigRect instance based on the given mouse down
   *  event and the state of the parent Editor. */
  public Fig createNewItem(MouseEvent me, int snapX, int snapY) {
    FigInk p = new FigInk(snapX, snapY, _editor.graphAttrs());
    _lastX = snapX; _lastY = snapY;
    return p;
  }

  ////////////////////////////////////////////////////////////////
  // Event handlers

  /** Mouse up means that the user is done. */
  public void mouseReleased(MouseEvent me) {
    _editor.damaged(_newItem);
    // do not call creationDrag()
    _editor.add(_newItem);
    _editor.getSelectionManager().select(_newItem);
    _newItem = null;
    done();
    me.consume();
  }

  /** Dragging adds points to the ink. */
  public void mouseDragged(MouseEvent me) {
    int x = me.getX(), y = me.getY();    
    FigInk ink = (FigInk)_newItem;
    if (!nearLast(x, y)) {
      _editor.damaged(_newItem); // startTrans?
      ink.addPoint(x, y);
      _editor.damaged(_newItem); // endTrans?
      _lastX = x; _lastY = y;
    }
    me.consume();
  }

  /** Internal function to test if the current point is so close to
   *  the last point that it should not be added to the ink. */
  protected boolean nearLast(int x, int y) {
    return x > _lastX - MIN_DELTA &&
      x < _lastX + MIN_DELTA &&
      y > _lastY - MIN_DELTA &&
      y < _lastY + MIN_DELTA;
  }

} /* end class ModeCreateFigInk */

