// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;


/**
 * Rule for sorting the ToDo list: Type -> Item.
 *
 */
public class GoListToTypeToItem extends AbstractGoList {

    ////////////////////////////////////////////////////////////////
    // TreeModel implementation


    /*
     * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
     */
    public Object getChild(Object parent, int index) {
	if (parent instanceof ToDoList) {
	    return KnowledgeTypeNode.getTypeList().get(index);
	}
	if (parent instanceof KnowledgeTypeNode) {
	    KnowledgeTypeNode ktn = (KnowledgeTypeNode) parent;
            for (ToDoItem item : Designer.theDesigner().getToDoList()) {
		if (item.containsKnowledgeType(ktn.getName())) {
		    if (index == 0) return item;
		    index--;
		}
	    }
	}
	throw new IndexOutOfBoundsException("getChild shouldnt get here "
					    + "GoListToTypeToItem");
    }

    /*
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    public int getChildCount(Object parent) {
	if (parent instanceof ToDoList) {
	    return KnowledgeTypeNode.getTypeList().size();
	}
	if (parent instanceof KnowledgeTypeNode) {
	    KnowledgeTypeNode ktn = (KnowledgeTypeNode) parent;
	    int count = 0;
            for (ToDoItem item : Designer.theDesigner().getToDoList()) {
		if (item.containsKnowledgeType(ktn.getName())) {
		    count++;
                }
	    }
	    return count;
	}
	return 0;
    }

    /*
     * @see javax.swing.tree.TreeModel#getIndexOfChild(
     * java.lang.Object, java.lang.Object)
     */
    public int getIndexOfChild(Object parent, Object child) {
	if (parent instanceof ToDoList) {
	    return KnowledgeTypeNode.getTypeList().indexOf(child);
	}
	if (parent instanceof KnowledgeTypeNode) {
	    // instead of makning a new vector, decrement index, return when
	    // found and index == 0
	    List<ToDoItem> candidates = new ArrayList<ToDoItem>();
	    KnowledgeTypeNode ktn = (KnowledgeTypeNode) parent;
            for (ToDoItem item : Designer.theDesigner().getToDoList()) {
		if (item.containsKnowledgeType(ktn.getName())) {
		    candidates.add(item);
                }
	    }
	    return candidates.indexOf(child);
	}
	return -1;
    }

    /*
     * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
     */
    public boolean isLeaf(Object node) {
	if (node instanceof ToDoList) return false;
	if (node instanceof KnowledgeTypeNode && getChildCount(node) > 0)
	    return false;
	return true;
    }

    /*
     * @see javax.swing.tree.TreeModel#valueForPathChanged(
     * javax.swing.tree.TreePath, java.lang.Object)
     */
    public void valueForPathChanged(TreePath path, Object newValue) { }

    /*
     * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
     */
    public void addTreeModelListener(TreeModelListener l) { }

    /*
     * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
     */
    public void removeTreeModelListener(TreeModelListener l) { }

} 
