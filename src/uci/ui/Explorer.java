package uci.ui;

/*
    This simple extension of the java.awt.Frame class
    contains all the elements necessary to act as the
    main window of an application.
 */

import java.awt.*;
import java.util.*;
import uci.util.*;
import symantec.itools.awt.*;


public class Explorer extends Frame {

  ////////////////////////////////////////////////////////////////
  // instance variables

//   /** The root object of the tree we are exploring.  All traversals
//    * start from here.  In WindowsExplorer this would be MyComputer.
//    * Needs-More-Work: add a _focus object that is where children are
//    * generated from, but is not the root of the tree.
//    * Needs-More-Work: allow multiple roots. */
//   protected Object _root = null;

//   /** This is a functor object that computes the children of any
//    * object in the tree.  Needs-More-Work: allow multiple
//    * generators. Needs-More-Work: define default generator that uses
//    * the Reflection API. */
//   protected ChildGenerator _gen = null;

  /** A table of menu item labels and the classes that they will
   * instanciate. These items will go under the create menu and in the
   * CreateMultiple dialog box.  Keys are the user-visiable labels.
   * Value are Class objects with a void constructor. */
  protected Hashtable _crateable = new Hashtable();

  /** A table of menu item labels and the tools that they will invoke.
   * Items are shown under a tools menu.  Keys are user-visiable
   * label.  Values are ActiveMenuItems, Functors, Strings that can be
   * passed to System.exec(), or Class objects with void constructors.
   * Needs-More-Work: not done yet. */
  protected Hashtable _tools = new Hashtable();

  /* Vector of strings that define which packages to look in when
   * finding an editor for a selected object. Needs-More-Work: not
   * used yet. */
  protected Vector _editorPaths = new Vector();

  protected Hashtable _editors = new Hashtable();


  /** Any events that are not handled by this class are forward to the
   * _defaultEventHandler (if it is not null). */
  protected IEventHandler _defaultEventHandler = null;


  ////////////////////////////////////////////////////////////////
  // constructors

  public Explorer(String title) {
    this();
    setTitle(title);
  }

