// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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


package org.argouml.uml.diagram.static_structure;

import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Category;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.foundation.core.CoreHelper;

import org.argouml.uml.diagram.UMLMutableGraphSupport;


import ru.novosoft.uml.behavior.common_behavior.MLinkEnd;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;

import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MGeneralization;

import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MRelationship;

import ru.novosoft.uml.model_management.MElementImport;



/** This class defines a bridge between the UML meta-model
 *  representation of the design and the GraphModel interface used by
 *  GEF.  This class handles only UML Class digrams.  */

public class ClassDiagramGraphModel extends UMLMutableGraphSupport
    implements VetoableChangeListener 
{
    protected static Category cat =
	Category.getInstance(ClassDiagramGraphModel.class);
    ////////////////////////////////////////////////////////////////
    // instance variables

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
	_model = m;
    }

    ////////////////////////////////////////////////////////////////
    // GraphModel implementation


    /** Return all ports on node or edge */
    public Vector getPorts(Object nodeOrEdge) {
	Vector res = new Vector();  //wasteful!
	if (ModelFacade.isAClass(nodeOrEdge)) res.addElement(nodeOrEdge);
	if (ModelFacade.isAInterface(nodeOrEdge)) res.addElement(nodeOrEdge);
	if (ModelFacade.isAInstance(nodeOrEdge)) res.addElement(nodeOrEdge);
	if (ModelFacade.isAModel(nodeOrEdge)) res.addElement(nodeOrEdge);
	return res;
    }

    /** Return the node or edge that owns the given port */
    public Object getOwner(Object port) {
	return port;
    }

    /** Return all edges going to given port (read Model Element).
     *
     * Instances can't currently be added to a class diagram.
     */
    public Vector getInEdges(Object port) {
      
	Vector edges = new Vector();
      
	// top of the hierarchy is ME:
	if (ModelFacade.isAModelElement(port)) {
	    Iterator it = ModelFacade.getSupplierDependencies(port).iterator();
	    while (it.hasNext()) {
		edges.add(it.next());
	    }
	}
	// then Generalizable Element
	if (ModelFacade.isAGeneralizableElement(port)) {
	    Iterator it = ModelFacade.getSpecializations(port).iterator();
	    while (it.hasNext()) {
		edges.add(it.next());
	    }
	}
	// then Classifier
	if (ModelFacade.isAClassifier(port)) {
	    Iterator it = ModelFacade.getAssociationEnds(port).iterator();
	    while (it.hasNext()) {
		Object nextAssocEnd = it.next();
		// navigable.... only want incoming
		if (ModelFacade.isNavigable(nextAssocEnd)) {
		    edges.add(nextAssocEnd);
		}
	    }
	}
      
	return edges;
      
	//    Vector res = new Vector(); //wasteful!
	//    if (port instanceof MClass) {
	//      MClass cls = (MClass) port;
	//      Collection ends = cls.getAssociationEnds();
	//      if (ends == null) return res; // empty Vector
	//      //java.util.Enumeration endEnum = ends.elements();
	//      Iterator iter = ends.iterator();
	//      while (iter.hasNext()) {
	//          MAssociationEnd ae = (MAssociationEnd) iter.next();
	//          res.add(ae.getAssociation());
	//      }
	//    }
	//    if (port instanceof MInterface) {
	//      MInterface Intf = (MInterface) port;
	//      Collection ends = Intf.getAssociationEnds();
	//      if (ends == null) return res; // empty Vector
	//      Iterator endEnum = ends.iterator();
	//      while (endEnum.hasNext()) {
	//        MAssociationEnd ae = (MAssociationEnd) endEnum.next();
	//        res.addElement(ae.getAssociation());
	//      }
	//    }
	//    if (port instanceof MPackage) {
	//      MPackage cls = (MPackage) port;
	//      Vector ends = cls.getAssociationEnd();
	//      if (ends == null) return res; // empty Vector
	//      java.util.Enumeration endEnum = ends.elements();
	//      while (endEnum.hasMoreElements()) {
	//        MAssociationEnd ae = (MAssociationEnd) endEnum.nextElement();
	//        res.addElement(ae.getAssociation());
	//      }
	//    }
	//    if (port instanceof MInstance) {
	//      MInstance inst = (MInstance) port;
	//      Collection ends = inst.getLinkEnds();
	//      res.addAll(ends);
	//    }
	//    return res;
    }

    /** Return all edges going from given port (model element)
     */
    public Vector getOutEdges(Object port) {

	Vector edges = new Vector();
      
	// top of the hierarchy is ME:
	if (ModelFacade.isAModelElement(port)) {
	    Iterator it = ModelFacade.getClientDependencies(port).iterator();
	    while (it.hasNext()) {
		edges.add(it.next());
	    }
	}
	// then Generalizable Element
	if (ModelFacade.isAGeneralizableElement(port)) {
	    Iterator it = ModelFacade.getGeneralizations(port).iterator();
	    while (it.hasNext()) {
		edges.add(it.next());
	    }
	}
	// then Classifier
	if (ModelFacade.isAClassifier(port)) {
	    Iterator it = ModelFacade.getAssociationEnds(port).iterator();
	    while (it.hasNext()) {
		Object nextAssocEnd = ModelFacade.getOppositeEnd(it.next());
		// navigable.... only want outgoing
		if (ModelFacade.isNavigable(nextAssocEnd)) {
		    edges.add(nextAssocEnd);
		}
	    }
	}
      
	return edges;
    }

    /** Return one end of an edge */
    public Object getSourcePort(Object edge) {
	if (ModelFacade.isARelationship(edge)) {
	    return CoreHelper.getHelper().getSource((MRelationship) edge);
	}
	cat.debug("TODO getSourcePort");
	return null;
    }

    /** Return  the other end of an edge */
    public Object getDestPort(Object edge) {
	if (ModelFacade.isARelationship(edge)) {
	    return CoreHelper.getHelper().getDestination((MRelationship) edge);
	}
	cat.debug("TODO getSourcePort");
	return null;
    }


    ////////////////////////////////////////////////////////////////
    // MutableGraphModel implementation

    /** Return true if the given object is a valid node in this graph */
    public boolean canAddNode(Object node) {
	if (_nodes.contains(node)) return false;
        // TODO This logic may well be worth moving into the model component.
        // Provide a similar grid to the connectionsGrid
	return (ModelFacade.isAClass(node)) ||
               (ModelFacade.isAInterface(node)) ||
               (ModelFacade.isAModel(node)) ||
               (ModelFacade.isAPackage(node)) ||
               (ModelFacade.isAInstance(node));
    }

    /** Return true if the given object is a valid edge in this graph */
    public boolean canAddEdge(Object edge)  {
	if (edge == null) return false;
	if (_edges.contains(edge)) return false;
	Object end0 = null, end1 = null;
	if (ModelFacade.isAAssociation(edge)) {
	    Collection conns = ModelFacade.getConnections(edge);
	    Iterator iter = conns.iterator();
	    if (conns.size() < 2) return false;
	    Object associationEnd0 = iter.next();
	    Object associationEnd1 = iter.next();
	    if (associationEnd0 == null || associationEnd1 == null) return false;
	    end0 = ModelFacade.getType(associationEnd0);
	    end1 = ModelFacade.getType(associationEnd1);
	} else if (ModelFacade.isAGeneralization(edge)) {
	    end0 = ModelFacade.getChild(edge);
	    end1 = ModelFacade.getParent(edge);
	} else if (ModelFacade.isADependency(edge)) {
	    Collection clients = ModelFacade.getClients(edge);
	    Collection suppliers = ModelFacade.getSuppliers(edge);
	    if (clients == null || suppliers == null) return false;
	    end0 = ((Object[]) clients.toArray())[0];
	    end1 = ((Object[]) suppliers.toArray())[0];
	} else if (ModelFacade.isALink(edge)) {
	    Collection roles = ModelFacade.getConnections(edge);
	    Object le0 = ((Object[]) roles.toArray())[0];
	    Object le1 = ((Object[]) roles.toArray())[0]; // TODO should be 1????
	    if (le0 == null || le1 == null) return false;
	    end0 = ((MLinkEnd)le0).getInstance();
	    end1 = ((MLinkEnd)le1).getInstance();
	}
	if (end0 == null || end1 == null) return false;
	if (!_nodes.contains(end0)) return false;
	if (!_nodes.contains(end1)) return false;
	return true;
    }


    /** Add the given node to the graph, if valid. */
    public void addNode(Object node) {
	cat.debug("adding class node!!");
	if (!canAddNode(node)) return;
	_nodes.addElement(node);
	if (ModelFacade.isAModelElement(node) &&
	    ModelFacade.getNamespace(node) == null) {
	    _model.addOwnedElement((MModelElement) node);
	}

	fireNodeAdded(node);
	cat.debug("adding " + node + " OK");
    }

    /** Add the given edge to the graph, if valid. */
    public void addEdge(Object edge) {
        cat.debug("adding class edge!!!!!!");
        if (!canAddEdge(edge)) return;
        _edges.addElement(edge);
        // TODO: assumes public
        if (ModelFacade.isAModelElement(edge) &&
                ModelFacade.getNamespace(edge) == null) {
	    ModelFacade.addOwnedElement(_model, edge);
        }
        fireEdgeAdded(edge);
    }

    /**
     * Adds the edges from the given node. For example, this method lets you add
     * an allready existing massociation between two figclassifiers.
     * @see org.tigris.gef.graph.MutableGraphModel#addNodeRelatedEdges(Object)
     */
    public void addNodeRelatedEdges(Object node) {
	if (ModelFacade.isAClassifier(node) ) {
	    Collection ends = ModelFacade.getAssociationEnds(node);
	    Iterator iter = ends.iterator();
	    while (iter.hasNext()) {
		Object associationEnd = iter.next();
		if (canAddEdge(ModelFacade.getAssociation(associationEnd))) {
		    addEdge(ModelFacade.getAssociation(associationEnd));
		    // return;
		}
	    }
	}
	if (ModelFacade.isAGeneralizableElement(node) ) {
	    Collection generalizations = ModelFacade.getGeneralizations(node);
	    Iterator iter = generalizations.iterator();
	    while (iter.hasNext()) {
		Object generalization = iter.next();
		if (canAddEdge(generalization)) {
		    addEdge(generalization);
		    // return;
		}
	    }
	    Collection specializations = ModelFacade.getSpecializations(node);
	    iter = specializations.iterator();
	    while (iter.hasNext()) {
		Object specialization = iter.next();
		if (canAddEdge(specialization)) {
		    addEdge(specialization);
		    // return;
		}
	    }
	}
	if (ModelFacade.isAModelElement(node) ) {
	    Vector specs =
		new Vector(ModelFacade.getClientDependencies(node));
	    specs.addAll(ModelFacade.getSupplierDependencies(node));
	    Iterator iter = specs.iterator();
	    while (iter.hasNext()) {
		Object dependency = iter.next();
		if (canAddEdge(dependency)) {
		    addEdge(dependency);
		    // return;
		}
	    }
	}
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
		cat.debug("model removed " + me);
		if (ModelFacade.isAClassifier(me)) removeNode(me);
		if (ModelFacade.isAPackage(me)) removeNode(me);
		if (ModelFacade.isAAssociation(me)) removeEdge(me);
		if (ModelFacade.isADependency(me)) removeEdge(me);
		if (ModelFacade.isAGeneralization(me)) removeEdge(me);
	    }
	    else {
		cat.debug("model added " + me);
	    }
	}
    }


    static final long serialVersionUID = -2638688086415040146L;


    /**
     * When rerouting an edge, this is the first method to
     * be called by SelectionRerouteEdge, in order to determine
     * whether the graphmodel will allow the change.
     *
     * <p>restricted to class<->association changes for now.
     *
     * @param newNode this is the new node that one of the ends is dragged to.
     * @param oldNode this is the existing node that is already connected.
     * @param edge this is the edge that is being dragged/rerouted
     *
     * @return whether or not the rerouting is allowed
     */
    public boolean canChangeConnectedNode(Object newNode, Object oldNode,
					  Object edge)
    {

	// prevent no changes...
	if ( newNode == oldNode)
	    return false;

	// check parameter types:
	if ( !(ModelFacade.isAClass(newNode) ||
	       ModelFacade.isAClass(oldNode) ||
	       ModelFacade.isAAssociation(edge) ) )
	{
	    return false;
	}

	return true;
    }

    /**
     * Reroutes the connection to the old node to be connected to
     * the new node.
     *
     * delegates to rerouteXXX(,,,) for each of the 4 possible edges in
     * a class diagram: Association, Dependency, Generalization, Link.
     *
     * @param newNode this is the new node that one of the ends is dragged to.
     * @param oldNode this is the existing node that is already connected.
     * @param edge this is the edge that is being dragged/rerouted
     * @param isSource tells us which end is being rerouted.
     */
    public void changeConnectedNode(Object newNode, Object oldNode,
				    Object edge, boolean isSource)
    {
	if (ModelFacade.isAAssociation(edge))
	    rerouteAssociation(newNode,  oldNode,  edge,  isSource);
	else if (ModelFacade.isAGeneralization(edge))
	    rerouteGeneralization(newNode,  oldNode,  edge,  isSource);
	else if (ModelFacade.isADependency(edge))
	    rerouteDependency(newNode,  oldNode,  edge,  isSource);
	else if (ModelFacade.isALink(edge))
	    rerouteLink(newNode,  oldNode,  edge,  isSource);
    }

    /**
     * helper method for changeConnectedNode
     */
    private void rerouteAssociation(Object newNode, Object oldNode,
				    Object edge, boolean isSource)
    {
	// check param types: only some connections are legal uml connections:

	if (!(ModelFacade.isAClassifier(newNode))
	    || !(ModelFacade.isAClassifier(oldNode)))
	    return;

	// can't have a connection between 2 interfaces:
	// get the 'other' end type
	MModelElement otherNode = null;

	if (isSource) {
	    otherNode =
		CoreHelper.getHelper().getDestination((MRelationship) edge);
	}
	else {
	    otherNode =
		CoreHelper.getHelper().getSource((MRelationship) edge);
	}

	if ((ModelFacade.isAInterface(newNode)) &&
	    (ModelFacade.isAInterface(otherNode)) )
	    return;

        // cast the params
	MAssociation edgeAssoc = (MAssociation) edge;

	Object theEnd = null;
	Object theOtherEnd = null;
        // rerouting the source:
        if (isSource) {
            theEnd =
		((Object[])(edgeAssoc.getConnections()).toArray())[0];

            theOtherEnd =
		((Object[])(edgeAssoc.getConnections()).toArray())[1];
        }
        // rerouting the destination:
        else {
            theOtherEnd =
		((Object[])(edgeAssoc.getConnections()).toArray())[0];

            theEnd =
		((Object[])(edgeAssoc.getConnections()).toArray())[1];
        }

        // set the ends navigability see also Class ActionNavigability
        if (ModelFacade.isAInterface(newNode)) {
            ModelFacade.setNavigable(theEnd, true);
            ModelFacade.setNavigable(theOtherEnd, false);
        }

        if (ModelFacade.isAInterface(otherNode)) {
            ModelFacade.setNavigable(theOtherEnd, true);
            ModelFacade.setNavigable(theEnd, false);
        }

        //set the new end type!
        ModelFacade.setType(theEnd, newNode);
    }

    /**
     * helper method for changeConnectedNode
     * <p>empty at the moment
     */
    private void rerouteGeneralization(Object newNode, Object oldNode,
				       Object edge, boolean isSource) {
      
    }

    /**
     * helper method for changeConnectedNode
     * <p>empty at the moment
     */
    private void rerouteDependency(Object newNode, Object oldNode,
				   Object edge, boolean isSource) {

    }

    /**
     * helper method for changeConnectedNode
     * <p>empty at the moment
     */
    private void rerouteLink(Object newNode, Object oldNode,
			     Object edge, boolean isSource) {

    }

} /* end class ClassDiagramGraphModel */