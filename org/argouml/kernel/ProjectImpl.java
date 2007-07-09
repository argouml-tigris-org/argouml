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
import java.beans.PropertyChangeListener;
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
import org.argouml.uml.CommentEdge;
import org.argouml.uml.Profile;
import org.argouml.uml.ProfileException;
import org.argouml.uml.ProfileJava;
import org.argouml.uml.ProjectMemberModel;
import org.argouml.uml.cognitive.ProjectMemberTodoList;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.generator.GenerationPreferences;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.undo.Memento;
import org.tigris.gef.undo.UndoManager;

/**
 * The ProjectImpl is a data structure that represents the designer's
 * current project. It manages the list of diagrams and UML models.
 * <p>
 * NOTE: This was named Project until 0.25.4 when it was replaced by an
 * interface of the same name and renamed to ProjectImpl.
 */
public class ProjectImpl implements java.io.Serializable, Project {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ProjectImpl.class);

    /**
     * Default name for a project.
     */
    private static final String UNTITLED_FILE =
	Translator.localize("label.projectbrowser-title");

    /**
     * The UID.
     */
    static final long serialVersionUID = 1399111233978692444L;

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

    private Profile profile;

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
    public ProjectImpl(URI theProjectUri) {
        this();
        uri = PersistenceManager.getInstance().fixUriExtension(theProjectUri);
    }

    /**
     * Constructor.
     */
    public ProjectImpl() {
        profile = new ProfileJava();
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
        try {
            // Jaap Branderhorst 2002-12-09
            // load the default model
            // this is NOT the way how it should be since this makes argo
            // depend on Java even more.
            setDefaultModel(profile.getProfileModel());
        } catch (ProfileException e) {
            // TODO: how are we going to handle exceptions here?
            // I think we need a ProjectException.
            LOG.error("Exception setting the default profile", e);
        }
        addSearchPath("PROJECT_DIR");
    }


    public String getBaseName() {
        String n = getName();
        n = PersistenceManager.getInstance().getBaseName(n);
        return n;
    }


    public String getName() {
        // TODO: maybe separate name
        if (uri == null) {
            return UNTITLED_FILE;
        }
        return new File(uri).getName();
    }


    public void setName(String n)
        throws URISyntaxException {
        String s = "";
        if (getURI() != null) {
            s = getURI().toString();
        }
        s = s.substring(0, s.lastIndexOf("/") + 1) + n;
        setURI(new URI(s));
    }


    public URI getURI() {
        return uri;
    }


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


    public Vector getSearchPath() {
        return searchpath;
    }


    public void addSearchPath(String searchPathElement) {
        if (!this.searchpath.contains(searchPathElement)) {
            searchpath.addElement(searchPathElement);
        }
    }


    public MemberList getMembers() {
        LOG.info("Getting the members there are " + members.size());
        return members;
    }


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


    public String getAuthorname() {
        return authorname;
    }


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


    public String getAuthoremail() {
        return authoremail;
    }


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


    public String getVersion() {
        return version;
    }


    public void setVersion(String s) {
        version = s;
    }


    public String getDescription() {
        return description;
    }


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


    public String getHistoryFile() {
        return historyFile;
    }


    public void setHistoryFile(String s) {
        historyFile = s;
    }


    public Vector getUserDefinedModels() {
        return models;
    }


    public Collection getModels() {
        Set ret = new HashSet();
        ret.addAll(models);
        ret.add(defaultModel);
        return ret;
    }


    public Object getModel() {
        if (models.size() != 1) {
            return null;
        }
        return models.elementAt(0);
    }


    public Object findType(String s) {
        return findType(s, true);
    }


    public Object getDefaultAttributeType() {
        // TODO: Move this to a profile - tfm - 20070307
        return findType("int");
    }


    public Object getDefaultParameterType() {
        // TODO: Move this to a profile - tfm - 20070307
        return findType("int");
    }
    

    public Object getDefaultReturnType() {
        // TODO: Move this to a profile - tfm - 20070307
        return findType("void");
    }


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


    public void setCurrentNamespace(Object m) {

        if (m != null && !Model.getFacade().isANamespace(m)) {
            throw new IllegalArgumentException();
    	}

        currentNamespace = m;
    }


    public Object getCurrentNamespace() {
        return currentNamespace;
    }


    public Vector getDiagrams() {
        return new Vector(diagrams);
    }


    public int getDiagramCount() {
        return diagrams.size();
    }


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


    public void addDiagram(ArgoDiagram d) {
        // send indeterminate new value instead of making copy of vector
	d.setProject(this);
        diagrams.add(d);
        
        // TODO: Remove this next line after GEF 0.12.4M4 
        // is replaced by a newer one - it fixes a GEF bug 
        // when removing a diagram:
        d.addVetoableChangeListener(new Vcl());

        d.addPropertyChangeListener("name", new NamePCL());
        setSaveEnabled(true);
    }

    /**
     * Listener to events from the Diagram class. <p>
     *
     * Purpose: changing the name of a diagram shall set the need save flag.
     *
     * @author Michiel
     */
    private class NamePCL implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            setSaveEnabled(true);
        }
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
        /* Remove the dependent
         * modelelements, such as the statemachine
         * for a statechartdiagram:
         */
        Object o = d.getDependentElement();
        if (o != null) {
            moveToTrash(o);
        }
    }

    /**
     * TODO: Remove this class after GEF 0.12.4M4 is replaced by a newer one
     *
     * @author Michiel
     */
    private class Vcl implements VetoableChangeListener {
        public void vetoableChange(PropertyChangeEvent evt)
            throws PropertyVetoException {
            //
        }
    }


    public int getPresentationCountFor(Object me) {

        if (!Model.getFacade().isAUMLElement(me)) {
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


    public Object getInitialTarget() {
        if (diagrams.size() > 0) {
            return diagrams.get(0);
        }
        if (models.size() > 0) {
            return models.elementAt(0);
        }
        return null;
    }


    public void setGenerationPrefs(GenerationPreferences cgp) {
        cgPrefs = cgp;
    }


    public GenerationPreferences getGenerationPrefs() {
        return cgPrefs;
    }


    public VetoableChangeSupport getVetoSupport() {
        if (vetoSupport == null) {
            vetoSupport = new VetoableChangeSupport(this);
        }
        return vetoSupport;
    }


    public void preSave() {
        for (int i = 0; i < diagrams.size(); i++) {
            ((Diagram) diagrams.get(i)).preSave();
        }
        // TODO: is preSave needed for models?
    }


    public void postSave() {
        for (int i = 0; i < diagrams.size(); i++) {
            ((Diagram) diagrams.get(i)).postSave();
        }
        // TODO: is postSave needed for models?
        setSaveEnabled(true);
    }


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
            removeProjectMemberDiagram((ArgoDiagram) obj);
            // only need to manually delete diagrams because they
            // don't have a decent event system set up.
            ExplorerEventAdaptor.getInstance().modelElementRemoved(obj);
        } else if (obj instanceof Fig) {
            ((Fig) obj).deleteFromModel();
            // TODO: Bob says - I've never seen this appear in the log.
            // I believe this code is never reached. If we delete a FigEdge
            // or FigNode we actually call this method with the owner not
            // the Fig itself.
            // MVW: This is now called by ActionDeleteModelElements
            // for primitive Figs (without owner).
            LOG.info("Request to delete a Fig " + obj.getClass().getName());
        } else if (obj instanceof CommentEdge) {
            CommentEdge ce = (CommentEdge) obj;
            LOG.info("Removing the link from " + ce.getAnnotatedElement()
                    + " to " + ce.getComment());
            ce.delete();
        }
    }


    public boolean isInTrash(Object obj) {
        return trashcan.contains(obj);
    }


    public void setDefaultModel(Object theDefaultModel) {

        if (!Model.getFacade().isAModel(theDefaultModel)) {
            throw new IllegalArgumentException(
                    "The default model must be a Model type. Received a "
                    + theDefaultModel.getClass().getName());
        }

        defaultModel = theDefaultModel;
        defaultModelTypeCache = new HashMap();
    }


    public Object getDefaultModel() {
        return defaultModel;
    }


    public Object findTypeInDefaultModel(String name) {
        if (defaultModelTypeCache.containsKey(name)) {
            return defaultModelTypeCache.get(name);
        }

        Object result = findTypeInModel(name, getDefaultModel());
        defaultModelTypeCache.put(name, result);
        return result;
    }


    public Object getRoot() {
        return Model.getModelManagementFactory().getRootModel();
    }


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


    public Vector getSearchpath() {
        return searchpath;
    }


    public URI getUri() {
        return uri;
    }


    public Map getUUIDRefs() {
        return uuidRefs;
    }


    public void setSearchpath(Vector theSearchpath) {
        this.searchpath = theSearchpath;
    }


    public void setUUIDRefs(HashMap uUIDRefs) {
        uuidRefs = uUIDRefs;
    }


    public void setVetoSupport(VetoableChangeSupport theVetoSupport) {
        this.vetoSupport = theVetoSupport;
    }


    public ArgoDiagram getActiveDiagram() {
        return activeDiagram;
    }


    public void setActiveDiagram(ArgoDiagram theDiagram) {
        activeDiagram = theDiagram;
    }


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

        trashcan.clear();
    }


    public int getPersistenceVersion() {
        return persistenceVersion;
    }


    public void setPersistenceVersion(int pv) {
        persistenceVersion = pv;
    }


    public Profile getProfile() {
        return profile;
    }


    public String repair() {
        String report = "";
        Iterator it = members.iterator();
        while (it.hasNext()) {
            ProjectMember member = (ProjectMember) it.next();
            report += member.repair();
        }
        return report;
    }


    public ProjectSettings getProjectSettings() {
        return projectSettings;
    }

}
