// $Id$
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

import junit.framework.TestCase;


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
    public TestUmlFactory(String arg0) {
        super(arg0);
    }

    /**
     * The UmlFactory for these tests.
     */
    private UmlFactory fy;

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
	fy = Model.getUmlFactory();
    }

    /**
     * Testing the delete method for Core.<p>
     *
     * Reasoning: Everything that is created is supposed to be able to be
     * deleted.
     */
    public void testDeleteCore() {
	//	fy.delete(Model.getCoreFactory().
	fy.delete(Model.getCoreFactory().createAssociationEnd());
	fy.delete(Model.getCoreFactory().createAttribute());
	fy.delete(Model.getCoreFactory().createBinding());
	fy.delete(Model.getCoreFactory().createClass());
	fy.delete(Model.getCoreFactory().createComment());
	fy.delete(Model.getCoreFactory().createComponent());
	fy.delete(Model.getCoreFactory().createConstraint());
	fy.delete(Model.getCoreFactory().createDataType());
	fy.delete(Model.getCoreFactory().createElementResidence());
	fy.delete(Model.getCoreFactory().createFlow());
	fy.delete(Model.getCoreFactory().createInterface());
	fy.delete(Model.getCoreFactory().createMethod());
	fy.delete(Model.getCoreFactory().createNode());
	fy.delete(Model.getCoreFactory().createOperation());
	fy.delete(Model.getCoreFactory().createParameter());
	fy.delete(Model.getCoreFactory().createPermission());
	fy.delete(Model.getCoreFactory().createTemplateParameter());
	fy.delete(Model.getCoreFactory().createUsage());
    }

    /**
     * Testing the delete method for ActivityGraphs.<p>
     *
     * Reasoning: Everything that is created is supposed to be able to be
     * deleted.
     */
    public void testDeleteActivityGraphs() {
	fy.delete(Model.getActivityGraphsFactory().createActionState());
	fy.delete(Model.getActivityGraphsFactory().createActivityGraph());
	fy.delete(Model.getActivityGraphsFactory().createCallState());
	fy.delete(Model.getActivityGraphsFactory().createClassifierInState());
	fy.delete(Model.getActivityGraphsFactory().createObjectFlowState());
	fy.delete(Model.getActivityGraphsFactory().createPartition());
	fy.delete(Model.getActivityGraphsFactory().createSubactivityState());
    }

    /**
     * Testing the delete method for UseCases.<p>
     *
     * Reasoning: Everything that is created is supposed to be able to be
     * deleted.
     */
    public void testDeleteUseCases() {
	fy.delete(Model.getUseCasesFactory().createExtend());
	fy.delete(Model.getUseCasesFactory().createExtensionPoint());
	fy.delete(Model.getUseCasesFactory().createActor());
	fy.delete(Model.getUseCasesFactory().createInclude());
	fy.delete(Model.getUseCasesFactory().createUseCase());
	fy.delete(Model.getUseCasesFactory().createUseCaseInstance());
    }

    /**
     * Testing the delete method for StateMachines.<p>
     *
     * Reasoning: Everything that is created is supposed to be able to be
     * deleted.
     */
    public void testDeleteStateMachines() {
	fy.delete(Model.getStateMachinesFactory().createCallEvent());
	fy.delete(Model.getStateMachinesFactory().createChangeEvent());
	fy.delete(Model.getStateMachinesFactory().createCompositeState());
	fy.delete(Model.getStateMachinesFactory().createFinalState());
	fy.delete(Model.getStateMachinesFactory().createGuard());
	fy.delete(Model.getStateMachinesFactory().createPseudostate());
	fy.delete(Model.getStateMachinesFactory().createSignalEvent());
	fy.delete(Model.getStateMachinesFactory().createSimpleState());
	fy.delete(Model.getStateMachinesFactory().createStateMachine());
	fy.delete(Model.getStateMachinesFactory().createStubState());
	fy.delete(Model.getStateMachinesFactory().createSubmachineState());
	fy.delete(Model.getStateMachinesFactory().createSynchState());
	fy.delete(Model.getStateMachinesFactory().createTimeEvent());
	fy.delete(Model.getStateMachinesFactory().createTransition());
    }

    // TODO:
    // CollaborationsFactory
    // CommonBehaviorFactory
    // DataTypesFactory
    // ExtensionMechanismsFactory
    // ModelManagementFactory
}
