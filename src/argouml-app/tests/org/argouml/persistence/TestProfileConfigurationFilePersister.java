/* $Id$
 *****************************************************************************
 * Copyright (c) 2009,2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    euluis
 *    Tom Morris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008-2009 The Regents of the University of California. All
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

package org.argouml.persistence;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.argouml.application.api.Argo;
import org.argouml.kernel.ProfileConfiguration;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiWriter;
import org.argouml.profile.FileModelLoader;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.UserProfileReference;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.profile.internal.ProfileUML;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Tests for the {@link ProfileConfigurationFilePersister} class.
 * 
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestProfileConfigurationFilePersister extends TestCase {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
    }

    @Override
    protected void tearDown() throws Exception {
        ProfileFacade.reset();
        super.tearDown();
    }
    
    /**
     * Tests whether XmiWriterMDRImpl fails to write a profile 
     * (i.e., the file will contain no model) previously loaded from a file.
     * 
     * @throws ProfileException if the loading of the profile fails.
     * @throws IOException if an IO error occurs while writing or reading a 
     * file.
     * @throws FileNotFoundException if a file isn't found.
     * @throws UmlException if an error occurs while writing the profile in 
     * XMI format.
     */
    public void testWritePreviouslyLoadedProfile() 
        throws ProfileException, FileNotFoundException, IOException, 
        UmlException {
        
        Object umlModel = ProfileFacade.getManager().getUMLProfile()
                .getProfilePackages().iterator().next();
        final String umlModelName = Model.getFacade().getName(umlModel);
        assertNotNull(umlModelName);
        File tempFile = File.createTempFile(umlModelName, ".xmi");
        FileOutputStream stream = new FileOutputStream(tempFile);
        XmiWriter xmiWriter = Model.getXmiWriter(umlModel, stream, "version-x");
        xmiWriter.write();
        stream.close();
        FileModelLoader fileModelLoader = new FileModelLoader();
        Collection loadedModel = fileModelLoader.loadModel(
                new UserProfileReference(tempFile.getPath()));
        assertEquals(1, loadedModel.size());
        umlModel = loadedModel.iterator().next();
        assertEquals(umlModelName, Model.getFacade().getName(umlModel));
    }

    private static final String TEST_PROFILE = 
        "<?xml version = \"1.0\" encoding = \"UTF-8\" ?>\n"
        // Although we've historically written out the DOCTYPE, the DTD doesn't
        // actually exist and this line will get stripped by the .uml file
        // persister
//        + "<!DOCTYPE profile SYSTEM \"profile.dtd\" >\n"
        + "<profile>\n"

        // Standard UML 1.4 profile
        + "\t\t<plugin>\n"
        + "\t\t\tUML 1.4\n"
        + "\t\t</plugin>\n"

        // TODO: User defined profile support untested currently
//        + "\t\t<userDefined>\n"
//        + "\t\t\t<filename>\n"
//        + "foo.profile\n"
//        + "</filename>\n"
//        + "\t\t\t<model>\n"
//        + "foo.profile.package\n"
//        + "\t\t\t</model>\n"
//        + "\t\t</userDefined>\n"
        
        + "</profile>";
        
    /**
     * Test the basic profile configuration parser.
     * 
     * @throws SAXException on a parse failure
     * @throws UnsupportedEncodingException if our default encoding (UTF-8) is
     *             unsupported. Should never happen.
     */
    public void testProfileConfigurationParser() throws SAXException,
        UnsupportedEncodingException {
        InputStream inStream = 
            new ByteArrayInputStream(
                    TEST_PROFILE.getBytes(Argo.getEncoding()));
        ProfileConfigurationParser parser = new ProfileConfigurationParser();
        parser.parse(new InputSource(inStream));
        Collection<Profile> profiles = parser.getProfiles();
        assertEquals("Wrong number of profiles", 1, profiles.size());
        Iterator<Profile> profileIter = profiles.iterator();
        assertTrue("Didn't get expected UML profile", 
                profileIter.next() instanceof ProfileUML);
    }
    
    /**
     * Test that we can save and restore the default profile configuration.
     * 
     * @throws IOException on io error
     * @throws SaveException on save error
     * @throws OpenException on load error
     */
    public void testSaveLoadDefaultConfiguration() throws IOException,
        SaveException, OpenException {
        
        // Create a default profile and record its contents
        Project project = ProjectManager.getManager().makeEmptyProject();
        ProfileConfiguration pc = new ProfileConfiguration(project);
        Collection<Profile> startingProfiles = 
            new ArrayList<Profile>(pc.getProfiles());

        // Write the profile out to a temp file
        ProfileConfigurationFilePersister persister = 
            new ProfileConfigurationFilePersister();
        File file = File.createTempFile(this.getName(), ".profile");
        OutputStream outStream = new FileOutputStream(file);
        persister.save(pc, outStream);
        outStream.close();
        
        // Read it back in to a new empty project
        project = ProjectManager.getManager().makeEmptyProject();
        persister.load(project,
                new InputSource(file.toURI().toURL().toExternalForm()));
 
        // Make sure we got what we started with
        assertEquals(startingProfiles, 
                project.getProfileConfiguration().getProfiles());
    }
}
