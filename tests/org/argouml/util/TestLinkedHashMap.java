/*
 * LinkedHashMapTest.java
 * JUnit based test
 *
 * Created on 04 August 2002, 17:49
 */

package org.argouml.util;

import junit.framework.*;
import java.util.Collection;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Iterator;

/**
 *
 * @author administrator
 */
public class TestLinkedHashMap extends TestCase {
    
    public TestLinkedHashMap(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    Object key0 = "key0";
    Object key1 = "keyskdhj11";
    Object key2 = null;
    Object key3 = "keyloiduh3";
    Object key4 = "keyakbdfe4";
    Object key5 = "keykdfhsd5";
    Object key6 = "keyoiudsf6";
    Object key7 = "keykjdfsf7";
    Object key8 = "keyoiufhs8";
    Object key9 = "key9";

    Object value0 = "value0";
    Object value1 = "valueakbjf1";
    Object value2 = null;
    Object value3 = "valueplsdf3";
    Object value4 = "valuejdsfg4";
    Object value5 = "valueperhd5";
    Object value6 = "valuemnbfd6";
    Object value7and8 = "value7and8";
    Object value9 = "value9";

    Object[] keys = {key0, key1, key2, key3, key4, key5, key6, key7, key8, key9};

    Object[] values = {value0, value1, value2, value3, value4, value5, value6,
                       value7and8,  // One of the values is repeated
                       value7and8,   //  but with different keys
                       value9
    };

    LinkedHashMap linkedHashMap;

    public void setUp() {
        linkedHashMap = new LinkedHashMap();
        for (int i=0; i<10; ++i) {
            linkedHashMap.put(keys[i], values[i]);
        }
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(TestLinkedHashMap.class);
        
        return suite;
    }

    public void testEntrySet() {
        System.out.println("testEntrySet");
        
        // Check that the set returned by keySet() contains all the items in the
        // keys array
        Set entrySet = linkedHashMap.entrySet();
        assertEntrySetAgainstArrays(entrySet, keys, values);
    }
    
    
    public void testEntrySet_Remove() {
        System.out.println("testEntrySet_Remove");
        
        // What happens when we remove an item from the values that appear more than once?
        Collection valuesCollection = linkedHashMap.values();
        Set keySet = linkedHashMap.keySet();
        Set entrySet = linkedHashMap.entrySet();
        
        assertTrue(entrySet.remove((entrySet.toArray())[7]));
        assertEquals(9, valuesCollection.size());
        assertEquals(9, keySet.size());
        assertEquals(9, entrySet.size());
        
        Object[] keys = {key0, key1, key2, key3, key4, key5, key6, key8, key9};

        Object[] values = {value0, value1, value2, value3, value4, value5, value6, value7and8, value9};

        assertIteratorAgainstArray(keySet.iterator(), keys);
        assertIteratorAgainstArray(valuesCollection.iterator(), values);
        assertEntrySetAgainstArrays(entrySet, keys, values);
    }


    /** Test of keySet method, of class uk.co.jbob.collections.j2sdk1_4.LinkedHashMap. */
    public void testKeySet() {
        System.out.println("testKeySet");
        
        // Check that the set returned by keySet() contains all the items in the
        // keys array
        Set keysSet = linkedHashMap.keySet();
        assertIteratorAgainstArray(keysSet.iterator(), keys);
    }
    
    public void testKeySet_Remove() {
        System.out.println("testKeySet_Remove");
        
        // What happens when we remove an item from the values that appear more than once?
        Collection valuesCollection = linkedHashMap.values();
        Set keySet = linkedHashMap.keySet();
        Set entrySet = linkedHashMap.entrySet();
        assertTrue(keySet.remove(key7));
        assertEquals(9, valuesCollection.size());
        assertEquals(9, keySet.size());
        assertEquals(9, entrySet.size());
        
        Object[] keys = {key0, key1, key2, key3, key4, key5, key6, key8, key9};

        Object[] values = {value0, value1, value2, value3, value4, value5, value6, value7and8, value9};

        assertIteratorAgainstArray(keySet.iterator(), keys);
        assertIteratorAgainstArray(valuesCollection.iterator(), values);
        assertEntrySetAgainstArrays(entrySet, keys, values);
    }

    public void testKeySet_Iterator_Remove() {
        System.out.println("testValues_Iterator_Remove");
        
        // What happens when we remove an item from the values that appear more than once?
        Set keySet = linkedHashMap.keySet();
        Set entrySet = linkedHashMap.entrySet();
        Collection valuesCollection = linkedHashMap.values();
        Iterator it = valuesCollection.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (value7and8.equals(o)) {
                it.remove();
                break;
            }
        }
        
        assertEquals(9, valuesCollection.size());
        assertEquals(9, keySet.size());
        assertEquals(9, entrySet.size());
        
        Object[] keys = {key0, key1, key2, key3, key4, key5, key6, key8, key9};

        Object[] values = {value0, value1, value2, value3, value4, value5, value6, value7and8, value9};

        assertIteratorAgainstArray(keySet.iterator(), keys);
        assertIteratorAgainstArray(valuesCollection.iterator(), values);
        assertEntrySetAgainstArrays(entrySet, keys, values);
    }

