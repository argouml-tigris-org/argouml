// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


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
