/*
 * PopupToolBoxButton.java
 *
 * Created on 15 February 2003, 20:27
 */

package org.argouml.swingext;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
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

import java.io.Serializable;

/** An extension of JButton to which alternative actions can be added.
 * The button to trigger these actions become available when a
 * dropdown icon is pressed on this button.
 * @author Bob Tarling
 */
public class PopupToolBoxButton extends JButton {

    //private JLabel _arrowLabel;
    private JButton _button;
    private Toolbox _toolBox;
    private DecoratedIcon _standardIcon;
    private DecoratedIcon _rolloverIcon;
    private int _division;
    
    /** Creates a new instance of PopupToolbox
     * @param a The default action when pressing this button
     * @param rows The number of rows of buttons to display in the popup toolbox
     * @param cols The number of columns of buttons to display in the popup toolbox
     */
    public PopupToolBoxButton(Action a, int rows, int cols) {
        super(a);
        _toolBox = new Toolbox(rows, cols);
        setAction(a);
        
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
        if (tooltip == null || tooltip.trim().length() == 0) {
            tooltip = _button.getText();
        }
        _button.setText(null);
        
        _standardIcon = new DropDownIcon((ImageIcon)_button.getIcon());
        _rolloverIcon = new DropDownIcon((ImageIcon)_button.getIcon(), true);
        
        // Remove any knowledge of the action to perform from the ancestor
        // we take control of performing the action and displaying the icon.
        super.setAction(null);
        setIcon(_standardIcon);
        setToolTipText(tooltip);
    }
    
    //private DecoratedIcon createIcon(int style) {
    //    DecoratedIcon dropDownIcon = new DecoratedIcon((ImageIcon)_button.getIcon(), style);
    //    return dropDownIcon;
    //}

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
    
    void setDivision(int division) {
        _division = division;
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
            if (me.getX() >= _division + getMargin().left) {
                setIcon(_rolloverIcon);
            } else {
                // Then we we move off take it away.
                setIcon(_standardIcon);
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
         * Be double sure the dropdowns rollover divider is removed when we leave the
         * button.
         */
        public void mouseExited(MouseEvent me) {
            setIcon(_standardIcon);
        }
        
        public void mouseClicked(MouseEvent me) {
            if (me.getX() >= _division + getMargin().left) {
                // If the dropdown area was clicked then do the dropdown action instead of the
                // current button action
                popup();
            } else {
                // If clicked elsewhere do the current button action
                _button.doClick();
            }
        }
        
        /**
         * Empty method to satisy interface only, there is no special
         * action to take place when the mouse is pressed on the
         * PopupToolBoxButton area
         */
        public void mousePressed(MouseEvent me) {}
        
        /**
         * Empty method to satisy interface only, there is no special
         * action to take place when the mouse is released on the
         * PopupToolBoxButton area
         */
        public void mouseReleased(MouseEvent me) {}
    }
}
