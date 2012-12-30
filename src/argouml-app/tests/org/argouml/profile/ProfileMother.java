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

import static org.argouml.model.Model.getCoreHelper;
import static org.argouml.model.Model.getExtensionMechanismsFactory;
import static org.argouml.model.Model.getExtensionMechanismsHelper;
import static org.argouml.model.Model.getFacade;
import static org.argouml.model.Model.getModelManagementFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.argouml.FileHelper;
import org.argouml.model.InvalidElementException;
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

    private static final Logger LOG =
        Logger.getLogger(ProfileMother.class.getName());

    /**
     * "profile".
     */
    public static final String STEREOTYPE_NAME_PROFILE = "profile";
    /**
     * "st" the example stereotype name.
     */
    public static final String STEREOTYPE_NAME_ST = "st";

    /**
     * "SimpleProfile" is the name of the SimpleProfile which ProfileMother
     * uses for the most basic profile being used.
     */
    public static final String DEFAULT_SIMPLE_PROFILE_NAME = "SimpleProfile";

    /**
     * ".xmi" is the default profile file extension.
     */
    public static final String XMI_FILE_EXTENSION = ".xmi";

    /**
     * "TagDef" is the name of the Tag Definition applicable to model elements
     * to which the stereotype named {@link ProfileMother#STEREOTYPE_NAME_ST}
     * of SimpleProfile was applied.
     */
    public static final String TAG_DEFINITION_NAME_TD = "TagDef";

    /**
     * Create a simple profile model with name
     * {@link ProfileMother#DEFAULT_SIMPLE_PROFILE_NAME}
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
        // TODO: should it remove the leftovers from other tests?
//        cleanAllExtents();
//        assert getFacade().getRootElements().size() == 0;
        Object model = getModelManagementFactory().createProfile();
        Object fooClass = Model.getCoreFactory().buildClass("foo", model);
        Object stereotype =
            getExtensionMechanismsFactory().buildStereotype(
                    fooClass,
                    STEREOTYPE_NAME_ST,
                    model);
        getCoreHelper().setName(model, profileName);
        getExtensionMechanismsFactory().buildTagDefinition(
            TAG_DEFINITION_NAME_TD, stereotype, null);
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
                    Model.getFacade().getName(stereotype))) {
                return stereotype;
            }
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
                LOG.log(Level.SEVERE, "Exception", e);
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
//        cleanAllExtentsBut(model); // TODO: why is this causing a crash?!?
        try {
            XmiWriter xmiWriter = Model.getXmiWriter(model, fileOut, "x("
                + UmlFilePersister.PERSISTENCE_VERSION + ")");
            xmiWriter.write();
            fileOut.flush();
        } catch (Exception e) {
            String msg = "Exception while saving profile model.";
            LOG.log(Level.SEVERE, msg, e);
            throw new IOException(msg);
        } finally {
            fileOut.close();
        }
    }

    public static void cleanAllExtents() {
        cleanAllExtentsBut(null);
    }

    public static void cleanAllExtentsBut(Object extentNotToClean) {
        // remove leftovers from other tests
        Collection rootElements = Model.getFacade().getRootElements();
        for (Object rootElement : rootElements) {
            if (extentNotToClean != rootElement) {
                try {
                    Model.getUmlFactory().deleteExtent(rootElement);
                } catch (InvalidElementException e) {
                    // ignored
                }
            }
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
    File createXmiDependentProfile(File profileFromWhichDependsFile,
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
            dependentProfileFilenamePrefix, XMI_FILE_EXTENSION, profilesDir);
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
    List<File> createProfileFilePairWith2ndDependingOn1stViaXmi()
        throws IOException, UmlException {

        File profilesDir = FileHelper.createTempDirectory();
        final File baseFile = File.createTempFile(
            "baseProfile", XMI_FILE_EXTENSION, profilesDir);
        Object model = createSimpleProfileModel();
        saveProfileModel(model, baseFile);
        Model.getUmlFactory().deleteExtent(model);
        ProfileMother.DependencyCreator dependencyCreator =
            new ProfileMother.DependencyCreator() {
                    public void create(Object profileFromWhichDepends,
                            Object dependentProfile) {
                        Object theClass =
                            Model.getCoreFactory().buildClass(
                                "DasClazz",
                                dependentProfile);
                        Collection stereotypes =
                            getFacade().getStereotypes(profileFromWhichDepends);
                        assert stereotypes.size() >= 1 : "";
                        Object stereotype = stereotypes.iterator().next();
                        Model.getCoreHelper().addStereotype(
                                theClass,
                                stereotype);
                    }
                };
        String dependentProfileFilenamePrefix = "dependentProfile";
        final File dependentFile = createXmiDependentProfile(
            baseFile, dependencyCreator, profilesDir,
            dependentProfileFilenamePrefix);
        return new ArrayList<File>() { { add(baseFile); add(dependentFile); }
        };
    }

    /**
     * Creates two profiles with one depending of the other. Saves the two
     * profiles in different temporary directories and returns the associated
     * {@link File Files}, being that the second XMI file depends on the first.
     *
     * Ensures that both profiles aren't remembered by the model sub-system
     * by renaming them.
     *
     * @return A list of two files, the second File contains an XMI that
     *         depends of the first.
     * @throws IOException if file IO causes errors.
     * @throws UmlException if the manipulation of models causes errors.
     */
    public List<File> createUnloadedProfilePairWith2ndDependingOn1stViaXmi()
        throws IOException, UmlException {

        List<File> profileFiles =
            createProfileFilePairWith2ndDependingOn1stViaXmi();
        File baseProfileFile = profileFiles.get(0);
        String baseProfileFileName = baseProfileFile.getName();
        // ensure that model subsystem implementation doesn't remember the
        // profiles by changing their names and directories
        baseProfileFile = FileHelper.moveFileToNewTempDirectory(
            baseProfileFile, "new-base-profile", XMI_FILE_EXTENSION,
            ProfileMother.class.getCanonicalName());
        File dependentProfileFile = profileFiles.get(1);
        replaceStringInFile(dependentProfileFile, baseProfileFileName,
            baseProfileFile.getName());
        dependentProfileFile = FileHelper.moveFileToNewTempDirectory(
            dependentProfileFile, "new-dependent-profile", XMI_FILE_EXTENSION,
            ProfileMother.class.getCanonicalName());
        profileFiles.clear();
        profileFiles.add(baseProfileFile);
        profileFiles.add(dependentProfileFile);
        return profileFiles;
    }

    /**
     * The regular expression occurrences in file are replaced by
     * replacement {@link String}.
     * @param file the file in which to replace.
     * @param regex the regular expression to replace.
     * @param replacement the replacement {@link String}.
     * @throws IOException if IO operations throw.
     */
    public static void replaceStringInFile(File file, String regex,
            String replacement)
        throws IOException {

        StringBuffer fileContents = new StringBuffer();
        BufferedReader reader = null;
        String fileContents2 = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = "";
            while (null != (line = reader.readLine())) {
                fileContents.append(line);
                fileContents.append("\n");
            }
            fileContents2 = fileContents.toString();
            fileContents2 = fileContents2.replaceAll(regex, replacement);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        if (fileContents2 != null && file.delete() && file.createNewFile()) {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.append(fileContents2);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    /**
     * Create the SimpleProfile (see
     * {@link ProfileMother#createSimpleProfileModel()}) in a way that it isn't
     * neither loaded in the model neither remembered by it. The file name has
     * the prefix {@link ProfileMother#DEFAULT_SIMPLE_PROFILE_NAME} and the
     * extension {@link ProfileMother#XMI_FILE_EXTENSION}.
     *
     * @return the {@link File} where the profile was stored.
     * @throws IOException
     */
    public File createUnloadedSimpleProfile() throws IOException {
        return createUnloadedSimpleProfile(DEFAULT_SIMPLE_PROFILE_NAME);
    }

    /**
     * Create the SimpleProfile (see
     * {@link ProfileMother#createSimpleProfileModel()}) in a way that it isn't
     * neither loaded in the model neither remembered by it. The file name has
     * the prefix profileName and the
     * extension {@link ProfileMother#XMI_FILE_EXTENSION}.
     *
     * @param profileName the name of the profile and the prefix of the
     *        profile file name.
     * @return the {@link File} where the profile was stored.
     * @throws IOException
     */
    public File createUnloadedSimpleProfile(String profileName)
        throws IOException {

        Object model = createSimpleProfileModel(profileName);
        File profileFile = File.createTempFile(profileName,
            XMI_FILE_EXTENSION);
        saveProfileModel(model, profileFile);
        Model.getUmlFactory().deleteExtent(model);
        File newProfileFile = File.createTempFile(profileName,
            XMI_FILE_EXTENSION);
        newProfileFile.delete();
        profileFile.renameTo(newProfileFile);
        return newProfileFile;
    }
}
