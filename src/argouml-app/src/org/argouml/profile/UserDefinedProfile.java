/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    thn
 *    euluis
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

package org.argouml.profile;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Decision;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.Translator;
import org.argouml.kernel.ProfileConfiguration;
import org.argouml.model.Model;
import org.argouml.profile.internal.ocl.CrOCL;
import org.argouml.profile.internal.ocl.InvalidOclException;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * Represents a profile defined by the user
 * 
 * @author maurelio1234
 */
public class UserDefinedProfile extends Profile {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger
            .getLogger(UserDefinedProfile.class);

    private String displayName;

    private File modelFile;

    private Collection profilePackages;

    private UserDefinedFigNodeStrategy figNodeStrategy 
        = new UserDefinedFigNodeStrategy();

    private class UserDefinedFigNodeStrategy implements FigNodeStrategy {

        private Map<String, Image> images = new HashMap<String, Image>();

        public Image getIconForStereotype(Object stereotype) {
            return images.get(Model.getFacade().getName(stereotype));
        }

        /**
         * Adds a new descriptor to this strategy
         * 
         * @param fnd
         */
        public void addDesrciptor(FigNodeDescriptor fnd) {
            images.put(fnd.stereotype, fnd.img);
        }
    }

    private class FigNodeDescriptor {
        private String stereotype;

        private Image img;

        private String src;

        private int length;

        /**
         * @return if this descriptor ir valid
         */
        public boolean isValid() {
            return stereotype != null && src != null && length > 0;
        }
    }

    /**
     * The default constructor for this class
     * 
     * @param file the file from where the model should be read
     * @param manager the profile manager which will be used to resolve
     *        dependencies 
     * @throws ProfileException if the profile could not be loaded
     */
    public UserDefinedProfile(File file, ProfileManager manager)
            throws ProfileException {
        LOG.info("load " + file);
        displayName = file.getName();
        modelFile = file;
        ProfileReference reference = null;
        try {
            reference = new UserProfileReference(file.getPath());
        } catch (MalformedURLException e) {
            throw new ProfileException(
                    "Failed to create the ProfileReference.", e);
        }
        profilePackages = new FileModelLoader().loadModel(reference);

        finishLoading(manager);
    }

    /**
     * The default constructor for this class
     * 
     * @param file the file from where the model should be read
     * @throws ProfileException if the profile could not be loaded
     * @deprecated for 0.30 by euluis. Use
     * {@link UserDefinedProfile#UserDefinedProfile(File, ProfileManager)}
     * instead.
     */
    @Deprecated
    public UserDefinedProfile(File file) throws ProfileException {
        this(file, getSomeProfileManager());
    }
    
    private static class NullProfileManager implements ProfileManager {

        public void addSearchPathDirectory(String path) {
        }

        public void addToDefaultProfiles(Profile profile) {
        }

        public void applyConfiguration(ProfileConfiguration pc) {
        }

        public List<Profile> getDefaultProfiles() {
            return new ArrayList<Profile>();
        }

        public Profile getProfileForClass(String className) {
            return null;
        }

        public List<Profile> getRegisteredProfiles() {
            return new ArrayList<Profile>();
        }

        public List<String> getSearchPathDirectories() {
            return new ArrayList<String>();
        }

        public Profile getUMLProfile() {
            return null;
        }

        public Profile lookForRegisteredProfile(String profile) {
            return null;
        }

        public void refreshRegisteredProfiles() {
        }

        public void registerProfile(Profile profile) {
        }

        public void removeFromDefaultProfiles(Profile profile) {
        }

        public void removeProfile(Profile profile) {
        }

        public void removeSearchPathDirectory(String path) {
        }
        
    }

    /**
     * A constructor that reads a file from an URL
     * 
     * @param url the URL
     * @param manager the profile manager which will be used to resolve
     *        dependencies
     * @throws ProfileException if the profile can't be read or is not valid
     */
    public UserDefinedProfile(URL url, ProfileManager manager)
            throws ProfileException {
        LOG.info("load " + url);
        ProfileReference reference = null;
        reference = new UserProfileReference(url.getPath(), url);
        profilePackages = new URLModelLoader().loadModel(reference);
        finishLoading(manager);
    }

    /**
     * A constructor that reads a file from an URL
     * 
     * @param url the URL
     * @throws ProfileException if the profile could not be loaded
     * @deprecated for 0.30 by euluis. Use
     * {@link UserDefinedProfile#UserDefinedProfile(URL, ProfileManager)}
     * instead.
     */
    @Deprecated
    public UserDefinedProfile(URL url) throws ProfileException {
        this(url, getSomeProfileManager());
    }

