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

import java.lang.reflect.Method;

import org.argouml.model.uml.Uml;

import junit.framework.TestCase;

/**
 * @author Thierry Lach
 */
public class GenericFacadeTestFixture extends TestCase
{

	/**
	 * Constructor for TestModelFacade.
	 * @param arg0
	 */
	public GenericFacadeTestFixture(String arg0)
	{
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

    protected void runGenericTests(String objectType) {

		// Ensure that the type is part of Uml class.    	
    	assertTrue("find " + objectType, Uml.getUmlClassList().containsKey(objectType));

        Class[] classes = new Class[1];
        classes[0] = Object.class;
		Object[] args = new Object[1];
		ModelFacade facade = ModelFacade.getFacade();
        
		Method methodIsA = null;
		Boolean rc = null;
        try {
			methodIsA = ModelFacade.class.getDeclaredMethod("isA" + objectType, classes);

			args[0] = null;
			rc = null;
			rc = (Boolean)methodIsA.invoke(facade, args);
			assertNotNull("isA" + objectType + " called with null", rc);
			assertTrue("isA" + objectType + " called with null", ! rc.booleanValue());

			args[0] = new Object();
			rc = null;
			rc = (Boolean)methodIsA.invoke(facade, args);
			assertNotNull("isA" + objectType + " called with new Object()", rc);
			assertTrue("isA" + objectType + " called with new Object()", ! rc.booleanValue());

		}
        catch (Exception e) {
			fail("Cannot execute ModelFacade.isA" + objectType + " because of " + e);
		}
    }

}
