package org.argouml.swingext;

import java.awt.*;
import java.util.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */


public class SerialLayout extends LineLayout {
    public final static int VERTICAL = Orientation.VERTICAL;
    public final static int HORIZONTAL = Orientation.HORIZONTAL;

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

    public SerialLayout(int orientation) {
        this(orientation, WEST, LEFTTORIGHT, TOP);
    }
    public SerialLayout(int orientation, String position) {
        this(orientation, position, LEFTTORIGHT, TOP);
    }
    public SerialLayout(int orientation, String position, int direction) {
        this(orientation, position, direction, TOP);
    }
    public SerialLayout(int orientation, String position, int direction, int alignment) {
        super(orientation);
        this.position = position;
        this.direction = direction;
        this.alignment = alignment;
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
                    loc = orientation.addLength(loc, comp);
                }
            }
        }
        else {
            if (orientation.isHorizontal()) loc = new Point(parent.getWidth() - insets.right, insets.top);
            else                            loc = new Point(insets.left, parent.getHeight() - insets.bottom);

            int nComps = parent.getComponentCount();
            for (int i = 0 ; i < nComps ; i++) {
                Component comp = parent.getComponent(i);
                if (comp != null && comp.isVisible()) {
                    if (loc == null) System.out.println("null orientation");
                    loc = orientation.subtractLength(loc, comp);
                    comp.setSize(comp.getPreferredSize());
                    comp.setLocation(loc);
                }
            }
        }
    }
}
