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

import org.argouml.model.Model;

/**
 * @since Oct 26, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLModelElementClientDependencyListModel extends TestCase {

    private Object elem = null;
    private UMLModelElementClientDependencyListModel model;
    private Object ns;

    /**
     * Constructor for TestUMLModelElementClientDependencyListModel.
     * @param arg0 is the name of the test case.
     */
    public TestUMLModelElementClientDependencyListModel(String arg0) {
        super(arg0);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ns = Model.getModelManagementFactory().createModel();
        elem = Model.getCoreFactory().buildClass(ns);
        model = new UMLModelElementClientDependencyListModel();
        model.setTarget(elem);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        Model.getUmlFactory().delete(elem);
        Model.getUmlFactory().delete(ns);
        model = null;
    }

    /**
     * Tests the programmatically adding of multiple elements to the list.
     */
    public void testAddMultiple() {
        Object[] suppliers = new Object[10];
        Object[] dependencies = new Object[10];
        for (int i = 0; i < 10; i++) {
            suppliers[i] = Model.getCoreFactory().buildClass(ns);
            dependencies[i] =
                Model.getCoreFactory().buildDependency(elem, suppliers[i]);
        }
        assertEquals(10, model.getSize());
        assertEquals(model.getElementAt(5), dependencies[5]);
        assertEquals(model.getElementAt(0), dependencies[0]);
        assertEquals(model.getElementAt(9), dependencies[9]);
    }

    /**
     * Test the removal of several elements from the list.
     */
    public void testRemoveMultiple() {
        Object[] suppliers = new Object[10];
        Object[] dependencies = new Object[10];
        for (int i = 0; i < 10; i++) {
            suppliers[i] = Model.getCoreFactory().buildClass(ns);
            dependencies[i] =
                Model.getCoreFactory().buildDependency(elem, suppliers[i]);
        }
        for (int i = 0; i < 5; i++) {
            Model.getCoreHelper().removeClientDependency(elem, dependencies[i]);
        }
        assertEquals(5, model.getSize());
        assertEquals(dependencies[5], model.getElementAt(0));
    }
}
