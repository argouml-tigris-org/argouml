// $Id: ProfileManager.java 13090 2007-07-16 12:37:40Z maurelio1234 $
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

package org.argouml.uml.profile;

import java.util.Vector;

/**
 * Manages the global set of registered profiles.
 *
 * @author maurelio1234
 */
public interface ProfileManager {
    /**
     * Register a new profile
     * 
     * @param p the profile
     */
    void registerProfile(Profile p);
    /**
     * Remove a profile from the list
     * 
     * @param p the profile
     */
    void removeProfile(Profile p);
    
    /**
     * @return the list of registered profiles
     */
    Vector<Profile> getRegisteredProfiles();
    
    /**
     * @return the profile instance for the class
     */
    Profile getProfileForClass(String className);
    
    /**
     * @return the default set of profiles
     */
    Vector<Profile> getDefaultProfiles();
    
    /**
     * Adds a profile to the default list.
     * 
     * @param p profile to add
     */
    void addToDefaultProfiles(Profile p);
    
    /**
     * Remove a profile from the default list
     * 
     * @param p the profile to be removed
     */
    void removeFromDefaultProfiles(Profile p);
    
    /**
     * Adds a new directory to the directory list
     * 
     * @param path user defined directory
     */
    void addSearchPathDirectory(String path);
    
    /**
     * Removes a directory from the directory list
     * 
     * @param path
     */
    void removeSearchPathDirectory(String path);
    
    /**
     * @return the current directory list
     */
    Vector<String> getSearchPathDirectories();
    
    /**
     * Looks for XMI files at the current directory list and registers
     * them as user defined profiles.
     */
    void refreshRegisteredProfiles();
}
