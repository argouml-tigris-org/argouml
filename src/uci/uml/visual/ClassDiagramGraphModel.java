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



// File: ClassDiagramGraphModel.java
// Classes: ClassDiagramGraphModel
// Original Author: jrobbins@ics.uci.edu
// $Id$


package uci.uml.visual;

import java.util.*;
import java.beans.*;

import uci.graph.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;


/** This class defines a bridge between the UML meta-model
 *  representation of the design and the GraphModel interface used by
 *  GEF.  This class handles only UML Class digrams.  */

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
    if (nodeOrEdge instanceof MMClass) res.addElement(nodeOrEdge);
    if (nodeOrEdge instanceof Interface) res.addElement(nodeOrEdge);
    if (nodeOrEdge instanceof Instance) res.addElement(nodeOrEdge);
    if (nodeOrEdge instanceof Model) res.addElement(nodeOrEdge);
    return res;
  }

  /** Return the node or edge that owns the given port */
  public Object getOwner(Object port) {
    return port;
  }

  /** Return all edges going to given port */
  public Vector getInEdges(Object port) {
    Vector res = new Vector(); //wasteful!
    if (port instanceof MMClass) {
      MMClass cls = (MMClass) port;
      Vector ends = cls.getAssociationEnd();
      if (ends == null) return res; // empty Vector 
      java.util.Enumeration endEnum = ends.elements();
      while (endEnum.hasMoreElements()) {
	AssociationEnd ae = (AssociationEnd) endEnum.nextElement();
	res.addElement(ae.getAssociation());
      }
    }
    if (port instanceof Interface) {
      Interface Intf = (Interface) port;
      Vector ends = Intf.getAssociationEnd();
      if (ends == null) return res; // empty Vector 
      java.util.Enumeration endEnum = ends.elements();
      while (endEnum.hasMoreElements()) {
	    AssociationEnd ae = (AssociationEnd) endEnum.nextElement();
	    res.addElement(ae.getAssociation());
	  }
    }
    /*if (port instanceof MMPackage) {
      MMPackage cls = (MMPackage) port;
      Vector ends = cls.getAssociationEnd();
      if (ends == null) return res; // empty Vector
      java.util.Enumeration endEnum = ends.elements();
      while (endEnum.hasMoreElements()) {
	    AssociationEnd ae = (AssociationEnd) endEnum.nextElement();
	    res.addElement(ae.getAssociation());
      }
    }*/
    if (port instanceof Instance) {
      Instance inst = (Instance) port;
      Vector ends = inst.getLinkEnd();
      if (ends == null) return res; // empty Vector
      java.util.Enumeration endEnum = ends.elements();
      while (endEnum.hasMoreElements()) {
	    LinkEnd le = (LinkEnd) endEnum.nextElement();
	    res.addElement(le.getLink());
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
    return (node instanceof MMClass) || (node instanceof Interface)
    || (node instanceof Model) || (node instanceof Instance);
  }

  /** Return true if the given object is a valid edge in this graph */
  public boolean canAddEdge(Object edge)  {
    Object end0 = null, end1 = null;
    if (edge instanceof Association) {
      Vector conns = ((Association)edge).getConnection();
      AssociationEnd ae0 = (AssociationEnd) conns.elementAt(0);
      AssociationEnd ae1 = (AssociationEnd) conns.elementAt(1);
      if (ae0 == null || ae1 == null) return false;
      end0 = ae0.getType();
      end1 = ae1.getType();
    }
    else if (edge instanceof Generalization) {
      end0 = ((Generalization)edge).getSubtype();
      end1 = ((Generalization)edge).getSupertype();
    }
    else if (edge instanceof Dependency) {
      Vector clients = ((Dependency)edge).getClient();
      Vector suppliers = ((Dependency)edge).getSupplier();
      if (clients == null || suppliers == null) return false;
      end0 = clients.elementAt(0);
      end1 = suppliers.elementAt(0);
    }
    else if (edge instanceof Link) {
      Vector roles = ((Link)edge).getLinkRole();
      LinkEnd le0 = (LinkEnd) roles.elementAt(0);
      LinkEnd le1 = (LinkEnd) roles.elementAt(1);
      if (le0 == null || le1 == null) return false;
      end0 = le0.getInstance();
      end1 = le1.getInstance();
    }
    else if (edge instanceof Realization) {
      end0 = ((Generalization)edge).getSubtype();
      end1 = ((Generalization)edge).getSupertype();
    }
    if (end0 == null || end1 == null) return false;
    if (!_nodes.contains(end0)) return false;
    if (!_nodes.contains(end1)) return false;
    return true;
  }

  /** Remove the given node from the graph. */
  public void removeNode(Object node) {
    if (!_nodes.contains(node)) return;
    _nodes.removeElement(node);
    fireNodeRemoved(node);
  }

  /** Add the given node to the graph, if valid. */
  public void addNode(Object node) {
    //System.out.println("adding class node!!");
    if (_nodes.contains(node)) return;
    _nodes.addElement(node);
    // needs-more-work: assumes public, user pref for default visibility?
    try {
      if (node instanceof ModelElement &&
	  ((ModelElement)node).getElementOwnership() == null) {
	_model.addPublicOwnedElement((ModelElement) node);
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
      if (edge instanceof ModelElement) {
	_model.addPublicOwnedElement((ModelElement) edge);
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
      if ((fromPort instanceof MMClass) && (toPort instanceof MMClass)) {
	    MMClass fromCls = (MMClass) fromPort;
	    MMClass toCls = (MMClass) toPort;

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
	    else if (edgeClass == Dependency.class) {
	      Dependency dep = new Dependency(fromCls, toCls);
	      addEdge(dep);
	      return dep;
	    }
	    else {
	      System.out.println("Cannot make a "+ edgeClass.getName() +
			     " between a " + fromPort.getClass().getName() +
			     " and a " + toPort.getClass().getName());
	      return null;
	    }
      }
      else if ((fromPort instanceof Model) && (toPort instanceof Model)) {
    	Model fromPack = (Model) fromPort;
    	Model toPack = (Model) toPort;
        if (edgeClass == Dependency.class) {
          Dependency dep = new Dependency(fromPack, toPack);
          addEdge(dep);
          return dep;
        }
      }

      // break
      else if ((fromPort instanceof MMClass) && (toPort instanceof Interface)) {
	MMClass fromCls = (MMClass) fromPort;
	Interface toIntf = (Interface) toPort;

	if (edgeClass == Realization.class) {
	  Realization real = new Realization(fromCls, toIntf);
	  addEdge(real);
	  return real;
	}
	else if (edgeClass == Association.class) {
	  Association asc = new Association(fromCls, toIntf);
	  Vector conn = asc.getConnection();
	  AssociationEnd ae = (AssociationEnd) conn.elementAt(0);
	  try { ae.setIsNavigable(false); }
	  catch (PropertyVetoException pve) { }
	  addEdge(asc);
	  return asc;
	}
	else if (edgeClass == Dependency.class) {
	  Dependency dep = new Dependency(fromCls, toIntf);
	  addEdge(dep);
	  return dep;
	}
	else {
	  System.out.println("Cannot make a "+ edgeClass.getName() +
			     " between a " + fromPort.getClass().getName() +
			     " and a " + toPort.getClass().getName());
	  return null;
	}
      }

      // break
      else if ((fromPort instanceof Interface) && (toPort instanceof MMClass)) {
	Interface fromIntf = (Interface) fromPort;
	MMClass toCls = (MMClass) toPort;

	if (edgeClass == Association.class) {
	  Association asc = new Association(fromIntf, toCls);
	  Vector conn = asc.getConnection();
	  AssociationEnd ae = (AssociationEnd) conn.elementAt(1);
	  try { ae.setIsNavigable(false); }
	  catch (PropertyVetoException pve) { }
	  addEdge(asc);
	  return asc;
	}
	else if (edgeClass == Dependency.class) {
	  Dependency dep = new Dependency(fromIntf, toCls);
	  addEdge(dep);
	  return dep;
	}
	else {
	  System.out.println("Cannot make a "+ edgeClass.getName() +
			     " between a " + fromPort.getClass().getName() +
			     " and a " + toPort.getClass().getName());
	  return null;
	}
      }

      // break
      else if ((fromPort instanceof Interface) && (toPort instanceof Interface)) {
	Interface fromIntf = (Interface) fromPort;
	Interface toIntf = (Interface) toPort;

	if (edgeClass == Generalization.class) {
	  Generalization gen = new Generalization(fromIntf, toIntf);
	  addEdge(gen);
	  return gen;
	}
	else if (edgeClass == Dependency.class) {
	  Dependency dep = new Dependency(fromIntf, toIntf);
	  addEdge(dep);
	  return dep;
	}
	else {
	  System.out.println("Cannot make a "+ edgeClass.getName() +
			     " between a " + fromPort.getClass().getName() +
			     " and a " + toPort.getClass().getName());
	  return null;
	}
      }

      // break
      else if ((fromPort instanceof Instance) && (toPort instanceof Instance)) {
        Instance fromInst = (Instance) fromPort;
        Instance toInst = (Instance) toPort;
    	if (edgeClass == Link.class) {
    	  Link link = new Link(fromInst, toInst);
    	  addEdge(link);
    	  return link;
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
	//System.out.println("model removed " + me);
	if (me instanceof Classifier) removeNode(me);	
	if (me instanceof Model) removeNode(me);
	if (me instanceof Association) removeEdge(me);
	if (me instanceof Dependency) removeEdge(me);
	if (me instanceof Generalization) removeEdge(me);
	//if (me instanceof Realization) removeEdge(me);
      }
      else {
	//System.out.println("model added " + me);
      }
    }
  }

  static final long serialVersionUID = -2638688086415040146L;

} /* end class ClassDiagramGraphModel */

