// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import org.argouml.model.InitializeModel;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetManager;

/**
 * @since Oct 28, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLCollaborationRepresentedClassifierComboBoxModel
    extends TestCase {

    private Object elem;
//    private Object oper;
    private Object clazz;
    private UMLCollaborationRepresentedClassifierComboBoxModel model;

    /**
     * Constructor for TestUMLCollaborationRepresentedClassifierComboBoxModel.
     *
     * @param arg0 is the name of the test case.
     */
    public TestUMLCollaborationRepresentedClassifierComboBoxModel(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();

        elem = Model.getCollaborationsFactory().createCollaboration();
        model = new UMLCollaborationRepresentedClassifierComboBoxModel();
        TargetManager.getInstance().setTarget(elem);
        Model.getPump().flushModelEvents();
        
        Project p = ProjectManager.getManager().getCurrentProject();
        Object m = p.getRoot();
        clazz = Model.getCoreFactory().buildClass(m);
        Model.getCollaborationsHelper().setRepresentedClassifier(elem, clazz);
        Model.getPump().flushModelEvents();
        /* Simulate a target change. */
        model.targetSet(new TargetEvent(this, null, null, new Object[] {elem}));
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
     * Test setRepresentedClassifier().
     */
    public void testSetRepresentedOperation() {
        /* Now the model should contain 
         * the one operation + the "" for clearing. */
        assertEquals(2, model.getSize());
        assertEquals(clazz, model.getElementAt(0));
    }

    /**
     * Test removing the represented operation.
     */
    public void testExtraRepresentedOperation() {
	Object cl2 = Model.getCoreFactory().createClass();
        Model.getCollaborationsHelper().setRepresentedClassifier(elem, cl2); 
        /* Simulate a target change. */
        model.targetSet(new TargetEvent(this, null, null, new Object[] {elem}));
        Model.getPump().flushModelEvents();
        assertEquals(3, model.getSize());
    }

}
