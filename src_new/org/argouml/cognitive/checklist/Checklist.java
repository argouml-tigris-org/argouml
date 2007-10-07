// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.cognitive.checklist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * A Checklist is basically a list of CheckItems.  It also provides
 * some convience functions for adding trivial CheckItems (ones that
 * have no predicate).
 *
 * In ArgoUML, Checklists are shown in the TabChecklist panel.
 *
 * @see org.argouml.cognitive.checklist.ui.TabChecklist
 *
 * @author Jason Robbins
 */
public class Checklist extends ArrayList<CheckItem> implements List<CheckItem>,
        Serializable {

    private String nextCategory = "General";

    /**
     * The constructor.
     *
     */
    public Checklist() {
        super();
    }

    /**
     * @return the items
     * @deprecated for 0.25.4 by tfmorris.  Use {@link #getCheckItemList()}.
     */
    @Deprecated
    public Vector<CheckItem> getCheckItems() {
        return new Vector<CheckItem>(this);
    }

    /**
     * @return the items
     */
    public List<CheckItem> getCheckItemList() {
        return this;
    }

    
    /**
     * @param item the item to be added to the list
     * @deprecated for 0.25.4 by tfmorris. Use {@link #add(CheckItem)}.
     */
    @Deprecated
    public void addItem(CheckItem item) {
	add(item);
    }

    /**
     * @param item the item to be removed
     * @deprecated for 0.25.4 by tfmorris. Use {@link #remove(CheckItem)}.
     */
    @Deprecated
    public void removeItem(CheckItem item) {
	remove(item);
    }

    /**
     * @param description the description for a new item
     */
    public void addItem(String description) {
	add(new CheckItem(nextCategory, description));
    }

    /**
     * Replace the list by the given new list.
     *
     * @param list the given new list
     */
    public synchronized void addAll(Checklist list) {
        for (CheckItem item : list) {
	    add(item);
	}
    }

    /**
     * @return the list in enumeration format
     * @deprecated for 0.25.4 by tfmorris.
     */
    @Deprecated
    public Enumeration<CheckItem> elements() {
        return new Vector<CheckItem>(this).elements();
    }


    /**
     * @param index the position of the item to retrieve
     * @return the item
     * @deprecated for 0.25.4 by tfmorris. Use {@link #get(int)}. 
     */
    @Deprecated
    public CheckItem elementAt(int index) {
	return get(index);
    }

    /**
     * @param cat the category
     */
    public void setNextCategory(String cat) {
        nextCategory = cat;
    }


    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append(getClass().getName() + " {\n");
        for (CheckItem item : this) {
	    sb.append("    " + item.toString() + "\n");
	}
	sb.append("  }");
	return sb.toString();
    }

}

