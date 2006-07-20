// $Id$
// Copyright (c) 2003-2006 The Regents of the University of California. All
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
public class DeploymentDiagramGraphModel
    extends UMLMutableGraphSupport
    implements VetoableChangeListener {
    /**
     * Logger.
     */
    private static final Logger LOG =
            Logger.getLogger(DeploymentDiagramGraphModel.class);

    ////////////////////////////////////////////////////////////////
    // GraphModel implementation


    /**
     * Return all ports on node or edge.
     *
     * @see org.tigris.gef.graph.GraphModel#getPorts(java.lang.Object)
     */
    public List getPorts(Object nodeOrEdge) {
        Vector res = new Vector();  //wasteful!
        if (Model.getFacade().isANode(nodeOrEdge)) {
            res.addElement(nodeOrEdge);
        }
        if (Model.getFacade().isANodeInstance(nodeOrEdge)) {
            res.addElement(nodeOrEdge);
        }

        if (Model.getFacade().isAComponent(nodeOrEdge)) {
            res.addElement(nodeOrEdge);
        }
        if (Model.getFacade().isAComponentInstance(nodeOrEdge)) {
            res.addElement(nodeOrEdge);
        }
        if (Model.getFacade().isAClass(nodeOrEdge)) {
            res.addElement(nodeOrEdge);
        }
        if (Model.getFacade().isAInterface(nodeOrEdge)) {
            res.addElement(nodeOrEdge);
        }
        if (Model.getFacade().isAObject(nodeOrEdge)) {
            res.addElement(nodeOrEdge);
        }
        return res;
    }

    /**
     * Return the node or edge that owns the given port.
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
        if (Model.getFacade().isANode(port)) {
            Collection ends = Model.getFacade().getAssociationEnds(port);
            if (ends == null) {
                return res; // empty Vector
            }
            Iterator iter = ends.iterator();
            while (iter.hasNext()) {
                Object aec = /*(MAssociationEnd)*/ iter.next();
                res.add(Model.getFacade().getAssociation(aec));
            }
        }
        if (Model.getFacade().isANodeInstance(port)) {
            Object noi = /*(MNodeInstance)*/ port;
            Collection ends = Model.getFacade().getLinkEnds(noi);
            res.addAll(ends);
        }
        if (Model.getFacade().isAComponent(port)) {
            Collection ends = Model.getFacade().getAssociationEnds(port);
            if (ends == null) {
                return res; // empty Vector
            }
            Iterator endEnum = ends.iterator();
            while (endEnum.hasNext()) {
                Object aec = /*(MAssociationEnd)*/ endEnum.next();
                res.addElement(Model.getFacade().getAssociation(aec));
            }
        }
        if (Model.getFacade().isAComponentInstance(port)) {
            Object coi = /*(MComponentInstance)*/ port;
            Collection ends = Model.getFacade().getLinkEnds(coi);
            res.addAll(ends);
        }
        if (Model.getFacade().isAClass(port)) {
            Collection ends = Model.getFacade().getAssociationEnds(port);
            if (ends == null) {
                return res; // empty Vector
            }
            Iterator endEnum = ends.iterator();
            while (endEnum.hasNext()) {
                Object ae = /*(MAssociationEnd)*/ endEnum.next();
                res.addElement(Model.getFacade().getAssociation(ae));
            }
        }
        if (Model.getFacade().isAInterface(port)) {
            Collection ends = Model.getFacade().getAssociationEnds(port);
            if (ends == null) {
                return res; // empty Vector
            }
            Iterator endEnum = ends.iterator();
            while (endEnum.hasNext()) {
                Object ae = /*(MAssociationEnd)*/ endEnum.next();
                res.addElement(Model.getFacade().getAssociation(ae));
            }
        }
        if (Model.getFacade().isAObject(port)) {
            Object clo = /*(MInstance)*/ port;
            Collection ends = Model.getFacade().getLinkEnds(clo);
            res.addAll(ends);
        }


        return res;
    }

    /**
     * Return all edges going from given port.
     *
     * @see org.tigris.gef.graph.GraphModel#getOutEdges(java.lang.Object)
     */
    public List getOutEdges(Object port) {
        return new Vector(); // TODO:?
    }

    ////////////////////////////////////////////////////////////////
    // MutableGraphModel implementation

    /**
     * Return true if the given object is a valid node in this graph.
     *
     * @see org.tigris.gef.graph.MutableGraphModel#canAddNode(java.lang.Object)
     */
    public boolean canAddNode(Object node) {
        if (node == null) {
            return false;
        }
        if (Model.getFacade().isAAssociation(node) && !Model.getFacade().isANaryAssociation(node)) {
            // A binary association is not a node so reject.
            return false;
        }
        if (containsNode(node)) {
            return false;
        }
        if (Model.getFacade().isAAssociation(node)) {
            Collection ends = Model.getFacade().getConnections(node);
            Iterator iter = ends.iterator();
            boolean canAdd = true;
            while (iter.hasNext()) {
                Object classifier =
                        Model.getFacade().getClassifier(iter.next());
                if (!containsNode(classifier)) {
                    canAdd = false;
                    break;
                }
            }
            return canAdd;
        }
        return (Model.getFacade().isANode(node))
                || (Model.getFacade().isAComponent(node))
                || (Model.getFacade().isAClass(node))
                || (Model.getFacade().isAInterface(node))
                || (Model.getFacade().isAObject(node))
                || (Model.getFacade().isANodeInstance(node))
                || (Model.getFacade().isAComponentInstance(node)
                || (Model.getFacade().isAComment(node)));
    }

    /**
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
        Object end0 = null, end1 = null;
        if (edge instanceof CommentEdge) {
            end0 = ((CommentEdge) edge).getSource();
            end1 = ((CommentEdge) edge).getDestination();
        } else if (Model.getFacade().isAAssociationEnd(edge)) {
            end0 = Model.getFacade().getAssociation(edge);
            end1 = Model.getFacade().getType(edge);

            return (end0 != null
                    && end1 != null
                    && (containsEdge(end0) || containsNode(end0))
                    && containsNode(end1));
        } else if (Model.getFacade().isARelationship(edge)) {
            end0 = Model.getCoreHelper().getSource(edge);
            end1 = Model.getCoreHelper().getDestination(edge);
        } else if (Model.getFacade().isALink(edge)) {
            end0 = Model.getCommonBehaviorHelper().getSource(edge);
            end1 =
                    Model.getCommonBehaviorHelper().getDestination(edge);
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
            LOG.error("Edge rejected. Its source end is attached to "
                    + end0
                    + " but this is not in the graph model");
            return false;
        }
        if (!containsNode(end1)
                && !containsEdge(end1)) {
            LOG.error("Edge rejected. Its destination end is attached to "
                    + end1
                    + " but this is not in the graph model");
            return false;
        }

        return true;
    }


    /**
     * Add the given node to the graph, if valid.
     *
     * @see org.tigris.gef.graph.MutableGraphModel#addNode(java.lang.Object)
     */
    public void addNode(Object node) {
        LOG.debug("adding class node!!");
        if (!canAddNode(node)) {
            return;
        }
        getNodes().add(node);
        // TODO: assumes public, user pref for default visibility?
        //do I have to check the namespace here? (Toby)
        if (Model.getFacade().isAModelElement(node)
                && (Model.getFacade().getNamespace(node) == null)) {
            Model.getCoreHelper().addOwnedElement(getHomeModel(), node);
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
        if (!canAddEdge(edge)) {
            return;
        }
        getEdges().add(edge);
        // TODO: assumes public
        if (Model.getFacade().isAModelElement(edge)
                && !Model.getFacade().isAAssociationEnd(edge)) {
            Model.getCoreHelper().addOwnedElement(getHomeModel(), edge);
        }
        fireEdgeAdded(edge);
    }

    /**
     * @see org.tigris.gef.graph.MutableGraphModel#addNodeRelatedEdges(java.lang.Object)
     */
    public void addNodeRelatedEdges(Object node) {
        super.addNodeRelatedEdges(node);

        if (Model.getFacade().isAClassifier(node)) {
            Collection ends = Model.getFacade().getAssociationEnds(node);
            Iterator iter = ends.iterator();
            while (iter.hasNext()) {
                Object ae = /*(MAssociationEnd)*/ iter.next();
                if (!Model.getFacade().isANaryAssociation(
                        Model.getFacade().getAssociation(ae))
                        && canAddEdge(Model.getFacade().getAssociation(ae))) {
                    addEdge(Model.getFacade().getAssociation(ae));
                }
                return;
            }
        }
        if (Model.getFacade().isAAssociation(node)) {
            Collection ends = Model.getFacade().getConnections(node);
            Iterator iter = ends.iterator();
            while (iter.hasNext()) {
                Object associationEnd = iter.next();
                if (canAddEdge(associationEnd)) {
                    addEdge(associationEnd);
                }
            }
        }
        if (Model.getFacade().isAInstance(node)) {
            Collection ends = Model.getFacade().getLinkEnds(node);
            Iterator iter = ends.iterator();
            while (iter.hasNext()) {
                Object link = Model.getFacade().getLink(iter.next());
                if (canAddEdge(link)) {
                    addEdge(link);
                }
                return;
            }
        }
        if (Model.getFacade().isAGeneralizableElement(node)) {
            Iterator iter =
                    Model.getFacade().getGeneralizations(node).iterator();
            while (iter.hasNext()) {
                // g contains a Generalization
                Object g = iter.next();
                if (canAddEdge(g)) {
                    addEdge(g);
                }
                return;
            }
            iter = Model.getFacade().getSpecializations(node).iterator();
            while (iter.hasNext()) {
                // s contains a specialization
                Object s = iter.next();
                if (canAddEdge(s)) {
                    addEdge(s);
                }
                return;
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
                }
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
            Object me = Model.getFacade().getModelElement(eo);
            if (oldOwned.contains(eo)) {
                LOG.debug("model removed " + me);
                if (Model.getFacade().isANode(me)) {
                    removeNode(me);
                }
                if (Model.getFacade().isANodeInstance(me)) {
                    removeNode(me);
                }
                if (Model.getFacade().isAComponent(me)) {
                    removeNode(me);
                }
                if (Model.getFacade().isAComponentInstance(me)) {
                    removeNode(me);
                }
                if (Model.getFacade().isAClass(me)) {
                    removeNode(me);
                }
                if (Model.getFacade().isAInterface(me)) {
                    removeNode(me);
                }
                if (Model.getFacade().isAObject(me)) {
                    removeNode(me);
                }
                if (Model.getFacade().isAAssociation(me)) {
                    removeEdge(me);
                }
                if (Model.getFacade().isADependency(me)) {
                    removeEdge(me);
                }
                if (Model.getFacade().isALink(me)) {
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
    static final long serialVersionUID = 1003748292917485298L;

} /* end class DeploymentDiagramGraphModel */
