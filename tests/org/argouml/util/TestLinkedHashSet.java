// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

/*
 * LinkedHashSetTest.java
 * JUnit based test
 *
 * Created on 03 August 2002, 22:12
 */

package org.argouml.util;

import junit.framework.*;
import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Arrays;
/**
 *
 * @author Bob Tarling
 */
public class TestLinkedHashSet extends TestCase {
    
    public TestLinkedHashSet(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    LinkedHashSet linkedHashSet;
    Object entity[] = {
	"entity0",
	"dsh1",
	"hfdgh2",
	"vrvds3",
	"yukjg4",
	"htdsg5",
	"ergtds6",
	null,
	"zdvfdg8",
	"xdhgfd9"
    };
 
    public void setUp() {
        linkedHashSet = new LinkedHashSet();
        linkedHashSet.add(entity[0]);
        linkedHashSet.add(entity[1]);
        linkedHashSet.add(entity[2]);
        linkedHashSet.add(entity[3]);
        linkedHashSet.add(entity[4]);
        linkedHashSet.add(entity[5]);
        linkedHashSet.add(entity[6]);
        linkedHashSet.add(entity[7]);
        linkedHashSet.add(entity[8]);
        linkedHashSet.add(entity[9]);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(TestLinkedHashSet.class);
        
        return suite;
    }

    public void testIsEmpty() {
        assertTrue(!linkedHashSet.isEmpty());
        assertTrue(new LinkedHashSet().isEmpty());
    }
    
    /** Test of iterator method, of class uk.co.jbob.collections.LinkedHashSet. */
    public void testIterator() {
        System.out.println("testIterator");
     
        assertIteratorAgainstArray(linkedHashSet.iterator(), entity);
    }
    
    public void testIterator_Remove() {
        Object[] compare = {
	    entity[0],
	    entity[1],
	    entity[2],
	    entity[3],
	    entity[4],
	    entity[6],
	    entity[7],
	    entity[8],
	    entity[9]
	};
        
        int count = 0;
        Iterator it = linkedHashSet.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            assertEquals(entity[count], o);
            if (count == 5) it.remove();
            count++;
        }
        assertIteratorAgainstArray(linkedHashSet.iterator(), compare);
    }
    
    public void testIterator_Remove2() {
        boolean exceptionCaught = false;
        
        Iterator it = linkedHashSet.iterator();

        linkedHashSet.remove(entity[5]);
        assertTrue(it.hasNext());
        try {
            it.next();
        }
        catch (java.util.ConcurrentModificationException ex) {
            exceptionCaught = true;
        }
        
        assertTrue(exceptionCaught);
    }
    
