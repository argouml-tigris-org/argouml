// $Id$
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.argouml.model.Model;


/**
 * Abstract class representing a Profile.  It contains default types and 
 * presentation characteristics that can be tailored to various modeling
 * environments.
 * 
 * @author Marcos Aurélio
 */
public abstract class Profile {
    
    private Set<Profile> importedProfiles  = new HashSet<Profile>();
    private Set<Profile> importingProfiles = new HashSet<Profile>();
        
    /**
     * Add a dependency on the given profile from this profile.
     * 
     * @param p
     *                the profile
     * @throws IllegalArgumentException
     *                 if there is some cycle on the dependency graph
     */
    protected final void addProfileDependency(Profile p)
        throws IllegalArgumentException {
        
        if (importingProfiles.contains(p)) {
            throw new IllegalArgumentException("This profile causes a cycle "
                    + "in the profile dependency graph!");
        } else {
            importedProfiles.add(p);
            importedProfiles.addAll(p.importedProfiles);

            for (Profile importedProfile : importedProfiles) {
                importedProfile.importingProfiles.add(this);
            }
        }
    }    

    /**
     * @return the dependencies
     */
    public final Set<Profile> getDependencies() {
        return importedProfiles;
    }
    
    /**
     * @return the name for this profile 
     */
    public abstract String getDisplayName();
    
    /**
     * @return the UML Model that contains the profile model
     * @deprecated by maurelio1234. Use {@link #getProfilePackages()} instead.
     */
    @Deprecated
    public Object getModel() {
        Collection c = null;
        try {
            c = getProfilePackages();
        } catch (ProfileException e) {
        }
        
        if (c != null) {
            return c.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * @return the formating strategy offered by this profile, if any. Returns
     *         <code>null</code> if this profile has no formating strategy.
     */
    public FormatingStrategy getFormatingStrategy() {
        return null;
    }

    /**
     * @return the FigNodeStrategy offered by this profile, if any. Returns
     *         <code>null</code> if this profile has no FigNodeStrategy.
     */
    public FigNodeStrategy getFigureStrategy() {
        return null;
    }

    /**
     * @return the DefaultTypeStrategy offered by this profile, if any. Returns
     *         <code>null</code> if this profile has no DefaultTypeStrategy.
     */
    public DefaultTypeStrategy getDefaultTypeStrategy() {
        return null;
    }
    
    /**
     * This method produces a string that represents the specific model element
     * in the context of the specified namespace.
     * 
     * @param element
     *                element to represent.
     * @param namespace
     *                context namespace (may be null).
     * @return a string representing the model element
     * 
     * @deprecated for 0.25.4 by maurelio1234. Use
     *             {@link #getFormatingStrategy()} instead.
     */
    @Deprecated
    public String formatElement(Object element,
                                         Object namespace) {
        FormatingStrategy fs = getFormatingStrategy(); 
        if (fs != null) {
            return fs.formatElement(element, namespace);
        } else {
            return null;
        }
    }
    
    /**
     * This method produces a string the represents the collection of model
     * elements in the context of the specified namespace.
     * 
     * @param iter
     *                iterator over collection
     * @param namespace
     *                context namespace (may be null).
     * @return a string representing the collection
     * 
     * @deprecated for 0.25.4 by maurelio1234. Use
     *             {@link #getFormatingStrategy()} instead.
     */
    @Deprecated
    public String formatCollection(Iterator iter,
                                            Object namespace) {
        FormatingStrategy fs = getFormatingStrategy(); 
        if (fs != null) {
            return fs.formatCollection(iter, namespace);
        } else {
            return null;
        }        
    }
    /**
     * @return the UML Model that contains the profile model
     * @throws ProfileException if failed to get profile.
     * @deprecated for 0.25.4 by tfmorris.  Use {@link #getProfilePackages()}.
     */
    @Deprecated
    public Object getProfileModel() throws ProfileException {
        if (getModel() == null) {
            Object profileModel = null;
            for (Object pkg : getProfilePackages()) {
                if (Model.getFacade().isAPackage(pkg)) {
                    profileModel = pkg;
                    return profileModel;
                }
            }
            profileModel = getProfilePackages().iterator().next();

            return profileModel;
        } else {
            return getModel();
        }
    }
    
    /**
     * @return a collection of the top level UML Packages containing the
     *         profile.
     * @throws ProfileException
     *                 if failed to get profile.
     */
    public abstract Collection getProfilePackages() throws ProfileException;
    
    /**
     * Set the filename to load the profile model from.  This will be
     * remembered and used as the file for all future loads.
     * @param filename file name of XMI file containing model to use as profile
     * 
     * @throws ProfileException if the given file isn't a valid profile
     * @deprecated by maurelio1234
     */
    @SuppressWarnings("unused")
    @Deprecated
    public void setProfileModelFilename(String filename)
        throws ProfileException {
        
    }
    
    /**
     * Return the filename that was or will be used to load the profile/default
     * model.
     * @return the filename
     * @deprecated by maurelio1234
     */
    @Deprecated
    public String getProfileModelFilename() {
        return "";
    }
    
    /**
     * @return the display name
     */
    @Override
    public String toString() {
        return getDisplayName();
    }
}
