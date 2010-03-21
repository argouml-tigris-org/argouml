/* $Id$
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */


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

package org.argouml.core.propertypanels.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.TreeUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.table.TableCellEditor;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.UmlHelper;
import org.argouml.ui.ActionCreateContainedModelElement;
import org.argouml.ui.UndoableAction;
import org.tigris.gef.presentation.FigTextEditor;
import org.tigris.swidgets.FlexiGridLayout;
import org.tigris.toolbar.ToolBar;
import org.tigris.toolbar.ToolBarFactory;

/**
 * A control for displaying the contents of a list model elements in a panel
 * that can be expanded to take maximum possible screen space or shrunk to
 * minimum at the user discretion.
 *
 * @author Bob Tarling
 * @since 0.29.2
 */
class RowSelector extends JPanel
        implements MouseListener, ListDataListener {

    /**
     * The logger
     */
    private static final Logger LOG = Logger.getLogger(RowSelector.class);

    /**
     * class uid
     */
    private static final long serialVersionUID = 3937183621483536749L;

    /**
     * The icon to use when the control is expanded
     */
    private static Icon expandedIcon;

    /**
     * The icon to use when the control is collapsed
     */
    private static Icon collapsedIcon;

    /**
     * The model element that is the target of this control
     */
    private final Object target;
    
    /**
     * Identifies if the model element is a readonly modelelement
     */
    private final boolean readonly;
    
    static {
        // Extract the icon that is used by the tree control
        // for the current look and feel
        final JTree dummyTree = new JTree();

        final TreeUI tu = dummyTree.getUI();

        if (tu instanceof BasicTreeUI) {
            final BasicTreeUI btu = (BasicTreeUI) tu;
            expandedIcon = btu.getExpandedIcon();
            collapsedIcon = btu.getCollapsedIcon();
        } else {
            // TODO: We want some default icons of our own here
            expandedIcon = null;
            collapsedIcon = null;
        }
    }

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
     * The label that contains the +/- symbol to indicate
     * expansion feature to user.
     */
    private final JLabel expander;

    /**
     * The toolbar of controls for manipulating items in the list
     */
    private final JToolBar toolbar;

    /**
     * The delete action that we must enable/disable
     */
    private final DeleteAction deleteAction;

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
    public RowSelector(DefaultListModel model, boolean expanded, boolean expandable) {
        super(new BorderLayout());
        
        this.expandable = expandable;
        Object metaType = null;
        List metaTypes = null;

        if (model instanceof UMLModelElementListModel) {
            // Temporary until SimpleListModel is used for all
            target = ((UMLModelElementListModel) model).getTarget();
            metaType = ((UMLModelElementListModel) model).getMetaType();
            scroll = new OldScrollList(model, 1);
            readonly = Model.getModelManagementHelper().isReadOnly(target);
        } else if (model instanceof org.argouml.core.propertypanels.ui.SimpleListModel) {
            target = ((org.argouml.core.propertypanels.ui.SimpleListModel) model).getUmlElement();
            metaType = ((org.argouml.core.propertypanels.ui.SimpleListModel) model).getMetaType();
            metaTypes = ((org.argouml.core.propertypanels.ui.SimpleListModel) model).getMetaTypes();
            scroll = new ScrollListImpl(model, 1);
            readonly = Model.getModelManagementHelper().isReadOnly(target);
        } else {
            // Temporary until SimpleListModel is used for all
            target = null;
            readonly = true;
        }
        
        assert (target != null);

        if (metaTypes == null) {
            metaTypes = new ArrayList();
            metaTypes.add(metaType);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating list for " + target);
            LOG.debug("model = " + model.getClass().getName());
            LOG.debug("metatype = " + metaType);
            LOG.debug("target = " + target);
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

        if (!expandable && !expanded) {
            jscroll.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            expander = null;
            toolbar = null;
            deleteAction = null;
            moveUpAction = null;
            moveDownAction = null;
            moveTopAction = null;
            moveBottomAction = null;
        } else {
            if (!readonly) {
        	// TODO: Lets build this into a separate buildToolbar method
        		
                // Create actions and expander if we have multiple rows
                final ArrayList<Action> actions = new ArrayList<Action>(6);

                for (Object meta : metaTypes) {
                    if (Model.getUmlFactory().isContainmentValid(meta, target)) {
                        final String label =
                            "button.new-" + Model.getMetaTypes().getName(meta).toLowerCase();
                        final Action createAction = new ActionCreateContainedModelElement(
                                meta,
                                target,
                                label);
                        actions.add(createAction);
                    }
                }
                deleteAction = new DeleteAction();
                actions.add(deleteAction);

                if (Model.getUmlHelper().isMovable(metaType)) {
                    moveUpAction = new MoveUpAction();
                    moveDownAction = new MoveDownAction();
                    moveTopAction = new MoveTopAction();
                    moveBottomAction = new MoveBottomAction();
                    actions.add(moveUpAction);
                    actions.add(moveDownAction);
                    actions.add(moveTopAction);
                    actions.add(moveBottomAction);
                } else {
                    moveUpAction = null;
                    moveDownAction = null;
                    moveTopAction = null;
                    moveBottomAction = null;
                }

                final ToolBarFactory tbf = new ToolBarFactory(actions);
                toolbar = tbf.createToolBar();
                toolbar.setRollover(true);
                toolbar.setOrientation(ToolBar.VERTICAL);
            } else {
                final ToolBarFactory tbf = new ToolBarFactory(new Object[] {});
                toolbar = tbf.createToolBar();
                toolbar.setRollover(true);
                toolbar.setOrientation(ToolBar.VERTICAL);
                moveUpAction = null;
                moveDownAction = null;
                moveTopAction = null;
                moveBottomAction = null;
                deleteAction = null;
            }

            JPanel buttonPanel =
                new JPanel(new FlexiGridLayout(2, 1, FlexiGridLayout.ROWCOLPREFERRED));
            expander = new JLabel();
            this.addMouseListener(this);
            setIcon();
            buttonPanel.add(expander);
            if (toolbar != null) {
                toolbar.setVisible(false);
                buttonPanel.add(toolbar);
            }
            add(buttonPanel, BorderLayout.WEST);

            if (!Model.getModelManagementHelper().isReadOnly(target)) {
                getList().addListSelectionListener(deleteAction);
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
        toggleExpansion();
    }

    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * Toggle between expansion and contraction of the control
     */
    private void toggleExpansion() {
        expanded = !expanded;

        setIcon();
        if (toolbar != null) {
            toolbar.setVisible(expanded);
        }

        // Force the parent to redraw
        getParent().invalidate();
        getParent().validate();
    }

    /**
     * Set the icon according to the current expansion setting
     */
    private void setIcon() {
        if (expanded) {
            expander.setIcon(expandedIcon);
        } else {
            expander.setIcon(collapsedIcon);
        }
    }

    /**
     * Remove all the listeners that were added in the constructor
     */
    public void removeNotify() {
        LOG.info("The RowSelector is being removed from a panel");
    	if (!readonly) {
	        getList().removeListSelectionListener(deleteAction);
	        if (moveUpAction != null) {
	            getList().removeListSelectionListener(moveUpAction);
	            getList().removeListSelectionListener(moveDownAction);
	            getList().removeListSelectionListener(moveTopAction);
	            getList().removeListSelectionListener(moveBottomAction);
	        }
    	}
        this.removeMouseListener(this);
        getModel().removeListDataListener(this);
        if (getModel() instanceof UMLModelElementListModel) {
            ((UMLModelElementListModel) getModel()).removeModelEventListener();
        } else {
            ((org.argouml.core.propertypanels.ui.SimpleListModel) getModel()).removeModelEventListener();
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
                && getModel().getElementAt(e.getIndex0()) == movedModelElement.getElement()) {
            LOG.info("Setting attribute to selected");
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

    /**
     * This action deletes the model elements that are selected in the JList
     */
    private class DeleteAction extends UndoableAction implements ListSelectionListener {

        /**
         * The class uid
         */
        private static final long serialVersionUID = -1466007194555518247L;

        /**
         * Construct the Action
         */
        public DeleteAction() {
            super("button.delete",
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
    private class MoveDownAction extends UndoableAction implements ListSelectionListener {

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
    private class MoveTopAction extends UndoableAction implements ListSelectionListener {

        /**
         * The class uid
         */
        private static final long serialVersionUID = -2767622732024791396L;

        /**
         * Construct the action
         */
        MoveTopAction() {
            super(Translator.localize("menu.popup.movetop"),
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
     * This action deletes the model elements that are selected in the JList
     */
    private class MoveBottomAction extends UndoableAction implements ListSelectionListener {

        /**
         * The class uid
         */
        private static final long serialVersionUID = -3459350012282215204L;

        /**
         * Construct the action
         */
        MoveBottomAction() {
            super(Translator.localize("menu.popup.movebottom"),
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
            LOG.info("Setting moved model element to " + element);
            this.element = element;
        }
    }
}
