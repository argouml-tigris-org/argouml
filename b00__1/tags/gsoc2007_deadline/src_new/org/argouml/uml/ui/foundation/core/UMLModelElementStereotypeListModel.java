// $Id:UMLModelElementStereotypeListModel.java 13108 2007-07-22 18:52:42Z mvw $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import org.argouml.model.Model;
import org.argouml.uml.StereotypeUtility;
import org.argouml.uml.ui.UMLModelElementListModel2;

/**
 * The swing List Model for displaying stereotypes.
 * @since Oct 24, 2005
 * @author Bob Tarling
 */
public class UMLModelElementStereotypeListModel
    extends UMLModelElementListModel2 {

    /**
     * Constructor for UMLModelElementNamespaceListModel.
     */
    public UMLModelElementStereotypeListModel() {
        super("stereotype");
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        removeAllElements();
        if (Model.getFacade().isAModelElement(getTarget())) {
            addAll(Model.getFacade().getStereotypes(getTarget()));
        }
    }


    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAStereotype(element);
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildPopup(javax.swing.JPopupMenu, int)
     */
    public boolean buildPopup(JPopupMenu popup, int index) {
        // Add stereotypes submenu
        Action[] stereoActions =
            StereotypeUtility.getApplyStereotypeActions(getTarget());
        if (stereoActions != null) {
            for (int i = 0; i < stereoActions.length; ++i) {
                popup.add(getCheckItem(stereoActions[i]));
            }
        }
        return true;
    }

    /**
     * Creates a new checkbox menu item attached to the specified
     * action object and appends it to the end of this menu.
     * TODO: This is almost a duplicate of ArgoJMenu.addCheckItem must find a
     * way to merge.
     *
     * @param     a     the Action for the checkbox menu item to be added
     * @return          the new checkbox menu item
     */
    private static JCheckBoxMenuItem getCheckItem(Action a) {
        String name = (String) a.getValue(Action.NAME);
        Icon icon = (Icon) a.getValue(Action.SMALL_ICON);
        Boolean selected = (Boolean) a.getValue("SELECTED");
        JCheckBoxMenuItem mi =
            new JCheckBoxMenuItem(name, icon,
                      (selected == null
                       || selected.booleanValue()));
        mi.setHorizontalTextPosition(SwingConstants.RIGHT);
        mi.setVerticalTextPosition(SwingConstants.CENTER);
        mi.setEnabled(a.isEnabled());
        mi.addActionListener(a);
        return mi;
    }

}
