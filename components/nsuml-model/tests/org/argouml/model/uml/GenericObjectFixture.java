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

import org.argouml.api.FacadeManager;
import org.argouml.api.ObjectFactoryManager;
import org.argouml.api.model.uml.Uml;
import org.argouml.api.model.uml.UmlModelFacade;
import org.argouml.api.model.uml.UmlObjectFactory;

import junit.framework.TestCase;

/**
 * @author Thierry
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class GenericObjectFixture extends TestCase
{

    private HashMap truths = new HashMap();
    
	private UmlObjectFactory factory;

	private UmlModelFacade facade;
	
	private boolean validated = false;

	/**
	 * Constructor for GenericObjectFixture.
	 * @param arg0
	 */
	public GenericObjectFixture(String arg0)
	{
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		System.setProperty(FacadeManager.UML_MODEL_FACADE_PROPERTY,
						   NsumlModelFacade.class.getName());

		Object o = FacadeManager.getUmlFacade();
		assertNotNull("Unable to get UML Model facade", o);
		assertEquals("Didn't get the correct UML Model facade",
					 o.getClass(), NsumlModelFacade.class);
		facade = (UmlModelFacade)o;

		System.setProperty(FacadeManager.UML_MODEL_FACADE_PROPERTY,
						   NsumlObjectFactory.class.getName());
		o = ObjectFactoryManager.getUmlFactory();
		assertNotNull("Unable to get UML Model factory", o);
		assertEquals("Didn't get the correct UML Model factory",
					 o.getClass(), NsumlObjectFactory.class);
		factory = (UmlObjectFactory)o;

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
        
		// Check superclass
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
	 * @return
	 */
	public UmlModelFacade getFacade() {
		return facade;
	}

	/**
	 * @return
	 */
	public UmlObjectFactory getFactory() {
		return factory;
	}

	/**
	 * Indicate if the class being tested should be a type of the testcase class.
	 * 
	 * @param class1
	 * @param b
	 */
	protected void setTruth(Class class1, boolean b) {
        truths.put(class1, new Boolean(b));
	}


    protected void runTruthTests(Object o) {
		runTruthTest(facade.isABase(o), true);
		runTruthTest(facade.isAAbstraction(o), Uml.ABSTRACTION);
		runTruthTest(facade.isAAssociation(o), Uml.ASSOCIATION);
		runTruthTest(facade.isAAssociationEnd(o), Uml.ASSOCIATION_END);
		runTruthTest(facade.isAAssociationRole(o), Uml.ASSOCIATION_ROLE);
		runTruthTest(facade.isAClass(o), Uml.CLASS);
		runTruthTest(facade.isAClassifier(o), Uml.CLASSIFIER);
		runTruthTest(facade.isAComment(o), Uml.COMMENT);
		runTruthTest(facade.isAComponent(o), Uml.COMPONENT);
		runTruthTest(facade.isAComponentInstance(o), Uml.COMPONENT_INSTANCE);
		runTruthTest(facade.isADataType(o), Uml.DATATYPE);
		runTruthTest(facade.isADependency(o), Uml.DEPENDENCY);
		runTruthTest(facade.isACompositeState(o), Uml.COMPOSITE_STATE);
		runTruthTest(facade.isAElement(o), Uml.ELEMENT);
		runTruthTest(facade.isAExpression(o), Uml.EXPRESSION);
		runTruthTest(facade.isAExtensionPoint(o), Uml.EXTENSION_POINT);
		runTruthTest(facade.isAFeature(o), Uml.FEATURE);
		runTruthTest(facade.isAGeneralizableElement(o), Uml.GENERALIZABLE_ELEMENT);
		runTruthTest(facade.isAGeneralization(o), Uml.GENERALIZATION);
		runTruthTest(facade.isAInstance(o), Uml.INSTANCE);
		runTruthTest(facade.isAInteraction(o), Uml.INTERACTION);
		runTruthTest(facade.isAInterface(o), Uml.INTERFACE);
		runTruthTest(facade.isALink(o), Uml.LINK);
		runTruthTest(facade.isAMethod(o), Uml.METHOD);
		runTruthTest(facade.isAModel(o), Uml.MODEL);
		runTruthTest(facade.isAModelElement(o), Uml.MODEL_ELEMENT);
		runTruthTest(facade.isANamespace(o), Uml.NAMESPACE);
		runTruthTest(facade.isANode(o), Uml.NODE);
		runTruthTest(facade.isANodeInstance(o), Uml.NODE_INSTANCE);
		runTruthTest(facade.isAOperation(o), Uml.OPERATION);
		runTruthTest(facade.isAObject(o), Uml.OBJECT);
		runTruthTest(facade.isAPermission(o), Uml.PERMISSION);
		runTruthTest(facade.isAPackage(o), Uml.PACKAGE);
		runTruthTest(facade.isAReception(o), Uml.RECEPTION);
		runTruthTest(facade.isARelationship(o), Uml.RELATIONSHIP);
		runTruthTest(facade.isAStateMachine(o), Uml.STATE_MACHINE);
		runTruthTest(facade.isAStateVertex(o), Uml.STATE_VERTEX);
		runTruthTest(facade.isAStereotype(o), Uml.STEREOTYPE);
		runTruthTest(facade.isAStructuralFeature(o), Uml.STRUCTURAL_FEATURE);
		runTruthTest(facade.isATaggedValue(o), Uml.TAGGED_VALUE);
		runTruthTest(facade.isATransition(o), Uml.TRANSITION);
		runTruthTest(facade.isAUseCase(o), Uml.USE_CASE);
		runTruthTest(facade.isAActor(o), Uml.ACTOR);
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
	private void runTruthTest(boolean result, Class class1) {
        // TODO Look up class in truth table
        Boolean truth = (Boolean)truths.get(class1);
        if (truth == null) {
        	truth = Boolean.FALSE;        
        }
        runTruthTest(result, truth.booleanValue());
	}
}
