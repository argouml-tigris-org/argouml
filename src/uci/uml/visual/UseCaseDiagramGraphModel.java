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
import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.model_management.*;

import uci.uml.util.MMUtil;


/** This class defines a bridge between the UML meta-model
 *  representation of the design and the GraphModel interface used by
 *  GEF.  This class handles only UML Use Case Digrams.  */

public class UseCaseDiagramGraphModel extends MutableGraphSupport
implements MutableGraphModel, VetoableChangeListener, MElementListener {
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Vector _nodes = new Vector();
  protected Vector _edges = new Vector();

  /** The "home" UML model of this diagram, not all ModelElements in this
   *  graph are in the home model, but if they are added and don't
   *  already have a model, they are placed in the "home model".
   *  Also, elements from other models will have their FigNodes add a
   *  line to say what their model is. */

  protected MNamespace _model;

  ////////////////////////////////////////////////////////////////
  // accessors

  public MNamespace getNamespace() { return _model; }
  public void setNamespace(MNamespace m) {
    if (_model != null) _model.removeMElementListener(this);
    _model = m;
    if (_model != null) _model.addMElementListener(this);
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
    if (nodeOrEdge instanceof MActor) res.addElement(nodeOrEdge);
    if (nodeOrEdge instanceof MUseCase) res.addElement(nodeOrEdge);
    return res;
  }

  /** Return the node or edge that owns the given port */
  public Object getOwner(Object port) {
    return port;
  }

  /** Return all edges going to given port */
  public Vector getInEdges(Object port) {
    Vector res = new Vector(); //wasteful!
    if (port instanceof MActor) {
      MActor act = (MActor) port;
      Vector ends = new Vector(act.getAssociationEnds());
      if (ends == null) return res; // empty Vector
      java.util.Enumeration endEnum = ends.elements();
      while (endEnum.hasMoreElements()) {
	MAssociationEnd ae = (MAssociationEnd) endEnum.nextElement();
	res.addElement(ae.getAssociation());
      }
    }
    if (port instanceof MUseCase) {
      MUseCase use = (MUseCase) port;
      Vector ends = new Vector(use.getAssociationEnds());
      if (ends == null) return res; // empty Vector
      java.util.Enumeration endEnum = ends.elements();
      while (endEnum.hasMoreElements()) {
	MAssociationEnd ae = (MAssociationEnd) endEnum.nextElement();
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
    if (edge instanceof MAssociation) {
      MAssociation assoc = (MAssociation) edge;
      Vector conns = new Vector(assoc.getConnections());
      return conns.elementAt(0);
    }
    System.out.println("needs-more-work getSourcePort");
    return null;
  }

  /** Return  the other end of an edge */
  public Object getDestPort(Object edge) {
    if (edge instanceof MAssociation) {
      MAssociation assoc = (MAssociation) edge;
      Vector conns = new Vector(assoc.getConnections());
      return conns.elementAt(1);
    }
    System.out.println("needs-more-work getDestPort");
    return null;
  }


  ////////////////////////////////////////////////////////////////
  // MutableGraphModel implementation

  /** Return true if the given object is a valid node in this graph */
  public boolean canAddNode(Object node) {
    return (node instanceof MActor) || (node instanceof MUseCase);
  }

  /** Return true if the given object is a valid edge in this graph */
  public boolean canAddEdge(Object edge)  {
    return (edge instanceof MAssociation) || (edge instanceof MGeneralization);
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
      if (node instanceof MClassifier) {
	_model.addOwnedElement((MClassifier) node);
      }
    fireNodeAdded(node);
  }

  /** Add the given edge to the graph, if valid. */
  public void addEdge(Object edge) {
    //System.out.println("adding class edge!!!!!!");
    if (_edges.contains(edge)) return;
    _edges.addElement(edge);
    // needs-more-work: assumes public
      if (edge instanceof MGeneralization) {
	_model.addOwnedElement((MGeneralization) edge);
      }
      if (edge instanceof MAssociation) {
	_model.addOwnedElement((MAssociation) edge);
      }
    fireEdgeAdded(edge);
  }

  public void addNodeRelatedEdges(Object node) { }


  /** Remove the given edge from the graph. */
  public void removeEdge(Object edge) {
    if (!_edges.contains(edge)) return;
    _edges.removeElement(edge);
    fireEdgeRemoved(edge);
  }

  /** Return true if the two given ports can be connected by a 
   * kind of edge to be determined by the ports. */
  public boolean canConnect(Object fromP, Object toP) {
    if ((fromP instanceof MActor) && (toP instanceof MActor)) return false;
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

      if (edgeClass == MAssociationImpl.class) {
	  MClassifier fromCls = (MClassifier)fromPort;
	  MClassifier toCls = (MClassifier)toPort;
	  MAssociation asc = MMUtil.SINGLETON.buildAssociation(fromCls, toCls);
	  addEdge(asc);
	  return asc;
      }
      else if (edgeClass == MGeneralizationImpl.class &&
	       ((fromPort instanceof MActor && toPort instanceof MActor) ||
		(fromPort instanceof MUseCase && toPort instanceof MUseCase))) {
	  MClassifier parent = (MClassifier)fromPort;
	  MClassifier child = (MClassifier)toPort;
	  MGeneralization gen = MMUtil.SINGLETON.buildGeneralization(parent,child);
	  addEdge(gen);
	  return gen;
      }
      else {
		  System.out.println("Incorrect edge");
		  return null;
      }
  }

	public void propertySet(MElementEvent mee) {
	}
	public void listRoleItemSet(MElementEvent mee) {
	}
	public void recovered(MElementEvent mee) {
	}
	public void removed(MElementEvent mee) {
	}
	public void roleAdded(MElementEvent mee) {
	}
	public void roleRemoved(MElementEvent mee) {
	}


  ////////////////////////////////////////////////////////////////
  // VetoableChangeListener implementation

  public void vetoableChange(PropertyChangeEvent pce) {
    //throws PropertyVetoException

    if ("ownedElement".equals(pce.getPropertyName())) {
      Vector oldOwned = (Vector) pce.getOldValue();
      MElementImport eo = (MElementImport) pce.getNewValue();
      MModelElement me = eo.getModelElement();
      if (oldOwned.contains(eo)) {
	//System.out.println("model removed " + me);
	if (me instanceof MClassifier) removeNode(me);
	if (me instanceof MAssociation) removeEdge(me);
	if (me instanceof MGeneralization) removeEdge(me);
      }
      else {
	//System.out.println("model added " + me);
      }
    }
  }

  static final long serialVersionUID = -8516841965639203796L;

} /* end class UseCaseDiagramGraphModel */

