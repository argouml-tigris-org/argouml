/*
 * Orientation.java
 */
package org.argouml.swingext;

import java.awt.*;
import java.awt.event.*;

/**
 * Various utilities to aid components that are aware of their horizontal/vertical orientation.
 * The Singleton pattern is used to ensure that only one instance of a horizontal and one 
 * instance of a vertical <code>Orientation</code> exist.<br />
 * <br />
 * Operations performed using length or breadth are transposed to width and height depending
 * on whether this is a vertical or horizontal orientation.<br />
 * <br />
 * Horizontal treats length as width, breadth as height and position as x.<br />
 * Vertical treats length as height, breadth as width and position as y.<br /><br />
 *<code>
 *&nbsp;&nbsp;&nbsp;&nbsp;HORIZONTAL&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;VERTICAL<br>
 *<br&nbsp;/>
 *&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A<br>
 *&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|<br>
 *&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;position&nbsp;=&nbsp;y<br>
 *&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|<br>
 *&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;V<br>
 *&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+-------------+&nbsp;&nbsp;&nbsp;A&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+-------------+&nbsp;&nbsp;&nbsp;A<br>
 *&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;|<br>
 *&nbsp;<--position-->&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;breadth&nbsp;=&nbsp;height&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;length&nbsp;=&nbsp;height<br>
 *&nbsp;&nbsp;&nbsp;&nbsp;=&nbsp;x&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;|<br>
 *&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+-------------+&nbsp;&nbsp;&nbsp;V&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+-------------+&nbsp;&nbsp;&nbsp;V<br>
 *&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<--&nbsp;length-->&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<--breadth--><br>
 *&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;=&nbsp;width&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;=&nbsp;width<br>
 *</code>
 * @author Bob Tarling
 */
public class Orientation {

    final static public int HORIZONTAL = 0;
    final static public int VERTICAL = 1;

    final static private Orientation vertical = new Orientation(VERTICAL);
    final static private Orientation horizontal = new Orientation(HORIZONTAL);

    private int orientation = 0;

    private Orientation(int orientation) {
        this.orientation = orientation;
    }

    /**
     * Get an instance of an <code>Orientation</code> object.
     *
     * @parameter orientation value representing the type of orientation required,
     * HORIZONTAL or VERTICAL.
     * @return An instance of <code>Orientation</code>.
     */
    public static Orientation getOrientation(int orientation) {
        if (orientation == VERTICAL) return vertical;
        else return horizontal;
    }

    /**
     * Get an instance of an <code>Orientation</code> perpendicular to this instance.<br />
     * If called on a horizontal instance then a vertical instance is returned.<br />
     * If called on a vertical instance then a horizontal instance is returned.<br />
     *
     * @return A vertical or horizontal orientation.
     */
    public Orientation getPerpendicular() {
        if (orientation == VERTICAL) return horizontal;
        else return vertical;
    }

    /**
     * Get the length of a <code>Dimension</code>.
     *
     * @parameter dim The <code>Dimension</code> of which to determine the length
     * @return The length of the <code>Dimension</code>.
     */
    public int getLength(Dimension dim) {
        if (orientation == VERTICAL) return (int)dim.getHeight();
        else                         return (int)dim.getWidth();
    }

    /**
     * Get the length of a <code>Component</code>.
     *
     * @parameter comp The <code>Component</code> of which to determine the length
     * @return The length of the <code>Component</code>.
     */
    public int getLength(Component comp) {
        if (orientation == VERTICAL) return comp.getHeight();
        else                         return comp.getWidth();
    }
    
    /**
     * Get the usable length of a <code>Container</code> minus its <code>insets</code>.
     *
     * @parameter cont The <code>Container</code> of which to determine the length
     * @return The length of the <code>Component</code>.
     */
    public int getLengthMinusInsets(Container cont) {
        Insets insets = cont.getInsets();
        if (orientation == VERTICAL) return cont.getHeight() - (insets.top + insets.bottom);
        else                         return cont.getWidth() - (insets.left + insets.right);
    }

    /**
     * Get the breadth of a <code>Dimension</code>.
     *
     * @parameter dim The <code>Dimension</code> of which to determine the breadth
     * @return The breadth of the <code>Dimension</code>.
     */
    public int getBreadth(Dimension dim) {
        if (orientation == VERTICAL) return (int)dim.getWidth();
        else                         return (int)dim.getHeight();
    }

    /**
     * Get the breadth of a <code>Component</code>.
     *
     * @parameter comp The <code>Component</code> of which to determine the breadth
     * @return The breadth of the <code>Component</code>.
     */
    public int getBreadth(Component comp) {
        if (orientation == VERTICAL) return comp.getWidth();
        else                         return comp.getHeight();
    }

