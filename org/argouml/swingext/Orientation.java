package org.argouml.swingext;

import java.awt.*;
import java.awt.event.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Bob Tarling
 * @version 1.0
 */

public class Orientation {

    final static public int HORIZONTAL = 0;
    final static public int VERTICAL = 1;

    final static public Orientation vertical = new Orientation(VERTICAL);
    final static public Orientation horizontal = new Orientation(HORIZONTAL);

    private int orientation = 0;

    private Orientation(int orientation) {
        this.orientation = orientation;
    }

    public static Orientation getOrientation(int orientation) {
        if (orientation == VERTICAL) return vertical;
        else return horizontal;
    }

    public Orientation getPerpendicular() {
        if (orientation == VERTICAL) return horizontal;
        else return vertical;
    }

    // Get either width or height depending on orientation
    public int getLength(Dimension dim) {
        if (orientation == VERTICAL) return (int)dim.getHeight();
        else                         return (int)dim.getWidth();
    }
    public int getLength(Component comp) {
        if (orientation == VERTICAL) return comp.getHeight();
        else                         return comp.getWidth();
    }
    public int getLengthMinusInsets(Container cont) {
        Insets insets = cont.getInsets();
        if (orientation == VERTICAL) return cont.getHeight() - (insets.top + insets.bottom);
        else                         return cont.getWidth() - (insets.left + insets.right);
    }

    // Get either width or height depending on orientation
    public int getBreadth(Dimension dim) {
        if (orientation == VERTICAL) return (int)dim.getWidth();
        else                         return (int)dim.getHeight();
    }
    public int getBreadth(Component comp) {
        if (orientation == VERTICAL) return comp.getWidth();
        else                         return comp.getHeight();
    }

    // Get either x or y depending on orientation
    public int getPosition(Point point) {
        if (orientation == VERTICAL) return (int)point.getY();
        else                         return (int)point.getX();
    }
    public int getPosition(Component comp) {
        if (orientation == VERTICAL) return comp.getY();
        else                         return comp.getX();
    }
    public int getPosition(MouseEvent me) {
        if (orientation == VERTICAL) return me.getY();
        else                         return me.getX();
    }

    public Dimension addLength(Dimension target, int source) {
        double width;
        double height;
        if (orientation == VERTICAL) {
            width = target.getWidth();
            height = target.getHeight() + source;
        }
        else {
            width = target.getWidth() + source;
            height = target.getHeight();
        }
        return new Dimension((int)width, (int)height);
    }

    public Dimension addLength(Dimension target, Dimension source) {
        return addLength(target, getLength(source));
    }

    public Dimension addLength(Dimension target, Component comp) {
        return addLength(target, getLength(comp.getSize()));
    }

    public Dimension subtractLength(Dimension target, int source) {
        return addLength(target, -source);
    }

    public Dimension subtractLength(Dimension target, Dimension source) {
        return subtractLength(target, getLength(source));
    }

    public Dimension subtractLength(Dimension target, Component comp) {
        return subtractLength(target, getLength(comp.getSize()));
    }

    public Point addLength(Point target, int source) {
        double x;
        double y;
        if (orientation == VERTICAL) {
            x = target.getX();
            y = target.getY() + source;
        }
        else {
            x = target.getX() + source;
            y = target.getY();
        }
        return new Point((int)x, (int)y);
    }

    public Point addLength(Point target, Dimension source) {
        return addLength(target, getLength(source));
    }

    public Point addLength(Point target, Component comp) {
        return addLength(target, getLength(comp.getSize()));
    }

    public Point subtractLength(Point target, int source) {
        return addLength(target, -source);
    }

    public Point subtractLength(Point target, Dimension source) {
        return subtractLength(target, getLength(source));
    }

    public Point subtractLength(Point target, Component comp) {
        return subtractLength(target, getLength(comp.getSize()));
    }

    public Dimension add(Dimension target, Dimension source) {
        return new Dimension((int)(target.getWidth() + source.getWidth()), (int)(target.getHeight() + source.getHeight()));
    }

    public Dimension add(Dimension target, Insets source) {
        return new Dimension((int)target.getWidth() + source.right + source.left, (int)target.getHeight() + source.top + source.bottom);
    }

    public Dimension setLength(Dimension target, int source) {
        if (orientation == VERTICAL) return new Dimension((int)target.getWidth(), source);
        else                         return new Dimension(source, (int)target.getHeight());
    }

    public Dimension setLength(Dimension target, Dimension source) {
        if (orientation == VERTICAL) return new Dimension((int)target.getWidth(), (int)source.getHeight());
        else                         return new Dimension((int)source.getWidth(), (int)target.getHeight());
    }

    public Dimension setBreadth(Dimension target, int source) {
        if (orientation == VERTICAL) return new Dimension(source, (int)target.getHeight());
        else                         return new Dimension((int)target.getWidth(), source);
    }

    public Dimension setBreadth(Dimension target, Dimension source) {
        if (orientation == VERTICAL) return new Dimension((int)source.getWidth(), (int)target.getHeight());
        else                         return new Dimension((int)target.getWidth(), (int)source.getHeight());
    }

    public boolean isVertical() {
        return (orientation == VERTICAL);
    }

    public boolean isHorizontal() {
        return (orientation == HORIZONTAL);
    }
}
