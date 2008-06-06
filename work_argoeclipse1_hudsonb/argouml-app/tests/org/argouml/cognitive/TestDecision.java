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

package org.argouml.cognitive;

import junit.framework.TestCase;

/**
 * Testing the initialization of a Decision.
 */
public class TestDecision extends TestCase {

    /**
     * The constructor.
     *
     * @param name the name of the test.
     */
    public TestDecision(String name) {
	super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
	super.setUp();
    }

    /**
     * Test constructor and some basic methods.
     */
    public void testBasics() {
	
	// initialize basic parameters that are
	// different from Decision.UNSPEC
	int priority = 2;
	String name = "misc.decision.inheritance";
	String translatedName = Translator.localize(name);
	
	// initialize a decision object with parameters
	Decision decision = new Decision(name, priority);
	
	// verify parameters were initialized correctly by the constructor
	assertTrue("Decision.getName() is incorrect",
		decision.getName() == translatedName);
	assertTrue("Decision.toString() is incorrect",
		decision.toString() == translatedName);	
	assertTrue("Decision.getPriority() is incorrect",
		decision.getPriority() == priority);
	
	// set the priority to a different value
	priority = 1;
	decision.setPriority(priority);
	
	// verify the priority was correctly updated
	assertTrue("Decision.setPriority() failed",
		decision.getPriority() == priority);
	
	// set the name to a different value
	name = "misc.decision.uncategorized";
	translatedName = Translator.localize(name);
	decision.setName(translatedName);
	
	// verify the name property was correctly updated
	assertTrue("Decision.setName() failed",
		decision.getName() == translatedName);
	assertTrue("Decision.toString() is incorrect",
		decision.toString() == translatedName);
	
	// following the update the priority should be
	// equivalent to Decision.UNSPEC
	assertTrue("Decision.equals(Decision) failed",
		decision.equals(Decision.UNSPEC));
    }
}
