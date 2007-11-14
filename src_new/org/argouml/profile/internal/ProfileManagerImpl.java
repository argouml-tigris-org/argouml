// $Id: ProfileManagerImpl.java 13298 2007-08-12 19:40:57Z maurelio1234 $
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

package org.argouml.profile.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileManager;
import org.argouml.profile.UserDefinedProfile;

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

    /**
     * Avoids recursive configuration update when loading configuration
     */
    private boolean disableConfigurationUpdate = false;
    
    private List<Profile> profiles = new ArrayList<Profile>();

    private List<Profile> defaultProfiles = new ArrayList<Profile>();

    private List<String> searchDirectories = new ArrayList<String>();

    public ProfileManagerImpl() {
        try {
            Profile uml = new ProfileUML();
            defaultProfiles.add(uml);
            registerProfile(uml);
            registerProfile(new ProfileJava(uml));
        } catch (ProfileException e) {
            throw new RuntimeException(e);
        }

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
            Profile p = null;
            
            if (desc.charAt(0) == 'U') {
                String fileName = desc.substring(1);
                p = findUserDefinedProfile(new File(fileName));                
            } else if (desc.charAt(0) == 'C') {
                String className = desc.substring(1);
                p = getProfileForClass(className);
            }

            if (p != null) {
                addToDefaultProfiles(p);
            }
        }

        disableConfigurationUpdate = false;
    }

    private void updateDefaultProfilesConfiguration() {
        if (!disableConfigurationUpdate) {
            StringBuffer buf = new StringBuffer();
            
            for (Profile p : defaultProfiles) {
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

            for (String s : searchDirectories) {
                buf.append(s + "*");
            }

            Configuration.setString(KEY_DEFAULT_DIRECTORIES, buf.toString());
        }
    }

    public List<Profile> getRegisteredProfiles() {
        return profiles;
    }


    public void registerProfile(Profile p) {        
        if (p != null && !profiles.contains(p)) {
            if (p instanceof UserDefinedProfile
                    || getProfileForClass(p.getClass().getName()) == null) {
                profiles.add(p);
                
                // this profile could have not been loaded when 
                // the default profile configuration 
                // was loaded at first, so we need to do it again
                loadDefaultProfilesfromConfiguration();
            }
        }
    }


    public void removeProfile(Profile p) {
        if (p != null) {
            profiles.remove(p);
            defaultProfiles.remove(p);
        }
    }


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


    public void addToDefaultProfiles(Profile p) {
        if (p != null && profiles.contains(p) && !defaultProfiles.contains(p)) {
            defaultProfiles.add(p);
            updateDefaultProfilesConfiguration();
        }
    }


    public List<Profile> getDefaultProfiles() {
        return defaultProfiles;
    }


    public void removeFromDefaultProfiles(Profile p) {
        if (p != null && profiles.contains(p)) {
            defaultProfiles.remove(p);
            updateDefaultProfilesConfiguration();
        }
    }

    public void addSearchPathDirectory(String path) {
        if (path != null && !searchDirectories.contains(path)) {
            searchDirectories.add(path);
            updateSearchDirectoriesConfiguration();
        }
    }


    public List<String> getSearchPathDirectories() {
        return searchDirectories;
    }


    public void removeSearchPathDirectory(String path) {
        if (path != null) {
            searchDirectories.remove(path);
            updateSearchDirectoriesConfiguration();
        }
    }


    public void refreshRegisteredProfiles() {

        for (String dirName : searchDirectories) {
            File dir = new File(dirName);

            if (dir.exists()) {
                for (File file : dir.listFiles()) {
                    // TODO: Allow .zargo as profile as well?
                    if (file.getName().toLowerCase().endsWith(".xmi")) {

                        boolean found = 
                            findUserDefinedProfile(file) != null;

                        if (!found) {
                            UserDefinedProfile udp = null;
                            try {
                                udp = new UserDefinedProfile(file);
                                registerProfile(udp);
                            } catch (ProfileException e) {
                                // if an exception is raised file is unusable
                            }
                        }
                    }
                }
            }
        }
    }

    private Profile findUserDefinedProfile(File file) {
        
        for (Profile p : profiles) {
            if (p instanceof UserDefinedProfile) {
                UserDefinedProfile udp = (UserDefinedProfile) p;

                if (udp.getModelFile().equals(file)) {
                    return udp;
                }
            }
        }
        return null;
    }

    public Profile getUMLProfile() {
        for (Profile p : getRegisteredProfiles())
            if (p.getDisplayName() != null 
                    && p.getDisplayName().contains("UML"))
                return p;
        Profile p = null;
        try {
            p = new ProfileUML();
        } catch (ProfileException e) {
            throw new RuntimeException(e);
        }
        return p;
    }

}
