/*
 * ArrowButton.java
 */
package org.argouml.swingext;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * A metal look and feel arrow button that can be created to point to a compass point.
 *
 * @author Bob Tarling
 */
public class ArrowButton extends javax.swing.JButton {

    /** Construct an ArrowButton pointing in the given direction
     * @param direction the direction the arrow will point, this being one of the constants NORTH, SOUTH, EAST, WEST
     */    
    public ArrowButton(int direction, Border border) {
        this(direction);
        setBorder(border);
    }

    /** Construct an ArrowButton pointing in the given direction
     * @param direction the direction the arrow will point, this being one of the constants NORTH, SOUTH, EAST, WEST
     */    
    public ArrowButton(int direction) {
        super();
        ArrowIcon arrowIcon = new ArrowIcon(direction);
        setIcon(arrowIcon);
        super.setFocusPainted(false);
        setPreferredSize(new Dimension(arrowIcon.getIconWidth(), arrowIcon.getIconHeight()));
        setMinimumSize(new Dimension(arrowIcon.getIconWidth(), arrowIcon.getIconHeight()));
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(getIcon().getIconWidth(), getIcon().getIconHeight());
    }
}
