/* $Id$
 *****************************************************************************
 * Copyright (c) 2009,2010 Contributors - see below
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

// Copyright (c) 1996-2008 The Regents of the University of California. All
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
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.argouml.kernel.Project;
import org.argouml.model.Facade;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.java.InitNotationJava;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.uml.diagram.activity.ui.InitActivityDiagram;
import org.argouml.uml.diagram.collaboration.ui.InitCollaborationDiagram;
import org.argouml.uml.diagram.deployment.ui.InitDeploymentDiagram;
import org.argouml.uml.diagram.state.ui.InitStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.InitClassDiagram;
import org.argouml.uml.diagram.ui.InitDiagramAppearanceUI;
import org.argouml.uml.diagram.use_case.ui.InitUseCaseDiagram;

/**
 * Testcase to load projects without exception.
 */
public class TestZargoFilePersister extends TestCase {

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
        (new InitStateDiagram()).init();
        (new InitClassDiagram()).init();
        (new InitUseCaseDiagram()).init();
        (new InitProfileSubsystem()).init();
    }
    

    @Override
    protected void tearDown() throws Exception {
        ProfileFacade.reset();
        super.tearDown();
    }

    /**
     * The constructor.
     *
     * @param name the name
     */
    public TestZargoFilePersister(String name) {
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
        TestSuite suite = new TestSuite(TestZargoFilePersister.class);

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
        URL url = TestZargoFilePersister.class.getResource(filename);
        assertTrue("Unintended failure: resource to be tested is not found: "
                + filename + ", converted to URL: " + url, url != null);
        ZargoFilePersister persister = new ZargoFilePersister();
        String name = url.getFile();
        Project p = persister.doLoad(new File(name));
        return p;
    }

    /**
     * Test loading an old empty zargo.
     *
     * @throws Exception when e.g. the file is not found
     */
    public void testDoLoadEmptyUml13() throws Exception {
        Project p = doLoad("/testmodels/uml13/Empty.zargo");
        p.remove();
    }

    /**
     * Test loading a new empty zargo.
     *
     * @throws Exception when e.g. the file is not found
     */
    public void testDoLoadEmptyUml14() throws Exception {
        Project p = doLoad("/testmodels/uml14/EmptyProject024.zargo");
        p.remove();
    }

    /**
     * Test loading UML 1.3 which needs to be upgraded on load zargo.
     *
     * @throws Exception when e.g. the file is not found
     */
    public void testDoLoadUml13() throws Exception {
        Project p = doLoad("/testmodels/uml13/Alittlebitofeverything.zargo");
        p.remove();
    }

    /**
     * Test loading a native UML 1.4 zargo.
     *
     * @throws Exception when e.g. the file is not found
     */
    public void testDoLoadUml14() throws Exception {
        Project p = doLoad("/testmodels/uml14/Alittlebitofeverything.zargo");
        p.remove();
    }

    /**
     * Test loading a native UML 1.4 zargo with i18n character.
     *
     * @throws Exception when e.g. the file is not found
     */
    public void testDoLoadUml14i18n() throws Exception {
        Project p = doLoad("/testmodels/uml14/i18n.zargo");
        p.remove();
    }

    
    /**
     * Test saving a zargo.
     *
     * @throws Exception when e.g. the file is not found
     */
    public void testSave() throws Exception {
        Project p = doLoad("/testmodels/uml14/Alittlebitofeverything.zargo");
        ZargoFilePersister persister = new ZargoFilePersister();
        persister.save(p, new File("Alittlebitofeverything2.zargo"));
        p.remove();
    }

    /**
     * Test loading some garbage in a zargo.
     */
    public void testLoadGarbage() {
        File file = null;
        boolean loaded = true;
        try {
            file = new File("/testmodels/Garbage.zargo");
            ZargoFilePersister persister = new ZargoFilePersister();
            persister.doLoad(file);
            fail("Load didn't throw an exception");
        } catch (OpenException io) {
            // This is the normal case.
            loaded = false;
        } catch (InterruptedException iExc) {
            fail("Got an unexpected InterruptedException during load");
        }
        assertTrue("No exception was thrown.", !loaded);
    }
    
    /**
     * Test loading a project which contains external links to a profile.
     * 
     * @throws OpenException
     *                 if there was an error opening the project
     * @throws InterruptedException
     *                 if interrupted - should never occur in test environment
     */
    public void testLoadLinkedProfile() throws OpenException,
            InterruptedException {
     
        // Load a project which contains links to it
        Project p = doLoad("/testmodels/uml14/LinkedProfile.zargo");
        
        // Make sure the contents match what we expect
        final Facade f = Model.getFacade();
        Collection topElements = p.getRoots();
        assertFalse("No top level elements", topElements.isEmpty());
        for (Object element : topElements) {
            if (f.isAClass(element)) {
                assertEquals("Bad Name", 
                        "HugeDecimal", 
                        f.getNamespace(element));
                Collection generalizations = f.getGeneralizations(element);
                Object generalization = generalizations.iterator().next();
                Object parent = f.getGeneral(generalization);
                assertEquals("superclass has wrong name", "BigDecimal", 
                        f.getName(parent));
            }
        }
        
        p.remove();
    }
}
