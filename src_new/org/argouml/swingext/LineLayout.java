/*
 * LineLayout.java
 */
package org.argouml.swingext;

import java.awt.*;
import java.util.*;

/**
 * Abstract class for a layout manager that sets all child components in a single row or single
 * column. PrefferedSize and MinimumSize are calculated.
 *
 * @author Bob Tarling
 */
public abstract class LineLayout implements LayoutManager2 {

    final static public Orientation HORIZONTAL = Horizontal.getInstance();
    final static public Orientation VERTICAL = Vertical.getInstance();

    protected Orientation orientation;

    public LineLayout(Orientation orientation) {
        this.orientation = orientation;
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void addLayoutComponent(Component comp, Object constraints) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public Dimension preferredLayoutSize(Container parent) {
        int nComps = parent.getComponentCount();
        Dimension preferredSize = new Dimension(0,0);
        for (int i = 0 ; i < nComps ; i++) {
            Component comp = parent.getComponent(i);
            if (comp.isVisible()) {
                preferredSize = orientation.addLength(preferredSize, comp.getPreferredSize());
                if (orientation.getBreadth(comp.getPreferredSize()) > orientation.getBreadth(preferredSize)) {
                    preferredSize = orientation.setBreadth(preferredSize, comp.getPreferredSize());
                }
            }
        }
        preferredSize = DimensionUtility.add(preferredSize, parent.getInsets());
        return preferredSize;
    }

    public Dimension minimumLayoutSize(Container parent) {
        int nComps = parent.getComponentCount();
        Dimension minimumSize = new Dimension(0,0);
        for (int i = 0 ; i < nComps ; i++) {
            Component comp = parent.getComponent(i);
            if (comp.isVisible()) {
                minimumSize = orientation.addLength(minimumSize, orientation.getLength(comp.getMinimumSize()));
                if (orientation.getBreadth(comp.getMinimumSize()) > orientation.getBreadth(minimumSize)) {
                    minimumSize = orientation.setBreadth(minimumSize, comp.getMinimumSize());
                }
            }
        }
        minimumSize = DimensionUtility.add(minimumSize, parent.getInsets());
        return minimumSize;
    }

    public Dimension maximumLayoutSize(Container parent) {
        int nComps = parent.getComponentCount();
        Dimension maximumSize = new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE);
        for (int i = 0 ; i < nComps ; i++) {
            Component comp = parent.getComponent(i);
            Dimension componentMaxSize = comp.getMaximumSize();
            if (comp.isVisible() && componentMaxSize != null) {
                maximumSize = orientation.addLength(maximumSize, orientation.getLength(componentMaxSize));
                if (orientation.getBreadth(componentMaxSize) < orientation.getBreadth(maximumSize)) {
                    maximumSize = orientation.setBreadth(maximumSize, componentMaxSize);
                }
            }
        }
        maximumSize = DimensionUtility.add(maximumSize, parent.getInsets());
        return maximumSize;
    }

    public void invalidateLayout(Container target) {}
    public float getLayoutAlignmentX(Container target) {return (float)0.5;}
    public float getLayoutAlignmentY(Container target) {return (float)0.5;}
}
