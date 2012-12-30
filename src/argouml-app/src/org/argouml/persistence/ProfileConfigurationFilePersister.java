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
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007-2009 The Regents of the University of California. All
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

package org.argouml.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.application.api.Argo;
import org.argouml.application.helpers.ApplicationVersion;
import org.argouml.configuration.Configuration;
import org.argouml.kernel.ProfileConfiguration;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiWriter;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.ProfileManager;
import org.argouml.profile.UserDefinedProfile;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Persister for project's profile configuration.
 *
 * @author maurelio1234
 */
public class ProfileConfigurationFilePersister extends MemberFilePersister {

    private static final Logger LOG =
        Logger.getLogger(ProfileConfigurationFilePersister.class.getName());

    /*
     * @see org.argouml.persistence.MemberFilePersister#getMainTag()
     */
    public String getMainTag() {
        return "profile";
    }

    public void load(Project project, InputStream inputStream)
        throws OpenException {
        load(project, new InputSource(inputStream));
    }

    /*
     * @see org.argouml.persistence.MemberFilePersister#load(org.argouml.kernel.Project, java.io.InputStream)
     */
    public void load(Project project, InputSource inputSource)
        throws OpenException {
        try {
            ProfileConfigurationParser parser =
                new ProfileConfigurationParser();
            parser.parse(inputSource);
            Collection<Profile> profiles = parser.getProfiles();

            Collection<String> unresolved = parser.getUnresolvedFilenames();
            if (!unresolved.isEmpty()) {
                profiles.addAll(loadUnresolved(unresolved));
            }

            ProfileConfiguration pc = new ProfileConfiguration(project,
                    profiles);
            project.setProfileConfiguration(pc);
        } catch (Exception e) {
            if (e instanceof OpenException) {
                throw (OpenException) e;
            }
            throw new OpenException(e);
        }
    }


    /**
     * Use XMI as a fall back alternative when the file for the user defined
     * profile isn't found by the profile manager.
     * <p>
     * TODO: work in progress, see issue 5039
     *
     * @param unresolved collection of unresolved filenames from the parser
     * @return collection of resolved profiles
     */
    private Collection<Profile> loadUnresolved(Collection<String> unresolved) {
        Collection<Profile> profiles = new ArrayList<Profile>();
        ProfileManager profileManager = ProfileFacade.getManager();
        for (String filename : unresolved) {
            // TODO: work in progress, see issue 5039
//            addUserDefinedProfile(filename, xmi, profileManager);
//            Profile profile = getMatchingUserDefinedProfile(filename,
//                    profileManager);
//            assert profile != null : "Profile should have been found now.";
//            profiles.add(profile);
        }
        return profiles;
    }

    /**
     * Register a user defined profile in the profileManager, using the backup
     * XMI file from the project being loaded.
     * <p>
     * <em>NOTE:</em> This has the side effect of permanently registering the
     * profile which may not be what the user wants.
     *
     * @param fileName name of original XMI file that the author of the project
     *                used when creating the UserDefinedProfile.
     * @param xmi the contents of the XMI file.
     * @param profileManager the {@link ProfileManager}.
     * @throws IOException on any i/o error
     */
    private void addUserDefinedProfile(String fileName, StringBuffer xmi,
            ProfileManager profileManager) throws IOException {
        File profilesDirectory = getProfilesDirectory(profileManager);
        File profileFile = new File(profilesDirectory, fileName);
        OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(profileFile),
                Argo.getEncoding());
        writer.write(xmi.toString());
        writer.close();

        LOG.log(Level.INFO,
                "Wrote user defined profile \"{0}\", with size {1}.",
                new Object[]{profileFile, xmi.length()});

