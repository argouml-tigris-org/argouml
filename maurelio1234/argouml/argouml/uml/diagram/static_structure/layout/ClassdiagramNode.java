// $Id: ClassdiagramNode.java 12700 2007-05-28 06:02:34Z tfmorris $
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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.argouml.uml.diagram.layout.LayoutedNode;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;
import org.argouml.uml.diagram.static_structure.ui.FigPackage;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;

/**
 * This class represents a node in the classdiagram (a class, interface or
 * package).
 * <p>
 * 
 * Things a node has to know:
 * <ul>
 * <li>Up- and downlinks for positioning in the hierarchy
 * <li>Weight of this node. This weight has to be strongly influenced by the
 * parent-nodes, because otherwise the order of nodes in the current row will
 * not be compatible with the order of the nodes in the row above.
 * </ul>
 */
class ClassdiagramNode implements LayoutedNode, Comparable {

    /**
     * Constant to be used as an initializer when this node is not placed at an
     * column.
     */
    public static final int NOCOLUMN = -1;

    /**
     * Constant to be used as an initializer when this node has no rank assigned
     * yet.
     */
    public static final int NORANK = -1;

    /**
     * Constant to be used as an initializer when this node has no weight.
     */
    public static final int NOWEIGHT = -1;

    // Attributes

    /**
     * Attribute column represents the current column of this node.
     */
    private int column = NOCOLUMN;

    /**
     * Attribute downlinks represents the nodes that contain the figures, which
     * are sources of edges with the figure of this node as destination.
     */
    private List<ClassdiagramNode> downlinks = 
        new ArrayList<ClassdiagramNode>();

    /**
     * Offset used for edges, which have this node as the "upper" node.
     */
    private int edgeOffset = 0;

    /**
     * Attribute figure represents the figure that this ClassdiagramNode
     * represents during the layout process.
     */
    private FigNode figure = null;

    /**
     * Attribute placementHint keeps information about preferred positioning.
     */
    private int placementHint = -1;

    /**
     * Attribute rank represents the current rank (i.e. row) of this node.
     */
    private int rank = NORANK;

    /**
     * Attribute uplinks represents the nodes that contain the figures, which
     * are destinations of edges with the figure of this node as source.
     */
    private List<ClassdiagramNode> uplinks = new ArrayList<ClassdiagramNode>();

    /**
     * This attribute stores the 'weight' of this node. This is a computed
     * attribute that is used during the horizontal placement process. It's
     * based on the position of the 'uplinked' objects. The actual purpose is to
     * minimize the number of link crossings in the diagram. Since we don't
     * compute the actual number of link crossings, we look where our uplinked
     * objects are, and then try to place our object in a way, that we can
     * expect to have a minimal number of crossings.
     */
    private float weight = NOWEIGHT;

    private static final float UPLINK_FACTOR = 5;

    /**
     * Operation ClassdiagramNode creates a new ClassdiagramNode.
     * 
     * @param f
     *            represents the figure in the diagram, that peers this layout
     *            node.
     */
    public ClassdiagramNode(FigNode f) {
        setFigure(f);
    }

    /**
     * Operation setDownlinks changes the value of the attribute _downlinks.
     * 
     * @param newDownlink
     *            Represents the new value of _downlinks.
     */
    public void addDownlink(ClassdiagramNode newDownlink) {
        downlinks.add(newDownlink);
    }

    /**
     * Add a constant to the rank of this node.
     * 
     * @param n
     *            The value to add.
     */
    public void addRank(int n) {
        setRank(n + getRank());
    }

    /**
     * Add an uplink to this node.
     * 
     * @param newUplink
     *            represents the new uplinks.
     */
    public void addUplink(ClassdiagramNode newUplink) {
        uplinks.add(newUplink);
    }

