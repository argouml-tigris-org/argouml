// $Id: UMLMutableGraphSupport.java 12908 2007-06-24 18:22:05Z mvw $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.diagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.DiDiagram;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.uml.CommentEdge;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Mode;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.graph.MutableGraphSupport;


/**
 * UMLMutableGraphSupport is a helper class which extends
 * MutableGraphSupport to provide additional helper and common methods
 * for UML Diagrams.
 *
 * @author mkl@tigris.org
 * @since November 14, 2002, 10:20 PM
 */
public abstract class UMLMutableGraphSupport extends MutableGraphSupport {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(UMLMutableGraphSupport.class);

    private DiDiagram diDiagram;

    /**
     * Contains all the nodes in the graphmodel/diagram.
     */
    private Vector nodes = new Vector();

    /**
     * Constains all the edges in the graphmodel/diagram.
     */
    private Vector edges = new Vector();

    /**
     * The "home" UML model of this diagram, not all ModelElements in this
     * graph are in the home model, but if they are added and don't
     * already have a model, they are placed in the "home model".
     * Also, elements from other models will have their FigNodes add a
     * line to say what their model is.
     */
    private Object homeModel;

    /**
     * The project this graph model is in.
     */
    private Project project;
    
    /**
     * Constructor.
     *
     * @see org.tigris.gef.graph.MutableGraphSupport
     */
    public UMLMutableGraphSupport() {
        super();
    }

    /**
     * Get all the nodes from the graphmodel/diagram.
     *
     * @see org.tigris.gef.graph.MutableGraphSupport#getNodes()
     * @return Vector of nodes in the graphmodel/diagram
     */
    public List getNodes() {
        return nodes;
    }

    /**
     * Get all the edges from the graphmodel/diagram.
     *
     * @return Vector of edges in the graphmodel/diagram
     */
    public List getEdges() {
        return edges;
    }
    
    /*
     * @see org.tigris.gef.graph.MutableGraphModel#containsNode(java.lang.Object)
     */
    public boolean containsNode(Object node) {
	return nodes.contains(node);
    }

    /**
     * @param edge the candidate edge
     * @return true if it is contained
     */
    public boolean constainsEdge(Object edge) {
	return edges.contains(edge);
    }

    /**
     * Remove a node from the diagram and notify GEF.
     *
     * @param node node to remove
     */
    public void removeNode(Object node) {
	if (!containsNode(node)) {
	    return;
	}
	nodes.removeElement(node);
	fireNodeRemoved(node);
    }

    /**
     * Remove an edge from the graphmodel and notify GEF.
     *
     * @param edge edge to remove
     */
    public void removeEdge(Object edge) {
	if (!containsEdge(edge)) {
	    return;
	}
	edges.removeElement(edge);
	fireEdgeRemoved(edge);
    }

    /**
     * Assume that anything can be connected to anything unless overridden
     * in a subclass.
     *
     * {@inheritDoc}
     */
    public boolean canConnect(Object fromP, Object toP) {
        return true;
    }


    /**
     * The connect method without specifying a connection
     * type is unavailable in the ArgoUML implmentation.
     *
     * {@inheritDoc}
     */
    public Object connect(Object fromPort, Object toPort) {
        throw new UnsupportedOperationException(
                "The connect method is not supported");
    }

    /**
     * Get the homemodel.
     *
     * @return the homemodel
     */
    public Object getHomeModel() {
        return homeModel;
    }

    /**
     * Set the homemodel.
     *
     * @param ns the namespace
     */
    public void setHomeModel(Object ns) {
        if (!Model.getFacade().isANamespace(ns)) {
            throw new IllegalArgumentException();
        }
        homeModel = ns;
    }

    /**
     * The connect method specifying a connection
     * type by class is unavailable in the ArgoUML implementation.
     * TODO: This should be unsupported. Use the 3 Object version
     *
     * {@inheritDoc}
     */
    public Object connect(Object fromPort, Object toPort, Class edgeClass) {
        return connect(fromPort, toPort, (Object) edgeClass);
    }

