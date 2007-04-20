// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml;

import java.io.IOException;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import junit.framework.TestCase;

/**
 * Test for Dependency cycles with JDepend. <p>
 * 
 * This test will guarantee that once 
 * a package is made free of dependency-cycles, 
 * (and it has been added here,)
 * it will stay dependecy-cycle free.
 * 
 * @author Michiel
 */
public class TestDependencies extends TestCase {
    private JDepend jdepend;

    /**
     * @see junit.framework.TestCase#setUp()
     * @throws IOException when the added dir is not found
     */
    protected void setUp() throws IOException {
        jdepend = new JDepend();

        jdepend.addDirectory("../argouml");
    }    

    /**  
     * Tests that a list of packages does not contain
     * any package dependency cycles.
     */
    public void testOnePackageCycle() {
        JavaPackage p;
        String msg = "JDepend indicates a dependency cycle in ";

        String[] clean = {"org.argouml.util.osdep.win32",
            "org.argouml.util.osdep",
            "org.argouml.application.configuration"};
        
        jdepend.analyze();

        for (int i = 0; i < clean.length; i++) {
            p = jdepend.getPackage(clean[i]);
            assertNotNull(p);
            assertFalse(msg + p.getName(), p.containsCycle());
        }
    }
}
