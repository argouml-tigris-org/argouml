// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.xml.argo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.argouml.cognitive.ProjectMemberTodoList;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.model.uml.UmlHelper;
import org.argouml.uml.ProjectMemberModel;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.argouml.xml.SAXParserBase;
import org.argouml.xml.XMLElement;
import org.argouml.xml.xmi.XMIReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @stereotype singleton
 */
public class ArgoParser extends SAXParserBase {

    /** logger */
    private static final Logger LOG = Logger.getLogger(ArgoParser.class);

    ////////////////////////////////////////////////////////////////
    // static variables

    /**
     * The singleton for this class.
     */
    public static final ArgoParser SINGLETON = new ArgoParser();

    ////////////////////////////////////////////////////////////////
    // instance variables

    private Project project;

    private ArgoTokenTable tokens = new ArgoTokenTable();

    private boolean addMembers = true;

    private URL url;

    private boolean lastLoadStatus = true;

    private String lastLoadMessage;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     * 
     */
    protected ArgoParser() {
        super();
    }

    ////////////////////////////////////////////////////////////////
    // main parsing methods

    /**
     * @param theUrl the url of the project to read
     * @throws IOException for a file problem
     * @throws ParserConfigurationException in case of a parser problem
     * @throws SAXException when parsing xml
     */
    public void readProject(URL theUrl) throws IOException,
            ParserConfigurationException, SAXException {
        
        if (theUrl == null) {
            throw new IllegalArgumentException("A URL must be supplied");
        }
        url = theUrl;
        addMembers = true;
        readProject();
    }

    /**
     * @param theUrl the url of the project to read
     * @param addTheMembers true if the members are to be added
     * @throws IOException for a file problem
     * @throws ParserConfigurationException in case of a parser problem
     * @throws SAXException when parsing xml
     */
    public void readProject(URL theUrl, boolean addTheMembers)
        throws IOException, ParserConfigurationException, SAXException {
        
        if (theUrl == null) {
            throw new IllegalArgumentException("A URL must be supplied");
        }
        url = theUrl;
        addMembers = addTheMembers;
        readProject();
    }

    /**
     * @param theUrl the url
     */
    public void setURL(URL theUrl) {
        this.url = theUrl;
    }

    /**
     * @throws IOException for a file problem
     * @throws ParserConfigurationException in case of a parser problem
     * @throws SAXException when parsing xml
     */
    private void readProject()
        throws IOException, SAXException, ParserConfigurationException {

        InputStream is = url.openStream();
        lastLoadStatus = true;
        lastLoadMessage = "OK";

        try {
            LOG.info("=======================================");
            LOG.info("== READING PROJECT " + url);
            project = new Project(url);
            parse(is);
        } catch (SAXException e) {
            lastLoadStatus = false;
            LOG.error("Exception reading project================");
            LOG.error(is.toString());
            lastLoadMessage = e.toString();
            throw e;
        } catch (IOException e) {
            lastLoadStatus = false;
            LOG.error("Exception reading project================");
            LOG.error(is.toString());
            lastLoadMessage = e.toString();
            throw e;
        } catch (ParserConfigurationException e) {
            lastLoadStatus = false;
            LOG.error("Exception reading project================");
            LOG.error(is.toString());
            lastLoadMessage = e.toString();
            throw e;
        }
    }

    /**
     * Get the project to which the URL is to be parsed.
     * @return the project
     */
    public Project getProject() {
        return project;
    }

    /**
     * Set the project to which the URL is to be parsed.
     * @param newProj the project
     */
    public void setProject(Project newProj) {
        project = newProj;
    }

    /**
     * @see org.argouml.xml.SAXParserBase#handleStartElement(org.argouml.xml.XMLElement)
     */
    public void handleStartElement(XMLElement e) throws SAXException {
        if (DBG) {
            LOG.debug("NOTE: ArgoParser handleStartTag:" + e.getName());
        }
        switch (tokens.toToken(e.getName(), true)) {
        case ArgoTokenTable.TOKEN_ARGO:
            handleArgo(e);
            break;
        case ArgoTokenTable.TOKEN_DOCUMENTATION:
            handleDocumentation(e);
            break;
        default:
            if (DBG) {
                LOG.warn("WARNING: unknown tag:" + e.getName());
            }
            break;
        }
    }

