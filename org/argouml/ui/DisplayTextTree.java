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

package org.argouml.ui;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.uml.ui.UMLTreeCellRenderer;

/**
 * This is the JTree that is the GUI component view of the UML model
 * navigation (the explorer) and the todo list.
 */
public class DisplayTextTree extends JTree {
    private static final Logger LOG = Logger.getLogger(DisplayTextTree.class);

    /**
     * A Map helping the tree maintain a consistent expanded paths state.
     *
     *<pre>
     * keys = the current TreeModel of this Tree
     * values = Vector of currently expanded paths.
     *</pre>
     */
    private Hashtable expandedPathsInModel;

    private boolean reexpanding;

    /** 
     * Sets the label renderer, line style angled, enable tooltips,
     * sets row height to 18 pixels.
     */
    public DisplayTextTree() {

        super();

        setCellRenderer(new UMLTreeCellRenderer());
        putClientProperty("JTree.lineStyle", "Angled");
        setRootVisible(false);
        setShowsRootHandles(true);
        setToolTipText("Tree"); // Enables tooltips for tree. Won't be shown.

        this.setRowHeight(18); 

        expandedPathsInModel = new Hashtable();
        reexpanding = false;
    }

    // ------------ methods that override JTree methods ---------

    /**
     * Override the default JTree implementation to display the
     * appropriate text for any object that will be displayed in
     * the todo list.
     * 
     * @param value the given object
     * @param selected ignored
     * @param expanded ignored
     * @param leaf ignored
     * @param row ignored
     * @param hasFocus ignored
     * 
     * @return the value converted to text.
     *
     * @see javax.swing.JTree#convertValueToText(java.lang.Object, 
     * boolean, boolean, boolean, int, boolean)
     */
    public String convertValueToText(
        Object value,
        boolean selected,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocus) {

	if (value instanceof ToDoItem) {
            return ((ToDoItem) value).getHeadline();
        }
        if (value instanceof ToDoList) {
            return "ToDoList";
        }
        if (value != null)
            return value.toString();
        else
            return "-";
    }

    /**
     * Tree MModel Expansion notification.<p>
     *
     * @param path a Tree node insertion event
     */
    public void fireTreeExpanded(TreePath path) {

        super.fireTreeExpanded(path);

        LOG.debug("fireTreeExpanded");
        if (reexpanding)
            return;
        if (path == null || expandedPathsInModel == null)
            return;
        Vector expanded = getExpandedPaths();
        expanded.removeElement(path);
        expanded.addElement(path);
    }

    /**
     * @see javax.swing.JTree#fireTreeCollapsed(javax.swing.tree.TreePath)
     */
    public void fireTreeCollapsed(TreePath path) {

        super.fireTreeCollapsed(path);

        LOG.debug("fireTreeCollapsed");
        if (path == null || expandedPathsInModel == null)
            return;
        Vector expanded = getExpandedPaths();
        expanded.removeElement(path);
    }

    /**
     * @see javax.swing.JTree#setModel(javax.swing.tree.TreeModel)
     */
    public void setModel(TreeModel newModel) {

        LOG.debug("setModel");
        Object r = newModel.getRoot();
        if (r != null)
            super.setModel(newModel);
        reexpand();
    }

    // ------------- other methods ------------------

    /**
     * Called in reexpand().
     *
     * @return a Vector containing all expanded paths
     */
    protected Vector getExpandedPaths() {

        LOG.debug("getExpandedPaths");
        TreeModel tm = getModel();
        Vector res = (Vector) expandedPathsInModel.get(tm);
        if (res == null) {
            res = new Vector();
            expandedPathsInModel.put(tm, res);
        }
        return res;
    }

    /** 
     * We re-expand the ones
     * that were open before to maintain the same viewable tree.
     *
     * called by doForceUpdate(), setModel()
     */
    private void reexpand() {

        LOG.debug("reexpand");
        if (expandedPathsInModel == null)
            return;

        reexpanding = true;

        Enumeration pathsEnum = getExpandedPaths().elements();
        while (pathsEnum.hasMoreElements()) {
            TreePath path = (TreePath) pathsEnum.nextElement();
            expandPath(path);
        }
        reexpanding = false;
    }

}
