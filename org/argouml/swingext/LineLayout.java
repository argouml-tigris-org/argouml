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
        Dimension preferredSize = new Dimension(0,0);
        int gap = 0;
        for (int i = 0 ; i < nComps ; i++) {
            Component comp = parent.getComponent(i);
            if (comp.isVisible()) {
                preferredSize = _orientation.addLength(preferredSize, _orientation.getLength(comp.getPreferredSize()) + gap);
                gap = _gap;
                if (_orientation.getBreadth(comp.getPreferredSize()) > _orientation.getBreadth(preferredSize)) {
                    preferredSize = _orientation.setBreadth(preferredSize, comp.getPreferredSize());
                }
            }
        }
        preferredSize = DimensionUtility.add(preferredSize, parent.getInsets());
        return preferredSize;
    }

    public Dimension minimumLayoutSize(Container parent) {
        int nComps = parent.getComponentCount();
        Dimension minimumSize = new Dimension(0,0);
        int gap = 0;
        for (int i = 0 ; i < nComps ; i++) {
            Component comp = parent.getComponent(i);
            if (comp.isVisible()) {
                minimumSize = _orientation.addLength(minimumSize, _orientation.getLength(comp.getMinimumSize()) + gap);
                gap = _gap;
                if (_orientation.getBreadth(comp.getMinimumSize()) > _orientation.getBreadth(minimumSize)) {
                    minimumSize = _orientation.setBreadth(minimumSize, comp.getMinimumSize());
                }
            }
        }
        minimumSize = DimensionUtility.add(minimumSize, parent.getInsets());
        return minimumSize;
    }

    public Dimension maximumLayoutSize(Container parent) {
        int nComps = parent.getComponentCount();
        Dimension maximumSize = new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE);
        int gap = 0;
        for (int i = 0 ; i < nComps ; i++) {
            Component comp = parent.getComponent(i);
            Dimension componentMaxSize = comp.getMaximumSize();
            if (comp.isVisible() && componentMaxSize != null) {
                maximumSize = _orientation.addLength(maximumSize, _orientation.getLength(componentMaxSize) + gap);
                gap = _gap;
                if (_orientation.getBreadth(componentMaxSize) < _orientation.getBreadth(maximumSize)) {
                    maximumSize = _orientation.setBreadth(maximumSize, componentMaxSize);
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
