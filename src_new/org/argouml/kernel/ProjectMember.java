// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
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

package org.argouml.kernel;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;

public abstract class ProjectMember {

    ////////////////////////////////////////////////////////////////
    // instance varables

    private String name;
    private Project project = null;

    ////////////////////////////////////////////////////////////////
    // constructors

    public ProjectMember(String name, Project theProject) {
	project = theProject;
	setName(name);
    }


    ////////////////////////////////////////////////////////////////
    // accessors


    /**
     * In contrast to {@link #getName} returns the member's name without the
     * prepended name of the project. This is the name that
     * {@link Project#findMemberByName} goes by.
     *
     * @author Steffen Zschaler
     *
     * @return the member's name without the prepended name of the project
     */
    public String getPlainName() {
	String s = name;
    
	if (s != null) {
	    if (!s.endsWith (getFileExtension())) {
		s += getFileExtension();
	    }
	}
    
	return s;
    }

    /**
     * In contrast to {@link #getPlainName} returns the member's name
     * including the project's base name. The project's base name is
     * prepended followed by an underscore '_'.
     *
     * @return the member's name including the project's base name
     */
    public String getName() {
	if (name == null)
	    return null;

	String s = project.getBaseName();

	if (name.length() > 0)
	    s += "_" + name;
    
	if (!s.endsWith(getFileExtension()))
	    s += getFileExtension();
    
	return s;
    }
  
    public void setName(String s) { 
	name = s;

	if (name == null)
	    return;

	if (name.startsWith (project.getBaseName())) {
	    name = name.substring (project.getBaseName().length());
	    int i = 0;
	    for (; i < name.length(); i++)
		if (name.charAt(i) != '_')
		    break;
	    if (i > 0)
		name = name.substring(i);
	}

	if (name.endsWith(getFileExtension()))
	    name =
		name.substring(0,
				name.length() - getFileExtension().length());
    }

    public Project getProject() { return project; }

    public abstract String getType();
    public abstract String getFileExtension();

    public URL getURL() {
	return getProject().findMemberURLInSearchPath(getName());
    }

    ////////////////////////////////////////////////////////////////
    // actions

    public abstract void load() throws IOException, org.xml.sax.SAXException;

    /**
     * Save the projectmember to the given writer.
     * @param writer
     * @throws Exception
     */
    public abstract void save(Writer writer) throws Exception;

    public void remove() {
        name = null;
        project = null;
    }
} /* end class ProjectMember */

