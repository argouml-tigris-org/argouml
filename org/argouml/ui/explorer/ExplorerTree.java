// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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
import java.util.*;
import java.beans.PropertyChangeListener;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.argouml.application.api.Configuration;
import org.argouml.application.api.Notation;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.ui.DisplayTextTree;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.generator.GeneratorDisplay;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;


/**
 * This class takes a lot of the responsibility from the NavigatorPane.
 *
 * Provides:
 *  - selection/target management
 *  - generate a name for all nodes
 *  - mouse listener for the pop up
 *
 * @author  alexb
 * @since 0.15.2
 */
public class ExplorerTree
    extends DisplayTextTree {
    
    /**
     * Holds state info about whether to display stereotypes in the
     * nav pane.
     */
    private boolean showStereotype;
    
    /**
     * prevents target event cycles between this and the TargetManager.
     */
    private boolean updatingSelection;
    
    /**
     * prevents target event cycles between this and the Targetmanager for tree selection events
     */
    private boolean updatingSelectionViaTreeSelection;
    
    /** Creates a new instance of ExplorerTree */
    public ExplorerTree() {
        super();
        
        // issue 2261: we must add this project property change first
        // in order to receive the new project event after
        // the ExplorerEventAdaptor 
        //(which is initialised in the ExplorerTreeModel).
        ProjectManager.getManager()
            .addPropertyChangeListener(new ProjectPropertyChangeListener());
        
        this.setModel(new ExplorerTreeModel(ProjectManager.getManager()
					        .getCurrentProject()));
        this.addMouseListener(new NavigatorMouseListener(this));
        this.addTreeSelectionListener(new NavigationTreeSelectionListener());
        this.addTreeWillExpandListener(new ExplorerTreeWillExpandListener());
        this.addTreeExpansionListener(new ExplorerTreeExpansionListener());
        
        TargetManager.getInstance()
	    .addTargetListener(new ExplorerTargetListener());
        
        showStereotype =
	    Configuration.getBoolean(Notation.KEY_SHOW_STEREOTYPES, false);
        
    }
    
    /** Listens to mouse events coming from the *JTree*,
     * on right click, brings up the pop-up menu.
     */
    class NavigatorMouseListener extends MouseAdapter {
        
        JTree mLTree;
        
        public NavigatorMouseListener(JTree newtree) {
            super();
            mLTree = newtree;
        }
        
        /** brings up the pop-up menu */
        public void mousePressed(MouseEvent me) {
            if (me.isPopupTrigger()) {
                me.consume();
                showPopupMenu(me);
            }
        }
        
        /** brings up the pop-up menu
         *
         * <p>On Windows and Motif platforms, the user brings up a popup menu
         *    by releasing the right mouse button while the cursor is over a
         *    component that is popup-enabled.
         */
        public void mouseReleased(MouseEvent me) {
            if (me.isPopupTrigger()) {
                me.consume();
                showPopupMenu(me);
            }
        }
        
        /** brings up the pop-up menu */
        public void mouseClicked(MouseEvent me) {
            if (me.isPopupTrigger()) {
                me.consume();
                showPopupMenu(me);
            }
        }
        
        /** builds a pop-up menu for extra functionality for the Tree*/
        public void showPopupMenu(MouseEvent me) {
            
            if (getLastSelectedPathComponent() == null) {
                return;
	    }
            
            Object selectedItem = 
                ((DefaultMutableTreeNode) getLastSelectedPathComponent())
                        .getUserObject();
            JPopupMenu popup = new ExplorerPopup(selectedItem, me);
            
            if (popup.getComponentCount() > 0) {
                popup.show(mLTree, me.getX(), me.getY());
            }
        }
        
    } /* end class NavigatorMouseListener */
    
    /**
     * override default JTree implementation to display the
     * appropriate text for any object that will be displayed in
     * the Nav pane.
     */
    public String convertValueToText(Object value,
				     boolean selected,
				     boolean expanded,
				     boolean leaf,
				     int row,
				     boolean hasFocus) {
        
        // do model elements first
        if (ModelFacade.isAModelElement(value)) {
            
            String name = null;
            
            // Jeremy Bennett patch
            if (ModelFacade.isATransition(value)
		    || ModelFacade.isAExtensionPoint(value)) {
                name = GeneratorDisplay.Generate(value);
            }
            // changing the label in case of comments
            // this is necessary since the name of the comment is the same as
            // the content of the comment causing the total comment to be
            // displayed in the perspective
            else if (ModelFacade.isAComment(value)) {
                name = ModelFacade.getName(value);
                
                if (name != null
		    && name.indexOf("\n") < 80
		    && name.indexOf("\n") > -1) {
                        
                    name = name.substring(0, name.indexOf("\n")) + "...";
                }
                else if (name != null && name.length() > 80) {
                    name = name.substring(0, 80) + "...";
                }
            } else {
                name = ModelFacade.getName(value);
            }
            
            if (name == null || name.equals("")) {
                name = "(anon " + ModelFacade.getUMLClassName(value) + ")";
            }
            
            // Look for stereotype
            if (showStereotype) {
                Object stereo = null;
                if (ModelFacade.getStereotypes(value).size() > 0) {
                    stereo =
			ModelFacade.getStereotypes(value).iterator().next();
                }
                if (stereo != null) {
                    name += " " + GeneratorDisplay.Generate(stereo);
                }
            }
            
            return name;
        }
        
        if (ModelFacade.isATaggedValue(value)) {
            String tagName = ModelFacade.getTagOfTag(value);
            if (tagName == null || tagName.equals(""))
                tagName = "(anon)";
            return ("1-" + tagName);
        }
        
        if (value instanceof Diagram) {
            return ((Diagram) value).getName();
        }
        if (value != null)
            return value.toString();
        else
            return "-";
    }
    
    /**
     * helps prepare state before a node is expanded.
     */
    class ExplorerTreeWillExpandListener implements TreeWillExpandListener {
        
        /** does nothing **/
        public void treeWillCollapse(TreeExpansionEvent tee) {
	}
        
        /**
         * updates stereotype setting,
         * adds all children per treemodel 'build on demand' design.
         */
        public void treeWillExpand(TreeExpansionEvent tee) {
            
            showStereotype =
		Configuration.getBoolean(Notation.KEY_SHOW_STEREOTYPES, false);
            
            if (getModel() instanceof ExplorerTreeModel) {
                
                ((ExplorerTreeModel) getModel()).updateChildren(tee.getPath());
            }
            
        }
    }
    
    /**
     * helps react to tree expansion events.
     */
    class ExplorerTreeExpansionListener implements TreeExpansionListener {
        
        /**
         * does nothing
         */
        public void treeCollapsed(TreeExpansionEvent event) {
        }
        
        /**
         * updates the selection state.
         */
        public void treeExpanded(TreeExpansionEvent event) {
            
            // need to update the selection state.
            setSelection(TargetManager.getInstance().getTargets().toArray());
        }
        
    }
    
    /**
     * Sets the selection state for a given set of targets.
     */
    private void setSelection(Object[] targets) {
        
        this.clearSelection();
        
        int rows = getRowCount();
        for (int i = 0; i < targets.length; i++) {
            Object target = targets[i];
            target =
		target instanceof Fig ? ((Fig) target).getOwner() : target;
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
    
    /**
     * manages selecting the item to show in Argo's other
     * views based on the highlighted row.
     */
    class NavigationTreeSelectionListener implements TreeSelectionListener {
        
        /**
         * change in nav tree selection -> set target in target manager.
         */
        public void valueChanged(TreeSelectionEvent e) {
            
            if (!updatingSelectionViaTreeSelection) {
                updatingSelectionViaTreeSelection = true;
                
                // get the elements                               
                TreePath[] addedOrRemovedPaths = e.getPaths();                
                TreePath[] selectedPaths = getSelectionPaths();
                List elementsAsList = new ArrayList();                
                for (int i = 0; selectedPaths != null && i < selectedPaths.length; i++) {
                    Object element = ((DefaultMutableTreeNode)selectedPaths[i].getLastPathComponent()).getUserObject();
                    elementsAsList.add(element);
//                  // scan the visible rows for duplicates of this elem and select them
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
                    Object element = ((DefaultMutableTreeNode) addedOrRemovedPaths[i]
                            .getLastPathComponent()).getUserObject();
                    if (!e.isAddedPath(i)) {
                        callSetTarget = false;
                        break;
                    } else {
                        addedElements.add(element);
                    }
                }
                
                if (callSetTarget && addedElements.size() == elementsAsList.size() && elementsAsList.containsAll(addedElements)) {              
                    TargetManager.getInstance().setTargets(elementsAsList);
                } else {
                    // we must call the correct method on targetmanager for each added or removed target
                    List removedTargets = new ArrayList();
                    List addedTargets = new ArrayList();
                    for (int i = 0; i < addedOrRemovedPaths.length; i++) {
                        Object element = ((DefaultMutableTreeNode) addedOrRemovedPaths[i]
                             .getLastPathComponent()).getUserObject();
                        if (e.isAddedPath(i)) {
                            addedTargets.add(element);
                        } else {
                            removedTargets.add(element);
                        }
                    }
                    // we can't remove the targets one by one, we have to do it in one go.
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
         * actions a change in targets received from the TargetManager.
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
        
        /**
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
                    target = target instanceof Fig ? ((Fig) target).getOwner()
                            : target;
                    for (int j = 0; j < rows; j++) {
                        Object rowItem = ((DefaultMutableTreeNode) getPathForRow(
                                j).getLastPathComponent()).getUserObject();
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
        
        /**
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
                    target = target instanceof Fig ? ((Fig) target).getOwner()
                            : target;
                    for (int j = 0; j < rows; j++) {
                        Object rowItem = ((DefaultMutableTreeNode) getPathForRow(
                                j).getLastPathComponent()).getUserObject();
                        if (rowItem == target) {
                            removeSelectionRow(j);
                        }
                    }
                }

                if (getSelectionCount() > 0) {
                    scrollRowToVisible(getSelectionRows()[0]);
                }
                updatingSelection = true;
            }
            // setTargets(e.getNewTargets());
        }
        
        /**
         * @see org.argouml.ui.targetmanager.TargetListener#targetSet(
	 *         org.argouml.ui.targetmanager.TargetEvent)
         */
        public void targetSet(TargetEvent e) {
            setTargets(e.getNewTargets());
            
        }
    }
    
    class ProjectPropertyChangeListener implements PropertyChangeListener {
        
        /**
         * Listens to events coming from the project manager,
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

}
