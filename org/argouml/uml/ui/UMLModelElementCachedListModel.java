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

package org.argouml.uml.ui;

import ru.novosoft.uml.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import java.util.*;
import java.awt.*;
import java.lang.reflect.*;

abstract public class UMLModelElementCachedListModel extends UMLModelElementListModel {

    public UMLModelElementCachedListModel(UMLUserInterfaceContainer container,String property,boolean showNone) {
        super(container,property,showNone);
    }

    abstract protected void resetCache();
    abstract protected java.util.List getCache();
    abstract public boolean isProperClass(Object obj);
    abstract Collection getRawCollection();

    java.util.Collection createCollection(int initialSize) {
        return new ArrayList(initialSize);
    }


    protected int recalcModelElementSize() {
        int size = 0;
        java.util.List cache = getCache();
        if(cache != null) {
            size = cache.size();
        }
        return size;
    }

    protected MModelElement getModelElementAt(int index) {
        MModelElement element = null;
        java.util.List cache = getCache();
        if(cache != null) {
            element = (MModelElement) cache.get(index);
        }
        return element;
    }

    public void targetChanged() {
        resetCache();
        super.targetChanged();
    }

    public void roleRemoved(final MElementEvent event) {
	Object eventProperty = event.getName();
        Object listProperty = getProperty();
        if(listProperty == null || eventProperty == null ||
            listProperty.equals(eventProperty)) {
            Object source = event.getSource();
            //
            //   if the thing removed was in our list
            //
            int index = getCache().indexOf(source);
            if(index >= 0) {
                resetSize();
                resetCache();
                fireIntervalRemoved(this,index,index);
            }
        }
    }

    public void roleAdded(final MElementEvent event) {
	Object eventProperty = event.getName();
        Object listProperty = getProperty();

        if(listProperty == null || eventProperty == null ||
            listProperty.equals(eventProperty)) {
            Object added = event.getAddedValue();

            if(isProperClass(added)) {
                int upper = getUpperBound();
                resetSize();
                resetCache();
                if(upper < 0) upper = 0;
                fireIntervalAdded(this,upper,upper);
            }
        }
    }


    public void recovered(final MElementEvent p1) {
    }

    public void listRoleItemSet(final MElementEvent p1) {
    }

    public void removed(final MElementEvent event) {
    }


    //
    //   this needs to be overriden if the derived class
    //      wants to resort based on the name change
    public void propertySet(final MElementEvent event) {
        //needs-more-work: update of listmodel is not correct!!
        //example: propertySet-event of classifier.setFeature(features)

        Object source = event.getSource();
        int index = getCache().indexOf(source);
        //
        //   we should only see promiscuous name changes
        //      so checking that the property is named 'name'
        //      is unnecessary

        if(index >= 0)
            fireContentsChanged(this,index,index);
    }

/**
 * Swap two items in a Collection. The Collection contains the attributes list
 * and operations list together, however these items need to be swapped
 * independantly of each other so we must iterate through the list to find
 * a "value match". The parameter "lowIndex" is no longer needed, however I
 * left it in for compatability. The same operation is performed twice,
 * once for the source Collection, and again for the cache list.
 */
    protected java.util.List swap(Collection source,int lowIndex,Object first,Object second) {
        java.util.List dest = new ArrayList(source);

        for(ListIterator i = dest.listIterator(); i.hasNext();) {
            if(first == i.next()) {
                dest.set(i.previousIndex(), second);
                dest.set(i.nextIndex(), first);
                break;
            }
        }

        java.util.List cache = getCache();

        if(cache != null) {
            for(ListIterator i = cache.listIterator(); i.hasNext();){
                if(first == i.next()){
                    cache.set(i.previousIndex(), second);
                    cache.set(i.nextIndex(),first);
                    break;
                }
            }
        }
        return dest;
    }


/**
 *  addElement method uses ListIterator and because we pass in the Object
 *  element (name of the element after which we want to insert
 *  the newElement) we can iterate down the list until we find the correct
 *  insertion point. This iteration is necessary because elements of different
 *  types are kept in the same collection. If element is null, the newElement
 *  is put at the beginning of the list.
 *
 *	Modified: July 18, 2001 - psager
 *	Modified: Dec  06, 2001 - thn
 *
 *  @param  source  underlying collection of attributes and operations.
 *  @param  index   location of the element within the list box.
 *  @param  newElement  element to be added.
 *  @param  element element at position before the add point (or null to add as first).
 *  @return dest    new collection as a ArrayList().
*/
        protected java.util.List addElement(Collection source, int index,
                                            MModelElement newElement,
                                            Object element) {
            java.util.List cache = getCache();
            java.util.List dest  = new ArrayList(source);

            if (element == null) {
              dest.add(0, newElement);
              cache.add(0,newElement);
            } else {
			  for(ListIterator i = dest.listIterator(); i.hasNext();){
                if(i.next() == element){
                  dest.add(i.nextIndex(), newElement);
                  break;
                }
              }
              cache.add(index+1,newElement);
            }
            return dest;
        }


    protected java.util.List buildCache() {
        java.util.List cache = null;
        Collection collection = null;
        Collection items = getRawCollection();
        if(items != null) {
            collection = createCollection(items.size());
            Iterator iter = items.iterator();
            Object item;
            int i = 0;
            while(iter.hasNext()) {
                item = iter.next();
                if(isProperClass(item)) {
                    collection.add(item);
                }
            }
        }
        //
        //   should be able to find something a little cheaper
        //      for an empty list
        if(collection == null) {
            cache = new ArrayList();
        }
        else {
            //
            //   if the collection was a List to begin with (non-alphabetized)
            //      then just return it
            if(collection instanceof java.util.List) {
                cache = (java.util.List) collection;
            }
            //
            //   otherwise, copy it to an ArrayList for fast access by index
            //
            else {
                cache = new ArrayList(collection);
            }
        }
        return cache;
    }
}









