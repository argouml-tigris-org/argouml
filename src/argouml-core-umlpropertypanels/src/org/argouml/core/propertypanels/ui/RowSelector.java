/* $Id$
 *******************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */

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

package org.argouml.core.propertypanels.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.core.propertypanels.model.IconIdentifiable;
import org.argouml.core.propertypanels.model.Named;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Command;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.UmlHelper;
import org.argouml.ui.ActionCreateContainedModelElement;
import org.argouml.ui.UndoableAction;
import org.tigris.gef.presentation.FigTextEditor;
import org.tigris.toolbar.toolbutton.ToolBox;

/**
 * A control for displaying the contents of a list model elements in a panel
 * that can be expanded to take maximum possible screen space or shrunk to
 * minimum at the user discretion.
 *
 * @author Bob Tarling
 * @since 0.29.2
 */
class RowSelector extends UmlControl
        implements MouseListener, ListDataListener, ListSelectionListener, Expandable {

    /**
     * The logger.
     */
    private static final Logger LOG =
        Logger.getLogger(RowSelector.class.getName());

    /**
     * class uid
     */
    private static final long serialVersionUID = 3937183621483536749L;

    /**
     * The model element that is the target of this control.
     */
    private final Object target;

    /**
     * Identifies if the model element is a readonly modelelement.
     */
    private final boolean readonly;

    private final List actions;

    /**
     * The scrollpane that will contain the list
     */
    private ScrollList scroll;

    /**
     * The preferred size of the component when shrunk
     */
    private final Dimension shrunkPreferredSize;

    /**
     * The preferred size of the component when expanded
     */
    private final Dimension expandedPreferredSize;

    /**
     * The maximum size of the component when expanded
     */
    private final Dimension expandedMaximumSize;

    /**
     * True if the component is expandable
     */
    private final boolean expandable;

    /**
     * The current expanded state
     */
    private boolean expanded = false;

    /**
     * The model element that is being moved. This is used because move
     * effectively removes the selected model element and then adds it in a
     * new place.
     * The problem to the user is that when added the selection is lost.
     * By recording the model element being moved the add event can detected
     * when the element is added and mark it as selected.
     */
    private MovedModelElement movedModelElement = new MovedModelElement();

    /**
     * The delete action that we must enable/disable
     */
    private final DeleteAction deleteAction;

    /**
     * The remove action that we must enable/disable
     */
    private final Action removeAction;

    /**
     * The delete action that we must enable/disable
     */
    private final MoveUpAction moveUpAction;

    /**
     * The delete action that we must enable/disable
     */
    private final MoveDownAction moveDownAction;

    /**
     * The delete action that we must enable/disable
     */
    private final MoveTopAction moveTopAction;

    /**
     * The delete action that we must enable/disable
     */
    private final MoveBottomAction moveBottomAction;

    /**
     * Constructor
     * @param model The single item list model
     */
    public RowSelector(DefaultListModel model) {
        this(model, false, true);

    }
    /**
     * Constructor
     * @param model The single item list model
     * @param expanded true if the control should be initially expanded
     * @param expandable true if the control should be expandable
     */
    public RowSelector(
            final DefaultListModel model,
            final boolean expanded,
            final boolean expandable) {
        super(new BorderLayout());

        this.expandable = expandable;
        Object metaType = null;
        List metaTypes = null;
        final Action addAction;
        List<Action> newActions = null;
        List<Command> additionalNewCommands = null;

        final String propertyName;
        if (model instanceof UMLModelElementListModel) {
            // Temporary until SimpleListModel is used for all
            target = ((UMLModelElementListModel) model).getTarget();
            metaType = ((UMLModelElementListModel) model).getMetaType();
            propertyName = null;
            scroll = new OldScrollList(model, 1);
            readonly = Model.getModelManagementHelper().isReadOnly(target);
            metaTypes = null;
            newActions = ((UMLModelElementListModel) model).getNewActions();
        } else if (model instanceof SimpleListModel) {
            target = ((SimpleListModel) model).getUmlElement();
            propertyName = ((SimpleListModel) model).getPropertyName();
            metaType = ((SimpleListModel) model).getMetaType();
            metaTypes = ((SimpleListModel) model).getMetaTypes();
            additionalNewCommands = ((SimpleListModel) model).getAdditionalCommands();
            scroll = new ScrollListImpl(model, 1);
            readonly = Model.getModelManagementHelper().isReadOnly(target);
        } else {
            // Temporary until SimpleListModel is used for all
            propertyName = null;
            target = null;
            readonly = true;
        }

        assert (target != null);

        if (metaTypes == null) {
            metaTypes = new ArrayList();
            metaTypes.add(metaType);
        }

        if (LOG.isLoggable(Level.FINE)) {
            LOG.log(Level.FINE, "Creating list for {0}", target);
            LOG.log(Level.FINE, "model = {0}", model.getClass().getName());
            LOG.log(Level.FINE, "metatype = {0}", metaType);
            LOG.log(Level.FINE, "target = {0}", target);
        }

        add((JComponent) scroll);

        shrunkPreferredSize = ((JComponent) scroll).getPreferredSize();

        remove(((JComponent) scroll));

        scroll = ScrollListFactory.create(model);
        JScrollPane jscroll = ((JScrollPane) scroll);

        add(jscroll);
        expandedPreferredSize = jscroll.getPreferredSize();
        expandedMaximumSize = jscroll.getMaximumSize();

        jscroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        if (model instanceof SimpleListModel
                && ((SimpleListModel) model).getAddCommand() != null) {
            removeAction =
                new RemoveAction(scroll.getList(), ((SimpleListModel) model));
            addAction =
                new AddAction(((SimpleListModel) model).getAddCommand());
        } else {
            removeAction = null;
            addAction = null;
        }


        actions = new ArrayList<Action>(6);
        if (!expandable && !expanded) {
            jscroll.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            deleteAction = null;
            moveUpAction = null;
            moveDownAction = null;
            moveTopAction = null;
            moveBottomAction = null;
            if (!readonly) {
                // Create popup tool if we have a single row

                for (Object meta : metaTypes) {
                    if (Model.getUmlFactory().isContainmentValid(
                            meta, target)) {
                        final String label =
                            "button.new-"
                            + Model.getMetaTypes().getName(meta).toLowerCase();
                        final Action createAction =
                            new ActionCreateContainedModelElement(
                                meta,
                                target,
                                propertyName,
                                label);
                        actions.add(createAction);
                    }
                }

                if (newActions != null) {
                    actions.addAll(newActions);
                }

                if (additionalNewCommands != null
                        && !additionalNewCommands.isEmpty()) {
                    for (Command cmd : additionalNewCommands) {
                        if (cmd instanceof IconIdentifiable
                                && cmd instanceof Named) {
                            actions.add(new CommandAction(
                                    cmd,
                                    ((Named)cmd).getName(),
                                    ((IconIdentifiable)cmd).getIcon()));
                        } else {
                            actions.add(new CommandAction(cmd));
                        }
                    }
                }

                if (!actions.isEmpty()) {
                    final JPanel buttonPanel =
                        createSingleButtonPanel(actions);

                    add(buttonPanel, BorderLayout.WEST);
                }
            }
        } else {
            if (!readonly) {
                // TODO: Lets build this into a separate buildToolbar method

                // Create add and remove buttons if needed first
                if (addAction != null) {
                    actions.add(addAction);
                }
                if (removeAction != null) {
                    actions.add(removeAction);
                    deleteAction = null;
                } else {
                    deleteAction = new DeleteAction();
                    actions.add(deleteAction);
                }

                // then any new buttons
                List<Action> createActions = new ArrayList<Action>();
                for (Object meta : metaTypes) {
                    if (Model.getUmlFactory().isContainmentValid(
                            meta, target)) {
                        final String label =
                            "button.new-"
                            + Model.getMetaTypes().getName(meta).toLowerCase();
                        final Action createAction =
                            new ActionCreateContainedModelElement(
                                meta,
                                target,
                                label);
                        createActions.add(createAction);
                    }
                }

                if (additionalNewCommands != null
                        && !additionalNewCommands.isEmpty()) {
                    for (Command cmd : additionalNewCommands) {
                        if (cmd instanceof IconIdentifiable && cmd instanceof Named) {
                            createActions.add(new CommandAction(cmd, ((Named)cmd).getName(), ((IconIdentifiable)cmd).getIcon()));
                        } else {
                            createActions.add(new CommandAction(cmd));
                        }
                    }
                }

                if (createActions.size() > 2) {
                    actions.add(createActions.toArray());
                } else {
                    actions.addAll(createActions);
                }

                if (Model.getUmlHelper().isMovable(metaType)) {
                    moveUpAction = new MoveUpAction();
                    moveDownAction = new MoveDownAction();
                    moveTopAction = new MoveTopAction();
                    moveBottomAction = new MoveBottomAction();
                } else {
                    moveUpAction = null;
                    moveDownAction = null;
                    moveTopAction = null;
                    moveBottomAction = null;
                }
            } else {
                moveUpAction = null;
                moveDownAction = null;
                moveTopAction = null;
                moveBottomAction = null;
                deleteAction = null;
            }

            this.addMouseListener(this);

            if (!Model.getModelManagementHelper().isReadOnly(target)) {
                if (deleteAction != null) {
                    getList().addListSelectionListener(deleteAction);
                }
                if (removeAction != null) {
                    getList().addListSelectionListener(this);
                }
                // TODO: We should really test the model instead for this
                // but we have no API yet.
                // Can we just check if the collection to build the JList
                // control implements the List interface?
                if (Model.getUmlHelper().isMovable(metaType)) {
                    getList().addListSelectionListener(moveUpAction);
                    getList().addListSelectionListener(moveDownAction);
                    getList().addListSelectionListener(moveTopAction);
                    getList().addListSelectionListener(moveBottomAction);
                }
            }
            getList().addMouseListener(this);

            getModel().addListDataListener(this);
        }
    }

    /**
     * Make sure the control is always a fixed height
     * @return the minimum size as the height of one row in a JList
     */
    public Dimension getMinimumSize() {
        return shrunkPreferredSize;
    }

    /**
     * Make sure the control is always a fixed height
     * @return the maximum size as the height of one row in a JList
     */
    public Dimension getMaximumSize() {
        Dimension size = super.getMaximumSize();
        if (expanded) {
            size.height = expandedMaximumSize.height;
        } else {
            size.height = shrunkPreferredSize.height;
        }
        return size;
    }

    /**
     * @return the preferred size as the height of one row in a JList
     */
    public Dimension getPreferredSize() {
        if (expanded) {
            return expandedPreferredSize;
        } else {
            return shrunkPreferredSize;
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JPopupMenu popup = new JPopupMenu();
            for (Object action : actions) {
                if (action instanceof Action) {
                    popup.add((Action) action);
                }
            }
            if (moveTopAction != null) {
                popup.add(moveTopAction);
                popup.add(moveBottomAction);
                popup.add(moveUpAction);
                popup.add(moveDownAction);
            }
            if (popup.getComponentCount() > 0) {
                popup.show(this, e.getX(), e.getY());
            }
            e.consume();
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JPopupMenu popup = new JPopupMenu();
            for (Object action : actions) {
                if (action instanceof Action) {
                    popup.add((Action) action);
                }
            }
            if (moveTopAction != null) {
                popup.add(moveTopAction);
                popup.add(moveBottomAction);
                popup.add(moveUpAction);
                popup.add(moveDownAction);
            }
            if (popup.getComponentCount() > 0) {
                popup.show(this, e.getX(), e.getY());
            }
            e.consume();
        }
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public JComponent getExpansion() {

        List<Action> flatActions = new ArrayList<Action>();
        for (Object o : actions) {
            if (o instanceof Action) {
                flatActions.add((Action) o);
            } else {
                Object[] oa = (Object[]) o;
                for (int j = 0; j < oa.length; ++j) {
                    flatActions.add((Action) oa[j]);
                }
            }
        }


        final ToolBox tb =
            new ToolBox(2, flatActions.size() / 2 + flatActions.size() % 2, true);
        for (int i = 0; i < flatActions.size() / 2 + flatActions.size() % 2; ++i) {
            tb.add((Action) flatActions.get(i));
        }
        if (moveUpAction != null) {
            tb.add(moveUpAction);
        }
        if (moveTopAction != null) {
            tb.add(moveTopAction);
        }
        if (flatActions.size() % 2 == 1) {
            tb.add(new JPanel());
        }
        for (int i = flatActions.size() / 2 + flatActions.size() % 2; i < flatActions.size(); ++i) {
            tb.add((Action) flatActions.get(i));
        }
        if (moveDownAction != null) {
            tb.add(moveDownAction);
        }
        if (moveBottomAction != null) {
            tb.add(moveBottomAction);
        }

        JComponent expander = new JPanel(
                new FlowLayout(FlowLayout.RIGHT));

        expander.add(tb);

        return expander;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public boolean isExpandable() {
        return expandable;
    }

    /**
     * Remove all the listeners that were added in the constructor
     */
    public void removeNotify() {
        if (!readonly) {
            getList().removeListSelectionListener(this);
            getList().removeListSelectionListener(deleteAction);
            if (moveUpAction != null) {
                getList().removeListSelectionListener(moveUpAction);
                getList().removeListSelectionListener(moveDownAction);
                getList().removeListSelectionListener(moveTopAction);
                getList().removeListSelectionListener(moveBottomAction);
                getList().removeMouseListener(this);
            }
        }
        this.removeMouseListener(this);
        getModel().removeListDataListener(this);
        if (getModel() instanceof UMLModelElementListModel) {
            ((UMLModelElementListModel) getModel()).removeModelEventListener();
        } else {
            ((SimpleListModel) getModel()).removeModelEventListener();
        }
    }


    /**
     * Add a listener for selection changes to the list
     * @param listener the listener
     */
    public void addListSelectionListener(ListSelectionListener listener) {
        scroll.getList().addListSelectionListener(listener);
    }

    /**
     * Add a listener for selection changes to the list
     * @param listener the listener
     */
    public void removeListSelectionListener(ListSelectionListener listener) {
        scroll.getList().removeListSelectionListener(listener);
    }

    /**
     * Get the JList that is wrapped in this component
     * @return the JList
     */
    JList getList() {
        return scroll.getList();
    }

    /**
     * Clear all selections in the list
     */
    public void clearSelection() {
        getList().clearSelection();
    }

    /**
     * Get the list model
     * @return the ListModel
     */
    private ListModel getModel() {
        return (ListModel) scroll.getList().getModel();
    }


    public void contentsChanged(ListDataEvent e) {
    }

    public void intervalAdded(ListDataEvent e) {
        if (e.getIndex0() == e.getIndex1()
                && getModel().getElementAt(e.getIndex0())
                == movedModelElement.getElement()) {
            LOG.log(Level.INFO, "Setting attribute to selected");
            final Object element = movedModelElement.getElement();
            movedModelElement.setElement(null);
            getList().setSelectedValue(element, true);
            // Pushing this to the end of the AWT thread seems to be the only
            // way to get this to update correctly
            Runnable doWorkRunnable = new Runnable() {
                public void run() {
                    getList().setSelectedValue(element, true);
                }
            };
            SwingUtilities.invokeLater(doWorkRunnable);
        }
    }

    public void intervalRemoved(ListDataEvent e) {
    }


    public void valueChanged(ListSelectionEvent e) {
        if (removeAction != null) {
            removeAction.setEnabled(getList().getSelectedIndex() > -1);
        }
    }


    /**
     * This action deletes the model elements that are selected in the JList
     */
    private class DeleteAction extends UndoableAction
            implements ListSelectionListener {

        /**
         * The class uid
         */
        private static final long serialVersionUID = -1466007194555518247L;

        /**
         * Construct the Action
         */
        public DeleteAction() {
            super(Translator.localize("menu.popup.delete"),
                    ResourceLoaderWrapper.lookupIconResource("DeleteFromModel"));
            setEnabled(false);
        }

        /**
         * Set the action as enabled when any row is selected
         * @param e the event
         */
        public void valueChanged(ListSelectionEvent e) {
            setEnabled(getList().getSelectedIndex() > -1);
        }

        /***
         * Perform the action
         * @param e the event
         */
        public void actionPerformed(ActionEvent ae) {
            super.actionPerformed(ae);
            // TODO Part of this is copied from ActionDeleteModelElement. We
            // maybe need some subclass for common code.
            KeyboardFocusManager focusManager =
                KeyboardFocusManager.getCurrentKeyboardFocusManager();
            Component focusOwner = focusManager.getFocusOwner();
            if (focusOwner instanceof FigTextEditor) {
                // TODO: Probably really want to cancel editing
                //((FigTextEditor) focusOwner).cancelEditing();
                ((FigTextEditor) focusOwner).endEditing();
            } else if (focusOwner instanceof JTable) {
                JTable table = (JTable) focusOwner;
                if (table.isEditing()) {
                    TableCellEditor ce = table.getCellEditor();
                    if (ce != null) {
                        ce.cancelCellEditing();
                    }
                }
            }

            final Project p = ProjectManager.getManager().getCurrentProject();
            final Object[] selectedValues = getList().getSelectedValues();
            p.moveToTrash(Arrays.asList(selectedValues));
        }
    }

    /**
     * This action deletes the model elements that are selected in the JList
     */
    private class MoveUpAction extends UndoableAction implements ListSelectionListener {

        /**
         * The class uid
         */
        private static final long serialVersionUID = 92834374054221267L;

        /**
         * Construct the action
         */
        MoveUpAction() {
            super(Translator.localize("menu.popup.moveup"),
                    ResourceLoaderWrapper.lookupIconResource("MoveUp"));
            setEnabled(false);
        }

        /**
         * Set the action as enabled when any row other then the first is selected
         * @param e the event
         */
         public void valueChanged(ListSelectionEvent e) {
            setEnabled(getList().getSelectedIndex() > 0);
        }

         /***
          * Perform the action
          * @param e the event
          */
        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            movedModelElement.setElement(getList().getSelectedValues()[0]);
            assert (movedModelElement != null);
            Model.getUmlHelper().move(
                    target,
                    movedModelElement.getElement(),
                    UmlHelper.Direction.UP);
        }
    }


    /**
     * This action deletes the model elements that are selected in the JList
     */
    private class MoveDownAction extends UndoableAction
            implements ListSelectionListener {

        /**
         * The class uid
         */
        private static final long serialVersionUID = -4898882853644454510L;

        /**
         * Construct the action
         */
        MoveDownAction() {
            super(Translator.localize("menu.popup.movedown"),
                    ResourceLoaderWrapper.lookupIconResource("MoveDown"));
            setEnabled(false);
        }

        /**
         * Set the action as enabled when any row other then the last is selected
         * @param e the event
         */
        public void valueChanged(ListSelectionEvent e) {
            final int index = getList().getSelectedIndex();
            setEnabled(index > -1 && index < getModel().getSize() - 1);
        }

        /***
         * Perform the action
         * @param e the event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            movedModelElement.setElement(getList().getSelectedValues()[0]);
            assert (movedModelElement != null);
            Model.getUmlHelper().move(
                    target,
                    movedModelElement.getElement(),
                    UmlHelper.Direction.DOWN);
        }
    }


    /**
     * This action deletes the model elements that are selected in the JList
     */
    private class MoveTopAction extends UndoableAction
            implements ListSelectionListener {

        /**
         * The class uid
         */
        private static final long serialVersionUID = -2767622732024791396L;

        /**
         * Construct the action
         */
        MoveTopAction() {
            super(Translator.localize("menu.popup.movetotop"),
                    ResourceLoaderWrapper.lookupIconResource("MoveTop"));
            setEnabled(false);
        }

        /**
         * Set the action as enabled when any row other then the first is selected
         * @param e the event
         */
        public void valueChanged(ListSelectionEvent e) {
            setEnabled(getList().getSelectedIndex() > 0);
        }

        /***
         * Perform the action
         * @param e the event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            movedModelElement.setElement(getList().getSelectedValues()[0]);
            assert (movedModelElement != null);
            Model.getUmlHelper().move(
                    target,
                    movedModelElement.getElement(),
                    UmlHelper.Direction.TOP);
        }
    }


    /**
     * This action moves up the model elements that are selected in the JList
     */
    private class MoveBottomAction extends UndoableAction
            implements ListSelectionListener {

        /**
         * The class uid
         */
        private static final long serialVersionUID = -3459350012282215204L;

        /**
         * Construct the action
         */
        MoveBottomAction() {
            super(Translator.localize("menu.popup.movetobottom"),
                    ResourceLoaderWrapper.lookupIconResource("MoveBottom"));
            setEnabled(false);
        }

        /**
         * Set the action as enabled when any row other then the last is selected
         * @param e the event
         */
        public void valueChanged(ListSelectionEvent e) {
            final int index = getList().getSelectedIndex();
            setEnabled(index > -1 && index < getModel().getSize() - 1);
        }

        /***
         * Perform the action
         * @param e the event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            movedModelElement.setElement(getList().getSelectedValues()[0]);
            assert (movedModelElement != null);
            Model.getUmlHelper().move(
                    target,
                    movedModelElement.getElement(),
                    UmlHelper.Direction.BOTTOM);
        }
    }

    private class MovedModelElement {
        private Object element;

        public Object getElement() {
            return element;
        }

        public void setElement(Object element) {
            LOG.log(Level.INFO, "Setting moved model element to {0}", element);
            this.element = element;
        }
    }

    private static class AddAction extends UndoableAction {

        private Command command;

        public AddAction(Command command) {
            super("", ResourceLoaderWrapper.lookupIcon("Add"));
            this.command = command;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            command.execute();
        }
    }

    private static class RemoveAction extends UndoableAction {

        private final SimpleListModel simpleListModel;
        private final JList list;

        public RemoveAction(JList list, SimpleListModel model) {
            super("", ResourceLoaderWrapper.lookupIcon("Remove"));
            this.simpleListModel = model;
            this.list = list;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            final Object objectToRemove = list.getSelectedValue();
            if (objectToRemove!= null) {
                Command command =
                    simpleListModel.getRemoveCommand(objectToRemove);
                command.execute();
            } else {
                LOG.log(Level.WARNING,
                        "No selected object was found in the list control - "
                        + "we shouldn't be able to get here");
            }
        }
    }

    private static class CommandAction extends UndoableAction {

        private final Command command;

        public CommandAction(Command cmd) {
            super("", ResourceLoaderWrapper.lookupIcon("Remove"));
            this.command = cmd;
        }

        public CommandAction(Command cmd, String name, Icon icon) {
            super(name, icon);
            this.command = cmd;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            command.execute();
        }
    }
}