    /** Test of values method, of class uk.co.jbob.collections.j2sdk1_4.LinkedHashMap. */
    public void testValues() {
        System.out.println("testValues");
        
        // Check that the collection returned by values() contains all the items in the
        // values array
        Collection valuesCollection = linkedHashMap.values();
        assertIteratorAgainstArray(valuesCollection.iterator(), values);
    }
    
    /** Test of values method, of class uk.co.jbob.collections.j2sdk1_4.LinkedHashMap. */
    
    public void testValues_Remove() {
        System.out.println("testValues_Remove");
        
        // What happens when we remove an item from the values that appear more than once?
        Collection valuesCollection = linkedHashMap.values();
        Set keySet = linkedHashMap.keySet();
        Set entrySet = linkedHashMap.entrySet();
        assertTrue(valuesCollection.remove(value7and8));
        assertEquals(9, valuesCollection.size());
        assertEquals(9, keySet.size());
        assertEquals(9, entrySet.size());
        
        Object[] keys = {key0, key1, key2, key3, key4, key5, key6, key8, key9};

        Object[] values = {value0, value1, value2, value3, value4, value5, value6, value7and8, value9};

        assertIteratorAgainstArray(keySet.iterator(), keys);
        assertIteratorAgainstArray(valuesCollection.iterator(), values);
        assertEntrySetAgainstArrays(entrySet, keys, values);
    }

    
    /** Test of values method, of class uk.co.jbob.collections.j2sdk1_4.LinkedHashMap. */
    
     public void testValues_Iterator_Remove() {
        System.out.println("testValues_Iterator_Remove");
        
        // What happens when we remove an item from the values that appear more than once?
        Set keySet = linkedHashMap.keySet();
        Set entrySet = linkedHashMap.entrySet();
        Collection valuesCollection = linkedHashMap.values();
        Iterator it = valuesCollection.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (value7and8.equals(o)) {
                it.remove();
                break;
            }
        }
        
        assertEquals(9, valuesCollection.size());
        assertEquals(9, keySet.size());
        assertEquals(9, entrySet.size());
        
        Object[] keys = {key0, key1, key2, key3, key4, key5, key6, key8, key9};

        Object[] values = {value0, value1, value2, value3, value4, value5, value6, value7and8, value9};

