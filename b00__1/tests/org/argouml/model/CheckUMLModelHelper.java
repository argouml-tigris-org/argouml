// $Id:CheckUMLModelHelper.java 13240 2007-08-03 21:49:06Z tfmorris $
// Copyright (c) 2002-2007 The Regents of the University of California. All
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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
     * Delete a model object, frees the reference and then checks that
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
  
	TestCase.assertTrue("Could not delete " + c, 
                Model.getUmlFactory().isRemoved(mo));
    }

    /**
     * Delete a model object, frees the reference and then checks that
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
        Model.getPump().flushModelEvents();

        TestCase.assertTrue("Could not delete " + c, 
                Model.getUmlFactory().isRemoved(mo));
    }

    /**
     * Create a UML modelelement (i.e. check if a creation function exists).
     * Then deletes it, looses the reference and then checks that
     * the object is reclaimed.
     *
     * @param factory the DataTypesFactory
     * @param names the UML elements to test
     * @param arguments the arguments of the UML elements
     */
    public static void createAndRelease(Object factory,
					String[] names,
					Object[] arguments) {
	Class [] argTypes = new Class[arguments.length];
	for (int i = 0; i < arguments.length; i++) {
	    argTypes[i] = arguments[i].getClass();
	}
        
        // Multiplicity, MultiplicityRange, and all Expression subtypes
        // don't have 0-argument create methods, so we special case them.
        Integer[] multArgs = {1, 1};
        Class[] multArgTypes = {int.class, int.class};
        String [] exprArgs = {"body text", "language text"};
        Class[] exprArgTypes = {String.class, String.class};

	for (int i = 0; i < names.length; i++) {
            Class[] types;
            Object[] args;
	    if (names[i] == null) {
	        continue;
	    } else if (names[i].startsWith("Multiplicity")) {
                types = multArgTypes;
                args = multArgs;
            } else if (names[i].endsWith("Expression")) {
                types = exprArgTypes;
                args = exprArgs;
            } else {
                types = argTypes;
                args = arguments;
            }
            String methodName = "create" + names[i];
	    Method createMethod;

	    // Find the create method in the offical API
	    createMethod = findMethod(factory.getClass(), Factory.class,
	            methodName, types);
	    if (createMethod == null) {
	        TestCase.fail("Method " + methodName
	                + " does not exist in any interface of factory "
	                + factory.getClass().getName());
	        return;
	    }

            
            Method isAMethod;
            String isAMethodName = "isA" + names[i];
            Object facade = Model.getFacade();
            try {
                // Now get the factory implementation method to be invoked
                isAMethod =
                    Facade.class.getDeclaredMethod(
                            isAMethodName,
                            new Class[] {Object.class});
            } catch (NoSuchMethodException e) {
                TestCase.fail("Method " + isAMethodName
                        + " does not exist in Facade");
                return;
            }

	    try {
		// Extra careful now, not to keep any references to the
		// second argument.
		try {
		    Object element = invoke(createMethod, factory, args);
		    TestCase.assertTrue("Facade method " + isAMethodName
                            + " returned false", (Boolean) invoke(
                            isAMethod, facade, new Object[] {
                                element
                            }));
                
                    deleteAndRelease(
                            createMethod.invoke(factory, args), names[i]);
		} catch (ClassCastException e) {
		    // Here it is another object sent to the test.
		    deleteAndRelease(createMethod.invoke(factory, args));
		} catch (IllegalArgumentException e) {
		    // Here it is another object sent to the test.
		    deleteAndRelease(createMethod.invoke(factory, args));
		}
	    } catch (IllegalAccessException e) {
		TestCase.fail("Method create" + names[i]
			      + " in " + factory + " cannot be called");
		return;
	    } catch (InvocationTargetException e) {
		TestCase.fail("Method create" + names[i]
			      + " in " + factory + " throws an exception.");
		return;
	    }
	}
    }
    
    /**
     * Convenience method to invoke a method and convert any thrown exceptions
     * to test failures with a useful message.
     */
    private static Object invoke(Method method, Object object, Object[] args) {
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException e) {
            TestCase.fail("Method " + method.getName() + " in " + object
                    + " cannot be called");
        } catch (InvocationTargetException e) {
            TestCase.fail("Method " + method.getName() + " in " + object
                    + " throws an exception.");
        }
        return null;
    }

   
    /**
     * Check all interfaces of the given class for an interface which both: 1)
     * extends the given marker interface (in our case called 'Factory') 2)
     * contains the given method.
     * <p>
     * 
     * This extra check is to make sure that the public methods of the given
     * factory are actually part of the public API interface.
     * 
     * @param factory
     *            the factory class to test
     * @param markerInterface
     *            the class of the marker interface to look for
     * @param methodName
     *            the name of the object that we want to create
     * @param argTypes
     *            the types of the arguments for the method
     * @return the requested method or null if no match is found
     */
    private static Method findMethod(Class factory,
            Class markerInterface, String methodName, Class[] argTypes) {
        Class[] interfaces = factory.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            if (markerInterface.equals(interfaces[i])) {
                Method m = getMethod(factory, methodName, argTypes);
                if (m != null) {
                    return m;
                }
            } else {
                Method m =
                        findMethod(
                                interfaces[i], markerInterface, methodName,
                                argTypes);
                if (m != null) {
                    return m;
                }
            }
        }
        return null;
    }
    
    private static Method getMethod(Class clazz, String methodName,
            Class[] classes) {
        try {
            return clazz.getDeclaredMethod(methodName, classes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * @param f
     *            the DataTypesFactory
     * @param names
     *            the UML elements to test
     */
    public static void createAndRelease(Object f, String[] names) {
	Object[] noarguments = {
	};
	createAndRelease(f, names, noarguments);
    }

    public static void createAndRelease(Object f, List<String>names) {
        Object[] noarguments = {
        };
        String[] elementNames = new String[names.size()];
        names.toArray(elementNames);
        createAndRelease(f, elementNames, noarguments);
    }
    
    /**
     * Test the presence of deletion methods for a list of modelelements.
     *
     * @param f the model factory that provides the "delete" methods
     *          for the modelelements
     * @param names the names of the modelelements
     */
    public static void hasDeleteMethod(Object f, String[] names) {
        for (int i = 0; i < names.length; i++) {
            String methodName = "delete" + names[i];
            try {
                f.getClass().getDeclaredMethod(methodName,
                        new Class[] {Object.class});
            } catch (SecurityException se) {
                TestCase.fail(
                        "SecurityException while retrieving all methods from "
                        + f.getClass().getName());
                return;
            } catch (NoSuchMethodException e) {
                TestCase.fail("Method " + methodName + " not found in "
                        + f.getClass().getName());   
            }
        }
    }

    /**
     * Check if for every metamodel element name a create function exists.
     *
     * @param factory the modelfactory that should contain the create function
     * @param names the metamodel class names
     */
    public static void metaModelNameCorrect(Object factory, String[] names) {
        try {
            for (int i = 0; i < names.length; i++) {
                Method m = findMethod(factory.getClass(), Factory.class,
                        "create" + names[i], new Class[] {});
                TestCase.assertNotNull("Failed to find method create"
                        + names[i], m);
                Object element = m.invoke(factory, new Object[] {});
                TestCase.assertTrue("Not a UML Element", 
                        Model.getFacade().isAUMLElement(element));
                String metaName =
                        Model.getExtensionMechanismsHelper().getMetaModelName(
                                element);
                TestCase.assertTrue(
                        "not a valid metaModelName " + names[i], 
                        metaName.equals(names[i]));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            TestCase.fail(
                    "Exception during test metaModelnameCorrect. Message: "
                    + ex.getMessage());
        }
    }

    public static void metaModelNameCorrect(Object factory, 
            List<String> names) {
        metaModelNameCorrect(factory, names.toArray(new String[names.size()]));
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
                Method m = findMethod(f.getClass(), Factory.class,
                        "create" + names[i], new Class[] {});
                if (m == null) {
                    TestCase.fail("Failed to find method create" + names[i]);
                }
                Object base = m.invoke(f, new Object[] {});
                if (Model.getFacade().isAModelElement(base)) {
                    Object stereo2 =
                            Model.getExtensionMechanismsFactory()
                                    .buildStereotype(base, "test2", ns);
                    TestCase.assertTrue(
                            "Unexpected invalid stereotype",
                            Model.getExtensionMechanismsHelper()
                            .isValidStereotype(base, stereo2));
                    if (!(Model.getFacade().isAClass(base))) {
                        TestCase.assertTrue(
                                "Stereotype with base class of Class" 
                                + " incorrectly allowed for this metaclass",
                                !Model.getExtensionMechanismsHelper()
                                .isValidStereotype(base, stereo1));
                    } else {
                        Object inter =
                            Model.getCoreFactory().createInterface();
                        Object stereo3 =
                                Model.getExtensionMechanismsFactory()
                                        .buildStereotype(inter, "test3", ns);
                        TestCase.assertTrue(
                                "Unexpected invalid stereotype",
                                !Model.getExtensionMechanismsHelper()
                                .isValidStereotype(base, stereo3));
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            TestCase.fail(
                    "Exception during test metaModelnameCorrect. Message: "
                    + ex.getMessage());
        }
    }
    
    public static void isValidStereoType(Object f, List<String> names) {
        isValidStereoType(f, names.toArray(new String[names.size()]));
    }
}
