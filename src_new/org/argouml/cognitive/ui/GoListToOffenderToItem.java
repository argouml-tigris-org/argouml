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

import org.tigris.gef.util.*;

import org.argouml.ui.*;
import org.argouml.cognitive.*;

public class GoListToOffenderToItem implements TreeModel {
  
    ////////////////////////////////////////////////////////////////
    // TreeModel implementation
  
    public Object getRoot() {
	throw new UnsupportedOperationException("getRoot should never be called");
    } 
    public void setRoot(Object r) { }

    public Object getChild(Object parent, int index) {
	Vector children = getChildren(parent);
	return (children == null) ? null : children.elementAt(index);
    }
  
    public int getChildCount(Object parent) {
	Vector children = getChildren(parent);
	return (children == null) ? 0 : children.size();
    }
  
    public int getIndexOfChild(Object parent, Object child) {
	Vector children = getChildren(parent);
	return (children == null) ? -1 : children.indexOf(child);
    }

    public boolean isLeaf(Object node) {
	if (node instanceof ToDoList) return false;
	if (getChildCount(node) > 0) return false;
	return true;
    }


    public Vector getChildren(Object parent) {
	VectorSet allOffenders = Designer.TheDesigner.getToDoList().getOffenders();
	if (parent instanceof ToDoList) {
	    return allOffenders.asVector();
	}
	//otherwise parent must be an offending design material
	if (allOffenders.contains(parent)) {
	    Vector res = new Vector();
	    ToDoList list = Designer.TheDesigner.getToDoList();
	    java.util.Enumeration enum = list.elements();
	    while (enum.hasMoreElements()) {
		ToDoItem item = (ToDoItem) enum.nextElement();
		VectorSet offs = item.getOffenders();
		if (offs.contains(parent)) res.addElement(item);
	    }
	    return res;
	}
	return null;
    }
  
    public void valueForPathChanged(TreePath path, Object newValue) { }
    public void addTreeModelListener(TreeModelListener l) { }
    public void removeTreeModelListener(TreeModelListener l) { }


} /* end class GoListToOffenderToItem */
