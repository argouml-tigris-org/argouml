// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
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

// File: DefaultGraphModel.java
// Interfaces: DefaultGraphModel
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.graph;

import uci.gef.NetList;
import uci.gef.NetNode;
import uci.gef.NetPort;
import uci.gef.NetEdge;

/** This interface provides a facade to a net-level
 *  representation. Similiar in concept to the Swing class TreeModel.
 *
 * @see 
 * @see AdjacencyMatrixGraphModel
 * @see uci.graph.demo.WordTransforms */

public class DefaultGraphModel implements MutableGraphModel {
  ////////////////////////////////////////////////////////////////
  // instance variables

  protected NetList _netList;
  protected Vector _graphListeners;

  ////////////////////////////////////////////////////////////////
  // constructors

  public DefaultGraphModel() { _netList = new NetList(); }

  public DefaultGraphModel(NetList nl) { _netList = nl; }

  ////////////////////////////////////////////////////////////////
  // accessors

  public NetList getNetList() { return _netList; }
  public void setNetList(NetList nl) { _netList = nl; }

  ////////////////////////////////////////////////////////////////
  // invariants

  public boolean OK() { return _netList != null; }

  ////////////////////////////////////////////////////////////////
  // interface GraphModel

  /** Return all nodes in the graph */
  public Vector getNodes() { return _netList.getNodes(); }

  /** Return all nodes in the graph */
  public Vector getEdges() { return _netList.getEdges();}

  /** Return all ports on node or edge */
  public Vector getPorts(Object nodeOrEdge) {
    if (nodeOrEdge instanceof NetNode)
      return ((NetNode)nodeOrEdge).getPorts();
    if (nodeOrEdge instanceof NetEdge)
      return ((NetEdge)nodeOrEdge).getPorts();
    return null; // raise exception
  }

  /** Return the node or edge that owns the given port */
  public Object getOwner(Object port) {
    if (port instanceof NetPort)
      return ((NetPort)port).getParent();
    if (port instanceof NetEdge)
      return ((NetEdge)port).getParent();
    return null; // raise exception
  }

  /** Return all edges going to given port */
  public Vector getInEdges(Object port) {
    // needs-more-work: only IN edges
    if (port instanceof NetPort)
      return ((NetPort)port).getEdges();
    return null; // raise exception
  }

  /** Return all edges going from given port */
  public Vector getOutEdges(Object port) {
    // needs-more-work: only IN edges
    if (port instanceof NetPort)
      return ((NetPort)port).getEdges();
    return null; // raise exception
  }

  /** Return one end of an edge */
  public Object getSourcePort(Object edge) {
    if (edge instanceof NetEdge)
      return ((NetEdge)edge).getSourcePort();
    return null; // raise exception
  }

  /** Return  the other end of an edge */
  public Object getDestPort(Object edge) {
    if (edge instanceof NetEdge)
      return ((NetEdge)edge).getDestPort();
    return null; // raise exception
  }

  public void addGraphEventListener(GraphListener listener) {
    if (_graphListeners == null) _graphListeners = new Vector();
    _graphListeners.addElement(listener);
  }
  public void removeGraphEventListener(GraphListener listener) {
    if (_graphListeners == null) return;
    _graphListeners.removeElement(listener);
  }

  ////////////////////////////////////////////////////////////////
  // event notifications

  public void fireNodeAddedEvent(NetNode n) {
    GraphEvent ge = new GraphEvent(this, n);
    Enumeration listeners = _graphListeners.elements();
    while (listeners.hasMoreElements()) {
      GraphListener listen = (GraphListener) listeners.nextElement();
      listen.nodeAdded(ge);
    }
  }

  public void fireNodeRemovedEvent(NetNode n) {
    GraphEvent ge = new GraphEvent(this, n);
    Enumeration listeners = _graphListeners.elements();
    while (listeners.hasMoreElements()) {
      GraphListener listen = (GraphListener) listeners.nextElement();
      listen.nodeRemoved(ge);
    }
  }

