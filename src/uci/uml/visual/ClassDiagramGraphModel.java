package uci.uml.visual;

import java.util.*;
import java.beans.*;

import uci.graph.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;


public class ClassDiagramGraphModel extends MutableGraphSupport
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
   
  protected Model _model;

  ////////////////////////////////////////////////////////////////
  // accessors

  public Model getModel() { return _model; }
  public void setModel(Model m) {
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
    if (isUMLClass(nodeOrEdge))
      res.addElement(nodeOrEdge);
    if (isUMLInterface(nodeOrEdge))
      res.addElement(nodeOrEdge);
    return res;
  }

  /** Return the node or edge that owns the given port */
  public Object getOwner(Object port) {
    return port;
  }

  /** Return all edges going to given port */
  public Vector getInEdges(Object port) {
    Vector res = new Vector(); //wasteful!
    if (isUMLClass(port)) {
      uci.uml.Foundation.Core.Class cls = (uci.uml.Foundation.Core.Class) port;
      Vector ends = cls.getAssociationEnd();
      if (ends == null) return res; // empty Vector 
      java.util.Enumeration endEnum = ends.elements();
      while (endEnum.hasMoreElements()) {
	AssociationEnd ae = (AssociationEnd) endEnum.nextElement();
	res.addElement(ae.getAssociation());
      }
    }
    if (isUMLInterface(port)) {
      // needs-more-work
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
  public boolean canAddNode(Object node) { return true; }

  /** Return true if the given object is a valid edge in this graph */
  public boolean canAddEdge(Object edge)  { return true; }

  /** Remove the given node from the graph. */
  public void removeNode(Object node) {
    if (!_nodes.contains(node)) return;
    _nodes.removeElement(node);
    fireNodeRemoved(node);
  }

  /** Add the given node to the graph, if valid. */
  public void addNode(Object node) {
    System.out.println("adding class node!!");
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
    System.out.println("adding class edge!!!!!!");
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
  public boolean canConnect(Object fromP, Object toP) { return true; }


  /** Contruct and add a new edge of a kind determined by the ports */
  public Object connect(Object fromPort, Object toPort) {
    System.out.println("should not enter here! connect2");
    return null;
  }

  /** Contruct and add a new edge of the given kind */
  public Object connect(Object fromPort, Object toPort,
			java.lang.Class edgeClass) {
    try {
    if (isUMLClass(fromPort) && isUMLClass(toPort)) {
      uci.uml.Foundation.Core.Class fromCls =
	(uci.uml.Foundation.Core.Class) fromPort;
      uci.uml.Foundation.Core.Class toCls =
	(uci.uml.Foundation.Core.Class) toPort;

      if (edgeClass == Generalization.class) {
	Generalization gen = new Generalization(fromCls, toCls);
	addEdge(gen);
	return gen;
      }
      else if (edgeClass == Association.class) {
	Association asc = new Association(fromCls, toCls);
	addEdge(asc);
	return asc;
      }
      else {
	System.out.println("asdwwads");
	return null;
      }
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
	System.out.println("model removed " + me);
	if (me instanceof Classifier) removeNode(me);
	if (me instanceof Association) removeEdge(me);
	if (me instanceof Generalization) removeEdge(me);
	//if (me instanceof Realization) removeEdge(me);
      }
      else {
	System.out.println("model added " + me);
      }
    }
  }


  ////////////////////////////////////////////////////////////////
  // utility methods

  protected boolean isUMLClass(Object o) {
    return (o instanceof uci.uml.Foundation.Core.Class);
  }

  protected boolean isUMLInterface(Object o) {
    return (o instanceof uci.uml.Foundation.Core.Interface);
  }

  
} /* end class ClassDiagramGraphModel */


//   protected Model _model;
  
//   ////////////////////////////////////////////////////////////////
//   // contructors

//   public ClassDiagramGraphModel(Model m) {
//     _model = m;
//   }
