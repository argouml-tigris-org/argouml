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

    /**
     * The singleton for this class.
     */
    public static final ArgoParser SINGLETON = new ArgoParser();

    ////////////////////////////////////////////////////////////////
    // instance variables

    private Project _proj = null;

    private ArgoTokenTable tokens = new ArgoTokenTable();

    private boolean addMembers = true;

    private URL url = null;

    private boolean lastLoadStatus = true;

    private String lastLoadMessage = null;

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

    // TODO: should be able to merge an existing project into
    // the current one.

    public synchronized void readProject(URL theUrl) throws IOException,
            ParserConfigurationException, SAXException {
        readProject(theUrl, true);
    }

    public synchronized void readProject(URL theUrl, boolean addTheMembers)
        throws IOException, ParserConfigurationException, SAXException {
        this.url = theUrl;
        readProject(this.url.openStream(), addTheMembers);
    }

    public void setURL(URL theUrl) {
        this.url = theUrl;
    }

    public synchronized void readProject(InputStream is, 
            boolean addTheMembers)
        throws IOException, SAXException, ParserConfigurationException {

        lastLoadStatus = true;
        lastLoadMessage = "OK";

        this.addMembers = addTheMembers;

        if ((url == null) && addTheMembers) {
            LOG.info("URL not set! Won't be able to add members! Aborting...");
            lastLoadMessage = "URL not set!";
            return;
        }

        try {
            LOG.info("=======================================");
            LOG.info("== READING PROJECT " + url);
            _proj = new Project(url);
            parse(is);
        } catch (SAXException saxEx) {
            lastLoadStatus = false;
            LOG.error("Exception reading project================");
            LOG.error(is.toString());
            lastLoadMessage = saxEx.toString();
            throw saxEx;

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

    public Project getProject() {
        Project returnValue = _proj;
        return _proj;
    }

    public void setProject(Project newProj) {
        _proj = newProj;
    }

    /**
     * @see org.argouml.xml.SAXParserBase#handleStartElement(org.argouml.xml.XMLElement)
     */
    public void handleStartElement(XMLElement e) {
        if (dbg)
            LOG.debug("NOTE: ArgoParser handleStartTag:" + e.getName());
        try {
            switch (tokens.toToken(e.getName(), true)) {
            case ArgoTokenTable.TOKEN_argo:
                handleArgo(e);
                break;
            case ArgoTokenTable.TOKEN_documentation:
                handleDocumentation(e);
                break;
            default:
                if (dbg)
                    LOG.warn("WARNING: unknown tag:" + e.getName());
                break;
            }
        } catch (Exception ex) {
            LOG.error(ex);
        }
    }

    /**
     * @see org.argouml.xml.SAXParserBase#handleEndElement(org.argouml.xml.XMLElement)
     */
    public void handleEndElement(XMLElement e) {
        if (dbg)
            LOG.debug("NOTE: ArgoParser handleEndTag:" + e.getName() + ".");
        try {
            switch (tokens.toToken(e.getName(), false)) {
            case ArgoTokenTable.TOKEN_authorname:
                handleAuthorname(e);
                break;
            case ArgoTokenTable.TOKEN_version:
                handleVersion(e);
                break;
            case ArgoTokenTable.TOKEN_description:
                handleDescription(e);
                break;
            case ArgoTokenTable.TOKEN_searchpath:
                handleSearchpath(e);
                break;
            case ArgoTokenTable.TOKEN_member:
                handleMember(e);
                break;
            case ArgoTokenTable.TOKEN_historyfile:
                handleHistoryfile(e);
                break;
            default:
                if (dbg)
                    LOG.warn("WARNING: unknown end tag:" + e.getName());
                break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void handleArgo(XMLElement e) {
        /* do nothing */
    }

    protected void handleDocumentation(XMLElement e) {
        /* do nothing */
    }

    protected void handleAuthorname(XMLElement e) {
        String authorname = e.getText().trim();
        _proj.setAuthorname(authorname);
    }

    protected void handleVersion(XMLElement e) {
        String version = e.getText().trim();
        _proj.setVersion(version);
    }

    protected void handleDescription(XMLElement e) {
        String description = e.getText().trim();
        _proj.setDescription(description);
    }

    protected void handleSearchpath(XMLElement e) {
        String searchpath = e.getAttribute("href").trim();
        _proj.addSearchPath(searchpath);
    }

    protected void handleMember(XMLElement e) {
        if (addMembers) {
            String name = e.getAttribute("name").trim();
            String type = e.getAttribute("type").trim();
            _proj.addMember(name, type);
        }
    }

    protected void handleHistoryfile(XMLElement e) {
        if (e.getAttribute("name") == null)
            return;
        String historyfile = e.getAttribute("name").trim();
        _proj.setHistoryFile(historyfile);
    }

    /**
     * return the status of the last load attempt. Used for junit tests.
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

} /* end class ArgoParser */
