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

import javax.swing.JPopupMenu;

import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * <p>
 * This class is the GUI front for a mutable linked list. The user can add,
 * delete or create modelelements to the model. He can do that via a popup
 * menu.
 * </p>
 * <p>
 * The developer using this class can turn on and off the actions the user can
 * do via various configuration switches. To turn on/off the add option for
 * example, he can call the method setAddOption. Default the options for delete
 * and add are on. The option for new is off, since this is much less used.
 * </p>
 * <p>
 * The implementation of the three actions, are delegated to several other
 * ActionClasses. ActionRemoveFromModel for the delete, the other actionclasses
 * need to be provided when constructing this object</p>
 * <p>
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLMutableLinkedList extends UMLLinkedList {
    
    private boolean _delete = true;
    private boolean _add = false;
    private boolean _new = false;
    
    private AbstractActionAddModelElement _addAction = null;
    private UMLChangeAction _newAction = null;
    
    protected class PopupMenu extends JPopupMenu {
        public PopupMenu() {
            super();
            if (isAdd()) {
                _addAction.setTarget((MModelElement)getContainer().getTarget());
                add(_addAction);
            }
            if (isDelete()) {
                add(new ActionRemoveModelElement((MModelElement)getSelectedValue()));
            }
            if (isNew()) {
                add(_newAction);
            }
        }
    }
    


    /**
     * Constructor for UMLMutableLinkedList.
     * @param container
     * @param dataModel
     */
    public UMLMutableLinkedList(
        UMLUserInterfaceContainer container,
        UMLModelElementListModel2 dataModel,
        AbstractActionAddModelElement addAction,
        UMLChangeAction newAction) {
        super(container, dataModel);
        setAddAction(addAction);
        setNewAction(newAction);
    }
    
    /**
     * Returns the add.
     * @return boolean
     */
    public boolean isAdd() {
        return _add;
    }

    /**
     * Returns the delete.
     * @return boolean
     */
    public boolean isDelete() {
        return _delete;
    }

    /**
     * Returns the n.
     * @return boolean
     */
    public boolean isNew() {
        return _new;
    }

    /**
     * Sets the delete.
     * @param delete The delete to set
     */
    public void setDelete(boolean delete) {
        _delete = delete;
    }

    /**
     * Returns the addAction.
     * @return UMLChangeAction
     */
    public UMLChangeAction getAddAction() {
        return _addAction;
    }

    /**
     * Returns the newAction.
     * @return UMLChangeAction
     */
    public UMLChangeAction getNewAction() {
        return _newAction;
    }

    /**
     * Sets the addAction.
     * @param addAction The addAction to set
     */
    public void setAddAction(AbstractActionAddModelElement addAction) {
        if (addAction != null) _add = true;
        _addAction = addAction;
    }

    /**
     * Sets the newAction.
     * @param newAction The newAction to set
     */
    public void setNewAction(UMLChangeAction newAction) {
        if (newAction != null) _new = true;
        _newAction = newAction;
    }
    
    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if (e.getSource() == this) {
            if (e.isPopupTrigger()) {
                PopupMenu popup = new PopupMenu();
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
            e.consume();
        }
    }

}

