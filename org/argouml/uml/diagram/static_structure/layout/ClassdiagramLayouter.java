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

package org.argouml.uml.diagram.static_structure.layout;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.layout.LayoutedObject;
import org.argouml.uml.diagram.layout.Layouter;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * This class implements a layout algoritms for class diagrams.
 */
public class ClassdiagramLayouter implements Layouter {

    /**
     * Logger for logging events.
     */
    private static final Logger LOG = Logger
            .getLogger(ClassdiagramLayouter.class);

    /**
     * Stores the current diagram.
     */
    private UMLDiagram diagram;

    /**
     * Stores all the nodes which will be layouted.
     */
    private Collection nodes;

    /**
     * This constructor is mainly for convenience, so we don't have add every
     * node manually to the layouter.
     *
     * @param theDiagram the diagram to layout
     */
    public ClassdiagramLayouter(UMLDiagram theDiagram) {

        this.diagram = theDiagram;
        // Get all the figures from the diagram.
        nodes = diagram.getLayer().getContents(null);
        Iterator nodeIter = nodes.iterator();

        // Create ClassdiagramNodes for all the figures
        // and add them to the layouter.
        while (nodeIter.hasNext()) {
            //        for (int i = 0; i < nodes.size(); i++) {
            // remember all Nodes
            Object fig = nodeIter.next();
            if (((Fig) fig).getEnclosingFig() == null) {
                if (fig instanceof FigNode) {
                    add(new ClassdiagramNode((FigNode) (fig)));
                }
                if (fig instanceof FigEdge) {

                    add(ClassdiagramModelElementFactory.SINGLETON
                            .getInstance(fig));
                }
            }
        }
        // built the subset vector
        layoutedClassNodes = getClassdiagramNodes();
    }

    /**
     * Add a object to layout.
     *
     * @param obj
     *            represents the object to layout.
     */
    public void add(LayoutedObject obj) {
        layoutedObjects.add(obj);
    }

    /**
     * Add a ClassdiagramNode to layout.
     *
     * @param obj
     *            represents the object to layout.
     */
    public void add(ClassdiagramNode obj) {
        layoutedObjects.add(obj);
    }

    /**
     * Remove a object from the layout process.
     *
     * @param obj
     *            represents the object to remove.
     */
    public void remove(LayoutedObject obj) {
        layoutedObjects.remove(obj);
    }

    /**
     * Operation getObjects returns all the objects currently participating in
     * the layout process.
     *
     * @return An array holding all the object in the layouter.
     */
    public LayoutedObject[] getObjects() {
        // Create an array for the result.
        LayoutedObject[] result = new LayoutedObject[layoutedObjects.size()];

        layoutedObjects.copyInto(result); // Copy the objects into the array.

        return result; // And return the array.
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
        return (LayoutedObject) (layoutedObjects.elementAt(index));
    }

    /**
     * Get a ClassdiagramNode from the layouted objects.
     *
     * @param index
     *            represents the index of this ClassdiagramNode.
     * @return The ClassdiagramNode for this index.
     */
    public ClassdiagramNode getClassdiagramNode(int index) {
        return (ClassdiagramNode) (layoutedClassNodes.elementAt(index));
    }

    /** extract the ClassdiagramNodes from all layouted objects */
    private Vector getClassdiagramNodes() {
        Vector classNodes = new Vector();
        for (int i = 0; i < layoutedObjects.size(); i++)
            if (layoutedObjects.elementAt(i) instanceof ClassdiagramNode)
                    classNodes.add(layoutedObjects.elementAt(i));

        return classNodes;
    }

