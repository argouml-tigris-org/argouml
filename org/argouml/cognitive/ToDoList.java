// $Id$
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

// File: ToDoList.java
// Class: ToDoList
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.cognitive;

import java.util.*;
import javax.swing.event.EventListenerList;

import org.tigris.gef.util.*;

import org.apache.log4j.Category;
import org.argouml.kernel.*;
import org.argouml.cognitive.critics.Critic;

/** Implements a list of ToDoItem's.
 *
 * <p>It spawns a "sweeper" thread that periodically goes through the list
 * and elimiates ToDoItem's that are no longer valid.
 *
 * <P>
 * One difficulty designers face is keeping track of all
 * the myrid details of thier task. It is all to
 * easy to skip a step in the design process,
 * leave part of the design unspecified, of make
 * a mistake that requires revision. Argo provides
 * the designer with a "to do" list user interface
 * that presents action items in an organized form.
 * These items can be suggestions from critics,
 * reminders to finish steps in the process model,
 * or personal notes entered by the designer.
 * The choice control at the top of the "to do"
 * list pane allow the designer to organize
 * items in different ways: by priority,
 * by decision supported, by offending design
 * element, etc.
 *
 * <P>
 * The to do lists right now are a bit
 * unstable. Please test and let us know
 * what you find through Issuezilla.
 *
 * <p>Items are shown under all applicable headings.
 * The "to do" list may also be viewed as a flat list.
 *
 * <p>This class is dependent on Designer.
 *
 * @see Designer#nondisruptivelyWarn
 */