    /**
     * @see org.argouml.xml.SAXParserBase#handleEndElement(org.argouml.xml.XMLElement)
     */
    public void handleEndElement(XMLElement e) throws SAXException {
        if (DBG) {
            LOG.debug("NOTE: ArgoParser handleEndTag:" + e.getName() + ".");
        }
        switch (tokens.toToken(e.getName(), false)) {
        case ArgoTokenTable.TOKEN_AUTHORNAME:
            handleAuthorname(e);
            break;
        case ArgoTokenTable.TOKEN_VERSION:
            handleVersion(e);
            break;
        case ArgoTokenTable.TOKEN_DESCRIPTION:
            handleDescription(e);
            break;
        case ArgoTokenTable.TOKEN_SEARCHPATH:
            handleSearchpath(e);
            break;
        case ArgoTokenTable.TOKEN_MEMBER:
            handleMember(e);
            break;
        case ArgoTokenTable.TOKEN_HISTORYFILE:
            handleHistoryfile(e);
            break;
        default:
            if (DBG) {
                LOG.warn("WARNING: unknown end tag:" + e.getName());
            }
        }
    }
    
    /**
     * @see org.argouml.xml.SAXParserBase#isElementOfInterest(java.util.String)
     */
    protected boolean isElementOfInterest(String name) {
        return tokens.contains(name);
    }

    /**
     * @param e the element
     */
    protected void handleArgo(XMLElement e) {
        /* do nothing */
    }

    /**
     * @param e the element
     */
    protected void handleDocumentation(XMLElement e) {
        /* do nothing */
    }

    /**
     * @param e the element
     */
    protected void handleAuthorname(XMLElement e) {
        String authorname = e.getText().trim();
        project.setAuthorname(authorname);
    }

    /**
     * @param e the element
     */
    protected void handleVersion(XMLElement e) {
        String version = e.getText().trim();
        project.setVersion(version);
    }

    /**
     * @param e the element
     */
    protected void handleDescription(XMLElement e) {
        String description = e.getText().trim();
        project.setDescription(description);
    }

    /**
     * @param e the element
     */
    protected void handleSearchpath(XMLElement e) {
        String searchpath = e.getAttribute("href").trim();
        project.addSearchPath(searchpath);
    }

    /**
     * @param e the element
     * @throws SAXException on any error parsing the member XML.
     */
    protected void handleMember(XMLElement e) throws SAXException {
        if (addMembers) {
            loadProjectMember(e);
        }
    }

    /**
     * Add a member to this project.
     * 
     * @param name The name of the member.
     * @param type The type of the member. 
     *         One of <tt>"pgml"</tt>, <tt>"xmi"</tt> or <tt>"todo"</tt>.
     */
    private void createProjectMember(String name, String type)
        throws SAXException {
        
        ProjectMember pm = project.findMemberByName(name);
        if (pm != null) {
            return;
        }
        if ("pgml".equals(type)) {
            pm = new ProjectMemberDiagram(name, project);
        } else if ("xmi".equals(type)) {
            pm = new ProjectMemberModel(name, project);
        } else if ("todo".equals(type)) {
            pm = new ProjectMemberTodoList(name, project);
        } else {
            throw new IllegalArgumentException("Unknown member type " + type);
        }
    
        project.addMember(pm);
    }

    
    
    /**
     * @param e the element
     */
    protected void handleHistoryfile(XMLElement e) {
        if (e.getAttribute("name") == null)
            return;
        String historyfile = e.getAttribute("name").trim();
        project.setHistoryFile(historyfile);
    }

    /**
     * @return the status of the last load attempt. Used for junit tests.
     */
    public boolean getLastLoadStatus() {
        return lastLoadStatus;
    }

    /**
     * Set the status of the last load attempt. Used for junit tests.
     *
     * @param status the status of the last load attempt
     */
    public void setLastLoadStatus(boolean status) {
        lastLoadStatus = status;
    }

    /**
     * Get the last message which caused loading to fail. Used for junit tests.
     *
     * @return the last message which caused loading to fail
     */
    public String getLastLoadMessage() {
        return lastLoadMessage;
    }

    /**
     * Set the last load message. Used for junit tests.
     *
     * @param msg the last load message
     */
    public void setLastLoadMessage(String msg) {
        lastLoadMessage = msg;
    }

