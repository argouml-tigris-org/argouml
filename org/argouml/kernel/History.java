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

package org.argouml.kernel;

import java.util.*;
import java.util.Enumeration;
import javax.swing.event.EventListenerList;

import org.argouml.cognitive.*;

public class History {
  ////////////////////////////////////////////////////////////////
  // class variables
  public static History TheHistory = new History();

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Vector _items = new Vector();
  protected EventListenerList listenerList = new EventListenerList();


  ////////////////////////////////////////////////////////////////
  // constructor

  public History() { }


  ////////////////////////////////////////////////////////////////
  // accessors

  public Vector getItems() { return _items; }

  public void addItem(HistoryItem hi) {
    _items.addElement(hi);
    fireHistoryAdded(hi, _items.size()-1);
  }

  public void addItem(String head, String desc, Object target,
		      Object oldValue, Object newValue) {
    HistoryItem hi = new HistoryItem(head, desc, target, oldValue, newValue);
    addItem(hi);
  }

  public void addItem(String head, String desc) {
    HistoryItem hi = new HistoryItem(head, desc);
    addItem(hi);
  }

  public void addItem(ToDoItem item, String desc) {
    HistoryItem hi = new HistoryItem(item, desc);
    addItem(hi);
  }

  public void addItemResolution(ToDoItem item, String reason) {
    HistoryItem hi = new HistoryItemResolve(item, reason);
    addItem(hi);
  }

  public void addItemManipulation(String head, String desc, Object target,
		     Object oldValue, Object newValue) {
    HistoryItem hi = new HistoryItemManipulation(head, desc, target,
						 oldValue, newValue);
    addItem(hi);
  }

  public void addItemCritique(ToDoItem item) {
    HistoryItem hi = new org.argouml.cognitive.critics.HistoryItemCritique(item);
    addItem(hi);
  }

  // items are never removed

  // TODO: support for search and queries


  ////////////////////////////////////////////////////////////////
  // event handling

  /**
   * Add a listener to the list that's notified each time a change
   * to the data model occurs.
   * @param l the ListDataListener
   */  
  public void addHistoryListener(HistoryListener l) {
    listenerList.add(HistoryListener.class, l);
  }
  

  /**
   * Remove a listener from the list that's notified each time a 
   * change to the data model occurs.
   * @param l the ListDataListener
   */  
  public void removeHistoryListener(HistoryListener l) {
    listenerList.remove(HistoryListener.class, l);
  }


  /*
   * AbstractListModel subclasses must call this method <b>after</b>
   * one or more elements are added to the model.  The new elements
   * are specified by a closed interval index0, index1, i.e. the
   * range that includes both index0 and index1.  Note that
   * index0 need not be less than or equal to index1.
   * 
   * @param source The ListModel that changed, typically "this".
   * @param index0 One end of the new interval.
   * @param index1 The other end of the new interval.
   * @see EventListenerList
   * @see DefaultListModel
   */
  protected void fireHistoryAdded(HistoryItem item, int index) {
    Object[] listeners = listenerList.getListenerList();
    HistoryEvent e = null;
    
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == HistoryListener.class) {
	if (e == null) e = new HistoryEvent(this, item, index);
	((HistoryListener)listeners[i+1]).historyAdded(e);
      }	       
    }
  }
  

} /* end class History */
