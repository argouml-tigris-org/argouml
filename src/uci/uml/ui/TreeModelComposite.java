package uci.uml.ui;

import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;


public class TreeModelComposite implements TreeModel {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected Vector _subTreeModels = new Vector();
  protected Vector _providedClasses = new Vector();
  protected Object _root;
  
  ////////////////////////////////////////////////////////////////
  // contructors

  public TreeModelComposite() { }

  public TreeModelComposite(Vector subs) { _subTreeModels = subs; }


  ////////////////////////////////////////////////////////////////
  // accessors

  public void addSubTreeModel(TreeModel tm) {
    if (tm instanceof TreeModelPrereqs) {
      Vector prereqs = ((TreeModelPrereqs)tm).getPrereqs();
      Enumeration preEnum = prereqs.elements();
      while (preEnum.hasMoreElements()) {
	Object pre = preEnum.nextElement();
	if (!_providedClasses.contains(pre)) {
	  System.out.println("You cannot add " + tm +
			     " until something provides " + pre);
	  return;
	}
      }
      Vector provided = ((TreeModelPrereqs)tm).getProvidedTypes();
      Enumeration proEnum = provided.elements();
      while (proEnum.hasMoreElements()) {
	_providedClasses.addElement(proEnum.nextElement());
      }
    }
    
    _subTreeModels.addElement(tm);
  }
  
  ////////////////////////////////////////////////////////////////
  // TreeModel implementation
  
  public Object getRoot() { return _root; }

  public void setRoot(Object r) { _root = r; }


  public Object getChild(Object parent, int index) {
    int nSubs = _subTreeModels.size();
    for (int i = 0; i < nSubs; i++) {
      TreeModel tm = (TreeModel) _subTreeModels.elementAt(i);
      int childCount = tm.getChildCount(parent);
      if (index < childCount) return tm.getChild(parent, index);
      index -= childCount;
    }
    System.out.println("TreeModelComposite should never get here");
    return null;
  }
  
  public int getChildCount(Object parent) {
    int childCount = 0;
    int nSubs = _subTreeModels.size();
    for (int i = 0; i < nSubs; i++) {
      TreeModel tm = (TreeModel) _subTreeModels.elementAt(i);
      childCount += tm.getChildCount(parent);
    }
    return childCount;
  }
  
  public int getIndexOfChild(Object parent, Object child) {
    int childCount = 0;
    int nSubs = _subTreeModels.size();
    for (int i = 0; i < nSubs; i++) {
      TreeModel tm = (TreeModel) _subTreeModels.elementAt(i);
      int childIndex = tm.getIndexOfChild(parent, child);
      if (childIndex != -1) return childIndex + childCount;
      childCount += tm.getChildCount(parent);
    }
    System.out.println("child not found!");
    return 0;
  }


  /**
   * Returns true if <I>node</I> is a leaf.  It is possible for this method
   * to return false even if <I>node</I> has no children.  A directory in a
   * filesystem, for example, may contain no files; the node representing
   * the directory is not a leaf, but it also has no children.
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
  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }
  
  
} /* end class TreeModelComposite */
