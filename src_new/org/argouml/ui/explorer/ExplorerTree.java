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

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
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
implements TargetListener {
    
    /** holds state info about whether to display stereotypes in the nav pane.*/
    private boolean showStereotype;
    
    /** Creates a new instance of ExplorerTree */
    public ExplorerTree() {
        super();
        
        this.setModel(new ExplorerTreeModel(ProjectManager.getManager().getCurrentProject()));
        this.addMouseListener(new NavigatorMouseListener(this));
        this.addTreeSelectionListener(new NavigationTreeSelectionListener());
        
        TargetManager.getInstance().addTargetListener(this);
        
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
     * manages selecting the item to show in Argo's other
     * views based on the highlighted row.
     */
    class NavigationTreeSelectionListener implements TreeSelectionListener {
        
        /**
         * change in nav tree selection -> set target in project browser.
         */
        public void valueChanged(TreeSelectionEvent e) {
            Object[] selections = getSelectedObjects();
            TargetManager.getInstance().setTargets(Arrays.asList(selections));
        }
    }
    
    /**
     * Gets all selected objects (for multiselect)
     * @return all selected objects (for multiselect)
     */
    public Object[] getSelectedObjects() {
        TreePath[] paths = getSelectionPaths();
        if (paths != null) {
            Object[] objects = new Object[paths.length];
            for (int i = 0; i < paths.length; i++) {
                objects[i] = 
                    ((DefaultMutableTreeNode)paths[i].getLastPathComponent())
                        .getUserObject();
            }
            return objects;
        }
        return new Object[0];
    }
    
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
    
    /** specific to the Navigator tree */
    public void fireTreeWillExpand(TreePath path) {
        
        showStereotype =
        Configuration.getBoolean(Notation.KEY_SHOW_STEREOTYPES, false);
        
        if(this.getModel() instanceof ExplorerTreeModel){
            
            ((ExplorerTreeModel)getModel()).addAllChildren(path);
        }
    }
    
    private void setTargets(Object[] targets) {
        
            clearSelection();
            int rowToSelect = 0;
            for (int i = 0; i < targets.length; i++) {
                Object target = targets[i];
                target =
                    target instanceof Fig ? ((Fig) target).getOwner() : target;
                int rows = getRowCount();
                for (int j = 0; j < rows; j++) {
                    Object rowItem = 
                        ((DefaultMutableTreeNode)getPathForRow(j)
                            .getLastPathComponent())
                                .getUserObject();
                    if (rowItem == target) {
                        addSelectionRow(j);
                        if (rowToSelect == 0) {
                            rowToSelect = j;
                        }
                    }
                }
                
            }
            scrollRowToVisible(rowToSelect);
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
