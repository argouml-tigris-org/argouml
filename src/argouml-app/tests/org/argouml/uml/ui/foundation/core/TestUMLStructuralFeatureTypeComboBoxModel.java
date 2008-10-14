// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.util.ThreadHelper;

/**
 * @since Nov 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLStructuralFeatureTypeComboBoxModel extends TestCase {

    /**
     * The number of elements in the test.
     */
    private static final int NO_OF_ELEMENTS = 10;

    /**
     * The list of elements.
     */
    private Object[] types;

    /**
     * The model tested.
     */
    private UMLStructuralFeatureTypeComboBoxModel model;

    /**
     * The element.
     */
    private Object elem;
    
    private Object dummy;

    /**
     * Constructor for TestUMLStructuralFeatureTypeComboBoxModel.
     * @param arg0 is the name of the test case.
     */
    public TestUMLStructuralFeatureTypeComboBoxModel(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
        Object mmodel =
            Model.getModelManagementFactory().createModel();
        Model.getCoreHelper().setName(mmodel, "untitledModel");
        elem = Model.getCoreFactory().createAttribute();
        dummy = Model.getCoreFactory().createAttribute();
        model = new UMLStructuralFeatureTypeComboBoxModel();
        model.targetSet(new TargetEvent(this, "set", new Object[0],
                new Object[] {elem}));
        types = new Object[NO_OF_ELEMENTS];
        Collection roots = new ArrayList();
        roots.add(mmodel);
        ProjectManager.getManager().getCurrentProject().setRoots(roots);
        Model.getCoreHelper().setNamespace(elem, mmodel);
        for (int i = 0; i < NO_OF_ELEMENTS; i++) {
            types[i] = Model.getCoreFactory().createClass();
            // give them unique names so they don't get merged
            Model.getCoreHelper().setName(types[i], "type" + i);
            Model.getCoreHelper().addOwnedElement(mmodel, types[i]);
        }
        Model.getCoreHelper().setType(elem, types[0]);
        ThreadHelper.synchronize();
    }

    /*
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Model.getUmlFactory().delete(elem);
        for (int i = 0; i < NO_OF_ELEMENTS; i++) {
            Model.getUmlFactory().delete(types[i]);
        }
        model = null;
    }

    /**
     * Test the test set up.
     */
    public void testSetUp() throws Exception {
        ThreadHelper.synchronize();
        // One can only do this by changing target,
        // so let's simulate that:
        changeTarget();
        model.buildModelList();
        assertTrue(model.contains(types[NO_OF_ELEMENTS / 2]));
        assertTrue(model.contains(types[0]));
        assertTrue(model.contains(types[NO_OF_ELEMENTS - 1]));
    }

    
    private void changeTarget() {
        model.targetSet(new TargetEvent(this, TargetEvent.TARGET_SET,
                new Object[] {elem}, new Object[] {dummy}));
        model.targetSet(new TargetEvent(this, TargetEvent.TARGET_SET,
                new Object[] {dummy}, new Object[] {elem}));
    }

    /**
     * Test the setType function.
     */
    public void testSetType() throws Exception {
        Model.getCoreHelper().setType(elem, types[0]);
        ThreadHelper.synchronize();
        changeTarget();
        assertTrue(model.getSelectedItem() == types[0]);
    }

    /**
     * this test does make a huge amount of sense because
     * the model cannot present null types. therefore until
     * the combobox model is changed itself, we test for
     * a not null value.
     */
    public void testSetTypeToNull() throws Exception {
        Model.getCoreHelper().setType(elem, types[0]);
        Model.getCoreHelper().setType(elem, null);
        ThreadHelper.synchronize();
        assertNotNull(model.getSelectedItem());
    }

    /**
     * The test for removing types.
     */
    public void testRemoveType() throws Exception {
        Model.getUmlFactory().delete(types[NO_OF_ELEMENTS - 1]);
        ThreadHelper.synchronize();
        assertTrue(!model.contains(types[NO_OF_ELEMENTS - 1]));
    }


}