    /**
     * @param project the project to load into
     * @param theUrl the URL to load from
     * @throws OpenException if opening the URL fails
     */
    private void loadProjectMember(XMLElement element) 
        throws SAXException {

        String name = element.getAttribute("name").trim();
        String type = element.getAttribute("type").trim();
        
        if (type.equals("xmi")) {
            try {
                loadModel(openStreamAtXmi(url));
            } catch (IOException e) {
                throw new SAXException(e);
            }
        }
            
            
            
            
            
//        if (type.equals("xmi")) {
//            String filename = url.toString();
//            filename = filename.substring(0, filename.length() - 4) + "xmi";
//            try {
//                loadModel(new URL(filename));
//            } catch (IOException e) {
//                throw new SAXException(e);
//            }
//        }
        
        
        
        
        
//            String xslt = 
//                "<xsl:stylesheet version='1.0' " +
//                        "xmlns:xsl='http://www.w3.org/1999/XSL/Transform' " +
//                        "xmlns:e4='http://csc/e4/Envelope'> " +
//                    "<xsl:output method='xml' indent='yes'/> " +
//                    "<xsl:template match='argo/member'> " +
//                        "<xsl:copy> " +
//                            "<xsl:apply-templates select='@*|node()' /> " +
//                        "</xsl:copy> " +
//                    "</xsl:template> " +
//                "</xsl:stylesheet> ";
//            Source xsltSource = new StreamSource(new StringReader(xslt));
//            Source inputSource = new StreamSource(theUrl.openStream());
//
//            Transformer transformer = 
//                TransformerFactory.newInstance().newTransformer(xsltSource);
//            
//            PipedReader pipedReader = new PipedReader();
//            Writer pipedWriter = new PipedWriter(pipedReader);
//            StreamResult result = new StreamResult(pipedWriter);
//            
//            transformer.transform(inputSource, result);
//            
//            InputSource is = new InputSource(pipedReader);
//            loadModel(project, is);
            
    }

    /**
     * Opens the input stream of a URL and positions
     * it at the start of the XMI.
     * This is a first draft of this method.
     * Work still in progress (Bob Tarling).
     */
    private InputStream openStreamAtXmi(URL theUrl) throws IOException {
        InputStream is = url.openStream();
        BufferedInputStream bis = null;
        if (is instanceof BufferedInputStream) {
            bis = (BufferedInputStream) is;
        } else {
            bis = new BufferedInputStream(is);
        }
        readUntil(bis, "<member type=\"xmi\"");
        while (bis.read() != '>');
        return bis;
    }

    /**
     * Keep on reading an input stream until a specific
     * sequence of characters has ben read.
     * This method assumes there is at least one match.
     * @param is the input stream.
     * @param search the characters to search for.
     * @throws IOException
     */
    private void readUntil(InputStream is, String search) throws IOException {
        char[] searchChars = search.toCharArray();
        int i;
        boolean found;
        while (true) {
            // Keep reading till we get the first character.
            while (is.read() != searchChars[0]);
            found = true;
            // Compare each following character
            for (i = 1; i < search.length(); ++i) {
                if (is.read() != searchChars[i]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return;
            }
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
     */
    private Object loadModel(URL theUrl)
        throws SAXException, IOException {
        if (LOG.isInfoEnabled()) {
            LOG.info("Loading Model from " + theUrl);
        }
        InputStream is = theUrl.openStream();
        return loadModel(is);
    }

    /**
     * Loads a model (XMI only) from an input source. BE ADVISED this
     * method has a side effect. It sets _UUIDREFS to the model.
     * 
     * If there is a problem with the xmi file, an error is set in the
     * getLastLoadStatus() field. This needs to be examined by the
     * calling function.
     *
     * @return The model loaded
     * @throws SAXException If the parser template is syntactically incorrect. 
     * @param is the input stream to load from
     */
    private Object loadModel(InputStream is) throws SAXException {
        
        InputSource source = new InputSource(is);
        Object mmodel = null;

        // 2002-07-18
        // Jaap Branderhorst
        // changed the loading of the projectfiles to solve hanging 
        // of argouml if a project is corrupted. Issue 913
        // Created xmireader with method getErrors to check if parsing went well
        XMIReader xmiReader = null;
        try {
            xmiReader = new XMIReader();
            source.setEncoding("UTF-8");
            mmodel = xmiReader.parseToModel(source);        
        } catch (SAXException e) { // duh, this must be caught and handled
            LOG.error("SAXException caught", e);
            throw e;
        } catch (ParserConfigurationException e) { 
            LOG.error("ParserConfigurationException caught", e);
            throw new SAXException(e);
        } catch (IOException e) {
            LOG.error("IOException caught", e);
            throw new SAXException(e);
        }

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
} /* end class ArgoParser */
