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
import org.argouml.ui.NavPerspective;
import org.argouml.ui.AbstractGoRule;

/**
 *
 * @author  alexb
 */
public class ExplorerTreeModel
extends DefaultTreeModel
implements TreeModelUMLEventListener,
ItemListener{
    
    Object rules[];
    
    Map modelElementMap;
    
    /** Creates a new instance of ExplorerTreeModel */
    public ExplorerTreeModel(Object root) {
        super(new DefaultMutableTreeNode(root));
        this.setAsksAllowsChildren(true);
        modelElementMap = new HashMap();
        
        ExplorerEventAdaptor.getInstance()
        .setTreeModelUMLEventListener(this);
    }
    
    public void modelElementChanged(Object node) {
        
        Iterator nodesIt = this.findNodes(node).iterator();
        while(nodesIt.hasNext()){
            
            DefaultMutableTreeNode changeNode = (DefaultMutableTreeNode)nodesIt.next();
            changeNode.removeAllChildren();
            addAllChildren(new TreePath(this.getPathToRoot(changeNode)));
            this.nodeStructureChanged(changeNode);
        }
        
        
    }
    
    public void modelElementAdded(Object node) {
        
        Iterator nodesIt = this.findNodes(node).iterator();
        while(nodesIt.hasNext()){
            
            DefaultMutableTreeNode changeNode = (DefaultMutableTreeNode)nodesIt.next();
            changeNode.removeAllChildren();
            addAllChildren(new TreePath(this.getPathToRoot(changeNode)));
            this.nodeStructureChanged(changeNode);
        }
    }
    
    public void modelElementRemoved(Object node) {
        
        Iterator nodesIt = this.findNodes(node).iterator();
        while(nodesIt.hasNext()){
            
            DefaultMutableTreeNode changeNode = (DefaultMutableTreeNode)nodesIt.next();
            changeNode.removeAllChildren();
            addAllChildren(new TreePath(this.getPathToRoot(changeNode)));
            this.nodeStructureChanged(changeNode);
        }
    }
    
    public void structureChanged() {
        
        Project proj = ProjectManager.getManager().getCurrentProject();
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(proj);
        DefaultMutableTreeNode displayRoot =
        new DefaultMutableTreeNode(proj.getModel());
        rootNode.add(displayRoot);
        super.setRoot(rootNode);
        this.nodeStructureChanged(rootNode);
        
        this.addToMap(proj, rootNode);
        this.addToMap(proj.getModel(), displayRoot);
    }
    
//    public void setRoot(Object root) {
//        
//        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(root);
//        DefaultMutableTreeNode displayRoot =
//        new DefaultMutableTreeNode(((Project)root).getModel());
//        rootNode.add(displayRoot);
//        super.setRoot(rootNode);
//        this.nodeStructureChanged(rootNode);
//        
//        this.addToMap(root, rootNode);
//        this.addToMap(((Project)root).getModel(), displayRoot);
//    }
    
    public void addRules(Object[] newRules){
        
        rules = newRules;
    }
    
    public void addAllChildren(TreePath path){
        
        DefaultMutableTreeNode node =
        (DefaultMutableTreeNode)path.getLastPathComponent();
        
        // if the node has children,
        // then it has been expanded meaning we do not need to add the nodes
        // manually.
        if(node.getChildCount() != 0)
            return;
        
        Object modelElement = node.getUserObject();
        
        for(int x=0;x<rules.length;x++){
            
            Collection children = ((AbstractGoRule)rules[x]).getChildren(modelElement);
            if(children != null){
                Iterator childrenIt = children.iterator();
                
                while(childrenIt.hasNext()){
                    Object child = childrenIt.next();
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(child);
                    this.addToMap(child, newNode);
                    
                    this.insertNodeInto(newNode,
                    node,
                    node.getChildCount());
                }
            }
        }
    }
    
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
    
    private Collection findNodes(Object modelElement){
        
        return (Set)modelElementMap.get(modelElement);
    }
    
    /** called when the user selects a perspective from the perspective
     *  combo. */
    public void itemStateChanged(ItemEvent e) {
        
        rules = (Object[])e.getItem();
        Object currentRoot = null;
        
        structureChanged();
        
        // root must be checked each time

//            currentRoot = getRoot();
//            currentRoot = ((DefaultMutableTreeNode)currentRoot).getUserObject();
//            
//            Object newRoot=null;
//            
//                newRoot = getRoot();
//                newRoot = ((DefaultMutableTreeNode)newRoot).getUserObject();
//                if(currentRoot != newRoot)
//                    setRoot(currentRoot);
    }
    
    public String toString(){
        return "Test perspective";
    }
}
