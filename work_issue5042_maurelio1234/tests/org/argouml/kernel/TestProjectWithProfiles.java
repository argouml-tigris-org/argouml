// $Id$
// Copyright (c) 2007-2008 The Regents of the University of California. All
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
import static org.argouml.model.Model.getModelManagementFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;

import junit.framework.TestCase;

import org.argouml.FileHelper;
import org.argouml.application.helpers.ApplicationVersion;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.persistence.AbstractFilePersister;
import org.argouml.persistence.OpenException;
import org.argouml.persistence.PersistenceManager;
import org.argouml.persistence.SaveException;
import org.argouml.persistence.XmiReferenceException;
import org.argouml.profile.Profile;
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
        
        if (ApplicationVersion.getVersion() == null) {
            Class argoVersionClass = 
                Class.forName("org.argouml.application.ArgoVersion");
            Method initMethod = argoVersionClass.getDeclaredMethod("init");
            initMethod.setAccessible(true);
            initMethod.invoke(null);
            assertNotNull(ApplicationVersion.getVersion());
        }
        String testCaseDirName = getClass().getPackage().getName();
        testCaseDir = FileHelper.setUpDir4Test(testCaseDirName);
    }
    
    @Override
    protected void tearDown() throws Exception {
        FileHelper.deleteDir(testCaseDir);
        super.tearDown();
    }

    /**
     * Basic test that a new project contains a {@link ProfileConfiguration} 
     * and that this contains at least the UML profile.
     */
    public void testCreatedProjectContainsProfileConfiguration() {
        Project project = ProjectManager.getManager().makeEmptyProject();
        ProfileConfiguration profileConfiguration = 
            project.getProfileConfiguration();
        assertNotNull(profileConfiguration);
        assertNotNull(profileConfiguration.getProfiles());
        assertTrue(profileConfiguration.getProfiles().contains(
                ProfileFacade.getManager().getUMLProfile()));
    }
    
    /**
     * WARNING: not a unit test, this is more like a functional test, where 
     * several subsystems are tested.
     * 
     * This test does:
     * <ol>
     *   <li>set UML Profile for Java as a default profile</li> 
     *   <li>create a new project and assert that it has the UML profile for 
     *   Java as part of the project's profile configuration</li>
     *   <li>create a dependency from the project's model to the UML profile 
     *   for Java</li>
     *   <li>remove the Java profile from the project's profile 
     *   configuration</li>
     *   <li>assert that the project's model elements that had a dependency to 
     *   the UML profile for Java don't get inconsistent</li>
     *   <li>save the project into a new file</li>
     *   <li>reopen the project and assert that the Java profile isn't part of 
     *   the profile configuration</li>
     *   <li>assert that the project's model elements that had a dependency to 
     *   the UML profile for Java are consistent</li>
     * </ol>
     * @throws OpenException if there was an error during a project load
     * @throws SaveException if there was an error during a project save
     * @throws InterruptedException if save or load was interrupted
     */
    public void testRemoveProfileWithModelThatRefersToProfile()
        throws OpenException, SaveException, InterruptedException {
        // set UML Profile for Java as a default profile
        ProfileManager profileManager = ProfileFacade.getManager();
        Profile javaProfile = profileManager.getProfileForClass(
                "org.argouml.profile.internal.ProfileJava");
        if (!profileManager.getDefaultProfiles().contains(javaProfile)) {
            profileManager.addToDefaultProfiles(javaProfile);
        }
        // create a new project and assert that it has the UML profile for 
        // Java as part of the project's profile configuration
        Project project = ProjectManager.getManager().makeEmptyProject();
        assertTrue(project.getProfileConfiguration().getProfiles().contains(
                javaProfile));
        // create a dependency from the project's model to the UML profile for 
        // Java
        Object model = getModelManagementFactory().getRootModel();
        assertNotNull(model);
        Object fooClass = Model.getCoreFactory().buildClass("Foo", model);
        Object javaListType = project.findType("List", false);
        assertNotNull(javaListType);
        Object barOperation = Model.getCoreFactory().buildOperation2(fooClass, 
                javaListType, "bar");
        assertEquals(barOperation, 
                getFacade().getOperations(fooClass).iterator().next());
        Object returnParam = getFacade().getParameter(barOperation, 0);
        assertNotNull(returnParam);
        Object returnParamType = getFacade().getType(returnParam);
        checkJavaListTypeExistsAndMatchesReturnParamType(project, 
                returnParamType);
        // remove the Java profile from the project's profile configuration
        project.getProfileConfiguration().removeProfile(javaProfile);
        // assert that the project's model elements that had a dependency to 
        // the UML profile for Java don't get inconsistent
        returnParamType = getFacade().getType(returnParam);
        checkJavaListTypeExistsAndMatchesReturnParamType(project, 
                returnParamType);
        assertNotNull(project.findType("Foo", false));
        // save the project into a new file
        File file = getFileInTestDir(
                "testRemoveProfileWithModelThatRefersToProfile.zargo");
        AbstractFilePersister persister = getProjectPersister(file);
        project.setVersion(ApplicationVersion.getVersion());
        persister.save(project, file);
        // reopen the project and assert that the Java profile isn't part of 
        // the profile configuration, including the fact that the type 
        // java.util.List isn't found
        project = persister.doLoad(file);
        project.postLoad();
        assertFalse(project.getProfileConfiguration().getProfiles().contains(
                javaProfile));
        assertNull(project.findType("List", false));
        // assert that the project's model elements that had a dependency to 
        // the UML profile for Java are consistent
        fooClass = project.findType("Foo", false);
        assertNotNull(fooClass);
        barOperation = getFacade().getOperations(fooClass).iterator().next();
        returnParam = getFacade().getParameter(barOperation, 0);
        assertNotNull(returnParam);
        // Return type was java.util.List from Java profile - should be gone
        returnParamType = getFacade().getType(returnParam);
        // TODO: with new reference resolving scheme, the model sub-system will
        // cache the systemId of the profile, open it and resolve the profile 
        // on its own. Thus, the java.util.List will be found and the return 
        // value will be present again...
        assertNotNull(returnParamType);
    }

    private void checkJavaListTypeExistsAndMatchesReturnParamType(
            Project project, Object returnParamType) {
        Object javaListType = project.findType("List", false);
        assertNotNull(javaListType);
        assertEquals(getFacade().getName(javaListType), 
                getFacade().getName(returnParamType));
        assertEquals(getFacade().getNamespace(javaListType), 
                getFacade().getNamespace(returnParamType));
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
        Profile userDefinedProfile = 
            new UserDefinedProfile(userDefinedProfileFile);
        ProfileManager profileManager = ProfileFacade.getManager();
        profileManager.registerProfile(userDefinedProfile);
        profileManager.addSearchPathDirectory(testCaseDir.getAbsolutePath());
        Project project = ProjectManager.getManager().makeEmptyProject();
        project.getProfileConfiguration().addProfile(userDefinedProfile);
        // create a dependency between the project's model and the user defined 
        // profile
        Object model = getModelManagementFactory().getRootModel();
        Model.getCoreHelper().setName(model, 
                "testProjectWithUserDefinedProfilePersistency-model");
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
        AbstractFilePersister persister = getProjectPersister(file);
        project.setVersion(ApplicationVersion.getVersion());
        persister.save(project, file);
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
        Profile userDefinedProfile = 
            new UserDefinedProfile(userDefinedProfileFile);
        ProfileManager profileManager = ProfileFacade.getManager();
        profileManager.registerProfile(userDefinedProfile);
        profileManager.addSearchPathDirectory(testCaseDir.getAbsolutePath());
        Project project = ProjectManager.getManager().makeEmptyProject();
        project.getProfileConfiguration().addProfile(userDefinedProfile);
        // create a dependency between the project's model and the user defined 
        // profile
        Object model = getModelManagementFactory().getRootModel();
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
        AbstractFilePersister persister = getProjectPersister(file);
        project.setVersion(ApplicationVersion.getVersion());
        persister.save(project, file);
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
        } catch (XmiReferenceException e) {
            // success
        }
    }

    private AbstractFilePersister getProjectPersister(File file) {
        AbstractFilePersister persister = 
            PersistenceManager.getInstance().getPersisterFromFileName(
                    file.getAbsolutePath());
        return persister;
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
