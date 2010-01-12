/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    linus
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;



/**
 * Rule for sorting the ToDo list: Poster -> Item.
 *
 */
public class GoListToPosterToItem extends AbstractGoList2 {

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
	if (node instanceof ToDoList) {
	    return false;
	}
	if (getChildCount(node) > 0) {
	    return false;
	}
	return true;
    }

    
    /**
     * Get the children of the given parent.  Unlike getChildren(),
     * this never returns null, instead returning an empty list if
     * there are no children.
     * 
     * @param parent the parent object to check for offspring
     * @return the children
     */
    public List getChildrenList(Object parent) {
        ListSet allPosters =
            Designer.theDesigner().getToDoList().getPosters();
        if (parent instanceof ToDoList) {
            return allPosters;
        }
        //otherwise parent must be an offending design material
        if (allPosters.contains(parent)) {
            List<ToDoItem> result = new ArrayList<ToDoItem>();
            List<ToDoItem> itemList = 
                Designer.theDesigner().getToDoList().getToDoItemList();
            synchronized (itemList) {
                for (ToDoItem item : itemList) {
                    Poster post = item.getPoster();
                    if (post == parent) {
                        result.add(item);
                    }
                }
            }
            return result;
        }
        return Collections.emptyList();
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
