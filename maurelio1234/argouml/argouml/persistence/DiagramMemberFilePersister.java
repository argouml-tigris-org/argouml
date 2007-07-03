// $Id: DiagramMemberFilePersister.java 12780 2007-06-08 07:50:15Z tfmorris $
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

package org.argouml.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.ocl.ExpansionException;
import org.tigris.gef.ocl.OCLExpander;
import org.tigris.gef.ocl.TemplateReader;

/**
 * The file persister for the diagram members.
 * @author Bob Tarling
 */
class DiagramMemberFilePersister extends MemberFilePersister {
    /**
     * The tee file for persistence.
     */
    private static final String PGML_TEE = "/org/argouml/persistence/PGML.tee";

    @Override
    public void load(Project project, InputStream inputStream)
        throws OpenException {

        // If the model repository doesn't manage a DI model
        // then we must generate our Figs by inspecting PGML
        try {
            // Give the parser a map of model elements
            // keyed by their UUID. This is used to allocate
            // figs to their owner using the "href" attribute
            // in PGML.
            PGMLStackParser parser = new PGMLStackParser(project.getUUIDRefs());
            Diagram d = parser.readDiagram(inputStream, false);
            inputStream.close();
            project.addMember(d);
        } catch (Exception e) {
            if (e instanceof OpenException) {
                throw (OpenException) e;
            }
            throw new OpenException(e);
        }
    }


    @Override
    public String getMainTag() {
        return "pgml";
    }


    @Override
    @Deprecated
    public void save(ProjectMember member, Writer writer, boolean xmlFragment)
    	throws SaveException {

        ProjectMemberDiagram diagramMember = (ProjectMemberDiagram) member;
        OCLExpander expander;
        try {
            expander =
                new OCLExpander(TemplateReader.getInstance().read(PGML_TEE));
        } catch (ExpansionException e) {
            throw new SaveException(e);
        }
        if (!xmlFragment) {
            try {
                expander.expand(writer, diagramMember.getDiagram());
            } catch (ExpansionException e) {
                throw new SaveException(e);
            }
        } else {
            try {
                File tempFile = File.createTempFile("pgml", null);
                tempFile.deleteOnExit();
                FileWriter w = new FileWriter(tempFile);
                expander.expand(w, diagramMember.getDiagram());
                w.close();
                addXmlFileToWriter((PrintWriter) writer, tempFile);
            } catch (ExpansionException e) {
                throw new SaveException(e);
            } catch (IOException e) {
                throw new SaveException(e);
            }
        }
    }
    

    @Override
    public void save(ProjectMember member, OutputStream outStream)
        throws SaveException {

        ProjectMemberDiagram diagramMember = (ProjectMemberDiagram) member;
        OCLExpander expander;
        try {
            expander =
                    new OCLExpander(
                            TemplateReader.getInstance().read(PGML_TEE));
        } catch (ExpansionException e) {
            throw new SaveException(e);
        }
        PrintWriter pw = new PrintWriter(outStream);
        try {
            // WARNING: the OutputStream version of this doesn't work! - tfm
            expander.expand(pw, diagramMember.getDiagram());
        } catch (ExpansionException e) {
            throw new SaveException(e);
        } finally {
            pw.flush();
        }
        
    }
}
