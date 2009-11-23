// $Id: UMLSingleRowSelector.java 15914 2008-10-13 17:10:00Z bobtarling $
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
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListModel;
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
        implements MouseListener, ContainerListener {
    
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
     * The label that contains the +/- symbol to indicate
     * expansion feature to user.
     */
    private final JLabel expander;
    
    /**
     * The toolbar of controls for manipulating items in the list
     */
    private final JToolBar tb;
    
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
    public RowSelector(UMLModelElementListModel model) {
        this(model, false, true);
        
    }
    /**
     * Constructor
     * @param model The single item list model
     * @param singleRow true if we only ever want a single row
     */
    public RowSelector(UMLModelElementListModel model, boolean expanded, boolean expandable) {
        super(new BorderLayout());
        
        this.expandable = expandable;
        
        Object target = model.getTarget();
        Object metaType = model.getMetaType();
        
        LOG.info("model = " + model.getClass().getName());
        LOG.info("metatype = " + metaType);
        LOG.info("target = " + target);
        
        scroll = new ScrollList(model, 1);
        add(scroll);
        
        shrunkPreferredSize = scroll.getPreferredSize();
        
        remove(scroll);
        scroll = new ScrollList(model);
        
        add(scroll);
        expandedPreferredSize = scroll.getPreferredSize();
        expandedMaximumSize = scroll.getMaximumSize();
        
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        if (!expandable && !expanded) {
            scroll.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            expander = null;
            tb = null;
            deleteAction = null;
            moveUpAction = null;
            moveDownAction = null;
            moveTopAction = null;
            moveBottomAction = null;
        } else {
            // Create actions and expander if we have multiple rows
            final ArrayList<Action> actions = new ArrayList<Action>(2);
            
            if (Model.getUmlFactory().isContainmentValid(metaType, target)) {
                final Action createAction = new ActionCreateContainedModelElement(
                        metaType,
                        target,
                        "button.new-" + Model.getMetaTypes().getName(metaType).toLowerCase());
                actions.add(createAction);
            }
            deleteAction = new DeleteAction();
            actions.add(deleteAction);
            
            if (getModel() instanceof UMLModelElementOrderedListModel) {
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
            tb = tbf.createToolBar();
            tb.setRollover(true);
            tb.setOrientation(ToolBar.VERTICAL);
            
            JPanel buttonPanel =
                new JPanel(new FlexiGridLayout(2, 1, FlexiGridLayout.ROWCOLPREFERRED));
            expander = new JLabel();
            this.addMouseListener(this);
            setIcon();
            buttonPanel.add(expander);
            if (tb != null) {
                tb.setVisible(false);
                buttonPanel.add(tb);
            }
            add(buttonPanel, BorderLayout.WEST);

            getList().addListSelectionListener(deleteAction);
            // TODO: We should really test the model instead for this
            // but we have no API yet.
            // Can we just check if the collection to build the JList
            // control implements the List interface?
            if (getModel() instanceof UMLModelElementOrderedListModel) {
                getList().addListSelectionListener(moveUpAction);
                getList().addListSelectionListener(moveDownAction);
                getList().addListSelectionListener(moveTopAction);
                getList().addListSelectionListener(moveBottomAction);
            }
            
            addContainerListener(this);
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
    
    @Override
    public void mouseClicked(MouseEvent e) {
        toggleExpansion();
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Toggle between expansion and contraction of the control
     */
    private void toggleExpansion() {
        expanded = !expanded;
        
        setIcon();
        if (tb != null) {
            tb.setVisible(expanded);
        }
        
        // Froce the parent to redraw
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
    
    @Override
    public void componentAdded(ContainerEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    /**
     * Remove all the listeners that were added in the constructor
     */
    @Override
    public void componentRemoved(ContainerEvent event) {
        getList().removeListSelectionListener(deleteAction);
        if (moveUpAction != null) {
            getList().removeListSelectionListener(moveUpAction);
            getList().removeListSelectionListener(moveDownAction);
            getList().removeListSelectionListener(moveTopAction);
            getList().removeListSelectionListener(moveBottomAction);
        }
        this.removeMouseListener(this);
        this.removeContainerListener(this);
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
    
    public JList getList() {
        return scroll.getList();
    }
    
    private ListModel getModel() {
        return (ListModel) scroll.getList().getModel();
    }
    
    /**
     * This action deletes the model elements that are selected in the JList
     */
    private class DeleteAction extends UndoableAction implements ListSelectionListener {
        
        DeleteAction() {
            super("button.delete",
                    ResourceLoaderWrapper.getInstance().lookupIconResource("DeleteFromModel"));
            setEnabled(false);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            setEnabled(getList().getSelectedIndex() > -1);
        }
        
        /*
         * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
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

            Project p = ProjectManager.getManager().getCurrentProject();
            Object[] targets = getList().getSelectedValues();
            p.moveToTrash(Arrays.asList(targets));
        }
    }
    
    /**
     * This action deletes the model elements that are selected in the JList
     */
    private class MoveUpAction extends UndoableAction implements ListSelectionListener {
        
        MoveUpAction() {
            super(Translator.localize("menu.popup.moveup"),
                    ResourceLoaderWrapper.lookupIconResource("MoveUp"));
            setEnabled(false);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            setEnabled(getList().getSelectedIndex() > -1);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            Model.getUmlHelper().move(getList().getSelectedValues()[0], UmlHelper.Direction.UP);
        }
    }
    
    
    /**
     * This action deletes the model elements that are selected in the JList
     */
    private class MoveDownAction extends UndoableAction implements ListSelectionListener {
        
        MoveDownAction() {
            super(Translator.localize("menu.popup.movedown"),
                    ResourceLoaderWrapper.lookupIconResource("MoveDown"));
            setEnabled(false);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            setEnabled(getList().getSelectedIndex() > -1);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            Model.getUmlHelper().move(getList().getSelectedValues()[0], UmlHelper.Direction.DOWN);
        }
    }
    
    
    /**
     * This action deletes the model elements that are selected in the JList
     */
    private class MoveTopAction extends UndoableAction implements ListSelectionListener {
        
        MoveTopAction() {
            super(Translator.localize("menu.popup.movetop"),
                    ResourceLoaderWrapper.lookupIconResource("MoveTop"));
            setEnabled(false);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            setEnabled(getList().getSelectedIndex() > -1);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            Model.getUmlHelper().move(getList().getSelectedValues()[0], UmlHelper.Direction.TOP);
        }
    }
    
    
    /**
     * This action deletes the model elements that are selected in the JList
     */
    private class MoveBottomAction extends UndoableAction implements ListSelectionListener {
        
        MoveBottomAction() {
            super(Translator.localize("menu.popup.movebottom"),
                    ResourceLoaderWrapper.lookupIconResource("MoveBottom"));
            setEnabled(false);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            setEnabled(scroll.getList().getSelectedIndex() > -1);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            Model.getUmlHelper().move(getList().getSelectedValues()[0], UmlHelper.Direction.BOTTOM);
        }
    }
    
}
