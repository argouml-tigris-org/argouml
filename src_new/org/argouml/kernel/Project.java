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

package org.argouml.kernel;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.beans.*;
import java.net.*;

import org.xml.sax.InputSource;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.xmi.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.util.*;

import org.argouml.application.api.*;
import org.argouml.cognitive.*;
import org.argouml.cognitive.ui.*;
import org.argouml.cognitive.critics.*;
import org.argouml.cognitive.critics.ui.*;
import org.argouml.cognitive.checklist.*;
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


/** A datastructure that represents the designer's current project.  A
 * Project consists of diagrams and UML models.
 *
 * This needs much work. Currently little project information is saved.
 *
 * This is a huge class. Perhaps need refactoring.
 */

public class Project implements java.io.Serializable {
  ////////////////////////////////////////////////////////////////
  // constants
  public static final String SEPARATOR = "/";
  public final static String FILE_EXT = ".zargo";
  public final static String TEMPLATES = "/org/argouml/templates/";
  //public final static String EMPTY_PROJ = "EmptyProject" + FILE_EXT;
  public final static String UNTITLED_FILE = "Untitled";

  ////////////////////////////////////////////////////////////////
  // instance variables

  //public String _pathname = "";
  //public String _filename = UNTITLED_FILE + FILE_EXT;

  //needs-more-work should just be the directory to write
  private URL _url = null;
  protected ChangeRegistry _saveRegistry;

  /** This information is not saved in any central location
   * or saved in the generated project file.
   *
   */  
  public String _authorname = "";
  /** This information is not saved. Needs work.
   */  
  public String _description = "";
  /** This would be very nice to have as users
   * continue to work and rework the projects and the
   * diagrams.
   */  
  public String _version = "";

  public Vector _searchpath = new Vector();
  public Vector _members = new Vector();
  public String _historyFile = "";

  public Vector _models = new Vector(); //instances of MModel
  public Vector _diagrams = new Vector(); // instances of LayerDiagram
  /** This, AFAIK, does not work at all.
   */  
  public boolean _needsSave = false;
  public MNamespace _curModel = null;
  public Hashtable _definedTypes = new Hashtable(80);
  public HashMap _UUIDRefs = null;
  public GenerationPreferences _cgPrefs = new GenerationPreferences();
  public transient VetoableChangeSupport _vetoSupport = null;

  ////////////////////////////////////////////////////////////////
  // constructor

  public Project(File file) throws MalformedURLException, IOException {
    this(Util.fileToURL(file));
  }

  public Project(URL url) {
    _url = Util.fixURLExtension(url, FILE_EXT);
    _saveRegistry = new UMLChangeRegistry();
  }

  public Project() {
    _saveRegistry = new UMLChangeRegistry();
  }

  public Project (MModel model) {
    Argo.log.info("making empty project with model: "+model.getName());
    _saveRegistry = new UMLChangeRegistry();

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

    addSearchPath("PROJECT_DIR");

    try {
      addMember(new UMLClassDiagram(model));
      addMember(new UMLUseCaseDiagram(model));
      addMember(model);
      setNeedsSave(false);
    }
    catch (PropertyVetoException pve) { }

    Runnable resetStatsLater = new ResetStatsLater();
    org.argouml.application.Main.addPostLoadAction(resetStatsLater);
  }

  /**   This method creates a project from the specified URL
   *
   *    Unlike the constructor which forces an .argo extension
   *    This method will attempt to load a raw XMI file
   */
  public static Project loadProject(URL url) {
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
            p._UUIDRefs = XMIParser.SINGLETON.getUUIDRefs();
            try {
                p.addMember(model);
                p.setNeedsSave(false);
            }
            catch (PropertyVetoException pve) { }
            org.argouml.application.Main.addPostLoadAction(new ResetStatsLater());
        }
	
