// Copyright (c) 2002 The Regents of the University of California. All
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

// $Id$

package org.argouml.model.uml.foundation.core;

import java.lang.ref.WeakReference;

import junit.framework.TestCase;

import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.modelmanagement.ModelManagementFactory;
import org.argouml.util.CheckUMLModelHelper;

import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MConstraint;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.model_management.MModel;



public class TestCoreFactory extends TestCase {
    
    static String[] allModelElements = {
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

    public TestCoreFactory(String n) { super(n); }



    public void testSingleton() {
	Object o1 = CoreFactory.getFactory();
	Object o2 = CoreFactory.getFactory();
	assertTrue("Different singletons", o1 == o2);
    }



    public void testCreates() {
        // Do not test BehavioralFeature, Feature, PresentationElement,
        //    StructuralFeature yet.
        // NSUML does not have create method.
        //
        // Never test for ModelElement.
	String [] objs = {
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
	    "Flow",
	    "Generalization",
	    "Interface",
	    "Method",

	    // "ModelElement",
	    "Namespace",
	    "Node",
	    "Operation",
	    "Parameter",
	    "Permission",

	    // "PresentationElement",
	    "Relationship",

	    // "StructuralFeature",
	    "TemplateParameter",
	    "Usage",

	    null
	};

	CheckUMLModelHelper.createAndRelease(this, 
	    CoreFactory.getFactory(),
	    objs);
    }
    
    public void testDeleteComplete() {
        CheckUMLModelHelper.deleteComplete(this, 
        CoreFactory.getFactory(), 
        allModelElements);
    }
    
    public void testDeleteClassifier1() {
        MModel model = ModelManagementFactory.getFactory().createModel();
        MClass class1 = CoreFactory.getFactory().buildClass(model);
        MClass class2 = CoreFactory.getFactory().buildClass(model);
        MAssociation assoc = CoreFactory.getFactory().buildAssociation(class1, class2);
        WeakReference class1wr =new WeakReference(class1);
        WeakReference assocwr = new WeakReference(assoc);
        UmlFactory.getFactory().delete(class1);
        class1 = null;
        assoc = null;
        System.gc();
        assertNull("class not removed", class1wr.get());
        assertNull("binary association not removed", assocwr.get());
    }
    
    public void testDeleteClassifierAssociation() {
        MModel model = ModelManagementFactory.getFactory().createModel();
        MClass class1 = CoreFactory.getFactory().buildClass(model);
        MClass class2 = CoreFactory.getFactory().buildClass(model);
        MAssociation assoc = CoreFactory.getFactory().buildAssociation(class1, class2);
        assoc.addConnection(CoreFactory.getFactory().createAssociationEnd());
        WeakReference class1wr =new WeakReference(class1);
        WeakReference assocwr = new WeakReference(assoc);
        UmlFactory.getFactory().delete(class1);
        class1 = null;
        assoc = null;
        System.gc();
        assertNull("class not removed", class1wr.get());
        assertNotNull("association removed", assocwr.get());
    }
    
    public void testDeleteModelelementDependency() {
        MModel model = ModelManagementFactory.getFactory().createModel();
        MClass class1 = CoreFactory.getFactory().buildClass(model);
        MClass class2 = CoreFactory.getFactory().buildClass(model);
        MDependency dep = CoreFactory.getFactory().buildDependency(class1, class2);
        WeakReference class1wr =new WeakReference(class1);
        WeakReference depwr = new WeakReference(dep);
        UmlFactory.getFactory().delete(class1);
        class1 = null;
        dep = null;
        System.gc();
        assertNull("class not removed", class1wr.get());
        assertNull("binary dependency not removed", depwr.get());
    }
    
    public void testDeleteModelelementDependencyClient() {
        MModel model = ModelManagementFactory.getFactory().createModel();
        MClass class1 = CoreFactory.getFactory().buildClass(model);
        MClass class2 = CoreFactory.getFactory().buildClass(model);
        MDependency dep = CoreFactory.getFactory().buildDependency(class1, class2);
        MClass class3 = CoreFactory.getFactory().buildClass(model);
        dep.addClient(class3);
        WeakReference class1wr =new WeakReference(class1);
        WeakReference depwr = new WeakReference(dep);
        UmlFactory.getFactory().delete(class1);
        class1 = null;
        dep = null;
        System.gc();
        assertNull("class not removed", class1wr.get());
        assertNotNull("dependency removed", depwr.get());
    }
    
    public void testDeleteModelelementDependencySupplier() {
        MModel model = ModelManagementFactory.getFactory().createModel();
        MClass class1 = CoreFactory.getFactory().buildClass(model);
        MClass class2 = CoreFactory.getFactory().buildClass(model);
        MDependency dep = CoreFactory.getFactory().buildDependency(class1, class2);
        MClass class3 = CoreFactory.getFactory().buildClass(model);
        dep.addSupplier(class3);
        WeakReference class1wr =new WeakReference(class1);
        WeakReference depwr = new WeakReference(dep);
        UmlFactory.getFactory().delete(class1);
        class1 = null;
        dep = null;
        System.gc();
        assertNull("class not removed", class1wr.get());
        assertNotNull("dependency removed", depwr.get());
    }
    
    /** construct a class, with two self associations and delete */
    public void testDeleteModelelementClassSelfAssociations() {
        MModel model = ModelManagementFactory.getFactory().createModel();
        MClass class1 = CoreFactory.getFactory().buildClass(model);
        MAssociation assoc1 = 
            CoreFactory.getFactory().
            buildAssociation(class1, true, class1, true);
        MAssociation assoc2 = 
            CoreFactory.getFactory().
            buildAssociation(class1, true, class1, true);
        WeakReference class1wr = new WeakReference(class1);
        WeakReference assoc1wr = new WeakReference(assoc1);
        WeakReference assoc2wr = new WeakReference(assoc2);
        UmlFactory.getFactory().delete(class1);
        class1 = null;
        assoc1 = null;
        assoc2 = null;
        System.gc();
        assertNull("class not removed", class1wr.get());
        assertNull("assoc1 not removed", assoc1wr.get());
        assertNull("assoc2 not removed", assoc2wr.get());
    }

    public void testBuildConstraint() {
        try {
            CoreFactory.getFactory().buildConstraint(null);
            fail("IllegalArgumentException should be thrown");
        }
        catch (IllegalArgumentException i) {}
        MModelElement elem = ModelManagementFactory.getFactory().createModel();
        MConstraint con = CoreFactory.getFactory().buildConstraint(elem);
        assertNull("Namespace is unexpectly set", con.getNamespace());
        assertTrue("Constrained element is not set", 
		   !con.getConstrainedElements().isEmpty());
        assertTrue("Constraint is not set", !elem.getConstraints().isEmpty());
        elem.setNamespace(CoreFactory.getFactory().createNamespace());
        con = CoreFactory.getFactory().buildConstraint(elem);
        assertNotNull("Namespace is not set", con.getNamespace());
    }
        
        
        
    
}

    
