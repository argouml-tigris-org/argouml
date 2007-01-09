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

package org.argouml.uml.diagram.static_structure.layout;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.uml.diagram.layout.LayoutedObject;
import org.argouml.uml.diagram.layout.Layouter;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.presentation.Fig;

/**
 * This class implements a layout algorithm for class diagrams.<p>
 *
 * The layout process is performed in a row by row way. The position of the
 * nodes in a row are set using the sequence given by the <em>natural order
 * </em> of the nodes.<p>
 *
 * The resulting layout sequence:
 * <ol>
 * <li>Standalone (i.e. without links) nodes first, followed by linked nodes
 * <li>Ordered by node-typ: package, interface, class, <em>other</em>
 * <li>Increasing level in link-hierarchy - root elements first
 * <li>Decreasing amount of weighted links
 * <li>Ascending name of model object
 * </ol>
 *
 * @see ClassdiagramNode#compareTo
 *
 */
public class ClassdiagramLayouter implements Layouter {
    // TODO: make the "magic numbers" configurable
    /**
     * This class keeps all the nodes in one row together and provides basic
     * functionality for them.
     *
     * @author David Gunkel
     */
    private class NodeRow {
        /**
         * Keeps all nodes of this row.
         */
        private List nodes = new ArrayList();

        /**
         * The row number of this row.
         */
        private int rowNumber;

        /**
         * Construct an empty NodeRow with the given row number.
         *
         * @param aRowNumber The row number of this row.
         */
        public NodeRow(int aRowNumber) {
            rowNumber = aRowNumber;
        }

        /**
         * Add a node to this NodeRow.
         *
         * @param node The node to be added
         */
        public void addNode(ClassdiagramNode node) {
            node.setRank(rowNumber);
            node.setColumn(nodes.size());
            nodes.add(node);
        }

        /**
         * Splittable are packages and standalone-nodes. A split is performed,
         * if the maximum width is reached or when a type change occurs (from
         * package to not-package, from standalone to not-standalone).
	 *
         * <ul>
         * <li>packages
         * <li>After standalone
         * </ul>
	 *
         * Split this row into two, if
         * <ul>
         * <li>at least one standalone node is available
         * <li>and the given maximum row width is exceeded
         * <li>or a non-standalone element is detected.
         * </ul>
	 *
         * Return the new NodeRow or null if this row is not split.
         *
         * @param maxWidth
         *            The maximum allowed row width
         * @param gap
         *            The horizontal gab between two nodes
         * @return NodeRow
         */
        public NodeRow doSplit(int maxWidth, int gap) {
            TreeSet ts = new TreeSet(nodes);
            if (ts.size() < 2) {
                return null;
            }
            ClassdiagramNode firstNode = (ClassdiagramNode) ts.first();
            if (!firstNode.isStandalone()) {
                return null;
            }
            ClassdiagramNode lastNode = (ClassdiagramNode) ts.last();
            if (firstNode.isStandalone() && lastNode.isStandalone()
                    && (firstNode.isPackage() == lastNode.isPackage())
                    && getWidth(gap) <= maxWidth) {
                return null;
            }
            boolean hasPackage = firstNode.isPackage();

            NodeRow newRow = new NodeRow(rowNumber + 1);
            ClassdiagramNode node = null;
            ClassdiagramNode split = null;
            Iterator iter;
            int width = 0;
            for (iter = ts.iterator(); iter.hasNext() && width < maxWidth;) {
                node = (ClassdiagramNode) iter.next();
                // split =
                //     (split == null || split.isStandalone()) ? node : split;
                split =
                    (split == null
                            || (hasPackage && split.isPackage() == hasPackage)
                            || split.isStandalone())
                    ? node
                    : split;
                width += node.getSize().width + gap;
            }
            nodes = new ArrayList(ts.headSet(split));
            for (iter = ts.tailSet(split).iterator(); iter.hasNext();) {
                newRow.addNode((ClassdiagramNode) iter.next());
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Row split. This row width: " + getWidth(gap)
                        + " next row(s) width: " + newRow.getWidth(gap));
            }
            return newRow;
        }

        /**
         * @return Returns the nodes.
         * @deprecated use {@link #getNodeList()}
         */
        public Vector getNodes() {
            return new Vector(nodes);
        }


        /**
         * @return Returns the nodes.
         */
        public List getNodeList() {
            return nodes;
        }

