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

package org.argouml.uml.profile;

import java.io.File;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;

/**
 * Default <code>ProfileManager</code> implementation
 *
 * @author Marcos Aurélio
 */
public class ProfileManagerImpl implements ProfileManager {

    /**
     * The configuration key for the default profiles.
     */
    public static final ConfigurationKey KEY_DEFAULT_PROFILES = Configuration
            .makeKey("profiles", "default");

    /**
     * The configuration key for the search directories.
     */
    public static final ConfigurationKey KEY_DEFAULT_DIRECTORIES = Configuration
            .makeKey("profiles", "directories");

    private static ProfileManager instance = null;

    /**
     * Avoids recursive configuration update when loading configuration
     */
    private boolean disableConfigurationUpdate = false;
    
    private Vector profiles = new Vector();

    private Vector defaultProfiles = new Vector();

    private Vector searchDirectories = new Vector();

    private ProfileManagerImpl() {
        defaultProfiles.add(ProfileUML.getInstance());
        
        registerProfile(ProfileUML.getInstance());
        registerProfile(ProfileJava.getInstance());
        registerProfile(ProfileCpp.getInstance());

        loadDirectoriesFromConfiguration();

        refreshRegisteredProfiles();

        loadDefaultProfilesfromConfiguration();
    }

    private void loadDefaultProfilesfromConfiguration() {    
        disableConfigurationUpdate = true;
        
        StringTokenizer tokenizer = new StringTokenizer(Configuration
                .getString(KEY_DEFAULT_PROFILES), "*", false);

        while (tokenizer.hasMoreTokens()) {
            String desc = tokenizer.nextToken();

            if (desc.charAt(0) == 'U') {
                String fileName = desc.substring(1);
                Profile p = findUserDefinedProfile(new File(fileName));
                
                if (p != null) {
                    addToDefaultProfiles(p);
                }
            } else if (desc.charAt(0) == 'C') {
                String className = desc.substring(1);
                Profile p = getProfileForClass(className);
                
                if (p != null) {
                    addToDefaultProfiles(p);
                }
            }
        }

        disableConfigurationUpdate = false;
    }

    private void updateDefaultProfilesConfiguration() {
        if (!disableConfigurationUpdate) {
            StringBuffer buf = new StringBuffer();

            Iterator it = defaultProfiles.iterator();

            while (it.hasNext()) {
                Profile p = (Profile) it.next();

                if (p instanceof UserDefinedProfile) {
                    buf.append("U"
                            + ((UserDefinedProfile) p).getModelFile()
                                    .getAbsolutePath());
                } else {
                    buf.append("C" + p.getClass().getName());
                }

                buf.append("*");
            }

            Configuration.setString(KEY_DEFAULT_PROFILES, buf.toString());
        }
    }

    private void loadDirectoriesFromConfiguration() {
        disableConfigurationUpdate = true;
        
        StringTokenizer tokenizer = new StringTokenizer(Configuration
                .getString(KEY_DEFAULT_DIRECTORIES), "*", false);

        while (tokenizer.hasMoreTokens()) {
            searchDirectories.add(tokenizer.nextToken());
        }
        
        disableConfigurationUpdate = false;
    }

    private void updateSearchDirectoriesConfiguration() {
        if (!disableConfigurationUpdate) {
            StringBuffer buf = new StringBuffer();

            Iterator it = searchDirectories.iterator();

            while (it.hasNext()) {
                String s = (String) it.next();
                buf.append(s + "*");
            }

            Configuration.setString(KEY_DEFAULT_DIRECTORIES, buf.toString());
        }
    }

    /**
     * @return the unique instance for this class
     */
    public static ProfileManager getInstance() {
        if (instance == null) {
            instance = new ProfileManagerImpl();
        }
        return instance;
    }

    /**
     * @return the list of profiles
     * @see org.argouml.uml.profile.ProfileManager#getRegisteredProfiles()
     */
    public Vector getRegisteredProfiles() {
        return profiles;
    }

