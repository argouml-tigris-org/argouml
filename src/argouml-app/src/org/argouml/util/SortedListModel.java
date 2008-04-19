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

package org.argouml.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.AbstractListModel;


/**
 * A ListModel which keeps the list in sorted order.
 * <p>
 * This is a low performance implementation designed for use with small lists
 * (such as typically appear in the GUI). It will resort the entire list after
 * each addition.
 */
public class SortedListModel extends AbstractListModel implements List
{
    private List delegate = new ArrayList();

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
        return delegate.get(index);
    }

    public Object get(int index) {
        return delegate.get(index);
    }


    public int size() {
        return delegate.size();
    }


    public boolean isEmpty() {
        return delegate.isEmpty();
    }



    public boolean contains(Object elem) {
        return delegate.contains(elem);
    }


    public int indexOf(Object elem) {
        return delegate.indexOf(elem);
    }

    public int lastIndexOf(Object elem) {
        return delegate.lastIndexOf(elem);
    }


    public boolean add(Object obj) {
        boolean status = delegate.add(obj);
        Collections.sort(delegate);
        fireContentsChanged(this, 0, delegate.size() - 1);
        return status;
    }
    

    /**
     * Add an element to the ListModel. Note: Although this accepts an index
     * parameter for compatibility with the List API, the ordering of the list
     * is determined by its contents, so the index is effectively ignored.
     * 
     * @param index ignored
     * @param element element to be added
     * @see java.util.List#add(int, java.lang.Object)
     */
    public void add(int index, Object element) {
        delegate.add(index, element);
        Collections.sort(delegate);
        fireContentsChanged(this, 0, delegate.size() - 1);
    }


    /**
     * Remove the element at the given index and add the new element in
     * sorted order.
     * 
     * @param index index of element to be removed
     * @param element new element to be added
     * @return the element that was removed
     * @see java.util.List#set(int, java.lang.Object)
     */
    public Object set(int index, Object element) {
        Object oldValue = delegate.set(index, element);
        Collections.sort(delegate);
        fireContentsChanged(this, 0, delegate.size() - 1);
        return oldValue;
    }
    
    public boolean addAll(Collection c) {
        boolean status = delegate.addAll(c);
        Collections.sort(delegate);
        fireContentsChanged(this, 0, delegate.size() - 1);
        return status;
    }

    /**
     * Add the collection of elements to the ListModel. Note: Although this
     * accepts an index parameter for compatibility with the List API, the
     * ordering of the list is determined by its contents, so the index is
     * effectively ignored.
     * 
     * @param index ignored
     * @param c collection of elements to be added
     * @see java.util.List#add(int, java.lang.Object)
     */
    public boolean addAll(int index, Collection c) {
        boolean status = delegate.addAll(index, c);
        Collections.sort(delegate);
        fireContentsChanged(this, 0, delegate.size() - 1);
        return status;
    }

    public Object remove(int index) {
        Object oldValue = delegate.remove(index);
        fireIntervalRemoved(this, index, index);
        return oldValue;
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

    public ListIterator listIterator() {
        return delegate.listIterator();
    }

    public ListIterator listIterator(int index) {
        return delegate.listIterator(index);
    }

    /**
     * Unimplemented optional operation.
     * 
     * @param c
     * @return
     * @see java.util.List#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    public List subList(int fromIndex, int toIndex) {
        return delegate.subList(fromIndex, toIndex);
    }


}