        /**
         * @return Returns the rowNumber.
         */
        public int getRowNumber() {
            return rowNumber;
        }

        /**
         * Get an Iterator for the nodes of this row, sorted by their natural
         * order.
         *
         * @return Iterator for sorted nodes
         */
        public Iterator getSortedIterator() {
            return (new TreeSet(nodes)).iterator();
        }

        /**
         * Get the width for this row using the given horizontal gap between
         * nodes.
         *
         * @param gap The horizontal gap between nodes.
         * @return The width of this row
         */
        public int getWidth(int gap) {
            int result = 0;
            for (Iterator i = nodes.iterator(); i.hasNext();) {
                result += ((ClassdiagramNode) i.next()).getSize().width + gap;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Width of row " + rowNumber + ": " + result);
            }
            return result;
        }

        /**
         * Set the row number of this row.
         *
         * @param rowNum The rowNumber to set.
         */
        public void setRowNumber(int rowNum) {
            this.rowNumber = rowNum;
            adjustRowNodes();
        }

        /**
         * Adjust the properties for all nodes in this row: rank,
         * column, offset for edges.
         */
        private void adjustRowNodes() {
            int col = 0;
            int numNodesWithDownlinks = 0;
            List v = new ArrayList();
            for (Iterator iter = getSortedIterator(); iter.hasNext();) {
                ClassdiagramNode node = (ClassdiagramNode) iter.next();
                node.setRank(rowNumber);
                node.setColumn(col++);
                if (!node.getDownlinks().isEmpty()) {
                    numNodesWithDownlinks++;
                    v.add(node);
                }
            }
            int offset = -numNodesWithDownlinks * E_GAP / 2;
            for (Iterator iter = v.iterator(); iter.hasNext();) {
                ((ClassdiagramNode) iter.next()).setEdgeOffset(offset);
                offset += E_GAP;
            }
        }
    }

    /**
     * Constant value for the gap between edges.
     */
    private static final int E_GAP = 5;

    /**
     * Constant value for the horizontal gap between nodes.
     */
    private static final int H_GAP = 80;

    /**
     * Logger for logging events.
     */
    private static final Logger LOG =
	Logger.getLogger(ClassdiagramLayouter.class);

    /**
     * Constant value for the maximum row width.
     */
    // TODO: this should be a configurable property
    private static final int MAX_ROW_WIDTH = 1200;

    /**
     * Constant value for the vertical gap between nodes.
     */
    private static final int V_GAP = 80;

    // Attributes

    /**
     * The diagram that will be layouted.
     */
    private UMLDiagram diagram;

    /**
     * HashMap with figures as key and Nodes as elements.
     */
    private HashMap figNodes = new HashMap();

    /**
     * layoutedClassNodes is a convenience which holds a subset of
     * layoutedObjects (only ClassNodes).
     */
    private List layoutedClassNodes = new ArrayList();

    /**
     * Holds all edges - subset of layoutedObjects.
     */
    private List layoutedEdges = new ArrayList();

    /**
     * Attribute layoutedObjects holds the objects to layout.
     */
    private List layoutedObjects = new ArrayList();

    /**
     * nodeRows contains all DiagramRows of the diagram.
     */
    private List nodeRows = new ArrayList();

    /**
     * internal.
     */
    private int xPos;

    /**
     * internal.
     */
    private int yPos;

    /**
     * Constructor for the layouter. Takes a diagram as input to extract all
     * LayoutedObjects, which will be layouted.
     *
     * @param theDiagram The diagram to layout.
     */
    public ClassdiagramLayouter(UMLDiagram theDiagram) {
        diagram = theDiagram;
        Iterator nodeIter = diagram.getLayer().getContents().iterator();
        while (nodeIter.hasNext()) {
            Fig fig = (Fig) nodeIter.next();
            if (fig.getEnclosingFig() == null) {
                add(ClassdiagramModelElementFactory.SINGLETON.getInstance(fig));
            }
        }
    }

    /**
     * Add an object to layout.
     *
     * @param obj represents the object to layout.
     */
    public void add(LayoutedObject obj) {
        // TODO: check for duplicates (is this possible???)
        layoutedObjects.add(obj);
        if (obj instanceof ClassdiagramNode) {
            layoutedClassNodes.add(obj);
        } else if (obj instanceof ClassdiagramEdge) {
            layoutedEdges.add(obj);
        }
    }

    /**
     * Get the horizontal gap between nodes.
     *
     * @return The horizontal gap between nodes.
     */
    private int getHGap() {
        return H_GAP;
    }

    /**
     * Operation getMinimumDiagramSize returns the minimum diagram size after
     * the layout process.
     *
     * @return The minimum diagram size after the layout process.
     */
    public Dimension getMinimumDiagramSize() {
        int width = 0, height = 0;
        int hGap2 = getHGap() / 2;
        int vGap2 = getVGap() / 2;
        for (Iterator iter = layoutedClassNodes.iterator(); iter.hasNext();) {
            ClassdiagramNode node = (ClassdiagramNode) iter.next();
            width =
		Math.max(width,
			 node.getLocation().x
			 + (int) node.getSize().getWidth() + hGap2);
            height =
		Math.max(height,
			 node.getLocation().y
			 + (int) node.getSize().getHeight() + vGap2);
        }
        return new Dimension(width, height);
    }

    /**
     * Operation getObject returns a object with a given index from the
     * layouter.
     *
     * @param index
     *            represents the index of this object in the layouter.
     * @return The LayoutedObject for the given index.
     */
    public LayoutedObject getObject(int index) {
        return (LayoutedObject) (layoutedObjects.get(index));
    }

    /**
     * Operation getObjects returns all the objects currently participating in
     * the layout process.
     *
     * @return An array holding all the object in the layouter.
     */
    public LayoutedObject[] getObjects() {
        return (LayoutedObject[]) layoutedObjects.toArray();
    }

    /**
     * Get the vertical gap between nodes.
     *
     * @return The vertical gap between nodes.
     */
    private int getVGap() {
        return V_GAP;
    }

    /**
     * Operation layout implements the actual layout algorithm.
     */
    public void layout() {
        long s = System.currentTimeMillis();
        setupLinks();
        rankAndWeightNodes();
        placeNodes();
        placeEdges();
        LOG.debug("layout duration: " + (System.currentTimeMillis() - s));
    }

    /**
     * All layoutedObjects of type "Edge" are placed using an
     * edge-type specific layout algorithm. The offset from a
     * <em>centered</em> edge is taken from the parent node to avoid
     * overlaps.
     *
     * @see ClassdiagramEdge
     */
    private void placeEdges() {
        ClassdiagramEdge.setVGap(getVGap());
        ClassdiagramEdge.setHGap(getHGap());
        for (Iterator iter = layoutedEdges.iterator(); iter.hasNext();) {
            ClassdiagramEdge edge = (ClassdiagramEdge) iter.next();
            if (edge instanceof ClassdiagramInheritanceEdge) {
                ClassdiagramNode parent =
		    (ClassdiagramNode) figNodes.get(edge.getDestFigNode());
                ((ClassdiagramInheritanceEdge) edge).setOffset(parent
                        .getEdgeOffset());
            }
            edge.layout();

        }
    }

    /**
     * Set the placement coordinate for a given node.
     *
     * @param node To be placed.
     */
    private void placeNode(ClassdiagramNode node) {
        List uplinks = node.getUplinks();
        List downlinks = node.getDownlinks();
        int curW = node.getSize().width;
        double xOffset = node.getSize().width + getHGap();
        int bumpX = getHGap() / 2; // (xOffset - curW) / 2;
        int xPosNew =
	    Math.max(xPos + bumpX,
		     uplinks.size() == 1 ? node.getPlacementHint() : -1);
        node.setLocation(new Point(xPosNew, yPos));
        if (LOG.isDebugEnabled()) {
            LOG.debug("placeNode - Row: " + node.getRank() + " Col: "
                    + node.getColumn() + " Weight: " + node.getWeight()
                    + " Position: (" + xPosNew + "," + yPos + ")");
        }
        if (downlinks.size() == 1
                && ((ClassdiagramNode) downlinks.get(0)).getUplinks()
                        .firstElement().equals(node)) {
            ((ClassdiagramNode) downlinks.get(0)).setPlacementHint(xPosNew);
        }
        xPos = (int) Math.max(node.getPlacementHint() + curW, xPos + xOffset);
    }

    /**
     * Place the NodeRows in the diagram.
     */
    private void placeNodes() {
        // TODO: place comments near connected classes
        // TODO: place from middle to out
        int xInit = 0;
        yPos = getVGap() / 2;
        for (Iterator iRow = nodeRows.iterator(); iRow.hasNext();) {
            Iterator iNode = ((NodeRow) iRow.next()).getSortedIterator();
            xPos = xInit;
            int rowHeight = 0;
            while (iNode.hasNext()) {
                ClassdiagramNode node = (ClassdiagramNode) iNode.next();
                placeNode(node);
                rowHeight = Math.max(rowHeight, node.getSize().height);
            }
            yPos += rowHeight + getVGap();

        }
    }

    /**
     * Rank the nodes depending on their level (position in hierarchy) and set
     * their weight to achieve a proper node-sequence for the layout. Rows
     * exceeding the maximum row width are split, if standalone nodes are
     * available.<p>
     *
     * Weight the other nodes to determine their columns.
     */
    private void rankAndWeightNodes() {
        int row = -1;
        int currentRank = -1;
        List comments = new ArrayList();
        nodeRows.clear();
        NodeRow nodeRow = new NodeRow(0);
        TreeSet nodeTree = new TreeSet(layoutedClassNodes);
//        boolean hasPackages = false;
        // TODO: move "package in row" to NodeRow
        for (Iterator iNode = nodeTree.iterator(); iNode.hasNext();) {
            ClassdiagramNode node = (ClassdiagramNode) iNode.next();
//            if (node.isPackage()) {
//                hasPackages = true;
//            } else if (hasPackages) {
//                hasPackages = false;
//                currentRank = -1;
//            }
            if (node.isComment()) {
                comments.add(node);
            } else {
                if (node.getRank() > currentRank) {
                    currentRank = node.getRank();
                    nodeRow = new NodeRow(++row);
                    nodeRows.add(nodeRow);
                }
                nodeRow.addNode(node);
            }
        }
        for (Iterator iter = comments.iterator(); iter.hasNext();) {
            ClassdiagramNode node = (ClassdiagramNode) iter.next();
            int rowInd =
		node.getUplinks().isEmpty()
		? 0
		: (((ClassdiagramNode) node.getUplinks().firstElement())
		   .getRank());

            ((NodeRow) nodeRows.get(rowInd)).addNode(node);
        }
        for (row = 0; row < nodeRows.size();) {
            NodeRow diaRow = (NodeRow) nodeRows.get(row);
            diaRow.setRowNumber(row++);
            diaRow = diaRow.doSplit(MAX_ROW_WIDTH, H_GAP);
            if (diaRow != null) {
                nodeRows.add(row, diaRow);
            }
            
            // TODO:  Add another pass to try and make diagram
            // as square as possible? (Rather than too wide)
        }
    }
    /**
     * Remove an object from the layout process.
     *
     * @param obj represents the object to remove.
     */
    public void remove(LayoutedObject obj) {
        layoutedObjects.remove(obj);
    }

    /**
     * Set the up- and downlinks for each node based on the edges which are
     * shown in the diagram.
     */
    private void setupLinks() {
        figNodes.clear();
        HashMap figParentEdges = new HashMap();
        for (Iterator iter = layoutedClassNodes.iterator(); iter.hasNext();) {
            ClassdiagramNode node = (ClassdiagramNode) iter.next();
            node.getUplinks().clear();
            node.getDownlinks().clear();
            figNodes.put(node.getFigure(), node);
        }
        for (Iterator iter = layoutedEdges.iterator(); iter.hasNext();) {
            ClassdiagramEdge edge = (ClassdiagramEdge) iter.next();
            Fig parentFig = edge.getDestFigNode();
            ClassdiagramNode child =
		(ClassdiagramNode) figNodes.get(edge.getSourceFigNode());
            ClassdiagramNode parent =
		(ClassdiagramNode) figNodes.get(parentFig);
            if (edge instanceof ClassdiagramInheritanceEdge) {
                if (parent != null && child != null) {
                    parent.addDownlink(child);
                    child.addUplink(parent);
                    List v = (List) figParentEdges.get(parentFig);
                    if (v == null) {
                        v = new ArrayList();
                        figParentEdges.put(parentFig, v);
                    }
                    v.add(edge);
                } else {
                    LOG.error("Edge with missing end(s): " + edge);
                }
            } else if (edge instanceof ClassdiagramNoteEdge) {
                if (parent.isComment()) {
                    parent.addUplink(child);
                } else if (child.isComment()) {
                    child.addUplink(parent);
                } else {
                    LOG.error("Unexpected parent/child constellation for edge: "
                                    + edge);
                }
            } // else {
            // Associations not supported, yet
            // TODO: Create appropriate ClassdiagramEdge
            // }
        }
    }
}
