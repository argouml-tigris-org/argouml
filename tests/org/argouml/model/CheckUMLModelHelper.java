// $Id$
// Copyright (c) 2002-2005 The Regents of the University of California. All
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

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * This class is a helper class of tests that test
 * the model stuff.<p>
 *
 * This class is used for testing the Model subsystem. It should not have any
 * dependency on any specific implementation.
 *
 * @author Linus Tolke
 * @since 0.11.2
 */
public final class CheckUMLModelHelper {
    /**
     * Constructor to forbid creation.
     */
    private CheckUMLModelHelper() {
    }

    /**
     * Deleted a model object, looses the refence and then checks that
     * the object is reclaimed.
     *
     * This must be called with just one reference to the object or
     * else it will fail.
     *
     * @param mo the model object that we try to delete, release and reclaim.
     */
    public static void deleteAndRelease(Object mo) {
	Class c = mo.getClass();

	// Call methods that exists for all objects and that always return
	// something meaningfull.
	TestCase.assertTrue("toString() corrupt in " + c,
		      mo.toString() != null);

	Model.getUmlFactory().delete(mo);
        Model.getPump().flushModelEvents();
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
     * @param mo the model object that we try to delete, release and reclaim.
     * @param name the class name of the uml object
     */
    private static void deleteAndRelease(Object mo, String name) {
	Class c = mo.getClass();

	// Call methods that exists for all objects and that always return
	// something meaningfull.
	TestCase.assertTrue("toString() corrupt in " + c,
		      	    mo.toString() != null);
	TestCase.assertTrue("getUMLClassName() corrupt in " + c,
	        Model.getFacade().getUMLClassName(mo) != null);
	TestCase.assertTrue(
	        "getUMLClassName() different from expected in " + c,
	        name.equals(Model.getFacade().getUMLClassName(mo)));

	Model.getUmlFactory().delete(mo);

        WeakReference wo = new WeakReference(mo);

        Model.getPump().flushModelEvents();
	mo = null;
	System.gc();
	TestCase.assertTrue("Could not reclaim " + c, wo.get() == null);
    }

    /**
     * Creates a UML modelelement (i.e. check if a creation function exists).
     * Then deletes it, looses the reference and then checks that
     * the object is reclaimed.
     *
     * @param f the DataTypesFactory
     * @param names the UML elements to test
     * @param args the arguments of the UML elements
     */
    public static void createAndRelease(Object f,
					String[] names,
					Object[] args) {
	Class [] classes = new Class[args.length];
	for (int i = 0; i < args.length; i++) {
	    classes[i] = args[i].getClass();
	}

	for (int i = 0; i < names.length; i++) {
	    if (names[i] == null) {
	        continue;
	    }
	    Method m;
	    try {
		m =
		    f.getClass().getDeclaredMethod("create" + names[i],
						   classes);
	    } catch (NoSuchMethodException e) {
		TestCase.fail("Method create" + names[i]
			      + " does not exist in " + f);
		return;
	    }

	    try {
		// Extra careful now, not to keep any references to the
		// second argument.
		try {
		    deleteAndRelease(m.invoke(f, args), names[i]);
		} catch (ClassCastException e) {
		    // Here it is another object sent to the test.
		    deleteAndRelease(m.invoke(f, args));
		} catch (IllegalArgumentException e) {
		    // Here it is another object sent to the test.
		    deleteAndRelease(m.invoke(f, args));
		}
	    } catch (IllegalAccessException e) {
		TestCase.fail("Method create" + names[i]
			      + " in " + f + " cannot be called");
		return;
	    } catch (InvocationTargetException e) {
		TestCase.fail("Method create" + names[i]
			      + " in " + f + " throws an exception.");
		return;
	    }
	}
    }

    /**
     * @param f the DataTypesFactory
     * @param names the UML elements to test
     */
    public static void createAndRelease(Object f, String[] names) {
	Object[] noarguments = {
	};

	createAndRelease(f, names, noarguments);
    }

    /**
     * Test the presence of deletion methods for a list of modelelements.
     *
     * @param f the model factory that provides the "delete" methods
     *          for the modelelements
     * @param names the names of the modelelements
     */
    public static void hasDeleteMethod(Object f, String[] names) {
        Method[] methods = null;
        try {
            methods = f.getClass().getDeclaredMethods();
        } catch (SecurityException se) {
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
     * @param f the modelfactory that should contain the creation function
     * @param names the metamodel class names
     */
    public static void metaModelNameCorrect(Object f, String[] names) {
        try {
            for (int i = 0; i < names.length; i++) {
                try {
                    Method m =
                        f.getClass()
                        	.getMethod("create" + names[i], new Class[] {});
                    Object base = m.invoke(f, new Object[] {});
                    if (Model.getFacade().isAModelElement(base)) {
                        TestCase.assertTrue(
                            "not a valid metaModelName " + names[i],
                            Model.getExtensionMechanismsHelper()
                                .getMetaModelName(base)
                                    .equals(names[i]));
                    }
                } catch (NoSuchMethodException ns) { }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            TestCase.fail(
                    "Exception during test metaModelnameCorrect. Message: "
                    + ex.getMessage());
        }
    }

    /**
     * Try creating a stereotype for every modelelement type.
     *
     * @param f the factory containing the creation function for
     *          all the given metamodel element names
     * @param names the metamodel element names
     */
    public static void isValidStereoType(Object f, String[] names) {
        try {
            Object ns = Model.getModelManagementFactory().createPackage();
            Object clazz = Model.getCoreFactory().buildClass(ns);
            Object stereo1 =
		Model.getExtensionMechanismsFactory()
                    .buildStereotype(clazz, "test1", ns);
            for (int i = 0; i < names.length; i++) {
                try {
                    Method m =
                        f.getClass()
                        	.getMethod("create" + names[i], new Class[] {});
                    Object base = m.invoke(f, new Object[] {});
                    if (Model.getFacade().isAModelElement(base)) {
                        Object stereo2 =
                            Model.getExtensionMechanismsFactory()
                                .buildStereotype(base, "test2", ns);
                        TestCase.assertTrue(
                            "Unexpected invalid stereotype",
                            Model.getExtensionMechanismsHelper()
                                .isValidStereoType(base, stereo2));
                        if (!(Model.getFacade().isAClass(base))) {
                            TestCase.assertFalse(
                                "Stereotype with base class of Class" 
                                    + " incorrectly allowed for this metaclass",
                                Model.getExtensionMechanismsHelper()
                                    .isValidStereoType(base, stereo1));
                        } else {
                            Object inter =
				Model.getCoreFactory().createInterface();
                            Object stereo3 =
				Model.getExtensionMechanismsFactory()
                                    .buildStereotype(inter, "test3", ns);
                            TestCase.assertTrue(
                                "Unexpected invalid stereotype",
                                !Model.getExtensionMechanismsHelper()
                                    .isValidStereoType(base, stereo3));
                        }
                    }
                } catch (NoSuchMethodException ns2) { }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            TestCase.fail(
                    "Exception during test metaModelnameCorrect. Message: "
                    + ex.getMessage());
        }
    }
}
