// $Id$
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

import org.apache.log4j.Category;
import org.argouml.cognitive.*;

public class ToDoByDecision extends ToDoPerspective
    implements ToDoListListener
{
    protected static Category cat = 
        Category.getInstance(ToDoByDecision.class);


    public ToDoByDecision() {
	super("combobox.todo-perspective-decision");
	addSubTreeModel(new GoListToDecisionsToItems());
    }

    //public String toString() { return "Decision"; }

    ////////////////////////////////////////////////////////////////
    // DecisionModelListener implementation

    ////////////////////////////////////////////////////////////////
    // ToDoListListener implementation

    public void toDoItemsChanged(ToDoListEvent tde) {
	cat.debug("toDoItemChanged");
	Vector items = tde.getToDoItems();
	int nItems = items.size();
	Object path[] = new Object[2];
	path[0] = Designer.TheDesigner.getToDoList();

	Vector decs = Designer.TheDesigner.getDecisions();
	java.util.Enumeration enum = decs.elements();
	while (enum.hasMoreElements()) {
	    Decision dec = (Decision) enum.nextElement();
	    int nMatchingItems = 0;
	    path[1] = dec;
	    for (int i = 0; i < nItems; i++) {
		ToDoItem item = (ToDoItem) items.elementAt(i);
		if (!item.supports(dec)) continue;
		nMatchingItems++;
	    }
	    if (nMatchingItems == 0) continue;
	    int childIndices[] = new int[nMatchingItems];
	    Object children[] = new Object[nMatchingItems];
	    nMatchingItems = 0;
	    for (int i = 0; i < nItems; i++) {
		ToDoItem item = (ToDoItem) items.elementAt(i);
		if (!item.supports(dec)) continue;
		childIndices[nMatchingItems] = getIndexOfChild(dec, item);
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

	Vector decs = Designer.TheDesigner.getDecisions();
	java.util.Enumeration enum = decs.elements();
	while (enum.hasMoreElements()) {
	    Decision dec = (Decision) enum.nextElement();
	    int nMatchingItems = 0;
	    path[1] = dec;
	    for (int i = 0; i < nItems; i++) {
		ToDoItem item = (ToDoItem) items.elementAt(i);
		if (!item.supports(dec)) continue;
		nMatchingItems++;
	    }
	    if (nMatchingItems == 0) continue;
	    int childIndices[] = new int[nMatchingItems];
	    Object children[] = new Object[nMatchingItems];
	    nMatchingItems = 0;
	    for (int i = 0; i < nItems; i++) {
		ToDoItem item = (ToDoItem) items.elementAt(i);
		if (!item.supports(dec)) continue;
		childIndices[nMatchingItems] = getIndexOfChild(dec, item);
		children[nMatchingItems] = item;
		nMatchingItems++;
	    }
	    fireTreeNodesInserted(this, path, childIndices, children);
	}
    }

    public void toDoItemsRemoved(ToDoListEvent tde) {
	cat.debug("toDoItemRemoved");
	ToDoList list = Designer.TheDesigner.getToDoList(); //source?
	Vector items = tde.getToDoItems();
	int nItems = items.size();
	Object path[] = new Object[2];
	path[0] = Designer.TheDesigner.getToDoList();

	Vector decs = Designer.TheDesigner.getDecisions();
	java.util.Enumeration enum = decs.elements();
	while (enum.hasMoreElements()) {
	    Decision dec = (Decision) enum.nextElement();
	    cat.debug("toDoItemRemoved updating decision node!");
	    boolean anyInDec = false;
	    for (int i = 0; i < nItems; i++) {
		ToDoItem item = (ToDoItem) items.elementAt(i);
		if (item.supports(dec)) anyInDec = true;
	    }
	    if (!anyInDec) continue;
	    path[1] = dec;
	    //fireTreeNodesChanged(this, path, childIndices, children);
	    fireTreeStructureChanged(path);
	}
    }

    public void toDoListChanged(ToDoListEvent tde) { }


    //   public static Decision decisionUNCATEGORIZED =
    //   new Decision("Uncategorized", 1);

    //   protected boolean isNeeded(ToDoPseudoNode node) {
    //     PredicateDecision pd = (PredicateDecision) node.getPredicate();
    //     Decision d = pd.getDecision();
    //     java.util.Enumeration items = _root.elements();
    //     while (items.hasMoreElements()) {
    //       ToDoItem item = (ToDoItem) items.nextElement();
    //       if (item.getPoster().supports(d)) return true;
    //     }
    //     return false;
    //   }

    //   protected Vector addNewPseudoNodes(ToDoItem item) {
    //     Vector newNodes = new Vector();
    //     Vector decs = item.getPoster().getSupportedDecisions();
    //     if (decs == null) {
    //       addNodeIfNeeded(Decision.UNSPEC, newNodes);
    //     }
    //     else {
    //       java.util.Enumeration enum = decs.elements();
    //       while (enum.hasMoreElements()) {
    // 	Decision itemDec = (Decision) enum.nextElement();
    // 	addNodeIfNeeded(itemDec, newNodes);
    //       }
    //     }
    //     return newNodes;
    //   }


    //   protected void addNodeIfNeeded(Decision itemDec, Vector newNodes) {
    //     java.util.Enumeration enum2 = _pseudoNodes.elements();
    //     while (enum2.hasMoreElements()) {
    //       ToDoPseudoNode node = (ToDoPseudoNode) enum2.nextElement();
    //       PredicateDecision pd = (PredicateDecision) node.getPredicate();
    //       Decision nodeDec = pd.getDecision();
    //       //if (nodeDec.getName().equals(itemDec.getName())) return;
    //       if (nodeDec == itemDec) return;
    //     }
    //     PredicateDecision pred = new PredicateDecision(itemDec);
    //     ToDoPseudoNode newNode = new ToDoPseudoNode(_root, pred);
    //     newNode.setLabel(itemDec.getName());
    //     _pseudoNodes.addElement(newNode);
    //     newNodes.addElement(newNode);
    //   }

} /* end class ToDoByDecision */


