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

import java.util.*;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeNode;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.explorer.rules.PerspectiveRule;

/**
 * The model for the Explorer tree view of the uml model.
 *
 * provides:
 *  - receives events from the uml model and updates itself and the tree ui.
 *  - responds to changes in perspetive and ordering.
 *
 * @author  alexb
 * @since 0.15.2
 */
public class ExplorerTreeModel
extends DefaultTreeModel
implements TreeModelUMLEventListener,
ItemListener{
    
    /**
     * an array of 
     * {@link org.argouml.ui.explorer.rules.PerspectiveRule PerspectiveRules},
     * that determine the tree view.
     */
    Object rules[];
    
    /**
     * a map used to resolve model elements to tree nodes when determining
     * what effect a model event will have on the tree.
     */
    Map modelElementMap;
    
    /**
     * the global order for siblings in the tree.
     */
    Comparator order;
    
    /** Creates a new instance of ExplorerTreeModel */
    public ExplorerTreeModel(Object root) {
        super(new ExplorerTreeNode(root));
        this.setAsksAllowsChildren(true);
        modelElementMap = new HashMap();
        
        ExplorerEventAdaptor.getInstance()
            .setTreeModelUMLEventListener(this);
        
        order = new TypeThenNameOrder();
    }
    
    /**
     * a model element has changed in some way.
     */
    public void modelElementChanged(Object node) {
        
        Object[] nodesArray = this.findNodes(node).toArray();
        
        for(int x=0;x<nodesArray.length;x++){
            
            ExplorerTreeNode changeNode = (ExplorerTreeNode)nodesArray[x];
            this.nodeChanged(changeNode);
            
//            ExplorerTreeNode parentNode = (ExplorerTreeNode)changeNode.getParent();
//            parentNode.orderChildren();
//            this.nodeStructureChanged(parentNode);
        }
    }
    
    /**
     * a model element has been added to the model.
     */
    public void modelElementAdded(Object node) {
        
        Iterator nodesIt = this.findNodes(node).iterator();
        while(nodesIt.hasNext()){

            DefaultMutableTreeNode changeNode = (DefaultMutableTreeNode)nodesIt.next();
            changeNode.removeAllChildren();
            addAllChildren(new TreePath(this.getPathToRoot(changeNode)));
        }
    }
    
    /**
     * a model element has been removed from the model.
     */
    public void modelElementRemoved(Object node) {
        
        Collection nodes = this.findNodes(node);
        Object[] nodesArray = this.findNodes(node).toArray();
        
        for(int x=0;x<nodesArray.length;x++){
            
            ExplorerTreeNode changeNode = (ExplorerTreeNode)nodesArray[x];
            
            if(changeNode.getParent() != null){
                this.removeNodeFromParent(changeNode);
            }
            else{
                nodes.remove(changeNode);
            }
            // remove reference for gc
            changeNode.remove();
        }
    }
    
    /**
     * the model structure has changed, eg a new project.
     */
    public void structureChanged() {
        
        // remove references for gc
        if(this.getRoot() instanceof ExplorerTreeNode)
            ((ExplorerTreeNode)this.getRoot()).remove();
        Collection values = modelElementMap.values();
        Iterator valuesIt = values.iterator();
        while(valuesIt.hasNext()){
            ((Collection)valuesIt.next()).clear();
        }
        modelElementMap.clear();
        
        Project proj = ProjectManager.getManager().getCurrentProject();
        ExplorerTreeNode rootNode = new ExplorerTreeNode(proj);
        rootNode.setOrder(order);
        ExplorerTreeNode displayRoot =
        new ExplorerTreeNode(proj.getModel());
        displayRoot.setOrder(order);
        rootNode.add(displayRoot);
        super.setRoot(rootNode);
        this.nodeStructureChanged(rootNode);
        
        modelElementMap = new HashMap();
        this.addToMap(proj, rootNode);
        this.addToMap(proj.getModel(), displayRoot);
    }
    
    /**
     * builds the next level of the explorer tree for a given tree path.
     */
    public void addAllChildren(TreePath path){
        
        ExplorerTreeNode node =
        (ExplorerTreeNode)path.getLastPathComponent();
        
        // if the node has children,
        // then it has been expanded meaning we do not need to add the nodes
        // manually.
        if(node.getChildCount() != 0)
            return;
        
        Object modelElement = node.getUserObject();
        
        for(int x=0;x<rules.length;x++){
            
            Collection children = ((PerspectiveRule)rules[x]).getChildren(modelElement);
            if(children != null){
                Iterator childrenIt = children.iterator();
                
                while(childrenIt.hasNext()){
                    Object child = childrenIt.next();
                    
                    if(child != null){
                        ExplorerTreeNode newNode = new ExplorerTreeNode(child);
                        newNode.setOrder(order);
                        this.addToMap(child, newNode);
                        
                        node.add(newNode);
                    }
                }
            }
            
        }
        
        if(node.getChildCount() > 0){
            node.orderChildren();
            this.nodeStructureChanged(node);
        }
    }
    
    /**
     * adds a new tree node and model element to the map.
     * nodes are removed from the map when a {@link #modelElementRemoved(Object)
     * modelElementRemoved} event is received.
     */
    private void addToMap(Object modelElement, TreeNode node){
        
        Object value = modelElementMap.get(modelElement);
        if(value != null){
            
            ((Set)value).add(node);
        }else{
            
            Set nodes = new HashSet();
            nodes.add(node);
            modelElementMap.put(modelElement,nodes);
        }
    }
    
    /**
     * node lookup for a given model element.
     */
    private Collection findNodes(Object modelElement){
        
        return (Set)modelElementMap.get(modelElement);
    }
    
    /**
     * Updates the explorer for new perspectives / orderings.
     */
    public void itemStateChanged(ItemEvent e) {
        
        if(e.getSource() instanceof PerspectiveComboBox){
        
            rules = ((ExplorerPerspective)e.getItem()).getRulesArray();
        }
        else{
            
            order = (Comparator)e.getItem();
        }
        
        structureChanged();
    }
}