    /**
     * @param p the profile
     * @see org.argouml.uml.profile.ProfileManager#registerProfile(org.argouml.uml.profile.Profile)
     */
    public void registerProfile(Profile p) {
        if (!profiles.contains(p)) {
            if (p instanceof UserDefinedProfile
                    || getProfileForClass(p.getClass().getName()) == null) {
                profiles.add(p);
            }
        }
    }

    /**
     * @param p the profile
     * @see org.argouml.uml.profile.ProfileManager#removeProfile(org.argouml.uml.profile.Profile)
     */
    public void removeProfile(Profile p) {
        profiles.remove(p);
        defaultProfiles.remove(p);
    }

    /**
     * @param profileClass the profile class
     * @return the profile object or null if there is no one
     * @see org.argouml.uml.profile.ProfileManager#getProfileForClass(java.lang.Class)
     */
    public Profile getProfileForClass(String profileClass) {
        Iterator it = profiles.iterator();
        Profile found = null;

        while (it.hasNext()) {
            Profile p = (Profile) it.next();
            if (p.getClass().getName().equals(profileClass)) {
                found = p;
                break;
            }
        }

        return found;
    }

    /**
     * @param p th profile
     * @see org.argouml.uml.profile.ProfileManager#addToDefaultProfiles(org.argouml.uml.profile.Profile)
     */
    public void addToDefaultProfiles(Profile p) {
        if (p != null && profiles.contains(p)) {
            defaultProfiles.add(p);
            updateDefaultProfilesConfiguration();
        }
    }

    /**
     * @return the list
     * @see org.argouml.uml.profile.ProfileManager#getDefaultProfiles()
     */
    public Vector getDefaultProfiles() {
        return defaultProfiles;
    }

    /**
     * @param p the profile
     * @see org.argouml.uml.profile.ProfileManager#removeFromDefaultProfiles(org.argouml.uml.profile.Profile)
     */
    public void removeFromDefaultProfiles(Profile p) {
        if (p != null && profiles.contains(p)) {
            defaultProfiles.remove(p);
            updateDefaultProfilesConfiguration();
        }
    }

    public void addSearchPathDirectory(String path) {
        if (!searchDirectories.contains(path)) {
            searchDirectories.add(path);
            updateSearchDirectoriesConfiguration();
        }
    }

    /**
     * @return the directory list
     * @see org.argouml.uml.profile.ProfileManager#getSearchPathDirectories()
     */
    public Vector getSearchPathDirectories() {
        return searchDirectories;
    }

    /**
     * @param path the directory to remove
     * @see org.argouml.uml.profile.ProfileManager#removeSearchPathDirectory(java.lang.String)
     */
    public void removeSearchPathDirectory(String path) {
        if (path != null) {
            searchDirectories.remove(path);
            updateSearchDirectoriesConfiguration();
        }
    }

    /**
     * @see org.argouml.uml.profile.ProfileManager#refreshRegisteredProfiles()
     */
    public void refreshRegisteredProfiles() {
        Iterator it = searchDirectories.iterator();

        while (it.hasNext()) {
            File dir = new File((String) it.next());

            if (dir.exists()) {
                File[] files = dir.listFiles();

                for (int i = 0; i < files.length; ++i) {
                    if (files[i].getName().toLowerCase().endsWith(".xmi")) {

                        boolean found = findUserDefinedProfile(files[i]) != null;

                        if (!found) {
                            UserDefinedProfile udp = new UserDefinedProfile(
                                    files[i]);
                            registerProfile(udp);
                        }
                    }
                }
            }
        }
    }

    private Profile findUserDefinedProfile(File file) {
        Iterator it2 = profiles.iterator();

        while (it2.hasNext()) {
            Profile p = (Profile) it2.next();

            if (p instanceof UserDefinedProfile) {
                UserDefinedProfile udp = (UserDefinedProfile) p;

                if (udp.getModelFile().equals(file)) {
                    return udp;
                }
            }
        }
        return null;
    }

}
