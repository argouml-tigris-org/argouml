/*
 * Splitter.java
 */
package org.argouml.swingext;

import java.io.Serializable;

import java.util.Vector;
import java.util.Enumeration;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.border.Border;

/**
 * Acts as a seperator between components which will automatically resize those components when
 * the splitter is moved.. 
 */
public class Splitter extends JComponent {

    final static public int HORIZONTAL_SPLIT = Orientation.HORIZONTAL;
    final static public int VERTICAL_SPLIT = Orientation.VERTICAL;

    final static public int NONE = -1;
    final static public int WEST = 0;
    final static public int EAST = 1;
    final static public int NORTH = 0;
    final static public int SOUTH = 1;

    private Orientation orientation;

    private int lastPosition;
    private int lastLength;
    private int quickHide = NONE;
    private boolean panelHidden = false;

    private boolean dynamicResize = true;

    private Component sideComponent[] = new Component[2];

    private static final int ONE_TOUCH_SIZE = 6;
    private static final int ONE_TOUCH_OFFSET = 2;

    private Color controlColor = MetalLookAndFeel.getControl();
    private Color primaryControlColor = MetalLookAndFeel.getPrimaryControl();

    private int inset = 2;
    private MetalBumps bumps = new MetalBumps(10, 10,
                                MetalLookAndFeel.getControlHighlight(),
                                MetalLookAndFeel.getControlDarkShadow(),
                                MetalLookAndFeel.getControl() );

    private MetalBumps focusBumps = new MetalBumps(10, 10,
                                MetalLookAndFeel.getPrimaryControlHighlight(),
                                MetalLookAndFeel.getPrimaryControlDarkShadow(),
                                MetalLookAndFeel.getPrimaryControl() );

    private int splitterSize = 10;

    ArrowButton buttonNorth = null;
    ArrowButton buttonSouth = null;

    public Splitter(int orientation) {
        this(Orientation.getOrientation(orientation));
    }

