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

package org.argouml.uml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.kernel.SaveException;
import org.argouml.model.ModelFacade;

import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.xmi.IncompleteXMIException;
import ru.novosoft.uml.xmi.XMIWriter;

/**
 * @author Piotr Kaminski
 * This file updated by Jim Holt 1/17/00 for nsuml support 
 */
public class ProjectMemberModel extends ProjectMember {

    private static final Logger LOG =
        Logger.getLogger(org.argouml.uml.ProjectMemberModel.class);

    ////////////////////////////////////////////////////////////////
    // constants

    private static final String MEMBER_TYPE = "xmi";
    private static final String FILE_EXT = "." + MEMBER_TYPE;
    
    ////////////////////////////////////////////////////////////////
    // instance variables

    private Object model;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     * TODO: This constructor is never user. Remove?
     * 
     * @param name the name of the model
     * @param p the project
     */
    public ProjectMemberModel(String name, Project p) {
        super(name, p);
    }

    /**
     * The constructor.
     * 
     * @param m the model
     * @param p the project
     */
    public ProjectMemberModel(Object m, Project p) {
        
        super(p.getBaseName() + FILE_EXT, p);
        
        if (!ModelFacade.isAModel(m))
            throw new IllegalArgumentException();
        
        setModel(m);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

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
        model = /*(MModel)*/m;
    }

    /**
     * @see org.argouml.kernel.ProjectMember#getType()
     */
    public String getType() {
        return MEMBER_TYPE;
    }
    /**
     * @see org.argouml.kernel.ProjectMember#getFileExtension()
     */
    public String getFileExtension() {
        return FILE_EXT;
    }

    ////////////////////////////////////////////////////////////////
    // actions

    /**
     * Save the project model to XMI.
     * @see org.argouml.kernel.ProjectMember#save(java.io.Writer)
     */
    public void save(Writer w, Integer indent) throws SaveException {
        if (w == null) {
            throw new IllegalArgumentException("No Writer specified!");
        }

        File tempFile = null;
        Writer writer = null;
        if (indent != null) {
            try {
                tempFile = File.createTempFile("xmi", null);
                tempFile.deleteOnExit();
                writer = new FileWriter(tempFile);
            } catch (IOException e) {
                throw new SaveException(e);
            }
        } else {
            writer = w;
        }
        XMIWriter xmiwriter = null;

        try {
            xmiwriter = new XMIWriter((MModel) model, writer);
            xmiwriter.gen();
        } catch (Exception ex) {
            logNotContainedElements(xmiwriter);
            if (ex instanceof SaveException) {
                throw (SaveException) ex;
            }
            throw new SaveException(ex);
        } finally {
            if (xmiwriter != null) {
                if (!xmiwriter.getNotContainedElements().isEmpty()) {
                    logNotContainedElements(xmiwriter);
                    throw new SaveException(new IncompleteXMIException());
                }
            }
        }
        if (indent != null) {
            addXmlFileToWriter((PrintWriter) w, tempFile, indent.intValue());
        }
    }

    private void logNotContainedElements(XMIWriter xmiwriter) {
        if (xmiwriter != null) {
            Iterator it = xmiwriter.getNotContainedElements().iterator();
            while (it.hasNext())
                LOG.error("Not contained in XMI: " + it.next());
        }
    }

} /* end class ProjectMemberModel */
