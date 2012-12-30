/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *    euluis
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.profile.init;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.cognitive.Critic;
import org.argouml.i18n.Translator;
import org.argouml.moduleloader.ModuleLoader2;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.UserDefinedProfile;

/**
 * This is the profile loader that loads modules profiles.
 *
 * @author maurelio1234
 */
public final class ProfileLoader {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ProfileLoader.class.getName());

    /**
     * The prefix in URL:s that are jars.
     */
    private static final String JAR_PREFIX = "jar:";

    /**
     * The prefix in URL:s that are files.
     */
    private static final String FILE_PREFIX = "file:";

    /**
     * Looks for profiles in the jars in the directories used by the
     * ModuleLoader to load modules.
     */
    public void doLoad() {
        List<String> extDirs =
            ModuleLoader2.getInstance().getExtensionLocations();

        for (String extDir : extDirs) {
            huntForProfilesInDir(extDir);
        }
    }

    private void huntForProfilesInDir(String dir) {
        LOG.log(Level.INFO, "Looking for Profiles in {0}", dir);

        File extensionDir = new File(dir);
        if (extensionDir.isDirectory()) {
            File[] files = extensionDir.listFiles(new JarFileFilter());
            for (File file : files) {
                JarFile jarfile = null;
                try {
                    jarfile = new JarFile(file);
                    if (jarfile != null) {
                        LOG.log(Level.INFO,
                                "Looking for Profiles in the Jar {0}",
                                jarfile.getName());

                        ClassLoader classloader = new URLClassLoader(
                                new URL[] {file.toURI().toURL()});
                        loadProfilesFromJarFile(jarfile.getManifest(), file,
                                classloader);
                    }
                } catch (IOException ioe) {
                    LOG.log(Level.FINE, "Cannot open Jar file " + file, ioe);
                }
            }
        }

    }

    /**
     * Interprets the MANIFEST file in the JAR in order to load the declared
     * profile.
     *
     * @param file the file object referencing the Jar
     * @param manifest the manifest file of the Jar
     * @param classloader the classloader that loads the classes referenced by
     *            the Jar
     */
    private void loadProfilesFromJarFile(Manifest manifest, File file,
            ClassLoader classloader) {
        Map<String, Attributes> entries = manifest.getEntries();
        boolean classLoaderAlreadyAdded = false;

        for (String entryName : entries.keySet()) {
            Attributes attr = entries.get(entryName);
            if (new Boolean(attr.getValue("Profile") + "").booleanValue()) {
                try {
                    // we only need to add the classloader once
                    // and if and only if there is at least a profile
                    // in the JAR
                    if (!classLoaderAlreadyAdded) {
                        Translator.addClassLoader(classloader);
                        classLoaderAlreadyAdded = true;
                    }
                    Set<Critic> critics = loadJavaCriticsForProfile(attr,
                            classloader);
                    String modelPath = attr.getValue("Model");
                    URL modelURL = null;

                    if (modelPath != null) {
                        modelURL = new URL(JAR_PREFIX + FILE_PREFIX
                                + file.getCanonicalPath() + "!" + modelPath);
                    }

                    UserDefinedProfile udp = new UserDefinedProfile(entryName,
                            modelURL, critics,
                            loadManifestDependenciesForProfile(attr),
                            ProfileFacade.getManager());

                    ProfileFacade.getManager().registerProfile(udp);
                    LOG.log(Level.FINE,
                            "Registered Profile: {0}", udp.getDisplayName());
                } catch (ProfileException e) {
                    LOG.log(Level.SEVERE, "Exception", e);
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Exception", e);
                }
            }

        }
    }

    /**
     * Resolves the dependencies for a Profile.
     *
     * @param attr a group of attributes in the MANIFEST file for this JAR
     *
     * @return the set of defined profiles
     */
    private Set<String> loadManifestDependenciesForProfile(Attributes attr) {
        Set<String> ret = new HashSet<String>();
        String value = attr.getValue("Depends-on");
        if (value != null) {
            StringTokenizer st = new StringTokenizer(value, ",");

            while (st.hasMoreElements()) {
                String entry = st.nextToken().trim();
                ret.add(entry);
            }
        }

        return ret;
    }

    /**
     * Loads the Java critics defined by a profile.
     *
     * @param attr the Manifest section of the profile
     * @param classloader the classloader of the Jar
     *
     * @return the set of defined critics
     */
    private Set<Critic> loadJavaCriticsForProfile(Attributes attr,
            ClassLoader classloader) {
        Set<Critic> ret = new HashSet<Critic>();

        String value = attr.getValue("Java-Critics");
        if (value != null) {
            StringTokenizer st = new StringTokenizer(value, ",");

            while (st.hasMoreElements()) {
                String entry = st.nextToken().trim();

                try {
                    Class cl = classloader.loadClass(entry);
                    Critic critic = (Critic) cl.newInstance();
                    ret.add(critic);
                } catch (ClassNotFoundException e) {
                    LOG.log(Level.SEVERE, "Error loading class: " + entry, e);
                } catch (InstantiationException e) {
                    LOG.log(Level.SEVERE,
                            "Error instantianting class: " + entry, e);
                } catch (IllegalAccessException e) {
                    LOG.log(Level.SEVERE, "Exception", e);
                }
            }
        }

        return ret;
    }

    /**
     * The file filter that selects Jar files.
     */
    static class JarFileFilter implements FileFilter {
        /*
         * @see java.io.FileFilter#accept(java.io.File)
         */
        public boolean accept(File pathname) {
            return (pathname.canRead()
                    && pathname.isFile()
                    && pathname.getPath().toLowerCase().endsWith(".jar"));
        }
    }

}
