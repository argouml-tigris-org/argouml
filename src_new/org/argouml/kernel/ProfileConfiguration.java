// $Id: ProfileConfiguration.java 13298 2007-08-12 19:40:57Z maurelio1234 $
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.kernel;

import java.awt.Image;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.model.Model;
import org.argouml.profile.DefaultTypeStrategy;
import org.argouml.profile.FigNodeStrategy;
import org.argouml.profile.FormatingStrategy;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileFacade;
import org.argouml.ui.explorer.ExplorerEventAdaptor;

/**
 *   This class captures represents the unique access point for the 
 *   configurability allowed by the use of profiles. 
 *
 *   @author maurelio1234
 */
public class ProfileConfiguration extends AbstractProjectMember {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger
            .getLogger(ProfileConfiguration.class);

    private FormatingStrategy formatingStrategy;

    private DefaultTypeStrategy defaultTypeStrategy;

    private List figNodeStrategies = new ArrayList();

    private List<Profile> profiles = new ArrayList<Profile>();

    private List<Object> profileModels = new ArrayList<Object>();
    
    /**
     * The extension used in serialization and returned by {@link #getType()}
     */
    public static final String EXTENSION = "profile";
    

    /**
     * The configuration key for the default stereotype view.
     */
    public static final ConfigurationKey KEY_DEFAULT_STEREOTYPE_VIEW = 
        Configuration.makeKey("profiles", "stereotypeView");
        
    /**
     * The default constructor for this class. Sets the Java profile as the 
     * default one and its formating strategy as the default one.
     * 
     * @param project the project that contains this configuration
     */
    public ProfileConfiguration(Project project) {
	super(EXTENSION, project);
	
        for (Profile p : ProfileFacade.getManager().getDefaultProfiles()) {
            addProfile(p);
        }

        updateStrategies();
    }
    
    private void updateStrategies() {
        for (Profile profile : profiles) {
            activateFormatingStrategy(profile);                
            activateDefaultTypeStrategy(profile);                
        }
    }

    /**
     * @return the current formating strategy
     */
    public FormatingStrategy getFormatingStrategy() {
        return formatingStrategy;
    }

    /**
     * @return the current default type strategy
     */
    public DefaultTypeStrategy getDefaultTypeStrategy() {
        return defaultTypeStrategy;
    }
    
    /**
     * Updates the current strategy to the strategy provided by the 
     * passed profile. The profile should have been previously registered.
     * 
     * @param profile the profile providing the current default type strategy
     */
    public void activateDefaultTypeStrategy(Profile profile) {
        if (profile != null && profile.getDefaultTypeStrategy() != null
                && getProfiles().contains(profile)) {
            this.defaultTypeStrategy = profile.getDefaultTypeStrategy();
        }
    }
    
    /**
     * Updates the current strategy to the strategy provided by the 
     * passed profile. The profile should have been previously registered.
     * 
     * @param profile the profile providing the current formating strategy
     */
    public void activateFormatingStrategy(Profile profile) {
	if (profile != null && profile.getFormatingStrategy() != null
		&& getProfiles().contains(profile)) {
	    this.formatingStrategy = profile.getFormatingStrategy();
	}
    }

    /**
     * @return the list of applied profiles
     */
    public List<Profile> getProfiles() {
        return profiles;
    }
    
    /**
     * Applies a new profile to this configuration
     * 
     * @param p the profile to be applied
     */
    @SuppressWarnings("unchecked")
    public void addProfile(Profile p) {
        if (!profiles.contains(p)) {
            profiles.add(p);
            try {
                profileModels.addAll(p.getProfilePackages());
            } catch (ProfileException e) {
                LOG.warn("Error retrieving profile's " + p + " packages.", e);
            }
            
            FigNodeStrategy fns = p.getFigureStrategy();
            if (fns != null) {
                figNodeStrategies.add(fns);
            }

            for (Profile dependency : p.getDependencies()) {
                addProfile(dependency);
            }

            updateStrategies();
            // FIXME: remove this dependency
            ExplorerEventAdaptor.getInstance().structureChanged();
        }
    }
        
    /**
     * @return the list of models of the currently applied profile.
     */
    private List getProfileModels() {
        return profileModels;
    }

