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

// File: ModeCreateArc.java
// Classes: ModeCreateArc
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.MouseEvent;
import uci.util.*;
import uci.graph.*;

/** A Mode to interpret user input while creating an arc. Basically
 *  mouse down starts creating an arc from a source NetPort, mouse
 *  motion paints a rubberband line, mouse up finds the destination port
 *  and finishes creating the arc and makes an FigEdge and sends
 *  it to the back of the Layer.  */

public class ModeCreateArc extends ModeCreate {
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The NetPort where the arc is paintn from */
  //private NetPort _startPort;
  private Object _startPort;

  /** The Fig that presents the starting NetPort */
  private Fig _startPortFig;

  /** The FigNode on the NetNode that owns the start port */
  private FigNode _sourceFigNode;

  /** The new NetEdge that is being created */
  //private NetEdge _newEdge;
  private Object _newEdge;

  ////////////////////////////////////////////////////////////////
  // constructor

  public ModeCreateArc() { super(); }
  public ModeCreateArc(Editor par) { super(par); }

  ////////////////////////////////////////////////////////////////
  // Mode API

  public String instructions() {
    return "Drag to define an arc to another port";
  }

  ////////////////////////////////////////////////////////////////
  // ModeCreate API

  /** Create the new item that will be drawn. In this case I would
   *  rather create the FigEdge when I am done. Here I just
   *  create a rubberband line to show during the interaction. */
  public Fig createNewItem(MouseEvent me, int snapX, int snapY) {
    return new FigLine(snapX, snapY, 0, 0,
		       Globals.getPrefs().rubberbandAttrs());
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** On mouse down determine what port the user is dragging from. The
   *  mouse down event is passed in from ModeSelect. */
  public void mousePressed(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    Editor _editor = Globals.curEditor();
    Fig underMouse = _editor.hit(x, y);
    if (underMouse == null) { done(); me.consume(); return; }
    if (!(underMouse instanceof FigNode)) { done(); me.consume(); return; }
    _sourceFigNode = (FigNode) underMouse;
    _startPort = _sourceFigNode.deepHitPort(x, y);
    if (_startPort == null) { done(); me.consume(); return; }
    _startPortFig = _sourceFigNode.getPortFig(_startPort);
    super.mousePressed(me);
  }

  /** On mouse up, find the destination port, ask the NetEdge
   *  (e.g., SampleArc) to connect the two ports. If that connection is
   *  allowed, then construct a new FigEdge and add it to the
   *  Layer and send it to the back. */
  public void mouseReleased(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    Class arcClass;
    Editor _editor = Globals.curEditor();

    Fig f = _editor.hit(x, y);
    GraphModel gm = _editor.getGraphModel();
    if (!(gm instanceof MutableGraphModel)) f = null;
    MutableGraphModel mgm = (MutableGraphModel) gm;

    if (f != null) {
      if (f instanceof FigNode) {
	FigNode destFigNode = (FigNode) f;
	/* If its a FigNode, then check within the  */
	/* FigNode to see if a port exists */
	Object foundPort = destFigNode.deepHitPort(x, y);

	if (foundPort != null && foundPort != _startPort) {
	  Fig destPortFig = destFigNode.getPortFig(foundPort);
	  Class edgeClass = (Class) getArg("edgeClass");
	  if (edgeClass != null)
	    _newEdge = mgm.connect(_startPort, foundPort, edgeClass);
	  else
	    _newEdge = mgm.connect(_startPort, foundPort);
	  
	  if (null != _newEdge) {
	    LayerManager lm = _editor.getLayerManager();
// 	    FigEdge pers = (FigEdge) lm.presentationFor(_newEdge);
// 	    if (pers == null)
	    //	    FigEdge pers =
	    //  ((NetEdge)_newEdge).presentationFor(lm.getActiveLayer());
	    GraphEdgeRenderer rend = _editor.getGraphEdgeRenderer();
	    // needs-more-work: pass in the layer
	    //FigEdge pers = rend.getFigEdgeFor(mgm, null, _newEdge);
	    //pers.sourcePortFig(_startPortFig);
	    //pers.destPortFig(destPortFig);
	    //pers.sourceFigNode(_sourceFigNode);
	    //pers.destFigNode(destFigNode);
	    //if (Dbg.on) Dbg.assert(pers != null, "FigEdge not found");
	    //_editor.add(pers); // adds it to the property sheet's universe
	    _newItem.damage();
	    _newItem = null;
	    // if (pers != null) {
// 	      pers.reorder(CmdReorder.SEND_TO_BACK,
// 			   _editor.getLayerManager().getActiveLayer());
// 	      _editor.getSelectionManager().select(pers);
	    //}

	    Fig pers = _editor.getLayerManager().getActiveLayer().
	      presentationFor(_newEdge);
	    if (pers != null) _editor.getSelectionManager().select(pers);
	    done();
	    me.consume();
	    return;
	  }
	}
      }
    }
    _newItem.damage();
    _newItem = null;
    done();
    me.consume();
  }

} /* end class ModeCreateArc */
