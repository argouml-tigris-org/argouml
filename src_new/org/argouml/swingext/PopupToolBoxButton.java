/*
 * PopupToolbox.java
 *
 * Created on 15 February 2003, 20:27
 */

package org.argouml.swingext;

import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Iterator;

import java.io.Serializable;
import javax.swing.plaf.metal.MetalLookAndFeel;

/** An extension of JButton to which alternative actions can be added.
 * The button to trigger these actions become available when a
 * dropdown icon is pressed on this button.
 * @author Bob Tarling
 */
public class PopupToolBoxButton extends JButton {

    private JLabel _arrowLabel;
    private JButton _button;
    private ToolBox _toolBox;
    private DropDownIcon standardIcon;
    private DropDownIcon rolloverIcon;
    private Insets _borderInsets;
    
    /** Creates a new instance of PopupToolbox
     * @param a The default action when pressing this button
     * @param rows The number of rows of buttons to display in the popup toolbox
     * @param cols The number of columns of buttons to display in the popup toolbox
     */
    public PopupToolBoxButton(Action a, int rows, int cols) {
        super(a);
        _toolBox = new ToolBox(rows, cols);
        setAction(a);
        
        _borderInsets = _button.getBorder().getBorderInsets(this);

        setLayout(new MyLayout());

        MyMouseListener myMouseListener = new MyMouseListener();
        addMouseMotionListener(myMouseListener);
        addMouseListener(myMouseListener);
    }
    
    /** Provide a new default action for this button
     * @param a The new default action
     */    
    public void setAction(Action a) {
        // Create an invisible button to contain the new action.
        // We can use this button to find out various info for the
        // current plaf (eg preferred button size) so we can emulate
        // whatever plaf the user is set to.
        _button = new JButton(a);
        String tooltip = _button.getToolTipText();
        if (tooltip == null || tooltip.trim().length() == 0) {
            tooltip = _button.getName();
        }
        _button.setText(null);
        
        // Create a label with the same icon as the new action
        JLabel iconLabel = new JLabel(_button.getIcon());
        
        // Create a label containing a dropdown arrow
        standardIcon = new DropDownIcon(DropDownIcon.STANDARD);
        standardIcon.setIconHeight(_button.getPreferredSize().height);
        
        rolloverIcon = new DropDownIcon(DropDownIcon.ROLLOVER);
        rolloverIcon.setIconHeight(_button.getPreferredSize().height);

        _arrowLabel = new JLabel(standardIcon);
        
        // Calculate size of arrow label
        Dimension arrowSize = _arrowLabel.getPreferredSize();
        arrowSize.height = iconLabel.getPreferredSize().height;
        _arrowLabel.setPreferredSize(arrowSize);
    
        // Remove any knowledge of the action to perform from the ancestor
        // we take control of performing the action and displaying the icon.
        super.setAction(null);
        setIcon(null);
        setText(null);
        
        // Make sure the component is empty and add the 2 labels and the tooltip
        removeAll();
        add(iconLabel);
        add(_arrowLabel);
        setToolTipText(tooltip);
    }
    
