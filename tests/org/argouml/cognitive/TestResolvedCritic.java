// $Id$
// Copyright (c) 2002-2007 The Regents of the University of California. All
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

package org.argouml.cognitive;

import java.util.Vector;

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;

import org.argouml.model.Model;

/**
 * Test the ResolvedCritic class.
 *
 */
public class TestResolvedCritic extends TestCase {

    /**
     * The constructor.
     *
     * @param name the name of the test
     */
    public TestResolvedCritic(String name) {
	super(name);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
	super.setUp();
        InitializeModel.initializeDefault();
    }

    /**
     * Test the constructors.
     */
    public void testConstructors() {
        Object testmc = Model.getCoreFactory().buildClass();
        Critic c = new Critic();
        String crString = "class org.argouml.cognitive.critics.Critic";
        ResolvedCritic rc;
        Vector<String> vec = new Vector<String>();
        ListSet set = new ListSet();

        /* ResolvedCritic(String, Vector) */

        rc = new ResolvedCritic("rc", null);
        assertTrue("getCritic simple", "rc".equals(rc.getCritic()));
        assertTrue(
        	   "getOffenderList simple",
        	   (rc.getOffenderList() == null
        	    || rc.getOffenderList().size() == 0));

        vec.add("str1");
        rc = new ResolvedCritic("rc2", vec);
        assertTrue("getCritic 2nd", "rc2".equals(rc.getCritic()));
        assertTrue(
        	   "getOffenderList 2nd",
        	   rc.getOffenderList() != null
        	   && "str1".equals(rc.getOffenderList().get(0)));

        /* ResolvedCritic(Critic, VectorSet) */

        try {
            rc = new ResolvedCritic(c, null);
            System.out.println(rc.getCritic());
            assertTrue("getCritic 1", crString.equals(rc.getCritic()));
            assertTrue(
        	       "getOffenderList 1",
        	       rc.getOffenderList() == null
        	       || rc.getOffenderList().size() == 0);
        } catch (UnresolvableException ure1) {
            assertTrue("create 1 with MClass", false);
        }

        set.addElement(testmc);
        try {
            rc = new ResolvedCritic(c, set);

            assertTrue("getCritic 2", crString.equals(rc.getCritic()));
            assertTrue(
        	       "assigns id 2",
        	       ItemUID.getIDOfObject(testmc, false) != null);
            assertTrue(
        	       "getOffenderList 2",
        	       rc.getOffenderList() != null
        	       && (ItemUID.getIDOfObject(testmc, false).equals(
        			   rc.getOffenderList().get(0))));
        } catch (UnresolvableException ure1) {
            assertTrue("create 2 with MClass", false);
        }

        /* ResolvedCritic(Critic, VectorSet, boolean) */

        /* testmc should now have an ItemUID so we should be able to
         * create without adding a new ItemUID */
        try {
            rc = new ResolvedCritic(c, set, false);

            assertTrue("getCritic 3", crString.equals(rc.getCritic()));
            assertTrue(
        	       "assigns id 3",
        	       ItemUID.getIDOfObject(testmc, false) != null);
            assertTrue(
        	       "getOffenderList 3",
        	       rc.getOffenderList() != null
        	       && (ItemUID.getIDOfObject(testmc, false).equals(
        			   rc.getOffenderList().get(0))));
        } catch (UnresolvableException ure1) {
            assertTrue("create 3 with MClass", false);
        }
        set.remove(testmc);
    }

    /**
     * Test the equals() method.
     */
    public void testEquals() {
	Critic c = new Critic();
	ResolvedCritic rc1, rc2;
	Vector<String> vec = new Vector<String>();
	ListSet set = new ListSet();

	rc1 = new ResolvedCritic("RC", null);
	rc2 = new ResolvedCritic("RC", null);
	assertTrue("Same empty ctor", rc1.equals(rc2) && rc2.equals(rc1));

	rc2 = new ResolvedCritic("RC", vec);
	assertTrue("Empty vec ctor", rc1.equals(rc2) && rc2.equals(rc1));

	vec.add("a");
	rc2 = new ResolvedCritic("RC", vec);
	assertTrue("Bigger rc2 - 1", rc1.equals(rc2));
	assertTrue("Bigger rc2 - 2", !rc2.equals(rc1));

	rc1 = new ResolvedCritic("RC", vec);
	assertTrue("Same vec ctor", rc1.equals(rc2) && rc2.equals(rc1));

	vec.clear();
	vec.add("b");
	rc2 = new ResolvedCritic("RC", vec);
	assertTrue("Diff rc2 - 1", !rc1.equals(rc2));
	assertTrue("Diff rc2 - 2", !rc2.equals(rc1));

	try {
	    rc1 = new ResolvedCritic(c, null);
	    rc2 = new ResolvedCritic(c, null);
	    assertTrue(
		       "Same empty ctor (c)",
		       rc1.equals(rc2) && rc2.equals(rc1));

	    rc2 = new ResolvedCritic(c, set);
	    assertTrue("Empty set ctor", rc1.equals(rc2) && rc2.equals(rc1));
	} catch (UnresolvableException ure) {
	    assertTrue("Test error URE", false);
	}
    }
}
