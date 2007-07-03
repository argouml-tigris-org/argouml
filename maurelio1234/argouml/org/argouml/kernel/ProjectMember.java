// $Id: ProjectMember.java 10737 2006-06-11 19:01:27Z mvw $
// Copyright (c) 2004-2006 The Regents of the University of California. All
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
 * A member of the project is a part of the datastructure that
 * makes up the project. A saved project data file contains all members.<p>
 *
 * Examples: The UML model, the ToDo list.
 *
 */
public interface ProjectMember {

    /**
     * In contrast to {@link #getZipName()} returns the member's
     * name without the prepended name of the project.
     *
     * @author Steffen Zschaler
     *
     * @return the member's name without any prefix or suffix
     */
    String getUniqueDiagramName();

    /**
     * Returns a unique member's name for storage in a zipfile.
     * The project's base name is prepended followed by an
     * underscore '_'.
     *
     * @return the name for zip file storage
     */
    String getZipName();

    /**
     * @return a short string defining the member type.
     * Usually equals the file extension.
     */
    String getType();

    /**
     * @return the file extension string
     */
    String getZipFileExtension();
    
    /**
     * Repair any corruptions in the project member. Executed before a save in
     * order to ensure persistence is robust.
     *
     * @return A text that explains what is repaired.
     */
    String repair();
}
