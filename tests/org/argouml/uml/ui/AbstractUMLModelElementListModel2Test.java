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
package org.argouml.uml.ui;

import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.MFactoryImpl;

import junit.framework.TestCase;

/**
 * An abstract class that serves as a basis for testing listmodels. Only works
 * for listmodels that can contain multiple elements.
 * @since Oct 27, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class AbstractUMLModelElementListModel2Test extends TestCase {
    
    private int oldEventPolicy;
    protected MBase elem;
    protected UMLModelElementListModel2 model;

    /**
     * Constructor for AbstractUMLModelElementListModel2Test.
     * @param arg0
     */
    public AbstractUMLModelElementListModel2Test(String arg0) {
        super(arg0);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        buildElement();
        oldEventPolicy = MFactoryImpl.getEventPolicy();
        MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
        MockUMLUserInterfaceContainer cont = new MockUMLUserInterfaceContainer();
        cont.setTarget(elem);
        buildModel(cont);
    }

    /**
     * Developers should build the target element in this method and assing 
     * this to the variable elem. The target
     * element is the MBase of which the listmodel to be tested shows 
     * an attribute
     */
    protected abstract void buildElement();
    
    /**
     * Developers should construct the listmodel to be tested in this method and
     * assign this to the variable model.
     */
    protected abstract void buildModel(UMLUserInterfaceContainer cont);
    
    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        UmlFactory.getFactory().delete(elem);
        MFactoryImpl.setEventPolicy(oldEventPolicy);
        model = null;
    }
    
    /**
     * Tests the programmatically adding of multiple elements to the list.
     */
    public void testAddMultiple() {      
        MBase[] elements = fillModel();
        assertEquals(10, model.getSize());
        assertEquals(model.getElementAt(5), elements[5]);
        assertEquals(model.getElementAt(0), elements[0]);
        assertEquals(model.getElementAt(9), elements[9]);
    }
    
    /**
     * Developers should set the attribute that the listmodel shows in this 
     * method. They should return the contents of the attribute in the form of 
     * a MBase[]. The number of elements inside the attribute should be 
     * 10.
     * @return MBase[]
     */
    protected abstract MBase[] fillModel();
    
    /**
     * Test the removal of several elements from the list
     */
    public void testRemoveMultiple() {
        MBase[] elements = fillModel();
        removeHalfModel(elements);
        assertEquals(5, model.getSize());
        assertEquals(elements[5], model.getElementAt(0));
    }
    
    /**
     * Developers should remove half the contents of the attribute in this method
     * That is: they should remove the upper 5 elements of the attribute.
     * @param elements
     */
    protected abstract void removeHalfModel(MBase[] elements);

}
