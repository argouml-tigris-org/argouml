// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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
 * <p>
 * 
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLMutableLinkedList extends UMLLinkedList implements MouseListener{

    private boolean _delete = true;

    private boolean _add = false;

    private boolean _new = false;

    private JPopupMenu _popupMenu;

    private AbstractActionAddModelElement _addAction = null;

    private AbstractActionNewModelElement _newAction = null;

    private AbstractActionRemoveElement _deleteAction = ActionRemoveModelElement.SINGLETON;

    private class PopupMenu extends JPopupMenu {
        public PopupMenu() {
            super();
            if (isAdd()) {
                _addAction.setTarget(getTarget());
                add(_addAction);
                if (isNew() || isDelete()) {
                    addSeparator();
                }
            }
            if (isNew()) {
                _newAction.setTarget(getTarget());
                add(_newAction);
                if (isDelete()) {
                    addSeparator();
                }
            }
            if (isDelete()) {
                _deleteAction.setObjectToRemove(getSelectedValue());
                _deleteAction.setTarget(getTarget());
                add(_deleteAction);
            }
        }
    }

    /**
     * Constructor that should be used if the developer wishes the popupmenu to
     * be constructed via the actions (as described in the javadoc of this class
     * itself).
     * 
     * @param dataModel
     */
    public UMLMutableLinkedList(UMLModelElementListModel2 dataModel,
            AbstractActionAddModelElement addAction,
            AbstractActionNewModelElement newAction,
            AbstractActionRemoveElement deleteAction, boolean showIcon) {
        super(dataModel, showIcon);          
        setAddAction(addAction);
        setNewAction(newAction);
        if (deleteAction != null)
            setDeleteAction(deleteAction);
        addMouseListener(this);
    }

    public UMLMutableLinkedList(UMLModelElementListModel2 dataModel,
            AbstractActionAddModelElement addAction,
            AbstractActionNewModelElement newAction) {
        this(dataModel, addAction, newAction, null, false);
    }
    
    public UMLMutableLinkedList(UMLModelElementListModel2 dataModel, AbstractActionAddModelElement addAction) {
        this(dataModel, addAction, null, null, false);
    }
    
    public UMLMutableLinkedList(UMLModelElementListModel2 dataModel, AbstractActionNewModelElement newAction) {
        this(dataModel, null, newAction, null, false);
    }
    
    protected UMLMutableLinkedList(UMLModelElementListModel2 dataModel) {
        this(dataModel, null, null, null, false);
        setDelete(false);
        setDeleteAction(null);
    }

    /**
     * Constructor that should be used if the developer wishes a customized
     * popupmenu.
     * 
     * @param dataModel
     * @param popup
     */
    public UMLMutableLinkedList(UMLModelElementListModel2 dataModel,
            JPopupMenu popup, boolean showIcon) {
        super(dataModel, showIcon);
        setPopupMenu(popup);
    }

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
        return _addAction != null && _add;
    }

    /**
     * Returns the delete.
     * 
     * @return boolean
     */
    public boolean isDelete() {
        return _deleteAction != null & _delete;
    }

    /**
     * Returns the new.
     * 
     * @return boolean
     */
    public boolean isNew() {
        return _newAction != null && _new;
    }

    /**
     * Sets the delete.
     * 
     * @param delete
     *            The delete to set
     */
    public void setDelete(boolean delete) {
        _delete = delete;
    }

    /**
     * Returns the addAction.
     * 
     * @return UMLChangeAction
     */
    public AbstractActionAddModelElement getAddAction() {
        return _addAction;
    }

    /**
     * Returns the newAction.
     * 
     * @return UMLChangeAction
     */
    public AbstractActionNewModelElement getNewAction() {
        return _newAction;
    }

    /**
     * Sets the addAction.
     * 
     * @param addAction
     *            The addAction to set
     */
    public void setAddAction(AbstractActionAddModelElement addAction) {
        if (addAction != null)
            _add = true;
        _addAction = addAction;
    }

    /**
     * Sets the newAction.
     * 
     * @param newAction
     *            The newAction to set
     */
    public void setNewAction(AbstractActionNewModelElement newAction) {
        if (newAction != null)
            _new = true;
        _newAction = newAction;
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(
     *      java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JPopupMenu popup = getPopupMenu();
            if (popup.getComponentCount() > 0) {
                getPopupMenu().show(this, e.getX(), e.getY());
            }
            e.consume();
        } 
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
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
     * Returns the popupMenu.
     * 
     * @return JPopupMenu
     */
    public JPopupMenu getPopupMenu() {
        return new PopupMenu();
    }

    /**
     * Sets the popupMenu.
     * 
     * @param popupMenu
     *            The popupMenu to set
     */
    public void setPopupMenu(JPopupMenu popupMenu) {
        _popupMenu = popupMenu;
    }

    /**
     * Returns the deleteAction.
     * 
     * @return AbstractActionRemoveElement
     */
    public AbstractActionRemoveElement getDeleteAction() {
        return _deleteAction;
    }

    /**
     * Sets the deleteAction.
     * 
     * @param deleteAction
     *            The deleteAction to set
     */
    public void setDeleteAction(AbstractActionRemoveElement deleteAction) {
        _deleteAction = deleteAction;
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JPopupMenu popup = getPopupMenu();
            if (popup.getComponentCount() > 0) {
                getPopupMenu().show(this, e.getX(), e.getY());
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
}