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

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.cognitive.ListSet;
import org.argouml.uml.PredicateNotInTrash;


/**
 * Rule for sorting the ToDo list: Offender -> Item.
 *
 */
public class GoListToOffenderToItem extends AbstractGoList {

    /**
     * The constructor.
     */
    public GoListToOffenderToItem() {
        setListPredicate(new PredicateNotInTrash());
    }

    ////////////////////////////////////////////////////////////////
    // TreeModel implementation

    /*
     * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
     */
    public Object getChild(Object parent, int index) {
	Vector children = getChildren(parent);
	return (children == null) ? null : children.elementAt(index);
    }

    /*
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    public int getChildCount(Object parent) {
	Vector children = getChildren(parent);
	return (children == null) ? 0 : children.size();
    }

    /*
     * @see javax.swing.tree.TreeModel#getIndexOfChild(
     * java.lang.Object, java.lang.Object)
     */
    public int getIndexOfChild(Object parent, Object child) {
	Vector children = getChildren(parent);
	return (children == null) ? -1 : children.indexOf(child);
    }

    /*
     * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
     */
    public boolean isLeaf(Object node) {
	if (node instanceof ToDoList) return false;
	if (getChildCount(node) > 0) return false;
	return true;
    }


    /**
     * @param parent the parent object to check for offspring
     * @return the children
     */
    public Vector getChildren(Object parent) {
        ListSet allOffenders = new ListSet();
        allOffenders.addAllElementsSuchThat(Designer.theDesigner()
            .getToDoList().getOffenders(),
            getListPredicate());


	if (parent instanceof ToDoList) {
	    return allOffenders.asVector();
	}
	//otherwise parent must be an offending design material
	if (allOffenders.contains(parent)) {
	    Vector res = new Vector();
	    ToDoList list = Designer.theDesigner().getToDoList();
	    Enumeration elems = list.elements();
	    while (elems.hasMoreElements()) {
		ToDoItem item = (ToDoItem) elems.nextElement();
		ListSet offs = new ListSet();
                offs.addAllElementsSuchThat(item.getOffenders(),
                    getListPredicate());
		if (offs.contains(parent)) res.addElement(item);
	    }
	    return res;
	}
	return null;
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


} /* end class GoListToOffenderToItem */
