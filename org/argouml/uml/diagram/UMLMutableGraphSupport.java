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

/*
 * UMLDiagramGraphModel.java
 *
 * Created on November 14, 2002, 10:20 PM
 */

package org.argouml.uml.diagram;

import org.tigris.gef.graph.MutableGraphSupport;

import java.util.Vector;

/** UMLMutableGraphSupport is a helper class which extends MutableGraphSupport to
 * provide additional helper and common methods for UML Diagrams.
 * @author mkl@tigris.org
 */
public abstract class UMLMutableGraphSupport extends MutableGraphSupport {
    
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
            return _nodes.contains(node); }
    
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
}
