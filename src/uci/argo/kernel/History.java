
package uci.argo.kernel;

import java.util.*;
import com.sun.java.swing.event.EventListenerList;

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

  public void addItem(String desc, Object target,
		      Object oldValue, Object newValue) {
    HistoryItem hi = new HistoryItem(desc, target, oldValue, newValue);
    addItem(hi);
  }

  public void addItem(String desc) {
    HistoryItem hi = new HistoryItem(desc);
    addItem(hi);
  }

  // items are never removed

  // needs-more-work: support for search and queries


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
