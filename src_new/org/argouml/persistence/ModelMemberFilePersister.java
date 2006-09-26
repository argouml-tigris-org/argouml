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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.argouml.application.ArgoVersion;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.model.Facade;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiReader;
import org.argouml.model.XmiWriter;
import org.argouml.ui.ArgoDiagram;
import org.argouml.uml.ProjectMemberModel;
import org.argouml.uml.cognitive.ProjectMemberTodoList;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.xml.sax.InputSource;

/**
 * The file persister for the UML model.
 * @author Bob Tarling
 */
class ModelMemberFilePersister extends MemberFilePersister 
    implements XmiExtensionParser {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ModelMemberFilePersister.class);

    /**
     * Loads a model (XMI only) from an input source. BE ADVISED this
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

        InputSource source = new InputSource(inputStream);
        Object mmodel = null;

        // 2002-07-18
        // Jaap Branderhorst
        // changed the loading of the projectfiles to solve hanging
        // of argouml if a project is corrupted. Issue 913
        // Created xmireader with method getErrors to check if parsing went well
        try {
            source.setEncoding(PersistenceManager.getEncoding());
            readModels(project, source);
            mmodel = getCurModel();
        } catch (OpenException e) {
            PersistenceManager.getInstance().setLastLoadStatus(false);
            PersistenceManager.getInstance().setLastLoadMessage(
                    "UmlException parsing XMI.");
            LOG.error("UmlException caught", e);
            throw new OpenException(e);
        }
        // This should probably be inside xmiReader.parse
        // but there is another place in this source
        // where XMIReader is used, but it appears to be
        // the NSUML XMIReader.  When Argo XMIReader is used
        // consistently, it can be responsible for loading
        // the listener.  Until then, do it here.
        Model.getUmlHelper().addListenersToModel(mmodel);

        project.addMember(mmodel);

        project.setUUIDRefs(new HashMap(getUUIDRefs()));
    }

    /**
     * @see org.argouml.persistence.MemberFilePersister#getMainTag()
     */
    public String getMainTag() {
        return "XMI";
    }

    /**
     * Save the project model to XMI.
     *
     * @see org.argouml.persistence.MemberFilePersister#save(
     *         org.argouml.kernel.ProjectMember, java.io.Writer,
     *         java.lang.Integer)
     */
    public void save(ProjectMember member, Writer w, Integer indent)
    	throws SaveException {

        if (w == null) {
            throw new IllegalArgumentException("No Writer specified!");
        }

        try {
            ProjectMemberModel pmm = (ProjectMemberModel) member;
            Object model = pmm.getModel();
//            XmiWriter xmiWriter = 
//                Model.getXmiWriter(model, w, 
//                        ArgoVersion.getVersion() + "(" 
//                        + UmlFilePersister.PERSISTENCE_VERSION + ")");
//            LOG.info("Registering extension writer to XmiWriter");
//            //xmiWriter.setXmiExtensionWriter(this);
//            xmiWriter.write();
            
            
            if (indent != null) {
                // If we have an indent then we are adding this file
                // to a superfile.
                // That is most likely inserting the XMI into the .uml file
                File tempFile = File.createTempFile("xmi", null);
                tempFile.deleteOnExit();

                Writer writer =
                    new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(tempFile), "UTF-8"));
                XmiWriter xmiWriter = 
                    Model.getXmiWriter(model, writer, 
                            ArgoVersion.getVersion() + "(" 
                            + UmlFilePersister.PERSISTENCE_VERSION + ")");
                
                xmiWriter.write();
                addXmlFileToWriter((PrintWriter) w, tempFile,
                                   indent.intValue());
            } else {
                // Othewise we are writing into a zip writer or to XMI.
                XmiWriter xmiWriter = 
                    Model.getXmiWriter(model, w, 
                            ArgoVersion.getVersion() + "(" 
                            + UmlFilePersister.PERSISTENCE_VERSION + ")");
                xmiWriter.write();
            }
        } catch (IOException e) {
            throw new SaveException(e);
        } catch (UmlException e) {
            throw new SaveException(e);
        }

    }
    
    /**
     * Get a MemberFilePersister based on a given ProjectMember.
     *
     * @param pm the project member
     * @return the persister
     */
    protected MemberFilePersister getMemberFilePersister(ProjectMember pm) {
        MemberFilePersister persister = null;
        if (pm instanceof ProjectMemberDiagram) {
            persister =
                PersistenceManager.getInstance()
                    .getDiagramMemberFilePersister();
        } else if (pm instanceof ProjectMemberTodoList) {
            persister = new TodoListMemberFilePersister();
        }
        return persister;
    }

    public void parse(String label, String xmiExtensionString) {
        LOG.info("Parsing an extension for " + label);
    }
    
    private Object curModel;
    private Project proj;
    private HashMap uUIDRefs;

    private Collection elementsRead;

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @return the current model
     */
    public Object/*MModel*/ getCurModel() {
        return curModel;
    }

    /**
     * @param p the project
     */
    public void setProject(Project p) {
        proj = p;
    }

    /**
     * Return XMI id to object map for the most recently read XMI file.
     * 
     * @return the UUID
     */
    public HashMap getUUIDRefs() {
        return uUIDRefs;
    }

    ////////////////////////////////////////////////////////////////
    // main parsing methods

    /**
     * Read an XMI file from the given URL.
     *
     * @param p the project
     * @param url the URL
     * @throws OpenException when there is an IO error
     */
    public synchronized void readModels(Project p, URL url, XmiExtensionParser xmiExtensionParser)
        throws OpenException {
        LOG.info("=======================================");
        LOG.info("== READING MODEL " + url);
        try {
            InputSource source = new InputSource(new XmiInputStream(url.openStream(), xmiExtensionParser, 10000000, 100000));
            source.setSystemId(url.toString());
            readModels(p, source);
        } catch (Exception ex) {
            throw new OpenException(ex);
        }
    }

    /**
     * Read a XMI file from the given inputsource.
     * @param p Project to which load the inputsource.
     * @param source The InputSource
     * @throws OpenException If an error occur while reading the source
     */
    public synchronized void readModels(Project p, InputSource source)
        throws OpenException {

        proj = p;

        XmiReader reader = null;
        try {
            reader = Model.getXmiReader();
            
            if (Configuration.getBoolean(Argo.KEY_XMI_STRIP_DIAGRAMS, false)) {
                reader.setIgnoredElements(new String[] {"UML:Diagram"});
            } else {
                reader.setIgnoredElements(null);
            }
            
            curModel = null;
            elementsRead = reader.parse(source, false);
            if (elementsRead != null && !elementsRead.isEmpty()) {
                Facade facade = Model.getFacade();
                Object current;
                Iterator elements = elementsRead.iterator();
                while (elements.hasNext()) {
                    current = elements.next();
                    if (facade.isAModel(current)) {
                        LOG.info("Loaded model '" + facade.getName(current)
                                 + "'");
                        if (curModel == null) {
                            curModel = current;
                        }
                    }
                }
            }
            uUIDRefs = new HashMap(reader.getXMIUUIDToObjectMap());
        } catch (Exception ex) {
            throw new OpenException(ex);
        }
        LOG.info("=======================================");
    }

    /**
     * Create and register diagrams for activity and statemachines in the
     * model(s) of the project. If no other diagrams are created and atLeastOne
     * is true, than a default Class Diagram will be created.  ArgoUML currently
     * requires at least one diagram for proper operation.
     *
     * @param project
     *            The project
     */
    public void registerDiagrams(Project project) {
        registerDiagrams(project, elementsRead, true);
    }
    
    /**
     * Create and register diagrams for activity and statemachines in the
     * model(s) of the project. If no other diagrams are created and atLeastOne
     * is true, than a default Class Diagram will be created.  ArgoUML currently
     * requires at least one diagram for proper operation.
     *
     * @param project
     *            The project
     * @param elements
     *            Collection of top level model elements to process
     * @param atLeastOne
     *            If true, forces at least one diagram to be created.
     */
    public void registerDiagrams(Project project, Collection elements,
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
            if (proj.getDiagramCount() == 0) {
                ArgoDiagram diagram = null;
                if (facade.isAActivityGraph(statemachine)) {
                    LOG.info("Creating activity diagram for "
                            + facade.getUMLClassName(statemachine)
                            + "<<" + facade.getName(statemachine) + ">>");
                    diagram =
                        diagramFactory.createDiagram(UMLActivityDiagram.class,
                                                     namespace, statemachine);
                } else {
                    LOG.info("Creating state diagram for "
                            + facade.getUMLClassName(statemachine)
                            + "<<" + facade.getName(statemachine) + ">>");
                    diagram =
                        diagramFactory.createDiagram(UMLStateDiagram.class,
                                                     namespace, statemachine);
                }
                if (diagram != null) {
                    proj.addMember(diagram);
                }
            }
        }
        // ISSUE 3516 : Make sure there is at least one diagram because
        // ArgoUML requires it for correct operation
        if (atLeastOne && proj.getDiagramCount() < 1) {
            ArgoDiagram d =
                diagramFactory.createDiagram(UMLClassDiagram.class,
                                             curModel, null);
            proj.addMember(d);
        }
        if (proj.getDiagramCount() >= 1 && proj.getActiveDiagram() == null) {
            proj.setActiveDiagram((ArgoDiagram) proj.getDiagrams().get(0));
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
