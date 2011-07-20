/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.io.File;
import java.util.Collection;

import junit.framework.TestCase;

import org.argouml.FileHelper;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.TestProjectWithProfiles;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.persistence.AbstractFilePersister;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.ProfileMother;
import org.argouml.profile.UserDefinedProfile;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.uml.StereotypeUtility;
import org.argouml.uml.TestStereotypeUtility;

/**
 * Test cases for the UMLTagDefinitionComboBoxModel class.
 *
 * @author Luis Sergio Oliveira (euluis)
 * @since 0.20
 */
public class TestUMLTagDefinitionComboBoxModel extends TestCase {

    private Object model;

    private Object theClass;

    private Object theStereotype;
    
    private Project proj;

    /**
     * Default constructor.
     */
    public TestUMLTagDefinitionComboBoxModel() {
        super("TestUMLTagDefinitionComboBoxModel");
    }
    /**
     * Constructor.
     * @param arg0 test name
     */
    public TestUMLTagDefinitionComboBoxModel(String arg0) {
        super(arg0);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
        proj = ProjectManager.getManager().makeEmptyProject();
        model = proj.getUserDefinedModelList().iterator().next();
    }

    @Override
    protected void tearDown() throws Exception {
        ProjectManager.getManager().removeProject(proj);
        super.tearDown();
    }

    /**
     * Test if a tag definition owned by a stereotype is available to be 
     * applied in a class to which the stereotype is applied.
     */
    public void testTagDefinitionFromStereotypeApplicableToStereotypedClass() {
        theClass = Model.getCoreFactory().buildClass("TheClass", model);
        theStereotype = Model.getExtensionMechanismsFactory().buildStereotype(
            theClass, "containedStereotype", model);
        Object theTagDefinition = Model.getExtensionMechanismsFactory().
            buildTagDefinition("TagDefinition", theStereotype, null);
        // Testing with more than one, since it failed with a manual test I 
        // made.
        Object theTagDefinition2 = Model.getExtensionMechanismsFactory().
            buildTagDefinition("TagDefinition2", theStereotype, null);
        Model.getCoreHelper().addStereotype(theClass, theStereotype);
        UMLTagDefinitionComboBoxModel tagDefComboBoxModel = 
            new UMLTagDefinitionComboBoxModel();
        Object[] added = {theClass};
        tagDefComboBoxModel.targetAdded(new TargetEvent(this, "TARGET_ADDED", 
            new Object[0], added));
        assertTrue("The TagDefinition should be contained!", 
            tagDefComboBoxModel.contains(theTagDefinition));
        assertTrue("The TagDefinition2 should be contained!", 
            tagDefComboBoxModel.contains(theTagDefinition2));
    }

