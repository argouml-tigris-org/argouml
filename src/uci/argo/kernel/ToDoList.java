// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.



// File: ToDoList.java
// Class: ToDoList
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.argo.kernel;

import java.util.*;
import com.sun.java.swing.event.EventListenerList;

import uci.util.*;

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
  protected Vector _items = new Vector(100);

  protected Set _allOffenders = new Set(100);
  protected Set _allKnowledgeTypes = new Set();
  protected Set _allPosters = new Set();
  
  /** ToDoItems that the designer has explicitly indicated that (s)he
   * considers resolved.  Needs-More-Work: generalize into a design
   * rationale logging facility. */
  protected Vector _resolvedItems = new Vector(100);

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
    _validityChecker = new Thread(this, "ValidityCheckingThread");
    _validityChecker.setDaemon(true);
    _validityChecker.setPriority(Thread.currentThread().getPriority() - 1);
    _validityChecker.start();
  }

  /** Periodically check to see if items on the list are still valid. */
  public void run() {
    Vector removes = new Vector();
    while (true) {
      forceValidityCheck(removes);
      removes.removeAllElements();
      try { _validityChecker.sleep(3000); }
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
    //Enumeration cur = _items.elements();
    int size = _items.size();
    for (int i = 0; i < size; ++i) {
      ToDoItem item = (ToDoItem) _items.elementAt(i);
      boolean valid;
      try { valid = item.stillValid(_designer); }
      catch (Exception ex) {
	valid = false;
	System.out.println("Exception raised in to do list cleaning");
	System.out.println(item.toString());
	ex.printStackTrace();
	System.out.println("----------");
      }
      if (!valid) removes.addElement(item);
    }
    //cur = removes.elements();
    size = removes.size();
    for (int i = 0; i < size; ++i) {
      ToDoItem item = (ToDoItem) removes.elementAt(i);
      resolve(item);
      History.TheHistory.addItemResolution(item, "no longer valid");
      //((ToDoItem)item).resolve("no longer valid");
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
  public Set getOffenders() { return _allOffenders; }
  public Vector getDecisions() { return new Vector(); }
  public Vector getGoals() { return new Vector(); }
  public Set getKnowledgeTypes() { return _allKnowledgeTypes; }
  public Set getPosters() { return _allPosters; }

  private synchronized void addE(ToDoItem item) {
    /* remove any identical items already on the list */
    if (_items.contains(item)) return;
    if (_resolvedItems.contains(item)) {
      //System.out.println("ToDoItem not added because it was resolved");
      return;
    }
    _items.addElement(item);
    _allOffenders.addAllElements(item.getOffenders());
    _allKnowledgeTypes.addAllElements(item.getPoster().getKnowledgeTypes());
    _allPosters.addElement(item.getPoster());
    if (item.getPoster() instanceof Designer)
      History.TheHistory.addItem(item, "note: ");
    else
      History.TheHistory.addItemCritique(item);
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
    recomputeAllOffenders();
    notifyObservers("removeElement", item);
    fireToDoItemRemoved(item);
    return res;
  }

  public boolean removeElement(ToDoItem item) {
    boolean res = removeE(item);
    return res;
  }

  public boolean resolve(ToDoItem item) {
    boolean res = removeE(item);
    fireToDoItemRemoved(item);
    return res;
  }

  public boolean explicitlyResolve(ToDoItem item, String reason) {
    boolean res = resolve(item);
    _resolvedItems.addElement(item);
    History.TheHistory.addItemResolution(item, reason); 
    return res;
  }

  public synchronized void removeAllElements() {
    _items.removeAllElements();
    notifyObservers("removeAllElements");
    fireToDoListChanged();
  }

  protected static Object _RecentOffender = null;
  protected static Vector _RecentOffenderItems = new Vector();
  public Vector elementsForOffender(Object off) {
    if (off == _RecentOffender) return _RecentOffenderItems;
    _RecentOffender = off;
    _RecentOffenderItems.removeAllElements();
    int size = _items.size();
    for (int i = 0; i < size; i++) {
      ToDoItem item = (ToDoItem) _items.elementAt(i);
      if (item.getOffenders().contains(off))
	_RecentOffenderItems.addElement(item);
    }
    return _RecentOffenderItems;
  }

  public Enumeration elements() {
    return _items.elements();
  }

  public ToDoItem elementAt(int index) {
    return (ToDoItem)_items.elementAt(index);
  }

  protected void recomputeAllOffenders() {
    _allOffenders = new Set(_items.size()*2);
    int size = _items.size();
    for (int i = 0; i < size; i++) {
      ToDoItem item = (ToDoItem) _items.elementAt(i);
      _allOffenders.addAllElements(item.getOffenders());
    }
  }

  protected void recomputeAllKnowledgeTypes() {
    _allKnowledgeTypes = new Set();
    Enumeration enum = _items.elements();
    while (enum.hasMoreElements()) {
      ToDoItem item = (ToDoItem) enum.nextElement();
      _allKnowledgeTypes.addAllElements(item.getPoster().getKnowledgeTypes());
    }
  }
  
  protected void recomputeAllPosters() {
    _allPosters = new Set();
    int size = _items.size();
    for (int i = 0; i < size; i++) {
      ToDoItem item = (ToDoItem) _items.elementAt(i);
      _allPosters.addElement(item.getPoster());
    }
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
    _RecentOffender = null;
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
    _RecentOffender = null;
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
    _RecentOffender = null;
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

