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
package org.argouml.uml.ui.behavior.collaborations;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.modelmanagement.ModelManagementFactory;
import org.argouml.ui.targetmanager.TargetEvent;

import ru.novosoft.uml.MFactoryImpl;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.model_management.MModel;

/**
 * @since Oct 30, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLAssociationRoleBaseComboBoxModel extends TestCase {

    private int oldEventPolicy;
    protected MAssociationRole elem;
    protected UMLAssociationRoleBaseComboBoxModel model;
    private MAssociation[] bases;
    
    /**
     * Constructor for TestUMLAssociationRoleBaseComboBoxModel.
     * @param arg0
     */
    public TestUMLAssociationRoleBaseComboBoxModel(String arg0) {
        super(arg0);
    }
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ArgoSecurityManager.getInstance().setAllowExit(true);
        UmlFactory.getFactory().setGuiEnabled(false);
        Project p = ProjectManager.getManager().getCurrentProject();
        model = new UMLAssociationRoleBaseComboBoxModel();
        MClass class1 = CoreFactory.getFactory().createClass();
        MClass class2 = CoreFactory.getFactory().createClass();
        MModel m = ModelManagementFactory.getFactory().createModel();
        p.setRoot(m);
        class1.setNamespace(m);
        class2.setNamespace(m);
        bases = new MAssociation[10];
        for (int i = 0; i < 10; i++) {
            bases[i] = CoreFactory.getFactory().buildAssociation(class1, class2);
        }
        MClassifierRole role1 = CollaborationsFactory.getFactory().createClassifierRole();
        MClassifierRole role2 = CollaborationsFactory.getFactory().createClassifierRole();
        role1.addBase(class1);
        role2.addBase(class2);
        MCollaboration col = CollaborationsFactory.getFactory().createCollaboration();
        role1.setNamespace(col);
        role2.setNamespace(col);
        elem = CollaborationsFactory.getFactory().buildAssociationRole(role1, role2);
        oldEventPolicy = MFactoryImpl.getEventPolicy();
        MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);   
        model.targetSet(new TargetEvent(this, "set", new Object[0], new Object[] {elem}));
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
        // there is one extra element due to the empty element that the user can select
        assertEquals(10 + 1, model.getSize());
        assertTrue(model.contains(bases[5]));
        assertTrue(model.contains(bases[0]));
        assertTrue(model.contains(bases[9]));
    }
    
    public void testSetBase() {
        elem.setBase(bases[0]);
        assertTrue(model.getSelectedItem() == bases[0]);
    }
    
    public void testSetBaseToNull() {
        elem.setBase(null);
        assertNull(model.getSelectedItem());
    }
    
    public void testRemoveBase() {
        UmlFactory.getFactory().delete(bases[9]);
        // there is one extra element since removal of the base is allowed.
        assertEquals(9 + 1, model.getSize());
        assertTrue(!model.contains(bases[9]));
    } 
        

}
