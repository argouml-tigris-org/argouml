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

import org.apache.commons.logging.Log;
import org.argouml.cognitive.*;

public class ToDoByType extends ToDoPerspective
implements ToDoListListener {
    protected static Log logger = 
        org.apache.commons.logging.LogFactory.getLog(ToDoByType.class);

  public ToDoByType() {
    super("todo.perspective.type");
    addSubTreeModel(new GoListToTypeToItem());
  }


  ////////////////////////////////////////////////////////////////
  // ToDoListListener implementation

  public void toDoItemsChanged(ToDoListEvent tde) {
    logger.debug("toDoItemsChanged");
    Vector items = tde.getToDoItems();
    int nItems = items.size();
    Object path[] = new Object[2];
    path[0] = Designer.TheDesigner.getToDoList();

    java.util.Enumeration enum = KnowledgeTypeNode.getTypes().elements();
    while (enum.hasMoreElements()) {
      KnowledgeTypeNode ktn = (KnowledgeTypeNode) enum.nextElement();
      String kt = ktn.getName();
      path[1] = ktn;
      int nMatchingItems = 0;
      for (int i = 0; i < nItems; i++) {
	ToDoItem item = (ToDoItem) items.elementAt(i);
	if (!item.containsKnowledgeType(kt)) continue;
	nMatchingItems++;
      }
      if (nMatchingItems == 0) continue;
      int childIndices[] = new int[nMatchingItems];
      Object children[] = new Object[nMatchingItems];
      nMatchingItems = 0;
      for (int i = 0; i < nItems; i++) {
	ToDoItem item = (ToDoItem) items.elementAt(i);
	if (!item.containsKnowledgeType(kt)) continue;
	childIndices[nMatchingItems] = getIndexOfChild(ktn, item);
	children[nMatchingItems] = item;
	nMatchingItems++;
      }
      fireTreeNodesChanged(this, path, childIndices, children);
    }
  }

  public void toDoItemsAdded(ToDoListEvent tde) {
    logger.debug("toDoItemAdded");
    Vector items = tde.getToDoItems();
    int nItems = items.size();
    Object path[] = new Object[2];
    path[0] = Designer.TheDesigner.getToDoList();

    java.util.Enumeration enum = KnowledgeTypeNode.getTypes().elements();
    while (enum.hasMoreElements()) {
      KnowledgeTypeNode ktn = (KnowledgeTypeNode) enum.nextElement();
      String kt = ktn.getName();
      path[1] = ktn;
      int nMatchingItems = 0;
      for (int i = 0; i < nItems; i++) {
	ToDoItem item = (ToDoItem) items.elementAt(i);
	if (!item.containsKnowledgeType(kt)) continue;
	nMatchingItems++;
      }
      if (nMatchingItems == 0) continue;
      int childIndices[] = new int[nMatchingItems];
      Object children[] = new Object[nMatchingItems];
      nMatchingItems = 0;
      for (int i = 0; i < nItems; i++) {
	ToDoItem item = (ToDoItem) items.elementAt(i);
	if (!item.containsKnowledgeType(kt)) continue;
	childIndices[nMatchingItems] = getIndexOfChild(ktn, item);
	children[nMatchingItems] = item;
	nMatchingItems++;
      }
      fireTreeNodesInserted(this, path, childIndices, children);
    }
  }

  public void toDoItemsRemoved(ToDoListEvent tde) {
    logger.debug("toDoItemRemoved");
    ToDoList list = Designer.TheDesigner.getToDoList(); //source?
    Vector items = tde.getToDoItems();
    int nItems = items.size();
    Object path[] = new Object[2];
    path[0] = Designer.TheDesigner.getToDoList();

    java.util.Enumeration enum = KnowledgeTypeNode.getTypes().elements();
    while (enum.hasMoreElements()) {
      KnowledgeTypeNode ktn = (KnowledgeTypeNode) enum.nextElement();
      boolean anyInKT = false;
      String kt = ktn.getName();
      for (int i = 0; i < nItems; i++) {
	ToDoItem item = (ToDoItem) items.elementAt(i);
	if (item.containsKnowledgeType(kt)) anyInKT = true;
      }
      if (!anyInKT) continue;
      logger.debug("toDoItemRemoved updating PriorityNode");
      path[1] = ktn;
      //fireTreeNodesChanged(this, path, childIndices, children);
      fireTreeStructureChanged(path);
    }
  }

  public void toDoListChanged(ToDoListEvent tde) { }


  
  

} /* end class ToDoByType */


