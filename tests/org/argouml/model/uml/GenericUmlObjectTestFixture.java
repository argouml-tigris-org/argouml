// $Id$
// Copyright (c) 2003-2005 The Regents of the University of California. All
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

import junit.framework.TestCase;

import org.argouml.model.ModelFacade;
import org.argouml.ui.NavigatorPane;

/**
 * @author Thierry Lach
 */
public abstract class GenericUmlObjectTestFixture extends TestCase {

    private HashMap truths = new HashMap();

    private boolean validated = false;

    private Object entity;

    /**
     * Constructor for GenericObjectFixture.
     *
     * @param arg0 is the name of the test case.
     * @param ent the model entity
     */
    public GenericUmlObjectTestFixture(String arg0, Object ent) {
	super(arg0);
	entity = ent;
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
	super.setUp();
	NavigatorPane.setInstance(null);
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
	super.tearDown();
        if (!validated) {
	    fail("Test case did not self-validate itself");
        }
    }

    private void assertNotNsumlClass(Class c) {
        if (c != null && c.getName().startsWith("ru.novosoft")) {
	    fail("Found nsuml class " + c.getName());
        }
    }

    /**
     * Ensure that the calling class does not reference NSUML in any way.
     *
     * @param self the testcase
     */
    protected void validateTestClassIsGeneric(TestCase self) {
        // TODO: Use reflection against the test case to ensure that it
        // does not import nsuml

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
     * Indicate if the class being tested should be a type of the
     * testcase class.
     *
     * @param class1 the model element
     * @param b the value
     */
    protected void setTruth(Object class1, boolean b) {
	truths.put(class1, new Boolean(b));
    }

    /**
     * @param o the given object for the tests
     */
    protected void runTruthTests(Object o) {
	runTruthTest(ModelFacade.isABase(o), true, null);
	runTruthTest(
	        ModelFacade.isAAbstraction(o),
	        ModelFacade.getAbstractionToken());
	runTruthTest(
	        ModelFacade.isAAssociation(o),
	        ModelFacade.getAssociationToken());
	runTruthTest(ModelFacade.isAAssociationEnd(o),
	        ModelFacade.getAssociationEndToken());
	runTruthTest(ModelFacade.isAAssociationRole(o),
	        ModelFacade.getAssociationRoleToken());
	runTruthTest(ModelFacade.isAClass(o), ModelFacade.getClassToken());
	runTruthTest(
	        ModelFacade.isAClassifier(o),
	        ModelFacade.getClassifierToken());
	runTruthTest(ModelFacade.isAComment(o), ModelFacade.getCommentToken());
	runTruthTest(
	        ModelFacade.isAComponent(o),
	        ModelFacade.getComponentToken());
	runTruthTest(ModelFacade.isAComponentInstance(o),
            ModelFacade.getComponentInstanceToken());
	runTruthTest(
	        ModelFacade.isADataType(o),
	        ModelFacade.getDatatypeToken());
	runTruthTest(
	        ModelFacade.isADependency(o),
	        ModelFacade.getDependencyToken());
	runTruthTest(ModelFacade.isACompositeState(o),
	        ModelFacade.getCompositeStateToken());

	// Not currently present as token in ModelFacade.
	// NIF: runTruthTest(ModelFacade.isAElement(o), ModelFacade.ELEMENT);
	// NIF: runTruthTest(ModelFacade.isAExpression(o),
	//           ModelFacade.EXPRESSION);
	// NIF: runTruthTest(ModelFacade.isAExtensionPoint(o),
	//           ModelFacade.EXTENSION_POINT);
	// NIF: runTruthTest(ModelFacade.isAFeature(o), ModelFacade.FEATURE);
	runTruthTest(ModelFacade.isAGeneralizableElement(o),
	        ModelFacade.getGeneralizableElementToken());
	runTruthTest(ModelFacade.isAGeneralization(o),
	        ModelFacade.getGeneralizationToken());

	// TODO: this fails on ModelElement - why?
	// runTruthTest(ModelFacade.isAInstance(o), ModelFacade.INSTANCE);
	// NIF: runTruthTest(ModelFacade.isAInteraction(o),
	//           ModelFacade.INTERACTION);

	runTruthTest(
	        ModelFacade.isAInterface(o),
	        ModelFacade.getInterfaceToken());
	runTruthTest(ModelFacade.isALink(o), ModelFacade.getLinkToken());
	// NIF: runTruthTest(ModelFacade.isAMethod(o), ModelFacade.METHOD);
	runTruthTest(ModelFacade.isAModel(o), ModelFacade.getModelToken());
	runTruthTest(
	        ModelFacade.isAModelElement(o),
	        ModelFacade.getModelElementToken());
	runTruthTest(
	        ModelFacade.isANamespace(o),
	        ModelFacade.getNamespaceToken());
	runTruthTest(ModelFacade.isANode(o), ModelFacade.getNodeToken());
	runTruthTest(
	        ModelFacade.isANodeInstance(o),
	        ModelFacade.getNodeInstanceToken());

	// TODO: this fails on ModelElement - why?
	// runTruthTest(ModelFacade.isAOperation(o), ModelFacade.OPERATION);

	// TODO: this fails on ModelElement - why?
	// runTruthTest(ModelFacade.isAObject(o), ModelFacade.OBJECT);

	runTruthTest(
	        ModelFacade.isAPermission(o),
	        ModelFacade.getPermissionToken());
	runTruthTest(ModelFacade.isAPackage(o), ModelFacade.getPackageToken());
	runTruthTest(
	        ModelFacade.isAReception(o),
	        ModelFacade.getReceptionToken());
	// NIF: runTruthTest(ModelFacade.isARelationship(o),
	//           ModelFacade.RELATIONSHIP);
	runTruthTest(
	        ModelFacade.isAStateMachine(o),
	        ModelFacade.getStateMachineToken());
	runTruthTest(
	        ModelFacade.isAStateVertex(o),
	        ModelFacade.getStateVertexToken());
	runTruthTest(
	        ModelFacade.isAStereotype(o),
	        ModelFacade.getStereotypeToken());
	// NIF: runTruthTest(ModelFacade.isAStructuralFeature(o),
        //     ModelFacade.STRUCTURAL_FEATURE);
	// NIF: runTruthTest(ModelFacade.isATaggedValue(o),
	//           ModelFacade.TAGGED_VALUE);
	runTruthTest(
	        ModelFacade.isATransition(o),
	        ModelFacade.getTransitionToken());
	runTruthTest(ModelFacade.isAUseCase(o), ModelFacade.getUseCaseToken());
	runTruthTest(ModelFacade.isAActor(o), ModelFacade.getActorToken());
    }

    /**
     * @param b
     * @param c
     */
    private void runTruthTest(boolean result, boolean expected,
            Object class1) {
	assertEquals("Failure testing " + class1,
		     expected,
		     result);
    }

    /**
     * @param class1
     */
    private void runTruthTest(boolean result, Object class1) {
        // TODO: Look up class in truth table
        Boolean truth = (Boolean) truths.get(class1);
        if (truth == null) {
	    truth = Boolean.FALSE;
        }
        runTruthTest(result, truth.booleanValue(), class1);
    }
}