    /**
     * Operation layout implements the actual layout algorithm.
     */
    public void layout() {

        HashMap maptoAssocNodes = new HashMap();

        // Determine all the up- and downlinks for each node
        for (int i = 0; i < layoutedClassNodes.size(); i++) {

            ClassdiagramNode classdiagramNode = getClassdiagramNode(i);

            if (!classdiagramNode.isPackage()) {
                Object node = classdiagramNode.getFigure().getOwner();

                if (ModelFacade.isAModelElement(node)) {
                    Vector specs = new Vector(ModelFacade
                            .getClientDependencies(node));
                    specs.addAll(ModelFacade.getSupplierDependencies(node));
                    for (Iterator iter = specs.iterator(); iter.hasNext();) {

                        // Realizations are stored as MAbstractions
                        // with a stereotype 'realize'. MAbstraction
                        // is a subclass of MDependency, so get all
                        // the dependencies for this node to get the
                        // abstractions, too.
                        Object dep = iter.next();
                        if (ModelFacade.isAAbstraction(dep)) {
                            // Is this a abstraction?
                            Object abstr = dep;
                            if (ModelFacade.isRealize(abstr)) {
                                // Is this node the class, that
                                // implements the interface?
                                Collection clients = ModelFacade
                                        .getClients(abstr);
                                for (Iterator iter2 = clients.iterator(); iter2
                                        .hasNext();) {
                                    Object me =
                                    /* (MModelElement) */iter2.next();
                                    if (node == me) {
                                        Collection suppliers = ModelFacade
                                                .getSuppliers(abstr);
                                        Iterator iter3 = suppliers.iterator();
                                        while (iter3.hasNext()) {
                                            Object me2 = iter3.next();
                                            if (ModelFacade.isAClassifier(me2))
                                            {
                                                ClassdiagramNode superNode =
                                                    getClassdiagramNode4owner(
                                                            me2);

                                                if (superNode != null) {
                                                    classdiagramNode
                                                        .addUplink(superNode);
                                                }
                                            }
                                        }
                                    }
                                }

                                // Or the implemented interface?
                                Collection suppliers = ModelFacade
                                        .getSuppliers(abstr);
                                for (Iterator iter2 = suppliers.iterator();
                                    iter2.hasNext();) {
                                    Object me =
                                    /* (MModelElement) */iter2.next();
                                    if (node == me) {
                                        clients = ModelFacade.getClients(abstr);
                                        for (Iterator iter3 = clients
                                                .iterator(); iter3.hasNext();) {
                                            Object me2 = iter3.next();
                                            if (ModelFacade.isAClassifier(me2))
                                            {
                                                ClassdiagramNode subNode =
                                                    getClassdiagramNode4owner(
                                                            me2);

                                                if (subNode != null) {
                                                    classdiagramNode
                                                        .addDownlink(subNode);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (ModelFacade.isAGeneralizableElement(node)) {
                    Iterator iter = ModelFacade.getGeneralizations(node)
                            .iterator();

                    while (iter.hasNext()) {
                        Object g = iter.next();
                        ClassdiagramNode superNode =
                            getClassdiagramNode4owner(ModelFacade.getParent(g));

                        if (superNode != null) {
                            classdiagramNode.addUplink(superNode);
                        }
                    }

                    iter = ModelFacade.getSpecializations(node).iterator();
                    while (iter.hasNext()) {
                        Object s = iter.next();
                        ClassdiagramNode subNode =
                            getClassdiagramNode4owner(ModelFacade.getChild(s));

                        if (subNode != null) {
                            classdiagramNode.addDownlink(subNode);
                        }
                    }
                }
                if (ModelFacade.isAClassifier(node)) {
                    Collection associatedClassifiers = ModelFacade
                            .getAssociatedClasses(node);
                    maptoAssocNodes
                            .put(classdiagramNode, associatedClassifiers);
                }
            }
        }

        // determine maximum package rank and move the classes below
        rankPackagesAndMoveClassesBelow(maptoAssocNodes);

        // layout the Packages
        layoutPackages();

        // weightAndPlaceClasses()
        weightAndPlaceClasses();

        ClassdiagramEdge.setVGap(vGap);
        ClassdiagramEdge.setHGap(hGap);
        for (int i = 0; i < layoutedObjects.size(); i++)
            if (layoutedObjects.elementAt(i) instanceof ClassdiagramEdge)
                    ((ClassdiagramEdge) layoutedObjects.elementAt(i)).layout();

        // Globals.curEditor().damageAll();

    }

    private void rankPackagesAndMoveClassesBelow(HashMap maptoAssocClasses) {
        // For now, all packages go above the classes and
        // interfaces
        int currentColumnPosition = 0;
        // The number of elements in the current row
        int currentRow = 0;
        for (int i = 0; i < layoutedClassNodes.size(); i++) {
            ClassdiagramNode node = getClassdiagramNode(i);

            if (node.isPackage()) {
                if (currentColumnPosition <= vMax) {
                    // If there are not too many elements in the current Row
                    node.setRank(currentRow);
                    currentColumnPosition++;
                } else {
                    node.setRank(++currentRow);
                    currentColumnPosition = 0;
                }
            }
        }

        // Find the maximum rank of the packages, and move
        // the classes below the packages
        for (int i = 0; i < layoutedClassNodes.size(); i++) {
            if (getClassdiagramNode(i).isPackage()
                    && (getClassdiagramNode(i).getRank() > maxPackageRank)) {
                maxPackageRank = getClassdiagramNode(i).getRank();
            }
        }
        maxPackageRank++;

        // Move the classes down below the rows containing
        // the packages.
        for (int i = 0; i < layoutedClassNodes.size(); i++) {
            if (!getClassdiagramNode(i).isPackage()) {
                ClassdiagramNode node = getClassdiagramNode(i);

                Collection assocClasses = (Collection) maptoAssocClasses
                        .get(node);
                if (assocClasses != null) {
                    Iterator iter = assocClasses.iterator();
                    while (iter.hasNext()) {
                        Object me = iter.next();
                        ClassdiagramNode curNode =
                            getClassdiagramNode4owner(me);
                        if (curNode != null && curNode != node
                                && curNode.isMovable()) {
                            LOG.debug("Moving:" + me);
                            int rank = (curNode.getRealRank()
                                    == ClassdiagramNode.NORANK)
                                    ? node.getRank()
                                    : curNode.getRank();
                            LOG.debug("rank: " + rank);
                            curNode.setRank((rank + node.getRank() + 1) / 2);
                            LOG.debug("New rank: " + curNode.getRank());
                            LOG.debug("Parent Rank: " + node.getRank());
                        }
                    }
                }

            }
        }
        for (int i = 0; i < layoutedClassNodes.size(); i++) {
            if (!getClassdiagramNode(i).isPackage()) {
                ClassdiagramNode node = getClassdiagramNode(i);
                node.addRank(maxPackageRank);
            }
        }
    }

    private void weightAndPlaceClasses() {

        int rows = getRows();
        for (int curRow = maxPackageRank; curRow < rows; curRow++) {
            // Do not include packages in this process

            // The placement for the leftmost figure on the screen.
            xPos = getHGap() / 2;

            // Get all the objects for this row
            ClassdiagramNode[] rowObject = getObjectsInRow(curRow);

            // Go through this row.
            for (int i = 0; i < rowObject.length; i++) {

                if (curRow == maxPackageRank) {
                    // Since we cannot use any uplinks at row 0, I
                    // simply use the number of downlinks to place the
                    // nodes. If a node has many objects linked to it,
                    // I'll place it more to the left. Another
                    // strategy would be to start with middle, but if
                    // there are several nodes with many links, they
                    // shouldn't be near to each other, so this would
                    // cause another problem.

                    // Get the number of downlinks of this object.
                    int nDownlinks = rowObject[i].getDownlinks().size();

                    rowObject[i].setWeight((nDownlinks > 0) ? (1 / nDownlinks)
                            : 2);
                } else {

                    // Get the uplinks for this node
                    Vector uplinks = rowObject[i].getUplinks();

                    int nUplinks = uplinks.size();
                    if (nUplinks > 0) {

                        // Find the average of all the columns of the uplinked
                        // objects.
                        float averageCol = 0;
                        for (int j = 0; j < uplinks.size(); j++) {
                            averageCol += ((ClassdiagramNode) (uplinks
                                    .elementAt(j))).getColumn();
                        }
                        averageCol /= nUplinks;
                        rowObject[i].setWeight(averageCol);
                    } else { // Just place this node at the right side.
                        rowObject[i].setWeight(1000);
                    }
                }
            }

            // At this point all the nodes in the current row have a
            // weight assigned. Sort the nodes according to this
            // weight and assign them a column.

            // Array to hold the current column of the objects.
            int[] pos = new int[rowObject.length];
            // Init the array.
            for (int i = 0; i < pos.length; i++) {
                pos[i] = i;
            }

            // Now just do a very simple bubblesort on the array
            // (slow, but the array should be small...)
            boolean swapped = true;
            while (swapped) {
                swapped = false;
                for (int i = 0; i < pos.length - 1; i++) {
                    if (rowObject[pos[i]].getWeight() > rowObject[pos[i + 1]]
                            .getWeight()) {
                        int temp = pos[i];
                        pos[i] = pos[i + 1];
                        pos[i + 1] = temp;
                        swapped = true;
                    }
                }
            }

            // Now all the elements in this row are sorted and we can
            // place them within the column.
            for (int i = 0; i < pos.length; i++) {
                // Required to sort the next rows.
                rowObject[pos[i]].setColumn(i);

                // If we have enough elements in this row and
                // this node has no links,
                // move it down in the diagram
                if ((i > vMax) && (rowObject[pos[i]].getUplinks().size() == 0)
                        && (rowObject[pos[i]].getDownlinks().size() == 0)) {
                    // If there are already too many elements in that row
                    if (getColumns(rows - 1) > vMax) {
                        // add a new empty row.
                        rows++;
                    }
                    // Place the object in the last row.
                    rowObject[pos[i]].setRank(rows - 1);
                } else {
                    // Now set the position within the diagram.
                    ClassdiagramNode curNode = rowObject[pos[i]];
                    Vector downlinks = curNode.getDownlinks();
                    int bumperX = 0;
                    int currentWidth = (int) (curNode.getSize().getWidth());
                    double xOffset = currentWidth;

                    if (downlinks != null && downlinks.size() > 1
                            && curNode.getUplinks().size() == 0) {

                        xOffset = 0;
                        for (int j = 0; j < downlinks.size(); j++) {
                            ClassdiagramNode node = (ClassdiagramNode) downlinks
                                    .get(j);
                            xOffset += node.getSize().getWidth();
                        }
                        xOffset += ((downlinks.size() - 1) * getHGap());
                        bumperX = (int) (xOffset / 2) - (currentWidth / 2);
                    }
                    curNode.setLocation(new Point(Math.max(xPos + bumperX,
                            curNode.getUplinks().size() == 1 ? curNode
                                    .getPlacementHint() : -1), yPos));

                    // put placement hint into a downlink
                    if (downlinks.size() == 1) {
                        ((ClassdiagramNode) downlinks.get(0))
                                .setPlacementHint(xPos + bumperX);
                    }

                    // Advance the horizontal position by the width of
                    // this figure.
                    xPos += Math.max(curNode.getPlacementHint(), xOffset
                            + getHGap());
                }
            }

            // Advance the vertical position by the height of that row
            // (which is the max. height of the figures in that row).
            yPos += getRowHeight(curRow) + getVGap();
        }
    }

    /**
     * position the packages of the diagram
     */
    void layoutPackages() {
        // It might help to add pseudo notes here to improve layout, but for
        // the moment I'll try to do without. They should be inserted, when
        // a link spans more than 1 row.

        int rows = getRows();

        xPos = getHGap() / 2;
        yPos = getVGap() / 2;
        LOG.debug("Number of rows in layout process: " + rows);

        // Layout the packages above the classes and interfaces
        for (int curRow = 0; curRow < maxPackageRank; curRow++) {

            LOG.debug("Processing row nr: " + curRow);
            // The placement for the leftmost figure on the screen.
            xPos = getHGap() / 2;

            // Get all the objects for this row
            ClassdiagramNode[] rowObject = getObjectsInRow(curRow);

            LOG.debug("Objects in this row: " + rowObject.length);
            // Go through this row.
            for (int i = 0; i < rowObject.length; i++) {

                rowObject[i].setColumn(i); // Required to sort the next rows.

                // Now set the position within the diagram.
                rowObject[i].setLocation(new Point(xPos, yPos));

                // Advance the horizontal position by the width of this figure.
                xPos += rowObject[i].getSize().getWidth() + getHGap();
            }

            // Advance the vertical position by the height of that row
            // (which is the max. height of the figures in that row).
            yPos += getRowHeight(curRow) + getVGap();
        }
    }

    /**
     * Search the nodes in this classdiagram for a node with a given owner.
     *
     * @return The classdiagram node for this owner, if it's in this diagram, or
     *         null if not.
     */
    private ClassdiagramNode getClassdiagramNode4owner(Object m) {
        for (int i = 0; i < layoutedClassNodes.size(); i++) {
            if (layoutedClassNodes.elementAt(i) instanceof ClassdiagramNode)
                if (getClassdiagramNode(i).getFigure().getOwner() == m)
                    return getClassdiagramNode(i);
        }
        return null;
    }

    /**
     * Operation getMinimumDiagramSize returns the minimum diagram size after
     * the layout process.
     *
     * @return The minimum diagram size after the layout process.
     */
    public Dimension getMinimumDiagramSize() {
        int width = 0, height = 0;

        for (int i = 0; i < layoutedObjects.size(); i++) {
            ClassdiagramNode node = getClassdiagramNode(i);

            if ((node.getLocation().x + node.getSize().getWidth() + getHGap()
                    / 2) >= width)
                    width = (int) (node.getLocation().x
                            + node.getSize().getWidth() + getHGap() / 2);

            if ((node.getLocation().y + node.getSize().getHeight() + getVGap()
                    / 2) >= height)
                    height = (int) (node.getLocation().y
                            + node.getSize().getHeight() + getVGap() / 2);
        }
        return new Dimension(width, height);
    }

    /**
     * Get the number of rows in this diagram.
     *
     * @return The number of rows in this layout.
     */
    private int getRows() {
        int result = 0;

        for (int i = 0; i < layoutedClassNodes.size(); i++) {
            ClassdiagramNode node = getClassdiagramNode(i);

            if (node.getRank() >= result) result = node.getRank() + 1;
        }
        return result;
    }

    /**
     * calculate the height of the row
     *
     * @param row
     *            the row to calculate
     * @return the height
     */
    private int getRowHeight(int row) {
        int currentHeight = 0; // Buffer for the result.

        // Check all the nodes in the layouter
        for (int i = 0; i < layoutedClassNodes.size(); i++) {
            if ((getClassdiagramNode(i)).getRank() == row) {
                // If the object is in this row
                if ((getClassdiagramNode(i)).getSize().height > currentHeight)
                        currentHeight =
                            (getClassdiagramNode(i)).getSize().height;
            }
        }

        return currentHeight;
    }

    /**
     * Get the number of elements in a given row
     *
     * @param row
     *            The row to check.
     * @return The number of elements in the given row.
     */
    private int getColumns(int row) {
        int result = 0;

        // Check all the nodes in the layouter
        for (int i = 0; i < layoutedClassNodes.size(); i++) {
            if ((getClassdiagramNode(i)).getRank() == row)
            // If the object is in this row
                    result++; // add it to the result.
        }
        return result;
    }

    /**
     * Operation getObject InRow returns all the objects for a given row.
     *
     * @param row
     *            represents the row of the returned objects.
     */
    private ClassdiagramNode[] getObjectsInRow(int row) {
        Vector resultBuffer = new Vector(); // A Vector to store the results.

        // Check all the nodes in the layouter
        for (int i = 0; i < layoutedClassNodes.size(); i++) {
            if ((getClassdiagramNode(i)).getRank() == row) {
                // If the object is in this row add it to the buffer.
                resultBuffer.add(getClassdiagramNode(i));
            }
        }

        // Create an array for the result.
        ClassdiagramNode[] result = new ClassdiagramNode[resultBuffer.size()];

        // If there are any results, copy them into the array.
        if (resultBuffer.size() > 0) resultBuffer.copyInto(result);

        return result; // Return the array.
    }

    /**
     * Get the vertical gap between nodes.
     *
     * @return The vertical gap between nodes.
     */
    protected int getVGap() {
        return vGap;
    }

    /**
     * Get the horizontal gap between nodes.
     *
     * @return The horizontal gap between nodes.
     */
    protected int getHGap() {
        return hGap;
    }

    // Attributes

    /**
     * Attribute _layoutedObjects holds the objects to layout.
     */
    private Vector layoutedObjects = new Vector();

    /**
     * _layoutedClassNodes is a convenience which holds a subset of
     * _layoutedObjects (only ClassNodes)
     */
    private Vector layoutedClassNodes = new Vector();

    private int maxPackageRank = -1;

    /**
     * The horizontal gap between nodes.
     */
    private int hGap = 80;

    /**
     * The vertical gap between nodes.
     */
    private int vGap = 80;

    /**
     * The maximum of elements in a particular row
     */
    private int vMax = 5;

    /** internal */
    private int xPos;

    /** internal */
    private int yPos;
}

