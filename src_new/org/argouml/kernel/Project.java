// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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

// File: Project.java
// Classes: Project

package org.argouml.kernel;

import java.beans.PropertyVetoException;
import java.beans.VetoableChangeSupport;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ProjectMemberTodoList;
import org.argouml.cognitive.ToDoList;
import org.argouml.cognitive.checklist.ChecklistStatus;
import org.argouml.cognitive.critics.Agency;
import org.argouml.cognitive.critics.Critic;
import org.argouml.cognitive.critics.ui.CriticBrowserDialog;
import org.argouml.cognitive.ui.DesignIssuesDialog;
import org.argouml.cognitive.ui.GoalsDialog;
import org.argouml.cognitive.ui.TabToDo;
import org.argouml.cognitive.ui.ToDoPane;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.FindDialog;
import org.argouml.ui.NavigatorConfigDialog;
import org.argouml.ui.NavigatorPane;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.TabResults;
import org.argouml.ui.UsageStatistic;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ProfileJava;
import org.argouml.uml.ProjectMemberModel;
import org.argouml.uml.UMLChangeRegistry;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.ui.ModeCreateEdgeAndNode;
import org.argouml.uml.diagram.ui.SelectionWButtons;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram;
import org.argouml.uml.generator.GenerationPreferences;
import org.argouml.util.ChangeRegistry;
import org.argouml.util.SubInputStream;
import org.argouml.util.Trash;
import org.argouml.xml.argo.ArgoParser;
import org.argouml.xml.pgml.PGMLParser;
import org.argouml.xml.xmi.XMIReader;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.ocl.OCLExpander;
import org.tigris.gef.ocl.TemplateReader;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.util.Util;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.model_management.MModel;

/** A datastructure that represents the designer's current project.  A
 *  Project consists of diagrams and UML models. */

public class Project implements java.io.Serializable, TargetListener {
    ////////////////////////////////////////////////////////////////
    // constants
    public static final String SEPARATOR = "/";
    public final static String COMPRESSED_FILE_EXT = ".zargo";
    public final static String UNCOMPRESSED_FILE_EXT = ".argo";
    public final static String PROJECT_FILE_EXT = ".argo";
    public final static String TEMPLATES = "/org/argouml/templates/";
    public static String ARGO_TEE = "/org/argouml/xml/dtd/argo.tee";
    //public final static String EMPTY_PROJ = "EmptyProject" + FILE_EXT;
    public final static String UNTITLED_FILE = "Untitled";

    ////////////////////////////////////////////////////////////////
    // static variables
    protected static OCLExpander expander = null;

    ////////////////////////////////////////////////////////////////
    // instance variables

    //public String _pathname = "";
    //public String _filename = UNTITLED_FILE + FILE_EXT;

    //TODO should just be the directory to write
    private URL _url = null;
    protected ChangeRegistry _saveRegistry;

    private String _authorname = "";
    private String _description = "";
    private String _version = "";

    private Vector _searchpath = new Vector();
    private Vector _members = new Vector();
    private String _historyFile = "";

    private Vector _models = new Vector(); //instances of MModel
    private Vector _diagrams = new Vector(); // instances of LayerDiagram
    protected MModel _defaultModel = null;
    private boolean _needsSave = false;
    protected MNamespace _currentNamespace = null;
    private HashMap _UUIDRefs = null;
    private GenerationPreferences _cgPrefs = new GenerationPreferences();
    private transient VetoableChangeSupport _vetoSupport = null;

    /**
     * True if we are in the proces of making a project, otherwise false
     */
    private static boolean _creatingProject;

    /**
     * The root of the modeltree the user is working on. (The untitled_model in
     * the navpane).
     */
    private MModel _root;
    
    /**
     * The active diagram, pointer to a diagram in the list with diagrams.
     */
    private ArgoDiagram _activeDiagram;

    protected static Category cat =
        Category.getInstance(org.argouml.kernel.Project.class);
    ////////////////////////////////////////////////////////////////
    // constructor

    public Project(File file) throws MalformedURLException, IOException {
        this(Util.fileToURL(file));
    }

    public Project(URL url) {
        this();
        _url = Util.fixURLExtension(url, COMPRESSED_FILE_EXT);
        _saveRegistry = new UMLChangeRegistry();

    }

    public Project() {
        _saveRegistry = new UMLChangeRegistry();
        Argo.log.info("making empty project with empty model");
        // Jaap Branderhorst 2002-12-09
        // load the default model
        // this is NOT the way how it should be since this makes argo depend on Java even more.
        setDefaultModel(ProfileJava.loadProfileModel());
        addSearchPath("PROJECT_DIR");
        setNeedsSave(false);
        TargetManager.getInstance().addTargetListener(this);

    }

