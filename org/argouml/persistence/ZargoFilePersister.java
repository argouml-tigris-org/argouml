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

package org.argouml.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.argouml.application.ArgoVersion;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.util.FileConstants;
import org.argouml.util.SubInputStream;
import org.tigris.gef.ocl.OCLExpander;
import org.tigris.gef.ocl.TemplateReader;

/**
 * To persist to and from zargo (zipped file) storage.
 *
 * @author Bob Tarling
 */
public class ZargoFilePersister extends UmlFilePersister {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ZargoFilePersister.class);

    /**
     * This is the old version of the ArgoUML tee file which
     * does not contain the detail of member elements.
     */
    private static final String ARGO_MINI_TEE =
        "/org/argouml/persistence/argo.tee";

    /**
     * The constructor.
     */
    public ZargoFilePersister() {
    }

    /**
     * @see org.argouml.persistence.AbstractFilePersister#getExtension()
     */
    public String getExtension() {
        return "zargo";
    }

    /**
     * @see org.argouml.persistence.AbstractFilePersister#getDesc()
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
     * @see org.argouml.persistence.ProjectFilePersister#save(
     * org.argouml.kernel.Project, java.io.File)
     */
    public void doSave(Project project, File file)
        throws SaveException {

        // frank: first backup the existing file to name+"#"
        File tempFile = new File(file.getAbsolutePath() + "#");
        File backupFile = new File(file.getAbsolutePath() + "~");
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

            Hashtable templates =
                TemplateReader.getInstance().read(ARGO_MINI_TEE);
            OCLExpander expander = new OCLExpander(templates);
            expander.expand(writer, project, "", "");

            writer.flush();

            stream.closeEntry();

            Collection names = new ArrayList();
            int counter = 0;
            int size = project.getMembers().size();
            for (int i = 0; i < size; i++) {
                ProjectMember projectMember =
                    (ProjectMember) project.getMembers().elementAt(i);
                if (LOG.isInfoEnabled()) {
                    LOG.info("Saving member: "
                            + ((ProjectMember) project.getMembers()
                                    .elementAt(i)).getName());
                }
                String name = projectMember.getName();
                if (!projectMember.getType().equalsIgnoreCase("xmi")) {
                    String originalName = name;
                    while (names.contains(name)) {
                        name = ++counter + originalName;
                    }
                    names.add(name);
                }
                stream.putNextEntry(new ZipEntry(name));
                projectMember.save(writer, null);
                writer.flush();
                stream.closeEntry();
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
            } catch (IOException ex) {
                // Do nothing.
            }

            // frank: in case of exception
            // delete name and mv name+"#" back to name if name+"#" exists
            // this is the "rollback" to old file
            file.delete();
            tempFile.renameTo(file);
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
     * @see org.argouml.persistence.ProjectFilePersister#doLoad(java.net.URL)
     */
    public Project doLoad(URL url) throws OpenException {
        try {
            File file = File.createTempFile("xxx", ".uml");
            file.deleteOnExit();

            String encoding = "UTF-8";
            FileOutputStream stream =
                new FileOutputStream(file);
            PrintWriter writer =
                new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        stream, encoding)));

            writer.println("<?xml version = \"1.0\" "
                    + "encoding = \"" + encoding + "\" ?>");

            // first read the .argo file from Zip
            ZipInputStream zis;
            String line;
            BufferedReader reader;

            zis = openZipStreamAt(url, FileConstants.PROJECT_FILE_EXT);
            reader = new BufferedReader(new InputStreamReader(zis));
            // Skip 2 lines
            reader.readLine();
            reader.readLine();

            String firstLine = reader.readLine();
            // TODO: take the version attribute from first
            // line if it's there.
            // An unknown version becomes version 1.
            writer.println("<uml version=\"2\">");
            writer.println(firstLine);
            while ((line = reader.readLine()) != null) {
                writer.println(line);
            }
            zis.close();
            reader.close();

            // then the xmi
            zis = openZipStreamAt(url, ".xmi");
            reader = new BufferedReader(new InputStreamReader(zis));
            // Skip 1 lines
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                writer.println(line);
            }
            zis.close();
            reader.close();

            // Loop round loading the diagrams
            zis = new ZipInputStream(url.openStream());
            SubInputStream sub = new SubInputStream(zis);

            ZipEntry currentEntry = null;
            while ((currentEntry = sub.getNextEntry()) != null) {
                if (currentEntry.getName().endsWith(".pgml")
                        || currentEntry.getName().endsWith(".todo")) {

                    reader = new BufferedReader(new InputStreamReader(sub));
                    // Skip 2 lines
                    reader.readLine();
                    reader.readLine();
                    while ((line = reader.readLine()) != null) {
                        writer.println(line);
                    }
                    sub.close();
                    reader.close();
                }
            }
            zis.close();

            writer.println("</uml>");
            writer.close();
            Project p = super.doLoad(file.toURL());
            p.setURL(url);
            return p;
        } catch (IOException e) {
            throw new OpenException(e);
        }
    }

    /**
     * Open a ZipInputStream to the first file found with
     * a given extension.
     *
     * @param url The URL of the zip file.
     * @param ext The required extension.
     * @return the zip stream positioned at the required location.
     * @throws IOException if there is a problem opening the file.
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
}
