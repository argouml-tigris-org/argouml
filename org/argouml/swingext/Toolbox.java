/*
 * ToolBox.java
 *
 * Created on 09 February 2003, 14:36
 */

package org.argouml.swingext;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusEvent;

import java.util.ArrayList;

/**
 *
 * @author  administrator
 */
public class Toolbox extends JButton {
    
    Toolbar _dropDownToolBox;
    JButton _target;
    JPopupMenu popup;
    
    /** Creates a new instance of ToolBox */
    public Toolbox(Icon icon, JButton target, int rows, int cols) {
        super(icon);
        _target = target;
        ArrayList actions = new ArrayList();
        
        _dropDownToolBox = new Toolbar();
        _dropDownToolBox.setLayout(new GridLayout(rows,cols));
        
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                popup = new JPopupMenu();
                popup.add(_dropDownToolBox);
                popup.show(_target, 0, (int)_target.getPreferredSize().getHeight());
            }
        });
    }

    public JButton add(Action a) {
        final JButton button = _dropDownToolBox.add(a);
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                button.setBorderPainted(false);
                popup.setVisible(false);
                button.setBorderPainted(true);
                popup = null;
                FocusEvent fe = new FocusEvent(button, FocusEvent.FOCUS_LOST);
                _target.setAction(button.getAction());
            }
        });
        return button;
    }
}
