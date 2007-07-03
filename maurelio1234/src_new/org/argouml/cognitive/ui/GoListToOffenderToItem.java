// $Id: GoListToOffenderToItem.java 12950 2007-07-01 08:10:04Z tfmorris $
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
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
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
	return getChildrenList(parent).get(index);
    }

    /*
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    public int getChildCount(Object parent) {
	return getChildrenList(parent).size();
    }

    /*
     * @see javax.swing.tree.TreeModel#getIndexOfChild(
     * java.lang.Object, java.lang.Object)
     */
    public int getIndexOfChild(Object parent, Object child) {
	return getChildrenList(parent).indexOf(child);
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
     * @deprecated for 0.25.4 by tfmorris.  Use 
     * {@link #getChildrenList(Object)}.
     */
    @Deprecated
    public Vector<ToDoItem> getChildren(Object parent) {
        List<ToDoItem> result = getChildrenList(parent);
        if (result.size() == 0) {
            return null;
        }
        return new Vector<ToDoItem>(result);
    }

    /**
     * Get a list of children.
     * 
     * @param parent
     *            the parent object to check
     * @return a list of children for the given object
     */
    public List<ToDoItem> getChildrenList(Object parent) {
        ListSet<ToDoItem> allOffenders = new ListSet<ToDoItem>();
        allOffenders.addAllElementsSuchThat(
                Designer.theDesigner().getToDoList().getOffenders(), 
                getListPredicate());

        if (parent instanceof ToDoList) {
            return allOffenders;
        }
        
        //otherwise parent must be an offending design material
        if (allOffenders.contains(parent)) {
            List<ToDoItem> result = new ArrayList<ToDoItem>();
            for (ToDoItem item : Designer.theDesigner().getToDoList()) {
                ListSet offs = new ListSet();
                offs.addAllElementsSuchThat(item.getOffenders(),
                    getListPredicate());
                if (offs.contains(parent)) {
                    result.add(item);
                }
            }
            return result;
        }
        return Collections.EMPTY_LIST;
    }
    
    /*
     * @see javax.swing.tree.TreeModel#valueForPathChanged(
     * javax.swing.tree.TreePath, java.lang.Object)
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    /*
     * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
     */
    public void addTreeModelListener(TreeModelListener l) {
    }

    /*
     * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
     */
    public void removeTreeModelListener(TreeModelListener l) {
    }


} 
