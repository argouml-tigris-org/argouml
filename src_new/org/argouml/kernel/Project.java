// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import java.beans.VetoableChangeSupport;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.application.ArgoVersion;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ProfileJava;
import org.argouml.uml.ProjectMemberModel;
import org.argouml.uml.UMLChangeRegistry;
import org.argouml.uml.cognitive.ProjectMemberTodoList;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram;
import org.argouml.uml.generator.GenerationPreferences;
import org.argouml.util.ChangeRegistry;
import org.argouml.util.FileConstants;
import org.argouml.util.Trash;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.util.Util;

/**
 * The Project is a datastructure that represents the designer's 
 * current project. It manages the list of diagrams and UML models.
 */
public class Project implements java.io.Serializable, TargetListener {

    /** logger */
    private static final Logger LOG = Logger.getLogger(Project.class);
    
    ////////////////////////////////////////////////////////////////
    // constants

    /**
     * Default name for a project.
     */
    private static final String UNTITLED_FILE = "Untitled";

    ////////////////////////////////////////////////////////////////
    // static variables
    
    static final long serialVersionUID = 1399111233978692444L;

    ////////////////////////////////////////////////////////////////
    // instance variables

    /** TODO: should just be the directory to write
     */
    private URL url;
    private ChangeRegistry saveRegistry;

    private String authorname;
    private String description;
    private String version;

    private Vector searchpath;
    private Vector members;
    private String historyFile;

    /**
     * The version number of the persistence format that last
     * saved this project.
     */
    private int persistenceVersion;

    /**
     * Instances of the uml model.
     */
    private Vector models;

    /**
     * Instances of the uml diagrams.
     */
    private Vector diagrams;
    private Object defaultModel;
    private boolean needsSave;
    private Object currentNamespace;
    private HashMap uuidRefs;
    private GenerationPreferences cgPrefs;
    private transient VetoableChangeSupport vetoSupport;

    /**
     * The root of the modeltree the user is working on. (The untitled_model in
     * the navpane).
     */
    private Object treeRoot;
    
    /**
     * The active diagram, pointer to a diagram in the list with diagrams.
     */
    private ArgoDiagram activeDiagram;

    /** Cache for the default model.
     */
    private HashMap defaultModelCache;

    /**
     * Constructor.
     * 
     * @param file File to read from.
     * @throws MalformedURLException if the file name is incorrect.
     * @throws IOException if we cannot read the file.
     */
    public Project(File file) throws MalformedURLException, IOException {
        this(Util.fileToURL(file));
    }

    /**
     * Constructor.
     *
     * @param theProjectUrl Url to read the project from.
     */
    public Project(URL theProjectUrl) {
        this();
        url = Util.fixURLExtension(theProjectUrl, 
                FileConstants.COMPRESSED_FILE_EXT);
    }

    /**
     * Constructor.
     */
    public Project() {
        
        authorname = "";
        description = "";
        // this should be moved to a ui action.
        version = ArgoVersion.getVersion();
        
        searchpath = new Vector();
        members = new Vector();
        historyFile = "";
        models = new Vector();
        diagrams = new Vector();
        cgPrefs = new GenerationPreferences();
        defaultModelCache = new HashMap();
        
        saveRegistry = new UMLChangeRegistry();
        LOG.info("making empty project with empty model");
        // Jaap Branderhorst 2002-12-09
        // load the default model
        // this is NOT the way how it should be since this makes argo
        // depend on Java even more.
        setDefaultModel(ProfileJava.loadProfileModel());
        addSearchPath("PROJECT_DIR");
        setNeedsSave(false);
        TargetManager.getInstance().addTargetListener(this);

    }

    /**
     * Makes a just created project to an untitled project with a
     * class diagram and a usecase diagram and an untitled model.
     */
    protected void makeUntitledProject() {
        if (getRoot() != null)
            throw new IllegalStateException(
                    "Tried to make a non-empty project "
                    + "to an untitled project");
        Object model =
            UmlFactory.getFactory().getModelManagement().createModel();
        ModelFacade.setName(model, "untitledModel");
        setRoot(model);
        setCurrentNamespace(model);
        addMember(new ProjectMemberTodoList("", this));
        addMember(new UMLClassDiagram(model));
        addMember(new UMLUseCaseDiagram(model));
        setNeedsSave(false);       
        setActiveDiagram((ArgoDiagram) getDiagrams().get(0));
    }
 
