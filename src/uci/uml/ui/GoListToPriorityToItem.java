package uci.uml.ui;

import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;

import uci.argo.kernel.*;

public class GoListToPriorityToItem implements TreeModel {
  
  ////////////////////////////////////////////////////////////////
  // TreeModel implementation
  
  public Object getRoot() {
    System.out.println("getRoot should never be called");
    return null;
  } 
  public void setRoot(Object r) { }

  public Object getChild(Object parent, int index) {
    if (parent instanceof ToDoList) {
      return PriorityNode.getPriorities().elementAt(index);
    }
    if (parent instanceof PriorityNode) {
      // instead of makning a new vector, decrement index, return when
      // found and index == 0
      Vector candidates = new Vector();
      PriorityNode pn = (PriorityNode) parent;
      Enumeration itemEnum = Designer.TheDesigner.getToDoList().elements();
      while (itemEnum.hasMoreElements()) {
	ToDoItem item = (ToDoItem) itemEnum.nextElement();
	if (item.getPriority() == pn.getPriority()) candidates.addElement(item);
      }
      return candidates.elementAt(index);
    }
    System.out.println("getChild shouldnt get here GoListToPriorityToItem");
    return null;
  }
  
  public int getChildCount(Object parent) {
    if (parent instanceof ToDoList) {
      return PriorityNode.getPriorities().size();
    }
    if (parent instanceof PriorityNode) {
      // instead of makning a new vector, decrement index, return when
      // found and index == 0
      Vector candidates = new Vector();
      PriorityNode pn = (PriorityNode) parent;
      Enumeration itemEnum = Designer.TheDesigner.getToDoList().elements();
      while (itemEnum.hasMoreElements()) {
	ToDoItem item = (ToDoItem) itemEnum.nextElement();
	if (item.getPriority() == pn.getPriority()) candidates.addElement(item);
      }
      return candidates.size();
    }
    return 0;
  }
  
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof ToDoList) {
      return PriorityNode.getPriorities().indexOf(child);
    }
    if (parent instanceof PriorityNode) {
      // instead of makning a new vector, decrement index, return when
      // found and index == 0
      Vector candidates = new Vector();
      PriorityNode pn = (PriorityNode) parent;
      Enumeration itemEnum = Designer.TheDesigner.getToDoList().elements();
      while (itemEnum.hasMoreElements()) {
	ToDoItem item = (ToDoItem) itemEnum.nextElement();
	if (item.getPriority() == pn.getPriority()) candidates.addElement(item);
      }
      return candidates.indexOf(child);
    }
    return -1;
  }

  public boolean isLeaf(Object node) {
    if (node instanceof ToDoList) return false;
    if (node instanceof PriorityNode && getChildCount(node) > 0) return false;
    return true;
  }

  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }

  
//     Vector removes = new Vector();
//     java.util.Enumeration enum = _pseudoNodes.elements();
//     while (enum.hasMoreElements()) {
//       ToDoPseudoNode node = (ToDoPseudoNode) enum.nextElement();
//       if (!isNeeded(node)) removes.addElement(node);
//     }
//     enum = removes.elements();
//     while (enum.hasMoreElements())
//       _pseudoNodes.removeElement(enum.nextElement());
//     enum = _root.elements();
//     while (enum.hasMoreElements()) {
//       ToDoItem item = (ToDoItem) enum.nextElement();
//       addNewPseudoNodes(item);
//     }
    
//     Object path[] = new Object[2];
//     path[0] = _root;
//     enum = _pseudoNodes.elements();
//     while (enum.hasMoreElements()) {
//       ToDoPseudoNode node = (ToDoPseudoNode) enum.nextElement();
//       path[1] = node;
//       fireTreeStructureChanged(path);
//     }

  
} /* end class GoListToDecisionsToItems */
