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
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.argouml.ui.*;
import org.argouml.cognitive.*;

public class GoListToDecisionsToItems implements TreeModel {
  
    ////////////////////////////////////////////////////////////////
    // TreeModel implementation
  
    public Object getRoot() {
	throw new UnsupportedOperationException();
    } 
    public void setRoot(Object r) { }

    public Object getChild(Object parent, int index) {
	if (parent instanceof ToDoList) {
	    return getDecisions().elementAt(index);
	}
	if (parent instanceof Decision) {
	    Decision dec = (Decision) parent;
	    Enumeration itemEnum =
		Designer.TheDesigner.getToDoList().elements();
	    while (itemEnum.hasMoreElements()) {
		ToDoItem item = (ToDoItem) itemEnum.nextElement();
		if (item.getPoster().supports(dec)) {
		    if (index == 0) return item;
		    index--;
		}
	    }
	}

	throw new IndexOutOfBoundsException("getChild shouldn't get here "
					    + "GoListToDecisionsToItems");
    }
  
    private int getChildCountCond(Object parent, boolean stopafterone) {
	if (parent instanceof ToDoList) {
	    return getDecisions().size();
	}
	if (parent instanceof Decision) {
	    Decision dec = (Decision) parent;
	    Enumeration itemEnum =
		Designer.TheDesigner.getToDoList().elements();
	    int count = 0;
	    while (itemEnum.hasMoreElements()) {
		ToDoItem item = (ToDoItem) itemEnum.nextElement();
		if (item.getPoster().supports(dec)) count++;
		if (stopafterone && count > 0) break;
	    }
	    return count;
	}
	return 0;
    }
  
    public int getChildCount(Object parent) {
        return getChildCountCond(parent, false);
    }
    private boolean hasChildren(Object parent) {
        return getChildCountCond(parent, true) > 0;
    }
      
  
    public int getIndexOfChild(Object parent, Object child) {
	if (parent instanceof ToDoList) {
	    return getDecisions().indexOf(child);
	}
	if (parent instanceof Decision) {
	    // instead of makning a new vector, decrement index, return when
	    // found and index == 0
	    Vector candidates = new Vector();
	    Decision dec = (Decision) parent;
	    Enumeration itemEnum =
		Designer.TheDesigner.getToDoList().elements();
	    while (itemEnum.hasMoreElements()) {
		ToDoItem item = (ToDoItem) itemEnum.nextElement();
		if (item.getPoster().supports(dec)) candidates.addElement(item);
	    }
	    return candidates.indexOf(child);
	}
	return -1;
    }

    public boolean isLeaf(Object node) {
	if (node instanceof ToDoList) return false;
	if (node instanceof Decision && hasChildren(node)) return false;
	return true;
    }

    public void valueForPathChanged(TreePath path, Object newValue) { }
    public void addTreeModelListener(TreeModelListener l) { }
    public void removeTreeModelListener(TreeModelListener l) { }

    ////////////////////////////////////////////////////////////////
    // utility methods

    public Vector getDecisions() {
	return Designer.TheDesigner.getDecisionModel().getDecisions();
    }
  


  
} /* end class GoListToDecisionsToItems */
