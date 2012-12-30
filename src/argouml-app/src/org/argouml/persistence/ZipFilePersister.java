/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectFactory;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectMember;
import org.argouml.model.Model;
import org.xml.sax.InputSource;

/**
 * To persist to and from zipped xmi file storage.
 *
 * @author Bob Tarling
 * @author Ludovic Ma&icirc;tre
 */
class ZipFilePersister extends XmiFilePersister {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ZipFilePersister.class.getName());

    /**
     * The constructor.
     */
    public ZipFilePersister() {
    }

    /*
     * @see org.argouml.persistence.AbstractFilePersister#getExtension()
     */
    public String getExtension() {
        return "zip";
    }

    /*
     * @see org.argouml.persistence.AbstractFilePersister#getDesc()
     */
    protected String getDesc() {
        return Translator.localize("combobox.filefilter.zip");
    }

    /*
     * @see org.argouml.persistence.XmiFilePersister#isSaveEnabled()
     */
    public boolean isSaveEnabled() {
        return true;
    }

    /**
     * Save the project in ZIP format.
     *
     * @param file
     *            The file to write.
     * @param project
     *            the project to save
     * @throws SaveException
     *             when anything goes wrong
     *
     * @see org.argouml.persistence.ProjectFilePersister#save(
     *      org.argouml.kernel.Project, java.io.File)
     */
    public void doSave(Project project, File file) throws SaveException {

        LOG.log(Level.INFO, "Receiving file {0}", file.getName());

        /* Retain the previous project file even when the save operation
         * crashes in the middle. Also create a backup file after saving. */
        boolean doSafeSaves = useSafeSaves();

        File lastArchiveFile = new File(file.getAbsolutePath() + "~");
        File tempFile = null;

        if (doSafeSaves) {
            try {
                tempFile = createTempFile(file);
            } catch (FileNotFoundException e) {
                throw new SaveException(Translator.localize(
                        "optionpane.save-project-exception-cause1"), e);
            } catch (IOException e) {
                throw new SaveException(Translator.localize(
                        "optionpane.save-project-exception-cause2"), e);
            }
        }

        OutputStream bufferedStream = null;
        try {
            //project.setFile(file);

            ZipOutputStream stream =
                new ZipOutputStream(new FileOutputStream(file));
            String fileName = file.getName();
            ZipEntry xmiEntry =
                new ZipEntry(fileName.substring(0, fileName.lastIndexOf(".")));
            stream.putNextEntry(xmiEntry);
            bufferedStream = new BufferedOutputStream(stream);

            int size = project.getMembers().size();
            for (int i = 0; i < size; i++) {
                ProjectMember projectMember =
                    project.getMembers().get(i);
                if (projectMember.getType().equalsIgnoreCase("xmi")) {
                    LOG.log(Level.FINE,
                            "Saving member of type: {0}",
                            projectMember.getType());

                    MemberFilePersister persister
                        = new ModelMemberFilePersister();
                    persister.save(projectMember, bufferedStream);
                }
            }
            stream.close();

            if (doSafeSaves) {
                // if save did not raise an exception
                // and name+"#" exists move name+"#" to name+"~"
                // this is the correct backup file
                if (lastArchiveFile.exists()) {
                    lastArchiveFile.delete();
                }
                if (tempFile.exists() && !lastArchiveFile.exists()) {
                    tempFile.renameTo(lastArchiveFile);
                }
                if (tempFile.exists()) {
                    tempFile.delete();
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Exception occured during save attempt", e);
            try {
                bufferedStream.close();
            } catch (IOException ex) {
                // If we get a 2nd error, just ignore it
            }

            if (doSafeSaves) {
                // frank: in case of exception
                // delete name and mv name+"#" back to name if name+"#" exists
                // this is the "rollback" to old file
                file.delete();
                tempFile.renameTo(file);
            }
            // we have to give a message to user and set the system to unsaved!
            throw new SaveException(e);
        }

        try {
            bufferedStream.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Failed to close save output writer", ex);
        }
    }

    /*
     * @see org.argouml.persistence.ProjectFilePersister#doLoad(java.io.File)
     */
    public Project doLoad(File file)
        throws OpenException {

        LOG.log(Level.INFO, "Receiving file {0}", file.getName());

        try {
            Project p = ProjectFactory.getInstance().createProject();
            String fileName = file.getName();
            String extension =
                fileName.substring(
                        fileName.indexOf('.'),
                        fileName.lastIndexOf('.'));
            InputStream stream = openZipStreamAt(file.toURI().toURL(),
                    extension);

            // TODO: What progressMgr is to be used here? Where does
            //       it come from?
            InputSource is =
                new InputSource(
                    new XmiInputStream(stream, this, 100000, null));
            is.setSystemId(file.toURI().toURL().toExternalForm());

            ModelMemberFilePersister modelPersister =
                new ModelMemberFilePersister();

            modelPersister.readModels(is);
            // TODO Handle multiple top level packages
            Object model = modelPersister.getCurModel();
            Model.getUmlHelper().addListenersToModel(model);
            p.setUUIDRefs(modelPersister.getUUIDRefs());
            p.addMember(model);
            parseXmiExtensions(p);
            modelPersister.registerDiagrams(p);

            p.setRoot(model);
            p.setRoots(modelPersister.getElementsRead());
            ProjectManager.getManager().setSaveEnabled(false);
            return p;
        } catch (IOException e) {
            throw new OpenException(e);
        }

    }

    /**
     * Open a ZipInputStream to the first file found with a given extension.
     *
     * @param url
     *            The URL of the zip file.
     * @param ext
     *            The required extension.
     * @return the zip stream positioned at the required location.
     * @throws IOException
     *             if there is a problem opening the file.
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
     * Returns false. Only Argo specific files have an icon.
     *
     * @see org.argouml.persistence.AbstractFilePersister#hasAnIcon()
     */
    @Override
    public boolean hasAnIcon() {
        return false;
    }
}
