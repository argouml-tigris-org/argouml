/*
 * PopupToolbox.java
 *
 * Created on 15 February 2003, 20:27
 */

package org.argouml.swingext;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
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

/**
 *
 * @author  Bob Tarling
 */
public class PopupToolBoxButton extends JButton {

    private JLabel _arrowLabel;
    private JButton _button;
    private ToolBox _toolBox;
    
    /** Creates a new instance of PopupToolbox */
    public PopupToolBoxButton(Action a, int rows, int cols) {
        super(a);
        _toolBox = new ToolBox(rows, cols);
        setAction(a);
        setLayout(new MyLayout());
        
        MyMouseListener myMouseListener = new MyMouseListener();
        addMouseMotionListener(myMouseListener);
        addMouseListener(myMouseListener);
    }
    
    public void setAction(Action a) {
        // Create an invisible button to contain the new action.
        // We can use this button to find out various info for the
        // current plaf. eg button size.
        _button = new JButton(a);
        String tooltip = _button.getToolTipText();
        if (tooltip == null || tooltip.trim().length() == 0) {
            tooltip = _button.getName();
        }
        _button.setText(null);
        
        // Create a label with the same icon as the new action
        JLabel iconLabel = new JLabel(_button.getIcon());
        
        // Create a label containing a dropdown arrow
        ArrowIcon arrowIcon = new ArrowIcon(ArrowIcon.SOUTH);
        arrowIcon.setIconHeight(_button.getHeight());
        _arrowLabel = new JLabel(arrowIcon);
        
        // Calculate size of arrow label
        Dimension arrowSize = _arrowLabel.getPreferredSize();
        arrowSize.height = iconLabel.getPreferredSize().height;
        _arrowLabel.setPreferredSize(arrowSize);
        
        // Remove any knowledge of the action to perform from the ancestor
        // we take control of performing the action and display the icon.
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

    public JButton add(Action a) {
        return _toolBox.add(a);
    }

    // We must override this method otherwise we get a crash in JDK1.4 windows plaf.
    public Dimension getMinimumSize() {
        int nComps = getComponentCount();
        Dimension size = new Dimension(0,0);
        for (int i = 0 ; i < nComps ; i++) {
            Component comp = getComponent(i);
            if (comp.isVisible()) {
                size.width += comp.getMinimumSize().width;
                if (comp.getMinimumSize().height > size.height) {
                    size.height = comp.getMinimumSize().height;
                }
            }
        }
        Insets insets = getInsets();
        size.width += insets.left + insets.right;
        size.height += insets.top + insets.bottom;
        return size;
    }

    public Dimension getPreferredSize() {
        int nComps = getComponentCount();
        Dimension preferredSize = new Dimension(0,0);
        for (int i = 0 ; i < nComps ; i++) {
            Component comp = getComponent(i);
            if (comp.isVisible()) {
                preferredSize.width += comp.getPreferredSize().width;
                if (comp.getPreferredSize().height > preferredSize.height) {
                    preferredSize.height = comp.getPreferredSize().height;
                }
            }
        }
        Insets insets = getInsets();
        preferredSize.width += insets.left + insets.right;
        preferredSize.height += insets.top + insets.bottom;
        return preferredSize;
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

        public void mouseMoved(MouseEvent me) {
            if (me.getX() >= _arrowLabel.getX() && me.getX() <= _arrowLabel.getX() + _arrowLabel.getWidth() &&
                me.getY() >= _arrowLabel.getY() && me.getY() <= _arrowLabel.getY() + _arrowLabel.getHeight())
            {
                // If the mouse moves into the area of the button that has the dropdown
                // then give the dropdown a rollover effect by giving it a border
                _arrowLabel.setBorder(new javax.swing.plaf.metal.MetalBorders.Flush3DBorder());
            } else {
                // Then we we move off take it away.
                _arrowLabel.setBorder(null);
            }
            //setBorder(mainButtonBorder);
        }

        /**
         * Empty method to satisy interface only, there is
         * no action when mouse enters splitter area
         */
        public void mouseEntered(MouseEvent me) {
        }

        public void mouseExited(MouseEvent me) {
            // Be double sure the dropdowns rollover border is removed when we leave the
            // whole button.
            _arrowLabel.setBorder(null);
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
                    loc.x += comp.getPreferredSize().width;
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
            int nComps = parent.getComponentCount();
            Dimension preferredSize = new Dimension(0,0);
            for (int i = 0 ; i < nComps ; i++) {
                Component comp = parent.getComponent(i);
                if (comp.isVisible()) {
                    preferredSize.width += comp.getPreferredSize().width;
                    if (comp.getPreferredSize().height > preferredSize.height) {
                        preferredSize.height = comp.getPreferredSize().height;
                    }
                }
            }
            Insets insets = parent.getInsets();
            preferredSize.width += insets.left + insets.right;
            preferredSize.height += insets.top + insets.bottom;
            return preferredSize;
        }

        public Dimension minimumLayoutSize(Container parent) {
            int nComps = parent.getComponentCount();
            Dimension size = new Dimension(0,0);
            for (int i = 0 ; i < nComps ; i++) {
                Component comp = parent.getComponent(i);
                if (comp.isVisible()) {
                    size.width += comp.getMinimumSize().width;
                    if (comp.getMinimumSize().height > size.height) {
                        size.height = comp.getMinimumSize().height;
                    }
                }
            }
            Insets insets = parent.getInsets();
            size.width += insets.left + insets.right;
            size.height += insets.top + insets.bottom;
            return size;
        }

        public Dimension maximumLayoutSize(Container parent) {
            int nComps = parent.getComponentCount();
            Dimension size = new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE);
            return size;
        }

        public void invalidateLayout(Container target) {}
        public float getLayoutAlignmentX(Container target) {return (float)0.5;}
        public float getLayoutAlignmentY(Container target) {return (float)0.5;}

    }
    
    public class ToolBox extends Toolbar {

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
