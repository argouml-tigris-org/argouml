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

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;

import org.apache.log4j.Category;
import org.argouml.cognitive.*;

public class ToDoByGoal extends ToDoPerspective
implements ToDoListListener {
    protected static Category cat = 
        Category.getInstance(ToDoByGoal.class);


  public ToDoByGoal() {
    super("todo.perspective.goal");
    addSubTreeModel(new GoListToGoalsToItems());
  }

  ////////////////////////////////////////////////////////////////
  // ToDoListListener implementation

  public void toDoItemsChanged(ToDoListEvent tde) {
    cat.debug("toDoItemsChanged");
    Vector items = tde.getToDoItems();
    int nItems = items.size();
    Object path[] = new Object[2];
    path[0] = Designer.TheDesigner.getToDoList();

    Vector goals = Designer.TheDesigner.getGoals();
    java.util.Enumeration enum = goals.elements();
    while (enum.hasMoreElements()) {
      Goal g = (Goal) enum.nextElement();
      path[1] = g;
      int nMatchingItems = 0;
      for (int i = 0; i < nItems; i++) {
	ToDoItem item = (ToDoItem) items.elementAt(i);
	if (!item.supports(g)) continue;
	nMatchingItems++;
      }
      if (nMatchingItems == 0) continue;
      int childIndices[] = new int[nMatchingItems];
      Object children[] = new Object[nMatchingItems];
      nMatchingItems = 0;
      for (int i = 0; i < nItems; i++) {
	ToDoItem item = (ToDoItem) items.elementAt(i);
	if (!item.supports(g)) continue;
	childIndices[nMatchingItems] = getIndexOfChild(g, item);
	children[nMatchingItems] = item;
	nMatchingItems++;
      }
      fireTreeNodesChanged(this, path, childIndices, children);
    }
  }

  public void toDoItemsAdded(ToDoListEvent tde) {
    cat.debug("toDoItemAdded");
    Vector items = tde.getToDoItems();
    int nItems = items.size();
    Object path[] = new Object[2];
    path[0] = Designer.TheDesigner.getToDoList();

    Vector goals = Designer.TheDesigner.getGoals();
    java.util.Enumeration enum = goals.elements();
    while (enum.hasMoreElements()) {
      Goal g = (Goal) enum.nextElement();
      path[1] = g;
      int nMatchingItems = 0;
      for (int i = 0; i < nItems; i++) {
	ToDoItem item = (ToDoItem) items.elementAt(i);
	if (!item.supports(g)) continue;
	nMatchingItems++;
      }
      if (nMatchingItems == 0) continue;
      int childIndices[] = new int[nMatchingItems];
      Object children[] = new Object[nMatchingItems];
      nMatchingItems = 0;
      for (int i = 0; i < nItems; i++) {
	ToDoItem item = (ToDoItem) items.elementAt(i);
	if (!item.supports(g)) continue;
	childIndices[nMatchingItems] = getIndexOfChild(g, item);
	children[nMatchingItems] = item;
	nMatchingItems++;
      }
      fireTreeNodesInserted(this, path, childIndices, children);
    }
  }

  public void toDoItemsRemoved(ToDoListEvent tde) {
    cat.debug("toDoItemAdded");
    ToDoList list = Designer.TheDesigner.getToDoList(); //source?
    Vector items = tde.getToDoItems();
    int nItems = items.size();
    Object path[] = new Object[2];
    path[0] = Designer.TheDesigner.getToDoList();

    Vector goals = Designer.TheDesigner.getGoals();
    java.util.Enumeration enum = goals.elements();
    while (enum.hasMoreElements()) {
      Goal g = (Goal) enum.nextElement();
      cat.debug("toDoItemRemoved updating decision node!");
      boolean anyInGoal = false;
      for (int i = 0; i < nItems; i++) {
	ToDoItem item = (ToDoItem) items.elementAt(i);
	if (item.supports(g)) anyInGoal = true;
      }
      if (!anyInGoal) continue;
      path[1] = g;
      //fireTreeNodesChanged(this, path, childIndices, children);
      fireTreeStructureChanged(path);
    }
  }

  public void toDoListChanged(ToDoListEvent tde) { }


} /* end class ToDoByGoal */


