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
import org.argouml.uml.diagram.static_structure.ui.*;
import org.tigris.gef.presentation.*;



/**
 * This class represents a node in the classdiagram
 * (a class, interface or package).
 */
class ClassdiagramNode implements LayoutedNode {

    // Operations

    /**
     * Operation ClassdiagramNode creates a new ClassdiagramNode.
     *
     * @param figure represents the figure in the diagram, that
     *               peers this layout node.
     */
    public ClassdiagramNode(FigNode figure) {
	setFigure(figure);
    }

    /**
     * Operation getSize returns the size of the figure associated
     * with this layout node.
     *
     * @return The size of the associated figure.
     */
    public Dimension getSize() {
        return getFigure().getSize();
    }

    /**
     * Operation getLocation returns the location of the associated
     * figure in the diagram.
     *
     * @return The location of the associated figure.
     */
    public Point getLocation() {
        return getFigure().getLocation();
    }

    /**
     * Operation setLocation set the new location of the
     * associated figure in the diagram.
     *
     * @param newLocation represents the new location for this figure.
     */
    public void setLocation(Point newLocation) {
        getFigure().setLocation(newLocation);
    }

    /**
     * Check if this node is associated with a package.
     *
     * @return true, if this node is associated with a package,
     *         false otherwise.
     */
    boolean isPackage() {
	return (getFigure() instanceof FigPackage);
    }

    /**
     * Compute or just return the rank of this node.
     *
     * @return The rank for this node.
     */
    public int getRank() {

	if(_rank == NORANK) {  // If the rank was not computed yet, compute it now.
	    if(getUplinks().size() == 0) {  // If there are no uplinks,
		_rank=0;  // place the node in the 1st row.
	    } else {  // Otherwise compute the max rank of the uplinks + 1
		for(int i=0; i < getUplinks().size(); i++) {
		    if(getUplink(i).getRank() + 1 > _rank) {
			_rank = getUplink(i).getRank() + 1;
		    }
		}
	    }
	}
	return _rank; 
    }

    /**
     * Operation setRank changes the value of the attribute _rank.
     *
     * @param new_rank represents the new value of _rank.	
     */
    public void setRank(int new_rank) { _rank = new_rank; }

    /**
     * Add a constant to the rank of this node.
     *
     * @param n The value to add.
     */
    public void addRank(int n) {
	setRank(n + getRank());
    }

    /**
     * Operation getColumn returns the value of the attribute _column.
     *
     * @return The value of the attribute _column.
     */
    public int getColumn() { return _column; }

    /**
     * Operation setColumn changes the value of the attribute _column.
     *
     * @param new_column represents the new value of _column.	
     */
    public void setColumn(int new_column) { _column = new_column; }

    /**
     * Operation getUplinks returns the value of the attribute _uplinks.
     *
     * @return The value of the attribute _uplinks.
     */
    public Vector getUplinks() { return _uplinks; }

    /**
     * Get the uplink for a given index.
     *
     * @param The index of this uplink.
     * @return The ClassdiagramNode for this uplink.
     */
    public ClassdiagramNode getUplink(int i) { return (ClassdiagramNode)(_uplinks.elementAt(i)); }

    /**
     * Add an uplink to this node.
     *
     * @param new_uplink represents the new uplinks.	
     */
    public void addUplink(ClassdiagramNode new_uplink) { _uplinks.add(new_uplink); }

    /**
     * Operation getDownlinks returns the value of the attribute _downlinks.
     *
     * @return The value of the attribute _downlinks.
     */
    public Vector getDownlinks() { return _downlinks; }

    /**
     * Get the downlink for a given index.
     *
     * @param The index of this downlink.
     * @return The ClassdiagramNode of this downlink.
     */
    public ClassdiagramNode getDownlink(int i) { return (ClassdiagramNode)(_downlinks.elementAt(i)); }

    /**
     * Operation setDownlinks changes the value of the attribute _downlinks.
     *
     * @param new_downlinks represents the new value of _downlinks.	
     */
    public void addDownlink(ClassdiagramNode new_downlink) { _downlinks.add(new_downlink); }

    /**
     * Operation getFigure returns the value of the attribute _figure.
     *
     * @return The value of the attribute _figure.
     */
    public FigNode getFigure() { return _figure; }

    /**
     * Operation setFigure changes the value of the attribute _figure.
     *
     * @param new_figure represents the new value of _figure.	
     */
    public void setFigure(FigNode new_figure) { _figure = new_figure; }

    /**
     * Return the weight of this node.
     *
     * @return The weight of this node.
     */
    public float getWeight() { return _weight; }

    /**
     * Set a new weight for this node.
     *
     * @param weight The new weight of this node.
     */
    public void setWeight( float weight) { _weight = weight; }

    /** a node is movable when it hasn't got any up- or downlinks, but sidelinks
     *
     * @return whether the node is movable
     */
    boolean isMovable() {
        return ( _downlinks.size()==0 && _uplinks.size()==0 && _sidelinks.size()>0);
    }

    /** A placementhint gives an indication where it might be feasible to
     *  place this node. It is used by the layouter, and there
     *  is no guarantee that it will be used.
     *  @param hint x coordinate of the desired placement
     */
    public void setPlacementHint(int hint) {
        placementHint = hint;
    }

    /** get the current placementhint. */
    public int getPlacementHint() { return placementHint; }

    // Attributes

    /**
     * Constant to be used as an initializer when this node has
     * no rank assigned yet.
     */
    public static int NORANK = -1;

    /**
     * Attribute _rank represents the current rank of this node.
     */
    private int _rank = NORANK;

    /**
     * Constant to be used as an initializer when this node is
     * not placed at an column.
     */
    public int NOCOLUMN = -1;

    /**
     * Attribute _column represents the current column of this node.
     */
    private int _column = NOCOLUMN;

    /**
     * Attribute _uplinks represents the links I consider as
     * an 'uplink'. An uplink is a link going to a superclass or
     * a interface that this class implements. Figures that are 
     * usually placed above this figure.
     */
    private Vector _uplinks = new Vector();

    /**
     * Attribute _downlinks represents the links I consider as
     * an 'downlink'. The opposite of an uplink. See explanation
     * above.
     */
    private Vector _downlinks = new Vector();
    
    /** Sidelinks are basically associations. We need to know if a node
     * has sidelinks in order to determine if we can move him around freely
     * after ranking.
     * @see isMovable() 
     */
    private Vector _sidelinks = new Vector();
    

    /**
     * Attribute _figure represents the figure, that this
     * ClassdiagramNode represents during the layout process.
     */
    private FigNode _figure = null;

    /**
     * This attributes stores the 'weight' of this node.
     * This is a computed attribute that is used during the horizontal
     * placement process. It's based on the position of the 'uplinked'
     * objects. The actual purpose is to minimize the number of link
     * crossings in the diagram. Since we don't compute the actual number
     * of link crossings, we look where our uplinked objects are, and then
     * try to place our object in a way, that we can expect to have a
     * minimal number of crossings.
     */
    private float _weight = 1;
    
    
    /** a Node is movable when it has associations and no up- or downlinks.
     * The logic behind this is, that we would like to move nodes which
     * were skipped during the rankking process to a position where they are
     * as close as possible to the respective(s) nodes they share an association to
     */
    private boolean _movable = false;

    private int placementHint = -1;
  
}



