// $Id$
// Copyright (c) 2003-2007 The Regents of the University of California. All
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

package org.argouml.model;

import java.util.Collection;

import junit.framework.TestCase;

/**
 * Tests some specific methods in Facade.<p>
 *
 * This is a complement to the tests in
 * {@link TestModelFacade3} that makes a lot more general tests.<p>
 *
 * As opposed to the tests in
 * {@link TestModelFacade3} that are run on a whole set of objects,
 * these tests are maintained manually.
 *
 * @author Linus Tolke
 */
public class TestModelFacade2 extends TestCase {

    /**
     * Constructor for TestModelFacade2.
     *
     * @param arg0 name of test case
     */
    public TestModelFacade2(String arg0) {
	super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
        InitializeModel.initializeDefault();
    }

    /**
     * Test that the correct error is thrown for isAsynchronous.
     */
    public void testErrorThrownInIsAsynchronous() {
	try {
	    Model.getFacade().isAsynchronous(new Object());
	    assertTrue("Error was not thrown", false);
	} catch (IllegalArgumentException e) {
	    // We expected an error to be thrown.
	}
    }

    /**
     * Test that the correct error is thrown for a setName with illegal name.
     */
    public void testSetName() {
        Object ob = Model.getCoreFactory().buildClass("initial");
        final String correctValue = "correct";
        Model.getCoreHelper().setName(ob, correctValue);
        assertEquals(correctValue, Model.getFacade().getName(ob));
    }


    /**
     * Test for setModelElementContainer.
     */
    public void testSetModelElementContainer() {
	Object container =
	    Model.getActivityGraphsFactory().createActivityGraph();
	Object partition = Model.getActivityGraphsFactory().createPartition();

	Model.getCoreHelper().setModelElementContainer(partition, container);

	Collection collection = Model.getFacade().getPartitions(container);
	assertTrue(collection.contains(partition));
        assertTrue(container.equals(Model.getFacade().getModelElementContainer(
                partition)));
    }

    /**
     * Test getModelElementContainer.
     */
    public void testGetModelElementContainer() {
        StateMachinesFactory factory = Model.getStateMachinesFactory();
        StateMachinesHelper helper = Model.getStateMachinesHelper();

        Object stateMachine = factory.createStateMachine();
        Object state = factory.createSimpleState();
        Object action = Model.getCommonBehaviorFactory().createCallAction();
        helper.setStateMachine(state, stateMachine);
        helper.setEntry(state, action);

        Object parentComposite =
            Model.getFacade().getModelElementContainer(action);
        assertTrue(state.equals(parentComposite));
        assertTrue(stateMachine.equals(Model.getFacade()
                .getModelElementContainer(parentComposite)));
    }


    /**
     * Test some Tagged Value functions.
     */
    private static final String TAG_TYPE = "fooTagType";
    private static final String TAG_VALUE = "fooTagValue";
    
    public void testTaggedValue() {
        Model.getModelManagementFactory().setRootModel(
                Model.getModelManagementFactory().createModel());
        Object cls = Model.getCoreFactory().buildClass();

	assertNull(Model.getFacade().getTaggedValue(cls, TAG_TYPE));
        Model.getExtensionMechanismsHelper().addTaggedValue(
                cls,
                Model.getExtensionMechanismsFactory().buildTaggedValue(
                        TAG_TYPE, TAG_VALUE));
        Object taggedValue = Model.getFacade().getTaggedValue(cls, TAG_TYPE);
        assertEquals(Model.getFacade().getValueOfTag(taggedValue), TAG_VALUE);
        Model.getExtensionMechanismsHelper()
                .removeTaggedValue(cls, taggedValue);
        Object unusedTaggedValue = Model.getExtensionMechanismsFactory()
                .buildTaggedValue("xxx", "yyy");
        Model.getExtensionMechanismsHelper().removeTaggedValue(cls,
                unusedTaggedValue);
        assertNull(Model.getFacade().getTaggedValue(cls, TAG_TYPE));
    }

    /**
     * Test the stereotypes.
     */
    public void testGetStereotypes() {
        Object cls = Model.getCoreFactory().buildClass();
        Model.getCoreHelper().setNamespace(cls,
        		Model.getModelManagementFactory().createPackage());
        Collection coll1 = Model.getFacade().getStereotypes(cls);
        assertEquals(0, coll1.size());

        Object stereotype =
            Model.getExtensionMechanismsFactory().buildStereotype(
                    "TestStereotype",
                    Model.getFacade().getNamespace(cls));

        Model.getCoreHelper().addStereotype(cls, stereotype);

        Collection coll2 = Model.getFacade().getStereotypes(cls);

        assertEquals(1, coll2.size());
        assertTrue(coll2.contains(stereotype));
    }
}
