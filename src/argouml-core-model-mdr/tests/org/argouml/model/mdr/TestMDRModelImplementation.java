/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.model.mdr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.jmi.reflect.InvalidObjectException;

import junit.framework.TestCase;

import org.argouml.model.UmlException;
import org.omg.uml.behavioralelements.statemachines.Guard;
import org.omg.uml.behavioralelements.statemachines.State;
import org.omg.uml.behavioralelements.statemachines.StateMachine;
import org.omg.uml.behavioralelements.statemachines.StateMachinesPackage;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;

/**
 * Testing the MDRModelImplementation.
 */
public class TestMDRModelImplementation extends TestCase {
    /**
     * The ModelImplementation.<p>
     *
     * The reason for not having this as a member variable that is created by
     * {@link #setUp()} is that the MDR is the initialized several times and
     * the current implementation fails on the second initialization.
     */
    private static MDRModelImplementation modelImplementation;
    
    static {
        try {
            modelImplementation = new MDRModelImplementation();
        } catch (UmlException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for TestMDRModelImplementation.
     * @param arg0 The name of the test case.
     */
    public TestMDRModelImplementation(String arg0) {
        super(arg0);
    }
    
    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        assertNotNull(modelImplementation);
    }

    /**
     * 
     */
    public void testGetFacade() {
        assertNotNull(modelImplementation.getFacade());
    }

    /**
     * 
     */
    public void testGetModelEventPump() {
        assertNotNull(modelImplementation.getModelEventPump());
    }

    /**
     * 
     */
    public void testGetActivityGraphsFactory() {
        assertNotNull(modelImplementation.getActivityGraphsFactory());
    }

    /**
     * 
     */
    public void testGetActivityGraphsHelper() {
        assertNotNull(modelImplementation.getActivityGraphsHelper());
    }

    /**
     * 
     */
    public void testGetCollaborationsFactory() {
        assertNotNull(modelImplementation.getCollaborationsFactory());
    }

    /**
     * 
     */
    public void testGetCollaborationsHelper() {
        assertNotNull(modelImplementation.getCollaborationsHelper());
    }

    /**
     * 
     */
    public void testGetCommonBehaviorFactory() {
        assertNotNull(modelImplementation.getCommonBehaviorFactory());
    }

    /**
     * 
     */
    public void testGetCommonBehaviorHelper() {
        assertNotNull(modelImplementation.getCommonBehaviorHelper());
    }

    /**
     * 
     */
    public void testGetCoreFactory() {
        assertNotNull(modelImplementation.getCoreFactory());
    }

    /**
     * 
     */
    public void testGetCoreHelper() {
        assertNotNull(modelImplementation.getCoreHelper());
    }

    /**
     * 
     */
    public void testGetDataTypesFactory() {
        assertNotNull(modelImplementation.getDataTypesFactory());
    }

    /**
     * 
     */
    public void testGetDataTypesHelper() {
        assertNotNull(modelImplementation.getDataTypesHelper());
    }

    /**
     * 
     */
    public void testGetExtensionMechanismsFactory() {
        assertNotNull(modelImplementation.getExtensionMechanismsFactory());
    }

    /**
     * 
     */
    public void testGetExtensionMechanismsHelper() {
        assertNotNull(modelImplementation.getExtensionMechanismsHelper());
    }

    /**
     * 
     */
    public void testGetModelManagementFactory() {
        assertNotNull(modelImplementation.getModelManagementFactory());
    }

    /**
     * 
     */
    public void testGetModelManagementHelper() {
        assertNotNull(modelImplementation.getModelManagementHelper());
    }

    /**
     * 
     */
    public void testGetStateMachinesFactory() {
        assertNotNull(modelImplementation.getStateMachinesFactory());
    }

    /**
     * 
     */
    public void testGetStateMachinesHelper() {
        assertNotNull(modelImplementation.getStateMachinesHelper());
    }

    /**
     * 
     */
    public void testGetUmlFactory() {
        assertNotNull(modelImplementation.getUmlFactory());
    }

    /**
     * 
     */
    public void testGetUmlHelper() {
        assertNotNull(modelImplementation.getUmlHelper());
    }

    /**
     * 
     */
    public void testGetUseCasesFactory() {
        assertNotNull(modelImplementation.getUseCasesFactory());
    }

    /**
     * 
     */
    public void testGetUseCasesHelper() {
        assertNotNull(modelImplementation.getUseCasesHelper());
    }

    /**
     * 
     */
    public void testGetMetaTypes() {
        assertNotNull(modelImplementation.getMetaTypes());
    }

    /**
     * 
     */
    public void testGetChangeableKind() {
        assertNotNull(modelImplementation.getChangeableKind());
    }

    /**
     * 
     */
    public void testGetAggregationKind() {
        assertNotNull(modelImplementation.getAggregationKind());
    }

    /**
     * 
     */
    public void testGetPseudostateKind() {
        assertNotNull(modelImplementation.getPseudostateKind());
    }

    /**
     * 
     */
    public void testGetScopeKind() {
        assertNotNull(modelImplementation.getScopeKind());
    }

    /**
     * 
     */
    public void testGetConcurrencyKind() {
        assertNotNull(modelImplementation.getConcurrencyKind());
    }

    /**
     * 
     */
    public void testGetDirectionKind() {
        assertNotNull(modelImplementation.getDirectionKind());
    }

    /**
     * 
     */
    public void testGetOrderingKind() {
        assertNotNull(modelImplementation.getOrderingKind());
    }

    /**
     * 
     */
    public void testGetVisibilityKind() {
        assertNotNull(modelImplementation.getVisibilityKind());
    }

    /**
     * @throws UmlException If an error occur
     */
    public void testGetXmiReader() throws UmlException {
        assertNotNull(modelImplementation.getXmiReader());
    }

    /**
     * @throws UmlException If an error occur
     * @throws IOException If the file isn't found
     */
    public void testGetXmiWriter() throws UmlException, IOException {
        assertNotNull(modelImplementation.getXmiWriter(
            "dummyModel", new FileOutputStream(
                File.createTempFile("dummy", null)), null));
    }
    

    /**
     * Test deleting a Guard composited in a Transition.
     * 
     * --- Known to fail with NetBeans MDR build number 200507110943 ---
     */
    public void notestDeleteCompositeGuard() {
        org.omg.uml.UmlPackage umlPkg = modelImplementation.getUmlPackage();
        StateMachinesPackage sm = umlPkg.getStateMachines();
        State state1 = sm.getSimpleState().createSimpleState();
        State state2 = sm.getSimpleState().createSimpleState();
        Transition transition = sm.getTransition().createTransition();
        transition.setSource(state1);
        transition.setTarget(state2);
        Guard guard = sm.getGuard().createGuard("guard",
                VisibilityKindEnum.VK_PUBLIC, false, null);
        transition.setGuard(guard);
        transition.refDelete();
        try {
            guard.getName();
            fail("Guard not deleted with owning transition");
        } catch (InvalidObjectException e) {
            // success
        } 
    }

    /**
     * Test deleting top State composited in StateMachine.
     * 
     * --- Known to fail with NetBeans MDR build number 200507110943 ---
     */
    public void notestDeleteCompositeTopState() {
        org.omg.uml.UmlPackage umlPkg = modelImplementation.getUmlPackage();
        StateMachinesPackage sm = umlPkg.getStateMachines();
        StateMachine machine = sm.getStateMachine().createStateMachine();
        State state1 = sm.getSimpleState().createSimpleState("top state", 
                VisibilityKindEnum.VK_PUBLIC, false);
        machine.setTop(state1);
        machine.refDelete();
        try {
            state1.getName();
            fail("Top state not deleted with owning state machine");
        } catch (InvalidObjectException e) {
            // success
        } 
    }

}