    /**
     * Calculate the weight of this node. The function distinguishes between
     * note-nodes and standard-nodes, because a note shall be positioned to the
     * right of its first related node, if it exists. Therefor the weight is a
     * function of the weight of the related node. For standard-nodes the weight
     * is a function of up-/downlinks, column and uplink factor.
     * 
     * @return The weight of this node.
     */
    public float calculateWeight() {
        weight = 0;
        float w = getSubtreeWeight();
        for (ClassdiagramNode node : uplinks) {
            weight = Math.max(weight, node.getWeight()
                    * UPLINK_FACTOR
                    * (1 + 1 / Math
                            .max(1, node.getColumn() + UPLINK_FACTOR)));
        }
        weight += w + 1 / Math.max(1, getColumn() + UPLINK_FACTOR);
        return weight;
    }

    /**
     * The "natural order" for ClassdiagramNodes is defined by the following
     * order.
     * <ul>
     * <li>First standalone, then linked nodes
     * <li>First Packages, then Interfaces/Classes/Notes
     * <li>increasing rank (rownumber)
     * <li>decreasing weight
     * <li>name of model object
     * <li>increasing hashcode (for uniqueness)
     * </ul>
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object arg0) {
        ClassdiagramNode node = (ClassdiagramNode) arg0;
        int result = 0;
        result =
                Boolean.valueOf(node.isStandalone()).compareTo(
                        Boolean.valueOf(isStandalone()));
        if (result == 0) {
            result = this.getTypeOrderNumer() - node.getTypeOrderNumer();
        }
        if (result == 0) {
            result = this.getRank() - node.getRank();
        }
        if (result == 0) {
            result = (int) Math.signum(node.getWeight() - this.getWeight());
        }
        if (result == 0) {
            result = String.valueOf(this.getFigure().getOwner()).compareTo(
                    String.valueOf(node.getFigure().getOwner()));
        }
        if (result == 0) {
            result = node.hashCode() - this.hashCode();
        }
        //System.out.println(result + " node1: " + this + ", node2 " + node);
        return result;
    }

    /**
     * Operation getColumn returns the value of the attribute _column.
     * 
     * @return The value of the attribute _column.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Get the downlinks of this node.
     * 
     * @return The downlinks of this node.
     * @deprecated for 0.25.4 by tfmorris - use {@link #getDownNodes()}
     */
    public Vector<ClassdiagramNode> getDownlinks() {
        return new Vector<ClassdiagramNode>(downlinks);
    }

    /**
     * Get the downlinks of this node.
     * 
     * @return The downlinks of this node.
     */
    public List<ClassdiagramNode> getDownNodes() {
        return downlinks;
    }
    
    /**
     * Get the offset which shall be used for edges with this node as parent.
     * 
     * @return The offset
     */
    public int getEdgeOffset() {
        return edgeOffset;
    }

    /**
     * Get the underlying figure of this node.
     * 
     * @return The figure.
     */
    public FigNode getFigure() {
        return figure;
    }

    /**
     * Get the level in the inheritance hierarchy for this node.
     * 
     * @return The level.
     */
    public int getLevel() {
        int result = 0;
        for (ClassdiagramNode node : uplinks) {
            result =
                    (node == this) ? result : Math.max(
                            node.getLevel() + 1, result);
        }
        return result;
    }

    /**
     * Get the location of the underlying figure in the diagram.
     * 
     * @return The location.
     */
    public Point getLocation() {
        return getFigure().getLocation();
    }

    /**
     * Get the current placement hint.
     * 
     * @return The placementhint for this node.
     */
    public int getPlacementHint() {
        return placementHint;
    }

    /**
     * Get the rank of this node.
     * 
     * @return The rank for this node.
     */
    public int getRank() {
        return rank == NORANK ? getLevel() : rank;
    }

    /**
     * Operation getSize returns the size of the figure associated with this
     * layout node.
     * 
     * @return The size of the associated figure.
     */
    public Dimension getSize() {
        return getFigure().getSize();
    }

