// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.collaboration;

import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.UMLMutableGraphSupport;

/**
 * This class defines a bridge between the UML meta-model
 * representation of the design and the GraphModel interface used by
 * GEF.  This class handles only UML Collaboration Diagrams.
 */
public class CollabDiagramGraphModel extends UMLMutableGraphSupport
    implements VetoableChangeListener {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(CollabDiagramGraphModel.class);

    /**
     * @param collaboration the collaboration to be set for this diagram
     */
    public void setCollaboration(Object collaboration) {
        try {
            if (collaboration == null) {
                throw new IllegalArgumentException(
                    "A null collaboration was supplied");
            }
            if (!(Model.getFacade().isACollaboration(collaboration))) {
                throw new IllegalArgumentException(
                    "Expected a collaboration. The type received was "
                    + collaboration.getClass().getName());
            }
        } catch (IllegalArgumentException e) {
            LOG.error("Illegal Argument to setCollaboration", e);
            throw e;
        }
        setHomeModel(collaboration);
    }


    ////////////////////////////////////////////////////////////////
    // GraphModel implementation


    /*
     * Return all ports on node or edge.
     *
     * @see org.tigris.gef.graph.GraphModel#getPorts(java.lang.Object)
     */
    public List getPorts(Object nodeOrEdge) {
	Vector res = new Vector();  //wasteful!
	if (Model.getFacade().isAClassifierRole(nodeOrEdge)) {
	    res.addElement(nodeOrEdge);
	}
	return res;
    }

    /*
     * Return the node or edge that owns the given port.
     *
     * @see org.tigris.gef.graph.BaseGraphModel#getOwner(java.lang.Object)
     */
    public Object getOwner(Object port) {
	return port;
    }

    /*
     * Return all edges going to given port.
     *
     * @see org.tigris.gef.graph.GraphModel#getInEdges(java.lang.Object)
     */
    public List getInEdges(Object port) {
	Vector res = new Vector(); //wasteful!
	if (Model.getFacade().isAClassifierRole(port)) {
	    Object cr = /*(MClassifierRole)*/ port;
	    Collection ends = Model.getFacade().getAssociationEnds(cr);
	    if (ends == null) {
                return res; // empty Vector
            }
	    Iterator iter = ends.iterator();
	    while (iter.hasNext()) {
		Object aer = /*(MAssociationEndRole)*/ iter.next();
		res.addElement(Model.getFacade().getAssociation(aer));
	    }
	}
	return res;
    }

    /*
     * Return all edges going from given port.
     *
     * @see org.tigris.gef.graph.GraphModel#getOutEdges(java.lang.Object)
     */
    public List getOutEdges(Object port) {
	return new Vector(); // TODO:?
    }

    ////////////////////////////////////////////////////////////////
    // MutableGraphModel implementation

    /*
     * Return true if the given object is a valid node in this graph.
     *
     * @see org.tigris.gef.graph.MutableGraphModel#canAddNode(java.lang.Object)
     */
    public boolean canAddNode(Object node) {
        if (node == null) {
            return false;
        }
        if (Model.getFacade().isAAssociation(node)
                && !Model.getFacade().isANaryAssociation(node)) {
            // A binary association is not a node so reject.
            return false;
        }
    
	if (containsNode(node)) {
            return false;
        }
	return (Model.getFacade().isAClassifierRole(node)
            || Model.getFacade().isAMessage(node)
            || Model.getFacade().isAComment(node));
    }

    /*
     * Return true if the given object is a valid edge in this graph.
     *
     * @see org.tigris.gef.graph.MutableGraphModel#canAddEdge(java.lang.Object)
     */
    public boolean canAddEdge(Object edge)  {
	if (edge == null) {
            return false;
        }
	if (containsEdge(edge)) {
            return false;
        }
	Object end0 = null;
        Object end1 = null;
	if (Model.getFacade().isAAssociationRole(edge)) {
	    Collection conns = Model.getFacade().getConnections(edge);
            Iterator iter = conns.iterator();
	    if (conns.size() < 2) {
                return false;
            }
	    Object associationEndRole0 = iter.next();
	    Object associationEndRole1 = iter.next();
	    if (associationEndRole0 == null || associationEndRole1 == null) {
	        return false;
	    }
	    end0 = Model.getFacade().getType(associationEndRole0);
	    end1 = Model.getFacade().getType(associationEndRole1);
	} else if (Model.getFacade().isAGeneralization(edge)) {
	    Object gen = /*(MGeneralization)*/ edge;
	    end0 = Model.getFacade().getParent(gen);
	    end1 = Model.getFacade().getChild(gen);
	} else if (Model.getFacade().isADependency(edge)) {
	    Collection clients = Model.getFacade().getClients(edge);
	    Collection suppliers = Model.getFacade().getSuppliers(edge);
	    if (clients == null || suppliers == null) {
                return false;
            }
	    end0 = (clients.toArray())[0];
	    end1 = (suppliers.toArray())[0];
	} else if (edge instanceof CommentEdge) {
	    end0 = ((CommentEdge) edge).getSource();
	    end1 = ((CommentEdge) edge).getDestination();
	} else {
	    return false;       
        }
        
        // Both ends must be defined and nodes that are on the graph already.
        if (end0 == null || end1 == null) {
            LOG.error("Edge rejected. Its ends are not attached to anything");
            return false;
        }
        
        if (!containsNode(end0)
                && !containsEdge(end0)) {
            LOG.error("Edge rejected. Its source end is attached to " + end0
                    + " but this is not in the graph model");
            return false;
        }
        if (!containsNode(end1)
                && !containsEdge(end1)) {
            LOG.error("Edge rejected. Its destination end is attached to "
                    + end1 + " but this is not in the graph model");
            return false;
        }
        
        return true;
    }


    /*
     * Add the given node to the graph, if valid.
     *
     * @see org.tigris.gef.graph.MutableGraphModel#addNode(java.lang.Object)
     */
    public void addNode(Object node) {
	LOG.debug("adding MClassifierRole node!!");
	if (!canAddNode(node)) {
            return;
        }
	getNodes().add(node);
	// TODO: assumes public, user pref for default visibility?
	if (Model.getFacade().isAClassifier(node)) {
	    Model.getCoreHelper().addOwnedElement(getHomeModel(), node);
	    // ((MClassifier)node).setNamespace(_collab.getNamespace());
	}

	fireNodeAdded(node);
    }

    /*
     * Add the given edge to the graph, if valid.
     *
     * @see org.tigris.gef.graph.MutableGraphModel#addEdge(java.lang.Object)
     */
    public void addEdge(Object edge) {
        LOG.debug("adding class edge!!!!!!");
        if (!canAddEdge(edge)) {
            return;
        }
        getEdges().add(edge);
        // TODO: assumes public
        if (Model.getFacade().isAModelElement(edge)
	    && Model.getFacade().getNamespace(edge) == null) {
            Model.getCoreHelper().addOwnedElement(getHomeModel(), edge);
        }
        fireEdgeAdded(edge);
    }

    /*
     * @see org.tigris.gef.graph.MutableGraphModel#addNodeRelatedEdges(java.lang.Object)
     */
    public void addNodeRelatedEdges(Object node) {
        super.addNodeRelatedEdges(node);

	if (Model.getFacade().isAClassifier(node)) {
	    Collection ends = Model.getFacade().getAssociationEnds(node);
	    Iterator iter = ends.iterator();
	    while (iter.hasNext()) {
		Object ae = /*(MAssociationEndRole)*/ iter.next();
		if (canAddEdge(Model.getFacade().getAssociation(ae))) {
                    addEdge(Model.getFacade().getAssociation(ae));
                }
	    }
	}
	if (Model.getFacade().isAGeneralizableElement(node)) {
	    Collection gn = Model.getFacade().getGeneralizations(node);
	    Iterator iter = gn.iterator();
	    while (iter.hasNext()) {
		Object g = /*(MGeneralization)*/ iter.next();
		if (canAddEdge(g)) {
		    addEdge(g);
		    return;
		}
	    }
	    Collection sp = Model.getFacade().getSpecializations(node);
	    iter = sp.iterator();
	    while (iter.hasNext()) {
		Object s = /*(MGeneralization)*/ iter.next();
		if (canAddEdge(s)) {
		    addEdge(s);
		    return;
		}
	    }
	}
	if (Model.getFacade().isAModelElement(node)) {
	    Vector specs =
		new Vector(Model.getFacade().getClientDependencies(node));
	    specs.addAll(Model.getFacade().getSupplierDependencies(node));
	    Iterator iter = specs.iterator();
	    while (iter.hasNext()) {
		Object dep = /*(MDependency)*/ iter.next();
		if (canAddEdge(dep)) {
		    addEdge(dep);
		    return;
		}
	    }
	}
    }


    /*
     * Return true if the two given ports can be connected by a
     * kind of edge to be determined by the ports.
     *
     * @see org.tigris.gef.graph.MutableGraphModel#canConnect(java.lang.Object,
     * java.lang.Object)
     */
    public boolean canConnect(Object fromP, Object toP) {
	if ((Model.getFacade().isAClassifierRole(fromP))
	    && (Model.getFacade().isAClassifierRole(toP))) {
            return true;
        }
	return false;
    }

    ////////////////////////////////////////////////////////////////
    // VetoableChangeListener implementation

    /*
     * @see java.beans.VetoableChangeListener#vetoableChange(java.beans.PropertyChangeEvent)
     */
    public void vetoableChange(PropertyChangeEvent pce) {
	//throws PropertyVetoException

	if ("ownedElement".equals(pce.getPropertyName())) {
	    Vector oldOwned = (Vector) pce.getOldValue();
	    Object eo = /*(MElementImport)*/ pce.getNewValue();
	    Object me = Model.getFacade().getModelElement(eo);
	    if (oldOwned.contains(eo)) {
		LOG.debug("model removed " + me);
		if (Model.getFacade().isAClassifier(me)) {
                    removeNode(me);
                }
		if (Model.getFacade().isAMessage(me)) {
                    removeNode(me);
                }
		if (Model.getFacade().isAAssociation(me)) {
                    removeEdge(me);
                }
	    } else {
		LOG.debug("model added " + me);
	    }
	}
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -4895696235473642985L;
} /* end class CollabDiagramGraphModel */
