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
import org.argouml.model.ModelFacade;
import org.argouml.ui.targetmanager.TargetEvent;

/**
 * @since Nov 1, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLIncludeAdditionComboBoxModel extends TestCase {

    private Object[] additions;
    private UMLIncludeAdditionComboBoxModel model;
    private Object elem;

    /**
     * Constructor for TestUMLIncludeAdditionComboBoxModel.
     * @param arg0 is the name of the test case.
     */
    public TestUMLIncludeAdditionComboBoxModel(String arg0) {
        super(arg0);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        elem = Model.getUseCasesFactory().createInclude();
        model = new UMLIncludeAdditionComboBoxModel();
        model.targetSet(new TargetEvent(this, "set", new Object[0],
                new Object[] {elem}));
        additions = new Object[10];
        Object m = Model.getModelManagementFactory().createModel();
        ProjectManager.getManager().getCurrentProject().setRoot(m);
        ModelFacade.setNamespace(elem, m);
        for (int i = 0; i < 10; i++) {
            additions[i] = Model.getUseCasesFactory().createUseCase();
            ModelFacade.addOwnedElement(m, additions[i]);
        }
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        Model.getUmlFactory().delete(elem);
        for (int i = 0; i < 10; i++) {
            Model.getUmlFactory().delete(additions[i]);
        }
        model = null;
    }

    /**
     * Test setup.
     */
    public void testSetUp() {
        assertEquals(10, model.getSize());
        assertTrue(model.contains(additions[5]));
        assertTrue(model.contains(additions[0]));
        assertTrue(model.contains(additions[9]));
    }

    /**
     * Test setBase().
     */
    public void testSetBase() {
        // NSUML has base and addition for includes mixed up. We mix it back.
        // Issue 2034.
        // The following line was:
        // ((MInclude) elem).setBase((MUseCase) additions[0]);
        ModelFacade.setAddition(elem, additions[0]);
        assertTrue(model.getSelectedItem() == additions[0]);
    }

    /**
     * Test setAddition() with null argument.
     */
    public void testSetBaseToNull() {
        ModelFacade.setAddition(elem, null);
        assertNull(model.getSelectedItem());
    }

    /**
     * Test deletion.
     */
    public void testRemoveBase() {
        Model.getUmlFactory().delete(additions[9]);
        assertEquals(9, model.getSize());
        assertTrue(!model.contains(additions[9]));
    }
}
