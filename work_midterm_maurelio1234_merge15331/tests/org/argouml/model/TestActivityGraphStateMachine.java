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

package org.argouml.model;

import java.util.Collection;

import junit.framework.TestCase;

import org.apache.log4j.Logger;


/**
 * Tests for the state machines in an ActivityGraph.
 * 
 * @author Tom Morris
 */
public class TestActivityGraphStateMachine extends TestCase {

    private static final Logger LOG = 
        Logger.getLogger(TestActivityGraphStateMachine.class);
    
    /**
     * The constructor.
     *
     * @param n the name
     */
    public TestActivityGraphStateMachine(String n) {
	super(n);
        InitializeModel.initializeDefault();
    }

    @Override
    public void setUp() throws Exception {
	super.setUp();
    }

     
    /**
     * Test that enclosed states get deleted along with their state machine.
     * <p>
     * NOTE: This needs a clean environment to start, so it can't go in the
     * same suite with any tests which leave residue in the model repository.
     */
    public void testDeleteStateMachineTop() {
        // Make sure we start off clean
       
        // TODO: This doesn't work, but should
//        Collection elements = Model.getFacade().getRootElements();
//        Model.getUmlHelper().deleteCollection(elements);
//        elements = Model.getFacade().getRootElements();
//        assertTrue("Failed to create clean environment", elements.isEmpty());
        
        // Build an activity graph with a single action state 
        // the way the GUI would
        Object model = Model.getModelManagementFactory().createModel();
        LOG.debug("Created model "  + model);
        Object activityGraph = Model.getActivityGraphsFactory()
                .buildActivityGraph(model);
        LOG.debug("Created activity graph "  + activityGraph);
        Object top = Model.getFacade().getTop(activityGraph);
        assertNotNull("Activity graph got created without a top state", top);
        LOG.debug("top state "  + top);

        Object actionState = Model.getActivityGraphsFactory()
                .createActionState();
        LOG.debug("Created action state "  + top);
        Model.getStateMachinesHelper().setContainer(actionState, top);

        Collection subs = Model.getFacade().getSubvertices(top);
        assertEquals("setContainer didn't work", actionState, 
                subs.iterator().next());

        Collection roots = Model.getFacade().getRootElements();
        assertEquals("More than one root element", 1, roots.size());
        assertEquals("Wrong root element", model, roots.iterator().next());
        
        // Delete the model and make sure everything inside goes with it
        Model.getUmlFactory().delete(model);
        Collection theDregs = Model.getFacade().getRootElements();
        assertTrue("Failed to delete all elements from an activity graph",
                theDregs.isEmpty());
    }
}