    /**
     * Get the position of a <code>Point</code>.
     *
     * @parameter point The <code>Point</code> of which to determine the position
     * @return The position of the <code>Point</code>.
     */
    public int getPosition(Point point) {
        if (orientation == VERTICAL) return (int)point.getY();
        else                         return (int)point.getX();
    }

    /**
     * Get the position of a <code>Component</code>.
     *
     * @parameter comp The <code>Component</code> of which to determine the position
     * @return The position of the <code>Component</code>.
     */
    public int getPosition(Component comp) {
        if (orientation == VERTICAL) return comp.getY();
        else                         return comp.getX();
    }

    /**
     * Get the position of a <code>MouseEvent</code>.
     *
     * @parameter me The <code>MouseEvent</code> of which to determine the position
     * @return The position of the <code>MouseEvent</code>.
     */
    public int getPosition(MouseEvent me) {
        if (orientation == VERTICAL) return me.getY();
        else                         return me.getX();
    }

    /**
     * Create a new <code>Dimension</code> from an existing <code>Dimension</code> with its length
     * increased by a given value.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter add The amount to add to the <code>Dimension</code>.
     * @return The resulting <code>Dimension</code>.
     */
    public Dimension addLength(Dimension original, int add) {
        double width;
        double height;
        if (orientation == VERTICAL) {
            width = original.getWidth();
            height = original.getHeight() + add;
        }
        else {
            width = original.getWidth() + add;
            height = original.getHeight();
        }
        return new Dimension((int)width, (int)height);
    }

    /**
     * Create a new <code>Dimension</code> from an existing <code>Dimension</code> with its length
     * increased by the length of another <code>Dimension</code>.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter add The <code>Dimension</code> whose length is to be taken as the added value.
     * @return The resulting <code>Dimension</code>.
     */
    public Dimension addLength(Dimension original, Dimension add) {
        return addLength(original, getLength(add));
    }

    /**
     * Create a new <code>Dimension</code> from an existing <code>Dimension</code> with its length
     * increased by the length of a <code>Component</code>.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter add The <code>Component</code> whose length is to be taken as the added value.
     * @return The resulting <code>Dimension</code>.
     */
    public Dimension addLength(Dimension original, Component add) {
        return addLength(original, getLength(add.getSize()));
    }

    /**
     * Create a new <code>Dimension</code> from an existing <code>Dimension</code> with its length
     * decreased by a given value.
     *
     * @parameter original The <code>Dimension</code> to be subtracted from.
     * @parameter subtract The amount to subtract from the <code>Dimension</code>.
     * @return The resulting <code>Dimension</code>.
     */
    public Dimension subtractLength(Dimension original, int subtract) {
        return addLength(original, -subtract);
    }

    /**
     * Create a new <code>Dimension</code> from an existing <code>Dimension</code> with its length
     * decreased by the length of another <code>Dimension</code>.
     *
     * @parameter original The <code>Dimension</code> to be subtracted from.
     * @parameter subtract The <code>Dimension</code> whose length is to be taken as the subtracted value.
     * @return The resulting <code>Dimension</code>.
     */
    public Dimension subtractLength(Dimension original, Dimension subtract) {
        return subtractLength(original, getLength(subtract));
    }

    /**
     * Create a new <code>Dimension</code> from an existing <code>Dimension</code> with its length
     * decreased by the length of a <code>Component</code>.
     *
     * @parameter original The <code>Dimension</code> to be subtracted from.
     * @parameter subtract The <code>Component</code> whose length is to be taken as the subtracted value.
     * @return The resulting <code>Dimension</code>.
     */
    public Dimension subtractLength(Dimension original, Component subtract) {
        return subtractLength(original, getLength(subtract.getSize()));
    }

    /**
     * Create a new <code>Point</code> from an existing <code>Point</code> with its position
     * increased by a given value.
     *
     * @parameter original The <code>Point</code> to be added to.
     * @parameter add The amount to add to the <code>Point</code>.
     * @return The resulting <code>Point</code>.
     */
    public Point addToPosition(Point original, int add) {
        double x;
        double y;
        if (orientation == VERTICAL) {
            x = original.getX();
            y = original.getY() + add;
        }
        else {
            x = original.getX() + add;
            y = original.getY();
        }
        return new Point((int)x, (int)y);
    }

