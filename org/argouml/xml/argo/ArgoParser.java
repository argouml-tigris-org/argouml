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

import org.apache.log4j.Category;
import org.argouml.application.api.*;
import java.util.*;
import java.io.*;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.argouml.kernel.Project;
import org.argouml.xml.SAXParserBase;
import org.argouml.xml.XMLElement;
import org.xml.sax.*;

/** @stereotype singleton
 */
public class ArgoParser extends SAXParserBase {
    protected static Category cat = Category.getInstance(ArgoParser.class);

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ArgoParser SINGLETON = new ArgoParser();

    ////////////////////////////////////////////////////////////////
    // instance variables

    protected Project _proj = null;

    private ArgoTokenTable _tokens = new ArgoTokenTable();

    private boolean _addMembers = true;

    private URL _url = null;

    private boolean lastLoadStatus = true;

    private String lastLoadMessage = null;

    ////////////////////////////////////////////////////////////////
    // constructors

    protected ArgoParser() {
        super();
    }

    ////////////////////////////////////////////////////////////////
    // main parsing methods

    // TODO: should be able to merge an existing project into
    // the current one.

    public synchronized void readProject(URL url) throws IOException, ParserConfigurationException, SAXException {
        readProject(url, true);
    }

    public synchronized void readProject(URL url, boolean addMembers)
        throws IOException, ParserConfigurationException, SAXException {
        _url = url;
        readProject(url.openStream(), addMembers);
    }

    public void setURL(URL url) {
        _url = url;
    }

    public synchronized void readProject(InputStream is, boolean addMembers) throws IOException, SAXException, ParserConfigurationException {

        lastLoadStatus = true;
        lastLoadMessage = "OK";

        _addMembers = addMembers;

        if ((_url == null) && _addMembers) {
            Argo.log.info(
                "URL not set! Won't be able to add members! Aborting...");
            lastLoadMessage = "URL not set!";
            return;
        }

        try {
            cat.info("=======================================");
            cat.info("== READING PROJECT " + _url);
            _proj = new Project(_url);
            parse(is);
        } catch (SAXException saxEx) {
            lastLoadStatus = false;
            cat.error("Exception reading project================");
            cat.error(is.toString());
            lastLoadMessage = saxEx.toString();
            throw saxEx;

        } catch (IOException e) {
            lastLoadStatus = false;
            cat.error("Exception reading project================");
            cat.error(is.toString());
            lastLoadMessage = e.toString();
            throw e;
        } catch (ParserConfigurationException e) {
            lastLoadStatus = false;
            cat.error("Exception reading project================");
            cat.error(is.toString());
            lastLoadMessage = e.toString();
            throw e;
        }        
    }

    public Project getProject() {
        return _proj;
    }

    public void handleStartElement(XMLElement e) {
        if (_dbg)
            cat.debug("NOTE: ArgoParser handleStartTag:" + e.getName());
        try {
            switch (_tokens.toToken(e.getName(), true)) {
                case ArgoTokenTable.TOKEN_argo :
                    handleArgo(e);
                    break;
                case ArgoTokenTable.TOKEN_documentation :
                    handleDocumentation(e);
                    break;
                default :
                    if (_dbg)
                        cat.warn("WARNING: unknown tag:" + e.getName());
                    break;
            }
        } catch (Exception ex) {
            cat.error(ex);
        }
    }

    public void handleEndElement(XMLElement e) {
        if (_dbg)
            cat.debug("NOTE: ArgoParser handleEndTag:" + e.getName() + ".");
        try {
            switch (_tokens.toToken(e.getName(), false)) {
                case ArgoTokenTable.TOKEN_authorname :
                    handleAuthorname(e);
                    break;
                case ArgoTokenTable.TOKEN_version :
                    handleVersion(e);
                    break;
                case ArgoTokenTable.TOKEN_description :
                    handleDescription(e);
                    break;
                case ArgoTokenTable.TOKEN_searchpath :
                    handleSearchpath(e);
                    break;
                case ArgoTokenTable.TOKEN_member :
                    handleMember(e);
                    break;
                case ArgoTokenTable.TOKEN_historyfile :
                    handleHistoryfile(e);
                    break;
                default :
                    if (_dbg)
                        cat.warn("WARNING: unknown end tag:" + e.getName());
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
        _proj._authorname = authorname;
    }

    protected void handleVersion(XMLElement e) {
        String version = e.getText().trim();
        _proj._version = version;
    }

    protected void handleDescription(XMLElement e) {
        String description = e.getText().trim();
        _proj._description = description;
    }

    protected void handleSearchpath(XMLElement e) {
        String searchpath = e.getAttribute("href").trim();
        _proj.addSearchPath(searchpath);
    }

    protected void handleMember(XMLElement e) {
        if (_addMembers) {
            String name = e.getAttribute("name").trim();
            String type = e.getAttribute("type").trim();
            _proj.addMember(name, type);
        }
    }

    protected void handleHistoryfile(XMLElement e) {
        if (e.getAttribute("name") == null)
            return;
        String historyfile = e.getAttribute("name").trim();
        _proj._historyFile = historyfile;
    }

    /** return the status of the last load attempt.
        Used for junit tests.
     */
    public boolean getLastLoadStatus() {
        return lastLoadStatus;
    }

    /** set the status of the last load attempt. 
        Used for junit tests.
     */
    public void setLastLoadStatus(boolean status) {
        lastLoadStatus = status;
    }

    /** get the last message which caused loading to fail. 
        Used for junit tests.
     */
    public String getLastLoadMessage() {
        return lastLoadMessage;
    }

    /** set the last load message.
        Used for junit tests.
    */
    public void setLastLoadMessage(String msg) {
        lastLoadMessage = msg;
    }

} /* end class ArgoParser */
