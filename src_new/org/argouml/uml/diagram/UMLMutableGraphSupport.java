// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlException;
import org.argouml.model.uml.UmlHelper;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;

import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.Mode;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.graph.MutableGraphSupport;
import org.tigris.gef.presentation.Fig;

import sun.security.action.GetLongAction;


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
     * @deprecated by Linus Tolke as of 0.16. Will be private.
     */
    private static final Logger LOG =
	Logger.getLogger(UMLMutableGraphSupport.class);
    
    /** contains all the nodes in the graphmodel/diagram. */    
    protected Vector _nodes = new Vector();
    /** constains all the edges in the graphmodel/diagram. */    
    protected Vector _edges = new Vector();
    
    
    /** constructor.
     * @see org.tigris.gef.graph.MutableGraphSupport
     */    
    public UMLMutableGraphSupport() {
        super();
    }
    
    /** get all the nodes from the graphmodel/diagram
     * @see org.tigris.gef.graph.MutableGraphSupport#getNodes()
     * @return Vector of nodes in the graphmodel/diagram
     */    
    public Vector getNodes() { return _nodes; }
    
    /** get all the edges from the graphmodel/diagram
     * @return Vector of edges in the graphmodel/diagram
     */    
    public Vector getEdges() { return _edges; }
    
    
    public boolean containsNode(Object node) {
	return _nodes.contains(node);
    }
    
    public boolean constainsEdge(Object edge) {
	return _edges.contains(edge);
    }
    
    /** remove a node from the diagram and notify GEF
     * @param node node to remove
     */    
    public void removeNode(Object node) {
	if (!containsNode(node)) return;
	_nodes.removeElement(node);
	fireNodeRemoved(node);
    }
    
    /** remove an edge from the graphmodel and notify GEF
     * @param edge edge to remove
     */    
    public void removeEdge(Object edge) {
	if (!containsEdge(edge)) return;
	_edges.removeElement(edge);
	fireEdgeRemoved(edge);
    }
    
    /**
     * Assume that anything can be connected to anything unless overridden
     * in a subclass.
     */
    public boolean canConnect(Object fromP, Object toP) {
        return true;
    }


    /** The connect method without specifying a connection
     * type is unavailable by default
     */
    public Object connect(Object fromPort, Object toPort) {        
        throw new UnsupportedOperationException("The connect method is "
						+ "not supported");
    }
    
    public abstract Object getNamespace();

    /** Contruct and add a new edge of the given kind and connect
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
			  java.lang.Class edgeClass)
    {
        Object connection = null;
        try {
            // If this was an association then there will be relevant
            // information to fetch out of the mode arguments.  If it
            // not an association then these will be passed forward
            // harmlessly as null.
            Editor curEditor = Globals.curEditor();
            ModeManager modeManager = curEditor.getModeManager();
            Mode mode = (Mode) modeManager.top();
            Hashtable args = mode.getArgs();
            Object style = args.get("aggregation"); //MAggregationKind
            Boolean unidirectional = (Boolean) args.get("unidirectional");
            // Create the UML connection of the given type between the
            // given model elements.
	    // default aggregation (none)
            connection =
		UmlFactory.getFactory().buildConnection(edgeClass, fromPort,
							style, toPort,
							null, unidirectional);
        } catch (UmlException ex) {
            // fail silently as we expect users to accidentally drop
            // on to wrong component
        }
        
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
        if (node == null) return false;
        if (ModelFacade.isAComment(node)) return true;
        return false;
    }
    
    
    /**
     * @see org.tigris.gef.graph.MutableGraphModel#canAddEdge(java.lang.Object)
     */
    public boolean canAddEdge(Object edge) {   
        if (edge == null) {
            return false;
        }
       return (UmlFactory.getFactory().isConnectionValid(edge.getClass(), UmlHelper.getHelper().getSource(edge), UmlHelper.getHelper().getDestination(edge)));
    }
}