  public void fireEdgeAddedEvent(NetEdge e) {
    GraphEvent ge = new GraphEvent(this, e);
    Enumeration listeners = _graphListeners.elements();
    while (listeners.hasMoreElements()) {
      GraphListener listen = (GraphListener) listeners.nextElement();
      listen.edgeAdded(ge);
    }
  }

  public void fireEdgeRemovedEvent(NetEdge e) {
    GraphEvent ge = new GraphEvent(this, e);
    Enumeration listeners = _graphListeners.elements();
    while (listeners.hasMoreElements()) {
      GraphListener listen = (GraphListener) listeners.nextElement();
      listen.edgeRemoved(ge);
    }
  }

  public void fireGraphChangedEvent(NetEdge e) {
    GraphEvent ge = new GraphEvent(this, null);
    Enumeration listeners = _graphListeners.elements();
    while (listeners.hasMoreElements()) {
      GraphListener listen = (GraphListener) listeners.nextElement();
      listen.graphChanged(ge);
    }
  }


  ////////////////////////////////////////////////////////////////
  // interface MutableGraphListener

  /** Return true if the given object is a valid node in this graph */
  public boolean canAddNode(Object node) { return (node instanceof NetNode); }

  /** Return true if the given object is a valid edge in this graph */
  public boolean canAddEdge(Object edge) { return (edge instanceof NetEdge); }

  /** Remove the given node from the graph. */
  public void removeNode(Object node) {
    NetNode n = (NetNode) node;
    _netList.removeNode(n);
    fireNodeRemovedEvent(n);
  }

  /** Add the given node to the graph, if valid. */
  public void addNode(Object node) {
    NetNode n = (NetNode) node;
    _netList.addNode(n);
    fireNodeAddedEvent(n);
  }

  /** Add the given edge to the graph, if valid. */
  public void addEdge(Object edge) {
    NetEdge e = (NetEdge) edge;
    _netList.addEdge(e);
    fireEdgeAddedEvent(e);
  }

  /** Remove the given edge from the graph. */
  public void removeEdge(Object edge) {
    NetEdge e = (NetEdge) edge;
    _netList.removeEdge(edge);
    fireEdgeRemovedEvent(e);
  }

  /** Return true if the two given ports can be connected by a 
   * kind of edge to be determined by the ports. */
  public boolean canConnect(Object srcPort, Object destPort) {
    if (srcPort instanceof NetPort && destPort instanceof NetPort) {
      NetPort s = (NetPort) srcPort;
      NetPort d = (NetPort) destPort;
      return s.canConnectTo(d) && d.canConnectTo(s);
    }
    else return false;
  }

  /** Return true if the two given ports can be connected by the given
   * kind of edge. */
  public boolean canConnect(Object srcPort, Object destPort, Class edgeClass) {
    // needs-more-work: take ask edgeClass
    return canConnect(srcPort, destPort);
  }

  /** Contruct and add a new edge of a kind determined by the ports */
  public Object connect(Object srcPort, Object destPort) {
    if (!canConnect(srcPort, destPort)) return null;
    if (srcPort instanceof NetPort && destPort instanceof NetPort) {
      NetPort s = (NetPort) srcPort;
      NetPort d = (NetPort) destPort;
      NetEdge e = s.makeEdgeFor(d);
      return connectInternal(s, d, e);
    }
    else return null;
  }

  /** Contruct and add a new edge of the given kind */
  public Object connect(Object srcPort, Object destPort, Class edgeClass) {
    if (!canConnect(srcPort, destPort)) return null;
    if (srcPort instanceof NetPort && destPort instanceof NetPort) {
      NetPort s = (NetPort) srcPort;
      NetPort d = (NetPort) destPort;
      NetEdge e = edgeClass.newInstance();
      return connectInternal(s, d, e);
    }
    else return null;
  }

  protected Object connectInternal(NetPort s, NetPort d, NetEdge e) {
      e.connect(s, d);
      addEdge(e);
      return e;
  }


} /* end class DefaultGraphModel */
