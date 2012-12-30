/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    maurelio1234
 *    euluis
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.profile;

import static org.argouml.model.Model.getExtensionMechanismsHelper;
import static org.argouml.model.Model.getFacade;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.argouml.FileHelper;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.xml.sax.InputSource;

/**
 * Integration tests for the {@link ProfileMother} class.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestProfileMother extends TestCase {

    private ProfileMother mother;

    @Override
    protected void setUp() throws Exception {
        InitializeModel.initializeDefault();
        mother = new ProfileMother();
    }

    protected void tearDown() throws Exception {
        ProfileFacade.reset();
        super.tearDown();
    }

    /**
     * Test the creation of a profile model.
     */
    public void testCreateProfileModel() {
        Object model = mother.createSimpleProfileModel();
        assertNotNull(model);
        Collection profileStereotypes = getFacade().getStereotypes(model);
        assertEquals(1, profileStereotypes.size());
        assertEquals(ProfileMother.STEREOTYPE_NAME_PROFILE, 
            getFacade().getName(profileStereotypes.iterator().next()));
        Model.getUmlFactory().delete(model);
    }

    /**
     * Test the creation of a simple profile model and check that specific
     * model elements are contained in it.
     */
    public void testCreateSimpleProfileModel() {
        final Object model = mother.createSimpleProfileModel();
        Collection<Object> models = new ArrayList<Object>() { {
                add(model);
            }
        };
        Collection stereotypes = 
            getExtensionMechanismsHelper().getStereotypes(models);
        Object st = null;
        for (Object stereotype : stereotypes) {
            if (ProfileMother.STEREOTYPE_NAME_ST.equals(
                    getFacade().getName(stereotype))) {
                st = stereotype;
                break;
            }
        }
        assertNotNull("\"st\" stereotype not found in model.", st);
        assertTrue(Model.getExtensionMechanismsHelper().isStereotype(st, 
            ProfileMother.STEREOTYPE_NAME_ST, "Class"));
        Model.getUmlFactory().delete(model);
    }

    /**
     * Test saving a profile model.
     *
     * @throws Exception when saving the profile model fails
     */
    public void testSaveProfileModel() throws Exception {
        Object model = mother.createSimpleProfileModel();
        File file = File.createTempFile("testSaveProfileModel", ".xmi");
        mother.saveProfileModel(model, file);
        assertTrue("The file to where the file was supposed to be saved " 
            + "doesn't exist.", file.exists());
        Model.getUmlFactory().delete(model);
    }

    /**
     * Test the creation of a profile which depends on another profile.
     * Doesn't use the {@link ProfileMother#createXmiDependentProfile(File, ProfileMother.DependencyCreator, File, String)}
     * method, but, it serves as good executable documentation of how this is
     * done as a whole.
     * @throws IOException When saving the profile models fails.
     * @throws UmlException When something in the model subsystem goes wrong.
     */
    public void testXmiDependentProfile() throws IOException, UmlException {
        Object model = mother.createSimpleProfileModel();
        File file = File.createTempFile("simple-profile", ".xmi");
        mother.saveProfileModel(model, file);
        XmiReader xmiReader = Model.getXmiReader();
        xmiReader.addSearchPath(file.getParent());
        InputSource pIs = new InputSource(file.toURI().toURL().toExternalForm());
        pIs.setPublicId(file.getName());
        Collection simpleModelTopElements = xmiReader.parse(pIs, true);
        Object model2 = mother.createSimpleProfileModel();
        Object theClass = Model.getCoreFactory().buildClass("TheClass", model2);
        Collection stereotypes = getFacade().getStereotypes(
            simpleModelTopElements.iterator().next());
        Object stereotype = stereotypes.iterator().next();
        Model.getCoreHelper().addStereotype(theClass, stereotype);
        File dependentProfileFile = File.createTempFile("dependent-profile",
            ".xmi");
        mother.saveProfileModel(model2, dependentProfileFile);
        assertTrue("The file to where the file was supposed to be saved " 
            + "doesn't exist.", dependentProfileFile.exists());
        assertStringInLineOfFile("The name of the file which contains the profile "
            + "from which the dependent profile depends must occur in the "
            + "file.",
            file.getName(), dependentProfileFile);
        // Clean up our two models and the extent that we read profile in to
        Model.getUmlFactory().delete(model);
        Model.getUmlFactory().delete(model2);
        Model.getUmlFactory().deleteExtent(
                simpleModelTopElements.iterator().next());
    }

    /**
     * Test the creation of a profile which depends on another profile.
     * @throws IOException When saving the profile models fails.
     * @throws UmlException When something in the model subsystem goes wrong.
     */
    public void testCreateXmiDependentProfile() throws IOException, UmlException {
        File profilesDir = FileHelper.createTempDirectory();
        File profileFromWhichDependsFile = File.createTempFile(
            "simple-profile", ".xmi", profilesDir);
        Object model = mother.createSimpleProfileModel();
        mother.saveProfileModel(model, profileFromWhichDependsFile);
        Model.getUmlFactory().deleteExtent(model);
        // setting up the dependent profile creation
        ProfileMother.DependencyCreator dependencyCreator =
            new ProfileMother.DependencyCreator() {
            public void create(Object profileFromWhichDepends,
                    Object dependentProfile) {
                Object theClass = Model.getCoreFactory().buildClass("TheClass",
                    dependentProfile);
                Collection stereotypes = getFacade().getStereotypes(
                    profileFromWhichDepends);
                Object stereotype = stereotypes.iterator().next();
                Model.getCoreHelper().addStereotype(theClass, stereotype);
            }
        };
        String dependentProfileFilenamePrefix = "dependent-profile";
        // actual call that executes everything
        File dependentProfileFile = mother.createXmiDependentProfile(
            profileFromWhichDependsFile, dependencyCreator,
            profilesDir, dependentProfileFilenamePrefix);
        // verifications
        assertTrue("The file to where the file was supposed to be saved "
            + "doesn't exist.", dependentProfileFile.exists());
        assertStringInLineOfFile("The name of the file which contains the profile "
            + "from which the dependent profile depends must occur in the "
            + "file.",
            profileFromWhichDependsFile.getName(), dependentProfileFile);
        XmiReader xmiReader = Model.getXmiReader();
        xmiReader.addSearchPath(profilesDir.getAbsolutePath());
        InputSource pIs = new InputSource(
            dependentProfileFile.toURI().toURL().toExternalForm());
        pIs.setPublicId(dependentProfileFile.getName());
        Collection dependentProfileModelTopElements = xmiReader.parse(pIs,
            true);
        assertEquals("There should exist only one top level element.",
            1, dependentProfileModelTopElements.size());
        // Clean up our model and the extent that we read profile in to
        Model.getUmlFactory().delete(model);
        Model.getUmlFactory().deleteExtent(
                dependentProfileModelTopElements.iterator().next());
    }

    private void assertStringInLineOfFile(String failureMsg, String str,
            File file) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        try {
            String line = "";
            while (null != (line = fileReader.readLine())) {
                if (line.contains(str))
                    return;
            }
        } finally {
            if (fileReader != null) {
                fileReader.close();
            }
        }
        throw new AssertionFailedError(failureMsg + " '" + str
            + "' not found in " + file.getName());
    }

    /**
     * Test {@link ProfileMother#createProfileFilePairWith2ndDependingOn1stViaXmi()}.
     * @throws IOException when file IO goes wrong...
     * @throws UmlException when UML manipulation goes wrong...
     */
    public void testCreateProfilePairWith2ndDependingOn1stViaXmi()
            throws IOException, UmlException {
        List<File> profilesFiles =
            mother.createProfileFilePairWith2ndDependingOn1stViaXmi();
        assertEquals("Should contain two elements.", 2, profilesFiles.size());
        File baseFile = profilesFiles.get(0);
        File dependentFile = profilesFiles.get(1);
        assertStringInLineOfFile("", baseFile.getName(), dependentFile);
    }

    /**
     * Test {@link ProfileMother#createUnloadedSimpleProfile()}.
     *
     * @throws IOException when file IO goes wrong...
     * @throws UmlException when UML manipulation goes wrong...
     */
    public void testCreateUnloadedSimpleProfile() throws IOException,
            UmlException {
        File profileFile = mother.createUnloadedSimpleProfile();
        profileFile.deleteOnExit();
        assertTrue("It doesn't exist or isn't a file.",
            profileFile.exists() && profileFile.isFile());
        XmiReader xmiReader = Model.getXmiReader();
        InputSource inputSource = new InputSource(
            profileFile.toURI().toURL().toExternalForm());
        Collection topModelElements = xmiReader.parse(inputSource, true);
        assertEquals("Unexpected number of top model elements.",
            1, topModelElements.size());
        Model.getUmlFactory().deleteExtent(topModelElements.iterator().next());
    }
}
