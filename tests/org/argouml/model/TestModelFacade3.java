// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

import junit.framework.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;


/**
 * This TestCase dynamically generates a suite of tests to throw
 * arguments at each method in the ModelFacade to check whether they
 * throw an IllegalArgumentException.
 */
public class TestModelFacade3 extends TestCase {
    
    private Method _methodToTest = null;
    private ModelFacade _facade = null;

    /**
     * Creates one of the test cases.
     *
     * @param method to be tested.
     */
    public TestModelFacade3(Method method) {
	super(method.toString());
	_methodToTest = method;
    }
 
    public static Test suite() {
	TestSuite suite = 
	    new TestSuite("Tests for " 
			  + TestModelFacade.class.getPackage().getName());

        Method methods[] = ModelFacade.class.getDeclaredMethods();
	for (int i = 0; i < methods.length; i++) {
            suite.addTest(new TestModelFacade3(methods[i]));
	}

	return suite;
    }

    /** This method checks whether a test should be executed for a given
     * method.
     *
     * @return true if method is not eligible for testing (e.g. helper methods)
     */
    protected boolean methodForbidden() {
	return _methodToTest.getName().equals("getClassNull") ||
	    _methodToTest.getName().startsWith("isA");
    }
        
    protected void runTest() throws Throwable {
	if (!methodForbidden()) {
	    if (_methodToTest.getParameterTypes() != null &&
		_methodToTest.getParameterTypes().length >= 1 &&
		Modifier.isPublic(_methodToTest.getModifiers()) ) {
		testOneOrMoreParameters();
	    }
	}
    }
        
    /** 
     * testOneOrMoreParameters checks whether a public method in the
     * ModelFacade throws an IllegalArgumentException when invoked
     * with stupid arguments. Stupid arguments are arguments which are
     * not from the UML domain, such as a plain Object.
     *
     * @throws IllegalAccessException if there is an error in the test setup
     */
    public void testOneOrMoreParameters() throws IllegalAccessException {
	Object[] foo = new Object[_methodToTest.getParameterTypes().length];
           
	try {
	    _methodToTest.invoke(_facade, foo);
	    fail(_methodToTest.getName()
		 + " does not deliver an IllegalArgumentException");
	}
	catch (InvocationTargetException e) {
	    if (e.getTargetException() instanceof IllegalArgumentException) {
		return;
	    }
	    fail("Test failed for " + _methodToTest.getName()
		 + " because of: " + e.getTargetException());
	}
    }
        
    protected void setUp() throws Exception {
	super.setUp();
	_facade = new ModelFacade();
	assertNotNull("Cound not get ModelFacade", _facade);
    }
}
