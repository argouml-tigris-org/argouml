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
 * Orientation.java
 */
package org.argouml.swingext;

import java.awt.*;
import java.awt.event.*;

/**
 * Various utilities to aid components that are aware of their
 * horizontal/vertical orientation.  The Singleton pattern is used to
 * ensure that only one instance of a horizontal and one instance of a
 * vertical <code>Orientation</code> exist.<p>
 *
 * Operations performed using length or breadth are transposed to
 * width and height depending on whether this is a vertical or
 * horizontal orientation.<p>
 *
 * Horizontal treats length as width, breadth as height, position as x
 * and offset as y.<p>
 *
 * Vertical treats length as height, breadth as width, position as y
 * and offset as x.<p>
 *
 *<pre>
 *    HORIZONTAL                                          VERTICAL
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
public abstract class Orientation {

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
     *
     * @return A vertical or horizontal orientation.
     */
    public abstract Orientation getPerpendicular();

    /**
     * Get the length of a <code>Dimension</code>.
     *
     * @parameter dim The <code>Dimension</code> of which to determine
     * the length
     * @return The length of the <code>Dimension</code>.
     */
    public abstract int getLength(Dimension dim);

    /**
     * Get the length of a <code>Component</code>.
     *
     * @parameter comp The <code>Component</code> of which to
     * determine the length
     * @return The length of the <code>Component</code>.
     */
    public abstract int getLength(Component comp);
    
    /**
     * Get the usable length of a <code>Container</code> minus its
     * <code>insets</code>.
     *
     * @parameter cont The <code>Container</code> of which to
     * determine the length
     * @return The length of the <code>Component</code>.
     */
    public abstract int getLengthMinusInsets(Container cont);

    /**
     * Get the breadth of a <code>Dimension</code>.
     *
     * @parameter dim The <code>Dimension</code> of which to determine
     * the breadth
     * @return The breadth of the <code>Dimension</code>.
     */
    public abstract int getBreadth(Dimension dim);

    /**
     * Get the breadth of a <code>Component</code>.
     *
     * @parameter comp The <code>Component</code> of which to
     * determine the breadth
     * @return The breadth of the <code>Component</code>.
     */
    public abstract int getBreadth(Component comp);

    /**
     * Get the position of a <code>Point</code>.
     *
     * @parameter point The <code>Point</code> of which to determine
     * the position
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
     * Determines the last usable position in a
     * <code>Container</code>. This takes into account the
     * <code>Insets</code> of the <code>Container</code>.
     *
     * @parameter cont the <code>Container</code> from which to
     * determine the last usable position.
     * @return The offset of the <code>Container</code>.
     */
    public abstract int getLastUsablePosition(Container cont);

    /**
     * Determines the first usable offset in a
     * <code>Container</code>. This takes into account the
     * <code>Insets</code> of the <code>Container</code>.
     *
     * @parameter cont the <code>Container</code> from which to
     * determine the first usable position.
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
     * @parameter comp The <code>Component</code> of which to
     * determine the position
     * @return The position of the <code>Component</code>.
     */
    public abstract int getPosition(Component comp);

    /**
     * Get the position of a <code>MouseEvent</code>.
     *
     * @parameter me The <code>MouseEvent</code> of which to determine
     * the position
     * @return The position of the <code>MouseEvent</code>.
     */
    public abstract int getPosition(MouseEvent me);

    /**
     * Create a new <code>Dimension</code> from an existing
     * <code>Dimension</code> with its length increased by a given
     * value.
     *
     * @parameter original The <code>Dimension</code> to be added to.
     * @parameter add The amount to add to the <code>Dimension</code>.
     * @return The resulting <code>Dimension</code>.
     */
    public abstract Dimension addLength(Dimension original, int add);

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
    public abstract Point addToPosition(Point original, int add);

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
    public abstract Dimension setLength(Dimension original, int length);

    /**
     * Create a new <code>Dimension</code> from an existing
     * <code>Dimension</code> with its length changed to the length of
     * another given <code>Dimension</code>.
     *
     * @parameter original The <code>Dimension</code> whose length is
     * to be modified.
     * @parameter length   The value to assign as the new length.
     * @return             The resulting <code>Dimension</code>.
     */
    public abstract Dimension setLength(Dimension original, Dimension length);

    /**
     * Create a new <code>Point</code> from an existing
     * <code>Point</code> with its position changed to a given value.
     *
     * @parameter original The <code>Point</code> whose position is to
     * be modified.
     * @parameter position The value to assign as the new position.
     * @return             The resulting <code>Point</code>.
     */
    public abstract Point setPosition(Point original, int position);

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
    public abstract Dimension setBreadth(Dimension original, int breadth);

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
