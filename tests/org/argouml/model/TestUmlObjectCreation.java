// $Id$
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import junit.framework.TestCase;

import org.argouml.model.uml.RefBaseObjectProxy;
import org.argouml.model.uml.Uml;
import org.argouml.ui.NavigatorPane;

import ru.novosoft.uml.behavior.collaborations.MAssociationRoleImpl;
import ru.novosoft.uml.behavior.collaborations.MClassifierRoleImpl;
import ru.novosoft.uml.behavior.common_behavior.MActionImpl;
import ru.novosoft.uml.behavior.common_behavior.MCallActionImpl;
import ru.novosoft.uml.behavior.common_behavior.MComponentInstanceImpl;
import ru.novosoft.uml.behavior.common_behavior.MInstanceImpl;
import ru.novosoft.uml.behavior.common_behavior.MLinkImpl;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstanceImpl;
import ru.novosoft.uml.behavior.common_behavior.MObjectImpl;
import ru.novosoft.uml.behavior.common_behavior.MReceptionImpl;
import ru.novosoft.uml.behavior.state_machines.MCompositeStateImpl;
import ru.novosoft.uml.behavior.state_machines.MEventImpl;
import ru.novosoft.uml.behavior.state_machines.MPseudostateImpl;
import ru.novosoft.uml.behavior.state_machines.MStateImpl;
import ru.novosoft.uml.behavior.state_machines.MStateVertexImpl;
import ru.novosoft.uml.behavior.state_machines.MTransitionImpl;
import ru.novosoft.uml.behavior.use_cases.MActorImpl;
import ru.novosoft.uml.behavior.use_cases.MExtendImpl;
import ru.novosoft.uml.behavior.use_cases.MIncludeImpl;
import ru.novosoft.uml.behavior.use_cases.MUseCaseImpl;
import ru.novosoft.uml.foundation.core.MAbstractionImpl;
import ru.novosoft.uml.foundation.core.MAssociationEndImpl;
import ru.novosoft.uml.foundation.core.MAssociationImpl;
import ru.novosoft.uml.foundation.core.MAttributeImpl;
import ru.novosoft.uml.foundation.core.MClassImpl;
import ru.novosoft.uml.foundation.core.MClassifierImpl;
import ru.novosoft.uml.foundation.core.MComponentImpl;
import ru.novosoft.uml.foundation.core.MDataTypeImpl;
import ru.novosoft.uml.foundation.core.MDependencyImpl;
import ru.novosoft.uml.foundation.core.MGeneralizationImpl;
import ru.novosoft.uml.foundation.core.MInterfaceImpl;
import ru.novosoft.uml.foundation.core.MNamespaceImpl;
import ru.novosoft.uml.foundation.core.MNodeImpl;
import ru.novosoft.uml.foundation.core.MOperationImpl;
import ru.novosoft.uml.foundation.core.MPermissionImpl;
import ru.novosoft.uml.foundation.core.MUsageImpl;
import ru.novosoft.uml.foundation.data_types.MActionExpression;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotypeImpl;
import ru.novosoft.uml.model_management.MPackageImpl;

/**
 * Test UML object creation in the model package.
 *
 */
public class TestUmlObjectCreation extends TestCase {

    /**
     * The constructor.
     * 
     * @param n the name of this test.
     */
    public TestUmlObjectCreation(String n) { super(n); }

