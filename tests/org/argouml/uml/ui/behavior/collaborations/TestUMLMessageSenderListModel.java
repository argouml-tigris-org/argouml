// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.collaborations;

import junit.framework.TestCase;

import org.argouml.model.Model;
import org.argouml.uml.ui.MockUMLUserInterfaceContainer;

/**
 * @since Oct 30, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLMessageSenderListModel extends TestCase {

    private UMLMessageSenderListModel model;
    private Object elem;

    /**
     * Constructor for TestUMLMessageSenderListModel.
     * @param arg0 is the name of the test case.
     */
    public TestUMLMessageSenderListModel(String arg0) {
        super(arg0);
    }


    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        elem = Model.getCollaborationsFactory().createMessage();
        MockUMLUserInterfaceContainer cont =
            new MockUMLUserInterfaceContainer();
        cont.setTarget(elem);
        model = new UMLMessageSenderListModel();
        model.setTarget(elem);
        Model.getPump().flushModelEvents();
    }

    /*
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        Model.getUmlFactory().delete(elem);
        model = null;
    }

    /**
     * Test setSender().
     */
    public void testSetSender() {
        Object role =
            Model.getCollaborationsFactory().createClassifierRole();
        Model.getCollaborationsHelper().setSender(elem, role);
        Model.getPump().flushModelEvents();
        assertEquals(1, model.getSize());
        assertEquals(role, model.getElementAt(0));
    }

    /**
     * Test setSender() with null argument.
     */
    public void testRemoveReceiver() {
        Object role =
            Model.getCollaborationsFactory().createClassifierRole();
        Model.getCollaborationsHelper().setSender(elem, role);
        Model.getCollaborationsHelper().setSender(elem, null);
        Model.getPump().flushModelEvents();
        assertEquals(0, model.getSize());
        assertTrue(model.isEmpty());
    }
}
