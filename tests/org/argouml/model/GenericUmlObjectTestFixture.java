// $Id$
// Copyright (c) 2003-2007 The Regents of the University of California. All
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

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;

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
     */
    public GenericUmlObjectTestFixture(String arg0) {
	super(arg0);
        InitializeModel.initializeDefault();
    }

    /**
     * Set the entity.
     *
     * @param ent the model entity
     */
    public void setEntity(Object ent) {
	entity = ent;
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
	super.tearDown();
        if (!validated) {
	    fail("Test case did not self-validate itself");
        }
    }

    private void assertNotProhibitedClass(Class c) {
        if (c != null && !c.getName().startsWith("org.argouml")) {
	    fail("Found non-ArgoUML class " + c.getName());
        }
    }

    /**
     * Ensure that the calling class does not reference non-ArgoUML classes in
     * any way.
     * 
     * @param self
     *            the testcase
     */
    protected void validateTestClassIsGeneric(TestCase self) {
        // TODO: Use reflection against the test case to ensure that it
        // does not import non-ArgoUML classes

	// Check superclass.
        //Make sure this class does not extend or reference non-ArgoUML classes
        assertNotProhibitedClass(self.getClass().getSuperclass());

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
	runTruthTest(Model.getFacade().isAModelElement(o), true, null);
	runTruthTest(
	        Model.getFacade().isAAbstraction(o),
	        Model.getMetaTypes().getAbstraction());
	runTruthTest(
	        Model.getFacade().isAAssociation(o),
	        Model.getMetaTypes().getAssociation());
	runTruthTest(Model.getFacade().isAAssociationEnd(o),
	        Model.getMetaTypes().getAssociationEnd());
	runTruthTest(Model.getFacade().isAAssociationRole(o),
	        Model.getMetaTypes().getAssociationRole());
	runTruthTest(Model.getFacade().isAClass(o),
	        Model.getMetaTypes().getUMLClass());
	runTruthTest(
	        Model.getFacade().isAClassifier(o),
	        Model.getMetaTypes().getClassifier());
	runTruthTest(Model.getFacade().isAComment(o),
	        Model.getMetaTypes().getComment());
	runTruthTest(
	        Model.getFacade().isAComponent(o),
	        Model.getMetaTypes().getComponent());
	runTruthTest(Model.getFacade().isAComponentInstance(o),
            Model.getMetaTypes().getComponentInstance());
	runTruthTest(
	        Model.getFacade().isADataType(o),
	        Model.getMetaTypes().getDataType());
	runTruthTest(
	        Model.getFacade().isADependency(o),
	        Model.getMetaTypes().getDependency());
	runTruthTest(Model.getFacade().isACompositeState(o),
	        Model.getMetaTypes().getCompositeState());

	// Not currently present as token in Model.getFacade().
	// NIF: runTruthTest(Model.getFacade().isAElement(o),
	//                   Model.getFacade().ELEMENT);
	// NIF: runTruthTest(Model.getFacade().isAExpression(o),
	//           Model.getFacade().EXPRESSION);
	// NIF: runTruthTest(Model.getFacade().isAExtensionPoint(o),
	//           Model.getFacade().EXTENSION_POINT);
	// NIF: runTruthTest(Model.getFacade().isAFeature(o),
	//                   Model.getFacade().FEATURE);
	runTruthTest(Model.getFacade().isAGeneralizableElement(o),
	        Model.getMetaTypes().getGeneralizableElement());
	runTruthTest(Model.getFacade().isAGeneralization(o),
	        Model.getMetaTypes().getGeneralization());

        runTruthTest(Model.getFacade().isAInstance(o),
                Model.getMetaTypes().getInstance());
	// NIF: runTruthTest(Model.getFacade().isAInteraction(o),
	//           Model.getFacade().INTERACTION);

	runTruthTest(
	        Model.getFacade().isAInterface(o),
	        Model.getMetaTypes().getInterface());
	runTruthTest(Model.getFacade().isALink(o),
	        Model.getMetaTypes().getLink());
        // NIF: unTruthTest(Model.getFacade().isAMethod(o),
        //	Model.getMetaTypes().getMethod());
	runTruthTest(Model.getFacade().isAModel(o),
	        Model.getMetaTypes().getModel());
	runTruthTest(
	        Model.getFacade().isAModelElement(o),
	        Model.getMetaTypes().getModelElement());
	runTruthTest(
	        Model.getFacade().isANamespace(o),
	        Model.getMetaTypes().getNamespace());
	runTruthTest(Model.getFacade().isANode(o),
	        Model.getMetaTypes().getNode());
	runTruthTest(
	        Model.getFacade().isANodeInstance(o),
	        Model.getMetaTypes().getNodeInstance());

	runTruthTest(Model.getFacade().isAOperation(o),
	        Model.getMetaTypes().getOperation());
	
	runTruthTest(Model.getFacade().isAObject(o),
	        Model.getMetaTypes().getObject());

	runTruthTest(
	        Model.getFacade().isAPermission(o),
	        Model.getMetaTypes().getPermission());
	runTruthTest(Model.getFacade().isAPackage(o),
	        Model.getMetaTypes().getPackage());
	runTruthTest(
	        Model.getFacade().isAReception(o),
	        Model.getMetaTypes().getReception());
        //	runTruthTest(Model.getFacade().isARelationship(o),
        //	        Model.getMetaTypes().getRelationship());
	runTruthTest(
	        Model.getFacade().isAStateMachine(o),
	        Model.getMetaTypes().getStateMachine());
	runTruthTest(
	        Model.getFacade().isAStateVertex(o),
	        Model.getMetaTypes().getStateVertex());
	runTruthTest(
	        Model.getFacade().isAStereotype(o),
	        Model.getMetaTypes().getStereotype());
        // NIF: runTruthTest(Model.getFacade().isAStructuralFeature(o),
        //          Model.getMetaTypes().getStructuralFeature());
        // NIF: runTruthTest(Model.getFacade().isATaggedValue(o),
        //          Model.getMetaTypes().getTaggedValue());
	runTruthTest(
	        Model.getFacade().isATransition(o),
	        Model.getMetaTypes().getTransition());
	runTruthTest(Model.getFacade().isAUseCase(o),
	        Model.getMetaTypes().getUseCase());
	runTruthTest(Model.getFacade().isAActor(o),
	        Model.getMetaTypes().getActor());
    }

    /**
     * @param result
     * @param expected
     * @param class1
     */
    private void runTruthTest(boolean result, boolean expected,
            Object class1) {
	assertEquals("Failure testing " + class1,
		     expected,
		     result);
    }

    /**
     * @param result
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
