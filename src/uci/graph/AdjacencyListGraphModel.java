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





package uci.graph;

import java.util.*;

/** This class is an example of an alternative way to implement
 * MutableGraphModel.  Needs-more-work: this code has not been used
 * or tested.
 *
 * @see DefaultGraphModel */

public abstract class AdjacencyListGraphModel extends MutableGraphSupport
implements java.io.Serializable {

  ////////////////////////////////////////////////////////////////
  // constants

  public static String UNLABELED = "Unlabeled";

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected Vector _nodes = new Vector();
  protected Vector _edges = new Vector();

  ////////////////////////////////////////////////////////////////
  // constructors

  public AdjacencyListGraphModel() { }


  ////////////////////////////////////////////////////////////////
  // invariants

  public boolean OK() {
    if (_nodes == null) return false;
    if (_edges == null) return false;
    // all edges must start and end on some port on a node in this graph
    Enumeration edgeNum = _edges.elements();
    while (edgeNum.hasMoreElements()) {
      Object[] e = (Object[]) edgeNum.nextElement();
      if (!containsPort(e[0]) || !containsPort(e[1])) return false;
    }
    return true;
  }

  ////////////////////////////////////////////////////////////////
  // GraphModel implementation

  public Vector getNodes() { return _nodes; }
  public Vector getEdges() { return _edges; }

  public abstract Vector getPorts(Object nodeOrEdge);
  public abstract Object getOwner(Object port);

  public Object getSourcePort(Object edge) {
    Object[] labeledEgde = (Object[]) edge;
    return labeledEgde[0];
  }

  public Object getDestPort(Object edge) {
    Object[] labeledEgde = (Object[]) edge;
    return labeledEgde[1];
  }

  public Vector getInEdges(Object port) {
    Vector res = new Vector();
    Enumeration edgeEnum = _edges.elements();
    while (edgeEnum.hasMoreElements()) {
      Object[] e = (Object[]) edgeEnum.nextElement();
      if (port == e[1]) res.addElement(e);
    }
    return res;
  }
  
  public Vector getOutEdges(Object port) {
    Vector res = new Vector();
    Enumeration edgeEnum = _edges.elements();
    while (edgeEnum.hasMoreElements()) {
      Object[] e = (Object[]) edgeEnum.nextElement();
      if (port == e[0]) res.addElement(e);
    }
    return res;
  }
  
  ////////////////////////////////////////////////////////////////
  // MutableGraphModel implementation

  // needs-more-work: notifications
  
  public boolean canAddNode(Object node) { return true; }

  public boolean canAddEdge(Object edge) {
    return (edge instanceof Object[]) && ((Object[])edge).length == 3;
  }

  public void addNode(Object node) { _nodes.addElement(node); }

  public void addEdge(Object edge) {
    if (canAddEdge(edge)) _edges.addElement(edge);
  }

  public void removeNode(Object node) {
    _nodes.removeElement(node);
    // needs-more-work: remove associated edges
  }
  
  public void removeEdge(Object edge) { _edges.removeElement(edge); }

  public boolean canConnect(Object srcNode, Object destNode) {
    return true;
  }

  public Object connect(Object srcPort, Object destPort) {
    return addLabeledEdge(srcPort, destPort, UNLABELED);
  }

  ////////////////////////////////////////////////////////////////
  // labeled edges

  public Object getEdgeLabel(Object edge) {
    Object[] labeledEgde = (Object[]) edge;
    return labeledEgde[2];
  }

  public Object addLabeledEdge(Object srcPort, Object destPort, Object label) {
    Object[] e = new Object[3];
    e[0] = srcPort;
    e[1] = destPort;
    e[2] = label;
    addEdge(e);
    return e;
  }

  public Vector getEdgesLabeled(Object label) {
    Vector res = new Vector();
    Enumeration edgeEnum = _edges.elements();
    while (edgeEnum.hasMoreElements()) {
      Object[] e = (Object[]) edgeEnum.nextElement();
      if (label == getEdgeLabel(e)) res.addElement(e);
    }
    return res;
  }

  
  static final long serialVersionUID = -4115949451676076220L;
} /* end class AdjacencyListGraphModel */
