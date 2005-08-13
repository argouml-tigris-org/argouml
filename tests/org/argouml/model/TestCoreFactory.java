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
     * Test creation.
     */
    public void testCreates() {
	// See issue #3407 for the elements not tested. 
        // These are the abstract meta classes,
    	// which cannot be instantiated.
    	// Ludo: I want to preserve the existing tests for NSUML, 
        // even if this is wrong regarding
    	// the UML specs to instantiates abstract classes like RelationShip. 
        // So do a switch when needed.

    	String nsumlImpl = "org.argouml.model.uml.NSUMLModelImplementation";
    	// Here we determine the UML version by looking at the model
    	// implementation in use. This is the right test in this case
    	// because we can imagine that a model implementation for UML 1.3
    	// other than NSUML (like the UML 1.3 metamodel 
        // of the OMG [01-12-02.xml])
    	// doesn't allow to instantiate the Namespace and others metaclasses.
    	boolean nsuml = nsumlImpl.equals(System.getProperty(
                    "argouml.model.implementation", nsumlImpl));    	
    	//Here we determine if the implementation support UML 1.4
    	boolean UML_14 = (Model.getMetaTypes().getTagDefinition()!=null);
    	
	Collection objs = new Vector();
	
	// The abstract metaclasses in UML 1.3 include Element, ModelElement, 
        // Feature, Namespace, GeneralizableElement, Classifier, 
        // StructuralFeature, BehavioralFeature, Relationship, 
        // PresentationElement, Action, StateVertex, and
    	// Event.
        // UML 1.4 changes Instance and State to be abstract also.

	objs.add("Abstraction");
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
	if (nsuml) {
	    objs.add("Namespace");
	}
	objs.add("Node");
	objs.add("Operation");
	objs.add("Parameter");
	objs.add("Permission");
	if (nsuml) {
	    objs.add("Relationship");
	}
	objs.add("TemplateParameter");
	objs.add("Usage");
	
	CheckUMLModelHelper.createAndRelease(
	    Model.getCoreFactory(),
	    // +1 in the size of the array because we also test the null value
	    (String[]) objs.toArray(new String[objs.size() + 1]));
    
	/*if (NSUML) {
		objs = new String[] {
	    // "Abstraction",
	    // "Association",
	    // "AssociationClass",
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
	    // "Dependency",

	    // "Element",
	    "ElementResidence",

	    // "Feature",
	    "Flow",
	    // "Generalization",
	    "Interface", "Method",

	    // "ModelElement",
	    "Namespace", "Node", "Operation", "Parameter", "Permission",

	    // "PresentationElement",
	    "Relationship",

	    // "StructuralFeature",
	    "TemplateParameter", "Usage",

	    null,
	};
	} else {
		
	}*/
    }

    /**
     * Test if deleting a classifier does also delete its association.
     */
    public void testDeleteClassifier1() {
        Object model = Model.getModelManagementFactory().createModel();
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);
        Object assoc =
            Model.getCoreFactory().buildAssociation(class1, class2);
        WeakReference class1wr = new WeakReference(class1);
        WeakReference assocwr = new WeakReference(assoc);
        Model.getUmlFactory().delete(class1);
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
        Object model = Model.getModelManagementFactory().createModel();
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);
        Object assoc =
            Model.getCoreFactory().buildAssociation(class1, class2);
        Model.getCoreHelper().addConnection(assoc,
                Model.getCoreFactory().createAssociationEnd());
        WeakReference class1wr = new WeakReference(class1);
        WeakReference assocwr = new WeakReference(assoc);
        Model.getUmlFactory().delete(class1);
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
        Object model = Model.getModelManagementFactory().createModel();
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);
        Object dep =
            Model.getCoreFactory().buildDependency(class1, class2);
        WeakReference class1wr = new WeakReference(class1);
        WeakReference depwr = new WeakReference(dep);
        Model.getUmlFactory().delete(class1);
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
        Object model = Model.getModelManagementFactory().createModel();
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);
        Object dep =
            Model.getCoreFactory().buildDependency(class1, class2);
        Object class3 = Model.getCoreFactory().buildClass(model);
        Model.getCoreHelper().addClient(dep, class3);
        WeakReference class1wr = new WeakReference(class1);
        WeakReference depwr = new WeakReference(dep);
        Model.getUmlFactory().delete(class1);
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
        Object model = Model.getModelManagementFactory().createModel();
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);
        Object dep =
            Model.getCoreFactory().buildDependency(class1, class2);
        Object class3 = Model.getCoreFactory().buildClass(model);
        Model.getCoreHelper().addSupplier(dep, class3);
        WeakReference class1wr = new WeakReference(class1);
        WeakReference depwr = new WeakReference(dep);
        Model.getUmlFactory().delete(class1);
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
        Object model = Model.getModelManagementFactory().createModel();
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object assoc1 =
            Model.getCoreFactory().buildAssociation(class1, class1);
        Object assoc2 =
            Model.getCoreFactory().buildAssociation(class1, class1);
        WeakReference class1wr = new WeakReference(class1);
        WeakReference assoc1wr = new WeakReference(assoc1);
        WeakReference assoc2wr = new WeakReference(assoc2);
        Model.getUmlFactory().delete(class1);
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
	    Model.getCoreFactory().buildConstraint(null);
	    fail("IllegalArgumentException should be thrown");
	} catch (IllegalArgumentException i) {
	    // Expected IllegalArgumentException seen
	}
	Object elem = Model.getModelManagementFactory().createModel();
	Object con = Model.getCoreFactory().buildConstraint(elem);
	assertNull("Namespace is unexpectly set",
            Model.getFacade().getNamespace(con));
	assertTrue(
		   "Constrained element is not set",
		   !Model.getFacade().getConstrainedElements(con).isEmpty());
	assertTrue("Constraint is not set",
	        !Model.getFacade().getConstraints(elem).isEmpty());
	Model.getCoreHelper().setNamespace(elem,
	//see issue #3407 for the use of a UmlPackage instead of a Namespace
	        Model.getModelManagementFactory().createPackage());
	con = Model.getCoreFactory().buildConstraint(elem);
	assertNotNull("Namespace is not set",
	        Model.getFacade().getNamespace(con));
    }

    /**
     * @return Returns the allModelElements.
     */
    static String[] getAllModelElements() {
        return allModelElements;
    }
}
