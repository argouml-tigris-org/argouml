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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.argouml.kernel.LastLoadInfo;
import org.argouml.kernel.Project;
import org.argouml.xml.SAXParserBase;
import org.argouml.xml.XMLElement;
import org.xml.sax.SAXException;

/**
 * @stereotype singleton
 */
public class ArgoParser extends SAXParserBase {

    /** logger */
    private static final Logger LOG = Logger.getLogger(ArgoParser.class);

    ////////////////////////////////////////////////////////////////
    // static variables

//    /**
//     * The singleton for this class.
//     */
//    private static final ArgoParser INSTANCE = new ArgoParser();
//
    ////////////////////////////////////////////////////////////////
    // instance variables

    private Project project;

    private ArgoTokenTable tokens = new ArgoTokenTable();

    private boolean addMembers = true;

    private URL url;
    private int diagramCount;

//    /**
//     * @return the singleton
//     */
//    public static final ArgoParser getInstance() {
//        return INSTANCE;   
//    }
    
    /**
     * The constructor.
     * 
     */
    public ArgoParser() {
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

        InputStream is = theUrl.openStream();
        readProject(theUrl, is, true);
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
        InputStream is = theUrl.openStream();

        readProject(theUrl, is, addTheMembers);
    }

    /**
     * @param theUrl the url of the project to read
     * @param is the inputStream
     * @param addTheMembers true if the members are to be added
     * @throws IOException for a file problem
     * @throws ParserConfigurationException in case of a parser problem
     * @throws SAXException when parsing xml
     */
    public void readProject(URL theUrl, InputStream is, boolean addTheMembers)
        throws IOException, SAXException, ParserConfigurationException {

        if (theUrl == null || is == null) {
            throw new IllegalArgumentException(
                    "A URL and an input stream must be supplied");
        }

        addMembers = addTheMembers;

        url = theUrl;

        LastLoadInfo.getInstance().setLastLoadMessage("OK");
        LastLoadInfo.getInstance().setLastLoadStatus(true);

        try {
            LOG.info("=======================================");
            LOG.info("== READING PROJECT " + url);
            project = new Project(url);
            parse(is);
        } catch (SAXException e) {
            LastLoadInfo.getInstance().setLastLoadStatus(false);
            LOG.error("Exception reading project================");
            LOG.error(is.toString());
            LastLoadInfo.getInstance().setLastLoadMessage(e.toString());
            throw e;
        } catch (IOException e) {
            LastLoadInfo.getInstance().setLastLoadStatus(false);
            LOG.error("Exception reading project================");
            LOG.error(is.toString());
            LastLoadInfo.getInstance().setLastLoadMessage(e.toString());
            throw e;
        } catch (ParserConfigurationException e) {
            LastLoadInfo.getInstance().setLastLoadStatus(false);
            LOG.error("Exception reading project================");
            LOG.error(is.toString());
            LastLoadInfo.getInstance().setLastLoadMessage(e.toString());
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
        case ArgoTokenTable.TOKEN_MEMBER:
            handleMember(e);
            break;
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
        LOG.info("Handle member");
        if (e.getAttribute("type").equals("pgml")) {
            ++diagramCount;
        }
        if (addMembers) {
            loadProjectMember(e);
        }
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
     * @param project the project to load into
     * @param theUrl the URL to load from
     * @throws OpenException if opening the URL fails
     */
    private void loadProjectMember(XMLElement element) 
        throws SAXException {

        String name = element.getAttribute("name").trim();
        String type = element.getAttribute("type").trim();
        
        MemberFilePersister memberParser = null;
        
        if (type.equals("xmi")) {
            LOG.info("Creating XML loader");
            memberParser = new ModelMemberFilePersister(url, project);
        } else if (type.equals("pgml")) {
            LOG.info("Creating PGML loader");
            memberParser = new DiagramMemberFilePersister(url, project);
        } else if (type.equals("todo")) {
            LOG.info("Creating todo loader");
            memberParser = new TodoListMemberFilePersister(url, project);
        }
        LOG.info("Loading member");
        //memberParser.load(attributeMap);
        LOG.info("Member loaded");
    }

    /**
     * Get the numer of diagram members read.
     * @return the numer of diagram members read.
     */
    public int getDiagramCount() {
        return diagramCount;
    }
} /* end class ArgoParser */
