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



// File: ModeCreatePolyEdge.java
// Classes: ModeCreateEdge
// Original Author: agauthie@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.*;
import uci.util.*;
import uci.graph.*;

/** A Mode to interpret user input while creating an edge.  Basically
 *  mouse down starts creating an edge from a source port Fig, mouse
 *  motion paints a rubberband line, mouse up finds the destination port
 *  and finishes creating the edge and makes an FigEdge and sends
 *  it to the back of the Layer.
 *
 *  The argument "edgeClass" determines the type if edge to suggest
 *  that the Editor's GraphModel construct.  The GraphModel is
 *  responsible for acutally making an edge in the underlying model
 *  and connecting it to other model elements. */

public class ModeCreatePolyEdge extends ModeCreate {
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The NetPort where the arc is paintn from */
  private Object _startPort;

  /** The Fig that presents the starting NetPort */
  private Fig _startPortFig;

  /** The FigNode on the NetNode that owns the start port */
  private FigNode _sourceFigNode;

  /** The new NetEdge that is being created */
  private Object _newEdge;

  /** The number of points added so far. */
  protected int _npoints = 0;
  protected int _lastX, _lastY, _startX, _startY;
  protected Handle _handle = new Handle(-1);

  ////////////////////////////////////////////////////////////////
  // constructor

  public ModeCreatePolyEdge() { super(); }
  public ModeCreatePolyEdge(Editor par) { super(par); }

  ////////////////////////////////////////////////////////////////
  // Mode API

  public String instructions() {
    return "Drag to define an edge to another port";
  }

  ////////////////////////////////////////////////////////////////
  // ModeCreate API

  /** Create the new item that will be drawn. In this case I would
   *  rather create the FigEdge when I am done. Here I just
   *  create a rubberband FigLine to show during dragging. */
  public Fig createNewItem(MouseEvent me, int snapX, int snapY) {
    FigPoly p = new FigPoly(snapX, snapY);
    p.setLineColor(Globals.getPrefs().getRubberbandColor());
    p.setFillColor(null);
    p.addPoint(snapX, snapY); // add the first point twice
    _startX = _lastX = snapX; _startY = _lastY = snapY;
    _npoints = 2;
    return p;
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** On mousePressed determine what port the user is dragging from.
   *  The mousePressed event is sent via ModeSelect. */
  public void mousePressed(MouseEvent me) {
    if (me.isConsumed()) return;
    int x = me.getX(), y = me.getY();
    //Editor _editor = Globals.curEditor();
    Fig underMouse = _editor.hit(x, y);
    if (underMouse == null) {
      //System.out.println("bighit");
      underMouse = _editor.hit(x-16, y-16, 32, 32);
    }
    if (underMouse == null && _npoints == 0) { done(); me.consume(); return; }
    if (!(underMouse instanceof FigNode) && _npoints == 0) {
      done();
      me.consume();
      return;
    }
    if (_sourceFigNode == null) { //_npoints == 0) {
      _sourceFigNode = (FigNode) underMouse;
      _startPort = _sourceFigNode.deepHitPort(x, y);
    }
    if (_startPort == null) { done(); me.consume(); return; }
    _startPortFig = _sourceFigNode.getPortFig(_startPort);

    if (_npoints == 0) { super.mousePressed(me); }
    //System.out.println("ModeCreatePolyEdge");
    me.consume();
  }

  /** On mouseReleased, find the destination port, ask the GraphModel
   *  to connect the two ports.  If that connection is allowed, then
   *  construct a new FigEdge and add it to the Layer and send it to
   *  the back. */
  public void mouseReleased(MouseEvent me) {
    if (me.isConsumed()) return;
    if (_sourceFigNode == null) { done(); me.consume(); return; }

    int x = me.getX(), y = me.getY();
    Fig f = _editor.hit(x, y);
    if (f == null) { f = _editor.hit(x-16, y-16, 32, 32); }
    GraphModel gm = _editor.getGraphModel();
    if (!(gm instanceof MutableGraphModel)) f = null;
    MutableGraphModel mgm = (MutableGraphModel) gm;
    // needs-more-work: potential class cast exception

    if (f instanceof FigNode) {
      FigNode destFigNode = (FigNode) f;
      // If its a FigNode, then check within the
      // FigNode to see if a port exists
      Object foundPort = destFigNode.deepHitPort(x, y);

      if (foundPort == _startPort && _npoints < 4) {
	// user made a false start
	done();
	me.consume();
	return;
      }

      if (foundPort != null) {
	Fig destPortFig = destFigNode.getPortFig(foundPort);
	FigPoly p = (FigPoly) _newItem;
	if (foundPort == _startPort && _npoints >= 4) p._isSelfLoop = true;
	//_npoints = 0;
	_editor.damaged(p);
	//_editor.getSelectionManager().select(p);
	p._isComplete = true;

	Class edgeClass = (Class) getArg("edgeClass");
	if (edgeClass != null)
	  _newEdge = mgm.connect(_startPort, foundPort, edgeClass);
	else
	  _newEdge = mgm.connect(_startPort, foundPort);

	// Calling connect() will add the edge to the GraphModel and
	// any LayerPersectives on that GraphModel will get a
	// edgeAdded event and will add an appropriate FigEdge
	// (determined by the GraphEdgeRenderer).

	if (null == _newEdge) {
	  System.out.println("MutableGraphModel connect() return null");
	}
	else {
	  LayerManager lm = _editor.getLayerManager();
	  _sourceFigNode.damage();
	  destFigNode.damage();
	  Layer lay = _editor.getLayerManager().getActiveLayer();
	  FigEdge fe = (FigEdge) lay.presentationFor(_newEdge);
	  _newItem.setLineColor(Color.black);
	  fe.setFig(_newItem);
	  fe.setSourcePortFig(_startPortFig);
	  fe.setSourceFigNode(_sourceFigNode);
	  fe.setDestPortFig(destPortFig);
	  fe.setDestFigNode(destFigNode);

	  if (fe != null) _editor.getSelectionManager().select(fe);
	  _editor.damaged(fe);
	}
	done();
	me.consume();
	return;
      }
    }
    if (!nearLast(x, y)) {
      _editor.damaged(_newItem);
      Point snapPt = new Point(x, y);
      _editor.snap(snapPt);
      ((FigPoly)_newItem).addPoint(snapPt.x, snapPt.y);
      _npoints++;
      _editor.damaged(_newItem);
    }
    _lastX = x; _lastY = y;
    me.consume();
  }

  public void mouseMoved(MouseEvent me) {
    mouseDragged(me);
  }

  public void mouseDragged(MouseEvent me) {
    if (me.isConsumed()) return;
    int x = me.getX(), y = me.getY();
    if (_npoints == 0) { me.consume(); return; }
    if (_newItem == null) { me.consume(); return; }
    FigPoly p = (FigPoly)_newItem;
    _editor.damaged(_newItem); // startTrans?
    Point snapPt = new Point(x, y);
    _editor.snap(snapPt);
    _handle.index = p.getNumPoints() - 1;
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

  public void done() {
    super.done();
    if (_newItem != null) _editor.damaged(_newItem);
    _newItem = null;// use this as the fig for the new FigEdge
    _npoints = 0;
    _sourceFigNode = null;
    _startPort = null;
    _startPortFig = null;
  }

  ////////////////////////////////////////////////////////////////
  // key events

  public void keyTyped(KeyEvent ke) {
    if (ke.getKeyChar() == '') { // escape
      done();
      ke.consume();
    }
  }

  static final long serialVersionUID = 4751338603988166591L;
} /* end class ModeCreatePolyEdge */
