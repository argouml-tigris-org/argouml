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

