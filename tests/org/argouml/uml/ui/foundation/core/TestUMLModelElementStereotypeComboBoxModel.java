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

import java.util.Collection;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.UUIDManager;
import org.argouml.ui.targetmanager.TargetEvent;

/**
 * @since Oct 13, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLModelElementStereotypeComboBoxModel extends TestCase {

    private Object elem;

    private UMLModelElementStereotypeComboBoxModel model;

    private Object[] stereotypes;

    private Object umlModel;

    private static final Logger LOG = Logger.
        getLogger(TestUMLModelElementStereotypeComboBoxModel.class);

    /**
     * Constructor for TestUMLAssociationRoleBaseComboBoxModel.
     * 
     * @param arg0
     *            is the name of the test case.
     */
    public TestUMLModelElementStereotypeComboBoxModel(String arg0) {
        super(arg0);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        Project p = ProjectManager.getManager().getCurrentProject();
        model = new UMLModelElementStereotypeComboBoxModel();
        elem = Model.getCoreFactory().createClass();
        umlModel = Model.getModelManagementFactory().createModel();
        p.setRoot(umlModel);
        Model.getCoreHelper().setNamespace(elem, umlModel);
        stereotypes = new Object[10];
        Object theModel = ProjectManager.getManager().getCurrentProject()
                .getModel();
        Collection models = ProjectManager.getManager().getCurrentProject()
                .getModels();
        
        for (int i = 0; i < 10; i++) {
            stereotypes[i] = Model.getExtensionMechanismsFactory()
                    .buildStereotype(elem, "test" + i, theModel, models);
        }
        
        model.targetSet(new TargetEvent(this, "set", new Object[0],
                new Object[] { elem }));
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
     * Test setup.
     */
    public void testSetUp() {
        assertTrue(model.contains(stereotypes[5]));
        assertTrue(model.contains(stereotypes[0]));
        assertTrue(model.contains(stereotypes[9]));
    }

    /**
     * Test setStereotype().
     */
    public void testSetBase() {
        Model.getCoreHelper().setStereotype(elem, stereotypes[0]);
        assertEquals(stereotypes[0], model.getSelectedItem());
        Model.getCoreHelper().setStereotype(elem, stereotypes[1]);
        assertEquals(stereotypes[1], model.getSelectedItem());
    }

    /**
     * Test setStereotype() with null argument.
     */
    public void testSetBaseToNull() {
        Model.getCoreHelper().setStereotype(elem, stereotypes[0]);
        assertEquals(stereotypes[0], model.getSelectedItem());
        Model.getCoreHelper().setStereotype(elem, null);
        assertEquals(0, Model.getFacade().getStereotypes(elem).size());
        assertEquals(null, model.getSelectedItem());
    }

    /**
     * Test deletion.
     */
    public void testRemoveBase() {
        Model.getUmlFactory().delete(stereotypes[9]);
        assertTrue(!model.contains(stereotypes[9]));
    }

    /**
     * Tests if stereotypes contained in packages (non top level) are available
     * for selection, for a model element for which these are valid, such as a
     * stereotype for a classifier, contained in the same package as the class
     * to which it would be applied.
     * 
     * @author euluis
     * @since 2005-06-03
     */
    public void testStereotypesContainedInPackagesAreAvailable4Selection() {
        // create a package within the model, which will contain a class and
        // a stereotype
        Object pack = Model.getModelManagementFactory().buildPackage("pack",
                UUIDManager.getInstance().getNewUUID());
        Model.getCoreHelper().setNamespace(pack, umlModel);

        Object theClass = Model.getCoreFactory().buildClass("TheClass", pack);

        Object theStereotype = Model.getExtensionMechanismsFactory()
                .buildStereotype(theClass, "containedStereotype", pack);
        
        // now, lets check if the stereotype is found in the model to be 
        // applied to the class it is already applied (yeah, this is failing!)
        model.targetSet(new TargetEvent(this, TargetEvent.TARGET_SET, 
                new Object[0], new Object[] { theClass }));
        assertTrue(model.contains(theStereotype));
    }
}