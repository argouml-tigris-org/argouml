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
// Original Author: ?

package org.argouml.ui;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Category;
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
public class DisplayTextTree extends JTree implements TargetListener {

    protected static Category cat = Category.getInstance(DisplayTextTree.class);

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

    /** holds state info about whether to display stereotypes in the nav pane.*/
    private boolean showStereotype;

    /** Runnable to help avoid too many tree updates. */
    private DisplayTextTreeRun _doit;

    /** needs documenting */
    public DisplayTextTree() {

        super();

        setCellRenderer(new UMLTreeCellRenderer());
        putClientProperty("JTree.lineStyle", "Angled");
        setRootVisible(false);
        setShowsRootHandles(true);
        setToolTipText("Tree"); // Enables tooltips for tree. Won't be shown.

        this.setRowHeight(18); 

        showStereotype =
            Configuration.getBoolean(Notation.KEY_SHOW_STEREOTYPES, false);
        _expandedPathsInModel = new Hashtable();
        _reexpanding = false;
        _doit = new DisplayTextTreeRun(cat, this);
    }

    // ------------ methods that override JTree methods ---------

    /**
     * override default JTree implementation to display the
     * appropriate text for any object that will be displayed in
     * the Nav pane or todo list.
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
                Object st =  ModelFacade.getStereoType(value);
                if (st != null) {
                    name += " " + GeneratorDisplay.Generate(st);
                }
            }

            return name;
        }

        if (value instanceof ToDoItem) {
            return ((ToDoItem) value).getHeadline();
        }
        if (value instanceof ToDoList) {
            return "ToDoList";
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

    /** Signals to the tree that something has changed and it is best
     * to update the tree.
     *
     * <p>specific to the Navigator tree
     *
     * <P>
     * For complex operations such as import(?) and add attribute(8), that
     * does several calls to this it is better if we defer the actual update
     * until later and if it is not performed don't do an extra.
     * Since import is done from invokeLater() we try to move this down
     * in priority by not running until the second invokeLater().
     * Depending on the queue order in invokeLater() this might result in
     * updates but it is probably far from every file.
     *
     * @see org.argouml.uml.ui.ActionRemoveFromModel
     * @see org.argouml.uml.ui.ActionAddDiagram
     * @see org.argouml.ui.NavigatorPane
     * @see org.argouml.uml.ui.foundation.core.PropPanelGeneralization
     * @see org.argouml.uml.ui.UMLReflectionListModel
     */
    public void forceUpdate() {

        cat.debug("forceUpdate");
        _doit.onceMore();
    }

    /**
     * Countpart to forceUpdate() that only updates viewable
     * rows, instead of rebuilding the whole tree; a vast improvement
     * in performance.
     *
     * <p>specific to the Navigator tree
     *
     * @see org.argouml.model.uml.UmlModelListener
     */
    public void forceUpdate(Object changed) {

        NavPerspective model = (NavPerspective) getModel();
        if (model instanceof NavPerspective) {

            // Special case for the 'top' state of a state machine (it
            // is never displayed in the tree(package parspective)),
            // therefore this method will not work unless we get its
            // statemachine and set that as the 'changed' object.
            if (ModelFacade.isAStateVertex(changed)) {
                changed = UmlHelper.getHelper().getStateMachines().getStateMachine(changed);
            }
            
            //if the changed object is added to the model
            //in a path that was previously expanded, but is no longer
            // then we need to clear the cache to prevent a model corruption.
            this.clearToggledPaths();

            // update any relevant rows
            int rows = this.getRowCount();
            for (int row = 0; row < rows; row++) {

                TreePath path = this.getPathForRow(row);
                Object rowItem = path.getLastPathComponent();

                if (rowItem == changed) {

                    model.fireTreeStructureChanged(changed, path.getPath());
                }
            }

        }
        reexpand();
    }

