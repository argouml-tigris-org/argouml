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

import junit.framework.TestCase;

import org.argouml.model.UmlFactory;


/**
 * Tests for the UmlFactory.
 *
 * @author Linus Tolke
 */
public class TestUmlFactory extends TestCase {
    /**
     * Constructor.
     *
     * @param arg0 is the test name.
     */
    public TestUmlFactory(String arg0) { super(arg0); }

    /**
     * The ModelImplementation for these tests.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * The UmlFactory for these tests.
     */
    private UmlFactory fy;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
        nsmodel = new NSUMLModelImplementation();
	fy = nsmodel.getUmlFactory();
    }

    /**
     * Testing the delete method for Core.<p>
     *
     * Reasoning: Everything that is created is supposed to be able to be
     * deleted.
     */
    public void testDeleteCore() {
	//	fy.delete(nsmodel.getCoreFactory().
	fy.delete(nsmodel.getCoreFactory().createAbstraction());
	fy.delete(nsmodel.getCoreFactory().createAssociation());
	fy.delete(nsmodel.getCoreFactory().createAssociationClass());
	fy.delete(nsmodel.getCoreFactory().createAssociationEnd());
	fy.delete(nsmodel.getCoreFactory().createAttribute());
	fy.delete(nsmodel.getCoreFactory().createBinding());
	fy.delete(nsmodel.getCoreFactory().createClass());
	fy.delete(nsmodel.getCoreFactory().createClassifier());
	fy.delete(nsmodel.getCoreFactory().createComment());
	fy.delete(nsmodel.getCoreFactory().createComponent());
	fy.delete(nsmodel.getCoreFactory().createConstraint());
	fy.delete(nsmodel.getCoreFactory().createDataType());
	fy.delete(nsmodel.getCoreFactory().createDependency());
	fy.delete(nsmodel.getCoreFactory().createElementResidence());
	fy.delete(nsmodel.getCoreFactory().createFlow());
	fy.delete(nsmodel.getCoreFactory().createGeneralization());
	fy.delete(nsmodel.getCoreFactory().createInterface());
	fy.delete(nsmodel.getCoreFactory().createMethod());
	fy.delete(nsmodel.getCoreFactory().createNamespace());
	fy.delete(nsmodel.getCoreFactory().createNode());
	fy.delete(nsmodel.getCoreFactory().createOperation());
	fy.delete(nsmodel.getCoreFactory().createParameter());
	fy.delete(nsmodel.getCoreFactory().createPermission());
	fy.delete(nsmodel.getCoreFactory().createRelationship());
	fy.delete(nsmodel.getCoreFactory().createTemplateParameter());
	fy.delete(nsmodel.getCoreFactory().createUsage());
    }

    /**
     * Testing the delete method for ActivityGraphs.<p>
     *
     * Reasoning: Everything that is created is supposed to be able to be
     * deleted.
     */
    public void testDeleteActivityGraphs() {
	fy.delete(nsmodel.getActivityGraphsFactory().createActionState());
	fy.delete(nsmodel.getActivityGraphsFactory().createActivityGraph());
	fy.delete(nsmodel.getActivityGraphsFactory().createCallState());
	fy.delete(nsmodel.getActivityGraphsFactory().createClassifierInState());
	fy.delete(nsmodel.getActivityGraphsFactory().createObjectFlowState());
	fy.delete(nsmodel.getActivityGraphsFactory().createPartition());
	fy.delete(nsmodel.getActivityGraphsFactory().createSubactivityState());
    }

    /**
     * Testing the delete method for UseCases.<p>
     *
     * Reasoning: Everything that is created is supposed to be able to be
     * deleted.
     */
    public void testDeleteUseCases() {
	fy.delete(nsmodel.getUseCasesFactory().createExtend());
	fy.delete(nsmodel.getUseCasesFactory().createExtensionPoint());
	fy.delete(nsmodel.getUseCasesFactory().createActor());
	fy.delete(nsmodel.getUseCasesFactory().createInclude());
	fy.delete(nsmodel.getUseCasesFactory().createUseCase());
	fy.delete(nsmodel.getUseCasesFactory().createUseCaseInstance());
    }

    /**
     * Testing the delete method for StateMachines.<p>
     *
     * Reasoning: Everything that is created is supposed to be able to be
     * deleted.
     */
    public void testDeleteStateMachines() {
	fy.delete(nsmodel.getStateMachinesFactory().createCallEvent());
	fy.delete(nsmodel.getStateMachinesFactory().createChangeEvent());
	fy.delete(nsmodel.getStateMachinesFactory().createCompositeState());
	fy.delete(nsmodel.getStateMachinesFactory().createFinalState());
	fy.delete(nsmodel.getStateMachinesFactory().createGuard());
	fy.delete(nsmodel.getStateMachinesFactory().createPseudostate());
	fy.delete(nsmodel.getStateMachinesFactory().createSignalEvent());
	fy.delete(nsmodel.getStateMachinesFactory().createSimpleState());
	fy.delete(nsmodel.getStateMachinesFactory().createState());
	fy.delete(nsmodel.getStateMachinesFactory().createStateMachine());
	fy.delete(nsmodel.getStateMachinesFactory().createStubState());
	fy.delete(nsmodel.getStateMachinesFactory().createSubmachineState());
	fy.delete(nsmodel.getStateMachinesFactory().createSynchState());
	fy.delete(nsmodel.getStateMachinesFactory().createTimeEvent());
	fy.delete(nsmodel.getStateMachinesFactory().createTransition());
    }

    // TODO:
    // CollaborationsFactory
    // CommonBehaviorFactory
    // DataTypesFactory
    // ExtensionMechanismsFactory
    // ModelManagementFactory
}
