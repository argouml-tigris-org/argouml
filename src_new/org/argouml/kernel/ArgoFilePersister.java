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
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.argouml.application.ArgoVersion;
import org.argouml.model.uml.UmlHelper;
import org.argouml.xml.argo.ArgoParser;
import org.argouml.xml.xmi.XMIReader;
import org.tigris.gef.ocl.ExpansionException;
import org.tigris.gef.ocl.OCLExpander;
import org.tigris.gef.ocl.TemplateReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * To persist to and from argo (xml file) storage.
 * 
 * @author Bob Tarling
 */
public class ArgoFilePersister extends AbstractFilePersister {
    
    private static final Logger LOG = 
        Logger.getLogger(ArgoFilePersister.class);
    
    private static final String ARGO_TEE = "/org/argouml/xml/dtd/argo2.tee";

    /**
     * The constructor.
     *  
     */
    public ArgoFilePersister() {
    }
    
    /**
     * @see org.argouml.kernel.AbstractFilePersister#getExtension()
     */
    public String getExtension() {
        return "argo";
    }
    
    /**
     * @see org.argouml.kernel.AbstractFilePersister#getDesc()
     */
    protected String getDesc() {
        return "Argo project file";
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
        
        Writer writer = null;
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
            writer =
                new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        stream, "UTF-8")));
    
            try {
                Hashtable templates = TemplateReader.readFile(ARGO_TEE);
                OCLExpander expander = new OCLExpander(templates);
                expander.expand(writer, project, "", "");
            } catch (ExpansionException e) {
                throw new SaveException(e);
            }
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
                InputStream is = url.openStream();

                // the "true" means that members should be added.
                ArgoParser.SINGLETON.setURL(url);
                ArgoParser.SINGLETON.readProject(is, false);
                p = ArgoParser.SINGLETON.getProject();
                ArgoParser.SINGLETON.setProject(null); // clear up project refs

                is.close();
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
    
    protected void loadProjectMembers(Project project, URL theUrl) throws OpenException {
        try {
            String filename = theUrl.toString();
            filename = filename.substring(0,filename.length()-4) + "xmi";
            theUrl = new URL(filename);
            loadModel(project, theUrl);
        } catch (SAXException e) {
            throw new OpenException(e);
        } catch (IOException e) {
            throw new OpenException(e);
        } catch (ParserConfigurationException e) {
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
        InputStream is = theUrl.openStream();
        InputSource source = new InputSource(is);
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
     * @param theUrl The url with the .zargo file
     * @return The model loaded
     * @throws IOException Thrown if the model or the .zargo file is corrupted.
     * @throws SAXException If the parser template is syntactically incorrect. 
     * @throws ParserConfigurationException If the initialization of 
     *         the parser fails.
     */
    protected Object loadModel(Project project, InputSource source)
            throws IOException, SAXException, ParserConfigurationException {
        // 2002-07-18
        // Jaap Branderhorst
        // changed the loading of the projectfiles to solve hanging 
        // of argouml if a project is corrupted. Issue 913
        // Created xmireader with method getErrors to check if parsing went well
        XMIReader xmiReader = null;
        try {
            xmiReader = new XMIReader();
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
            ArgoParser.SINGLETON.setLastLoadStatus(false);
            ArgoParser.SINGLETON.setLastLoadMessage(
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
