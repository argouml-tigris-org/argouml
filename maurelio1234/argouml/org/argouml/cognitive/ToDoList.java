// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.cognitive;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Observable;
import java.util.Set;
import java.util.Vector;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.argouml.cognitive.critics.Critic;
import org.argouml.i18n.Translator;


/**
 * Implements a list of ToDoItem's.<p>
 *
 * It spawns a "sweeper" thread that periodically goes through the list
 * and elimiates ToDoItem's that are no longer valid.<p>
 *
 * One difficulty designers face is keeping track of all
 * the myrid details of their task. It is all to
 * easy to skip a step in the design process,
 * leave part of the design unspecified, of make
 * a mistake that requires revision. ArgoUML provides
 * the designer with a "to do" list user interface
 * that presents action items in an organized form.
 * These items can be suggestions from critics,
 * reminders to finish steps in the process model,
 * or personal notes entered by the designer.
 * The choice control at the top of the "to do"
 * list pane allow the designer to organize
 * items in different ways: by priority,
 * by decision supported, by offending design
 * element, etc.<p>
 *
 * Items are shown under all applicable headings.<p>
 *
 * This class is dependent on Designer.<p>
 *
 * @see Designer#nondisruptivelyWarn
 * @author Jason Robbins
 */
public class ToDoList extends Observable implements Runnable {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ToDoList.class);

    private static Object recentOffender;
    private static Vector<ToDoItem> recentOffenderItems;

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * Pending ToDoItems for the designer to consider.
     */
    private Vector<ToDoItem> items;

    /**
     * These are computed when needed.
     */
    private ListSet allOffenders;

    /**
     * These are computed when needed.
     */
    private ListSet allPosters;

    /**
     * ToDoItems that the designer has explicitly indicated that (s)he
     * considers resolved.<p>
     *
     * TODO: generalize into a design rationale logging facility.
     */
    private LinkedHashSet<ResolvedCritic> resolvedItems;

    /**
     * A Thread that keeps checking if the items on the list are
     * still valid.
     */
    private Thread validityChecker;

    /**
     * The designer, used in determining if a ToDoItem is still valid.
     */
    private Designer designer;

    private EventListenerList listenerList;

    private static int longestToDoList;
    private static int numNotValid;

    /**
     * state variable for whether the validity checking thread is paused
     * (waiting).
     */
    private boolean isPaused;

    ////////////////////////////////////////////////////////////////
    // constructor

    /**
     * Creates a new todolist. The only ToDoList is owned by the Designer.
     */
    ToDoList() {

	items = new Vector<ToDoItem>(100);
	resolvedItems = new LinkedHashSet<ResolvedCritic>(100);
	listenerList = new EventListenerList();
	longestToDoList = 0;
	numNotValid = 0;
	recentOffenderItems = new Vector<ToDoItem>();
    }

    /**
     * Start a Thread to delete old items from the ToDoList.
     *
     * @param d the designer
     */
    public synchronized void spawnValidityChecker(Designer d) {
        designer = d;
        validityChecker = new Thread(this, "ValidityCheckingThread");
        validityChecker.setDaemon(true);
        validityChecker.setPriority(Thread.MIN_PRIORITY);
        validityChecker.start();
    }

    /**
     * Periodically check to see if items on the list are still valid.
     */
    public void run() {
        Vector<ToDoItem> removes = new Vector<ToDoItem>();
        while (true) {

            // the validity checking thread should wait if disabled.
            synchronized (this) {
                while (isPaused) {
                    try {
                        this.wait();
                    } catch (InterruptedException ignore) {
                        LOG.error("InterruptedException!!!", ignore);
                    }
                }
            }

            forceValidityCheck(removes);
            removes.removeAllElements();
            try {
		Thread.sleep(3000);
	    } catch (InterruptedException ignore) {
                LOG.error("InterruptedException!!!", ignore);
            }
        }
    }

    /**
     * Check each ToDoItem on the list to see if it is still valid.  If
     * not, then remove that item.  This is called automatically by the
     * ValidityCheckingThread, and it can be called by the user
     * pressing a button via forceValidityCheck().
     */
    public void forceValidityCheck() {
        Vector<ToDoItem> removes = new Vector<ToDoItem>();
        forceValidityCheck(removes);
    }

    /**
     * Check each ToDoItem on the list to see if it is still valid.  If
     * not, then remove that item.  This is called automatically by the
     * ValidityCheckingThread, and it can be called by the user
     * pressing a button via forceValidityCheck(). <p>
     *
     * <em>Warning: Fragile code!</em> No method that this method calls can
     * synchronized the Designer, otherwise there will be deadlock.
     *
     * @param removes the items removed
     */
    protected synchronized void forceValidityCheck(Vector<ToDoItem> removes) {
        //Enumeration cur = _items.elements();
        int size = items.size();
        for (int i = 0; i < size; ++i) {
            ToDoItem item = items.elementAt(i);
            boolean valid;
            try {
		valid = item.stillValid(designer);
	    } catch (Exception ex) {
                valid = false;
                StringBuffer buf =
                    new StringBuffer("Exception raised in to do list cleaning");
                buf.append("\n");
                buf.append(item.toString());
                LOG.error(buf.toString(), ex);
            }
            if (!valid) {
                numNotValid++;
                removes.addElement(item);
            }
        }
        //cur = removes.elements();
        size = removes.size();
        for (int i = 0; i < size; ++i) {
            ToDoItem item = removes.elementAt(i);
            removeE(item);
//            History.TheHistory.addItemResolution(item, "no longer valid");
            //((ToDoItem)item).resolve("no longer valid");
            //notifyObservers("removeElement", item);
        }
        recomputeAllOffenders();
        recomputeAllPosters();
        fireToDoItemsRemoved(removes);
    }


    /**
     * Pause.
     */
    public void pause() {
        isPaused = true;
    }

    /**
     * Resume.
     */
    public synchronized void resume() {
        notifyAll();
    }

    /**
     * @return true is paused
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * sets the pause state.
     *
     * @param paused if set to false, calls resume() also to start working
     */
    public void setPaused(boolean paused) {
        isPaused = paused;
        if (!isPaused) {
            resume();
	}
    }

    ////////////////////////////////////////////////////////////////
    // Notifications and Updates

    /**
     * @param action the action
     * @param arg the argument
     */
    public void notifyObservers(String action, Object arg) {
        setChanged();
        Vector<Object> v = new Vector<Object>(2);
        v.addElement(action);
        v.addElement(arg);
        super.notifyObservers(v);
    }

    /*
     * @see java.util.Observable#notifyObservers(java.lang.Object)
     */
    public void notifyObservers(Object o) {
        setChanged();
        super.notifyObservers(o);
    }

    /*
     * @see Observable#notifyObservers()
     */
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @return the todo items
     */
    public Vector<ToDoItem> getToDoItems() { return items; }

    /**
     * @return the resolved items
     */
    public Set<ResolvedCritic> getResolvedItems() { return resolvedItems; }

    /**
     * @return the set of offenders
     */
    public ListSet getOffenders() {
        // Extra care to be taken since _allOffenders can be reset while
        // this method is running.
        ListSet all = allOffenders;
        if (all == null) {
            int size = items.size();
            all = new ListSet(size * 2);
            for (int i = 0; i < size; i++) {
                ToDoItem item = items.elementAt(i);
                all.addAllElements(item.getOffenders());
            }
            allOffenders = all;
        }
        return all;
    }

    private void addOffenders(ListSet newoffs) {
        if (allOffenders != null) {
            allOffenders.addAllElements(newoffs);
	}
    }

    /**
     * @return the set of all the posters
     */
    public ListSet getPosters() {
        // Extra care to be taken since _allPosters can be reset while
        // this method is running.
        ListSet all = allPosters;
        if (all == null) {
            int size = items.size();
            all = new ListSet();
            for (int i = 0; i < size; i++) {
                ToDoItem item = items.elementAt(i);
                all.addElement(item.getPoster());
            }
            allPosters = all;
        }
        return all;
    }

    private void addPosters(Poster newp) {
        if (allPosters != null) {
            allPosters.addElement(newp);
	}
    }

    /**
     * @return the decisions
     */
    public static Vector getDecisions() { return new Vector(); }

    /**
     * @return the goals
     */
    public static Vector getGoals() { return new Vector(); }

    /*
     * TODO: needs documenting, why synchronized?
     */
    private synchronized void addE(ToDoItem item) {
        /* remove any identical items already on the list */
        if (items.contains(item)) {
            return;
	}

        if (item.getPoster() instanceof Critic) {
            ResolvedCritic rc;
            try {
                rc =
		    new ResolvedCritic((Critic) item.getPoster(),
				       item.getOffenders(),
				       false);
                Iterator<ResolvedCritic> elems = resolvedItems.iterator();
                //cat.debug("Checking for inhibitors " + rc);
                while (elems.hasNext()) {
                    if (elems.next().equals(rc)) {
                        LOG.debug("ToDoItem not added because it was resolved");
                        return;
                    }
                }
            } catch (UnresolvableException ure) {
            }
        }

        items.addElement(item);
        longestToDoList = Math.max(longestToDoList, items.size());
        addOffenders(item.getOffenders());
        addPosters(item.getPoster());
//        if (item.getPoster() instanceof Designer)
//            History.TheHistory.addItem(item, "note: ");
//        else
//            History.TheHistory.addItemCritique(item);
        notifyObservers("addElement", item);
        fireToDoItemAdded(item);
    }

    /**
     * @param item the todo item to be added
     */
    public synchronized void addElement(ToDoItem item) {
        addE(item);
    }


    /**
     * @param list the todo items to be removed
     */
    public void removeAll(ToDoList list) {
        Enumeration<ToDoItem> cur = list.elements();
        while (cur.hasMoreElements()) {
            ToDoItem item = cur.nextElement();
            removeE(item);
        }
        recomputeAllOffenders();
        recomputeAllPosters();
        fireToDoItemsRemoved(list.getToDoItems());
    }

    /**
     * @param item the todo item to be removed
     * @return <code>true</code> if the argument was a component of this
     *          vector; <code>false</code> otherwise
     */
    private synchronized boolean removeE(ToDoItem item) {
        boolean res = items.removeElement(item);
        return res;
    }

    /**
     * @param item the todo item to be removed
     * @return <code>true</code> if the argument was a component of this
     *          vector; <code>false</code> otherwise
     */
    public boolean removeElement(ToDoItem item) {
        boolean res = removeE(item);
        recomputeAllOffenders();
        recomputeAllPosters();
        fireToDoItemRemoved(item);
        notifyObservers("removeElement", item);
        return res;
    }

    /**
     * @param item the todo item to be resolved
     * @return <code>true</code> if the argument was a component of this
     *          vector; <code>false</code> otherwise
     */
    public boolean resolve(ToDoItem item) {
        boolean res = removeE(item);
        fireToDoItemRemoved(item);
        return res;
    }

    /**
     * @param item the todo item
     * @param reason the reason TODO: Use it!
     * @return <code>true</code> if the argument was a component of this
     *          vector; <code>false</code> otherwise
     * @throws UnresolvableException unable to resolve
     */
    public boolean explicitlyResolve(ToDoItem item, String reason)
	throws UnresolvableException {

        if (item.getPoster() instanceof Designer) {
            boolean res = resolve(item);
//            History.TheHistory.addItemResolution(item, reason);
            return res;
        }

        if (!(item.getPoster() instanceof Critic)) {
            throw new UnresolvableException(Translator.localize(
                    "misc.todo-unresolvable", 
                    new Object[]{item.getPoster().getClass()}));
	}

        ResolvedCritic rc =
	    new ResolvedCritic((Critic) item.getPoster(),
			       item.getOffenders());
        boolean res = resolve(item);
        if (res) {
            res = addResolvedCritic(rc);
        }
        return res;
    }

    /**
     * Add the given resolved critic to the list of resolved critics.
     *
     * @param rc the resolved critic
     * @return <code>true</code> if successfully added;
     *         <code>false</code> otherwise
     */
    public boolean addResolvedCritic(ResolvedCritic rc) {
        return resolvedItems.add(rc);
    }

    /**
     * Remove all todo items.
     */
    public synchronized void removeAllElements() {
        LOG.debug("removing all todo items");
        Vector<ToDoItem> oldItems = new Vector<ToDoItem>(items);
        for (ToDoItem tdi : oldItems) {
            removeE(tdi);
	}

        recomputeAllOffenders();
        recomputeAllPosters();
        notifyObservers("removeAllElements");
        fireToDoItemsRemoved(oldItems);
    }

    /**
     * @param off the offender
     * @return the todo tems for this offender
     */
    public Vector<ToDoItem> elementsForOffender(Object off) {
        if (off == recentOffender) {
	    return recentOffenderItems;
	}
        recentOffender = off;
        recentOffenderItems.removeAllElements();
        synchronized (items) {
            for (int i = 0; i < items.size(); i++) {
                ToDoItem item = items.elementAt(i);
                if (item.getOffenders().contains(off)) {
                    recentOffenderItems.addElement(item);
		}
            }
        }
        return recentOffenderItems;
    }

    /**
     * @return the number of todo items
     */
    public int size() { return items.size(); }

    /**
     * @return the todo items
     */
    public Enumeration<ToDoItem> elements() {
        return items.elements();
    }

    /**
     * @param index an index into the todo items list
     * @return the item at the index
     */
    public ToDoItem elementAt(int index) {
        return items.elementAt(index);
    }

    /**
     * Re-compute all offenders.
     */
    protected void recomputeAllOffenders() {
        allOffenders = null;
    }

    /**
     * Reset all posters.
     */
    protected void recomputeAllPosters() {
        allPosters = null;
    }


    ////////////////////////////////////////////////////////////////
    // event related stuff

    /**
     * @param l the listener to be added
     */
    public void addToDoListListener(ToDoListListener l) {
        listenerList.add(ToDoListListener.class, l);
    }

    /**
     * @param l the listener to be removed
     */
    public void removeToDoListListener(ToDoListListener l) {
        listenerList.remove(ToDoListListener.class, l);
    }

    /**
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireToDoListChanged() {
        recentOffender = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        ToDoListEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ToDoListListener.class) {
                // Lazily create the event:
                if (e == null) {
		    e = new ToDoListEvent();
		}
                ((ToDoListListener) listeners[i + 1]).toDoListChanged(e);
            }
        }
    }

    /**
     * @param item the todo item
     */
    protected void fireToDoItemChanged(ToDoItem item) {
        Object[] listeners = listenerList.getListenerList();
        ToDoListEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ToDoListListener.class) {
                // Lazily create the event:
                if (e == null) {
                    Vector<ToDoItem> its = new Vector<ToDoItem>();
                    its.addElement(item);
                    e = new ToDoListEvent(its);
                }
                ((ToDoListListener) listeners[i + 1]).toDoItemsChanged(e);
            }
        }
    }

    /**
     * @param item the todo item
     */
    protected void fireToDoItemAdded(ToDoItem item) {
        Vector<ToDoItem> v = new Vector<ToDoItem>();
        v.addElement(item);
        fireToDoItemsAdded(v);
    }

    /**
     * @param theItems the todo items
     */
    protected void fireToDoItemsAdded(Vector<ToDoItem> theItems) {
        recentOffender = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        ToDoListEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ToDoListListener.class) {
                // Lazily create the event:
                if (e == null) {
		    e = new ToDoListEvent(theItems);
		}
                ((ToDoListListener) listeners[i + 1]).toDoItemsAdded(e);
            }
        }
    }

    /**
     * @param item the todo item
     */
    protected void fireToDoItemRemoved(ToDoItem item) {
        Vector<ToDoItem> v = new Vector<ToDoItem>();
        v.addElement(item);
        fireToDoItemsRemoved(v);
    }

    /**
     * @param theItems the todo items
     */
    protected void fireToDoItemsRemoved(Vector<ToDoItem> theItems) {
        recentOffender = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        ToDoListEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ToDoListListener.class) {
                // Lazily create the event:
                if (e == null) {
		    e = new ToDoListEvent(theItems);
		}
                ((ToDoListListener) listeners[i + 1]).toDoItemsRemoved(e);
            }
        }
    }

    ////////////////////////////////////////////////////////////////
    // internal methods

    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer res = new StringBuffer(100);
        res.append(getClass().getName()).append(" {\n");
        Enumeration<ToDoItem> cur = elements();
        while (cur.hasMoreElements()) {
            ToDoItem item = cur.nextElement();
            res.append("    ").append(item.toString()).append("\n");
        }
        res.append("  }");
        return res.toString();
    }

    /**
     * The UID.
     */
    // private static final long serialVersionUID = -1288801672594900893L;
} /* end class ToDoList */
