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
 * Horizontal treats length as width, breadth as height, position as x and offset as y.<br />
 * Vertical treats length as height, breadth as width, position as y and offset as x.<br /><br />
 * <br />
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
public abstract class Orientation {

    /**
     * Get an instance of an <code>Orientation</code> perpendicular to this instance.<br />
     * If called on a horizontal instance then a vertical instance is returned.<br />
     * If called on a vertical instance then a horizontal instance is returned.<br />
     *
     * @return A vertical or horizontal orientation.
     */
    public abstract Orientation getPerpendicular();

    /**
     * Get the length of a <code>Dimension</code>.
     *
     * @parameter dim The <code>Dimension</code> of which to determine the length
     * @return The length of the <code>Dimension</code>.
     */
    public abstract int getLength(Dimension dim);

    /**
     * Get the length of a <code>Component</code>.
     *
     * @parameter comp The <code>Component</code> of which to determine the length
     * @return The length of the <code>Component</code>.
     */
    public abstract int getLength(Component comp);
    
    /**
     * Get the usable length of a <code>Container</code> minus its <code>insets</code>.
     *
     * @parameter cont The <code>Container</code> of which to determine the length
     * @return The length of the <code>Component</code>.
     */
    public abstract int getLengthMinusInsets(Container cont);

    /**
     * Get the breadth of a <code>Dimension</code>.
     *
     * @parameter dim The <code>Dimension</code> of which to determine the breadth
     * @return The breadth of the <code>Dimension</code>.
     */
    public abstract int getBreadth(Dimension dim);

    /**
     * Get the breadth of a <code>Component</code>.
     *
     * @parameter comp The <code>Component</code> of which to determine the breadth
     * @return The breadth of the <code>Component</code>.
     */
    public abstract int getBreadth(Component comp);

    /**
     * Get the position of a <code>Point</code>.
     *
     * @parameter point The <code>Point</code> of which to determine the position
     * @return The position of the <code>Point</code>.
     */
    public abstract int getPosition(Point point);

    /**
     * Get the offset of a <code>Point</code>.
     *
     * @parameter point The <code>Point</code> of which to determine the offset
     * @return The offset of the <code>Point</code>.
     */
    public abstract int getOffset(Point point);

    /**
     * Determines the last usable position in a <code>Container</code>. This takes into account
     * the <code>Insets</code> of the <code>Container</code>.
     *
     * @parameter cont the <code>Container</code> from which to determine the last usable 
     *                 position.
     * @return The offset of the <code>Container</code>.
     */
    public abstract int getLastUsablePosition(Container cont);

    /**
     * Determines the first usable offset in a <code>Container</code>. This takes into account
     * the <code>Insets</code> of the <code>Container</code>.
     *
     * @parameter cont the <code>Container</code> from which to determine the first usable 
     *                 position.
     * @return The offset of the <code>Container</code>.
     */
    public abstract int getFirstUsableOffset(Container cont);

    /**
     * Generate a new <code>Point</code> object from position and offset values.
     *
     * @parameter position the required position of the new <code>Point</code>.
     * @parameter offset the required offset of the new <code>Point</code>.
     * @return The newly created <code>Point</code> object.
     */
    public abstract Point newPoint(int position, int offset);
    
    /**
     * Get the position of a <code>Component</code>.
     *
     * @parameter comp The <code>Component</code> of which to determine the position
     * @return The position of the <code>Component</code>.
     */
    public abstract int getPosition(Component comp);

    /**
     * Get the position of a <code>MouseEvent</code>.
     *
     * @parameter me The <code>MouseEvent</code> of which to determine the position
     * @return The position of the <code>MouseEvent</code>.
     */
    public abstract int getPosition(MouseEvent me);

    /**
     * Create a new <code>Dimension</code> from an existing <code>Dimension</code> with its length
     * increased by a given value.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter add The amount to add to the <code>Dimension</code>.
     * @return The resulting <code>Dimension</code>.
     */
    public abstract Dimension addLength(Dimension original, int add);

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
    public abstract Point addToPosition(Point original, int add);

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
    public abstract Dimension setLength(Dimension original, int length);

    /**
     * Create a new <code>Dimension</code> from an existing <code>Dimension</code> with its
     * length changed to the length of another given <code>Dimension</code>.
     *
     * @parameter original The <code>Dimension</code> whose length is to be modified.
     * @parameter length   The value to assign as the new length.
     * @return             The resulting <code>Dimension</code>.
     */
    public abstract Dimension setLength(Dimension original, Dimension length);

    /**
     * Create a new <code>Point</code> from an existing <code>Point</code> with its
     * position changed to a given value.
     *
     * @parameter original The <code>Point</code> whose position is to be modified.
     * @parameter position The value to assign as the new position.
     * @return             The resulting <code>Point</code>.
     */
    public abstract Point setPosition(Point original, int position);

    /**
     * Create a new <code>Dimension</code> from an existing <code>Dimension</code> with its
     * breadth changed to a given value.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter breadth  The breadth to assign to the new <code>Dimension</code>.
     * @return             The resulting <code>Dimension</code>.
     */
    public abstract Dimension setBreadth(Dimension original, int breadth);

    /**
     * Create a new <code>Dimension</code> from an existing <code>Dimension</code> with its
     * breadth changed to the breadth of another given <code>Dimension</code>.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter breadth  The <code>Dimension</code> whose breadth is to be assigned to the new
     *                     <code>Dimension</code>.
     * @return             The resulting <code>Dimension</code>.
     */
    public abstract Dimension setBreadth(Dimension original, Dimension breadth);
    
    /**
     * Get a cursor object pointing in the same direction as the orientation.
     *
     * @return The resulting <code>Cursor</code>.
     */
    public abstract Cursor getCursor();
    
    /**
     * Get an arrow button pointing to the start of the orientation.
     *
     * @return The resulting <code>ArrowButton</code>.
     */
    public abstract ArrowButton getStartArrowButton();
    
    /**
     * Get an arrow button pointing to the end of the orientation.
     *
     * @return The resulting <code>ArrowButton</code>.
     */
    public abstract ArrowButton getEndArrowButton();
    
}
