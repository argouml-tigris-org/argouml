// $Id$
// Copyright (c) 2002 The Regents of the University of California. All
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

package org.argouml.model.uml.behavioralelements.collaborations;

import java.lang.ref.WeakReference;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.modelmanagement.ModelManagementFactory;
import org.argouml.util.CheckUMLModelHelper;

import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.model_management.MModel;

public class TestCollaborationsFactory extends TestCase {

    static String[] allModelElements =
    {
	"AssociationEndRole",
	"AssociationRole",
	"ClassifierRole",
	"Collaboration",
	"Interaction",
	"Message",
    };

    public TestCollaborationsFactory(String n) {
	super(n);
    }

    public void testSingleton() {

	Object o1 = CollaborationsFactory.getFactory();

	Object o2 = CollaborationsFactory.getFactory();

	assertTrue("Different singletons", o1 == o2);

    }

    public void testCreates() {

	String[] objs = {
	    "AssociationEndRole",
	    "AssociationRole",
	    "ClassifierRole",
	    "Collaboration",
	    "Interaction",
	    "Message",
	    null 
	};

	CheckUMLModelHelper.createAndRelease(
					     this,
					     CollaborationsFactory.getFactory(),
					     objs);

    }

    public void testDeleteComplete() {
	CheckUMLModelHelper.deleteComplete(
					   this,
					   CollaborationsFactory.getFactory(),
					   allModelElements);
    }

    /** test to check whether elements which are attached to a
     *  ClassifierRole get deleted upon deletion of the 
     *  ClassifierRole. These elements are Interaction, Message,
     *  AssociationRole.
     */
    public void testDeleteClassifierRole() {

	MModel model = ModelManagementFactory.getFactory().createModel();
	MCollaboration collab =
	    CollaborationsFactory.getFactory().buildCollaboration(model);
	MClassifierRole cr1 =
	    CollaborationsFactory.getFactory().createClassifierRole();
	MClassifierRole cr2 =
	    CollaborationsFactory.getFactory().createClassifierRole();
	MAssociationRole role =
	    CollaborationsFactory.getFactory().buildAssociationRole(cr1, cr2);
	MInteraction inter =
	    CollaborationsFactory.getFactory().buildInteraction(collab);
	MMessage mes =
	    CollaborationsFactory.getFactory().buildMessage(inter, role);

	WeakReference cr1wr = new WeakReference(cr1);
	WeakReference rolewr = new WeakReference(role);
	WeakReference interwr = new WeakReference(inter);
	WeakReference meswr = new WeakReference(mes);

	UmlFactory.getFactory().delete(cr1);
	cr1 = null;
	role = null;
	inter = null;
	mes = null;
	System.gc();
	assertNull("ClassifierRole not removed", cr1wr.get());
	assertNull("AssociationRole not removed", rolewr.get());
	// Interaction should not be removed when removing classifierrole...
	// maybe if the last message is removed from the interaction but even
	// then it's doubtfull since it will probably lead to backward compatibility 
	// problems in save formats.
	// assertNull("Interaction not removed", interwr.get());
	assertNull("Message not removed", meswr.get());

    }
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
	super.setUp();
	ArgoSecurityManager.getInstance().setAllowExit(true);
        UmlFactory.getFactory().setGuiEnabled(false);
    }
}