    /**
     * A constructor that reads a file from an URL associated with some profiles
     * 
     * @param dn the display name of the profile
     * @param url the URL of the profile mode
     * @param critics the Critics defined by this profile
     * @param dependencies the dependencies of this profile
     * @param manager the profile manager which will be used to resolve
     *        dependencies
     * @throws ProfileException if the model cannot be loaded
     */
    public UserDefinedProfile(String dn, URL url, Set<Critic> critics,
            Set<String> dependencies, ProfileManager manager)
            throws ProfileException {
        LOG.info("load " + url);

        this.displayName = dn;
        if (url != null) {
            ProfileReference reference = null;
            reference = new UserProfileReference(url.getPath(), url);
            profilePackages = new URLModelLoader().loadModel(reference);
        } else {
            profilePackages = new ArrayList(0);
        }

        this.setCritics(critics);

        for (String profileID : dependencies) {
            addProfileDependency(profileID);
        }

        finishLoading(manager);
    }

    /**
     * A constructor that reads a file from an URL associated with some profiles
     * 
     * @param dn the display name of the profile
     * @param url the URL of the profile mode
     * @param critics the Critics defined by this profile
     * @param dependencies the dependencies of this profile
     * @throws ProfileException if the model cannot be loaded
     * 
     * @deprecated for 0.30 by euluis. Use
     * {@link UserDefinedProfile#UserDefinedProfile(String, URL, Set, Set, ProfileManager)}
     * instead.
     */
    @Deprecated
    public UserDefinedProfile(String dn, URL url, Set<Critic> critics,
            Set<String> dependencies) throws ProfileException {
        this(dn, url, critics, dependencies, getSomeProfileManager());
    }

    private static ProfileManager getSomeProfileManager() {
        if (ProfileFacade.isInitiated()) {
            return ProfileFacade.getManager();
        }
        return new NullProfileManager();
    }

    /**
     * Reads the informations defined as TaggedValues
     * @param manager the profile manager which will be used to resolve
     *        dependencies
     */
    private void finishLoading(ProfileManager manager) {
        Collection packagesInProfile = filterPackages();
        
        for (Object obj : packagesInProfile) {
            // if there is only one package in the model, we should suppose it's
            // the profile model, if there is more than one, we take the ones
            // marked as <<profile>>
            if (Model.getFacade().isAModelElement(obj)
                    && (Model.getFacade().isAProfile(obj)
                            || (packagesInProfile.size() == 1))) {

                // load profile name
                String name = Model.getFacade().getName(obj);
                if (name != null) {
                    displayName = name;
                } else {
                    if (displayName == null) {
                        displayName = Translator
                                .localize("misc.profile.unnamed");
                    }
                }
                LOG.info("profile " + displayName);

                if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
                    // load profile dependencies
                    String dependencyListStr = Model.getFacade()
                            .getTaggedValueValue(obj, "Dependency");
                    StringTokenizer st = new StringTokenizer(dependencyListStr,
                            " ,;:");

                    String dependencyName = null;

                    while (st.hasMoreTokens()) {
                        dependencyName = st.nextToken();
                        if (dependencyName != null) {
                            LOG.debug("Adding dependency " + dependencyName);
                            Profile profile =
                                manager.lookForRegisteredProfile(
                                    dependencyName);
                            if (profile != null) {
                                addProfileDependency(profile);
                            } else {
                                LOG.warn("The profile \"" + displayName
                                    + "\" has a dependency named \""
                                    + dependencyName
                                    + "\" which isn't solvable.");
                            }
                        }
                    }
                }
                // TODO: profile dependencies for UML2
            }
        }

        // load fig nodes
        Collection allStereotypes = Model.getExtensionMechanismsHelper()
                .getStereotypes(packagesInProfile);
        for (Object stereotype : allStereotypes) {
            Collection tags = Model.getFacade().getTaggedValuesCollection(
                    stereotype);

            for (Object tag : tags) {
                String tagName = Model.getFacade().getTag(tag);
                if (tagName == null) {
                    LOG.debug("profile package with stereotype "
                            + Model.getFacade().getName(stereotype)
                            + " contains a null tag definition");
                } else if (tagName.toLowerCase().equals("figure")) {
                    LOG.debug("AddFigNode "
                            + Model.getFacade().getName(stereotype));

                    String value = Model.getFacade().getValueOfTag(tag);
                    File f = new File(value);
                    FigNodeDescriptor fnd = null;
                    try {
                        fnd = loadImage(Model.getFacade().getName(stereotype)
                                .toString(), f);
                        figNodeStrategy.addDesrciptor(fnd);
                    } catch (IOException e) {
                        LOG.error("Error loading FigNode", e);
                    }
                }
            }
        }

