// $Id$
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

package org.argouml.uml.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractListModel;



/**
 * A ListModel which keeps the list in sorted order. Many, but not all, of the
 * methods from the java.util.List are implemented. Those which are obey its
 * contract.
 * <p>
 * This is a low performance implementation designed for use with small lists
 * (such as typically appear in the GUI). It does a linear search of the 
 * set for any indexed operations (e.g. getElementAt(int)).
 */
public class SortedListModel extends AbstractListModel implements Collection {
    
    private Set delegate = new TreeSet(new PathComparator());

    /**
     * Returns the number of components in this list.
     * <p>
     * This method is identical to <code>size</code>, which implements the 
     * <code>List</code> interface defined in the 1.2 Collections framework.
     * This method exists in conjunction with <code>setSize</code> so that
     * <code>size</code> is identifiable as a JavaBean property.
     *
     * @return  the number of components in this list
     * @see #size()
     */
    public int getSize() {
        return delegate.size();
    }

    /**
     * Returns the component at the specified index.
     * <blockquote>
     * <b>Note:</b> Although this method is not deprecated, the preferred
     *    method to use is <code>get(int)</code>, which implements the 
     *    <code>List</code> interface defined in the 1.2 Collections framework.
     * </blockquote>
     * @param      index   an index into this list
     * @return     the component at the specified index
     * @exception  ArrayIndexOutOfBoundsException  if the <code>index</code> 
     *             is negative or greater than the current size of this 
     *             list
     * @see #get(int)
     */
    public Object getElementAt(int index) {
        Object result = null;
        // TODO: If this turns out to be a performance bottleneck, we can 
        // probably optimize the common case by caching our iterator and current
        // position, assuming that the next request will be for a greater index
        Iterator it = delegate.iterator();
        while (index >= 0) {
            if (it.hasNext()) {
                result = it.next();
            } else {
                throw new ArrayIndexOutOfBoundsException();
            }
            index--;
        }
        return result;
    }

    /**
     * Returns the component at the specified index.
     * @param      index   an index into this list
     * @return     the component at the specified index
     * @exception  ArrayIndexOutOfBoundsException  if the <code>index</code> 
     *             is negative or greater than the current size of this 
     *             list
     */
    public Object get(int index) {
        return getElementAt(index);
    }
    
    /**
     * @param o object to search for
     * @return index of object or -1 if not found
     * @see java.util.List#indexOf(Object)
     */
    public int indexOf(Object o) {
        int index = 0;
        Iterator it = delegate.iterator();
        if (o == null) {
            while (it.hasNext()) {
                if (o == it.next()) {
                    return index;
                }
                index++;
            }            
        } else {
            while (it.hasNext()) {
                if (o.equals(it.next())) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    public int size() {
        return getSize();
    }


    public boolean isEmpty() {
        return delegate.isEmpty();
    }


    public boolean contains(Object elem) {
        return delegate.contains(elem);
    }


    public boolean add(Object obj) {
        boolean status = delegate.add(obj);
        int index = indexOf(obj);
        fireIntervalAdded(this, index, index);
        return status;
    }
    
    
    public boolean addAll(Collection c) {
        boolean status = delegate.addAll(c);
        fireContentsChanged(this, 0, delegate.size() - 1);
        return status;
    }

    
    public boolean remove(Object obj) {
        int index = indexOf(obj);
        boolean rv = delegate.remove(obj);
        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
        }
        return rv;
    }
    
    public boolean removeAll(Collection c) {
        boolean status = false;
        for (Object o : c) {
            status = status | remove(o);
        }
        return status;
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    public Object[] toArray() {
        return delegate.toArray();
    }
    

    public Object[] toArray(Object[] a) {
        return delegate.toArray(a);
    }


    public void clear() {
        int index1 = delegate.size() - 1;
        delegate.clear();
        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1);
        }
    }

    public boolean containsAll(Collection c) {
        return delegate.containsAll(c);
    }

    public Iterator iterator() {
        return delegate.iterator();
    }

    public boolean retainAll(Collection c) {
        int size = delegate.size();
        boolean status =  delegate.retainAll(c);
        // TODO: is this the right range here?
        fireContentsChanged(this, 0, size - 1);
        return status;
    }

}

