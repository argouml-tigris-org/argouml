// $Id$
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
import java.util.Iterator;
import java.util.Vector;

import org.argouml.kernel.AbstractProjectMember;
import org.argouml.kernel.Project;
/**
 *   This class captures represents the unique access point for the 
 *   configurability allowed by the use of profiles. 
 *
 *   @author maurelio1234
 */
public class ProfileConfiguration extends AbstractProjectMember {

    private FormatingStrategy formatingStrategy;
    private Vector	      figNodeStrategies = new Vector();
    
    private Profile defaultProfile; 
    private Vector profiles = new Vector();
    private Vector profileModels = new Vector();
    
    /**
     * The extension used in serialization and returned by {@link #getType()}
     */
    public static final String EXTENSION = "profile";
    
    /**
     * Logger.
     */
//    private static final Logger LOG = 
//    		Logger.getLogger(ProfileConfiguration.class);
    
    /**
     * The default constructor for this class. Sets the Java profile as the 
     * default one and its formating strategy as the default one.
     * 
     * @param project the project that contains this configuration
     */
    public ProfileConfiguration(Project project) {
	super(EXTENSION, project);
	
	defaultProfile = ProfileUML.getInstance();
	
	Profile cppProfile = ProfileCpp.getInstance();
	Profile javaProfile = ProfileJava.getInstance();
	
	ProfileManagerImpl.getInstance().registerProfile(cppProfile);
	ProfileManagerImpl.getInstance().registerProfile(javaProfile);	

	addProfile(defaultProfile);
	addProfile(javaProfile);
	activateFormatingStrategy(defaultProfile);
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
	if (profile.getFormatingStrategy() != null
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
     * @return the default profile
     */
    public Profile getDefaultProfile() {
        return defaultProfile;
    }
    
    /**
     * Applies a new profile to this configuration
     * 
     * @param p the profile to be applied
     */
    public void addProfile(Profile p) {
        if (!profiles.contains(p)) {
            profiles.add(p);
            profileModels.add(p.getModel());

            FigNodeStrategy fns = p.getFigureStrategy();
            if (fns != null) {
                figNodeStrategies.add(fns);
            }
        }
    }
    
    /**
     * Sets the default profile.
     * 
     * @param profile the default profile to be set
     */
    public void setDefaultProfile(Profile profile) {
        this.defaultProfile = profile;
    }
    
    /**
     * @return the list of models of the currently applied profile.
     */
    public Vector getProfileModels() {
        return profileModels;
    }

    /**
     * Removes the passed profile from the configuration. The default cannot be 
     * removed. Use {@link #setDefaultProfile(Profile)} instead.
     * 
     * @param p the profile to be removed
     */
    public void removeProfile(Profile p) {
	if (p != defaultProfile) {
	    profiles.remove(p);
	    profileModels.remove(p.getModel());

	    FigNodeStrategy fns = p.getFigureStrategy();
	    if (fns != null) {
		figNodeStrategies.remove(fns);
	    }

	}
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

}
