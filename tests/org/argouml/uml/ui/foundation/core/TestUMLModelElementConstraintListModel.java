// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import org.argouml.model.InitializeModel;

import org.argouml.model.Model;

/**
 * @since Oct 27, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLModelElementConstraintListModel extends TestCase {

    /**
     * The number of elements used in the tests.
     */
    private static final int NO_OF_ELEMENTS = 10;

    /**
     * The element.
     */
    private Object elem;

    /**
     * The model to test.
     */
    private UMLModelElementConstraintListModel model;

    /**
     * The uml model / namespace where the elements reside.
     */
    private Object ns;

    /**
     * Constructor for TestUMLModelElementConstraintListModel.
     * @param arg0 is the name of the test case.
     */
    public TestUMLModelElementConstraintListModel(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        ns = Model.getModelManagementFactory().createModel();
        elem = Model.getCoreFactory().buildClass(ns);
        model = new UMLModelElementConstraintListModel();
        model.setTarget(elem);
        Model.getPump().flushModelEvents();
    }

    /*
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
        Object[] constraints = new Object[NO_OF_ELEMENTS];
        for (int i = 0; i < constraints.length; i++) {
            constraints[i] = Model.getCoreFactory().createConstraint();
            Model.getCoreHelper().addConstraint(elem, constraints[i]);
        }
        Model.getPump().flushModelEvents();
        assertEquals(NO_OF_ELEMENTS, model.getSize());
        assertEquals(
                model.getElementAt(NO_OF_ELEMENTS / 2),
                constraints[NO_OF_ELEMENTS / 2]);
        assertEquals(model.getElementAt(0), constraints[0]);
        assertEquals(
                model.getElementAt(NO_OF_ELEMENTS - 1),
                constraints[NO_OF_ELEMENTS - 1]);
    }

     /**
     * Test the removal of several elements from the list.
     */
    public void testRemoveMultiple() {
        Object[] constraints = new Object[NO_OF_ELEMENTS];
        for (int i = 0; i < constraints.length; i++) {
            constraints[i] = Model.getCoreFactory().createConstraint();
            Model.getCoreHelper().addConstraint(elem, constraints[i]);
        }
        for (int i = 0; i < NO_OF_ELEMENTS / 2; i++) {
            Model.getCoreHelper().removeConstraint(elem, constraints[i]);
        }
        Model.getPump().flushModelEvents();
        assertEquals(NO_OF_ELEMENTS - (NO_OF_ELEMENTS / 2), model.getSize());
        assertEquals(constraints[NO_OF_ELEMENTS / 2], model.getElementAt(0));
    }
}
