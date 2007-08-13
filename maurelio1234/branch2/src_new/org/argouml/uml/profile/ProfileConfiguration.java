// $Id: ProfileConfiguration.java 13298 2007-08-12 19:40:57Z maurelio1234 $
// Copyright (c) 1996-2006 The Regents of the University of California. All
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


package org.argouml.uml.profile;

import java.awt.Image;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.kernel.AbstractProjectMember;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.ui.explorer.ActionExportProfileXMI;
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
    private Vector	      figNodeStrategies = new Vector();
    
    private Vector<Profile> profiles = new Vector<Profile>();
    private Vector<Object> profileModels = new Vector<Object>();
    
    /**
     * The extension used in serialization and returned by {@link #getType()}
     */
    public static final String EXTENSION = "profile";
        
    /**
     * The default constructor for this class. Sets the Java profile as the 
     * default one and its formating strategy as the default one.
     * 
     * @param project the project that contains this configuration
     */
    public ProfileConfiguration(Project project) {
	super(EXTENSION, project);
	
        Iterator it = ProfileManagerImpl.getInstance().getDefaultProfiles().iterator();
        
        while (it.hasNext()) {
            Profile p = (Profile) it.next();
            addProfile(p);
            activateFormatingStrategy(p);                
        }
	
    }
    
    /**
     * @return the current formating strategy
     */
    public FormatingStrategy getFormatingStrategy() {
        return formatingStrategy;
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
    public Vector getProfiles() {
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
            }
            
            FigNodeStrategy fns = p.getFigureStrategy();
            if (fns != null) {
                figNodeStrategies.add(fns);
            }

            for (Profile dependency : p.getDependencies()) {
                addProfile(dependency);
            }
            
            ExplorerEventAdaptor.getInstance().structureChanged();
        }
    }
        
    /**
     * @return the list of models of the currently applied profile.
     * @deprecated by maurelio1234
     */
    @SuppressWarnings("dep-ann")
    @Deprecated
    public Vector getProfileModels() {
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

        Vector<Profile> markForRemoval = new Vector<Profile>();
        for (Profile profile : profiles) {
            if (profile.getDependencies().contains(p)) {
                markForRemoval.add(profile);
            }
        }

        for (Profile profile : markForRemoval) {
            removeProfile(profile);
        }

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
    public String toString() {
        return "Profile Configuration";
    }

    /**
     * @param string
     * @param obj 
     * @return
     */
    @SuppressWarnings("deprecation")
    public Object findStereotypeForObject(String string, Object obj) {
        Iterator iter = null;
        
        for (Object model : profileModels) {
            iter = Model.getFacade().getOwnedElements(model).iterator();

            while (iter.hasNext()) {
                Object stereo = iter.next();
                if (!Model.getFacade().isAStereotype(stereo)
                        || !string.equals(Model.getFacade().getName(stereo))) {
                    continue;
                }

                if (Model.getExtensionMechanismsHelper().isValidStereoType(obj,
                        stereo)) {
                    return Model.getModelManagementHelper()
                            .getCorrespondingElement(stereo,
                                    Model.getFacade().getModel(obj));
                }
            }            
        }

        return null;
    }

    /**
     * @param name
     * @return
     */
    public Object findType(String name) {
        Object result = null;
        Iterator it = getProfileModels().iterator();
        while (result == null && it.hasNext()) {
            result = findTypeInModel(name, it.next());
        }
        
        return result;
    }

    private Object findTypeInModel(String s, Object model) {

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

    /**
     * @param metaType
     * @return
     */
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
     * @return
     */
    public Collection findAllStereotypesForModelElement(Object modelElement) {
        return Model.getExtensionMechanismsHelper().getAllPossibleStereotypes(
                getProfileModels(), modelElement);
    }

    public Object getDefaultAttributeType() {
        return findType("int");
    }

    public Object getDefaultParameterType() {
        return findType("int");
    }

    public Object getDefaultReturnType() {
        return findType("void");
    }
}
