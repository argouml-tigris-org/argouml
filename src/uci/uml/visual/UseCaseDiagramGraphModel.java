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


// File: UseCaseDiagramGraphModel.java
// Classes: UseCaseDiagramGraphModel
// Original Author: your email address here
// $Id$

package uci.uml.visual;

import java.util.*;
import java.beans.*;

import uci.graph.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;
import uci.uml.Model_Management.*;


/** This class defines a bridge between the UML meta-model
 *  representation of the design and the GraphModel interface used by
 *  GEF.  This class handles only UML Use Case Digrams.  */

public class UseCaseDiagramGraphModel extends MutableGraphSupport
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

  ////////////////////////////////////////////////////////////////
  // accessors

  public Namespace getNamespace() { return _model; }
  public void setNamespace(Namespace m) {
    if (_model != null) _model.removeVetoableChangeListener(this);
    _model = m;
    if (_model != null) _model.addVetoableChangeListener(this);
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
    if (nodeOrEdge instanceof Actor) res.addElement(nodeOrEdge);
    if (nodeOrEdge instanceof UseCase) res.addElement(nodeOrEdge);
    return res;
  }

  /** Return the node or edge that owns the given port */
  public Object getOwner(Object port) {
    return port;
  }

  /** Return all edges going to given port */
  public Vector getInEdges(Object port) {
    Vector res = new Vector(); //wasteful!
    if (port instanceof Actor) {
      Actor act = (Actor) port;
      Vector ends = act.getAssociationEnd();
      if (ends == null) return res; // empty Vector
      java.util.Enumeration endEnum = ends.elements();
      while (endEnum.hasMoreElements()) {
	AssociationEnd ae = (AssociationEnd) endEnum.nextElement();
	res.addElement(ae.getAssociation());
      }
    }
    if (port instanceof UseCase) {
      UseCase use = (UseCase) port;
      Vector ends = use.getAssociationEnd();
      if (ends == null) return res; // empty Vector
      java.util.Enumeration endEnum = ends.elements();
      while (endEnum.hasMoreElements()) {
	AssociationEnd ae = (AssociationEnd) endEnum.nextElement();
	res.addElement(ae.getAssociation());
      }
    }
    return res;
  }

  /** Return all edges going from given port */
  public Vector getOutEdges(Object port) {
    return new Vector(); // needs-more-work?
  }

  /** Return one end of an edge */
  public Object getSourcePort(Object edge) {
    if (edge instanceof IAssociation) {
      IAssociation assoc = (IAssociation) edge;
      Vector conns = assoc.getConnection();
      return conns.elementAt(0);
    }
    System.out.println("needs-more-work getSourcePort");
    return null;
  }

  /** Return  the other end of an edge */
  public Object getDestPort(Object edge) {
    if (edge instanceof IAssociation) {
      IAssociation assoc = (IAssociation) edge;
      Vector conns = assoc.getConnection();
      return conns.elementAt(1);
    }
    System.out.println("needs-more-work getDestPort");
    return null;
  }


  ////////////////////////////////////////////////////////////////
  // MutableGraphModel implementation

  /** Return true if the given object is a valid node in this graph */
  public boolean canAddNode(Object node) {
    return (node instanceof Actor) || (node instanceof UseCase);
  }

  /** Return true if the given object is a valid edge in this graph */
  public boolean canAddEdge(Object edge)  {
    return (edge instanceof Association) || (edge instanceof Generalization);
  }

  /** Remove the given node from the graph. */
  public void removeNode(Object node) {
    if (!_nodes.contains(node)) return;
    _nodes.removeElement(node);
    fireNodeRemoved(node);
  }

  /** Add the given node to the graph, if valid. */
  public void addNode(Object node) {
    //System.out.println("adding usecase node!!");
    if (_nodes.contains(node)) return;
    _nodes.addElement(node);
    // needs-more-work: assumes public, user pref for default visibility?
    try {
      if (node instanceof Classifier) {
	_model.addPublicOwnedElement((Classifier) node);
      }
    }
    catch (PropertyVetoException pve) {
      System.out.println("got a PropertyVetoException");
    }
    fireNodeAdded(node);
  }

  /** Add the given edge to the graph, if valid. */
  public void addEdge(Object edge) {
    //System.out.println("adding class edge!!!!!!");
    if (_edges.contains(edge)) return;
    _edges.addElement(edge);
    // needs-more-work: assumes public
    try {
      if (edge instanceof Generalization) {
	_model.addPublicOwnedElement((Generalization) edge);
      }
      if (edge instanceof Association) {
	_model.addPublicOwnedElement((Association) edge);
      }
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
  public boolean canConnect(Object fromP, Object toP) {
    if ((fromP instanceof Actor) && (toP instanceof Actor)) return false;
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
    try {
      if (edgeClass == Association.class) {
	Association asc = new Association((Classifier) fromPort,
					  (Classifier) toPort);
	addEdge(asc);
	return asc;
      }
      else if (edgeClass == Generalization.class &&
		((fromPort instanceof Actor && toPort instanceof Actor) ||
		 (fromPort instanceof UseCase && toPort instanceof UseCase))) {
	Generalization gen = new Generalization((Classifier) fromPort,
						(Classifier) toPort);
	gen.addStereotype(Stereotype.EXTENDS);
	addEdge(gen);
	return gen;
      }
      else {
	System.out.println("Incorrect edge");
	return null;
      }
    }
    catch (java.beans.PropertyVetoException ex) { }
    System.out.println("should not enter here! connect3");
    return null;
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
	if (me instanceof Classifier) removeNode(me);
	if (me instanceof Association) removeEdge(me);
	if (me instanceof Generalization) removeEdge(me);
      }
      else {
	//System.out.println("model added " + me);
      }
    }
  }

  static final long serialVersionUID = -8516841965639203796L;

} /* end class UseCaseDiagramGraphModel */

