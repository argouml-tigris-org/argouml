// $Id$
// Copyright (c) 2003-2005 The Regents of the University of California. All
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

package org.argouml.uml.diagram.deployment;

import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;

/**
 * This class defines a bridge between the UML meta-model
 * representation of the design and the GraphModel interface used by
 * GEF.<p>
 *
 * This class handles only UML Deployment Diagrams.<p>
 *
 */
public class DeploymentDiagramGraphModel extends UMLMutableGraphSupport
    implements VetoableChangeListener {
    /**
     * Logger.
     */
    private static final Logger LOG =
	    Logger.getLogger(DeploymentDiagramGraphModel.class);

    /** The "home" UML model of this diagram, not all ModelElements in this
     *  graph are in the home model, but if they are added and don't
     *  already have a model, they are placed in the "home model".
     *  Also, elements from other models will have their FigNodes add a
     *  line to say what their model is. */
    private Object model;

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * Get the homemodel.
     *
     * @see org.argouml.uml.diagram.UMLMutableGraphSupport#getNamespace()
     */
    public Object getNamespace() { return model; }

    /**
     * Set the homemodel.
     *
     * @param namespace the namespace
     */
    public void setNamespace(Object namespace) {

        if (!ModelFacade.isANamespace(namespace))
            throw new IllegalArgumentException();
	model = namespace;
    }

    ////////////////////////////////////////////////////////////////
    // GraphModel implementation


    /**
     * Return all ports on node or edge.
     *
     * @see org.tigris.gef.graph.GraphModel#getPorts(java.lang.Object)
     */
    public List getPorts(Object nodeOrEdge) {
        Vector res = new Vector();  //wasteful!
        if (ModelFacade.isANode(nodeOrEdge)) res.addElement(nodeOrEdge);
        if (ModelFacade.isANodeInstance(nodeOrEdge)) {
	    res.addElement(nodeOrEdge);
        }

	if (ModelFacade.isAComponent(nodeOrEdge)) res.addElement(nodeOrEdge);
	if (ModelFacade.isAComponentInstance(nodeOrEdge)) {
	    res.addElement(nodeOrEdge);
        }
        if (ModelFacade.isAClass(nodeOrEdge)) res.addElement(nodeOrEdge);
        if (ModelFacade.isAInterface(nodeOrEdge)) res.addElement(nodeOrEdge);
        if (ModelFacade.isAObject(nodeOrEdge)) res.addElement(nodeOrEdge);
        return res;
    }

    /** Return the node or edge that owns the given port
     *
     * @see org.tigris.gef.graph.BaseGraphModel#getOwner(java.lang.Object)
     */
    public Object getOwner(Object port) {
	return port;
    }


    /**
     * Return all edges going to given port.
     *
     * @see org.tigris.gef.graph.GraphModel#getInEdges(java.lang.Object)
     */
    public List getInEdges(Object port) {
	Vector res = new Vector(); //wasteful!
	if (ModelFacade.isANode(port)) {
	    Collection ends = ModelFacade.getAssociationEnds(port);
	    if (ends == null) return res; // empty Vector
	    Iterator iter = ends.iterator();
	    while (iter.hasNext()) {
		Object aec = /*(MAssociationEnd)*/ iter.next();
		res.add(ModelFacade.getAssociation(aec));
	    }
	}
	if (ModelFacade.isANodeInstance(port)) {
	    Object noi = /*(MNodeInstance)*/ port;
	    Collection ends = ModelFacade.getLinkEnds(noi);
	    res.addAll(ends);
	}
	if (ModelFacade.isAComponent(port)) {
	    Collection ends = ModelFacade.getAssociationEnds(port);
	    if (ends == null) return res; // empty Vector
	    Iterator endEnum = ends.iterator();
	    while (endEnum.hasNext()) {
		Object aec = /*(MAssociationEnd)*/ endEnum.next();
		res.addElement(ModelFacade.getAssociation(aec));
	    }
	}
	if (ModelFacade.isAComponentInstance(port)) {
	    Object coi = /*(MComponentInstance)*/ port;
	    Collection ends = ModelFacade.getLinkEnds(coi);
	    res.addAll(ends);
	}
	if (ModelFacade.isAClass(port)) {
	    Collection ends = ModelFacade.getAssociationEnds(port);
	    if (ends == null) return res; // empty Vector
	    Iterator endEnum = ends.iterator();
	    while (endEnum.hasNext()) {
		Object ae = /*(MAssociationEnd)*/ endEnum.next();
		res.addElement(ModelFacade.getAssociation(ae));
	    }
	}
	if (ModelFacade.isAInterface(port)) {
	    Collection ends = ModelFacade.getAssociationEnds(port);
	    if (ends == null) return res; // empty Vector
	    Iterator endEnum = ends.iterator();
	    while (endEnum.hasNext()) {
		Object ae = /*(MAssociationEnd)*/ endEnum.next();
		res.addElement(ModelFacade.getAssociation(ae));
	    }
	}
	if (ModelFacade.isAObject(port)) {
	    Object clo = /*(MInstance)*/ port;
	    Collection ends = ModelFacade.getLinkEnds(clo);
	    res.addAll(ends);
	}


	return res;
    }

    /** Return all edges going from given port
     *
     * @see org.tigris.gef.graph.GraphModel#getOutEdges(java.lang.Object)
     */
    public List getOutEdges(Object port) {
	return new Vector(); // TODO:?
    }


    /**
     * Return one end of an edge.
     *
     * @see org.tigris.gef.graph.BaseGraphModel#getSourcePort(java.lang.Object)
     */
    public Object getSourcePort(Object edge) {
        if (edge instanceof CommentEdge) {
            return ((CommentEdge) edge).getSource();
        } else if (ModelFacade.isARelationship(edge)) {
	    return Model.getCoreHelper().getSource(/*(MRelationship)*/ edge);
	} else
	    if (ModelFacade.isALink(edge)) {
		return Model.getCommonBehaviorHelper().getSource(edge);
	    }

	LOG.debug("TODO: getSourcePort");

	return null;
    }


    /** Return  the other end of an edge
     *
     * @see org.tigris.gef.graph.BaseGraphModel#getDestPort(java.lang.Object)
     */
    public Object getDestPort(Object edge) {
        if (edge instanceof CommentEdge) {
            return ((CommentEdge) edge).getSource();
        } else if (ModelFacade.isARelationship(edge)) {
	    return Model.getCoreHelper().getDestination(edge);
	} else if (ModelFacade.isALink(edge)) {
	    return Model.getCommonBehaviorHelper()
		.getDestination(/*(MLink)*/ edge);
	}

	LOG.debug("TODO: getDestPort");

	return null;
    }



    ////////////////////////////////////////////////////////////////
    // MutableGraphModel implementation

    /**
     * Return true if the given object is a valid node in this graph.
     *
     * @see org.tigris.gef.graph.MutableGraphModel#canAddNode(java.lang.Object)
     */
    public boolean canAddNode(Object node) {
	if (node == null) return false;
	if (containsNode(node)) return false;
	return (ModelFacade.isANode(node))
	    || (ModelFacade.isAComponent(node))
	    || (ModelFacade.isAClass(node))
	    || (ModelFacade.isAInterface(node))
	    || (ModelFacade.isAObject(node))
	    || (ModelFacade.isANodeInstance(node))
	    || (ModelFacade.isAComponentInstance(node)
	    || (ModelFacade.isAComment(node)));
    }

    /**
     * Return true if the given object is a valid edge in this graph.
     *
     * @see org.tigris.gef.graph.MutableGraphModel#canAddEdge(java.lang.Object)
     */
    public boolean canAddEdge(Object edge)  {
	if (edge == null) return false;
	if (containsEdge(edge)) return false;
	Object end0 = null, end1 = null;
        if (edge instanceof CommentEdge) {
            end0 = ((CommentEdge) edge).getSource();
            end1 = ((CommentEdge) edge).getDestination();
        } else if (ModelFacade.isARelationship(edge)) {
	    end0 = Model.getCoreHelper().getSource(edge);
	    end1 = Model.getCoreHelper().getDestination(edge);
	}
	else if (ModelFacade.isALink(edge)) {
	    end0 = Model.getCommonBehaviorHelper().getSource(edge);
	    end1 =
		Model.getCommonBehaviorHelper().getDestination(edge);
	}
	if (end0 == null || end1 == null) return false;
	if (!containsNode(end0)) return false;
	if (!containsNode(end1)) return false;
	return true;
    }


    /**
     * Add the given node to the graph, if valid.
     *
     * @see org.tigris.gef.graph.MutableGraphModel#addNode(java.lang.Object)
     */
    public void addNode(Object node) {
        LOG.debug("adding class node!!");
	if (!canAddNode(node)) return;
	getNodes().add(node);
	// TODO: assumes public, user pref for default visibility?
	//do I have to check the namespace here? (Toby)
	if (ModelFacade.isAModelElement(node)
            && (ModelFacade.getNamespace(node) == null)) {
	    Model.getCoreHelper().addOwnedElement(model, node);
	}
	fireNodeAdded(node);
    }

    /**
     * Add the given edge to the graph, if valid.
     *
     * @see org.tigris.gef.graph.MutableGraphModel#addEdge(java.lang.Object)
     */
    public void addEdge(Object edge) {
        LOG.debug("adding class edge!!!!!!");
	if (!canAddEdge(edge)) return;
	getEdges().add(edge);
	// TODO: assumes public
	if (ModelFacade.isAModelElement(edge)) {
	    Model.getCoreHelper().addOwnedElement(model, edge);
	}
	fireEdgeAdded(edge);
    }

    /**
     * @see org.tigris.gef.graph.MutableGraphModel#addNodeRelatedEdges(java.lang.Object)
     */
    public void addNodeRelatedEdges(Object node) {
        super.addNodeRelatedEdges(node);
        
	if (ModelFacade.isAClassifier(node)) {
	    Collection ends = ModelFacade.getAssociationEnds(node);
	    Iterator iter = ends.iterator();
	    while (iter.hasNext()) {
		Object ae = /*(MAssociationEnd)*/ iter.next();
		if (canAddEdge(ModelFacade.getAssociation(ae)))
		    addEdge(ModelFacade.getAssociation(ae));
		return;
	    }
	}
	if (ModelFacade.isAInstance(node)) {
	    Collection ends = ModelFacade.getLinkEnds(node);
	    Iterator iter = ends.iterator();
	    while (iter.hasNext()) {
		Object link = ModelFacade.getLink(iter.next());
		if (canAddEdge(link))
		    addEdge(link);
		return;
	    }
	}
	if (ModelFacade.isAGeneralizableElement(node)) {
	    Iterator iter = ModelFacade.getGeneralizations(node).iterator();
	    while (iter.hasNext()) {
		// g contains a Generalization
		Object g = iter.next();
		if (canAddEdge(g))
		    addEdge(g);
		return;
	    }
	    iter = ModelFacade.getSpecializations(node).iterator();
	    while (iter.hasNext()) {
		// s contains a specialization
		Object s = iter.next();
		if (canAddEdge(s))
		    addEdge(s);
		return;
	    }
	}
	if (ModelFacade.isAModelElement(node)) {
	    Vector specs =
		new Vector(ModelFacade.getClientDependencies(node));
	    specs.addAll(ModelFacade.getSupplierDependencies(node));
	    Iterator iter = specs.iterator();
	    while (iter.hasNext()) {
		Object dep = /*(MDependency)*/ iter.next();
		if (canAddEdge(dep))
		    addEdge(dep);
		return;
	    }
	}
    }


    /**
     * @see java.beans.VetoableChangeListener#vetoableChange(java.beans.PropertyChangeEvent)
     */
    public void vetoableChange(PropertyChangeEvent pce) {
	if ("ownedElement".equals(pce.getPropertyName())) {
	    Vector oldOwned = (Vector) pce.getOldValue();
	    Object eo = /*(MElementImport)*/ pce.getNewValue();
	    Object me = ModelFacade.getModelElement(eo);
	    if (oldOwned.contains(eo)) {
		LOG.debug("model removed " + me);
		if (ModelFacade.isANode(me)) {
		    removeNode(me);
		}
		if (ModelFacade.isANodeInstance(me)) {
		    removeNode(me);
		}
		if (ModelFacade.isAComponent(me)) {
		    removeNode(me);
		}
		if (ModelFacade.isAComponentInstance(me)) {
		    removeNode(me);
		}
		if (ModelFacade.isAClass(me)) {
		    removeNode(me);
		}
		if (ModelFacade.isAInterface(me)) {
		    removeNode(me);
		}
		if (ModelFacade.isAObject(me)) {
		    removeNode(me);
		}
		if (ModelFacade.isAAssociation(me)) {
		    removeEdge(me);
		}
		if (ModelFacade.isADependency(me)) {
		    removeEdge(me);
		}
		if (ModelFacade.isALink(me)) {
		    removeEdge(me);
		}
	    } else {
		LOG.debug("model added " + me);
	    }
	}
    }

    static final long serialVersionUID = 1003748292917485298L;

} /* end class DeploymentDiagramGraphModel */
