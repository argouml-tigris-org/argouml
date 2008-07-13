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

import java.util.List;

/**
 * Interface to the manager for the global set of registered profiles.
 *
 * @author maurelio1234
 */
public interface ProfileManager {

    /**
     * Register a new profile.
     * 
     * @param profile A profile to be registered so that it is available to the
     *                users.
     */
    void registerProfile(Profile profile);

    /**
     * Remove a profile from the list of registered profiles.
     * 
     * @param profile the profile to unregister. It will no longer be available
     *                for selection by users
     */
    void removeProfile(Profile profile);
    
    /**
     * @return the list of registered profiles
     */
    List<Profile> getRegisteredProfiles();
    
    /**
     * Search for a Profile with the given Java classname.
     * 
     * @return the profile instance for the class or null if there is none.
     * @param className the name of the Java class to search for.
     */
    Profile getProfileForClass(String className);
    
    /**
     * @return the default list of profiles
     */
    List<Profile> getDefaultProfiles();
    
    /**
     * Add a profile to the default list.
     * 
     * @param profile profile to be added to the default application profiles.
     *                New models will reference it by default
     */
    void addToDefaultProfiles(Profile profile);
    
    /**
     * Remove a profile from the default list.
     * 
     * @param profile the profile to be removed
     */
    void removeFromDefaultProfiles(Profile profile);
    
    /**
     * Add a new directory to the directory list.
     * 
     * @param path a directory name where the manager will try to look for 
     *             user defined profiles as XMI files
     */
    void addSearchPathDirectory(String path);
    
    /**
     * Remove a directory from the directory list.
     * 
     * @param path the directory path to be removed.
     */
    void removeSearchPathDirectory(String path);
    
    /**
     * @return the current directory list
     */
    List<String> getSearchPathDirectories();
    
    /**
     * Look for XMI files at the current directory list and registers
     * them as user defined profiles.
     */
    void refreshRegisteredProfiles();

    /**
     * @return the Profile for UML, i.e., the base UML profile as defined by 
     *         the standard.
     */
    Profile getUMLProfile();

    /**
     * Looks for registered Profile
     * 
     * @param profile name
     * @return profile
     */
    Profile lookForRegisteredProfile(String profile);
}
