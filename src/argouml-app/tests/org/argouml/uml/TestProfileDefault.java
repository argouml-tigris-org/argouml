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

// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml;

import java.util.Collection;

import junit.framework.TestCase;

import org.argouml.kernel.ProfileConfiguration;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.init.InitProfileSubsystem;

/**
 * Automated integration tests that guarantee that the default profiles work.
 * @author Luis Sergio Oliveira (euluis)
 * @since 0.20
 */
public class TestProfileDefault extends TestCase {

    private Project proj;

    /**
     * The constructor.
     *
     * @param name the name of the test.
     */
    public TestProfileDefault(String name) {
        super(name);
    }
    
    /*
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {
	super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
    }

    @Override
    protected void tearDown() throws Exception {
        if (proj != null) {
            ProjectManager.getManager().removeProject(proj);
        }
        super.tearDown();
    }
    /**
     * Test whether we can load default profile.
     * @throws ProfileException if {@link Profile} operations throw.
     */
    public void testLoadProfileModel() throws ProfileException {
        proj = ProjectManager.getManager().makeEmptyProject();
        ProfileConfiguration config = proj.getProfileConfiguration();
        assertNotNull("Can't load profile configuration", config);
        Profile umlProfile = ProfileFacade.getManager().getUMLProfile();
        assertNotNull(umlProfile);
        assertTrue(!umlProfile.getProfilePackages().isEmpty());
        Object model = proj.getUserDefinedModelList().iterator().next();
        if (!config.getProfiles().contains(umlProfile)) {
            config.addProfile(umlProfile, model);
        }
        Collection stereos = config.findAllStereotypesForModelElement(Model
            .getCoreFactory().createClass());
        assertTrue("No stereotypes found in default profiles", 
            stereos.size() > 0);
    }
}
