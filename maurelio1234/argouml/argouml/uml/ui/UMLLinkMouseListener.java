// $Id: UMLLinkMouseListener.java 11516 2006-11-25 04:30:15Z tfmorris $
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.SwingUtilities;

import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;

/**
 * A mouselistener that implements behaviour
 * to navigate to a selected modelelement
 * on double click of the left mousebutton
 * for a JList.
 *
 * @since Juli 9, 2004
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLLinkMouseListener implements MouseListener {

    /**
     * The graphical object for which this mouselistener is registrated.
     */
    private JList owner = null;

    /**
     * The total amount of mouseclicks the user has to do,
     * to go to the selected element.
     */
    private int numberOfMouseClicks;

    /**
     * The constructor.
     *
     * @param theOwner the graphical object for which
     *                 this mouselistener is registered
     */
    public UMLLinkMouseListener(JList theOwner) {
        this(theOwner, 2);
    }

    /**
     * The constructor.
     *
     * @param theOwner the graphical object for which
     *                 this mouselistener is registered
     * @param numberOfmouseClicks the total amount of mouseclicks the user
     *                            has to do, to go to the selected element
     */
    private UMLLinkMouseListener(JList theOwner, int numberOfmouseClicks) {
        owner = theOwner;
        numberOfMouseClicks = numberOfmouseClicks;
    }

    /*
     * @see java.awt.event.MouseListener#mouseClicked(
     *          java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() >= numberOfMouseClicks
                && SwingUtilities.isLeftMouseButton(e)) {

            Object o = owner.getSelectedValue();
            if (Model.getFacade().isAModelElement(o)) {
                TargetManager.getInstance().setTarget(o);
            }
            e.consume();
        }

    }

    /*
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
        // ignored
    }

    /*
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
        // ignored
    }

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
        // ignored
    }

    /*
     * @see java.awt.event.MouseListener#mouseReleased(
     *          java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        // ignored
    }

}
