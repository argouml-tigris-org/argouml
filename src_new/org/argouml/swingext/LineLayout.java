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
 * LineLayout.java
 */
package org.argouml.swingext;

import java.awt.*;

/**
 * Abstract class for a layout manager that sets all child components
 * in a single row or single column. PrefferedSize and MinimumSize are
 * calculated.
 *
 * @author Bob Tarling
 */
public abstract class LineLayout implements LayoutManager2 {

    /**
     * The horizoontal orientation.
     */
    public static final Orientation HORIZONTAL = Horizontal.getInstance();
    
    /**
     * The vertical orientation.
     */
    public static final Orientation VERTICAL = Vertical.getInstance();

    /**
     * The orientation for this layout.
     */
    private Orientation myOrientation;
    
    /**
     * The gap.
     */
    private int gap = 0;
    
    /**
     * The constructor.
     * 
     * @param o the orientation
     */
    public LineLayout(Orientation o) {
        myOrientation = o;
    }

    /**
     * @param o the orientation
     * @param g the gap
     */
    public LineLayout(Orientation o, int g) {
        myOrientation = o;
        gap = g;
    }

    /**
     * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String, 
     * java.awt.Component)
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * @see java.awt.LayoutManager2#addLayoutComponent(java.awt.Component, 
     * java.lang.Object)
     */
    public void addLayoutComponent(Component comp, Object constraints) {
    }

    /**
     * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
     */
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
     */
    public Dimension preferredLayoutSize(Container parent) {
        int nComps = parent.getComponentCount();
        Dimension preferredSize = new Dimension(0, 0);
        int myGap = 0;
        for (int i = 0; i < nComps; i++) {
            Component comp = parent.getComponent(i);
            if (comp.isVisible()) {
                preferredSize =
		    myOrientation.addLength(preferredSize,
					   myOrientation
					   .getLength(comp.getPreferredSize())
					   + myGap);
                myGap = gap;
                if (myOrientation.getBreadth(comp.getPreferredSize())
		    > myOrientation.getBreadth(preferredSize))
		{
                    preferredSize =
			myOrientation.setBreadth(preferredSize,
						comp.getPreferredSize());
                }
            }
        }
        preferredSize = DimensionUtility.add(preferredSize, parent.getInsets());
        return preferredSize;
    }

    /**
     * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
     */
    public Dimension minimumLayoutSize(Container parent) {
        int nComps = parent.getComponentCount();
        Dimension minimumSize = new Dimension(0, 0);
        int myGap = 0;
        for (int i = 0; i < nComps; i++) {
            Component comp = parent.getComponent(i);
            if (comp.isVisible()) {
                minimumSize =
		    myOrientation.addLength(minimumSize,
					   myOrientation
					   .getLength(comp.getMinimumSize())
					   + myGap);
                myGap = gap;
                if (myOrientation.getBreadth(comp.getMinimumSize())
		    > myOrientation.getBreadth(minimumSize))
		{
                    minimumSize =
			myOrientation.setBreadth(minimumSize,
						comp.getMinimumSize());
                }
            }
        }
        minimumSize = DimensionUtility.add(minimumSize, parent.getInsets());
        return minimumSize;
    }

    /**
     * @see java.awt.LayoutManager2#maximumLayoutSize(java.awt.Container)
     */
    public Dimension maximumLayoutSize(Container parent) {
        int nComps = parent.getComponentCount();
        Dimension maximumSize =
	    new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        int myGap = 0;
        for (int i = 0; i < nComps; i++) {
            Component comp = parent.getComponent(i);
            Dimension componentMaxSize = comp.getMaximumSize();
            if (comp.isVisible() && componentMaxSize != null) {
                maximumSize =
		    myOrientation.addLength(maximumSize,
					   myOrientation
					   .getLength(componentMaxSize)
					   + myGap);
                myGap = gap;
                if (myOrientation.getBreadth(componentMaxSize)
		    < myOrientation.getBreadth(maximumSize))
		{
                    maximumSize =
			myOrientation.setBreadth(maximumSize, componentMaxSize);
                }
            }
        }
        maximumSize = DimensionUtility.add(maximumSize, parent.getInsets());
        return maximumSize;
    }

    /**
     * @see java.awt.LayoutManager2#invalidateLayout(java.awt.Container)
     */
    public void invalidateLayout(Container target) { }
    
    /**
     * @see java.awt.LayoutManager2#getLayoutAlignmentX(java.awt.Container)
     */
    public float getLayoutAlignmentX(Container target) { return (float) 0.5; }
    
    /**
     * @see java.awt.LayoutManager2#getLayoutAlignmentY(java.awt.Container)
     */
    public float getLayoutAlignmentY(Container target) { return (float) 0.5; }

    /**
     * @return Returns the myOrientation.
     */
    protected Orientation getMyOrientation() {
        return myOrientation;
    }

    /**
     * @return Returns the gap.
     */
    protected int getGap() {
        return gap;
    }
}
