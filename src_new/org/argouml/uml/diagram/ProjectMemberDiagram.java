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

package org.argouml.uml.diagram;


import org.argouml.kernel.Project;
import org.argouml.kernel.AbstractProjectMember;
import org.tigris.gef.util.Util;

/**
 * @author Piotr Kaminski
 */
public class ProjectMemberDiagram extends AbstractProjectMember {

    private static final String MEMBER_TYPE = "pgml";
    private static final String FILE_EXT = ".pgml";

    private ArgoDiagram diagram;

    /**
     * The constructor.
     *
     * @param d the diagram
     * @param p the project
     */
    public ProjectMemberDiagram(ArgoDiagram d, Project p) {
        super(null, p);
        String s = Util.stripJunk(d.getName());
        makeUniqueName(s);
        setDiagram(d);
    }

    /**
     * Used by "argo.tee".
     * 
     * @return the diagram
     */
    public ArgoDiagram getDiagram() {
        return diagram;
    }
    
    /*
     * @see org.argouml.kernel.AbstractProjectMember#getType()
     */
    public String getType() {
        return MEMBER_TYPE;
    }
    
    /*
     * @see org.argouml.kernel.AbstractProjectMember#getZipFileExtension()
     */
    @Override
    public String getZipFileExtension() {
        return FILE_EXT;
    }

    /**
     * @param d the diagram
     */
    protected void setDiagram(ArgoDiagram d) {
        diagram = d;
    }
    
    /*
     * @see org.argouml.kernel.ProjectMember#repair()
     */
    public String repair() {
        return diagram.repair();
    }

}
