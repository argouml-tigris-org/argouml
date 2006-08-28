// $Id$
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.argouml.application.ArgoVersion;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.uml.ProjectMemberModel;
import org.argouml.uml.cognitive.ProjectMemberTodoList;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.argouml.util.ThreadUtils;
import org.tigris.gef.ocl.ExpansionException;
import org.tigris.gef.ocl.OCLExpander;
import org.tigris.gef.ocl.TemplateReader;

/**
 * To persist to and from argo (xml file) storage.
 *
 * @author Bob Tarling
 */
class UmlFilePersister extends AbstractFilePersister {

    /**
     * The PERSISTENCE_VERSION is increased every time the persistence format
     * changes.
     * This controls conversion of old persistence version files to be
     * converted to the current one, keeping ArgoUML backwards compatible.
     */
    protected static final int PERSISTENCE_VERSION = 5;
    
    /**
     * The TOTAL_PHASES_LOAD constant is the number of phases used by the load
     * process.
     */
    protected static final int UML_PHASES_LOAD = 2;
    
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(UmlFilePersister.class);

    private static final String ARGO_TEE =
	"/org/argouml/persistence/argo2.tee";

    /**
     * The constructor.
     */
    public UmlFilePersister() {
    }

    /**
     * @see org.argouml.persistence.AbstractFilePersister#getExtension()
     */
    public String getExtension() {
        return "uml";
    }

    /**
     * @see org.argouml.persistence.AbstractFilePersister#getDesc()
     */
    protected String getDesc() {
        return Translator.localize("combobox.filefilter.uml");
    }

    /**
     * It is being considered to save out individual
     * xmi's from individuals diagrams to make
     * it easier to modularize the output of Argo.
     *
     * @param file The file to write.
     * @param project the project to save
     * @throws SaveException when anything goes wrong
     * @throws InterruptedException     if the thread is interrupted
     *
     * @see org.argouml.persistence.ProjectFilePersister#save(
     * org.argouml.kernel.Project, java.io.File)
     */
    public void doSave(Project project, File file)
        throws SaveException, InterruptedException {

        ProgressMgr progressMgr = new ProgressMgr();
        progressMgr.setNumberOfPhases(4);
        progressMgr.nextPhase();
        
        File lastArchiveFile = new File(file.getAbsolutePath() + "~");
        File tempFile = null;

        try {
            tempFile = createTempFile(file);
        } catch (FileNotFoundException e) {
            throw new SaveException(
                    "Failed to archive the previous file version", e);
        } catch (IOException e) {
            throw new SaveException(
                    "Failed to archive the previous file version", e);
        }

        try {
            project.setFile(file);
            project.setVersion(ArgoVersion.getVersion());
            project.setPersistenceVersion(PERSISTENCE_VERSION);

            FileOutputStream stream =
                new FileOutputStream(file);

            writeProject(project, stream, progressMgr);

            stream.close();

            progressMgr.nextPhase();

            String path = file.getParent();
            if (LOG.isInfoEnabled()) {
                LOG.info("Dir ==" + path);
            }

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
            
            progressMgr.nextPhase();
            
        } catch (Exception e) {
            LOG.error("Exception occured during save attempt", e);

            // frank: in case of exception
            // delete name and mv name+"#" back to name if name+"#" exists
            // this is the "rollback" to old file
            file.delete();
            tempFile.renameTo(file);
            if (e instanceof InterruptedException) {
                throw (InterruptedException) e;
            } else {
                // we have to give a message to user and set the system 
                // to unsaved!
                throw new SaveException(e);
            }
        }
    }
    
    /**
     * The .uml save format is no longer available to save
     * @see org.argouml.persistence.AbstractFilePersister#isSaveEnabled()
     */
    public boolean isSaveEnabled() {
        return false;
    }

    /**
     * Write the output for a project on the given stream.
     *
     * @param project The project to output.
     * @param stream The stream to write to.
     * @throws SaveException If something goes wrong.
     * @throws InterruptedException     if the thread is interrupted
     */
    void writeProject(Project project, 
            OutputStream stream, 
            ProgressMgr progressMgr) throws SaveException, 
            InterruptedException {
        OutputStreamWriter outputStreamWriter;
        try {
            outputStreamWriter = new OutputStreamWriter(stream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new SaveException(e);
        }
        PrintWriter writer =
            new PrintWriter(new BufferedWriter(outputStreamWriter));

        try {
            Integer indent = new Integer(4);

            writer.println("<?xml version = \"1.0\" "
                    + "encoding = \"" 
                    + PersistenceManager.getEncoding() + "\" ?>");
            writer.println("<uml version=\"" + PERSISTENCE_VERSION + "\">");
            // Write out header section
            try {
                Hashtable templates =
                    TemplateReader.getInstance().read(ARGO_TEE);
                OCLExpander expander = new OCLExpander(templates);
                expander.expand(writer, project, "  ");
            } catch (ExpansionException e) {
                throw new SaveException(e);
            }

            // Write out XMI section first
            int size = project.getMembers().size();
            for (int i = 0; i < size; i++) {
                ProjectMember projectMember =
                    (ProjectMember) project.getMembers().get(i);
                if (projectMember.getType().equalsIgnoreCase("xmi")) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Saving member of type: "
                              + ((ProjectMember) project.getMembers()
                                    .get(i)).getType());
                    }
                    MemberFilePersister persister
                        = getMemberFilePersister(projectMember);
                    persister.save(projectMember, writer, indent);
                }
            }

