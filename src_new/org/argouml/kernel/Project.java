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

// File: Project.java
// Classes: Project
// Original Author: not known

// 16 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to remove
// include and extend relationships when deleting a use case.

// 3 May 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to mark the
// project as needing saving if any object is trashed.


package org.argouml.kernel;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.beans.*;
import java.net.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.xmi.*;
import ru.novosoft.uml.MBase;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.use_cases.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.util.*;
import org.tigris.gef.ocl.*;

import org.apache.log4j.Category;
import org.argouml.application.api.*;
import org.argouml.cognitive.*;
import org.argouml.cognitive.ui.*;
import org.argouml.cognitive.critics.*;
import org.argouml.cognitive.critics.ui.*;
import org.argouml.cognitive.checklist.*;
import org.argouml.model.uml.UmlHelper;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.uml.*;
import org.argouml.uml.generator.*;
import org.argouml.uml.diagram.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.static_structure.ui.*;
import org.argouml.uml.diagram.use_case.ui.*;
import org.argouml.language.java.generator.*;
import org.argouml.ui.*;
import org.argouml.util.*;
import org.argouml.xml.argo.*;
import org.argouml.xml.pgml.*;
import org.argouml.xml.xmi.XMIParser;
import org.argouml.xml.xmi.XMIReader;


/** A datastructure that represents the designer's current project.  A
 *  Project consists of diagrams and UML models. */

public class Project implements java.io.Serializable {
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

    public String _authorname = "";
    public String _description = "";
    public String _version = "";

    public Vector _searchpath = new Vector();
    public Vector _members = new Vector();
    public String _historyFile = "";

    public Vector _models = new Vector(); //instances of MModel
    public Vector _diagrams = new Vector(); // instances of LayerDiagram
    protected MModel _defaultModel = null;
    public boolean _needsSave = false;
    protected MNamespace _currentNamespace = null;
    public HashMap _UUIDRefs = null;
    public GenerationPreferences _cgPrefs = new GenerationPreferences();
    public transient VetoableChangeSupport _vetoSupport = null;
    
    /**
     * The project the user is working on at the moment
     */
    private static Project _currentProject;
    
    /**
     * True if we are in the proces of making a project, otherwise false
     */
    private static boolean _creatingProject;
    
