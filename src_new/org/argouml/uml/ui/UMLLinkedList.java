// Copyright (c) 1996-99 The Regents of the University of California. All
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

// $header$
package org.argouml.uml.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import ru.novosoft.uml.foundation.core.MModelElement;


/**
 * An UMLList that implements 'jump' behaviour. As soon as the user doubleclicks 
 * on an element in the list, that element is selected in argouml.
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLLinkedList extends UMLList2 implements MouseListener {

    /**
     * Constructor for UMLLinkedList.
     * @param container
     * @param dataModel
     */
    public UMLLinkedList(
        UMLUserInterfaceContainer container,
        UMLModelElementListModel2 dataModel) {
        super(container, dataModel);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addMouseListener(this);
    }

    /**
     * @see org.argouml.uml.ui.UMLList2#doIt(javax.swing.event.ListSelectionEvent)
     */
    protected void doIt(ListSelectionEvent e) {
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == this) {
            if (e.getClickCount() >=2 && SwingUtilities.isLeftMouseButton(e)) {
                Object o = getSelectedValue();
                if (o instanceof MModelElement) {
                    getContainer().navigateTo(o);
                }
            }
            e.consume();
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
    }

}
