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

package org.argouml.ui.explorer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectSettings;
import org.argouml.ui.DisplayTextTree;
import org.argouml.ui.ProjectActions;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.presentation.Fig;

/**
 * This class is the JTree for the explorer. It provides:<p>
 * <pre>
 *  - selection/target management
 *  - mouse listener for the pop up </pre>
 *
 * @author  alexb
 * @since 0.15.2
 */
public class ExplorerTree
    extends DisplayTextTree {


    /**
     * Prevents target event cycles between this and the TargetManager.
     */
    private boolean updatingSelection;

    /**
     * Prevents target event cycles between this and the TargetManager
     * for tree selection events.
     */
    private boolean updatingSelectionViaTreeSelection;

    /**
     * Creates a new instance of ExplorerTree.
     */
    public ExplorerTree() {
        super();
        this.setModel(new ExplorerTreeModel(ProjectManager.getManager()
			                    .getCurrentProject(), this));
        this.addMouseListener(new ExplorerMouseListener(this));
        this.addTreeSelectionListener(new ExplorerTreeSelectionListener());
        this.addTreeWillExpandListener(new ExplorerTreeWillExpandListener());
        this.addTreeExpansionListener(new ExplorerTreeExpansionListener());

        TargetManager.getInstance()
	    .addTargetListener(new ExplorerTargetListener());
    }

    /**
     * Listens to mouse events coming from the *JTree*,
     * on right click, brings up the pop-up menu.
     */
    class ExplorerMouseListener extends MouseAdapter {

        private JTree mLTree;

        /**
         * The constructor.
         * @param newtree
         */
        public ExplorerMouseListener(JTree newtree) {
            super();
            mLTree = newtree;
        }

        /**
         * Brings up the pop-up menu.
         * 
         * @see java.awt.event.MouseListener#mousePressed(
         *      java.awt.event.MouseEvent)
         */
        @Override
        public void mousePressed(MouseEvent me) {
            if (me.isPopupTrigger()) {
                me.consume();
                showPopupMenu(me);
            }
        }

        /**
         * Brings up the pop-up menu.
         * 
         * On Windows and Motif platforms, the user brings up a popup menu by
         * releasing the right mouse button while the cursor is over a component
         * that is popup-enabled.
         * 
         * @see java.awt.event.MouseListener#mouseReleased(
         *      java.awt.event.MouseEvent)
         */
        @Override
        public void mouseReleased(MouseEvent me) {
            if (me.isPopupTrigger()) {
                me.consume();
                showPopupMenu(me);
            }
        }

        /**
         * Brings up the pop-up menu.
         * 
         * @see java.awt.event.MouseListener#mouseClicked(
         *      java.awt.event.MouseEvent)
         */
        @Override
        public void mouseClicked(MouseEvent me) {
            if (me.isPopupTrigger()) {
                me.consume();
                showPopupMenu(me);
            }
            if (me.getClickCount() >= 2) {
                myDoubleClick();
            }
        }

        /**
         * Double-clicking on an item attempts
         * to show the item in a diagram.
         */
        private void myDoubleClick() {
            Object target = TargetManager.getInstance().getTarget();
            if (target != null) {
                List show = new ArrayList();
                show.add(target);
                ProjectActions.jumpToDiagramShowing(show);
            }
        }

        /**
         * Builds a pop-up menu for extra functionality for the Tree.
         *
         * @param me The mouse event.
         */
        public void showPopupMenu(MouseEvent me) {

            TreePath path = getPathForLocation(me.getX(), me.getY());
            if (path == null) {
                return;
            }

            /*
             * We preserve the current (multiple) selection,
             * if we are over part of it ...
             */
            if (!isPathSelected(path)) {
                /* ... otherwise we select the item below the mousepointer. */
                getSelectionModel().setSelectionPath(path);
            }

            Object selectedItem =
                ((DefaultMutableTreeNode) path.getLastPathComponent())
                        .getUserObject();
            JPopupMenu popup = new ExplorerPopup(selectedItem, me);

            if (popup.getComponentCount() > 0) {
                popup.show(mLTree, me.getX(), me.getY());
            }
        }

    } /* end class ExplorerMouseListener */


    /**
     * Helps prepare state before a node is expanded.
     */
    class ExplorerTreeWillExpandListener implements TreeWillExpandListener {

        /*
         * @see javax.swing.event.TreeWillExpandListener#treeWillCollapse(javax.swing.event.TreeExpansionEvent)
         */
        public void treeWillCollapse(TreeExpansionEvent tee) {
            // unimplemented - we only care about expanding
	}

        /*
         * Updates stereotype setting,
         * adds all children per treemodel 'build on demand' design.
         *
         * @see javax.swing.event.TreeWillExpandListener#treeWillExpand(javax.swing.event.TreeExpansionEvent)
         */
        public void treeWillExpand(TreeExpansionEvent tee) {
            // TODO: This should not need to know about ProjectSettings - tfm
            Project p = ProjectManager.getManager().getCurrentProject();
            ProjectSettings ps = p.getProjectSettings();
            setShowStereotype(ps.getShowStereotypesValue());

            if (getModel() instanceof ExplorerTreeModel) {
                ((ExplorerTreeModel) getModel()).updateChildren(tee.getPath());
            }
        }
    }

    /**
     * Helps react to tree expansion events.
     */
    class ExplorerTreeExpansionListener implements TreeExpansionListener {

        /*
         * @see javax.swing.event.TreeExpansionListener#treeCollapsed(
         *         javax.swing.event.TreeExpansionEvent)
         */
        public void treeCollapsed(TreeExpansionEvent event) {
            // does nothing.
        }

        /*
         * @see javax.swing.event.TreeExpansionListener#treeExpanded(
         *         javax.swing.event.TreeExpansionEvent)
         * Updates the selection state.
         */
        public void treeExpanded(TreeExpansionEvent event) {

            // need to update the selection state.
            setSelection(TargetManager.getInstance().getTargets().toArray());
        }
    }

    /**
     * Refresh the selection of the tree nodes.
     * This does not cause new events to be fired to the TargetManager.
     */
    public void refreshSelection() {
        Collection targets = TargetManager.getInstance().getTargets();
        updatingSelectionViaTreeSelection = true;
        setSelection(targets.toArray());
        updatingSelectionViaTreeSelection = false;
    }

    /**
     * Sets the selection state for a given set of targets.
     *
     * @param targets the targets
     */
    private void setSelection(Object[] targets) {
        updatingSelectionViaTreeSelection = true;
        this.clearSelection();
        addTargetsInternal(targets);
        updatingSelectionViaTreeSelection = false;
    }

    private void addTargetsInternal(Object[] addedTargets) {
        if (addedTargets.length < 1) {
            return;
        }
        Set targets = new HashSet();
        for (Object t : addedTargets) {
            if (t instanceof Fig) {
                targets.add(((Fig) t).getOwner());
            } else {
                targets.add(t);
            }
        }

        ExplorerTreeModel model = (ExplorerTreeModel) getModel();
        ExplorerTreeNode root = (ExplorerTreeNode) model.getRoot();
        
        
        selectChildren(model, root, targets);
    
        int[] selectedRows = getSelectionRows();
        if (selectedRows != null && selectedRows.length > 0) {
            // TODO: This only works if the item is visible
            // (all its parents are expanded)
            // getExpandedDescendants, makeVisible
            makeVisible(getPathForRow(selectedRows[0]));
            scrollRowToVisible(selectedRows[0]);
        }
    }

    /*
     * Perform recursive search of subtree rooted at 'node', selecting all nodes which 
     * have a userObject matching one of our targets.
     */
    private void selectChildren(ExplorerTreeModel model, ExplorerTreeNode node, Set targets) {
        if (targets.isEmpty()) {
            return;
        }
        Object nodeObject = node.getUserObject();
        if (nodeObject != null) {
            for (Object t : targets) {
                if (t == nodeObject) {
                    updatingSelectionViaTreeSelection = true;
                    addSelectionPath(new TreePath(node.getPath()));
                    updatingSelectionViaTreeSelection = false;
                    // target may appear multiple places in the tree, so 
                    // we don't stop here (but it's expensive to search
                    // the whole tree) - tfm - 20070904
//                  targets.remove(t);
//                  break;
                }
            }
        }

        model.updateChildren(new TreePath(node.getPath()));
        Enumeration e = node.children();
        while (e.hasMoreElements()) {
            selectChildren(model, (ExplorerTreeNode) e.nextElement(), targets);
        }

    }

    /**
     * Manages selecting the item to show in Argo's other
     * views based on the highlighted row.
     */
    class ExplorerTreeSelectionListener implements TreeSelectionListener {

        /**
         * @see javax.swing.event.TreeSelectionListener#valueChanged(
         *         javax.swing.event.TreeSelectionEvent)
         *
         * Change in explorer tree selection -> set target in target manager.
         */
        public void valueChanged(TreeSelectionEvent e) {

            if (!updatingSelectionViaTreeSelection) {
                updatingSelectionViaTreeSelection = true;

                // get the elements
                TreePath[] addedOrRemovedPaths = e.getPaths();
                TreePath[] selectedPaths = getSelectionPaths();
                List elementsAsList = new ArrayList();
                for (int i = 0;
                    selectedPaths != null && i < selectedPaths.length; i++) {
                    Object element =
                        ((DefaultMutableTreeNode)
                                selectedPaths[i].getLastPathComponent())
                                .getUserObject();
                    elementsAsList.add(element);
                    // scan the visible rows for duplicates of
                    // this elem and select them
                    int rows = getRowCount();
                    for (int row = 0; row < rows; row++) {
                        Object rowItem =
			    ((DefaultMutableTreeNode) getPathForRow(row)
			            .getLastPathComponent())
			            .getUserObject();
                        if (rowItem == element
			    && !(isRowSelected(row))) {
                            addSelectionRow(row);
                        }
                    }
                }

                // check which targetmanager method to call
                boolean callSetTarget = true;
                List addedElements = new ArrayList();
                for (int i = 0; i < addedOrRemovedPaths.length; i++) {
                    Object element =
                        ((DefaultMutableTreeNode)
                            addedOrRemovedPaths[i].getLastPathComponent())
                            .getUserObject();
                    if (!e.isAddedPath(i)) {
                        callSetTarget = false;
                        break;
                    }
                    addedElements.add(element);
                }

                if (callSetTarget && addedElements.size()
                        == elementsAsList.size()
                        && elementsAsList.containsAll(addedElements)) {
                    TargetManager.getInstance().setTargets(elementsAsList);
                } else {
                    // we must call the correct method on targetmanager
                    // for each added or removed target
                    List removedTargets = new ArrayList();
                    List addedTargets = new ArrayList();
                    for (int i = 0; i < addedOrRemovedPaths.length; i++) {
                        Object element =
                            ((DefaultMutableTreeNode)
                                addedOrRemovedPaths[i]
                                        .getLastPathComponent())
                                .getUserObject();
                        if (e.isAddedPath(i)) {
                            addedTargets.add(element);
                        } else {
                            removedTargets.add(element);
                        }
                    }
                    // we can't remove the targets in one go, we have to
                    // do it one by one.
                    if (!removedTargets.isEmpty()) {
                        Iterator it = removedTargets.iterator();
                        while (it.hasNext()) {
                            TargetManager.getInstance().removeTarget(it.next());
                        }
                    }
                    if (!addedTargets.isEmpty()) {
                        Iterator it = addedTargets.iterator();
                        while (it.hasNext()) {
                            TargetManager.getInstance().addTarget(it.next());
                        }
                    }
                }

                updatingSelectionViaTreeSelection = false;
            }
        }
    }

    class ExplorerTargetListener implements TargetListener {

        /**
         * Actions a change in targets received from the TargetManager.
         *
         * @param targets the targets
         */
        private void setTargets(Object[] targets) {

            if (!updatingSelection) {
                updatingSelection = true;
                if (targets.length <= 0) {
                    clearSelection();
                } else {
                    setSelection(targets);
                }
                updatingSelection = false;
            }
        }

        /*
         * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(
	 *         org.argouml.ui.targetmanager.TargetEvent)
         */
        public void targetAdded(TargetEvent e) {
            if (!updatingSelection) {
                updatingSelection = true;
                Object[] addedTargets = e.getAddedTargets();
                addTargetsInternal(addedTargets);
                updatingSelection = false;
            }
            // setTargets(e.getNewTargets());
        }

        /*
         * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(
	 *         org.argouml.ui.targetmanager.TargetEvent)
         */
        public void targetRemoved(TargetEvent e) {
            if (!updatingSelection) {
                updatingSelection = true;

                Object[] targets = e.getRemovedTargets();

                int rows = getRowCount();
                for (int i = 0; i < targets.length; i++) {
                    Object target = targets[i];
                    if (target instanceof Fig) {
                        target = ((Fig) target).getOwner();
                    }
                    for (int j = 0; j < rows; j++) {
                        Object rowItem =
                            ((DefaultMutableTreeNode)
                                    getPathForRow(j).getLastPathComponent())
                            .getUserObject();
                        if (rowItem == target) {
                            updatingSelectionViaTreeSelection = true;
                            removeSelectionRow(j);
                            updatingSelectionViaTreeSelection = false;
                        }
                    }
                }

                if (getSelectionCount() > 0) {
                    scrollRowToVisible(getSelectionRows()[0]);
                }
                updatingSelection = false;
            }
            // setTargets(e.getNewTargets());
        }

        /*
         * @see org.argouml.ui.targetmanager.TargetListener#targetSet(
	 *         org.argouml.ui.targetmanager.TargetEvent)
         */
        public void targetSet(TargetEvent e) {
            setTargets(e.getNewTargets());

        }
    }


    /**
     * The UID.
     */
    private static final long serialVersionUID = 992867483644759920L;
}
