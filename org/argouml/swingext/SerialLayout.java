/*
 * SerialLayout.java
 */
package org.argouml.swingext;

import java.awt.*;
import java.util.*;

/**
 * Lays out components in a single row or column starting from any side and aligning  components
 * to eachother.<br />
 * Components can be set to start draw from, LEFTTORIGHT, TOPTOBOTTOM, RIGHTTOLEFT or BOTTOMTOTOP.<br />
 * Components will line up with eachother by edge or follow a common central line.<br />
 * The gap to leave before the first component and the following gaps between each component can
 * be set to be set.
 *
 * @author Bob Tarling
 */
public class SerialLayout extends LineLayout {
    public final static int LEFTTORIGHT = 10;
    public final static int TOPTOBOTTOM = 10;
    public final static int RIGHTTOLEFT = 11;
    public final static int BOTTOMTOTOP = 11;

    public final static String NORTH = "North";
    public final static String SOUTH = "South";
    public final static String EAST = "East";
    public final static String WEST = "West";
    public final static String NORTHEAST = "NorthEast";
    public final static String NORTHWEST = "NorthWest";
    public final static String SOUTHEAST = "SouthEast";
    public final static String SOUTHWEST = "SouthWest";

    public final static int LEFT = 20;
    public final static int RIGHT = 21;
    public final static int TOP = 20;
    public final static int BOTTOM = 21;
    public final static int CENTER = 22;

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
    public SerialLayout(Orientation orientation, String position, int direction) {
        this(orientation, position, direction, TOP);
    }
    public SerialLayout(Orientation orientation, String position, int direction, int alignment) {
        super(orientation);
        this.position = position;
        this.direction = direction;
        this.alignment = alignment;
    }

    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();

        Point loc;
        if (direction == LEFTTORIGHT) {
            loc = new Point(insets.left, insets.top);
            int nComps = parent.getComponentCount();
            for (int i = 0 ; i < nComps ; i++) {
                Component comp = parent.getComponent(i);
                if (comp != null && comp.isVisible()) {
                    comp.setSize(comp.getPreferredSize());
                    comp.setLocation(loc);
                    loc = orientation.addToPosition(loc, comp);
                }
            }
        }
        else {
            int lastUsablePosition = orientation.getLastUsablePosition(parent);
            int firstUsableOffset = orientation.getFirstUsableOffset(parent);
            loc = orientation.newPoint(lastUsablePosition, firstUsableOffset);

            int nComps = parent.getComponentCount();
            for (int i = 0 ; i < nComps ; i++) {
                Component comp = parent.getComponent(i);
                if (comp != null && comp.isVisible()) {
                    if (loc == null) System.out.println("null orientation");
                    loc = orientation.subtractFromPosition(loc, comp);
                    comp.setSize(comp.getPreferredSize());
                    comp.setLocation(loc);
                }
            }
        }
    }
}