    private void popup() {
        final JPopupMenu popup = new JPopupMenu();
        
        MouseAdapter m = (new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                popup.setVisible(false);
                Component c = e.getComponent();
                if (c instanceof JButton) {
                    Action a = ((JButton)c).getAction();
                    setAction(a);
                }
            }
        });
        _toolBox.setButtonMouseListener(m);
        
        _toolBox.rebuild();
        popup.add(_toolBox);
        popup.show(this, 0, getHeight());
    }

    /** Add a new action to appear as a button on the
     * popup toolbox
     * @param a The action to be added
     * @return The button generated to trigger the action
     */    
    public JButton add(Action a) {
        return _toolBox.add(a);
    }

    /**
     *  It would have been easier to put a mouse listener on the arrowlabel
     *  but this seems to introduce a swing bug and the main JButton
     *  loses its rollover border. So the mouselistener is more complex
     *  and needs to determine where oon the main button the mouse is.
     */
    private class MyMouseListener implements MouseMotionListener, MouseListener {

        public void mouseDragged(MouseEvent me) {
        }

        /**
         * If the mouse movement occurs within the PopupToolBoxButton.
         * If the mouse moves in and out of the area of the button that has the dropdown
         * then change the icon.
         */
        public void mouseMoved(MouseEvent me) {
            if (me.getX() >= _arrowLabel.getX() && me.getX() <= _arrowLabel.getX() + _arrowLabel.getWidth() &&
                me.getY() >= _arrowLabel.getY() && me.getY() <= _arrowLabel.getY() + _arrowLabel.getHeight())
            {
                _arrowLabel.setIcon(rolloverIcon);
            } else {
                // Then we we move off take it away.
                _arrowLabel.setIcon(standardIcon);
            }
        }

        /**
         * Empty method to satisy interface only, there is no special
         * action to take place when the mouse first enters the
         * PopupToolBoxButton area
         */
        public void mouseEntered(MouseEvent me) {
        }

        /**
         * Be double sure the dropdowns rollover border is removed when we leave the
         * whole button.
         */
        public void mouseExited(MouseEvent me) {
            _arrowLabel.setIcon(standardIcon);
        }
        
        public void mouseClicked(MouseEvent me) {
            if (me.getX() >= _arrowLabel.getX() && me.getX() <= _arrowLabel.getX() + _arrowLabel.getWidth() &&
                me.getY() >= _arrowLabel.getY() && me.getY() <= _arrowLabel.getY() + _arrowLabel.getHeight())
            {
                // If the dropdown area was clicked then do the dropdown action instead of the
                // current button action
                popup();
            } else {
                // If clicked elsewhere do the current button action
                _button.doClick();
            }
        }
        public void mousePressed(MouseEvent me) {}
        public void mouseReleased(MouseEvent me) {}
    }
    
    
    public class MyLayout implements LayoutManager2 {

        public void layoutContainer(Container parent) {
            Insets insets = parent.getInsets();

            Point loc;
            loc = new Point(insets.left, insets.top);
            int nComps = parent.getComponentCount();
            for (int i = 0 ; i < nComps ; i++) {
                Component comp = parent.getComponent(i);
                if (comp != null && comp.isVisible()) {
                    comp.setLocation(loc);
                    comp.setSize(comp.getPreferredSize());
                    loc.x += comp.getPreferredSize().width + insets.left;
                }
            }
        }

        public void addLayoutComponent(String name, Component comp) {
        }

        public void addLayoutComponent(Component comp, Object constraints) {
        }

        public void removeLayoutComponent(Component comp) {
        }

        public Dimension preferredLayoutSize(Container parent) {
            Insets insets = parent.getInsets();
            int nComps = parent.getComponentCount();
            Dimension preferredSize = new Dimension(0,0);
            for (int i = 0 ; i < nComps ; ++i) {
                Component comp = parent.getComponent(i);
                if (comp.isVisible()) {
                    preferredSize.width += comp.getPreferredSize().width + insets.left;
                    if (comp.getPreferredSize().height > preferredSize.height) {
                        preferredSize.height = comp.getPreferredSize().height;
                    }
                }
            }
            
            return addInsets(preferredSize, insets);
        }

        public Dimension minimumLayoutSize(Container parent) {
            Insets insets = parent.getInsets();
            int nComps = parent.getComponentCount();
            Dimension size = new Dimension(0,0);
            for (int i = 0 ; i < nComps ; ++i) {
                Component comp = parent.getComponent(i);
                if (comp.isVisible()) {
                    size.width += comp.getMinimumSize().width + insets.left;
                    if (comp.getMinimumSize().height > size.height) {
                        size.height = comp.getMinimumSize().height;
                    }
                }
            }
            return addInsets(size, insets);
        }
        
        private Dimension addInsets(Dimension dim, Insets insets) {
            // Don't add the insets width. The left width has already been added
            // and we don't want the right width as we want the dropdown right
            // next to the right hand edge.
            dim.width += 2;
            dim.height += insets.top + insets.bottom;
            return dim;
        }

        public Dimension maximumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        public void invalidateLayout(Container target) {}
        public float getLayoutAlignmentX(Container target) {return (float)0.5;}
        public float getLayoutAlignmentY(Container target) {return (float)0.5;}

    }
    
    
    
    
    /**
     * A metal look and feel arrow icon that can be created to point to a compass point.
     *
     * @author  administrator
     */
    private class DropDownIcon implements Icon, Serializable, SwingConstants {

        public static final int ROLLOVER = 0;
        public static final int STANDARD = 1;
        
        // Sprite buffer for the arrow image of the left button
        private int[][] buffer;

        protected int[][] standardBuffer = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 3, 3, 0},
            {0, 0, 0, 1, 1, 1, 1, 3, 3, 0, 0},
            {0, 0, 0, 0, 1, 1, 3, 3, 0, 0, 0},
            {0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        protected int[][] rolloverBuffer = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 3, 3, 1},
            {1, 0, 0, 1, 1, 1, 1, 3, 3, 0, 1},
            {1, 0, 0, 0, 1, 1, 3, 3, 0, 0, 1},
            {1, 0, 0, 0, 0, 3, 3, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        int direction;
        int iconWidth = 11;
        int iconHeight = 16;
        /** Construct an dropdown icon pointing in the given direction
         * @param direction the direction the arrow will point, this being one of the constants NORTH, SOUTH, EAST, WEST
         */        
        public DropDownIcon(int style) {
            if (style == ROLLOVER) buffer = rolloverBuffer;
            else buffer = standardBuffer;
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

            for (int i=0; i<buffer[0].length; i++) {
                for (int j=0; j<iconHeight; j++) {
                    if (buffer[j][i] != 0) {
                        g.setColor(colors[buffer[j][i]]);
                        g.drawLine(i, j, i, j);
                    }
                }
            }
        }

        /**
         * Gets the height of the icon.
         * @return the height of the icon
         */ 
        public int getIconWidth() {
            return iconWidth;
        }


        /**
         * Gets the height of the icon.
         * @return the height of the icon
         */ 
        public int getIconHeight() {
            return iconHeight;
        }

        public void setIconHeight(int height) {
            //iconHeight = height;
        }
        public void setIconWidth(int width) {
            //iconWidth = width;
        }
    }    
    
    
    
    
    
    // TODO move this into its own class
    private class ToolBox extends Toolbar {

        int _rows;
        int _cols;
        ArrayList _actions = new ArrayList();
        MouseListener _mouseListener;

        /** Creates a new instance of ToolBoxx */
        public ToolBox(int rows, int cols) {
            super();
            _rows = rows;
            _cols = cols;
            setLayout(new GridLayout(_rows,_cols));
        }

        public JButton add(Action action) {
            _actions.add(action);
            return super.add(action);
        }

        public void setButtonMouseListener(MouseListener mouseListener) {
            _mouseListener = mouseListener;
        }

        /**
         * Occasionally the ToolBox gets in a state where a button
         * shows rollover status at the wrong time.
         * The only way to get around this is to rebuild the ToolBox.
         */
        public void rebuild() {
            super.removeAll();
            Iterator it = _actions.iterator();
            while(it.hasNext()) {
                final Action a = (Action)it.next();
                JButton button = super.add(a);
                if (_mouseListener != null) {
                    button.addMouseListener(_mouseListener);
                }
            }
        }    
    }
}
