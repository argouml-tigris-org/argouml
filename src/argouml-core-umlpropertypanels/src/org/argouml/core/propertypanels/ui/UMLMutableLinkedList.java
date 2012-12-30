/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

// $header$
package org.argouml.core.propertypanels.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JPopupMenu;

import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.AbstractActionRemoveElement;

/**
 * This class is the GUI front for a mutable linked list. The user can add,
 * delete or create modelelements to the model. He can do that via a popup menu.
 * <p>
 *
 * The developer using this class can turn on and off the actions the user can
 * do via various configuration switches. To turn on/off the add option for
 * example, he can call the method setAddOption. Default the options for delete
 * and add are on. The option for new is off, since this is much less used.
 * <p>
 *
 * The implementation of the three actions, are delegated to several other
 * ActionClasses. ActionRemoveFromModel for the delete, the other actionclasses
 * need to be provided when constructing this object.
 * <p>
 *
 * Since december 14th, an option is added to configure the popupmenu that
 * should be shown.
 *
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
class UMLMutableLinkedList extends UMLLinkedList implements MouseListener {

    private static final Logger LOG =
        Logger.getLogger(UMLMutableLinkedList.class.getName());

    private boolean deletePossible = true;

    private boolean addPossible = false;

    private boolean newPossible = false;

    // TODO: This should not be an instance variable. It should just be
    // created and discarded as needed.
    private JPopupMenu popupMenu;

    private AbstractActionAddModelElement2 addAction = null;

    private AbstractActionNewModelElement newAction = null;

    private AbstractActionRemoveElement deleteAction = null;

    private class PopupMenu extends JPopupMenu {
        public PopupMenu() {
            super();
            if (isAdd()) {
                addAction.setTarget(getTarget());
                add(addAction);
                if (isNew() || isDelete()) {
                    addSeparator();
                }
            }
            if (isNew()) {
                newAction.setTarget(getTarget());
                add(newAction);
                if (isDelete()) {
                    addSeparator();
                }
            }
            if (isDelete()) {
                deleteAction.setObjectToRemove(getSelectedValue());
                deleteAction.setTarget(getTarget());
                add(deleteAction);
            }
        }
    }

    /**
     * Constructor that should be used if the developer wishes the popupmenu to
     * be constructed via the actions (as described in the javadoc of this class
     * itself).
     *
     * @param dataModel the data model
     * @param theAddAction the action for adding
     * @param theNewAction the action for new
     * @param theDeleteAction the action for deleting
     * @param showIcon true if an icon should be shown
     */
    public UMLMutableLinkedList(DefaultListModel dataModel,
            AbstractActionAddModelElement2 theAddAction,
            AbstractActionNewModelElement theNewAction,
            AbstractActionRemoveElement theDeleteAction) {
        super(dataModel, true);
        setAddAction(theAddAction);
        setNewAction(theNewAction);
        deleteAction = theDeleteAction;
        addMouseListener(this);
    }

    /**
     * The constructor.
     *
     * @param dataModel the data model
     * @param theAddAction the action for adding
     * @param theNewAction the action for new
     */
    public UMLMutableLinkedList(UMLModelElementListModel dataModel,
            AbstractActionAddModelElement2 theAddAction,
            AbstractActionNewModelElement theNewAction) {
        this(dataModel, theAddAction, theNewAction, null);
    }

    /**
     * The constructor.
     *
     * @param dataModel the data model
     * @param theAddAction the action for adding
     */
    public UMLMutableLinkedList(UMLModelElementListModel dataModel,
            AbstractActionAddModelElement2 theAddAction) {
        this(dataModel, theAddAction, null, null);
    }

    /**
     * The constructor.
     *
     * @param dataModel the data model
     * @param theNewAction the action for new
     */
    public UMLMutableLinkedList(UMLModelElementListModel dataModel,
            AbstractActionNewModelElement theNewAction) {
        this(dataModel, null, theNewAction, null);
    }

    /**
     * The constructor.
     *
     * @param dataModel the data model
     */
    protected UMLMutableLinkedList(UMLModelElementListModel dataModel) {
        this(dataModel,
                dataModel.getAddAction(),
                dataModel.getNewAction(),
                dataModel.getRemoveAction());
    }

    /**
     * Constructor that should be used if the developer wishes a customized
     * popupmenu.
     *
     * @param dataModel the data model
     * @param popup the popup menu
     * @param showIcon true if an icon should be shown
     */
    public UMLMutableLinkedList(UMLModelElementListModel dataModel,
            JPopupMenu popup, boolean showIcon) {
        super(dataModel, showIcon);
        setPopupMenu(popup);
    }

    /**
     * Constructor that should be used if the developer wishes a customized
     * popupmenu, without icons.
     *
     * @param dataModel the data model
     * @param popup the popup menu
     */
    public UMLMutableLinkedList(UMLModelElementListModel dataModel,
            JPopupMenu popup) {
        this(dataModel, popup, false);
    }

    /**
     * Returns the add.
     *
     * @return boolean
     */
    public boolean isAdd() {
        return addAction != null && addPossible;
    }

    /**
     * Returns the delete.
     *
     * @return boolean
     */
    public boolean isDelete() {
        return deleteAction != null & deletePossible;
    }

    /**
     * Returns the new.
     *
     * @return boolean
     */
    public boolean isNew() {
        return newAction != null && newPossible;
    }

    /**
     * Sets the delete.
     *
     * @param delete
     *            The delete to set
     */
