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

package org.argouml.uml.diagram;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.persistence.SaveException;
import org.argouml.ui.ArgoDiagram;
import org.tigris.gef.ocl.ExpansionException;
import org.tigris.gef.ocl.OCLExpander;
import org.tigris.gef.ocl.TemplateReader;
import org.tigris.gef.util.Util;

/**
 * @author Piotr Kaminski
 */
public class ProjectMemberDiagram extends ProjectMember {
    private static final Logger LOG = 
        Logger.getLogger(ProjectMemberDiagram.class);

    ////////////////////////////////////////////////////////////////
    // constants

    private static final String MEMBER_TYPE = "pgml";
    private static final String FILE_EXT = "." + MEMBER_TYPE;
    private static final String PGML_TEE = "/org/argouml/persistence/PGML.tee";

    ////////////////////////////////////////////////////////////////
    // instance variables

    private ArgoDiagram diagram;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     * 
     * @param name the name
     * @param p the project
     */
    public ProjectMemberDiagram(String name, Project p) {
        super(name, p);
    }

    /**
     * The constructor.
     * 
     * @param d the diagram
     * @param p the project
     */
    public ProjectMemberDiagram(ArgoDiagram d, Project p) {
        super(null, p);
        String s = Util.stripJunk(d.getName());
        setName(s);
        setDiagram(d);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @return the diagram
     */
    public ArgoDiagram getDiagram() {
        return diagram;
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

    /**
     * Write the diagram to the given writer.
     * @see org.argouml.kernel.ProjectMember#save(java.io.Writer, Integer)
     */
    public void save(Writer writer, Integer indent) throws SaveException {
        OCLExpander expander;
        try {
            expander = 
                new OCLExpander(TemplateReader.getInstance().read(PGML_TEE));
        } catch (FileNotFoundException e) {
            throw new SaveException(e);
        }
        if (indent == null) {
            try {
                expander.expand(writer, diagram, "", "");
            } catch (ExpansionException e) {
                throw new SaveException(e);
            }
        } else {
            try {
                File tempFile = File.createTempFile("pgml", null);
                tempFile.deleteOnExit();
                FileWriter w = new FileWriter(tempFile);
                expander.expand(w, diagram, "", "");
                w.close();
                addXmlFileToWriter(
                        (PrintWriter) writer,
                        tempFile,
                        indent.intValue());
            } catch (ExpansionException e) {
                throw new SaveException(e);
            } catch (IOException e) {
                throw new SaveException(e);
            }
        }
    }

    /**
     * @param d the diagram
     */
    protected void setDiagram(ArgoDiagram d) {
        diagram = d;
    }

} /* end class ProjectMemberDiagram */
