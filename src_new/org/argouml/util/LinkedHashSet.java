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

package org.argouml.util;

import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This implements all the same methods of the JDK1.4 LinkedHashSet class and is
 * provided to give the same functionality for previous versions of JDK.
 * @see http://java.sun.com/j2se/1.4.2/docs/api/java/util/LinkedHashSet.html
 *
 * @author Bob Tarling
 */
public class LinkedHashSet extends HashSet {
    
    private java.util.LinkedList list;
    /** Creates a new instance of LinkedHashSet */
    public LinkedHashSet() {
        super();
        list = new LinkedList();
    }
    
    LinkedHashSet(Collection c) {
        super(c);
        list = new LinkedList(c);
    }
    
    LinkedHashSet(int initialCapacity) {
        super(initialCapacity);
        list = new LinkedList();
    }
    
    LinkedHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        list = new LinkedList();
    }
    
    public boolean add(Object o) {
        boolean changed = super.add(o);
        if (changed) list.add(o);
        return changed;
    }
    
    public boolean remove(Object o) {
        boolean found = super.remove(o);
        if (found) list.remove(o);
        return found;
    }
    
    public boolean retainAll(Collection c) {
        boolean changed = false;
        Iterator it = iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (!c.contains(o)) {
                changed = true;
                it.remove();
            }
        }
        return changed;
    }
    
    public Object[] toArray() {
        return list.toArray();
    }
    
    public Object[] toArray(Object[] a) {
        return list.toArray(a);
    }
    
    public void clear() {
        super.clear();
        list.clear();
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof LinkedHashSet)) return false;
        LinkedHashSet rhs = (LinkedHashSet)o;
        return list.equals(rhs.list);
    }
    
    public Iterator iterator() {
        return new LinkedHashSetIterator(list.iterator());
    }
    
    int indexOf(Object o) {
        return list.indexOf(o);
    }

    Object remove(int index) {
        Object o = list.remove(index);
        super.remove(o);
        return o;
    }

    Object get(int index) {
        return list.get(index);
    }
    
    private class LinkedHashSetIterator implements Iterator {
        private Iterator listIterator;
        private Object lastObject = null;
        private boolean valid = false;
        
        LinkedHashSetIterator(Iterator listIterator) {
            this.listIterator = listIterator;
        }
        
        public boolean hasNext() {
            return listIterator.hasNext();
        }
        
        public Object next() {
            lastObject = listIterator.next();
            valid = true;
            return lastObject;
        }
        
        public void remove() {
            if (!valid) throw new IllegalStateException();
            LinkedHashSet.super.remove(lastObject);
            listIterator.remove();
            valid = false;
        }
    }
}
