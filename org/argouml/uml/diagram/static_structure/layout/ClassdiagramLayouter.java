// Copyright (c) 1996-01 The Regents of the University of California. All
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

import java.awt.*;
import java.util.*;
import org.argouml.uml.diagram.layout.*;
import org.argouml.uml.diagram.ui.*;
import org.tigris.gef.presentation.*;
import ru.novosoft.uml.foundation.core.*;


/**
 * This class implements a layout algoritms for class diagrams.
 */
public class ClassdiagramLayouter implements Layouter {

    /**
     * This constructor is mainly for convenience, so we don't have 
     * add every node manually to the layouter.
     */
    public ClassdiagramLayouter(UMLDiagram diagram) {

	// Get all the figures from the diagram.
	Vector nodes = diagram.getLayer().getContents(); 

	// Create ClassdiagramNodes for all the figures
	// and add them to the layouter.
	for(int i=0; i < nodes.size(); i++) {
	    if(nodes.elementAt(i) instanceof FigNode) {
		ClassdiagramNode c =
		    new ClassdiagramNode((FigNode)(nodes.elementAt(i)));
		add(new ClassdiagramNode((FigNode)(nodes.elementAt(i))));
	    }
	}
    }

    /**
     * Add a object to layout.
     *
     * @param obj represents the object to layout.
     */
    public void add(LayoutedObject obj) {
	_layoutedObjects.add(obj);
    }

    /**
     * Add a ClassdiagramNode to layout.
     *
     * @param obj represents the object to layout.
     */
    public void add(ClassdiagramNode obj) {
	_layoutedObjects.add(obj);
    }

    /**
     * Remove a object from the layout process.
     *
     * @param obj represents the object to remove.
     */
    public void remove(LayoutedObject obj) {
	_layoutedObjects.remove(obj);
    }

    /**
     * Operation getObjects returns all the objects
     * currently participating in the layout process.
     *
     * @return An array holding all the object in the layouter.
     */
    public LayoutedObject [] getObjects() {
	// Create an array for the result.
	LayoutedObject [] result = new LayoutedObject[_layoutedObjects.size()];

	_layoutedObjects.copyInto(result);  // Copy the objects into the array.

        return result;  // And return the array.
    }

    /**
     * Operation getObject returns a object with a given index from the layouter.
     *
     * @param index represents the index of this object in the layouter.
     * @return The LayoutedObject for the given index.
     */
    public LayoutedObject getObject(int index) {
	return (LayoutedObject)(_layoutedObjects.elementAt(index));
    }
    
    /**
     * Get a ClassdiagramNode from the layouted objects.
     *
     * @param index represents the index of this ClassdiagramNode.
     * @return The ClassdiagramNode for this index.
     */
    public ClassdiagramNode getClassdiagramNode(int index) {
	return (ClassdiagramNode)(_layoutedObjects.elementAt(index));
    }

    /**
     * Operation layout implements the actual layout algorithm
     */
    public void layout() {

	// Determine all the up- and downlinks for each node
	for(int i=0; i < _layoutedObjects.size(); i++) {
	    ClassdiagramNode classdiagramNode = getClassdiagramNode(i);
	    
	    if(!classdiagramNode.isPackage()) {
		Object node = classdiagramNode.getFigure().getOwner(); 
		
		if ( node instanceof MGeneralizableElement ) {
		    Collection gn = ((MClassifier)node).getGeneralizations();
		    Iterator iter = gn.iterator();
		    while (iter.hasNext()) {
			MGeneralization g = (MGeneralization) iter.next();
			ClassdiagramNode superNode = getClassdiagramNode4owner((MClassifier)(g.getParent()));

			if(superNode != null) {
			    classdiagramNode.addUplink(superNode);
			}
		    }
		    Collection sp = ((MClassifier)node).getSpecializations();
		    iter = sp.iterator();
		    while (iter.hasNext()) {
			MGeneralization s = (MGeneralization)iter.next();
			ClassdiagramNode subNode = getClassdiagramNode4owner((MClassifier)(s.getChild()));
			
			if(subNode != null) {
			    classdiagramNode.addDownlink(subNode);
			}
		    }
		}           
	    }
	}

	//
	// Check for circular references here??? Or let the RE code handle this?
	//

	// For now, all packages go above the classes and
	// interfaces
	for(int i=0; i < _layoutedObjects.size(); i++) {
	    ClassdiagramNode node = getClassdiagramNode(i);

	    if(node.isPackage()) {
		node.setRank(0);
	    }
	}
	
	// Find the maximum rank of the packages, and move
	// the classes below the packages
	int maxPackageRank = -1;
	for(int i=0; i < _layoutedObjects.size(); i++) {
	    if(getClassdiagramNode(i).isPackage() 
	       && (getClassdiagramNode(i).getRank() > maxPackageRank)) {
		maxPackageRank = getClassdiagramNode(i).getRank();
	    }
	}
	maxPackageRank++;
	for(int i=0; i < _layoutedObjects.size(); i++) {
	    if(!getClassdiagramNode(i).isPackage())
		getClassdiagramNode(i).addRank(maxPackageRank);
	}

	// Now move the figures to the places in their rows
	int xPos, yPos = getVGap() / 2;
	int rows = getRows();
	for(int curRow=0; curRow < rows; curRow++) {
	    xPos = getHGap() / 2;

	    // Get all the objects for this row
	    ClassdiagramNode [] rowObject = getObjectsInRow(curRow);

	    // Sort objects in row here!!!
	    // NOT IMPLEMENTED YET!!!
	    
	    for(int column=0; column < rowObject.length; column++) {
		rowObject[column].setLocation(new Point(xPos, yPos));

		xPos += rowObject[column].getSize().getWidth() + getHGap();
	    }

	    yPos += getRowHeight(curRow) + getVGap();
	}
    }
	
