// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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
 * SerialLayout.java
 */
package org.argouml.swingext;

import java.awt.*;
import java.util.*;

import org.apache.log4j.Category;

/**
 * Lays out components in a single row or column starting from any
 * side and aligning components to eachother.<p>
 *
 * Components can be set to start draw from, LEFTTORIGHT, TOPTOBOTTOM,
 * RIGHTTOLEFT or BOTTOMTOTOP.<p>
 *
 * Components will line up with eachother by edge or follow a common
 * central line.<p>
 *
 * The gap to leave before the first component and the following gaps
 * between each component can be set.
 *
 * @author Bob Tarling
 */
public class SerialLayout extends LineLayout {
    
    protected static Category cat = 
        Category.getInstance(SerialLayout.class);
        
    public static final int LEFTTORIGHT = 10;
    public static final int TOPTOBOTTOM = 10;
    public static final int RIGHTTOLEFT = 11;
    public static final int BOTTOMTOTOP = 11;
		  		 
    public static final String NORTH = "North";
    public static final String SOUTH = "South";
    public static final String EAST = "East";
    public static final String WEST = "West";
    public static final String NORTHEAST = "NorthEast";
    public static final String NORTHWEST = "NorthWest";
    public static final String SOUTHEAST = "SouthEast";
    public static final String SOUTHWEST = "SouthWest";
		  		 
    public static final int LEFT = 20;
    public static final int RIGHT = 21;
    public static final int TOP = 20;
    public static final int BOTTOM = 21;
    public static final int CENTER = 22;
    public static final int FILL = 23;

    String position = WEST;
    int direction = LEFTTORIGHT;
    int alignment = TOP;

    public SerialLayout() {
        this(Horizontal.getInstance(), WEST, LEFTTORIGHT, TOP);
    }
    public SerialLayout(Orientation orientation) {
        this(orientation, WEST, LEFTTORIGHT, TOP);
    }
    public SerialLayout(Orientation orientation, String position) {
        this(orientation, position, LEFTTORIGHT, TOP);
    }
    public SerialLayout(Orientation orientation, String position,
			int direction) {
        this(orientation, position, direction, TOP);
    }
    
    public SerialLayout(Orientation orientation, String position,
			int direction, int alignment) {
        super(orientation);
        this.position = position;
        this.direction = direction;
        this.alignment = alignment;
    }

    public SerialLayout(Orientation orientation, String position,
			int direction, int alignment, int gap) {
        super(orientation, gap);
        this.position = position;
        this.direction = direction;
        this.alignment = alignment;
    }

    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();

        Point loc;
        int preferredBreadth =
	    _orientation.getBreadth(parent.getPreferredSize());
        if (direction == LEFTTORIGHT) {
            if (position.equals(EAST)) {
                loc =
		    new Point(parent.getWidth()
			      - (insets.right
				 + preferredLayoutSize(parent).width),
			      insets.top);
            } else {
                loc = new Point(insets.left, insets.top);
            }
            int nComps = parent.getComponentCount();
            for (int i = 0; i < nComps; i++) {
                Component comp = parent.getComponent(i);
                if (comp != null && comp.isVisible()) {
                    Dimension size = comp.getPreferredSize();
                    if (alignment == FILL) {
                        _orientation.setBreadth(size, preferredBreadth);
                    }
                    comp.setSize(size);
                    comp.setLocation(loc);
                    loc = _orientation.addToPosition(loc, comp);
                    loc = _orientation.addToPosition(loc, _gap);
                }
            }
        }
        else {
            int lastUsablePosition = _orientation.getLastUsablePosition(parent);
            int firstUsableOffset = _orientation.getFirstUsableOffset(parent);
            loc = _orientation.newPoint(lastUsablePosition, firstUsableOffset);

            int nComps = parent.getComponentCount();
            for (int i = 0; i < nComps; i++) {
                Component comp = parent.getComponent(i);
                if (comp != null && comp.isVisible()) {
                    loc = _orientation.subtractFromPosition(loc, comp);
                    Dimension size = comp.getPreferredSize();
                    if (alignment == FILL) {
                        _orientation.setBreadth(size, preferredBreadth);
                    }
                    comp.setSize(size);
                    comp.setLocation(loc);
                    loc = _orientation.subtractFromPosition(loc, _gap);
                }
            }
        }
    }
}
