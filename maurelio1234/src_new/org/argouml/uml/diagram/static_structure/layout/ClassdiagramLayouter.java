// $Id: ClassdiagramLayouter.java 12924 2007-06-29 19:26:40Z mvw $
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
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.layout.LayoutedObject;
import org.argouml.uml.diagram.layout.Layouter;
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
        private List<ClassdiagramNode> nodes =
                new ArrayList<ClassdiagramNode>();

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
            TreeSet<ClassdiagramNode> ts = new TreeSet<ClassdiagramNode>(nodes);
            if (ts.size() < 2) {
                return null;
            }
            ClassdiagramNode firstNode = ts.first();
            if (!firstNode.isStandalone()) {
                return null;
            }
            ClassdiagramNode lastNode = ts.last();
            if (firstNode.isStandalone() && lastNode.isStandalone()
                    && (firstNode.isPackage() == lastNode.isPackage())
                    && getWidth(gap) <= maxWidth) {
                return null;
            }
            boolean hasPackage = firstNode.isPackage();

            NodeRow newRow = new NodeRow(rowNumber + 1);
            ClassdiagramNode node = null;
            ClassdiagramNode split = null;
            Iterator<ClassdiagramNode> iter;
            int width = 0;
            for (iter = ts.iterator(); iter.hasNext() && width < maxWidth;) {
                node = iter.next();
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
            nodes = new ArrayList<ClassdiagramNode>(ts.headSet(split));
            for (iter = ts.tailSet(split).iterator(); iter.hasNext();) {
                newRow.addNode(iter.next());
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Row split. This row width: " + getWidth(gap)
                        + " next row(s) width: " + newRow.getWidth(gap));
            }
            return newRow;
        }

        /**
         * @return Returns the nodes.
         * @deprecated for 0.25.1 by tfmorris - use {@link #getNodeList()}
         */
        public Vector<ClassdiagramNode> getNodes() {
            return new Vector<ClassdiagramNode>(nodes);
        }


        /**
         * @return Returns the nodes.
         */
        public List<ClassdiagramNode> getNodeList() {
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
        public Iterator<ClassdiagramNode> getSortedIterator() {
            return (new TreeSet<ClassdiagramNode>(nodes)).iterator();
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
            for (ClassdiagramNode node : nodes) {
                result += node.getSize().width + gap;
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
            List<ClassdiagramNode> list = new ArrayList<ClassdiagramNode>();
            for (Iterator<ClassdiagramNode> iter = getSortedIterator(); iter
                    .hasNext();) {
                ClassdiagramNode node = iter.next();
                node.setRank(rowNumber);
                node.setColumn(col++);
                if (!node.getDownNodes().isEmpty()) {
                    numNodesWithDownlinks++;
                    list.add(node);
                }
            }
            int offset = -numNodesWithDownlinks * E_GAP / 2;
            for (ClassdiagramNode node : list ) {
                node.setEdgeOffset(offset);
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
    private ArgoDiagram diagram;

    /**
     * HashMap with figures as key and Nodes as elements.
     */
    private HashMap<Fig, ClassdiagramNode> figNodes =
            new HashMap<Fig, ClassdiagramNode>();

    /**
     * layoutedClassNodes is a convenience which holds a subset of
     * layoutedObjects (only ClassNodes).
     */
    private List<ClassdiagramNode> layoutedClassNodes =
            new ArrayList<ClassdiagramNode>();

    /**
     * Holds all edges - subset of layoutedObjects.
     */
    private List<ClassdiagramEdge> layoutedEdges =
            new ArrayList<ClassdiagramEdge>();

    /**
     * Attribute layoutedObjects holds the objects to layout.
     */
    private List<LayoutedObject> layoutedObjects =
            new ArrayList<LayoutedObject>();

    /**
     * nodeRows contains all DiagramRows of the diagram.
     */
    private List<NodeRow> nodeRows = new ArrayList<NodeRow>();

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
    public ClassdiagramLayouter(ArgoDiagram theDiagram) {
        diagram = theDiagram;
        Iterator<Fig> nodeIter = diagram.getLayer().getContents().iterator();
        while (nodeIter.hasNext()) {
            Fig fig = nodeIter.next();
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
            layoutedClassNodes.add((ClassdiagramNode) obj);
        } else if (obj instanceof ClassdiagramEdge) {
            layoutedEdges.add((ClassdiagramEdge) obj);
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
        for (ClassdiagramNode node : layoutedClassNodes) {
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
        return layoutedObjects.get(index);
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
        for (ClassdiagramEdge edge : layoutedEdges) {
            if (edge instanceof ClassdiagramInheritanceEdge) {
                ClassdiagramNode parent = figNodes.get(edge.getDestFigNode());
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
        List<ClassdiagramNode> uplinks = node.getUpNodes();
        List<ClassdiagramNode> downlinks = node.getDownNodes();
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
                    + " Position: (" + xPosNew + "," + yPos + ") xPos: " 
                    + xPos + " hint: " + node.getPlacementHint());
        }
        if (downlinks.size() == 1
                && downlinks.get(0).getUpNodes().get(0).equals(node)) {
            downlinks.get(0).setPlacementHint(xPosNew);
        }
        xPos = (int) Math.max(node.getPlacementHint() + curW, xPos + xOffset);
    }

    /**
     * Place the NodeRows in the diagram.
     */
    private void placeNodes() {
        // TODO: place comments near connected classes
        // TODO: place from middle towards outer edges? (or place largest 
        // groups first)
        int xInit = 0;
        yPos = getVGap() / 2;
        for (NodeRow row : nodeRows) {
            Iterator<ClassdiagramNode> iNode = row.getSortedIterator();
            xPos = xInit;
            int rowHeight = 0;
            while (iNode.hasNext()) {
                ClassdiagramNode node = iNode.next();
                placeNode(node);
                rowHeight = Math.max(rowHeight, node.getSize().height);
            }
            yPos += rowHeight + getVGap();

        }
        centerParents();
    }

    /**
     * Center parents over their children, working from bottom to top.
     */
    private void centerParents() {
        for (int i = nodeRows.size() - 1; i >= 0; i--) {
            Iterator<ClassdiagramNode> iNode =
                    nodeRows.get(i).getSortedIterator();
            while (iNode.hasNext()) {
                ClassdiagramNode node = iNode.next();
                List<ClassdiagramNode> children = node.getDownNodes();
                if (children.size() > 0) {
                    node.setLocation(new Point(xCenter(children)
                            - node.getSize().width / 2, node.getLocation().y));
                }
            }
            // TODO: Make another pass to deal with overlaps?
        }
    }
    
    /**
     * Compute the horizontal center of a list of nodes.
     * @param nodes the list of nodes
     * @return the computed X coordinate
     */
    private int xCenter(List<ClassdiagramNode> nodes) {
        int left = 9999999;
        int right = 0;
        for (ClassdiagramNode node : nodes) {
            int x = node.getLocation().x;
            left = Math.min(left, x);
            right = Math.max(right, x + node.getSize().width);
        }
        return (right + left) / 2;
    }

    /**
     * Rank the nodes depending on their level (position in hierarchy) and set
     * their weight to achieve a proper node-sequence for the layout. Rows
     * exceeding the maximum row width are split, if standalone nodes are
     * available.
     * <p>
     * Weight the other nodes to determine their columns.
     * <p>
     * TODO: Weighting doesn't appear to be working as intended because multiple
     * groups of children/specializations get intermixed in name order rather
     * than being grouped by their parent/generalization. - tfm - 20070314
     */
    private void rankAndWeightNodes() {
        List<ClassdiagramNode> comments = new ArrayList<ClassdiagramNode>();
        nodeRows.clear();
        TreeSet<ClassdiagramNode> nodeTree =
                new TreeSet<ClassdiagramNode>(layoutedClassNodes);
//        boolean hasPackages = false;
        // TODO: move "package in row" to NodeRow
        for (ClassdiagramNode node : nodeTree) {
//            if (node.isPackage()) {
//                hasPackages = true;
//            } else if (hasPackages) {
//                hasPackages = false;
//                currentRank = -1;
//            }
            if (node.isComment()) {
                comments.add(node);
            } else {
                int rowNum = node.getRank();
                for (int i = nodeRows.size(); i <= rowNum; i++) {
                    nodeRows.add(new NodeRow(rowNum));                    
                }
                nodeRows.get(rowNum).addNode(node);
            }
        }
        for (ClassdiagramNode node : comments) {
            int rowInd =
                    node.getUpNodes().isEmpty() 
                            ? 0 
                            : ((node.getUpNodes().get(0)).getRank());

            nodeRows.get(rowInd).addNode(node);
        }
        for (int row = 0; row < nodeRows.size();) {
            NodeRow diaRow = nodeRows.get(row);
            diaRow.setRowNumber(row++);
            diaRow = diaRow.doSplit(MAX_ROW_WIDTH, H_GAP);
            if (diaRow != null) {
                nodeRows.add(row, diaRow);
            }
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
        HashMap<Fig, List<ClassdiagramInheritanceEdge>> figParentEdges =
                new HashMap<Fig, List<ClassdiagramInheritanceEdge>>();
        for (ClassdiagramNode node : layoutedClassNodes) {
            node.getUpNodes().clear();
            node.getDownNodes().clear();
            figNodes.put(node.getFigure(), node);
        }
        for (ClassdiagramEdge edge : layoutedEdges) {
            Fig parentFig = edge.getDestFigNode();
            ClassdiagramNode child = figNodes.get(edge.getSourceFigNode());
            ClassdiagramNode parent = figNodes.get(parentFig);
            if (edge instanceof ClassdiagramInheritanceEdge) {
                if (parent != null && child != null) {
                    parent.addDownlink(child);
                    child.addUplink(parent);
                    List<ClassdiagramInheritanceEdge> edgeList =
                            figParentEdges.get(parentFig);
                    if (edgeList == null) {
                        edgeList = new ArrayList<ClassdiagramInheritanceEdge>();
                        figParentEdges.put(parentFig, edgeList);
                    }
                    edgeList.add((ClassdiagramInheritanceEdge) edge);
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
            } else if (edge instanceof ClassdiagramAssociationEdge) {
                // Associations not supported, yet
                // TODO: Create appropriate ClassdiagramEdge
            } else {
                LOG.error("Unsupported edge type");
            }
            
        }
    }
}
