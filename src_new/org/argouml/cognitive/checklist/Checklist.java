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

package org.argouml.cognitive.checklist;

import java.io.Serializable;
import java.util.Enumeration;
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
public class Checklist implements Serializable {

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * Pending CheckItems for the designer to consider.
     */
    private Vector items = new Vector();

    private String nextCategory = "General";

    /**
     * The constructor.
     *
     */
    public Checklist() { }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @return the items
     */
    public Vector getCheckItems() { return items; }

    /**
     * @param item the item to be added to the list
     */
    public void addItem(CheckItem item) {
	items.addElement(item);
    }

    /**
     * @param item the item to be removed
     */
    public void removeItem(CheckItem item) {
	items.removeElement(item);
    }

    /**
     * @param description the description for a new item
     */
    public void addItem(String description) {
	CheckItem item = new CheckItem(nextCategory, description);
	items.addElement(item);
    }

    /**
     * Replace the list by the given new list.
     *
     * @param list the given new list
     */
    public synchronized void addAll(Checklist list) {
	Enumeration cur = list.elements();
	while (cur.hasMoreElements()) {
	    CheckItem item = (CheckItem) cur.nextElement();
	    addItem(item);
	}
    }

    /**
     * @return the list in enumeration format
     */
    public Enumeration elements() { return items.elements(); }

    /**
     * @return the number of items in the list
     */
    public int size() { return items.size(); }

    /**
     * @param index the position of the item to retrieve
     * @return the item
     */
    public CheckItem elementAt(int index) {
	return (CheckItem) items.elementAt(index);
    }

    /**
     * @param cat the category
     */
    public void setNextCategory(String cat) { nextCategory = cat; }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
	String res;
	res = getClass().getName() + " {\n";
	Enumeration cur = elements();
	while (cur.hasMoreElements()) {
	    CheckItem item = (CheckItem) cur.nextElement();
	    res += "    " + item.toString() + "\n";
	}
	res += "  }";
	return res;
    }

} /* end class Checklist */

