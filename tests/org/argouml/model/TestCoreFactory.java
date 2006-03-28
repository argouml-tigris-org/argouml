// $Id$
// Copyright (c) 2002-2006 The Regents of the University of California. All
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
import java.util.Vector;

import junit.framework.TestCase;


/**
 * Test the CoreFactory interface and implementation beyond.
 */
public class TestCoreFactory extends TestCase {

    /**
     * The model elements to test.
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

    /**
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
	Model.getFacade();
    }

    /**
     * Test creation of metatypes.
     *
     * TODO: we could add tests here to make sure that
     * we can NOT create abstract types
     */
    public void testCreates() {
	Collection objs = new Vector();

	// The abstract metaclasses in UML 1.3 include Element, ModelElement,
        // Feature, Namespace, GeneralizableElement, Classifier,
        // StructuralFeature, BehavioralFeature, Relationship,
        // PresentationElement, Action, StateVertex, and
    	// Event.
        // UML 1.4 changes Instance and State to be abstract also.

        // TODO: createAbstraction is not part of Model interface
	objs.add("Abstraction");
	//Association are abstract metaclass but we return an UmlAssociation
	//from the createAssociation method of the CoreFactory interface
	objs.add("Association");
	objs.add("AssociationClass");
	objs.add("AssociationEnd");
	objs.add("Attribute");
	objs.add("Binding");
	objs.add("Class");
	objs.add("Comment");
	objs.add("Component");
	objs.add("Constraint");
	objs.add("DataType");
	objs.add("Dependency");
	objs.add("ElementResidence");
	objs.add("Flow");
	objs.add("Generalization");
	objs.add("Interface");
	objs.add("Method");
	objs.add("Node");
	objs.add("Operation");
	objs.add("Parameter");
	objs.add("Permission");
	objs.add("TemplateParameter");
	objs.add("Usage");

	CheckUMLModelHelper.createAndRelease(
	    Model.getCoreFactory(),
	    // +1 in the size of the array because we also test the null value
	    (String[]) objs.toArray(new String[objs.size() + 1]));
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
     * @return Returns the allModelElements.
     */
    static String[] getAllModelElements() {
        return allModelElements;
    }
}
