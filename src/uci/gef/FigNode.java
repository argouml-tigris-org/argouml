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

// File: FigNode.java
// Classes: FigNode
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;
import uci.util.*;
import uci.ui.*;

/** Class to display graphics for a NetNode in a diagram.
 *  <A HREF="../features.html#graph_visualization_nodes">
 *  <TT>FEATURE: graph_visualization_nodes</TT></A>
 *  <A HREF="../features.html#graph_visualization_ports">
 *  <TT>FEATURE: graph_visualization_ports</TT></A>
 */

public class FigNode extends FigGroup {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Each FigNode keeps a collection of the NetPorts that are
   *  shown in this FigNode.
   *  <A HREF="../features.html#graph_visualization_ports">
   *  <TT>FEATURE: graph_visualization_ports</TT></A>
   */
  public Vector _ports = new Vector(); //? final

  /** True if you want ports to show when the mouse moves in and
   *  be invisible otherwise. */
  public boolean _blinkPorts = false;

  /** True when we want to draw the user's attention to this FigNode. */
  public boolean _highlight = false;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Constructs a new FigNode on the given NetNode with the
   *  given Fig's. */
  public FigNode(NetNode nn) {
    setOwner(nn);
    nn.addPersistantObserver(this);
  }

  public FigNode(NetNode nn, Vector figs) {
    this(nn);
    setFigs(figs);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Set the property of highlighting ports when the user moves the
   *  mouse over this FigNode.
   *  <A HREF="../features.html#graph_visualization_ports">
   *  <TT>FEATURE: graph_visualization_ports</TT></A>
   */
  public void setBlinkPorts(boolean b) { _blinkPorts = b; }
  public boolean getBlinkPorts() { return _blinkPorts; }


  /** Reply a collection of FigEdge's for all the NetEdges that
   *  are connected to NetPorts of the NetNode being
   *  displayed. Needs-More-Work: this code is really slow. */
  protected Vector figEdges(Editor ed) {
    Vector arcs = new Vector();
    Vector figEdges = new Vector();
    Enumeration curPort =  _ports.elements();
    while (curPort.hasMoreElements()) {
      NetPort p = (NetPort) curPort.nextElement();
      Enumeration curArc = p.getEdges().elements();
      while (curArc.hasMoreElements()) {
	NetEdge a = (NetEdge) curArc.nextElement();
	arcs.addElement(a);
      }
    }
    Enumeration curDE = ed.figs();
    while (curDE.hasMoreElements()) {
      Fig f = (Fig) curDE.nextElement();
      Object owner = f.getOwner();
      if (owner != null && arcs.contains(owner)) {
	figEdges.addElement(f);
      }
    }
    return figEdges;
  }


  ////////////////////////////////////////////////////////////////
  // Editor API

  /** When a FigNode is damaged, all of its arcs may need repainting. */
  public void damagedIn(Editor ed) {
    Enumeration arcPers = figEdges(ed).elements();
    while (arcPers.hasMoreElements()) {
      Fig f = (Fig) arcPers.nextElement();
      f.damagedIn(ed);
    }
    super.damagedIn(ed);
  }

  public void removeFrom(Editor ed) {
    Object own = getOwner();
    if (own instanceof NetNode) {
      ((NetNode)own).deleteObserver(this);
      own = null;
    }
    super.removeFrom(ed);
  }

  /** When a FigNode is removed, dispose all of the NetEdges and
   * the underlying NetNode. Needs-More-Work: is this right? Doesn't
   * ActionDelete call this method also? How is deletion of a
   * FigNode handled without disposing the underlying model? */
  public void dispose(Editor ed) {
    Enumeration arcPers = figEdges(ed).elements();
    while (arcPers.hasMoreElements()) {
      Fig f = (Fig) arcPers.nextElement();
      f.dispose(ed);
    }
    Object own = getOwner();
    if (own instanceof NetNode) ((NetNode)own).dispose();
    super.dispose(ed);
  }


  ////////////////////////////////////////////////////////////////
  // ports

  /** Adds a port into the current FigNode.
   *  <A HREF="../features.html#graph_visualization_ports">
   *  <TT>FEATURE: graph_visualization_ports</TT></A>
   */
  public void bindPort(NetPort np, Fig f) {
    Fig oldPortFig = getPortFig(np);
    if (oldPortFig != null) oldPortFig.setOwner(null); //?
    f.setOwner(np);
    if (!_ports.contains(np)) _ports.addElement(np);
  }

  /** Removes a port from the current FigNode.
   *  <A HREF="../features.html#graph_visualization_ports">
   *  <TT>FEATURE: graph_visualization_ports</TT></A>
   */
  public void removePort(Fig rep) {
    if (rep.getOwner() != null) {
      _ports.removeElement(rep.getOwner());
      rep.setOwner(null);
    }
  }

  /** Reply the NetPort associated with the Fig under the mouse, or
   *  null if there is none.
   *  <A HREF="../features.html#graph_visualization_ports">
   *  <TT>FEATURE: graph_visualization_ports</TT></A>
   */
  public final NetPort hitPort(Point p) { return hitPort(p.x, p.y); }

  /** Reply the port that "owns" the Fig under the given point, or
   *  null if none.
   *  <A HREF="../features.html#graph_visualization_ports">
   *  <TT>FEATURE: graph_visualization_ports</TT></A>
   */
  public NetPort hitPort(int x, int y) {
    Fig f = hitFig(new Rectangle(x, y, 1, 1)); //?
    if (f != null) {
      Object own = f.getOwner();
      if (own instanceof NetPort) return (NetPort) own;
    }
    return null;
  }

  /** Reply the Fig that displays the given NetPort.
   *  <A HREF="../features.html#graph_visualization_ports">
   *  <TT>FEATURE: graph_visualization_ports</TT></A>
   */
  public Fig getPortFig(NetPort np) {
    Enumeration figs = elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if (f.getOwner() == np) return f;
    }
    return null;
  }