    /**
     * Makes a just created project to an untitled project with a class diagram and 
     * a usecase diagram and an untitled model.
     */
    protected void makeUntitledProject() {
        if (getRoot() != null)
            throw new IllegalStateException("Tried to make a non-empty project to an untitled project");
        MModel model =
            UmlFactory.getFactory().getModelManagement().createModel();
        model.setName("untitledModel");
        setRoot(model);
        setCurrentNamespace(model);
        addMember(new ProjectMemberTodoList("", this));
        addMember(new UMLClassDiagram(model));
        addMember(new UMLUseCaseDiagram(model));
        setNeedsSave(false);       
        setActiveDiagram((ArgoDiagram)getDiagrams().get(0));
    }
 
    public Project(MModel model) {
        this();
        Argo.log.info("making empty project with model: " + model.getName());
        setRoot(model);
        setCurrentNamespace(model);
        setNeedsSave(false);
        /*
        Runnable resetStatsLater = new ResetStatsLater();
        org.argouml.application.Main.addPostLoadAction(resetStatsLater);
        */

    }
    
    /**
     * Loads a model (XMI only) from a .zargo file. BE ADVISED this method has a side
     * effect. It sets _UUIDREFS to the model.
     * <p>
     * If there is a problem with the xmi file, an error is set in the
     * ArgoParser.SINGLETON.getLastLoadStatus() field. This needs to be
     * examined by the calling function.
     *
     * @deprecated As of ArgoUml version unknown, replaced by unknown.
     * @param url The url with the .zargo file
     * @return MModel The model loaded
     * @throws IOException Thrown if the model or the .zargo file itself is corrupted in any way.
     */
    protected MModel loadModelFromXMI(URL url) throws IOException, SAXException, ParserConfigurationException {
        ZipInputStream zis = new ZipInputStream(url.openStream());

        String name = zis.getNextEntry().getName();
        while (!name.endsWith(".xmi")) {
            name = zis.getNextEntry().getName();
        }
        Argo.log.info("Loading Model from " + url);
        // 2002-07-18
        // Jaap Branderhorst
        // changed the loading of the projectfiles to solve hanging 
        // of argouml if a project is corrupted. Issue 913
        // Created xmireader with method getErrors to check if parsing went well
        XMIReader xmiReader = null;
        try {
            xmiReader = new org.argouml.xml.xmi.XMIReader();
        } catch (SAXException se) { // duh, this must be catched and handled
            cat.error(se);
            throw se;
        } catch (ParserConfigurationException pc) { // duh, this must be catched and handled
            cat.error(pc);
            throw pc;
        }
        MModel mmodel = null;

        InputSource source = new InputSource(zis);
        source.setEncoding("UTF-8");
        mmodel = xmiReader.parseToModel(source);        
        if (xmiReader.getErrors()) {
            ArgoParser.SINGLETON.setLastLoadStatus(false);
            ArgoParser.SINGLETON.setLastLoadMessage(
                "XMI file " + url.toString() + " could not be " + "parsed.");
            cat.error("XMI file " + url.toString() + " could not be " + "parsed.");
            throw new SAXException("XMI file " + url.toString() + " could not be " + "parsed.");
        }

        // This should probably be inside xmiReader.parse
        // but there is another place in this source
        // where XMIReader is used, but it appears to be
        // the NSUML XMIReader.  When Argo XMIReader is used
        // consistently, it can be responsible for loading
        // the listener.  Until then, do it here.
        UmlHelper.getHelper().addListenersToModel(mmodel);

        // if (mmodel != null && !xmiReader.getErrors()) {

        addMember(mmodel);

        //}
        //        else {
        //throw new IOException("XMI file " + url.toString() + 
        //" could not be parsed.");
        //}

        _UUIDRefs = new HashMap(xmiReader.getXMIUUIDToObjectMap());
        return mmodel;
    }

    /**
     * Loads all the members from a zipped input stream.
     *
     * @throws IOException if there is something wrong with the zipped archive
     *                     or with the model.
     * @throws PropertyVetoException if the adding of a diagram is vetoed.
     */
    protected void loadZippedProjectMembers(URL url)
        throws IOException, ParserConfigurationException, SAXException {

        loadModelFromXMI(url); // throws a IOException if things go wrong
        // user interface has to handle that one
        try {

            // now close again, reopen and read the Diagrams.

            PGMLParser.SINGLETON.setOwnerRegistry(_UUIDRefs);

            //zis.close();
            ZipInputStream zis = new ZipInputStream(url.openStream());
            SubInputStream sub = new SubInputStream(zis);

            ZipEntry currentEntry = null;
            while ((currentEntry = sub.getNextEntry()) != null) {
                if (currentEntry.getName().endsWith(".pgml")) {
                    Argo.log.info(
                        "Now going to load "
                            + currentEntry.getName()
                            + " from ZipInputStream");

                    // "false" means the stream shall not be closed, but it doesn't seem to matter...
                    ArgoDiagram d =
                        (ArgoDiagram)PGMLParser.SINGLETON.readDiagram(
                            sub,
                            false);
                    addMember(d);
                    // sub.closeEntry();
                    Argo.log.info("Finished loading " + currentEntry.getName());
                }
                if (currentEntry.getName().endsWith(".todo")) {
                    ProjectMemberTodoList pm =
                        new ProjectMemberTodoList(currentEntry.getName(), this);
                    try {
                        pm.load(sub);
                    } catch (IOException ioe) {
                    } catch (org.xml.sax.SAXException se) {
                    }
                    addMember(pm);
                }
            }
            zis.close();

       } catch (IOException e) {
            ArgoParser.SINGLETON.setLastLoadStatus(false);
            ArgoParser.SINGLETON.setLastLoadMessage(e.toString());
            cat.error(
                "Oops, something went wrong in Project.loadZippedProjectMembers() ",
                e);
            throw e;
        }
    }

