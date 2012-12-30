/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.UMLMutableGraphSupport;

/**
 * This class defines a bridge between the UML meta-model
 * representation of the design and the GraphModel interface used by
 * GEF.  This class handles only UML Collaboration Diagrams.
 */
public class CollabDiagramGraphModel extends UMLMutableGraphSupport
    implements PropertyChangeListener, VetoableChangeListener {

    /**
     * The interaction that is shown on the communication diagram.
     */
    private Object interaction;

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(CollabDiagramGraphModel.class.getName());

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
            LOG.log(Level.SEVERE, "Illegal Argument to setCollaboration", e);
            throw e;
        }
        setHomeModel(collaboration);
    }

    /**
     * Gets the interaction that is shown on the sequence diagram.
     * @return the interaction of the diagram.
     */
    private Object getInteraction() {
        if (interaction == null) {
            interaction =
                Model.getCollaborationsFactory().buildInteraction(
                    getHomeModel());
            LOG.log(Level.FINE, "Interaction built.");
            Model.getPump().addModelEventListener(this, interaction);
        }
        return interaction;
    }

    ////////////////////////////////////////////////////////////////
    // GraphModel implementation


    /*
     * Return all ports on node or edge.
     *
     * @see org.tigris.gef.graph.GraphModel#getPorts(java.lang.Object)
     */
    public List getPorts(Object nodeOrEdge) {
	if (Model.getFacade().isAClassifierRole(nodeOrEdge)) {
	    List result = new ArrayList();
	    result.add(nodeOrEdge);
	    return result;
	}
	return Collections.EMPTY_LIST;
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

	if (Model.getFacade().isAClassifierRole(port)) {
	    Object cr = port;
	    Collection ends = Model.getFacade().getAssociationEnds(cr);
	    if (ends == null) {
                return Collections.EMPTY_LIST;
            }
	    List result = new ArrayList();
	    for (Object end : ends) {
		result.add(Model.getFacade().getAssociation(end));
	    }
	}
	return Collections.EMPTY_LIST;
    }

    /*
     * Return all edges going from given port.
     *
     * @see org.tigris.gef.graph.GraphModel#getOutEdges(java.lang.Object)
     */
    public List getOutEdges(Object port) {
	return Collections.EMPTY_LIST; // TODO:?
    }

    ////////////////////////////////////////////////////////////////
    // MutableGraphModel implementation

    /*
     * Return true if the given object is a valid node in this graph.
     *
     * @see org.tigris.gef.graph.MutableGraphModel#canAddNode(java.lang.Object)
     */
    @Override
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
    @Override
    public boolean canAddEdge(Object edge)  {
	if (edge == null) {
            return false;
        }
	if (containsEdge(edge)) {
            return false;
        }
	Object end0 = null;
        Object end1 = null;
        if (Model.getFacade().isAConnector(edge)) {
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
            end0 = Model.getFacade().getLifeline(associationEndRole0);
            end1 = Model.getFacade().getLifeline(associationEndRole1);
        } else if (Model.getFacade().isAAssociationRole(edge)) {
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
	    end0 = Model.getFacade().getGeneral(gen);
	    end1 = Model.getFacade().getSpecific(gen);
	} else if (Model.getFacade().isADependency(edge)) {
	    Collection clients = Model.getFacade().getClients(edge);
	    Collection suppliers = Model.getFacade().getSuppliers(edge);
	    if (clients == null || clients.isEmpty()
	            || suppliers == null || suppliers.isEmpty()) {
                return false;
            }
	    end0 = clients.iterator().next();
	    end1 = suppliers.iterator().next();
	} else if (edge instanceof CommentEdge) {
	    end0 = ((CommentEdge) edge).getSource();
	    end1 = ((CommentEdge) edge).getDestination();
	} else {
	    return false;
        }

        // Both ends must be defined and nodes that are on the graph already.
        if (end0 == null || end1 == null) {
            LOG.log(Level.SEVERE, "Edge rejected. Its ends are not attached to anything");
            return false;
        }

        if (!containsNode(end0)
                && !containsEdge(end0)) {
            LOG.log(Level.SEVERE, "Edge rejected. Its source end is attached to " + end0
                    + " but this is not in the graph model");
            return false;
        }
        if (!containsNode(end1)
                && !containsEdge(end1)) {
            LOG.log(Level.SEVERE, "Edge rejected. Its destination end is attached to "
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
    @Override
    public void addNode(Object node) {
  LOG.log(Level.FINE, "adding MClassifierRole node!!");
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
    @Override
    public void addEdge(Object edge) {
        LOG.log(Level.FINE, "adding class edge!!!!!!");
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
    @Override
    public void addNodeRelatedEdges(Object node) {
        super.addNodeRelatedEdges(node);

	if (Model.getFacade().isAClassifier(node)) {
	    Collection ends = Model.getFacade().getAssociationEnds(node);
	    for (Object end : ends) {
		if (canAddEdge(Model.getFacade().getAssociation(end))) {
                    addEdge(Model.getFacade().getAssociation(end));
                }
	    }
	}
	if (Model.getFacade().isAGeneralizableElement(node)) {
	    Collection generalizations =
	        Model.getFacade().getGeneralizations(node);
	    for (Object generalization : generalizations) {
		if (canAddEdge(generalization)) {
		    addEdge(generalization);
		    return;
		}
	    }
	    Collection specializations = Model.getFacade().getSpecializations(node);
	    for (Object specialization : specializations) {
		if (canAddEdge(specialization)) {
		    addEdge(specialization);
		    return;
		}
	    }
	}
	if (Model.getFacade().isAModelElement(node)) {
	    Collection dependencies =
		new ArrayList(Model.getFacade().getClientDependencies(node));
	    dependencies.addAll(Model.getFacade().getSupplierDependencies(node));
	    for (Object dependency : dependencies) {
		if (canAddEdge(dependency)) {
		    addEdge(dependency);
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
    @Override
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
	    List oldOwned = (List) pce.getOldValue();
	    Object eo = /*(MElementImport)*/ pce.getNewValue();
	    Object me = Model.getFacade().getModelElement(eo);
	    if (oldOwned.contains(eo)) {

    LOG.log(Level.FINE, "model removed {0}", me);

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
    LOG.log(Level.FINE, "model added {0}", me);
	    }
	}
    }

    /**
     * In UML1.4 the sequence diagram is owned by a collaboration.
     * In UML2 it is owned by an Interaction (which might itself be owned by a
     * collaboration or some other namespace)
     * @return the owner of the sequence diagram
     */
    public Object getOwner() {
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            return getHomeModel();
        } else {
            return getInteraction();
        }
    }

    /**
     * Look for delete events of the interaction that this diagram
     * represents. Null our interaction reference if detected.
     * @param evt the property change event
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt instanceof DeleteInstanceEvent
                && evt.getSource() == interaction) {
            Model.getPump().removeModelEventListener(this, interaction);
            interaction = null;
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -4895696235473642985L;
} /* end class CollabDiagramGraphModel */
