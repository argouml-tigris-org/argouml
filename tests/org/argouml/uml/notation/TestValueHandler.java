// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.uml.notation;

import junit.framework.TestCase;

/**
 * @author Michiel
 */
public class TestValueHandler extends TestCase {

    public void testValueHandler() {
        ValueHandler vh = new ValueHandlerImpl();
        vh.putValue("testBoolTrue", true);
        vh.putValue("testBoolFalse", false);
        Object a = new Object();
        Object b = new Object();
        vh.putValue("testObject1", a);
        vh.putValue("testObject2", b);
        vh.putValue("testObject1", null);
        
        assertTrue("Test boolean true", vh.isValue("testBoolTrue"));
        assertTrue("Test boolean false", !vh.isValue("testBoolFalse"));

        assertTrue("Test nonexisting key", !vh.isValue("non-existing"));
        assertTrue("Test non-boolean value retrieved with isValue", 
                !vh.isValue("testObject2"));
        
        assertTrue("Test object retrieval", vh.getValue("testObject2") == b);
        assertTrue("Test object removal", null == vh.getValue("testObject1"));
        
        assertTrue("Test boolean retrieval", 
                vh.getValue("testBoolTrue") instanceof Boolean);
    }

    private class ValueHandlerImpl extends ValueHandler {

        /**
         * @see org.argouml.notation.NotationProvider4#getParsingHelp()
         */
        public String getParsingHelp() {
            return null;
        }

        /**
         * @see org.argouml.notation.NotationProvider4#parse(java.lang.String)
         */
        public String parse(String text) {
            return null;
        }
        
    }
}
