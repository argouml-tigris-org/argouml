/*
 * Splitter.java
 */
package org.argouml.swingext;

import java.awt.*;
import java.awt.event.*;

/**
 * Acts as a seperator between components and will automatically resize those components
 * when the splitter is moved by dragging the mouse across it.
 *
 * @author Bob Tarling
 *
 * @todo Bring splitter to top when not dynamic resize
 * @todo Add constructor and getter/setter for dynamic resize
 * @todo Implement plafs other than metal plaf
 * @todo Implement the setLocation method, anything currently calling setLocation
 * should then call super.setLocation.
 */
public class Splitter extends MetalBumpComponent {

    final static public Orientation HORIZONTAL_SPLIT = Horizontal.getInstance();
    final static public Orientation VERTICAL_SPLIT = Vertical.getInstance();

    final static public int NONE = -1;
    final static public int WEST = 0;
    final static public int EAST = 1;
    final static public int NORTH = 0;
    final static public int SOUTH = 1;

    /**
     * The orientation of this splitter. Orientation does not represent the shape of the
     * splitter but rather the layout of the objects being seperated by the splitter.
     * In other words a horizontal splitter seperates components layed out in a horizontal
     * row.
     */
    private Orientation orientation;

    private int lastPosition;
    private int lastLength;
    
    /**
     * Is quick hide available and if so which component ahould it hide
     */
    private int quickHide = NONE;
    
    /**
     * True if a component has been hidden by using the quick hide process of the Splitter
     */ 
    private boolean panelHidden = false;

    /**
     * True if components are resized dymically when the plitter is dragged. If false
     * then components are only resized when the splitter is dropped by releasing the 
     * mouse.
     */ 
    private boolean dynamicResize = true;

    /**
     * The 2 components which the splitter is designed to seperate
     */
    private Component sideComponent[] = new Component[2];

    /**
     * The standard width of a splitter
     */
    private int splitterSize = 10;

    /**
     * The quick hide buttons
     */
    ArrowButton buttonNorth = null;
    ArrowButton buttonSouth = null;

    /**
     * The constructor
     *
     * @parameter orientation A Horizontal or Vertical object to indicate whether this
     *                        splitter is designed to seperate components laid out
     *                        horizontally or vertically.
     */ 
    public Splitter(Orientation orientation) {
        super();
        
        this.orientation = orientation;

        setCursor(orientation.getCursor());
        
        setLayout(new SerialLayout(orientation.getPerpendicular()));
        setSize(splitterSize, splitterSize);
        setPreferredSize(this.getSize());

        MyMouseListener myMouseListener = new MyMouseListener();
        addMouseListener(myMouseListener);
        addMouseMotionListener(myMouseListener);
    }

    /**
     * Register a component to be resized by this splitter.
     *
     * @parameter side the side of the splitter to place the component being one of the
     *                 constants NORTH, SOUTH, EAST or WEST
     */
    public void registerComponent(int side, Component comp)
    {
        this.sideComponent[side] = comp;
        setVisible(this.sideComponent[WEST] != null && this.sideComponent[EAST] != null);
    }

    /**
     * Get a registered component.
     *
     * @parameter side the side of the splitter of the component to return, being one of the
     *                 constants NORTH, SOUTH, EAST or WEST
     * @return the registered component
     */
    public Component getRegisteredComponent(int side)
    {
        return sideComponent[side];
    }

