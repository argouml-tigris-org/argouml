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
 * DecoratedIcon.java
 *
 * Created on 25 February 2003, 20:47
 */

package org.argouml.swingext;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * The base class for adding simple decorations to existing icons.
 * This should be extended for each decoration style.
 * @author Bob Tarling
 */
public abstract class DecoratedIcon extends ImageIcon {
    
    public static final int ROLLOVER = 0;
    public static final int STANDARD = 1;

    // Sprite buffer for the arrow image of the left button
    protected int[][] _buffer;

    protected int _popupIconWidth = 11;
    protected int _popupIconHeight = 16;
    private int _popupIconOffset = 5;

    private ImageIcon _imageIcon;
    
    /** Construct a decorated icon made up of the given icon and decorated with
     * the icon defined in the descendant class.
     * @param The icon to decorate
     */        
    DecoratedIcon(ImageIcon imageIcon) {
        _imageIcon = imageIcon;
    }
    
    protected void init(int[][] buffer) {
        _buffer = buffer;
        _popupIconWidth = _buffer[0].length;
        _popupIconHeight = _buffer.length;
        BufferedImage mergedImage =
	    new BufferedImage(_imageIcon.getIconWidth()
			      + _popupIconOffset + _popupIconWidth,
			      _imageIcon.getIconHeight(),
			      BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = mergedImage.createGraphics();
        g2.drawImage(_imageIcon.getImage(), null, null);
        setImage(mergedImage);
    }

    /** Paints the icon. The top-left corner of the icon is drawn at
     * the point (x, y) in the coordinate space of the graphics
     * context g. If this icon has no image observer, this method uses
     * the c component as the observer.
     *
     * @param c the component to be used as the observer if this icon
     * has no image observer
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

        for (int i = 0; i < _popupIconWidth; i++) {
            for (int j = 0; j < _popupIconHeight; j++) {
                if (_buffer[j][i] != 0) {
                    g.setColor(colors[_buffer[j][i]]);
                    g.drawLine(xOffset + i, y + j, xOffset + i, y + j);
                }
            }
        }
    }
}
