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

import org.argouml.model.Model;
import org.argouml.model.UmlFactory;

import junit.framework.TestCase;


/**
 * Tests for the UmlFactory.
 *
 * @author Linus Tolke
 */
public class TestUmlFactory extends TestCase {
    /**
     * Constructor
     *
     * @param arg0 is the test name.
     */
    public TestUmlFactory(String arg0) { super(arg0); }

    /**
     * Testing the delete method.<p>
     *
     * Reasoning: Everything that is created is supposed to be able to be
     * deleted.
     */
    public void testDelete() {
	UmlFactory fy = Model.getUmlFactory();

	//	fy.delete(fy.getCore().
	fy.delete(fy.getCore().createAbstraction());
	fy.delete(fy.getCore().createAssociation());
	fy.delete(fy.getCore().createAssociationClass());
	fy.delete(fy.getCore().createAssociationEnd());
	fy.delete(fy.getCore().createAttribute());
	fy.delete(fy.getCore().createBinding());
	fy.delete(fy.getCore().createClass());
	fy.delete(fy.getCore().createClassifier());
	fy.delete(fy.getCore().createComment());
	fy.delete(fy.getCore().createComponent());
	fy.delete(fy.getCore().createConstraint());
	fy.delete(fy.getCore().createDataType());
	fy.delete(fy.getCore().createDependency());
	fy.delete(fy.getCore().createElementResidence());
	fy.delete(fy.getCore().createFlow());
	fy.delete(fy.getCore().createGeneralization());
	fy.delete(fy.getCore().createInterface());
	fy.delete(fy.getCore().createMethod());
	fy.delete(fy.getCore().createNamespace());
	fy.delete(fy.getCore().createNode());
	fy.delete(fy.getCore().createOperation());
	fy.delete(fy.getCore().createParameter());
	fy.delete(fy.getCore().createPermission());
	fy.delete(fy.getCore().createRelationship());
	fy.delete(fy.getCore().createTemplateParameter());
	fy.delete(fy.getCore().createUsage());

	fy.delete(fy.getActivityGraphs().createActionState());
	fy.delete(fy.getActivityGraphs().createActivityGraph());
	fy.delete(fy.getActivityGraphs().createCallState());
	fy.delete(fy.getActivityGraphs().createClassifierInState());
	fy.delete(fy.getActivityGraphs().createObjectFlowState());
	fy.delete(fy.getActivityGraphs().createPartition());
	fy.delete(fy.getActivityGraphs().createSubactivityState());

	fy.delete(fy.getUseCases().createExtend());
	fy.delete(fy.getUseCases().createExtensionPoint());
	fy.delete(fy.getUseCases().createActor());
	fy.delete(fy.getUseCases().createInclude());
	fy.delete(fy.getUseCases().createUseCase());
	fy.delete(fy.getUseCases().createUseCaseInstance());

	fy.delete(fy.getStateMachines().createCallEvent());
	fy.delete(fy.getStateMachines().createChangeEvent());
	fy.delete(fy.getStateMachines().createCompositeState());
	fy.delete(fy.getStateMachines().createFinalState());
	fy.delete(fy.getStateMachines().createGuard());
	fy.delete(fy.getStateMachines().createPseudostate());
	fy.delete(fy.getStateMachines().createSignalEvent());
	fy.delete(fy.getStateMachines().createSimpleState());
	fy.delete(fy.getStateMachines().createState());
	fy.delete(fy.getStateMachines().createStateMachine());
	fy.delete(fy.getStateMachines().createStubState());
	fy.delete(fy.getStateMachines().createSubmachineState());
	fy.delete(fy.getStateMachines().createSynchState());
	fy.delete(fy.getStateMachines().createTimeEvent());
	fy.delete(fy.getStateMachines().createTransition());


	// TODO:
	// CollaborationsFactory
	// CommonBehaviorFactory
	// DataTypesFactory
	// ExtensionMechanismsFactory
	// ModelManagementFactory
    }
}