  public Explorer() {
    //{{INIT_CONTROLS
    setLayout(new BorderLayout(0,0));
    addNotify();
    resize(insets().left + insets().right + 815,insets().top + insets().bottom + 529);
    openFileDialog = new java.awt.FileDialog(this, "Open",FileDialog.LOAD);
    //$$ openFileDialog.move(120,444);
    toolBarPanel = new ToolBarPanel();
    //    toolBarPanel = new symantec.itools.awt.util.ToolBarPanel();
    toolBarPanel.setLayout(null);
    toolBarPanel.reshape(insets().left + 0,insets().top + 0,815,26);
    toolBarPanel.setBackground(new Color(12632256));
    add("North", toolBarPanel);
    splitterPanel = new SplitterPanel();
    splitterPanel.split(splitterPanel.SPLIT_VER);
    add("Center", splitterPanel);
    statusBar = new StatusBar();
    statusBar.setFont(new Font("Dialog", Font.PLAIN, 10));
    statusBar.setInternalInsets(new Insets(10, 10, 10, 10));    
    statusBar.setBevelStyle(BevelStyle.BEVEL_LOWERED);
    add("South", statusBar);
    toolBarPanel.setIPadTop(0);
    toolBarPanel.setIPadBottom(0);
    toolBarPanel.setIPadSides(0);
//     imageButton1 = new ImageButton();
//     imageButton1.reshape(0,0,25,25);
//     toolBarPanel.add(imageButton1);
//     imageButton2 = new ImageButton();
//     imageButton2.reshape(25,0,25,25);
//     toolBarPanel.add(imageButton2);
//     imageButton3 = new ImageButton();
//     imageButton3.reshape(50,0,25,25);
//     toolBarPanel.add(imageButton3);
//     imageButton4 = new ImageButton();
//     imageButton4.reshape(75,0,25,25);
//     toolBarPanel.add(imageButton4);
//     toolBarSpacer1 = new ToolBarSpacer();
//     toolBarSpacer1.reshape(100,0,5,25);
//     toolBarPanel.add(toolBarSpacer1);
//     imageButton5 = new ImageButton();
//     imageButton5.reshape(105,0,25,25);
//     toolBarPanel.add(imageButton5);
//     imageButton6 = new ImageButton();
//     imageButton6.reshape(130,0,25,25);
//     toolBarPanel.add(imageButton6);
    objectEditorPanel = new java.awt.Panel();
    objectEditorPanel.setLayout(null);
    objectEditorPanel.reshape(insets().left + 175,insets().top + 26,640,503);
    //add("Center", objectEditorPanel);
    splitterPanel.getRightPanel().add(objectEditorPanel);
    treePers = new TreePerspective();
    treePers.reshape(insets().left + 0,insets().top + 26,175,503);
    treePers.setFont(new Font("Dialog", Font.PLAIN, 12));
    //		add("West", treePers);
    splitterPanel.getLeftPanel().add(treePers);
    setTitle("CoRE Tool Prototype");
    //}}

    //{{INIT_MENUS
    mainMenuBar = new java.awt.MenuBar();

    fileMenu = new java.awt.Menu("File");
    newItem = new java.awt.MenuItem("New");
    fileMenu.add(newItem);
    openItem = new java.awt.MenuItem("Open...");
    fileMenu.add(openItem);
    saveItem = new java.awt.MenuItem("Save");
    fileMenu.add(saveItem);
    saveAsItem = new java.awt.MenuItem("Save");
    fileMenu.add(saveAsItem);
    printItem = new java.awt.MenuItem("Print");
    fileMenu.add(printItem);
    fileMenu.addSeparator();
    exitItem = new java.awt.MenuItem("Exit");
    fileMenu.add(exitItem);
    mainMenuBar.add(fileMenu);

    editMenu = new java.awt.Menu("Edit");
    undoItem = new java.awt.MenuItem("Undo");
    editMenu.add(undoItem);
    cutItem = new java.awt.MenuItem("Cut");
    editMenu.add(cutItem);
    copyItem = new java.awt.MenuItem("Copy");
    editMenu.add(copyItem);
    pasteItem = new java.awt.MenuItem("Paste");
    editMenu.add(pasteItem);
    mainMenuBar.add(editMenu);

    viewMenu = new java.awt.Menu("View", true);
    newWindowItem = new java.awt.MenuItem("New Window");
    viewMenu.add(newWindowItem);
    sortItem = new java.awt.MenuItem("Sort...");
    viewMenu.add(sortItem);
    optionsItem = new java.awt.MenuItem("Options...");
    viewMenu.add(optionsItem);
    refreshItem = new java.awt.MenuItem("Refresh");
    viewMenu.add(refreshItem);
    mainMenuBar.add(viewMenu);

    createMenu = new java.awt.Menu("Create", true);
//     createClassItem = new java.awt.MenuItem("Class");
//     createMenu.add(createClassItem);
//     createTypeItem = new java.awt.MenuItem("Type");
//     createMenu.add(createTypeItem);
//     createModeClassItem = new java.awt.MenuItem("Mode Class");
//     createMenu.add(createModeClassItem);
//     createConstantItem = new java.awt.MenuItem("Constant");
//     createMenu.add(createConstantItem);
//     createVariableItem = new java.awt.MenuItem("Variable");
//     createMenu.add(createVariableItem);
//     createAssertionItem = new java.awt.MenuItem("Assertion");
//     createMenu.add(createAssertionItem);
    mainMenuBar.add(createMenu);

    toolsMenu = new java.awt.Menu("Tools", true);
//     ccCheckerItem = new java.awt.MenuItem("CC Checker");
//     ccCheckerItem.disable();
//     toolsMenu.add(ccCheckerItem);
//     simulatorItem = new java.awt.MenuItem("Simulator");
//     simulatorItem.disable();
//     toolsMenu.add(simulatorItem);
//     dependencyGraphItem = new java.awt.MenuItem("Dependency Graph");
//     dependencyGraphItem.disable();
//     toolsMenu.add(dependencyGraphItem);
    mainMenuBar.add(toolsMenu);

    helpMenu = new java.awt.Menu("Help");
    mainMenuBar.setHelpMenu(helpMenu);
    aboutItem = new java.awt.MenuItem("About...");
    helpMenu.add(aboutItem);
    mainMenuBar.add(helpMenu);
    setMenuBar(mainMenuBar);
    //$$ mainMenuBar.move(96,444);
    //}}
    setStatus("Welcome to the CoRE Prototype");
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public Object getRootObject() { return treePers.getRootObject(); }
  public void setRootObject(Object root) { treePers.setRootObject(root); }


  public void setChildGenerator(ChildGenerator cg) {
    treePers.setChildGenerator(cg);
  }
  public ChildGenerator getChildGenerator() {
    return treePers.getChildGenerator();
  }

  public void setFilter(Predicate f) { treePers.setFilter(f); }
  public Predicate getFilter() { return treePers.getFilter(); }


  ////////////////////////////////////////////////////////////////
  // display related methods

  public void setStatus(String s) { statusBar.setStatusText(s); }

  public synchronized void show() {
    move(50, 50);
    super.show();
    splitterPanel.getLeftPanel().setMinimumSize(new Dimension(150, 50));
    splitterPanel.setEnforceMinDim(true);
    splitterPanel.moveSplit(-2000, 10);
    splitterPanel.setEnforceMinDim(false);
  }



//   protected int _nextClassNum = 1;

//   public void createRqtsSpec() { setRqtsSpec(new RqtsSpec()); }

//   public void setRqtsSpec(RqtsSpec rs) {
//     _RqtsSpec = rs;
//     treePers.setRootObject(_RqtsSpec);
//   }

//   public void createClass() {
//     createClass("Class" + _nextClassNum++);
//   }

//   public void createClass(String packName) {
//     CoreClass p = new CoreClass(packName);
//     _RqtsSpec.addCoreClass(p);
//   }

//   public CoreClass selectedClass() {
//     Editable ed = treePers.selectedObject();
//     if (ed == null) return null;
//     if (ed instanceof CoreClass) return (CoreClass) ed;
//     else return ed.getCoreClass();
//   }

//   protected int _nextVariableName = 1;

//   public void createVariable() {
//     CoreClass p = selectedClass();
//     if (p == null) return; // you must select a package first
//     Variable v = new Variable("Variable" + _nextVariableName++, p);
//     p.addVariable(v);
//   }

//   protected int _nextAssertionName = 1;
//   public void createAssertion() {
//     CoreClass p = selectedClass();
//     if (p == null) return; // you must select a package first
//     Assertion v = new Assertion("Assertion" + _nextAssertionName++, p);
//     p.addAssertion(v);
//   }

//   protected int _nextTypeName = 1;
//   public void createType() {
//     CoreClass p = selectedClass();
//     if (p == null) return; // you must select a package first
//     Type v = new Type("Type" + _nextTypeName++, p);
//     p.addType(v);
//   }


//   protected int _nextConstantName = 1;
//   public void createConstant() {
//     CoreClass p = selectedClass();
//     if (p == null) return; // you must select a package first
//     Constant v = new Constant("Constant" + _nextConstantName++, p);
//     p.addConstant(v);
//   }

//   protected int _nextModeClassName = 1;
//   public void createModeClass() {
//     CoreClass p = selectedClass();
//     if (p == null) return; // you must select a package first
//     ModeClass v = new ModeClass("ModeClass" + _nextModeClassName++, p);
//     p.addModeClass(v);
//   }


  ////////////////////////////////////////////////////////////////
  // customization related methods

  /** Add a new menu to the Explorer menu bar.  Action Events about
   * items on this menu will be sent to _defaultEventHandler.  */
  public void addMenu(Menu m) { mainMenuBar.add(m); }

  /** Add a new menu item to the Explorer View menu.  Action Events
   * about this menu item will be sent to _defaultEventHandler.
   * Needs-More-Work: should add after Print, before Exit. */
  public void addFileMenuItem(MenuItem mi) { fileMenu.add(mi); }

  /** Add a new menu item to the Explorer View menu.  Action Events about
   * this menu item will be sent to _defaultEventHandler.  */
  public void addViewMenuItem(MenuItem mi) { viewMenu.add(mi); }

  /** Add a new menu item to the Explorer View menu.  Action Events about
   * this menu item will be sent to _defaultEventHandler.  */
  public void addHelpMenuItem(MenuItem mi) { helpMenu.add(mi); }

  ////////////////////////////////////////////////////////////////
  // event handling

  public boolean handleEvent(Event event) {
    if (event.id == Event.WINDOW_DESTROY) {
      hide();         // hide the Frame
      dispose();      // free the system resources
      System.exit(0); // close the application
      return true;
    }
    if (event.target == treePers && event.id == Event.MOUSE_UP) {
      treePers_MouseUp(event);
      return true;
    }
    if (event.target == treePers && event.id == Event.KEY_ACTION_RELEASE) {
      treePers_KeyActionRelease(event);
      return true;
    }
    if (_defaultEventHandler != null &&
	_defaultEventHandler.handleEvent(event)) {
      return true;
    }
    return super.handleEvent(event);
  }

  public boolean action(Event event, Object arg) {
    if (event.target instanceof MenuItem) {
      String label = (String) arg;
//       if (event.target == createAssertionItem) {
// 	createAssertionItem_Action(event);
// 	return true;
//       }
//       else if (event.target == createConstantItem) {
// 	createConstantItem_Action(event);
// 	return true;
//       }
//       else if (event.target == createModeClassItem) {
// 	createModeClassItem_Action(event);
// 	return true;
//       }
//       else if (event.target == createTypeItem) {
// 	createTypeItem_Action(event);
// 	return true;
//       }
//       else if (event.target == createClassItem) {
// 	createClassItem_Action(event);
// 	return true;
//       }
//       else if (event.target == createVariableItem) {
// 	createVariableItem_Action(event);
// 	return true;
//       }
//       else
      if (label.equalsIgnoreCase("Open...")) {
	Open_Action(event);
	return true;
      }
      else if (label.equalsIgnoreCase("About")) {
	About_Action(event);
	return true;
      }
      else if (label.equalsIgnoreCase("Exit")) {
	Exit_Action(event);
	return true;
      }
      else if (_defaultEventHandler != null &&
	       _defaultEventHandler.handleMenuEvent(event)) {
	return true;
      }
    }
    if (_defaultEventHandler != null &&
	_defaultEventHandler.handleEvent(event)) {
      return true;
    }
    return super.action(event, arg);
  }

  void treePers_KeyActionRelease(Event event) { updateObjectEditor();  }

  void treePers_MouseUp(Event event) { updateObjectEditor(); }

  void deselect() { }
  void select(Object target) { }

  public void updateObjectEditor() {
    TreeNode selectedNode = treePers.getSelectedNode();
    enableMenus(selectedNode);
    if (selectedNode != null) {
      Object obj = treePers.itemFor(selectedNode);
      objectEditorPanel.removeAll();
      Panel editor = editorFor(obj);
      if (editor != null) objectEditorPanel.add(editor);
      else System.out.println("no editor");
    }
  }

  /* EditorPanel is somewhat like java.beans.Customizer */
  public Panel editorFor(Object obj) {
    Class objClass = obj.getClass();
    EditorPanel ep = (EditorPanel) _editors.get(objClass);
    if (ep == null) {
      try {
	String editorClassName = objClass.getName() + "Editor";
	// Needs-More-Work: use editor path
	Class editorClass = Class.forName(editorClassName);
	ep = (EditorPanel) editorClass.newInstance();
	if (ep instanceof EditorPanel) _editors.put(objClass, ep);
      }
      catch (java.lang.ClassNotFoundException ignore) { }
      catch (java.lang.Exception ignore) { ignore.printStackTrace(); }
    }
    ep.setObject(obj);
    return ep;
  }

  public void enableMenus(TreeNode selectedNode) {
    boolean e = (selectedNode != null); 
//     createTypeItem.enable(e);
//     createModeClassItem.enable(e);
//     createConstantItem.enable(e);
//     createVariableItem.enable(e);
//     createAssertionItem.enable(e);
  }

  void Open_Action(Event event) {
    //{{CONNECTION
    // Action from Open... Show the OpenFileDialog
    openFileDialog.show();
    //}}
  }

  void About_Action(Event event) {
    //{{CONNECTION
    // Action from About Create and show as modal
    (new AboutDialog(this, true)).show();
    //}}
  }

  void Exit_Action(Event event) {
    //{{CONNECTION
    // Action from Exit Create and show as modal
    (new QuitDialog(this, true)).show();
    //}}
  }

  ////////////////////////////////////////////////////////////////
  // widget instance variables

  //{{DECLARE_CONTROLS
  java.awt.FileDialog openFileDialog;
  ToolBarPanel toolBarPanel;
//   ImageButton imageButton1;
//   ImageButton imageButton2;
//   ImageButton imageButton3;
//   ImageButton imageButton4;
//   ToolBarSpacer toolBarSpacer1;
//   ImageButton imageButton5;
//   ImageButton imageButton6;
  java.awt.Panel objectEditorPanel;
  TreePerspective treePers;
  SplitterPanel splitterPanel;
  StatusBar statusBar;
  //}}

  //{{DECLARE_MENUS
  java.awt.MenuBar mainMenuBar;
  java.awt.Menu fileMenu;
  java.awt.MenuItem newItem;
  java.awt.MenuItem openItem;
  java.awt.MenuItem saveItem;
  java.awt.MenuItem saveAsItem;
  java.awt.MenuItem printItem;
  java.awt.MenuItem exitItem;
  java.awt.Menu editMenu;
  java.awt.MenuItem undoItem;
  java.awt.MenuItem cutItem;
  java.awt.MenuItem copyItem;
  java.awt.MenuItem pasteItem;
  java.awt.Menu viewMenu;
  java.awt.MenuItem newWindowItem;
  java.awt.MenuItem optionsItem;
  java.awt.MenuItem sortItem;
  java.awt.MenuItem refreshItem;
  java.awt.Menu createMenu;
//   java.awt.MenuItem createClassItem;
//   java.awt.MenuItem createTypeItem;
//   java.awt.MenuItem createModeClassItem;
//   java.awt.MenuItem createConstantItem;
//   java.awt.MenuItem createVariableItem;
//   java.awt.MenuItem createAssertionItem;
  java.awt.Menu toolsMenu;
//   java.awt.MenuItem ccCheckerItem;
//   java.awt.MenuItem simulatorItem;
//   java.awt.MenuItem dependencyGraphItem;
  java.awt.Menu helpMenu;
  java.awt.MenuItem aboutItem;
  //}}
} /* end class Explorer */

