/* $Id$
 *******************************************************************************
 * Copyright (c) 2010-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 *
 * Some portions of this file were previously release using the BSD License:
 */

// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import org.argouml.i18n.Translator;
import org.argouml.model.InvalidElementException;

/**
 * Implements a list of ToDoItem's.
 * <p>
 *
 * It spawns a "sweeper" thread that periodically goes through the list and
 * eliminates ToDoItem's that are no longer valid.
 * <p>
 *
 * One difficulty designers face is keeping track of all the myriad details of
 * their task. It is all too easy to skip a step in the design process, leave
 * part of the design unspecified, or make a mistake that requires revision.
 * ArgoUML provides the designer with a "to do" list user interface that
 * presents action items in an organized form. These items can be suggestions
 * from critics, reminders to finish steps in the process model, or personal
 * notes entered by the designer. The choice control at the top of the "to do"
 * list pane allow the designer to organize items in different ways: by
 * priority, by decision supported, by offending design element, etc.
 * <p>
 *
 * Items are shown under all applicable headings.
 * <p>
 *
 * This class is dependent on Designer.
 * <p>
 *
 * @see Designer#inform
 * @author Jason Robbins
 */
public class ToDoList extends Observable implements Runnable {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ToDoList.class.getName());

    /**
     * Number of seconds thread should sleep between passes
     */
    private static final int SLEEP_SECONDS = 3;

    /**
     * Pending ToDoItems for the designer to consider.
     */
    private List<ToDoItem> items;

    private Set<ToDoItem> itemSet;

    /**
     * These are computed when needed.
     */
    // TODO: Offenders need to be more strongly typed. - tfm 20070630
    private volatile ListSet allOffenders;

    /**
     * These are computed when needed.
     */
    private volatile ListSet<Poster> allPosters;

    /**
     * ToDoItems that the designer has explicitly indicated that (s)he considers
     * resolved.
     * <p>
     * TODO: generalize into a design rationale logging facility.
     */
    private Set<ResolvedCritic> resolvedItems;

    /**
     * A Thread that keeps checking if the items on the list are still valid.
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

    private Object pausedMutex = new Object();

    /**
     * Creates a new todolist. The only ToDoList is owned by the Designer.
     */
    ToDoList() {

        items = Collections.synchronizedList(new ArrayList<ToDoItem>(100));
        itemSet = Collections.synchronizedSet(new HashSet<ToDoItem>(100));
        resolvedItems =
            Collections.synchronizedSet(new LinkedHashSet<ResolvedCritic>(100));
        listenerList = new EventListenerList();
        longestToDoList = 0;
        numNotValid = 0;
    }

    /**
     * Start a Thread to delete old items from the ToDoList.
     *
     * @param d the designer
     */
    public synchronized void spawnValidityChecker(Designer d) {
        designer = d;
        validityChecker = new Thread(this, "Argo-ToDoValidityCheckingThread");
        validityChecker.setDaemon(true);
        validityChecker.setPriority(Thread.MIN_PRIORITY);
        setPaused(false);
        validityChecker.start();
    }

    /**
     * Entry point for validity checker thread. Periodically check to see if
     * items on the list are still valid.
     */
    public void run() {
        final List<ToDoItem> removes = new ArrayList<ToDoItem>();

        while (true) {

            // the validity checking thread should wait if disabled.
            synchronized (pausedMutex) {
                while (isPaused) {
                    try {
                        pausedMutex.wait();
                    } catch (InterruptedException ignore) {
                        LOG.log(Level.SEVERE, "InterruptedException!!!", ignore);
                    }
                }
            }

            forceValidityCheck(removes);
            removes.clear();
            try {
                Thread.sleep(SLEEP_SECONDS * 1000);
            } catch (InterruptedException ignore) {
                LOG.log(Level.SEVERE, "InterruptedException!!!", ignore);
            }
        }
    }

    /**
     * Check each ToDoItem on the list to see if it is still valid. If not, then
     * remove that item. This is called automatically by the
     * ValidityCheckingThread, and it can be called by the user pressing a
     * button via forceValidityCheck().
     */
    public void forceValidityCheck() {
        final List<ToDoItem> removes = new ArrayList<ToDoItem>();
        forceValidityCheck(removes);
    }

    /**
     * Check each ToDoItem on the list to see if it is still valid. If not, then
     * remove that item. This is called automatically by the
     * ValidityCheckingThread, and it can be called by the user pressing a
     * button via forceValidityCheck().
     * <p>
     *
     * <em>Warning: Fragile code!</em> No method that this method calls can
     * synchronized the Designer, otherwise there will be deadlock.
     *
     * @param removes a list containing the items to be removed
     */
    private synchronized void forceValidityCheck(
            final List<ToDoItem> removes) {
        synchronized (items) {
            for (ToDoItem item : items) {
                boolean valid;
                try {
                    valid = item.stillValid(designer);
                } catch (InvalidElementException ex) {
                    // If element has been deleted, it's no longer valid
                    valid = false;
                } catch (Exception ex) {
                    valid = false;
                    StringBuffer buf = new StringBuffer(
                            "Exception raised in ToDo list cleaning");
                    buf.append("\n");
                    buf.append(item.toString());
                    LOG.log(Level.SEVERE,buf.toString(), ex);
                }
                if (!valid) {
                    numNotValid++;
                    removes.add(item);
                }
            }
        }

        for (ToDoItem item : removes) {
            removeE(item);
            // History.TheHistory.addItemResolution(item,
            // "no longer valid");
            // ((ToDoItem)item).resolve("no longer valid");
            // notifyObservers("removeElement", item);
        }
        recomputeAllOffenders();
        recomputeAllPosters();
        fireToDoItemsRemoved(removes);
    }

    /**
     * Pause the validity checking thread.
     */
    public void pause() {
        synchronized (pausedMutex) {
            isPaused = true;
        }
    }

    /**
     * Resume the validity checking thread.
     */
    public void resume() {
        synchronized (pausedMutex) {
            isPaused = false;
            pausedMutex.notifyAll();
        }
    }

    /**
     * @return true is paused
     */
    public boolean isPaused() {
        synchronized (pausedMutex) {
            return isPaused;
        }
    }

    /**
     * sets the pause state.
     *
     * @param paused if set to false, calls resume() also to start working
     */
    public void setPaused(boolean paused) {
        if (paused) {
            pause();
        } else {
            resume();
        }
    }

    // //////////////////////////////////////////////////////////////
    // Notifications and Updates

    /**
     * @param action the action
     * @param arg the argument
     */
    public void notifyObservers(String action, Object arg) {
        setChanged();
        List<Object> l = new ArrayList<Object>(2);
        l.add(action);
        l.add(arg);
        super.notifyObservers(l);
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


    /**
     * Returns the List of the ToDoItems.  It is <em>mandatory</em> that
     * code iterating over this list synchronize access to the list as described
     * in {@link Collections#synchronizedList(List)}.
     * <pre>
     *  List<ToDoItem> list = toDoList.getToDoItemList();
     *      ...
     *  synchronized(list) {
     *      for (ToDoItem item : list ) { // Must be in synchronized block
     *      ....
     *  }
     * </pre>
     * @see Collections#synchronizedList(List)
     * @return the List of ToDo items.
     */
    public List<ToDoItem> getToDoItemList() {
        return items;
    }

    /**
     * Returns the set of ResolvedCritics.  It is <em>mandatory</em> that
     * code iterating over this set synchronize access to the set as described
     * in {@link Collections#synchronizedSet(Set)}.
     * <pre>
     *  Set<ResolvedCritic> set = toDoList.getResolvedItems();
     *      ...
     *  synchronized(set) {
     *      for (ResolvedCritic item : set ) { // Must be in synchronized block
     *      ....
     *  }
     * </pre>
     * @see Collections#synchronizedSet(Set)
     * @return the resolved items
     */
    public Set<ResolvedCritic> getResolvedItems() {
        return resolvedItems;
    }

    /**
     * @return the set of offenders
     *
     * TODO: The return value needs to be more strongly typed. - tfm - 20070630
     */
    public ListSet getOffenders() {
        // Extra care to be taken since allOffenders can be reset while
        // this method is running.
        ListSet all = allOffenders;
        if (all == null) {
            int size = items.size();
            all = new ListSet(size * 2);
            synchronized (items) {
                for (ToDoItem item : items) {
                    all.addAll(item.getOffenders());
                }
            }
            allOffenders = all;
        }
        return all;
    }

    private void addOffenders(ListSet newoffs) {
        if (allOffenders != null) {
            allOffenders.addAll(newoffs);
        }
    }

    /**
     * @return the set of all the posters
     */
    public ListSet<Poster> getPosters() {
        // Extra care to be taken since allPosters can be reset while
        // this method is running.
        ListSet<Poster> all = allPosters;
        if (all == null) {
            all = new ListSet<Poster>();
            synchronized (items) {
                for (ToDoItem item : items) {
                    all.add(item.getPoster());
                }
            }
            allPosters = all;
        }
        return all;
    }

    private void addPosters(Poster newp) {
        if (allPosters != null) {
            allPosters.add(newp);
        }
    }


    /**
     * @return the list of Decisions (empty by default).
     */
    public static List<Decision> getDecisionList() {
        return new ArrayList<Decision>();
    }


    /**
     * @return The list of Goals (empty by default).
     */
    public static List<Goal> getGoalList() {
        return new ArrayList<Goal>();
    }

    /*
     * TODO: needs documenting, why synchronized?
     */
    private void addE(ToDoItem item) {
        /* skip any identical items already on the list */
        if (itemSet.contains(item)) {
            return;
        }

        if (item.getPoster() instanceof Critic) {
            ResolvedCritic rc;
            try {
                rc = new ResolvedCritic((Critic) item.getPoster(), item
                        .getOffenders(), false);
                Iterator<ResolvedCritic> elems = resolvedItems.iterator();
                // cat.debug("Checking for inhibitors " + rc);
                while (elems.hasNext()) {
                    if (elems.next().equals(rc)) {
                        LOG.log(Level.FINE, "ToDoItem not added because it was resolved");
                        return;
                    }
                }
            } catch (UnresolvableException ure) {
            }
        }

        items.add(item);
        itemSet.add(item);
        longestToDoList = Math.max(longestToDoList, items.size());
        addOffenders(item.getOffenders());
        addPosters(item.getPoster());
        // if (item.getPoster() instanceof Designer)
        // History.TheHistory.addItem(item, "note: ");
        // else
        // History.TheHistory.addItemCritique(item);
        notifyObservers("addElement", item);
        fireToDoItemAdded(item);
    }

    /**
     * @param item the todo item to be added
     */
    public void addElement(ToDoItem item) {
        addE(item);
    }

    /**
     * @param list the todo items to be removed
     */
    public void removeAll(ToDoList list) {
        List<ToDoItem> itemList = list.getToDoItemList();
        synchronized (itemList) {
            for (ToDoItem item : itemList) {
                removeE(item);
            }
            recomputeAllOffenders();
            recomputeAllPosters();
            fireToDoItemsRemoved(itemList);
        }
    }

    /**
     * @param item the todo item to be removed
     * @return <code>true</code> if the argument was a component of this list;
     *         <code>false</code> otherwise
     */
    private boolean removeE(ToDoItem item) {
        itemSet.remove(item);
        return items.remove(item);
    }

    /**
     * @param item the todo item to be removed
     * @return <code>true</code> if the argument was a component of this list;
     *         <code>false</code> otherwise
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
     * @return <code>true</code> if the argument was a component of this list;
     *         <code>false</code> otherwise
     */
    public boolean resolve(ToDoItem item) {
        boolean res = removeE(item);
        fireToDoItemRemoved(item);
        return res;
    }

    /**
     * @param item the todo item
     * @param reason the reason TODO: Use it!
     * @return <code>true</code> if the argument was a component of this list;
     *         <code>false</code> otherwise
     * @throws UnresolvableException unable to resolve
     */
    public boolean explicitlyResolve(ToDoItem item, String reason)
        throws UnresolvableException {

        if (item.getPoster() instanceof Designer) {
            boolean res = resolve(item);
            // History.TheHistory.addItemResolution(item, reason);
            return res;
        }

        if (!(item.getPoster() instanceof Critic)) {
            throw new UnresolvableException(Translator.localize(
                    "misc.todo-unresolvable", new Object[] {item.getPoster()
                            .getClass()}));
        }

        ResolvedCritic rc = new ResolvedCritic((Critic) item.getPoster(), item
                .getOffenders());
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
     * @return <code>true</code> if successfully added; <code>false</code>
     *         otherwise
     */
    public boolean addResolvedCritic(ResolvedCritic rc) {
        return resolvedItems.add(rc);
    }

    /**
     * Remove all todo items.
     */
    public void removeAllElements() {
        LOG.log(Level.FINE, "removing all todo items");
        List<ToDoItem> oldItems = new ArrayList<ToDoItem>(items);
        items.clear();
        itemSet.clear();

        recomputeAllOffenders();
        recomputeAllPosters();
        notifyObservers("removeAllElements");
        fireToDoItemsRemoved(oldItems);
    }


    /**
     * @param offender the offender
     * @return A List of todo items for this offender.
     *         <p>
     *         Note: the previous implementation returned an internal static
     *         (global) list which could be modified at any point, requiring the
     *         caller to copy the list before using it (negating the value of
     *         caching the static copy). The current implementation returns a
     *         private copy which will not change, so callers don't need to copy
     *         it.
     */
    public List<ToDoItem> elementListForOffender(Object offender) {
        List<ToDoItem> offenderItems = new ArrayList<ToDoItem>();
        synchronized (items) {
            for (ToDoItem item : items) {
                if (item.getOffenders().contains(offender)) {
                    offenderItems.add(item);
                }
            }
        }
        return offenderItems;
    }

    /**
     * @return the number of todo items
     */
    public int size() {
        return items.size();
    }

    /**
     * @param index 0-based index to retrieve ToDoItem from
     * @return the ToDoItem at the given index
     */
    public ToDoItem get(int index) {
        return items.get(index);
    }

    /**
     * Re-compute all offenders.
     */
    private void recomputeAllOffenders() {
        allOffenders = null;
    }

    /**
     * Reset all posters.
     */
    private void recomputeAllPosters() {
        allPosters = null;
    }

    // //////////////////////////////////////////////////////////////
    // event related stuff

    /**
     * @param l the listener to be added
     */
    public void addToDoListListener(ToDoListListener l) {
        // EventListenerList.add() is synchronized, so we don't need to
        // synchronize ourselves
        listenerList.add(ToDoListListener.class, l);
    }

    /**
     * @param l the listener to be removed
     */
    public void removeToDoListListener(ToDoListListener l) {
        // EventListenerList.remove() is synchronized, so we don't need to
        // synchronize ourselves
        listenerList.remove(ToDoListListener.class, l);
    }


    /**
     * @param item the todo item
     */
    void fireToDoItemChanged(ToDoItem item) {
        Object[] listeners = listenerList.getListenerList();
        ToDoListEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ToDoListListener.class) {
                // Lazily create the event:
                if (e == null) {
                    List<ToDoItem> its = new ArrayList<ToDoItem>();
                    its.add(item);
                    e = new ToDoListEvent(its);
                }
                ((ToDoListListener) listeners[i + 1]).toDoItemsChanged(e);
            }
        }
    }

    /**
     * @param item the todo item
     */
    private void fireToDoItemAdded(ToDoItem item) {
        List<ToDoItem> l = new ArrayList<ToDoItem>();
        l.add(item);
        fireToDoItemsAdded(l);
    }

    /**
     * @param theItems the todo items
     */
    private void fireToDoItemsAdded(List<ToDoItem> theItems) {
        if (theItems.size() > 0) {
            // Guaranteed to return a non-null array
            final Object[] listeners = listenerList.getListenerList();
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
    }

    /**
     * @param item the todo item
     */
    private void fireToDoItemRemoved(ToDoItem item) {
        List<ToDoItem> l = new ArrayList<ToDoItem>();
        l.add(item);
        fireToDoItemsRemoved(l);
    }

    /**
     * @param theItems the todo items
     */
    private void fireToDoItemsRemoved(final List<ToDoItem> theItems) {
        if (theItems.size() > 0) {
            // Guaranteed to return a non-null array
            final Object[] listeners = listenerList.getListenerList();
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
    }

    @Override
    public String toString() {
        StringBuffer res = new StringBuffer(100);
        res.append(getClass().getName()).append(" {\n");
        List<ToDoItem> itemList = getToDoItemList();
        synchronized (itemList) {
            for (ToDoItem item : itemList) {
                res.append("    ").append(item.toString()).append("\n");
            }
        }
        res.append("  }");
        return res.toString();
    }

}
