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
import uci.util.*;

/** A Mode to interpret user input while creating an arc. Basically
 *  mouse down starts creating an arc from a source NetPort, mouse
 *  motion paints a rubberband line, mouse up finds the destination port
 *  and finishes creating the arc and makes an FigEdge and sends
 *  it to the back of the Layer.  */

public class ModeCreateArc extends ModeCreate {
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The NetPort where the arc is paintn from */
  private NetPort _startPort;

  /** The Fig that presents the starting NetPort */
  private Fig _startPortFig;

  /** The FigNode on the NetNode that owns the start port */
  private FigNode _sourceFigNode;

  /** The new NetEdge that is being created */
  private NetEdge _newEdge;

  ////////////////////////////////////////////////////////////////
  // constructor

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
  public Fig createNewItem(Event e, int snapX, int snapY) {
    return new FigLine(snapX, snapY, 0, 0, Globals.getPrefs().rubberbandAttrs());
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** On mouse down determine what port the user is dragging from. The
   *  mouse down event is passed in from ModeSelect. */
  public boolean mouseDown(Event e, int x, int y) {
    Fig underMouse = _editor.hit(x, y);
    if (underMouse == null) { done(); return true; }
    if (!(underMouse instanceof FigNode)) { done(); return true; }
    _sourceFigNode = (FigNode) underMouse;
    _startPort = _sourceFigNode.hitPort(x, y);
    if (_startPort == null) { done(); return true; }
    _startPortFig = _sourceFigNode.getPortFig(_startPort);
    return super.mouseDown(e, x, y);
  }

  /** On mouse up, find the destination port, ask the NetEdge
   *  (e.g., SampleArc) to connect the two ports. If that connection is
   *  allowed, then construct a new FigEdge and add it to the
   *  Layer and send it to the back. */
  public boolean mouseUp(Event e, int x, int y) {
    Class arcClass;

    Fig f = _editor.hit(x, y);

    if (f != null) {
      if (f instanceof FigNode) {
	FigNode destFigNode = (FigNode) f;
	/* If its a FigNode, then check within the  */
	/* FigNode to see if a port exists */
	NetPort foundPort = destFigNode.hitPort(x, y);

	if (foundPort != null && foundPort != _startPort) {
	  Fig destPortFig = destFigNode.getPortFig(foundPort);
	  _newEdge = _startPort.makeEdgeFor(foundPort);
	  if (_newEdge == null) return true;
	  if (_newEdge.connect(_startPort, foundPort)) {
 	    _editor.net().addEdge(_newEdge);
	    LayerManager lm = _editor.getLayerManager();
// 	    FigEdge pers = (FigEdge) lm.presentationFor(_newEdge);
// 	    if (pers == null)
	    FigEdge pers = _newEdge.presentationFor(lm.getActiveLayer());
	    if (Dbg.on) Dbg.assert(pers != null, "FigEdge not found");
	    _editor.add(pers); // adds it to the property sheet's universe
	    _newItem.damagedIn(_editor);
	    _newItem = null;
	    if (pers != null) {
	      pers.reorder(ActionReorder.SEND_TO_BACK,
			   _editor.getLayerManager().getActiveLayer());
	      _editor.select(pers);
	    }
	    done();
	    return true;
	  }
	}
      }
    }
    _newItem.damagedIn(_editor);
    _newItem = null;
    done();
    return true;
  }

} /* end class ModeCreateArc */