        assertIteratorAgainstArray(keySet.iterator(), keys);
        assertIteratorAgainstArray(valuesCollection.iterator(), values);
        assertEntrySetAgainstArrays(entrySet, keys, values);
    }

    public void testValues_Iterator_Remove2() {
        System.out.println("testValues_Iterator_Remove2");
        
        // What happens when we remove an item from the values that appear more than once
        // removing the first one found?
        Set keySet = linkedHashMap.keySet();
        Set entrySet = linkedHashMap.entrySet();
        Collection valuesCollection = linkedHashMap.values();
        Iterator it = valuesCollection.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (value7and8.equals(o)) {
                it.remove();
                break;
            }
        }
        
        assertEquals(9, valuesCollection.size());
        assertEquals(9, keySet.size());
        assertEquals(9, entrySet.size());
        
        Object[] keys = {key0, key1, key2, key3, key4, key5, key6, key8, key9};

        Object[] values = {value0, value1, value2, value3, value4, value5, value6, value7and8, value9};

        assertIteratorAgainstArray(keySet.iterator(), keys);
        assertIteratorAgainstArray(valuesCollection.iterator(), values);
        assertEntrySetAgainstArrays(entrySet, keys, values);
    }
    
    public void testValues_Iterator_Remove3() {
        System.out.println("testValues_Iterator_Remove2");
        
        // What happens when we remove an item from the values that appear more than once
        // removing the second instance?
        int count = 0;
        Set keySet = linkedHashMap.keySet();
        Set entrySet = linkedHashMap.entrySet();
        Collection valuesCollection = linkedHashMap.values();
        Iterator it = valuesCollection.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (value7and8.equals(o)) {
                count++;
                if (count == 2) {
                    it.remove();
                    break;
                }
            }
        }
        
        assertEquals(9, valuesCollection.size());
        assertEquals(9, keySet.size());
        assertEquals(9, entrySet.size());
        
        Object[] keys = {key0, key1, key2, key3, key4, key5, key6, key7, key9};

        Object[] values = {value0, value1, value2, value3, value4, value5, value6, value7and8, value9};

        assertIteratorAgainstArray(keySet.iterator(), keys);
        assertIteratorAgainstArray(valuesCollection.iterator(), values);
        assertEntrySetAgainstArrays(entrySet, keys, values);
    }
    
    public void trueValues_Iterator_Remove4() {
        System.out.println("testValues_Iterator_Remove4");
        
        // What happens when we remove an item after an iterator has been created?
        boolean exceptionCaught = false;
        
        Set keySet = linkedHashMap.keySet();
        Collection valuesCollection = linkedHashMap.values();

        Iterator itValues = valuesCollection.iterator();

        keySet.remove(key3);
        
        itValues.hasNext();
        try {
            Object o = itValues.next();
        }
        catch (java.util.ConcurrentModificationException ex) {
            exceptionCaught = true;
        }
        
        assertTrue(exceptionCaught);
    }

    /** Test of put method, of class uk.co.jbob.collections.j2sdk1_4.LinkedHashMap. */
    public void testPut() {
        System.out.println("testPut");
        
        assertNull(linkedHashMap.put("newkey", values[4]));
        Object[] keys = {key0, key1, key2, key3, key4, key5, key6, key7, key8, key9, "newkey"};
        Set keysSet = linkedHashMap.keySet();
        assertIteratorAgainstArray(keysSet.iterator(), keys);
        
        Object[] values = {value0, value1, value2, value3, value4, value5, value6, value7and8, value7and8, value9, value4};
        Collection valuesCollection = linkedHashMap.values();
        assertIteratorAgainstArray(valuesCollection.iterator(), values);
    }
    
    /** Test of putAll method, of class uk.co.jbob.collections.j2sdk1_4.LinkedHashMap. */
    public void testPutAll() {
        System.out.println("testPutAll");
    }
    
    /** Test of remove method, of class uk.co.jbob.collections.j2sdk1_4.LinkedHashMap. */
    public void testRemove() {
        System.out.println("testRemove");
    }
    
    /** Test of clear method, of class uk.co.jbob.collections.j2sdk1_4.LinkedHashMap. */
    public void testClear() {
        System.out.println("testClear");
    }
    
    /** Test of clone method, of class uk.co.jbob.collections.j2sdk1_4.LinkedHashMap. */
    public void testClone() {
        System.out.println("testClone");
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
    
    public void assertEntrySetAgainstArrays(Set entrySet, Object[] keyArray, Object[] valueArray) {
        // Check that the set returned by keySet() contains all the items in the
        // keys array
        int count = 0;
        Iterator it = entrySet.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            assertEquals(keyArray[count], key);
            assertEquals(valueArray[count], value);
            count++;
        }
        assertEquals(keyArray.length , count);
        assertEquals(valueArray.length , count);
    }
}
