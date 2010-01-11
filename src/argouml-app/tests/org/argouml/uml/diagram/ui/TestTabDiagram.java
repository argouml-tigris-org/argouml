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

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.Rectangle;
import java.util.Date;

import junit.framework.TestCase;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.java.InitNotationJava;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.activity.ui.InitActivityDiagram;
import org.argouml.uml.diagram.collaboration.ui.InitCollaborationDiagram;
import org.argouml.uml.diagram.deployment.ui.InitDeploymentDiagram;
import org.argouml.uml.diagram.state.ui.InitStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.diagram.static_structure.ui.InitClassDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.use_case.ui.InitUseCaseDiagram;
import org.tigris.gef.graph.presentation.JGraph;

/**
 * @author jaap.branderhorst@xs4all.nl
 * @since Apr 13, 2003
 */
public class TestTabDiagram extends TestCase {

    private static final int NUMBER_OF_DIAGRAMS = 10;

    private static final boolean PERFORMANCE_TEST = false;

    private ArgoDiagram diagram;

    /**
     * Constructor for TestTabDiagram.
     *
     * @param arg0 is the name of the test case.
     */
    public TestTabDiagram(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        (new InitNotation()).init();
        (new InitNotationUml()).init();
        (new InitNotationJava()).init();
        (new InitDiagramAppearanceUI()).init();
        (new InitActivityDiagram()).init();
        (new InitCollaborationDiagram()).init();
        (new InitDeploymentDiagram()).init();
        (new InitStateDiagram()).init();
        (new InitClassDiagram()).init();
        (new InitUseCaseDiagram()).init();
        (new InitProfileSubsystem()).init();
        ProjectManager.getManager().makeEmptyProject();
        diagram = new UMLClassDiagram();
    }

    /*
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        diagram = null;

    }

    /**
     * Test diagram tab construction.
     */
    public void testConstruction() {
        try {
            TabDiagram tabDiagram = new TabDiagram();
            assertEquals(tabDiagram.getTitle(), "Diagram");
        } catch (Exception noHead) {
        }
    }

    /**
     * Tests the setTarget method when a diagram is the target.
     */
    public void testSetTargetWithDiagram() {
        try {
            TabDiagram tabDiagram = new TabDiagram();
            TargetManager.getInstance().setTarget(diagram);
            assertEquals(
                tabDiagram.getJGraph().getGraphModel(),
                diagram.getGraphModel());
            assertEquals(tabDiagram.getTarget(), diagram);
            assertTrue(tabDiagram.shouldBeEnabled(diagram));
        } catch (Exception noHead) {
        }
    }

    /**
     * Tests the settarget method when the target is not a diagram but a simple
     * object.
     *
     */
    public void testSetTargetWithNoDiagram() {
        try {
            TabDiagram tabDiagram = new TabDiagram();
            JGraph graph = tabDiagram.getJGraph();
            TargetManager.getInstance().setTarget(diagram);
            // the graph should stay the same.
            assertEquals(tabDiagram.getJGraph(), graph);
        } catch (Exception noHead) {
        }
    }

    /**
     * Test the performance of adding an operation to 1 class that's
     * represented on 10 different diagrams. The last created diagram
     * is the one selected.
     *
     */
    public void testFireModelEventPerformance() {
        // setup
        if (PERFORMANCE_TEST) {
            // Arbitrary settings - not used used for testing
            DiagramSettings settings = new DiagramSettings();
            Rectangle bounds = new Rectangle(10, 10, 20, 20);
            try {
                ArgoDiagram[] diagrams = new ArgoDiagram[NUMBER_OF_DIAGRAMS];
                Project project =
                    ProjectManager.getManager().getCurrentProject();
                Object clazz = Model.getCoreFactory().buildClass();
                for (int i = 0; i < NUMBER_OF_DIAGRAMS; i++) {
                    diagrams[i] = new UMLClassDiagram(project.getRoot());
                    diagrams[i].add(
                        new FigClass(clazz, bounds, settings));
                    TargetManager.getInstance().setTarget(diagrams[i]);
                }

                // real test
                long currentTime = (new Date()).getTime();
                Object returnType = project.getDefaultReturnType();
                Model.getCoreFactory().buildOperation(clazz, returnType);
                System.out.println(
                    "Time needed for adding operation: "
                        + ((new Date()).getTime() - currentTime));
            } catch (Exception noHead) {
            }
        }
    }

}