        if (isSomeProfileDirectoryConfigured(profileManager)) {
            profileManager.refreshRegisteredProfiles();
        } else {
            profileManager.addSearchPathDirectory(
                profilesDirectory.getAbsolutePath());
        }
    }


    private static File getProfilesDirectory(ProfileManager profileManager) {
        if (isSomeProfileDirectoryConfigured(profileManager)) {
            List<String> directories =
                profileManager.getSearchPathDirectories();
            return new File(directories.get(0));
        } else {
            File userSettingsFile = new File(
                Configuration.getFactory().getConfigurationHandler().
                    getDefaultPath());
            return userSettingsFile.getParentFile();
        }
    }

    private static boolean isSomeProfileDirectoryConfigured(
            ProfileManager profileManager) {
        return profileManager.getSearchPathDirectories().size() > 0;
    }

    /*
     * @see org.argouml.persistence.MemberFilePersister#save(org.argouml.kernel.ProjectMember, java.io.OutputStream)
     */
    public void save(ProjectMember member, OutputStream stream)
	throws SaveException {

        PrintWriter w;
        try {
            w = new PrintWriter(new OutputStreamWriter(stream, "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            throw new SaveException("UTF-8 encoding not supported on platform",
                    e1);
        }
        saveProjectMember(member, w);
        w.flush();
    }

    private void saveProjectMember(ProjectMember member, PrintWriter w)
	throws SaveException {

        try {
	    if (member instanceof ProfileConfiguration) {
		ProfileConfiguration pc = (ProfileConfiguration) member;

		w.println("<?xml version = \"1.0\" encoding = \"UTF-8\" ?>");
		// TODO: This DTD doesn't exist, so we can't tell readers to
		// look for it
//		w.println("<!DOCTYPE profile SYSTEM \"profile.dtd\" >");
		// but we need a 2nd line to make the funky UML persister work
		w.println(""); // remove this line if the above is uncommented
		w.println("<profile>");

		for (Profile profile : pc.getProfiles()) {
                    if (profile instanceof UserDefinedProfile) {
                        UserDefinedProfile uprofile =
                            (UserDefinedProfile) profile;
                        w.println("\t\t<userDefined>");
                        w.println("\t\t\t<filename>"
                                + uprofile.getModelFile().getName()
                                + "</filename>");
                        w.println("\t\t\t<model>");

                        printModelXMI(w, uprofile.getProfilePackages());

                        w.println("\t\t\t</model>");
                        w.println("\t\t</userDefined>");
                    } else {
                        w.println("\t\t<plugin>");
                        w.println("\t\t\t" + profile.getProfileIdentifier());
                        w.println("\t\t</plugin>");
                    }
                }

		w.println("</profile>");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new SaveException(e);
	}
    }

    private void printModelXMI(PrintWriter w, Collection profileModels)
        throws UmlException {

        // TODO: Why is this not executed?  Remove if not needed - tfm
        if (true) {
            return;
        }

        StringWriter myWriter = new StringWriter();
        for (Object model : profileModels) {
            XmiWriter xmiWriter = Model.getXmiWriter(model,
                (OutputStream) null, //myWriter,
                ApplicationVersion.getVersion() + "("
                    + UmlFilePersister.PERSISTENCE_VERSION + ")");
            xmiWriter.write();
        }

        myWriter.flush();
        w.println("" + myWriter.toString());
    }


    @Override
    public void load(Project project, URL url) throws OpenException {
        load(project, new InputSource(url.toExternalForm()));
    }

}

/**
 * Parser for Profile Configuration.
 *
 * @author Tom Morris <tfmorris@gmail.com>
 */
class ProfileConfigurationParser extends SAXParserBase {

    private static final Logger LOG =
        Logger.getLogger(ProfileConfigurationParser.class.getName());

    private ProfileConfigurationTokenTable tokens =
        new ProfileConfigurationTokenTable();

    private Profile profile;

    private String model;

    private String filename;

    private Collection<Profile> profiles = new ArrayList<Profile>();

    private Collection<String> unresolvedFilenames = new ArrayList<String>();

    /**
     * Construct the parser.
     */
    public ProfileConfigurationParser() {
        // Empty constructor
    }

    public Collection<Profile> getProfiles() {
        return profiles;
    }

    public Collection<String> getUnresolvedFilenames() {
        return unresolvedFilenames;
    }

    public void handleStartElement(XMLElement e) {

        try {
            switch (tokens.toToken(e.getName(), true)) {

            case ProfileConfigurationTokenTable.TOKEN_PROFILE:
                break;
            case ProfileConfigurationTokenTable.TOKEN_PLUGIN:
                profile = null;
                break;
            case ProfileConfigurationTokenTable.TOKEN_USER_DEFINED:
                profile = null;
                filename = null;
                model = null;
                break;
            case ProfileConfigurationTokenTable.TOKEN_FILENAME:
                break;
            case ProfileConfigurationTokenTable.TOKEN_MODEL:
                break;

            default:
                LOG.log(Level.WARNING, "WARNING: unknown tag:" + e.getName());
                break;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Exception in startelement", ex);
        }
    }

    /**
     * Called by the XML implementation to signal the end of an XML entity.
     *
     * @param e The XML entity that ends.
     * @throws SAXException on any error
     */
    public void handleEndElement(XMLElement e) throws SAXException {

        try {
            switch (tokens.toToken(e.getName(), false)) {

            case ProfileConfigurationTokenTable.TOKEN_PROFILE:
                handleProfileEnd(e);
                break;
            case ProfileConfigurationTokenTable.TOKEN_PLUGIN:
                handlePluginEnd(e);
                break;
            case ProfileConfigurationTokenTable.TOKEN_USER_DEFINED:
                handleUserDefinedEnd(e);
                break;
            case ProfileConfigurationTokenTable.TOKEN_FILENAME:
                handleFilenameEnd(e);
                break;
            case ProfileConfigurationTokenTable.TOKEN_MODEL:
                handleModelEnd(e);
                break;

            default:
                LOG.log(Level.WARNING,
                        "WARNING: unknown end tag:" + e.getName());
                break;
            }
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }

    protected void handleProfileEnd(XMLElement e) {
        if (profiles.isEmpty()) {
            LOG.log(Level.WARNING, "No profiles defined");
        }
    }

    protected void handlePluginEnd(XMLElement e) throws SAXException {
        String name = e.getText().trim();
        profile = lookupProfile(name);
        if (profile != null) {
            profiles.add(profile);
            LOG.log(Level.FINE, "Found plugin profile {0}", name);
        } else {
            LOG.log(Level.SEVERE, "Unabled to find plugin profile - {0}", name);
        }
    }

    private static Profile lookupProfile(String profileIdentifier)
        throws SAXException {
        Profile profile;
        profile = ProfileFacade.getManager().lookForRegisteredProfile(
                profileIdentifier);
        if (profile == null) {

            // for compatibility with older format
            profile = ProfileFacade.getManager().getProfileForClass(
                    profileIdentifier);

            if (profile == null) {
                throw new SAXException("Plugin profile \"" + profileIdentifier
                        + "\" is not available in installation.", null);
            }
        }
        return profile;
    }

    protected void handleUserDefinedEnd(XMLElement e) {
        // <model> is not used in current implementation
        if (filename == null /* || model == null */) {
            LOG.log(Level.SEVERE,
                    "Got badly formed user defined profile entry " + e);
        }
        profile = getMatchingUserDefinedProfile(filename, ProfileFacade
                .getManager());

        if (profile == null) {
            unresolvedFilenames.add(filename);
        } else {
            profiles.add(profile);
            LOG.log(Level.FINE,
                    "Loaded user defined profile - filename = {0}", filename);
        }

    }

    private static Profile getMatchingUserDefinedProfile(String fileName,
            ProfileManager profileManager) {
        for (Profile candidateProfile
                : profileManager.getRegisteredProfiles()) {
            if (candidateProfile instanceof UserDefinedProfile) {
                UserDefinedProfile userProfile =
                    (UserDefinedProfile) candidateProfile;
                if (userProfile.getModelFile() != null
                        && fileName
                                .equals(userProfile.getModelFile().getName())) {
                    return userProfile;
                }
            }
        }
        return null;
    }

    protected void handleFilenameEnd(XMLElement e) {
        filename = e.getText().trim();
        LOG.log(Level.FINE, "Got filename = {0}", filename);
    }

    protected void handleModelEnd(XMLElement e) {
        model = e.getText().trim();
        LOG.log(Level.FINE, "Got model = {0}", model);
    }

    /**
     * Token Table for Profile Configuration parser.
     *
     * @author Tom Morris
     */
    class ProfileConfigurationTokenTable extends XMLTokenTableBase {

        private static final String STRING_PROFILE = "profile";

        private static final String STRING_PLUGIN = "plugin";

        private static final String STRING_USER_DEFINED = "userDefined";

        private static final String STRING_FILENAME = "filename";

        private static final String STRING_MODEL = "model";

        public static final int TOKEN_PROFILE = 1;

        public static final int TOKEN_PLUGIN = 2;

        public static final int TOKEN_USER_DEFINED = 3;

        public static final int TOKEN_FILENAME = 4;

        public static final int TOKEN_MODEL = 5;

        private static final int TOKEN_LAST = 5;

        public static final int TOKEN_UNDEFINED = 999;

        /**
         * Construct the token table.,
         */
        public ProfileConfigurationTokenTable() {
            super(TOKEN_LAST);
        }

        protected void setupTokens() {
            addToken(STRING_PROFILE, Integer.valueOf(TOKEN_PROFILE));
            addToken(STRING_PLUGIN, Integer.valueOf(TOKEN_PLUGIN));
            addToken(STRING_USER_DEFINED, Integer.valueOf(TOKEN_USER_DEFINED));
            addToken(STRING_FILENAME, Integer.valueOf(TOKEN_FILENAME));
            addToken(STRING_MODEL, Integer.valueOf(TOKEN_MODEL));
        }
    }

}
