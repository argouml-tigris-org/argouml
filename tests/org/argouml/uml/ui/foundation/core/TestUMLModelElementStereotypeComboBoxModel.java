// Copyright (c) 1996-99 The Regents of the University of California. All
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

// $header$
package org.argouml.uml.ui.foundation.core;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsFactory;
import org.argouml.model.uml.modelmanagement.ModelManagementFactory;

import junit.framework.TestCase;

import ru.novosoft.uml.MFactoryImpl;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassImpl;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotypeImpl;
import ru.novosoft.uml.model_management.MModel;

/**
 * @since Oct 13, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLModelElementStereotypeComboBoxModel extends TestCase {

       private int oldEventPolicy;
    protected MClass elem;
    protected UMLModelElementStereotypeComboBoxModel model;
    private MStereotype[] stereotypes;
    
    /**
     * Constructor for TestUMLAssociationRoleBaseComboBoxModel.
     * @param arg0
     */
    public TestUMLModelElementStereotypeComboBoxModel(String arg0) {
        super(arg0);
    }
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ArgoSecurityManager.getInstance().setAllowExit(true);
        Project p = ProjectManager.getManager().getCurrentProject();
        model = new UMLModelElementStereotypeComboBoxModel();
        elem = CoreFactory.getFactory().createClass();
        MModel m = ModelManagementFactory.getFactory().createModel();
        p.setRoot(m);
        elem.setNamespace(m);       
        stereotypes = new MStereotype[10];
        for (int i = 0; i < 10; i++) {
            stereotypes[i] = ExtensionMechanismsFactory.getFactory().buildStereotype(elem, "test" + i);
        }
        oldEventPolicy = MFactoryImpl.getEventPolicy();
        MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);   
        model.targetChanged(elem);
    }
    
    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        UmlFactory.getFactory().delete(elem);
        MFactoryImpl.setEventPolicy(oldEventPolicy);
        model = null;
    }
    
    public void testSetUp() {
        assertTrue(model.contains(stereotypes[5]));
        assertTrue(model.contains(stereotypes[0]));
        assertTrue(model.contains(stereotypes[9]));
    }
    
    public void testSetBase() {
        elem.setStereotype(stereotypes[0]);
        assertTrue(model.getSelectedItem() == stereotypes[0]);
    }
    
    public void testSetBaseToNull() {
        elem.setStereotype(null);
        assertNull(model.getSelectedItem());
    }
    
    public void testRemoveBase() {
        UmlFactory.getFactory().delete(stereotypes[9]);
        assertTrue(!model.contains(stereotypes[9]));
    } 
}
