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
import java.awt.event.MouseEvent;

import uci.graph.*;

/** Mode to place new a FigNode on a node in a diagram.
 *  Normally invoked via ActionCreateNode.
 *
 * @see ActionCreateNode
 * @see FigNode
 */

public class ModePlace extends Mode {

  /** The (new) node being placed. It might be an existing node that
   *  is adding a new FigNode. */
  private Object _node;

  /** The (new) FigNode being placed. It might be an existing
   *  FigNode on an existing node being place in another diagram. */
  private FigNode _pers;

  /** Construct a new instance of ModePlace and store the given node. */
  ModePlace(Object node) {
    _node = node;
    _pers = null;
  }

  /** A string to be shown in the status bar of the Editor when this
   * mode is on top of the ModeManager. */
  public String instructions() {
    if (_node == null) return "";
    else return "Click to place " + _node.toString();
  }

  /** Move the perpective along with the mouse. */
  public void mouseEntered(MouseEvent me) {
    start();
    _editor = Globals.curEditor();
    GraphModel gm = _editor.getGraphModel();
    GraphNodeRenderer renderer = _editor.getGraphNodeRenderer();
    Layer lay = _editor.getLayerManager().getActiveLayer();
    _pers = renderer.getFigNodeFor(gm, lay, _node);
    me.consume();
  }

  /** Move the perpective along with the mouse. */
  public void mouseExited(MouseEvent me) {
    _editor.damaged(_pers);
    _pers = null;
    me.consume();
  }

  /** Move the perpective along with the mouse. */
  public void mouseMoved(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    if (_pers == null) {System.out.println("null pers"); me.consume(); return; }
    _editor.damaged(_pers);
    Point snapPt = new Point(x, y);
    _editor.snap(snapPt);
    _pers.setLocation(snapPt.x, snapPt.y);
    _editor.damaged(_pers); /* needed? */
    me.consume();
  }

  /** Eat this event and do nothing */
  public void mousePressed(MouseEvent me) {
    me.consume();
  }

  /* Eactly the same as mouse move */
  public void mouseDragged(MouseEvent me) {
    mouseMoved(me);
  }

  /** Actually add the Perpective to the diagram.
   *  And give the node a chance to do post processing.
   *
   * @see uci.graph.GraphNodeHooks#postPlacement */
  public void mouseReleased(MouseEvent me) {
    _editor.add(_pers);
    _editor.getSelectionManager().select(_pers);
    if (_node instanceof GraphNodeHooks)
      ((GraphNodeHooks)_node).postPlacement(_editor);
    done();
    me.consume();
  }

  /** Paint the FigNode being dragged around. */
  public void paint(Graphics g) { if (_pers != null) _pers.paint(g); }

} /* end class ModePlace */
