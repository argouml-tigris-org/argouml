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

package org.argouml.profile;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.critics.CrProfile;

/**
 * Represents a profile defined by the user
 * 
 * @author maurelio1234
 */
public class UserDefinedProfile extends Profile {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(UserDefinedProfile.class);

    private String displayName;

    private File modelFile;

    private Collection model;

    private boolean fromZargo;

    private UserDefinedFigNodeStrategy figNodeStrategy 
                                    = new UserDefinedFigNodeStrategy();

    
    private class UserDefinedFigNodeStrategy implements FigNodeStrategy {

        private HashMap<String, Image> images = new HashMap<String, Image>();

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
        String stereotype;

        Image img;

        String src;

        int length;

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
     * @throws ProfileException if the profile could not be loaded
     */
    public UserDefinedProfile(File file) throws ProfileException {
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
        model = new FileModelLoader().loadModel(reference);
        fromZargo = false;
        finishLoading();
    }

    /**
     * A constructor that takes a file name and a reader, being the reader the
     * input method to get the profile model.
     * 
     * @param fileName name of the profile model file.
     * @param reader a reader opened from where the profile model will be
     *            loaded.
     * @throws ProfileException if something goes wrong in initializing the
     *             profile.
     */
    public UserDefinedProfile(String fileName, Reader reader)
        throws ProfileException {
        LOG.info("load " + fileName);
        displayName = fileName;
        ProfileReference reference = null;
        try {
            reference = new UserProfileReference(fileName);
        } catch (MalformedURLException e) {
            throw new ProfileException(
                    "Failed to create the ProfileReference.", e);
        }
        model = new ReaderModelLoader(reader).loadModel(reference);
        fromZargo = true;
        finishLoading();
    }

    /**
     * A constructor that reads a file from an URL
     * 
     * @param url the URL
     * @throws ProfileException
     */
    public UserDefinedProfile(URL url) throws ProfileException {
        LOG.info("load " + url);
        
        ProfileReference reference = null;
        reference = new UserProfileReference(url.getPath(), url);
        model = new URLModelLoader().loadModel(reference);
        fromZargo = false;

        finishLoading();
    }
    
    /**
     * A constructor that reads a file from an URL 
     * associated with some profiles
     * 
     * @param url the URL
     * @param critics the Critics defined by this profile
     * @throws ProfileException
     */
    public UserDefinedProfile(URL url, Set<CrProfile> critics) throws ProfileException {
        LOG.info("load " + url);
        
        ProfileReference reference = null;
        reference = new UserProfileReference(url.getPath(), url);
        model = new URLModelLoader().loadModel(reference);
        fromZargo = false;
        this.critics = critics;
        
        finishLoading();
    }
    

    /**
     * Reads the informations defined as TaggedValues
     */
    private void finishLoading() {

        for (Object obj : model) {            
            if (Model.getExtensionMechanismsHelper().hasStereotype(obj,
                    "profile")) {


                // load profile name
                String name = Model.getFacade().getName(obj);
                if (name != null) {
                    displayName = name;
                } else {
                    displayName = Translator.localize("misc.profile.unnamed");
                }
                LOG.info("profile " + displayName);

                // load profile dependencies
                String dependencyListStr = Model.getFacade().getTaggedValueValue(obj,
                        "Dependency");
                StringTokenizer st = new StringTokenizer(dependencyListStr, " ,;:");

                String profile = null;

                do {
                    profile = st.nextToken();
                    if (profile != null) {
                      LOG.debug("AddingDependency " + profile);
                      this.
                        addProfileDependency(lookForRegisteredProfile(profile));
                    }
                } while (st.hasMoreTokens());

            }
        }

        // load fig nodes
        Collection allStereotypes = Model.getExtensionMechanismsHelper().getStereotypes(
                model);
        for (Object stereotype : allStereotypes) {
            Collection tags = Model.getFacade().getTaggedValuesCollection(stereotype);

            for (Object tag : tags) {
                if (Model.getFacade().getTag(tag).toLowerCase()
                        .equals("figure")) {
                    LOG.debug("AddFigNode " + Model.getFacade().getName(stereotype));
                    
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
        Collection allCritiques = getAllCritiques();
        
        for (Object critique : allCritiques) {
            CrProfile c = generateCriticFromModel(critique);
            if (c!=null) {
                this.critics.add(c);
            }
        }
    }

    private CrProfile generateCriticFromModel(Object critique) {
//        String ocl = Model.getDataTypesHelper().getBody(critique);
                
        return null;
    }

    @SuppressWarnings("unchecked")
    private Collection getAllCritiques() {
        Collection ret = new Vector();

        for (Object obj : model) {
            Collection comments = Model.getModelManagementHelper()
                    .getAllModelElementsOfKindWithModel(obj,
                            Model.getMetaTypes().getComment());

            for (Object comment : comments) {
                if (Model.getExtensionMechanismsHelper().hasStereotype(comment,
                        "Critic")) {
                    ret.add(comment);
                }
            }
            
        }
        return ret;
    }

    private Profile lookForRegisteredProfile(String value) {
        ProfileManager manager = ProfileFacade.getManager();
        List<Profile> registeredProfiles = manager.getRegisteredProfiles();

        for (Profile profile : registeredProfiles) {
            if (profile.getDisplayName().equalsIgnoreCase(value)) {
                return profile;
            }
        }
        return null;
    }

    /**
     * @return the string that should represent this profile in the GUI. An
     *         start (*) is placed on it if it comes from the currently opened
     *         zargo file.
     */
    public String getDisplayName() {
        return displayName + (fromZargo ? "*" : "");
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
        return super.toString() + " [" + getModelFile() + "]";
    }

    @Override
    public Collection getProfilePackages() {
        return model;
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
