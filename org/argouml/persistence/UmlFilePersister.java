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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.argouml.application.ArgoVersion;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
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
    
    private static final Logger LOG = 
        Logger.getLogger(UmlFilePersister.class);
    
    private static final String ARGO_TEE = "/org/argouml/persistence/argo2.tee";

    /**
     * The constructor.
     *  
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
        File tempFile = new File( file.getAbsolutePath() + "#");
        File backupFile = new File( file.getAbsolutePath() + "~");
        if (tempFile.exists()) {
            tempFile.delete();
        }
        
        PrintWriter writer = null;
        try {
            if (file.exists()) {
                copyFile(tempFile, file);
            }
            // frank end
    
            project.setFile(file);
            project.setVersion(ArgoVersion.getVersion());
            project.setPersistenceVersion(PERSISTENCE_VERSION);

            String encoding = "UTF-8";
            FileOutputStream stream =
                new FileOutputStream(file);
            writer =
                new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        stream, encoding)));
            
            Integer indent = new Integer(4);
            
            writer.println("<?xml version = \"1.0\" encoding = \"" + encoding + "\" ?>");
            writer.println("<uml version=\"" + PERSISTENCE_VERSION + "\">");
            // Write out header section
            try {
                Hashtable templates = TemplateReader.getInstance()
                    .read(ARGO_TEE);
                OCLExpander expander = new OCLExpander(templates);
                expander.expand(writer, project, "  ", "");
                //expander.expand(writer, project, "  "); //For next version of GEF
            } catch (ExpansionException e) {
                throw new SaveException(e);
            }

            // Write out non xmi sections
            int size = project.getMembers().size();
            for (int i = 0; i < size; i++) {
                ProjectMember projectMember = 
                    (ProjectMember) project.getMembers().elementAt(i);
                if (projectMember.getType().equalsIgnoreCase("xmi")) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Saving member of type: "
                              + ((ProjectMember) project.getMembers()
                                    .elementAt(i)).getType());
                    }
                    projectMember.save(writer, indent);
                }
            }
            
            for (int i = 0; i < size; i++) {
                ProjectMember projectMember = 
                    (ProjectMember) project.getMembers().elementAt(i);
                if (!projectMember.getType().equalsIgnoreCase("xmi")) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Saving member of type: "
                              + ((ProjectMember) project.getMembers()
                                    .elementAt(i)).getType());
                    }
                    projectMember.save(writer, indent);
                }
            }
            
            writer.println("</uml>");
            
            writer.flush();
            
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
            writer.close();
            
            // frank: in case of exception 
            // delete name and mv name+"#" back to name if name+"#" exists
            // this is the "rollback" to old file
            file.delete();
            tempFile.renameTo( file);
            // we have to give a message to user and set the system to unsaved!
            throw new SaveException(e);
        }

        writer.close();
    }
    
    /**
     * @see org.argouml.persistence.ProjectFilePersister#loadProject(java.net.URL)
     */
    public Project doLoad(URL url) throws OpenException {
        try {
            // First scan fist line of real XML to get version number
            
            // If version < PERSISTENCE_VERSION call stylesheets and return final url
            
            XmlInputStream inputStream =
                        new XmlInputStream(url.openStream(), "argo");

            ArgoParser parser = new ArgoParser();
            Project p = new Project(url);
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
    
    /**
     * Transform a string of XML data according to the service required
     * @param xml The original XML
     * @param xslt the XSLT transformation
     * @return the transformed XML
     * @throws TransformerException on XSLT transformation error
     */
    public static final String transform(String xml, String xslt)
            throws TransformerException {

        StreamSource xsltStreamSource = new StreamSource(new StringReader(xslt));
        return transform(xml, xsltStreamSource);
    }
    
    /**
     * Transform a string of XML data according to the service required
     * @param xml The original XML
     * @param xsltStreamSource the transformation stream
     * @return the transformed XML
     * @throws TransformerException on XSLT transformation error
     */
    public static final String transform(String xml, StreamSource xsltStreamSource)
            throws TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsltStreamSource);
        Source input = new StreamSource(new StringReader(xml));

        StringWriter writer = new StringWriter();
        Result result = new StreamResult(writer);

        transformer.transform(input, result);
        String transformation = writer.toString();
        return transformation;
    }
}
