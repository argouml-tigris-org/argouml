// Copyright (c) 1996-2002 The Regents of the University of California. All
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
 * Toolbar.java
 *
 * Created on 29 September 2002, 21:01
 */

package org.argouml.swingext;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 * A toolbar class which assumes rollover effects and automatically gives tooltip
 * to any buttons created by adding an action.
 *
 * @author  Bob Tarling
 */
public class Toolbar extends JToolBar implements MouseListener {
    
    private static final Color selectedBack = new Color(153,153,153);
    private static final Color buttonBack = new Color(204,204,204);
    private static Color normalBack;

    private boolean _rollover;
    
    /** Creates a new instance of Toolbar
     */
    public Toolbar() {
        super();
        this.setFloatable(false);
        this.setRollover(true);
        this.setMargin(new Insets(0,0,0,0));
    }
    
    /** Creates a new instance of Toolbar
     * @param title The title to display in the titlebar when toolbar is floating
     */
    public Toolbar(String title) {
        this(title, false);
    }

    /** Creates a new instance of Toolbar
     * @param title The title to display in the titlebar when toolbar is floating
     * @param floatable true if the toolbar can be dragged to a floating position
     */
    public Toolbar(String title, boolean floatable) {
        super(title);
        this.setFloatable(floatable);
        this.setRollover(true);
        this.setMargin(new Insets(0,0,0,0));
    }
    
    /** Creates a new instance of Toolbar with the given orientation
     * @param orientation HORIZONTAL or VERTICAL
     */
    public Toolbar(int orientation) {
        super(orientation);
        this.setFloatable(false);
        this.setRollover(true);
        this.setMargin(new Insets(0,0,0,0));
    }

    public void setRollover(boolean rollover) {
        super.setRollover(rollover);
        this._rollover = rollover;
        this.putClientProperty("JToolBar.isRollover", Boolean.valueOf(rollover));
    }
    
    public JButton add(Action action) {
        JButton button;

        if (action instanceof ButtonAction) {
            button = new JButton(action);
            String tooltip = button.getToolTipText();
            if (tooltip == null || tooltip.trim().length() == 0) {
                tooltip = button.getText();
            }
            button = super.add(action);
            button.setToolTipText(tooltip);
        } else {
            button = super.add(action);
        }
        button.setFocusPainted(false);
        button.addMouseListener(this);

        return button;
    }
    
    ////////////////////////////////////////////////////////////////
    // MouseListener implementation

    public void mouseEntered(MouseEvent me) { }
    public void mouseExited(MouseEvent me) { }
    public void mousePressed(MouseEvent me) { }
    public void mouseReleased(MouseEvent me) { }
    public void mouseClicked(MouseEvent me) {
        Object src = me.getSource();
        if (src instanceof JButton && ((JButton)src).getAction() instanceof ButtonAction) {
            JButton button = (JButton)src;
            ButtonAction action = (ButtonAction)button.getAction();
            if (action.isModal()) {
                Color currentBack = button.getBackground();
                if (currentBack.equals(selectedBack)) {
                    button.setBackground(normalBack);
                    button.setRolloverEnabled(_rollover);
                } else {
                    button.setBackground(selectedBack);
                    button.setRolloverEnabled(false);
                    normalBack = currentBack;
                }
                if (me.getClickCount() >= 2 && action.getLockMethod() == AbstractButtonAction.NONE) {
                    // FIXME Here I need to lock the button in place.
                    // The button should stay in place until it is pressed again (when
                    // it is released but not acted on) or any other key in its group
                    // is pressed.
                }
                else if (me.getClickCount() == 1) {
                }
            }
        }
    }
}
