// $Id$
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.model;

import junit.framework.TestCase;

/**
 * Checks that the {@link UmlFactory#buildNode(Object)} method works with
 * all conceivable alternatives.
 */
public class TestUmlFactoryBuildNode extends TestCase {
    /**
     * Constructor.
     *
     * @param arg0 name of the test case
     */
    public TestUmlFactoryBuildNode(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
        InitializeModel.initializeDefault();
    }

    /**
     * Testing Core elements.
     */
    public void testBuildCoreNodes() {
        assertNotNull(Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getUMLClass()));
        assertNotNull(Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getModel()));
        assertNotNull(Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getPackage()));
        // ...
    }

    // ActivityGraphs

    /**
     * Testing Use Cases elements.
     */
    public void testBuildUseCasesNodes() {
        assertNotNull(Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getActor()));
        assertNotNull(Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getUseCase()));
        assertNotNull(Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getPackage()));

         // Instance (in UML 1.4) and Classifier are abstract and not tested

        /*
         *  Extend, Include, UseCaseInstance & ExtensionPoint not implemented
         */
//        assertNotNull(Model.getUmlFactory().buildNode(
//                Model.getMetaTypes().getExtend()));
//        assertNotNull(Model.getUmlFactory().buildNode(
//                Model.getMetaTypes().getInclude()));
//        assertNotNull(Model.getUmlFactory().buildNode(
//                Model.getMetaTypes().getUseCaseInstance()));
//        assertNotNull(Model.getUmlFactory().buildNode(
//                Model.getMetaTypes().getExtensionPoint()));
        // ...
    }

    /**
     * Tests for StateMachines elements.
     */
    public void testBuildStateMachineNodes() {
        assertNotNull(Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getCompositeState()));
        assertNotNull(Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getFinalState()));
        assertNotNull(Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getSimpleState()));

        /*
         * State is concrete in UML 1.3, but becomes abstract in UML 1.4, so we
         * never allow it to be created (and don't test creation)
         */
//        assertNotNull(Model.getUmlFactory().buildNode(
//                Model.getMetaTypes().getState()));
        assertNotNull(Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getPseudostate()));
        assertNotNull(Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getSynchState()));
        assertNotNull(Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getStubState()));
        assertNotNull(Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getSubmachineState()));
        // ...
    }

    /**
     * Tests for Collaborations elements.
     */
    public void testBuildCollaborationsNodes() {
        assertNotNull(Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getUMLClass()));
        // ...
    }

    // CommonBehaviorFactory
    // DataTypesFactory
    // ExtensionMechanismsFactory
    // ModelManagementFactory
}