//    public void setDelete(boolean delete) {
//        deletePossible = delete;
//    }

    /**
     * Returns the addAction.
     *
     * @return UMLChangeAction
     */
    public AbstractActionAddModelElement2 getAddAction() {
        return addAction;
    }

    /**
     * Returns the newAction.
     *
     * @return UMLChangeAction
     */
    public AbstractActionNewModelElement getNewAction() {
        return newAction;
    }

    /**
     * Sets the addAction.
     *
     * @param action
     *            The addAction to set
     */
    public void setAddAction(AbstractActionAddModelElement2 action) {
        if (action != null)
            addPossible = true;
        addAction = action;
    }

    /**
     * Sets the newAction.
     *
     * @param action
     *            The newAction to set
     */
    public void setNewAction(AbstractActionNewModelElement action) {
        if (action != null)
            newPossible = true;
        newAction = action;
    }

    /**
     * Tell the actions what objects they should work on.
     */
    protected void initActions() {
        if (isAdd()) {
            addAction.setTarget(getTarget());
        }
        if (isNew()) {
            newAction.setTarget(getTarget());
        }
        if (isDelete()) {
            deleteAction.setObjectToRemove(getSelectedValue());
            deleteAction.setTarget(getTarget());
        }
    }

    /*
     * @see java.awt.event.MouseListener#mouseReleased(
     *      java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()
                && !Model.getModelManagementHelper().isReadOnly(getTarget())) {
            Point point = e.getPoint();
            int index = locationToIndex(point);
            JPopupMenu popup = getPopupMenu();
            Object model = getModel();
            if (model instanceof UMLModelElementListModel) {
                ((UMLModelElementListModel) model).buildPopup(popup, index);
            }
            if (popup.getComponentCount() > 0) {
                initActions();
                LOG.log(Level.INFO,
                        "Showing popup at " + e.getX() + "," + e.getY());

                popup.show(this, e.getX(), e.getY());
            }
            e.consume();
        }
    }

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()
                && !Model.getModelManagementHelper().isReadOnly(getTarget())) {
            JPopupMenu popup = getPopupMenu();
            if (popup.getComponentCount() > 0) {
                initActions();
                LOG.log(Level.FINE,
                        "Showing popup at {0},{1}",
                        new Object[]{e.getX(), e.getY()});
                getPopupMenu().show(this, e.getX(), e.getY());
            }
            e.consume();
        }
    }

    /**
     * Returns the popupMenu.
     *
     * @return JPopupMenu
     */
    public JPopupMenu getPopupMenu() {
        popupMenu =  new PopupMenu();
        return popupMenu;
    }

    /**
     * Sets the popupMenu. <p>
     *
     * This allows to replace the complete default menu with a custom menu.
     * If nobody is using this, then we better remove this functionality, and
     * just return a new menu all the time - that would simplify initializing
     * it.
     *
     * @param menu
     *            The popupMenu to set
     */
    public void setPopupMenu(JPopupMenu menu) {
        popupMenu = menu;
    }

    /**
     * Returns the deleteAction.
     *
     * @return AbstractActionRemoveElement
     */
    public AbstractActionRemoveElement getDeleteAction() {
        return deleteAction;
    }

//    /**
//     * Sets the deleteAction.
//     *
//     * @param action
//     *            The deleteAction to set
//     */
//    public void setDeleteAction(AbstractActionRemoveElement action) {
//        deleteAction = action;
//    }

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.isPopupTrigger()
                && !Model.getModelManagementHelper().isReadOnly(getTarget())) {
            JPopupMenu popup = getPopupMenu();
            if (popup.getComponentCount() > 0) {
                initActions();
                LOG.log(Level.INFO,
                        "Showing popup at " + e.getX() + "," + e.getY());
                getPopupMenu().show(this, e.getX(), e.getY());
            }
            e.consume();
        }
    }

    /*
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    /*
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}
