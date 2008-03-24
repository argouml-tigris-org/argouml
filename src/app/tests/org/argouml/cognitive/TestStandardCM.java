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
public class TestStandardCM extends TestCase {

    /**
     * The constructor.
     *
     * @param name the name of the test.
     */
    public TestStandardCM(String name) {
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
     * Test isRelevant method of EnabledCM.
     */
    public void testEnabledCM() {
	
	Critic critic = new Critic();
	EnabledCM cm = new EnabledCM();
	
	// EnabledCM isRelevant checks if the critic is enabled
	// initialize this to true to verify isRelevant returns true
	critic.setEnabled(true);
	
	// verify isRelevant returns true
	assertTrue("EnabledCM.isRelevant(Critic, Designer) is incorrect",
		cm.isRelevant(critic, Designer.theDesigner()));
	
	// set enabled to false and check the opposite condition
	critic.setEnabled(false);
	
	// verify isRelevant not is false
	assertTrue("EnabledCM.isRelevant(Critic, Designer) is incorrect",
		!cm.isRelevant(critic, Designer.theDesigner()));
    }

    /**
     * Test isRelevant method of NotSnoozedCM.
     */
    public void testNotSnoozedCM() {
	
	Critic critic = new Critic();
	NotSnoozedCM cm = new NotSnoozedCM();
	
	// NotSnoozedCM isRelevant checks if the critic is snoozed
	// snooze the critic to verify isRelevant returns false
	critic.snooze();
	
	// verify isRelevant returns true
	assertTrue("NotSnoozedCM.isRelevant(Critic, Designer) is incorrect " +
			"when critic snoozed",
		!cm.isRelevant(critic, Designer.theDesigner()));
	
	// unsnooze the critic to verify the critic is not relevant
	critic.unsnooze();
	
	// verify isRelevant is true
	assertTrue("NotSnoozedCM.isRelevant(Critic, Designer) is incorrect " +
			"when critic not snoozed",
		cm.isRelevant(critic, Designer.theDesigner()));
    }

    /**
     * Test isRelevant method of DesignGoalsCM.
     */
    public void testDesignGoalsCM() {
	
	Critic critic = new Critic();
	DesignGoalsCM cm = new DesignGoalsCM();
	
	// DesignGoalsCM checks if the critic isRelevantToGoals of the Designer
	// this value is always true for the Critic base class so DesignerGoalsCM
	// should always return true in this condition
	assertTrue("DesignGoalsCM.isRelevant(Critic, Designer) is incorrect",
		cm.isRelevant(critic, Designer.theDesigner()));	
    }

    /**
     * Test isRelevant method of CurDecisionCM.
     */
    public void testCurDecisionCM() {
	
	Critic critic = new Critic();
	CurDecisionCM cm = new CurDecisionCM();
	
	// CurDecisionCM isRelevant checks to see if the critic has
	// any decisions that have a priority > 0 and less than the
	// priority of the critic.  By default the critic has no
	// decisions so isRelevant should return false.
	assertTrue("CurDecisionCM.isRelevant(Critic, Designer) is incorrect " +
			"when critic has 0 decisions",
		!cm.isRelevant(critic, Designer.theDesigner()));
	
	// add a decision but set the critic priority to 0 so isRelevant
	// should still be false
	critic.setPriority(0);
	critic.addSupportedDecision(Decision.UNSPEC);
	
	// verify isRelevant is still false
	assertTrue("CurDecisionCM.isRelevant(Critic, Designer) is " +
			"incorrect with one decision and critic has priority 0",
		!cm.isRelevant(critic, Designer.theDesigner()));
	
	// update the priority of the critic to be the same priority as the decision
	critic.setPriority(Decision.UNSPEC.getPriority());
	
	// isRelevant should now be true
	assertTrue("CurDecisionCM.isRelevant(Critic, Designer) is incorrect with " +
			"one decision and priority has equal priority",
		cm.isRelevant(critic, Designer.theDesigner()));
	
	critic.setPriority(Decision.UNSPEC.getPriority()+1);
	// isRelevant should still be true
	assertTrue("CurDecisionCM.isRelevant(Critic, Designer) is incorrect with one " +
			"decision and priority has greater priority",
		cm.isRelevant(critic, Designer.theDesigner()));
    }
 }
