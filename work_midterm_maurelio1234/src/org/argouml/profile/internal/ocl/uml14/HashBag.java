// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.profile.internal.ocl.uml14;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


/**
 * An implementation for the Bag interface
 * 
 * @param <E>
 * @author maas
 */
public class HashBag<E> implements Bag<E> {

    private HashMap<E,Integer> map = new HashMap<E,Integer>(); 
    
    /**
     * Creates a new Bag from a Collection
     * 
     * @param col
     */
    @SuppressWarnings("unchecked")
    public HashBag(Collection col) {
        addAll(col);
    }
    
    /**
     * @see org.argouml.profile.internal.ocl.uml14.Bag#count(java.lang.Object)
     */
    public int count(Object element) {
        Integer c = map.get(element);
        return  c == null ? 0 : c;
    }

    /**
     * @param e
     * @see java.util.Set#add(java.lang.Object)
     */
    public boolean add(E e) {
        if (map.get(e) == null) {
            map.put(e, 0);
        } else {
            map.put(e, map.get(e) + 1);
        }
        
        return true;
    }

    /**
     * @see java.util.Set#addAll(java.util.Collection)
     */
    @SuppressWarnings("unchecked")
    public boolean addAll(Collection c) {
        for (Object object : c) {
            add((E) object);
        }        
        return true;
    }

    /**
     * @see java.util.Set#clear()
     */
    public void clear() {
        map.clear();
    }

    /**
     * @see java.util.Set#contains(java.lang.Object)
     */
    public boolean contains(Object o) {
        return count(o) > 0;            
    }

    /**
     * @see java.util.Set#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection c) {
        for (Object object : c) {
            if (!contains(object)) return false;
        }
        return true;
    }

    /**
     * @see java.util.Set#isEmpty()
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * @see java.util.Set#iterator()
     */
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    /**
     * @see java.util.Set#remove(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        boolean c = contains(o);
        map.put((E) o, null);
        return c;
    }

    /**
     * @param c
     * @return if the bag changed
     * @see java.util.Set#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection c) {
        boolean changed = false;
        for (Object object : c) {
            changed |= remove(object);
        }
        return changed;
    }

    /**
     * @param c
     * @see java.util.Set#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the number of elements in this bag
     * @see java.util.Set#size()
     */
    public int size() {
        int sum = 0;
        
        Iterator<E> it = iterator();
        while(it.hasNext()) {
            E next = it.next();
            sum += count(next);
        }
        return sum;
    }

    /**
     * @see java.util.Set#toArray()
     */
    public Object[] toArray() {
        return map.keySet().toArray();
    }

    /**
     * @see java.util.Set#toArray(T[])
     */
    public <T> T[] toArray(T[] a) {
        return map.keySet().toArray(a);
    }

}
