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

// $header$
package org.argouml.uml.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;

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
public class UMLMutableLinkedList extends UMLLinkedList
    implements MouseListener {

    private boolean deletePossible = true;

    private boolean addPossible = false;

    private boolean newPossible = false;

    private JPopupMenu popupMenu;

    private AbstractActionAddModelElement addAction = null;

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
    public UMLMutableLinkedList(UMLModelElementListModel2 dataModel,
            AbstractActionAddModelElement theAddAction,
            AbstractActionNewModelElement theNewAction,
            AbstractActionRemoveElement theDeleteAction, boolean showIcon) {
        super(dataModel, showIcon);
        setAddAction(theAddAction);
        setNewAction(theNewAction);
        if (theDeleteAction != null)
            setDeleteAction(theDeleteAction);
        addMouseListener(this);
    }

    /**
     * The constructor.
     *
     * @param dataModel the data model
     * @param theAddAction the action for adding
     * @param theNewAction the action for new
     */
    public UMLMutableLinkedList(UMLModelElementListModel2 dataModel,
            AbstractActionAddModelElement theAddAction,
            AbstractActionNewModelElement theNewAction) {
        this(dataModel, theAddAction, theNewAction, null, true);
    }

    /**
     * The constructor.
     *
     * @param dataModel the data model
     * @param theAddAction the action for adding
     */
    public UMLMutableLinkedList(UMLModelElementListModel2 dataModel,
            AbstractActionAddModelElement theAddAction) {
        this(dataModel, theAddAction, null, null, true);
    }

    /**
     * The constructor.
     *
     * @param dataModel the data model
     * @param theNewAction the action for new
     */
    public UMLMutableLinkedList(UMLModelElementListModel2 dataModel,
            AbstractActionNewModelElement theNewAction) {
        this(dataModel, null, theNewAction, null, true);
    }

    /**
     * The constructor.
     *
     * @param dataModel the data model
     */
    protected UMLMutableLinkedList(UMLModelElementListModel2 dataModel) {
        this(dataModel, null, null, null, true);
        setDelete(false);
        setDeleteAction(null);
    }

    /**
     * Constructor that should be used if the developer wishes a customized
     * popupmenu.
     *
     * @param dataModel the data model
     * @param popup the popup menu
     * @param showIcon true if an icon should be shown
     */
    public UMLMutableLinkedList(UMLModelElementListModel2 dataModel,
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
    public UMLMutableLinkedList(UMLModelElementListModel2 dataModel,
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
    public void setDelete(boolean delete) {
        deletePossible = delete;
    }

    /**
     * Returns the addAction.
     *
     * @return UMLChangeAction
     */
    public AbstractActionAddModelElement getAddAction() {
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
    public void setAddAction(AbstractActionAddModelElement action) {
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

    /*
     * @see java.awt.event.MouseListener#mouseReleased(
     *      java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            Point point = e.getPoint();
            int index = locationToIndex(point);
            JPopupMenu popup = getPopupMenu();
            Object model = getModel();
            if (model instanceof UMLModelElementListModel2) {
                ((UMLModelElementListModel2) model).buildPopup(popup, index);
            }
            if (popup.getComponentCount() > 0) {
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
        if (e.isPopupTrigger()) {
            JPopupMenu popup = getPopupMenu();
            if (popup.getComponentCount() > 0) {
                getPopupMenu().show(this, e.getX(), e.getY());
            }
            e.consume();
        }
    }

    /**
     * Returns a new popupMenu, initialised
     * with the current target and selected item.
     *
     * @return JPopupMenu
     */
    public JPopupMenu getPopupMenu() {
        popupMenu =  new PopupMenu();
        return popupMenu;
    }

    /**
     * Sets the popupMenu.
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

    /**
     * Sets the deleteAction.
     *
     * @param action
     *            The deleteAction to set
     */
    public void setDeleteAction(AbstractActionRemoveElement action) {
        deleteAction = action;
    }

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JPopupMenu popup = getPopupMenu();
            if (popup.getComponentCount() > 0) {
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