    /**
     * Added Eugenio's patches to load 0.8.1 projects.
     */
    public String getBaseName() {
        String n = getName();

        if (n.endsWith(COMPRESSED_FILE_EXT)) {
            return n.substring(0, n.length() - COMPRESSED_FILE_EXT.length());
        }
        if (n.endsWith(UNCOMPRESSED_FILE_EXT)) {
            return n.substring(0, n.length() - UNCOMPRESSED_FILE_EXT.length());
        }
        return n;
    }

    public String getName() {
        // TODO: maybe separate name
        if (_url == null)
            return UNTITLED_FILE;
        String name = _url.getFile();
        int i = name.lastIndexOf('/');
        return name.substring(i + 1);
    }

    public void setName(String n)
        throws PropertyVetoException, MalformedURLException {
        String s = "";
        if (getURL() != null)
            s = getURL().toString();
        s = s.substring(0, s.lastIndexOf("/") + 1) + n;
        setURL(new URL(s));
    }

    public URL getURL() {
        return _url;
    }

    public void setURL(URL url) {
        if (url != null) {
            url = Util.fixURLExtension(url, COMPRESSED_FILE_EXT);
        }

        cat.debug(
            "Setting project URL from \"" + _url + "\" to \"" + url + "\".");

        _url = url;
    }

    //   public void setFilename(String path, String name) throws PropertyVetoException {
    //     if (!(name.endsWith(FILE_EXT))) name += FILE_EXT;
    //     if (!(path.endsWith("/"))) path += "/";
    //     URL url = new URL("file://" + path + name);
    //     getVetoSupport().fireVetoableChange("url", _url, url);
    //     _url = url;
    //   }

    public void setFile(File file) {
        try {
            URL url = Util.fileToURL(file);

            cat.debug(
                "Setting project file name from \""
                    + _url
                    + "\" to \""
                    + url
                    + "\".");

            _url = url;
        } catch (MalformedURLException murle) {
            cat.error("problem in setFile:" + file, murle);
        } catch (IOException ex) {
            cat.error("problem in setFile:" + file, ex);

        }
    }

    public Vector getSearchPath() {
        return _searchpath;
    }
    public void addSearchPath(String searchpath) {
        _searchpath.addElement(searchpath);
    }

    public URL findMemberURLInSearchPath(String name) {
        //ignore searchpath, just find it relative to the project file
        String u = "";
        if (getURL() != null)
            u = getURL().toString();
        u = u.substring(0, u.lastIndexOf("/") + 1);
        URL url = null;
        try {
            url = new URL(u + name);
        } catch (MalformedURLException murle) {
            cat.error(
                "MalformedURLException in findMemberURLInSearchPath:"
                    + u
                    + name,
                murle);
        }
        return url;
    }

    public Vector getMembers() {
        return _members;
    }

    public void addMember(String name, String type) {
        //try {
        URL memberURL = findMemberURLInSearchPath(name);
        if (memberURL == null) {
            cat.debug("null memberURL");
            return;
        } else
            cat.debug("memberURL = " + memberURL);
        ProjectMember pm = findMemberByName(name);
        if (pm != null)
            return;
        if ("pgml".equals(type))
            pm = new ProjectMemberDiagram(name, this);
        else if ("xmi".equals(type))
            pm = new ProjectMemberModel(name, this);
        else
            throw new RuntimeException("Unknown member type " + type);
        _members.addElement(pm);
        //} catch (java.net.MalformedURLException e) {
        //throw new UnexpectedException(e);
        //}
    }

    public void addMember(ArgoDiagram d) {
        ProjectMember pm = new ProjectMemberDiagram(d, this);
        addDiagram(d);
        // if diagram added successfully, add the member too
        _members.addElement(pm);
    }

    public void addMember(ProjectMemberTodoList pm) {
        Iterator iter = _members.iterator();
        Object currentMember = null;
        while (iter.hasNext()) {
            currentMember = iter.next();
            if (currentMember instanceof ProjectMemberTodoList) {
                /* No need to have several of these */
                return;
            }
        }
        // got past the veto, add the member
        _members.addElement(pm);
    }

    public void addMember(MModel m) {
        Iterator iter = _members.iterator();
        Object currentMember = null;
        boolean memberFound = false;
        while (iter.hasNext()) {
            currentMember = iter.next();
            if (currentMember instanceof ProjectMemberModel) {
                MModel currentModel =
                    ((ProjectMemberModel)currentMember).getModel();
                if (currentModel == m) {
                    memberFound = true;
                    break;
                }
            }
        }
        if (!memberFound) {
            if (!_models.contains(m)) {
                addModel(m);
            }
            // got past the veto, add the member
            ProjectMember pm = new ProjectMemberModel(m, this);
            _members.addElement(pm);
        }
    }