        // load critiques
        Set<Critic> myCritics = this.getCritics();         
        myCritics.addAll(getAllCritiquesInModel());
        this.setCritics(myCritics);
    }

    /**
     * @return the packages in the <code>profilePackages</code>
     */
    private Collection filterPackages() {
        Collection ret = new ArrayList();
        for (Object object : profilePackages) {
            if (Model.getFacade().isAPackage(object)) {
                if (Model.getFacade().isAProfile(object)) {
                    object = Model.getExtensionMechanismsHelper()
                        .makeProfileApplicable(object);
                }
                ret.add(object);
            }
        }
        return ret;
    }

    private CrOCL generateCriticFromComment(Object critique) {
        String ocl = "" + Model.getFacade().getBody(critique);
        String headline = null;
        String description = null;
        int priority = ToDoItem.HIGH_PRIORITY;
        List<Decision> supportedDecisions = new ArrayList<Decision>();
        List<String> knowledgeTypes = new ArrayList<String>();
        String moreInfoURL = null;

        Collection tags = Model.getFacade().getTaggedValuesCollection(critique);
        boolean i18nFound = false;
        
        for (Object tag : tags) {
            if (Model.getFacade().getTag(tag).toLowerCase().equals("i18n")) {
                i18nFound = true;
                String i18nSource = Model.getFacade().getValueOfTag(tag);
                headline = Translator.localize(i18nSource + "-head");
                description = Translator.localize(i18nSource + "-desc");
                moreInfoURL = Translator.localize(i18nSource + "-moreInfoURL");
            } else if (!i18nFound
                    && Model.getFacade().getTag(tag).toLowerCase().equals(
                            "headline")) {
                headline = Model.getFacade().getValueOfTag(tag);
            } else if (!i18nFound
                    && Model.getFacade().getTag(tag).toLowerCase().equals(
                            "description")) {
                description = Model.getFacade().getValueOfTag(tag);
            } else if (Model.getFacade().getTag(tag).toLowerCase().equals(
                    "priority")) {
                priority = str2Priority(Model.getFacade().getValueOfTag(tag));
            } else if (Model.getFacade().getTag(tag).toLowerCase().equals(
                    "supporteddecision")) {
                String decStr = Model.getFacade().getValueOfTag(tag);

                StringTokenizer st = new StringTokenizer(decStr, ",;:");

                while (st.hasMoreTokens()) {
                    Decision decision = str2Decision(st.nextToken().trim()
                            .toLowerCase());
                    
                    if (decision != null) {
                        supportedDecisions.add(decision);
                    }
                }
            } else if (Model.getFacade().getTag(tag).toLowerCase().equals(
                    "knowledgetype")) {
                String ktStr = Model.getFacade().getValueOfTag(tag);

                StringTokenizer st = new StringTokenizer(ktStr, ",;:");

                while (st.hasMoreTokens()) {
                    String knowledge = str2KnowledgeType(st.nextToken().trim()
                            .toLowerCase());
                    
                    if (knowledge != null) {
                        knowledgeTypes.add(knowledge);
                    }
                }
            } else if (!i18nFound
                    && Model.getFacade().getTag(tag).toLowerCase().equals(
                            "moreinfourl")) {
                moreInfoURL = Model.getFacade().getValueOfTag(tag);
            }

        }

        LOG.debug("OCL-Critic: " + ocl);

        try {
            return new CrOCL(ocl, headline, description, priority,
                    supportedDecisions, knowledgeTypes, moreInfoURL);
        } catch (InvalidOclException e) {
            LOG.error("Invalid OCL in XMI!", e);

            return null;
        }

    }

    private String str2KnowledgeType(String token) {
        String knowledge = null;
        
        if (token.equals("completeness")) {
            knowledge = Critic.KT_COMPLETENESS;
        }
        if (token.equals("consistency")) {
            knowledge = Critic.KT_CONSISTENCY;
        }
        if (token.equals("correctness")) {
            knowledge = Critic.KT_CORRECTNESS;
        }
        if (token.equals("designers")) {
            knowledge = Critic.KT_DESIGNERS;
        }
        if (token.equals("experiencial")) {
            knowledge = Critic.KT_EXPERIENCIAL;
        }
        if (token.equals("optimization")) {
            knowledge = Critic.KT_OPTIMIZATION;
        }
        if (token.equals("organizational")) {
            knowledge = Critic.KT_ORGANIZATIONAL;
        }
        if (token.equals("presentation")) {
            knowledge = Critic.KT_PRESENTATION;
        }
        if (token.equals("semantics")) {
            knowledge = Critic.KT_SEMANTICS;
        }
        if (token.equals("syntax")) {
            knowledge = Critic.KT_SYNTAX;
        }
        if (token.equals("tool")) {
            knowledge = Critic.KT_TOOL;
        }
        return knowledge;
    }

    private int str2Priority(String prioStr) {
        int prio = ToDoItem.MED_PRIORITY;
        
        if (prioStr.toLowerCase().equals("high")) {
            prio = ToDoItem.HIGH_PRIORITY;
        } else if (prioStr.toLowerCase().equals("med")) {
            prio = ToDoItem.MED_PRIORITY;
        } else if (prioStr.toLowerCase().equals("low")) {
            prio = ToDoItem.LOW_PRIORITY;
        } else if (prioStr.toLowerCase().equals("interruptive")) {
            prio = ToDoItem.INTERRUPTIVE_PRIORITY;
        }
        return prio;
    }

    private Decision str2Decision(String token) {
        Decision decision = null;
        
        if (token.equals("behavior")) {
            decision = UMLDecision.BEHAVIOR;
        }
        if (token.equals("containment")) {                        
            decision = UMLDecision.CONTAINMENT;
        }
        if (token.equals("classselection")) {
            decision = UMLDecision.CLASS_SELECTION;
        }
        if (token.equals("codegen")) {
            decision = UMLDecision.CODE_GEN;
        }
        if (token.equals("expectedusage")) {
            decision = UMLDecision.EXPECTED_USAGE;
        }
        if (token.equals("inheritance")) {
            decision = UMLDecision.INHERITANCE;
        }
        if (token.equals("instantiation")) {
            decision = UMLDecision.INSTANCIATION;
        }
        if (token.equals("methods")) {
            decision = UMLDecision.METHODS;
        }
        if (token.equals("modularity")) {
            decision = UMLDecision.MODULARITY;
        }
        if (token.equals("naming")) {                       
            decision = UMLDecision.NAMING;
        }
        if (token.equals("patterns")) {
            decision = UMLDecision.PATTERNS;
        }
        if (token.equals("plannedextensions")) {
            decision = UMLDecision.PLANNED_EXTENSIONS;
        }
        if (token.equals("relationships")) {
            decision = UMLDecision.RELATIONSHIPS;
        }
        if (token.equals("statemachines")) {
            decision = UMLDecision.STATE_MACHINES;
        }
        if (token.equals("stereotypes")) {
            decision = UMLDecision.STEREOTYPES;
        }
        if (token.equals("storage")) {
            decision = UMLDecision.STORAGE;
        }
        return decision;
    }

    private List<CrOCL> getAllCritiquesInModel() {
        List<CrOCL> ret = new ArrayList<CrOCL>();

        Collection<Object> comments = getAllCommentsInModel(profilePackages);

        for (Object comment : comments) {
            if (Model.getExtensionMechanismsHelper().hasStereotype(comment,
                    "Critic")) {
                CrOCL cr = generateCriticFromComment(comment);

                if (cr != null) {
                    ret.add(cr);
                }
            }
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    private Collection<Object> getAllCommentsInModel(Collection objs) {
        Collection<Object> col = new ArrayList<Object>();
        for (Object obj : objs) {
            if (Model.getFacade().isAComment(obj)) {
                col.add(obj);
            } else if (Model.getFacade().isANamespace(obj)) {
                Collection contents = Model
                        .getModelManagementHelper().getAllContents(obj);
                if (contents != null) {
                    col.addAll(contents);
                }
            }
        }
        return col;
    }

    /**
     * @return the string that should represent this profile in the GUI.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns null. This profile has no formatting strategy.
     * 
     * @return null.
     */
    @Override
    public FormatingStrategy getFormatingStrategy() {
        return null;
    }

    /**
     * Returns null. This profile has no figure strategy.
     * 
     * @return null.
     */
    @Override
    public FigNodeStrategy getFigureStrategy() {
        return figNodeStrategy;
    }

    /**
     * @return the file passed at the constructor
     */
    public File getModelFile() {
        return modelFile;
    }

    /**
     * @return the name of the model and the file name
     */
    @Override
    public String toString() {
        File str = getModelFile();
        return super.toString() + (str != null ? " [" + str + "]" : "");
    }

    @Override
    public Collection getProfilePackages() {
        return profilePackages;
    }

    private FigNodeDescriptor loadImage(String stereotype, File f)
        throws IOException {
        FigNodeDescriptor descriptor = new FigNodeDescriptor();
        descriptor.length = (int) f.length();
        descriptor.src = f.getPath();
        descriptor.stereotype = stereotype;

        BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(f));

        byte[] buf = new byte[descriptor.length];
        try {
            bis.read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        descriptor.img = new ImageIcon(buf).getImage();

        return descriptor;
    }
}
