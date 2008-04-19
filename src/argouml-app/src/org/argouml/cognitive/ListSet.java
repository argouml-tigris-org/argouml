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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Vector;

import org.tigris.gef.util.ChildGenerator;
import org.tigris.gef.util.Predicate;
import org.tigris.gef.util.PredicateTrue;

/**
 * An Ordered, non-duplicated collection of objects (not exactly a
 * mathemetical set because it is ordered).
 * 
 * @param <T> The type of objects this ListSet is to contain.
 */
public class ListSet<T extends Object> 
    implements Serializable, Set<T>, List<T> {
    ////////////////////////////////////////////////////////////////
    // constants
    private static final int TC_LIMIT = 50;

    ////////////////////////////////////////////////////////////////
    // instance variables
    private List<T> list;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    public ListSet() {
        list = new ArrayList<T>();
    }

    /**
     * The constructor.
     *
     * @param n the initial capacity of the ListSet
     */
    public ListSet(int n) {
        list = new ArrayList<T>(n);
    }

    /**
     * The constructor.
     *
     * @param o1 the first object to add
     */
    public ListSet(T o1) {
        list = new ArrayList<T>();
        add(o1);
    }

    /**
     * @param o
     *            the object to add
     * @deprecated for 0.25.4 by tfmorris. Use List methods instead of Vector
     *             methods.
     */
    @Deprecated
    public void addElement(T o) {
        if (!contains(o)) {
            list.add(o);
        }
    }
    
    /**
     * @param v a collection of objects to be added
     * @deprecated for 0.25.4 by tfmorris.  Use {@link #addAll(Collection)}.
     */
    @Deprecated
    public void addAllElements(Collection< ? extends T> v) {
        if (v == null) {
            return;
        }
        addAll(v);
    }

    /**
     * @param iter an enumeration of objects to be added
     */
    public void addAllElements(Enumeration<T> iter) {
        while (iter.hasMoreElements()) {
            add(iter.nextElement());
        }
    }

    /**
     * @param iter an iterator of objects to be added
     */
    public void addAllElements(Iterator<T> iter) {
        while (iter.hasNext()) {
            add(iter.next());
        }
    }

    /**
     * @param iter an iterator of objects to be added
     * @param p the predicate the objects have to fulfill to be added
     */
    public void addAllElementsSuchThat(Iterator<T> iter, Predicate p) {
        if (p instanceof PredicateTrue) {
            addAllElements(iter);
        } else {
            while (iter.hasNext()) {
                T e = iter.next();
                if (p.predicate(e)) {
                    add(e);
                }
            }
        }
    }

    /**
     * @param s a listset of objects to be added
     * @deprecated for 0.25.4 by tfmorris.  Use {@link #addAll(Collection)}.
     */
    @Deprecated
    public void addAllElements(ListSet<T> s) {
        addAll(s);
    }

    /**
     * @param s a listset of objects to be added
     * @param p the predicate the objects have to fulfill to be added
     */
    public void addAllElementsSuchThat(ListSet<T> s, Predicate p) {
        addAllElementsSuchThat(s.iterator(), p);
    }

    /*
     * @see java.util.Collection#remove(java.lang.Object)
     */
    public boolean remove(Object o) {
        boolean result = contains(o);
        if (o != null) {
            list.remove(o);
        }
        return result;
    }

    /**
     * @param o the object to be removed
     */
    public void removeElement(Object o) {
        if (o != null) {
            list.remove(o);
        }
    }

    /**
     * Remove all objects.
     */
    public void removeAllElements() {
        list.clear();
    }

    /*
     * @see java.util.Collection#contains(java.lang.Object)
     */
    public boolean contains(Object o) {
        if (o != null) {
            return list.contains(o);
        }
        return false;
    }

    /**
     * @param p the predicate the objects have to fulfill
     * @return true if at least one object in the listset fulfills the predicate
     */
    public boolean containsSuchThat(Predicate p) {
        return findSuchThat(p) != null;
    }

    /**
     * Return the first object that causes the given predicate to return
     * true.
     *
     * @param p the predicate the objects have to fulfill
     * @return the found object or null
     */
    public Object findSuchThat(Predicate p) {
        for (Object o : list) {
            if (p.predicate(o)) {
                return o;
            }
        }
        return null;
    }

    /**
     * @return all the objects as enumeration
     * @deprecated for 0.25.4 by tfmorris. Use {@link #iterator()}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public Enumeration<T> elements() {
        return asVector().elements();
    }

    /**
     * @param index the location
     * @return the object at the given index
     * @deprecated for 0.25.4 by tfmorris. Use {@link #get(int)}.
     */
    @Deprecated
    public T elementAt(int index) {
        return list.get(index);
    }

    /**
     * @param iter an enumeration of objects to be added
     * @param p the predicate the objects have to fulfill to be added
     */
    public void addAllElementsSuchThat(Enumeration<T> iter, Predicate p) {
        if (p instanceof PredicateTrue) {
            addAllElements(iter);
        } else {
            while (iter.hasMoreElements()) {
                T e = iter.nextElement();
                if (p.predicate(e)) {
                    add(e);
                }
            }
        }
    }

    /**
     * @return all the objects as vector
     * @deprecated for 0.25.4 by tfmorris. Use List methods instead of Vector
     *             methods.
     */
    @Deprecated
    public Vector<T> asVector() {
        return new Vector<T>(list);
    }

    /*
     * @see java.lang.Object#hashCode()
     *
     * This will result in rather bad performance but at least we will
     * not violate the contract together with {@link #equals(Object)}.
     */
    public int hashCode() {
        return 0;
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (!(o instanceof ListSet)) {
            return false;
        }
        ListSet set = (ListSet) o;
        if (set.size() != size()) {
            return false;
        }
        for (Object obj : list) {
            if (!(set.contains(obj))) {
                return false;
            }
        }
        return true;
    }


    /**
     * @return the first object
     * @deprecated for 0.25.4 by tfmorris.  Use {@link #get(int)} get(0).
     */
    @Deprecated
    public T firstElement() {
        return list.get(0);
    }

    /*
     * @see java.util.Collection#size()
     */
    public int size() {
        return list.size();
    }

    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("Set{");        
        for (Iterator it = iterator(); it.hasNext(); ) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Reply the Set of all objects that can be reached from the
     * receiving Set by taking steps defined by the given
     * ChildGenerator.  The result includes the elements of the original
     * Set. In order to avoid very deep searches which are often
     * programming mistakes, only paths of length TC_LIMIT or less are
     * considered.
     *
     * @param cg the given childgenerator
     * @return the resulting listset
     */
    public ListSet<T> transitiveClosure(ChildGenerator cg) {
        return transitiveClosure(cg, TC_LIMIT, PredicateTrue.theInstance());
    }

    /**
     * Reply the Set of all objects that can be reached from the
     * receiving Set by taking steps defined by the given
     * ChildGenerator.  The result DOES NOT include the elements of the
     * original Set. In order to avoid very deep searches which are
     * often programming mistakes, only paths of length TC_LIMIT or less
     * are considered.
     *
     * @param cg the given childgenerator
     * @return the resulting listset
     */
    public ListSet<T> reachable(ChildGenerator cg) {
        return reachable(cg, TC_LIMIT, PredicateTrue.theInstance());
    }

    /**
     * Reply the Set of all objects that can be reached from the
     * receiving Set by taking steps defined by the given
     * ChildGenerator.  The result DOES NOT include the elements of the
     * original Set. In order to avoid very deep searches which are
     * often programming mistakes, only paths of given max length or
     * less are considered. Only paths consisting of elements which all
     * cause p.predicate() to return true are considered.
     *
     * @param cg the given childgenerator
     * @param max the maximum depth
     * @param p the predicate the objects have to fulfill
     * @return the resulting listset
     */
    public ListSet<T> reachable(ChildGenerator cg, int max, Predicate p) {
        ListSet<T> kids = new ListSet<T>();
        for (Object r : list) {
            kids.addAllElementsSuchThat(cg.gen(r), p);
        }
        return kids.transitiveClosure(cg, max, p);
    }

    /**
     * Reply the Set of all objects that can be reached from the
     * receiving Set by taking steps defined by the given
     * ChildGenerator.  The result includes the elements of the original
     * Set. In order to avoid very deep searches which are often
     * programming mistakes, only paths of given max length or less are
     * considered. Only paths consisting of elements which all cause
     * p.predicate() to return true are considered.
     *
     * @param cg the given childgenerator
     * @param max the maximum depth
     * @param p the predicate the objects have to fulfill
     * @return the resulting listset
     * 
     * TODO: This shouldn't depend on GEF.  Replace ChildGenerator in this API.
     * - tfm 20070630
     */
    public ListSet<T> transitiveClosure(ChildGenerator cg, int max, 
            Predicate p) {
        int iterCount = 0;
        int lastSize = -1;
        ListSet<T> touched = new ListSet<T>();
        ListSet<T> frontier;
        ListSet<T> recent = this;

        touched.addAll(this);
        while ((iterCount < max) && (touched.size() > lastSize)) {
            iterCount++;
            lastSize = touched.size();
            frontier = new ListSet<T>();
            for (T recentElement : recent) {
                Enumeration frontsEnum = cg.gen(recentElement);
                frontier.addAllElementsSuchThat(frontsEnum, p);
            }
            touched.addAll(frontier);
            recent = frontier;
        }
        return touched;
    }

    /*
     * @see java.util.Collection#isEmpty()
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /*
     * @see java.util.Collection#iterator()
     */
    public Iterator<T> iterator() {
        return list.iterator();
    }

    /*
     * @see java.util.Collection#toArray()
     */
    public Object[] toArray() {
        return list.toArray();
    }

    /*
     * @see java.util.Collection#toArray(java.lang.Object[])
     */
    public <A> A[] toArray(A[] arg0) {
        return list.toArray(arg0);
    }


    /*
     * @see java.util.Collection#add(java.lang.Object)
     */
    public boolean add(T arg0) {
        boolean result = list.contains(arg0);
        if (!result) {
            list.add(arg0);
        }
        return !result;
    }

    /*
     * @see java.util.Collection#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection arg0) {
        return list.containsAll(arg0);
    }


    /*
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    public boolean addAll(Collection< ? extends T> arg0) {
        return list.addAll(arg0);
    }

    /*
     * @see java.util.Collection#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection< ? > arg0) {
        return list.retainAll(arg0);
    }

    /*
     * @see java.util.Collection#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection arg0) {
        boolean result = false;
        for (Iterator iter = arg0.iterator(); iter.hasNext();) {
            result = result || remove(iter.next());
        }
        return result;

    }

    /*
     * @see java.util.Collection#clear()
     */
    public void clear() {
        list.clear();
    }

    /*
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    public boolean addAll(int arg0, Collection< ? extends T> arg1) {
        return list.addAll(arg0, arg1);
    }

    /*
     * @see java.util.List#get(int)
     */
    public T get(int index) {
        return list.get(index);
    }

    /*
     * @see java.util.List#set(int, java.lang.Object)
     */
    public T set(int arg0, T o) {
        if (contains(o)) {
            list.remove(o);
        }
        return list.set(arg0, o);
    }

    /*
     * @see java.util.List#add(int, java.lang.Object)
     */
    public void add(int arg0, T arg1) {
        if (!list.contains(arg1)) {
            list.add(arg0, arg1);
        }
    }

    /*
     * @see java.util.List#remove(int)
     */
    public T remove(int index) {
        return list.remove(index);
    }

    /*
     * @see java.util.List#indexOf(java.lang.Object)
     */
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    /*
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    /*
     * @see java.util.List#listIterator()
     */
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    /*
     * @see java.util.List#listIterator(int)
     */
    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    /*
     * @see java.util.List#subList(int, int)
     */
    public List<T> subList(int fromIndex, int toIndex) {
        return subList(fromIndex, toIndex);
    }

}
