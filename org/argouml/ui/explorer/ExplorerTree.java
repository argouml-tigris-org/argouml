// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

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
import org.argouml.ui.NavPerspective;
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
extends DisplayTextTree
{
    
    /** holds state info about whether to display stereotypes in the nav pane.*/
    private boolean showStereotype;
    
    /**
     * prevents target event cycles between this and the TargetManager.
     */
    private boolean updatingSelection;
    
    /** Creates a new instance of ExplorerTree */
    public ExplorerTree() {
        super();
        
        this.setModel(new ExplorerTreeModel(ProjectManager.getManager().getCurrentProject()));
        this.addMouseListener(new NavigatorMouseListener(this));
        this.addTreeSelectionListener(new NavigationTreeSelectionListener());
        this.addTreeWillExpandListener(new ExplorerTreeWillExpandListener());
        this.addTreeExpansionListener(new ExplorerTreeExpansionListener());
        
        TargetManager.getInstance().addTargetListener(new ExplorerTargetListener());
        
        showStereotype =
        Configuration.getBoolean(Notation.KEY_SHOW_STEREOTYPES, false);
    }
    
    /** Listens to mouse events coming from the *JTree*,
     * on right click, brings up the pop-up menu.
     */
    class NavigatorMouseListener extends MouseAdapter {
        
        JTree mLTree;
        
        public NavigatorMouseListener(JTree newtree){
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
            
            Object selectedItem = 
                ((DefaultMutableTreeNode)getLastSelectedPathComponent())
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
    public String convertValueToText(
    Object value,
    boolean selected,
    boolean expanded,
    boolean leaf,
    int row,
    boolean hasFocus) {
        
        //cat.debug("convertValueToText");
        
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
            // displayed in the navperspective
            else if (ModelFacade.isAComment(value)) {
                name = ModelFacade.getName(value);
                if (name != null && name.length() > 10) {
                    name = name.substring(0, 10) + "...";
                }
            } else {
                name = ModelFacade.getName(value);
            }
            
            if (name == null || name.equals("")) {
                
                name =
                "(anon " + ModelFacade.getUMLClassName(value) + ")";
            }
            
            // Look for stereotype
            if (showStereotype) {
                Object stereo = null;
                if (ModelFacade.getStereotypes(value).size() > 0) {
                    stereo = ModelFacade.getStereotypes(value).iterator().next();
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
    class ExplorerTreeWillExpandListener implements TreeWillExpandListener{
        
        /** does nothing **/
        public void treeWillCollapse(TreeExpansionEvent tee) {}
        
        /**
         * updates stereotype setting,
         * adds all children per treemodel 'build on demand' design.
         */
        public void treeWillExpand(TreeExpansionEvent tee) {
            
            showStereotype =
            Configuration.getBoolean(Notation.KEY_SHOW_STEREOTYPES, false);
            
            if(getModel() instanceof ExplorerTreeModel){
                
                ((ExplorerTreeModel)getModel()).addAllChildren(tee.getPath());
            }
            
        }
    }
    
    /**
     * helps react to tree expansion events.
     */
    class ExplorerTreeExpansionListener implements TreeExpansionListener{
        
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
    private void setSelection(Object[] targets){
        
        int rowToSelect = 0;
        int[] rowIndexes = new int[targets.length];
        int rowIndexCounter = 0;
        int rows = getRowCount();
        for (int i = 0; i < targets.length; i++) {
            Object target = targets[i];
            target =
            target instanceof Fig
            ? ((Fig)target).getOwner()
            : target;
            for (int j = 0; j < rows; j++) {
                Object rowItem =
                ((DefaultMutableTreeNode)getPathForRow(j)
                .getLastPathComponent())
                .getUserObject();
                if (rowItem == target) {
                    rowIndexes[rowIndexCounter] = j;
                    rowIndexCounter++;
                    if (rowToSelect == 0) {
                        rowToSelect = j;
                    }
                    break;
                }
            }
            
        }
        if (rowIndexCounter < targets.length) {
            int[] rowIndexestmp = rowIndexes;
            rowIndexes = new int[rowIndexCounter];
            for (int i = 0 ; i < rowIndexCounter; i++) {
                rowIndexes[i] = rowIndexestmp[i];
            }
        }
        setSelectionRows(rowIndexes);
        scrollRowToVisible(rowToSelect);
    }
    
    /**
     * manages selecting the item to show in Argo's other
     * views based on the highlighted row.
     */
    class NavigationTreeSelectionListener implements TreeSelectionListener {
        
        /**
         * change in nav tree selection -> set target in project browser.
         */
        public void valueChanged(TreeSelectionEvent e) {
            
            if(!updatingSelection){
                updatingSelection = true;
                
                TreePath[] paths = e.getPaths();
                List targets = new ArrayList();
                if (paths != null) {
                    for (int i = 0; i < paths.length; i++) {
                        if (e.isAddedPath(paths[i]))
                            targets.add(
                                ((DefaultMutableTreeNode)paths[i]
                                    .getLastPathComponent())
                                        .getUserObject()
                                );
                    }
                }
                TargetManager.getInstance().setTargets(targets);

                updatingSelection = false;
            }
        }
    }
    
    class ExplorerTargetListener implements TargetListener{
    
        /**
         * actions a change in targets received from the TargetManager.
         */
        private void setTargets(Object[] targets) {
            
            if(!updatingSelection){
                updatingSelection = true;
                
                if (targets == null
                || (targets.length == 1 && targets[0] == null)) {
                    clearSelection();
                } else {
                    
                    setSelection(targets);
                }
                repaint();
                updatingSelection = false;
            }
        }
        
        /**
         * @see
         * org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
         */
        public void targetAdded(TargetEvent e) {
            setTargets(e.getNewTargets());
        }
        
        /**
         * @see
         * org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
         */
        public void targetRemoved(TargetEvent e) {
            setTargets(e.getNewTargets());
        }
        
        /**
         * @see
         * org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
         */
        public void targetSet(TargetEvent e) {
            setTargets(e.getNewTargets());
            
        }
    }
}
