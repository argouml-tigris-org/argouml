/*
 * DecoratedIcon.java
 *
 * Created on 25 February 2003, 20:47
 */

package org.argouml.swingext;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 *
 * @author  Administrator
 */
abstract public class DecoratedIcon extends ImageIcon {
    
    public static final int ROLLOVER = 0;
    public static final int STANDARD = 1;

    // Sprite buffer for the arrow image of the left button
    protected int[][] _buffer;

    protected int _popupIconWidth = 11;
    protected int _popupIconHeight = 16;
    private int _popupIconOffset = 5;

    private ImageIcon _imageIcon;

    private boolean _showSplitter;
    
    /** Construct an dropdown icon pointing in the given direction
     * @param direction the direction the arrow will point, this being one of the constants NORTH, SOUTH, EAST, WEST
     */        
    public DecoratedIcon(ImageIcon imageIcon) {
        _imageIcon = imageIcon;
    }
    
    public DecoratedIcon(ImageIcon imageIcon, boolean showSplitter) {
        _imageIcon = imageIcon;
        _showSplitter = showSplitter;
    }
    
    protected void init(int[][] buffer) {
        _buffer = buffer;
        _popupIconWidth = _buffer[0].length;
        _popupIconHeight = _buffer.length;
        BufferedImage mergedImage = new BufferedImage(_imageIcon.getIconWidth() + _popupIconOffset + _popupIconWidth, _imageIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = mergedImage.createGraphics();
        g2.drawImage(_imageIcon.getImage(), null, null);
        setImage(mergedImage);
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
        super.paintIcon(c, g, x, y);

        int xOffset = x + _imageIcon.getIconWidth() + _popupIconOffset;
        // Initialize the color array
        Color[] colors = {
                c.getBackground(),
                MetalLookAndFeel.getPrimaryControlDarkShadow(),
                MetalLookAndFeel.getPrimaryControlInfo(),
                MetalLookAndFeel.getPrimaryControlHighlight()};

        if (_showSplitter) {
            showSplitter(colors[1], g, x + _imageIcon.getIconWidth() + 2, 0, c.getHeight());
            showSplitter(colors[3], g, x + _imageIcon.getIconWidth() + 3, 0, c.getHeight());
        }
                
        if (c instanceof PopupToolBoxButton) {
            PopupToolBoxButton popupToolBoxButton = (PopupToolBoxButton)c;
            popupToolBoxButton.setDivision(_imageIcon.getIconWidth() + 3);
        }
                
        for (int i=0; i < _popupIconWidth; i++) {
            for (int j=0; j < _popupIconHeight; j++) {
                if (_buffer[j][i] != 0) {
                    g.setColor(colors[_buffer[j][i]]);
                    g.drawLine(xOffset + i, y + j, xOffset + i, y + j);
                }
            }
        }
    }
    
    public void showSplitter(Color c, Graphics g, int x, int y, int height) {
        g.setColor(c);
        g.drawLine(x, y + 0, x, y + height);
    }
}
