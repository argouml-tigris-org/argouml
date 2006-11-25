// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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
 * Test the collaborations factory for all model implementations.<p>
 *
 * No imports from org.argouml.model.uml or other subpackage!
 */
public class TestCollaborationsFactory extends TestCase {

    /**
     * All the ModelElements we are going to test.
     */
    private static String[] allModelElements =
    {
	"AssociationEndRole",
	"AssociationRole",
	"ClassifierRole",
	"Collaboration",
	"Interaction",
	"Message",
    };

    /**
     * The constructor.
     *
     * @param n the name
     */
    public TestCollaborationsFactory(String n) {
        super(n);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
	//this should instantiate a new implementation
	Model.getFacade();
    }

    /**
     * Test the creation of the elements.
     */
    public void testCreates() {

        String[] objs = {
	    "AssociationEndRole",
	    "AssociationRole",
	    "ClassifierRole",
	    "Collaboration",
	    "Interaction",
	    "Message",
	    null,
	};

        CheckUMLModelHelper.createAndRelease(
            Model.getCollaborationsFactory(),
            objs);

    }

    /**
     * Test for delete methods.
     */
    public void testAllDeleteMethodsAvailable() {
        CheckUMLModelHelper.hasDeleteMethod(Model.getCollaborationsFactory(),
                allModelElements);
    }

    /**
     * Test to check whether elements which are attached to a ClassifierRole get
     * deleted upon deletion of the ClassifierRole. These elements are
     * Message and AssociationRole.
     */
    public void testDeleteClassifierRole() {
        Object model = Model.getModelManagementFactory().createModel();

        Object collab =
	    Model.getCollaborationsFactory().buildCollaboration(model);
        Object cr1 = Model.getCollaborationsFactory().createClassifierRole();
        Object cr2 = Model.getCollaborationsFactory().createClassifierRole();
        // Set namespace so buildAssocationRole works
        Model.getCoreHelper().setNamespace(cr1, collab);
        Model.getCoreHelper().setNamespace(cr2, collab);
        Object role =
	    Model.getCollaborationsFactory().buildAssociationRole(cr1, cr2);
        assertNotNull("Failed to create role", role);
        Object inter =
	    Model.getCollaborationsFactory().buildInteraction(collab);
        assertNotNull("Failed to build interaction", inter);
        Object message =
            Model.getCollaborationsFactory().buildMessage(inter, role);
        assertNotNull("Failed to build message", message);

        Model.getUmlFactory().delete(cr1);
        Model.getPump().flushModelEvents();

        assertTrue("ClassifierRole not removed",
                Model.getUmlFactory().isRemoved(cr1));
        assertTrue("AssociationRole not removed",
                Model.getUmlFactory().isRemoved(role));
        assertTrue("Message not removed",
                Model.getUmlFactory().isRemoved(message));
        /*
         * This comment was included in a previous version (before 1/2005)
         * of the test which had this assertion commented out:
         * ------
         * Interaction should not be removed when removing ClassifierRole...
         * maybe if the last message is removed from the interaction but even
         * then it's doubtfull since it will probably lead to backward
         * compatibility problems in save formats.
         * ------
         * but my reading of the UML 1.4 specification is that an Interaction
         * without at least one message is definitely illegal, so MDR is
         * doing the right thing by removing it in this case where we only
         * have a single message, which then gets deleted. - tfm
         */
        assertTrue("Interaction not removed",
                Model.getUmlFactory().isRemoved(inter));
    }

    /**
     * @return Returns the allModelElements.
     */
    static String[] getAllModelElements() {
        return allModelElements;
    }

    /**
     * Test that IllegalArgumentException is thrown when a null is sent.
     */
    public void testExpectedIllegalArgumentException() {
        try {
            Model.getCollaborationsFactory().buildActivator(null, null);
            fail("Exception missing");
        } catch (IllegalArgumentException e) {
            // Correct Exception was thrown.
        }

        try {
            Model.getCollaborationsFactory().buildMessage(null, null);
            fail("Exception missing");
        } catch (IllegalArgumentException e) {
            // Correct Exception was thrown.
        }

        Object collab = Model.getCollaborationsFactory().createCollaboration();
        try {
            Model.getCollaborationsFactory().buildMessage(collab, null);
            fail("Exception missing");
        } catch (IllegalArgumentException e) {
            // Correct Exception was thrown.
        }

        Object inter = Model.getCollaborationsFactory().createInteraction();
        try {
            Model.getCollaborationsFactory().buildMessage(inter, null);
            fail("Exception missing");
        } catch (IllegalArgumentException e) {
            // Correct Exception was thrown.
        }

    }
}
