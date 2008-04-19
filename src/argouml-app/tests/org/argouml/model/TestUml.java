// $Id$
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

package org.argouml.model;

import junit.framework.TestCase;

/**
 * @author Thierry
 */
public class TestUml extends TestCase
{

    /**
     * Constructor for Uml.
     *
     * @param arg0 is the name of the test case.
     */
    public TestUml(String arg0)
    {
        super(arg0);
    }


    /**
     * TODO: Rewrite this test using reflection against Uml class.
     * 
     * This code has been commented out since September 2003
     */
    public void testUmlClassList() {
//        for (Iterator i = Uml.getUmlClassList().iterator(); i.hasNext();) {
//            UmlModelEntity type = (UmlModelEntity) i.next();
//            String typeName = type.getClass().getName().toUpperCase();
//            String expected = type.getName().toUpperCase();
//            // System.out.println(type.getClass().getName().toUpperCase());
//            // System.out.println("ORG.ARGOUML.MODEL.UML.UML$TYPE"
//            // + key.toString().toUpperCase());
//            assertEquals(
//                "Not the correct class",
//                type.getClass().getName().toUpperCase(),
//                "ORG.ARGOUML.MODEL.UML.UML$TYPE"
//                    + type.getName().toUpperCase());
//        }
    }

}
