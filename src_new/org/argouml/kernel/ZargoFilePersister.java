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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.argouml.application.ArgoVersion;
import org.argouml.model.uml.UmlHelper;
import org.argouml.model.uml.XmiReader;
import org.argouml.ui.ArgoDiagram;
import org.argouml.uml.cognitive.ProjectMemberTodoList;
import org.argouml.util.FileConstants;
import org.argouml.util.SubInputStream;
import org.argouml.xml.argo.ArgoParser;
import org.argouml.xml.pgml.PGMLParser;
import org.tigris.gef.ocl.OCLExpander;
import org.tigris.gef.ocl.TemplateReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * To persist to and from zargo (zipped file) storage.
 * 
 * @author Bob Tarling
 */
public class ZargoFilePersister extends AbstractFilePersister {
    
    private static final Logger LOG = 
        Logger.getLogger(ZargoFilePersister.class);
    
    /**
     * This is the old version of the ArgoUML tee file which
     * does not contain the detail of member elements.
     */
    private static final String ARGO_MINI_TEE =
        "/org/argouml/xml/dtd/argo.tee";
    
    /**
     * The constructor.
     */
    public ZargoFilePersister() {
    }

    /**
     * @see org.argouml.kernel.AbstractFilePersister#getExtension()
     */
    public String getExtension() {
        return "zargo";
    }
    
    /**
     * @see org.argouml.kernel.AbstractFilePersister#getDesc()
     */
    protected String getDesc() {
        return "ArgoUML compressed project file";
    }
    
