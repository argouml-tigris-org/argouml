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




package uci.uml.ui;

import java.io.*;
import java.util.*;
import java.beans.*;
import java.net.*;

import uci.gef.*;
import uci.argo.kernel.*;
import uci.argo.checklist.*;
import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.uml.generate.*;
import uci.uml.visual.*;
import uci.xml.argo.*;
import uci.xml.xmi.*;
import uci.xml.pgml.*;
import uci.util.*;

/** A datastructure that represents the designer's current project.  A
 *  Project consists of diagrams and UML models. */

public class Project implements java.io.Serializable {
  ////////////////////////////////////////////////////////////////
  // constants
  public static final String SEPARATOR = "/";
  public final static String FILE_EXT = ".argo";
  public final static String TEMPLATES = "/uci/uml/templates/";
  //public final static String EMPTY_PROJ = "EmptyProject" + FILE_EXT;
  public final static String UNTITLED_FILE = "Untitled";

  ////////////////////////////////////////////////////////////////
  // instance variables

  //public String _pathname = "";
  //public String _filename = UNTITLED_FILE + FILE_EXT;

  //needs-more-work should just be the directory to write
  private URL _url = null;

  public String _authorname = "";
  public String _description = "";
  public String _version = "";

  public Vector _searchpath = new Vector();
  public Vector _members = new Vector();
  public String _historyFile = "";

  public Vector _models = new Vector(); //instances of Model
  public Vector _diagrams = new Vector(); // instances of LayerDiagram
  public boolean _needsSave = false;
  public Namespace _curModel = null;
  public Hashtable _definedTypes = new Hashtable(80);
  public Hashtable _idRegistry = new Hashtable(80);
  public GenerationPreferences _cgPrefs = new GenerationPreferences();
  public transient VetoableChangeSupport _vetoSupport = null;

  ////////////////////////////////////////////////////////////////
  // constructor

  public Project(File file) throws MalformedURLException, IOException {
    this(Util.fileToURL(file));
  }

  public Project(URL url) {
    _url = Util.fixURLExtension(url, FILE_EXT);
  }

  public Project() { }


