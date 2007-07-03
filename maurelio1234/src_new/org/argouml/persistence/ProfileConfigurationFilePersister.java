// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
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

package org.argouml.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.uml.profile.Profile;
import org.argouml.uml.profile.ProfileConfiguration;
import org.argouml.uml.profile.ProfileManagerImpl;
import org.argouml.uml.profile.UserDefinedProfile;

/**
 * Persister for Project's Profile Configuration
 *
 * @author maurelio1234
 */
public class ProfileConfigurationFilePersister extends MemberFilePersister {

    /**
     * @return the tag used to store this content on the uml file
     * 
     * @see org.argouml.persistence.MemberFilePersister#getMainTag()
     */
    public String getMainTag() {
	return "profile";
    }

    /**
     * @param project
     * @param inputStream
     * @throws OpenException
     * 
     * @see org.argouml.persistence.MemberFilePersister#load(org.argouml.kernel.Project, java.io.InputStream)
     */
    public void load(Project project, InputStream inputStream)
	    throws OpenException {
	try {
	    ProfileConfiguration pc = new ProfileConfiguration(project);

	    BufferedReader br = new BufferedReader(new InputStreamReader(
		    inputStream));

	    String line = null;
	    while (true) {
		line = br.readLine();
		if (line.trim().equals("<profile>")) {
		    break;
		}
	    }

	    while (true) {
		line = br.readLine().trim();

		if (line.equals("</profile>")) {
		    break;
		}

		Profile profile = null;
		Vector profiles = ProfileManagerImpl.getInstance()
			.getRegisteredProfiles();

		if (line.equals("<userDefined>")) {
		    String fileName = br.readLine().trim();

		    File file = new File(fileName);

		    boolean found = false;
		    for (int i = 0; i < profiles.size(); ++i) {
			if (profiles.get(i) instanceof UserDefinedProfile) {
			    UserDefinedProfile p = (UserDefinedProfile) profiles
				    .get(i);
			    if (p.getModelFile().equals(file)) {
				found = true;
				profile = p;
				break;
			    }
			}
		    }

		    if (!found) {
			UserDefinedProfile ud = new UserDefinedProfile(file);
			profile = ud;
		    }
		    
		    line = br.readLine().trim();		    
		} else if (line.equals("<plugin>")) {
		    String className = br.readLine().trim();
		    for (int i = 0; i < profiles.size(); ++i) {
			Profile p = (Profile) profiles.get(i);
			if (p.getClass().getName().equals(className)) {
			    profile = p;
			    break;
			}
		    }
		    
		    line = br.readLine().trim();
		}

		if (profile != null) {
		    pc.addProfile(profile);
		}
	    }
	    project.setProfileConfiguration(pc);
	} catch (Exception e) {
	    // LOG.error("Exception", e);
	    throw new OpenException(e);
	}
    }

    /**
     * @param member
     * @param writer
     * @param xmlFragment
     * @throws SaveException
     * @deprecated
     * 
     * @see org.argouml.persistence.MemberFilePersister#save(org.argouml.kernel.ProjectMember, java.io.Writer, boolean)
     */
    public void save(ProjectMember member, Writer writer, boolean xmlFragment)
	    throws SaveException {
	PrintWriter w = new PrintWriter(writer);
	saveProjectMember(member, w);
    }

    /**
     * @param member
     * @param stream
     * @throws SaveException
     * 
     * @see org.argouml.persistence.MemberFilePersister#save(org.argouml.kernel.ProjectMember, java.io.OutputStream)
     */
    public void save(ProjectMember member, OutputStream stream)
	    throws SaveException {
	PrintWriter w = new PrintWriter(stream);
	saveProjectMember(member, w);
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

		    if (profile != pc.getDefaultProfile()) {
			if (profile instanceof UserDefinedProfile) {
			    w.println("\t\t<userDefined>");
			    w.println("\t\t\t"
				    + ((UserDefinedProfile) profile)
					    .getModelFile().getCanonicalPath());
			    w.println("\t\t</userDefined>");
			} else {
			    w.println("\t\t<plugin>");
			    w.println("\t\t\t" + profile.getClass().getName());
			    w.println("\t\t</plugin>");
			}
		    }
		}

		w.println("</profile>");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new SaveException(e);
	}
    }

}
