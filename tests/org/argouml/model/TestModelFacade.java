// $Id$
// Copyright (c) 2003-2005 The Regents of the University of California. All
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

import java.lang.reflect.Method;
import java.util.Iterator;

import javax.jmi.reflect.RefBaseObject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.argouml.model.uml.Uml;

/**
 * Tests the accessor methods of the _facade.
 *
 * TestUmlObjectCreation tests the object creation SPI.
 *
 * @author Thierry Lach
 */
public class TestModelFacade extends TestCase {
    /**
     * The modelfacade tested.
     *
     * This is so that we can use reflection.
     */
    private ModelFacade facade = null;

    /**
     * Constructor for TestModelFacade.
     * @param arg0 name of test case
     */
    public TestModelFacade(String arg0) {
	super(arg0);
    }

    /**
     * @return the test suite
     */
    public static Test suite() {
	TestSuite suite =
	    new TestSuite("Tests for "
			  + TestModelFacade.class.getPackage().getName());

	Iterator i = Uml.getUmlClassList().iterator();
	while (i.hasNext()) {
            UmlModelEntity e = (UmlModelEntity) i.next();
	    suite.addTest(new TestModelFacade(e.getName()));
	}

	return suite;
    }

    /**
     * Test a specific element.
     * @see junit.framework.TestCase#runTest()
     */
    protected void runTest() throws Exception {
	String objectType = getName();
	UmlModelEntity umlClass = Uml.getDeclaredType(objectType);
	// Ensure that the type is part of Uml class.
	assertNotNull("Uml class does not know about '" + objectType + "'",
		      umlClass);

	Class[] classes = new Class[1];
	classes[0] = Object.class;
	Object[] args = new Object[1];

	Method methodIsA;
	try {
	    methodIsA =
	        ModelFacade.class.getDeclaredMethod("isA" + objectType,
	                			    classes);
	} catch (NoSuchMethodException e) {
	    return;
	}

	// Test with null
	args[0] = null;
	Boolean rc = (Boolean) methodIsA.invoke(facade, args);
	assertNotNull("isA" + objectType + " called with null", rc);
	assertTrue("isA" + objectType + " called with null",
	           !rc.booleanValue());

	// Test with an object
	args[0] = new Object();
	rc = null;
	rc = (Boolean) methodIsA.invoke(facade, args);
	assertNotNull("isA" + objectType + " called with new Object()", rc);
	assertTrue("isA" + objectType + " called with new Object()",
	           !rc.booleanValue());

	if (umlClass.isCreatable()) {
	    Object testObject = null;
	    // TODO: Make sure MultiplicityRange works properly -
	    // currently it does not
	    if (!"MultiplicityRange".equals(umlClass.getName())) {
	        // Test after creating the class using create()
	        // without proxy
	        Model.getUmlFactory().setJmiProxyCreated(false);
	        testObject = Model.getUmlFactory().create(umlClass);
	        assertNotNull("Unable to create '" + umlClass + "'",
	                      testObject);
	        args[0] = testObject;
	        rc = null;
	        rc = (Boolean) methodIsA.invoke(facade, args);
	        assertTrue("isA" + objectType
	                   + " did not work with legacy create",
	                   rc.booleanValue());
	    }

	    // TODO: Make sure ActionExpression and MultiplicityRange
	    // work properly - currently it does not
	    if ("ActionExpression".equals(umlClass.getName())
	            || "MultiplicityRange".equals(umlClass.getName())) {
	        return;
	    }

	    // Test after creating the class using create() with proxy
	    Model.getUmlFactory().setJmiProxyCreated(true);
	    testObject = Model.getUmlFactory().create(umlClass);
	    assertNotNull("Unable to create '" + umlClass + "'",
	            	  testObject);
	    args[0] = testObject;
	    rc = null;
	    rc = (Boolean) methodIsA.invoke(facade, args);
	    assertTrue("Not JMI interface",
	               testObject instanceof RefBaseObject);
	    assertTrue("isA" + objectType + " did not work with proxy create",
	               rc.booleanValue());
	    return;
	}

	// Test after creating the class using create() without proxy
	Model.getUmlFactory().setJmiProxyCreated(false);
	Object testObject = Model.getUmlFactory().create(umlClass);
	assertNull("Should not be able to create legacy '"
	        + umlClass + "'",
	        testObject);

	// Test after creating the class using create() with proxy
	Model.getUmlFactory().setJmiProxyCreated(true);
	testObject = Model.getUmlFactory().create(umlClass);
	assertNull("Should not be able to create new '"
	        + umlClass + "'",
	        testObject);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
	super.setUp();
	facade = new ModelFacade();
	assertNotNull("Cound not get ModelFacade", facade);
    }
}
