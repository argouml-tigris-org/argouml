// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
 * An Ordered, non-duplicated collecton of objects (not exactly a
 * mathemetical set because it is ordered).
 */
public class ListSet implements Serializable, Set, List {
    ////////////////////////////////////////////////////////////////
    // constants
    private static final int TC_LIMIT = 50;

    ////////////////////////////////////////////////////////////////
    // instance variables
    private Vector vector;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    public ListSet() {
        vector = new Vector();
    }

    /**
     * The constructor.
     *
     * @param n the initial capacity of the vector
     */
    public ListSet(int n) {
        vector = new Vector(n);
    }

    /**
     * The constructor.
     *
     * @param o1 the first object to add
     */
    public ListSet(Object o1) {
        vector = new Vector();
        addElement(o1);
    }

    /**
     * @param o the object to add
     */
    public void addElement(Object o) {
        if (!contains(o)) {
            vector.addElement(o);
        }
    }

    /**
     * @param v a collection of objects to be added
     */
    public void addAllElements(Collection v) {
        if (v == null) {
            return;
        }
        addAllElements(v.iterator());
    }

    /**
     * @param iter an enumeration of objects to be added
     */
    public void addAllElements(Enumeration iter) {
        while (iter.hasMoreElements()) {
            addElement(iter.nextElement());
        }
    }

    /**
     * @param iter an iterator of objects to be added
     */
    public void addAllElements(Iterator iter) {
        while (iter.hasNext()) {
            addElement(iter.next());
        }
    }

    /**
     * @param iter an enumeration of objects to be added
     * @param p the predicate the objects have to fulfill to be added
     */
    public void addAllElementsSuchThat(Enumeration iter, Predicate p) {
        if (p instanceof PredicateTrue) {
            addAllElements(iter);
        } else {
            while (iter.hasMoreElements()) {
                Object e = iter.nextElement();
                if (p.predicate(e)) {
                    addElement(e);
                }
            }
        }
    }

    /**
     * @param iter an iterator of objects to be added
     * @param p the predicate the objects have to fulfill to be added
     */
    public void addAllElementsSuchThat(Iterator iter, Predicate p) {
        if (p instanceof PredicateTrue) {
            addAllElements(iter);
        } else {
            while (iter.hasNext()) {
                Object e = iter.next();
                if (p.predicate(e)) {
                    addElement(e);
                }
            }
        }
    }

    /**
     * @param s a listset of objects to be added
     */
    public void addAllElements(ListSet s) {
        addAllElements(s.elements());
    }

    /**
     * @param s a listset of objects to be added
     * @param p the predicate the objects have to fulfill to be added
     */
    public void addAllElementsSuchThat(ListSet s, Predicate p) {
        addAllElementsSuchThat(s.elements(), p);
    }

    /*
     * @see java.util.Collection#remove(java.lang.Object)
     */
    public boolean remove(Object o) {
        boolean result = contains(o);
        if (o != null) {
            vector.removeElement(o);
        }
        return result;
    }

    /**
     * @param o the object to be removed
     */
    public void removeElement(Object o) {
        if (o != null) {
            vector.removeElement(o);
        }
    }

    /**
     * Remove all objects.
     */
    public void removeAllElements() {
        vector.removeAllElements();
    }

