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

// File: ModePlace.java
// Classes: ModePlace
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;

/** Mode to place new a FigNode on a NetNode in a diagram.
 *  Normally invoked via ActionCreateNode.
 *
 * @see ActionCreateNode
 * @see FigNode
 */

public class ModePlace extends Mode {

  /** The (new) node being placed. It might be an existing node that
   *  is adding a new FigNode. */
  private NetNode _node;

  /** The (new) FigNode being placed. It might be an existing
   *  FigNode on an existing node being place in another diagram. */
  private FigNode _pers;

  /** Construct a new instance of ModePlace and store the given NetNode. */
  ModePlace(NetNode n) {
    _node = n;
    _pers = null;
  }

  /** A string to be shown in the status bar of the Editor when this
   * mode is on top of the ModeManager. */
  public String instructions() {
    if (_node == null) return "";
    else return "Click to place " + _node.toString();
  }

  /** Move the perpective along with the mouse. */
  public boolean mouseEnter(Event evt, int x, int y) {
    start();
    _pers = _node.presentationFor(_editor.getLayerManager().getActiveLayer());
    return true;
  }

  /** Move the perpective along with the mouse. */
  public boolean mouseExit(Event evt, int x, int y) {
    _editor.damaged(_pers);
    _pers = null;
    return true;
  }

  /** Move the perpective along with the mouse. */
  public boolean mouseMove(Event evt, int x, int y) {
    if (_pers == null) {System.out.println("null pers"); return true; }
    _editor.damaged(_pers);
    Point snapPt = new Point(x, y);
    _editor.snap(snapPt);
    _pers.setLocation(snapPt.x, snapPt.y);
    _editor.damaged(_pers); /* needed? */
    return true;
  }

  /** Eat this event and do nothing */
  public boolean mouseDown(Event evt, int x, int y) {
    return true;
  }

  /* Eactly the same as mouse move */
  public boolean mouseDrag(Event evt, int x, int y) {
    return mouseMove(evt, x, y);
  }

  /** Actually add the Perpective to the diagram.
   *  And give the NetNode a chance to do post processing.
   *
   * @see NetNode#postPlacement */
  public boolean mouseUp(Event evt, int x, int y) {
    _editor.add(_pers);
    _editor.select(_pers);
    _node.postPlacement(_editor);
    done();
    return true;
  }

  /** Paint the FigNode being dragged around. */
  public void paint(Graphics g) { if (_pers != null) _pers.paint(g); }

} /* end class ModePlace */