    /**
     * Construct and add a new edge of the given kind and connect
     * the given ports.
     *
     * @param fromPort   The originating port to connect
     *
     * @param toPort     The destination port to connect
     *
     * @param edgeType  The type of edge to create. This is one of the types
     *                  returned by the methods of
     *                  <code>org.argouml.model.MetaTypes</code>
     *
     * @return           The type of edge created (the same as
     *                   <code>edgeClass</code> if we succeeded,
     *                   <code>null</code> otherwise)
     */
    public Object connect(Object fromPort, Object toPort, Object edgeType) {
        // If this was an association then there will be relevant
        // information to fetch out of the mode arguments.  If it
        // not an association then these will be passed forward
        // harmlessly as null.
        Editor curEditor = Globals.curEditor();
        ModeManager modeManager = curEditor.getModeManager();
        Mode mode = modeManager.top();
        Dictionary args = mode.getArgs();
        Object style = args.get("aggregation"); //MAggregationKind
        Boolean unidirectional = (Boolean) args.get("unidirectional");
        Object model =
            ProjectManager.getManager().getCurrentProject().getModel();

        // Create the UML connection of the given type between the
        // given model elements.
        // default aggregation (none)
        Object connection =
            buildConnection(
                edgeType, fromPort, style, toPort,
                null, unidirectional,
                model);

        if (connection == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Cannot make a " + edgeType
                        + " between a " + fromPort.getClass().getName()
                        + " and a " + toPort.getClass().getName());
            }
            return null;
        }

        addEdge(connection);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Connection type" + edgeType
                      + " made between a " + fromPort.getClass().getName()
                      + " and a " + toPort.getClass().getName());
        }
        return connection;
    }

    /**
     * Construct and add a new edge of the given kind and connect
     * the given ports.
     *
     * @param fromPort   The originating port to connect
     *
     * @param toPort     The destination port to connect
     *
     * @param edgeType   An indicator of the edge type to create.
     *
     * @param styleAttributes key/value pairs from which to style the edge.
     *
     * @return           The type of edge created (the same as
     *                   <code>edgeClass</code> if we succeeded,
     *                   <code>null</code> otherwise)
     */
    public Object connect(Object fromPort, Object toPort, Object edgeType,
            Map styleAttributes) {
        return null;
    }


    /*
     * @see org.tigris.gef.graph.MutableGraphModel#canAddNode(java.lang.Object)
     */
    public boolean canAddNode(Object node) {
        if (node == null) {
            return false;
        }
        if (Model.getFacade().isAComment(node)) {
            return true;
        }
        return false;
    }

    /**
     * Return the source end of an edge.
     *
     * @param edge  The edge for which we want the source port.
     *
     * @return      The source port for the edge, or <code>null</code> if the
     *              edge given is of the wrong type or has no source defined.
     */
    public Object getSourcePort(Object edge) {

        if (edge instanceof CommentEdge) {
            return ((CommentEdge) edge).getSource();
        } else if (Model.getFacade().isARelationship(edge)
            || Model.getFacade().isATransition(edge)
            || Model.getFacade().isAAssociationEnd(edge))  {
            return Model.getUmlHelper().getSource(edge);
        } else if (Model.getFacade().isALink(edge)) {
            return Model.getCommonBehaviorHelper().getSource(edge);
        }

        // Don't know what to do otherwise

        LOG.error(this.getClass().toString() + ": getSourcePort("
                + edge.toString() + ") - can't handle");

        return null;
    }


    /**
     * Return the destination end of an edge.
     *
     * @param edge  The edge for which we want the destination port.
     *
     * @return      The destination port for the edge, or <code>null</code> if
     *              the edge given is otf the wrong type or has no destination
     *              defined.
     */
    public Object getDestPort(Object edge) {
        if (edge instanceof CommentEdge) {
            return ((CommentEdge) edge).getDestination();
        } else if (Model.getFacade().isAAssociation(edge)) {
            Vector conns = new Vector(Model.getFacade().getConnections(edge));
            return conns.elementAt(1);
        } else if (Model.getFacade().isARelationship(edge)
                || Model.getFacade().isATransition(edge)
                || Model.getFacade().isAAssociationEnd(edge)) {
            return Model.getUmlHelper().getDestination(edge);
        } else if (Model.getFacade().isALink(edge)) {
            return Model.getCommonBehaviorHelper().getDestination(edge);
        }

        // Don't know what to do otherwise

        LOG.error(this.getClass().toString() + ": getDestPort("
                + edge.toString() + ") - can't handle");

        return null;
    }


    /*
     * @see org.tigris.gef.graph.MutableGraphModel#canAddEdge(java.lang.Object)
     */
    public boolean canAddEdge(Object edge) {
        if (edge instanceof CommentEdge) {
            CommentEdge ce = (CommentEdge) edge;
            return isConnectionValid(CommentEdge.class,
                    ce.getSource(),
                    ce.getDestination());
        } else if (edge != null 
                && Model.getUmlFactory().isConnectionType(edge)) {
            return isConnectionValid(edge.getClass(),
                Model.getUmlHelper().getSource(edge),
                Model.getUmlHelper().getDestination(edge));
        }
        return false;
    }

    /*
     * @see org.tigris.gef.graph.MutableGraphModel#addNodeRelatedEdges(java.lang.Object)
     */
    public void addNodeRelatedEdges(Object node) {
        // Commentlinks for comments. Iterate over all the comment links
        // to find the comment and annotated elements.

        Collection cmnt = new ArrayList();
        if (Model.getFacade().isAComment(node)) {
            cmnt.addAll(Model.getFacade().getAnnotatedElements(node));
        }
        // TODO: Comments are on Element in UML 2.x
        if (Model.getFacade().isAModelElement(node)) {
            cmnt.addAll(Model.getFacade().getComments(node));
        }
        Iterator iter = cmnt.iterator();
        while (iter.hasNext()) {
            Object ae = iter.next();
            CommentEdge ce = new CommentEdge(node, ae);
            if (canAddEdge(ce)) {
                addEdge(ce);
            }
        }
    }

    /**
     * Create an edge of the given type and connect it to the
     * given nodes.
     *
     * @param edgeType       the UML object type of the connection
     * @param fromElement    the UML object for the "from" element
     * @param fromStyle      the aggregationkind for the connection
     *                       in case of an association
     * @param toElement      the UML object for the "to" element
     * @param toStyle        the aggregationkind for the connection
     *                       in case of an association
     * @param unidirectional for association and associationrole
     * @param namespace      the namespace to use if it can't be determined
     * @return               the newly build connection (UML object)
     */
    protected Object buildConnection(
            Object edgeType,
            Object fromElement,
            Object fromStyle,
            Object toElement,
            Object toStyle,
            Object unidirectional,
            Object namespace) {

        Object connection = null;
        if (edgeType == CommentEdge.class) {
            connection =
                buildCommentConnection(fromElement, toElement);
        } else {
            try {
                connection =
                    Model.getUmlFactory().buildConnection(
                            edgeType,
                            fromElement,
                            fromStyle,
                            toElement,
                            toStyle,
                            unidirectional,
                            namespace);
        	LOG.info("Created " + connection + " between " + fromElement + " and " + toElement);
            } catch (UmlException ex) {
                // fail silently as we expect users to accidentally drop
                // on to wrong component
            } catch (IllegalArgumentException iae) {
                // idem, e.g. for a generalization with leaf/root object
                // TODO: but showing the message in the statusbar would help
        	// TODO: IllegalArgumentException should not be used for
        	// events we expect to happen. We need a different way of
        	// catching well-formedness rules.
        	LOG.warn("IllegalArgumentException caught", iae);
            }
        }
        return connection;
    }

    /**
     * Builds the model behind a connection between a comment and
     * the annotated modelelement.
     *
     * @param from The comment or annotated element.
     * @param to The comment or annotated element.
     * @return A commentEdge representing the model behind the connection
     *         between a comment and an annotated modelelement.
     */
    public CommentEdge buildCommentConnection(Object from, Object to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Either fromNode == null "
                                       + "or toNode == null");
        }
        Object comment = null;
        Object annotatedElement = null;
        if (Model.getFacade().isAComment(from)) {
            comment = from;
            annotatedElement = to;
        } else {
            if (Model.getFacade().isAComment(to)) {
                comment = to;
                annotatedElement = from;
            } else {
                return null;
            }
        }

        CommentEdge connection = new CommentEdge(from, to);
        Model.getCoreHelper().addAnnotatedElement(comment, annotatedElement);
        return connection;

    }

    /**
     * Checks if some type of edge is valid to connect two
     * types of node.
     *
     * @param edgeType  the UML object type of the connection
     * @param fromElement     the UML object type of the "from"
     * @param toElement       the UML object type of the "to"
     * @return true if valid
     */
    protected boolean isConnectionValid(
            Object edgeType,
            Object fromElement,
            Object toElement) {

        if (!nodes.contains(fromElement) || !nodes.contains(toElement)) {
            // The connection is not valid unless both nodes are
            // in this graph model.
            return false;
        }

        if (edgeType.equals(CommentEdge.class)) {
            return ((Model.getFacade().isAComment(fromElement)
                   && Model.getFacade().isAModelElement(toElement))
                 || (Model.getFacade().isAComment(toElement)
                   && Model.getFacade().isAModelElement(fromElement)));
        }
        return Model.getUmlFactory().isConnectionValid(
                edgeType,
                fromElement,
                toElement,
                true);
    }

    /**
     * Package scope. Only the factory is supposed to set this.
     * @param dd
     */
    void setDiDiagram(DiDiagram dd) {
        this.diDiagram = dd;
    }

    /**
     * Get the object that represents this diagram
     * in the DiagramInterchangeModel.
     *
     * @return the Diagram Interchange Diagram.
     */
    public DiDiagram getDiDiagram() {
        return diDiagram;
    }

    /**
     * Return true if the current targets may be removed from the diagram.
     *
     * @param figs a collection with the selected figs
     * @return true if the targets may be removed
     */
    public boolean isRemoveFromDiagramAllowed(Collection figs) {
        return !figs.isEmpty();
    }
    
    /**
     * Set the project that the graph model is inside.
     * @param p the project
     */
    public void setProject(Project p) {
	this.project = p;
    }
    
    /**
     * Get the project that the graph model is inside.
     * @return the project
     */
    public Project getProject() {
	return project;
    }
}