    /**
     * Constructor.
     * 
     * @param model The new model.
     */
    public Project(Object model) {
        this();
        
        if (!ModelFacade.isAModel(model)) {
            throw new IllegalArgumentException();
        }
        
        LOG.info("making empty project with model: "
		        + ModelFacade.getName(model));
        setRoot(model);
        setCurrentNamespace(model);
        setNeedsSave(false);
    }
    
    /**
     * Find the base name of this project.<p>This is the name minus
     * any file extension.
     * 
     * @return The name (a String).
     */
    public String getBaseName() {
        String n = getName();

        if (n.endsWith(FileConstants.COMPRESSED_FILE_EXT)) {
            return n.substring(0,
			       n.length()
			       - FileConstants.COMPRESSED_FILE_EXT.length());
        }
        if (n.endsWith(FileConstants.UNCOMPRESSED_FILE_EXT)) {
            return n.substring(0,
			       n.length()
			       - FileConstants.UNCOMPRESSED_FILE_EXT.length());
        }
        return n;
    }

    /**
     * @return the name of the project
     */
    public String getName() {
        // TODO: maybe separate name
        if (url == null)
            return UNTITLED_FILE;
        String name = url.getFile();
        int i = name.lastIndexOf('/');
        return name.substring(i + 1);
    }

    /**
     * Set the project URL.
     * 
     * @param n The new URL (as a String).
     * @throws MalformedURLException if the argument cannot be converted to 
     *         an URL.
     */
    public void setName(String n)
        throws MalformedURLException {
        String s = "";
        if (getURL() != null)
            s = getURL().toString();
        s = s.substring(0, s.lastIndexOf("/") + 1) + n;
        setURL(new URL(s));
    }

    /**
     * Get the URL for this project.
     * 
     * @return The URL.
     */
    public URL getURL() {
        return url;
    }

    /**
     * Set the URL for this project.
     * 
     * @param theUrl The URL to set.
     */
    public void setURL(URL theUrl) {
        if (theUrl != null) {
            theUrl = Util.fixURLExtension(theUrl, 
                    FileConstants.COMPRESSED_FILE_EXT);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Setting project URL from \"" + url 
                      + "\" to \"" + theUrl + "\".");
        }