    /**
     * Create a new <code>Point</code> from an existing <code>Point</code> with its length
     * increased by the length of a <code>Dimension</code>.
     *
     * @parameter original The <code>Point</code> to be added to.
     * @parameter add The <code>Dimension</code> whose length is to be taken as the added value.
     * @return The resulting <code>Point</code>.
     */
    public Point addToPosition(Point original, Dimension add) {
        return addToPosition(original, getLength(add));
    }

    /**
     * Create a new <code>Point</code> from an existing <code>Point</code> with its length
     * increased by the length of a <code>Component</code>.
     *
     * @parameter original The <code>Point</code> to be added to.
     * @parameter add The <code>Dimension</code> whose length is to be taken as the added value.
     * @return The resulting <code>Point</code>.
     */
    public Point addToPosition(Point original, Component add) {
        return addToPosition(original, getLength(add.getSize()));
    }

    /**
     * Create a new <code>Point</code> from an existing <code>Point</code> with its position
     * decreased by a given value.
     *
     * @parameter original The <code>Point</code> to be added to.
     * @parameter subtract The amount to subtract to the <code>Point</code>.
     * @return The resulting <code>Point</code>.
     */
    public Point subtractFromPosition(Point original, int subtract) {
        return addToPosition(original, -subtract);
    }

    /**
     * Create a new <code>Point</code> from an existing <code>Point</code> with its length
     * decreased by the length of a <code>Dimension</code>.
     *
     * @parameter original The <code>Point</code> to be added to.
     * @parameter subtract The <code>Dimension</code> whose length is to be taken as the subtracted value.
     * @return The resulting <code>Point</code>.
     */
    public Point subtractFromPosition(Point original, Dimension subtract) {
        return addToPosition(original, -getLength(subtract));
    }

    /**
     * Create a new <code>Point</code> from an existing <code>Point</code> with its length
     * decreased by the length of a <code>Component</code>.
     *
     * @parameter original The <code>Point</code> to be added to.
     * @parameter subtract The <code>Component</code> whose length is to be taken as the subtracted value.
     * @return The resulting <code>Point</code>.
     */
    public Point subtractFromPosition(Point original, Component subtract) {
        return addToPosition(original, -getLength(subtract.getSize()));
    }

    /**
     * Create a new <code>Dimension</code> from an existing <code>Dimension</code> with its
     * length changed to a given value.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter length   The length to assign to the new <code>Dimension</code>.
     * @return             The resulting <code>Dimension</code>.
     */
    public Dimension setLength(Dimension original, int length) {
        if (orientation == VERTICAL) return new Dimension((int)original.getWidth(), length);
        else                         return new Dimension(length, (int)original.getHeight());
    }

    /**
     * Create a new <code>Dimension</code> from an existing <code>Dimension</code> with its
     * length changed to the length of another given <code>Dimension</code>.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter length   The <code>Dimension</code> whose length is to be assigned to the new
     *                     <code>Dimension</code>.
     * @return             The resulting <code>Dimension</code>.
     */
    public Dimension setLength(Dimension original, Dimension length) {
        if (orientation == VERTICAL) return new Dimension((int)original.getWidth(), (int)length.getHeight());
        else                         return new Dimension((int)length.getWidth(), (int)original.getHeight());
    }

    /**
     * Create a new <code>Dimension</code> from an existing <code>Dimension</code> with its
     * breadth changed to a given value.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter breadth  The breadth to assign to the new <code>Dimension</code>.
     * @return             The resulting <code>Dimension</code>.
     */
    public Dimension setBreadth(Dimension original, int breadth) {
        if (orientation == VERTICAL) return new Dimension(breadth, (int)original.getHeight());
        else                         return new Dimension((int)original.getWidth(), breadth);
    }

    /**
     * Create a new <code>Dimension</code> from an existing <code>Dimension</code> with its
     * breadth changed to the breadth of another given <code>Dimension</code>.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter breadth  The <code>Dimension</code> whose breadth is to be assigned to the new
     *                     <code>Dimension</code>.
     * @return             The resulting <code>Dimension</code>.
     */
    public Dimension setBreadth(Dimension original, Dimension breadth) {
        if (orientation == VERTICAL) return new Dimension((int)breadth.getWidth(), (int)original.getHeight());
        else                         return new Dimension((int)original.getWidth(), (int)breadth.getHeight());
    }

    /**
     * Determine if the <code>Dimension</code> is a vertical
     *
     * @return true if vertical.
     */
    public boolean isVertical() {
        return (orientation == VERTICAL);
    }

    /**
     * Determine if the <code>Dimension</code> is a horizontal
     *
     * @return true if horizontal.
     */
    public boolean isHorizontal() {
        return (orientation == HORIZONTAL);
    }
}
