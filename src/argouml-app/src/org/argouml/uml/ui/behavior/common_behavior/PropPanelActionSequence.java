// $Id$
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

package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionRemoveElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.ActionRemoveModelElement;
import org.argouml.uml.ui.UMLModelElementOrderedListModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;

/**
 * Properties panel of an ActionSequence.
 */
public class PropPanelActionSequence extends PropPanelModelElement {

    private JScrollPane actionsScroll;

    /**
     * Construct a default property panel for an Action.
     */
    public PropPanelActionSequence() {
        this("label.action-sequence", lookupIcon("ActionSequence"));
    }

    /**
     * Construct an ActionSequence property panel with the given name and icon.
     *
     * @param name
     *            the name of the properties panel
     * @param icon
     *            the icon to be shown next to the name
     */
    public PropPanelActionSequence(String name, ImageIcon icon) {
        super(name, icon);
        initialize();
    }

    /**
     * The initialization of the panel with its fields and stuff.
     */
    public void initialize() {

        addField(Translator.localize("label.name"), getNameTextField());

//        JList actionsList = new UMLMutableLinkedList(
//                    new UMLActionSequenceActionListModel(),
//                    new ActionAddAction(),
//                    null, // new
//                    new ActionRemoveAction(),
//                    true);
        JList actionsList = new UMLActionSequenceActionList();
        actionsList.setVisibleRowCount(5);
        actionsScroll = new JScrollPane(actionsList);
        addField(Translator.localize("label.actions"),
                actionsScroll);

        addAction(new ActionNavigateContainerElement());
        addAction(new ActionNewStereotype());
        addAction(getDeleteAction());

    }

}

/**
 * Model for ActionSequence's list of Actions.
 */
class UMLActionSequenceActionListModel
    extends UMLModelElementOrderedListModel2 {

    /**
     * Constructor.
     */
    public UMLActionSequenceActionListModel() {
        super("action");
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        if (getTarget() != null) {
            setAllElements(Model.getFacade().getActions(getTarget()));
        }
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(java.lang.Object)
     */
    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAAction(element);
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#moveDown(int)
     */
    protected void moveDown(int index) {
        Object target = getTarget();
        List c = Model.getFacade().getActions(target);
        if (index < c.size() - 1) {
            Object item = c.get(index);
            Model.getCommonBehaviorHelper().removeAction(target, item);
            Model.getCommonBehaviorHelper().addAction(target, index + 1, item);
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#moveToBottom(int)
     */
    @Override
    protected void moveToBottom(int index) {
        Object target = getTarget();
        List c = Model.getFacade().getActions(target);
        if (index < c.size() - 1) {
            Object item = c.get(index);
            Model.getCommonBehaviorHelper().removeAction(target, item);
            Model.getCommonBehaviorHelper().addAction(target, c.size(), item);
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#moveToTop(int)
     */
    @Override
    protected void moveToTop(int index) {
        Object target = getTarget();
        List c = Model.getFacade().getActions(target);
        if (index > 0) {
            Object item = c.get(index);
            Model.getCommonBehaviorHelper().removeAction(target, item);
            Model.getCommonBehaviorHelper().addAction(target, 0, item);
        }
    }
}


class ActionRemoveAction extends AbstractActionRemoveElement {


    /**
     * Construct an action to remove an Action.
     */
    public ActionRemoveAction() {
        super(Translator.localize("menu.popup.remove"));
    }

    /*
     * @see org.tigris.gef.undo.UndoableAction#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object action = getObjectToRemove();
        if (action != null) {
            Object as = getTarget();
            if (Model.getFacade().isAActionSequence(as)) {
                Model.getCommonBehaviorHelper().removeAction(as, action);
            }
        }
    }

}

class UMLActionSequenceActionList extends UMLMutableLinkedList {

    /**
     * Construct a default object with a new UMLActionSequenceActionListModel.
     */
    public UMLActionSequenceActionList() {
        super(new UMLActionSequenceActionListModel());
    }

    /*
     * @see org.argouml.uml.ui.UMLMutableLinkedList#getPopupMenu()
     */
    @Override
    public JPopupMenu getPopupMenu() {
        return new PopupMenuNewAction(ActionNewAction.Roles.MEMBER, this);
    }

}

class PopupMenuNewActionSequenceAction extends JPopupMenu {

    /**
     * Constructs a new popupmenu. The given parameter role determines what
     * the purpose is of the actions that can be created via this popupmenu.
     * The parameter must comply to the interface Roles
     * defined on ActionNewAction.
     * @param role the role
     * @param list the list
     */
    public PopupMenuNewActionSequenceAction(String role,
            UMLMutableLinkedList list) {
        super();

        JMenu newMenu = new JMenu();
        newMenu.setText(Translator.localize("action.new"));

        newMenu.add(ActionNewCallAction.getInstance());
        ActionNewCallAction.getInstance().setTarget(list.getTarget());
        ActionNewCallAction.getInstance().putValue(
                ActionNewAction.ROLE, role);

        add(newMenu);

        addSeparator();

        ActionRemoveModelElement.SINGLETON.setObjectToRemove(ActionNewAction
             .getAction(role, list.getTarget()));
        add(ActionRemoveModelElement.SINGLETON);
    }
}

