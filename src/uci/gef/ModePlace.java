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



// File: ModePlace.java
// Classes: ModePlace
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import uci.graph.*;

/** Mode to place new a FigNode on a node in a diagram.
 *  Normally invoked via CmdCreateNode.
 *
 * @see ActionCreateNode
 * @see FigNode
 */

public class ModePlace extends Mode {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The (new) node being placed. It might be an existing node that
   *  is adding a new FigNode. */
  protected Object _node;

  /** The (new) FigNode being placed. It might be an existing
   *  FigNode on an existing node being place in another diagram. */
  protected FigNode _pers;

  protected GraphFactory _factory;

  ////////////////////////////////////////////////////////////////
  // constructor

  /** Construct a new instance of ModePlace and store the given node. */
  public ModePlace(GraphFactory gf) {
    _factory = gf;
    _node = null;
    _pers = null;
  }

  ////////////////////////////////////////////////////////////////
  // user feedback

  /** A string to be shown in the status bar of the Editor when this
   * mode is on top of the ModeManager. */
  public String instructions() {
    if (_node != null) return "Click to place " + _node.toString();
    //if (_factory != null) return "Click to place " + _factory.toString();
    return "";
  }

  /** By default all creation modes use CROSSHAIR_CURSOR. */
  public Cursor getInitialCursor() {
    return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** Move the perpective along with the mouse. */
  public void mousePressed(MouseEvent me) {
    if (me.isConsumed()) return;
    _node = _factory.makeNode();
    start();
    _editor = Globals.curEditor();
    GraphModel gm = _editor.getGraphModel();
    GraphNodeRenderer renderer = _editor.getGraphNodeRenderer();
    Layer lay = _editor.getLayerManager().getActiveLayer();
    _pers = renderer.getFigNodeFor(gm, lay, _node);
    mouseMoved(me); // move _pers into position
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
    if (me.isConsumed()) return;
    int x = me.getX(), y = me.getY();
    if (_pers == null) { me.consume(); return; }
    _editor.damaged(_pers);
    Point snapPt = new Point(x, y);
    _editor.snap(snapPt);
    _pers.setLocation(snapPt.x, snapPt.y);
    _editor.damaged(_pers); /* needed? */
    me.consume();
  }

  /** Eat this event and do nothing */
  public void mouseEntered(MouseEvent me) {
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
    if (me.isConsumed()) return;
    GraphModel gm = _editor.getGraphModel();
    if (!(gm instanceof MutableGraphModel)) return;

    MutableGraphModel mgm = (MutableGraphModel) gm;
    if (mgm.canAddNode(_node)) {
      _editor.add(_pers);
      mgm.addNode(_node);
      Fig encloser = null;
      Rectangle bbox = _pers.getBounds();
      Layer lay = _editor.getLayerManager().getActiveLayer();
      Vector otherFigs = lay.getContents();
      Enumeration others = otherFigs.elements();
      while (others.hasMoreElements()) {
        Fig otherFig = (Fig) others.nextElement();
        if (!(otherFig instanceof FigNode)) continue;
        if (otherFig.equals(_pers)) continue;
        Rectangle trap = otherFig.getTrapRect();
        if (trap != null && (trap.contains(bbox.x, bbox.y) &&
             trap.contains(bbox.x + bbox.width, bbox.y + bbox.height)))
          encloser = otherFig;
      }
      _pers.setEnclosingFig(encloser);
      if (_node instanceof GraphNodeHooks)
	((GraphNodeHooks)_node).postPlacement(_editor);
      _editor.getSelectionManager().select(_pers);
    }
    done();
    me.consume();
  }

  public void done() {
    super.done();
    _pers = null;
    _node = null;
  }

  /** Paint the FigNode being dragged around. */
  public void paint(Graphics g) { if (_pers != null) _pers.paint(g); }

  static final long serialVersionUID = -6165862797493969514L;
} /* end class ModePlace */