  /** Reply a list of Fig's that have a NetPort as their owner */
  public Vector getPortFigs() {
    Vector res = new Vector();
    Enumeration figs = elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if (f.getOwner() instanceof NetPort) res.addElement(f);
    }
    return res;
  }

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paints the FigNode to the given Graphics. */
  public void paint(Graphics g) {
    super.paint(g);
    if (_highlight) {
      g.setColor(Globals.getPrefs().getHighlightColor()); /* needs-more-work */
      g.drawRect(_x - 3, _y - 3, _w + 6 - 1, _h + 6 - 1);
      g.drawRect(_x - 2, _y - 2, _w + 4 - 1, _h + 4 - 1);
    }
  }

  ////////////////////////////////////////////////////////////////
  // notifications and updates

  /** If I get a notification that I should be Highlighted or
   *  Unhighlight, set a flag and mark my area as damaged. */
  public void update(Observable o, Object arg) {
    if (Globals.HIGHLIGHT.equals(arg)) {
      startTrans();
      _highlight = true;
      endTrans();
    }
    else if (Globals.UNHIGHLIGHT.equals(arg)) {
      startTrans();
      _highlight = false;
      endTrans();
    }
    else if (arg instanceof Vector) {
      Vector v = (Vector) arg;
      String s = (String) v.elementAt(0);
      Object obj = v.elementAt(1);
      if (s.equals(Globals.REMOVE) && obj == getOwner()) {
	removeFrom(null);
      }
    }
  }

  /**  <A HREF="../features.html#graph_visualization_ports">
   *  <TT>FEATURE: graph_visualization_ports</TT></A>
   */
  public void showPorts() {
    startTrans();
    //    Color c = Globals.getPrefs().highlightColor();
    Enumeration figs = elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if (f.getOwner() != null) {
	f.setLineWidth(1);
	f.setFilled(true);
      }
    }
    endTrans();
  }

  /**  <A HREF="../features.html#graph_visualization_ports">
   *  <TT>FEATURE: graph_visualization_ports</TT></A>
   */
  public void hidePorts() {
    startTrans();
    Enumeration figs = elements();
    while (figs.hasMoreElements()) {
      Fig f = (Fig) figs.nextElement();
      if (f.getOwner() != null) {
	f.setLineWidth(0);
	f.setFilled(false);
      }
    }
    endTrans();
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** If the mouse enters this FigNode's bbox and the
   *  _blinkPorts flag is set, then display ports. */
  public boolean mouseEnter(Event e, int x, int y) {
    if (_blinkPorts) showPorts();
    return super.mouseEnter(e, x, y);
  }

  /** If the mouse exits this FigNode's bbox and the
   *  _blinkPorts flag is set, then unhighlight ports. */
  public boolean mouseExit(Event e, int x, int y) {
    if (_blinkPorts) hidePorts();
    return super.mouseExit(e, x, y);
  }

} /* end class FigNode */

