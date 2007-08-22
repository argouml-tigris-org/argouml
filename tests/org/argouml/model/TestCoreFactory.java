// $Id$
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;


/**
 * Test the CoreFactory interface and implementation beyond.
 */
public class TestCoreFactory extends TestCase {

    /**
     * The UML 1.4 model elements to test.
     * 
     * TODO: This needs to be updated with a version for UML 2.x.
     */
    private static String[] allModelElements =
    {
	"Abstraction",
	"Association",
	"AssociationClass",
	"AssociationEnd",
	"Attribute",
	"BehavioralFeature",
	"Binding",
	"Class",
	"Classifier",
	"Comment",
	"Component",
	"Constraint",
	"DataType",
	"Dependency",
	"Element",
	"ElementResidence",
	"Feature",
	"Flow",
	"GeneralizableElement",
	"Generalization",
	"Interface",
	"Method",
	"ModelElement",
	"Namespace",
	"Node",
	"Operation",
	"Parameter",
	"Permission",
	"PresentationElement",
	"Relationship",
	"StructuralFeature",
        "TemplateArgument",
	"TemplateParameter",
	"Usage",
    };

    /**
     * The constructor.
     *
     * @param n the name of the test
     */
    public TestCoreFactory(String n) {
	super(n);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
	super.setUp();
        InitializeModel.initializeDefault();
    }

    /**
     * @return Returns the allModelElements.
     */
    static String[] getAllModelElements() {
        return allModelElements;
    }
    
    /**
     * @return the concrete ModelElements which are testable
     */
    static List<String> getTestableModelElements() {
        List<String> c = new ArrayList<String>(Arrays.asList(allModelElements));
        c.remove("BehavioralFeature");
        c.remove("Element");
        c.remove("ModelElement");
        c.remove("Namespace");
        c.remove("GeneralizableElement");
        c.remove("Classifier");
        c.remove("Feature");
        c.remove("StructuralFeature");
        c.remove("BehavioralFeature");
        c.remove("Relationship");
        c.remove("PresentationElement");
        
        // TODO: This is temporary.  We need a new list for UML 2.x
        // The following UML 1.4 elements are not in UML 2.x
        if (!"1.4".equals(Model.getFacade().getUmlVersion())) {
            c.remove("AssociationEnd");
            c.remove("Attribute");
            c.remove("Binding");
            c.remove("ElementResidence");
            c.remove("Flow");
            c.remove("Method");
            c.remove("Permission");
            c.remove("TemplateArgument");
        }
        return c;
    }
    
    /**
     * Test creation of metatypes.
     *
     * TODO: we could add tests here to make sure that
     * we can NOT create abstract types
     */
    public void testCreates() {
        CheckUMLModelHelper.createAndRelease(
                Model.getCoreFactory(), getTestableModelElements());
    }

    /**
     * Test complete deletion.
     */
    public void testDeleteComplete() {
        CheckUMLModelHelper.hasDeleteMethod(Model.getCoreFactory(),
                allModelElements);
    }

    /**
     * Test that deleting a classifier also deletes a binary association.
     */
    public void testDeleteClassifier1() {
        Object model = Model.getModelManagementFactory().createModel();
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);
        Object assoc = Model.getCoreFactory().buildAssociation(class1, class2);
        Model.getUmlFactory().delete(class1);
        Model.getPump().flushModelEvents();

