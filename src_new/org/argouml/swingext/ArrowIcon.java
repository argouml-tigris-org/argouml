/*
 * ArrowIcon.java
 *
 * Created on 22 July 2002, 21:15
 */

package org.argouml.swingext;

import java.io.Serializable;
import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * A metal look and feel arrow icon that can be created to point to a compass point.
 *
 * @author  administrator
 */
public class ArrowIcon implements Icon, Serializable, SwingConstants {

    private static final int SIZE = 10;
    private static final int ONE_TOUCH_SIZE = 6;
    private static final int ONE_TOUCH_OFFSET = 2;

    // Sprite buffer for the arrow image of the left button
    private int[][] buffer;

    private int[][] northWestBuffer = {{0, 0, 0, 2, 2, 0, 0, 0, 0},
                               {0, 0, 2, 1, 1, 1, 0, 0, 0},
                               {0, 2, 1, 1, 1, 1, 1, 0, 0},
                               {2, 1, 1, 1, 1, 1, 1, 1, 0},
                               {0, 3, 3, 3, 3, 3, 3, 3, 3}};

    private int[][] southEastBuffer = {{2, 2, 2, 2, 2, 2, 2, 2, 0},
                               {0, 1, 1, 1, 1, 1, 1, 3, 3},
                               {0, 0, 1, 1, 1, 1, 3, 3, 0},
                               {0, 0, 0, 1, 1, 3, 3, 0, 0},
                               {0, 0, 0, 0, 3, 3, 0, 0, 0}};

    int direction;

    /** Construct an ArrowIcon pointing in the given direction
     * @param direction the direction the arrow will point, this being one of the constants NORTH, SOUTH, EAST, WEST
     */        
    public ArrowIcon(int direction) {
        this.direction = direction;
    }

    /** Paints the icon. The top-left corner of the icon is drawn at the point
     * (x, y) in the coordinate space of the graphics context g. If this icon has
     * no image observer, this method uses the c component as the observer.
     *
     * @param c the component to be used as the observer if this icon has no image observer
     * @param g the graphics context
     * @param x the X coordinate of the icon's top-left corner
     * @param y the Y coordinate of the icon's top-left corner
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        int blockSize = Math.min(SIZE, ONE_TOUCH_SIZE);

        // Initialize the color array
        Color[] colors = {
                c.getBackground(),
                MetalLookAndFeel.getPrimaryControlDarkShadow(),
                MetalLookAndFeel.getPrimaryControlInfo(),
                MetalLookAndFeel.getPrimaryControlHighlight()};

        // Fill the background first ...
        g.setColor(c.getBackground());
        g.fillRect(0, 0, c.getWidth(), c.getHeight());

        // ... then draw the arrow.
        if (c instanceof ArrowButton) {
            ArrowButton button = (ArrowButton)c;
            ButtonModel model = button.getModel();

            if (model.isPressed()) {
                // Adjust color mapping for pressed button state
                colors[1] = colors[2];
            }
        }
        
        if (direction == NORTH || direction == SOUTH) {
            if (direction == NORTH) buffer=northWestBuffer;
            else buffer=southEastBuffer;
            for (int i=1; i<=buffer[0].length; i++) {
                for (int j=1; j<blockSize; j++) {
                    if (buffer[j-1][i-1] != 0) {
                        g.setColor(colors[buffer[j-1][i-1]]);
                        g.drawLine(i, j, i, j);
                    }
                }
            }
        }
        else {
            if (direction == WEST) buffer=northWestBuffer;
            else buffer=southEastBuffer;
            for (int i=1; i<=buffer[0].length; i++) {
                for (int j=1; j<blockSize; j++) {
                    if (buffer[j-1][i-1] != 0) {
                        g.setColor(colors[buffer[j-1][i-1]]);
                        g.drawLine(j, i, j, i);
                    }
                }
            }
        }
    }

    /**
     * Gets the height of the icon.
     * @return the height of the icon
     */ 
    public int getIconWidth() {
        return SIZE;
    }


    /**
     * Gets the height of the icon.
     * @return the height of the icon
     */ 
    public int getIconHeight() {
        return SIZE;
    }
}
