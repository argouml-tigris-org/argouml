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
import org.apache.log4j.Logger;


import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.cognitive.ToDoListEvent;
import org.argouml.cognitive.ToDoListListener;
import org.argouml.cognitive.ListSet;

/**
 * Represents a perspective for ToDo items: grouping by poster type.
 *
 */
public class ToDoByPoster extends ToDoPerspective
    implements ToDoListListener {
    private static final Logger LOG =
        Logger.getLogger(ToDoByPoster.class);

    /**
     * The constructor.
     *
     */
    public ToDoByPoster() {
	super("combobox.todo-perspective-poster");
	addSubTreeModel(new GoListToPosterToItem());
    }

    ////////////////////////////////////////////////////////////////
    // ToDoListListener implementation

    /**
     * @see org.argouml.cognitive.ToDoListListener#toDoItemsChanged(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoItemsChanged(ToDoListEvent tde) {
	LOG.debug("toDoItemsChanged");
	Vector items = tde.getToDoItems();
	int nItems = items.size();
	Object[] path = new Object[2];
	path[0] = Designer.theDesigner().getToDoList();

	ListSet posters = Designer.theDesigner().getToDoList().getPosters();
	Enumeration elems = posters.elements();
	while (elems.hasMoreElements()) {
	    Poster p = (Poster) elems.nextElement();
	    path[1] = p;
	    int nMatchingItems = 0;
	    for (int i = 0; i < nItems; i++) {
		ToDoItem item = (ToDoItem) items.elementAt(i);
		Poster post = item.getPoster();
		if (post != p) continue;
		nMatchingItems++;
	    }
	    if (nMatchingItems == 0) continue;
	    int[] childIndices = new int[nMatchingItems];
	    Object[] children = new Object[nMatchingItems];
	    nMatchingItems = 0;
	    for (int i = 0; i < nItems; i++) {
		ToDoItem item = (ToDoItem) items.elementAt(i);
		Poster post = item.getPoster();
		if (post != p) continue;
		childIndices[nMatchingItems] = getIndexOfChild(p, item);
		children[nMatchingItems] = item;
		nMatchingItems++;
	    }
	    fireTreeNodesChanged(this, path, childIndices, children);
	}
    }

    /**
     * @see org.argouml.cognitive.ToDoListListener#toDoItemsAdded(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoItemsAdded(ToDoListEvent tde) {
	LOG.debug("toDoItemAdded");
	Vector items = tde.getToDoItems();
	int nItems = items.size();
	Object[] path = new Object[2];
	path[0] = Designer.theDesigner().getToDoList();

	ListSet posters = Designer.theDesigner().getToDoList().getPosters();
	Enumeration elems = posters.elements();
	while (elems.hasMoreElements()) {
	    Poster p = (Poster) elems.nextElement();
	    path[1] = p;
	    int nMatchingItems = 0;
	    for (int i = 0; i < nItems; i++) {
		ToDoItem item = (ToDoItem) items.elementAt(i);
		Poster post = item.getPoster();
		if (post != p) continue;
		nMatchingItems++;
	    }
	    if (nMatchingItems == 0) continue;
	    int[] childIndices = new int[nMatchingItems];
	    Object[] children = new Object[nMatchingItems];
	    nMatchingItems = 0;
	    for (int i = 0; i < nItems; i++) {
		ToDoItem item = (ToDoItem) items.elementAt(i);
		Poster post = item.getPoster();
		if (post != p) continue;
		childIndices[nMatchingItems] = getIndexOfChild(p, item);
		children[nMatchingItems] = item;
		nMatchingItems++;
	    }
	    fireTreeNodesInserted(this, path, childIndices, children);
	}
    }

    /**
     * @see org.argouml.cognitive.ToDoListListener#toDoItemsRemoved(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoItemsRemoved(ToDoListEvent tde) {
	LOG.debug("toDoItemRemoved");
	ToDoList list = Designer.theDesigner().getToDoList(); //source?
	Object[] path = new Object[2];
	path[0] = Designer.theDesigner().getToDoList();


	Enumeration elems = list.getPosters().elements();
	while (elems.hasMoreElements()) {
	    Poster p = (Poster) elems.nextElement();
	    //       boolean anyInPoster = false;
	    //       for (int i = 0; i < nItems; i++) {
	    // 	ToDoItem item = (ToDoItem) items.elementAt(i);
	    // 	Poster post = item.getPoster();
	    // 	if (post == p) anyInPoster = true;
	    //       }
	    //       if (!anyInPoster) continue;
	    path[1] = p;
	    fireTreeStructureChanged(path);
	}
    }

    /**
     * @see org.argouml.cognitive.ToDoListListener#toDoListChanged(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoListChanged(ToDoListEvent tde) { }


} /* end class ToDoByPoster */