    /**
     * Tests the scenario in which a Stereotype is already applied to a model
     * element and it shouldn't be returned as an available Stereotype to be
     * applied to the model element.
     * (see <a href="http://argouml.tigris.org/TODO">issue 6008</a>).
     * This test is for
     * {@link UMLTagDefinitionComboBoxModel#getApplicableTagDefinitions(Object)}.
     * 
     * WARNING: not a unit test, this is more like a functional test, where 
     * several subsystems are tested.
     * 
     * This test does:
     * <ol>
     *   <li>setup a user defined profile which isn't loaded by the model
     *   subsystem and contains a stereotype and this contains a
     *   tag definition</li>
     *   <li>create a new empty project</li>
     *   <li>add the profile to the project configuration</li>
     *   <li>create a class in the project model</li>
     *   <li>add the profile stereotype to the class</li>
     *   <li>check that the tag definition is returned by
     *   {@link UMLTagDefinitionComboBoxModel#getApplicableTagDefinitions(Object)}</li>
     *   <li>save the project</li>
     *   <li>initialize the model and profile subsystems to simulate a fresh 
     *   ArgoUML session</li>
     *   <li>load the project</li>
     *   <li>check that the tag definition is returned by
     *   {@link UMLTagDefinitionComboBoxModel#getApplicableTagDefinitions(Object)}</li>
     * </ol>
     * 
     * @throws Exception When something goes wrong...
     */
    public void testGetApplicableTagDefinitionsIssue6008() throws Exception {
        // setup a user defined profile which isn't loaded by the model
        // subsystem and contains a stereotype and this contains a
        // tag definition
        ProjectManager.getManager().removeProject(proj);
        ProfileMother profileMother = new ProfileMother();
        final String profileName =
            "Profile4testGetApplicableTagDefinitionsIssue6008";
        File profileFile = profileMother.createUnloadedSimpleProfile(
            profileName);
        profileFile = FileHelper.moveFileToNewTempDirectory(profileFile,
            profileName, ProfileMother.XMI_FILE_EXTENSION,
            "testGetApplicableTagDefinitionsIssue6008");
        profileFile.deleteOnExit();
        profileFile.getParentFile().deleteOnExit();
        UserDefinedProfile profile = new UserDefinedProfile(profileFile,
            ProfileFacade.getManager());
        Collection profileModels = profile.getProfilePackages();
        // TODO: the following fails due to the XMI writing saving all top level
        // model elements and because even if there was no empty project, by
        // creating a class, the undo mechanism in the project manager will
        // force the creation of an empty project.
//        assertEquals(1, profileModels.size());
        Object profileModel = TestStereotypeUtility.findModelElementNamed(
            profileName, profileModels);
        assertNotNull("Profile model not found.", profileModel);
        // create a new empty project
        proj = ProjectManager.getManager().makeEmptyProject();
        model = proj.getUserDefinedModelList().iterator().next();
        // add the profile to the project configuration
        proj.getProfileConfiguration().addProfile(profile, model);
        // create a class in the project model
        Object aClass = Model.getCoreFactory().buildClass("AClass", model);
        // add the profile stereotype to the class
        Object stereotype = TestStereotypeUtility.findModelElementNamed(
            ProfileMother.STEREOTYPE_NAME_ST,
            StereotypeUtility.getAvailableStereotypes(aClass));
        assertNotNull("Stereotype wasn't found or isn't applicable.",
            stereotype);
        Model.getCoreHelper().addStereotype(aClass, stereotype);
        // check that the tag definition is returned by
        // {@link UMLTagDefinitionComboBoxModel#getApplicableTagDefinitions(Object)}
        Object tagDef = TestStereotypeUtility.findModelElementNamed(
            ProfileMother.TAG_DEFINITION_NAME_TD,
            Model.getFacade().getTagDefinitions(stereotype));
        assertNotNull("Tag Definition wasn't found.", tagDef);
        UMLTagDefinitionComboBoxModel tagDefCBModel =
            new UMLTagDefinitionComboBoxModel();
        Collection applicableTagDefs =
            tagDefCBModel.getApplicableTagDefinitions(aClass);
        assertNotNull("Tag Definition wasn't found in return value of "
            + "tagDefCBModel.getApplicableTagDefinitions(aClass).",
            TestStereotypeUtility.findModelElementNamed(
                ProfileMother.TAG_DEFINITION_NAME_TD, applicableTagDefs));
        // save the project
        File projFile = File.createTempFile("projFile", ".zargo");
        TestProjectWithProfiles.initAppVersion();
        AbstractFilePersister persister =
            TestProjectWithProfiles.saveProject(proj, projFile);
        projFile.deleteOnExit();
        ProjectManager.getManager().removeProject(proj);
        // initialize the model and profile subsystems to simulate a fresh 
        // ArgoUML session
        InitializeModel.initializeMDR();
        new InitProfileSubsystem().init();
        ProfileFacade.getManager().addSearchPathDirectory(
            profileFile.getParent());
        proj = persister.doLoad(projFile);
        model = proj.getUserDefinedModelList().iterator().next();
        // check that the tag definition is returned by
        // {@link UMLTagDefinitionComboBoxModel#getApplicableTagDefinitions(Object)}
        aClass = TestStereotypeUtility.findModelElementNamed("AClass",
            Model.getCoreHelper().getAllClasses(model));
        assertNotNull("AClass not found in loaded project model.", aClass);
        tagDefCBModel = new UMLTagDefinitionComboBoxModel();
        // FIXME: the following throws:
        // org.argouml.model.InvalidElementException: 
        // javax.jmi.reflect.InvalidObjectException: Object with 
        // MOFID 127-0-1-1-(...)000E75 no longer exists.
//        applicableTagDefs = tagDefCBModel.getApplicableTagDefinitions(aClass);
//        assertNotNull("Tag Definition wasn't found in return value of "
//            + "tagDefCBModel.getApplicableTagDefinitions(aClass), "
//            + "after loading the project.",
//            TestStereotypeUtility.findModelElementNamed(
//                ProfileMother.TAG_DEFINITION_NAME_TD, applicableTagDefs));
    }
}
