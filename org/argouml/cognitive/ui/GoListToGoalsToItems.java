// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.cognitive.ui;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.argouml.ui.*;
import org.argouml.cognitive.*;

public class GoListToGoalsToItems implements TreeModel {
  
  ////////////////////////////////////////////////////////////////
  // TreeModel implementation
  
  public Object getRoot() {
      throw new UnsupportedOperationException("getRoot should never be called");
  } 
  public void setRoot(Object r) { }

  public Object getChild(Object parent, int index) {
    if (parent instanceof ToDoList) {
      return getGoals().elementAt(index);
    }
    if (parent instanceof Goal) {
      Goal g = (Goal) parent;
      java.util.Enumeration itemEnum = Designer.TheDesigner.getToDoList().elements();
      while (itemEnum.hasMoreElements()) {
	ToDoItem item = (ToDoItem) itemEnum.nextElement();
	if (item.getPoster().supports(g)) {
	    if (index == 0) return item;
	    index--;
	}
      }
    }
    throw new IndexOutOfBoundsException("getChild shouldnt get here GoListToGoalsToItems");
  }
  
  public int getChildCount(Object parent) {
    if (parent instanceof ToDoList) {
      return getGoals().size();
    }
    if (parent instanceof Goal) {
      Goal g = (Goal) parent;
      java.util.Enumeration itemEnum = Designer.TheDesigner.getToDoList().elements();
      int count = 0;
      while (itemEnum.hasMoreElements()) {
	ToDoItem item = (ToDoItem) itemEnum.nextElement();
	if (item.getPoster().supports(g)) count++;
      }
      return count;
    }
    return 0;
  }
  
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof ToDoList) {
      return getGoals().indexOf(child);
    }
    if (parent instanceof Goal) {
      // instead of makning a new vector, decrement index, return when
      // found and index == 0
      Vector candidates = new Vector();
      Goal g = (Goal) parent;
      java.util.Enumeration itemEnum = Designer.TheDesigner.getToDoList().elements();
      while (itemEnum.hasMoreElements()) {
	ToDoItem item = (ToDoItem) itemEnum.nextElement();
	if (item.getPoster().supports(g)) candidates.addElement(item);
      }
      return candidates.indexOf(child);
    }
    return -1;
  }

  public boolean isLeaf(Object node) {
    if (node instanceof ToDoList) return false;
    if (node instanceof Goal && getChildCount(node) > 0) return false;
    return true;
  }

  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }



  ////////////////////////////////////////////////////////////////
  // utility methods

  public Vector getGoals() {
    return Designer.TheDesigner.getGoalModel().getGoals();
  }
  
} /* end class GoListToGoalsToItems */
