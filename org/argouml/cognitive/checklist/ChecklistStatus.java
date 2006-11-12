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
 * A list of CheckItems that the designer has marked off as already
 * considered.  In the ArgoUML system, this determines which items
 * in the TabChecklist have checkmarks.
 *
 * @see org.argouml.cognitive.checklist.ui.TabChecklist
 * @author Jason Robbins
 */
public class ChecklistStatus implements Serializable {
    private static int numChecks = 0;
    ////////////////////////////////////////////////////////////////
    // instance variables

    /** CheckItems that the designer has marked off as already considered. */
    private Vector items = new Vector();

    /**
     * The constructor.
     *
     */
    public ChecklistStatus() { }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @return the checked items
     */
    public Vector getCheckItems() { return items; }

    /**
     * @param item the item to be checkmarked
     */
    public void addItem(CheckItem item) {
	items.addElement(item);
	numChecks++;
    }

    /**
     * @param list set the list of checkmarked items
     */
    public synchronized void addAll(ChecklistStatus list) {
	Enumeration cur = list.elements();
	while (cur.hasMoreElements()) {
	    CheckItem item = (CheckItem) cur.nextElement();
	    addItem(item);
	}
    }

    /**
     * @param item the item for which to remove a checkmark
     */
    public void removeItem(CheckItem item) {
	items.removeElement(item);
    }

    /**
     * @return the items in Enumeration format
     */
    public Enumeration elements() { return items.elements(); }

    /**
     * @param index an index into this vector
     * @return the element
     */
    public CheckItem elementAt(int index) {
	return (CheckItem) items.elementAt(index);
    }

    /**
     * @param item the item
     * @return true if the given item is contained in the list
     */
    public boolean contains(CheckItem item) {
	return items.contains(item);
    }

    /*
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

} /* end class ChecklistStatus */