        else if(suffix.equals(".zargo")) {
	    try {
		ZipInputStream zis = new ZipInputStream(url.openStream());
		
		// first read the .argo file from Zip
		String name = zis.getNextEntry().getName();
		while(!name.endsWith(".argo")) {
		    name = zis.getNextEntry().getName();
		}
		
		// the "false" means that members should not be added,
		// we want to do this by hand from the zipped stream.
		ArgoParser.SINGLETON.setURL(url);
		ArgoParser.SINGLETON.readProject(zis,false);
		p = ArgoParser.SINGLETON.getProject();
		
		zis.close();
	    } catch (Exception e) {
		System.out.println("Oops, something went wrong in Project.loadProject "+e );
		e.printStackTrace();
	    }
	    
	    p.loadZippedProjectMembers(url);
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
    
    public void loadZippedProjectMembers(URL url) {

	try {
	    ZipInputStream zis = new ZipInputStream(url.openStream());


	    // first load the Model
	    String name = zis.getNextEntry().getName();
	    while(!name.endsWith(".xmi")) {
		name = zis.getNextEntry().getName();
	    }

	    Argo.log.info("Loading Model from "+url);

	    XMIReader xmiReader = new XMIReader();
	    MModel mmodel = xmiReader.parse(new InputSource(zis));
	    addMember(mmodel);

            _UUIDRefs = new HashMap(xmiReader.getXMIUUIDToObjectMap());

	    // now close again, reopen and read the Diagrams.

	    PGMLParser.SINGLETON.setOwnerRegistry(_UUIDRefs);

	    //zis.close();
	    zis = new ZipInputStream(url.openStream());
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

	    
	} catch (Exception e) {
	    System.out.println("Oops, something went wrong in Project.loadZippedProjectMembers() "+e );
	    e.printStackTrace();
	}
    }

  public static Project makeEmptyProject() {
    Argo.log.info("making empty project");
    Project p = new Project();

    p.defineType(JavaUML.VOID_TYPE);     //J.101
    p.defineType(JavaUML.CHAR_TYPE);     //J.102
    p.defineType(JavaUML.INT_TYPE);      //J.103
    p.defineType(JavaUML.BOOLEAN_TYPE);  //J.104
    p.defineType(JavaUML.BYTE_TYPE);     //J.105
    p.defineType(JavaUML.LONG_TYPE);     //J.106
    p.defineType(JavaUML.FLOAT_TYPE);    //J.107
    p.defineType(JavaUML.DOUBLE_TYPE);   //J.108
    p.defineType(JavaUML.STRING_CLASS);  //J.109
    p.defineType(JavaUML.CHAR_CLASS);    //J.110
    p.defineType(JavaUML.INT_CLASS);     //J.111
    p.defineType(JavaUML.BOOLEAN_CLASS); //J.112
    p.defineType(JavaUML.BYTE_CLASS);    //J.113
    p.defineType(JavaUML.LONG_CLASS);    //J.114
    p.defineType(JavaUML.FLOAT_CLASS);   //J.115
    p.defineType(JavaUML.DOUBLE_CLASS);  //J.116

    p.defineType(JavaUML.RECTANGLE_CLASS); //J.201
    p.defineType(JavaUML.POINT_CLASS);     //J.202
    p.defineType(JavaUML.COLOR_CLASS);     //J.203

    p.defineType(JavaUML.VECTOR_CLASS);    //J.301
    p.defineType(JavaUML.HASHTABLE_CLASS); //J.302
    p.defineType(JavaUML.STACK_CLASS);     //J.303
// 	try { p.addMember(JavaUML.javastandards); }
// 	catch (PropertyVetoException pve) { }

    p.addSearchPath("PROJECT_DIR");

// 	try {
// 		XMIReader reader = new XMIReader();
// 		MModel model  = reader.parse(new org.xml.sax.InputSource("java.xmi"));
// 		model.setName("Java standards");
// 		p.addMember(model);
// 	} catch (Exception ex) {
// 		ex.printStackTrace();
// 	}

    MModel m1 = new MModelImpl();
    m1.setUUID(UUIDManager.SINGLETON.getNewUUID());
    m1.setName("untitledModel");

    try {
      p.addMember(new UMLClassDiagram(m1));
      p.addMember(new UMLUseCaseDiagram(m1));
      p.addMember(m1);
      p.setNeedsSave(false);
    }
    catch (PropertyVetoException pve) { }

    Runnable resetStatsLater = new ResetStatsLater();
    org.argouml.application.Main.addPostLoadAction(resetStatsLater);

    return p;
  }

//   /** This method is currently not called.  It is an example of how to
//    *  support loading template files.  However, makeEmptyProject() is
//    *  much faster, and that is important since it is done often. */
//   public static Project loadEmptyProject() {
//     System.out.println("Reading " + TEMPLATES + EMPTY_PROJ + "...");
//     URL url = Project.class.getResource(TEMPLATES + EMPTY_PROJ);
//     Project p = null;
//     //try {
//     ArgoParser.SINGLETON.readProject(url);
//     //     }
//     //     catch (IOException ignore) {
//     //       System.out.println("IOException in makeEmptyProject");
//     //     }
//     //     catch (org.xml.sax.SAXException ignore) {
//     //       System.out.println("SAXException in makeEmptyProject");
//     //     }
//     p = ArgoParser.SINGLETON.getProject();
//     //p.initProject();
//     p.loadAllMembers();
//     p.postLoad();
//     return p;
//   }

  ////////////////////////////////////////////////////////////////
  // accessors
  // needs-more-work

    /**
     * Added Eugenio's patches to load 0.8.1 projects.
     */  
    public String getBaseName() {
	String n = getName();
	
	if (n.endsWith(FILE_EXT)) {
	    return n.substring(0, n.length() - FILE_EXT.length());
	}
	if (n.endsWith(".argo")) {
	    return n.substring(0, n.length() - ".argo".length());
	}
	return n;
    }

  public String getName() {
    // needs-more-work: maybe separate name
    if (_url == null) return UNTITLED_FILE;
    String name = _url.getFile();
    int i = name.lastIndexOf('/');
    return name.substring(i+1);
  }

  public void setName(String n) throws PropertyVetoException, MalformedURLException {
    String s = "";
    if (getURL() != null) s = getURL().toString();
    s = s.substring(0, s.lastIndexOf("/") + 1) + n;
    //JH    System.out.println("s = " + s);
    setURL(new URL(s));
  }

  public void updateMemberNames() {
    for (int i = 0 ; i < _members.size(); i++) {
      ProjectMember pm = (ProjectMember) _members.elementAt(i);
      pm.updateProjectName();
    }
  }

//   public void setName(String n) throws PropertyVetoException {
//     getVetoSupport().fireVetoableChange("Name", _filename, n);
//     _filename = n;
//   }

  public URL getURL() { return _url; }

  public void setURL(URL url) throws PropertyVetoException {
    url = Util.fixURLExtension(url, FILE_EXT);
    getVetoSupport().fireVetoableChange("url", _url, url);
    _url = url;
    updateMemberNames();
  }

//   public void setFilename(String path, String name) throws PropertyVetoException {
//     if (!(name.endsWith(FILE_EXT))) name += FILE_EXT;
//     if (!(path.endsWith("/"))) path += "/";
//     URL url = new URL("file://" + path + name);
//     getVetoSupport().fireVetoableChange("url", _url, url);
//     _url = url;
//   }

  public void setFile(File file) throws PropertyVetoException {
    try {
      URL url = Util.fileToURL(file);
      getVetoSupport().fireVetoableChange("url", _url, url);
      _url = url;
      updateMemberNames();
    }
    catch (MalformedURLException murle) {
      System.out.println("problem in setFile:" + file);
      murle.printStackTrace();
    }
    catch (IOException ex) {
      System.out.println("problem in setFile:" + file);
      ex.printStackTrace();
    }
  }

//   public String getFilename() { return _filename; }
//   public void setFilename(String n) throws PropertyVetoException {
//     getVetoSupport().fireVetoableChange("Filename", _filename, n);
//     _filename = n;
//   }

//   public String getPathname() { return _pathname; }
//   public void setPathname(String n) throws PropertyVetoException {
//     if (!n.endsWith(SEPARATOR)) n += SEPARATOR;
//     getVetoSupport().fireVetoableChange("Pathname", _pathname, n);
//     _pathname = n;
//   }

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
      System.out.println("MalformedURLException in findMemberURLInSearchPath:" + u + name);
      murle.printStackTrace();
    }
    return url;
  }

  public Vector getMembers() { return _members; }

  public void addMember(String name, String type) {
    //try {
      URL memberURL = findMemberURLInSearchPath(name);
      if (memberURL == null) {
	System.out.println("null memberURL");
	return;
      }
      else System.out.println("memberURL = " + memberURL);
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

  public void addMember(MModel m) throws PropertyVetoException {
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

  public void addModel(MNamespace m) throws PropertyVetoException {
    // fire indeterminate change to avoid copying vector
    getVetoSupport().fireVetoableChange("Models", _models, null);
    if (! _models.contains(m)) _models.addElement(m);
    setCurrentNamespace(m);
    setNeedsSave(true);
  }

//   public void removeMember(Diagram d) {
//     int size = _members.size();
//     for (int i = 0; i < size; i++) {
//       ProjectMember pm = (ProjectMember) _members.elementAt(i);
//       if (pm.member == d) {
// 	_members.removeElementAt(i);
// 	try { removeDiagram(d); }
// 	catch (PropertyVetoException pve) { }
// 	return;
//       }
//     }
//   }


  public void removeMember(ArgoDiagram d) throws PropertyVetoException {
    removeDiagram(d);
    _members.removeElement(d);
  }

  public ProjectMember findMemberByName(String name) {
    for (int i = 0; i < _members.size(); i++) {
      ProjectMember pm = (ProjectMember) _members.elementAt(i);
      if (name.equals(pm.getName())) return pm;
    }
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
      System.out.println("IOException in makeEmptyProject");
    }
    catch (org.xml.sax.SAXException ignore) {
      System.out.println("SAXException in makeEmptyProject");
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

  public void saveAllMembers(String path, boolean overwrite) {
      saveAllMembers(path, overwrite, null, null);
  }

  public void saveAllMembers(String path, boolean overwrite, Writer writer, ZipOutputStream zos) {

      if (writer == null) {
	  System.out.println("No Writer specified!");
	  return;
      }

    int size = _members.size();
    
    try {

	// make sure to save the XMI file first so we get the id references
	for (int i = 0; i < size; i++) {
	    ProjectMember p = (ProjectMember) _members.elementAt(i);
	    if (!(p.getType().equalsIgnoreCase("xmi"))){
		Argo.log.info("Saving member of type: " + ((ProjectMember)_members.elementAt(i)).getType());
		zos.putNextEntry(new ZipEntry(p.getName()));
		p.save(path,overwrite,writer);
		writer.flush();
		zos.closeEntry();
	    }
	}

	for (int i = 0; i < size; i++) {
	    ProjectMember p = (ProjectMember) _members.elementAt(i);
	    if (p.getType().equalsIgnoreCase("xmi")) {
		Argo.log.info("Saving member of type: " + ((ProjectMember)_members.elementAt(i)).getType());
		zos.putNextEntry(new ZipEntry(p.getName()));
		p.save(path,overwrite,writer);
	    }
	}

    } catch (IOException e) {
	System.out.println("hat nicht geklappt: "+e);
	e.printStackTrace();
    }
    // needs-more-work: check if each file is dirty
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

  public Vector getModels() { return _models; }

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

  public Vector getDefinedTypesVector() { return new Vector(_definedTypes.values()); }
  public Hashtable getDefinedTypes() { return _definedTypes; }
  public void setDefinedTypes(Hashtable h) { _definedTypes = h; }
  public void defineType(MClassifier cls) {
    //needs-more-work: should take namespaces into account!
    // this is a hack because names are not always being assigned under argo-nsuml branch - JH
    String name = cls.getName();
    if (name == null) name = "anon";
    _definedTypes.put(name,  cls);
  }
  public MClassifier findType(String s) {
    if (s != null) s = s.trim();
    if (s == null || s.length()==0) return null;
    MClassifier cls = null;
    int numModels = _models.size();
    for (int i = 0; i < numModels; i++) {
      cls = findTypeInModel(s, (MNamespace) _models.elementAt(i));
      if (cls != null) return cls;
    }
    cls = (MClassifier) _definedTypes.get(s);
    if (cls == null) {
		System.out.println("new Type defined!");
		cls = new MClassImpl();
		cls.setName(s);
		_definedTypes.put(s, cls);
	}
	if (cls.getNamespace() == null)
		cls.setNamespace(getCurrentNamespace());
    return cls;
  }

	public MClassifier findTypeInModel(String s, MNamespace ns) {
		// System.out.println("Looking for type "+s+" in Namespace "+ns.getName());
		Collection ownedElements = ns.getOwnedElements();
		Iterator oeIterator = ownedElements.iterator();

		while(oeIterator.hasNext()) {
			MModelElement me = (MModelElement)oeIterator.next();
			if (me instanceof MClassifier && (me.getName() != null && me.getName().equals(s)))
				return (MClassifier) me;
			if (me instanceof MNamespace) {
				MClassifier res = findTypeInModel(s, (MNamespace) me);
				if (res != null) return res;
			}
		}
		return null;
	}

  public void setCurrentNamespace(MNamespace m) { _curModel = m; }
  public MNamespace getCurrentNamespace() { return _curModel; }

  public Vector getDiagrams() { return _diagrams; }
  public void addDiagram(ArgoDiagram d) throws PropertyVetoException {
    // send indeterminate new value instead of making copy of vector
    getVetoSupport().fireVetoableChange("Diagrams", _diagrams, null);
    _diagrams.addElement(d);
    d.addChangeRegistryAsListener( _saveRegistry );
    setNeedsSave(true);
  }
  public void removeDiagram(ArgoDiagram d) throws PropertyVetoException {
    getVetoSupport().fireVetoableChange("Diagrams", _diagrams, null);
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

  public void addVetoableChangeListener(VetoableChangeListener l) {
    getVetoSupport().removeVetoableChangeListener(l);
    getVetoSupport().addVetoableChangeListener(l);
  }

  public void removeVetoableChangeListener(VetoableChangeListener l) {
    getVetoSupport().removeVetoableChangeListener(l);
  }

  public VetoableChangeSupport getVetoSupport() {
    if (_vetoSupport == null) _vetoSupport = new VetoableChangeSupport(this);
    return _vetoSupport;
  }

  public void preSave() {
    for (int i = 0; i < _diagrams.size(); i++)
      ((Diagram)_diagrams.elementAt(i)).preSave();
    // needs-more-work: is preSave needed for models?
  }

  public void postSave() {
    for (int i = 0; i < _diagrams.size(); i++)
      ((Diagram)_diagrams.elementAt(i)).postSave();
    // needs-more-work: is postSave needed for models?
    setNeedsSave(false);
  }

  public void postLoad() {
    for (int i = 0; i < _diagrams.size(); i++)
      ((Diagram)_diagrams.elementAt(i)).postLoad();
    // needs-more-work: is postLoad needed for models?
    setNeedsSave(false);
    // we don't need this HashMap anymore so free up the memory
    _UUIDRefs = null;
  }

  ////////////////////////////////////////////////////////////////
  // trash related methos

  // Attention: whole Trash mechanism should be rethought concerning nsuml
  public void moveToTrash(Object obj) {
	  if (Trash.SINGLETON.contains(obj)) return;
	  trashInternal(obj);

	/* old version
	  if (Trash.SINGLETON.contains(obj)) return;
	  Vector alsoTrash = null;
	  if (obj instanceof MModelElementImpl)
      alsoTrash = ((MModelElementImpl)obj).alsoTrash();
	  trashInternal(obj);
	  if (alsoTrash != null) {
      int numTrash = alsoTrash.size();
      for (int i = 0; i < numTrash; i++)
	  moveToTrash(alsoTrash.elementAt(i));
	  }
	*/


  }

  // Attention: whole Trash mechanism should be rethought concerning nsuml
  protected void trashInternal(Object obj) {
	  if (obj instanceof MClassifier) {
		  // System.out.println("trashInternal: "+obj);
		  MClassifier me = (MClassifier) obj;
		  // me.remove();
		  MMUtil.SINGLETON.remove(me);
	  }
	  if (obj instanceof MStateVertex) {
		  // System.out.println("trashInternal: "+obj);
		  MStateVertex me = (MStateVertex) obj;
		  // me.remove();
		  MMUtil.SINGLETON.remove(me);
	  }
	  if (obj instanceof MObject) {
		  // System.out.println("trashInternal: "+obj);
		  MObject me = (MObject) obj;
		  // me.remove();
		  MMUtil.SINGLETON.remove(me);
	  }
	  if (obj instanceof MStimulus) {
		  // System.out.println("trashInternal: "+obj);
		  MStimulus me = (MStimulus) obj;
		  // me.remove();
		  MMUtil.SINGLETON.remove(me);
	  }
	  else if (obj instanceof MModelElement) {
		  //System.out.println("trashInternal: "+obj);
		  MModelElement me = (MModelElement) obj;
		  me.remove();
	  }

	  /* old version
		 if (obj instanceof MModelElement) {
		 MModelElement me = (MModelElement) obj;
		 Vector places = new Vector();
		 java.util.Enumeration diagramEnum = _diagrams.elements();
		 while (diagramEnum.hasMoreElements()) {
		 Diagram d = (Diagram) diagramEnum.nextElement();
		 Fig f = d.getLayer().presentationFor(me);
		 while (f != null) {
		 f.delete();
		 if (!places.contains(f)) places.addElement(f);
		 f = d.getLayer().presentationFor(me);
		 } // end while
		 } // end while
		 Trash.SINGLETON.addItemFrom(obj, places);
		 if (obj instanceof MNamespace) trashDiagramsOn((MNamespace)obj);
		 }
		 // needs-more-work: trash diagrams

		 }

		 protected void trashDiagramsOn(MNamespace ns) {
		 //System.out.println("trashDiagramsOn: " + ns);
		 int size = _diagrams.size();
		 Vector removes = new Vector();
		 for (int i = 0; i < size; i++) {
		 Object obj = _diagrams.elementAt(i);
		 if (!(obj instanceof UMLDiagram)) continue;
		 if (ns == ((UMLDiagram)obj).getNamespace()) {
		 //System.out.println("found diagram to remove");
		 removes.addElement(obj);
		 }
		 }
		 int numRemoves = removes.size();
		 for (int i = 0; i < numRemoves; i++) {
		 Diagram d = (Diagram) removes.elementAt(i);
		 try { removeMember(d); }
		 catch (PropertyVetoException pve) { }
		 }

	  */
  }

  public void moveFromTrash(Object obj) {
    System.out.println("needs-more-work: not restoring " + obj);
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
    System.out.println("setStat: " + n + " = " + v);
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
      System.out.println("unknown UsageStatistic: " + n);
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

  static final long serialVersionUID = 1399111233978692444L;

} /* end class Project */


class ResetStatsLater implements Runnable {
  public void run() {
    Project.resetStats();
  }
} /* end class ResetStatsLater */