    private void runObjectFactoryType(boolean useProxy,
				      UmlModelEntity c,
				      Class expected,
				      Object legacy,
				      boolean runTest) {
	// First test the traditional proxy
	Model.getUmlFactory().setJmiProxyCreated(useProxy);

	assertTrue("Not a valid entity: " + c.getClass(),
		   c instanceof UmlModelEntity);
		
	Object o = null;
	o = ModelFacade.create(c);
	assertNotNull("Could not create " + c.getClass().getName(), o);
		
	// Make sure that the new create() mechanism gives the same
	// class as the legacy create
	if (legacy != null) {
	    if (useProxy) {
		if (Proxy.isProxyClass(o.getClass())) {
		    InvocationHandler ih = Proxy.getInvocationHandler(o);
		    // This assumes that all objects must be proxied
		    assertTrue(o.getClass().getName() + " is not a proxy",
			       ih instanceof RefBaseObjectProxy);
		    Object proxiedObject =
			RefBaseObjectProxy.getProxiedObject(
			        (RefBaseObjectProxy) ih);
		    assertEquals("Not the correct class",
				 proxiedObject.getClass(),
				 legacy.getClass());
		}
		else {
		    fail("Did not get a proxy class for " + legacy.getClass());
		}
	    }
	    else {
		assertEquals("Not the same class",
			     o.getClass(),
			     legacy.getClass());
	    }
	}
    }
	
    private void testObjectFactoryType(UmlModelEntity c,
				       Class expected,
				       Object legacy,
				       boolean runTest) {
        // Certain objects cannot be instantiated by NSUML.
        // We allow these to be part of the test list for
        // completeness, but do not test them.
        if (!runTest) {
	    return;
        }
	runObjectFactoryType(false, c, expected, legacy, runTest);
	runObjectFactoryType(true, c, expected, legacy, runTest);
    }

