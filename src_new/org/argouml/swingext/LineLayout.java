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
import java.util.*;

/**
 * Abstract class for a layout manager that sets all child components
 * in a single row or single column. PrefferedSize and MinimumSize are
 * calculated.
 *
 * @author Bob Tarling
 */
public abstract class LineLayout implements LayoutManager2 {

    public static final Orientation HORIZONTAL = Horizontal.getInstance();
    public static final Orientation VERTICAL = Vertical.getInstance();

    protected Orientation _orientation;
    protected int _gap = 0;
    
    public LineLayout(Orientation orientation) {
        _orientation = orientation;
    }

    public LineLayout(Orientation orientation, int gap) {
        _orientation = orientation;
        _gap = gap;
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void addLayoutComponent(Component comp, Object constraints) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public Dimension preferredLayoutSize(Container parent) {
        int nComps = parent.getComponentCount();
        Dimension preferredSize = new Dimension(0, 0);
        int gap = 0;
        for (int i = 0; i < nComps; i++) {
            Component comp = parent.getComponent(i);
            if (comp.isVisible()) {
                preferredSize =
		    _orientation.addLength(preferredSize,
					   _orientation
					   .getLength(comp.getPreferredSize())
					   + gap);
                gap = _gap;
                if (_orientation.getBreadth(comp.getPreferredSize())
		    > _orientation.getBreadth(preferredSize))
		{
                    preferredSize =
			_orientation.setBreadth(preferredSize,
						comp.getPreferredSize());
                }
            }
        }
        preferredSize = DimensionUtility.add(preferredSize, parent.getInsets());
        return preferredSize;
    }

    public Dimension minimumLayoutSize(Container parent) {
        int nComps = parent.getComponentCount();
        Dimension minimumSize = new Dimension(0, 0);
        int gap = 0;
        for (int i = 0; i < nComps; i++) {
            Component comp = parent.getComponent(i);
            if (comp.isVisible()) {
                minimumSize =
		    _orientation.addLength(minimumSize,
					   _orientation
					   .getLength(comp.getMinimumSize())
					   + gap);
                gap = _gap;
                if (_orientation.getBreadth(comp.getMinimumSize())
		    > _orientation.getBreadth(minimumSize))
		{
                    minimumSize =
			_orientation.setBreadth(minimumSize,
						comp.getMinimumSize());
                }
            }
        }
        minimumSize = DimensionUtility.add(minimumSize, parent.getInsets());
        return minimumSize;
    }

    public Dimension maximumLayoutSize(Container parent) {
        int nComps = parent.getComponentCount();
        Dimension maximumSize =
	    new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        int gap = 0;
        for (int i = 0; i < nComps; i++) {
            Component comp = parent.getComponent(i);
            Dimension componentMaxSize = comp.getMaximumSize();
            if (comp.isVisible() && componentMaxSize != null) {
                maximumSize =
		    _orientation.addLength(maximumSize,
					   _orientation
					   .getLength(componentMaxSize)
					   + gap);
                gap = _gap;
                if (_orientation.getBreadth(componentMaxSize)
		    < _orientation.getBreadth(maximumSize))
		{
                    maximumSize =
			_orientation.setBreadth(maximumSize, componentMaxSize);
                }
            }
        }
        maximumSize = DimensionUtility.add(maximumSize, parent.getInsets());
        return maximumSize;
    }

    public void invalidateLayout(Container target) { }
    public float getLayoutAlignmentX(Container target) { return (float) 0.5; }
    public float getLayoutAlignmentY(Container target) { return (float) 0.5; }
}
