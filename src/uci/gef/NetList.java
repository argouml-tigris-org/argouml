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

// File: NetList.java
// Classes: NetList
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.util.*;
import java.awt.*;
import java.io.*;

/** A class that implements the concept of a connected graph. A
 *  NetList is not any one object in the connected graph, it is the
 *  overall graph. A NetList contains a list of nodes, and the nodes
 *  refer to their ports and edges.
 *  <A HREF="../features.html#graph_representation_nets">
 *  <TT>FEATURE: graph_representation_nets</TT></A>
 */

public class NetList extends NetPrimitive {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The nodes in the NetList */
  private Vector _nodes = new Vector();

  /** The edges in the NetList */
  private Vector _edges = new Vector();

  /** The name of this connected graph. */
  String _name;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new NetList with no contained nodes. */
  public NetList() { }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void name(String n) { _name = n; }
  public String name() { return _name; }

  /** Reply the vector of nodes */
  public Vector nodes() { return _nodes; }

  /** Reply the vector of edges */
  public Vector edges() { return _edges; }

  /** Add a node to this NetList. Needs-More-Work: should I send a
   *  notification? This is called from the Editor when a
   *  Fig is added that is a FigNode.
   *
   * @see Editor#add */
  public void addNode(NetNode n) {
    setChanged();
    Vector v = new Vector(2);
    v.addElement("add");
    v.addElement(n);
    notifyObservers(v);
    _nodes.addElement(n);
  }

  /** Remove a node from this NetList. When a node is deleted a
   *  notification is sent out. */
  public void removeNode(NetNode n) {
    if (n != null && _nodes.contains(n)) {
      _nodes.removeElement(n);
      Vector v = new Vector(2);
      v.addElement(Globals.REMOVE);
      v.addElement(n);
      setChanged();
      notifyObservers(v);
    }
  }

  /** Add a NetEdge to this NetList. Needs-More-Work: should I send a
   *  notification? This is called from the Editor when a
   *  Fig is added that is a FigEdge.
   *
   * @see Editor#add */
  public void addEdge(NetEdge a) {
    setChanged();
    Vector v = new Vector(2);
    v.addElement("add");
    v.addElement(a);
    notifyObservers(v);
    _edges.addElement(a);
  }

  /** Remove a Edge from this NetList. When a Edge is deleted a
   *  notification is sent out. */
  public void removeEdge(NetEdge a) {
    if (a != null && _edges.contains(a)) {
      _edges.removeElement(a);
      Vector v = new Vector(2);
      v.addElement(Globals.REMOVE);
      v.addElement(a);
      setChanged();
      notifyObservers(v);
    }
  }

} /* end class NetList */