    /**
     * Test the factories for UML objects.
     */
    public void testObjectFactory() {
	testObjectFactoryType(Uml.ABSTRACTION, MAbstractionImpl.class,
			      Model.getCoreFactory().createAbstraction(),
			      true);
	testObjectFactoryType(Uml.ASSOCIATION, MAssociationImpl.class,
			      Model.getCoreFactory().createAssociation(),
			      true);
	testObjectFactoryType(Uml.ASSOCIATION_ROLE, MAssociationRoleImpl.class,
			      Model.getCollaborationsFactory()
			          .createAssociationRole(),
			      true);
	testObjectFactoryType(Uml.DEPENDENCY, MDependencyImpl.class,
			      Model.getCoreFactory().createDependency(),
			      true);
	testObjectFactoryType(Uml.EXTEND, MExtendImpl.class,
			      Model.getUseCasesFactory().createExtend(),
			      true);
	testObjectFactoryType(Uml.GENERALIZATION, MGeneralizationImpl.class,
			      Model.getCoreFactory().createGeneralization(),
			      true);
	testObjectFactoryType(Uml.INCLUDE, MIncludeImpl.class,
			      Model.getUseCasesFactory().createInclude(),
			      true);
	testObjectFactoryType(Uml.LINK, MLinkImpl.class,
			      Model.getCommonBehaviorFactory().createLink(),
			      true);
	testObjectFactoryType(Uml.PERMISSION, MPermissionImpl.class,
			      Model.getCoreFactory().createPermission(),
			      true);
	testObjectFactoryType(Uml.USAGE, MUsageImpl.class,
			      Model.getCoreFactory().createUsage(),
			      true);
	testObjectFactoryType(Uml.TRANSITION, MTransitionImpl.class,
			      Model.getStateMachinesFactory()
			          .createTransition(),
			      true);
	testObjectFactoryType(Uml.ACTOR, MActorImpl.class,
			      Model.getUseCasesFactory().createActor(),
			      true);
	testObjectFactoryType(Uml.CLASS, MClassImpl.class,
			      Model.getCoreFactory().createClass(),
			      true);
	testObjectFactoryType(Uml.CLASSIFIER, MClassifierImpl.class,
			      Model.getCoreFactory().createClassifier(),
			      true);
	testObjectFactoryType(Uml.CLASSIFIER_ROLE, MClassifierRoleImpl.class,
			      Model.getCollaborationsFactory()
			          .createClassifierRole(),
			      true);
	testObjectFactoryType(Uml.COMPONENT, MComponentImpl.class,
			      Model.getCoreFactory().createComponent(),
			      true);
	testObjectFactoryType(Uml.INSTANCE, MInstanceImpl.class,
			      Model.getCommonBehaviorFactory()
			          .createInstance(),
			      true);
	testObjectFactoryType(Uml.INTERFACE, MInterfaceImpl.class,
			      Model.getCoreFactory().createInterface(),
			      true);
	testObjectFactoryType(Uml.NODE, MNodeImpl.class,
			      Model.getCoreFactory().createNode(),
			      true);
	testObjectFactoryType(Uml.NODE_INSTANCE, MNodeInstanceImpl.class,
			      Model.getCommonBehaviorFactory()
			          .createNodeInstance(),
			      true);
	testObjectFactoryType(Uml.OBJECT, MObjectImpl.class,
			      Model.getCommonBehaviorFactory().createObject(),
			      true);
	testObjectFactoryType(Uml.PACKAGE, MPackageImpl.class,
			      Model.getModelManagementFactory()
			          .createPackage(),
			      true);
	testObjectFactoryType(Uml.STATE, MStateImpl.class,
			      Model.getStateMachinesFactory().createState(),
			      true);
	testObjectFactoryType(Uml.COMPOSITE_STATE, MCompositeStateImpl.class,
			      Model.getStateMachinesFactory()
			          .createCompositeState(),
			      true);
	testObjectFactoryType(Uml.PSEUDOSTATE, MPseudostateImpl.class,
			      Model.getStateMachinesFactory()
			          .createPseudostate(),
			      true);
	testObjectFactoryType(Uml.USE_CASE, MUseCaseImpl.class,
			      Model.getUseCasesFactory().createUseCase(),
			      true);
	testObjectFactoryType(Uml.ACTION, MActionImpl.class,
			      Model.getCommonBehaviorFactory().createAction(),
			      true);
	testObjectFactoryType(Uml.ASSOCIATION_END, MAssociationEndImpl.class,
			      Model.getCoreFactory().createAssociationEnd(),
			      true);
	testObjectFactoryType(Uml.CALL_ACTION, MCallActionImpl.class,
			      Model.getCommonBehaviorFactory()
			          .createCallAction(),
			      true);
	testObjectFactoryType(Uml.NAMESPACE, MNamespaceImpl.class,
			      Model.getCoreFactory().createNamespace(),
			      true);
	testObjectFactoryType(Uml.RECEPTION, MReceptionImpl.class,
			      Model.getCommonBehaviorFactory()
			          .createReception(),
			      true);
	testObjectFactoryType(Uml.STEREOTYPE, MStereotypeImpl.class,
			      Model.getExtensionMechanismsFactory()
			          .createStereotype(),
			      true);
	testObjectFactoryType(Uml.ATTRIBUTE, MAttributeImpl.class,
			      Model.getCoreFactory().createAttribute(),
			      true);
	testObjectFactoryType(Uml.OPERATION, MOperationImpl.class,
			      Model.getCoreFactory().createOperation(),
			      true);
	testObjectFactoryType(Uml.DATATYPE, MDataTypeImpl.class,
			      Model.getCoreFactory().createDataType(),
			      true);
	testObjectFactoryType(Uml.ACTION_EXPRESSION, MActionExpression.class,
			      null, true);
	testObjectFactoryType(Uml.COMPONENT_INSTANCE, 
	                      MComponentInstanceImpl.class,
			      Model.getCommonBehaviorFactory()
			          .createComponentInstance(),
			      true);
	// NSUML cannot instantiate a State Vertex object
	testObjectFactoryType(Uml.STATE_VERTEX, MStateVertexImpl.class,
			      Model.getDataTypesFactory(),
			      false);
	// NSUML cannot instantiate an Event object
	testObjectFactoryType(Uml.EVENT, MEventImpl.class, null, false);
    }

    /** @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
	super.setUp();

	/* Running the tests here causes instantiation errors from the
	 * navigator pane.  This is a temporary hack until the object
	 * model is cleaned up.
	 */
	NavigatorPane.setInstance(null);
	assertNull("Still getting NavigatorPane", NavigatorPane.getInstance());
    }
}
