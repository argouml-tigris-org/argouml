// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.helpers.ApplicationVersion;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.persistence.PersistenceManager;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ProjectMemberModel;
import org.argouml.uml.cognitive.ProjectMemberTodoList;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.generator.GenerationPreferences;
import org.argouml.uml.profile.ProfileConfiguration;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.undo.Memento;
import org.tigris.gef.undo.UndoManager;

/**
 * The Project is a datastructure that represents the designer's
 * current project. It manages the list of diagrams and UML models.
 */
public class Project implements java.io.Serializable, TargetListener {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(Project.class);

    ////////////////////////////////////////////////////////////////
    // constants

    /**
     * Default name for a project.
     */
    private static final String UNTITLED_FILE =
	Translator.localize("label.projectbrowser-title");

    ////////////////////////////////////////////////////////////////
    // static variables

    /**
     * The UID.
     */
    static final long serialVersionUID = 1399111233978692444L;

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * TODO: should just be the directory to write.
     */
    private URI uri;

    /* The preferences with project-scope: */
    private String authorname;
    private String authoremail;
    private String description;
    /* The ArgoUML version with which this project was last saved: */
    private String version;

    private ProjectSettings projectSettings;

    private Vector searchpath;

    // TODO: break into 3 main member types
    // model, diagram and other
    private final MemberList members = new MemberList();

    private String historyFile;

    /**
     * The version number of the persistence format that last
     * saved this project.
     */
    private int persistenceVersion;

    /**
     * Instances of the uml model.
     */
    private final Vector models = new Vector();

    /**
     * Instances of the uml diagrams.
     */
    private final List diagrams = new ArrayList();
    private Object defaultModel;
    private Object currentNamespace;
    private Map uuidRefs;
    private GenerationPreferences cgPrefs;
    private transient VetoableChangeSupport vetoSupport;

    private ProfileConfiguration profileConfiguration;

    /**
     * The active diagram, pointer to a diagram in the list with diagrams.
     */
    private ArgoDiagram activeDiagram;

    /**
     * Cache for the default model.
     */
    private HashMap defaultModelTypeCache;

    private Collection trashcan = new ArrayList();

    /**
     * Constructor.
     *
     * @param theProjectUri Uri to read the project from.
     */
    public Project(URI theProjectUri) {
        this();
        uri = PersistenceManager.getInstance().fixUriExtension(theProjectUri);
    }

    /**
     * Constructor.
     */
    public Project() {
	setProfileConfiguration(new ProfileConfiguration(this));

        projectSettings = new ProjectSettings();        
        
        Model.getModelManagementFactory().setRootModel(null);

        authorname = Configuration.getString(Argo.KEY_USER_FULLNAME);
        authoremail = Configuration.getString(Argo.KEY_USER_EMAIL);
        description = "";
        // this should be moved to a ui action.
        version = ApplicationVersion.getVersion();

        searchpath = new Vector();
        historyFile = "";
        cgPrefs = new GenerationPreferences();
        defaultModelTypeCache = new HashMap();

        LOG.info("making empty project with empty model");
 
        // Jaap Branderhorst 2002-12-09
        // load the default model
        // this is NOT the way how it should be since this makes argo
        // depend on Java even more.
        setDefaultModel(profileConfiguration.getDefaultProfile().getModel());
        
        addSearchPath("PROJECT_DIR");
        TargetManager.getInstance().addTargetListener(this);
    }

    /**
     * Find the base name of this project.<p>
     *
     * This is the name minus any valid file extension.
     *
     * @return The name (a String).
     */
    public String getBaseName() {
        String n = getName();
        n = PersistenceManager.getInstance().getBaseName(n);
        return n;
    }

    /**
     * Get the project name. This is just the name part of the full filename.
     * @return the name of the project
     */
    public String getName() {
        // TODO: maybe separate name
        if (uri == null) {
            return UNTITLED_FILE;
        }
        return new File(uri).getName();
    }

    /**
     * Set the project URI.
     *
     * @param n The new URI (as a String).
     * @throws URISyntaxException if the argument cannot be converted to
     *         an URI.
     */
    public void setName(String n)
        throws URISyntaxException {
        String s = "";
        if (getURI() != null) {
            s = getURI().toString();
        }
        s = s.substring(0, s.lastIndexOf("/") + 1) + n;
        setURI(new URI(s));
    }

