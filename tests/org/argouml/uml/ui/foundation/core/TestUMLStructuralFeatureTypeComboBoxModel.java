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

package org.argouml.uml.ui.foundation.core;

import junit.framework.TestCase;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;

/**
 * @since Nov 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLStructuralFeatureTypeComboBoxModel extends TestCase {

    private Object[] types;
    private UMLStructuralFeatureTypeComboBoxModel model;
    private Object elem;

    /**
     * Constructor for TestUMLStructuralFeatureTypeComboBoxModel.
     * @param arg0 is the name of the test case.
     */
    public TestUMLStructuralFeatureTypeComboBoxModel(String arg0) {
        super(arg0);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        //ProjectManager.getManager().getCurrentProject();
        Object mmodel =
            Model.getModelManagementFactory().createModel();
        Model.getCoreHelper().setName(mmodel, "untitledModel");
        Model.getModelManagementFactory().setRootModel(mmodel);
        //
        elem = Model.getCoreFactory().createAttribute();
        model = new UMLStructuralFeatureTypeComboBoxModel();
        model.targetSet(new TargetEvent(this, "set", new Object[0],
                new Object[] {elem}));
        types = new Object[10];
        Object m = Model.getModelManagementFactory().createModel();
        ProjectManager.getManager().getCurrentProject().setRoot(m);
        Model.getCoreHelper().setNamespace(elem, m);
        for (int i = 0; i < 10; i++) {
            types[i] = Model.getCoreFactory().createClass();
            Model.getCoreHelper().addOwnedElement(m, types[i]);
        }
        Model.getCoreHelper().setType(elem, types[0]);
        model.buildModelList();
        Model.getPump().reallyFlushModelEvents();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        Model.getUmlFactory().delete(elem);
        for (int i = 0; i < 10; i++) {
            Model.getUmlFactory().delete(types[i]);
        }
        model = null;
    }

    /**
     * Test the test set up.
     */
    public void testSetUp() {
        Model.getPump().reallyFlushModelEvents();
        assertTrue(model.contains(types[5]));
        assertTrue(model.contains(types[0]));
        assertTrue(model.contains(types[9]));
    }

    /**
     * Test the setType function.
     */
    public void testSetType() {
        Model.getCoreHelper().setType(elem, types[0]);
        Model.getPump().reallyFlushModelEvents();
        assertTrue(model.getSelectedItem() == types[0]);
    }

    /**
     * this test does make a huge amount of sense because
     * the model cannot present null types. therefore until
     * the combobox model is changed itself, we test for
     * a not null value.
     */
    public void testSetTypeToNull() {
        Model.getCoreHelper().setType(elem, types[0]);
        Model.getCoreHelper().setType(elem, null);
        Model.getPump().reallyFlushModelEvents();
        assertNotNull(model.getSelectedItem());
    }

    /**
     * The test for removing types.
     */
    public void testRemoveType() {
        Model.getUmlFactory().delete(types[9]);
        Model.getPump().reallyFlushModelEvents();
        assertTrue(!model.contains(types[9]));
    }


}
