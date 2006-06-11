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

package org.argouml.util;

import java.lang.reflect.Modifier;
import java.util.*;

import junit.framework.*;
import junit.runner.*;

/**
 * A class to run all the tests.
 *
 */
public class DoAllTests extends TestSuite {

    /**
     * @return the test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        int count = 0;

        for (Enumeration e = (new LoadingTestCollector()).collectTests();
	     e.hasMoreElements();
	     ) {
            Object o = e.nextElement();

            if (!(o instanceof String))
                continue;
            String s = (String) o;

            if (s.equals("org.argouml.util.DoAllTests"))
                continue;

            Class candidate;
            try {
                candidate = Class.forName(s);
            } catch (ClassNotFoundException exception) {
                System.err.println("Cannot load class: " + s);
                continue;
            }
            if (!Modifier.isAbstract(candidate.getModifiers())) {
                suite.addTest(new TestSuite(candidate));
                count++;
            }

        }
        System.out.println("Number of test classes found: " + count);

        return suite;
    }
}
