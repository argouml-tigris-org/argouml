/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
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

import static org.argouml.model.Model.getCoreHelper;
import static org.argouml.model.Model.getExtensionMechanismsFactory;
import static org.argouml.model.Model.getExtensionMechanismsHelper;
import static org.argouml.model.Model.getFacade;
import static org.argouml.model.Model.getModelManagementFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import org.argouml.FileHelper;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.argouml.model.XmiWriter;
import org.argouml.persistence.UmlFilePersister;
import org.xml.sax.InputSource;

/**
 * Based on the 
 * <a href="http://www.xpuniverse.com/2001/pdfs/Testing03.pdf">ObjectMother 
 * design pattern</a>, provides reusable facilities to create profiles.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class ProfileMother {

    /**
     * This interface implementors can be used to create dependencies between
     * profiles.
     * @author Luis Sergio Oliveira (euluis)
     */
    public interface DependencyCreator {
        /**
         * Creates a dependency between dependentProfile and
         * profileFromWhichDepends.
         * @param profileFromWhichDepends the profile model to which
         *        dependentProfile will depend.
         * @param dependentProfile the profile model that will be dependent
         *        of profileFromWhichDepends.
         */
        void create(Object profileFromWhichDepends, Object dependentProfile);
    }

    private static final Logger LOG = Logger.getLogger(ProfileMother.class);

    /**
     * "profile"
     */
    public static final String STEREOTYPE_NAME_PROFILE = "profile";
    /**
     * "st" the example stereotype name.
     */
    public static final String STEREOTYPE_NAME_ST = "st";
    
    private final String DEFAULT_SIMPLE_PROFILE_NAME = "SimpleProfile";

    /**
     * Create a simple profile model with name {@link ProfileMother#DEFAULT_SIMPLE_PROFILE_NAME}
     * with a class named "foo" and with a stereotype named
     * {@link ProfileMother#STEREOTYPE_NAME_ST}.
     * 
     * @return the profile model.
     */
    public Object createSimpleProfileModel() {
        return createSimpleProfileModel(DEFAULT_SIMPLE_PROFILE_NAME);
    }

    /**
     * Create a simple profile model with name profileName,
     * with a class named "foo" and with a stereotype named
     * {@link ProfileMother#STEREOTYPE_NAME_ST}.
     * 
     * @param profileName the name that the created profile shall have.
     * @return the profile model.
     */
    public Object createSimpleProfileModel(String profileName) {
        Object model = getModelManagementFactory().createProfile();
        Object fooClass = Model.getCoreFactory().buildClass("foo", model);
        getExtensionMechanismsFactory().buildStereotype(fooClass, 
                STEREOTYPE_NAME_ST, model);
        getCoreHelper().setName(model, profileName);
        return model;
    }

    Object getProfileStereotype() {
        Object umlProfile = getUmlProfileModel();
        Collection<Object> models = new ArrayList<Object>();
        models.add(umlProfile);
        Collection stereotypes = getExtensionMechanismsHelper().getStereotypes(
            models);
        for (Object stereotype : stereotypes) {
            if (STEREOTYPE_NAME_PROFILE.equals(
                    Model.getFacade().getName(stereotype)))
                return stereotype;
        }
        return null;
    }
    
    private Object umlProfileModel;

    Object getUmlProfileModel() {
        if (ProfileFacade.isInitiated()) {
            try {
                umlProfileModel = ProfileFacade.getManager().getUMLProfile().
                    getProfilePackages().iterator().next();
                TestCase.assertTrue(getFacade().isAModel(umlProfileModel));
            } catch (ProfileException e) {
                LOG.error("Exception", e);
            }
        }
        if (umlProfileModel == null) {
            umlProfileModel = getModelManagementFactory().createModel();
            getExtensionMechanismsFactory().buildStereotype(
                umlProfileModel, STEREOTYPE_NAME_PROFILE, umlProfileModel);
        }
        return umlProfileModel;
    }

    /**
     * Save the profile model into the given file.
     * 
     * @param model the profile model.
     * @param file the file into which to save the profile model.
     * @throws IOException if IO goes wrong.
     */
    public void saveProfileModel(Object model, File file) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(file);
        try {
            XmiWriter xmiWriter = Model.getXmiWriter(model, fileOut, "x(" 
                    + UmlFilePersister.PERSISTENCE_VERSION + ")");
            xmiWriter.write();
            fileOut.flush();
        } catch (Exception e) {
            String msg = "Exception while saving profile model.";
            LOG.error(msg, e);
            throw new IOException(msg);
        } finally {
            fileOut.close();
        }
    }

    /**
     * Create a XMI file that stores a UML profile which depends via XMI
     * of the profile stored in profileFromWhichDependsFile.
     * @param profileFromWhichDependsFile a {@link File} associated to the file
     *        which contains the XMI for the profile from which the created
     *        profile will depend.
     * @param dependencyCreator the object that will be called to actually
     *        create the dependency between the new model and the other.
     * @param profilesDir the directory within which the profile XMI file that
     *        will be created is stored.
     * @param dependentProfileFilenamePrefix the file name prefix for the file
     *        that will contain the XMI of the created profile.
     * @return the {@link File} associated to the file where the created
     * dependent profile is stored (XMI).
     * @throws IOException if the new profile file creation fails.
     * @throws UmlException if the model subsystem throws.
     */
    public File createXmiDependentProfile(File profileFromWhichDependsFile,
            DependencyCreator dependencyCreator,
            File profilesDir, String dependentProfileFilenamePrefix)
        throws IOException, UmlException {
        XmiReader xmiReader = Model.getXmiReader();
        xmiReader.addSearchPath(profileFromWhichDependsFile.getParent());
        InputSource pIs = new InputSource(
            profileFromWhichDependsFile.toURI().toURL().toExternalForm());
        pIs.setPublicId(UserProfileReference.DEFAULT_USER_PROFILE_BASE_URL
            + profileFromWhichDependsFile.getName());
        Collection profileFromWhichDependsModelTopElements = xmiReader.parse(
            pIs, true);
        Object dependentProfile = getModelManagementFactory().createProfile();
        Object profileFromWhichDependsModel = null;
        for (Object topElement : profileFromWhichDependsModelTopElements) {
            if (DEFAULT_SIMPLE_PROFILE_NAME.equals(
                    getFacade().getName(topElement))) {
                profileFromWhichDependsModel = topElement;
                break;
            }
        }
        dependencyCreator.create(profileFromWhichDependsModel,
            dependentProfile);
        File dependentProfileFile = File.createTempFile(
            dependentProfileFilenamePrefix, ".xmi", profilesDir);
        saveProfileModel(dependentProfile, dependentProfileFile);
        Model.getUmlFactory().deleteExtent(dependentProfile);
        xmiReader.removeSearchPath(profileFromWhichDependsFile.getParent());
        return dependentProfileFile;
    }

    /**
     * Creates two profiles with one depending of the other. Saves the two
     * profiles in a temporary directory and returns the associated
     * {@link File Files}, being that the second XMI file depends on the first.
     * @return A list of two files, the second File contains an XMI that
     *         depends of the first.
     * @throws IOException if file IO causes errors.
     * @throws UmlException if the manipulation of models causes errors.
     */
    public List<File> createProfileFilePairWithSecondDependingOnFirstThroughXmi()
            throws IOException, UmlException {
        File profilesDir = FileHelper.createTempDirectory();
        final File baseFile = File.createTempFile(
            "baseProfile", ".xmi", profilesDir);
        Object model = createSimpleProfileModel();
        saveProfileModel(model, baseFile);
        Model.getUmlFactory().deleteExtent(model);
        ProfileMother.DependencyCreator dependencyCreator =
            new ProfileMother.DependencyCreator() {
            public void create(Object profileFromWhichDepends,
                    Object dependentProfile) {
                Object theClass = Model.getCoreFactory().buildClass("DasClazz",
                    dependentProfile);
                Collection stereotypes = getFacade().getStereotypes(
                    profileFromWhichDepends);
                assert stereotypes.size() >= 1: "";
                Object stereotype = stereotypes.iterator().next();
                Model.getCoreHelper().addStereotype(theClass, stereotype);
            }
        };
        String dependentProfileFilenamePrefix = "dependentProfile";
        final File dependentFile = createXmiDependentProfile(
            baseFile, dependencyCreator, profilesDir,
            dependentProfileFilenamePrefix);
        return new ArrayList<File>() { { add(baseFile); add(dependentFile); }
        };
    }
}
