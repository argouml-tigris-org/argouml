package org.argouml.ui;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.tree.TreeModel;

import org.apache.log4j.Category;

/**
 * Abstract class to be used as a convenience class for implementing 'go rules'.
 * Go rules are rules to which the navperspectives comply if they are registred
 * with the navperspectives. This usually happens in the static block of
 * NavPerspective.
 * If some modelelement is not showing up in a navperspective, in most cases this
 * means that there is no go rule defined or an incorrect one. Via the abstract method
 * getChildren the children of some tree element in a perspective are returned.
 * Only the returned children are shown for some tree element.
 *
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class AbstractGoRule implements TreeModel {

    private static Category cat =
        Category.getInstance(org.argouml.ui.AbstractGoRule.class);
 

    // ----------- TreeModel helpers -----------

    /**
     * @see javax.swing.tree.TreeModel#getChild(Object, int)
     */
    public Object getChild(Object parent, int index) {

        int count = 0;
        Collection col = getChildren(parent);
        if (col != null) {
            Iterator it = col.iterator();
            while (it.hasNext()) {

                Object candidate = it.next();
                if (count == index)
                    return candidate;
                count++;
            }
        }
        return null;
    }

    /**
     * @see javax.swing.tree.TreeModel#getChildCount(Object)
     */
    public int getChildCount(Object parent) {
        Collection c = getChildren(parent);

        if (c == null)
            return 0;

        return c.size();
    }

    /**
     * @see javax.swing.tree.TreeModel#isLeaf(Object)
     */
    public final boolean isLeaf(Object node) {

        return (getChildCount(node) < 1);
    }

    /**
     * @see javax.swing.tree.TreeModel#getIndexOfChild(Object, Object)
     */
    public int getIndexOfChild(Object parent, Object child) {

        int index = 0;
        Collection col = getChildren(parent);
        if (col != null) {
            Iterator it = col.iterator();
            while (it.hasNext()) {

                Object candidate = it.next();
                if (candidate == child)
                    return index;
                index++;
            }
        }
        return -1;
    }

    // -------------- other helper methods --------------------

    /**
     * this is the method that should be overridden by GoRules
     */
    public abstract Collection getChildren(Object parent);

    /** return the name of the rule as it is displayed in
     *  the nav perspective edit pane. Returns for example
     *  "State->Substates".
     *  @see #toString()
     */
    public abstract String getRuleName();

    /** wrapper around getRuleName()
     */
    public String toString() {
        return getRuleName();
    }

   

    // ------------- not used TreeModel methods -------------

    public void addTreeModelListener(
        javax.swing.event.TreeModelListener treeModelListener) {
        }

    public Object getRoot() {
        return null;
    }

    public void removeTreeModelListener(
        javax.swing.event.TreeModelListener treeModelListener) {
        }

    public void valueForPathChanged(
        javax.swing.tree.TreePath treePath,
        Object obj) {
        }

}
