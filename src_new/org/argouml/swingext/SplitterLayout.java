/*
 * SplitterLayout.java
 */
package org.argouml.swingext;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
 * A <code>ProportionalLayout</code> which recognises a contained <code>Splitter</code> and 
 * automatically registers components either side to be resized.
 *
 * @author Bob Tarling
 */
public class SplitterLayout extends ProportionalLayout {

    SplitterMouseListener splitterMouseListener;

    public SplitterLayout() {
        this(HORIZONTAL);
    }

    public SplitterLayout(Orientation orientation) {
        super(orientation);
        splitterMouseListener = new SplitterMouseListener();
    }

    public void addLayoutComponent(String name, Component comp) {
        super.addLayoutComponent(name, comp);

        comp.setSize(comp.getPreferredSize());

        Component parent = comp.getParent();
        if (parent instanceof Container) {

            Container container = (Container)parent;
            int componentPosition = getComponentPosition(comp);
            int componentCount = container.getComponentCount();

            if (comp instanceof Splitter) {
                Splitter splitter = (Splitter)comp;
                splitter.addMouseListener(splitterMouseListener);

                if (componentPosition > 0) {
                    Component westComponent = container.getComponent(componentPosition-1);
                    if (!(westComponent instanceof Splitter)) {
                        splitter.registerComponent(Splitter.WEST, westComponent);
                    }
                }
                if (componentPosition < componentCount-1) {
                    Component eastComponent = container.getComponent(componentPosition+1);
                    if (!(eastComponent instanceof Splitter)) {
                        splitter.registerComponent(Splitter.EAST, eastComponent);
                    }
                }
            }
            else {
                if (componentPosition > 0) {
                    Component westComponent = container.getComponent(componentPosition-1);
                    if (westComponent instanceof Splitter) {
                        Splitter splitter = (Splitter)westComponent;
                        splitter.registerComponent(Splitter.EAST, comp);
                    }
                }
                if (componentPosition < componentCount-1) {
                    Component eastComponent = container.getComponent(componentPosition+1);
                    if (eastComponent instanceof Splitter) {
                        Splitter splitter = (Splitter)eastComponent;
                        splitter.registerComponent(Splitter.WEST, comp);
                    }
                }
            }
        }
    }

    private void calculateProportions() {
        // Find the total proportional size of all visible components
        double totalProportionalLength = 0;

        Enumeration enumKeys = componentTable.keys();
        while (enumKeys.hasMoreElements()) {
            Component comp = (Component)enumKeys.nextElement();
            String size = (String)(componentTable.get(comp));
            if (size.length() != 0) {
                totalProportionalLength += orientation.getLength(comp);
            }
        }

        enumKeys = componentTable.keys();
        while (enumKeys.hasMoreElements()) {
            Component comp = (Component)enumKeys.nextElement();
            String size = (String)(componentTable.get(comp));
            if (size.length() != 0) {
                double proportionalLength = orientation.getLength(comp) * 100 / totalProportionalLength;
                componentTable.put(comp, Double.toString(proportionalLength));
            }
        }
    }

    // Looks at all components that are register as proportional. Recalculates the proportions as a percentage
    // based on their current size.
    private void calculateProportions(Component westComponent, Component eastComponent) {
        String westProportionalLength = (String)(componentTable.get(westComponent));
        String eastProportionalLength = (String)(componentTable.get(eastComponent));

        double westComponentLength = orientation.getLength(westComponent);
        double eastComponentLength = orientation.getLength(eastComponent);
        double totalProportionalLength = Double.parseDouble(westProportionalLength) + Double.parseDouble(eastProportionalLength);
        double newWestProportionalLength = totalProportionalLength * westComponentLength / (westComponentLength+eastComponentLength);
        double newEastProportionalLength = totalProportionalLength - newWestProportionalLength;
        componentTable.put(westComponent, Double.toString(newWestProportionalLength));
        componentTable.put(eastComponent, Double.toString(newEastProportionalLength));
        return;
    }

    public int getComponentPosition(Component comp) {
       Component parent = comp.getParent();
        if (parent instanceof Container) {
            Container container = (Container)parent;
            int numberOfComponents = container.getComponentCount();
            for (int i = 0 ; i < numberOfComponents; ++i) {
                if (comp == container.getComponent(i)) return i;
            }
        }
        return -1;
    }

    private class SplitterMouseListener implements MouseListener {
        public void mouseReleased(MouseEvent me)
        {
            Splitter splitter = (Splitter) me.getComponent();
            Component westComponent = splitter.getRegisteredComponent(Splitter.WEST);
            Component eastComponent = splitter.getRegisteredComponent(Splitter.EAST);
            // Only act on a splitter release if it has both components registered
            if (westComponent != null && eastComponent != null) {
                String westProportionalLength = (String)(componentTable.get(westComponent));
                String eastProportionalLength = (String)(componentTable.get(eastComponent));
                if (westProportionalLength.length() != 0 && eastProportionalLength.length() != 0) {
                    // If the components resized were both flagged to keep proportion
                    // then we only have to recalculate their proportions to eachother
                    calculateProportions(westComponent, eastComponent);
                }
                else if (westProportionalLength.length() != 0 || eastProportionalLength.length() != 0) {
                    // If only one component is flagged as proportioned then all proportioned
                    // components need to have their proportions recalculated.
                    calculateProportions();
                }
            }
        }

        public void mouseEntered(MouseEvent me)
        {
        }
        public void mouseExited(MouseEvent me)
        {
        }

        public void mousePressed(MouseEvent me)
        {
        }

        public void mouseClicked(MouseEvent me)
        {
        }

        public void mouseMoved(MouseEvent me)
        {
        }

        public void mouseDragged(MouseEvent me)
        {
        }
    }
}
