// $Id$
// Copyright (c) 2004 The Regents of the University of California. All
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
import java.lang.reflect.Modifier;

import junit.framework.TestCase;

/**
 * Test that the Model class returns only NSUML-free interfaces.
 */
public class TestModel extends TestCase {
    /**
     * @param arg0 The name of the test case.
     */
    public TestModel(String arg0) {
        super(arg0);
    }
    
    /**
     * Test each of the interfaces returned so that they don't contain any 
     * NSUML in any of their signatures.
     */
    public void testInterfaces() {
        Method[] modelMethods = Model.class.getDeclaredMethods();
        
        for (int i = 0; i < modelMethods.length; i++) {
            Method modelMethod = modelMethods[i];
            
            if (!Modifier.isPublic(modelMethod.getModifiers())) {
                // This is a private method in Model.
                continue;
            }
            
            assertTrue("The method " + modelMethod + "is not static",
                       Modifier.isStatic(modelMethod.getModifiers()));
            
            Class factoryIF = modelMethod.getReturnType();
            
            assertTrue("The return type from " + modelMethod
                       + " must be an interface.",
                       factoryIF.isInterface());

            checkInterface(factoryIF);
        }
    }

    /**
     * Check that an interface obeys the rules.<p>
     *
     * This checks all extended interfaces recursively.<p>
     *
     * @param theInterface The interface to check.
     */
    private void checkInterface(Class theInterface) {
        Method[] methods = theInterface.getDeclaredMethods();
        
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            
            assertTrue("The method " + method + " has invalid return type "
                       + method.getReturnType(), 
                       isValid(method.getReturnType()));
            
            Class[] parameters = method.getParameterTypes();

            for (int k = 0; k < parameters.length; k++) {
                assertTrue("The method " + method 
                           + " has invalid parameter type "
                           + parameters[k],
                           isValid(parameters[k]));
            }
        }

        Class[] inherited = theInterface.getInterfaces();
        for (int i = 0; i < inherited.length; i++) {
            checkInterface(inherited[i]);
        }
    }

    /**
     * What we compare against to determine if it is an NSUML class or not.
     */
    private static final String UML_PATH_PREFIX = "ru.novosoft.uml.";

    /**
     * Tells if a type is valid or not.
     *
     * @param cls The class to test.
     * @return <tt>true</tt> if it is.
     */
    private boolean isValid(Class cls) {
        int length = UML_PATH_PREFIX.length();
        if (cls.getName().length() > length
                && cls.getName().substring(0, length).equals(UML_PATH_PREFIX)) {
            return false;            
        }

        return true;
    }
}
