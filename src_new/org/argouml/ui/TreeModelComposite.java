// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.ui;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.cognitive.ToDoItem;

/**
 * This class is the TreeModel for the navigator and todo list panels.
 *
 * <p>It is called <strong>Composite</strong> because each node in
 * the tree appears to be another TreeModelComposite instance..?
 *
 * <p>$Id$
 */
public class TreeModelComposite
    implements
        TreeModel,
        Cloneable {
    
    protected static Category cat =
        Category.getInstance(TreeModelComposite.class);
    
    ////////////////////////////////////////////////////////////////
    // instance variables
    
    /** needs documenting */
    protected Vector _subTreeModels;
    
    /** needs documenting */
    protected Object _root;
    
    /** needs documenting */
    protected boolean _flat;
    
    /** needs documenting */
    protected Vector _flatChildren;
    
    /** needs documenting */
    protected String _name;
    
    ////////////////////////////////////////////////////////////////
    // contructors
    
    /** needs documenting */
    public TreeModelComposite(String name) {
        
        setName(Argo.localize("Tree", name));
        _subTreeModels = new Vector();
        _flatChildren = new Vector();
    }
    
    /** needs documenting */
    public TreeModelComposite(String name, Vector subs) {
        this(name);
        _subTreeModels = subs;
    }
    
    ////////////////////////////////////////////////////////////////
    // TreeModel implementation
    
    /** needs documenting */
    public Object getRoot() { return _root; }
    
    /**
     * Finds the each of the children of a parent in the tree.
     *
     * @param parent in the tree
     * @param index of child to find
     * @return the child found at index. Null if index is out of bounds.
     */
    public Object getChild(Object parent, int index) {
        if (_flat && parent == _root) {
            return _flatChildren.elementAt(index);
        }
        int nSubs = _subTreeModels.size();
        for (int i = 0; i < nSubs; i++) {
            TreeModel tm = (TreeModel) _subTreeModels.elementAt(i);
            int childCount = tm.getChildCount(parent);
            if (index < childCount) return tm.getChild(parent, index);
            index -= childCount;
        }
        return null;
    }
    
    /** needs documenting */
    public int getChildCount(Object parent) {
        if (_flat && parent == _root) {
            return _flatChildren.size();
        }
        int childCount = 0;
        int nSubs = _subTreeModels.size();
        for (int i = 0; i < nSubs; i++) {
            TreeModel tm = (TreeModel) _subTreeModels.elementAt(i);
            childCount += tm.getChildCount(parent);
        }
        return childCount;
    }
    
    /** needs documenting */
    public int getIndexOfChild(Object parent, Object child) {
        if (_flat && parent == _root) {
            return _flatChildren.indexOf(child);
        }
        int childCount = 0;
        int nSubs = _subTreeModels.size();
        for (int i = 0; i < nSubs; i++) {
            TreeModel tm = (TreeModel) _subTreeModels.elementAt(i);
            int childIndex = tm.getIndexOfChild(parent, child);
            if (childIndex != -1) return childIndex + childCount;
            childCount += tm.getChildCount(parent);
        }
        cat.debug("child not found!");
        
        //The child is sometimes not found when the tree is being updated
        return -1;
    }
    
    /**
     * Returns true if <I>node</I> is a leaf.  It is possible for this method
     * to return false even if <I>node</I> has no children.  A directory in a
     * filesystem, for example, may contain no files; the node representing
     * the directory is not a leaf, but it also has no children.
     * <P>
     * If none of the subTreeModels is not a leaf, then we are not a leaf.
     *
     * @param   node    a node in the tree, obtained from this data source
     * @return  true if <I>node</I> is a leaf
     */
    public boolean isLeaf(Object node) {
        int nSubs = _subTreeModels.size();
        for (int i = 0; i < nSubs; i++) {
            TreeModel tm = (TreeModel) _subTreeModels.elementAt(i);
            if (!tm.isLeaf(node)) return false;
        }
        return true;
    }
    
    /**
     * Messaged when the user has altered the value for the item identified
     * by <I>path</I> to <I>newValue</I>.  If <I>newValue</I> signifies
     * a truly new value the model should post a treeNodesChanged
     * event.
     *
     * @param path path to the node that the user has altered.
     * @param newValue the new value from the TreeCellEditor.
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        cat.debug("valueForPathChanged TreeModelComposite");
    }
    
    /** empty implementation */
    public void addTreeModelListener(TreeModelListener l) {
    }
    
    /** empty implementation */
    public void removeTreeModelListener(TreeModelListener l) {
    }
    
    ////////////////////////////////////////////////////////////////
    // other methods
    
    /** needs documenting */
    public void setRoot(Object r) { _root = r; }
    
    /** Return true if this node will always be a leaf, it is not an
     *  "empty folder" */
    public boolean isAlwaysLeaf(Object node) { return false; }
    
    /** empty implementation */
    public void fireTreeStructureChanged() {
    }
    
    /** empty implementation */
    public void fireTreeStructureChanged(TreePath path) {
    }
    
    /** needs documenting */
    public void setFlat(boolean b) {
        _flat = false;
        if (b) calcFlatChildren();
        _flat = b;
    }
    
    /** needs documenting */
    public boolean getFlat() { return _flat; }
    
    /** needs documenting */
    public void addSubTreeModel(TreeModel tm) {
        if (_subTreeModels.contains(tm)) return;
        _subTreeModels.addElement(tm);
    }
    
    /** needs documenting */
    public void removeSubTreeModel(TreeModel tm) {
        // TODO: check for dangling prereqs
        _subTreeModels.removeElement(tm);
    }
    
    /** needs documenting */
    public Vector getSubTreeModels() { return _subTreeModels; }
    
    /** needs documenting */
    public void calcFlatChildren() {
        _flatChildren.removeAllElements();
        addFlatChildren(_root);
    }
    
    /** needs documenting */
    public void addFlatChildren(Object node) {
        if (node == null) return;
        cat.debug("addFlatChildren");
        // hack for to do items only, should check isLeaf(node), but that
        // includes empty folders. Really I need alwaysLeaf(node).
        if ((node instanceof ToDoItem) && !_flatChildren.contains(node))
            _flatChildren.addElement(node);
        
        int nKids = getChildCount(node);
        for (int i = 0; i <nKids; i++) {
            addFlatChildren(getChild(node, i));
        }
    }
    
    /** needs documenting */
    public String getName() { return _name; }
    
    /** needs documenting */
    public void setName(String s) { _name = s; }
    
    /** needs documenting */
    public String toString() {
        if (getName() != null) return getName();
        else return super.toString();
    }
    
} /* end class TreeModelComposite */