    /**
     * This is the real update function. It won't return until the tree
     * really is updated.
     * <P>
     * Never call this one from any code, it is package private.
     *
     *  <p>specific to the Navigator tree
     *
     * @since 0.13.1
     */
    void doForceUpdate() {

        cat.debug("doForceUpdate");
        Object rootArray[] = new Object[1];
        rootArray[0] = getModel().getRoot();
        Object noChildren[] = null;
        int noIndexes[] = null;
        TreeModelEvent tme = new TreeModelEvent(this, new TreePath(rootArray));
        treeModelListener.treeStructureChanged(tme);
        TreeModel tm = getModel();
        if (tm instanceof NavPerspective) {
            NavPerspective np = (NavPerspective) tm;
            np.fireTreeStructureChanged(this, rootArray, noIndexes, noChildren);
        }
        reexpand();
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

    /**
     * This methods sets the target of the treemodel to the given object. It's
     * a means to set the target programmatically from within the setTarget
     * method in the ProjectBrowser.
     *
     * <p>If the tree view shows the model element more than once,
     * all such rows shall be selected.
     *
     * <p>This may take some time for a lot of rows... put the work on
     * a worker thread perhaps?
     *
     * @param target a selected Fig or Model element.
     */
    public void setTarget(Object target) {

        cat.debug("setTarget");
        // specific to the Navigator tree
        if (getModel() instanceof NavPerspective) {

            if (target instanceof Fig) {
                target = ((Fig) target).getOwner();
            }

        }

        // clear the tree selection
        this.clearSelection();

        // add the any relevant rows
        int rows = this.getRowCount();
        for (int row = 0; row < rows; row++) {

            TreePath path = this.getPathForRow(row);
            Object rowItem = path.getLastPathComponent();

            if (rowItem == target) {
                this.addSelectionRow(row);
                this.scrollRowToVisible(row);
            }
        }
    }
    private void setTargets(Object[] targets) {
        if (getModel() instanceof NavPerspective) {
            clearSelection();
            int rowToSelect = 0;
            for (int i = 0; i < targets.length; i++) {
                Object target = targets[i];
                target =
                    target instanceof Fig ? ((Fig) target).getOwner() : target;
                int rows = getRowCount();
                for (int j = 0; j < rows; j++) {
                    Object rowItem = getPathForRow(j).getLastPathComponent();
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

} /* end class DisplayTextTree */

/**
 * Because there <strong>may</strong> be many calls from Argo to
 * update the tree view in a very short space of time (eg. during some
 * automatic processing) , this class will discard all update calls
 * except one in order to improve the <strong>performance</strong> of
 * Argo.
 *
 * <p>This class is used to defer the actual update until "late"
 * in the invokeLater()-call chain.
 *
 * <P>The real update will hopefully take place at the end of whatever long
 * chain of forceUpdate:s that will be performed.
 */
class DisplayTextTreeRun implements Runnable {

    /** needs documenting */
    //protected Category cat;
    protected static Category cat =
        Category.getInstance(DisplayTextTreeRun.class);

    /** needs documenting */
    private DisplayTextTree _tree;

    /** needs documenting */
    int _timesToRun;

    /** needs documenting */
    boolean _queued;

    /** needs documenting */
    public DisplayTextTreeRun(Category c, DisplayTextTree t) {

        cat.debug("DisplayTextTreeRun constructor");
        //cat = c;
        _tree = t;
        _timesToRun = 0;
        _queued = false;
    }

    /** needs documenting */
    public synchronized void onceMore() {

        cat.debug("onceMore");
        if (!_queued) {
            _queued = true;
            SwingUtilities.invokeLater(this);
        }
        _timesToRun++;
    }

    /** needs documenting */
    public synchronized void run() {

        cat.debug("run");
        if (_timesToRun > 100)
            cat.debug("" + _timesToRun + " forceUpdates encountered.");

        if (_timesToRun > 0) {
            // another forceUpdate was seen, wait again
            _queued = true;
            SwingUtilities.invokeLater(this);
            _timesToRun = 0;
        } else if (_queued) {
            _queued = false;
            SwingUtilities.invokeLater(this);
        } else {
            _tree.doForceUpdate();
        }
    }
}
