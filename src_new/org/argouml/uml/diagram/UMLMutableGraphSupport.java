// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.util.Dictionary;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.argouml.model.UmlException;
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;
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

    /**
     * Contains all the nodes in the graphmodel/diagram.
     */
    private Vector nodes = new Vector();

    /**
     * Constains all the edges in the graphmodel/diagram.
     */
    private Vector edges = new Vector();


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


    /**
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
     * @see org.tigris.gef.graph.MutableGraphModel#canConnect(
     *         java.lang.Object, java.lang.Object)
     */
    public boolean canConnect(Object fromP, Object toP) {
        return true;
    }


    /**
     * The connect method without specifying a connection
     * type is unavailable by default.
     *
     * @see org.tigris.gef.graph.MutableGraphModel#connect(
     *         java.lang.Object, java.lang.Object)
     */
    public Object connect(Object fromPort, Object toPort) {
        throw new UnsupportedOperationException("The connect method is "
						+ "not supported");
    }

    /**
     * @return the namespace of the diagram
     */
    public abstract Object getNamespace();

    /**
     * Construct and add a new edge of the given kind and connect
     * the given ports.
     *
     * @param fromPort   The originating port to connect
     *
     * @param toPort     The destination port to connect
     *
     * @param edgeClass  The NSUML type of edge to create.
     *
     * @return           The type of edge created (the same as
     *                   <code>edgeClass</code> if we succeeded,
     *                   <code>null</code> otherwise)
     */
    public Object connect(Object fromPort, Object toPort,
			  java.lang.Class edgeClass) {
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
        Object connection = buildConnection(
                edgeClass, fromPort, style, toPort,
                null, unidirectional,
                model);

        if (connection == null) {
            LOG.debug("Cannot make a " + edgeClass.getName()
		      + " between a " + fromPort.getClass().getName()
		      + " and a " + toPort.getClass().getName());
            return null;
        }

        addEdge(connection);
        LOG.debug("Connection type" + edgeClass.getName()
		  + " made between a " + fromPort.getClass().getName()
		  + " and a " + toPort.getClass().getName());
        return connection;
    }


    /**
     * @see org.tigris.gef.graph.MutableGraphModel#canAddNode(java.lang.Object)
     */
    public boolean canAddNode(Object node) {
        if (node == null) {
            return false;
        }
        if (ModelFacade.isAComment(node)) {
            return true;
        }
        return false;
    }


    /**
     * @see org.tigris.gef.graph.MutableGraphModel#canAddEdge(java.lang.Object)
     */
    public boolean canAddEdge(Object edge) {
        if (edge == null) {
            return false;
        }
       return isConnectionValid(edge.getClass(),
               Model.getUmlHelper().getSource(edge),
               Model.getUmlHelper().getDestination(edge));
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
                Model.getCoreFactory().buildCommentConnection(fromElement, toElement);
        } else {
            try {
                connection = Model.getUmlFactory().buildConnection(
                    edgeType,
                    fromElement,
                    fromStyle,
                    toElement,
                    toStyle,
                    unidirectional,
                    namespace);
            } catch (UmlException ex) {
                // fail silently as we expect users to accidentally drop
                // on to wrong component
            }
        }
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
        if (edgeType.equals(CommentEdge.class) ) {
            return ((ModelFacade.isAComment(fromElement)
                   && ModelFacade.isAModelElement(toElement))
                 || (ModelFacade.isAComment(toElement)
                   && ModelFacade.isAModelElement(fromElement)));
        } else {
            return Model.getUmlFactory().isConnectionValid(
                edgeType,
                fromElement,
                toElement);
        }
    }
}
