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

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.argouml.ui.targetmanager.TargetEvent;

import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.model_management.MModel;

/**
 * @since Oct 13, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLModelElementStereotypeComboBoxModel extends TestCase {

    private Object elem;
    private UMLModelElementStereotypeComboBoxModel model;
    private Object[] stereotypes;

    /**
     * Constructor for TestUMLAssociationRoleBaseComboBoxModel.
     * @param arg0 is the name of the test case.
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
        Object m = Model.getModelManagementFactory().createModel();
        p.setRoot(m);
        ModelFacade.setNamespace(elem, m);
        stereotypes = new MStereotype[10];
        Object theModel =
            ProjectManager.getManager().getCurrentProject().getModel();
        Collection models =
            ProjectManager.getManager().getCurrentProject().getModels();
        for (int i = 0; i < 10; i++) {
            stereotypes[i] =
                Model.getExtensionMechanismsFactory()
                	.buildStereotype(elem, "test" + i, theModel, models);
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
        ModelFacade.setStereotype(elem, stereotypes[0]);
        assertTrue(model.getSelectedItem() == stereotypes[0]);
    }

    /**
     * Test setStereotype() with null argument.
     */
    public void testSetBaseToNull() {
        ModelFacade.setStereotype(elem, null);
        assertNull(model.getSelectedItem());
    }

    /**
     * Test deletion.
     */
    public void testRemoveBase() {
        Model.getUmlFactory().delete(stereotypes[9]);
        assertTrue(!model.contains(stereotypes[9]));
    }
}
