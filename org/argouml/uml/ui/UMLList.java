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

package org.argouml.uml.ui;
import javax.swing.event.*;
import javax.swing.*;
import java.lang.reflect.*;
import ru.novosoft.uml.*;
import java.awt.event.*;
import java.awt.*;
import ru.novosoft.uml.foundation.core.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by {@link org.argouml.uml.ui.UMLList2},
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLList extends JList implements UMLUserInterfaceComponent, MouseListener {

    private UMLModelElementListModel _umlListModel;
    private boolean _navigate;

    public UMLList(UMLModelElementListModel listModel,boolean navigate) {
        super(listModel);
        _umlListModel = listModel;
        _navigate = navigate;
        setFont(MetalLookAndFeel.getSubTextFont());

        if(navigate) {
            addMouseListener(this);
        }
    }

    public void targetChanged() {
        _umlListModel.targetChanged();
        updateUI();
    }

    public void targetReasserted() {
        _umlListModel.targetReasserted();
        updateUI();
    }

    public void roleAdded(final MElementEvent event) {
        _umlListModel.roleAdded(event);
        updateUI();
    }

    public void recovered(final MElementEvent event) {
        _umlListModel.recovered(event);
        updateUI();
    }

    public void roleRemoved(final MElementEvent event) {
        _umlListModel.roleRemoved(event);
        updateUI();
    }

    public void listRoleItemSet(final MElementEvent event) {
        _umlListModel.listRoleItemSet(event);
        updateUI();
    }

    public void removed(final MElementEvent event) {
        _umlListModel.removed(event);
        updateUI();
    }
    public void propertySet(final MElementEvent event) {
          _umlListModel.propertySet(event);
          updateUI();
    }

    public void mouseReleased(final MouseEvent event) {
        if(event.isPopupTrigger()) {
            showPopup(event);
        }
    }

    public void mouseEntered(final MouseEvent event) {
        if(event.isPopupTrigger()) {
            showPopup(event);
        }
    }

    public void mouseClicked(final MouseEvent event) {
        if(event.isPopupTrigger()) {
            showPopup(event);
        }
        else {
            int mods = event.getModifiers();
            if(mods == InputEvent.BUTTON1_MASK) {
                int index = locationToIndex(event.getPoint());
                _umlListModel.open(index);
            }
        }
    }

    public void mousePressed(final MouseEvent event) {
        if(event.isPopupTrigger()) {
            showPopup(event);
        }
    }

    public void mouseExited(final MouseEvent event) {
        if(event.isPopupTrigger()) {
            showPopup(event);
        }
    }

/**
 *  modified July, 21, 2001 -  psager...
 */
    private final void showPopup(MouseEvent event) {
        Point point = event.getPoint();
        int index = locationToIndex(point);

       // JList returns -1 if list is empty or user right clicks on an area 
       // that has no list item, such as when the JList is not full. This code 
       // compensates for the user not clicking over a list item. pjs.
        if (index == -1){
            index = _umlListModel.getModelElementSize();
        }

        JPopupMenu popup = new JPopupMenu();
        if(_umlListModel.buildPopup(popup,index)) {
            popup.show(this,point.x,point.y);
        }
    }
}
