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

package org.argouml.uml.diagram.use_case;

import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.UMLMutableGraphSupport;

/**
 * This class defines a bridge between the UML meta-model
 * representation of the design and the GraphModel interface used by
 * GEF.<p>
 *
 * This class handles only UML Use Case Diagrams.<p>
 */
public class UseCaseDiagramGraphModel
        extends UMLMutableGraphSupport
        implements VetoableChangeListener {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(UseCaseDiagramGraphModel.class);

    ///////////////////////////////////////////////////////////////////////////
    //
    // Methods that implement the GraphModel itself
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return all ports on a node or edge supplied as argument.<p>
     *
     * The only objects on our diagram that have any ports are use
     * cases and actors, and they each have one - themself.<p>
     *
     * @param nodeOrEdge  A model element, for whom the list of ports is
     *                    wanted.
     *
     * @return            A List of the ports found.
     */
    public List getPorts(Object nodeOrEdge) {
        if (Model.getFacade().isAActor(nodeOrEdge)) {
            List result = new ArrayList();
            result.add(nodeOrEdge);
            return result;
        } else if (Model.getFacade().isAUseCase(nodeOrEdge)) {
            List result = new ArrayList();
            result.add(nodeOrEdge);
            return result;
        }

        return Collections.EMPTY_LIST;
    }


    /**
     * Return the node or edge that owns the given port.<p>
     *
     * In our implementation the only objects with ports, use
     * themselves as the port, so are there own owner.<p>
     *
     * @param port  The port, whose owner is wanted.
     *
     * @return      The owner of the port.
     */
    public Object getOwner(Object port) {
        return port;
    }


    /**
     * Return all edges going to given port.<p>
     *
     * The only objects with ports on the use case diagram are actors
     * and use cases.  In each case we find the attached association
     * ends, and build a list of them as the incoming ports.<p>
     *
     * @param port  The port for which we want to know the incoming edges.
     *
     * @return      A list of objects which are the incoming edges.
     */
    public List getInEdges(Object port) {
        if (Model.getFacade().isAActor(port) 
                || Model.getFacade().isAUseCase(port)) {
            List result = new ArrayList();
            Collection ends = Model.getFacade().getAssociationEnds(port);
            if (ends == null) {
                return Collections.EMPTY_LIST;
            }
            for (Object ae : ends) {
                result.add(Model.getFacade().getAssociation(ae));
            }
            return result;
        }
        return Collections.EMPTY_LIST;
    }


    /**
     * Return all edges going from the given port.<p>
     *
     * <em>Needs more work</em>.  This would seem superficially to be
     * identical to {@link #getInEdges}, but in our implementation we
     * return an empty list.<p>
     *
     * @param port  The port for which we want to know the outgoing edges.
     *
     * @return      A list of objects which are the outgoing edges. Currently
     *              return the empty list.
     */
    public List getOutEdges(Object port) {
        return Collections.EMPTY_LIST;
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    // Methods that implement the MutableGraphModel interface
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Determine if the given node can validly be placed on this
     * graph.<p>
     *
     * This is simply a matter of determining if the node is an actor
     * or use case.<p>
     *
     * <em>Note</em>. This is inconsistent with {@link #addNode},
     * which will not allow a node to be added to the graph if it is
     * already there.<p>
     *
     * @param node  The node to be considered
     *
     * @return      <code>true</code> if the given object is a valid node in
     *              this graph, <code>false</code> otherwise.
     */
    @Override
    public boolean canAddNode(Object node) {
        if (Model.getFacade().isAAssociation(node)
                && !Model.getFacade().isANaryAssociation(node)) {
            // A binary association is not a node so reject.
            return false;
        }
        if (super.canAddNode(node)) {
            return true;
        }
        if (containsNode(node)) {
	    return false;
	}
        return Model.getFacade().isAActor(node)
            || Model.getFacade().isAUseCase(node)
            || Model.getFacade().isAPackage(node);
    }


    /**
     * Determine if the given edge can validly be placed on this graph.<p>
     *
     * We cannot do so if the edge is already on the graph (unlike
     * nodes they may not appear more than once).<p>
     *
     * Otherwise, for all valid types of edge (binary association,
     * generalization, extend, include, dependency) we get the two
     * ends. If they are both nodes already on the graph we are OK,
     * otherwise we cannot place the edge on the graph.<p>
     *
     * @param edge  The edge to be considered
     *
     * @return      <code>true</code> if the given object is a valid edge in
     *              this graph, <code>false</code> otherwise.
     */
    @Override
    public boolean canAddEdge(Object edge)  {
        if (edge == null) {
            return false;
        }
        if (containsEdge(edge)) {
            return false;
        }

        // Get the two ends of any valid edge
        Object sourceModelElement = null;
        Object destModelElement = null;
        if (Model.getFacade().isAAssociation(edge)) {

            // Only allow binary associations

            Collection conns = Model.getFacade().getConnections(edge);
            Iterator iter = conns.iterator();

            if (conns.size() < 2) {
                return false;
            }

            Object associationEnd0 = iter.next();
            Object associationEnd1 = iter.next();

            // Give up if the assocation ends don't have a type defined

            if ((associationEnd0 == null) || (associationEnd1 == null)) {
                return false;
            }

            sourceModelElement = Model.getFacade().getType(associationEnd0);
            destModelElement = Model.getFacade().getType(associationEnd1);
        } else if (Model.getFacade().isAGeneralization(edge)) {
            sourceModelElement = Model.getFacade().getSpecific(edge);
            destModelElement = Model.getFacade().getGeneral(edge);
        } else if (Model.getFacade().isAExtend(edge)) {
            sourceModelElement = Model.getFacade().getBase(edge);
            destModelElement = Model.getFacade().getExtension(edge);
        } else if (Model.getFacade().isAInclude(edge)) {

            sourceModelElement = Model.getFacade().getBase(edge);
            destModelElement = Model.getFacade().getAddition(edge);
        } else if (Model.getFacade().isADependency(edge)) {

            // A dependency potentially has many clients and suppliers. We only
            // consider the first of each (not clear that we should really
            // accept the case where there is more than one of either)

            Collection clients   = Model.getFacade().getClients(edge);
            Collection suppliers = Model.getFacade().getSuppliers(edge);

            if (clients == null || clients.isEmpty() 
                    || suppliers == null || suppliers.isEmpty()) {
                return false;
            }
            sourceModelElement = clients.iterator().next();
            destModelElement = suppliers.iterator().next();

        } else if (edge instanceof CommentEdge) {
            sourceModelElement = ((CommentEdge) edge).getSource();
            destModelElement = ((CommentEdge) edge).getDestination();
        } else {
            return false;
        }

        // Both ends must be defined and nodes that are on the graph already.
        if (sourceModelElement == null || destModelElement == null) {
            LOG.error("Edge rejected. Its ends are not attached to anything");
            return false;
        }

        if (!containsNode(sourceModelElement)
                && !containsEdge(sourceModelElement)) {
            LOG.error("Edge rejected. Its source end is attached to "
                    + sourceModelElement
                    + " but this is not in the graph model");
            return false;
        }
        if (!containsNode(destModelElement)
                && !containsEdge(destModelElement)) {
            LOG.error("Edge rejected. Its destination end is attached to "
                    + destModelElement
                    + " but this is not in the graph model");
            return false;
        }

        return true;
    }


    /**
     * Add the given node to the graph, if valid.<p>
     *
     * We add the node if it is not already on the graph, and
     * (assuming it to be an actor or use case) add it to the owned
     * elements for the model.<p>
     *
     * <em>Needs more work</em>. In adding the node to the owned
     * elements of the model namespace, we are implicitly making it
     * public visibility (it could be private to this namespace).<p>
     *
     * <em>Note</em>.  This method is inconsistent with
     * {@link #canAddNode}, which will allow a node to be added to the
     * graph if it is already there.<p>
     *
     * @param node  The node to be added to the graph.
     */
    @Override
    public void addNode(Object node) {

        LOG.debug("adding usecase node");

        // Give up if we are already on the graph. This is a bit inconistent
        // with canAddNode above.

        if (!canAddNode(node)) {
            return;
        }

        // Add the node, check that it is an actor or use case and add it to
        // the model namespace.

        getNodes().add(node);

        if (Model.getFacade().isAUMLElement(node)
                && Model.getFacade().getNamespace(node) == null) {
            Model.getCoreHelper().addOwnedElement(getHomeModel(), node);
        }

        // Tell GEF its changed

        fireNodeAdded(node);
    }


    /**
     * Add the given edge to the graph, if valid.<p>
     *
     * We add the edge if it is not already on the graph, and
     * (assuming it to be an association, generalization, extend,
     * include or dependency) add it to the owned elements for the
     * model.<p>
     *
     * <em>Needs more work</em>. In adding the edge to the owned
     * elements of the model namespace, we are implicitly making it
     * public visibility (it could be private to this namespace).<p>
     *
     * @param edge  The edge to be added to the graph.
     */
    @Override
    public void addEdge(Object edge) {
        if (edge == null) {
            throw new IllegalArgumentException("Cannot add a null edge");
        }

        if (getDestPort(edge) == null || getSourcePort(edge) == null) {
            throw new IllegalArgumentException(
                    "The source and dest port should be provided on an edge");
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("Adding an edge of type "
                   + edge.getClass().getName()
                   + " to use case diagram.");
        }

        if (!canAddEdge(edge)) {
            LOG.info("Attempt to add edge rejected");
            return;
        }

        // Add the element and place it in the namespace of the model
        getEdges().add(edge);

        // TODO: assumes public
        if (Model.getFacade().isAUMLElement(edge)
                && Model.getFacade().getNamespace(edge) == null) {
            Model.getCoreHelper().addOwnedElement(getHomeModel(), edge);
        }

        // Tell GEF

        fireEdgeAdded(edge);
    }

    /**
     * Add the various types of edge that may be connected with the
     * given node.<p>
     *
     * For use cases we may find extend and include relationships. For
     * classifiers (effectively actors and use cases) we may find
     * associations. For generalizable elements (effectively actors
     * and use cases again) we may find generalizations and
     * specializations. For ModelElements (effectively actors and use
     * cases again) we may find dependencies.<p>
     *
     * @param node  The node whose edges are to be added.
     */
    @Override
    public void addNodeRelatedEdges(Object node) {
        super.addNodeRelatedEdges(node);

        if (Model.getFacade().isAUseCase(node)) {
            List relations = new ArrayList();

            relations.addAll(Model.getFacade().getIncludes(node));
            relations.addAll(Model.getFacade().getIncluders(node));
            relations.addAll(Model.getFacade().getExtends(node));
            relations.addAll(Model.getFacade().getExtenders(node));

            for (Object relation : relations) {
                if (canAddEdge(relation)) {
                    addEdge(relation);
                }
            }
        }

        if (Model.getFacade().isAClassifier(node)) {
            Collection ends = Model.getFacade().getAssociationEnds(node);
            for (Object ae : ends) {
                if (canAddEdge(Model.getFacade().getAssociation(ae))) {
                    addEdge(Model.getFacade().getAssociation(ae));
                }
            }
        }

        if (Model.getFacade().isAGeneralizableElement(node)) {
            Collection gn = Model.getFacade().getGeneralizations(node);
            for (Object g : gn) {
                if (canAddEdge(g)) {
                    addEdge(g);
                }
            }
            Collection sp = Model.getFacade().getSpecializations(node);
            for (Object s : sp) {
                if (canAddEdge(s)) {
                    addEdge(s);
                }
            }
        }

        if (Model.getFacade().isAUMLElement(node)) {
            Collection dependencies =
                new ArrayList(Model.getFacade().getClientDependencies(node));

            dependencies.addAll(Model.getFacade().getSupplierDependencies(node));

            for (Object dependency : dependencies) {
                if (canAddEdge(dependency)) {
                    addEdge(dependency);
                }
            }
        }
    }



    /**
     * Determine if the two given ports can be connected by a kind of
     * edge to be determined by the ports.<p>
     *
     * <em>Note</em>. There appears to be a problem with the
     * implementation, since it suggests actors cannot connect. In
     * fact generalization is permitted, and this works, suggesting
     * this method is not actually invoked in the current
     * implementation of ArgoUML.<p>
     *
     * @param fromP  The source port of the connection
     *
     * @param toP    The destination port of the connection.
     *
     * @return       <code>true</code> if the two given ports can be connected
     *               by a kind of edge to be determined by the
     *               ports. <code>false</code> otherwise.
     */
    @Override
    public boolean canConnect(Object fromP, Object toP) {

        // Suggest that actors may not connect (see JavaDoc comment about
        // this).

        if (Model.getFacade().isAActor(fromP)
                && Model.getFacade().isAActor(toP)) {
            return false;
        }

        // Everything else is OK

        return true;
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Methods that implement the VetoableChangeListener interface
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Called when a property of interest has been changed - in this
     * case the owned elements of the model. Provided to implement the
     * {@link VetoableChangeListener} interface.<p>
     *
     * We could throw a PropertyVetoException if we wished to allow
     * the change to be rolled back, but we don't.<p>
     *
     * @param pce  The event that triggered us, and from which we can extract
     *             the name of the property that triggered us.
     */
    public void vetoableChange(PropertyChangeEvent pce) {

        // Only interested in the "ownedElement" property. Either something has
        // been added to the namespace for this model, or removed. In the
        // latter case the "something" will be in the old value of the
        // property, which is the collection of owned elements, and the new value
        // will be the element import describing the model element and the
        // model from which it was removed

        if ("ownedElement".equals(pce.getPropertyName())) {
            List oldOwned = (List) pce.getOldValue();

            Object eo = /*(MElementImport)*/ pce.getNewValue();
            Object  me = Model.getFacade().getModelElement(eo);

            // If the element import is in the old owned, it means it must have
            // been removed. Make sure the associated model element is removed.

            if (oldOwned.contains(eo)) {

                LOG.debug("model removed " + me);

                // Remove a node

                if ((Model.getFacade().isAActor(me))
		    || (Model.getFacade().isAUseCase(me))) {

                    removeNode(me);
                } else if ((Model.getFacade().isAAssociation(me))
			 || (Model.getFacade().isAGeneralization(me))
			 || (Model.getFacade().isAExtend(me))
			 || (Model.getFacade().isAInclude(me))
			 || (Model.getFacade().isADependency(me))) {
                    // Remove an edge
                    removeEdge(me);
                }
            } else {
                // Something was added - nothing for us to worry about
                LOG.debug("model added " + me);
            }
        }
    }

    /**
     * The UID.
     */
    static final long serialVersionUID = -8516841965639203796L;

}
