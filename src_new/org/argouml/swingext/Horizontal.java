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
 * Horizontal.java
 */
package org.argouml.swingext;

import java.awt.*;
import java.awt.event.*;
import javax.swing.border.Border;

/**
 * A horizontal implementation of <code>Orientation</code>.  The
 * Singleton pattern is used to ensure that only one instance of this
 * class can exist.<p>
 *
 * <code>Horizontal</code> treats length as width, breadth as height
 * and position as x.<p>
 *
 *<pre>
 *    HORIZONTAL
 *
 *                                                    A
 *                                                    |
 *                                                 position = y
 *                                                    |
 *                                                    V
 *                +-------------+   A                 +-------------+   A
 *                |             |   |                 |             |   |
 * <--position--> |             | breadth = height    |             | length =
 *    = x         |             |   |                 |             |   |height
 *                +-------------+   V                 +-------------+   V
 *                 <-- length-->                       <--breadth-->
 *                    = width                             = width
 *</pre>
 *
 * @author Bob Tarling
 */
public class Horizontal extends Orientation {

    private static final Horizontal horizontal = new Horizontal();

    protected Horizontal() {
    }

    /**
     * Get an instance of a <code>Horizontal</code> object.
     *
     * @parameter orientation value representing the type of
     * orientation required, HORIZONTAL or VERTICAL.
     * @return An instance of <code>Orientation</code>.
     */
    public static Orientation getInstance() {
        return horizontal;
    }

    /**
     * Get an instance of an <code>Orientation</code> perpendicular to
     * this instance.<p>
     *
     * If called on a horizontal instance then a vertical instance is
     * returned.<p>
     *
     * If called on a vertical instance then a horizontal instance is
     * returned.<p>
     *
     * @return A vertical or horizontal orientation.
     */
    public Orientation getPerpendicular() {
        return Vertical.getInstance();
    }

    /**
     * Get the length of a <code>Dimension</code>.
     *
     * @parameter dim The <code>Dimension</code> of which to determine
     * the length
     * @return The length of the <code>Dimension</code>.
     */
    public int getLength(Dimension dim) {
        return (int) dim.getWidth();
    }

    /**
     * Get the length of a <code>Component</code>.
     *
     * @parameter comp The <code>Component</code> of which to
     * determine the length
     * @return The length of the <code>Component</code>.
     */
    public int getLength(Component comp) {
        return comp.getWidth();
    }
    
    /**
     * Get the usable length of a <code>Container</code> minus its
     * <code>insets</code>.
     *
     * @parameter cont The <code>Container</code> of which to
     * determine the length
     * @return The length of the <code>Component</code>.
     */
    public int getLengthMinusInsets(Container cont) {
        Insets insets = cont.getInsets();
        return cont.getWidth() - (insets.left + insets.right);
    }

    /**
     * Get the breadth of a <code>Dimension</code>.
     *
     * @parameter dim The <code>Dimension</code> of which to determine
     * the breadth
     * @return The breadth of the <code>Dimension</code>.
     */
    public int getBreadth(Dimension dim) {
        return (int) dim.getHeight();
    }

    /**
     * Get the breadth of a <code>Component</code>.
     *
     * @parameter comp The <code>Component</code> of which to
     * determine the breadth
     * @return The breadth of the <code>Component</code>.
     */
    public int getBreadth(Component comp) {
        return comp.getHeight();
    }

    /**
     * Get the position of a <code>Point</code>.
     *
     * @parameter point The <code>Point</code> of which to determine
     * the position
     * @return The position of the <code>Point</code>.
     */
    public int getPosition(Point point) {
        return (int) point.getX();
    }

    /**
     * Get the position of a <code>Component</code>.
     *
     * @parameter comp The <code>Component</code> of which to
     * determine the position
     * @return The position of the <code>Component</code>.
     */
    public int getPosition(Component comp) {
        return comp.getX();
    }

    /**
     * Get the offset of a <code>Component</code>.
     *
     * @parameter comp The <code>Component</code> of which to
     * determine the offset.
     * @return The position of the <code>Component</code>.
     */
    public int getOffset(Point point) {
        return (int) point.getY();
    }

    /**
     * Determines the last usable position in a
     * <code>Container</code>. This takes into account the
     * <code>Insets</code> of the <code>Container</code>.
     *
     * @parameter cont the <code>Container</code> from which to
     * determine the last usable position.
     * @return The offset of the <code>Container</code>.
     */
    public int getLastUsablePosition(Container cont) {
        return cont.getWidth() - cont.getInsets().right;
    }

