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



// File: Checklist.java
// Class: Checklist
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.cognitive.checklist;

import java.util.*;

/** A Checklist is basically a list of CheckItems.  It also provides
 *  some convience functions for adding trivial CheckItems (ones that
 *  have no predicate).
 *
 *  In Argo/UML, Checklists are shown in the TabChecklist panel.
 *
 *  @see org.argouml.cognitive.checklist.ui.TabChecklist
 */

public class Checklist implements java.io.Serializable {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Pending CheckItems for the designer to consider. */
  protected Vector _items = new Vector(100);

  protected String _nextCategory = "General";

  ////////////////////////////////////////////////////////////////
  // constructor

  public Checklist() { }

  ////////////////////////////////////////////////////////////////
  // accessors

  public Vector getCheckItems() { return _items; }

  public void addItem(CheckItem item) {
    _items.addElement(item);
  }

  public void removeItem(CheckItem item) {
    _items.removeElement(item);
  }

  public void addItem(String description) {
    CheckItem item = new CheckItem(_nextCategory, description);
    _items.addElement(item);
  }

  public synchronized void addAll(Checklist list) {
    Enumeration cur = list.elements();
    while (cur.hasMoreElements()) {
      CheckItem item = (CheckItem) cur.nextElement();
      addItem(item);
    }
  }

  public Enumeration elements() { return _items.elements(); }

  public int size() { return _items.size(); }

  public CheckItem elementAt(int index) {
    return (CheckItem)_items.elementAt(index);
  }

  public void setNextCategory(String cat) { _nextCategory = cat; }

  
  ////////////////////////////////////////////////////////////////
  // internal methods
  
  /** Sort the items by priority.
   *
   *  Needs-More-Work: not done yet.  It has been pointed out that
   *  sorting and priorities will probably be pretty arbitrary and hard
   *  to match with the Designer's (tacit) feelings about the
   *  importance of various items.  We are thinking about a
   *  sort-by-category user interface that would be part of a complete
   *  java PIM (personal information manager, AKA, a daily planner).  */
  private synchronized void sort() {
    // do some sorting?
  }

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

