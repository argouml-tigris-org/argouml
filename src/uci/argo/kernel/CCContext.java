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

// File: CCContext.java
// Classes: CCContext
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.argo.kernel;

import java.util.*;
import uci.util.*;

/** */

public class CCContext implements Runnable {

  ////////////////////////////////////////////////////////////////
  // instance variables
  public Object _dm;
  public Designer _dsgr;
  public CCNetwork _network;
  public Set _activeNodes = new Set();
  public Hashtable _context = new Hashtable();
  public ToDoItem _item;
  


  ////////////////////////////////////////////////////////////////
  // constructor

  /** Used when initially detecting problems. */
  public CCContext(Object dm, Designer dsgr, CCNetwork net) {
    _dm = dm;
    _dsgr = dsgr;
    _network = net;
  }

  /** Used after a problem has been found and a ToDoItemCC has been
   *  made. */
  public CCContext(CCContext ccc, Critic c) {
    _dm = ccc._dm;
    _dsgr = ccc._dsgr;
    _network = ccc._network;
    _context = (Hashtable) ccc._context.clone();
    _activeNodes = new Set();
    CCNodeFire fire = ccc.findFireNodeFor(c);
    _activeNodes.addElement(fire);
  }


  
  ////////////////////////////////////////////////////////////////
  // accessors

  public boolean fired(Critic c) { return findFireNodeFor(c) != null; }

  public CCNodeFire findFireNodeFor(Critic c) {
    Enumeration enum = _activeNodes.elements();
    while (enum.hasMoreElements()) {
      CCNode node = (CCNode) enum.nextElement();
      if (node instanceof CCNodeFire &&
	  ((CCNodeFire)node).getCritic().equals(c))
	return (CCNodeFire) node;
    }
    return null;
  }

  
  
  public ToDoItem getToDoItem() { return _item; }
  public void setToDoItem(ToDoItem item) { _item = item; }

  public void put(String name, Object val) { _context.put(name, val); }

  public Object get(String name) { return _context.get(name); }

  public Object get(String name, Object defaultValue) {
    if (_context.containsKey(name)) return _context.get(name);
    else return defaultValue;
  }

  ////////////////////////////////////////////////////////////////
  // evaluation

  public void reset() {
    _activeNodes = new Set();
    _activeNodes.addElement(_network.getStart());
  }

  public void run() {
    boolean progress = true;
    while (progress) {
      progress = false;
      Set nextNodes = new Set();
      int nActive = _activeNodes.size();
      for (int i = 0; i < nActive; ++i) {
	CCNode node = (CCNode) _activeNodes.elementAt(i);
	if (node.predicate(this)) {
	  nextNodes.addAllElements(node.getSuccessors());
	  progress = true;
	}
	if (node.shouldStay(this)) nextNodes.addElement(node);
      }
      _activeNodes = nextNodes;
    }
  }
   

} /* end class CCContext */

