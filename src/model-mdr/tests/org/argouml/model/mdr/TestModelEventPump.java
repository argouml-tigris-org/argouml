// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.util.Collection;

import junit.framework.TestCase;

/**
 * Tests for the MDR implementation of the event pump
 * @author Tom Morris
 *
 */
public class TestModelEventPump extends TestCase {

    private Registry<String> registry ;
    
    /*
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() {
        registry = new Registry<String>();
    }
    
    /**
     * Test the registry mechanism used by the event pump
     * to register and look up listeners
     */
    public void testRegistry() {
        Collection<String> matches;

        registry.register("value1", "key1", null);
        registry.register("main2", "key2", null);
        registry.register("sub2", "key2",
                new String[] {"subkey2"});

        matches = registry.getMatches("key1", "foo");
        checkSingle("value1", matches);
        
        assertTrue(registry.getMatches("key3", "foo")
                .isEmpty()); 
        
        matches = registry.getMatches("key2", "foo");
        checkSingle("main2", matches);

        matches = registry.getMatches("key2", "subkey2");
        assertTrue(matches.contains("sub2"));
        assertTrue(matches.contains("main2"));
        assertEquals(2, matches.size());

        registry.unregister("main2", "key2", null);
        matches = registry.getMatches("key2", "subkey2");
        checkSingle("sub2", matches);
        
        // Test multipe items registered for single key set
        registry.register("multi1", "key4", null);
        registry.register("multi2", "key4", null);
        matches = registry.getMatches("key4", "foo");
        assertTrue(matches.contains("multi1"));
        assertTrue(matches.contains("multi2"));
        assertEquals(2, matches.size());

        registry.unregister("multi1", "key4", null);
        matches = registry.getMatches("key4", "foo");
        checkSingle("multi2", matches);
        
        registry.unregister("multi3", "key4", null);
        matches = registry.getMatches("key4", "foo");
        checkSingle("multi2", matches);        

        registry.unregister("multi2", "key4", null);
        assertTrue(registry.getMatches("key4", "foo")
                .isEmpty());

        // Remove with null item should remove all entries for key
        registry.register("multi3", "key4", null);
        registry.register("multi4", "key4", null);
        registry.unregister(null, "key4", null);
        assertTrue(registry.getMatches("key4", "foo")
                .isEmpty());


        registry.register("multi1", "key4",
                new String[] {"subkey"});
        registry.register("multi2", "key4",
                new String[] {"subkey"});
        matches = registry.getMatches("key4", "subkey");
        assertTrue(matches.contains("multi1"));
        assertTrue(matches.contains("multi2"));
        assertEquals(2, matches.size());
        
        assertTrue(registry.getMatches("key4", "foo")
                .isEmpty());
        
        registry.unregister("multi3", "key4",
                new String[] {"subkey"});
        matches = registry.getMatches("key4", "subkey");
        matches = registry.getMatches("key4", "subkey");
        assertTrue(matches.contains("multi1"));
        assertTrue(matches.contains("multi2"));
        assertEquals(2, matches.size());
        
        registry.unregister("multi2", "key4",
                new String[] {"subkey"});
        matches = registry.getMatches("key4", "subkey");
        checkSingle("multi1", matches);        

        registry.register("multi3", "key4",
                new String[] {"subkey"});
        registry.register("multi4", "key4", null);

        registry.unregister(null, "key4", null);
        assertTrue(registry.getMatches("key4", "subkey")
                .isEmpty());

    }
    
    private void checkSingle(String value, Collection<String> matches) {
        assertEquals(1, matches.size());
        assertEquals(value, matches.iterator().next()); 
    }
}
