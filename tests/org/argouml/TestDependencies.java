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
import java.util.Collection;

import jdepend.framework.JDepend;
import jdepend.framework.JavaClass;
import jdepend.framework.JavaPackage;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test for Dependency cycles with JDepend. <p>
 * 
 * This test will guarantee that once 
 * a package is made free of dependency-cycles, 
 * (and it has been added here,)
 * it will stay dependency-cycle free.
 * 
 * @author Michiel
 */
public class TestDependencies extends TestCase {
    /**  
     * Tests that a list of packages does not contain
     * any package dependency cycles.
     *
     * @return a list of tests.
     */
    public static Test suite() {
        JDepend jdepend = new JDepend();

        try {
	    jdepend.addDirectory("../argouml");
	} catch (IOException e) {
	    // Ignore if the directory does not exist.
	    // This error will throw when running from the ant setup.
	    System.out.println("Assuming running from ant!");
	}

	try {
	    jdepend.addDirectory("../build/classes");
	} catch (IOException e) {
	    // Ignore if the directory does not exist.
	    // This error will throw when running from the Eclipse setup.
	    System.out.println("Assuming running from Eclipse!");
	}
        
        jdepend.analyze();

        TestSuite suite =
            new TestSuite("Tests for dependencies using Jdepend");

        String[] clean = {
            "org.argouml.application.configuration",
            "org.argouml.application.events",
            "org.argouml.application.helpers",
            "org.argouml.application.security",
            "org.argouml.cognitive.checklist",
            "org.argouml.application.api",
            "org.argouml.i18n",
            "org.argouml.swingext",
            "org.argouml.taskmgmt",
            "org.argouml.uml.diagram.layout",
            "org.argouml.notation.providers",
            //"org.argouml.notation.providers.java",
            //"org.argouml.notation.providers.uml",
            //"org.argouml.notation",
            //"org.argouml.notation.ui",
            "org.argouml.uml.util.namespace",
            "org.argouml.util.logging",
            "org.argouml.util.osdep.win32",
            "org.argouml.util.osdep",
            "org.argouml.swingext",
        };

        for (int i = 0; i < clean.length; i++) {
            suite.addTest(new CheckOnePackage(jdepend, clean[i]));
        }
        return suite;
    }

    static class CheckOnePackage extends TestCase {
        private String packageName;
        private JDepend jdepend;

        CheckOnePackage(JDepend jd, String name) {
            super(name);
            jdepend = jd;
            packageName = name;
        }

        public void runTest() {
            JavaPackage p = jdepend.getPackage(packageName);
            assertNotNull(p);
            if (p.containsCycle()) {
                StringBuffer msg = new StringBuffer(
                        "JDepend indicates a dependency cycle in ");
                msg.append(p.getName());
                msg.append("(" + p.getClassCount() + " classes: ");
                Collection<JavaClass> c = p.getClasses();
                for (JavaClass jc : c) {
                    msg.append(jc.getName());
                    msg.append(" ");
                }
                msg.append(")");
                assertTrue(msg.toString(), false);
            }
        }
    }
}
