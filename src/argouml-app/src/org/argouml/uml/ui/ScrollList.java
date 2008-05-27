// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.ui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;

/**
 * A scrollable list of items. This makes sure that there is no horizontal
 * scrollbar (which takes up too much screen real estate) and that sideways
 * scrolling can be achieved instead with arrow keys.
 * @author Bob Tarling
 */
public class ScrollList extends JScrollPane implements KeyListener {

    /**
     * The UID.
     */
    private static final long serialVersionUID = 6711776013279497682L;

    /**
     * The Component that this scroll is wrapping.
     */
    private Component list;
    
    /**
     * Builds a JList from a given list model and wraps
     * in a scrollable view.
     * @param listModel The model from which to build the list
     */
    public ScrollList(ListModel listModel) {
        this(listModel, true, true);
    }

    /**
     * Builds a JList from a given list model and wraps
     * in a scrollable view.
     * @param listModel The model from which to build the list
     * @param showIcon show an icon with elements in the list
     * @param showPath show containment path for elements in list
     */
    public ScrollList(ListModel listModel, boolean showIcon, boolean showPath) {
        setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setViewportView(new UMLLinkedList(listModel, showIcon, showPath));
    }
    
    public void setViewportView(Component view) {
        super.setViewportView(view);
        list = view;
    }
    
    /**
     * Examine key event to scroll left or right depending on key press
     * @param e the key event to examine
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            final Point posn = getViewport().getViewPosition();
            if (posn.x > 0) {
                getViewport().setViewPosition(new Point(posn.x - 1, posn.y));
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            final Point posn = getViewport().getViewPosition();
            if (list.getWidth() - posn.x > getViewport().getWidth()) {
                getViewport().setViewPosition(new Point(posn.x + 1, posn.y));
            }
        }
    }

    public void keyReleased(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }
    
    public void addNotify() {
        super.addNotify();
        list.addKeyListener(this);
    }
    
    public void removeNotify() {
        super.removeNotify();
        list.removeKeyListener(this);
    }
}
