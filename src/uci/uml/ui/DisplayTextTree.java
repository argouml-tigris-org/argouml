package uci.uml.ui;

import java.util.*;
import java.beans.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
import com.sun.java.swing.plaf.basic.*;

import uci.argo.kernel.*;
import uci.gef.*;
import uci.uml.Foundation.Core.*;

public class DisplayTextTree extends JTree
implements VetoableChangeListener {

  Hashtable _expandedPathsInModel = new Hashtable();
  boolean _reexpanding = false;
  
  public DisplayTextTree() {
    setCellRenderer(new UMLTreeCellRenderer());
    putClientProperty("JTree.lineStyle", "Angled");
  }

  public String convertValueToText(Object value, boolean selected,
				   boolean expanded, boolean leaf, int row,
				   boolean hasFocus) {
    if (value == null) return "(null)";
    if (value instanceof ToDoItem) {
      return ((ToDoItem)value).getHeadline();
    }
    if (value instanceof Element) {
      Element e = (Element) value;
      String ocl = "";
      if (e instanceof ElementImpl)
	ocl = ((ElementImpl)e).getOCLTypeStr();
      String name = e.getName().getBody();
      if (name.equals("")) name = "(anon)";
      //return ocl + ": " + name;
      return name;
    }
    if (value instanceof Diagram) {
      return ((Diagram)value).getName();
    }
    return value.toString();
  }


  protected Vector getExpandedPaths() {
    TreeModel tm = getModel();
    Vector res = (Vector) _expandedPathsInModel.get(tm);
    if (res == null) {
      res = new Vector();
      _expandedPathsInModel.put(tm, res);
    }
    return res;
  }

  /**
   * Tree Model Expansion notification.
   *
   * @param e  a Tree node insertion event
   */
  public void fireTreeExpanded(TreePath path) {
    super.fireTreeExpanded(path);
    if (_reexpanding) return;
    if (path == null || _expandedPathsInModel == null) return;
    Vector expanded = getExpandedPaths();
    expanded.removeElement(path);
    expanded.addElement(path);
    addListenerToPath(path);
  }

  protected void addListenerToPath(TreePath path) {
    Object node = path.getLastPathComponent();
    addListenerToNode(node);
  }
  
  protected void addListenerToNode(Object node) {
    if (node instanceof ElementImpl)
      ((ElementImpl)node).addVetoableChangeListener(this);
    if (node instanceof Project)
      ((Project)node).addVetoableChangeListener(this);
    // diagram
    TreeModel tm = getModel();
    int childCount = tm.getChildCount(node);
    for (int i = 0; i < childCount; i++) {
      Object child = tm.getChild(node, i);
      if (child instanceof ElementImpl) 
	((ElementImpl)child).addVetoableChangeListener(this);
      //diagram
    }
  }
  
  public void fireTreeCollapsed(TreePath path) {
    super.fireTreeCollapsed(path);
    if (path == null || _expandedPathsInModel == null) return;
    Vector expanded = getExpandedPaths();
    expanded.removeElement(path);
  }

  public void setModel(TreeModel newModel) {
    super.setModel(newModel);
    if (newModel.getRoot() instanceof ElementImpl)
      ((ElementImpl)newModel.getRoot()).addVetoableChangeListener(this);
    if (newModel.getRoot() instanceof Project)
      ((Project)newModel.getRoot()).addVetoableChangeListener(this);
    int childCount = newModel.getChildCount(newModel.getRoot());
    for (int i = 0; i < childCount; i++) {
      Object child = newModel.getChild(newModel.getRoot(), i);
      if (child instanceof ElementImpl) 
	((ElementImpl)child).addVetoableChangeListener(this);
      //diagram
    }
    reexpand();
  }

  
  public void vetoableChange(PropertyChangeEvent e) {
    Runnable updateTree;
    updateTree = new UpdateTreeHack(this);
    SwingUtilities.invokeLater(updateTree);
  }



  
  public static final int DEPTH_LIMIT = 10;
  public static final int CHANGE = 1;
  public static final int ADD = 2;
  public static final int REMOVE = 3;
  public static Object path[] = new Object[DEPTH_LIMIT];
  
  public void reexpand() {
    if (_expandedPathsInModel == null) return;
    _reexpanding = true;
    ((AbstractTreeUI)getUI()).rebuild();
    java.util.Enumeration enum = getExpandedPaths().elements();
    while (enum.hasMoreElements()) {
      TreePath path = (TreePath) enum.nextElement();
      getUI().expandPath(path);
      addListenerToPath(path);
    }
    _reexpanding = false;
    
//     if (depth >= DEPTH_LIMIT) return;
//     path[depth] = searchNode;
//     if (updateNode == searchNode) {
//       System.out.println("= = = = depth " + depth);
//       System.out.println("= = = = found! " + searchNode);
//       int childIndices[] = new int[1];
//       Object children[] = new Object[1];
//       Object path2[] = new Object[depth-1];
//       for (int i = 1; i <= depth; i++) path2[i-1] = path[i];
//       NavPerspective pers = (NavPerspective) getModel();
//       children[0] = newValue;
//       if (newValue != null)
// 	childIndices[0] = pers.getIndexOfChild(updateNode, newValue);
      
//       //if (action == ADD)
//       //pers.fireTreeNodesInserted(this, path2, childIndices, children);
//       //else if (action == REMOVE) 
//       //pers.fireTreeNodesRemoved(this, path2, childIndices, children);
//       //else if (action == CHANGE)
//       pers.fireTreeStructureChanged(this, path2);      
//     }
//     TreeModel tm = getModel();
//     int numChildren = tm.getChildCount(searchNode);
//     for (int i = 0; i < numChildren; i++) {
//       Object child = tm.getChild(searchNode, i);
//       searchAndUpdate(updateNode, child, action, newValue, depth + 1);
//     }
//     if (depth == 0) invalidate();
  }
  
} /* end class DisplayTextTree */



class UpdateTreeHack implements Runnable {
  DisplayTextTree _tree;

  public UpdateTreeHack(DisplayTextTree t) {
    _tree = t;
  }
  
  public void run() {
    _tree.reexpand();
  }
} /* end class UpdateTreeHack */
