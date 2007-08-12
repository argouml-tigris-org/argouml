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

package org.argouml.persistence;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.application.helpers.ApplicationVersion;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.argouml.model.XmiWriter;
import org.argouml.uml.profile.Profile;
import org.argouml.uml.profile.ProfileConfiguration;
import org.argouml.uml.profile.ProfileManagerImpl;
import org.argouml.uml.profile.StreamModelLoader;
import org.argouml.uml.profile.UserDefinedProfile;
import org.xml.sax.InputSource;

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
                    line = br.readLine().trim();
		    String fileName = line.substring(line.indexOf(">")+1, line.indexOf("</")).trim();

                    // consumes the <model> tag
                    br.readLine();
                    
		    File file = new File(fileName);

		    StringBuffer xmi = new StringBuffer();
                    
                    while(true) {
                        line = br.readLine();
                        if (line == null || line.contains("</model>")) {
                            break;
                        }
                        
                        xmi.append(line+"\n");
                    }
                    
                    Object model = readModelXMI(xmi.toString());
                    profile = new UserDefinedProfile(fileName, model);
                    
                    // consumes the </userDefined>
		    line = br.readLine().trim();		    
		} else if (line.equals("<plugin>")) {
		    String className = br.readLine().trim();

                    profile = ProfileManagerImpl.getInstance()
                            .getProfileForClass(className);
		    
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
                        UserDefinedProfile uprofile = (UserDefinedProfile) profile;
                        w.println("\t\t<userDefined>");
                        w.println("\t\t\t<filename>"
                                + uprofile.getModelFile().getName()
                                + "</filename>");
                        w.println("\t\t\t<model>");

                        printModelXMI(w, uprofile.getModel());

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

    private void printModelXMI(PrintWriter w, Object model) throws UmlException {
        StringWriter myWriter = new StringWriter();
        XmiWriter xmiWriter = Model.getXmiWriter(model, myWriter, ApplicationVersion
                .getVersion()
                + "(" + UmlFilePersister.PERSISTENCE_VERSION + ")");
        xmiWriter.write();
        
        myWriter.flush();
        w.println("" + myWriter.toString());
    }

    private Object readModelXMI(String xmi) throws UmlException {
        XmiReader xmiReader = Model.getXmiReader();
        InputSource inputSource = new InputSource(new ByteArrayInputStream(xmi.getBytes()));
        Collection elements = xmiReader.parse(inputSource, true);
        return elements.iterator().next();
    }

}
