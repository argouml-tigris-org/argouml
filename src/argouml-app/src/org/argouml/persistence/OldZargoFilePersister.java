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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.argouml.application.helpers.ApplicationVersion;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.ocl.OCLExpander;
import org.argouml.util.FileConstants;
import org.tigris.gef.ocl.TemplateReader;

/**
 * To persist to and from zargo (zipped file) storage containing seperate
 * xmi, argo, plgml and todo zip entries.
 * @author Bob Tarling
 * TODO: Review use of this against ZargoFilePersister - Bob
 */
class OldZargoFilePersister extends ZargoFilePersister {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(OldZargoFilePersister.class.getName());

    /**
     * This is the old version of the ArgoUML tee file which does not contain
     * the detail of member elements.
     */
    private static final String ARGO_MINI_TEE =
        "/org/argouml/persistence/argo.tee";

    /**
     * The constructor.
     */
    public OldZargoFilePersister() {
    }

    /*
     * @see org.argouml.persistence.AbstractFilePersister#getDesc()
     */
    protected String getDesc() {
        return Translator.localize("combobox.filefilter.zargo");
    }

    /*
     * @see org.argouml.persistence.AbstractFilePersister#isSaveEnabled()
     */
    public boolean isSaveEnabled() {
        return true;
    }

    /**
     * Save the project in ".zargo" format.
     *
     * @param file
     *            The file to write.
     * @param project
     *            the project to save
     * @throws SaveException
     *             when anything goes wrong
     * @throws InterruptedException     if the thread is interrupted
     *
     * @see org.argouml.persistence.ProjectFilePersister#save(
     *      org.argouml.kernel.Project, java.io.File)
     */
    public void doSave(Project project, File file) throws SaveException,
    InterruptedException {

        /* Retain the previous project file even when the save operation
         * crashes in the middle. Also create a backup file after saving. */
        boolean doSafeSaves = useSafeSaves();

        ProgressMgr progressMgr = new ProgressMgr();
        progressMgr.setNumberOfPhases(4);
        progressMgr.nextPhase();

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

        BufferedWriter writer = null;
        try {

            project.setFile(file);
            project.setVersion(ApplicationVersion.getVersion());
            project.setPersistenceVersion(PERSISTENCE_VERSION);

            ZipOutputStream stream =
                new ZipOutputStream(new FileOutputStream(file));
            writer =
                new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));

            // Save the .argo entry
            // TODO: Cyclic dependency with PersistenceManager
            // move PersistenceManager..getProjectBaseName() someplace else
            ZipEntry zipEntry =
                new ZipEntry(PersistenceManager.getInstance()
                            .getProjectBaseName(project)
                        + FileConstants.UNCOMPRESSED_FILE_EXT);
            stream.putNextEntry(zipEntry);

            Hashtable templates =
                TemplateReader.getInstance().read(ARGO_MINI_TEE);
            OCLExpander expander = new OCLExpander(templates);
            expander.expand(writer, project);

            writer.flush();

            stream.closeEntry();

            int counter = 0;
            int size = project.getMembers().size();
            Collection<String> names = new ArrayList<String>();
            for (int i = 0; i < size; i++) {
                ProjectMember projectMember = project.getMembers().get(i);
                if (!(projectMember.getType().equalsIgnoreCase("xmi"))) {
                    
                    LOG.log(Level.INFO,
                            "Saving member: {0}", projectMember.getZipName());

                    String name = projectMember.getZipName();
                    String originalName = name;
                    while (names.contains(name)) {
                        /* Issue 4806 explains why we need this! */
                        name = ++counter + originalName;
                    }
                    names.add(name);
                    stream.putNextEntry(new ZipEntry(name));
                    MemberFilePersister persister =
                        getMemberFilePersister(projectMember);
                    persister.save(projectMember, stream);
                    stream.flush();
                    stream.closeEntry();
                }
            }

            for (int i = 0; i < size; i++) {
                ProjectMember projectMember = project.getMembers().get(i);
                if (projectMember.getType().equalsIgnoreCase("xmi")) {
                    
                    LOG.log(Level.INFO,
                            "Saving member of type: {0}",
                            projectMember.getType());

                    stream.putNextEntry(
                            new ZipEntry(projectMember.getZipName()));
                    OldModelMemberFilePersister persister =
                        new OldModelMemberFilePersister();
                    persister.save(projectMember, stream);
                    stream.flush();
                }
            }

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

            progressMgr.nextPhase();

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Exception occured during save attempt", e);
            try {
                writer.close();
            } catch (Exception ex) {
                // Do nothing.
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
            writer.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Failed to close save output writer", ex);
        }
    }

    /**
     * The .zargo save format is able to save. We must override
     * UmlFilePersister which has turned this off (suggests a need for some
     * refactoring here)
     * @see org.argouml.persistence.AbstractFilePersister#isSaveEnabled()
     *
     * @return boolean
     */
    public boolean isLoadEnabled() {
        return false;
    }
}
