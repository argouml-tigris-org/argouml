// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.



// File: ModeCreateEdge.java
// Classes: ModeCreateEdge
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.MouseEvent;
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

public class ModeCreateEdge extends ModeCreate {
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

  ////////////////////////////////////////////////////////////////
  // constructor

  public ModeCreateEdge() { super(); }
  public ModeCreateEdge(Editor par) { super(par); }

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
    return new FigLine(snapX, snapY, 0, 0,
		       Globals.getPrefs().getRubberbandColor());
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** On mousePressed determine what port the user is dragging from.
   *  The mousePressed event is sent via ModeSelect. */
  public void mousePressed(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    Editor _editor = Globals.curEditor();
    Fig underMouse = _editor.hit(x, y);
    if (underMouse == null) { 
      //System.out.println("bighit");
        underMouse = _editor.hit(x-16, y-16, 32, 32); 
    }
    if (underMouse == null) { done(); me.consume(); return; }
    if (!(underMouse instanceof FigNode)) { done(); me.consume(); return; }
    _sourceFigNode = (FigNode) underMouse;
    _startPort = _sourceFigNode.deepHitPort(x, y);
    if (_startPort == null) { done(); me.consume(); return; }
    _startPortFig = _sourceFigNode.getPortFig(_startPort);
    super.mousePressed(me);
  }

  /** On mouseReleased, find the destination port, ask the GraphModel
   *  to connect the two ports.  If that connection is allowed, then
   *  construct a new FigEdge and add it to the Layer and send it to
   *  the back. */
  public void mouseReleased(MouseEvent me) {
    if (_sourceFigNode == null) { done(); me.consume(); return; }

    int x = me.getX(), y = me.getY();
    Class arcClass;
    Editor _editor = Globals.curEditor();
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

      if (foundPort != null && foundPort != _startPort) {
	Fig destPortFig = destFigNode.getPortFig(foundPort);
	Class edgeClass = (Class) getArg("edgeClass");
	if (edgeClass != null)
	  _newEdge = mgm.connect(_startPort, foundPort, edgeClass);
	else
	  _newEdge = mgm.connect(_startPort, foundPort);

	// Calling connect() will add the edge to the GraphModel and
	// any LayerPersectives on that GraphModel will get a
	// edgeAdded event and will add an appropriate FigEdge
	// (determined by the GraphEdgeRenderer).

	if (null != _newEdge) {
	  LayerManager lm = _editor.getLayerManager();
	  _editor.damaged(_newItem);
	  _sourceFigNode.damage();
	  destFigNode.damage();
	  _newItem = null;

	  Fig pers = _editor.getLayerManager().getActiveLayer().
	    presentationFor(_newEdge);
	  if (pers != null) _editor.getSelectionManager().select(pers);
	  done();
	  me.consume();
	  return;
	}
	else
	  System.out.println("connection return null");
      }
    }
    _sourceFigNode.damage();
    _editor.damaged(_newItem);
    _newItem = null;
    done();
    me.consume();
  }

} /* end class ModeCreateEdge */
