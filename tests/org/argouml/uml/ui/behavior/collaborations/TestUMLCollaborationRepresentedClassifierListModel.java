// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

/**
 * @since Oct 28, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLCollaborationRepresentedClassifierListModel
    extends TestCase {

    private Object elem;
    private UMLCollaborationRepresentedClassifierListModel model;

    /**
     * Constructor for TestUMLCollaborationRepresentedClassifierListModel.
     *
     * @param arg0 is the name of the test case.
     */
    public TestUMLCollaborationRepresentedClassifierListModel(String arg0) {
        super(arg0);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        elem = Model.getCollaborationsFactory().createCollaboration();
        model = new UMLCollaborationRepresentedClassifierListModel();
        model.setTarget(elem);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        Model.getUmlFactory().delete(elem);
        model = null;
    }

    /**
     * Test setRepresentedClassifier().
     */
    public void testSetRepresentedOperation() {
        Object oper = Model.getCoreFactory().createClassifier();
        Model.getCollaborationsHelper().setRepresentedClassifier(elem, oper);
        assertEquals(1, model.getSize());
        assertEquals(oper, model.getElementAt(0));
    }

    /**
     * Test removing the represented operation.
     */
    public void testRemoveRepresentedOperation() {
	Object oper = Model.getCoreFactory().createClassifier();
        Model.getCollaborationsHelper().setRepresentedClassifier(elem, oper);
        Model.getCollaborationsHelper().setRepresentedClassifier(elem, null);
        assertEquals(0, model.getSize());
        assertTrue(model.isEmpty());
    }

}
