/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    euluis
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007-2009 The Regents of the University of California. All
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

package org.argouml.kernel;

import static org.argouml.model.Model.getCoreFactory;
import static org.argouml.model.Model.getExtensionMechanismsHelper;
import static org.argouml.model.Model.getFacade;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.argouml.FileHelper;
import org.argouml.application.helpers.ApplicationVersion;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.model.XmiReferenceException;
import org.argouml.model.XmiReferenceRuntimeException;
import org.argouml.persistence.AbstractFilePersister;
import org.argouml.persistence.OpenException;
import org.argouml.persistence.PersistenceManager;
import org.argouml.persistence.SaveException;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.ProfileManager;
import org.argouml.profile.ProfileMother;
import org.argouml.profile.UserDefinedProfile;
import org.argouml.profile.init.InitProfileSubsystem;

/**
 * Tests the {@link ProjectImpl} with profiles, specifically this enables the 
 * testing of the org.argouml.profile subsystem API for the project and the 
 * interactions between {@link ProfileConfiguration} and the {@link Project}.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestProjectWithProfiles extends TestCase {

    private File testCaseDir;

    /*
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeMDR();
        new InitProfileSubsystem().init();
        
        initAppVersion();
        assertNotNull(ApplicationVersion.getVersion());
        String testCaseDirNamePrefix = getClass().getPackage().getName();
        testCaseDir = FileHelper.setUpDir4Test(testCaseDirNamePrefix);
    }
    
    @Override
    protected void tearDown() throws Exception {
        FileHelper.delete(testCaseDir);
        super.tearDown();
    }

    /**
     * Basic test that a new project contains a {@link ProfileConfiguration} 
     * and that this contains the default profiles.
     */
    public void testCreatedProjectContainsProfileConfiguration() {
        List<Profile> defaultProfiles 
            = ProfileFacade.getManager().getDefaultProfiles();               
        
        Project project = ProjectManager.getManager().makeEmptyProject();
        ProfileConfiguration profileConfiguration = 
            project.getProfileConfiguration();
                        
        assertNotNull(profileConfiguration);
        assertNotNull(profileConfiguration.getProfiles());
        
        for (Profile profile : defaultProfiles) {
            assertTrue(profileConfiguration.getProfiles().contains(profile));
        }
    }
    
    /**
     * WARNING: not a unit test, this is more like a functional test, where 
     * several subsystems are tested.
     * 
     * This test does:
     * <ol>
     *   <li>set UML Profile for MetaProfile as a default profile</li> 
     *   <li>create a new project and assert that it has the MetaProfile 
     *   as part of the project's profile configuration</li>
     *   <li>create a dependency from the project's model to the
     *   MetaProfile</li>
     *   <li>remove the MetaProfile from the project's profile 
     *   configuration</li>
     *   <li>assert that the project's model elements that had a dependency to 
     *   the MetaProfile don't get inconsistent</li>
     *   <li>save the project into a new file</li>
     *   <li>reopen the project and assert that the MetaProfile isn't part of 
     *   the profile configuration</li>
     *   <li>assert that the project's model elements that had a dependency to 
     *   the MetaProfile are consistent</li>
     * </ol>
     * @throws OpenException if there was an error during a project load
     * @throws SaveException if there was an error during a project save
     * @throws InterruptedException if save or load was interrupted
     */
    public void testRemoveProfileWithModelThatRefersToProfile()
        throws OpenException, SaveException, InterruptedException {
        // set MetaProfile as a default profile
        ProfileManager profileManager = ProfileFacade.getManager();
        Profile metaProfile = profileManager.getProfileForClass(
                "org.argouml.profile.internal.ProfileMeta");
        if (!profileManager.getDefaultProfiles().contains(metaProfile)) {
            profileManager.addToDefaultProfiles(metaProfile);
        }
        // create a new project and assert that it has the MetaProfile 
        // as part of the project's profile configuration
        Project project = ProjectManager.getManager().makeEmptyProject();
        assertTrue(project.getProfileConfiguration().getProfiles().contains(
                metaProfile));
        Object pckge = null;
        try {
            pckge = metaProfile.getProfilePackages().iterator().next();
        } catch (ProfileException pe) {
            fail();
        }
        assertNotNull(pckge);
        Object theStereotype = null;
        Iterator iter = Model.getFacade().getOwnedElements(pckge).iterator();
        while (theStereotype == null &&  iter.hasNext()) {
            Object elem = iter.next();
            if (Model.getFacade().isAStereotype(elem) &&
                Model.getFacade().getName(elem).equals("Critic")) {
                theStereotype = elem;
            }
        }
        assertNotNull(theStereotype);
        // create a dependency from the project's model to the MetaProfile
        Object model = project.getUserDefinedModelList().get(0);
        assertNotNull(model);
        Object foo = Model.getCoreFactory().buildComment(null, model);
        Model.getCoreHelper().setName(foo, "foo");
        Model.getCoreHelper().addStereotype(foo, theStereotype);
        // remove the MetaProfile from the project's profile configuration
        project.getProfileConfiguration().removeProfile(metaProfile, model);
        // assert that the project's model element that had a dependency to 
        // the MetaProfile doesn't get inconsistent
        theStereotype =
            Model.getFacade().getStereotypes(foo).iterator().next();
        assertNotNull(theStereotype);
        // save the project into a new file
        File file = getFileInTestDir(
                "testRemoveProfileWithModelThatRefersToProfile.zargo");
        AbstractFilePersister persister = saveProject(project, file);
        project.remove();
        
        // reopen the project and assert that the MetaProfile isn't part of 
        // the profile configuration, including the fact that the stereotype 
        // <<Critic>> isn't found
        project = persister.doLoad(file);
        project.postLoad();
        assertFalse(project.getProfileConfiguration().getProfiles().contains(
                metaProfile));
        // assert that the project's model elements that had a dependency to 
        // the UML profile for Java are consistent
        pckge = project.getUserDefinedModelList().get(0);
        assertNotNull(pckge);
        foo = null;
        iter = Model.getFacade().getOwnedElements(pckge).iterator();
        while (foo == null &&  iter.hasNext()) {
            Object elem = iter.next();
            if (Model.getFacade().isAComment(elem) &&
                Model.getFacade().getName(elem).equals("foo")) {
                foo = elem;
            }
        }
        assertNotNull(foo);
        theStereotype =
            Model.getFacade().getStereotypes(foo).iterator().next();
        assertNotNull(theStereotype);
        // TODO: with new reference resolving scheme, the model sub-system will
        // cache the systemId of the profile, open it and resolve the profile 
        // on its own. Thus, the java.util.List will be found and the return 
        // value will be present again...
        //assertNotNull(returnParamType);
    }

    private File getFileInTestDir(String fileName) {
        return new File(testCaseDir, fileName);
    }
    
    /**
     * WARNING: not a unit test, this is more like a functional test, where 
     * several subsystems are tested.
     * 
     * This test does:
     * <ol>
     *   <li>setup a user defined profile</li>
     *   <li>add it to the project configuration</li>
     *   <li>create a dependency between the project's model and the user 
     *   defined profile</li>
     *   <li>save the project</li>
     *   <li>load the project and assert that the model element that depends on 
     *   the profile is consistent</li>
     * </ol>
     * 
     * @throws Exception when things go wrong
     */
    public void testProjectWithUserDefinedProfilePersistency() 
        throws Exception {
        // setup a user defined profile
        File userDefinedProfileFile = createUserProfileFile(testCaseDir,
            "testProjectWithUserDefinedProfilePersistency-TestUserProfile.xmi");
        // add it to the project configuration
        ProfileManager profileManager = ProfileFacade.getManager();
        Profile userDefinedProfile = 
            new UserDefinedProfile(userDefinedProfileFile, profileManager);
        profileManager.registerProfile(userDefinedProfile);
        profileManager.addSearchPathDirectory(testCaseDir.getAbsolutePath());
        Project project = ProjectManager.getManager().makeEmptyProject();
        Object model = project.getUserDefinedModelList().get(0);
        Model.getCoreHelper().setName(model, 
                "testProjectWithUserDefinedProfilePersistency-model");
        // create a dependency between the project's model and the user defined 
        // profile
        project.getProfileConfiguration().addProfile(userDefinedProfile, model);
        Object fooClass = getCoreFactory().buildClass(
                "testProjectWithUserDefinedProfilePersistency-class", model);
        Collection stereotypes = getExtensionMechanismsHelper().getStereotypes(
                project.getModels());
        Object stStereotype = null;
        for (Object stereotype : stereotypes) {
            if (ProfileMother.STEREOTYPE_NAME_ST.equals(
                    getFacade().getName(stereotype))) {
                stStereotype = stereotype;
                break;
            }
        }
        assertNotNull("Didn't find stereotype", stStereotype);
        Model.getCoreHelper().addStereotype(fooClass, stStereotype);
        assertEquals("Setting stereotype didn't work", 1, 
                getFacade().getStereotypes(fooClass).size());
        // save the project
        File file = getFileInTestDir(
            "testProjectWithUserDefinedProfilePersistency.zargo");
        AbstractFilePersister persister = saveProject(project, file);
        project.remove();
        
        // load the project
        project = persister.doLoad(file);
        project.postLoad();
        // assert that the model element that depends on the profile is 
        // consistent
        fooClass = project.findType(
                "testProjectWithUserDefinedProfilePersistency-class", false);
        assertNotNull(fooClass);
        Collection fooStereotypes = getFacade().getStereotypes(fooClass);
        assertEquals(1, fooStereotypes.size());
        assertEquals(ProfileMother.STEREOTYPE_NAME_ST, 
                getFacade().getName(fooStereotypes.iterator().next()));
    }

    
    /**
     * WARNING: not a unit test, this is more like a functional test, where 
     * several subsystems are tested.
     * 
     * This test does:
     * <ol>
     *   <li>setup a user defined profile</li>
     *   <li>add it to the project configuration</li>
     *   <li>create a dependency between the project's model and the user 
     *   defined profile</li>
     *   <li>save the project</li>
     *   <li>remove the directory where the user defined profile was stored in 
     *   order to test the handling of opening a zargo without the user defined
     *   profile</li>
     *   <li>initialize the model and profile subsystems to simulate a fresh 
     *   ArgoUML session</li>
     *   <li>load the project and assert that the model element that depends on 
     *   the profile is consistent</li>
     * </ol>
     * 
     * @throws Exception when things go wrong
     */
    public void testProjectWithRemovedUserDefinedProfilePersistency() 
        throws Exception {
        final String testName = 
            "testProjectWithRemovedUserDefinedProfilePersistency";
        File userDefinedProfileFile = createUserProfileFile(testCaseDir,
            testName + "-TestUserProfile.xmi");
        // add it to the project configuration
        ProfileManager profileManager = ProfileFacade.getManager();
        Profile userDefinedProfile = 
            new UserDefinedProfile(userDefinedProfileFile, profileManager);
        profileManager.registerProfile(userDefinedProfile);
        profileManager.addSearchPathDirectory(testCaseDir.getAbsolutePath());
        Project project = ProjectManager.getManager().makeEmptyProject();
        Object model = project.getUserDefinedModelList().get(0);
        // create a dependency between the project's model and the user defined 
        // profile
        project.getProfileConfiguration().addProfile(userDefinedProfile, model);
        final String className = "Foo4" + testName;
        Object fooClass = getCoreFactory().buildClass(className, model);
        Collection stereotypes = getExtensionMechanismsHelper().getStereotypes(
                project.getModels());
        Object stStereotype = null;
        for (Object stereotype : stereotypes) {
            if (ProfileMother.STEREOTYPE_NAME_ST.equals(
                    getFacade().getName(stereotype))) {
                stStereotype = stereotype;
                break;
            }
        }
        Model.getCoreHelper().addStereotype(fooClass, stStereotype);
        // save the project
        File file = getFileInTestDir(testName + ".zargo");
        AbstractFilePersister persister = saveProject(project, file);
        // remove the user defined profile and the directory where it is
        profileManager.removeProfile(userDefinedProfile);
        profileManager.removeSearchPathDirectory(testCaseDir.getAbsolutePath());
        // initialize the model and profile subsystems to simulate a fresh 
        // ArgoUML session
        InitializeModel.initializeMDR();
        new InitProfileSubsystem().init();
        try {
            project = persister.doLoad(file);
            fail("Failed to throw exception for missing user defined profile");
        } catch (OpenException e) {
            // Success - expected exception
            Throwable cause = e.getCause();
            assertTrue("Cause isn't of one of the expected types.",
                cause instanceof XmiReferenceRuntimeException
                || cause instanceof XmiReferenceException);
        }
    }

    /**
     * @param project the ArgoUML {@link Project} to save in file.
     * @param file the {@link File} in which an ArgoUML {@link Project} will
     * be persisted.
     * @return the persister used and usable for file.
     * @throws SaveException if saving the file goes wrong.
     * @throws InterruptedException if an interrupt occurs while saving.
     * TODO: move this to an helper class.
     */
    public static AbstractFilePersister saveProject(Project project, File file)
        throws SaveException, InterruptedException {
        AbstractFilePersister persister = getProjectPersister(file);
        project.setVersion(ApplicationVersion.getVersion());
        persister.save(project, file);
        return persister;
    }

    /**
     * Get an {@link AbstractFilePersister} for file.
     *
     * @param file the {@link File} in which an ArgoUML {@link Project} will
     * be persisted.
     * @return the appropriate persister for file or null if the file's
     * extension doesn't match a supported persister.
     * TODO: move this to an helper class.
     */
    public static AbstractFilePersister getProjectPersister(File file) {
        AbstractFilePersister persister = 
            PersistenceManager.getInstance().getPersisterFromFileName(
                file.getAbsolutePath());
        return persister;
    }

    /**
     * Initialize the ArgoUML application version, so that
     * {@link ApplicationVersion#getVersion()} doesn't return null.
     *
     * @throws Exception if something goes wrong...
     * TODO: move this to an helper class.
     */
    @SuppressWarnings("unchecked")
    public static void initAppVersion() throws Exception {
        if (ApplicationVersion.getVersion() == null) {
            Class argoVersionClass = 
                Class.forName("org.argouml.application.ArgoVersion");
            Method initMethod = argoVersionClass.getDeclaredMethod("init");
            initMethod.setAccessible(true);
            initMethod.invoke(null);
        }
    }

    private File createUserProfileFile(File directory, String filename)
        throws IOException {
        ProfileMother mother = new ProfileMother();
        Object profileModel = mother.createSimpleProfileModel();
        Model.getCoreHelper().setName(profileModel, filename);
        File userDefinedProfileFile = new File(directory, filename);
        mother.saveProfileModel(profileModel, userDefinedProfileFile);
        // Clean up after ourselves by deleting profile model
        Model.getUmlFactory().delete(profileModel);
        return userDefinedProfileFile;
    }

}