        assertTrue("class not removed",
                Model.getUmlFactory().isRemoved(class1));
        assertTrue("binary association not removed",
                Model.getUmlFactory().isRemoved(assoc));
    }

    /**
     * Test if deleting a classifier from a 3 way
     * association results in a binary association
     * remaining between the other classifiers.
     */
    public void testDeleteClassifierAssociation() {
        Object model = Model.getModelManagementFactory().createModel();
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);
        Object assoc = Model.getCoreFactory().buildAssociation(class1, class2);
        Model.getCoreHelper().addConnection(assoc,
                Model.getCoreFactory().createAssociationEnd());
        Model.getUmlFactory().delete(class1);
        Model.getPump().flushModelEvents();
        assertTrue("class not removed",
                Model.getUmlFactory().isRemoved(class1));
        // Check to see if association still exists
        Collection ends = Model.getFacade().getAssociationEnds(class2);
        assertEquals(1, ends.size());
        Object assoc1 =
            Model.getFacade()
                .getAssociation(ends.iterator().next());
        assertEquals(2, Model.getFacade().getConnections(assoc1).size());
    }

    /**
     * Test if deleting a class also deletes its dependency.
     */
    public void testDeleteModelelementDependency() {
        Object model = Model.getModelManagementFactory().createModel();
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);
        Model.getCoreFactory().buildDependency(class1, class2);
        assertEquals("client dependency invalid",
                1, Model.getFacade().getClientDependencies(class1).size());
        assertEquals("supplier dependency invalid",
                1, Model.getFacade().getSupplierDependencies(class2).size());

        Model.getUmlFactory().delete(class1);
        Model.getPump().flushModelEvents();

        assertTrue("class not removed",
                Model.getUmlFactory().isRemoved(class1));
        assertEquals("invalid supplier dependency not removed",
                0, Model.getFacade().getSupplierDependencies(class2).size());
    }

    /**
     * Test that deleting a class doesn't delete a valid dependency.
     * <pre>
     * Build 2
     *      / \
     *     1   3
     * </pre>
     * delete 1 and confirm that dependency still exists
     */
    public void testDeleteModelelementDependencyClient() {
        Object model = Model.getModelManagementFactory().createModel();
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);
        Object dep = Model.getCoreFactory().buildDependency(class1, class2);
        Model.getCoreHelper().setNamespace(dep, model);
        Object class3 = Model.getCoreFactory().buildClass(model);
        Model.getCoreHelper().addClient(dep, class3);
        Model.getPump().flushModelEvents();
        assertEquals(
                "client dependency invalid",
                1,
                Model.getFacade().getClientDependencies(class1).size());
        assertEquals(
                "client dependency invalid",
                1,
                Model.getFacade().getClientDependencies(class3).size());
        assertEquals(
                "supplier dependency invalid",
                1,
                Model.getFacade().getSupplierDependencies(class2).size());

        Model.getUmlFactory().delete(class1);
        Model.getPump().flushModelEvents();

        assertTrue("class not removed",
                Model.getUmlFactory().isRemoved(class1));
        assertEquals("valid client dependency removed",
                1, Model.getFacade().getClientDependencies(class3).size());
        assertEquals("valid supplier dependency removed",
                1, Model.getFacade().getSupplierDependencies(class2).size());
    }

    /**
     * Test that deleting a class doesn't leave an invalid dependency.
     * <pre>
     * Build 2
     *      / \
     *     1   3
     * </pre>
     * delete 2 and confirm that the dependency is deleted
     */
    public void testDeleteModelelementDependencyClient2() {
        Object model = Model.getModelManagementFactory().createModel();
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);
        Object dep = Model.getCoreFactory().buildDependency(class1, class2);
        Object class3 = Model.getCoreFactory().buildClass(model);
        Model.getCoreHelper().addClient(dep, class3);

        Model.getUmlFactory().delete(class2);
        Model.getPump().flushModelEvents();

        assertTrue("class not removed",
                Model.getUmlFactory().isRemoved(class2));
        assertTrue("Invalid dependency not removed",
                Model.getFacade().getClientDependencies(class3).isEmpty());
        assertTrue("Invalid dependency not removed",
                Model.getFacade().getClientDependencies(class1).isEmpty());
    }

    /**
     * Test if deleting a class with a dependency containing two suppliers
     * also deletes the invalid dependency.
     * <pre>
     * Build 2   3
     *        \ /
     *         1
     * </pre>
     * Delete 1 and confirm that dependency gets deleted
     */
    public void testDeleteModelelementDependencySupplier() {
        Object model = Model.getModelManagementFactory().createModel();
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);
        Object dep = Model.getCoreFactory().buildDependency(class1, class2);
        Object class3 = Model.getCoreFactory().buildClass(model);
        Model.getCoreHelper().addSupplier(dep, class3);

        Model.getUmlFactory().delete(class1);
        Model.getPump().flushModelEvents();

        assertTrue("class not removed",
                Model.getUmlFactory().isRemoved(class1));
        assertEquals(
                "Invalid dependency not removed",
                0,
                Model.getFacade().getSupplierDependencies(class2).size());
        assertEquals(
                "Invalid dependency not removed",
                0,
                Model.getFacade().getSupplierDependencies(class3).size());
    }

    /**
     * Test if deleting a class which is one of two suppliers to a dependency
     * leaves the dependency intact.
     * <pre>
     * Build 2   3
     *        \ /
     *         1
     * </pre>
     * Delete 2 and confirm that dependency remains
     */
    public void testDeleteModelelementDependencySupplier2() {
        Object model = Model.getModelManagementFactory().createModel();
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);
        Object dep = Model.getCoreFactory().buildDependency(class1, class2);
        Object class3 = Model.getCoreFactory().buildClass(model);
        Model.getCoreHelper().addSupplier(dep, class3);

        Model.getUmlFactory().delete(class2);
        Model.getPump().flushModelEvents();

        assertTrue("class not removed",
                Model.getUmlFactory().isRemoved(class2));
        assertEquals(
                "Invalid dependency not removed",
                1,
                Model.getFacade().getClientDependencies(class1).size());
        assertEquals(
                "Invalid dependency not removed",
                1,
                Model.getFacade().getSupplierDependencies(class3).size());
    }

    /**
     * Construct a class, with two self associations and delete the class.
     * Test if both associations were deleted in the process.
     */
    public void testDeleteModelelementClassSelfAssociations() {
        Object model = Model.getModelManagementFactory().createModel();
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object assoc1 = Model.getCoreFactory().buildAssociation(class1, class1);
        Object assoc2 = Model.getCoreFactory().buildAssociation(class1, class1);
        Model.getUmlFactory().delete(class1);
        Model.getPump().flushModelEvents();

        assertTrue("class not removed",
                Model.getUmlFactory().isRemoved(class1));
        assertTrue("assoc1 not removed",
                Model.getUmlFactory().isRemoved(assoc1));
        assertTrue("assoc2 not removed",
                Model.getUmlFactory().isRemoved(assoc2));
    }

    /**
     * Test buildConstraint().
     */
    public void testBuildConstraint() {
	try {
	    Model.getCoreFactory().buildConstraint(null);
	    fail("IllegalArgumentException should be thrown");
	} catch (IllegalArgumentException i) {
	    // Expected IllegalArgumentException seen
	}
	Object elem = Model.getModelManagementFactory().createModel();
	Object con = Model.getCoreFactory().buildConstraint(elem);
        Model.getPump().flushModelEvents();
	assertNull("Namespace is unexpectly set", Model.getFacade()
                .getNamespace(con));
	assertTrue(
		   "Constrained element is not set",
		   !Model.getFacade().getConstrainedElements(con).isEmpty());
	assertTrue("Constraint is not set", !Model.getFacade().getConstraints(
                elem).isEmpty());
        Model.getCoreHelper().setNamespace(elem,
                Model.getModelManagementFactory().createPackage());
	con = Model.getCoreFactory().buildConstraint(elem);
        Model.getPump().flushModelEvents();
	assertNotNull("Namespace is not set", Model.getFacade().getNamespace(
                con));
    }

    /**
     * Test building a template ModelElement.
     */
    public void testBuildTemplate() {
        Object model = Model.getModelManagementFactory().createModel();
        Object templatedClass =
                Model.getCoreFactory().buildClass("Template", model);

        Object parameterizedClass = Model.getCoreFactory().buildClass(
                "ParameterizedClass", model);
        assertNotNull("Failed to create Class", parameterizedClass);
        Object binding = Model.getCoreFactory().buildBinding(
                parameterizedClass, templatedClass, null);
        assertNotNull("Failed to create build Binding with null arg list",
                binding);        
        
        Object paramType1 = Model.getCoreFactory().buildClass("ParamType1");
        Object paramType2 = Model.getCoreFactory().buildInterface("ParamType2");
        Object paramType3 = Model.getCoreFactory().buildClass("ParamType3");
        
        Object param1 = Model.getCoreFactory().createTemplateParameter();
        assertNotNull("Failed to create Template Parameter", param1);
        Object param2 = Model.getCoreFactory().createTemplateParameter();
        Object param3 = Model.getCoreFactory().createTemplateParameter();
        
        Model.getCoreHelper().setParameter(param1, paramType1);
        assertEquals("Parameters don't match", paramType1, 
                Model.getFacade().getParameter(param1));
        Model.getCoreHelper().setParameter(param2, paramType2);
        Model.getCoreHelper().setParameter(param3, paramType3);
        
        Object default1 = Model.getCoreFactory().buildClass("Default1");
        Object default2 = Model.getCoreFactory().buildInterface("Default2");
        Object default3 = Model.getCoreFactory().buildClass("Default3");
        
        Model.getCoreHelper().setDefaultElement(param1, default1);
        assertEquals("Default element doesn't match", default1, 
                Model.getFacade().getDefaultElement(param1));
        Model.getCoreHelper().setDefaultElement(param2, default2);
        Model.getCoreHelper().setDefaultElement(param3, default3);
        
        Model.getCoreHelper().addTemplateParameter(templatedClass, param2);
        Model.getCoreHelper().addTemplateParameter(templatedClass, param3);
        Model.getCoreHelper().addTemplateParameter(templatedClass, 0, param1);
        
        List params = new ArrayList();
        params.add(param1);
        params.add(param2);
        params.add(param3);

        assertEquals("TemplateParameter list is wrong", params,
                Model.getFacade().getTemplateParameters(templatedClass));
        
        Model.getCoreHelper().removeTemplateParameter(templatedClass, param2);
        params.remove(1);
        assertEquals("removeTemplateParameter gave wrong result", params,
                Model.getFacade().getTemplateParameters(templatedClass));

        Model.getCoreHelper().addTemplateParameter(templatedClass, 1, param2);
        params.add(1, param2);
        assertEquals("addTemplateParameter in middle gave wrong result",
                params, Model.getFacade().getTemplateParameters(templatedClass));

        Object arg1 = Model.getCoreFactory().buildClass("Arg1");
        Object arg2 = Model.getCoreFactory().buildInterface("Arg2");
        Object arg3 = Model.getCoreFactory().buildClass("Arg3");
        
        Object ta1 =  Model.getCoreFactory().buildTemplateArgument(arg1);
        assertNotNull("buildTemplateArgument produced null result", ta1);
        assertEquals("model element of template argument doesn't match", 
                arg1, Model.getFacade().getModelElement(ta1));
        Object ta2 =  Model.getCoreFactory().buildTemplateArgument(arg2);
        Object ta3 =  Model.getCoreFactory().buildTemplateArgument(arg3);

        Model.getCoreHelper().addTemplateArgument(binding, ta2);
        Model.getCoreHelper().addTemplateArgument(binding, ta3);
        Model.getCoreHelper().addTemplateArgument(binding, 0, ta1);
        
        List args = new ArrayList();
        args.add(ta1);
        args.add(ta2);
        args.add(ta3);
        assertEquals("Binding arguments don't match", args, 
                Model.getFacade().getArguments(binding));
        
        Model.getCoreHelper().removeTemplateArgument(binding, ta2);
        args.remove(ta2);
        assertEquals("Binding arguments don't match", args, 
                Model.getFacade().getArguments(binding));
        
        Model.getCoreHelper().addTemplateArgument(binding, 1, ta2);
        args.add(1, ta2);
        assertEquals("Binding arguments don't match", args, 
                Model.getFacade().getArguments(binding));
        
        // A parameterized class can only be the client of a single binding
        try {
            Model.getCoreFactory()
                    .buildBinding(
                            parameterizedClass,
                            Model.getCoreFactory().buildClass(
                                    "Template2", model), null);
            fail("Attempt to create 2nd binding for a client didn't fail "
                    + "as expected.");
        } catch (IllegalArgumentException e) {
            // exception expected - test success
        }

        // Create a different client to bind to the same supplier
        Object parameterizedClass2 = Model.getCoreFactory().buildClass(
                "ParameterizedClass2", model);
        
        args.remove(0);
        try {
            Model.getCoreFactory().buildBinding(
                    parameterizedClass2, templatedClass, args);
            fail("Expected exception for mismatched number of args & params");
        } catch (IllegalArgumentException e) {
            // expected - test success
        }

        args.add(arg1);
        try {
            Model.getCoreFactory().buildBinding(
                    parameterizedClass2, templatedClass, args);
            fail("Expected exception for mismatched type/order of args "
                    + "& params");
        } catch (IllegalArgumentException e) {
            // expected - test success
        }

        // Create a new arg list with a different set of args (but same types)
        args.clear();
        // NOTE: Although it's not shown in the UML 1.4 spec, a TemplateArgument
        // appears to be a datavalue (like MultiplicityRange) and can not be
        // reused in multiple instances.  The UML diagrams don't show it as a
        // composition, but it effectively is.
        ta1 = Model.getCoreFactory().buildTemplateArgument(
                Model.getCoreFactory().buildClass("ta1a"));
        ta2 = Model.getCoreFactory().buildTemplateArgument(
                Model.getCoreFactory().buildInterface("ta2a"));
        ta3 = Model.getCoreFactory().buildTemplateArgument(
                Model.getCoreFactory().buildClass("ta3a"));
        args.add(ta1);
        args.add(ta2);
        args.add(ta3);
        Object binding2 = Model.getCoreFactory().buildBinding(
                parameterizedClass2, templatedClass, args);
        assertNotNull("Failed to create 2nd binding to same template", 
                binding2);
        assertEquals("Binding arguments don't match", args, 
                Model.getFacade().getArguments(binding2));
        
        Collection deps =
                Model.getFacade().getClientDependencies(parameterizedClass2);
        assertEquals(1, deps.size());
        Object dep = deps.iterator().next();
        assertEquals(binding2, dep);
        Collection suppliers =  Model.getFacade().getSuppliers(dep);
        assertEquals(1, suppliers.size());
        Collection clients =  Model.getFacade().getClients(dep);
        assertEquals(1, clients.size());
        assertEquals(templatedClass, suppliers.iterator().next());
    }


    /**
     * Test building an association between classes in two different packages
     * and verify that the 
     */
    public void testBuildAssociation() {
        Object model = Model.getModelManagementFactory().createModel();
        Object packA = Model.getModelManagementFactory().buildPackage("package-a", "111");
        Object packB = Model.getModelManagementFactory().buildPackage("package-b", "222");
        Object packC = Model.getModelManagementFactory().buildPackage("package-c", "222");

        Model.getCoreHelper().setNamespace(packA, model);
        Model.getCoreHelper().setNamespace(packB, packA);
        Model.getCoreHelper().setNamespace(packC, packA);
        Object class1 = Model.getCoreFactory().buildClass(packB);
        Object class2 = Model.getCoreFactory().buildClass(packC);
        Model.getModelManagementFactory().buildElementImport(packB, class2);

        Object assoc = Model.getCoreFactory().buildAssociation(class1, class2);
        assertTrue("buildAssociation() and isValidNamespace() don't agree on " +
        		"namespace for an Association", 
        		Model.getCoreHelper().isValidNamespace(assoc, 
        		        Model.getFacade().getNamespace(assoc)));
        
    }


}