            writer.println("</uml>");

            writer.flush();
        } finally {
            writer.close();
        }
    }

    /**
     * @see org.argouml.persistence.ProjectFilePersister#doLoad(java.io.File)
     */
    public Project doLoad(File file) throws OpenException, 
    InterruptedException {
        // let's initialize the progressMgr
        ProgressMgr progressMgr = new ProgressMgr();
        progressMgr.setNumberOfPhases(UML_PHASES_LOAD);
        
        ThreadUtils.checkIfInterrupted();
        return doLoad(file, file, progressMgr);
    }

    /**
     * @see org.argouml.persistence.ProjectFilePersister#doLoad(java.io.File)
     */
    public Project doLoad(File originalFile, File file, ProgressMgr progressMgr)
        throws OpenException, InterruptedException {

        XmlInputStream inputStream = null;
        try {
            Project p = new Project(file.toURI());
            
            // Run through any stylesheet upgrades
            int fileVersion = getPersistenceVersionFromFile(file);

            // If we're trying to load a file from a future version
            // complain and refuse.
            if (fileVersion > PERSISTENCE_VERSION) {
                String release = getReleaseVersionFromFile(file);
                throw new VersionException(
                    "The file selected is from a more up to date version of "
                    + "ArgoUML. It has been saved with ArgoUML version "
                    + release
                    + ". Please load with that or a more up to date"
                    + "release of ArgoUML");
            }

            // If we're about to upgrade the file lets take an archive
            // of it first.
            if (fileVersion < PERSISTENCE_VERSION) {
                String release = getReleaseVersionFromFile(file);
                copyFile(
                    originalFile,
                    new File(originalFile.getAbsolutePath() + '~' + release));
            }
           
            LOG.info("Loading uml file of version " + fileVersion);
            while (fileVersion < PERSISTENCE_VERSION) {
                ++fileVersion;
                LOG.info("Upgrading to version " + fileVersion);
                long startTime = System.currentTimeMillis();
                file = transform(file, fileVersion);
                long endTime = System.currentTimeMillis();
                LOG.info("Upgrading took "
                        + ((endTime - startTime) / 1000)
                        + " seconds");
            }
            progressMgr.nextPhase();

            inputStream = new XmlInputStream(
                        file.toURL().openStream(),
                        "argo",
                        file.length(),
                        100000);

            ArgoParser parser = new ArgoParser();
            parser.readProject(p, inputStream);
            
            List memberList = parser.getMemberList();

            LOG.info(memberList.size() + " members");

            for (int i = 0; i < memberList.size(); ++i) {
                MemberFilePersister persister
                    = getMemberFilePersister((String) memberList.get(i));
                LOG.info("Loading member with "
                        + persister.getClass().getName());
                inputStream.reopen(persister.getMainTag());
                persister.load(p, inputStream);
            }
            
            // let's update the progress
            progressMgr.nextPhase();
            ThreadUtils.checkIfInterrupted();
            inputStream.realClose();
            p.postLoad();
            return p;
        } catch (InterruptedException e) {
            throw e;
        } catch (OpenException e) {
            throw e;
        } catch (Exception e) {
            throw new OpenException(e);
        }
    }


    
    /**
     * Transform a string of XML data according to the service required.
     *
     * @param file The XML file to be transformed
     * @param version the version of the persistence format
     *                the XML is to be transformed to.
     * @return the transformed XML file
     * @throws OpenException on XSLT transformation error or file read
     */
    public final File transform(File file, int version)
        throws OpenException {

        try {
            String upgradeFilesPath = "/org/argouml/persistence/upgrades/";
            String upgradeFile = "upgrade" + version + ".xsl";

            String xsltFileName = upgradeFilesPath + upgradeFile;
            URL xsltUrl = UmlFilePersister.class.getResource(xsltFileName);
            LOG.info("Resource is " + xsltUrl);

            // Read xsltStream into a temporary file
            // Get url for temp file.
            // openStream from url and wrap in StreamSource
            StreamSource xsltStreamSource =
                new StreamSource(xsltUrl.openStream());
            xsltStreamSource.setSystemId(xsltUrl.toExternalForm());

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xsltStreamSource);

            File transformedFile =
                File.createTempFile("upgrade_" + version + "_", ".uml");
            transformedFile.deleteOnExit();

            FileOutputStream stream =
                new FileOutputStream(transformedFile);
            Writer writer =
                new BufferedWriter(new OutputStreamWriter(stream, 
                        PersistenceManager.getEncoding()));
            Result result = new StreamResult(writer);

            StreamSource inputStreamSource = new StreamSource(file);
            inputStreamSource.setSystemId(file);
            transformer.transform(inputStreamSource, result);

            writer.close();
            return transformedFile;
        } catch (IOException e) {
            throw new OpenException(e);
        } catch (TransformerException e) {
            throw new OpenException(e);
        }
    }

    /**
     * Reads an XML file of uml format and extracts the
     * persistence version number from the root tag.
     *
     * @param file the XML file
     * @return The version number
     * @throws OpenException on any error
     */
    private int getPersistenceVersionFromFile(File file) throws OpenException {
        BufferedInputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = new BufferedInputStream(file.toURL().openStream());
            reader = new BufferedReader(new InputStreamReader(inputStream, 
                    PersistenceManager.getEncoding()));
            String rootLine = reader.readLine();
            while (!rootLine.startsWith("<uml ")) {
                rootLine = reader.readLine();
                if (rootLine == null) {
                    throw new OpenException(
                            "Failed to find the root <uml> tag");
                }
            }
            return Integer.parseInt(getVersion(rootLine));
        } catch (IOException e) {
            throw new OpenException(e);
        } catch (NumberFormatException e) {
            throw new OpenException(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // No more we can do here on failure
            }
        }
    }


    /**
     * Reads an XML file of uml format and extracts the
     * persistence version number from the root tag.
     *
     * @param file the XML file
     * @return The ArgoUML release number
     * @throws OpenException on any error
     */
    private String getReleaseVersionFromFile(File file) throws OpenException {
        BufferedInputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = new BufferedInputStream(file.toURL().openStream());
            reader = new BufferedReader(new InputStreamReader(inputStream, 
                    PersistenceManager.getEncoding()));
            String versionLine = reader.readLine();
            while (!versionLine.trim().startsWith("<version>")) {
                versionLine = reader.readLine();
                if (versionLine == null) {
                    throw new OpenException(
                            "Failed to find the release <version> tag");
                }
            }
            versionLine = versionLine.trim();
            int end = versionLine.lastIndexOf("</version>");
            return versionLine.trim().substring(9, end);
        } catch (IOException e) {
            throw new OpenException(e);
        } catch (NumberFormatException e) {
            throw new OpenException(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // No more we can do here on failure
            }
        }
    }

    /**
     * Get the version attribute value from a string of XML.
     * @param rootLine the line
     * @return the version
     */
    protected String getVersion(String rootLine) {
        String version;
        int versionPos = rootLine.indexOf("version=\"");
        if (versionPos > 0) {
            int startPos = versionPos + 9;
            int endPos = rootLine.indexOf("\"", startPos);
            version = rootLine.substring(startPos, endPos);
        } else {
            version = "1";
        }
        return version;
    }

    /**
     * Get a MemberFilePersister based on a given ProjectMember.
     *
     * @param pm the project member
     * @return the persister
     */
    protected MemberFilePersister getMemberFilePersister(ProjectMember pm) {
        MemberFilePersister persister = null;
        if (pm instanceof ProjectMemberDiagram) {
            persister =
		PersistenceManager.getInstance()
		        .getDiagramMemberFilePersister();
        } else if (pm instanceof ProjectMemberTodoList) {
            persister = new TodoListMemberFilePersister();
        } else if (pm instanceof ProjectMemberModel) {
            persister = new ModelMemberFilePersister();
        }
        return persister;
    }

    /**
     * Get a MemberFilePersister based on a given ProjectMember.
     *
     * @param tag The tag.
     * @return the persister
     */
    private MemberFilePersister getMemberFilePersister(String tag) {
        MemberFilePersister persister = null;
        if (tag.equals("pgml")) {
            persister =
		PersistenceManager.getInstance()
                        .getDiagramMemberFilePersister();
        } else if (tag.equals("todo")) {
            persister = new TodoListMemberFilePersister();
        } else if (tag.equals("xmi")) {
            persister = new ModelMemberFilePersister();
        }
        return persister;
    }

    /**
     * Returns true. All Argo specific files have an icon.
     * 
     * @see org.argouml.persistence.AbstractFilePersister#hasAnIcon()
     */
    public boolean hasAnIcon() {
        return true;
    }
}