    /**
     * Get the weight of the subtree defined by this node. Impact on weight is
     * decreasing with increasing hierarchical distance
     * 
     * @return The weight of the subtree.
     */
    private float getSubtreeWeight() {

        float w = 1;
        for (ClassdiagramNode node : downlinks) {
            w += node.getSubtreeWeight() / UPLINK_FACTOR;
        }
        return w;
    }

    /**
     * Get the type order number of this node. This number may be used to
     * influence the sort order of ClassdiagramNodes.
     * 
     * @return Type order number.
     */
    public int getTypeOrderNumer() {
        int result = 99;
        if (getFigure() instanceof FigPackage) {
            result = 0;
        } else if (getFigure() instanceof FigInterface) {
            result = 1;
        }
        return result;
    }

    /**
     * Get the uplinks of this node.
     * 
     * @return The uplinks of this node.
     * @deprecated for 0.25.4 by tfmorris - use {@link #getUpNodes()}
     */
    public Vector<ClassdiagramNode> getUplinks() {
        return new Vector<ClassdiagramNode>(uplinks);
    }

    /**
     * Get the uplinks of this node.
     * 
     * @return The uplinks of this node.
     */
    public List<ClassdiagramNode> getUpNodes() {
        return uplinks;
    }
    
    /**
     * Return the weight of this node, which is used for positioning in a row.
     * 
     * @return The weight of this node.
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Check if this node is associated with a note.
     * 
     * @return Result of test.
     */
    public boolean isComment() {
        return (getFigure() instanceof FigComment);
    }

    /**
     * Check if this node is associated with a package.
     * 
     * @return Result of test.
     */
    public boolean isPackage() {
        return (getFigure() instanceof FigPackage);
    }

    /**
     * Test whether this node has no connection to other nodes. Return
     * <code>true</code> if node has no connections, <code>false</code>
     * otherwise.
     * 
     * @return Result of test.
     */
    public boolean isStandalone() {
        return uplinks.isEmpty() && downlinks.isEmpty();
    }

    /**
     * Set the column of this node. A re-calculation of the weight is performed,
     * because the column is an input parameter for the weight.
     * 
     * @param newColumn
     *            The new column.
     */
    public void setColumn(int newColumn) {
        column = newColumn;
        calculateWeight();
    }

    /**
     * Set the offset for edges to this node.
     * 
     * @param newOffset
     *            Offset for edges with this node as one endpoint.
     */
    public void setEdgeOffset(int newOffset) {
        edgeOffset = newOffset;
    }

    /**
     * Operation setFigure changes the value of the attribute _figure.
     * 
     * @param newFigure
     *            represents the new value of _figure.
     */
    public void setFigure(FigNode newFigure) {
        figure = newFigure;
    }

    /**
     * Operation setLocation set the new location of the associated figure in
     * the diagram.
     * 
     * @param newLocation
     *            represents the new location for this figure.
     */
    public void setLocation(Point newLocation) {
        Point oldLocation = getFigure().getLocation();

        getFigure().setLocation(newLocation);
        int xTrans = newLocation.x - oldLocation.x;
        int yTrans = newLocation.y - oldLocation.y;
        for (Iterator<Fig> iter = getFigure().getEnclosedFigs().iterator(); 
                iter.hasNext();) {
            iter.next().translate(xTrans, yTrans);
        }
    }

    /**
     * A placementhint gives an indication where it might be feasible to place
     * this node. It is used by the layouter, and there is no guarantee that it
     * will be used.
     * 
     * @param hint
     *            x coordinate of the desired placement
     */
    public void setPlacementHint(int hint) {
        placementHint = hint;
    }

    /**
     * Operation setRank changes the value of the attribute _rank.
     * 
     * @param newRank
     *            represents the new value of _rank.
     */
    public void setRank(int newRank) {
        rank = newRank;
    }

    /**
     * Set the weight for this node.
     * 
     * @param w
     *            The new weight of this node.
     */
    public void setWeight(float w) {
        weight = w;
    }

}
