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

import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.cognitive.ToDoItem;
import org.argouml.ui.TreeModelComposite;

/**
 * This class represents a todo tree model / perspective.<p>
 *
 * A todo tree model / perspective is a collection of GoRules.
 */
public abstract class ToDoPerspective extends TreeModelComposite {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ToDoPerspective.class);

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * todoList specific.
     */
    private boolean flat;

    /**
     * todoList specific.
     */
    private Vector flatChildren;

    /**
     * The constructor.
     *
     * @param name the name that will be localized
     */
    public ToDoPerspective(String name) {

        super(name);
        flatChildren = new Vector();
    }

    ////////////////////////////////////////////////////////////////
    // TreeModel implementation - todo specific stuff

    /**
     * Finds the each of the children of a parent in the tree.
     *
     * @param parent in the tree
     * @param index of child to find
     * @return the child found at index. Null if index is out of bounds.
     */
    public Object getChild(Object parent, int index) {
        if (flat && parent == getRoot()) {
            return flatChildren.elementAt(index);
        }
        return super.getChild(parent,  index);
    }

    /*
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    public int getChildCount(Object parent) {
        if (flat && parent == getRoot()) {
            return flatChildren.size();
        }
        return super.getChildCount(parent);
    }

    /*
     * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object,
     *      java.lang.Object)
     */
    public int getIndexOfChild(Object parent, Object child) {
        if (flat && parent == getRoot()) {
            return flatChildren.indexOf(child);
        }
        return super.getIndexOfChild(parent, child);
    }

    // ------------ other methods ------------

    /**
     * todoList specific.
     *
     * @param b true if flat
     */
    public void setFlat(boolean b) {
        flat = false;
        if (b) {
	    calcFlatChildren();
	}
        flat = b;
    }

    /**
     * todoList specific.
     *
     * @return the flatness: true if flat
     */
    public boolean getFlat() { return flat; }

    /**
     * TodoList specific.
     */
    public void calcFlatChildren() {
        flatChildren.removeAllElements();
        addFlatChildren(getRoot());
    }

    /**
     * TodoList specific.
     *
     * @param node the object to be added
     */
    public void addFlatChildren(Object node) {
        if (node == null) {
	    return;
	}
        LOG.debug("addFlatChildren");
        // hack for to do items only, should check isLeaf(node), but that
        // includes empty folders. Really I need alwaysLeaf(node).
        if ((node instanceof ToDoItem) && !flatChildren.contains(node)) {
            flatChildren.addElement(node);
	}

        int nKids = getChildCount(node);
        for (int i = 0; i < nKids; i++) {
            addFlatChildren(getChild(node, i));
        }
    }

} /* end class ToDoPerspective */
