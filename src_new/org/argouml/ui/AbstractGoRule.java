package org.argouml.ui;

import java.util.Collection;
import java.util.Vector;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

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
	
	private static Category cat = Category.getInstance(org.argouml.ui.AbstractGoRule.class);

	/**
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	public Object getRoot() {
		return null;
	}

	/**
	 * @see javax.swing.tree.TreeModel#getChild(Object, int)
	 */
	public Object getChild(Object parent, int index) {
		Vector children = toVector(getChildren(parent));
    	if (children != null) {
    		return children.elementAt(index); 
    	} else {
    		cat.fatal("getChild should never get here");
    		return null;
    	}
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
	
	private Vector toVector(Collection col) {
		Vector ret = new Vector();
		if (col != null && !col.isEmpty()) {
			ret.addAll(col);
		}
		return ret;
	}

	/**
	 * @see javax.swing.tree.TreeModel#isLeaf(Object)
	 */
	public abstract boolean isLeaf(Object node); 

	/**
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(TreePath, Object)
	 */
	public void valueForPathChanged(TreePath path, Object newValue) {
	}

	/**
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(Object, Object)
	 */
	public int getIndexOfChild(Object parent, Object child) {
		Vector children = toVector(getChildren(parent));
    	if (children != null && children.contains(child))
      		return children.indexOf(child);
    	return -1;
	}

	/**
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(TreeModelListener)
	 */
	public void addTreeModelListener(TreeModelListener l) {
	}

	/**
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(TreeModelListener)
	 */
	public void removeTreeModelListener(TreeModelListener l) {
	}
	
	public abstract Collection getChildren(Object parent);

    /** return the name of the rule as it is displayed in 
     *  the nav perspective edit pane. Returns for example 
     *  "State->Substates".
     *  @see #toString()
     */   
    public abstract String getRuleName();
    
    /** wrapper around getRuleName()
     */
    public String toString() { return getRuleName(); }
    

}






