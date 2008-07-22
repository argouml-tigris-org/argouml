// $Id$
// Copyright (c) 2007-2008 The Regents of the University of California. All
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
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

/**
 * Persister for project's profile configuration.
 *
 * @author maurelio1234
 */
public class ProfileConfigurationFilePersister extends MemberFilePersister {
    
    private static final Logger LOG = 
        Logger.getLogger(ProfileConfigurationFilePersister.class);

    /*
     * @see org.argouml.persistence.MemberFilePersister#getMainTag()
     */
    public String getMainTag() {
        return "profile";
    }

    /*
     * @see org.argouml.persistence.MemberFilePersister#load(org.argouml.kernel.Project, java.io.InputStream)
     */
    public void load(Project project, InputStream inputStream)
        throws OpenException {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    inputStream));

            String line = null;
            while (true) {
                line = br.readLine();
                if (line.trim().equals("<profile>")) {
                    break;
                }
            }
            Collection<Profile> profiles = new ArrayList<Profile>();
            while (true) {
                line = br.readLine().trim();
                if (line.equals("</profile>")) {
                    break;
                }
                
                Profile profile = null;

                if (line.equals("<userDefined>")) {
                    profile = handleUserDefinedProfile(br);
                    // consumes the </userDefined>
                    br.readLine();
                } else if (line.equals("<plugin>")) {
                    profile = handlePluginProfile(br);
                    // consumes closing tag
                    br.readLine();
                }

                if (profile != null) {
                    profiles.add(profile);
                }
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

    private static Profile handlePluginProfile(BufferedReader br)
        throws IOException, XmiReferenceException {
        Profile profile;
        String className = br.readLine().trim();
        profile = ProfileFacade.getManager().getProfileForClass(
                className);
        if (profile == null) 
            throw new XmiReferenceException(
                "Plugin profile \"" + className 
                + "\" is not available in installation.", null);
        return profile;
    }

    private static Profile handleUserDefinedProfile(BufferedReader br)
        throws IOException, XmiReferenceException {
        String line;
        Profile profile;
        line = br.readLine().trim();
        String fileName = line.substring(line.indexOf(">") + 1,
                line.indexOf("</")).trim();

        // consumes the <model> tag
        br.readLine();

        StringBuffer xmi = new StringBuffer();
        
        while (true) {
            line = br.readLine();
            if (line == null || line.contains("</model>")) {
                break;
            }
            xmi.append(line + "\n");
        }
        ProfileManager profileManager = ProfileFacade.getManager();
        profile = getMatchingUserDefinedProfile(fileName, 
            profileManager);
        if (profile == null) {
            throw new XmiReferenceException(
                "User defined profile \"" + fileName 
                + "\" isn't available in the current configuration.",
                    null);
            // Use XMI as a fall back alternative when the 
            // file for the user defined profile isn't found by the 
            // profile manager.
            // TODO: work in progress, see issue 5039
//                        addUserDefinedProfile(fileName, xmi, profileManager);
//                        profile = getMatchingUserDefinedProfile(fileName, 
//                            profileManager);
//                        assert profile != null 
//                            : "Profile should have been found now.";
        }
        return profile;
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
        FileWriter writer = new FileWriter(profileFile);
        writer.write(xmi.toString());
        writer.close();
        LOG.info("Wrote user defined profile \"" + profileFile 
            + "\", with size " + xmi.length() + ".");
        if (isSomeProfileDirectoryConfigured(profileManager))
            profileManager.refreshRegisteredProfiles();
        else 
            profileManager.addSearchPathDirectory(
                profilesDirectory.getAbsolutePath());
    }

    private static Profile getMatchingUserDefinedProfile(String fileName, 
            ProfileManager profileManager) {
        for (Profile candidateProfile 
            : profileManager.getRegisteredProfiles()) {
            if (candidateProfile instanceof UserDefinedProfile) {
                UserDefinedProfile userProfile = 
                    (UserDefinedProfile) candidateProfile;
                if (userProfile.getDisplayName().equals(fileName)) {
                    return userProfile;
                }
            }
        }
        return null;
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
     * @see org.argouml.persistence.MemberFilePersister#save(org.argouml.kernel.ProjectMember, java.io.Writer, boolean)
     */
    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public void save(ProjectMember member, Writer writer, boolean xmlFragment)
        throws SaveException {
        PrintWriter w = new PrintWriter(writer);
        saveProjectMember(member, w);
    }

    /*
     * @see org.argouml.persistence.MemberFilePersister#save(org.argouml.kernel.ProjectMember, java.io.OutputStream)
     */
    public void save(ProjectMember member, OutputStream stream)
	throws SaveException {
	
        PrintWriter w = new PrintWriter(stream);
	saveProjectMember(member, w);
        w.flush();
    }

    private void saveProjectMember(ProjectMember member, PrintWriter w)
	throws SaveException {
	
        try {
	    if (member instanceof ProfileConfiguration) {
		ProfileConfiguration pc = (ProfileConfiguration) member;

		w.println("<?xml version = \"1.0\" encoding = \"UTF-8\" ?>");
		w.println("<!DOCTYPE profile SYSTEM \"profile.dtd\" >");
		w.println("<profile>");

		Iterator it = pc.getProfiles().iterator();
		while (it.hasNext()) {
                    Profile profile = (Profile) it.next();

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
                        w.println("\t\t\t" + profile.getClass().getName());
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
        if (true) return;

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
        try {
            load(project, url.openStream());
        } catch (IOException e) {
            throw new OpenException(e);
        }
    }

}
