// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

/**
 * A member of the project.
 *
 */
public abstract class AbstractProjectMember implements ProjectMember {

    ////////////////////////////////////////////////////////////////
    // instance varables

    private String uniqueName;
    private Project project = null;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     *
     * @param theUniqueName the name of the member, this must
     *                      be different for all members. Note
     *                      that for diagram members this is
     *                      not the name of the diagram.
     * @param theProject the owning project
     */
    public AbstractProjectMember(String theUniqueName, Project theProject) {
        project = theProject;
        makeUniqueName(theUniqueName);
    }

    /**
     * In contrast to {@link #getZipName()} returns the member's
     * name without the prepended name of the project.
     *
     * @author Steffen Zschaler
     *
     * @return the member's name without any prefix or suffix
     */
    public String getUniqueDiagramName() {
        String s = uniqueName;

        if (s != null) {
            if (!s.endsWith (getZipFileExtension())) {
                s += getZipFileExtension();
            }
        }

        return s;
    }

    /**
     * Returns a unique member's name for storage in a zipfile.
     * The project's base name is prepended followed by an
     * underscore '_'.
     *
     * @return the name for zip file storage
     */
    public String getZipName() {
        if (uniqueName == null) {
	    return null;
	}

        String s = project.getBaseName();

        if (uniqueName.length() > 0) {
            s += "_" + uniqueName;
	}

        if (!s.endsWith(getZipFileExtension())) {
            s += getZipFileExtension();
        }

        return s;
    }

    /**
     * Makes a unique name for this member.
     * Note this is not the diagram name and this appears
     * to be flawed.
     * @param s a string which will make up part of this
     *          unique name.
     */
    protected void makeUniqueName(String s) {
        uniqueName = s;

        if (uniqueName == null) {
            return;
        }

        if (uniqueName.startsWith (project.getBaseName())) {
            uniqueName = uniqueName.substring (project.getBaseName().length());
            int i = 0;
            for (; i < uniqueName.length(); i++) {
            	if (uniqueName.charAt(i) != '_') {
            	    break;
                }
            }
            if (i > 0) {
                uniqueName = uniqueName.substring(i);
            }
        }

        if (uniqueName.endsWith(getZipFileExtension())) {
            uniqueName =
                uniqueName.substring(0,
                        uniqueName.length() - getZipFileExtension().length());
        }
    }

    /**
     * @return a short string defining the member type.
     * Usually equals the file extension.
     */
    public abstract String getType();

    /**
     * @return the file extension string
     */
    public String getZipFileExtension() {
        return "." + getType();
    }

    ////////////////////////////////////////////////////////////////
    // actions

    /**
     * Remove this member from its project.
     */
    protected void remove() {
        uniqueName = null;
        project = null;
    }
} /* end class ProjectMember */
