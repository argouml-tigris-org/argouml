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

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectSettings;
import org.xml.sax.SAXException;



/**
 * @stereotype singleton
 */
class ArgoParser extends SAXParserBase {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ArgoParser.class);

    ////////////////////////////////////////////////////////////////
    // instance variables

    private Project project;
    private ProjectSettings ps;

    private ArgoTokenTable tokens = new ArgoTokenTable();

    private List memberList = new ArrayList();

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
     * @param theProject the project to populate
     * @param is the reader
     * @throws SAXException on error when parsing xml
     */
    public void readProject(Project theProject, Reader is)
    	throws SAXException {

        if (is == null) {
            throw new IllegalArgumentException(
                    "An input stream must be supplied");
        }

        PersistenceManager.getInstance().setLastLoadMessage("OK");
        PersistenceManager.getInstance().setLastLoadStatus(true);

        try {
            LOG.info("=======================================");
            LOG.info("== READING PROJECT " + theProject);
            project = theProject;
            ps = project.getProjectSettings();
            parse(is);
        } catch (SAXException e) {
            PersistenceManager.getInstance().setLastLoadStatus(false);
            LOG.error("Exception reading project================");
            LOG.error(is.toString());
            PersistenceManager.getInstance().setLastLoadMessage(e.toString());
            throw e;
        }
    }

    /**
     * Get the project to which the URI is to be parsed.
     * @return the project
     */
    public Project getProject() {
        return project;
    }

    /**
     * Set the project to which the URI is to be parsed.
     * @param newProj the project
     */
    public void setProject(Project newProj) {
        project = newProj;
        ps = project.getProjectSettings();
    }

    /*
     * @see org.argouml.persistence.SAXParserBase#handleStartElement(
     *         org.argouml.persistence.XMLElement)
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
        case ArgoTokenTable.TOKEN_SETTINGS:
            handleSettings(e);
            break;
        default:
            if (DBG) {
                LOG.warn("WARNING: unknown tag:" + e.getName());
            }
            break;
        }
    }

    /*
     * @see org.argouml.persistence.SAXParserBase#handleEndElement(
     *         org.argouml.persistence.XMLElement)
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
            handleAuthorName(e);
            break;
        case ArgoTokenTable.TOKEN_AUTHOREMAIL:
            handleAuthorEmail(e);
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
        case ArgoTokenTable.TOKEN_NOTATIONLANGUAGE:
            handleNotationLanguage(e);
            break;
        case ArgoTokenTable.TOKEN_SHOWBOLDNAMES:
            handleShowBoldNames(e);
            break;
        case ArgoTokenTable.TOKEN_USEGUILLEMOTS:
            handleUseGuillemots(e);
            break;
        case ArgoTokenTable.TOKEN_SHOWVISIBILITY:
            handleShowVisibility(e);
            break;
        case ArgoTokenTable.TOKEN_SHOWMULTIPLICITY:
            handleShowMultiplicity(e);
            break;
        case ArgoTokenTable.TOKEN_SHOWINITIALVALUE:
            handleShowInitialValue(e);
            break;
        case ArgoTokenTable.TOKEN_SHOWPROPERTIES:
            handleShowProperties(e);
            break;
        case ArgoTokenTable.TOKEN_SHOWTYPES:
            handleShowTypes(e);
            break;
        case ArgoTokenTable.TOKEN_SHOWSTEREOTYPES:
            handleShowStereotypes(e);
            break;
        case ArgoTokenTable.TOKEN_DEFAULTSHADOWWIDTH:
            handleDefaultShadowWidth(e);
            break;
        default:
            if (DBG) {
                LOG.warn("WARNING: unknown end tag:" + e.getName());
            }
            break;
        }
    }

    /*
     * @see org.argouml.persistence.SAXParserBase#isElementOfInterest(String)
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
    protected void handleSettings(XMLElement e) {
        /* do nothing */
    }

    /**
     * @param e the element
     */
    protected void handleAuthorName(XMLElement e) {
        String authorname = e.getText().trim();
        project.setAuthorname(authorname);
    }

    /**
     * @param e the element
     */
    protected void handleAuthorEmail(XMLElement e) {
        String authoremail = e.getText().trim();
        project.setAuthoremail(authoremail);
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
        if (e == null) {
            throw new SAXException("XML element is null");
        }
        String type = e.getAttribute("type");
        // The members list dictates the order in which the
        // members are loaded. So make sure that XMI is at the top
        // and others below.
        if (type.equals("xmi")) {
            memberList.add(0, type);
        } else {
            memberList.add(type);
        }
    }

    /**
     * @param e the element
     */
    protected void handleHistoryfile(XMLElement e) {
        if (e.getAttribute("name") == null) {
            return;
        }
        String historyfile = e.getAttribute("name").trim();
        project.setHistoryFile(historyfile);
    }
    
    /**
     * @param e the element
     */
    protected void handleNotationLanguage(XMLElement e) {
        String language = e.getText().trim();
        ps.setNotationLanguage(language);
    }

    /**
     * @param e the element
     */
    protected void handleShowBoldNames(XMLElement e) {
        String ug = e.getText().trim();
        ps.setShowBoldNames(ug);
    }

    /**
     * @param e the element
     */
    protected void handleUseGuillemots(XMLElement e) {
        String ug = e.getText().trim();
        ps.setUseGuillemots(ug);
    }

    /**
     * @param e the element
     */
    protected void handleShowVisibility(XMLElement e) {
        String showVisibility = e.getText().trim();
        ps.setShowVisibility(showVisibility);
    }

    /**
     * @param e the element
     */
    protected void handleShowMultiplicity(XMLElement e) {
        String showMultiplicity = e.getText().trim();
        ps.setShowMultiplicity(showMultiplicity);
    }

    /**
     * @param e the element
     */
    protected void handleShowInitialValue(XMLElement e) {
        String showInitialValue = e.getText().trim();
        ps.setShowInitialValue(showInitialValue);
    }

    /**
     * @param e the element
     */
    protected void handleShowProperties(XMLElement e) {
        String showproperties = e.getText().trim();
        ps.setShowProperties(showproperties);
    }

    /**
     * @param e the element
     */
    protected void handleShowTypes(XMLElement e) {
        String showTypes = e.getText().trim();
        ps.setShowTypes(showTypes);
    }

    /**
     * @param e the element
     */
    protected void handleShowStereotypes(XMLElement e) {
        String showStereotypes = e.getText().trim();
        ps.setShowStereotypes(showStereotypes);
    }

    /**
     * @param e the element
     */
    protected void handleDefaultShadowWidth(XMLElement e) {
        String dsw = e.getText().trim();
        ps.setDefaultShadowWidth(dsw);
    }
    
    /**
     * Get the numer of diagram members read.
     * @return the numer of diagram members read.
     */
    public List getMemberList() {
        return memberList;
    }
} /* end class ArgoParser */
