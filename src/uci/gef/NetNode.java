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

// File: NetNode.java
// Classes: NetNode
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.io.*;
import java.util.*;

/** This class models a node in our underlying connected graph
 *  model. Nodes have ports that are their connection points to other
 *  nodes. Arcs from one port to another.
 *  <A HREF="../features.html#graph_representation_nodes">
 *  <TT>FEATURE: graph_representation_nodes</TT></A>
 *
 * @see NetEdge
 * @see NetPort
 */

public class NetNode extends NetPrimitive {
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** An array of the ports on this node */
  protected NetPort portList[];
  /** Number ports on this node */
  protected int numPorts;

  /** Nodes may have a list of predefined FigNode's that give the
   *  node different looks. Needs-More-Work: This code is not fully
   *  operational right now. */
  protected Hashtable _presentations = new Hashtable();

  ////////////////////////////////////////////////////////////////
  // constructors and related methods

  /** Construct a new node from the given default node and number of
   *  ports. The attributes of the default node will be used if they
   *  are not overridden in this node (i.e., nodes have attributes and
   *  there is a virual copy relationship between some nodes). */
  public NetNode(NetNode deft, int nPorts) {
    numPorts = nPorts;
    portList = new NetPort[nPorts];
  }

  /** Construct a new NetNode with no default attributes and no ports. */
  public NetNode() { this(null, 0); }

  /** Usually when nodes are created it is deon through newInstance
   *  and there is no chance to supply a default node or to connect
   *  this node to some other application level object. So after a
   *  node is constructed initialize is called to supply that
   *  information. <p>
   *
   * Needs-More-Work: what is the class protocol design here? */
  public void initialize(NetNode default_node, Object model) { }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Returns the attribute table of the node. */
  public Object getAttributes() { return null; }

  /** reply my NetPort with the given index. */
  public NetPort getPort(int i) { return portList[i]; }

  /** returns the FigNodeList */
  public Hashtable getPresentations() { return _presentations; }

  ////////////////////////////////////////////////////////////////
  // Editor API

  /** Remove this node from the underling connected graph model. */
  public void dispose() {
    //System.out.println("disposing: " + toString());
    for (int i = 0; i < numPorts; ++i) {
      if (portList[i] != null) portList[i].dispose();
    }
    Vector v = new Vector(2);
    v.addElement(Globals.REMOVE);
    v.addElement(this);
    setChanged();
    notifyObservers(v);
  }

  ////////////////////////////////////////////////////////////////
  // Visualization related methods

  /** Reply the FigNode that is appropriate for visualizing this
   *  node in the given Layer.  If no such FigNode already exists,
   *  instanciate a new one. */
  public FigNode presentationFor(Layer lay) {
    FigNode fn = (FigNode) lay.presentationFor(this);
    if (null == fn) {
      fn = makePresentation(lay);
      _presentations.put(lay, fn);
    }
    return fn;
  }

  /** Construct and return a new FigNode to present this NetNode
   *  in the given Layer. A default implementation is supplied as an
   *  example, but all subclasses should override this method. NetPorts
   *  of this NetNode should be associated with individual Figs that
   *  make up the FigNode. */
  public FigNode makePresentation(Layer lay) {
    Fig obj1 = new FigRect(-25, -25, 50, 50, Color.black, Color.white);
    Fig obj2 = new FigCircle(-20, -20, 40, 40, Color.red, null);
    Fig obj3 = new FigCircle( -5, -30, 10, 10, Color.black, Color.blue);
    Fig obj4 = new FigCircle( -5,  20, 10, 10, Color.black, Color.blue);
    Fig obj5 = new FigRect(-30,  -5, 10, 10, Color.black, Color.green);
    Fig obj6 = new FigRect( 20,  -5, 10, 10, Color.black, Color.green);
    Vector figs = new Vector();
    figs.addElement(obj1);
    figs.addElement(obj2);
    figs.addElement(obj3);
    figs.addElement(obj4);
    figs.addElement(obj5);
    figs.addElement(obj6);
    FigNode fn = new FigNode(this, figs);

    fn.addPort(portList[0], obj3);
    fn.addPort(portList[1], obj4);
    fn.addPort(portList[2], obj5);
    fn.addPort(portList[3], obj6);

    return fn;
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** NetNode's can process events. If the Editor does not want to
   *  process an Event and the Editor's current Mode does not want to
   *  process it, then the Event is passed to the NetNode under the
   *  mouse, if there is one. This allows nodes to react to user
   *  input to do application specific actions. By default all of
   *  these event handlers do nothing and return false. By
   *  convention, event handlers in subclasses should call the
   *  handler in their superclass if the choose not to handle an
   *  event. */

  /** This event handler is defined to call editNode when the user
   *  clicks the right-hand mouse button on a FigNode. */
  public boolean mouseUp(Event e, int x, int y) {
    if ((e.modifiers | Event.META_MASK) != 0) {
      Editor ce = Globals.curEditor();
      ActionEditNode act = new ActionEditNode(this);
      ce.executeAction(act, e);
      return true;
    }
    return false;
  }

  /** Pop up a meny showing a list of options/commands for this
   *  node. This is not currently availible because AWT 1.0.2 does not
   *  support pop up menus. */
  public void popNodeMenu() {
    System.out.println("now showing a menu of node options");
  }

  /** Edit this NetNode in some application specific way. Typically
   *  open a dialog box to edit some attributes of the
   *  node. Needs-More-Work: shuold provide a default attribute editor
   *  for my system of node attributes. */
  public void editNode() {
    Globals.startPropertySheet();
    Globals.curEditor().updatePropertySheet();
  }

  ////////////////////////////////////////////////////////////////
  // net-level hooks

  /** Do some application specific action just after this node is
   *  connected to another node. the arguments contain some info about
   *  what ports were connected. */
  public void postConnect(NetNode anotherNode,
			  NetPort myPort, NetPort otherPort) { }

  /** Do some application specific action just after this node is
   *  disconnected from another node. the arguments contain some info
   *  about what ports were connected. */
  public void postDisconnect(NetNode anotherNode,
			     NetPort myPort, NetPort otherPort) { }

  ////////////////////////////////////////////////////////////////
  // net-level constraints

  /** Allow foir application specific rules about which nodes can be
   *  connected to which other nodes. This is called from the NetPort,
   *  so the port has first say as to whether it can be connected to
   *  some other port. NetPort.canConnectTo() just calls
   *  NetNode.canConnectTo(). By default anything can be connected to
   *  anything. */
  public boolean canConnectTo(NetNode otherNode,
			      NetPort otherPort, NetPort myPort) {
    return true;
  }

  ////////////////////////////////////////////////////////////////
  // diagram-level hooks

  /** Do some application specific actions after the node is placed in
   *  a drawing area. */
  public void postPlacement(Editor ed) { }

} /* end class NetNode */