    /**
     * Determines the first usable offset in a
     * <code>Container</code>. This takes into account the
     * <code>Insets</code> of the <code>Container</code>.
     *
     * @parameter cont the <code>Container</code> from which to
     * determine the first usable position.
     * @return The offset of the <code>Container</code>.
     */
    public int getFirstUsableOffset(Container cont) {
        return cont.getInsets().left;
    }

    /**
     * Generate a new <code>Point</code> object from position and offset values.
     *
     * @parameter position the required position of the new <code>Point</code>.
     * @parameter offset the required offset of the new <code>Point</code>.
     * @return The newly created <code>Point</code> object.
     */
    public Point newPoint(int position, int offset) {
        return new Point(position, offset);
    }

    /**
     * Get the position of a <code>MouseEvent</code>.
     *
     * @parameter me The <code>MouseEvent</code> of which to determine
     * the position
     * @return The position of the <code>MouseEvent</code>.
     */
    public int getPosition(MouseEvent me) {
        return me.getX();
    }

    /**
     * Create a new <code>Dimension</code> from an existing
     * <code>Dimension</code> with its length increased by a given
     * value.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter add The amount to add to the <code>Dimension</code>.
     * @return The resulting <code>Dimension</code>.
     */
    public Dimension addLength(Dimension original, int add) {
        double width = original.getWidth() + add;
        double height = original.getHeight();
        return new Dimension((int) width, (int) height);
    }

    /**
     * Create a new <code>Dimension</code> from an existing
     * <code>Dimension</code> with its length increased by the length
     * of another <code>Dimension</code>.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter add The <code>Dimension</code> whose length is to be
     * taken as the added value.
     * @return The resulting <code>Dimension</code>.
     */
    public Dimension addLength(Dimension original, Dimension add) {
        return addLength(original, getLength(add));
    }

    /**
     * Create a new <code>Dimension</code> from an existing
     * <code>Dimension</code> with its length increased by the length
     * of a <code>Component</code>.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter add The <code>Component</code> whose length is to be
     * taken as the added value.
     * @return The resulting <code>Dimension</code>.
     */
    public Dimension addLength(Dimension original, Component add) {
        return addLength(original, getLength(add.getSize()));
    }

    /**
     * Create a new <code>Dimension</code> from an existing
     * <code>Dimension</code> with its length decreased by a given
     * value.
     *
     * @parameter original The <code>Dimension</code> to be subtracted from.
     * @parameter subtract The amount to subtract from the
     * <code>Dimension</code>.
     * @return The resulting <code>Dimension</code>.
     */
    public Dimension subtractLength(Dimension original, int subtract) {
        return addLength(original, -subtract);
    }

    /**
     * Create a new <code>Dimension</code> from an existing
     * <code>Dimension</code> with its length decreased by the length
     * of another <code>Dimension</code>.
     *
     * @parameter original The <code>Dimension</code> to be subtracted from.
     * @parameter subtract The <code>Dimension</code> whose length is
     * to be taken as the subtracted value.
     * @return The resulting <code>Dimension</code>.
     */
    public Dimension subtractLength(Dimension original, Dimension subtract) {
        return subtractLength(original, getLength(subtract));
    }

    /**
     * Create a new <code>Dimension</code> from an existing
     * <code>Dimension</code> with its length decreased by the length
     * of a <code>Component</code>.
     *
     * @parameter original The <code>Dimension</code> to be subtracted from.
     * @parameter subtract The <code>Component</code> whose length is
     * to be taken as the subtracted value.
     * @return The resulting <code>Dimension</code>.
     */
    public Dimension subtractLength(Dimension original, Component subtract) {
        return subtractLength(original, getLength(subtract.getSize()));
    }

    /**
     * Create a new <code>Point</code> from an existing
     * <code>Point</code> with its position increased by a given
     * value.
     *
     * @parameter original The <code>Point</code> to be added to.
     * @parameter add The amount to add to the <code>Point</code>.
     * @return The resulting <code>Point</code>.
     */
    public Point addToPosition(Point original, int add) {
        double x = original.getX() + add;
        double y = original.getY();
        return new Point((int) x, (int) y);
    }

    /**
     * Create a new <code>Point</code> from an existing
     * <code>Point</code> with its length increased by the length of a
     * <code>Dimension</code>.
     *
     * @parameter original The <code>Point</code> to be added to.
     * @parameter add The <code>Dimension</code> whose length is to be
     * taken as the added value.
     * @return The resulting <code>Point</code>.
     */
    public Point addToPosition(Point original, Dimension add) {
        return addToPosition(original, getLength(add));
    }

