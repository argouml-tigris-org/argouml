/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    thn
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.application.api.Argo;
import org.argouml.application.helpers.ApplicationVersion;
import org.argouml.configuration.Configuration;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.model.Facade;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiException;
import org.argouml.model.XmiReader;
import org.argouml.model.XmiWriter;
import org.argouml.uml.ProjectMemberModel;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.DiagramFactory.DiagramType;
import org.xml.sax.InputSource;

/**
 * The file persister for the UML model.
 * @author Bob Tarling
 */
class ModelMemberFilePersister extends MemberFilePersister
    implements XmiExtensionParser {

    private static final Logger LOG =
        Logger.getLogger(ModelMemberFilePersister.class.getName());

    private Object curModel;
    private HashMap<String, Object> uUIDRefs;

    private Collection elementsRead;

    /**
     * Loads a model (XMI only) from a URL. BE ADVISED this
     * method has a side effect. It sets _UUIDREFS to the model.<p>
     *
     * If there is a problem with the xmi file, an error is set in the
     * getLastLoadStatus() field. This needs to be examined by the
     * calling function.<p>
     */
    public void load(Project project, URL url)
        throws OpenException {

        load(project, new InputSource(url.toExternalForm()));
    }

    /**
     * Loads a model (XMI only) from an input stream. BE ADVISED this
     * method has a side effect. It sets _UUIDREFS to the model.<p>
     *
     * If there is a problem with the xmi file, an error is set in the
     * getLastLoadStatus() field. This needs to be examined by the
     * calling function.<p>
     *
     * @see org.argouml.persistence.MemberFilePersister#load(org.argouml.kernel.Project,
     * java.io.InputStream)
     */
    public void load(Project project, InputStream inputStream)
        throws OpenException {

        load(project, new InputSource(inputStream));
    }


    public void load(Project project, InputSource source)
        throws OpenException {

        Object mmodel = null;

        // 2002-07-18
        // Jaap Branderhorst
        // changed the loading of the projectfiles to solve hanging
        // of argouml if a project is corrupted. Issue 913
        // Created xmireader with method getErrors to check if parsing went well
        try {
            source.setEncoding(Argo.getEncoding());
            readModels(source);
            mmodel = getCurModel();
        } catch (OpenException e) {
            LOG.log(Level.SEVERE, "UmlException caught", e);
            throw e;
        }
        // This should probably be inside xmiReader.parse
        // but there is another place in this source
        // where XMIReader is used, but it appears to be
        // the NSUML XMIReader.  When Argo XMIReader is used
        // consistently, it can be responsible for loading
        // the listener.  Until then, do it here.
        Model.getUmlHelper().addListenersToModel(mmodel);

        // TODO Add all top level packages
        project.addMember(mmodel);

        project.setUUIDRefs(new HashMap<String, Object>(getUUIDRefs()));
    }

    /*
     * @see org.argouml.persistence.MemberFilePersister#getMainTag()
     */
    public String getMainTag() {
        try {
            return Model.getXmiReader().getTagName();
        } catch (UmlException e) {
            // Should never happen - something's really wrong
            throw new RuntimeException(e);
        }
    }

    /**
     * Save the project model to XMI.
     *
     * @see org.argouml.persistence.MemberFilePersister#save(ProjectMember, OutputStream)
     */
    public void save(ProjectMember member, OutputStream outStream)
        throws SaveException {

        ProjectMemberModel pmm = (ProjectMemberModel) member;
        Object model = pmm.getModel();

        try {
            XmiWriter xmiWriter =
                Model.getXmiWriter(model, outStream,
                        ApplicationVersion.getVersion() + "("
                        + UmlFilePersister.PERSISTENCE_VERSION + ")");

            xmiWriter.write();
            outStream.flush();
        } catch (UmlException e) {
            throw new SaveException(e);
        } catch (IOException e) {
            throw new SaveException(e);
        }

    }

    public void parse(String label, String xmiExtensionString) {
        LOG.log(Level.INFO, "Parsing an extension for {0}", label);
    }


    /**
     * @return the current model
     * @deprecated by tfmorris for 0.33.1
     */
    @Deprecated
    public Object getCurModel() {
        return curModel;
    }

    /**
     * Return XMI id to object map for the most recently read XMI file.
     *
     * @return the UUID
     */
    public HashMap<String, Object> getUUIDRefs() {
        return uUIDRefs;
    }

    ////////////////////////////////////////////////////////////////
    // main parsing methods

    /**
     * Read an XMI file from the given URL.
     *
     * @param url the URL
     * @param xmiExtensionParser the XmiExtensionParser
     * @throws OpenException when there is an IO error
     */
    public synchronized void readModels(URL url,
            XmiExtensionParser xmiExtensionParser) throws OpenException {
        LOG.log(Level.INFO,
                "=======================================\n"
                +"== READING MODEL {0}", url);

        try {
            // TODO: What progressMgr is to be used here? Where does
            //       it come from?
            InputSource source =
                new InputSource(new XmiInputStream(
                    url.openStream(), xmiExtensionParser, 100000, null));

            source.setSystemId(url.toString());
            readModels(source);
        } catch (IOException ex) {
            throw new OpenException(ex);
        }
    }

    /**
     * Read a XMI file from the given inputsource.
     *
     * @param source The InputSource. The systemId of the input source should be
     *                set so that it can be used to resolve external references.
     * @throws OpenException If an error occur while reading the source
     */
    public synchronized void readModels(InputSource source)
        throws OpenException {

        XmiReader reader = null;
        try {
            reader = Model.getXmiReader();

            if (Configuration.getBoolean(Argo.KEY_XMI_STRIP_DIAGRAMS, false)) {
                // TODO: Not implemented by eUML
                reader.setIgnoredElements(new String[] {"UML:Diagram"});
            } else {
                reader.setIgnoredElements(null);
            }

            List<String> searchPath = reader.getSearchPath();
            String pathList =
                System.getProperty("org.argouml.model.modules_search_path");
            if (pathList != null) {
                String[] paths = pathList.split(",");
                for (String path : paths) {
                    if (!searchPath.contains(path)) {
                        reader.addSearchPath(path);
                    }
                }
            }
            reader.addSearchPath(source.getSystemId());

            curModel = null;
            elementsRead = reader.parse(source, false);
            if (elementsRead != null && !elementsRead.isEmpty()) {
                Facade facade = Model.getFacade();
                Object current;
                Iterator elements = elementsRead.iterator();
                while (elements.hasNext()) {
                    current = elements.next();
                    if (facade.isAModel(current)) {
                        if (LOG.isLoggable(Level.INFO)) {
                            LOG.log(Level.INFO,
                                    "Loaded model {0}",
                                    facade.getName(current));
                        }
                        if (curModel == null) {
                            curModel = current;
                        }
                    } else if (facade.isAProfile(current)) {
                        LOG.log(Level.INFO,
                                "Loaded profile '" + facade.getName(current)
                                + "'");
                        if (curModel == null) {
                            curModel = current;
                        }
                    }
                    // TODO: add stereotype application (eCore AnyType?)
                }
            }
            uUIDRefs =
                new HashMap<String, Object>(reader.getXMIUUIDToObjectMap());
        } catch (XmiException ex) {
            throw new XmiFormatException(ex);
        } catch (UmlException ex) {
            // Could this be some other type of internal error that we want
            // to handle differently?  Don't think so.  - tfm
            throw new XmiFormatException(ex);
        }
        LOG.log(Level.INFO, "=======================================");
    }

    /**
     * Create and register diagrams for activity and statemachines in the
     * model(s) of the project. If no other diagrams are created, a default
     * Class Diagram will be created. ArgoUML currently requires at least one
     * diagram for proper operation.
     *
     * TODO: Move to XmiFilePersister (protected)
     *
     * @param project
     *            The project
     */
    public void registerDiagrams(Project project) {
        registerDiagramsInternal(project, elementsRead, true);
    }


    /**
     * Internal method create diagrams for activity graphs and state machines.
     * It exists soley to contain common functionality from the two public
     * methods.  It can be merged into its caller when the deprecated version
     * of the public method goes away.
     *
     * @param project
     *            The project
     * @param elements
     *            Collection of top level model elements to process
     * @param atLeastOne
     *            If true, forces at least one diagram to be created.
     */
    private void registerDiagramsInternal(Project project, Collection elements,
            boolean atLeastOne) {
        Facade facade = Model.getFacade();
        Collection diagramsElement = new ArrayList();
        Iterator it = elements.iterator();
        while (it.hasNext()) {
            Object element = it.next();
            if (facade.isAModel(element)) {
                diagramsElement.addAll(Model.getModelManagementHelper()
                        .getAllModelElementsOfKind(element,
                                Model.getMetaTypes().getStateMachine()));
            } else if (facade.isAStateMachine(element)) {
                diagramsElement.add(element);
            }
        }
        DiagramFactory diagramFactory = DiagramFactory.getInstance();
        it = diagramsElement.iterator();
        while (it.hasNext()) {
            Object statemachine = it.next();
            Object namespace = facade.getNamespace(statemachine);
            if (namespace == null) {
                namespace = facade.getContext(statemachine);
                Model.getCoreHelper().setNamespace(statemachine, namespace);
            }

            ArgoDiagram diagram = null;
            if (facade.isAActivityGraph(statemachine)) {
                if (LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO,
                            "Creating activity diagram for {0}<<{1}>>",
                            new Object[] {
                                facade.getUMLClassName(statemachine),
                                facade.getName(statemachine)
                            });
                }
                diagram = diagramFactory.createDiagram(
                        DiagramType.Activity,
                	namespace,
                	statemachine);
            } else {
                if (LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO,
                            "Creating activity diagram for {0}<<{1}>>",
                            new Object[] {
                                facade.getUMLClassName(statemachine),
                                facade.getName(statemachine)
                            });
                }

                diagram = diagramFactory.createDiagram(
                        DiagramType.State,
                	namespace,
                	statemachine);
            }
            if (diagram != null) {
                project.addMember(diagram);
            }

        }
        // ISSUE 3516 : Make sure there is at least one diagram because
        // ArgoUML requires it for correct operation
        if (atLeastOne && project.getDiagramCount() < 1) {
            ArgoDiagram d = diagramFactory.create(
                    DiagramType.Class, curModel,
                    project.getProjectSettings().getDefaultDiagramSettings());
            project.addMember(d);
        }
        if (project.getDiagramCount() >= 1
                && project.getActiveDiagram() == null) {
            project.setActiveDiagram(
                    project.getDiagramList().get(0));
        }
    }

    /**
     * @return Returns the elementsRead.
     */
    public Collection getElementsRead() {
        return elementsRead;
    }

    /**
     * @param elements The elementsRead to set.
     */
    public void setElementsRead(Collection elements) {
        this.elementsRead = elements;
    }
}