    public Splitter(Orientation orientation) {
        this.orientation = orientation;

        if (orientation.isVertical()) setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
        else                          setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));

        setLayout(new SerialLayout(orientation.getPerpendicular()));//, SerialLayout.EAST, SerialLayout.RIGHTTOLEFT));
        setSize(splitterSize, splitterSize);
        setPreferredSize(this.getSize());

        MyMouseListener myMouseListener = new MyMouseListener();
        addMouseListener(myMouseListener);
        addMouseMotionListener(myMouseListener);
    }

    public void registerComponent(int side, Component comp)
    {
      this.sideComponent[side] = comp;
      setVisible(this.sideComponent[WEST] != null && this.sideComponent[EAST] != null);
    }

    public Component getRegisteredComponent(int side)
    {
      return sideComponent[side];
    }

    public void setQuickHide(int side) {
        quickHide = side;
        if (buttonNorth == null) {
            if (orientation.isVertical()) {
                buttonNorth = new ArrowButton(ArrowButton.NORTH);
                buttonSouth = new ArrowButton(ArrowButton.SOUTH);
            }
            else {
                buttonNorth = new ArrowButton(ArrowButton.WEST);
                buttonSouth = new ArrowButton(ArrowButton.EAST);
            }
            ActionListener al = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    toggleHide();
                }
            };
            buttonNorth.addActionListener(al);
            buttonSouth.addActionListener(al);
            add(buttonNorth);
            add(buttonSouth);
        }
        showButton();
    }

    private void showButton() {
        if (panelHidden) {
            buttonNorth.setVisible(quickHide != this.NORTH);
            buttonSouth.setVisible(quickHide != this.SOUTH);
        }
        else {
            buttonNorth.setVisible(quickHide == this.NORTH);
            buttonSouth.setVisible(quickHide == this.SOUTH);
        }
    }

    public void paint(Graphics g) {
	MetalBumps usedBumps;
	if (hasFocus()) {
	    usedBumps = focusBumps;
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

    // Get either x or y depending on orientation
    private int getPosition() {
        return orientation.getPosition(this);
    }

    public void toggleHide()
    {
        int position = 0;

        if (panelHidden) {
            position = lastPosition;
            if (quickHide == EAST) {
                position = getPosition() - lastLength;
            }
        }
        else {
            if (quickHide == EAST) position = getPosition() + orientation.getLength(sideComponent[EAST]);
        }

        lastLength = orientation.getLength(sideComponent[quickHide]);
        lastPosition = getPosition();

        if (orientation.isVertical()) {
            setLocation(getX(), position);
        }
        else {
            setLocation(position, getY());
        }

        int movement = position - lastPosition;

        if (orientation.isVertical()) {
            sideComponent[NORTH].setSize(sideComponent[NORTH].getWidth(), sideComponent[NORTH].getHeight() + movement);
            sideComponent[SOUTH].setSize(sideComponent[SOUTH].getWidth(), sideComponent[SOUTH].getHeight() - movement);
            sideComponent[SOUTH].setLocation(sideComponent[SOUTH].getX(), sideComponent[SOUTH].getY() + movement);
        }
        else {
            sideComponent[WEST].setSize(sideComponent[WEST].getWidth() + movement, sideComponent[WEST].getHeight());
            sideComponent[EAST].setSize(sideComponent[EAST].getWidth() - movement, sideComponent[EAST].getHeight());
            sideComponent[EAST].setLocation(sideComponent[EAST].getX() + movement, sideComponent[EAST].getY());
        }
        sideComponent[WEST].validate();
        sideComponent[EAST].validate();

        panelHidden = !panelHidden;
        showButton();
    }

    private class MyMouseListener implements MouseMotionListener, MouseListener {
        private int mousePositionOnSplitterWhenPressed;
        private int positionOfSplitterWhenPressed;

        public void mousePressed(MouseEvent me)
        {
            mousePositionOnSplitterWhenPressed = orientation.getPosition(me);
            positionOfSplitterWhenPressed = getPosition();
        }

        public void mouseReleased(MouseEvent me)
        {
            if (!dynamicResize && sideComponent[WEST] != null && sideComponent[EAST] != null) {
                int movement = orientation.getPosition(me) - mousePositionOnSplitterWhenPressed;

                if (movement >= 0) {
                    movement = restrictMovement(sideComponent[WEST], sideComponent[EAST], movement, -1);
                }
                else {
                    movement = restrictMovement(sideComponent[EAST], sideComponent[WEST], movement, 1);
                }

                int position = getPosition() + movement;

                if (orientation.isVertical()) setLocation(getX(), position);
                else                          setLocation(position, getY());

                movement = position - positionOfSplitterWhenPressed;

                if (orientation.isVertical()) {
                    sideComponent[NORTH].setSize(sideComponent[NORTH].getWidth(), sideComponent[NORTH].getHeight() + movement);
                    sideComponent[SOUTH].setSize(sideComponent[SOUTH].getWidth(), sideComponent[SOUTH].getHeight() - movement);
                    sideComponent[SOUTH].setLocation(sideComponent[SOUTH].getX(), sideComponent[SOUTH].getY() + movement);
                }
                else {
                    sideComponent[WEST].setSize(sideComponent[WEST].getWidth() + movement, sideComponent[WEST].getHeight());
                    sideComponent[EAST].setSize(sideComponent[EAST].getWidth() - movement, sideComponent[EAST].getHeight());
                    sideComponent[EAST].setLocation(sideComponent[EAST].getX() + movement, sideComponent[EAST].getY());
                }
                sideComponent[WEST].validate();
                sideComponent[EAST].validate();
            }
        }

        public void mouseClicked(MouseEvent me)
        {
            if (me.getClickCount() == 2 && quickHide != NONE && sideComponent[WEST] != null && sideComponent[EAST] != null) {
                toggleHide();
            }
        }

        public void mouseDragged(MouseEvent me)
        {
            if (sideComponent[WEST] != null && sideComponent[EAST] != null) {
                int movement = orientation.getPosition(me) - mousePositionOnSplitterWhenPressed;

                if (movement >= 0) {
                    movement = restrictMovement(sideComponent[WEST], sideComponent[EAST], movement, -1);
                }
                else {
                    movement = restrictMovement(sideComponent[EAST], sideComponent[WEST], movement, 1);
                }

                int position = getPosition() + movement;

                if (orientation.isVertical()) {
                    setLocation(getX(), position);
                }
                else {
                    setLocation(position, getY());
                }

                if (dynamicResize) {
                    if (orientation.isVertical()) {
                        sideComponent[NORTH].setSize(sideComponent[NORTH].getWidth(), sideComponent[NORTH].getHeight() + movement);
                        sideComponent[SOUTH].setSize(sideComponent[SOUTH].getWidth(), sideComponent[SOUTH].getHeight() - movement);
                        sideComponent[SOUTH].setLocation(sideComponent[SOUTH].getX(), sideComponent[SOUTH].getY() + movement);
                    }
                    else {
                        sideComponent[WEST].setSize(sideComponent[WEST].getWidth() + movement, sideComponent[WEST].getHeight());
                        sideComponent[EAST].setSize(sideComponent[EAST].getWidth() - movement, sideComponent[EAST].getHeight());
                        sideComponent[EAST].setLocation(sideComponent[EAST].getX() + movement, sideComponent[EAST].getY());
                    }
                    sideComponent[WEST].validate();
                    sideComponent[EAST].validate();
                }
                panelHidden = false;
                showButton();
            }
        }

        // Restrict movement depending on max or min size of components
        private int restrictMovement(Component growingComponent, Component shrinkingComponent, int movement, int sign) {

            Dimension maxSize = growingComponent.getMaximumSize();
            int maxLength = orientation.getLength(maxSize);
            int currentLength = orientation.getLength(growingComponent);

            if (currentLength + movement*sign > maxLength) {
                movement = (currentLength - maxLength) * sign;
            }

            Dimension minSize = shrinkingComponent.getMinimumSize();
            int minLength = orientation.getLength(minSize);
            currentLength = orientation.getLength(shrinkingComponent);

            if (currentLength + movement*sign < minLength) {
                movement = (minLength - currentLength) * sign;
            }

            return movement;
        }

        public void mouseEntered(MouseEvent me)
        {
        }

        public void mouseExited(MouseEvent me)
        {
        }

        public void mouseMoved(MouseEvent me)
        {
        }
    }

    private class ArrowButton extends javax.swing.JButton {

        private static final int SIZE = 10;
        private static final int ONE_TOUCH_SIZE = 6;
        private static final int ONE_TOUCH_OFFSET = 2;

        public ArrowButton(int direction) {
            super();
            setIcon(new ArrowIcon(direction));
            super.setFocusPainted(false);
            setPreferredSize(new Dimension(SIZE, SIZE));
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        public void setBorder(Border b) {
        }

        public void setFocusPainted(boolean b) {
        }

        // Don't want the button to participate in focus traversable.
        public boolean isFocusTraversable() {
            return false;
        }

        private class ArrowIcon implements Icon, Serializable, SwingConstants {

            // Sprite buffer for the arrow image of the left button
            int[][] buffer;

            int[][] northWestBuffer = {{0, 0, 0, 2, 2, 0, 0, 0, 0},
                                       {0, 0, 2, 1, 1, 1, 0, 0, 0},
                                       {0, 2, 1, 1, 1, 1, 1, 0, 0},
                                       {2, 1, 1, 1, 1, 1, 1, 1, 0},
                                       {0, 3, 3, 3, 3, 3, 3, 3, 3}};

            int[][] southEastBuffer = {{2, 2, 2, 2, 2, 2, 2, 2, 0},
                                       {0, 1, 1, 1, 1, 1, 1, 3, 3},
                                       {0, 0, 1, 1, 1, 1, 3, 3, 0},
                                       {0, 0, 0, 1, 1, 3, 3, 0, 0},
                                       {0, 0, 0, 0, 3, 3, 0, 0, 0}};

            int direction;

            public ArrowIcon(int direction) {
                this.direction = direction;
            }

            protected int getControlSize() { return 0; }

            public void paintIcon(Component c, Graphics g, int x, int y) {
                int blockSize = Math.min(SIZE, ONE_TOUCH_SIZE);

                ArrowButton button = (ArrowButton)c;
                ButtonModel model = button.getModel();

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
                if (model.isPressed()) {
                    // Adjust color mapping for pressed button state
                    colors[1] = colors[2];
                }
                if (this.direction == this.NORTH || this.direction == this.SOUTH) {
                    if (this.direction == this.NORTH) buffer=northWestBuffer;
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
                    if (this.direction == this.WEST) buffer=northWestBuffer;
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

            public int getIconWidth() {
                return getControlSize();
            }

            public int getIconHeight() {
                return getControlSize();
            }
        }
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

    public MetalBumps( Dimension bumpArea ) {
        this( bumpArea.width, bumpArea.height );
    }

    public MetalBumps( int width, int height ) {
        setBumpArea( width, height );
        buffer = getBuffer( topColor, shadowColor, backColor );
        if ( buffer == null ) {
            createBuffer();
        }
    }

    public MetalBumps(int width, int height, Color newTopColor, Color newShadowColor, Color newBackColor ) {
        setBumpArea( width, height );
        setBumpColors( newTopColor, newShadowColor, newBackColor );
        buffer = getBuffer( topColor, shadowColor, backColor );
        if ( buffer == null ) {
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

    public void setBumpArea( int width, int height ) {
        xBumps = width / 2;
        yBumps = height / 2;
    }

    public void setBumpColors( Color newTopColor, Color newShadowColor, Color newBackColor ) {
        topColor = newTopColor;
        shadowColor = newShadowColor;
        backColor = newBackColor;
        buffer = getBuffer( topColor, shadowColor, backColor );
        if ( buffer == null ) {
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
            frame = new Frame( "bufferCreator" );
        }

        if (component == null ) {
            component = new Canvas();
            frame.add( component, BorderLayout.CENTER );
        }
        // fix for 4185993 (moved this outside if block)
        frame.addNotify();
    }
}
