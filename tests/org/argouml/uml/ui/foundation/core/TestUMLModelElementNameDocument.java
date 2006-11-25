// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import javax.swing.text.BadLocationException;

import junit.framework.TestCase;

import org.argouml.model.Model;
import org.argouml.uml.ui.MockUMLUserInterfaceContainer;

/**
 * @since Oct 26, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLModelElementNameDocument extends TestCase {

    /**
     * The element.
     */
    private Object elem;

    /**
     * The model to test.
     */
    private UMLModelElementNameDocument model;

    /**
     * The uml model / namespace the element reside in.
     */
    private Object ns;

    /**
     * Constructor for TestUMLModelElementNameDocument.
     * @param arg0 is the name of the test case.
     */
    public TestUMLModelElementNameDocument(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ns = Model.getModelManagementFactory().createModel();
        elem = Model.getCoreFactory().buildClass(ns);
        MockUMLUserInterfaceContainer cont =
            new MockUMLUserInterfaceContainer();
        //cont.setTarget(elem);
        model = new UMLModelElementNameDocument();
        model.setTarget(elem);
        Model.getPump().flushModelEvents();
    }

    /*
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        Model.getUmlFactory().delete(ns);
        Model.getUmlFactory().delete(elem);
        elem = null;
        ns = null;
        model = null;
    }

    /**
     * Test setName().
     *
     * @throws BadLocationException when the location is refused
     */
    public void testSetName() throws BadLocationException {
        Model.getCoreHelper().setName(elem, "test");
        Model.getPump().flushModelEvents();
        assertEquals("test", model.getText(0, model.getLength()));
    }

    /**
     * Test setName() for removal of a name.
     *
     * @throws BadLocationException when the location is refused
     */
    public void testRemoveName() throws BadLocationException {
        Model.getCoreHelper().setName(elem, "test");
        Model.getCoreHelper().setName(elem, "");
        Model.getPump().flushModelEvents();
        assertEquals("", model.getText(0, model.getLength()));
    }

    /**
     * Test insertString().
     *
     * @throws BadLocationException when the location is refused
     */
    public void testInsertString()
	throws BadLocationException {
        Model.getCoreHelper().setName(elem, "");
        Model.getPump().flushModelEvents();
    	model.insertString(0, "test", null);
        Model.getPump().flushModelEvents();
        assertEquals("test", Model.getFacade().getName(elem));
    }

    /**
     * Test remove().
     *
     * @throws BadLocationException when the location is refused
     */
    public void testRemoveString()
	throws BadLocationException {
	model.insertString(0, "test", null);
        Model.getPump().flushModelEvents();
	model.remove(0, model.getLength());
        Model.getPump().flushModelEvents();
        assertEquals("", Model.getFacade().getName(elem));
    }

    /**
     * Test insertString() for appending.
     *
     * @throws BadLocationException when the location is refused
     */
    public void testAppendString()
	throws BadLocationException {
        Model.getCoreHelper().setName(elem, "test");
        Model.getPump().flushModelEvents();
    	model.insertString(model.getLength(), "test", null);
        Model.getPump().flushModelEvents();
        assertEquals("testtest", Model.getFacade().getName(elem));
    }

    /**
     * Test insertString() for inserting in the middle.
     *
     * @throws BadLocationException when the location is refused
     */
    public void testInsertStringHalfway()
	throws BadLocationException {
        Model.getCoreHelper().setName(elem, "test");
        Model.getPump().flushModelEvents();
    	model.insertString(1, "test", null);
        Model.getPump().flushModelEvents();
        assertEquals("ttestest", Model.getFacade().getName(elem));
    }

    /**
     * Test removing a string from the middle.
     *
     * @throws BadLocationException when the location is refused
     */
    public void testRemoveStringHalfway()
	throws BadLocationException {
        Model.getCoreHelper().setName(elem, "test");
        Model.getPump().flushModelEvents();
    	model.remove(1, model.getLength() - 2);
        Model.getPump().flushModelEvents();
        assertEquals("tt", Model.getFacade().getName(elem));
    }
}
