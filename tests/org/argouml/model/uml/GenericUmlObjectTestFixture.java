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

package org.argouml.model.uml;

import java.util.HashMap;

import javax.jmi.reflect.RefAssociation;
import javax.jmi.reflect.RefAssociationLink;
import javax.jmi.reflect.RefBaseObject;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefEnum;
import javax.jmi.reflect.RefFeatured;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;
import javax.jmi.reflect.RefStruct;

import org.argouml.model.ModelEntity;
import org.argouml.model.ModelFacade;
import org.argouml.ui.NavigatorPane;

import junit.framework.TestCase;

/**
 * @author Thierry Lach
 */
public abstract class GenericUmlObjectTestFixture extends TestCase
{

    private HashMap truths = new HashMap();
    
	private boolean validated = false;

	private ModelEntity entity;

	/**
	 * Constructor for GenericObjectFixture.
	 * @param arg0
	 */
	public GenericUmlObjectTestFixture(String arg0,
	                                ModelEntity ent)
	{
		super(arg0);
		entity = ent;
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		NavigatorPane.setInstance(null);
		initializeTruth();
	}

	/**
	 * Called to set up the truth table.
	 */
	protected abstract void initializeTruth();

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
        if (! validated) {
        	fail ("Test case did not self-validate itself");
        }
	}

    private void assertNotNsumlClass(Class c) {
        if (c != null && c.getName().startsWith("ru.novosoft")) {
        	fail ("Found nsuml class " + c.getName());
        }
    }

    /**
     * Ensure that the calling class does not reference NSUML in any way
     */
    protected void validateTestClassIsGeneric(TestCase self) {
        // TODO Use reflection against the test case to ensure that it does not import nsuml
        
		// Check superclass.
        //Make sure this class does not extend or reference nsuml.
        assertNotNsumlClass(self.getClass().getSuperclass());
        
		// Enumerate declaring class
        
		// Enumerate constructors
        
        // Enumerate methods
        // Enumerate method arguments
        
        // Enumerate fields

        // Indicate that the test was validated
        validated = true;
    }

	/**
	 * Indicate that the class being tested should be a type of the passed class.
	 * 
	 * @param class1
	 */
	protected void setShouldBe(ModelEntity class1) {
		truths.put(class1, new Boolean(true));
	}

	/**
	 * Indicate if the class being tested should be a type of the testcase class.
	 * 
	 * @param class1
	 * @param b
	 */
	protected void setTruth(ModelEntity class1, boolean b) {
		truths.put(class1, new Boolean(b));
	}

	/**
	 * Run the standard tests against a RefBaseObject
	 */
	protected void runTestRefBaseObject(Object o) {
		assertTrue("Should be a RefBaseObject", o instanceof RefBaseObject);
		RefBaseObject rbo = (RefBaseObject)o;
		// TODO test method refMofId());
		// TODO test method refImmediatePackage()
		// TODO test method refMetaObject()
		// TODO test method refOutermostPackage()
		// TODO test method refVerifyConstraints(true));
		// TODO test method refVerifyConstraints(false));
	}

	/**
	 * Run the standard tests against a RefPackage
	 */
	protected void runTestRefPackage(Object o) {
		assertTrue("Should be a RefPackage", o instanceof RefPackage);
		RefPackage rp = (RefPackage)o;
		// TODO test method refAllAssociations()
		// TODO test method refAllClasses()
		// TODO test method refAllPackages()
		// TODO implement additional method tests
	}

	/**
	 * Run the standard tests against a RefClass
	 */
	protected void runTestRefClass(Object o) {
		assertTrue("Should be a RefClass", o instanceof RefClass);
		RefClass rc = (RefClass)o;
		// TODO implement additional method tests
	}

	/**
	 * Run the standard tests against a RefObject
	 */
	protected void runTestRefObject(Object o) {
		assertTrue("Should be a RefObject", o instanceof RefObject);
		RefObject ro = (RefObject)o;
		// TODO implement additional method tests
	}

	/**
	 * Run the standard tests against a RefEnum
	 */
	protected void runTestRefEnum(Object o) {
		assertTrue("Should be a RefEnum", o instanceof RefEnum);
		RefEnum ro = (RefEnum)o;
		// TODO implement additional method tests
	}

	/**
	 * Run the standard tests against a RefAssociation
	 */
	protected void runTestRefAssociation(Object o) {
		assertTrue("Should be a RefAssociation", o instanceof RefAssociation);
		RefAssociation ro = (RefAssociation)o;
		// TODO implement additional method tests
	}

	/**
	 * Run the standard tests against a RefFeatured
	 */
	protected void runTestRefFeatured(Object o) {
		assertTrue("Should be a RefFeatured", o instanceof RefFeatured);
		RefFeatured ro = (RefFeatured)o;
		// TODO implement additional method tests
	}

    protected void runTruthTests(Object o) {
		runTruthTest(ModelFacade.isABase(o), true, null);
		runTruthTest(ModelFacade.isAAbstraction(o), Uml.ABSTRACTION);
		runTruthTest(ModelFacade.isAAssociation(o), Uml.ASSOCIATION);
		runTruthTest(ModelFacade.isAAssociationEnd(o), Uml.ASSOCIATION_END);
		runTruthTest(ModelFacade.isAAssociationRole(o), Uml.ASSOCIATION_ROLE);
		runTruthTest(ModelFacade.isAClass(o), Uml.CLASS);
		runTruthTest(ModelFacade.isAClassifier(o), Uml.CLASSIFIER);
		runTruthTest(ModelFacade.isAComment(o), Uml.COMMENT);
		runTruthTest(ModelFacade.isAComponent(o), Uml.COMPONENT);
		runTruthTest(ModelFacade.isAComponentInstance(o), Uml.COMPONENT_INSTANCE);
		runTruthTest(ModelFacade.isADataType(o), Uml.DATATYPE);
		runTruthTest(ModelFacade.isADependency(o), Uml.DEPENDENCY);
		runTruthTest(ModelFacade.isACompositeState(o), Uml.COMPOSITE_STATE);
		runTruthTest(ModelFacade.isAElement(o), Uml.ELEMENT);
		runTruthTest(ModelFacade.isAExpression(o), Uml.EXPRESSION);
		runTruthTest(ModelFacade.isAExtensionPoint(o), Uml.EXTENSION_POINT);
		runTruthTest(ModelFacade.isAFeature(o), Uml.FEATURE);
		runTruthTest(ModelFacade.isAGeneralizableElement(o), Uml.GENERALIZABLE_ELEMENT);
		runTruthTest(ModelFacade.isAGeneralization(o), Uml.GENERALIZATION);

		// TODO this fails on ModelElement - why?
		// runTruthTest(ModelFacade.isAInstance(o), Uml.INSTANCE);
		runTruthTest(ModelFacade.isAInteraction(o), Uml.INTERACTION);

		runTruthTest(ModelFacade.isAInterface(o), Uml.INTERFACE);
		runTruthTest(ModelFacade.isALink(o), Uml.LINK);
		runTruthTest(ModelFacade.isAMethod(o), Uml.METHOD);
		runTruthTest(ModelFacade.isAModel(o), Uml.MODEL);
		runTruthTest(ModelFacade.isAModelElement(o), Uml.MODEL_ELEMENT);
		runTruthTest(ModelFacade.isANamespace(o), Uml.NAMESPACE);
		runTruthTest(ModelFacade.isANode(o), Uml.NODE);
		runTruthTest(ModelFacade.isANodeInstance(o), Uml.NODE_INSTANCE);

		// TODO this fails on ModelElement - why?
		// runTruthTest(ModelFacade.isAOperation(o), Uml.OPERATION);

		// TODO this fails on ModelElement - why?
		// runTruthTest(ModelFacade.isAObject(o), Uml.OBJECT);

		runTruthTest(ModelFacade.isAPermission(o), Uml.PERMISSION);
		runTruthTest(ModelFacade.isAPackage(o), Uml.PACKAGE);
		runTruthTest(ModelFacade.isAReception(o), Uml.RECEPTION);
		runTruthTest(ModelFacade.isARelationship(o), Uml.RELATIONSHIP);
		runTruthTest(ModelFacade.isAStateMachine(o), Uml.STATE_MACHINE);
		runTruthTest(ModelFacade.isAStateVertex(o), Uml.STATE_VERTEX);
		runTruthTest(ModelFacade.isAStereotype(o), Uml.STEREOTYPE);
		runTruthTest(ModelFacade.isAStructuralFeature(o), Uml.STRUCTURAL_FEATURE);
		runTruthTest(ModelFacade.isATaggedValue(o), Uml.TAGGED_VALUE);
		runTruthTest(ModelFacade.isATransition(o), Uml.TRANSITION);
		runTruthTest(ModelFacade.isAUseCase(o), Uml.USE_CASE);
		runTruthTest(ModelFacade.isAActor(o), Uml.ACTOR);
    }

	/**
	 * @param b
	 * @param c
	 */
	private void runTruthTest(boolean result, boolean expected, ModelEntity class1) {
		assertEquals("Failure testing " +
		 (class1 == null ? "MBase" : class1.getName()),
		  expected, result);
	}

	/**
	 * @param class1
	 */
	private void runTruthTest(boolean result, ModelEntity class1) {
        // TODO Look up class in truth table
        Boolean truth = (Boolean)truths.get(class1);
        if (truth == null) {
        	truth = Boolean.FALSE;        
        }
        runTruthTest(result, truth.booleanValue(), class1);
	}

	/**
	 * @return
	 */
	public ModelEntity getEntity() {
		return entity;
	}

}
