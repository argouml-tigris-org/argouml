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

// File: ToDoList.java
// Class: ToDoList
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.argo.kernel;

import java.util.*;
import com.sun.java.swing.event.EventListenerList;

/** This class implments a list of ToDoItem's.  If desired it can also
 *  spwan a "sweeper" thread that periodically goes through the list
 *  and elimiates ToDoItem's that are no longer valid.
 *
 * @see jargo.ui.UiToDoList
 * @see Designer#nondisruptivelyWarn
 */

public class ToDoList extends Observable
implements Runnable, java.io.Serializable {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Pending ToDoItems for the designer to consider. */
  private Vector _items = new Vector(100);

  /** ToDoItems that the designer has explicitly indicated that (s)he
   * considers resolved.  Needs-More-Work: generalize into a design
   * rationale logging facility. */
  private Vector _resolvedItems = new Vector(100);

  /** A Thread that keeps checking if the items on the list are still valid. */
  protected Thread _validityChecker;

  /** The designer, used in determining if a ToDoItem is still valid.  */
  protected Designer _designer;

  protected EventListenerList _listenerList = new EventListenerList();
  
  ////////////////////////////////////////////////////////////////
  // constructor

  public ToDoList() { }

  /** Start a Thread to delete old items from the ToDoList. */
  public void spawnValidityChecker(Designer d) {
    _designer = d;
    _validityChecker = new Thread(this);
    _validityChecker.setDaemon(true);
    _validityChecker.start();
  }

  /** Periodically check to see if items on the list are still valid. */
  public void run() {
    Vector removes = new Vector();
    while (true) {
      forceValidityCheck(removes);
      removes.removeAllElements();
      try { _validityChecker.sleep(6000); }
      catch (InterruptedException ignore) {
	System.out.println("InterruptedException!!!");
      }
    }
  }

  public void forceValidityCheck() {
    Vector removes = new Vector();
    forceValidityCheck(removes);
  }
  
  protected synchronized void forceValidityCheck(Vector removes) {
    Enumeration cur = _items.elements();
    while (cur.hasMoreElements()) {
      ToDoItem item = (ToDoItem) cur.nextElement();
      if (!item.stillValid(_designer)) removes.addElement(item);
    }
    cur = removes.elements();
    while (cur.hasMoreElements()) {
      Object item = cur.nextElement();
      ((ToDoItem)item).resolve("no longer valid");
      notifyObservers("removeElement", item);
    }
  }
  
  ////////////////////////////////////////////////////////////////
  // Notifications and Updates

  public void notifyObservers(String action, Object arg) {
    setChanged();
    Vector v = new Vector(2);
    v.addElement(action);
    v.addElement(arg);
    super.notifyObservers(v);
  }

  public void notifyObservers(Object o) {
    setChanged();
    super.notifyObservers(o);
  }

  public void notifyObservers() {
    setChanged();
    super.notifyObservers();
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public Vector getToDoItems() { return _items; }

  // needs-more-work: not implemented
  public Vector getOffenders() { return new Vector(); }
  public Vector getDecisions() { return new Vector(); }
  public Vector getGoals() { return new Vector(); }
  public Vector getKnowledgeTypes() { return new Vector(); }
  public Vector getPosters() { return new Vector(); }

  
  
  private synchronized void addE(ToDoItem item) {
    /* remove any identical items already on the list */
    if (_items.contains(item)) return;
    if (_resolvedItems.contains(item)) {
      System.out.println("ToDoItem not added because it was resolved");
      return;
    }
    _items.addElement(item);
    notifyObservers("addElement", item);
    fireToDoItemAdded(item);
  }

  public synchronized void addElement(ToDoItem item) {
    addE(item);
  }

  public synchronized void addAll(ToDoList list) {
    Enumeration cur = list.elements();
    while (cur.hasMoreElements()) {
      ToDoItem item = (ToDoItem) cur.nextElement();
      addElement(item);
    }
    fireToDoListChanged();
  }

  public void removeAll(ToDoList list) {
    Enumeration cur = list.elements();
    while (cur.hasMoreElements()) {
      ToDoItem item = (ToDoItem) cur.nextElement();
      removeE(item);
    }
    fireToDoListChanged();
  }

  private synchronized boolean removeE(ToDoItem item) {
    boolean res = _items.removeElement(item);
    notifyObservers("removeElement", item);
    fireToDoItemRemoved(item);
    return res;
  }

  public boolean removeElement(ToDoItem item) {
    boolean res = removeE(item);
    return res;
  }

  public boolean resolve(ToDoItem item, Object reason) {
    boolean res = removeE(item);
    //System.out.println("reason=" + reason.toString());
    if ("explicit resolve".equals(reason))
      _resolvedItems.addElement(item);
    fireToDoItemRemoved(item);
    return res;
  }

  public synchronized void removeAllElements() {
    _items.removeAllElements();
    notifyObservers("removeAllElements");
    fireToDoListChanged();
  }

  public Enumeration elements() {
    return _items.elements();
  }

  public ToDoItem elementAt(int index) {
    return (ToDoItem)_items.elementAt(index);
  }


  ////////////////////////////////////////////////////////////////
  // event related stuff

  public void addToDoListListener(ToDoListListener l) {
    _listenerList.add(ToDoListListener.class, l);
  }

  public void removeToDoListListener(ToDoListListener l) {
    _listenerList.remove(ToDoListListener.class, l);
  }
  
  
  /*
   * Notify all listeners that have registered interest for
   * notification on this event type.  The event instance 
   * is lazily created using the parameters passed into 
   * the fire method.
   * @see EventListenerList
   */
  protected void fireToDoListChanged() {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    ToDoListEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==ToDoListListener.class) {
	// Lazily create the event:
	if (e == null) e = new ToDoListEvent();
	((ToDoListListener)listeners[i+1]).toDoListChanged(e);
      }          
    }
  }

  protected void fireToDoItemAdded(ToDoItem item) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    ToDoListEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==ToDoListListener.class) {
	// Lazily create the event:
	if (e == null) e = new ToDoListEvent(item);
	((ToDoListListener)listeners[i+1]).toDoItemAdded(e);
      }          
    }
  }

  protected void fireToDoItemRemoved(ToDoItem item) {
    // Guaranteed to return a non-null array
    Object[] listeners = _listenerList.getListenerList();
    ToDoListEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==ToDoListListener.class) {
	// Lazily create the event:
	if (e == null) e = new ToDoListEvent(item);
	((ToDoListListener)listeners[i+1]).toDoItemRemoved(e);
      }          
    }
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
      ToDoItem item = (ToDoItem) cur.nextElement();
      res += "    " + item.toString() + "\n";
    }
    res += "  }";
    return res;
  }

} /* end class ToDoList */

