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

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.modelmanagement.ModelManagementFactory;

import ru.novosoft.uml.MFactoryImpl;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.model_management.MModel;

/**
 * @since Oct 26, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLModelElementClientDependencyListModel extends TestCase {

    private MModelElement elem = null;
    private int oldEventPolicy;
    private UMLModelElementClientDependencyListModel model;
    private MModel ns;
    
    /**
     * Constructor for TestUMLModelElementClientDependencyListModel.
     * @param arg0
     */
    public TestUMLModelElementClientDependencyListModel(String arg0) {
        super(arg0);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ArgoSecurityManager.getInstance().setAllowExit(true);
        ns = ModelManagementFactory.getFactory().createModel();
        elem = CoreFactory.getFactory().buildClass(ns);
        oldEventPolicy = MFactoryImpl.getEventPolicy();
        MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
        model = new UMLModelElementClientDependencyListModel();
        elem.addMElementListener(model);
        model.setTarget(elem);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        UmlFactory.getFactory().delete(elem);
        UmlFactory.getFactory().delete(ns);
        MFactoryImpl.setEventPolicy(oldEventPolicy);
        model = null;
    }
    
    /**
     * Tests the programmatically adding of multiple elements to the list.
     */
    public void testAddMultiple() {      
        MModelElement[] suppliers = new MModelElement[10];
        MDependency[] dependencies = new MDependency[10];
        for (int i = 0; i < 10; i++) {
            suppliers[i] = CoreFactory.getFactory().buildClass(ns);
            dependencies[i] = CoreFactory.getFactory().buildDependency(elem, suppliers[i]);
        }
        assertEquals(10, model.getSize());
        assertEquals(model.getElementAt(5), dependencies[5]);
        assertEquals(model.getElementAt(0), dependencies[0]);
        assertEquals(model.getElementAt(9), dependencies[9]);
    }
    
    /**
     * Test the removal of several elements from the list
     */
    public void testRemoveMultiple() {
        MModelElement[] suppliers = new MModelElement[10];
        MDependency[] dependencies = new MDependency[10];
        for (int i = 0; i < 10; i++) {
            suppliers[i] = CoreFactory.getFactory().buildClass(ns);
            dependencies[i] = CoreFactory.getFactory().buildDependency(elem, suppliers[i]);
        }
        for (int i = 0; i < 5; i++) {
            elem.removeClientDependency(dependencies[i]);
        }
        assertEquals(5, model.getSize());
        assertEquals(dependencies[5], model.getElementAt(0));
    }
}
