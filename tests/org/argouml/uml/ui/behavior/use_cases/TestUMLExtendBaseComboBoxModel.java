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

package org.argouml.uml.ui.behavior.use_cases;

import junit.framework.TestCase;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;

/**
 * @since Oct 31, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLExtendBaseComboBoxModel extends TestCase {

    private Object[] bases;
    private UMLExtendBaseComboBoxModel model;
    private Object elem;

    /**
     * Constructor for TestUMLExtendBaseComboBoxModel.
     * @param arg0 is the name of the test case.
     */
    public TestUMLExtendBaseComboBoxModel(String arg0) {
        super(arg0);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        Object mmodel =
            Model.getModelManagementFactory().createModel();
        Model.getCoreHelper().setName(mmodel, "untitledModel");
        Model.getModelManagementFactory().setRootModel(mmodel);
        elem = Model.getUseCasesFactory().createExtend();
        model = new UMLExtendBaseComboBoxModel();
        bases = new Object[10];
        Object m = Model.getModelManagementFactory().createModel();
        ProjectManager.getManager().getCurrentProject().setRoot(m);
        for (int i = 0; i < 10; i++) {
            bases[i] = Model.getUseCasesFactory().createUseCase();
            Model.getCoreHelper().addOwnedElement(m, bases[i]);
            Model.getPump().addModelEventListener(model, bases[i]);
        }
        model.targetSet(new TargetEvent(this, "set", new Object[0],
                new Object[] {elem}));
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        Model.getUmlFactory().delete(elem);
        for (int i = 0; i < 10; i++) {
            Model.getUmlFactory().delete(bases[i]);
        }
        model = null;
    }

    /**
     * Test setup.
     */
    public void testSetUp() {
        assertEquals(10, model.getSize());
        assertTrue(model.contains(bases[5]));
        assertTrue(model.contains(bases[0]));
        assertTrue(model.contains(bases[9]));
    }

    /**
     * Test setBase().
     */
    public void testSetBase() {
        Model.getUseCasesHelper().setBase(elem, bases[0]);
        assertTrue(model.getSelectedItem() == bases[0]);
    }

    /**
     * Test to make sure we get an exception if trying to set
     * to null.
     */
    public void testSetBaseToNull() {
        Model.getUseCasesHelper().setBase(elem, bases[0]);
        boolean exceptionCaught = false;
        try {
            Model.getUseCasesHelper().setBase(elem, null);
        } catch (IllegalArgumentException e) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
    }

    /**
     * Test removing a Base.
     */
    public void testRemoveBase() {
        Model.getUmlFactory().delete(bases[9]);
        assertEquals(9, model.getSize());
        assertTrue(!model.contains(bases[9]));
    }
}
