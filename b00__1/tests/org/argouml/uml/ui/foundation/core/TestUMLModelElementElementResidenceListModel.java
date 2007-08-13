// $Id:TestUMLModelElementElementResidenceListModel.java 12483 2007-05-02 20:20:37Z linus $
// Copyright (c) 2002-2007 The Regents of the University of California. All
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
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLModelElementElementResidenceListModel extends TestCase {

    /**
     * The element.
     */
    private Object elem;

    /**
     * The model that we test.
     */
    private UMLModelElementElementResidenceListModel list;

    /**
     * Constructor for TestUMLModelElementElementResidenceListModel.
     * @param arg0 is the name of the test case.
     */
    public TestUMLModelElementElementResidenceListModel(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        elem =
            Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getUMLClass());
        list = new UMLModelElementElementResidenceListModel();
        list.setTarget(elem);
        Model.getPump().addModelEventListener(list, elem);
        Model.getPump().flushModelEvents();
    }

    /*
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
//        elem.remove();
//        Model.getUmlFactory().delete(elem);
        list = null;
    }

    /**
     * Test addElementResidence().
     */
    public void testElementAdded() {
        Object res = Model.getCoreFactory().createElementResidence();
        Model.getCoreHelper().addElementResidence(elem, res);
        Model.getPump().flushModelEvents();
        assertTrue(list.getSize() == 1);
        assertTrue(list.getElementAt(0) == res);
    }

    /**
     * Test removeElementResidence().
     */
    public void testElementRemoved() {
        Object res = Model.getCoreFactory().createElementResidence();
        Model.getCoreHelper().addElementResidence(elem, res);
        Model.getPump().flushModelEvents();
        assertTrue(list.getSize() == 1);
        assertTrue(list.getElementAt(0) == res);
        Model.getCoreHelper().removeElementResidence(elem, res);
        Model.getPump().flushModelEvents();
        assertTrue(list.getSize() == 0);
    }

    /**
     * Test getting an element when there is none.
     */
    public void testNoElements() {
        Model.getPump().flushModelEvents();
        try {
            list.getElementAt(0);
            fail();
        } catch (ArrayIndexOutOfBoundsException a) {
            // The correct exception is thrown.
        }
        assertTrue(list.size() == 0);
        assertTrue(Model.getFacade().getElementResidences(elem).isEmpty());
    }



}