    /**
     * It is being considered to save out individual
     * xmi's from individuals diagrams to make
     * it easier to modularize the output of Argo.
     * 
     * @param file The file to write.
     * @param project the project to save
     * @throws SaveException when anything goes wrong
     *
     * @see org.argouml.kernel.ProjectFilePersister#save(
     * org.argouml.kernel.Project, java.io.File)
     */
    public void doSave(Project project, File file)
        throws SaveException {
        
        // frank: first backup the existing file to name+"#"
        File tempFile = new File( file.getAbsolutePath() + "#");
        File backupFile = new File( file.getAbsolutePath() + "~");
        if (tempFile.exists()) {
            tempFile.delete();
        }
        
        BufferedWriter writer = null;
        try {
            if (file.exists()) {
                copyFile(tempFile, file);
            }
            // frank end
    
            project.setFile(file);
            project.setVersion(ArgoVersion.getVersion());
            project.setPersistenceVersion(PERSISTENCE_VERSION);

            ZipOutputStream stream =
                new ZipOutputStream(new FileOutputStream(file));
            writer =
                new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
    
            ZipEntry zipEntry =
                new ZipEntry(project.getBaseName() 
                        + FileConstants.UNCOMPRESSED_FILE_EXT);
            stream.putNextEntry(zipEntry);
            
            Hashtable templates = TemplateReader.getInstance()
                .read(ARGO_MINI_TEE);
            OCLExpander expander = new OCLExpander(templates);
            expander.expand(writer, project, "", "");
                
            writer.flush();
            
            stream.closeEntry();
    
            // First we save all objects that are not XMI objects i.e. the
            // diagrams (first for loop).
            // Then we save all XMI objects (second for loop).
            // This is because order is important on saving.
            // Bob - Why not do it the other way around? Surely
            // when reloading it is better to load XMI first
            // then the diagrams.
            Collection names = new ArrayList();
            int counter = 0;  
            int size = project.getMembers().size();
            for (int i = 0; i < size; i++) {
                ProjectMember projectMember = 
                    (ProjectMember) project.getMembers().elementAt(i);
                if (!(projectMember.getType().equalsIgnoreCase("xmi"))) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Saving member: "
                              + ((ProjectMember) project.getMembers()
                                    .elementAt(i)).getName());
                    }
                    String name = projectMember.getName();
                    String originalName = name;
                    while (names.contains(name)) {
                        name = ++counter + originalName;
                    }
                    names.add(name);
                    stream.putNextEntry(new ZipEntry(name));
                    projectMember.save(writer, null);
                    writer.flush();
                    stream.closeEntry();
                }
            }

            for (int i = 0; i < size; i++) {
                ProjectMember projectMember = 
                    (ProjectMember) project.getMembers().elementAt(i);
                if (projectMember.getType().equalsIgnoreCase("xmi")) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Saving member of type: "
                              + ((ProjectMember) project.getMembers()
                                    .elementAt(i)).getType());
                    }
                    stream.putNextEntry(new ZipEntry(projectMember.getName()));
                    projectMember.save(writer, null);
                }
            }
            
            // if save did not raise an exception 
            // and name+"#" exists move name+"#" to name+"~"
            // this is the correct backup file
            if (backupFile.exists()) {
                backupFile.delete();
            }
            if (tempFile.exists() && !backupFile.exists()) {
                tempFile.renameTo(backupFile);
            }
            if (tempFile.exists()) {
                tempFile.delete();
            }
        } catch (Exception e) {
            LOG.error("Exception occured during save attempt", e);
            try {
                writer.close();
            } catch (IOException ex) { }
            
            // frank: in case of exception 
            // delete name and mv name+"#" back to name if name+"#" exists
            // this is the "rollback" to old file
            file.delete();
            tempFile.renameTo( file);
            // we have to give a message to user and set the system to unsaved!
            throw new SaveException(e);
        }

        try {
            writer.close();
        } catch (IOException ex) {
            LOG.error("Failed to close save output writer", ex);
        }
    }

    /**
     * @see org.argouml.kernel.ProjectFilePersister#loadProject(java.net.URL)
     */
    public Project loadProject(URL url) throws OpenException {
        try {
            Project p = null;
            // read the argo
            try {
                // first read the .argo file from Zip
                ZipInputStream zis =
                    openZipStreamAt(url, FileConstants.PROJECT_FILE_EXT);

                // the "false" means that members should not be added,
                // we want to do this by hand from the zipped stream.
                ArgoParser parser = new ArgoParser();
                parser.readProject(url, zis);
                p = parser.getProject();
                // clear up project refs:
                parser.setProject(null); 
                zis.close();
            } catch (IOException e) {
                // exception can occur both due to argouml code as to J2SE
                // code, so lets log it
                LOG.error(e);
                throw e;
            }
            loadProjectMembers(p, url);
            p.postLoad();
            return p;
        } catch (IOException e) {
            throw new OpenException(e);
        } catch (SAXException e) {
            throw new OpenException(e);
        } catch (ParserConfigurationException e) {
            throw new OpenException(e);
        }
    }
    
    /**
     * Open a ZipInputStream to the first file found with
     * a given extension.
     * @param url The URL of the zip file.
     * @param ext The required extension.
     * @return the zip stream positioned at the required location.
     */
    private ZipInputStream openZipStreamAt(URL url, String ext)
        throws IOException {
        ZipInputStream zis = new ZipInputStream(url.openStream());
        ZipEntry entry = zis.getNextEntry();
        while (entry != null && !entry.getName().endsWith(ext)) {
            entry = zis.getNextEntry();
        }
        return zis;
    }
    
    /**
     * Loads all the members from a zipped input stream.
     *
     * @param theUrl The URL to the input stream.
     * @throws OpenException if there is something wrong with the zipped archive
     *                     or with the model.
     * @param project the project to load into
     */
    protected void loadProjectMembers(Project project, URL theUrl) 
        throws OpenException {

        try {
            loadModel(project, theUrl);

            // now close again, reopen and read the Diagrams.
            PGMLParser parser = new PGMLParser();
            parser.setOwnerRegistry(project.getUUIDRefs());

            //zis.close();
            ZipInputStream zis = new ZipInputStream(theUrl.openStream());
            SubInputStream sub = new SubInputStream(zis);

            ZipEntry currentEntry = null;
            while ((currentEntry = sub.getNextEntry()) != null) {
                if (currentEntry.getName().endsWith(".pgml")) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Now going to load "
                                 + currentEntry.getName()
                                 + " from ZipInputStream");
                    }

                    // "false" means the stream shall not be closed,
                    // but it doesn't seem to matter...
                    ArgoDiagram d =
                        (ArgoDiagram) parser.readDiagram(
                                      sub,
                                      false);
                    if (d != null) {
                        project.addMember(d);
                    }
                    else {
                        LOG.error("An error occurred while loading " 
                            + currentEntry.getName());
                    }
                    // sub.closeEntry();
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Finished loading " + currentEntry.getName());
                    }
                }
                if (currentEntry.getName().endsWith(".todo")) {
                    ProjectMemberTodoList pm =
                        new ProjectMemberTodoList(currentEntry.getName(), 
                                project);
                    pm.load(sub);
                    project.addMember(pm);
                }
            }
            zis.close();

        } catch (SAXException e) {
        throw new OpenException(e);
        } catch (ParserConfigurationException e) {
        throw new OpenException(e);
        } catch (IOException e) {
            LastLoadInfo.getInstance().setLastLoadStatus(false);
            LastLoadInfo.getInstance().setLastLoadMessage(e.toString());
            LOG.error("Failure in Project.loadProjectMembers()", e);
            throw new OpenException(e);
        }
    }
    
    /**
     * Loads a model (XMI only) from a .zargo file. BE ADVISED this
     * method has a side effect. It sets _UUIDREFS to the model.
     * 
     * If there is a problem with the xmi file, an error is set in the
     * ArgoParser.SINGLETON.getLastLoadStatus() field. This needs to be
     * examined by the calling function.
     *
     * @param theUrl The url with the .zargo file
     * @param project the project to load into
     * @return The model loaded
     * @throws IOException Thrown if the model or the .zargo file is corrupted.
     * @throws SAXException If the parser template is syntactically incorrect. 
     * @throws ParserConfigurationException If the initialization of 
     *         the parser fails.
     */
    protected Object loadModel(Project project, URL theUrl)
        throws IOException, SAXException, ParserConfigurationException {
        if (LOG.isInfoEnabled()) {
            LOG.info("Loading Model from " + theUrl);
        }
        ZipInputStream zis = openZipStreamAt(theUrl, ".xmi");
        InputSource source = new InputSource(zis);
        return loadModel(project, source);
    }

    /**
     * Loads a model (XMI only) from a .zargo file. BE ADVISED this
     * method has a side effect. It sets _UUIDREFS to the model.
     * 
     * If there is a problem with the xmi file, an error is set in the
     * ArgoParser.SINGLETON.getLastLoadStatus() field. This needs to be
     * examined by the calling function.
     *
     * @param project the project to load into
     * @param source the source to load from
     * @return The model loaded
     * @throws IOException Thrown if the model or the .zargo file is corrupted.
     * @throws SAXException If the parser template is syntactically incorrect. 
     * @throws ParserConfigurationException If the initialization of 
     *         the parser fails.
     *
     */
    protected Object loadModel(Project project, InputSource source)
        throws IOException, SAXException, ParserConfigurationException {
        // 2002-07-18
        // Jaap Branderhorst
        // changed the loading of the projectfiles to solve hanging 
        // of argouml if a project is corrupted. Issue 913
        // Created xmireader with method getErrors to check if parsing went well
        XmiReader xmiReader = null;
        try {
            xmiReader = new XmiReader();
        } catch (SAXException se) { // duh, this must be caught and handled
            LOG.error("SAXException caught", se);
            throw se;
        } catch (ParserConfigurationException pc) { 
            // duh, this must be caught and handled
            LOG.error("ParserConfigurationException caught", pc);
            throw pc;
        }
        Object mmodel = null;

        source.setEncoding("UTF-8");
        mmodel = xmiReader.parseToModel(source);        
        if (xmiReader.getErrors()) {
            LastLoadInfo.getInstance().setLastLoadStatus(false);
            LastLoadInfo.getInstance().setLastLoadMessage(
                    "XMI file could not be parsed.");
            LOG.error("XMI file could not be parsed.");
            throw new SAXException(
                    "XMI file could not be parsed.");
        }

        // This should probably be inside xmiReader.parse
        // but there is another place in this source
        // where XMIReader is used, but it appears to be
        // the NSUML XMIReader.  When Argo XMIReader is used
        // consistently, it can be responsible for loading
        // the listener.  Until then, do it here.
        UmlHelper.getHelper().addListenersToModel(mmodel);

        project.addMember(mmodel);

        project.setUUIDRefs(new HashMap(xmiReader.getXMIUUIDToObjectMap()));
        
        return mmodel;
    }
}
