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

package org.argouml.model;

import java.util.HashMap;

import org.argouml.model.uml.Uml;
import org.argouml.ui.NavigatorPane;

import junit.framework.TestCase;

/**
 * @author Thierry Lach
 */
public abstract class GenericObjectTestFixture extends TestCase
{

    private HashMap truths = new HashMap();
    
	private boolean validated = false;

	/**
	 * Constructor for GenericObjectFixture.
	 * @param arg0
	 */
	public GenericObjectTestFixture(String arg0)
	{
		super(arg0);
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
	 * Indicate if the class being tested should be a type of the testcase class.
	 * 
	 * @param class1
	 * @param b
	 */
	protected void setTruth(ModelEntity class1, boolean b) {
        truths.put(class1, new Boolean(b));
	}


    protected void runTruthTests(Object o) {
		runTruthTest(ModelFacade.isABase(o), true);
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
		runTruthTest(ModelFacade.isAInstance(o), Uml.INSTANCE);
		runTruthTest(ModelFacade.isAInteraction(o), Uml.INTERACTION);
		runTruthTest(ModelFacade.isAInterface(o), Uml.INTERFACE);
		runTruthTest(ModelFacade.isALink(o), Uml.LINK);
		runTruthTest(ModelFacade.isAMethod(o), Uml.METHOD);
		runTruthTest(ModelFacade.isAModel(o), Uml.MODEL);
		runTruthTest(ModelFacade.isAModelElement(o), Uml.MODEL_ELEMENT);
		runTruthTest(ModelFacade.isANamespace(o), Uml.NAMESPACE);
		runTruthTest(ModelFacade.isANode(o), Uml.NODE);
		runTruthTest(ModelFacade.isANodeInstance(o), Uml.NODE_INSTANCE);
		runTruthTest(ModelFacade.isAOperation(o), Uml.OPERATION);
		runTruthTest(ModelFacade.isAObject(o), Uml.OBJECT);
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
	private void runTruthTest(boolean result, boolean expected) {
		assertEquals("Failure", expected, result);
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
        runTruthTest(result, truth.booleanValue());
	}

}
