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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Tests for the MDR implementation of the event pump
 * @author Tom Morris
 *
 */
public class TestModelEventPump extends TestCase {

    private Map registry;
    
    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
        registry = Collections.synchronizedMap(new HashMap());
    }
    
    /**
     * Test the registry mechanism used by the event pump
     * to register and look up listeners
     */
    public void testRegistry() {
        Collection matches;

        ModelEventPumpMDRImpl.register(registry, "value1", "key1", null);
        ModelEventPumpMDRImpl.register(registry, "main2", "key2", null);
        ModelEventPumpMDRImpl.register(registry, "sub2", "key2",
                new String[] {"subkey2"});

        matches = ModelEventPumpMDRImpl.getMatches(registry, "key1", "foo");
        checkSingle("value1", matches);
        
        assertTrue(ModelEventPumpMDRImpl.getMatches(registry, "key3", "foo")
                .isEmpty()); 
        
        matches = ModelEventPumpMDRImpl.getMatches(registry, "key2", "foo");
        checkSingle("main2", matches);

        matches = ModelEventPumpMDRImpl.getMatches(registry, "key2", "subkey2");
        assertTrue(matches.contains("sub2"));
        assertTrue(matches.contains("main2"));
        assertEquals(2, matches.size());

        ModelEventPumpMDRImpl.unregister(registry, "main2", "key2", null);
        matches = ModelEventPumpMDRImpl.getMatches(registry, "key2", "subkey2");
        checkSingle("sub2", matches);
        
        // Test multipe items registered for single key set
        ModelEventPumpMDRImpl.register(registry, "multi1", "key4", null);
        ModelEventPumpMDRImpl.register(registry, "multi2", "key4", null);
        matches = ModelEventPumpMDRImpl.getMatches(registry, "key4", "foo");
        assertTrue(matches.contains("multi1"));
        assertTrue(matches.contains("multi2"));
        assertEquals(2, matches.size());

        ModelEventPumpMDRImpl.unregister(registry, "multi1", "key4", null);
        matches = ModelEventPumpMDRImpl.getMatches(registry, "key4", "foo");
        checkSingle("multi2", matches);
        
        ModelEventPumpMDRImpl.unregister(registry, "multi3", "key4", null);
        matches = ModelEventPumpMDRImpl.getMatches(registry, "key4", "foo");
        checkSingle("multi2", matches);        

        ModelEventPumpMDRImpl.unregister(registry, "multi2", "key4", null);
        assertTrue(ModelEventPumpMDRImpl.getMatches(registry, "key4", "foo")
                .isEmpty());

        // Remove with null item should remove all entries for key
        ModelEventPumpMDRImpl.register(registry, "multi3", "key4", null);
        ModelEventPumpMDRImpl.register(registry, "multi4", "key4", null);
        ModelEventPumpMDRImpl.unregister(registry, null, "key4", null);
        assertTrue(ModelEventPumpMDRImpl.getMatches(registry, "key4", "foo")
                .isEmpty());


        ModelEventPumpMDRImpl.register(registry, "multi1", "key4",
                new String[] {"subkey"});
        ModelEventPumpMDRImpl.register(registry, "multi2", "key4",
                new String[] {"subkey"});
        matches = ModelEventPumpMDRImpl.getMatches(registry, "key4", "subkey");
        assertTrue(matches.contains("multi1"));
        assertTrue(matches.contains("multi2"));
        assertEquals(2, matches.size());
        
        assertTrue(ModelEventPumpMDRImpl.getMatches(registry, "key4", "foo")
                .isEmpty());
        
        ModelEventPumpMDRImpl.unregister(registry, "multi3", "key4",
                new String[] {"subkey"});
        matches = ModelEventPumpMDRImpl.getMatches(registry, "key4", "subkey");
        matches = ModelEventPumpMDRImpl.getMatches(registry, "key4", "subkey");
        assertTrue(matches.contains("multi1"));
        assertTrue(matches.contains("multi2"));
        assertEquals(2, matches.size());
        
        ModelEventPumpMDRImpl.unregister(registry, "multi2", "key4",
                new String[] {"subkey"});
        matches = ModelEventPumpMDRImpl.getMatches(registry, "key4", "subkey");
        checkSingle("multi1", matches);        

        ModelEventPumpMDRImpl.register(registry, "multi3", "key4",
                new String[] {"subkey"});
        ModelEventPumpMDRImpl.register(registry, "multi4", "key4", null);

        ModelEventPumpMDRImpl.unregister(registry, null, "key4", null);
        assertTrue(ModelEventPumpMDRImpl.getMatches(registry, "key4", "subkey")
                .isEmpty());

    }
    
    private void checkSingle(String value, Collection matches) {
        assertEquals(1, matches.size());
        assertEquals(value, matches.iterator().next()); 
    }
}
