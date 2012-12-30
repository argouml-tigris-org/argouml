/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
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

// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram;

import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.uml.diagram.DiagramFactory.DiagramType;

/**
 * Test the DiagramFactory.
 *
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class TestDiagramFactory extends TestCase {

    private static final Logger LOG =
        Logger.getLogger(TestDiagramFactory.class.getName());
    private Project project;

    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();

        project = ProjectManager.getManager().makeEmptyProject();
    }

    @Override
    protected void tearDown() throws Exception {
        ProjectManager.getManager().removeProject(project);
        super.tearDown();
    }

    /**
     * Test creation of the default diagram type.
     */
    public void testCreateDefaultDiagram() {
        Object namespace = Model.getModelManagementFactory().createModel();
        ArgoDiagram diagram =
            DiagramFactory.getInstance().createDefaultDiagram(namespace);
        assertNotNull(diagram);
        DiagramSettings settings = diagram.getDiagramSettings();
        assertNotNull(settings);
        DiagramSettings settings2 = new DiagramSettings();
        settings2.setFontName("test font");
        assertNotSame(settings, settings2);
        diagram.setDiagramSettings(settings2);
        assertEquals(settings2, diagram.getDiagramSettings());
        Model.getUmlFactory().delete(namespace);
    }

    /**
     * Test creation of all supported diagram types.
     */
    public void testCreateAndRemove() {
        Object model = Model.getModelManagementFactory().createModel();
        DiagramSettings settings = new DiagramSettings();
        for (DiagramType type : DiagramType.values()) {
            ArgoDiagram diagram;
            if (type == DiagramType.Sequence) {
                // TODO: Fix this so that new sequence diagrams are tested
                LOG.log(Level.WARNING,
                        "Skipping Sequence Diagram test "
                        + "because they are in a separate module");
                return;
            } else if (type == DiagramType.State) {
                Object context = Model.getCoreFactory().buildClass(model);
                Object machine = Model.getStateMachinesFactory()
                        .buildStateMachine(context);
                diagram = DiagramFactory.getInstance().create(type, machine,
                        settings);
            } else if (type == DiagramType.Collaboration
                    || type == DiagramType.Sequence) {
                Object collab = Model.getCollaborationsFactory()
                        .buildCollaboration(model);
                diagram = DiagramFactory.getInstance().create(type, collab,
                        settings);
            } else if (type == DiagramType.Activity) {
                Object context = Model.getCoreFactory().buildClass(model);
                Object activityGraph = Model.getActivityGraphsFactory()
                        .buildActivityGraph(context);
                diagram = DiagramFactory.getInstance().create(type,
                        activityGraph, settings);
            } else {
                // Handle the simple cases
                // Anything very exotic will fail, but that will be our clue
                // that the test needs to be extended
                diagram = DiagramFactory.getInstance().create(type, model,
                        settings);
            }
            assertNotNull(diagram);
            DiagramFactory.getInstance().removeDiagram(diagram);
        }
        Model.getUmlFactory().delete(model);
    }


}