    /**
     * The root of the modeltree the user is working on. (The untitled_model in
     * the navpane).
     */
    private MModel _root;
   
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
         // Jaap Branderhorst 2002-12-09
        // load the default model
        // this is NOT the way how it should be since this makes argo depend on Java even more.
       setDefaultModel(ProfileJava.loadProfileModel());
    }

    public Project (MModel model) {
    	this();
        Argo.log.info("making empty project with model: "+model.getName());
        _saveRegistry = new UMLChangeRegistry();
		/*
        defineType(JavaUML.VOID_TYPE);     //J.101
        defineType(JavaUML.CHAR_TYPE);     //J.102
        defineType(JavaUML.INT_TYPE);      //J.103
        defineType(JavaUML.BOOLEAN_TYPE);  //J.104
        defineType(JavaUML.BYTE_TYPE);     //J.105
        defineType(JavaUML.LONG_TYPE);     //J.106
        defineType(JavaUML.FLOAT_TYPE);    //J.107
        defineType(JavaUML.DOUBLE_TYPE);   //J.108
        defineType(JavaUML.STRING_CLASS);  //J.109
        defineType(JavaUML.CHAR_CLASS);    //J.110
        defineType(JavaUML.INT_CLASS);     //J.111
        defineType(JavaUML.BOOLEAN_CLASS); //J.112
        defineType(JavaUML.BYTE_CLASS);    //J.113
        defineType(JavaUML.LONG_CLASS);    //J.114
        defineType(JavaUML.FLOAT_CLASS);   //J.115
        defineType(JavaUML.DOUBLE_CLASS);  //J.116

        defineType(JavaUML.RECTANGLE_CLASS); //J.201
        defineType(JavaUML.POINT_CLASS);     //J.202
        defineType(JavaUML.COLOR_CLASS);     //J.203

        defineType(JavaUML.VECTOR_CLASS);    //J.301
        defineType(JavaUML.HASHTABLE_CLASS); //J.302
        defineType(JavaUML.STACK_CLASS);     //J.303
		*/
        addSearchPath("PROJECT_DIR");

        try {
            addMember(new UMLClassDiagram("class diagram 1", model));
            addMember(new UMLUseCaseDiagram("use case diagram 1", model));
            setRoot(model);
            setNeedsSave(false);
        }
        catch (PropertyVetoException pve) { }

        Runnable resetStatsLater = new ResetStatsLater();
        org.argouml.application.Main.addPostLoadAction(resetStatsLater);
        setCurrentNamespace(model);
        
    }

    /**   This method creates a project from the specified URL
     *
     *    Unlike the constructor which forces an .argo extension
     *    This method will attempt to load a raw XMI file
     * <P>
     * This method can fail in several different ways. Either by throwing
     * an exception or by having the ArgoParser.SINGLETON.getLastLoadStatus()
     * set to not true.
     * <P>
     * TODO: This method NEEDS a refactoring.
     */
    public static Project loadProject(URL url) throws IOException, Exception {
        Project p = null;
        String urlString = url.toString();
        int lastDot = urlString.lastIndexOf(".");
        String suffix = "";
        if(lastDot >= 0) {
            suffix = urlString.substring(lastDot).toLowerCase();
        }
        //
        //    just read XMI file
        //
        if(suffix.equals(".xmi")) {
            p = new Project();
            XMIParser.SINGLETON.readModels(p,url);
            MModel model = XMIParser.SINGLETON.getCurModel();
            UmlHelper.getHelper().addListenersToModel(model);
            p._UUIDRefs = XMIParser.SINGLETON.getUUIDRefs();        
            p.addMember(model);
            p.setNeedsSave(false);            
            org.argouml.application.Main.addPostLoadAction(new ResetStatsLater());
        }
	
        else if(suffix.equals(COMPRESSED_FILE_EXT)) { // normal case
            try {
                ZipInputStream zis = new ZipInputStream(url.openStream());
		
                // first read the .argo file from Zip
                String name = zis.getNextEntry().getName();
                while(!name.endsWith(PROJECT_FILE_EXT)) {
                    name = zis.getNextEntry().getName();
                }
		
                // the "false" means that members should not be added,
                // we want to do this by hand from the zipped stream.
                ArgoParser.SINGLETON.setURL(url);
                ArgoParser.SINGLETON.readProject(zis,false);
                p = ArgoParser.SINGLETON.getProject();
		
                zis.close();
                
            } catch (Exception e) {
                cat.error("Oops, something went wrong in Project.loadProject ");
                cat.error(e);
                // yeah and now we want to have the old state of the Project back but we dont know the old state
                // so we propagate the error
                throw e;
            }
            try {
                p.loadZippedProjectMembers(url);
            }
            catch (IOException e) {
                cat.error("Project file corrupted");
                cat.error(e);
                throw e;
            }
            p.postLoad();
        }
	
        else {
            ArgoParser.SINGLETON.readProject(url);
            p = ArgoParser.SINGLETON.getProject();
            p.loadAllMembers();
            p.postLoad();
        }
        return p;
    }
    
    /**
     * Loads a model (XMI only) from a .zargo file. BE ADVISED this method has a side
     * effect. It sets _UUIDREFS to the model.
     * <p>
     * If there is a problem with the xmi file, an error is set in the
     * ArgoParser.SINGLETON.getLastLoadStatus() field. This needs to be
     * examined by the calling function.
     *
     * @param url The url with the .zargo file
     * @return MModel The model loaded
     * @throws IOException Thrown if the model or the .zargo file itself is corrupted in any way.
     */
    public MModel loadModelFromXMI(URL url) throws IOException {
        ZipInputStream zis = new ZipInputStream(url.openStream());
   
        String name = zis.getNextEntry().getName();
        while(!name.endsWith(".xmi")) {
            name = zis.getNextEntry().getName();
        }
        Argo.log.info("Loading Model from "+url);
        // 2002-07-18
        // Jaap Branderhorst
        // changed the loading of the projectfiles to solve hanging 
        // of argouml if a project is corrupted. Issue 913
        // Created xmireader with method getErrors to check if parsing went well
        XMIReader xmiReader = null;
        try {
            xmiReader = new org.argouml.xml.xmi.XMIReader();
        }
        catch (SAXException se) {}
        catch (ParserConfigurationException pc) {}
        MModel mmodel = null;
        
        InputSource source = new InputSource(zis);
        source.setEncoding("UTF-8");
        try {
        	mmodel = xmiReader.parse(new InputSource(zis));
        }
        catch (ClassCastException cc) {
        	ArgoParser.SINGLETON.setLastLoadStatus(false);
			ArgoParser.SINGLETON.setLastLoadMessage("XMI file "
							+ url.toString()
							+ " could not be "
							+ "parsed.");
        }
        if (xmiReader.getErrors()) {
		ArgoParser.SINGLETON.setLastLoadStatus(false);
		ArgoParser.SINGLETON.setLastLoadMessage("XMI file "
							+ url.toString()
							+ " could not be "
							+ "parsed.");
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
    public void loadZippedProjectMembers(URL url) 
	throws IOException, PropertyVetoException {

     
        loadModelFromXMI(url); // throws a IOException if things go wrong
                               // user interface has to handle that one
        try {
        
            
            // now close again, reopen and read the Diagrams.

            PGMLParser.SINGLETON.setOwnerRegistry(_UUIDRefs);

            //zis.close();
            ZipInputStream zis = new ZipInputStream(url.openStream());
            SubInputStream sub = new SubInputStream(zis);

            ZipEntry currentEntry = null;
            while ( (currentEntry = sub.getNextEntry()) != null) {
                if (currentEntry.getName().endsWith(".pgml")) {
                    Argo.log.info("Now going to load "+currentEntry.getName()+" from ZipInputStream");

                    // "false" means the stream shall not be closed, but it doesn't seem to matter...
                    ArgoDiagram d = (ArgoDiagram)PGMLParser.SINGLETON.readDiagram(sub,false);
                    addMember(d);
                    // sub.closeEntry();
                    Argo.log.info("Finished loading "+currentEntry.getName());
                }
            }
            zis.close();

	    
        } catch (IOException e) {
            ArgoParser.SINGLETON.setLastLoadStatus(false);
            ArgoParser.SINGLETON.setLastLoadMessage(e.toString());
            cat.error("Oops, something went wrong in Project.loadZippedProjectMembers() ", e);
            throw e; 
        }
    }

    public static Project makeEmptyProject() {
        Argo.log.info("making empty project");
        _creatingProject = true;
        MModel m1 = UmlFactory.getFactory().getModelManagement().createModel();
        m1.setName("untitledModel");
        Project p = new Project(m1);
         setCurrentProject(p);

        p.addSearchPath("PROJECT_DIR");
        _creatingProject = false;
       

        return p;
    }

    ////////////////////////////////////////////////////////////////
    // accessors
    // TODO

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
        if (_url == null) return UNTITLED_FILE;
        String name = _url.getFile();
        int i = name.lastIndexOf('/');
        return name.substring(i+1);
    }

    public void setName(String n) throws PropertyVetoException, MalformedURLException {
        String s = "";
        if (getURL() != null) s = getURL().toString();
        s = s.substring(0, s.lastIndexOf("/") + 1) + n;
        setURL(new URL(s));
    }

    public URL getURL() { return _url; }

    public void setURL(URL url) {
        if (url != null) {
            url = Util.fixURLExtension(url, COMPRESSED_FILE_EXT);
        }

        cat.debug ("Setting project URL from \"" + _url + "\" to \"" + url + "\".");
    
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
      
            cat.debug ("Setting project file name from \"" + _url + "\" to \"" + url + "\".");
      
            _url = url;
        }
        catch (MalformedURLException murle) {
            cat.error("problem in setFile:" + file, murle);
        }
        catch (IOException ex) {
            cat.error("problem in setFile:" + file, ex);
            
        }
    }

    public Vector getSearchPath() { return _searchpath; }
    public void addSearchPath(String searchpath) {
        _searchpath.addElement(searchpath);
    }

    public URL findMemberURLInSearchPath(String name) {
        //ignore searchpath, just find it relative to the project file
        String u = "";
        if (getURL() != null) u = getURL().toString();
        u = u.substring(0, u.lastIndexOf("/") + 1);
        URL url = null;
        try { url = new URL(u + name); }
        catch (MalformedURLException murle) {
            cat.error("MalformedURLException in findMemberURLInSearchPath:" + u + name, murle);
        }
        return url;
    }

    public Vector getMembers() { return _members; }

    public void addMember(String name, String type) {
        //try {
        URL memberURL = findMemberURLInSearchPath(name);
        if (memberURL == null) {
            cat.debug("null memberURL");
            return;
        }
        else cat.debug("memberURL = " + memberURL);
        ProjectMember pm = findMemberByName(name);
        if (pm != null) return;
        if ("pgml".equals(type))
            pm = new ProjectMemberDiagram(name, this);
        else if ("xmi".equals(type))
            pm = new ProjectMemberModel(name, this);
        else throw new RuntimeException("Unknown member type " + type);
        _members.addElement(pm);
        //} catch (java.net.MalformedURLException e) {
        //throw new UnexpectedException(e);
        //}
    }

    public void addMember(ArgoDiagram d) throws PropertyVetoException {
        ProjectMember pm = new ProjectMemberDiagram(d, this);
        addDiagram(d);
        // if diagram added successfully, add the member too
        _members.addElement(pm);
    }

    public void addMember(MModel m) {
        Iterator iter = _members.iterator();
        Object currentMember = null;
        boolean memberFound = false;
        while(iter.hasNext()) {
            currentMember = iter.next();
            if(currentMember instanceof ProjectMemberModel) {
                MModel currentModel = ((ProjectMemberModel) currentMember).getModel();
                if(currentModel == m) {
                    memberFound = true;
                    break;
                }
            }
        }
        if(!memberFound) {
            if(!_models.contains(m)) {
                addModel(m);
            }
            // got past the veto, add the member
            ProjectMember pm = new ProjectMemberModel(m, this);
            _members.addElement(pm);
        }
    }

    public void addModel(MNamespace m) {
        // fire indeterminate change to avoid copying vector
        if (! _models.contains(m)) _models.addElement(m);
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
        cat.debug ("findMemberByName called for \"" + name + "\".");
        for (int i = 0; i < _members.size(); i++) {
            ProjectMember pm = (ProjectMember) _members.elementAt(i);
            if (name.equals(pm.getPlainName())) return pm;
        }
        cat.debug ("Member \"" + name + "\" not found.");
        return null;
    }

    public static Project load(URL url) throws IOException, org.xml.sax.SAXException {
        Dbg.log("org.argouml.kernel.Project", "Reading " + url);
        ArgoParser.SINGLETON.readProject(url);
        Project p = ArgoParser.SINGLETON.getProject();
        p.loadAllMembers();
        p.postLoad();
        Dbg.log("org.argouml.kernel.Project", "Done reading " + url);
        return p;
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
        if (type == null) return;
        java.util.Enumeration enum = getMembers().elements();
        try {
            while (enum.hasMoreElements()) {
                ProjectMember pm = (ProjectMember) enum.nextElement();
                if (type.equalsIgnoreCase(pm.getType())) pm.load();
            }
        }
        catch (IOException ignore) {
            cat.error("IOException in makeEmptyProject", ignore);
        }
        catch (org.xml.sax.SAXException ignore) {
            cat.error("SAXException in makeEmptyProject", ignore);
        }
    }


    public void loadAllMembers() {
        loadMembersOfType("xmi");
        loadMembersOfType("argo");
        loadMembersOfType("pgml");
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
    public void save(boolean overwrite, File file) throws IOException, Exception {
        if (expander == null) {
            java.util.Hashtable templates = TemplateReader.readFile(ARGO_TEE);
            expander = new OCLExpander(templates);
        }
    
        preSave();
        
        ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(file));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
 
        ZipEntry zipEntry = new ZipEntry (getBaseName() + UNCOMPRESSED_FILE_EXT);
        stream.putNextEntry (zipEntry);
        expander.expand (writer, this, "", "");
        writer.flush();
        stream.closeEntry();
        
        String path = file.getParent();
        Argo.log.info ("Dir ==" + path);
        int size = _members.size();
    
        try {
	    // First we save all objects that are not XMI objects i.e. the
	    // diagrams (first for loop).
	    // The we save all XMI objects (second for loop).
	    // This is because order is important on saving.
            for (int i = 0; i < size; i++) {
                ProjectMember p = (ProjectMember) _members.elementAt(i);
                if (!(p.getType().equalsIgnoreCase("xmi"))){
                    Argo.log.info("Saving member of type: " + ((ProjectMember)_members.elementAt(i)).getType());
                    stream.putNextEntry(new ZipEntry(p.getName()));
                    p.save(path,overwrite,writer);
                    writer.flush();
                    stream.closeEntry();
                }
            }

            for (int i = 0; i < size; i++) {
                ProjectMember p = (ProjectMember) _members.elementAt(i);
                if (p.getType().equalsIgnoreCase("xmi")) {
                    Argo.log.info("Saving member of type: " + ((ProjectMember)_members.elementAt(i)).getType());
                    stream.putNextEntry(new ZipEntry(p.getName()));
                    p.save(path,overwrite,writer);
                }
            }

        } catch (IOException e) {
            cat.debug("hat nicht geklappt: "+e);
            e.printStackTrace();
        }
    
        //TODO: in future allow independent saving
        writer.close();
        // zos.close();
    
        postSave();

       
        setFile(file);
       
    }

    public String getAuthorname() { return _authorname; }
    public void setAuthorname(String s) { _authorname = s; }

    public String getVersion() { return _version; }
    public void setVersion(String s) { _version = s; }

    public String getDescription() { return _description; }
    public void setDescription(String s) { _description = s; }

    public String getHistoryFile() { return _historyFile; }
    public void setHistoryFile(String s) { _historyFile = s; }

    public void setNeedsSave(boolean newValue) { _saveRegistry.setChangeFlag(newValue); }
    public boolean needsSave() { return _saveRegistry.hasChanged(); }

    /**
     * Returns all models defined by the user. I.e. this does not return the
     * default model but all other models.
     * @return Vector
     */
    public Vector getUserDefinedModels() { return _models; }

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
		if (_models.size() != 1) return null;
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
        if (s != null) s = s.trim();
        if (s == null || s.length()==0) return null;
        MClassifier cls = null;
        int numModels = _models.size();
        for (int i = 0; i < numModels; i++) {
            cls = findTypeInModel(s, (MNamespace) _models.elementAt(i));
            if (cls != null) return cls;
        }
        cls = findTypeInModel(s, _defaultModel);
        
        if (cls == null && defineNew) {
            cat.debug("new Type defined!");
            cls = UmlFactory.getFactory().getCore().buildClass(getCurrentNamespace());
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
    	while(it.hasNext()) {
    		ArgoDiagram diagram = (ArgoDiagram)it.next();
    		Fig fig = diagram.getContainingFig(member);
    		if (fig != null) {
    			figs.add(fig);
    		}
    	}
    	return figs;
    }
    
	public MClassifier findTypeInModel(String s, MNamespace ns) {
		// s is short name
		// will only return first found element
		Collection allClassifiers = ModelManagementHelper.getHelper().getAllModelElementsOfKind(ns, MClassifier.class);
		Iterator it = allClassifiers.iterator();
		while (it.hasNext()) {
			MClassifier classifier = (MClassifier)it.next();
			if (classifier.getName() != null && classifier.getName().equals(s)) return classifier;
		}
		return null;
	}

    public void setCurrentNamespace(MNamespace m) { _currentNamespace = m; }
    public MNamespace getCurrentNamespace() { return _currentNamespace; }

    public Vector getDiagrams() { return _diagrams; }
    public void addDiagram(ArgoDiagram d) throws PropertyVetoException {
        // send indeterminate new value instead of making copy of vector
        _diagrams.addElement(d);
        d.addChangeRegistryAsListener( _saveRegistry );
        setNeedsSave(true);
    }
    
    /**
     * Removes a diagram from the list with diagrams. 
     * Removes (hopefully) the event listeners for this diagram.
     * Does not remove the diagram from the project members. This should not be called
     * directly. Use moveToTrash if you want to remove a diagram.
     * @param d
     * @throws PropertyVetoException
     */
    protected void removeDiagram(ArgoDiagram d) {
        _diagrams.removeElement(d);
        d.removeChangeRegistryAsListener( _saveRegistry );
        setNeedsSave(true);
    }

    public int getPresentationCountFor(MModelElement me) {
        int presentations = 0;
        int size = _diagrams.size();
        for (int i = 0; i < size; i++) {
            Diagram d = (Diagram) _diagrams.elementAt(i);
            presentations += d.getLayer().presentationCountFor(me);
        }
        return presentations;
    }

    public Object getInitialTarget() {
        if (_diagrams.size() > 0) return _diagrams.elementAt(0);
        if (_models.size() > 0) return _models.elementAt(0);
        return null;
    }

    public void setGenerationPrefs(GenerationPreferences cgp) { _cgPrefs = cgp; }
    public GenerationPreferences getGenerationPrefs() { return _cgPrefs; }

    ////////////////////////////////////////////////////////////////
    // event handling

    public VetoableChangeSupport getVetoSupport() {
        if (_vetoSupport == null) _vetoSupport = new VetoableChangeSupport(this);
        return _vetoSupport;
    }

    public void preSave() {
        for (int i = 0; i < _diagrams.size(); i++)
            ((Diagram)_diagrams.elementAt(i)).preSave();
        // TODO: is preSave needed for models?
    }

    public void postSave() {
        for (int i = 0; i < _diagrams.size(); i++)
            ((Diagram)_diagrams.elementAt(i)).postSave();
        // TODO: is postSave needed for models?
        setNeedsSave(false);
    }

    public void postLoad() {
        for (int i = 0; i < _diagrams.size(); i++)
            ((Diagram)_diagrams.elementAt(i)).postLoad();
        // TODO: is postLoad needed for models?
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
        if (Trash.SINGLETON.contains(obj)) return;
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
    	
    	if (obj instanceof MBase) { // an object that can be represented
    		ProjectBrowser.TheInstance.getEditorPane().removePresentationFor(obj, getDiagrams());
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
        addStat(s, "toDoPerspectivesChanged", ToDoPane._toDoPerspectivesChanged);

        addStat(s, "navPerspectivesChanged",
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
        addStat(s, "Drags_To_Existing", ModeCreateEdgeAndNode.Drags_To_Existing);

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
     * Returns the currentProject.
     * @return Project
     */
    public static Project getCurrentProject() {
        if (_currentProject == null && !_creatingProject) {
            Project newProject = makeEmptyProject();
            setCurrentProject(newProject);
        }
        return _currentProject;
    }

    /**
     * Sets the currentProject.
     * @param currentProject The currentProject to set
     */
    public static void setCurrentProject(Project currentProject) {
        // update the graphics
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        if (pb != null) {
            pb.setProject(currentProject);
        }
        _currentProject = currentProject;
    }
    
    

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

} /* end class Project */


class ResetStatsLater implements Runnable {
  public void run() {
    Project.resetStats();
  }
} /* end class ResetStatsLater */