        url = theUrl;
    }

    /**
     * Set the project file.
     * 
     * This only works if it is possible to convert the File to an url.
     *
     * @param file File to set the project to.
     */
    public void setFile(File file) {
        try {
            URL theProjectUrl = Util.fileToURL(file);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Setting project file name from \"" 
                          + url 
                          + "\" to \"" 
                          + theProjectUrl 
                          + "\".");
            }

            url = theProjectUrl;
        } catch (MalformedURLException murle) {
            LOG.error("problem in setFile:" + file, murle);
        } catch (IOException ex) {
            LOG.error("problem in setFile:" + file, ex);
        }
    }

    /**
     * @return the search path
     */
    public Vector getSearchPath() {
        return searchpath;
    }

    /**
     * @param searchPathElement the element to be added to the searchpath
     */
    public void addSearchPath(String searchPathElement) {
        this.searchpath.addElement(searchPathElement);
    }

    /**
     * Get all members of the project.
     * 
     * @return a Vector with all members.
     */
    public Vector getMembers() {
        return members;
    }

    /**
     * @param d the diagram
     */
    public void addMember(ArgoDiagram d) {
        ProjectMember pm = new ProjectMemberDiagram(d, this);
        addDiagram(d);
        // if diagram added successfully, add the member too
        members.addElement(pm);
    }

    /**
     * @param pm the member to be added
     */
    public void addMember(ProjectMemberTodoList pm) {
        Iterator iter = members.iterator();
        Object currentMember = null;
        while (iter.hasNext()) {
            currentMember = iter.next();
            if (currentMember instanceof ProjectMemberTodoList) {
                /* No need to have several of these */
                return;
            }
        }
        // got past the veto, add the member
        members.addElement(pm);
    }

    /**
     * @param m the member to be added
     */
    public void addMember(Object m) {
        
        if (!ModelFacade.isAModel(m)) {
            throw new IllegalArgumentException(
                "The member must be a UML model. It is " 
                    + m.getClass().getName());
        }
        
        Iterator iter = members.iterator();
        Object currentMember = null;
        boolean memberFound = false;
        while (iter.hasNext()) {
            currentMember = iter.next();
            if (currentMember instanceof ProjectMemberModel) {
                Object currentModel =
                    ((ProjectMemberModel) currentMember).getModel();
                if (currentModel == m) {
                    memberFound = true;
                    break;
                }
            }
        }
        if (!memberFound) {
            if (!models.contains(m)) {
                addModel(m);
            }
            // got past the veto, add the member
            ProjectMember pm = new ProjectMemberModel(m, this);
            members.addElement(pm);
        }
    }

    /**
     * @param m a namespace
     */
    public void addModel(Object m) {
        
        if (!ModelFacade.isANamespace(m)) {
            throw new IllegalArgumentException();
	}
        
        // fire indeterminate change to avoid copying vector
        if (!models.contains(m))
            models.addElement(m);
        setCurrentNamespace(m);
        setNeedsSave(true);
    }

    /**
     * Removes a project member diagram completely from the project.
     * @param d the ArgoDiagram
     */
    protected void removeProjectMemberDiagram(ArgoDiagram d) {

        removeDiagram(d);

        // should remove the corresponding ProjectMemberDiagram not
        // the ArgoDiagram from the members

        // _members.removeElement(d);
        // ehhh lets remove the diagram really and remove it from its
        // corresponding projectmember too
        Iterator it = members.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof ProjectMemberDiagram) {
                ProjectMemberDiagram pmd = (ProjectMemberDiagram) obj;
                if (pmd.getDiagram() == d) {
                    members.removeElement(pmd);
                    break;
                }
            }
        }
    }

    /**
     * @param name the name of the member to be found
     * @return the member
     */
    public ProjectMember findMemberByName(String name) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("findMemberByName called for \"" + name + "\".");
        }
        for (int i = 0; i < members.size(); i++) {
            ProjectMember pm = (ProjectMember) members.elementAt(i);
            if (name.equals(pm.getPlainName()))
                return pm;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Member \"" + name + "\" not found.");
        }
        return null;
    }

    /**
     * Get the author name.
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
    public void setAuthorname(String s) {
        authorname = s;
    }

    /**
     * Get the version.
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
    public void setDescription(String s) {
        description = s;
    }

    /**
     * Get the history file.
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
     * Set the needs-to-be-saved flag.
     * 
     * @param newValue The new value.
     */
    public void setNeedsSave(boolean newValue) {
        saveRegistry.setChangeFlag(newValue);
    }
    
    /**
     * Test if the model needs to be saved.
     * 
     * @return <tt>true</tt> if the model needs to be saved.
     */
    public boolean needsSave() {
        return saveRegistry.hasChanged();
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
     * If there isn't exactly one model, <tt>null</tt> is returned.
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
     * Searches for a type/classifier with name s. If the type is not found,
     * a new type is created and added to the current namespace.
     * @param s the name of the type/classifier to be found
     * @return MClassifier
     */
    public Object findType(String s) {
        return findType(s, true);
    }

    /**
     * Searches for a type/classifier with name s. If defineNew is
     * true, a new type is defined if the type/classifier is not
     * found. The newly created type is added to the currentNamespace
     * and given the name s.
     * @param s the name of the type/classifier to be found
     * @param defineNew if true, define a new one 
     * @return MClassifier the found classifier
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
        cls = findTypeInModel(s, defaultModel);
        // hey, now we should move it to the model the user is working in
        if (cls != null) {
            cls = ModelManagementHelper.
                getHelper().getCorrespondingElement(cls, getRoot());
        }
        if (cls == null && defineNew) {
            LOG.debug("new Type defined!");
            cls = UmlFactory.getFactory().getCore()
        		  .buildClass(getCurrentNamespace());
            ModelFacade.setName(cls, s);
        }
        return cls;
    }

    /**
     * Finds all figs on the diagrams for some project member,
     * including the figs containing the member (so for some
     * operation, the containing figclass is returned).
     * 
     * @param member The member we are looking for. 
     *               This can be a NSUML object but also another object.
     * @return Collection The collection with the figs.
     */
    public Collection findFigsForMember(Object member) {
        Collection figs = new ArrayList();
        Iterator it = getDiagrams().iterator();
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
     * Finds a classifier with a certain name.<p>
     * 
     * Will only return first classifier with the matching name.
     *
     * @param s is short name.
     * @param ns Namespace where we do the search.
     * @return the found classifier (or <tt>null</tt> if not found).
     */
    public Object findTypeInModel(String s, Object ns) {
        
        if (!ModelFacade.isANamespace(ns)) {
            throw new IllegalArgumentException();
    	}
        
        Collection allClassifiers =
            ModelManagementHelper.getHelper()
	        .getAllModelElementsOfKind(ns, (Class) ModelFacade.CLASSIFIER);
        
        Object[] classifiers = allClassifiers.toArray();
        Object classifier = null;
        
        for (int i = 0; i < classifiers.length; i++) {
            
            classifier = classifiers[i];
            if (ModelFacade.getName(classifier) != null
            		&& ModelFacade.getName(classifier).equals(s)) {
                return classifier;
            }
        }
        
        return null;
    }

    /**
     * @param m the namespace
     */
    public void setCurrentNamespace(Object m) {
        
        if (m != null && !ModelFacade.isANamespace(m)) {
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
     */
    public Vector getDiagrams() {
        return diagrams;
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
        diagrams.addElement(d);
        d.addChangeRegistryAsListener(saveRegistry);
        setNeedsSave(true);
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
        diagrams.removeElement(d);
        // if the diagram is a statechart,
        // remove its associated statemachine model elements
        if (d instanceof UMLStateDiagram) {
            UMLStateDiagram statediagram = (UMLStateDiagram) d;
            // if the statemachine has already been deleted,
            // and is now null,
            // don't attempt to delete it!
            if (statediagram.getStateMachine() != null) {
                this.moveToTrash(statediagram.getStateMachine());
            }
        }
        d.removeChangeRegistryAsListener(saveRegistry);
        setNeedsSave(true);
    }

    /**
     * @param me the given modelelement
     * @return the total number of presentation 
     *         for the given modelelement in the project
     */
    public int getPresentationCountFor(Object me) {
        
        if (!ModelFacade.isAModelElement(me)) {
            throw new IllegalArgumentException();
    	}
        
        int presentations = 0;
        int size = diagrams.size();
        for (int i = 0; i < size; i++) {
            Diagram d = (Diagram) diagrams.elementAt(i);
            presentations += d.getLayer().presentationCountFor(me);
        }
        return presentations;
    }

    /**
     * @return an initial target, in casu a diagram or a model
     */
    public Object getInitialTarget() {
        if (diagrams.size() > 0) {
            return diagrams.elementAt(0);
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
            ((Diagram) diagrams.elementAt(i)).preSave();
        }
        // TODO: is preSave needed for models?
    }

    /**
     * This is execcuted after a save.
     */
    public void postSave() {
        for (int i = 0; i < diagrams.size(); i++) {
            ((Diagram) diagrams.elementAt(i)).postSave();
        }
        // TODO: is postSave needed for models?
        setNeedsSave(false);
    }

    /**
     * This is executed after a load.
     */
    protected void postLoad() {
        for (int i = 0; i < diagrams.size(); i++) {
            ((Diagram) diagrams.elementAt(i)).postLoad();
        }
        // issue 1725: the root is not set, which leads to problems
        // with displaying prop panels
        setRoot( getModel());
        
        setNeedsSave(false);
        // we don't need this HashMap anymore so free up the memory
        uuidRefs = null;
    }

    ////////////////////////////////////////////////////////////////
    // trash related methods

    /**
     * Moves some object to trash. 
     *
     * TODO: This mechanism must be rethought since it only deletes an
     * object completely from the project
     *
     * @param obj The object to be deleted
     * @see org.argouml.kernel.Project#trashInternal(Object)
     */
    // Attention: whole Trash mechanism should be rethought concerning nsuml
    public void moveToTrash(Object obj) {
        if (Trash.SINGLETON.contains(obj)) {
            return;
        }
        trashInternal(obj);
    }

    /**
     * Removes some object from the project. Does not update GUI since
     * this method only handles project management.
     * @param obj
     *
     * <p>Attention: whole Trash mechanism should be rethought concerning nsuml
     * 
     * <p>Note that at present these are all if, not
     * else-if. Rather than make a big change, I've just explicitly dealt with
     * the case where we have a use case that is not classifier.
     */
    protected void trashInternal(Object obj) {
        boolean needSave = false;
        if (obj != null) {
            TargetManager.getInstance().removeTarget(obj);
            TargetManager.getInstance().removeHistoryElement(obj);
            Trash.SINGLETON.addItemFrom(obj, null);
        }
        if (ModelFacade.isABase(obj)) { // an object that can be represented
            ProjectBrowser.getInstance().getEditorPane()
        		.removePresentationFor(obj, getDiagrams());
            // UmlModelEventPump.getPump().stopPumpingEvents();
            UmlFactory.getFactory().delete(obj);
            // UmlModelEventPump.getPump().startPumpingEvents();
            if (members.contains(obj)) {
                members.remove(obj);
            }
            if (models.contains(obj)) {
                models.remove(obj);
            }           
            needSave = true;
        } else {
            if (obj instanceof ArgoDiagram) {
                removeProjectMemberDiagram((ArgoDiagram) obj);
                needSave = true;
                // only need to manually delete diagrams because they
                // don't have a decent event system set up.
                ExplorerEventAdaptor.getInstance().modelElementRemoved(obj);
            }
            if (obj instanceof Fig) {
                ((Fig) obj).deleteFromModel();
                needSave = true;
                // for explorer deletion:
                obj = ((Fig) obj).getOwner();
            }
        }        
        setNeedsSave(needSave);
    }

    /**
     * @param obj the object to be moved from the trash
     * TODO: Is "move" remove or restore? 
     */
    public void moveFromTrash(Object obj) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("TODO: not restoring " + obj);
        }
    }

    /**
     * @param dm the object 
     * @return true if the object is trashed
     */
    public boolean isInTrash(Object dm) {
        return Trash.SINGLETON.contains(dm);
    }

    /**
     * @param theDefaultModel a uml model
     */
    public void setDefaultModel(Object theDefaultModel) {
        
        if (!ModelFacade.isAModel(theDefaultModel)) {
            throw new IllegalArgumentException();
        }
        
        defaultModel = theDefaultModel;
        defaultModelCache = new HashMap();
    }

    /**
     * Get the default model.
     * 
     * @return A model.
     */
    public Object getDefaultModel() {
        return defaultModel;
    }

    /** Find a type by name in the default model.
     *
     * @param name the name.
     * @return the type.
     */
    public Object findTypeInDefaultModel(String name) {
        if (defaultModelCache.containsKey(name)) {
            return defaultModelCache.get(name);
        }
        
        Object result = findTypeInModel(name, getDefaultModel());
        defaultModelCache.put(name, result);
        return result;
    }

    /**
     * Returns the root.
     * @return MModel
     */
    public Object getRoot() {
        return treeRoot;
    }

    /**
     * Sets the root.
     * @param root The root to set, a uml model
     */
    public void setRoot(Object root) {
        
        if (!ModelFacade.isAModel(root)) {
            throw new IllegalArgumentException();
        }
        
        if (treeRoot != null) {
            members.remove(treeRoot);
            models.remove(treeRoot);
        }
        treeRoot = root;
        addMember(root);
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
        Iterator it = getDiagrams().iterator();
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
     * Returns the cgPrefs.
     * @return GenerationPreferences
     */
    public GenerationPreferences getCgPrefs() {
        return cgPrefs;
    }

    /**
     * Returns the needsSave.
     * @return boolean
     */
    public boolean isNeedsSave() {
        return needsSave;
    }

    /**
     * Returns the saveRegistry.
     * @return ChangeRegistry
     */
    public ChangeRegistry getSaveRegistry() {
        return saveRegistry;
    }

    /**
     * Returns the searchpath.
     * @return Vector
     */
    public Vector getSearchpath() {
        return searchpath;
    }

    /**
     * Returns the url.
     * @return URL
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Returns the uUIDRefs.
     * @return HashMap
     */
    public HashMap getUUIDRefs() {
        return uuidRefs;
    }

    /**
     * Sets the cgPrefs.
     * @param theCgPrefs The cgPrefs to set
     */
    public void setCgPrefs(GenerationPreferences theCgPrefs) {
        this.cgPrefs = theCgPrefs;
    }

    /**
     * Sets the members.
     * @param theMembers The members to set
     */
    public void setMembers(Vector theMembers) {
        this.members = theMembers;
    }

    /**
     * Sets the saveRegistry.
     * @param theSaveRegistry The saveRegistry to set
     */
    public void setSaveRegistry(ChangeRegistry theSaveRegistry) {
        saveRegistry = theSaveRegistry;
    }

    /**
     * Sets the searchpath.
     * @param theSearchpath The searchpath to set
     */
    public void setSearchpath(Vector theSearchpath) {
        this.searchpath = theSearchpath;
    }

    /**
     * Sets the url.
     * @param theUrl The url to set
     */
    public void setUrl(URL theUrl) {
        url = theUrl;
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
     * Get the current viewed diagram
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

    /** 
     * @see TargetListener#targetAdded(TargetEvent)
     */
    public void targetAdded(TargetEvent e) {       
    	setTarget(e.getNewTarget());
    }

    /** 
     * @see TargetListener#targetRemoved(TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
    	setTarget(e.getNewTarget());
    }

    /** 
     * @see TargetListener#targetSet(TargetEvent)
     */
    public void targetSet(TargetEvent e) {
    	setTarget(e.getNewTarget());
    }
    
    /**
     * Called to update the current namespace and active diagram after
     * the target has changed.
     * @param target
     */
    private void setTarget(Object target) {
        Object theCurrentNamespace = null;
        target = TargetManager.getInstance().getModelTarget();
        if (target instanceof UMLDiagram) {
            theCurrentNamespace = ((UMLDiagram) target).getNamespace();
        } else if (ModelFacade.isANamespace(target)) {
            theCurrentNamespace = target;
        } else if (ModelFacade.isAModelElement(target)) {
            theCurrentNamespace = ModelFacade.getNamespace(target);
        } else {
            theCurrentNamespace = getRoot();
        }
        setCurrentNamespace(theCurrentNamespace);
               
        if (target instanceof ArgoDiagram) {
            setActiveDiagram((ArgoDiagram) target);
        }                  
    }

    /**
     * Remove the project.
     */
    public void remove() {
        
        if (members != null) {
            Iterator membersIt = members.iterator();
            while (membersIt.hasNext()) {
                ((ProjectMember) membersIt.next()).remove();
            }
            members.clear();
        }
        
        if (models != null) {
            models.clear();
        }
        
        if (diagrams != null) {
            diagrams.clear();
        }
        
        if (uuidRefs != null) {
            uuidRefs.clear();
        }
        
        if (defaultModelCache != null) {
            defaultModelCache.clear();
        }
        
        members = null;
        models = null;
        diagrams = null;
        uuidRefs = null;
        defaultModelCache = null;
        
        url = null;
        saveRegistry = null;
        authorname = null;
        description = null;
        version = null;
        searchpath = null;
        historyFile = null;
        defaultModel = null;
        currentNamespace = null;
        cgPrefs = null;
        vetoSupport = null;
        treeRoot = null;
        activeDiagram = null;
        defaultModelCache = null;
        
        TargetManager.getInstance().removeTargetListener(this);
    }
    
    /**
     * @return Returns the persistenceVersion.
     */
    public int getPersistenceVersion() {
        return persistenceVersion;
    }
    
    /**
     * @param pv The persistenceVersion to set.
     */
    public void setPersistenceVersion(int pv) {
        this.persistenceVersion = pv;
    }
} /* end class Project */