    /*
     * @see java.util.Collection#contains(java.lang.Object)
     */
    public boolean contains(Object o) {
        if (o != null) {
            return vector.contains(o);
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
        Enumeration elts = elements();
        while (elts.hasMoreElements()) {
            Object o = elts.nextElement();
            if (p.predicate(o)) {
                return o;
            }
        }
        return null;
    }

    /**
     * @return all the objects as enumeration
     */
    public Enumeration elements() {
        return vector.elements();
    }

    /**
     * @param index the location
     * @return the object at the given index
     */
    public Object elementAt(int index) {
        return vector.elementAt(index);
    }

    /**
     * @return all the objects as vector
     */
    public Vector asVector() {
        return vector;
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
        Enumeration myEs = elements();
        while (myEs.hasMoreElements()) {
            Object obj = myEs.nextElement();
            if (!(set.contains(obj))) {
                return false;
            }
        }
        return true;
    }


    /**
     * @return the first object
     */
    public Object firstElement() {
        return vector.firstElement();
    }

    /*
     * @see java.util.Collection#size()
     */
    public int size() {
        return vector.size();
    }

    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String res = "Set{";
        Enumeration eles = elements();
        while (eles.hasMoreElements()) {
            res += eles.nextElement();
            if (eles.hasMoreElements()) {
                res += ", ";
            }
        }
        return res + "}";
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
    public ListSet transitiveClosure(ChildGenerator cg) {
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
    public ListSet reachable(ChildGenerator cg) {
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
    public ListSet reachable(ChildGenerator cg, int max, Predicate p) {
        ListSet kids = new ListSet();
        Enumeration rootEnum = elements();
        while (rootEnum.hasMoreElements()) {
            Object r = rootEnum.nextElement();
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
     */
    public ListSet transitiveClosure(ChildGenerator cg, int max, Predicate p) {
        int iterCount = 0;
        int lastSize = -1;
        ListSet touched = new ListSet();
        ListSet frontier;
        ListSet recent = this;

        touched.addAllElements(this);
        while ((iterCount < max) && (touched.size() > lastSize)) {
            iterCount++;
            lastSize = touched.size();
            frontier = new ListSet();
            Enumeration recentEnum = recent.elements();
            while (recentEnum.hasMoreElements()) {
                Enumeration frontsEnum = cg.gen(recentEnum.nextElement());
                frontier.addAllElementsSuchThat(frontsEnum, p);
            }
            touched.addAllElements(frontier);
            recent = frontier;
        }
        return touched;
    }

    /*
     * @see java.util.Collection#isEmpty()
     */
    public boolean isEmpty() {
        return vector.isEmpty();
    }

    /*
     * @see java.util.Collection#iterator()
     */
    public Iterator iterator() {
        return vector.iterator();
    }

    /*
     * @see java.util.Collection#toArray()
     */
    public Object[] toArray() {
        return vector.toArray();
    }

    /*
     * @see java.util.Collection#toArray(java.lang.Object[])
     */
    public Object[] toArray(Object[] arg0) {
        return vector.toArray(arg0);
    }

    /*
     * @see java.util.Collection#add(java.lang.Object)
     */
    public boolean add(Object arg0) {
        boolean result = contains(arg0);
        if (!result) {
            addElement(arg0);
        }
        return !result;
    }

    /*
     * @see java.util.Collection#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection arg0) {
        return vector.containsAll(arg0);
    }


    /*
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    public boolean addAll(Collection arg0) {
        boolean result = containsAll(arg0);
        addAllElements(arg0);
        return !result;

    }

    /*
     * @see java.util.Collection#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection arg0) {
        Vector copy = (Vector) vector.clone();
        boolean result = false;
        for (Iterator iter = copy.iterator(); iter.hasNext();) {
            Object elem = iter.next();
            if (!arg0.contains(elem)) {
                result = result || remove(elem);
            }
        }
        return result;
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
        vector.clear();
    }

    /*
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    public boolean addAll(int arg0, Collection arg1) {
        return vector.addAll(arg0, arg1);
    }

    /*
     * @see java.util.List#get(int)
     */
    public Object get(int index) {
        return vector.get(index);
    }

    /*
     * @see java.util.List#set(int, java.lang.Object)
     */
    public Object set(int arg0, Object o) {
        if (contains(o)) {
            vector.remove(o);
        }
        return vector.set(arg0, o);
    }

    /*
     * @see java.util.List#add(int, java.lang.Object)
     */
    public void add(int arg0, Object arg1) {
        if (!vector.contains(arg1)) {
            vector.add(arg0, arg1);
        }
    }

    /*
     * @see java.util.List#remove(int)
     */
    public Object remove(int index) {
        return vector.remove(index);
    }

    /*
     * @see java.util.List#indexOf(java.lang.Object)
     */
    public int indexOf(Object o) {
        return vector.indexOf(o);
    }

    /*
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    public int lastIndexOf(Object o) {
        return vector.lastIndexOf(o);
    }

    /*
     * @see java.util.List#listIterator()
     */
    public ListIterator listIterator() {
        return vector.listIterator();
    }

    /*
     * @see java.util.List#listIterator(int)
     */
    public ListIterator listIterator(int index) {
        return listIterator(index);
    }

    /*
     * @see java.util.List#subList(int, int)
     */
    public List subList(int fromIndex, int toIndex) {
        return subList(fromIndex, toIndex);
    }

} /* end class VectorSet */