    public void addModel(MNamespace m) {
        // fire indeterminate change to avoid copying vector
        if (!_models.contains(m))
            _models.addElement(m);
        setCurrentNamespace(m);
        setNeedsSave(true);
    }

    /**
     * Removes a project member diagram completely from the project.
     * @param d
     */

    protected void removeProjectMemberDiagram(ArgoDiagram d) {

        removeDiagram(d);

        // should remove the corresponding ProjectMemberDiagram not the ArgoDiagram from the members

        // _members.removeElement(d);
        // ehhh lets remove the diagram really and remove it from its corresponding projectmember too
        Iterator it = _members.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof ProjectMemberDiagram) {
                ProjectMemberDiagram pmd = (ProjectMemberDiagram)obj;
                if (pmd.getDiagram() == d) {
                    _members.removeElement(pmd);
                    break;
                }
            }
        }
    }

    public ProjectMember findMemberByName(String name) {
        cat.debug("findMemberByName called for \"" + name + "\".");
        for (int i = 0; i < _members.size(); i++) {
            ProjectMember pm = (ProjectMember)_members.elementAt(i);
            if (name.equals(pm.getPlainName()))
                return pm;
        }
        cat.debug("Member \"" + name + "\" not found.");
        return null;
    }


    //   public void loadAllMembers() {
    //     for (java.util.Enumeration enum = members.elements(); enum.hasMoreElements(); ) {
    //       ((ProjectMember) enum.nextElement()).load();
    //     }
    //   }

    //   public static Project loadEmpty() throws IOException, org.xml.sax.SAXException {
    //     URL emptyURL = Project.class.getResource(TEMPLATES + EMPTY_PROJ);
    //     if (emptyURL == null)
    //       throw new IOException("Unable to get empty project resource.");
    //     return load(emptyURL);
    //   }

    public void loadMembersOfType(String type) {
        if (type == null)
            return;
        java.util.Enumeration enum = getMembers().elements();
        try {
            while (enum.hasMoreElements()) {
                ProjectMember pm = (ProjectMember)enum.nextElement();
                if (type.equalsIgnoreCase(pm.getType()))
                    pm.load();
            }
        } catch (IOException ignore) {
            cat.error("IOException in makeEmptyProject", ignore);
        } catch (org.xml.sax.SAXException ignore) {
            cat.error("SAXException in makeEmptyProject", ignore);
        }
    }

    public void loadAllMembers() {
        loadMembersOfType("xmi");
        loadMembersOfType("argo");
        loadMembersOfType("pgml");
        loadMembersOfType("todo");
        loadMembersOfType("text");
        loadMembersOfType("html");
    }

    //   public void loadMembersOfType(String type) {
    //     int size = _members.size();
    //     for (int i = 0; i < size; i++) {
    //       ProjectMember pm = (ProjectMember) _members.elementAt(i);
    //       if (pm.type != null && pm.type.equalsIgnoreCase(type))
    // 	pm.load();
    //     }
    //   }

    /**
     * There are known issues with saving, particularly
     * losing the xmi at save time. see issue
     * http://argouml.tigris.org/issues/show_bug.cgi?id=410
     *
     * It is also being considered to save out individual
     * xmi's from individuals diagrams to make
     * it easier to modularize the output of Argo.
     */
    public void save(boolean overwrite, File file)
        throws IOException, Exception {
        setFile(file);

        if (expander == null) {
            java.util.Hashtable templates = TemplateReader.readFile(ARGO_TEE);
            expander = new OCLExpander(templates);
        }

        preSave();

        ZipOutputStream stream =
            new ZipOutputStream(new FileOutputStream(file));
        BufferedWriter writer =
            new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));

        ZipEntry zipEntry = new ZipEntry(getBaseName() + UNCOMPRESSED_FILE_EXT);
        stream.putNextEntry(zipEntry);
        expander.expand(writer, this, "", "");
        writer.flush();
        stream.closeEntry();

        String path = file.getParent();
        Argo.log.info("Dir ==" + path);
        int size = _members.size();

        try {
            // First we save all objects that are not XMI objects i.e. the
            // diagrams (first for loop).
            // The we save all XMI objects (second for loop).
            // This is because order is important on saving.
            Collection names = new ArrayList();
            int counter = 0;  
            for (int i = 0; i < size; i++) {
                ProjectMember p = (ProjectMember)_members.elementAt(i);
                if (!(p.getType().equalsIgnoreCase("xmi"))) {
                    Argo.log.info(
                        "Saving member of type: "
                            + ((ProjectMember)_members.elementAt(i)).getType());
                    String name = p.getName();
                    String originalName = name;                                  
                    while (names.contains(name)) {
                        name = ++counter + originalName;
                    }
                    names.add(name);
                    stream.putNextEntry(new ZipEntry(name));
                    p.save(path, overwrite, writer);
                    writer.flush();
                    stream.closeEntry();
                }
            }

            for (int i = 0; i < size; i++) {
                ProjectMember p = (ProjectMember)_members.elementAt(i);
                if (p.getType().equalsIgnoreCase("xmi")) {
                    Argo.log.info(
                        "Saving member of type: "
                            + ((ProjectMember)_members.elementAt(i)).getType());
                    stream.putNextEntry(new ZipEntry(p.getName()));
                    p.save(path, overwrite, writer);
                }
            }

        } catch (IOException e) {
            cat.debug("hat nicht geklappt: " + e);
            e.printStackTrace();
        }

        //TODO: in future allow independent saving
        writer.close();
        // zos.close();

        postSave();
    }

    public String getAuthorname() {
        return _authorname;
    }
    public void setAuthorname(String s) {
        _authorname = s;
    }

    public String getVersion() {
        return _version;
    }
    public void setVersion(String s) {
        _version = s;
    }

    public String getDescription() {
        return _description;
    }
    public void setDescription(String s) {
        _description = s;
    }

    public String getHistoryFile() {
        return _historyFile;
    }
    public void setHistoryFile(String s) {
        _historyFile = s;
    }

    public void setNeedsSave(boolean newValue) {
        _saveRegistry.setChangeFlag(newValue);
    }
    public boolean needsSave() {
        return _saveRegistry.hasChanged();
    }

    /**
     * Returns all models defined by the user. I.e. this does not return the
     * default model but all other models.
     * @return Vector
     */
    public Vector getUserDefinedModels() {
        return _models;
    }

    /**
     * Returns all models, including the default model (default.xmi).
     * @return Collection
     */
    public Collection getModels() {
        Set ret = new HashSet();
        ret.addAll(_models);
        ret.add(_defaultModel);
        return ret;
    }

    public MNamespace getModel() {
        if (_models.size() != 1)
            return null;
        return (MNamespace)_models.elementAt(0);
    }
    //   public void addModel(MNamespace m) throws PropertyVetoException {
    //     getVetoSupport().fireVetoableChange("Models", _models, m);
    //     _models.addElement(m);
    //     setCurrentNamespace(m);
    //     _needsSave = true;
    //   }

    /**
     * Searches for a type/classifier with name s. If the type is not found,
     * a new type is created and added to the current namespace.
     * @param s
     * @return MClassifier
     */
    public MClassifier findType(String s) {
        return findType(s, true);
    }

    /**
     * Searches for a type/classifier with name s. If defineNew is true, 
     * a new type is defined if the type/classifier is not found. The newly created
     * type is added to the currentNamespace and given the name s.
     * @param s
     * @param defineNew
     * @return MClassifier
     */
    public MClassifier findType(String s, boolean defineNew) {
        if (s != null)
            s = s.trim();
        if (s == null || s.length() == 0)
            return null;
        MClassifier cls = null;
        int numModels = _models.size();
        for (int i = 0; i < numModels; i++) {
            cls = findTypeInModel(s, (MNamespace)_models.elementAt(i));
            if (cls != null)
                return cls;
        }
        cls = findTypeInModel(s, _defaultModel);
        // hey, now we should move it to the model the user is working in
        cls = (MClassifier)ModelManagementHelper.getHelper().getCorrespondingElement(cls, getRoot());

        if (cls == null && defineNew) {
            cat.debug("new Type defined!");
            cls =
                UmlFactory.getFactory().getCore().buildClass(
                    getCurrentNamespace());
            cls.setName(s);
        }
        return cls;
    }

    /**
     * Finds all figs on the diagrams for some project member, including the 
     * figs containing the member (so for some operation, the containing figclass
     * is returned).
     * @param member The member we are looking for. This can be a NSUML object but also another object.
     * @return Collection The collection with the figs.
     */
    public Collection findFigsForMember(Object member) {
        Collection figs = new ArrayList();
        Iterator it = getDiagrams().iterator();
        while (it.hasNext()) {
            ArgoDiagram diagram = (ArgoDiagram)it.next();
            Fig fig = diagram.getContainingFig(member);
            if (fig != null) {
                figs.add(fig);
            }
        }
        return figs;
    }

    /** Will only return first classifier with the matching name
     *
     * @param s is short name
     */
    public MClassifier findTypeInModel(String s, MNamespace ns) {
        Collection allClassifiers =
            ModelManagementHelper.getHelper().getAllModelElementsOfKind(
                ns,
                MClassifier.class);
        Iterator it = allClassifiers.iterator();
        while (it.hasNext()) {
            MClassifier classifier = (MClassifier)it.next();
            if (classifier.getName() != null && classifier.getName().equals(s))
                return classifier;
        }
        return null;
    }

    public void setCurrentNamespace(MNamespace m) {
        _currentNamespace = m;
    }
    public MNamespace getCurrentNamespace() {
        return _currentNamespace;
    }

    public Vector getDiagrams() {
        return _diagrams;
    }
    public void addDiagram(ArgoDiagram d) {
        // send indeterminate new value instead of making copy of vector
        _diagrams.addElement(d);
        d.addChangeRegistryAsListener(_saveRegistry);
        setNeedsSave(true);
    }

    /**
     * Removes a diagram from the list with diagrams. 
     * Removes (hopefully) the event listeners for this diagram.
     * Does not remove the diagram from the project members. This should not be called
     * directly. Use moveToTrash if you want to remove a diagram.
     * @param d
     */
    protected void removeDiagram(ArgoDiagram d) {
        _diagrams.removeElement(d);
        if (d instanceof UMLStateDiagram) {
            UMLStateDiagram statediagram = (UMLStateDiagram)d;
            ProjectManager.getManager().getCurrentProject().moveToTrash(
                statediagram.getStateMachine());
        }
        d.removeChangeRegistryAsListener(_saveRegistry);
        setNeedsSave(true);
    }

    public int getPresentationCountFor(MModelElement me) {
        int presentations = 0;
        int size = _diagrams.size();
        for (int i = 0; i < size; i++) {
            Diagram d = (Diagram)_diagrams.elementAt(i);
            presentations += d.getLayer().presentationCountFor(me);
        }
        return presentations;
    }

    public Object getInitialTarget() {
        if (_diagrams.size() > 0)
            return _diagrams.elementAt(0);
        if (_models.size() > 0)
            return _models.elementAt(0);
        return null;
    }

    public void setGenerationPrefs(GenerationPreferences cgp) {
        _cgPrefs = cgp;
    }
    public GenerationPreferences getGenerationPrefs() {
        return _cgPrefs;
    }

    ////////////////////////////////////////////////////////////////
    // event handling

    public VetoableChangeSupport getVetoSupport() {
        if (_vetoSupport == null)
            _vetoSupport = new VetoableChangeSupport(this);
        return _vetoSupport;
    }

    private void preSave() {
        for (int i = 0; i < _diagrams.size(); i++)
             ((Diagram)_diagrams.elementAt(i)).preSave();
        // TODO: is preSave needed for models?
    }

    private void postSave() {
        for (int i = 0; i < _diagrams.size(); i++)
             ((Diagram)_diagrams.elementAt(i)).postSave();
        // TODO: is postSave needed for models?
        setNeedsSave(false);
    }

    /**
     * @deprecated As of 28 Apr 2003 (ArgoUml version 0.13.5). Will be protected in future.
     */
    public void postLoad() {
        for (int i = 0; i < _diagrams.size(); i++)
             ((Diagram)_diagrams.elementAt(i)).postLoad();
        // issue 1725: the root is not set, which leads to problems with displaying prop panels
        setRoot((MModel)getModel());
        
        setNeedsSave(false);
        // we don't need this HashMap anymore so free up the memory
        _UUIDRefs = null;
    }

    /**
     * Moves some object to trash. This mechanisme must be rethought since it only
     * deletes an object completely from the project
     * @param obj The object to be deleted
     * @see org.argouml.kernel.Project#trashInternal
     */
    ////////////////////////////////////////////////////////////////
    // trash related methos

    // Attention: whole Trash mechanism should be rethought concerning nsuml
    public void moveToTrash(Object obj) {
        if (Trash.SINGLETON.contains(obj))
            return;
        trashInternal(obj);

        /* old version
           if (Trash.SINGLETON.contains(obj)) return;
           Vector alsoTrash = null;
           if (obj instanceof MModelElement)
           alsoTrash = ((MModelElementImpl)obj).alsoTrash();
           trashInternal(obj);
           if (alsoTrash != null) {
           int numTrash = alsoTrash.size();
           for (int i = 0; i < numTrash; i++)
           moveToTrash(alsoTrash.elementAt(i));
           }
        */

    }

    /**
     * Removes some object from the project. Does not update GUI since this method only
     * handles project management.
     * @param obj
     */
    // Attention: whole Trash mechanism should be rethought concerning nsuml

    // Jeremy Bennett. Note that at present these are all if, not
    // else-if. Rather than make a big change, I've just explicitly dealt with
    // the case where we have a use case that is not classifier.

    protected void trashInternal(Object obj) {
        boolean needSave = false;
        if (obj != null) {
            TargetManager.getInstance().removeTarget(obj);
            TargetManager.getInstance().removeHistoryElement(obj);
            Trash.SINGLETON.addItemFrom(obj, null);
        }
        if (obj instanceof MBase) { // an object that can be represented
            ProjectBrowser.getInstance().getEditorPane().removePresentationFor(
                obj,
                getDiagrams());
            UmlFactory.getFactory().delete((MBase)obj);
            if (_members.contains(obj)) {
                _members.remove(obj);
            }
            if (_models.contains(obj)) {
                _models.remove(obj);
            }
            needSave = true;
        } else {
            if (obj instanceof ArgoDiagram) {
                removeProjectMemberDiagram((ArgoDiagram)obj);
                needSave = true;
            }
            if (obj instanceof Fig) {
                ((Fig)obj).dispose();
                needSave = true;
            }
        }        
        ProjectBrowser.getInstance().getNavigatorPane().forceUpdate();
        setNeedsSave(needSave);
    }

    public void moveFromTrash(Object obj) {
        cat.debug("TODO: not restoring " + obj);
    }

    public boolean isInTrash(Object dm) {
        return Trash.SINGLETON.contains(dm);
    }

    ////////////////////////////////////////////////////////////////
    // usage statistics

    public static void resetStats() {
        ToDoPane._clicksInToDoPane = 0;
        ToDoPane._dblClicksInToDoPane = 0;
        ToDoList._longestToDoList = 0;
        Designer._longestAdd = 0;
        Designer._longestHot = 0;
        Critic._numCriticsFired = 0;
        ToDoList._numNotValid = 0;
        Agency._numCriticsApplied = 0;
        ToDoPane._toDoPerspectivesChanged = 0;

        NavigatorPane._navPerspectivesChanged = 0;
        NavigatorPane._clicksInNavPane = 0;
        FindDialog._numFinds = 0;
        TabResults._numJumpToRelated = 0;
        DesignIssuesDialog._numDecisionModel = 0;
        GoalsDialog._numGoalsModel = 0;

        CriticBrowserDialog._numCriticBrowser = 0;
        NavigatorConfigDialog._numNavConfig = 0;
        TabToDo._numHushes = 0;
        ChecklistStatus._numChecks = 0;
        SelectionWButtons.Num_Button_Clicks = 0;
        ModeCreateEdgeAndNode.Drags_To_New = 0;
        ModeCreateEdgeAndNode.Drags_To_Existing = 0;
    }

    public static void setStat(String n, int v) {
        //     String n = us.name;
        //     int v = us.value;
        cat.debug("setStat: " + n + " = " + v);
        if (n.equals("clicksInToDoPane"))
            ToDoPane._clicksInToDoPane = v;
        else if (n.equals("dblClicksInToDoPane"))
            ToDoPane._dblClicksInToDoPane = v;
        else if (n.equals("longestToDoList"))
            ToDoList._longestToDoList = v;
        else if (n.equals("longestAdd"))
            Designer._longestAdd = v;
        else if (n.equals("longestHot"))
            Designer._longestHot = v;
        else if (n.equals("numCriticsFired"))
            Critic._numCriticsFired = v;
        else if (n.equals("numNotValid"))
            ToDoList._numNotValid = v;
        else if (n.equals("numCriticsApplied"))
            Agency._numCriticsApplied = v;
        else if (n.equals("toDoPerspectivesChanged"))
            ToDoPane._toDoPerspectivesChanged = v;

        else if (n.equals("navPerspectivesChanged"))
            NavigatorPane._navPerspectivesChanged = v;
        else if (n.equals("clicksInNavPane"))
            NavigatorPane._clicksInNavPane = v;
        else if (n.equals("numFinds"))
            FindDialog._numFinds = v;
        else if (n.equals("numJumpToRelated"))
            TabResults._numJumpToRelated = v;
        else if (n.equals("numDecisionModel"))
            DesignIssuesDialog._numDecisionModel = v;
        else if (n.equals("numGoalsModel"))
            GoalsDialog._numGoalsModel = v;

        else if (n.equals("numCriticBrowser"))
            CriticBrowserDialog._numCriticBrowser = v;
        else if (n.equals("numNavConfig"))
            NavigatorConfigDialog._numNavConfig = v;
        else if (n.equals("numHushes"))
            TabToDo._numHushes = v;
        else if (n.equals("numChecks"))
            ChecklistStatus._numChecks = v;

        else if (n.equals("Num_Button_Clicks"))
            SelectionWButtons.Num_Button_Clicks = v;
        else if (n.equals("Drags_To_New"))
            ModeCreateEdgeAndNode.Drags_To_New = v;
        else if (n.equals("Drags_To_Existing"))
            ModeCreateEdgeAndNode.Drags_To_Existing = v;

        else {
            cat.warn("unknown UsageStatistic: " + n);
        }
    }

    public static Vector getStats() {
        Vector s = new Vector();
        addStat(s, "clicksInToDoPane", ToDoPane._clicksInToDoPane);
        addStat(s, "dblClicksInToDoPane", ToDoPane._dblClicksInToDoPane);
        addStat(s, "longestToDoList", ToDoList._longestToDoList);
        addStat(s, "longestAdd", Designer._longestAdd);
        addStat(s, "longestHot", Designer._longestHot);
        addStat(s, "numCriticsFired", Critic._numCriticsFired);
        addStat(s, "numNotValid", ToDoList._numNotValid);
        addStat(s, "numCriticsApplied", Agency._numCriticsApplied);
        addStat(
            s,
            "toDoPerspectivesChanged",
            ToDoPane._toDoPerspectivesChanged);

        addStat(
            s,
            "navPerspectivesChanged",
            NavigatorPane._navPerspectivesChanged);
        addStat(s, "clicksInNavPane", NavigatorPane._clicksInNavPane);
        addStat(s, "numFinds", FindDialog._numFinds);
        addStat(s, "numJumpToRelated", TabResults._numJumpToRelated);
        addStat(s, "numDecisionModel", DesignIssuesDialog._numDecisionModel);
        addStat(s, "numGoalsModel", GoalsDialog._numGoalsModel);
        addStat(s, "numCriticBrowser", CriticBrowserDialog._numCriticBrowser);
        addStat(s, "numNavConfig", NavigatorConfigDialog._numNavConfig);
        addStat(s, "numHushes", TabToDo._numHushes);
        addStat(s, "numChecks", ChecklistStatus._numChecks);

        addStat(s, "Num_Button_Clicks", SelectionWButtons.Num_Button_Clicks);
        addStat(s, "Drags_To_New", ModeCreateEdgeAndNode.Drags_To_New);
        addStat(
            s,
            "Drags_To_Existing",
            ModeCreateEdgeAndNode.Drags_To_Existing);

        return s;
    }

    public static void addStat(Vector stats, String name, int value) {
        stats.addElement(new UsageStatistic(name, value));
    }

    public void setDefaultModel(MModel defaultModel) {
        _defaultModel = defaultModel;
    }

    public MModel getDefaultModel() {
        return _defaultModel;
    }

    static final long serialVersionUID = 1399111233978692444L;

    /**
     * Returns the root.
     * @return MModel
     */
    public MModel getRoot() {
        return _root;
    }

    /**
     * Sets the root.
     * @param root The root to set
     */
    public void setRoot(MModel root) {
        if (_root != null) {
            _members.remove(_root);
            _models.remove(_root);
        }
        _root = root;
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
            ArgoDiagram diagram = (ArgoDiagram)it.next();
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
        return _cgPrefs;
    }

    /**
     * Returns the needsSave.
     * @return boolean
     */
    public boolean isNeedsSave() {
        return _needsSave;
    }

    /**
     * Returns the saveRegistry.
     * @return ChangeRegistry
     */
    public ChangeRegistry getSaveRegistry() {
        return _saveRegistry;
    }

    /**
     * Returns the searchpath.
     * @return Vector
     */
    public Vector getSearchpath() {
        return _searchpath;
    }

    /**
     * Returns the url.
     * @return URL
     */
    public URL getUrl() {
        return _url;
    }

    /**
     * Returns the uUIDRefs.
     * @return HashMap
     */
    public HashMap getUUIDRefs() {
        return _UUIDRefs;
    }

    /**
     * Sets the cgPrefs.
     * @param cgPrefs The cgPrefs to set
     */
    public void setCgPrefs(GenerationPreferences cgPrefs) {
        _cgPrefs = cgPrefs;
    }

    /**
     * Sets the diagrams.
     * @param diagrams The diagrams to set
     */
    public void setDiagrams(Vector diagrams) {
        _diagrams = diagrams;
    }

    /**
     * Sets the members.
     * @param members The members to set
     */
    public void setMembers(Vector members) {
        _members = members;
    }

    /**
     * Sets the models.
     * @param models The models to set
     */
    public void setModels(Vector models) {
        _models = models;
    }

    /**
     * Sets the saveRegistry.
     * @param saveRegistry The saveRegistry to set
     */
    public void setSaveRegistry(ChangeRegistry saveRegistry) {
        _saveRegistry = saveRegistry;
    }

    /**
     * Sets the searchpath.
     * @param searchpath The searchpath to set
     */
    public void setSearchpath(Vector searchpath) {
        _searchpath = searchpath;
    }

    /**
     * Sets the url.
     * @param url The url to set
     */
    public void setUrl(URL url) {
        _url = url;
    }

    /**
     * Sets the uUIDRefs.
     * @param uUIDRefs The uUIDRefs to set
     */
    public void setUUIDRefs(HashMap uUIDRefs) {
        _UUIDRefs = uUIDRefs;
    }

    /**
     * Sets the vetoSupport.
     * @param vetoSupport The vetoSupport to set
     */
    public void setVetoSupport(VetoableChangeSupport vetoSupport) {
        _vetoSupport = vetoSupport;
    }

    /**
     * @return
     */
    public ArgoDiagram getActiveDiagram() {
        return _activeDiagram;
    }

    /**
     * @param diagram
     */
    public void setActiveDiagram(ArgoDiagram diagram) {
        _activeDiagram = diagram;
    }

    /** 
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {       
    }

    /** 
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        setTarget(e.getNewTargets()[0]);

    }

    /** 
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTargets()[0]);

    }
    
    /**
     * Called to update the current namespace and active diagram after the target 
     * has changed.
     * @param target
     */
    private void setTarget(Object target) {
        Object currentNamespace = null;
        if (target instanceof UMLDiagram) {
            currentNamespace = ((UMLDiagram)target).getNamespace();
        } else
        if (target instanceof MNamespace)
            currentNamespace = target;
        else 
        if (target instanceof MModelElement) 
            currentNamespace = ModelFacade.getNamespace(target);
        else
            currentNamespace = getRoot();
        setCurrentNamespace((MNamespace)currentNamespace);
               
        if (target instanceof ArgoDiagram) {
            setActiveDiagram((ArgoDiagram)target);
        }                  
    }

} /* end class Project */

class ResetStatsLater implements Runnable {
    public void run() {
        Project.resetStats();
    }
} /* end class ResetStatsLater */