    /**
     * Removes the passed profile from the configuration. 
     * 
     * @param p the profile to be removed
     */
    public void removeProfile(Profile p) {
        profiles.remove(p);
        try {
            profileModels.removeAll(p.getProfilePackages());
        } catch (ProfileException e) {
            LOG.error("Exception", e);
        }

        FigNodeStrategy fns = p.getFigureStrategy();
        if (fns != null) {
            figNodeStrategies.remove(fns);
        }

        if (formatingStrategy == p.getFormatingStrategy()) {
            formatingStrategy = null;
        }

        List<Profile> markForRemoval = new ArrayList<Profile>();
        for (Profile profile : profiles) {
            if (profile.getDependencies().contains(p)) {
                markForRemoval.add(profile);
            }
        }

        for (Profile profile : markForRemoval) {
            removeProfile(profile);
        }

        updateStrategies();        
        // FIXME: remove this dependency
        ExplorerEventAdaptor.getInstance().structureChanged();
    }
    
    private FigNodeStrategy compositeFigNodeStrategy = new FigNodeStrategy() {

	public Image getIconForStereotype(Object element) {
	    Iterator it = figNodeStrategies.iterator();

	    while (it.hasNext()) {
		FigNodeStrategy strat = (FigNodeStrategy) it.next();
		Image extra = strat.getIconForStereotype(element);

		if (extra != null) {
		    return extra;
		}
	    }
	    return null;
	}
	
    };
    
    /**
     * @return the current FigNodeStrategy
     */
    public FigNodeStrategy getFigNodeStrategy() {
	return compositeFigNodeStrategy; 
    }

    /**
     * @return the extension for this project member
     * @see org.argouml.kernel.AbstractProjectMember#getType()
     */
    public String getType() {
	return EXTENSION;
    }

    /**
     * Objects of this class are always consistent, there's no need 
     * to repair them.
     * 
     * @return the empty string.
     * @see org.argouml.kernel.ProjectMember#repair()
     */
    public String repair() {
	return "";
    }

    /**
     * @return the "Profile Configuration" string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Profile Configuration";
    }


    /**
     * Find a stereotype with the given name which is applicable to the given 
     * element.
     * 
     * @param name name of stereotype to look for
     * @param element model element to which the stereotype must be applicable
     * @return the stereotype or null if none found
     */
    public Object findStereotypeForObject(String name, Object element) {
        Iterator iter = null;
        
        for (Object model : profileModels) {
            iter = Model.getFacade().getOwnedElements(model).iterator();

            while (iter.hasNext()) {
                Object stereo = iter.next();
                if (!Model.getFacade().isAStereotype(stereo)
                        || !name.equals(Model.getFacade().getName(stereo))) {
                    continue;
                }

                if (Model.getExtensionMechanismsHelper().isValidStereotype(
                        element, stereo)) {
                    return stereo;
                }
            }
        }

        return null;
    }

    /**
     * Search for the given type in all of the profile models.
     * 
     * @param name name of type to be found
     * @return the type or null
     */
    public Object findType(String name) {
        for (Object model : getProfileModels()) {
            Object result = findTypeInModel(name, model);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Finds a type in a model by name
     * 
     * FIXME: duplicated from the method with the same name in 
     * org.argouml.profile.internal.ModelUtils.
     * 
     * @param s the type name
     * @param model the model
     * @return the type or <code>null</code> if the type has not been found.
     */
    public static Object findTypeInModel(String s, Object model) {

        if (!Model.getFacade().isANamespace(model)) {
            throw new IllegalArgumentException(
                    "Looking for the classifier " + s
                    + " in a non-namespace object of " + model
                    + ". A namespace was expected.");
        }

        Collection allClassifiers =
            Model.getModelManagementHelper()
                .getAllModelElementsOfKind(model,
                        Model.getMetaTypes().getClassifier());

        Object[] classifiers = allClassifiers.toArray();
        Object classifier = null;

        for (int i = 0; i < classifiers.length; i++) {

            classifier = classifiers[i];
            if (Model.getFacade().getName(classifier) != null
                        && Model.getFacade().getName(classifier).equals(s)) {
                return classifier;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public Collection findByMetaType(Object metaType) {
        Set elements = new HashSet();

        Iterator it = getProfileModels().iterator();
        while (it.hasNext()) {
            Object model = it.next();
            elements.addAll(Model.getModelManagementHelper()
                    .getAllModelElementsOfKind(model, metaType));
        }
        return elements;
    }

    /**
     * @param modelElement
     *                ModelElement for which find possible stereotypes
     * @return collection of stereotypes which are valid for the given model
     *         element.
     */
    public Collection findAllStereotypesForModelElement(Object modelElement) {
        return Model.getExtensionMechanismsHelper().getAllPossibleStereotypes(
                getProfileModels(), modelElement);
    }
}
