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

package org.argouml.model.uml;

import java.lang.ref.WeakReference;

import junit.framework.TestCase;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MConstraint;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.model_management.MModel;

/**
 * Test the CoreFactory NSUML implementation.<p>
 *
 * No references to the org.argouml.model package.
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
     * The ModelImplementation used in this test.
     */
    private NSUMLModelImplementation nsumlmodel;

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
        nsumlmodel = new NSUMLModelImplementation();
    }

    /**
     * Test creation.
     */
    public void testCreates() {
	// Do not test BehavioralFeature, Feature, PresentationElement,
	//    StructuralFeature yet.
	// NSUML does not have create method.
	//
	// Never test for ModelElement.
	String[] objs = {
	    "Abstraction",
	    "Association",
	    "AssociationClass",
	    "AssociationEnd",
	    "Attribute",

	    // "BehavioralFeature",
	    "Binding",
	    "Class",
	    "Classifier",
	    "Comment",
	    "Component",
	    "Constraint",
	    "DataType",
	    "Dependency",

	    // "Element",
	    "ElementResidence",

	    // "Feature",
	    "Flow", "Generalization", "Interface", "Method",

	    // "ModelElement",
	    "Namespace", "Node", "Operation", "Parameter", "Permission",

	    // "PresentationElement",
	    "Relationship",

	    // "StructuralFeature",
	    "TemplateParameter", "Usage",

	    null,
	};

	CheckNSUMLModelHelper.createAndRelease(
					     this,
					     nsumlmodel.getCoreFactory(),
					     objs);
    }

    /**
     * Test complete deletion.
     */
    public void testDeleteComplete() {
	CheckNSUMLModelHelper.deleteComplete(
					   this,
					   nsumlmodel.getCoreFactory(),
					   allModelElements);
    }

    /**
     * Test if deleting a classifier does also delete its association.
     */
    public void testDeleteClassifier1() {
        MModel model = 
            (MModel) nsumlmodel.getModelManagementFactory().createModel();
        Object class1 = nsumlmodel.getCoreFactory().buildClass(model);
        Object class2 = nsumlmodel.getCoreFactory().buildClass(model);
        MAssociation assoc =
            (MAssociation)
            	nsumlmodel.getCoreFactory().buildAssociation(class1, class2);
        WeakReference class1wr = new WeakReference(class1);
        WeakReference assocwr = new WeakReference(assoc);
        nsumlmodel.getUmlFactory().delete(class1);
        class1 = null;
        assoc = null;
        System.gc();
        assertNull("class not removed", class1wr.get());
        assertNull("binary association not removed", assocwr.get());
    }

    /**
     * Test if deleting a classifier does also delete its association.
     */
    public void testDeleteClassifierAssociation() {
        MModel model =
            (MModel) nsumlmodel.getModelManagementFactory().createModel();
        Object class1 = nsumlmodel.getCoreFactory().buildClass(model);
        Object class2 = nsumlmodel.getCoreFactory().buildClass(model);
        MAssociation assoc =
            (MAssociation)
            	nsumlmodel.getCoreFactory().buildAssociation(class1, class2);
        assoc.addConnection(
                (MAssociationEnd)
                	nsumlmodel.getCoreFactory().createAssociationEnd());
        WeakReference class1wr = new WeakReference(class1);
        WeakReference assocwr = new WeakReference(assoc);
        nsumlmodel.getUmlFactory().delete(class1);
        class1 = null;
        assoc = null;
        System.gc();
        assertNull("class not removed", class1wr.get());
        assertNotNull("association removed", assocwr.get());
    }

    /**
     * Test if deleting a class also deletes its dependency.
     */
    public void testDeleteModelelementDependency() {
        MModel model =
            (MModel) nsumlmodel.getModelManagementFactory().createModel();
        Object class1 = nsumlmodel.getCoreFactory().buildClass(model);
        Object class2 = nsumlmodel.getCoreFactory().buildClass(model);
        Object dep =
            nsumlmodel.getCoreFactory().buildDependency(class1, class2);
        WeakReference class1wr = new WeakReference(class1);
        WeakReference depwr = new WeakReference(dep);
        nsumlmodel.getUmlFactory().delete(class1);
        class1 = null;
        dep = null;
        System.gc();
        assertNull("class not removed", class1wr.get());
        assertNull("binary dependency not removed", depwr.get());
    }

    /**
     * Test if deleting a class also deletes its dependency.
     */
    public void testDeleteModelelementDependencyClient() {
        MModel model =
            (MModel) nsumlmodel.getModelManagementFactory().createModel();
        Object class1 = nsumlmodel.getCoreFactory().buildClass(model);
        Object class2 = nsumlmodel.getCoreFactory().buildClass(model);
        MDependency dep =
            (MDependency)
            	nsumlmodel.getCoreFactory().buildDependency(class1, class2);
        MClass class3 = (MClass) nsumlmodel.getCoreFactory().buildClass(model);
        dep.addClient(class3);
        WeakReference class1wr = new WeakReference(class1);
        WeakReference depwr = new WeakReference(dep);
        nsumlmodel.getUmlFactory().delete(class1);
        class1 = null;
        dep = null;
        System.gc();
        assertNull("class not removed", class1wr.get());
        assertNotNull("dependency removed", depwr.get());
    }

    /**
     * Test if deleting a class also deletes its dependency.
     */
    public void testDeleteModelelementDependencySupplier() {
        MModel model =
            (MModel) nsumlmodel.getModelManagementFactory().createModel();
        Object class1 = nsumlmodel.getCoreFactory().buildClass(model);
        Object class2 = nsumlmodel.getCoreFactory().buildClass(model);
        MDependency dep =
            (MDependency)
            	nsumlmodel.getCoreFactory().buildDependency(class1, class2);
        MClass class3 = (MClass) nsumlmodel.getCoreFactory().buildClass(model);
        dep.addSupplier(class3);
        WeakReference class1wr = new WeakReference(class1);
        WeakReference depwr = new WeakReference(dep);
        nsumlmodel.getUmlFactory().delete(class1);
        class1 = null;
        dep = null;
        System.gc();
        assertNull("class not removed", class1wr.get());
        assertNotNull("dependency removed", depwr.get());
    }

    /**
     * Construct a class, with two self associations and delete the class.
     * Test if both associations were deleted in the process.
     */
    public void testDeleteModelelementClassSelfAssociations() {
        MModel model = 
            (MModel) nsumlmodel.getModelManagementFactory().createModel();
        Object class1 = nsumlmodel.getCoreFactory().buildClass(model);
        MAssociation assoc1 =
            (MAssociation)
            	nsumlmodel.getCoreFactory().buildAssociation(class1, class1);
        MAssociation assoc2 =
            (MAssociation)
            	nsumlmodel.getCoreFactory().buildAssociation(class1, class1);
        WeakReference class1wr = new WeakReference(class1);
        WeakReference assoc1wr = new WeakReference(assoc1);
        WeakReference assoc2wr = new WeakReference(assoc2);
        nsumlmodel.getUmlFactory().delete(class1);
        class1 = null;
        assoc1 = null;
        assoc2 = null;
        System.gc();
        assertNull("class not removed", class1wr.get());
        assertNull("assoc1 not removed", assoc1wr.get());
        assertNull("assoc2 not removed", assoc2wr.get());
    }

    /**
     * Test buildConstraint().
     */
    public void testBuildConstraint() {
	try {
	    nsumlmodel.getCoreFactory().buildConstraint(null);
	    fail("IllegalArgumentException should be thrown");
	} catch (IllegalArgumentException i) {
	    // Expected IllegalArgumentException seen
	}
	MModelElement elem =
	    (MModel) nsumlmodel.getModelManagementFactory().createModel();
	MConstraint con =
	    (MConstraint) nsumlmodel.getCoreFactory().buildConstraint(elem);
	assertNull("Namespace is unexpectly set", con.getNamespace());
	assertTrue(
		   "Constrained element is not set",
		   !con.getConstrainedElements().isEmpty());
	assertTrue("Constraint is not set", !elem.getConstraints().isEmpty());
	elem.setNamespace(
	        (MNamespace) nsumlmodel.getCoreFactory().createNamespace());
	con = (MConstraint) nsumlmodel.getCoreFactory().buildConstraint(elem);
	assertNotNull("Namespace is not set", con.getNamespace());
    }

    /**
     * @return Returns the allModelElements.
     */
    static String[] getAllModelElements() {
        return allModelElements;
    }
}