    public void testIterator_Remove3() {
        boolean exceptionCaught = false;
        Object[] compare = {
	    entity[0],
	    entity[1],
	    entity[2],
	    entity[3],
	    entity[4],
	    entity[6],
	    entity[7],
	    entity[8],
	    entity[9]
	};
        
        int count = 0;
        Iterator it = linkedHashSet.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            assertEquals(entity[count], o);
            if (count == 5) {
                it.remove();
                try {
                    it.remove();
                }
                catch (java.lang.IllegalStateException ex) {
                    exceptionCaught = true;
                }
            }
            count++;
        }
        assertTrue(exceptionCaught);
        assertIteratorAgainstArray(linkedHashSet.iterator(), compare);
    }
    
    /** Test of add method, of class uk.co.jbob.collections.LinkedHashSet. */
    public void testAdd() {
        System.out.println("testAdd");

        assertTrue(!linkedHashSet.add(entity[5]));
        assertIteratorAgainstArray(linkedHashSet.iterator(), entity);
    }
    
    /** Test of remove method, of class uk.co.jbob.collections.LinkedHashSet. */
    public void testRemove() {
        System.out.println("testRemove");

        // Assert that an attempt to remove an item that is not actually
        // in the LinkedHashSet results in no change to the collection.
        assertTrue(!linkedHashSet.remove("thisdoesnotexist"));
        assertIteratorAgainstArray(linkedHashSet.iterator(), entity);
        
        // Assert that actually removing an items from the collection
        // doesn't change the iteration order of the remaining items
        assertTrue(linkedHashSet.remove(entity[0]));
        assertTrue(linkedHashSet.remove(entity[2]));
        assertTrue(linkedHashSet.remove(entity[4]));
        assertTrue(linkedHashSet.remove(entity[6]));
        assertTrue(linkedHashSet.remove(entity[8]));
        
        Object[] remaining = {entity[1], entity[3], entity[5], entity[7], entity[9]};
        assertIteratorAgainstArray(linkedHashSet.iterator(), remaining);
    }
    
    /** Test of removeAll method, of class uk.co.jbob.collections.LinkedHashSet. */
    public void testRemoveAll() {
        System.out.println("testRemoveAll");
        
        // Assert that an attempt to remove an item that is not actually
        // in the LinkedHashSet results in no change to the collection.
        LinkedList noMatch = new LinkedList();
        noMatch.add("sdfs");
        noMatch.add("dggf");
        noMatch.add("rhty");
        noMatch.add("azxf");
        noMatch.add("olfv");
        assertTrue(!linkedHashSet.removeAll(noMatch));
        assertIteratorAgainstArray(linkedHashSet.iterator(), entity);
        
        // Assert that an attempt to remove items from another collection
        // where not all match does change the iterator and leaves the 
        // expected items behind.
        LinkedList someMatch = new LinkedList();
        someMatch.add("sdfs");
        someMatch.add(entity[0]);
        someMatch.add("rhty");
        someMatch.add(entity[5]);
        someMatch.add("azxf");
        someMatch.add(entity[9]);
        assertTrue(linkedHashSet.removeAll(someMatch));
        
        Object whatsleft[] = {
	    entity[1],
	    entity[2],
	    entity[3],
	    entity[4],
	    entity[6],
	    entity[7],
	    entity[8]
	};

        assertIteratorAgainstArray(linkedHashSet.iterator(), whatsleft);
    }
    
    /** Test of retainAll method, of class uk.co.jbob.collections.LinkedHashSet. */
    public void testRetainAll() {
        System.out.println("testRetainAll");
        
        LinkedList someMatch = new LinkedList();
        someMatch.add("sdfs");
        someMatch.add(entity[0]);
        someMatch.add("rhty");
        someMatch.add(entity[5]);
        someMatch.add("azxf");
        someMatch.add(entity[9]);
        assertTrue(linkedHashSet.retainAll(someMatch));
        
        Object whatsleft[] = {
	    entity[0],
	    entity[5],
	    entity[9]
	};

        assertIteratorAgainstArray(linkedHashSet.iterator(), whatsleft);
    }
    
    /** Test of toArray method, of class uk.co.jbob.collections.LinkedHashSet. */
    public void testToArray() {
        System.out.println("testToArray");

        Object[] a = linkedHashSet.toArray();
        assertTrue(Arrays.equals(entity, a));
        assertIteratorAgainstArray(linkedHashSet.iterator(), a);
        
        Object[] x = {"lkj", "kjh"};
        Object[] sa = linkedHashSet.toArray(x);
        assertTrue(Arrays.equals(entity, sa));
        assertIteratorAgainstArray(linkedHashSet.iterator(), sa);
    }
    
    /** Test of clear method, of class uk.co.jbob.collections.LinkedHashSet. */
    public void testClear() {
        System.out.println("testClear");

        linkedHashSet.clear();
        assertEquals(0, linkedHashSet.size());
        assertTrue(linkedHashSet.isEmpty());
    }
    
    /** Test of equals method, of class uk.co.jbob.collections.LinkedHashSet. */
    public void testEquals() {
        System.out.println("testEquals");
    
        LinkedHashSet compareHashSet = new LinkedHashSet();
        compareHashSet.add(entity[0]);
        compareHashSet.add(entity[1]);
        compareHashSet.add(entity[2]);
        compareHashSet.add(entity[3]);
        compareHashSet.add(entity[4]);
        compareHashSet.add(entity[5]);
        compareHashSet.add(entity[6]);
        compareHashSet.add(entity[7]);
        compareHashSet.add(entity[8]);
        compareHashSet.add(entity[9]);
        
        assertEquals(linkedHashSet, compareHashSet);
    }

    private void assertIteratorAgainstArray(Iterator it, Object[] array) {
        int count = 0;
        while (it.hasNext()) {
            Object o = it.next();
            assertEquals(array[count], o);
            count++;
        }
        assertEquals(array.length , count);
    }
}
