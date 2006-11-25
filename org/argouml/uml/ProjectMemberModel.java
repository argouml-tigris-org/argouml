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

package org.argouml.uml;

import org.argouml.kernel.AbstractProjectMember;
import org.argouml.kernel.Project;
import org.argouml.model.Model;

/**
 * @author Piotr Kaminski
 */
public class ProjectMemberModel extends AbstractProjectMember {

    private static final String MEMBER_TYPE = "xmi";
    private static final String FILE_EXT = "." + MEMBER_TYPE;

    private Object model;

    /**
     * The constructor.
     *
     * @param m the model
     * @param p the project
     */
    public ProjectMemberModel(Object m, Project p) {

        super(p.getBaseName() + FILE_EXT, p);

        if (!Model.getFacade().isAModel(m))
            throw new IllegalArgumentException();

        setModel(m);
    }

    /**
     * @return the model
     */
    public Object getModel() {
        return model;
    }

    /**
     * @param m the model
     */
    protected void setModel(Object m) {
        model = m;
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
    public String getZipFileExtension() {
        return FILE_EXT;
    }
    
    /**
     * There is not yet any repair task for the UML model but this is open to
     * implement as and when any problems areas are discovered.
     * 
     * {@inheritDoc}
     */
    public String repair() {
        return "";
    }

} /* end class ProjectMemberModel */
