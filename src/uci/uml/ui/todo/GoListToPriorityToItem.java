// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
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

package uci.uml.ui.todo;

import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;

import uci.argo.kernel.*;
import uci.uml.ui.nav.TreeModelPrereqs;

public class GoListToPriorityToItem implements TreeModelPrereqs {
  
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

  public Vector getPrereqs() {
    Vector res = new Vector();
    res.addElement(ToDoList.class);
    return res;
  }

  public Vector getProvidedTypes() {
    Vector pros = new Vector();
    pros.addElement(PriorityNode.class);
    pros.addElement(ToDoItem.class);
    return pros;
  }

  
} /* end class GoListToPriorityToItem */