    /**
     * Get the URI for this project.
     *
     * @return The URI.
     */
    public URI getURI() {
        return uri;
    }

    /**
     * Set the URI for this project.
     *
     * @param theUri The URI to set.
     */
    public void setURI(URI theUri) {
        if (theUri != null) {
            theUri = PersistenceManager.getInstance().fixUriExtension(theUri);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Setting project URI from \"" + uri
                      + "\" to \"" + theUri + "\".");
        }

        uri = theUri;
    }

    /**
     * Set the project file.
     *
     * This only works if it is possible to convert the File to an uri.
     *
     * @param file File to set the project to.
     */
    public void setFile(File file) {
        URI theProjectUri = file.toURI();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Setting project file name from \""
                      + uri
                      + "\" to \""
                      + theProjectUri
                      + "\".");
        }

        uri = theProjectUri;
    }

    /**
     * Used by "argo.tee".
     * 
     * @return the search path
     */
    public Vector getSearchPath() {
        return searchpath;
    }

    /**
     * @param searchPathElement the element to be added to the searchpath
     */
    public void addSearchPath(String searchPathElement) {
        if (!this.searchpath.contains(searchPathElement)) {
            this.searchpath.addElement(searchPathElement);
        }
    }

    /**
     * Get all members of the project.
     * Used by "argo2.tee".
     *
     * @return a Vector with all members.
     */
    public MemberList getMembers() {
        LOG.info("Getting the members there are " + members.size());
        return members;
    }

    /**
     * @param d the diagram
     */
    private void addDiagramMember(ArgoDiagram d) {
        ProjectMember pm = new ProjectMemberDiagram(d, this);
        addDiagram(d);
        // if diagram added successfully, add the member too
        members.add(pm);
    }

    /**
     * @param pm the member to be added
     */
    private void addTodoMember(ProjectMemberTodoList pm) {
        // Adding a todo member removes any existing one.
        members.add(pm);
        LOG.info("Added todo member, there are now " + members.size());
    }

    /**
     * @param m the member to be added
     */
    public void addMember(Object m) {

        if (m == null) {
            throw new IllegalArgumentException(
                    "A model member must be suppleid");
        } else if (m instanceof ArgoDiagram) {
            LOG.info("Adding diagram member");
            addDiagramMember((ArgoDiagram) m);
        } else if (m instanceof ProjectMemberTodoList) {
            LOG.info("Adding todo member");
            addTodoMember((ProjectMemberTodoList) m);
        } else if (Model.getFacade().isAModel(m)) {
            LOG.info("Adding model member");
            addModelMember(m);
        } else {
            throw new IllegalArgumentException(
                    "The member must be a UML model todo member or diagram."
                    + "It is " + m.getClass().getName());
        }
        LOG.info("There are now " + members.size() + " members");
    }

    /**
     * @param m the model
     */
    private void addModelMember(Object m) {

        boolean memberFound = false;
        Object currentMember =
            members.getMember(ProjectMemberModel.class);
        if (currentMember != null) {
            Object currentModel =
                ((ProjectMemberModel) currentMember).getModel();
            if (currentModel == m) {
                memberFound = true;
            }
        }

        if (!memberFound) {
            if (!models.contains(m)) {
                addModel(m);
            }
            // got past the veto, add the member
            ProjectMember pm = new ProjectMemberModel(m, this);
            LOG.info("Adding model member to start of member list");
            members.add(pm);
        } else {
            LOG.info("Attempted to load 2 models");
            throw new IllegalArgumentException(
                    "Attempted to load 2 models");
        }
    }

    /**
     * @param model a namespace
     */
    public void addModel(Object model) {

        if (!Model.getFacade().isANamespace(model)) {
            throw new IllegalArgumentException();
	}

        // fire indeterminate change to avoid copying vector
        if (!models.contains(model)) {
            models.addElement(model);
        }
        setCurrentNamespace(model);
        setSaveEnabled(true);
    }

    /**
     * Removes a project member diagram completely from the project.
     * @param d the ArgoDiagram
     */
    protected void removeProjectMemberDiagram(ArgoDiagram d) {
        if (activeDiagram == d) {
            ArgoDiagram defaultDiagram;
            if (diagrams.size() == 1) {
                // We're deleting the last diagram so lets create a new one
                // TODO: Once we go MDI we won't need this.
                Object treeRoot =
                    Model.getModelManagementFactory().getRootModel();
                defaultDiagram =
                    DiagramFactory.getInstance().createDiagram(
                	    UMLClassDiagram.class,
                	    treeRoot,
                	    null);
                addMember(defaultDiagram);
            } else {
                // Make the topmost diagram (that is not the one being deleted)
                // current.
                defaultDiagram = (ArgoDiagram) diagrams.get(0);
                if (defaultDiagram == d) {
                    defaultDiagram = (ArgoDiagram) diagrams.get(1);
                }
            }
            activeDiagram = defaultDiagram;
            TargetManager.getInstance().setTarget(defaultDiagram);
        }

        removeDiagram(d);
        members.remove(d);
        d.remove();
        setSaveEnabled(true);
    }
    
    /**
     * Enables the save action if this project is the current project
     * @param enable true to enable
     */
    private void setSaveEnabled(boolean enable) {
        ProjectManager pm = ProjectManager.getManager();
        if (pm.getCurrentProject() == this) {
            pm.setSaveEnabled(enable);
        }
    }


    /**
     * Get the author name. 
     * Used by "argo.tee".
     *
     * @return The author name.
     */
    public String getAuthorname() {
        return authorname;
    }

    /**
     * Set the author name.
     *
     * @param s The new author name.
     */
    public void setAuthorname(final String s) {
        final String oldAuthorName = authorname;
        Memento memento = new Memento() {
            public void redo() {
                authorname = s;
            }

            public void undo() {
                authorname = oldAuthorName;
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        setSaveEnabled(true);
    }

    /**
     * Get the author name.
     * Used by "argo.tee".
     *
     * @return The author name.
     */
    public String getAuthoremail() {
        return authoremail;
    }

    /**
     * Set the author name.
     *
     * @param s The new author name.
     */
    public void setAuthoremail(final String s) {
        final String oldAuthorEmail = authoremail;
        Memento memento = new Memento() {
            public void redo() {
                authoremail = s;
            }

            public void undo() {
                authoremail = oldAuthorEmail;
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        setSaveEnabled(true);
    }

    /**
     * Get the version.
     * Used by "argo.tee".
     *
     * @return the version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set the new version.
     * @param s The new version.
     */
    public void setVersion(String s) {
        version = s;
    }

    /**
     * Get the description.
     * Used by "argo.tee".
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set a new description.
     *
     * @param s The new description.
     */
    public void setDescription(final String s) {
        final String oldDescription = description;
        Memento memento = new Memento() {
            public void redo() {
                description = s;
            }

            public void undo() {
                description = oldDescription;
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        setSaveEnabled(true);
    }

    /**
     * Get the history file.
     * Used by "argo.tee".
     *
     * @return The history file.
     */
    public String getHistoryFile() {
        return historyFile;
    }

    /**
     * Set the history file.
     *
     * @param s The new history file.
     */
    public void setHistoryFile(String s) {
        historyFile = s;
    }

    /**
     * Returns all models defined by the user. I.e. this does not return the
     * default model but all other models.
     *
     * @return A Vector of all user defined models.
     */
    public Vector getUserDefinedModels() {
        return models;
    }

    /**
     * Returns all models, including the default model (default.xmi).
     *
     * @return A Collection containing all models.
     */
    public Collection getModels() {
        Set ret = new HashSet();
        ret.addAll(models);
        ret.add(defaultModel);
        return ret;
    }

    /**
     * Return the model.<p>
     *
     * If there isn't exactly one model, <code>null</code> is returned.
     *
     * @return the model.
     */
    public Object getModel() {
        if (models.size() != 1) {
            return null;
        }
        return models.elementAt(0);
    }

    /**
     * Searches for a type/classifier with name s. If the type is not found, a
     * new type is created and added to the current namespace.
     * <p>
     * @param s
     *            the name of the type/classifier to be found
     * @return Classifier
     */
    public Object findType(String s) {
        return findType(s, true);
    }

    /**
     * Return the default type for an attribute.
     * 
     * @return a Classifier to use as the default type
     */
    public Object getDefaultAttributeType() {
        // TODO: Move this to a profile - tfm - 20070307
        return findType("int");
    }

    /**
     * Return the default type for a parameter.
     * 
     * @return a Classifier to use as the default type
     */
    public Object getDefaultParameterType() {
        // TODO: Move this to a profile - tfm - 20070307
        return findType("int");
    }
    
    /**
     * Return the default type for the return parameter of a method.
     * 
     * @return a Classifier to use as the default type
     */
    public Object getDefaultReturnType() {
        // TODO: Move this to a profile - tfm - 20070307
        return findType("void");
    }

    /**
     * Searches for a type/classifier with name s. If defineNew is
     * true, a new type is defined if the type/classifier is not
     * found. The newly created type is added to the currentNamespace
     * and given the name s.
     * 
     * TODO: Move to Model subsystem - tfm 20070307
     * 
     * @param s the name of the type/classifier to be found
     * @param defineNew if true, define a new one
     * @return Classifier the found classifier
     */
    public Object findType(String s, boolean defineNew) {
        if (s != null) {
            s = s.trim();
        }
        if (s == null || s.length() == 0) {
            return null;
        }
        Object cls = null;
        int numModels = models.size();
        for (int i = 0; i < numModels; i++) {
            cls = findTypeInModel(s, models.elementAt(i));
            if (cls != null) {
                return cls;
            }
        }
        cls = findTypeInDefaultModel(s);
        // hey, now we should move it to the model the user is working in
        if (cls != null) {
            cls =
                Model.getModelManagementHelper()
                	.getCorrespondingElement(cls, getRoot());
        }
        if (cls == null && defineNew) {
            LOG.debug("new Type defined!");
            cls =
                Model.getCoreFactory().buildClass(getCurrentNamespace());
            Model.getCoreHelper().setName(cls, s);
        }
        return cls;
    }

    /**
     * Finds all figs on the diagrams for some project member,
     * including the figs containing the member (so for some
     * operation, the containing figclass is returned).
     *
     * @param member The member we are looking for.
     *              This can be a model element object but also another object.
     * @return Collection The collection with the figs.
     */
    public Collection findFigsForMember(Object member) {
        Collection figs = new ArrayList();
        Iterator it = diagrams.iterator();
        while (it.hasNext()) {
            ArgoDiagram diagram = (ArgoDiagram) it.next();
            Object fig = diagram.getContainingFig(member);
            if (fig != null) {
                figs.add(fig);
            }
        }
        return figs;
    }

    /**
     * Returns a list with all figs for some UML object on all diagrams.
     *
     * @param obj the given UML object
     * @return List the list of figs
     */
    public Collection findAllPresentationsFor(Object obj) {
        Collection figs = new ArrayList();
        Iterator it = diagrams.iterator();
        while (it.hasNext()) {
            Diagram diagram = (Diagram) it.next();
            Fig aFig = diagram.presentationFor(obj);
            if (aFig != null) {
                figs.add(aFig);
            }
        }
        return figs;
    }

    /**
     * Finds a classifier with a certain name.<p>
     *
     * Will only return first classifier with the matching name.
     *
     * TODO: Move to Model subsystem - tfm 20070307
     * 
     * @param s is short name.
     * @param ns Namespace where we do the search.
     * @return the found classifier (or <code>null</code> if not found).
     */
    public Object findTypeInModel(String s, Object ns) {

        if (!Model.getFacade().isANamespace(ns)) {
            throw new IllegalArgumentException(
                    "Looking for the classifier " + s
                    + " in a non-namespace object of " + ns
                    + ". A namespace was expected.");
    	}

        Collection allClassifiers =
            Model.getModelManagementHelper()
	        .getAllModelElementsOfKind(ns,
	                Model.getMetaTypes().getClassifier());

        Object[] classifiers = allClassifiers.toArray();
        Object classifier = null;

        for (int i = 0; i < classifiers.length; i++) {

            classifier = classifiers[i];
            if (Model.getFacade().getName(classifier) != null
            		&& Model.getFacade().getName(classifier).equals(s)) {
                return classifier;
            }
        }

        return null;
    }

    /**
     * @param m the namespace
     */
    public void setCurrentNamespace(Object m) {

        if (m != null && !Model.getFacade().isANamespace(m)) {
            throw new IllegalArgumentException();
    	}

        currentNamespace = m;
    }

    /**
     * @return the namespace
     */
    public Object getCurrentNamespace() {
        return currentNamespace;
    }

    /**
     * @return the diagrams
     * TODO: Whats the best way of changing this to List without effecting
     * modules or argoeclipse?
     */
    public Vector getDiagrams() {
        return new Vector(diagrams);
    }

    /**
     * Get the number of diagrams in this project.
     * Used by argo2.tee!!
     * @return the number of diagrams in this project.
     */
    public int getDiagramCount() {
        return diagrams.size();
    }

    /**
     * Finds a diagram with a specific name or UID.
     *
     * @return the diagram object (if found). Otherwize null.
     * @param name is the name to search for.
     */
    public ArgoDiagram getDiagram(String name) {
        Iterator it = diagrams.iterator();
        while (it.hasNext()) {
            ArgoDiagram ad = (ArgoDiagram) it.next();
            if (ad.getName() != null && ad.getName().equals(name)) {
                return ad;
            }
            if (ad.getItemUID() != null
                    && ad.getItemUID().toString().equals(name)) {
                return ad;
            }
        }
        return null;
    }

    /**
     * @param d the diagram to be added
     */
    public void addDiagram(ArgoDiagram d) {
        // send indeterminate new value instead of making copy of vector
	d.setProject(this);
        diagrams.add(d);
        d.addVetoableChangeListener(new Vcl());
        setSaveEnabled(true);
    }

    /**
     * Removes a diagram from the list with diagrams.
     *
     * Removes (hopefully) the event listeners for this diagram.  Does
     * not remove the diagram from the project members. This should
     * not be called directly. Use moveToTrash if you want to remove a
     * diagram.
     *
     * @param d the ArgoDiagram
     */
    protected void removeDiagram(ArgoDiagram d) {
        d.removeVetoableChangeListener(new Vcl());
        diagrams.remove(d);
        if (d instanceof UMLDiagram) {
            /* If this is a UML diagram, then remove the dependent
             * modelelements, such as the statemachine
             * for a statechartdiagram.
             */
            Object o = ((UMLDiagram) d).getDependentElement();
            if (o != null) {
                moveToTrash(o);
            }
        }
    }

    /**
     * Listener to events from the Diagram class. <p>
     *
     * Purpose: changing the name of a diagram shall set the need save flag.
     *
     * @author mvw@tigris.org
     */
    private class Vcl implements VetoableChangeListener {
        public void vetoableChange(PropertyChangeEvent evt)
            throws PropertyVetoException {
            if (evt.getPropertyName().equals("name")) {
                setSaveEnabled(true);
            }
        }
    }

    /**
     * @param me the given modelelement
     * @return the total number of presentation
     *         for the given modelelement in the project
     */
    public int getPresentationCountFor(Object me) {

        if (!Model.getFacade().isAModelElement(me)) {
            throw new IllegalArgumentException();
    	}

        int presentations = 0;
        int size = diagrams.size();
        for (int i = 0; i < size; i++) {
            Diagram d = (Diagram) diagrams.get(i);
            presentations += d.getLayer().presentationCountFor(me);
        }
        return presentations;
    }

    /**
     * @return an initial target, in casu a diagram or a model
     */
    public Object getInitialTarget() {
        if (diagrams.size() > 0) {
            return diagrams.get(0);
        }
        if (models.size() > 0) {
            return models.elementAt(0);
        }
        return null;
    }


    /**
     * @param cgp the generation preferences
     */
    public void setGenerationPrefs(GenerationPreferences cgp) {
        cgPrefs = cgp;
    }

    /**
     * @return the generation preferences
     */
    public GenerationPreferences getGenerationPrefs() {
        return cgPrefs;
    }

    ////////////////////////////////////////////////////////////////
    // event handling

    /**
     * @return the VetoableChangeSupport
     */
    public VetoableChangeSupport getVetoSupport() {
        if (vetoSupport == null) {
            vetoSupport = new VetoableChangeSupport(this);
        }
        return vetoSupport;
    }

    /**
     * This is executed before a save.
     */
    public void preSave() {
        for (int i = 0; i < diagrams.size(); i++) {
            ((Diagram) diagrams.get(i)).preSave();
        }
        // TODO: is preSave needed for models?
    }

    /**
     * This is execcuted after a save.
     */
    public void postSave() {
        for (int i = 0; i < diagrams.size(); i++) {
            ((Diagram) diagrams.get(i)).postSave();
        }
        // TODO: is postSave needed for models?
        setSaveEnabled(true);
    }

    /**
     * This is executed after a load.
     */
    public void postLoad() {
        for (int i = 0; i < diagrams.size(); i++) {
            ((Diagram) diagrams.get(i)).postLoad();
        }
        // issue 1725: the root is not set, which leads to problems
        // with displaying prop panels
        Object model = getModel();

        LOG.info("Setting root model to " + model);

        setRoot(model);

        setSaveEnabled(true);
        // we don't need this HashMap anymore so free up the memory
        uuidRefs = null;
    }

    ////////////////////////////////////////////////////////////////
    // trash related methods

    /**
     * Moves some object to trash, i.e. deletes it completely with all
     * dependent structures. <p>
     *
     * Deleting an object involves: <pre>
     * - Removing Target history
     * - Deleting all Fig representations for the object
     * - Deleting the UML element
     * - Deleting all dependent UML modelelements
     * - Deleting CommentEdges (which are not UML elements)
     * - Move to trash for enclosed objects, i.e. graphically drawn on top of
     * - Move to trash subdiagrams for the object
     * - Saveguard that there is always at least 1 diagram left
     * - If the current diagram has been deleted, select a new one to show
     * - Trigger the explorer when a diagram is deleted
     * - Set the needsSave (dirty) flag of the projectmanager
     * </pre>
     *
     * @param obj The object to be deleted
     * @see org.argouml.kernel.Project#trashInternal(Object)
     */
    public void moveToTrash(Object obj) {
        if (obj instanceof Collection) {
            Iterator i = ((Collection) obj).iterator();
            while (i.hasNext()) {
                Object trash = i.next();
                if (!trashcan.contains(trash)) {
                    trashInternal(trash);
                }
            }
        } else {
            if (!trashcan.contains(obj)) {
                trashInternal(obj);
            }
        }
    }

    /**
     * Removes some object from the project.
     *
     * @param obj the object to be thrown away
     */
    protected void trashInternal(Object obj) {
        if (Model.getFacade().isAModel(obj)) {
            return; //Can not delete the model
        }

        if (obj != null) {
            trashcan.add(obj);
        }
        if (Model.getFacade().isAUMLElement(obj)) {

            Model.getUmlFactory().delete(obj);

            if (obj instanceof ProjectMember
                    && members.contains(obj)) {
                // TODO: Bob says - can this condition ever be reached?
                // Surely obj cannot be both a model element (previous if) and
                // a ProjectMember (this if)
                members.remove(obj);
            }

            // TODO: Presumably this is only relevant if
            // obj is actually a Model.
            // An added test of Model.getFacade.isAModel(obj) would clarify what
            // is going on here.
            if (models.contains(obj)) {
                models.remove(obj);
            }
        } else if (obj instanceof ArgoDiagram) {
            TargetManager.getInstance().removeTarget(obj);
            TargetManager.getInstance().removeHistoryElement(obj);
            removeProjectMemberDiagram((ArgoDiagram) obj);
            // only need to manually delete diagrams because they
            // don't have a decent event system set up.
            ExplorerEventAdaptor.getInstance().modelElementRemoved(obj);
        } else if (obj instanceof Fig) {
            TargetManager.getInstance().removeTarget(obj);
            TargetManager.getInstance().removeHistoryElement(obj);
            ((Fig) obj).deleteFromModel();
            // TODO: Bob says - I've never seen this appear in the log.
            // I believe this code is never reached. If we delete a FigEdge
            // or FigNode we actually call this method with the owner not
            // the Fig itself.
            // For Figs with no owner (primitives like circle etc) then they
            // are not deleted (crtl-Del) they are removed (Del)
            LOG.error("Request to delete a Fig " + obj.getClass().getName());
        } else if (obj instanceof CommentEdge) {
            TargetManager.getInstance().removeTarget(obj);
            TargetManager.getInstance().removeHistoryElement(obj);
            CommentEdge ce = (CommentEdge) obj;
            LOG.info("Removing the link from " + ce.getAnnotatedElement()
                    + " to " + ce.getComment());
            Model.getCoreHelper().removeAnnotatedElement(
                    ce.getComment(), ce.getAnnotatedElement());
        }
    }

    /**
     * @param obj the object
     * @return true if the object is trashed
     */
    public boolean isInTrash(Object obj) {
        return trashcan.contains(obj);
    }

    /**
     * @param theDefaultModel a uml model
     */
    public void setDefaultModel(Object theDefaultModel) {

        if (!Model.getFacade().isAModel(theDefaultModel)) {
            throw new IllegalArgumentException(
                    "The default model must be a Model type. Received a "
                    + theDefaultModel.getClass().getName());
        }

        defaultModel = theDefaultModel;
        defaultModelTypeCache = new HashMap();
    }

    /**
     * Get the default model.
     *
     * @return A model.
     */
    public Object getDefaultModel() {
        return defaultModel;
    }

    /**
     * Find a type by name in the default model.
     *
     * @param name the name.
     * @return the type.
     */
    public Object findTypeInDefaultModel(String name) {
        if (defaultModelTypeCache.containsKey(name)) {
            return defaultModelTypeCache.get(name);
        }

        Object result = findTypeInModel(name, getDefaultModel());
        defaultModelTypeCache.put(name, result);
        return result;
    }

    /**
     * Returns the root.
     * @return MModel
     */
    public Object getRoot() {
        return Model.getModelManagementFactory().getRootModel();
    }

    /**
     * Sets the root.
     * @param root The root to set, a uml model
     */
    public void setRoot(Object root) {

        if (root == null) {
            throw new IllegalArgumentException(
        	    "A root model element is required");
        }
        if (!Model.getFacade().isAModel(root)) {
            throw new IllegalArgumentException(
        	    "The root model element must be a model - got "
        	    + root.getClass().getName());
        }

        Object treeRoot = Model.getModelManagementFactory().getRootModel();
        if (treeRoot != null) {
            models.remove(treeRoot);
        }
        Model.getModelManagementFactory().setRootModel(root);
        addModel(root);
    }

    /**
     * Returns true if the given name is a valid name for a diagram. Valid means
     * that it does not occur as a name for a diagram yet.
     * @param name The name to test
     * @return boolean True if there are no problems with this name, false if
     * it's not valid.
     */
    public boolean isValidDiagramName(String name) {
        Iterator it = diagrams.iterator();
        boolean rv = true;
        while (it.hasNext()) {
            ArgoDiagram diagram = (ArgoDiagram) it.next();
            if (diagram.getName().equals(name)) {
                rv = false;
                break;
            }
        }
        return rv;
    }

    /**
     * Returns the searchpath.
     * @return Vector
     */
    public Vector getSearchpath() {
        return searchpath;
    }

    /**
     * Returns the uri.
     * @return URI
     */
    public URI getUri() {
        return uri;
    }

    /**
     * Returns the uUIDRefs.
     * @return HashMap
     */
    public Map getUUIDRefs() {
        return uuidRefs;
    }

    /**
     * Sets the searchpath.
     * @param theSearchpath The searchpath to set
     */
    public void setSearchpath(Vector theSearchpath) {
        this.searchpath = theSearchpath;
    }

    /**
     * Sets the uUIDRefs.
     * @param uUIDRefs The uUIDRefs to set
     */
    public void setUUIDRefs(HashMap uUIDRefs) {
        uuidRefs = uUIDRefs;
    }

    /**
     * Sets the vetoSupport.
     * @param theVetoSupport The vetoSupport to set
     */
    public void setVetoSupport(VetoableChangeSupport theVetoSupport) {
        this.vetoSupport = theVetoSupport;
    }

    /**
     * Get the current viewed diagram.
     *
     * @return the current viewed diagram
     */
    public ArgoDiagram getActiveDiagram() {
        return activeDiagram;
    }

    /**
     * @param theDiagram the ArgoDiagram
     */
    public void setActiveDiagram(ArgoDiagram theDiagram) {
        activeDiagram = theDiagram;
    }

    public void targetAdded(TargetEvent e) {
    	setTarget(e.getNewTarget());
    }

    public void targetRemoved(TargetEvent e) {
    	setTarget(e.getNewTarget());
    }

    public void targetSet(TargetEvent e) {
    	setTarget(e.getNewTarget());
    }

    /**
     * Called to update the current namespace and active diagram after
     * the target has changed.
     *
     * TODO: The parameter is not used. Why?
     * @param target Not used.
     */
    private void setTarget(Object target) {
        Object theCurrentNamespace = null;
        target = TargetManager.getInstance().getModelTarget();
        if (target instanceof UMLDiagram) {
            theCurrentNamespace = ((UMLDiagram) target).getNamespace();
        } else if (Model.getFacade().isANamespace(target)) {
            theCurrentNamespace = target;
        } else if (Model.getFacade().isAModelElement(target)) {
            theCurrentNamespace = Model.getFacade().getNamespace(target);
        } else {
            theCurrentNamespace = getRoot();
        }
        setCurrentNamespace(theCurrentNamespace);

        if (target instanceof ArgoDiagram) {
            activeDiagram = (ArgoDiagram) target;
        }
    }

    /**
     * Remove the project.
     */
    public void remove() {

        for (Iterator it = diagrams.iterator(); it.hasNext();) {
            Diagram diagram = (Diagram) it.next();
            diagram.remove();
        }

        members.clear();

        for (Iterator it = models.iterator(); it.hasNext();) {
            Object model = it.next();
            LOG.debug("Deleting project model "
                    + Model.getFacade().getName(model));
            Model.getUmlFactory().delete(model);
        }
        models.clear();

        if (defaultModel != null) {
            LOG.debug("Deleting profile model "
                    + Model.getFacade().getName(defaultModel));
            Model.getUmlFactory().delete(defaultModel);
            defaultModel = null;
        }

        diagrams.clear();

        if (uuidRefs != null) {
            uuidRefs.clear();
        }

        if (defaultModelTypeCache != null) {
            defaultModelTypeCache.clear();
        }

        uuidRefs = null;
        defaultModelTypeCache = null;

        uri = null;
        authorname = null;
        authoremail = null;
        description = null;
        version = null;
        searchpath = null;
        historyFile = null;
        defaultModel = null;
        currentNamespace = null;
        cgPrefs = null;
        vetoSupport = null;
        activeDiagram = null;

        TargetManager.getInstance().removeTargetListener(this);
        trashcan.clear();
    }

    /**
     * Used by "argo.tee".
     * 
     * @return Returns the persistenceVersion.
     */
    public int getPersistenceVersion() {
        return persistenceVersion;
    }

    /**
     * @param pv The persistenceVersion to set.
     */
    public void setPersistenceVersion(int pv) {
        persistenceVersion = pv;
    }

    /**
     * @return Returns the profile.
     */
    public ProfileConfiguration getProfileConfiguration() {
        return profileConfiguration;
    }

    /**
     * Sets the profile configuration
     */
    public void setProfileConfiguration(ProfileConfiguration pc) {
	if (this.profileConfiguration != null) {
	    this.members.remove(this.profileConfiguration);	    
	}
	
	this.profileConfiguration = pc;

	// there's just one ProfileConfiguration in a project
        // and there's no other way to add another one
	members.add(pc);	
    }
    
    /**
     * Repair all parts of the project before a save takes place.
     * @return a report of any fixes
     */
    public String repair() {
        String report = "";
        Iterator it = members.iterator();
        while (it.hasNext()) {
            ProjectMember member = (ProjectMember) it.next();
            report += member.repair();
        }
        return report;
    }

    /**
     * Used by "argo.tee".
     * 
     * @return the settings of this project
     */
    public ProjectSettings getProjectSettings() {
        return projectSettings;
    }

} /* end class Project */
