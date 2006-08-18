// $Id$
// Copyright (c) 2003-2006 The Regents of the University of California. All
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


package org.argouml.uml.cognitive.critics;

import org.argouml.model.Model;

import junit.framework.TestCase;


/**
 * Tests for the CrUnconventionalAttrName class.
 *
 * @author mkl
 *
 */
public class TestCrUnconventionalAttrName extends TestCase {


    private CrUnconventionalAttrName cr = new CrUnconventionalAttrName();

    /**
     * The constructor.
     *
     * @param arg0 the name of the test
     */
    public TestCrUnconventionalAttrName(String arg0) {
        super(arg0);
    }

    /**
     * Not giving a name should not result in a suggestion.
     */
    public void testNullName() {
        assertEquals("attr", cr.computeSuggestion(null));
    }

    /**
     * A name not starting with a capital should capitalize the first character.
     */
    public void testNoUnderscoreName() {
        assertEquals("test", cr.computeSuggestion("Test"));
    }

    /**
     * A name not starting with a capital should capitalize
     * the first non-underscore character.
     */
    public void testSmallUnderscoreName() {
        assertEquals("_x", cr.computeSuggestion("_X"));
    }

    /**
     * ...and only the first character, not the 2nd.
     */
    public void testLongUnderscoreName() {
        assertEquals("_xx", cr.computeSuggestion("_Xx"));
    }

    /**
     * A name that consists of a single underscore should remain untouched.
     */
    public void testOnlyUnderscoreName() {
        assertEquals("_", cr.computeSuggestion("_"));
    }
    
    public void testPredicate2() {
        Object me = Model.getCoreFactory().createAttribute();
        Model.getCoreHelper().setName(me, null);
        assertFalse(cr.predicate2(me, null));
        
        Model.getCoreHelper().setName(me, "UpperCase");
        assertTrue(cr.predicate2(me, null));
    }

}
