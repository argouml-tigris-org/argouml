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
public class TestGoal extends TestCase {

    /**
     * The constructor.
     *
     * @param name the name of the test.
     */
    public TestGoal(String name) {
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
	
	// initialize the GoalModel
	GoalModel goalModel = new GoalModel();
	Goal unspecifiedGoal = Goal.getUnspecifiedGoal();
	
	// check that the model was initialized with one goal
	assertTrue("GoalModel not initialized with one goal",
		goalModel.getGoalList().size() == 1);
	
	// check that the model was initialized with the unspecified goal
	assertTrue("GoalModel not initilized with UnspecifiedGoal",
		goalModel.hasGoal(unspecifiedGoal.getName()));
	
	// change priority of unspecified goal so we can verify it was updated
        goalModel.setGoalPriority(unspecifiedGoal.getName(), unspecifiedGoal
                .getPriority() + 1);
	
	// first verify that the set goal priority did not add an extra goal
	assertTrue("GoalModel.setGoalPriority added an extra Goal",
		goalModel.getGoalList().size() == 1);
	
	// check that the model correctly updates a goal's priority
        assertTrue("GoalModel not correctly updating goal priority", goalModel
                .getGoalList().get(0).getPriority() == unspecifiedGoal
                .getPriority() + 1);
	
	// test the removal of a goal
	goalModel.removeGoal(unspecifiedGoal);
	
	// number of goals should now be 0
	assertTrue("GoalModel.removeGoal(goalName) failed",
		goalModel.getGoalList().size() == 0);
	
	// start desiring a goal which should add a goal and bump the size to 1
        goalModel.startDesiring(unspecifiedGoal.getName());
	
	// number of goals should now be 1
	assertTrue("GoalModel.startDesiring did not add a Goal",
		goalModel.getGoalList().size() == 1);
	
	// stop desiring the goal which should remove it from the model
	goalModel.stopDesiring(unspecifiedGoal.getName());
	
	// number of goals should now be 0
	assertTrue("GoalModel.stopDesiring(goalName) failed",
		goalModel.getGoalList().size() == 0);
    }
}
