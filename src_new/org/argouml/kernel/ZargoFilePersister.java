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
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.argouml.application.ArgoVersion;
import org.argouml.util.FileConstants;
import org.argouml.xml.argo.ArgoParser;
import org.tigris.gef.ocl.ExpansionException;
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
        return "Argo compressed project file";
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
    public void save(Project project, File file)
        throws SaveException {
        
        project.setFile(file);
        project.setVersion(ArgoVersion.getVersion());
        project.setPersistenceVersion(PERSISTENCE_VERSION);


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
    
            ZipOutputStream stream =
                new ZipOutputStream(new FileOutputStream(file));
            writer =
                new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
    
            ZipEntry zipEntry =
                new ZipEntry(project.getBaseName() 
                        + FileConstants.UNCOMPRESSED_FILE_EXT);
            stream.putNextEntry(zipEntry);
            expand(writer, project);
            writer.flush();
            
            stream.closeEntry();
    
            String path = file.getParent();
            if (LOG.isInfoEnabled()) {
                LOG.info("Dir ==" + path);
            }
            int size = project.getMembers().size();

            // First we save all objects that are not XMI objects i.e. the
            // diagrams (first for loop).
            // The we save all XMI objects (second for loop).
            // This is because order is important on saving.
            Collection names = new ArrayList();
            int counter = 0;  
            for (int i = 0; i < size; i++) {
                ProjectMember projectMember = 
                    (ProjectMember) project.getMembers().elementAt(i);
                if (!(projectMember.getType().equalsIgnoreCase("xmi"))) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Saving member: "
                              + ((ProjectMember) project.getMembers()
                                    .elementAt(i)).getURL());
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

    private void expand(Writer writer, Object project) throws SaveException {
        try {
            getExpander(getArgoTee2Template()).expand(writer, project, "", "");
        } catch (ExpansionException e) {
            throw new SaveException(e);
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
                ArgoParser.SINGLETON.setURL(url);
                ArgoParser.SINGLETON.readProject(zis, false);
                p = ArgoParser.SINGLETON.getProject();
                ArgoParser.SINGLETON.setProject(null); // clear up project refs

                zis.close();
            } catch (IOException e) {
                // exception can occur both due to argouml code as to J2SE
                // code, so lets log it
                LOG.error(e);
                throw e;
            }
            p.loadZippedProjectMembers(url);
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
        while (entry != null
                && !entry.getName().endsWith(FileConstants.PROJECT_FILE_EXT)) {
            entry = zis.getNextEntry();
        }
        return zis;
    }
}
