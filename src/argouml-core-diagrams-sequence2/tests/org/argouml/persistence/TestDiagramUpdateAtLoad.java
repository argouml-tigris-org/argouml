/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    linus
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2009 The Regents of the University of California. All
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

package org.argouml.persistence;

import java.io.File;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.argouml.kernel.Project;
import org.argouml.model.InitializeModel;
import org.argouml.moduleloader.ModuleLoader2;
import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.java.InitNotationJava;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.sequence2.SequenceDiagramModule;
import org.argouml.uml.diagram.activity.ui.InitActivityDiagram;
import org.argouml.uml.diagram.collaboration.ui.InitCollaborationDiagram;
import org.argouml.uml.diagram.deployment.ui.InitDeploymentDiagram;
import org.argouml.uml.diagram.state.ui.InitStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.InitClassDiagram;
import org.argouml.uml.diagram.ui.InitDiagramAppearanceUI;
import org.argouml.uml.diagram.use_case.ui.InitUseCaseDiagram;

/**
 * Test case to load projects without exception.
 */
public class TestDiagramUpdateAtLoad extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        (new InitNotation()).init();
        (new InitNotationUml()).init();
        (new InitNotationJava()).init();
        (new InitDiagramAppearanceUI()).init();
        (new InitActivityDiagram()).init();
        (new InitCollaborationDiagram()).init();
        (new InitDeploymentDiagram()).init();
	// Init the sequence diagrams from the module
        (new InitStateDiagram()).init();
        (new InitClassDiagram()).init();
        (new InitUseCaseDiagram()).init();
        (new InitProfileSubsystem()).init();
        ModuleLoader2.addClass(SequenceDiagramModule.class.getName());
        ModuleLoader2.doLoad(false);
    }

    /**
     * The constructor.
     *
     * @param name the name
     */
    public TestDiagramUpdateAtLoad(String name) {
        super(name);
        InitializeModel.initializeDefault();
    }

    /**
     * @param args the arguments given on the commandline
     */
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * @return the test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(TestDiagramUpdateAtLoad.class);

        return suite;
    }

    /**
     * Tests that a project is loadable.
     *
     * @param filename of the project file to load
     * @throws OpenException if something goes wrong.
     */
    private Project doLoad(String filename) 
        throws OpenException, InterruptedException {
        assertTrue(ModuleLoader2.isEnabled("ArgoUML-Sequence"));
        URL url = getClass().getResource(filename);
        assertNotNull("Resource to be tested is not found: " + filename, url);

        ZargoFilePersister persister = new ZargoFilePersister();
        String name = url.getFile();
        Project p = persister.doLoad(new File(name));
        return p;
    }

    /**
     * Test loading a native UML 1.4 zargo.
     *
     * @throws Exception when e.g. the file is not found
     */
    public void testDoLoadUml14() throws Exception {
        Project p = doLoad("/testmodels/uml14/Alittlebitofeverything.zargo");
        p.remove();

        p = doLoad("/testmodels/uml14/SequenceDiagram.zargo");
        p.remove();
    }
}