    /**
     * Change the quick hide action. If quick hide is turned on then an arrow button
     * appears on the splitter to allow the user to instantly reposition the splitter to
     * hide one of the components.
     *
     * @parameter side the side of the splitter of the component to be hidden on a quick
     *                 hide action. This being one of the constants NORTH, SOUTH, EAST, 
     *                 WEST or NONE.
     */
    public void setQuickHide(int side) {
        quickHide = side;
        // Only create the arrow buttons the first time they are required.
        if (side != NONE && buttonNorth == null) {
            buttonNorth = orientation.getStartArrowButton();
            buttonSouth = orientation.getEndArrowButton();
            
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
        showButtons();
    }

    /*
     * Show the correct button symbol. The arrow button should point in the direction that
     * the splitter will move on the button press. This will be towards the component to
     * hide or if already hidden it should point away from the hidden component.
     */
    private void showButtons() {
        if (buttonNorth != null) {
            if (panelHidden) {
                buttonNorth.setVisible(quickHide == this.SOUTH);
                buttonSouth.setVisible(quickHide == this.NORTH);
            }
            else {
                buttonNorth.setVisible(quickHide == this.NORTH);
                buttonSouth.setVisible(quickHide == this.SOUTH);
            }
        }
    }

    /*
     * Hide or restore the component currently selected as the quick hide component.
     */
    public void toggleHide()
    {
        if (quickHide == NONE) return;
        
        int position = 0;

        if (panelHidden) {
            position = lastPosition;
            if (quickHide == EAST) {
                position = orientation.getPosition(this) - lastLength;
            }
        }
        else if (quickHide == EAST) {
            position = orientation.getPosition(this) + orientation.getLength(sideComponent[EAST]);
        }

        lastLength = orientation.getLength(sideComponent[quickHide]);
        lastPosition = orientation.getPosition(this);

        setLocation(orientation.setPosition(getLocation(), position));

        resizeComponents(position - lastPosition);

        panelHidden = !panelHidden;
        showButtons();
    }

    /*
     * Attempt to move the splitter by a given amount. It may not be possible to move
     * the splitter as far as requested because it may result in one of the components
     * having a negative size or breaking it min/max size.
     *
     * @parameter movement the distance in pixels to move the splitter from its current
     *                     position.
     *
     * @return the actual number of pixels the splitter was moved.
     */
    private int moveSplitter(int movement)
    {
        if (sideComponent[WEST] != null && sideComponent[EAST] != null) {

            int restrictedMovement = 0;
            
            if (movement >= 0) {
                restrictedMovement = restrictMovement(sideComponent[WEST], sideComponent[EAST], movement, -1);
            }
            else {
                restrictedMovement = restrictMovement(sideComponent[EAST], sideComponent[WEST], movement, 1);
            }

            setLocation(orientation.addToPosition(getLocation(), restrictedMovement));

            return restrictedMovement;
        }
        return 0;
    }

    /**
     * Resize and reposition the components according to the movement of the splitter
     *
     * @parameter movement the distance the splitter has moved.
     */
    private void resizeComponents(int movement) {
        sideComponent[NORTH].setSize(orientation.addLength(sideComponent[NORTH].getSize(), movement));
        sideComponent[SOUTH].setSize(orientation.subtractLength(sideComponent[SOUTH].getSize(), movement));
        sideComponent[SOUTH].setLocation(orientation.addToPosition(sideComponent[SOUTH].getLocation(), movement));
        sideComponent[NORTH].validate();
        sideComponent[SOUTH].validate();
    }
    
    /**
     * calculates any restriction of movement based on the min/max values of the
     * registered components.
     *
     * @parameter growingComponent   The component that is expanding as the result of a
     *                               splitter move.
     * @parameter shrinkingComponent The component that is shrinking as the result of a
     *                               splitter move.
     * @parameter movement           The number of pixels of the attempted move
     * @parameter sign               The direction of the move -ve or +ve (-1 or +1)
     */
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

    /**
     * The mouse listener to detect mouse interaction with this splitter
     */
    private class MyMouseListener implements MouseMotionListener, MouseListener {
        /**
         * When the mouse is pressed the splitter position is recorded so that the
         * the difference in position can be calculated when the mouse is released.
         */
        private int positionOfSplitterWhenPressed;
        
        /**
         * A value is recorded here when the mouse is pressed on the splitter. This allows
         * the position of the mouse on the splitter to remain consistent when the splitter
         * is moved.
         */
        private int mousePositionOnSplitterWhenPressed;

        /**
         * On a mouse release make sure that components are repositioned.
         */
        public void mouseReleased(MouseEvent me)
        {
            if (!dynamicResize) {
                moveSplitter(orientation.getPosition(me) - mousePositionOnSplitterWhenPressed);
                resizeComponents(orientation.getPosition(getLocation()) - positionOfSplitterWhenPressed);
            }
            mousePositionOnSplitterWhenPressed = 0;
            positionOfSplitterWhenPressed = 0;
        }

        /**
         * On a mouse drag attempt to reposition splitter.
         */
        public void mouseDragged(MouseEvent me)
        {
            int mouseMovement = orientation.getPosition(me) - mousePositionOnSplitterWhenPressed;
            int restrictedMovement = moveSplitter(mouseMovement);
            if (restrictedMovement == 0) return;
            
            if (dynamicResize) {
                resizeComponents(restrictedMovement);
            }
            if (panelHidden) {
                panelHidden = false;
                showButtons();
            }
        }

        /**
         * On a mouse press record the position of the splitter and the position of the
         * mouse on the splitter.
         *
         */
        public void mousePressed(MouseEvent me)
        {
            mousePositionOnSplitterWhenPressed = orientation.getPosition(me);
            positionOfSplitterWhenPressed = orientation.getPosition(getLocation());
        }

        /**
         * On a double click either hide or show the component selected for quick hide.
         *
         */
        public void mouseClicked(MouseEvent me)
        {
            if (me.getClickCount() == 2 && sideComponent[WEST] != null && sideComponent[EAST] != null) {
                toggleHide();
            }
        }

        /**
         * Empty method to satisy interface only, there is
         * no action when mouse enters splitter area
         */
        public void mouseEntered(MouseEvent me)
        {
        }

        /**
         * Empty method to satisy interface only, there is
         * no action when mouse leaves splitter area
         */
        public void mouseExited(MouseEvent me)
        {
        }

        /**
         * Empty method to satisy interface only, there is
         * no action when mouse moves through splitter area
         */
        public void mouseMoved(MouseEvent me)
        {
        }
    }
}
