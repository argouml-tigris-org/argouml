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

import org.apache.log4j.Logger;

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
    
    private static final Logger LOG = 
        Logger.getLogger(SerialLayout.class);
        
    /** Starting point and direction: LEFTTORIGHT */
    public static final int LEFTTORIGHT = 10;
    /** Starting point and direction: TOPTOBOTTOM */
    public static final int TOPTOBOTTOM = 10;
    /** Starting point and direction: RIGHTTOLEFT */
    public static final int RIGHTTOLEFT = 11;
    /** Starting point and direction: BOTTOMTOTOP */
    public static final int BOTTOMTOTOP = 11;
		  		 
    /** Position identification: NORTH */
    public static final String NORTH = "North";
    /** Position identification: SOUTH */
    public static final String SOUTH = "South";
    /** Position identification: EAST */
    public static final String EAST = "East";
    /** Position identification: WEST */
    public static final String WEST = "West";
    /** Position identification: NORTHEAST */
    public static final String NORTHEAST = "NorthEast";
    /** Position identification: NORTHWEST */
    public static final String NORTHWEST = "NorthWest";
    /** Position identification: SOUTHEAST */
    public static final String SOUTHEAST = "SouthEast";
    /** Position identification: SOUTHWEST */
    public static final String SOUTHWEST = "SouthWest";
		  		 
    /** Alignment identification: LEFT */
    public static final int LEFT = 20;
    /** Alignment identification: RIGHT */
    public static final int RIGHT = 21;
    /** Alignment identification: TOP */
    public static final int TOP = 20;
    /** Alignment identification: BOTTOM */
    public static final int BOTTOM = 21;
    /** Alignment identification: CENTER */
    public static final int CENTER = 22;
    /** Alignment identification: FILL */
    public static final int FILL = 23;

    private String position = WEST;
    private int direction = LEFTTORIGHT;
    private int alignment = TOP;

    /**
     * The constructor.
     */
    public SerialLayout() {
        this(Horizontal.getInstance(), WEST, LEFTTORIGHT, TOP);
    }
    
    /**
     * The constructor.
     * 
     * @param orientation the orientation
     */
    public SerialLayout(Orientation orientation) {
        this(orientation, WEST, LEFTTORIGHT, TOP);
    }
    
    /**
     * The constructor.
     * 
     * @param o the orientation
     * @param p the position
     */
    public SerialLayout(Orientation o, String p) {
        this(o, p, LEFTTORIGHT, TOP);
    }
    
    /**
     *  The constructor.
     * 
     * @param o the orientation
     * @param p the position
     * @param d the direction
     */
    public SerialLayout(Orientation o, String p,
			int d) {
        this(o, p, d, TOP);
    }
    
    /**
     *  The constructor.
     * 
     * @param o the orientation
     * @param p the position
     * @param d the direction
     * @param a the alignment
     */
    public SerialLayout(Orientation o, String p, int d, int a) {
        super(o);
        this.position = p;
        this.direction = d;
        this.alignment = a;
    }

    /**
     * The constructor.
     * 
     * @param o the orientation
     * @param p the position
     * @param d the direction
     * @param a the alignment
     * @param gap the gap
     */
    public SerialLayout(Orientation o, String p,
			int d, int a, int gap) {
        super(o, gap);
        this.position = p;
        this.direction = d;
        this.alignment = a;
    }

    /**
     * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
     */
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();

        Point loc;
        int preferredBreadth =
	    getMyOrientation().getBreadth(parent.getPreferredSize());
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
                        getMyOrientation().setBreadth(size, preferredBreadth);
                    }
                    comp.setSize(size);
                    comp.setLocation(loc);
                    loc = getMyOrientation().addToPosition(loc, comp);
                    loc = getMyOrientation().addToPosition(loc, getGap());
                }
            }
        }
        else {
            int lastUsablePosition = getMyOrientation()
                .getLastUsablePosition(parent);
            int firstUsableOffset = getMyOrientation()
                .getFirstUsableOffset(parent);
            loc = getMyOrientation()
                .newPoint(lastUsablePosition, firstUsableOffset);

            int nComps = parent.getComponentCount();
            for (int i = 0; i < nComps; i++) {
                Component comp = parent.getComponent(i);
                if (comp != null && comp.isVisible()) {
                    loc = getMyOrientation().subtractFromPosition(loc, comp);
                    Dimension size = comp.getPreferredSize();
                    if (alignment == FILL) {
                        getMyOrientation().setBreadth(size, preferredBreadth);
                    }
                    comp.setSize(size);
                    comp.setLocation(loc);
                    loc = getMyOrientation().subtractFromPosition(loc, 
                                                                getGap());
                }
            }
        }
    }
}