public class ToDoList
    extends Observable
    implements Runnable, // TODO refactor per issue 1024
               java.io.Serializable {
    
    protected static Category cat = Category.getInstance(ToDoList.class);
    
    /** needs documenting */
    protected static Object _RecentOffender;
    /** needs documenting */
    protected static Vector _RecentOffenderItems;
    
    ////////////////////////////////////////////////////////////////
    // instance variables
    
    /** Pending ToDoItems for the designer to consider. */
    protected Vector _items;
    
    /** These are computed when needed. */
    private VectorSet _allOffenders;
    /** These are computed when needed. */
    private VectorSet _allPosters;
    
    /** ToDoItems that the designer has explicitly indicated that (s)he
     * considers resolved.
     *
     * <p>TODO: generalize into a design rationale logging facility.
     */
    protected Vector _resolvedItems;
    
    /** A Thread that keeps checking if the items on the list are still valid. */
    protected Thread _validityChecker;
    
    /** The designer, used in determining if a ToDoItem is still valid.  */
    protected Designer _designer;
    
    /** needs documenting */
    protected EventListenerList _listenerList;
    
    /** needs documenting */
    public static int _longestToDoList;
    /** needs documenting */
    public static int _numNotValid;
    
    ////////////////////////////////////////////////////////////////
    // constructor
    
    /** needs documenting */
    public ToDoList() {
    
	_items = new Vector(100);
	_resolvedItems = new Vector(100);
	_listenerList = new EventListenerList();
	_longestToDoList = 0;
	_numNotValid = 0;
	_RecentOffenderItems = new Vector();
    }
    
    /** Start a Thread to delete old items from the ToDoList. */
    public void spawnValidityChecker(Designer d) {
        _designer = d;
        _validityChecker = new Thread(this, "ValidityCheckingThread");
        _validityChecker.setDaemon(true);
        _validityChecker.setPriority(Thread.MIN_PRIORITY);
        _validityChecker.start();
    }
    
    /** Periodically check to see if items on the list are still valid. */
    public void run() {
        Vector removes = new Vector();
        while (true) {
            forceValidityCheck(removes);
            removes.removeAllElements();
            try { Thread.sleep(3000); }
            catch (InterruptedException ignore) {
                cat.error("InterruptedException!!!", ignore);
            }
        }
    }
    
    /** needs documenting */
    public void forceValidityCheck() {
        Vector removes = new Vector();
        forceValidityCheck(removes);
    }
    
    /** Check each ToDoItem on the list to see if it is still valid.  If
     *  not, then remove that item.  This is called automatically by the
     *  ValidityCheckingThread, and it can be called by the user
     *  pressing a button via forceValidityCheck(). <p>
     *
     *  <b>Warning: Fragile code!<b> No method that this method calls can
     *  synchronized the Designer, otherwise there will be deadlock.
     */
    protected synchronized void forceValidityCheck(Vector removes) {
        //Enumeration cur = _items.elements();
        int size = _items.size();
        for (int i = 0; i < size; ++i) {
            ToDoItem item = (ToDoItem) _items.elementAt(i);
            boolean valid;
            try { valid = item.stillValid(_designer); }
            catch (Exception ex) {
                valid = false;
                StringBuffer buf = 
                    new StringBuffer("Exception raised in to do list cleaning");
                buf.append("\n");
                buf.append(item.toString());
                cat.error(buf.toString(), ex);
            }
            if (!valid) {
                _numNotValid++;
                removes.addElement(item);
            }
        }
        //cur = removes.elements();
        size = removes.size();
        for (int i = 0; i < size; ++i) {
            ToDoItem item = (ToDoItem) removes.elementAt(i);
            removeE(item);
            History.TheHistory.addItemResolution(item, "no longer valid");
            //((ToDoItem)item).resolve("no longer valid");
            //notifyObservers("removeElement", item);
        }
        recomputeAllOffenders();
        recomputeAllPosters();
        fireToDoItemsRemoved(removes);
    }
    
    ////////////////////////////////////////////////////////////////
    // Notifications and Updates
    
    /** needs documenting */
    public void notifyObservers(String action, Object arg) {
        setChanged();
        Vector v = new Vector(2);
        v.addElement(action);
        v.addElement(arg);
        super.notifyObservers(v);
    }
    
    /** needs documenting */
    public void notifyObservers(Object o) {
        setChanged();
        super.notifyObservers(o);
    }
    
    /** needs documenting */
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
    
    ////////////////////////////////////////////////////////////////
    // accessors
    
    /** needs documenting */
    public Vector getToDoItems() { return _items; }
    /** needs documenting */
    public Vector getResolvedItems() { return _resolvedItems; }
    
    /**
     * @return the set of offenders
     */
    public VectorSet getOffenders() {
        // Extra care to be taken since _allOffenders can be reset while
        // this method is running.
        VectorSet all = _allOffenders;
        if (all == null) {
            int size = _items.size();
            all = new VectorSet(size * 2);
            for (int i = 0; i < size; i++) {
                ToDoItem item = (ToDoItem) _items.elementAt(i);
                all.addAllElements(item.getOffenders());
            }
            _allOffenders = all;
        }
        return all;
    }

    /** needs documenting */
    private void addOffenders(VectorSet newoffs) {
        if (_allOffenders != null)
            _allOffenders.addAllElements(newoffs);
    }
    
    /**
     * @return the set of all the posters
     */
    public VectorSet getPosters() {
        // Extra care to be taken since _allPosters can be reset while
        // this method is running.
        VectorSet all = _allPosters;
        if (all == null) {
            int size = _items.size();
            all = new VectorSet();
            for (int i = 0; i < size; i++) {
                ToDoItem item = (ToDoItem) _items.elementAt(i);
                all.addElement(item.getPoster());
            }
            _allPosters = all;
        }
        return all;
    }

    /** needs documenting */
    private void addPosters(Poster newp) {
        if (_allPosters != null)
            _allPosters.addElement(newp);
    }
    
    /** needs documenting */
    public Vector getDecisions() { return new Vector(); }

    /** needs documenting */
    public Vector getGoals() { return new Vector(); }
    
    /** needs documenting, why synchronised? */
    private synchronized void addE(ToDoItem item) {
        /* remove any identical items already on the list */
        if (_items.contains(item))
            return;
        
        if (item.getPoster() instanceof Critic) {
            ResolvedCritic rc;
            try {
                rc = new ResolvedCritic((Critic) item.getPoster(),
                                        item.getOffenders(), false);
                Enumeration enum = _resolvedItems.elements();
                //cat.debug("Checking for inhibitors " + rc);
                while (enum.hasMoreElements()) {
                    if (enum.nextElement().equals(rc)) {
                        cat.debug("ToDoItem not added because it was resolved");
                        return;
                    }
                }
            } catch (UnresolvableException ure) {
            }
        }
        
        _items.addElement(item);
        _longestToDoList = Math.max(_longestToDoList, _items.size());
        addOffenders(item.getOffenders());
        addPosters(item.getPoster());
        if (item.getPoster() instanceof Designer)
            History.TheHistory.addItem(item, "note: ");
        else
            History.TheHistory.addItemCritique(item);
        notifyObservers("addElement", item);
        fireToDoItemAdded(item);
    }
    
    /** needs documenting */
    public synchronized void addElement(ToDoItem item) {
        addE(item);
    }
    
    /** needs documenting */
    public synchronized void addAll(ToDoList list) {
        Enumeration cur = list.elements();
        while (cur.hasMoreElements()) {
            ToDoItem item = (ToDoItem) cur.nextElement();
            addElement(item);
        }
        fireToDoListChanged();
    }
    
    /** needs documenting */
    public void removeAll(ToDoList list) {
        Enumeration cur = list.elements();
        while (cur.hasMoreElements()) {
            ToDoItem item = (ToDoItem) cur.nextElement();
            removeE(item);
        }
        recomputeAllOffenders();
        recomputeAllPosters();
        fireToDoItemsRemoved(list.getToDoItems());
    }
    
    /** needs documenting */
    private synchronized boolean removeE(ToDoItem item) {
        boolean res = _items.removeElement(item);
        return res;
    }
    
    /** needs documenting */
    public boolean removeElement(ToDoItem item) {
        boolean res = removeE(item);
        recomputeAllOffenders();
        recomputeAllPosters();
        fireToDoItemRemoved(item);
        notifyObservers("removeElement", item);
        return res;
    }
    
    /** needs documenting */
    public boolean resolve(ToDoItem item) {
        boolean res = removeE(item);
        fireToDoItemRemoved(item);
        return res;
    }
    
    /** needs documenting */
    public boolean explicitlyResolve(ToDoItem item, String reason) 
	throws UnresolvableException {
          
        if (item.getPoster() instanceof Designer) {
            boolean res = resolve(item);
            History.TheHistory.addItemResolution(item, reason);
            return res;
        }
        
        if (!(item.getPoster() instanceof Critic))
            throw new UnresolvableException("Unable to resolve with poster of type: "
					    + item.getPoster().getClass());
        
        ResolvedCritic rc = new ResolvedCritic((Critic) item.getPoster(),
					       item.getOffenders());
        boolean res = resolve(item);
        _resolvedItems.addElement(rc);
        History.TheHistory.addItemResolution(item, reason);
        return res;
    }
    
    /** needs documenting */
    public synchronized void removeAllElements() {
        cat.debug("removing all todo items");
        Vector oldItems = (Vector) _items.clone();
        int size = oldItems.size();
        for (int i = 0; i < size; i++)
            removeE((ToDoItem) oldItems.elementAt(i));
        
        recomputeAllOffenders();
        recomputeAllPosters();
        notifyObservers("removeAllElements");
        fireToDoItemsRemoved(oldItems);
    }
    
    /** needs documenting */
    public Vector elementsForOffender(Object off) {
        if (off == _RecentOffender) return _RecentOffenderItems;
        _RecentOffender = off;
        _RecentOffenderItems.removeAllElements();
        synchronized (_items) {
            int size = _items.size();
            for (int i = 0; i < _items.size(); i++) {
                ToDoItem item = (ToDoItem) _items.elementAt(i);
                if (item.getOffenders().contains(off))
                    _RecentOffenderItems.addElement(item);
            }
        }
        return _RecentOffenderItems;
    }
    
    /** needs documenting */
    public int size() { return _items.size(); }
    
    /** needs documenting */
    public Enumeration elements() {
        return _items.elements();
    }
    
    /** needs documenting */
    public ToDoItem elementAt(int index) {
        return (ToDoItem) _items.elementAt(index);
    }
    
    /** needs documenting */
    protected void recomputeAllOffenders() {
        _allOffenders = null;
    }
    
    /** needs documenting */
    protected void recomputeAllPosters() {
        _allPosters = null;
    }
    
    
    ////////////////////////////////////////////////////////////////
    // event related stuff
    
    /** needs documenting */
    public void addToDoListListener(ToDoListListener l) {
        _listenerList.add(ToDoListListener.class, l);
    }
    
    /** needs documenting */
    public void removeToDoListListener(ToDoListListener l) {
        _listenerList.remove(ToDoListListener.class, l);
    }
    
    /**
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
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ToDoListListener.class) {
                // Lazily create the event:
                if (e == null) e = new ToDoListEvent();
                ((ToDoListListener) listeners[i + 1]).toDoListChanged(e);
            }
        }
    }
    
    /** needs documenting */
    protected void fireToDoItemChanged(ToDoItem item) {
        Object[] listeners = _listenerList.getListenerList();
        ToDoListEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ToDoListListener.class) {
                // Lazily create the event:
                if (e == null) {
                    Vector items = new Vector();
                    items.addElement(item);
                    e = new ToDoListEvent(items);
                }
                ((ToDoListListener) listeners[i + 1]).toDoItemsChanged(e);
            }
        }
    }
    
    /** needs documenting */
    protected void fireToDoItemAdded(ToDoItem item) {
        Vector v = new Vector();
        v.addElement(item);
        fireToDoItemsAdded(v);
    }

    /** needs documenting */
    protected void fireToDoItemsAdded(Vector items) {
        _RecentOffender = null;
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        ToDoListEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ToDoListListener.class) {
                // Lazily create the event:
                if (e == null) e = new ToDoListEvent(items);
                ((ToDoListListener) listeners[i + 1]).toDoItemsAdded(e);
            }
        }
    }
    
    /** needs documenting */
    protected void fireToDoItemRemoved(ToDoItem item) {
        Vector v = new Vector();
        v.addElement(item);
        fireToDoItemsRemoved(v);
    }

    /** needs documenting */
    protected void fireToDoItemsRemoved(Vector items) {
        _RecentOffender = null;
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        ToDoListEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ToDoListListener.class) {
                // Lazily create the event:
                if (e == null) e = new ToDoListEvent(items);
                ((ToDoListListener) listeners[i + 1]).toDoItemsRemoved(e);
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////
    // internal methods
    
    /**
     *  TODO: not done yet(empty implementation), Sort the items by priority.
     *
     *  It has been pointed out that
     *  sorting and priorities will probably be pretty arbitrary and hard
     *  to match with the Designer's (tacit) feelings about the
     *  importance of various items.  We are thinking about a
     *  sort-by-category user interface that would be part of a complete
     *  java PIM (personal information manager, AKA, a daily planner).  */
    private synchronized void sort() {
        // do some sorting?
    }
    
    /** needs documenting */
    public String toString() {
        StringBuffer res = new StringBuffer(100);
        res.append(getClass().getName()).append(" {\n");
        Enumeration cur = elements();
        while (cur.hasMoreElements()) {
            ToDoItem item = (ToDoItem) cur.nextElement();
            res.append("    ").append(item.toString()).append("\n");
        }
        res.append("  }");
        return res.toString();
    }
    
} /* end class ToDoList */
