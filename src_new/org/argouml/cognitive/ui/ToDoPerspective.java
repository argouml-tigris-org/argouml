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

import java.io.Serializable;
import java.util.Vector;
import org.apache.log4j.Logger;


import org.argouml.cognitive.ToDoItem;
import org.argouml.ui.TreeModelComposite;

/**
 *
 *<pre>
 * This class represents:
 *   - a todo tree model / perspective (which is a collection of GoRules)
 *</pre>
 *
 * $Id$
 */
public abstract class ToDoPerspective extends TreeModelComposite
    implements Serializable 
{
    
    private static Logger cat = Logger.getLogger(ToDoPerspective.class);
    
    ////////////////////////////////////////////////////////////////
    // instance variables
    
    /** todoList specific */
    protected boolean _flat;
    
    /** todoList specific */
    protected Vector _flatChildren;
    
    ////////////////////////////////////////////////////////////////
    // constructor
    public ToDoPerspective(String name) {
        
        super(name);
        _flatChildren = new Vector();
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
        if (_flat && parent == _root) {
            return _flatChildren.elementAt(index);
        }
        return super.getChild( parent,  index);
    }
    
    /** needs documenting */
    public int getChildCount(Object parent) {
        if (_flat && parent == _root) {
            return _flatChildren.size();
        }
        return super.getChildCount( parent);
    }
    
    /** needs documenting */
    public int getIndexOfChild(Object parent, Object child) {
        if (_flat && parent == _root) {
            return _flatChildren.indexOf(child);
        }
        return super.getIndexOfChild(parent, child);
    }
    
    // ------------ other methods ------------
    
    /** todoList specific */
    public void setFlat(boolean b) {
        _flat = false;
        if (b) calcFlatChildren();
        _flat = b;
    }
    
    /** todoList specific */
    public boolean getFlat() { return _flat; }
    
    /** todoList specific */
    public void calcFlatChildren() {
        _flatChildren.removeAllElements();
        addFlatChildren(_root);
    }
    
    /** todoList specific */
    public void addFlatChildren(Object node) {
        if (node == null) return;
        cat.debug("addFlatChildren");
        // hack for to do items only, should check isLeaf(node), but that
        // includes empty folders. Really I need alwaysLeaf(node).
        if ((node instanceof ToDoItem) && !_flatChildren.contains(node))
            _flatChildren.addElement(node);
        
        int nKids = getChildCount(node);
        for (int i = 0; i < nKids; i++) {
            addFlatChildren(getChild(node, i));
        }
    }
    
} /* end class ToDoPerspective */
