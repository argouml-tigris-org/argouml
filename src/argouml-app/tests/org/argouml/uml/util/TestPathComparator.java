// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.uml.util;

import java.util.Comparator;

import junit.framework.TestCase;

import org.argouml.model.InitializeModel;
import org.argouml.model.Model;

/**
 * Testing the PathComparator class.
 *
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class TestPathComparator extends TestCase {


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
    }
    
    /**
     * Test the PathComparator to make sure that it returns expected results
     * as well as conforms to the contract for Comparator.
     */
    public void testComparator() {
        Comparator comp = new PathComparator();
        Object root = Model.getModelManagementFactory().createModel();
        setName(root, "rootModel");
        Object unnamed1 = Model.getCoreFactory().buildClass(root);
        // Name is set to the empty string (yuck!) by default - fix it
        setName(unnamed1, null);
        
        Object unnamed2 = Model.getCoreFactory().buildClass(root);
        setName(unnamed2, null);
        Object a = Model.getCoreFactory().buildClass("a", root);
        Object a1 = Model.getCoreFactory().buildClass("a", root);
        Object b = Model.getCoreFactory().buildClass("b", root);
        Object b1 = Model.getCoreFactory().buildClass("B", root);
        Object c = Model.getCoreFactory().buildClass("c", root);
        
        Object ba = Model.getCoreFactory().buildClass("b", a);    
        Object bc = Model.getCoreFactory().buildClass("b", c);    
        
        assertEquals("Two nulls should be equal", 0, comp.compare(null, null));
        
        assertEquals("Null should be less than anything", -1, 
                comp.compare(null, ""));
        assertEquals("Null should be less than anything", 1, 
                comp.compare("", null));
        
        assertEquals("Simple string compare failed", comp.compare("a","b"),
                "a".compareTo("b"));

        assertFalse("Two different unnamed elements should not be equal", 
                comp.compare(unnamed1, unnamed2) == 0);
        assertEquals("Unnamed elements should collate before named", -1, 
                comp.compare(unnamed1, a));
        assertEquals("Unnamed elements should collate before named", 1, 
                comp.compare(a, unnamed1));

        assertFalse("Two different elements with same name should not be equal", 
                comp.compare(a, a1) == 0);

        assertEquals("Comparison not stable for elements with same name", 
                comp.compare(a, a1), -1 * comp.compare(a1, a));
        
        assertEquals("Shorter paths should collate before longer paths", 1,
                comp.compare(b, ba));
        assertEquals("Shorter paths should collate before longer paths", -1,
                comp.compare(ba, b));

        assertEquals("Comparison failed on container", -1,
                comp.compare(ba, bc));
        assertEquals("Comparison failed on container", 1,
                comp.compare(bc, ba));
        
        assertEquals("Comparator results should match String.compareTo",
                comp.compare(b, b1), 
                Model.getFacade().getName(b).compareTo(
                        Model.getFacade().getName(b1)));
        assertEquals("Comparator results should match String.compareTo",
                comp.compare(b1, b), 
                Model.getFacade().getName(b1).compareTo(
                        Model.getFacade().getName(b)));        
        Model.getUmlFactory().delete(root);
    }

    
    private void setName(Object elem, String name) {
        Model.getCoreHelper().setName(elem, name);
    }

}
