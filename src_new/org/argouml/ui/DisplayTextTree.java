// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// File: DisplayTextTree.java
// Classes: DisplayTextTree, DipslayTextTreeRun

package org.argouml.ui;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.Notation;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlHelper;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.uml.generator.GeneratorDisplay;
import org.argouml.uml.ui.UMLTreeCellRenderer;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;

/**
 * This is the JTree that is the gui component view of the model navigation and
 * todo list.
 */
public class DisplayTextTree extends JTree{

    protected static Logger cat = Logger.getLogger(DisplayTextTree.class);

    /**
     * A Map helping the tree maintain a consistent expanded paths state.
     *
     *<pre>
     * keys = the current TreeModel of this Tree
     * values = Vector of currently expanded paths.
     *</pre>
     */
    private Hashtable _expandedPathsInModel;

    /** needs documenting */
    private boolean _reexpanding;

    /** Sets the label renderer, line style angled, enable tooltips,
     *  sets row hieght to 18 pixels.
     */
    public DisplayTextTree() {

        super();

        setCellRenderer(new UMLTreeCellRenderer());
        putClientProperty("JTree.lineStyle", "Angled");
        setRootVisible(false);
        setShowsRootHandles(true);
        setToolTipText("Tree"); // Enables tooltips for tree. Won't be shown.

        this.setRowHeight(18); 

        _expandedPathsInModel = new Hashtable();
        _reexpanding = false;
    }

    // ------------ methods that override JTree methods ---------

    /**
     * override default JTree implementation to display the
     * appropriate text for any object that will be displayed in
     * the todo list.
     */
    public String convertValueToText(
        Object value,
        boolean selected,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocus) {

            String name = null;

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
     * Tree MModel Expansion notification.
     *
     * @param e  a Tree node insertion event
     */
    public void fireTreeExpanded(TreePath path) {

        super.fireTreeExpanded(path);

        cat.debug("fireTreeExpanded");
        if (_reexpanding)
            return;
        if (path == null || _expandedPathsInModel == null)
            return;
        Vector expanded = getExpandedPaths();
        expanded.removeElement(path);
        expanded.addElement(path);
    }

    /** needs documenting */
    public void fireTreeCollapsed(TreePath path) {

        super.fireTreeCollapsed(path);

        cat.debug("fireTreeCollapsed");
        if (path == null || _expandedPathsInModel == null)
            return;
        Vector expanded = getExpandedPaths();
        expanded.removeElement(path);
    }

    /** needs documenting */
    public void setModel(TreeModel newModel) {

        cat.debug("setModel");
        Object r = newModel.getRoot();
        if (r != null)
            super.setModel(newModel);
        reexpand();
    }

    // ------------- other methods ------------------

    /** needs documenting
     *
     * called in reexpand()
     */
    protected Vector getExpandedPaths() {

        cat.debug("getExpandedPaths");
        TreeModel tm = getModel();
        Vector res = (Vector) _expandedPathsInModel.get(tm);
        if (res == null) {
            res = new Vector();
            _expandedPathsInModel.put(tm, res);
        }
        return res;
    }

    /** 
     * we re-expand the ones
     * that were open before to maintain the same viewable tree.
     *
     * called by doForceUpdate(), setModel()
     */
    private void reexpand() {

        cat.debug("reexpand");
        if (_expandedPathsInModel == null)
            return;

        _reexpanding = true;

        Enumeration enum = getExpandedPaths().elements();
        while (enum.hasMoreElements()) {
            TreePath path = (TreePath) enum.nextElement();
            expandPath(path);
        }
        _reexpanding = false;
    }

}