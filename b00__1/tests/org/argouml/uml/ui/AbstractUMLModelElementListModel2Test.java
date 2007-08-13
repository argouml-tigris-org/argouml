// $Id:AbstractUMLModelElementListModel2Test.java 12483 2007-05-02 20:20:37Z linus $
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

package org.argouml.uml.ui;

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;

import org.argouml.model.Model;

/**
 * An abstract class that serves as a basis for testing listmodels. Only works
 * for listmodels that can contain multiple elements.
 * @since Oct 27, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class AbstractUMLModelElementListModel2Test extends TestCase {
    private Object elem;
    private UMLModelElementListModel2 model;

    /**
     * Constructor for AbstractUMLModelElementListModel2Test.
     *
     * @param arg0 is the name of the test case.
     */
    public AbstractUMLModelElementListModel2Test(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        buildElement();
        // Tests used to be coded to assume immediate event delivery.
        // They've been modified to use flush() where needed. - tfm
        //oldEventPolicy = MFactoryImpl.getEventPolicy();
        //MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
        buildModel();
        model.setTarget(elem);
        Model.getPump().flushModelEvents();
    }

    /**
     * Developers should build the target element in this method and assing
     * this to the variable elem. The target
     * element is the ModelElement of which the listmodel to be tested shows
     * an attribute
     */
    protected abstract void buildElement();

    /**
     * Developers should construct the listmodel to be tested in this method and
     * assign this to the variable model.
     */
    protected abstract void buildModel();

    /*
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        Model.getUmlFactory().delete(elem);
        // restore original event policy - not supported by MDR - tfm
        //MFactoryImpl.setEventPolicy(oldEventPolicy);
        model = null;
    }

    /**
     * Tests the programmatically adding of multiple elements to the list.
     */
    public void testAddMultiple() {
        Object[] elements = fillModel();
        Model.getPump().flushModelEvents();
        assertEquals(10, model.getSize());
        assertEquals(model.getElementAt(5), elements[5]);
        assertEquals(model.getElementAt(0), elements[0]);
        assertEquals(model.getElementAt(9), elements[9]);
    }

    /**
     * Developers should set the attribute that the listmodel shows in this
     * method. They should return the contents of the attribute in the form of
     * a ModelElement[]. The number of elements inside the attribute should be
     * 10.
     * @return ModelElement[]
     */
    protected abstract Object[] fillModel();

    /**
     * Test the removal of several elements from the list.
     */
    public void testRemoveMultiple() {
        Object[] elements = fillModel();
        Model.getPump().flushModelEvents();
        removeHalfModel(elements);
        Model.getPump().flushModelEvents();
        assertEquals(5, model.getSize());
        assertEquals(elements[5], model.getElementAt(0));
    }

    /**
     * Developers should remove half the contents of the attribute in
     * this method That is: they should remove the upper 5 elements of
     * the attribute.
     *
     * @param elements the element
     */
    protected abstract void removeHalfModel(Object[] elements);

    /**
     * @param theModel The model to set.
     */
    protected void setModel(UMLModelElementListModel2 theModel) {
        model = theModel;
    }

    /**
     * @return Returns the model.
     */
    protected UMLModelElementListModel2 getModel() {
        return model;
    }

    /**
     * @param theElement The elem to set.
     */
    protected void setElem(Object theElement) {
        elem = theElement;
    }

    /**
     * @return Returns the elem.
     */
    protected Object getElem() {
        return elem;
    }

}