    /**
     * Create a new <code>Point</code> from an existing
     * <code>Point</code> with its length increased by the length of a
     * <code>Component</code>.
     *
     * @parameter original The <code>Point</code> to be added to.
     * @parameter add The <code>Dimension</code> whose length is to be
     * taken as the added value.
     * @return The resulting <code>Point</code>.
     */
    public Point addToPosition(Point original, Component add) {
        return addToPosition(original, getLength(add.getSize()));
    }

    /**
     * Create a new <code>Point</code> from an existing
     * <code>Point</code> with its position decreased by a given
     * value.
     *
     * @parameter original The <code>Point</code> to be added to.
     * @parameter subtract The amount to subtract to the <code>Point</code>.
     * @return The resulting <code>Point</code>.
     */
    public Point subtractFromPosition(Point original, int subtract) {
        return addToPosition(original, -subtract);
    }

    /**
     * Create a new <code>Point</code> from an existing
     * <code>Point</code> with its length decreased by the length of a
     * <code>Dimension</code>.
     *
     * @parameter original The <code>Point</code> to be added to.
     * @parameter subtract The <code>Dimension</code> whose length is
     * to be taken as the subtracted value.
     * @return The resulting <code>Point</code>.
     */
    public Point subtractFromPosition(Point original, Dimension subtract) {
        return addToPosition(original, -getLength(subtract));
    }

    /**
     * Create a new <code>Point</code> from an existing
     * <code>Point</code> with its length decreased by the length of a
     * <code>Component</code>.
     *
     * @parameter original The <code>Point</code> to be added to.
     * @parameter subtract The <code>Component</code> whose length is
     * to be taken as the subtracted value.
     * @return The resulting <code>Point</code>.
     */
    public Point subtractFromPosition(Point original, Component subtract) {
        return addToPosition(original, -getLength(subtract.getSize()));
    }

    /**
     * Create a new <code>Dimension</code> from an existing
     * <code>Dimension</code> with its length changed to a given
     * value.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter length The length to assign to the new
     * <code>Dimension</code>.
     * @return             The resulting <code>Dimension</code>.
     */
    public Dimension setLength(Dimension original, int length) {
        return new Dimension(length, (int) original.getHeight());
    }

    /**
     * Create a new <code>Dimension</code> from an existing
     * <code>Dimension</code> with its length changed to the length of
     * another given <code>Dimension</code>.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter length The <code>Dimension</code> whose length is to
     * be assigned to the new <code>Dimension</code>.
     * @return             The resulting <code>Dimension</code>.
     */
    public Dimension setLength(Dimension original, Dimension length) {
        return new Dimension((int) length.getWidth(),
			     (int) original.getHeight());
    }

    /**
     * Create a new <code>Dimension</code> from an existing
     * <code>Dimension</code> with its breadth changed to a given
     * value.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter breadth The breadth to assign to the new
     * <code>Dimension</code>.
     * @return             The resulting <code>Dimension</code>.
     */
    public Dimension setBreadth(Dimension original, int breadth) {
        return new Dimension((int) original.getWidth(), breadth);
    }

    /**
     * Create a new <code>Dimension</code> from an existing
     * <code>Dimension</code> with its breadth changed to the breadth
     * of another given <code>Dimension</code>.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter breadth The <code>Dimension</code> whose breadth is
     * to be assigned to the new <code>Dimension</code>.
     * @return             The resulting <code>Dimension</code>.
     */
    public Dimension setBreadth(Dimension original, Dimension breadth) {
        return new Dimension((int) original.getWidth(),
			     (int) breadth.getHeight());
    }
    
    /**
     * Create a new <code>Point</code> from an existing
     * <code>Point</code> with its position changed to a given value.
     *
     * @parameter original The <code>Point</code> whose position is to
     * be modified.
     * @parameter position The value to assign as the new position.
     * @return             The resulting <code>Point</code>.
     */
    public Point setPosition(Point original, int position) {
        return new Point(position, (int) original.getY());
    }
    
    /**
     * Get a cursor object pointing in the same direction as the orientation.
     *
     * @return The resulting <code>Cursor</code>.
     */
    public Cursor getCursor() {
        return new Cursor(Cursor.W_RESIZE_CURSOR);
    }
    
    /**
     * Get an arrow button pointing to the start of the orientation.
     *
     * @return The resulting <code>ArrowButton</code>.
     */
    public ArrowButton getStartArrowButton() {
        return new ArrowButton(ArrowButton.WEST, (Border) null);
    }
    
    /**
     * Get an arrow button pointing to the end of the orientation.
     *
     * @return The resulting <code>ArrowButton</code>.
     */
    public ArrowButton getEndArrowButton() {
        return new ArrowButton(ArrowButton.EAST, (Border) null);
    }
}
