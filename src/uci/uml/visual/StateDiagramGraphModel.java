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



// File: StateDiagramGraphModel.java
// Classes: StateDiagramGraphModel
// Original Author: your email address here
// $Id$


package uci.uml.visual;

import java.util.*;
import java.beans.*;

import uci.graph.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.PseudostateKind;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.State_Machines.*;


/** This class defines a bridge between the UML meta-model
 *  representation of the design and the GraphModel interface used by
 *  GEF.  This class handles only UML State Digrams.  */

public class StateDiagramGraphModel extends MutableGraphSupport
implements MutableGraphModel, VetoableChangeListener {
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Vector _nodes = new Vector();
  protected Vector _edges = new Vector();

  /** The "home" UML model of this diagram, not all ModelElements in this
   *  graph are in the home model, but if they are added and don't
   *  already have a model, they are placed in the "home model".
   *  Also, elements from other models will have their FigNodes add a
   *  line to say what their model is. */

  protected Namespace _model;

  /** The statemachine we are diagramming */
  protected StateMachine _machine;

  ////////////////////////////////////////////////////////////////
  // accessors

  public Namespace getNamespace() { return _model; }
  public void setNamespace(Namespace m) {
    if (_model != null) _model.removeVetoableChangeListener(this);
    _model = m;
    if (_model != null) _model.addVetoableChangeListener(this);
  }

  public StateMachine getMachine() { return _machine; }
  public void setMachine(StateMachine sm) {
    if (_machine != null) _machine.removeVetoableChangeListener(this);
    _machine = sm;
    if (_machine != null) _machine.addVetoableChangeListener(this);
  }

  ////////////////////////////////////////////////////////////////
  // GraphModel implementation

  /** Return all nodes in the graph */
  public Vector getNodes() { return _nodes; }

  /** Return all nodes in the graph */
  public Vector getEdges() { return _edges; }

  /** Return all ports on node or edge */
  public Vector getPorts(Object nodeOrEdge) {
    Vector res = new Vector();  //wasteful!
    if (nodeOrEdge instanceof State) res.addElement(nodeOrEdge);
    if (nodeOrEdge instanceof Pseudostate) res.addElement(nodeOrEdge);
    return res;
  }

  /** Return the node or edge that owns the given port */
  public Object getOwner(Object port) {
    return port;
  }

  /** Return all edges going to given port */
  public Vector getInEdges(Object port) {
    if (port instanceof StateVertex) {
      return ((StateVertex)port).getIncoming();
    }
    System.out.println("needs-more-work getInEdges of State");
    return new Vector(); //wasteful!
  }

  /** Return all edges going from given port */
  public Vector getOutEdges(Object port) {
    if (port instanceof StateVertex) {
      return ((StateVertex)port).getOutgoing();
    }
    System.out.println("needs-more-work getOutEdges of State");
    return new Vector(); //wasteful!
  }

  /** Return one end of an edge */
  public Object getSourcePort(Object edge) {
    if (edge instanceof Transition) {
      Transition tr = (Transition) edge;
      return tr.getSource();
    }
    System.out.println("needs-more-work getSourcePort of Transition");
    return null;
  }

  /** Return  the other end of an edge */
  public Object getDestPort(Object edge) {
    if (edge instanceof Transition) {
      Transition tr = (Transition) edge;
      return tr.getTarget();
    }
    System.out.println("needs-more-work getDestPort of Transition");
    return null;
  }


  ////////////////////////////////////////////////////////////////
  // MutableGraphModel implementation

  /** Return true if the given object is a valid node in this graph */
  public boolean canAddNode(Object node) {
    return (node instanceof StateVertex);
  }

  /** Return true if the given object is a valid edge in this graph */
  public boolean canAddEdge(Object edge)  {
    return (edge instanceof Transition);
  }

  /** Remove the given node from the graph. */
  public void removeNode(Object node) {
    if (!_nodes.contains(node)) return;
    _nodes.removeElement(node);
    fireNodeRemoved(node);
  }

  /** Add the given node to the graph, if valid. */
  public void addNode(Object node) {
    //System.out.println("adding state diagram node: " + node);
    if (!(node instanceof StateVertex)) {
      System.out.println("internal error: got past canAddNode");
      return;
    }
    StateVertex sv = (StateVertex) node;
    if (_nodes.contains(sv)) return;
    _nodes.addElement(sv);
    try {
      // needs-more-work: assumes public, user pref for default visibility?
//       if (sv.getElementOwnership() == null)
// 	_model.addPublicOwnedElement(sv);
      // needs-more-work: assumes not nested in another composite state
      CompositeState top = (CompositeState) _machine.getTop();
      top.addSubstate(sv);
//       sv.setParent(top);
//       if (sv instanceof State) ((State)sv).setStateMachine(_machine);
    }
    catch (PropertyVetoException pve) {
      System.out.println("PropertyVetoException in StateDiagramGraphModel addNode");
    }
    fireNodeAdded(node);
  }

  /** Add the given edge to the graph, if valid. */
  public void addEdge(Object edge) {
    //System.out.println("adding state diagram edge!!!!!!");
    if (!(edge instanceof Transition)) {
      System.out.println("internal error: got past canAddEdge");
      return;
    }
    Transition tr = (Transition) edge;
    if (_edges.contains(tr)) return;
    _edges.addElement(tr);
    try {
      // needs-more-work: assumes public
//       if (tr.getElementOwnership() == null)
// 	_model.addPublicOwnedElement(tr);
      //_machine.addTransition(tr);
      tr.setStateMachine(_machine);
    }
    catch (PropertyVetoException pve) {
      System.out.println("got a PropertyVetoException");
    }
    fireEdgeAdded(edge);
  }

  /** Remove the given edge from the graph. */
  public void removeEdge(Object edge) {
    if (!_edges.contains(edge)) return;
    _edges.removeElement(edge);
    fireEdgeRemoved(edge);
  }

  /** Return true if the two given ports can be connected by a 
   * kind of edge to be determined by the ports. */
  public boolean canConnect(Object fromPort, Object toPort) {
    if (!(fromPort instanceof StateVertex)) {
      System.out.println("internal error not from sv");
      return false;
    }
    if (!(toPort instanceof StateVertex)) {
      System.out.println("internal error not to sv");
      return false;
    }
    StateVertex fromSV = (StateVertex) fromPort;
    StateVertex toSV = (StateVertex) toPort;

    if (fromSV instanceof Pseudostate)
      if (PseudostateKind.FINAL.equals(((Pseudostate)fromSV).getKind()))
	return false;
    if (toSV instanceof Pseudostate)
      if (PseudostateKind.INITIAL.equals(((Pseudostate)toSV).getKind()))
	  return false;

    return true;
  }


  /** Contruct and add a new edge of a kind determined by the ports */
  public Object connect(Object fromPort, Object toPort) {
    System.out.println("should not enter here! connect2");
    return null;
  }

  /** Contruct and add a new edge of the given kind */
  public Object connect(Object fromPort, Object toPort,
			java.lang.Class edgeClass) {
    //    try {
    if (!(fromPort instanceof StateVertex)) {
      System.out.println("internal error not from sv");
      return null;
    }
    if (!(toPort instanceof StateVertex)) {
      System.out.println("internal error not to sv");
      return null;
    }
    StateVertex fromSV = (StateVertex) fromPort;
    StateVertex toSV = (StateVertex) toPort;

    if (fromSV instanceof Pseudostate)
      if (PseudostateKind.FINAL.equals(((Pseudostate)fromSV).getKind()))
	return null;
    if (toSV instanceof Pseudostate)
      if (PseudostateKind.INITIAL.equals(((Pseudostate)toSV).getKind()))
	return null;

    if (edgeClass == Transition.class) {
      Transition tr = new Transition(fromSV, toSV);
      // the constructor adds the edge to the SV's incoming and
      // outgoing vectors
      addEdge(tr);
      return tr;
    }
    else {
      System.out.println("wrong kind of edge in StateDiagram connect3");
      return null;
    }
      //}
//     catch (java.beans.PropertyVetoException ex) { }
//     System.out.println("should not enter here! StateDiagram connect3");
//     return null;
  }


  ////////////////////////////////////////////////////////////////
  // VetoableChangeListener implementation

  public void vetoableChange(PropertyChangeEvent pce) {
    //throws PropertyVetoException

    if ("ownedElement".equals(pce.getPropertyName())) {
      Vector oldOwned = (Vector) pce.getOldValue();
      ElementOwnership eo = (ElementOwnership) pce.getNewValue();
      ModelElement me = eo.getModelElement();
      if (oldOwned.contains(eo)) {
	//System.out.println("model removed " + me);
	if (me instanceof State) removeNode(me);
	if (me instanceof Pseudostate) removeNode(me);
	if (me instanceof Transition) removeEdge(me);
      }
      else {
	// System.out.println("model added " + me);
      }
    }
  }

  static final long serialVersionUID = -8056507319026044174L;

} /* end class StateDiagramGraphModel */

