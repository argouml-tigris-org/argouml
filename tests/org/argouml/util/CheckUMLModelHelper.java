// $Id$
// Copyright (c) 2002-2003 The Regents of the University of California. All
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

package org.argouml.util;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.argouml.model.uml.CoreFactory;
import org.argouml.model.uml.ExtensionMechanismsFactory;
import org.argouml.model.uml.ExtensionMechanismsHelper;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.AbstractUmlModelFactory;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;


/**
 *   This class is the base class of tests that tests
 *   the model stuff.
 *
 *   @author Linus Tolke
 *   @since 0.11.2
 */
public class CheckUMLModelHelper {

    /**
     * Deleted a model object, looses the refence and then checks that 
     * the object is reclaimed.
     *
     * This must be called with just one reference to the object or
     * else it will fail.
     *
     * @param tc the test case where we run the assertions.
     * @param mo the model object that we try to delete, release and reclaim.
     */
    public static void deleteAndRelease(TestCase tc,
					Object mo) {
	Class c = mo.getClass();

	// Call methods that exists for all objects and that always return
	// something meaningfull.
	TestCase.assertTrue("toString() corrupt in " + c, 
		      mo.toString() instanceof String);
 
	UmlFactory.getFactory().delete(mo);

	WeakReference wo = new WeakReference(mo);
	mo = null;
	System.gc();
	TestCase.assertTrue("Could not reclaim " + c, wo.get() == null);
    }

    /**
     * Deleted a model object, looses the reference and then checks that 
     * the object is reclaimed.
     *
     * This must be called with just one reference to the object or
     * else it will fail.
     *
     * @param tc the test case where we run the assertions.
     * @param mo the model object that we try to delete, release and reclaim.
     * @param name the class name of the uml object
     */
    private static void deleteAndRelease(TestCase tc,
					 MBase mo,
					 String name) {
	Class c = mo.getClass();

	// Call methods that exists for all objects and that always return
	// something meaningfull.
	TestCase.assertTrue("toString() corrupt in " + c, 
		      mo.toString() instanceof String);
	TestCase.assertTrue("getUMLClassName() corrupt in " + c, 
		      mo.getUMLClassName() instanceof String);

	TestCase.assertTrue(
            "getUMLClassName() different from expected in " + c, 
	    name.equals(mo.getUMLClassName()));

	UmlFactory.getFactory().delete(mo);

	WeakReference wo = new WeakReference(mo);
	mo = null;
	System.gc();
	TestCase.assertTrue("Could not reclaim " + c, wo.get() == null);
    }

    /**
     * Creates a UML modelelement (i.e. check if a creation function exists).
     * Then deletes it, looses the reference and then checks that 
     * the object is reclaimed.
     *  
     * @param tc the testcase class
     * @param f the DataTypesFactory
     * @param names the UML elements to test
     * @param args the arguments of the UML elements
     */
    public static void createAndRelease(TestCase tc,
					AbstractUmlModelFactory f,
					String [] names,
					Object [] args) {
	Class [] classes = new Class[args.length];
	for (int i = 0; i < args.length; i++) {
	    classes[i] = args[i].getClass();
	}

	for (int i = 0; i < names.length; i++) {
	    if (names[i] == null) continue;
	    Method m;
	    try {
		m = f.getClass().getMethod("create" + names[i], classes);
	    }
	    catch (NoSuchMethodException e) {
		TestCase.fail("Method create" + names[i] 
			      + " does not exist in " + f);
		return;
	    }

	    try {
		// Extra careful now, not to keep any references to the
		// second argument.
		try {
		    deleteAndRelease(tc, (MBase) m.invoke(f, args), names[i]);
		}
		catch (ClassCastException e) {
		    // Here it is another object sent to the test.
		    deleteAndRelease(tc, m.invoke(f, args));
		}
	    }
	    catch (IllegalAccessException e) {
		TestCase.fail("Method create" + names[i] 
			      + " in " + f + " cannot be called");
		return;
	    }
	    catch (InvocationTargetException e) {
		TestCase.fail("Method create" + names[i] 
			      + " in " + f + " throws an exception.");
		return;
	    }
	}
    }

    /**
     * @param tc the testcase class
     * @param f the DataTypesFactory
     * @param names the UML elements to test
     */
    public static void createAndRelease(TestCase tc,
					AbstractUmlModelFactory f,
					String [] names) {
	Object[] noarguments = {
	};

	createAndRelease(tc, f, names, noarguments);
    }
    
