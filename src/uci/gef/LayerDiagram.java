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

// File: LayerDiagram.java
// Classes: LayerDiagram
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;
import uci.util.*;

/** A Layer like found in many drawing applications. It contains a
 *  collection of Fig's, ordered from back to front. Each
 *  LayerDiagram contains part of the overall picture that the user is
 *  drawing.  Needs-More-Work: eventually add a "Layers" menu to the
 *  Editor.
 *  <A HREF="../features.html#graph_visualization">
 *  <TT>FEATURE: graph_visualization</TT></A>
 */

public class LayerDiagram extends Layer {
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The Fig's that are contained in this layer. */
  protected Vector _contents = new Vector();

  /** A counter so that layers have default names like 'One', 'Two', ... */
  protected static int _nextLayerNumbered = 1;

  ////////////////////////////////////////////////////////////////
  // constuctors and related methods

  /** Construct a new LayerDiagram with a default name and do not put
   *  it on the Layer's menu. */
  public LayerDiagram() {
    this("Layer" + numberWordFor(_nextLayerNumbered++));
  }

  /** Construct a new LayerDiagram with the given name, and add it to
   *  the menu of layers. Needs-More-Work: I have not implemented a
   *  menu of layers yet. I don't know if that is really the right user
   *  interface. */
  public LayerDiagram(String name) {
    super(name);
    _onMenu = true;
  }

  /** A utility function to give the spelled-out word for numbers. */
  protected static String numberWordFor(int n) {
    switch(n) {
    case 1: return "One";
    case 2: return "Two";
    case 3: return "Three";
    case 4: return "Four";
    case 5: return "Five";
    case 6: return "Six";
    case 7: return "Seven";
    case 8: return "Eight";
    case 9: return "Nine";
    default: return "Layer " + n;
    }
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Add a Fig to the contents of this layer. Items are
   *  added on top of all other items. */
  public void add(Fig f) {
    if (Dbg.on) Dbg.assert(f != null, "tried to add null Fig");
    _contents.removeElement(f); // act like a set
    _contents.addElement(f);
    f.addObserver(this); // needs-more-work: old?
    f.setLayer(this);
    f.endTrans();
  }

  /** Remove the given Fig from this layer. */
  public void remove(Fig f) {
    _contents.removeElement(f);
    f.endTrans();
    f.deleteObserver(this);
    f.setLayer(null);
  }

  /** Enumerate over all Fig's in this layer. */
  public Enumeration elements() { return _contents.elements(); }

  /** Reply the contents of this layer. Do I really want to do this? */
  public Vector contents() { return _contents; }

  /** Reply the 'top' Fig under the given (mouse)
   *  coordinates. Needs-More-Work: For now, just do a linear search.
   *  Later, optimize this routine using Quad Trees (or other)
   *  techniques.  */
  public Fig hit(Rectangle r) {
    /* search backward so that highest item is found first */
    for (int i = _contents.size() - 1; i >= 0; i--) {
      Fig f = (Fig) _contents.elementAt(i);
      if (f.hit(r)) return f;
    }
    return null;
  }

  /** Delete all Fig's from this layer. */
  public void removeAll() {
    for (int i = _contents.size() - 1; i >= 0; i--) {
      Fig f = (Fig) _contents.elementAt(i);
      f.setLayer(null);
    }
    _contents.removeAllElements();
    //notify?
  }

  /** Find the FigNode that is being used to visualize the
   * given NetPort, or null if there is none in this layer. */
  public FigNode getPortFig(Object port) {
    Enumeration figs = elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if (f instanceof FigNode) {
	FigNode fn = (FigNode) f;
	Fig port_fig = fn.getPortFig(port);
	if (port_fig != null) return fn;
      }
    }
    return null;
  }

  /** Find the Fig that visualized the given NetNode in
   * this layer, or null if there is none. */
  public Fig presentationFor(Object obj) {
    Enumeration figs = elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if (f.getOwner() == obj) return f;
    }
    return null;
  }

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paint all the Fig's that belong to this layer. */
  public void paintContents(Graphics g) {
    Rectangle clip = g.getClipRect();
    Enumeration figs = elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if (clip == null || f.getBounds().intersects(clip)) {
	f.paint(g);
      }
    }
  }

  ////////////////////////////////////////////////////////////////
  // ordering of Figs

  /** Reorder the given Fig in this layer. */
  public void sendToBack(Fig f) {
    _contents.removeElement(f);
    _contents.insertElementAt(f, 0);
  }

  /** Reorder the given Fig in this layer. */
  public void bringToFront(Fig f) {
    _contents.removeElement(f);
    _contents.addElement(f);
  }

  /** Reorder the given Fig in this layer. Needs-more-work:
   * Should come backward/forward until they change positions with an
   * object they overlap. Maybe... */
  public void sendBackward(Fig f) {
    int i = _contents.indexOf(f);
    if (i == -1 || i == 0) return;
    Object prevFig = _contents.elementAt(i - 1);
    _contents.setElementAt(prevFig, i);
    _contents.setElementAt(f, i - 1);
  }

  /** Reorder the given Fig in this layer. */
  public void bringForward(Fig f) {
    int i = _contents.indexOf(f);
    if (i == -1 || i == _contents.size()-1) return;
    Object nextFig = _contents.elementAt(i + 1);
    _contents.setElementAt(nextFig, i);
    _contents.setElementAt(f, i + 1);
  }

  /** Reorder the given Fig in this layer. */
  public void reorder(Fig f, int function) {
    switch (function) {
    case ActionReorder.SEND_TO_BACK:
      sendToBack(f);
      break;
    case ActionReorder.BRING_TO_FRONT:
      bringToFront(f);
      break;
    case ActionReorder.SEND_BACKWARD:
      sendBackward(f);
      break;
    case ActionReorder.BRING_FORWARD:
      bringForward(f);
      break;
    }
  }

} /* end class LayerDiagram */

