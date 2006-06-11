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

package org.argouml.ui;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

/**
 * This class is the TreeModel for the navigator and todo list panels.<p>
 *
 * It is called <strong>Composite</strong> because there are a set of rules
 * that determine how to link parents to children in the tree. Those
 * rules can now be found in PerspectiveSupport.<p>
 */
public class TreeModelComposite extends TreeModelSupport implements TreeModel {

    private static final Logger LOG =
        Logger.getLogger(TreeModelComposite.class);

    /** The root of the model. */
    private Object root;

    ////////////////////////////////////////////////////////////////
    // contructors

    /**
     * The constructor.
     *
     * @param name the name that will be localized
     */
    public TreeModelComposite(String name) {
        super(name);
    }

    ////////////////////////////////////////////////////////////////
    // TreeModel implementation

    /** Getter for the root of the model.
     * @see javax.swing.tree.TreeModel#getRoot()
     */
    public Object getRoot() {
        return root;
    }

    /**
     * Finds the each of the children of a parent in the tree.
     *
     * @param parent in the tree
     * @param index of child to find
     * @return the child found at index. Null if index is out of bounds.
     */
    public Object getChild(Object parent, int index) {

        int nSubs = getGoRules().size();
        for (int i = 0; i < nSubs; i++) {
            TreeModel tm = (TreeModel) getGoRules().elementAt(i);
            int childCount = tm.getChildCount(parent);
            if (index < childCount) {
                return tm.getChild(parent, index);
            }
            index -= childCount;
        }
        return null;
    }

    /**
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    public int getChildCount(Object parent) {

        int childCount = 0;
        int nSubs = getGoRules().size();
        for (int i = 0; i < nSubs; i++) {
            TreeModel tm = (TreeModel) getGoRules().elementAt(i);
            childCount += tm.getChildCount(parent);
        }
        return childCount;
    }

    /**
     * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object,
     * java.lang.Object)
     */
    public int getIndexOfChild(Object parent, Object child) {

        int childCount = 0;
        int nSubs = getGoRules().size();
        for (int i = 0; i < nSubs; i++) {
            TreeModel tm = (TreeModel) getGoRules().elementAt(i);
            int childIndex = tm.getIndexOfChild(parent, child);
            if (childIndex != -1) {
                return childIndex + childCount;
            }
            childCount += tm.getChildCount(parent);
        }
        LOG.debug("child not found!");

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
        int nSubs = getGoRules().size();
        for (int i = 0; i < nSubs; i++) {
            TreeModel tm = (TreeModel) getGoRules().elementAt(i);
            if (!tm.isLeaf(node))
                return false;
        }
        return true;
    }

    /**
     * Empty implementation - not used.<p>
     *
     * Messaged when the user has altered the value for the item identified
     * by <I>path</I> to <I>newValue</I>.  If <I>newValue</I> signifies
     * a truly new value the model should post a treeNodesChanged
     * event.<p>
     *
     * @param path path to the node that the user has altered.
     * @param newValue the new value from the TreeCellEditor.
     */
    public void valueForPathChanged(TreePath path, Object newValue) { }

    ////////////////////////////////////////////////////////////////
    // other methods

    /**
     * @param r the root of the model
     */
    public void setRoot(Object r) {
        root = r;
    }

} /* end class TreeModelComposite */
