/*
 * Created on 13.10.2004
 */
package org.argouml.uml;

import junit.framework.TestCase;

/**
 * @author MarkusK
 *
 */
public class TestUUIDManager extends TestCase {
    
    
    public TestUUIDManager(String name) {
        super(name);
    }
    
    public void testGetNewUUID() {
        String uuid1 = UUIDManager.getInstance().getNewUUID();
        String uuid2 = UUIDManager.getInstance().getNewUUID();
        
        assertNotNull(uuid1);
        assertNotNull(uuid2);
        assertTrue(!uuid1.equals(uuid2));
    }

}
