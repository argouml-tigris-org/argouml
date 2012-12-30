/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.util.Collection;

import junit.framework.TestCase;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.ui.UndoableAction;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.UMLMutableGraphSupport;

/**
 * 
 */
public class TestActionActivityGraphDiagram extends TestCase {

    /**
     * The action to be tested.
     */
    private UndoableAction action;

    /**
     * The namespace a created diagram should have.
     */
    private Object ns;

    private Project project;

    
    /**
     * Constructor.
     *
     * @param arg0 test case name
     */
    public TestActionActivityGraphDiagram(String arg0) {
        super(arg0);
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
    }


    protected UndoableAction getAction() {
        return new ActionActivityDiagram();
    }


    protected Object getNamespace() {
        return Model.getModelManagementFactory().createModel();
    }

    @Override
    protected void setUp() {
        project = ProjectManager.getManager().makeEmptyProject();
        action = getAction();
        ns = getNamespace();
        TargetManager.getInstance().setTarget(ns);
    }

    /**
     * Test to create a diagram.
     */
    public void testCreateDiagram() {
        Model.getPump().flushModelEvents();
        Collection startingElements = Model.getFacade().getRootElements();
        action.actionPerformed(null);
        Object d = TargetManager.getInstance().getTarget();
        assertTrue("No diagram generated", d instanceof ArgoDiagram);
        Model.getPump().flushModelEvents();
        ArgoDiagram diagram = (ArgoDiagram) d;
        assertNotNull(
                      "The diagram has no namespace",
                      diagram.getNamespace());
        assertNotNull(
                      "The diagram has no graphmodel",
                      diagram.getGraphModel());
        assertTrue("The graphmodel of the diagram is not a "
                   + "UMLMutableGraphSupport",
                   diagram.getGraphModel() instanceof UMLMutableGraphSupport);
        assertNotNull("The diagram has no name", diagram.getName());
        
        // Make sure everything created gets deleted
        Model.getUmlFactory().delete(ns);
        Collection leftovers = Model.getFacade().getRootElements();
        leftovers.removeAll(startingElements);
        assertTrue("All elements of diagram not deleted", leftovers.isEmpty());
    }




}
