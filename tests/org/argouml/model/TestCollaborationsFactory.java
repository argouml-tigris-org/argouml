// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

import java.lang.ref.WeakReference;

import junit.framework.TestCase;

import org.argouml.util.CheckUMLModelHelper;

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
            this,
            Model.getCollaborationsFactory(),
            objs);

    }

    /**
     * Test for deletion.
     */
    public void testDeleteComplete() {
        CheckUMLModelHelper.deleteComplete(
            this,
            Model.getCollaborationsFactory(),
            allModelElements);
    }

    /**
     * Test to check whether elements which are attached to a
     * ClassifierRole get deleted upon deletion of the
     * ClassifierRole. These elements are Interaction, Message,
     * AssociationRole.
     */
    public void testDeleteClassifierRole() {

        Object model = Model.getModelManagementFactory().createModel();
        Object collab =
            Model.getCollaborationsFactory().buildCollaboration(model);
        Object cr1 =
            Model.getCollaborationsFactory().createClassifierRole();
        Object cr2 =
            Model.getCollaborationsFactory().createClassifierRole();
        Object role =
            Model.getCollaborationsFactory().buildAssociationRole(cr1, cr2);
        Object inter =
            Model.getCollaborationsFactory().buildInteraction(collab);
        Object mes =
            Model.getCollaborationsFactory().buildMessage(
                inter,
                role);

        WeakReference cr1wr = new WeakReference(cr1);
        WeakReference rolewr = new WeakReference(role);
        WeakReference interwr = new WeakReference(inter);
        WeakReference meswr = new WeakReference(mes);

        Model.getUmlFactory().delete(cr1);
        cr1 = null;
        role = null;
        inter = null;
        mes = null;
        System.gc();
        assertNull("ClassifierRole not removed", cr1wr.get());
        assertNull("AssociationRole not removed", rolewr.get());
        // Interaction should not be removed when removing
        // classifierrole...  maybe if the last message is removed
        // from the interaction but even then it's doubtfull since it
        // will probably lead to backward compatibility problems in
        // save formats.
        // assertNull("Interaction not removed", interwr.get());
        assertNull("Message not removed", meswr.get());

    }

    /**
     * @return Returns the allModelElements.
     */
    static String[] getAllModelElements() {
        return allModelElements;
    }
}
