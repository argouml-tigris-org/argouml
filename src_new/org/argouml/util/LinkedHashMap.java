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

import java.util.Collection;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Iterator;

/**
 * This implements all the same methods of the JDK1.4 LinkedHashMap class and is
 * provided to give the same functionality for previous versions of JDK.
 * @see http://java.sun.com/j2se/1.4.2/docs/api/java/util/LinkedHashMap.html
 *
 * @author Bob Tarling
 */
public class LinkedHashMap extends java.util.HashMap {

    private boolean accessOrder;

    private transient KeySet keySet = new KeySet();
    private transient ValuesList values = new ValuesList();
    private transient EntrySet entrySet = new EntrySet();

    /** Creates a new instance of LinkedHashMap */
    public LinkedHashMap() {
        super();
    }
    
    public LinkedHashMap(int initialCapacity) {
        super(initialCapacity);
    }
    
    public LinkedHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }
    
    public LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor);
    }
    
    public Object put(Object key, Object value) {
        keySet.add(key);
        values.add(value);
        entrySet.add(new LinkedHashMap.Entry(key, value));
        return super.put(key, value);
    }
    
    // The stuff below happens in the ancestor
    //public void putAll(Map map) {
        //Set entrySet = map.entrySet();
        //Iterator it = entrySet.iterator();
        //while (it.hasNext()) {
        //    Map.Entry entry = (Map.Entry)it.next();
        //    this.put(entry.getKey(), entry.getValue());
        //}
    //}
    
    public Object remove(Object key) {
        if (containsKey(key)) {
            int index = keySet.indexOf(key);
            keySet.remove(index);
            values.remove(index);
            entrySet.remove(index);
            return super.remove(key);
        }
        else {
            return null;
        }
    }

    //Object remove(int index) {
        //if (containsKey(key)) {
        //    int index = keySet.indexOf(key);
        //    keySet.remove(index);
        //    values.remove(index);
        //    return super.remove(key);
        //}
        //else {
      //      return null;
        //}
    //}
    

    public void clear() {
        keySet.clear();
        values.clear();
        super.clear();
    }
    
    public Object clone() {
        LinkedHashMap linkedHashMap = (LinkedHashMap)super.clone();
        linkedHashMap.entrySet = (EntrySet)entrySet.clone();
        linkedHashMap.keySet = (KeySet)keySet.clone();
        linkedHashMap.values = (ValuesList)values.clone();
        return linkedHashMap;
    }
    
    public Set keySet() {
        return keySet;
    }
    
    public Collection values() {
        return values;
    }
    
    public Set entrySet() {
        return entrySet;
    }
    
    private static class Entry implements Map.Entry {
	Object key;
	Object value;

	Entry(Object key, Object value) {
	    this.key = key;
	    this.value = value;
	}

	protected Object clone() {
	    return new Entry(key, value);
	}

	public Object getKey() {
	    return key;
	}

	public Object getValue() {
	    return value;
	}

	public Object setValue(Object value) {
	    Object oldValue = this.value;
	    this.value = value;
	    return oldValue;
	}

	public boolean equals(Object o) {
	    if (!(o instanceof Map.Entry)) return false;
	    Map.Entry other = (Map.Entry)o;

	    return (key==null ? other.getKey()==null : key.equals(other.getKey())) &&
	       (value==null ? other.getValue()==null : value.equals(other.getValue()));
	}

	public int hashCode() {
	    return (key==null ? 0 : key.hashCode()) ^ (value==null ? 0 : value.hashCode());
	}

	public String toString() {
	    return key+"="+value;
	}
    }
    
    private class LhmSet extends LinkedHashSet {
        
        int modCount = 0;
        
        public LhmSet() {
            super();
            //list = new LinkedList();
        }

        LhmSet(Collection c) {
            super(c);
            //list = new LinkedList(c);
        }

        LhmSet(int initialCapacity) {
            super(initialCapacity);
            //list = new LinkedList();
        }

        LhmSet(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
            //list = new LinkedList();
        }
        
        //public boolean add(Object o) {
        //    throw new java.lang.UnsupportedOperationException();
        //}
        
        //public boolean addAll(Collection c) {
        //    throw new java.lang.UnsupportedOperationException();
        //}
        
        //public boolean remove(Object o) {
        //    boolean changed = super.remove(o);
        //    if (changed) ++modCount;
        //    return changed;
        //}
        
        private class SetIterator implements Iterator {
            private Iterator listIterator;
            private Object lastObject = null;
            private boolean valid = false;

            private int expectedModCount = modCount;
            
            SetIterator(Iterator listIterator) {
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
                if (modCount != expectedModCount) throw new java.util.ConcurrentModificationException();
                if (!valid) throw new IllegalStateException();
                LhmSet.this.remove(lastObject);
                //LhmSet.super.remove(lastObject);
                //listIterator.remove();
                //++modCount;
                ++expectedModCount;
                valid = false;
            }
        }
    }
    
    private class KeySet extends LhmSet {
        public KeySet() {
            super();
        }
        
        public boolean remove(Object o) {
            int index = indexOf(o);
            boolean changed = super.remove(o);
            if (changed) {
                ++modCount;
                entrySet.remove(index);
                values.remove(index);
            }
            return changed;
        }
    }
    
    private class EntrySet extends LhmSet {
        public EntrySet() {
            super();
        }
        
        public boolean remove(Object o) {
            int index = indexOf(o);
            boolean changed = super.remove(o);
            if (changed) {
                ++modCount;
                keySet.remove(index);
                values.remove(index);
            }
            return changed;
        }
    }
    
    private class ValuesList extends java.util.LinkedList {
        
        private int modCount = 0;
        
        public ValuesList() {
        }
        
        public ValuesList(Collection c) {
        }
        
        //TODO turn off after built
        //public boolean add(Object o) {
        //    throw new java.lang.UnsupportedOperationException();
        //}
        
        //TODO turn off after built
        //public boolean addAll(Collection c) {
        //    throw new java.lang.UnsupportedOperationException();
        //}
        
        public boolean remove(Object o) {
            int index = indexOf(o);
            boolean changed = super.remove(o);
            if (changed) {
                ++modCount;
                keySet.remove(index);
                entrySet.remove(index);
            }
            return changed;
        }
        
        public Iterator iterator() {
            return new ValuesIterator(super.iterator());
        }

        private class ValuesIterator implements Iterator {
            private Iterator listIterator;
            private Object lastObject = null;
            private boolean valid = false;
            private int index = -1;
            
            private int expectedModCount = modCount;
        
            ValuesIterator(Iterator listIterator) {
                this.listIterator = listIterator;
            }

            public boolean hasNext() {
                return listIterator.hasNext();
            }

            public Object next() {
                ++index;
                lastObject = listIterator.next();
                valid = true;
                return lastObject;
            }

            public void remove() {
                if (modCount != expectedModCount) throw new java.util.ConcurrentModificationException();
                if (!valid) throw new IllegalStateException();
                if (!ValuesList.super.remove(lastObject)) throw new java.util.ConcurrentModificationException();
                keySet.remove(index);
                entrySet.remove(index);
                ++modCount;
                ++expectedModCount;
                --index;
                valid = false;
            }
        }
    }
}
