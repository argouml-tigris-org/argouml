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
    
    public void roleAdded(final MElementEvent event) {
        String eventName = event.getName();
        String property = getProperty();
        if(eventName == null || property == null || eventName.equals(property)) {
            Object addedValue = event.getAddedValue();
            if(isProperClass(addedValue)) {
                boolean found = false;
                //
                //   see if attribute is already in our list
                //
                java.util.List cache = getCache();
                if(cache != null) {
                    Iterator iter = cache.iterator();
                    for(int i = 0;iter.hasNext();i++) {
                        if(iter.next() == addedValue) {
                            found = true;
                            break;
                        }
                    }
                }
                //
                //  if the attribute wasn't found then
                //     reset the cache and our attributes list
                if(!found) {
                    resetCache();
                    resetSize();
                    fireContentsChanged(this,0,getSize());
                }
            }
        }
    }
    

    public void roleRemoved(final MElementEvent event) {
        String eventName = event.getName();
        String property = getProperty();
        if(eventName == null || property == null || eventName.equals(property)) {
            Object removedValue = event.getRemovedValue();
            if(isProperClass(removedValue)) {
                //
                //   see if attribute is already in our list
                //
                java.util.List cache = getCache();
                if(cache != null) {
                    Iterator iter = cache.iterator();
                    for(int i = 0;iter.hasNext();i++) {
                        if(iter.next() == removedValue) {
                            resetSize();
                            resetCache();
                            fireIntervalRemoved(this,i,i);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public void recovered(final MElementEvent p1) {
    }
    public void listRoleItemSet(final MElementEvent p1) {
    }
    public void removed(final MElementEvent p1) {
    }
    public void propertySet(final MElementEvent p1) {
    }
    
    protected java.util.List swap(Collection source,int lowIndex,Object before,Object after) {
        ArrayList dest = new ArrayList(source.size());
        Object obj = null;
        Iterator iter = source.iterator();
        for(int i = 0; iter.hasNext(); i++) {
            obj = iter.next();
            if(obj == before) {
                dest.add(i++,after);
                dest.add(i,before);
            }
            else {
                if(obj != after) {
                    dest.add(i,obj);
                }
            }
        }
        
        java.util.List cache = getCache();
        if(cache != null) {
            cache.set(lowIndex,after);
            cache.set(lowIndex+1,before);
        }
        return dest;
    }
    
    protected java.util.List addElement(Collection source,int index,MModelElement newElement) {
        int size = getModelElementSize();
        AbstractList dest = null;
        boolean succeeded = false;
        java.util.List cache = getCache();
        if(index < size-1) {
            //
            //  find the selected operation
            //
            Object atElement = cache.get(index);
            if(source != null) {
                dest = new ArrayList(source.size()+1);
                Iterator iter = source.iterator();
                Object obj = null;
                int i;
                for(i = 0; iter.hasNext() && obj != atElement; i++) {
                    obj = iter.next();
                    dest.set(i,obj);
                }
                dest.set(i++,newElement);
                for(; iter.hasNext(); i++) {
                    dest.set(i,iter.next());
                }

                //
                //  we can add this in our cache and not be forced to rebuild it
                //
                cache.add(index+1,newElement);
            }
        }
        else {
            cache.add(newElement);
        }
        resetSize();
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




