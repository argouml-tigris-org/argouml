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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectFactory;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectMember;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.ProjectMemberTodoList;
import org.argouml.util.ThreadUtils;
import org.xml.sax.InputSource;

/**
 * To persist to and from XMI file storage.
 *
 * @author Bob Tarling
 */
class XmiFilePersister extends AbstractFilePersister
    implements XmiExtensionParser {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(XmiFilePersister.class.getName());

    private List<String> pgmlStrings = new ArrayList<String>();

    private String todoString;

    private String argoString;

    /**
     * The constructor.
     */
    public XmiFilePersister() {
    }

    /*
     * @see org.argouml.persistence.AbstractFilePersister#getExtension()
     */
    public String getExtension() {
        return "xmi";
    }

    /*
     * @see org.argouml.persistence.AbstractFilePersister#getDesc()
     */
    protected String getDesc() {
        return Translator.localize("combobox.filefilter.xmi");
    }

    /*
     * @see org.argouml.persistence.AbstractFilePersister#isSaveEnabled()
     */
    public boolean isSaveEnabled() {
        return false;
    }

    /**
     * Save a project to a file in XMI format.
     *
     * @param project the project to save.
     * @param file The file to write.
     * @throws SaveException if anything goes wrong.
     * @throws InterruptedException     if the thread is interrupted
     */
    public void doSave(Project project, File file)
        throws SaveException, InterruptedException {

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

        OutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            writeProject(project, stream, progressMgr);
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
        } catch (InterruptedException exc) {
            try {
                stream.close();
            } catch (IOException ex) { }
            throw exc;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Exception occured during save attempt", e);
            try {
                stream.close();
            } catch (IOException ex) { }

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
        progressMgr.nextPhase();
    }

    /**
     * Write the output for a project on the given stream.
     *
     * @param project The project to output.
     * @param stream The stream to write to.
     * @param progressMgr The progress manager/monitor, if any.  If null, no
     *                    progress will be reported.
     * @throws SaveException If something goes wrong.
     * @throws InterruptedException     if the thread is interrupted
     */
    void writeProject(Project project,
            OutputStream stream,
            ProgressMgr progressMgr) throws SaveException,
            InterruptedException {

        int size = project.getMembers().size();
        for (int i = 0; i < size; i++) {
            ProjectMember projectMember =
                project.getMembers().get(i);
            if (projectMember.getType().equalsIgnoreCase(getExtension())) {
                if (LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO, "Saving member of type: {0}",
                            projectMember.getType());
                }
                MemberFilePersister persister = new ModelMemberFilePersister();
                persister.save(projectMember, stream);
            }
        }

        if (progressMgr != null) {
            progressMgr.nextPhase();
        }

    }


    /**
     * This method creates a project from the specified URL
     *
     * Unlike the constructor which forces an .argo extension This
     * method will attempt to load a raw XMI file
     *
     * This method can fail in several different ways. Either by
     * throwing an exception or by having the
     * ArgoParser.SINGLETON.getLastLoadStatus() set to not true.
     *
     * @param file The file to load the project from.
     * @return The newly loaded project.
     * @throws OpenException if the file can not be opened
     * @throws InterruptedException     if the thread is interrupted
     *
     * @see org.argouml.persistence.ProjectFilePersister#doLoad(java.io.File)
     */
    public Project doLoad(File file)
        throws OpenException, InterruptedException {

        LOG.log(Level.INFO, "Loading with XMIFilePersister");

        try {
            Project p = ProjectFactory.getInstance().createProject();


            long length = file.length();
            long phaseSpace = 100000;
            int phases = (int) (length / phaseSpace);
            if (phases < 10) {
                phaseSpace = length / 10;
                phases = 10;
            }
            LOG.log(Level.INFO, "File length is " + length + " phase space is "
                    + phaseSpace + " phases is " + phases);
            ProgressMgr progressMgr = new ProgressMgr();
            progressMgr.setNumberOfPhases(phases);
            ThreadUtils.checkIfInterrupted();

            InputSource source = new InputSource(new XmiInputStream(file
                    .toURI().toURL().openStream(), this, phaseSpace,
                    progressMgr));
            source.setSystemId(file.toURI().toURL().toString());

            ModelMemberFilePersister modelPersister =
                new ModelMemberFilePersister();

            modelPersister.readModels(source);
            Object model = modelPersister.getCurModel();
            progressMgr.nextPhase();
            Model.getUmlHelper().addListenersToModel(model);
            p.setUUIDRefs(modelPersister.getUUIDRefs());
            // TODO Handle multiple top level packages
            p.addMember(model);
            parseXmiExtensions(p);
            modelPersister.registerDiagrams(p);

            p.setRoot(model);
            p.setRoots(modelPersister.getElementsRead());
            p.updateRoots();
            File defaultProjectFile = new File(file.getPath() + ".zargo");
            // Make sure the file doesn't exist so the user will
            // get prompted to choose a new name
            for (int i = 0; i < 99; i++) {
                if (!defaultProjectFile.exists()) {
                    break;
                }
                defaultProjectFile =
                        new File(file.getPath() + "." + i + ".zargo");
            }
            PersistenceManager.getInstance().setProjectURI(
                    defaultProjectFile.toURI(), p);
            progressMgr.nextPhase();
            ProjectManager.getManager().setSaveEnabled(false);
            return p;
        } catch (IOException e) {
            throw new OpenException(e);
        }
    }

    /**
     * Returns true. All Argo specific files have an icon.
     *
     * @see org.argouml.persistence.AbstractFilePersister#hasAnIcon()
     */
    public boolean hasAnIcon() {
        return true;
    }

    /**
     * Parse a string of XML that is the XMI.extension contents.
     * This implementation simply stores the xml strings to process
     * in one hit after all the standard XMI has been read.
     * @see org.argouml.persistence.XmiExtensionParser#parse(java.lang.String, java.lang.String)
     */
    public void parse(String label, String xmiExtensionString) {
        if (label.equals("pgml")) {
            pgmlStrings.add(xmiExtensionString);
        } else if (label.equals("argo")) {
            argoString = xmiExtensionString;
        } else if (label.equals("todo")) {
            todoString = xmiExtensionString;
        }
    }

    /**
     * Parse all the extensions that were found when reading XMI
     *
     * @param project
     * @exception OpenException
     */
    public void parseXmiExtensions(Project project) throws OpenException {

        if (argoString != null) {
            LOG.log(Level.INFO, "Parsing argoString {0}", argoString.length());

            StringReader inputStream = new StringReader(argoString);
            ArgoParser parser = new ArgoParser();
            try {
                parser.readProject(project, inputStream);
            } catch (Exception e) {
                throw new OpenException("Exception caught", e);
            }
        } else {
            project.addMember(new ProjectMemberTodoList("", project));
        }
        for (String pgml : pgmlStrings) {
            LOG.log(Level.INFO, "Parsing pgml {0}", pgml.length());

            InputStream inputStream = new ByteArrayInputStream(pgml.getBytes());
            MemberFilePersister persister =
            // TODO: Cyclic dependency between PersistanceManager and here
                PersistenceManager.getInstance()
                        .getDiagramMemberFilePersister();
            // possibly use the following instead
//            MemberFilePersister persister = new DiagramMemberFilePersister();
            persister.load(project, inputStream);
        }
        if (todoString != null) {
            LOG.log(Level.INFO, "Parsing todoString {0}", todoString.length());

            InputStream inputStream =
                new ByteArrayInputStream(todoString.getBytes());
            MemberFilePersister persister = null;
            persister = new TodoListMemberFilePersister();
            persister.load(project, inputStream);
        } else {
            project.addMember(new ProjectMemberTodoList("", project));
        }
    }
}
