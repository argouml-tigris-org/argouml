/*
 * MetalBumpComponent.java
 */
package org.argouml.swingext;

import java.util.Vector;
import java.util.Enumeration;

import javax.swing.*;

import java.awt.*;

import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * A swing component with a background of metal look bumps
 *
 * @author Bob Tarling
 */
// TODO - This class needs a major re-write. Should be a simple subclass of Image.
class MetalBumpComponent extends JComponent {

    private Color controlColor = MetalLookAndFeel.getControl();
    private Color primaryControlColor = MetalLookAndFeel.getPrimaryControl();

    private int inset = 0;
    private MetalBumps bumps = new MetalBumps(10, 10,
                                MetalLookAndFeel.getControlHighlight(),
                                MetalLookAndFeel.getControlDarkShadow(),
                                MetalLookAndFeel.getControl() );

    private MetalBumps highlightBumps = new MetalBumps(10, 10,
                                MetalLookAndFeel.getPrimaryControlHighlight(),
                                MetalLookAndFeel.getPrimaryControlDarkShadow(),
                                MetalLookAndFeel.getPrimaryControlShadow() );
    
    

    boolean highlight;
    
    public MetalBumpComponent() {
        super();
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }
    
    public void paint(Graphics g) {
	MetalBumps usedBumps;
        // TODO Is highlight ever set?
	if (highlight) {
	    usedBumps = highlightBumps;
	    g.setColor(primaryControlColor);
	}
	else {
	    usedBumps = bumps;
	    g.setColor(controlColor);
	}
	Rectangle clip = g.getClipBounds();
	Insets insets = getInsets();
	g.fillRect(clip.x, clip.y, clip.width, clip.height);
        Dimension  size = getSize();
        size.width -= inset * 2;
        size.height -= inset * 2;
	int drawX = inset;
	int drawY = inset;
	if (insets != null) {
	    size.width -= (insets.left + insets.right);
	    size.height -= (insets.top + insets.bottom);
	    drawX += insets.left;
	    drawY += insets.top;
	}
        usedBumps.setBumpArea(size);
        usedBumps.paintIcon(this, g, drawX, drawY);
        super.paint(g);
    }
}
    
class MetalBumps implements Icon {

    protected int xBumps;
    protected int yBumps;
    protected Color topColor = MetalLookAndFeel.getPrimaryControlHighlight();
    protected Color shadowColor = MetalLookAndFeel.getPrimaryControlDarkShadow();
    protected Color backColor = MetalLookAndFeel.getPrimaryControlShadow();

    protected static Vector buffers = new Vector();
    protected BumpBuffer buffer;

    public MetalBumps(Dimension bumpArea) {
        this(bumpArea.width, bumpArea.height );
    }

    public MetalBumps(int width, int height) {
        setBumpArea(width, height);
        buffer = getBuffer(topColor, shadowColor, backColor);
        if (buffer == null) {
            createBuffer();
        }
    }

    public MetalBumps(int width, int height, Color newTopColor, Color newShadowColor, Color newBackColor ) {
        setBumpArea(width, height);
        setBumpColors(newTopColor, newShadowColor, newBackColor );
        buffer = getBuffer( topColor, shadowColor, backColor );
        if (buffer == null) {
            createBuffer();
        }
    }

    protected void createBuffer() {
        buffer = new BumpBuffer( topColor, shadowColor, backColor );
        buffers.addElement( buffer );
    }

    protected BumpBuffer getBuffer( Color aTopColor, Color aShadowColor, Color aBackColor ) {
        BumpBuffer result = null;

        Enumeration elements = buffers.elements();

        while ( elements.hasMoreElements() ) {
            BumpBuffer aBuffer = (BumpBuffer)elements.nextElement();
            if ( aBuffer.hasSameColors( aTopColor, aShadowColor, aBackColor ) ) {
                result = aBuffer;
                break;
            }
        }

        return result;
    }

    public void setBumpArea( Dimension bumpArea ) {
        setBumpArea( bumpArea.width, bumpArea.height );
    }

    public void setBumpArea(int width, int height) {
        xBumps = width / 2;
        yBumps = height / 2;
    }

    public void setBumpColors( Color newTopColor, Color newShadowColor, Color newBackColor ) {
        topColor = newTopColor;
        shadowColor = newShadowColor;
        backColor = newBackColor;
        buffer = getBuffer( topColor, shadowColor, backColor );
        if (buffer == null) {
            createBuffer();
        }
    }

    public void paintIcon( Component c, Graphics g, int x, int y ) {
        int bufferWidth = buffer.getImageSize().width;
        int bufferHeight = buffer.getImageSize().height;
        int iconWidth = getIconWidth();
        int iconHeight = getIconHeight();

        int x2 = x + iconWidth;
        int y2 = y + iconHeight;

        int savex = x;
        while (y < y2) {
            int h = Math.min(y2 - y, bufferHeight);
            for (x = savex; x < x2; x += bufferWidth) {
                int w = Math.min(x2 - x, bufferWidth);
                g.drawImage(buffer.getImage(),
                            x, y, x+w, y+h,
                            0, 0, w, h,
                            null);
            }
            y += bufferHeight;
        }
    }

    public int getIconWidth() {
        return xBumps * 2;
    }

    public int getIconHeight() {
        return yBumps * 2;
    }
}

class BumpBuffer {

    static Frame frame;
    static Component component;

    static final int IMAGE_SIZE = 64;
    static Dimension imageSize = new Dimension( IMAGE_SIZE, IMAGE_SIZE );

    transient Image image;
    Color topColor;
    Color shadowColor;
    Color backColor;

    public BumpBuffer( Color aTopColor, Color aShadowColor, Color aBackColor ) {
        createComponent();
        image = getComponent().createImage( IMAGE_SIZE, IMAGE_SIZE );
        topColor = aTopColor;
        shadowColor = aShadowColor;
        backColor = aBackColor;
        fillBumpBuffer();
    }

    public boolean hasSameColors( Color aTopColor, Color aShadowColor, Color aBackColor ) {
        return topColor.equals(aTopColor) && shadowColor.equals( aShadowColor ) && backColor.equals( aBackColor );
    }

    public Image getImage() {
        if (image == null) {
            image = getComponent().createImage( IMAGE_SIZE, IMAGE_SIZE );
            fillBumpBuffer();
        }
        return image;
    }

    public Dimension getImageSize() {
        return imageSize;
    }

    protected void fillBumpBuffer() {
        Graphics g = image.getGraphics();

        g.setColor( backColor );
        g.fillRect( 0, 0, IMAGE_SIZE, IMAGE_SIZE );

        g.setColor(topColor);
        for (int x = 0; x < IMAGE_SIZE; x+=4) {
            for (int y = 0; y < IMAGE_SIZE; y+=4) {
                g.drawLine( x, y, x, y );
                g.drawLine( x+2, y+2, x+2, y+2);
            }
        }

        g.setColor(shadowColor);
        for (int x = 0; x < IMAGE_SIZE; x+=4) {
            for (int y = 0; y < IMAGE_SIZE; y+=4) {
                g.drawLine( x+1, y+1, x+1, y+1 );
                g.drawLine( x+3, y+3, x+3, y+3);
            }
        }
        g.dispose();
    }

    protected Component getComponent() {return component;}

    protected void createComponent() {
        if (frame == null) {
            frame = new Frame("bufferCreator");
        }

        if (component == null ) {
            component = new Canvas();
            frame.add( component, BorderLayout.CENTER );
        }
        frame.addNotify();
    }
}
