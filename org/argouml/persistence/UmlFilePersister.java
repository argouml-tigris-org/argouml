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
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.uml.ProjectMemberModel;
import org.argouml.uml.cognitive.ProjectMemberTodoList;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.tigris.gef.ocl.ExpansionException;
import org.tigris.gef.ocl.OCLExpander;
import org.tigris.gef.ocl.TemplateReader;
import org.xml.sax.SAXException;

/**
 * To persist to and from argo (xml file) storage.
 *
 * @author Bob Tarling
 */
public class UmlFilePersister extends AbstractFilePersister {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(UmlFilePersister.class);

    private static final String ARGO_TEE = "/org/argouml/persistence/argo2.tee";

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
        return "ArgoUML project file";
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

        try {
            if (file.exists()) {
                copyFile(tempFile, file);
            }
            // frank end

            project.setFile(file);
            project.setVersion(ArgoVersion.getVersion());
            project.setPersistenceVersion(PERSISTENCE_VERSION);

            FileOutputStream stream =
                new FileOutputStream(file);

            writeProject(project, stream);

            stream.close();

            String path = file.getParent();
            if (LOG.isInfoEnabled()) {
                LOG.info("Dir ==" + path);
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

            // frank: in case of exception
            // delete name and mv name+"#" back to name if name+"#" exists
            // this is the "rollback" to old file
            file.delete();
            tempFile.renameTo(file);
            // we have to give a message to user and set the system to unsaved!
            throw new SaveException(e);
        }
    }

    /**
     * Write the output for a project on the given stream.
     *
     * @param project The project to output.
     * @param stream The stream to write to.
     * @throws SaveException If something goes wrong.
     */
    void writeProject(Project project, OutputStream stream)
        throws SaveException {
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
                    + "encoding = \"" + "UTF-8" + "\" ?>");
            writer.println("<uml version=\"" + PERSISTENCE_VERSION + "\">");
            // Write out header section
            try {
                Hashtable templates =
                    TemplateReader.getInstance().read(ARGO_TEE);
                OCLExpander expander = new OCLExpander(templates);
                expander.expand(writer, project, "  ", "");
                // For next version of GEF:
                // expander.expand(writer, project, "  ");
            } catch (FileNotFoundException e) {
                throw new SaveException(e);
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
                    MemberFilePersister persister = null;
                    if (projectMember instanceof ProjectMemberDiagram) {
                        persister = new DiagramMemberFilePersister();
                    } else if (projectMember instanceof ProjectMemberTodoList) {
                        persister = new TodoListMemberFilePersister();
                    } else if (projectMember instanceof ProjectMemberModel) {
                        persister = new ModelMemberFilePersister();
                    }
                    persister.save(projectMember, writer, indent);
                }
            }

            // Write out all non-XMI sections
            for (int i = 0; i < size; i++) {
                ProjectMember projectMember =
                    (ProjectMember) project.getMembers().get(i);
                if (!projectMember.getType().equalsIgnoreCase("xmi")) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Saving member of type: "
                              + ((ProjectMember) project.getMembers().
                                    get(i)).getType());
                    }
                    MemberFilePersister persister = null;
                    if (projectMember instanceof ProjectMemberDiagram) {
                        persister = new DiagramMemberFilePersister();
                    } else if (projectMember instanceof ProjectMemberTodoList) {
                        persister = new TodoListMemberFilePersister();
                    } else if (projectMember instanceof ProjectMemberModel) {
                        persister = new ModelMemberFilePersister();
                    }
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
     * @see org.argouml.persistence.ProjectFilePersister#doLoad(java.net.URL)
     */
    public Project doLoad(URL url) throws OpenException {
        try {
            Project p = new Project(url);

            // Run through any stylesheet upgrades
            url = upgrade(url);
            
            XmlInputStream inputStream =
                        new XmlInputStream(url.openStream(), "argo");

            ArgoParser parser = new ArgoParser();
            parser.readProject(p, inputStream);

            List memberList = parser.getMemberList();

            LOG.info(memberList.size() + " members");

            MemberFilePersister persister = null;
            for (int i = 0; i < memberList.size(); ++i) {
                if (memberList.get(i).equals("pgml")) {
                    persister = new DiagramMemberFilePersister();
                } else if (memberList.get(i).equals("todo")) {
                    persister = new TodoListMemberFilePersister();
                } else if (memberList.get(i).equals("xmi")) {
                    persister = new ModelMemberFilePersister();
                }
                LOG.info("Loading member with "
                        + persister.getClass().getName());
                inputStream.reopen(persister.getMainTag());
                persister.load(p, inputStream);
            }
            inputStream.realClose();
            p.postLoad();
            return p;
        } catch (IOException e) {
            LOG.error("IOException", e);
            throw new OpenException(e);
        } catch (SAXException e) {
            LOG.error("SAXException", e);
            throw new OpenException(e);
        }
    }

    private URL upgrade(URL url) throws OpenException {
        try {
            int versionFromFile = Integer.parseInt(getVersion(url));
    
            LOG.info("Loading uml file of version " + versionFromFile);
            while (versionFromFile < PERSISTENCE_VERSION) {
                ++versionFromFile;
                LOG.info("Upgrading to version " + versionFromFile);
                url = transform(url, versionFromFile);
            }
            return url;
        } catch (IOException e) {
            throw new OpenException(e);
        }
    }
    
    /**
     * Transform a string of XML data according to the service required.
     *
     * @param url The URL of the XML to be transformed
     * @param version the version of the persistence format
     *                the XML is to be transformed to.
     * @return the URL of the transformed XML
     * @throws OpenException on XSLT transformation error or file read
     */
    public final URL transform(URL url, int version)
        throws OpenException {

        try {
            String upgradeFilesPath = "/org/argouml/persistence/upgrades/";
            String upgradeFile = "upgrade" + version + ".xsl";

            // TODO: But should instead access a resource inside the jar
            String xsltFileName = upgradeFilesPath + upgradeFile;
            URL xsltUrl = UmlFilePersister.class.getResource(xsltFileName);
            LOG.info("Resource is " + xsltUrl);

            // Read xsltStream into a temporary file
            // Get url for temp file.
            // openStream from url and wrap in StreamSource
            StreamSource xsltStreamSource =
                new StreamSource(xsltUrl.openStream());

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xsltStreamSource);

            File file = File.createTempFile("transformation", ".uml");
            file.deleteOnExit();

            String encoding = "UTF-8";
            FileOutputStream stream =
                new FileOutputStream(file);
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    stream, encoding));
            Result result = new StreamResult(writer);

            StreamSource inputStreamSource = new StreamSource(url.openStream());
            transformer.transform(inputStreamSource, result);

            writer.close();
            return file.toURL();
        } catch (IOException e) {
            throw new OpenException(e);
        } catch (TransformerException e) {
            throw new OpenException(e);
        }
    }

    /**
     * Read an XML file at the given URL and extracts the version number
     * from the root tag.
     * @param url the URL of the XML file
     * @return The version number
     * @throws IOException
     */
    private String getVersion(URL url) throws IOException {
        BufferedInputStream inputStream =
            new BufferedInputStream(url.openStream());
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(inputStream));
        String rootLine = reader.readLine();
        while (!rootLine.startsWith("<uml ")) {
            rootLine = reader.readLine();
        }
        inputStream.close();
        reader.close();
        return getVersion(rootLine);
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

}
