/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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
import java.util.Map;

/**
 * An implementation for the Bag interface which is backed by a HashMap.
 * 
 * @param <E>
 * @author maas
 */
public class HashBag<E> implements Bag<E> {

    private Map<E, Integer> map = new HashMap<E, Integer>();

    /**
     * Default Constructor
     * 
     */
    public HashBag() {
    }
    
    /**
     * Creates a new Bag from a Collection
     * 
     * @param col the collection
     */
    public HashBag(Collection col) {
        this();
        addAll(col);
    }

    /*
     * @see org.argouml.profile.internal.ocl.uml14.Bag#count(java.lang.Object)
     */
    public int count(Object element) {
        Integer c = map.get(element);
        return c == null ? 0 : c;
    }


    public boolean add(E e) {
        if (e != null) {
            if (map.get(e) == null) {
                map.put(e, 1);
            } else {
                map.put(e, map.get(e) + 1);
            }
        }
        return true;
    }


    @SuppressWarnings("unchecked")
    public boolean addAll(Collection c) {
        for (Object object : c) {
            add((E) object);
        }
        return true;
    }


    public void clear() {
        map.clear();
    }


    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    public boolean containsAll(Collection c) {
        return map.keySet().containsAll(c);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    public boolean remove(Object o) {
        return (map.remove(o) == null);
    }


    public boolean removeAll(Collection c) {
        boolean changed = false;
        for (Object object : c) {
            changed |= remove(object);
        }
        return changed;
    }

    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the number of elements in this bag
     * @see java.util.Set#size()
     */
    public int size() {
        int sum = 0;

        for (E e : map.keySet()) {
            sum += count(e);
        }

        return sum;
    }


    public Object[] toArray() {
        return map.keySet().toArray();
    }


    public <T> T[] toArray(T[] a) {
        return map.keySet().toArray(a);
    }

    @Override
    public String toString() {
        return map.toString();
    }
    

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Bag) {
            Bag bag = (Bag) obj;
            for (Object object : bag) {
                if (count(object) != bag.count(object)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return map.hashCode() * 35;
    }
}
