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

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectSettings;
import org.argouml.notation.NotationSettings;
import org.argouml.uml.diagram.DiagramSettings;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Parser for ArgoUML project description file (.argo).
 */
class ArgoParser extends SAXParserBase {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ArgoParser.class.getName());

    private Project project;

    private ProjectSettings ps;

    private DiagramSettings diagramDefaults;

    private NotationSettings notationSettings;

    private ArgoTokenTable tokens = new ArgoTokenTable();

    private List<String> memberList = new ArrayList<String>();

    /**
     * The constructor.
     *
     */
    public ArgoParser() {
        super();
    }

    /**
     * @param theProject the project to populate
     * @param source the input source
     * @throws SAXException on error when parsing xml
     */
    public void readProject(Project theProject, InputSource source)
        throws SAXException {

        if (source == null) {
            throw new IllegalArgumentException(
                    "An InputSource must be supplied");
        }

        preRead(theProject);

        try {
            parse(source);
        } catch (SAXException e) {
            logError(source.toString(), e);
            throw e;
        }
    }

    /**
     * @param theProject the project to populate
     * @param reader the reader
     * @throws SAXException on error when parsing xml
     */
    public void readProject(Project theProject, Reader reader)
    	throws SAXException {

        if (reader == null) {
            throw new IllegalArgumentException(
                    "A reader must be supplied");
        }

        preRead(theProject);

        try {
            parse(reader);
        } catch (SAXException e) {
            logError(reader.toString(), e);
            throw e;
        }
    }

    private void preRead(Project theProject) {
        LOG.log(Level.INFO,
                "=======================================\n"
                + "== READING PROJECT {0}",
                theProject);

        project = theProject;
        ps = project.getProjectSettings();
        diagramDefaults = ps.getDefaultDiagramSettings();
        notationSettings = ps.getNotationSettings();
    }

    private void logError(String projectName, SAXException e) {
        LOG.log(Level.SEVERE,
                "Exception reading project ================"+projectName, e);
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
            LOG.log(Level.FINE,
                    "NOTE: ArgoParser handleStartTag: {0}", e.getName());
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
                LOG.log(Level.WARNING, "WARNING: unknown tag:" + e.getName());
            }
            break;
        }
    }

    /*
     * @see org.argouml.persistence.SAXParserBase#handleEndElement(
     *         org.argouml.persistence.XMLElement)
     */
    @SuppressWarnings("deprecation")
    public void handleEndElement(XMLElement e) throws SAXException {
        if (DBG) {
            LOG.log(Level.FINE,
                    "NOTE: ArgoParser handleEndTag: {0}", e.getName());
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
        case ArgoTokenTable.TOKEN_SHOWSINGULARMULTIPLICITIES:
            handleShowSingularMultiplicities(e);
            break;
        case ArgoTokenTable.TOKEN_DEFAULTSHADOWWIDTH:
            handleDefaultShadowWidth(e);
            break;
        case ArgoTokenTable.TOKEN_FONTNAME:
            handleFontName(e);
            break;
        case ArgoTokenTable.TOKEN_FONTSIZE:
            handleFontSize(e);
            break;
        case ArgoTokenTable.TOKEN_GENERATION_OUTPUT_DIR:
            // ignored - it shouldn't have been in the project in the 1st place
            break;
        case ArgoTokenTable.TOKEN_SHOWASSOCIATIONNAMES:
            handleShowAssociationNames(e);
            break;
        case ArgoTokenTable.TOKEN_HIDEBIDIRECTIONALARROWS:
            handleHideBidirectionalArrows(e);
            break;
        case ArgoTokenTable.TOKEN_ACTIVE_DIAGRAM:
            handleActiveDiagram(e);
            break;
        default:
            if (DBG) {
                LOG.log(Level.WARNING,
                        "WARNING: unknown end tag:" + e.getName());
            }
            break;
        }
    }

    /*
     * @see org.argouml.persistence.SAXParserBase#isElementOfInterest(String)
     */
    @Override
    protected boolean isElementOfInterest(String name) {
        return tokens.contains(name);
    }

    /**
     * @param e the element
     */
    protected void handleArgo(@SuppressWarnings("unused") XMLElement e) {
        /* do nothing */
    }

    /**
     * @param e the element
     */
    protected void handleDocumentation(
            @SuppressWarnings("unused") XMLElement e) {
        /* do nothing */
    }

    /**
     * @param e the element
     */
    protected void handleSettings(@SuppressWarnings("unused") XMLElement e) {
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
        memberList.add(type);
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
        boolean success = ps.setNotationLanguage(language);
        /* TODO: Here we should e.g. show the user a message that
         * the loaded project was using a Notation that is not
         * currently available and a fall back on the default Notation
         * was done. Maybe this can be implemented in the
         * PersistenceManager? */
    }

    /**
     * @param e the element
     */
    protected void handleShowBoldNames(XMLElement e) {
        String ug = e.getText().trim();
        diagramDefaults.setShowBoldNames(Boolean.parseBoolean(ug));
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
        notationSettings.setShowVisibilities(
                Boolean.parseBoolean(showVisibility));
    }

    /**
     * @param e the element
     */
    protected void handleShowMultiplicity(XMLElement e) {
        String showMultiplicity = e.getText().trim();
        notationSettings.setShowMultiplicities(
                Boolean.parseBoolean(showMultiplicity));
    }

    /**
     * @param e the element
     */
    protected void handleShowInitialValue(XMLElement e) {
        String showInitialValue = e.getText().trim();
        notationSettings.setShowInitialValues(
                Boolean.parseBoolean(showInitialValue));
    }

    /**
     * @param e the element
     */
    protected void handleShowProperties(XMLElement e) {
        String showproperties = e.getText().trim();
        notationSettings.setShowProperties(
                Boolean.parseBoolean(showproperties));
    }

    /**
     * @param e the element
     */
    protected void handleShowTypes(XMLElement e) {
        String showTypes = e.getText().trim();
        notationSettings.setShowTypes(Boolean.parseBoolean(showTypes));
    }

    /**
     * @param e the element
     */
    protected void handleShowStereotypes(XMLElement e) {
        String showStereotypes = e.getText().trim();
        ps.setShowStereotypes(Boolean.parseBoolean(showStereotypes));
    }

    /**
     * @param e the element
     */
    protected void handleShowSingularMultiplicities(XMLElement e) {
        String showSingularMultiplicities = e.getText().trim();
        notationSettings.setShowSingularMultiplicities(
                Boolean.parseBoolean(showSingularMultiplicities));
    }

    /**
     * @param e the element
     */
    protected void handleDefaultShadowWidth(XMLElement e) {
        String dsw = e.getText().trim();
        diagramDefaults.setDefaultShadowWidth(Integer.parseInt(dsw));
    }

    /**
     * @param e the element
     */
    protected void handleFontName(XMLElement e) {
        String dsw = e.getText().trim();
        diagramDefaults.setFontName(dsw);
    }

    /**
     * @param e the element
     */
    protected void handleFontSize(XMLElement e) {
        String dsw = e.getText().trim();
        try {
            diagramDefaults.setFontSize(Integer.parseInt(dsw));
        } catch (NumberFormatException e1) {
            LOG.log(Level.SEVERE,
                    "NumberFormatException while parsing Font Size", e1);
        }
    }

    /**
     * @param e the element
     */
    protected void handleShowAssociationNames(XMLElement e) {
        String showAssociationNames = e.getText().trim();
        notationSettings.setShowAssociationNames(
                Boolean.parseBoolean(showAssociationNames));
    }

    /**
     * @param e the element
     */
    protected void handleHideBidirectionalArrows(XMLElement e) {
        String hideBidirectionalArrows = e.getText().trim();
        // NOTE: For historical reasons true == hide, so we need to invert
        // the sense of this
        diagramDefaults.setShowBidirectionalArrows(!
                Boolean.parseBoolean(hideBidirectionalArrows));
    }


    protected void handleActiveDiagram(XMLElement e) {
        /* At this stage during loading, the diagrams are
         * not created yet - so we have to store this name for later use. */
        project.setSavedDiagramName(e.getText().trim());
    }

    /**
     * Get the number of diagram members read.
     * @return the number of diagram members read.
     */
    public List<String> getMemberList() {
        return memberList;
    }
}
