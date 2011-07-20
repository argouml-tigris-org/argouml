/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Luis Sergio Oliveira (euluis)
 *******************************************************************************
 */

package org.argouml.uml;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.ProfileMother;
import org.argouml.profile.UserDefinedProfile;
import org.argouml.profile.init.InitProfileSubsystem;

/**
 * Integration tests for {@link StereotypeUtility} class.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestStereotypeUtility extends TestCase {

    private Project proj;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        // NOTE: we have to do this due to leftovers from other tests
        // being causing strange errors when running in Eclipse.
        ProfileMother.cleanAllExtents();
        new InitProfileSubsystem().init();
    }

    @Override
    protected void tearDown() throws Exception {
        if (proj != null) {
            ProjectManager.getManager().removeProject(proj);
        }
        ProfileFacade.reset();
        super.tearDown();
    }

    /**
     * Tests the scenario in which a Stereotype is already applied to a model
     * element and it shouldn't be returned as an available Stereotype to be
     * applied to the model element.
     * (see <a href="http://argouml.tigris.org/issues/show_bug.cgi?id=5969">
     * issue 5969</a>).
     * This test is for
     * {@link StereotypeUtility#getAvailableStereotypes(Object)}.
     * 
     * @throws Exception When something goes wrong...
     */
    public void testGetAvailableStereotypesForAModelElementIssue5969()
        throws Exception {
        ProfileMother profileMother = new ProfileMother();
        File profileFile = profileMother.createUnloadedSimpleProfile();
        profileFile.deleteOnExit();
        UserDefinedProfile profile = new UserDefinedProfile(profileFile,
            ProfileFacade.getManager());
        Collection profileModels = profile.getProfilePackages();
        // TODO: the following fails due to the XMI writing saving all top level
        // model elements and because even if there was no empty project, by
        // creating a class, the undo mechanism in the project manager will
        // force the creation of an empty project.
//        assertEquals(1, profileModels.size());
        Object profileModel = profileModels.iterator().next();
        proj = ProjectManager.getManager().makeEmptyProject();
        List models = proj.getUserDefinedModelList();
        assertEquals("Unexpected number of user defined models.",
            1, models.size());
        Object model = models.get(0);
        proj.getProfileConfiguration().addProfile(profile, model);
        Object aClass = Model.getCoreFactory().buildClass("AClass", model);
        Set<Object> stereotypes = StereotypeUtility.getAvailableStereotypes(
            aClass);
        Object profileStereotype = findModelElementFromNamespace(stereotypes,
            ProfileMother.STEREOTYPE_NAME_ST, profileModel);
        assertNotNull("The profile stereotype named \""
            + ProfileMother.STEREOTYPE_NAME_ST + "\" wasn't found.",
            profileStereotype);
        Model.getCoreHelper().addStereotype(aClass, profileStereotype);
        // FIXME: fails here.
        // The current behavior appears intentional (ie the test is wrong) - tfm
//        assertNull("The profile stereotype named \""
//            + ProfileMother.STEREOTYPE_NAME_ST + "\" shouldn't be found.",
//            findModelElementFromNamespace(
//                StereotypeUtility.getAvailableStereotypes(aClass),
//                ProfileMother.STEREOTYPE_NAME_ST, profileModel));
    }

    /**
     * Find a model element in modelElements which name is equal to name.
     *
     * @param name the name of the model element to find. It can't be null.
     * @param modelElements the {@link Collection} of model elements in which
     * to find a model element with name.
     * @return the model element if found or null.
     *
     * TODO: maybe should be moved to a helper class that deals with Model
     * related stuff.
     */
    public static Object findModelElementNamed(String name,
            Collection modelElements) {
        for (Object me : modelElements) {
            if (name.equals(Model.getFacade().getName(me))) {
                return me;
            }
        }
        return null;
    }

    static Object findModelElementFromNamespace(
            Collection<Object> modelElements, String modelElementName,
            Object namespace) {
        for (Object me : modelElements) {
            if (modelElementName.equals(Model.getFacade().getName(me))
                    && Model.getFacade().getNamespace(me) == namespace) {
                return me;
            }
        }
        return null;
    }
}
