/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.cognitive.Decision;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoListEvent;
import org.argouml.cognitive.ToDoListListener;

/**
 * Represents a perspective for ToDo items: grouping by decision type.
 */
public class ToDoByDecision extends ToDoPerspective
    implements ToDoListListener {
    private static final Logger LOG =
        Logger.getLogger(ToDoByDecision.class.getName());


    /**
     * The constructor.
     *
     */
    public ToDoByDecision() {
	super("combobox.todo-perspective-decision");
	addSubTreeModel(new GoListToDecisionsToItems());
    }

    ////////////////////////////////////////////////////////////////
    // ToDoListListener implementation

    /*
     * @see org.argouml.cognitive.ToDoListListener#toDoItemsChanged(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoItemsChanged(ToDoListEvent tde) {
        LOG.log(Level.FINE, "toDoItemChanged");
        List<ToDoItem> items = tde.getToDoItemList();
	Object[] path = new Object[2];
	path[0] = Designer.theDesigner().getToDoList();

        for (Decision dec : Designer.theDesigner().getDecisionModel()
                .getDecisionList()) {
	    int nMatchingItems = 0;
	    path[1] = dec;
	    for (ToDoItem item : items) {
		if (!item.supports(dec)) {
                    continue;
                }
		nMatchingItems++;
	    }
	    if (nMatchingItems == 0) {
                continue;
            }
	    int[] childIndices = new int[nMatchingItems];
	    Object[] children = new Object[nMatchingItems];
	    nMatchingItems = 0;
            for (ToDoItem item : items) {
		if (!item.supports(dec)) {
                    continue;
                }
		childIndices[nMatchingItems] = getIndexOfChild(dec, item);
		children[nMatchingItems] = item;
		nMatchingItems++;
	    }
	    fireTreeNodesChanged(this, path, childIndices, children);
	}
    }

    /*
     * @see org.argouml.cognitive.ToDoListListener#toDoItemsAdded(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoItemsAdded(ToDoListEvent tde) {
        LOG.log(Level.FINE, "toDoItemAdded");
	List<ToDoItem> items = tde.getToDoItemList();
	Object[] path = new Object[2];
	path[0] = Designer.theDesigner().getToDoList();

        for (Decision dec : Designer.theDesigner().getDecisionModel()
                .getDecisionList()) {
	    int nMatchingItems = 0;
	    path[1] = dec;
            for (ToDoItem item : items) {
		if (!item.supports(dec)) {
                    continue;
                }
		nMatchingItems++;
	    }
	    if (nMatchingItems == 0) {
                continue;
            }
	    int[] childIndices = new int[nMatchingItems];
	    Object[] children = new Object[nMatchingItems];
	    nMatchingItems = 0;
            for (ToDoItem item : items) {
		if (!item.supports(dec)) {
                    continue;
                }
		childIndices[nMatchingItems] = getIndexOfChild(dec, item);
		children[nMatchingItems] = item;
		nMatchingItems++;
	    }
	    fireTreeNodesInserted(this, path, childIndices, children);
	}
    }

    /*
     * @see org.argouml.cognitive.ToDoListListener#toDoItemsRemoved(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoItemsRemoved(ToDoListEvent tde) {
        LOG.log(Level.FINE, "toDoItemRemoved");
	List<ToDoItem> items = tde.getToDoItemList();
	Object[] path = new Object[2];
	path[0] = Designer.theDesigner().getToDoList();

        for (Decision dec : Designer.theDesigner().getDecisionModel()
                .getDecisionList()) {
            LOG.log(Level.FINE, "toDoItemRemoved updating decision node!");
	    boolean anyInDec = false;
            for (ToDoItem item : items) {
		if (item.supports(dec)) {
                    anyInDec = true;
                }
	    }
	    if (!anyInDec) {
                continue;
            }
	    path[1] = dec;
	    //fireTreeNodesChanged(this, path, childIndices, children);
	    fireTreeStructureChanged(path);
	}
    }

    /*
     * @see org.argouml.cognitive.ToDoListListener#toDoListChanged(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoListChanged(ToDoListEvent tde) { }


    //   public static Decision decisionUNCATEGORIZED =
    //   new Decision("Uncategorized", 1);

    //   protected boolean isNeeded(ToDoPseudoNode node) {
    //     PredicateDecision pd = (PredicateDecision) node.getPredicate();
    //     Decision d = pd.getDecision();
    //     Enumeration items = _root.elements();
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
    //       Enumeration elems = decs.elements();
    //       while (elems.hasMoreElements()) {
    // 	Decision itemDec = (Decision) elems.nextElement();
    // 	addNodeIfNeeded(itemDec, newNodes);
    //       }
    //     }
    //     return newNodes;
    //   }


    //   protected void addNodeIfNeeded(Decision itemDec, Vector newNodes) {
    //     Enumeration elems = _pseudoNodes.elements();
    //     while (elems.hasMoreElements()) {
    //       ToDoPseudoNode node = (ToDoPseudoNode) elems.nextElement();
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
