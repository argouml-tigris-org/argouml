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


// File: CollabDiagramGraphModel.java
// Classes: CollabDiagramGraphModel
// Original Author: agauthie@ics.uci.edu
// $Id$

package uci.uml.visual;

import com.sun.java.util.collections.*;
import java.beans.*;

import uci.graph.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.model_management.*;


/** This class defines a bridge between the UML meta-model
 *  representation of the design and the GraphModel interface used by
 *  GEF.  This class handles only UML Use Case Digrams.  */

public class CollabDiagramGraphModel extends MutableGraphSupport
implements MutableGraphModel, MElementListener, VetoableChangeListener {
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Vector _nodes = new Vector();
  protected Vector _edges = new Vector();

  /** The "home" UML model of this diagram, not all ModelElements in this
   *  graph are in the home model, but if they are added and don't
   *  already have a model, they are placed in the "home model".
   *  Also, elements from other models will have their FigNodes add a
   *  line to say what their model is. */

  /** The collaboration / interaction we are diagramming */
	protected MCollaboration _collab;
	protected MInteraction _interaction;

  ////////////////////////////////////////////////////////////////
  // accessors

  public MNamespace getNamespace() { return _collab; }
  public void setNamespace(MNamespace m) {
    if (!(m instanceof MCollaboration)) {
      System.out.println("invalid namespace for CollabDiagramGraphModel");
      return;
    }
    if (_collab != null) _collab.removeMElementListener(this);
    _collab = (MCollaboration) m;
    if (_collab != null) _collab.addMElementListener(this);
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
    if (nodeOrEdge instanceof MClassifierRole) res.addElement(nodeOrEdge);
    return res;
  }

  /** Return the node or edge that owns the given port */
  public Object getOwner(Object port) {
    return port;
  }

  /** Return all edges going to given port */
  public Vector getInEdges(Object port) {
    Vector res = new Vector(); //wasteful!
    if (port instanceof MClassifierRole) {
      MClassifierRole cr = (MClassifierRole) port;
      Collection ends = cr.getAssociationEnds();
      if (ends == null) return res; // empty Vector
	  Iterator iter = ends.iterator();
      while (iter.hasNext()) {
	    MAssociationEndRole aer = (MAssociationEndRole) iter.next();
	    res.addElement(aer.getAssociation());
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
    if (edge instanceof MAssociationRole ) {
      MAssociationRole assoc = (MAssociationRole) edge;
      Collection conns = assoc.getConnections();
      return ((Object[])conns.toArray())[0];
    }
    System.out.println("needs-more-work getSourcePort");
    return null;
  }

  /** Return  the other end of an edge */
  public Object getDestPort(Object edge) {
    if (edge instanceof MAssociation) {
      MAssociationRole assoc = (MAssociationRole) edge;
      Collection conns = assoc.getConnections();
      return ((Object[])conns.toArray())[1];
    }
    System.out.println("needs-more-work getDestPort");
    return null;
  }


  ////////////////////////////////////////////////////////////////
  // MutableGraphModel implementation

  /** Return true if the given object is a valid node in this graph */
  public boolean canAddNode(Object node) {
    return (node instanceof MClassifierRole || node instanceof MMessage);
  }

  /** Return true if the given object is a valid edge in this graph */
  public boolean canAddEdge(Object edge)  {
    return (edge instanceof MAssociationRole);
  }

  /** Remove the given node from the graph. */
  public void removeNode(Object node) {
    if (!_nodes.contains(node)) return;
    _nodes.removeElement(node);
    fireNodeRemoved(node);
  }

  /** Add the given node to the graph, if valid. */
  public void addNode(Object node) {
    //System.out.println("adding MClassifierRole node!!");
    if (_nodes.contains(node)) return;
    _nodes.addElement(node);
    // needs-more-work: assumes public, user pref for default visibility?
      if (node instanceof MClassifier) {
		  _collab.addOwnedElement((MClassifier) node);
		  // ((MClassifier)node).setNamespace(_collab.getNamespace());
      }
    
    fireNodeAdded(node);
  }

  /** Add the given edge to the graph, if valid. */
  public void addEdge(Object edge) {
    //System.out.println("adding class edge!!!!!!");
    if (_edges.contains(edge)) return;
    _edges.addElement(edge);
    // needs-more-work: assumes public
      if (edge instanceof MAssociation) {
		  _collab.addOwnedElement((MAssociation) edge);
		  // ((MAssociation)edge).setNamespace(_collab.getNamespace());
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
    if ((fromP instanceof MClassifierRole) && (toP instanceof MClassifierRole)) return true;
    return false;
  }


  /** Contruct and add a new edge of a kind determined by the ports */
  public Object connect(Object fromPort, Object toPort) {
    System.out.println("should not enter here! connect2");
    return null;
  }

  /** Contruct and add a new edge of the given kind */
  public Object connect(Object fromPort, Object toPort,
			java.lang.Class edgeClass) {
    //try {
      if (edgeClass == MAssociationRoleImpl.class &&
		(fromPort instanceof MClassifierRole && toPort instanceof MClassifierRole)) {
	    MAssociationRole asr = new MAssociationRoleImpl();
		MAssociationEndRole aer0 = new MAssociationEndRoleImpl();
		aer0.setType((MClassifierRole) fromPort);
		MAssociationEndRole aer1 = new MAssociationEndRoleImpl();
		aer1.setType((MClassifierRole) toPort);
		asr.addConnection(aer0);
		asr.addConnection(aer1);
	    addEdge(asr);
	    return asr;
      }
      /*else if (edgeClass == MGeneralization.class &&
		((fromPort instanceof MActor && toPort instanceof MActor) ||
		 (fromPort instanceof MUseCase && toPort instanceof MUseCase))) {
	    MGeneralization gen = new MGeneralization((MClassifier) fromPort,
						(MClassifier) toPort);
	    gen.addStereotype(MStereotype.EXTENDS);
	    addEdge(gen);
	    return gen;
      }*/
      else {
	    System.out.println("Incorrect edge");
	    return null;
      }
    //}
    //catch (java.beans.PropertyVetoException ex) { }
    //System.out.println("should not enter here! connect3");
    //return null;
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
	    if (me instanceof MMessage) removeNode(me);
	    if (me instanceof MAssociation) removeEdge(me);
      }
      else {
	    //System.out.println("model added " + me);
      }
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
} /* end class CollabDiagramGraphModel */

