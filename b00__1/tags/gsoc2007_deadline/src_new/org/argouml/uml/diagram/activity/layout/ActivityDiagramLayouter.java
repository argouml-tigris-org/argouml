// $Id:ActivityDiagramLayouter.java 12924 2007-06-29 19:26:40Z mvw $
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.activity.layout;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.layout.LayoutedObject;
import org.argouml.uml.diagram.layout.Layouter;
import org.tigris.gef.presentation.Fig;

/**
 * A simple layout class for Activity Diagrams. It lays out the states in a
 * single column, densely packed on the assumption that they'll be split into
 * one or more swim lanes by the user. The graph is traversed starting at the
 * initial state and states are placed in the position where they are first
 * encountered. Nothing sophisticated is done for cycles or nodes with multiple
 * incoming or outgoing edges.
 * 
 * @author Tom Morris
 * 
 */
public class ActivityDiagramLayouter implements Layouter {
    
    /*
     * The diagram to be laid out.
     */
    private ArgoDiagram diagram;

    /*
     * List of objects.
     * 
     * NOTE: This methods which read/write this don't appear to be used.
     */
    private List objects = new ArrayList();
    
    /*
     * Point at which to start layout (initial state goes here). The X
     * coordinate must be greater than half the width of the widest figure to
     * be placed (because figures are centered on this location).
     */
    private static final Point STARTING_POINT = new Point(100, 10);
    
    /*
     * Amount to increment Y position by for each node placed. We pack them
     * densely on the assumption that the user is going to split them into at
     * least two swimlanes.
     */
    private static final int OFFSET_Y = 25;
    
    /*
     * FinalState element for ActivityDiagram
     */
    private Object finalState = null;

    /**
     * Construct a new layout engine for an ActivityDiagram.
     * @param d the ActivityDiagram to be laid out.
     */
    public ActivityDiagramLayouter(ArgoDiagram d)  {
        this.diagram = d;
    }

    /*
     * @see org.argouml.uml.diagram.layout.Layouter#add(org.argouml.uml.diagram.layout.LayoutedObject)
     */
    public void add(LayoutedObject object) {
        objects.add(object);
    }

    /*
     * @see org.argouml.uml.diagram.layout.Layouter#remove(org.argouml.uml.diagram.layout.LayoutedObject)
     */
    public void remove(LayoutedObject object) {
        objects.remove(object);
    }

    /*
     * @see org.argouml.uml.diagram.layout.Layouter#getObjects()
     */
    public LayoutedObject[] getObjects() {
        return (LayoutedObject[]) objects.toArray();
    }

    /*
     * @see org.argouml.uml.diagram.layout.Layouter#getObject(int)
     */
    public LayoutedObject getObject(int index) {
        return (LayoutedObject) objects.get(index);
    }

    /*
     * @see org.argouml.uml.diagram.layout.Layouter#layout()
     */
    public void layout() {
        Object first = null;
        // Find our Initial State
        for (Iterator it = diagram.getNodes().iterator(); it.hasNext();) {
            Object node = it.next();
            if (Model.getFacade().isAPseudostate(node) 
                    && Model.getDataTypesHelper().equalsINITIALKind(
                            Model.getFacade().getKind(node))) {
                first = node;
                break;
            } 
        }
        assert first != null;
        assert Model.getFacade().getIncomings(first).isEmpty();

        // Place all the nodes
        int lastIndex = placeNodes(new ArrayList(), first, 0);

        // Place the final state last with a little separation
        Point location = new Point(STARTING_POINT);
        location.y += OFFSET_Y * (lastIndex + 2);
        diagram.getContainingFig(finalState).setLocation(location);
    }
    
    /*
     * Recursively place all nodes pointed to by outgoing transitions.
     * 
     * Because of the recursive algorithm multiple outgoing transitions
     * will end up very lopsided because one entire subgraph will be done
     * before dealing with the other transition(s).
     * 
     * @param seen set of nodes seen so far
     * @param node the node to collect neighbors for
     */
    private int placeNodes(List seen, Object node, int index) {
        if (!seen.contains(node)) {
            seen.add(node);
            if (Model.getFacade().isAFinalState(node)) {
                finalState = node;
            }
            Fig fig = diagram.getContainingFig(node);
            Point location = new Point(STARTING_POINT.x - fig.getWidth() / 2,
                    STARTING_POINT.y + OFFSET_Y * index++);
            //System.out.println("Setting location to " + location);
            fig.setLocation(location);
            for (Iterator it = Model.getFacade().getOutgoings(node).iterator(); 
                    it.hasNext();) {
                index = placeNodes(seen, Model.getFacade().getTarget(it.next()),
                                  index);
            }
        }
        return index;
    }

    /*
     * @see org.argouml.uml.diagram.layout.Layouter#getMinimumDiagramSize()
     */
    public Dimension getMinimumDiagramSize() {
        return new Dimension(
                STARTING_POINT.x + 300,
                STARTING_POINT.y + OFFSET_Y * objects.size()
        );
    }

}
