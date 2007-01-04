// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectSettings;
import org.argouml.ui.DisplayTextTree;
import org.argouml.ui.ProjectBrowser;
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
     * Creates a new instance of ExplorerTree.
     */
    public ExplorerTree() {
        super();

        // issue 2261: we must add this project property change first
        // in order to receive the new project event after
        // the ExplorerEventAdaptor
        //(which is initialised in the ExplorerTreeModel).
        ProjectManager.getManager()
            .addPropertyChangeListener(new ProjectPropertyChangeListener());

        this.setModel(new ExplorerTreeModel(ProjectManager.getManager()
			                    .getCurrentProject(), this));
        this.addMouseListener(new ExplorerMouseListener(this));
        //this.addTreeSelectionListener(new ExplorerTreeSelectionListener());
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
         * @see java.awt.event.MouseListener#mousePressed(
         *         java.awt.event.MouseEvent)
         *
         * Brings up the pop-up menu.
         */
        public void mousePressed(MouseEvent me) {
            if (me.isPopupTrigger()) {
                me.consume();
                showPopupMenu(me);
            }
        }

        /**
         * @see java.awt.event.MouseListener#mouseReleased(
         *         java.awt.event.MouseEvent)
         *
         * Brings up the pop-up menu.
         *
         * On Windows and Motif platforms, the user brings up a popup menu
         * by releasing the right mouse button while the cursor is over a
         * component that is popup-enabled.
         * 
         */
        public void mouseReleased(MouseEvent me) {
            if (me.isPopupTrigger()) {
                me.consume();
                showPopupMenu(me);
            }
        }

        /**
         * @see java.awt.event.MouseListener#mouseClicked(
         *         java.awt.event.MouseEvent)
         *
         * Brings up the pop-up menu.
         */
        public void mouseClicked(MouseEvent me) {
            if (me.isPopupTrigger()) {
                me.consume();
                showPopupMenu(me);
            }
            TreePath selPath = mLTree.getPathForLocation(me.getX(), me.getY());
            
            if (selPath != null) {
                
                Object selectedItem =
                    ((DefaultMutableTreeNode) selPath.getLastPathComponent())
                            .getUserObject();
                
                Collection currentTargets =
                    TargetManager.getInstance().getTargets();
                boolean selected =
                    currentTargets.contains(selectedItem);
                if (me.getModifiersEx() == MouseEvent.CTRL_DOWN_MASK) {
                    if (selected) {
                        TargetManager.getInstance().removeTarget(selectedItem);
                    } else {
                        TargetManager.getInstance().addTarget(selectedItem);
                    }
                } else if (!selected || currentTargets.size() > 1) {
                    TargetManager.getInstance().setTarget(selectedItem);
                }
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
                Vector show = new Vector();
                show.add(target);
                ProjectBrowser.getInstance().jumpToDiagramShowing(show);
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
        setSelection(targets.toArray());
    }

    /**
     * Sets the selection state for a given set of targets.
     *
     * @param targets the targets
     */
    private void setSelection(Object[] targets) {
        this.clearSelection();
        int rows = getRowCount();
        for (int i = 0; i < targets.length; i++) {
            Object target = targets[i];
            if (target instanceof Fig) {
                target = ((Fig) target).getOwner();
            }
            for (int j = 0; j < rows; j++) {
                Object rowItem =
		    ((DefaultMutableTreeNode) getPathForRow(j)
		            .getLastPathComponent()).getUserObject();
                if (rowItem == target) {
                    this.addSelectionRow(j);
                }
            }
        }

        if (this.getSelectionCount() > 0) {
            scrollRowToVisible(this.getSelectionRows()[0]);
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
                Object[] targets = e.getAddedTargets();

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
                            addSelectionRow(j);
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
                            removeSelectionRow(j);
                        }
                    }
                }

                if (getSelectionCount() > 0) {
                    scrollRowToVisible(getSelectionRows()[0]);
                }
                updatingSelection = false;
            }
        }

        /*
         * @see org.argouml.ui.targetmanager.TargetListener#targetSet(
	 *         org.argouml.ui.targetmanager.TargetEvent)
         */
        public void targetSet(TargetEvent e) {
            setTargets(e.getNewTargets());

        }
    }

    class ProjectPropertyChangeListener implements PropertyChangeListener {

        /**
         * @see java.beans.PropertyChangeListener#propertyChange(
         *         java.beans.PropertyChangeEvent)
         *
         * Listens to events coming from the project manager,
         * i.e. when the current project changes,
         * in order to expand the root node by default.
         */
        public void propertyChange(java.beans.PropertyChangeEvent pce) {

            // project events
            if (pce.getPropertyName()
                    .equals(ProjectManager.CURRENT_PROJECT_PROPERTY_NAME)) {

                TreeModel model = getModel();

                if (model != null && model.getRoot() != null) {

                    expandPath(getPathForRow(0));

                }
            }
        }
    }


    /**
     * The UID.
     */
    private static final long serialVersionUID = 992867483644759920L;
}