    /**
     * Search the nodes in this classdiagram for a node
     * with a given owner.
     * 
     * @return The classdiagram node for this owner, if it's in this
     *         diagram, or null if not.
     */
    private ClassdiagramNode getClassdiagramNode4owner(MClassifier m) {
	for(int i=0; i < _layoutedObjects.size(); i++) {
	    if(getClassdiagramNode(i).getFigure().getOwner() == m)
		return getClassdiagramNode(i);
	}
	return null;
    }

    /**
     * Operation getMinimumDiagramSize returns the minimum diagram
     * size after the layout process.
     *
     * @return The minimum diagram size after the layout process.
     */
    public Dimension getMinimumDiagramSize() {
	int width = 0, height = 0;

	for(int i=0; i < _layoutedObjects.size(); i++) {
	    ClassdiagramNode node = getClassdiagramNode(i);
	    
	    if(node.getLocation().x + node.getSize().getWidth() + getHGap() / 2 >= width)
		width = (int)(node.getLocation().x + node.getSize().getWidth() + getHGap() / 2);

	    if(node.getLocation().y + node.getSize().getHeight() + getVGap() / 2 >= height)
		height = (int)(node.getLocation().y + node.getSize().getHeight() + getVGap() / 2);
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

	for(int i=0; i < _layoutedObjects.size(); i++) {
	    ClassdiagramNode node = getClassdiagramNode(i);

	    if(node.getRank() >= result)
		result = node.getRank() + 1;
	}
	return result;
    }

    /**
     * Operation getRowHeight does ...
     *
     * @param row represents ...
     */
    private int getRowHeight(int row) {
	int currentHeight = 0;  // Buffer for the result.

	// Check all the nodes in the layouter
	for(int i=0; i < _layoutedObjects.size(); i++) {
	    if((getClassdiagramNode(i)).getRank() == row) {  // If the object is in this row
		if((getClassdiagramNode(i)).getSize().height > currentHeight)
		    currentHeight = (getClassdiagramNode(i)).getSize().height;
	    }
	}

	return currentHeight;
    }

    /**
     * Operation getObject InRow returns all the objects for a given row.
     *
     * @param row represents the row of the returned objects.
     */
    private ClassdiagramNode [] getObjectsInRow(int row) {
	Vector resultBuffer = new Vector();  // A Vector to store the results.

	// Check all the nodes in the layouter
	for(int i=0; i < _layoutedObjects.size(); i++) {
	    if((getClassdiagramNode(i)).getRank() == row)    // If the object is in this row
		resultBuffer.add(getClassdiagramNode(i));  // add it to the buffer.
	}

	// Create an array for the result.
	ClassdiagramNode [] result = new ClassdiagramNode[resultBuffer.size()];

	// If there are any results, copy them into the array.
	if(resultBuffer.size() > 0)
	    resultBuffer.copyInto(result);

	return result;  // Return the array.
    }

    /**
     * Get the vertical gap between nodes.
     *
     * @return The vertical gap between nodes.
     */
    protected int getVGap() {
	return _vGap;
    }

    /**
     * Get the horizontal gap between nodes.
     *
     * @return The horizontal gap between nodes.
     */
    protected int getHGap() {
	return _hGap;
    }
    
    // Attributes

    /**
     * Attribute _layoutedObjects holds the objects to layout.
     */
    private Vector _layoutedObjects = new Vector();

    /**
     * The horizontal gap between nodes.
     */
    private int _hGap = 80;

    /**
     * The vertical gap between nodes.
     */
    private int _vGap = 80;
}









