/*
 * Orientation.java
 */
package org.argouml.swingext;

import java.awt.*;
import java.awt.event.*;
import javax.swing.border.Border;

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
 *
 * @author Bob Tarling
 */
public class Vertical extends Orientation {

    final static private Vertical vertical = new Vertical();

    protected Vertical() {
    }

    /**
     * Get an instance of an <code>Orientation</code> object.
     *
     * @parameter orientation value representing the type of orientation required,
     * HORIZONTAL or VERTICAL.
     * @return An instance of <code>Orientation</code>.
     */
    public static Orientation getInstance() {
        return vertical;
    }

    /**
     * Get an instance of an <code>Orientation</code> perpendicular to this instance.<br />
     * If called on a horizontal instance then a vertical instance is returned.<br />
     * If called on a vertical instance then a horizontal instance is returned.<br />
     *
     * @return A vertical or horizontal orientation.
     */
    public Orientation getPerpendicular() {
        return Horizontal.getInstance();
    }

    /**
     * Get the length of a <code>Dimension</code>.
     *
     * @parameter dim The <code>Dimension</code> of which to determine the length
     * @return The length of the <code>Dimension</code>.
     */
    public int getLength(Dimension dim) {
        return (int)dim.getHeight();
    }

    /**
     * Get the length of a <code>Component</code>.
     *
     * @parameter comp The <code>Component</code> of which to determine the length
     * @return The length of the <code>Component</code>.
     */
    public int getLength(Component comp) {
        return comp.getHeight();
    }
    
    /**
     * Get the usable length of a <code>Container</code> minus its <code>insets</code>.
     *
     * @parameter cont The <code>Container</code> of which to determine the length
     * @return The length of the <code>Component</code>.
     */
    public int getLengthMinusInsets(Container cont) {
        Insets insets = cont.getInsets();
        return cont.getHeight() - (insets.top + insets.bottom);
    }

    /**
     * Get the breadth of a <code>Dimension</code>.
     *
     * @parameter dim The <code>Dimension</code> of which to determine the breadth
     * @return The breadth of the <code>Dimension</code>.
     */
    public int getBreadth(Dimension dim) {
        return (int)dim.getWidth();
    }

    /**
     * Get the breadth of a <code>Component</code>.
     *
     * @parameter comp The <code>Component</code> of which to determine the breadth
     * @return The breadth of the <code>Component</code>.
     */
    public int getBreadth(Component comp) {
        return comp.getWidth();
    }

    /**
     * Get the position of a <code>Point</code>.
     *
     * @parameter point The <code>Point</code> of which to determine the position
     * @return The position of the <code>Point</code>.
     */
    public int getPosition(Point point) {
        return (int)point.getY();
    }

    /**
     * Get the offset of a <code>Point</code>.
     *
     * @parameter point The <code>Point</code> of which to determine the offset.
     * @return The position of the <code>Point</code>.
     */
    public int getOffset(Point point) {
        return (int)point.getX();
    }

    /**
     * Determines the last usable position in a <code>Container</code>. This takes into account
     * the <code>Insets</code> of the <code>Container</code>.
     *
     * @parameter cont the <code>Container</code> from which to determine the last usable 
     *                 position.
     * @return The offset of the <code>Container</code>.
     */
    public int getLastUsablePosition(Container cont) {
        return cont.getHeight() - cont.getInsets().bottom;
    }

    /**
     * Determines the first usable offset in a <code>Container</code>. This takes into account
     * the <code>Insets</code> of the <code>Container</code>.
     *
     * @parameter cont the <code>Container</code> from which to determine the first usable 
     *                 position.
     * @return The offset of the <code>Container</code>.
     */
    public int getFirstUsableOffset(Container cont) {
        return cont.getInsets().top;
    }

    
    /**
     * Generate a new <code>Point</code> object from position and offset values.
     *
     * @parameter position the required position of the new <code>Point</code>.
     * @parameter offset the required offset of the new <code>Point</code>.
     * @return The newly created <code>Point</code> object.
     */
    public Point newPoint(int position, int offset) {
        return new Point(offset, position);
    }

    
    /**
     * Get the position of a <code>Component</code>.
     *
     * @parameter comp The <code>Component</code> of which to determine the position
     * @return The position of the <code>Component</code>.
     */
    public int getPosition(Component comp) {
        return comp.getY();
    }

    /**
     * Get the position of a <code>MouseEvent</code>.
     *
     * @parameter me The <code>MouseEvent</code> of which to determine the position
     * @return The position of the <code>MouseEvent</code>.
     */
    public int getPosition(MouseEvent me) {
        return me.getY();
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
        double width = original.getWidth();
        double height = original.getHeight() + add;
        return new Dimension((int)width, (int)height);
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
        double x = original.getX();
        double y = original.getY() + add;
        return new Point((int)x, (int)y);
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
        return new Dimension((int)original.getWidth(), length);
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
        return new Dimension((int)original.getWidth(), (int)length.getHeight());
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
        return new Dimension(breadth, (int)original.getHeight());
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
        return new Dimension((int)breadth.getWidth(), (int)original.getHeight());
    }

    /**
     * Create a new <code>Point</code> from an existing <code>Point</code> with its
     * position changed to a given value.
     *
     * @parameter original The <code>Point</code> whose position is to be modified.
     * @parameter position The value to assign as the new position.
     * @return             The resulting <code>Point</code>.
     */
    public Point setPosition(Point original, int position) {
        return new Point((int)original.getX(), position);
    }
    
    /**
     * Get a cursor object pointing in the same direction as the orientation.
     *
     * @return The resulting <code>Cursor</code>.
     */
    public Cursor getCursor() {
        return new Cursor(Cursor.N_RESIZE_CURSOR);
    }
    
    /**
     * Get an arrow button pointing to the start of the orientation.
     *
     * @return The resulting <code>ArrowButton</code>.
     */
    public ArrowButton getStartArrowButton() {
        return new ArrowButton(ArrowButton.NORTH, (Border)null);
    }
    
    /**
     * Get an arrow button pointing to the end of the orientation.
     *
     * @return The resulting <code>ArrowButton</code>.
     */
    public ArrowButton getEndArrowButton() {
        return new ArrowButton(ArrowButton.SOUTH, (Border)null);
    }
}