    /**
     * Test the presence of deletion functions for a list of modelelements.
     * 
     * @param tc the testcase class
     * @param f the model factory that provides the "delete" functions 
     *          for the modelelements
     * @param names the names of the modelelements
     */
    public static void deleteComplete(TestCase tc, 
				      AbstractUmlModelFactory f, 
				      String[] names) {
        Method[] methods = null;
        try {
            methods = f.getClass().getMethods();
        }
        catch (SecurityException se) {
            TestCase.fail(
                    "SecurityException while retrieving all methods from " 
                    + f.getClass().getName());
            return;
        }
        for (int i = 0; i < names.length; i++) {
            String methodName = "delete" + names[i];
            boolean testFailed = true;
            for (int j = 0; j < methods.length; j++) {
                Method method = methods[j];
                if (method.getName().equals(methodName)) {
                    testFailed = false;
                    break;
                }
            }
            if (testFailed) {
                TestCase.fail("Method " + methodName + " not found in " 
                        + f.getClass().getName());
            }
        }
    }
    
    /**
     * Check if for every metamodel element name a create function exists.
     * 
     * @param tc the testcase class
     * @param f the modelfactory that should contain the creation function
     * @param names the metamodel class names
     */
    public static void metaModelNameCorrect(TestCase tc, 
            AbstractUmlModelFactory f, String[] names) {
        try {
            for (int i = 0; i < names.length; i++) {
                try {
                    Method m = f.getClass()
                        .getMethod("create" + names[i], new Class[] {});
                    Object base = m.invoke(f, new Object[] {});
                    if (base instanceof MModelElement) {
                        TestCase.assertTrue(
                            "not a valid metaModelName " + names[i], 
                            ExtensionMechanismsHelper.getHelper()
                                .getMetaModelName((MModelElement) base)
                                    .equals(names[i]));
                    }
                }
                catch (NoSuchMethodException ns) { }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            TestCase.fail(
                    "Exception during test metaModelnameCorrect. Message: " 
                    + ex.getMessage());
            
        }
    } 
    
    /**
     * Try creating a stereotype for every modelelement type.
     * 
     * @param tc the testcase class
     * @param f the factory containing the creation function for 
     *          all the given metamodel element names
     * @param names the metamodel element names
     */
    public static void isValidStereoType(
            TestCase tc, 
            AbstractUmlModelFactory f, 
            String[] names) {
        try {
            MNamespace ns = CoreFactory.getFactory().createNamespace();
            Object clazz = CoreFactory.getFactory().buildClass(ns);
            MStereotype stereo1 = ExtensionMechanismsFactory.getFactory()
                    .buildStereotype(clazz, "test1", ns);
            for (int i = 0; i < names.length; i++) {
                try {
                    Method m = f.getClass()
                        .getMethod("create" + names[i], new Class[] {});
                    Object base = m.invoke(f, new Object[] {});
                    if (base instanceof MModelElement) {
                        MStereotype stereo2 = 
                            ExtensionMechanismsFactory.getFactory()
                                .buildStereotype((MModelElement) base, 
                                                "test2", 
                                                ns);
                        TestCase.assertTrue(
                            "Unexpected invalid stereotype", 
                            ExtensionMechanismsHelper.getHelper()
                                .isValidStereoType((MModelElement) base, 
                                                    stereo2));
                        if (!(base instanceof MClass)) {
                            TestCase.assertTrue(
                                "Unexpected invalid stereotype", 
                                !ExtensionMechanismsHelper.getHelper()
                                    .isValidStereoType((MModelElement) base, 
                                                        stereo1));
                        } else {
                            MInterface inter = 
                                CoreFactory.getFactory().createInterface();
                            MStereotype stereo3 = ExtensionMechanismsFactory
                                .getFactory().buildStereotype(inter, 
                                                              "test3", 
                                                              ns);
                            TestCase.assertTrue(
                                "Unexpected invalid stereotype", 
                                !ExtensionMechanismsHelper.getHelper()
                                    .isValidStereoType((MModelElement) base, 
                                                        stereo3));
                        }
                    }
                }
                catch (NoSuchMethodException ns2) { }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            TestCase.fail(
                    "Exception during test metaModelnameCorrect. Message: " 
                    + ex.getMessage());
            
        }
    }
}