  public static Project makeEmptyProject() {
    System.out.println("making empty project");
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

    p.addSearchPath("PROJECT_DIR");

    Model m1 = new Model("untitledpackage");
    try {
      p.addMember(new UMLClassDiagram(m1));
      p.addMember(new UMLUseCaseDiagram(m1));
      p.addMember(m1);
      p.setNeedsSave(false);
    }
    catch (PropertyVetoException pve) { }

    Runnable resetStatsLater = new ResetStatsLater();
    uci.uml.Main.addPostLoadAction(resetStatsLater);

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

  public Hashtable getIDRegistry() { return _idRegistry; }

  public String getBaseName() {
    String n = getName();
    if (!n.endsWith(FILE_EXT)) return n;
    return n.substring(0, n.length() - FILE_EXT.length());
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
    System.out.println("s = " + s);
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

  public void addMember(Diagram d) throws PropertyVetoException {
    ProjectMember pm = new ProjectMemberDiagram(d, this);
    addDiagram(d);
    // if diagram added successfully, add the member too
    _members.addElement(pm);
  }

  public void addMember(Model m) throws PropertyVetoException {
    if (_models.contains(m)) return;
    ProjectMember pm = new ProjectMemberModel(m, this);
    addModel(m);
    // got past the veto, add the member
    _members.addElement(pm);
  }

  public void addModel(Namespace m) throws PropertyVetoException {
    // fire indeterminate change to avoid copying vector
    getVetoSupport().fireVetoableChange("Models", _models, null);
    if (! _models.contains(m)) _models.addElement(m);
    setCurrentNamespace(m);
    _needsSave = true;
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


  public void removeMember(Diagram d) throws PropertyVetoException {
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
    Dbg.log("uci.uml.ui.Project", "Reading " + url);
    ArgoParser.SINGLETON.readProject(url);
    Project p = ArgoParser.SINGLETON.getProject();
    p.loadAllMembers();
    p.postLoad();
    Dbg.log("uci.uml.ui.Project", "Done reading " + url);
    return p;
  }

//   public void loadAllMembers() {
//     for (Enumeration enum = members.elements(); enum.hasMoreElements(); ) {
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
    Enumeration enum = getMembers().elements();
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
    int size = _members.size();
    for (int i = 0; i < size; i++)
      ((ProjectMember)_members.elementAt(i)).save(path, overwrite);
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

  public boolean getNeedsSave() { return _needsSave; }
  public void setNeedsSave(boolean ns) { _needsSave = ns; }
  public void needsSave() { setNeedsSave(true); }

  public Vector getModels() { return _models; }
//   public void addModel(Namespace m) throws PropertyVetoException {
//     getVetoSupport().fireVetoableChange("Models", _models, m);
//     _models.addElement(m);
//     setCurrentNamespace(m);
//     _needsSave = true;
//   }

  public Vector getDefinedTypesVector() {
    Vector res = new Vector();
    Enumeration enum = _definedTypes.elements();
    while (enum.hasMoreElements()) res.addElement(enum.nextElement());
    return res;
  }
  public Hashtable getDefinedTypes() { return _definedTypes; }
  public void setDefinedTypes(Hashtable h) { _definedTypes = h; }
  public void defineType(Classifier cls) {
    //needs-more-work: should take namespaces into account!
    _definedTypes.put(cls.getName().getBody(), cls);
  }
  public Classifier findType(String s) {
    if (s != null) s = s.trim();
    if (s == null || s.length()==0) return null;
    Classifier cls = null;
    int numModels = _models.size();
    for (int i = 0; i < numModels; i++) {
      cls = findTypeInModel(s, (Namespace) _models.elementAt(i));
      if (cls != null) return cls;
    }
    cls = (Classifier) _definedTypes.get(s);
    if (cls == null) {
      cls = new MMClass(s);
      _definedTypes.put(s, cls);
    }
    return cls;
  }

  public Classifier findTypeInModel(String s, Namespace ns) {
    Vector ownedElements = ns.getOwnedElement();
    int size = ownedElements.size();
    for (int i = 0; i < size; i++) {
      ElementOwnership eo = (ElementOwnership) ownedElements.elementAt(i);
      ModelElement me = eo.getModelElement();
      if (me instanceof Classifier && me.getName().getBody().equals(s))
	return (Classifier) me;
      if (me instanceof Namespace) {
	Classifier res = findTypeInModel(s, (Namespace) me);
	if (res != null) return res;
      }
    }
    return null;
  }

  public void setCurrentNamespace(Namespace m) { _curModel = m; }
  public Namespace getCurrentNamespace() { return _curModel; }

  public Vector getDiagrams() { return _diagrams; }
  public void addDiagram(Diagram d) throws PropertyVetoException {
    // send indeterminate new value instead of making copy of vector
    getVetoSupport().fireVetoableChange("Diagrams", _diagrams, null);
    _diagrams.addElement(d);
    _needsSave = true;
  }
  public void removeDiagram(Diagram d) throws PropertyVetoException {
    getVetoSupport().fireVetoableChange("Diagrams", _diagrams, null);
    _diagrams.removeElement(d);
    _needsSave = true;
  }

  public int getPresentationCountFor(ModelElement me) {
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
    _needsSave = false;
  }

  public void postLoad() {
    for (int i = 0; i < _diagrams.size(); i++)
      ((Diagram)_diagrams.elementAt(i)).postLoad();
    // needs-more-work: is postLoad needed for models?
    _needsSave = false;
  }

  ////////////////////////////////////////////////////////////////
  // trash related methos
  public void moveToTrash(Object obj) {
    if (Trash.SINGLETON.contains(obj)) return;
    Vector alsoTrash = null;
    if (obj instanceof ModelElementImpl)
      alsoTrash = ((ModelElementImpl)obj).alsoTrash();
    trashInternal(obj);
    if (alsoTrash != null) {
      int numTrash = alsoTrash.size();
      for (int i = 0; i < numTrash; i++)
	moveToTrash(alsoTrash.elementAt(i));
    }
  }

  protected void trashInternal(Object obj) {
    //System.out.println("trashing: " + obj);
    if (obj instanceof ModelElement) {
      ModelElement me = (ModelElement) obj;
      Vector places = new Vector();
      Enumeration diagramEnum = _diagrams.elements();
      while (diagramEnum.hasMoreElements()) {
	Diagram d = (Diagram) diagramEnum.nextElement();
	Fig f = d.getLayer().presentationFor(me);
	while (f != null) {
	  f.delete();
	  if (!places.contains(f)) places.addElement(f);
	  f = d.getLayer().presentationFor(me);
	} /* end while */
      } /* end while */
      Trash.SINGLETON.addItemFrom(obj, places);
      if (obj instanceof Namespace) trashDiagramsOn((Namespace)obj);
    }
    // needs-more-work: trash diagrams
  }

  protected void trashDiagramsOn(Namespace ns) {
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
