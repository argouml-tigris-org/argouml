// $Id$
// Copyright (c) 2003-2004 The Regents of the University of California. All
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
import javax.swing.UIManager;

/**
 * The base class for adding simple decorations to existing icons.
 * This should be extended for each decoration style.
 * @author Bob Tarling
 */
public abstract class DecoratedIcon extends ImageIcon {
    
    /**
     * If the icon is for hoovering: <code>ROLLOVER</code>
     */
    public static final int ROLLOVER = 0;
    
    /**
     * If the icon is the normally shown one: <code>STANDARD</code>
     */
    public static final int STANDARD = 1;

    /**
     * This is the sprite buffer for the arrow image of the left button.
     */
    private int[][] imageBuffer;

    private int popupIconWidth = 11;
    private int popupIconHeight = 16;
    private int popupIconOffset = 5;

    private ImageIcon imageIcon;
    
    /** Construct a decorated icon made up of the given icon and decorated with
     * the icon defined in the descendant class.
     * @param theImageIcon The icon to decorate
     */        
    DecoratedIcon(ImageIcon theImageIcon) {
        imageIcon = theImageIcon;
    }
    
    /**
     * Initialise the icon.
     * @param buffer the buffer containing the icon definition (pixels)
     */
    protected void init(int[][] buffer) {
        imageBuffer = buffer;
        popupIconWidth = imageBuffer[0].length;
        popupIconHeight = imageBuffer.length;
        BufferedImage mergedImage =
	    new BufferedImage(imageIcon.getIconWidth()
			      + popupIconOffset + popupIconWidth,
			      imageIcon.getIconHeight(),
			      BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = mergedImage.createGraphics();
        g2.drawImage(imageIcon.getImage(), null, null);
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

        int xOffset = x + imageIcon.getIconWidth() + popupIconOffset;
        // Initialize the color array
        Color[] colors = {
                c.getBackground(),
                UIManager.getColor("controlDkShadow"),
                UIManager.getColor("infoText"),
                UIManager.getColor("controlHighlight")};

        for (int i = 0; i < popupIconWidth; i++) {
            for (int j = 0; j < popupIconHeight; j++) {
                if (imageBuffer[j][i] != 0) {
                    g.setColor(colors[imageBuffer[j][i]]);
                    g.drawLine(xOffset + i, y + j, xOffset + i, y + j);
                }
            }
        }
    }
}
