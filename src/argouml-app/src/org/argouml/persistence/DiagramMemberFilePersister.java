// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.tigris.gef.ocl.ExpansionException;
import org.tigris.gef.ocl.OCLExpander;
import org.tigris.gef.ocl.TemplateReader;

/**
 * The file persister for the diagram members.
 * @author Bob Tarling
 */
class DiagramMemberFilePersister extends MemberFilePersister {
    
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(DiagramMemberFilePersister.class);
    
    /**
     * The tee file for persistence.
     */
    private static final String PGML_TEE = "/org/argouml/persistence/PGML.tee";
    
    private static final Map<String, String> CLASS_TRANSLATIONS =
        new HashMap<String, String>();

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
            DiagramSettings defaultSettings = 
                project.getProjectSettings().getDefaultDiagramSettings();
            // TODO: We need the project specific diagram settings here
            PGMLStackParser parser = new PGMLStackParser(project.getUUIDRefs(),
                    defaultSettings);
            LOG.info("Adding translations registered by modules");
            for (Map.Entry<String, String> translation
                    : CLASS_TRANSLATIONS.entrySet()) {
                parser.addTranslation(
                        translation.getKey(),
                        translation.getValue());
            }
            ArgoDiagram d = parser.readArgoDiagram(inputStream, false);
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
    public void load(Project project, URL url) throws OpenException {   
        try {
            load(project, url.openStream());
        } catch (IOException e) {
            throw new OpenException(e);
        }
    }

    @Override
    public String getMainTag() {
        return "pgml";
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
        OutputStreamWriter outputWriter;
        try {
            outputWriter = 
                new OutputStreamWriter(outStream, Argo.getEncoding());
        } catch (UnsupportedEncodingException e1) {
            throw new SaveException("Bad encoding", e1);
        }
        
        try {
            // WARNING: the OutputStream version of this doesn't work! - tfm
            expander.expand(outputWriter, diagramMember.getDiagram());
        } catch (ExpansionException e) {
            throw new SaveException(e);
        } finally {
            try {
                outputWriter.flush();
            } catch (IOException e) {
                throw new SaveException(e);
            }
        }
        
    }

    /**
     * Figs are stored by class name and recreated by reflection. If the class
     * name changes or moves this provides a simple way of translating from
     * class name at time of save to the current class name without need for
     * XSL.
     * @param originalClassName
     * @param newClassName
     */
    public void addTranslation(
            final String originalClassName,
            final String newClassName) {
        CLASS_TRANSLATIONS.put(originalClassName, newClassName);
    }
}
