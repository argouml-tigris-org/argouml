// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: ChecklistStatus.java
// Class: ChecklistStatus
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.argo.checklist;

import java.util.*;

import uci.util.*;

/** 
 */

public class ChecklistStatus implements java.io.Serializable {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Pending ToDoItems for the designer to consider. */
  protected Vector _items = new Vector();

  ////////////////////////////////////////////////////////////////
  // constructor

  public ChecklistStatus() { }

  ////////////////////////////////////////////////////////////////
  // accessors

  public Vector getCheckItems() { return _items; }

  public void addItem(CheckItem item) { _items.addElement(item); }

  public synchronized void addAll(ChecklistStatus list) {
    Enumeration cur = list.elements();
    while (cur.hasMoreElements()) {
      CheckItem item = (CheckItem) cur.nextElement();
      addItem(item);
    }
  }

  public void removeItem(CheckItem item) {
    _items.removeElement(item);
  }

  public Enumeration elements() { return _items.elements(); }

  public CheckItem elementAt(int index) {
    return (CheckItem)_items.elementAt(index);
  }

  public boolean contains(CheckItem item) {
    return _items.contains(item);
  }
  
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

} /* end class ChecklistStatus */

