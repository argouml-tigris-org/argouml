package org.argouml.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    private boolean _cacheable = false;

    private Map _cache = new HashMap();

    // ----------- TreeModel helpers -----------

    /**
     * @see javax.swing.tree.TreeModel#getChild(Object, int)
     */
    public Object getChild(Object parent, int index) {

        int count = 0;
        Collection col = getCachedChildren(parent);
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
        Collection c = getCachedChildren(parent);

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
        Collection col = getCachedChildren(parent);
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

    /**
     * Gets the cached children.
     * @param parent The parent of which the children are wanted
     * @return the children
     */
    private Collection getCachedChildren(Object parent) {
        Collection children = null;
        if (_cacheable) {
            children = (Collection)_cache.get(parent);
            if (children == null) {
                children = getChildren(parent);
                _cache.put(parent, children);
            }
        } else {
            children = getChildren(parent);
        }
        return children;
    }
    
    public void setCacheable(Object parent, boolean cacheable) {
        if (!cacheable) {
            _cache.remove(parent);
        }
        _cacheable = cacheable;
    }
    
    public boolean isCacheable(Object parent) {
        return _cacheable;
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
