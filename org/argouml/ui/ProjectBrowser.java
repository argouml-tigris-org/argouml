// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.*;
import org.tigris.gef.ui.*;
import org.tigris.gef.util.*;

import org.argouml.application.api.*;
import org.argouml.application.events.*;
import org.argouml.kernel.*;
import org.argouml.cognitive.*;
import org.argouml.cognitive.ui.*;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.*;
import org.argouml.swingext.*;

/** The main window of the Argo/UML application. */

public class ProjectBrowser extends JFrame
implements IStatusBar, NavigationListener, ArgoModuleEventListener {
  ////////////////////////////////////////////////////////////////
  // constants

  private static int DEFAULT_COMPONENTWIDTH = 220;
  private static int DEFAULT_COMPONENTHEIGHT = 200;

  ////////////////////////////////////////////////////////////////
  // class variables

  public static ProjectBrowser TheInstance;


  protected static Action _actionCreateMultiple = Actions.CreateMultiple;
  // ----- diagrams
  /** this fires the Class Diagram view
   */
  protected static Action _actionClassDiagram = ActionClassDiagram.SINGLETON;
  /** this fires the use case Diagram view
   */
  protected static Action _actionUseCaseDiagram = ActionUseCaseDiagram.SINGLETON;
  /** this fires the state Diagram view
   */
  protected static Action _actionStateDiagram = ActionStateDiagram.SINGLETON;
  /** this fires the activity view
   */
  protected static Action _actionActivityDiagram = ActionActivityDiagram.SINGLETON;
  /** this fires the Collaboration Diagram view
   */
  protected static Action _actionCollaborationDiagram = ActionCollaborationDiagram.SINGLETON;
  /** this fires the deployment Diagram view
   */
  protected static Action _actionDeploymentDiagram = ActionDeploymentDiagram.SINGLETON;
  /** this fires the sequence Diagram view
   */
  protected static Action _actionSequenceDiagram = ActionSequenceDiagram.SINGLETON;

  // ----- model elements
  //protected static Action _actionModel = Actions.MModel;
  protected static Action _actionAddTopLevelPackage = ActionAddTopLevelPackage.SINGLETON;

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected String _appName = "ProjectBrowser";
  protected Project _project = null;

  protected NavigatorPane _navPane;
  /** The toDoPane currently does not remember todos
   * nor is there a way of exporting todos to anothter
   * format
   */
  public ToDoPane _toDoPane;
  protected MultiEditorPane _editorPane;
  protected DetailsPane _northEastPane;
  protected DetailsPane _northPane;
  protected DetailsPane _northWestPane;
  protected DetailsPane _eastPane;
  protected DetailsPane _southEastPane;
  protected DetailsPane _southPane;
  
  private Map detailsPanesByCompassPoint = new HashMap();
  
  protected JMenuBar _menuBar = new JMenuBar();

  /** Submenu of file for importing sources and other things.
   */
  protected JMenu _import = null;

  /** Edit menu
   */
  protected JMenu _edit = null;
  /** unknown where this appears in the UI
   */
  protected JMenu _select = null;
  /** toobar: view under which is the goto diagram/ find
   * zoom!!! this should be activated as a right click command.
   * editor tabs/details tabs/ adjust grid etc.*
   */
  protected ArgoJMenu _view = null;
  /** Toolbar:create diagram
   */
  protected JMenu _createDiagrams = null;
  /** currently disactivated
   */
  protected JMenu _tools = null;
  /** currently supports rudimentary java generation,
   * new modules for php and html/javadocs are planned
   * feel free to contribute here!
   */
  protected JMenu _generate = null;
  /** this should be invoked automatically when
   * importing sources.
   */
  protected JMenu _arrange = null;
  /** currently undergoing significant testing
   */
  protected ArgoJMenu _critique = null;
  /** It needs it. Currently there is only an
   * about text. hyperlinking to online docs at
   * argouml.org considered basic improvement.
   */
  protected JMenu _help = null;
  /**
   * The menu item for hiding/showing the Details pane.
   */
  //protected JCheckBoxMenuItem _showDetailsMenuItem = null;
  /** partially implemented. needs work to display
   * import of source and saving of zargo
   */
  protected StatusBar _statusBar = new StatusBar();
  //protected JToolBar _toolBar = new JToolBar();

  /** this needs work so that users can set the font
   * size through a gui preference window
   */
  public Font defaultFont = new Font("Dialog", Font.PLAIN, 10);
  //  public static JFrame _Frame;

  //protected JSplitPane _mainSplit, _topSplit, _botSplit;
  protected BorderSplitPane _workarea;
  private NavigationHistory _history = new NavigationHistory();

  /**
   * The diagram which the user is currently working on.
   */
  private ArgoDiagram _activeDiagram;


  ////////////////////////////////////////////////////////////////
  // constructors

    public ProjectBrowser() {new ProjectBrowser("Test",null,0);}

    public ProjectBrowser(String appName, StatusBar sb, int theme) {
	super(appName);
	setCurrentTheme(theme);
        sb.showStatus("Making Project Browser: Navigator Pane");
        sb.incProgress(5);
        _navPane = new NavigatorPane();
        sb.showStatus("Making Project Browser: To Do Pane");
        sb.incProgress(5);
        _toDoPane = new ToDoPane();
        _editorPane = new MultiEditorPane(sb);
        _editorPane.addNavigationListener(this);
        
        _eastPane      = makeDetailsPane(sb, BorderSplitPane.EAST.toLowerCase(), Vertical.getInstance());
        _southPane     = makeDetailsPane(sb, BorderSplitPane.SOUTH. toLowerCase(), Horizontal.getInstance());
        _southEastPane = makeDetailsPane(sb, BorderSplitPane.SOUTHEAST.toLowerCase(), Horizontal.getInstance());
        _northWestPane = makeDetailsPane(sb, BorderSplitPane.NORTHWEST.toLowerCase(), Horizontal.getInstance());
        _northPane     = makeDetailsPane(sb, BorderSplitPane.NORTH.toLowerCase(), Horizontal.getInstance());
        _northEastPane = makeDetailsPane(sb, BorderSplitPane.NORTHEAST.toLowerCase(), Horizontal.getInstance());

        if (_southPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.SOUTH, _southPane);
        if (_southEastPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.SOUTHEAST, _southEastPane);
        if (_eastPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.EAST, _eastPane);
        if (_northWestPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.NORTHWEST, _northWestPane);
        if (_northPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.NORTH, _northPane);
        if (_northEastPane != null) detailsPanesByCompassPoint.put(BorderSplitPane.NORTHEAST, _northEastPane);

        getTabProps().addNavigationListener(this);

        setAppName(appName);
        if (TheInstance == null) TheInstance = this;
        //setName(title);
        //loadImages();
        getContentPane().setFont(defaultFont);
        getContentPane().setLayout(new BorderLayout());
        initMenus();
        //initToolBar();
        getContentPane().add(_menuBar, BorderLayout.NORTH);
        //JPanel p = new JPanel();
        //p.setLayout(new BorderLayout());
        //getContentPane().add(p, BorderLayout.CENTER);
        //p.add(_toolBar, BorderLayout.NORTH);
        getContentPane().add(createPanels(), BorderLayout.CENTER);
        getContentPane().add(_statusBar, BorderLayout.SOUTH);
        _toDoPane.setRoot(Designer.TheDesigner.getToDoList());

        // allows me to ask "Do you want to save first?"
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowCloser());
        ImageIcon argoImage = ResourceLoader.lookupIconResource("Model");
        this.setIconImage(argoImage.getImage());
    }
    
//   void loadImages() {
//     String s = "A blue bullet icon - to draw attention to a menu item";
//     blueDot = loadImageIcon("images/dot.gif", s);
//     s = "A red bullet icon - to draw attention to a menu item";
//     redDot = loadImageIcon("images/redDot.gif", s);
//   }

  public Locale getLocale() {
    return Locale.getDefault();
  }

  static final protected KeyStroke getShortcut(String key) {
    return Localizer.getShortcut("CoreMenu",key);
  }

  /** This should be a user specified option. New laws
   * for handicapped people who cannot use the
   * mouse require software developers in US to
   * make all components of User interface accessible
   * through keyboard
   */
  static final protected void setMnemonic(JMenuItem item,String key,char defMnemonic) {
    String localMnemonic = Argo.localize("CoreMenu","Mnemonic_" + key);
    char mnemonic = defMnemonic;
    if(localMnemonic != null && localMnemonic.length() == 1) {
      mnemonic = localMnemonic.charAt(0);
    }
    item.setMnemonic(mnemonic);
  }

  static final protected String menuLocalize(String key) {
    return Argo.localize("CoreMenu",key);
  }

  static final protected void setAccelerator(JMenuItem item,KeyStroke keystroke) {
    if(keystroke != null) {
      item.setAccelerator(keystroke);
    }
  }

    /** Scans through all loaded modules to see if it has an item to add
     * in this diagram.
     *
     * @param menuitem The menuitem which this menuitem would attach to.
     * @param key Non-localized string that tells the module where we are.
     */
  private void appendPluggableMenus(JMenuItem menuitem, String key) {
    Object[] context = { menuitem, key };
    ArrayList arraylist = Argo.getPlugins(PluggableMenu.class, context);
    ListIterator iterator = arraylist.listIterator();
    while (iterator.hasNext()) {
      PluggableMenu module = (PluggableMenu)iterator.next();
	    menuitem.add(module.getMenuItem(context));
	    menuitem.setEnabled(true);
    }
  }

  protected void initMenus() {
    KeyStroke ctrlN = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK);
    KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK);
    KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK);
    KeyStroke ctrlP = KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK);
    KeyStroke ctrlA = KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK);
    KeyStroke ctrlC = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK);
    KeyStroke ctrlV = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK);
    KeyStroke ctrlX = KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK);
    KeyStroke ctrlR = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK);

    KeyStroke F3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
    KeyStroke F7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
    KeyStroke altF4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK);

    KeyStroke delKey = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);

    JMenuItem mi;
    // File Menu
    JMenu file = new JMenu(menuLocalize("File"));
    _menuBar.add(file);
    setMnemonic(file,"File",'F');
    JMenuItem newItem = file.add(ActionNew.SINGLETON);
    setMnemonic(newItem,"New",'N');
    setAccelerator(newItem,ctrlN);
    JMenuItem openProjectItem = file.add(ActionOpenProject.SINGLETON);
    setMnemonic(openProjectItem,"Open",'O');
    setAccelerator(openProjectItem,ctrlO);
    //JMenuItem saveItem = file.add(_actionSave);
    //file.add(_actionSaveAs);
    //file.add(_actionSaveAsXMI);
    JMenuItem saveProjectItem = file.add(ActionSaveProject.SINGLETON);
    setMnemonic(saveProjectItem,"Save",'S');
    setAccelerator(saveProjectItem,ctrlS);
    JMenuItem saveProjectAsItem = file.add(ActionSaveProjectAs.SINGLETON);
    setMnemonic(saveProjectAsItem,"SaveAs",'A');
    file.addSeparator();

    _import = new JMenu(menuLocalize("Import"));
    JMenuItem importProjectAsItem = _import.add(ActionImportFromSources.SINGLETON);
    appendPluggableMenus(_import, "File:Import");
    file.add(_import);

    file.addSeparator();
    // JMenuItem loadModelFromDBItem = file.add(ActionLoadModelFromDB.SINGLETON);
    // JMenuItem storeModelToDBItem = file.add(ActionStoreModelToDB.SINGLETON);
    // file.addSeparator();
    JMenuItem printItem = file.add(Actions.Print);
    setMnemonic(printItem,"Print",'P');
    setAccelerator(printItem,ctrlP);
    JMenuItem pageSetupItem = file.add(Actions.PageSetup);
    JMenuItem saveGraphicsItem = file.add(ActionSaveGraphics.SINGLETON);
    setMnemonic(saveGraphicsItem,"SaveGraphics",'G');
    // JMenuItem savePSItem = file.add(Actions.SavePS);
    file.addSeparator();
    file.add(ActionSaveConfiguration.SINGLETON);
    file.addSeparator();
    JMenuItem exitItem = file.add(ActionExit.SINGLETON);
    setMnemonic(exitItem,"Exit",'x');
    setAccelerator(exitItem,altF4);

    _edit = (JMenu) _menuBar.add(new JMenu(menuLocalize("Edit")));
    setMnemonic(_edit,"Edit",'E');

    _select = new JMenu(menuLocalize("Select"));
    _edit.add(_select);
    JMenuItem selectAllItem = _select.add(new CmdSelectAll());
    setAccelerator(selectAllItem,ctrlA);
    JMenuItem selectNextItem = _select.add(new CmdSelectNext(false));
    //tab
    JMenuItem selectPrevItem = _select.add(new CmdSelectNext(true));
    // shift tab
    _select.add(new CmdSelectInvert());

    _edit.add(Actions.Undo);
    _edit.add(Actions.Redo);
    _edit.addSeparator();
    JMenuItem cutItem = _edit.add(ActionCut.SINGLETON);
    setMnemonic(cutItem,"Cut",'X');
    setAccelerator(cutItem,ctrlX);
    JMenuItem copyItem = _edit.add(ActionCopy.SINGLETON);
    setMnemonic(copyItem,"Copy",'C');
    setAccelerator(copyItem,ctrlC);
    JMenuItem pasteItem = _edit.add(ActionPaste.SINGLETON);
    setMnemonic(pasteItem,"Paste",'V');
    setAccelerator(pasteItem,ctrlV);
    _edit.addSeparator();
    // needs-more-work: confusing name change
    JMenuItem deleteItem = _edit.add(ActionDeleteFromDiagram.SINGLETON);
    setMnemonic(deleteItem,"RemoveFromDiagram",'R');
    setAccelerator(deleteItem,delKey);
    JMenuItem removeItem = _edit.add(ActionRemoveFromModel.SINGLETON);
    setMnemonic(removeItem,"DeleteFromModel",'D');
    setAccelerator(removeItem,ctrlR);
    JMenuItem emptyItem = _edit.add(ActionEmptyTrash.SINGLETON);
    _edit.addSeparator();
    _edit.add(ActionSettings.getInstance());

    _view = (ArgoJMenu) _menuBar.add(new ArgoJMenu(menuLocalize("View")));
    // maybe should be Navigate instead of view
    setMnemonic(_view,"View",'V');

//     JMenu nav = (JMenu) view.add(new JMenu("Navigate"));
//     JMenuItem downItem = nav.add(_actionNavDown);
//     downItem.setAccelerator(ctrldown);
//     JMenuItem upItem = nav.add(_actionNavUp);
//     upItem.setAccelerator(ctrlup);
//     JMenuItem backItem = nav.add(_actionNavBack);
//     backItem.setAccelerator(ctrlleft);
//     JMenuItem forwItem = nav.add(_actionNavForw);
//     forwItem.setAccelerator(ctrlright);

    _view.add(Actions.GotoDiagram);
    JMenuItem findItem =  _view.add(Actions.Find);
    setAccelerator(findItem,F3);

    _view.addSeparator();

    JMenu zoom = (JMenu) _view.add(new JMenu(menuLocalize("Zoom")));
    zoom.add(new ActionZoom(10));
    zoom.add(new ActionZoom(25));
    zoom.add(new ActionZoom(50));
    zoom.add(new ActionZoom(75));
    zoom.add(new ActionZoom(100));
    zoom.add(new ActionZoom(150));

    _view.addSeparator();

    JMenu editTabs = (JMenu) _view.add(new JMenu(menuLocalize("Editor Tabs")));

    //view.addSeparator();
    //view.add(_actionAddToFavorites);
    JMenu detailsTabs = (JMenu) _view.add(new JMenu(menuLocalize("Details Tabs")));

    _view.addSeparator();
    _view.add(new CmdAdjustGrid());
    _view.add(new CmdAdjustGuide());
    _view.add(new CmdAdjustPageBreaks());
    _view.addCheckItem(Actions.ShowRapidButtons);
    //_view.addCheckItem(Actions.ShowDiagramList);
    //_view.addCheckItem(Actions.ShowToDoList);
    //_showDetailsMenuItem = _view.addCheckItem(Actions.ShowDetails);

    _view.addSeparator();
    _view.add(org.argouml.language.ui.ActionNotation.getInstance().getMenu());

    appendPluggableMenus(_view, "View");

    //JMenu create = (JMenu) _menuBar.add(new JMenu(menuLocalize("Create")));
    //setMnemonic(create,"Create",'C');
    //create.add(Actions.CreateMultiple);
    //create.addSeparator();

    _createDiagrams = (JMenu) _menuBar.add(new JMenu(menuLocalize("Create Diagram")));
    setMnemonic(_createDiagrams, "Create Diagram",'C');
    _createDiagrams.add(ActionClassDiagram.SINGLETON);
    _createDiagrams.add(ActionUseCaseDiagram.SINGLETON);
    _createDiagrams.add(ActionStateDiagram.SINGLETON);
    _createDiagrams.add(ActionActivityDiagram.SINGLETON);
    _createDiagrams.add(ActionCollaborationDiagram.SINGLETON);
    _createDiagrams.add(ActionDeploymentDiagram.SINGLETON);
    _createDiagrams.add(ActionSequenceDiagram.SINGLETON);
    appendPluggableMenus(_createDiagrams, "Create Diagrams");

    //JMenu createModelElements = (JMenu) create.add(new JMenu("Model Elements"));
    //createModelElements.add(Actions.AddTopLevelPackage);
    //createModelElements.add(_actionClass);
    //createModelElements.add(_actionInterface);
    //createModelElements.add(_actionActor);
    //createModelElements.add(_actionUseCase);
    //createModelElements.add(_actionState);
    //createModelElements.add(_actionPseudostate);
    //createModelElements.add(_actionAttr);
    //createModelElements.add(_actionOper);

    //JMenu createFig = (JMenu) create.add(new JMenu("Shapes"));
    //createFig.add(_actionRectangle);
    //createFig.add(_actionRRectangle);
    //createFig.add(_actionCircle);
    //createFig.add(_actionLine);
    //createFig.add(_actionText);
    //createFig.add(_actionPoly);
    //createFig.add(_actionInk);

    _arrange = (JMenu) _menuBar.add(new JMenu(menuLocalize("Arrange")));
    setMnemonic(_arrange,"Arrange",'A');

    JMenu align = (JMenu) _arrange.add(new JMenu(menuLocalize("Align")));
    JMenu distribute = (JMenu) _arrange.add(new JMenu(menuLocalize("Distribute")));
    JMenu reorder = (JMenu) _arrange.add(new JMenu(menuLocalize("Reorder")));
    JMenu nudge = (JMenu) _arrange.add(new JMenu(menuLocalize("Nudge")));
    JMenu layout = (JMenu) _arrange.add(new JMenu(menuLocalize("Layout")));
    appendPluggableMenus(_arrange, "Arrange");

    Runnable initLater = new
      InitMenusLater(align, distribute, reorder, nudge, layout, editTabs, detailsTabs);
    org.argouml.application.Main.addPostLoadAction(initLater);

    _generate = (JMenu) _menuBar.add(new JMenu(menuLocalize("Generation")));
    setMnemonic(_generate,"Generate",'G');
    _generate.add(ActionGenerateOne.SINGLETON);
    JMenuItem genAllItem = _generate.add(ActionGenerateAll.SINGLETON);
    setAccelerator(genAllItem,F7);
    //generate.add(Actions.GenerateWeb);
    appendPluggableMenus(_generate, "Generate");

    _critique = (ArgoJMenu) _menuBar.add(new ArgoJMenu(menuLocalize("Critique")));
    setMnemonic(_critique,"Critique",'R');
    _critique.addCheckItem(Actions.AutoCritique);
    _critique.addSeparator();
    _critique.add(Actions.OpenDecisions);
    _critique.add(Actions.OpenGoals);
    _critique.add(Actions.OpenCritics);

    // Tools Menu
    _tools = new JMenu(menuLocalize("Tools"));
    _tools.setEnabled(false);
    appendPluggableMenus(_tools, "Tools");
    _menuBar.add(_tools);

    // tools.add(ActionTest.getInstance());

    // Help Menu
    _help = new JMenu(menuLocalize("Help"));
    setMnemonic(_help,"Help",'H');
    appendPluggableMenus(_help, "Help");
    if (_help.getItemCount() > 0) {
        _help.insertSeparator(0);
    }

    _help.add(Actions.SystemInfo);
    _help.addSeparator();
    _help.add(Actions.AboutArgoUML);

    //_menuBar.setHelpMenu(help);
    _menuBar.add(_help);

    ArgoEventPump.addListener(ArgoEventTypes.ANY_MODULE_EVENT, this);
  }


  protected Component createPanels() {
    // Set preferred sizes from config file
    _toDoPane.setPreferredSize(new Dimension(
        Configuration.getInteger(Argo.KEY_SCREEN_SOUTHWEST_WIDTH, DEFAULT_COMPONENTWIDTH),
        Configuration.getInteger(Argo.KEY_SCREEN_SOUTH_HEIGHT, DEFAULT_COMPONENTHEIGHT)
    ));

    if (_southPane != null) {
        _southPane.setPreferredSize(new Dimension(
            0, Configuration.getInteger(Argo.KEY_SCREEN_SOUTH_HEIGHT, DEFAULT_COMPONENTHEIGHT)
        ));
    }

    _navPane.setPreferredSize(new Dimension(
        Configuration.getInteger(Argo.KEY_SCREEN_WEST_WIDTH, DEFAULT_COMPONENTWIDTH),0
    ));

    // The workarea is all the visible space except the menu, toolbar and status bar.
    // Workarea is layed out as a BorderSplitPane where the various components that make
    // up the argo application can be positioned.
    _workarea = new BorderSplitPane();
    _workarea.add(_toDoPane, BorderSplitPane.SOUTHWEST);
    _workarea.add(_navPane, BorderSplitPane.WEST);
    //_workarea.add(_northPane, BorderSplitPane.NORTH);
    
    Iterator it = detailsPanesByCompassPoint.entrySet().iterator();
    while(it.hasNext()) {
        Map.Entry entry = (Map.Entry)it.next();
        _workarea.add((DetailsPane)entry.getValue(), (String)entry.getKey());
    }
    _workarea.add(_editorPane);

    // Toolbar boundry is the area between the menu and the status bar. It contains
    // the workarea at centre and the toolbar position north, south, east or west.
    // @todo - there is work in progress for toolbars - Bob Tarling
    JPanel toolbarBoundry = new JPanel();
    toolbarBoundry.setLayout(new BorderLayout());
    
    toolbarBoundry.add(_workarea, BorderLayout.CENTER);

    return toolbarBoundry;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setProject(Project p) {
    _project = p;
    _navPane.setRoot(_project);
    updateTitle();
    Actions.updateAllEnabled();
    //Designer.TheDesigner.getToDoList().removeAllElements();
    Designer.TheDesigner.setCritiquingRoot(_project);
    // update all panes
    setTarget(_project.getInitialTarget());
    _navPane.forceUpdate();
  }
  public Project getProject() {
	  // only for testing...
	  // if (_project == null) _project = Project.makeEmptyProject();
	  return _project;
  }

  public void updateTitle() {
    if (_project == null) setTitle(null);
    else setTitle(_project.getName());
  }

  public void setTitle(String title) {
    if (title == null || "".equals(title)) setTitle(getAppName());
    else super.setTitle(getAppName() + " - " + title);
  }

  public String getAppName() { return _appName; }
  public void setAppName(String n) { _appName = n; }

  public void setPerspectives(Vector v) {
    _navPane.setPerspectives(v);
  }
  public Vector getPerspectives() {
    return _navPane.getPerspectives();
  }

  public void setCurPerspective(NavPerspective tm) {
    _navPane.setCurPerspective(tm);
  }
  public NavPerspective getCurPerspective() {
    return _navPane.getCurPerspective();
  }

  public void setToDoPerspectives(Vector v) {
    _toDoPane.setPerspectives(v);
  }
  public Vector getToDoPerspectives() {
    return _toDoPane.getPerspectives();
  }
  public void setToDoCurPerspective(TreeModel tm) {
    _toDoPane.setCurPerspective(tm);
  }

    public void select(Object o) {
        _editorPane.select(o);
        setDetailsTarget(o);
    }

    public void setTarget(Object o) {
        _editorPane.setTarget(o);
        
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while(it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane)it.next();
            detailsPane.setTarget(o);
        }
         
	if (o instanceof MNamespace) _project.setCurrentNamespace((MNamespace)o);
	if (o instanceof UMLDiagram) {
	    MNamespace m = ((UMLDiagram)o).getNamespace();
	    if (m != null) _project.setCurrentNamespace(m);
	}
        if (o instanceof ArgoDiagram) {
            setActiveDiagram ((ArgoDiagram) o);
        }
	if (o instanceof MModelElement) {
	    MModelElement eo = (MModelElement)o;
	    if (eo == null) { System.out.println("no path to model"); return; }
	    _project.setCurrentNamespace(eo.getNamespace());
	}
	Actions.updateAllEnabled();
    }

  public Object getTarget() {
    if (_editorPane == null) return null;
    return _editorPane.getTarget();
  }

  /**
   * Set the diagram on which the user is currently working. This is called from
   * When importing sources this should call the name of the folder from
   *which the classes were imported.
   *It should also default the model name as well.
   *{@link #setTarget}.
   */
  public void setActiveDiagram (ArgoDiagram ad) {
    _activeDiagram = ad;
    //System.out.println ("Active diagram set to " + ad.getName());
  }

  /**
   * Return the diagram, the user is currently working on.
   */
  public ArgoDiagram getActiveDiagram() {
    return _activeDiagram;
  }

    /**
     * Select the tab page containing the todo item
     *
     * @todo should introduce an instance variable to go straight to the correct tab instead of trying all
     */
    public void setToDoItem(Object o) {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while(it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane)it.next();
            if (detailsPane.setToDoItem(o)) return;
        }
    }

    public void setDetailsTarget(Object o) {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while(it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane)it.next();
            detailsPane.setTarget(o);
        }
        Actions.updateAllEnabled();
    }

    public Object getDetailsTarget() {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        if (it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane)it.next();
            return detailsPane.getTarget();
        }
        return null; // TODO Bob Tarling - Should probably throw exception here
    }

  public StatusBar getStatusBar() { return _statusBar; }

  public JMenuBar getJMenuBar() { return _menuBar; }

  public ToDoPane getToDoPane() { return _toDoPane; }
  public NavigatorPane getNavPane() { return _navPane; }
  public MultiEditorPane getEditorPane() { return _editorPane; }
  //public DetailsPane getDetailsPane() { return _southPane; }

    /**
     * Find the tabpage with the given label and make it the front tab
     * @param The tabpage label
     * @return false if no tab was found of given name
     */
    public void selectTabNamed(String tabName) {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while(it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane)it.next();
            if (detailsPane.selectTabNamed(tabName)) return;
        }
        throw new IllegalArgumentException("No such tab named " + tabName);
    }

    /**
     * Find the tabpage with the given label
     * @param The tabpage label
     * @return the tabpage
     */
    public JPanel getNamedTab(String tabName) {
        JPanel panel;
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while(it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane)it.next();
            panel = detailsPane.getNamedTab(tabName);
            if (panel != null) return panel;
        }
        return null;
        //throw new IllegalArgumentException("No such tab named " + tabName);
    }

    /**
     * Find the tabpage with the given label
     * @param The tabpage label
     * @return the tabpage
     */
    public TabProps getTabProps() {
        Iterator it = detailsPanesByCompassPoint.values().iterator();
        while(it.hasNext()) {
            DetailsPane detailsPane = (DetailsPane)it.next();
            TabProps tabProps = detailsPane.getTabProps();
            if (tabProps != null) {
                return tabProps;
            }
        }
        return null;
        //throw new IllegalStateException("No such tab named " + tabName);
    }
    
  public void jumpToDiagramShowing(VectorSet dms) {
    if (dms.size() == 0) return;
    Object first = dms.elementAt(0);
    if (first instanceof Diagram && dms.size() > 1) {
      setTarget(first);
      select(dms.elementAt(1));
      return;
    }
    if (first instanceof Diagram && dms.size() == 1) {
      setTarget(first);
      select(null);
      return;
    }
    Vector diagrams = getProject().getDiagrams();
    Object target = _editorPane.getTarget();
    if ((target instanceof Diagram) && ((Diagram)target).countContained(dms) == dms.size()) {
      select(first);
      return;
    }

    Diagram bestDiagram = null;
    int bestNumContained = 0;
    for (int i = 0; i < diagrams.size(); i++) {
      Diagram d = (Diagram) diagrams.elementAt(i);
      int nc = d.countContained(dms);
      if (nc > bestNumContained) {
	      bestNumContained = nc;
	      bestDiagram = d;
      }
      if (nc == dms.size()) break;
    }
    if (bestDiagram != null) {
      setTarget(bestDiagram);
      select(first);
    }
  }

  ////////////////////////////////////////////////////////////////
  // window operations

  public void setVisible(boolean b) {
    super.setVisible(b);
    if (b) org.tigris.gef.base.Globals.setStatusBar(this);
  }

  ////////////////////////////////////////////////////////////////
  // IStatusBar
  public void showStatus(String s) { _statusBar.showStatus(s); }

  /**    Called by a user interface element when a request to
   *    navigate to a model element has been received.
   */
  public void navigateTo(Object element) {
    _history.navigateTo(element);
    setTarget(element);
  }

  /**   Called by a user interface element when a request to
   *   open a model element in a new window has been recieved.
   */
  public void open(Object element) {
  }

  public boolean navigateBack(boolean attempt) {
    boolean navigated = false;
    if(attempt) {
      Object target = _history.navigateBack(attempt);
      if(target != null) {
        navigated = true;
        setTarget(target);
      }
    }
    return navigated;
  }

  public boolean navigateForward(boolean attempt) {
    boolean navigated = false;
    if(attempt) {
      Object target = _history.navigateForward(attempt);
      if(target != null) {
        navigated = true;
        setTarget(target);
      }
    }
    return navigated;
  }

  public boolean isNavigateBackEnabled() {
    return _history.isNavigateBackEnabled();
  }

  public boolean isNavigateForwardEnabled() {
    return _history.isNavigateForwardEnabled();
  }

  public void moduleLoaded(ArgoModuleEvent event) {
  	if (event.getSource() instanceof PluggableMenu) {
	    PluggableMenu module = (PluggableMenu)event.getSource();
            Object[] context = new Object[] { _tools, "Tools" };
	    if (module.inContext(context)) {
		    _tools.add(module.getMenuItem(context));
	      _tools.setEnabled(true);
	    }
            context = new Object[] { _import, "File:Import" };
	    if (module.inContext(context)) {
               _import.add(module.getMenuItem(context));
	    }
            context = new Object[] { _generate, "Generate" };
	    if (module.inContext(context)) {
                _generate.add(module.getMenuItem(context));
	    }
            context = new Object[] { _edit, "Edit" };
	    if (module.inContext(context)) {
                _edit.add(module.getMenuItem(context));
	    }
            context = new Object[] { _view, "View" };
	    if (module.inContext(context)) {
      	        _view.add(module.getMenuItem(context));
	    }
            context = new Object[] { _createDiagrams, "Create Diagrams" };
	    if (module.inContext(context)) {
       	        _createDiagrams.add(module.getMenuItem(context));
	    }
            context = new Object[] { _arrange, "Arrange" };
	    if (module.inContext(context)) {
       	        _arrange.add(module.getMenuItem(context));
	    }
            context = new Object[] { _help, "Help" };
	    if (module.inContext(context)) {
        if (_help.getItemCount() == 1) {
		      _help.insertSeparator(0);
        }
		    _help.insert(module.getMenuItem(context), 0);
	    }
    }
	}
	/**
	  * Makes the navigator pane visible or invisible
	  * @param     visible  true to make the navigator pane visible.
	  */
	public void setNavigatorPaneVisible(boolean visible) {
        /*
		if (_navPane.isVisible() != visible) {
			if (!visible) {
				_topSplit.setLastDividerLocation(_topSplit.getDividerLocation());
				_topSplit.setDividerSize(0);
			}
			else {
				_topSplit.setDividerSize(2);
			}

			_navPane.setVisible(visible);
			if (visible) {
				_topSplit.setDividerLocation(_topSplit.getLastDividerLocation());
			}
		}
        */
	}

	/**
	  * Makes the todo pane visible or invisible
	  * @param     visible  true to make the todo pane visible.
	  */
	public void setToDoPaneVisible(boolean visible) {
	  //setLowerPaneVisible(_toDoPane, visible);
	}

	/**
	  * Makes the details pane visible or invisible
	  * @param     visible  true to make the details pane visible.
	  */
	public void setDetailsPaneVisible(boolean visible) {
        /*
		_showDetailsMenuItem.setSelected(visible);
		setLowerPaneVisible(_detailsPane, visible);
        */
	}

	/**
	  * Makes one of the lower panes (either the details pane or the todo list pane) visible or invisible.
	  * If both panes become invisible then the entire lower pane is removed from view.
	  * @param     pane     The <code>JPanel</code> object to hide/show
	  * @param     visible  true to make the <code>pane</code> visible.
	  */
	private void setLowerPaneVisible(JPanel pane, boolean visible) {
        /*
		// Only process if the visible state is changing from its existing state.
		if (pane.isVisible() == visible) return;

		// Determine which pane to change and which is the other.
		JPanel otherPane = null;
		if (pane.equals(_toDoPane)) otherPane = _detailsPane;
		else if (pane.equals(_detailsPane)) otherPane = _toDoPane;
		else return;

		if (!visible && otherPane.isVisible()) {
			// If the pane is being made invisible with the other pane left behind
			// then remember the lower divider location and hide the divider.
			_botSplit.setLastDividerLocation(_botSplit.getDividerLocation());
			_botSplit.setDividerSize(0);
		}

		// Show/Hide the required pane.
		pane.setVisible(visible);

		if (visible && otherPane.isVisible()) {
			// If both panes have now become visible, restore the lower divider location and make it visible.
			_botSplit.setDividerSize(2);
			_botSplit.setDividerLocation(_botSplit.getLastDividerLocation());
		}
		else if (!otherPane.isVisible() && !visible) {
			// If both of the lower panes are now invisible make the bottom pane invisible and make
			// the top pane take the entire space available. The position of the main divider is stored
			// before it is removed.
			_mainSplit.setLastDividerLocation(_mainSplit.getDividerLocation());
			_botSplit.setVisible(false);
			_mainSplit.setDividerLocation(_mainSplit.getHeight());
			_mainSplit.setDividerSize(0);
		}
		else if ((otherPane.isVisible() || visible) && !_botSplit.isVisible()) {
			// If either of the lower panes is visible but the bottom pane that encloses them is invisible
			// then show the bottom pane and restore the main divider.
			_botSplit.setVisible(true);
			_mainSplit.setDividerSize(2);
			_mainSplit.setDividerLocation(_mainSplit.getLastDividerLocation());
		}
        */
	}

	/**
	  * test if the navigator pane is visible
	  * @return     true if visible
	  */
	public boolean isNavigatorPaneVisible() {
	  return _navPane.isVisible();
	}

	/**
	  * test if the details pane is visible
	  * @return     true if visible
	  */
	public boolean isDetailsPaneVisible() {
	  return _southPane.isVisible();
	}

	/**
	  * test if the todo pane is visible
	  * @return     true if visible
	  */
	public boolean isToDoPaneVisible() {
	  return _toDoPane.isVisible();
  }


    private static final int ThemeNormal = 1;
    private static final int ThemeBig = 2;
    private static final int ThemeHuge = 3;

    /**
     * Detecting the theme from the command line.
     */
    public static int getThemeFromArg(String arg) {
	if (arg.equalsIgnoreCase("-big")) {
	    return ThemeBig;
	} else if (arg.equalsIgnoreCase("-huge")) {
	    return ThemeHuge;
	}
	return 0;
    }
    public static void printThemeArgs() {
	System.err.println("  -big            use big fonts");
	System.err.println("  -huge           use huge fonts");
    }

    private int currentTheme = -1;
    public int getCurrentTheme() { return currentTheme; }
    public void setCurrentTheme(int t) {
	if (t == 0) {
	    t = Configuration.getInteger(Argo.KEY_SCREEN_THEME, ThemeNormal);
	}

	if (currentTheme == t)
	    return;
	currentTheme = t;
	switch (t) {
	case ThemeNormal:
	default:
	    currentTheme = ThemeNormal;
	    MetalLookAndFeel.setCurrentTheme(new org.argouml.ui.JasonsTheme());
	    break;

	case ThemeBig:
	    MetalLookAndFeel.setCurrentTheme(new org.argouml.ui.JasonsBigTheme());
	    break;

	case ThemeHuge:
	    MetalLookAndFeel.setCurrentTheme(new org.argouml.ui.JasonsHugeTheme());
	    break;
	}
	try {
	    UIManager.setLookAndFeel(new MetalLookAndFeel());
	}
	catch (UnsupportedLookAndFeelException e) {
	}
	SwingUtilities.updateComponentTreeUI(this);
	pack();
	Configuration.setInteger(Argo.KEY_SCREEN_THEME, currentTheme);
    }

    
    public void setCurrentTheme(String arg) {
	if ("normal".equals(arg))
	    setCurrentTheme(ThemeNormal);
	else if ("big".equals(arg))
	    setCurrentTheme(ThemeBig);
	else if ("huge".equals(arg))
	    setCurrentTheme(ThemeHuge);
	else {
	    System.out.println("ProjectBrowser.setCurrentTheme: "
			       + "Incorrect theme: " + arg);
	}
    }
    public boolean isCurrentTheme(String arg) {
	if ("normal".equals(arg))
	    return getCurrentTheme() == ThemeNormal;
	else if ("big".equals(arg))
	    return getCurrentTheme() == ThemeBig;
	else if ("huge".equals(arg))
	    return getCurrentTheme() == ThemeHuge;
	else {
	    System.out.println("ProjectBrowser.isCurrentTheme: "
			       + "Incorrect theme: " + arg);
	    return false;
	}
    }

    public void saveScreenConfiguration() {
        Configuration.setInteger(Argo.KEY_SCREEN_WEST_WIDTH, _navPane.getWidth());
        Configuration.setInteger(Argo.KEY_SCREEN_SOUTHWEST_WIDTH, _toDoPane.getWidth());
        Configuration.setInteger(Argo.KEY_SCREEN_SOUTH_HEIGHT, _southPane.getHeight());
        Configuration.setInteger(Argo.KEY_SCREEN_WIDTH, getWidth());
        Configuration.setInteger(Argo.KEY_SCREEN_HEIGHT, getHeight());
        Configuration.setInteger(Argo.KEY_SCREEN_LEFT_X, getX());
        Configuration.setInteger(Argo.KEY_SCREEN_TOP_Y, getY());
    }

  public void moduleUnloaded(ArgoModuleEvent event) {
      // needs-more-work:  Disable menu
  }

  public void moduleEnabled(ArgoModuleEvent event) {
      // needs-more-work:  Enable menu
  }

  public void moduleDisabled(ArgoModuleEvent event) {
        // needs-more-work:  Disable menu
  }
  
    /**
     * Build a new details pane for the given compass point
     * @param compassPoint the position for which to build the pane
     * @param orientation the required orientation of the pane.
     * @return the details pane or null if none is required for the given
     *         compass point.
     */
    private DetailsPane makeDetailsPane(StatusBar sb, String compassPoint, Orientation orientation) {
        DetailsPane detailsPane = new DetailsPane(sb, compassPoint, orientation);
        if (detailsPane.getTabCount() == 0) return null;
        return detailsPane;
    }


  class WindowCloser extends WindowAdapter {
    public WindowCloser() { }
    public void windowClosing(WindowEvent e) {
      
      ActionExit.SINGLETON.actionPerformed(null);
    }
  } /* end class WindowCloser */


} /* end class ProjectBrowser */


class InitMenusLater implements Runnable {
  JMenu align, distribute, reorder, nudge, layout;
  JMenu editTabs, detailsTabs;

  public InitMenusLater(JMenu a, JMenu d, JMenu r, JMenu n, JMenu l, JMenu et, JMenu dt) {
    align = a;
    distribute = d;
    reorder = r;
    nudge = n;
    layout = l;
    editTabs = et;
    detailsTabs = dt;
  }

  public void run() {
    KeyStroke F1 = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
    KeyStroke F2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
    KeyStroke F3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
    KeyStroke F4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
    KeyStroke F5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
    KeyStroke F6 = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0);
    KeyStroke F7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
    KeyStroke F8 = KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0);
    KeyStroke F9 = KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0);
    KeyStroke F10 = KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0);

    KeyStroke alt1 = KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.ALT_MASK);
    KeyStroke alt2 = KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.ALT_MASK);
    KeyStroke alt3 = KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.ALT_MASK);
    KeyStroke alt4 = KeyStroke.getKeyStroke(KeyEvent.VK_4, KeyEvent.ALT_MASK);
    KeyStroke alt5 = KeyStroke.getKeyStroke(KeyEvent.VK_5, KeyEvent.ALT_MASK);
    KeyStroke alt6 = KeyStroke.getKeyStroke(KeyEvent.VK_6, KeyEvent.ALT_MASK);
    KeyStroke alt7 = KeyStroke.getKeyStroke(KeyEvent.VK_7, KeyEvent.ALT_MASK);
    KeyStroke alt8 = KeyStroke.getKeyStroke(KeyEvent.VK_8, KeyEvent.ALT_MASK);
    KeyStroke alt9 = KeyStroke.getKeyStroke(KeyEvent.VK_9, KeyEvent.ALT_MASK);
    KeyStroke alt0 = KeyStroke.getKeyStroke(KeyEvent.VK_0, KeyEvent.ALT_MASK);

    KeyStroke altshift1 =
      KeyStroke.getKeyStroke(KeyEvent.VK_1,
			     KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK);
    KeyStroke altshift2 =
      KeyStroke.getKeyStroke(KeyEvent.VK_2,
			     KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK);
    KeyStroke altshift3 =
      KeyStroke.getKeyStroke(KeyEvent.VK_3,
			     KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK);

    align.add(new CmdAlign(CmdAlign.ALIGN_TOPS));
    align.add(new CmdAlign(CmdAlign.ALIGN_BOTTOMS));
    align.add(new CmdAlign(CmdAlign.ALIGN_LEFTS));
    align.add(new CmdAlign(CmdAlign.ALIGN_RIGHTS));
    align.add(new CmdAlign(CmdAlign.ALIGN_H_CENTERS));
    align.add(new CmdAlign(CmdAlign.ALIGN_V_CENTERS));
    align.add(new CmdAlign(CmdAlign.ALIGN_TO_GRID));

    distribute.add(new CmdDistribute(CmdDistribute.H_SPACING));
    distribute.add(new CmdDistribute(CmdDistribute.H_CENTERS));
    distribute.add(new CmdDistribute(CmdDistribute.V_SPACING));
    distribute.add(new CmdDistribute(CmdDistribute.V_CENTERS));

    reorder.add(new CmdReorder(CmdReorder.BRING_FORWARD));
    reorder.add(new CmdReorder(CmdReorder.SEND_BACKWARD));
    reorder.add(new CmdReorder(CmdReorder.BRING_TO_FRONT));
    reorder.add(new CmdReorder(CmdReorder.SEND_TO_BACK));
    
   

    nudge.add(new CmdNudge(CmdNudge.LEFT));
    nudge.add(new CmdNudge(CmdNudge.RIGHT));
    nudge.add(new CmdNudge(CmdNudge.UP));
    nudge.add(new CmdNudge(CmdNudge.DOWN));

    JMenuItem autoLayout = layout.add(new ActionLayout("Automatic"));
    JMenuItem incrLayout = layout.add(new ActionLayout("Incremental"));
    /** incremental layout is currently not implemented */
    incrLayout.setEnabled(false);

    JMenuItem nextEditItem = editTabs.add(Actions.NextEditTab);
    nextEditItem.setAccelerator(F6);
    editTabs.addSeparator();

    JMenuItem tabe1Item = editTabs.add(new ActionGoToEdit("As Diagram"));
    tabe1Item.setAccelerator(altshift1);
    JMenuItem tabe2Item = editTabs.add(new ActionGoToEdit("As Table"));
    tabe2Item.setAccelerator(altshift2);
    JMenuItem tabe3Item = editTabs.add(new ActionGoToEdit("As Metrics"));
    tabe3Item.setAccelerator(altshift3);

//    JMenuItem nextDetailsItem = detailsTabs.add(Actions.NextDetailsTab);
//    nextDetailsItem.setAccelerator(F5);
//    detailsTabs.addSeparator();

    JMenuItem tab1Item = detailsTabs.add(new ActionGoToDetails("ToDo Item"));
    tab1Item.setAccelerator(alt1);
    JMenuItem tab2Item = detailsTabs.add(new ActionGoToDetails("Properties"));
    tab2Item.setAccelerator(alt2);
    JMenuItem tab3Item = detailsTabs.add(new ActionGoToDetails("Javadocs"));
    tab3Item.setAccelerator(alt3);
    JMenuItem tab4Item = detailsTabs.add(new ActionGoToDetails("Source"));
    tab4Item.setAccelerator(alt4);
    JMenuItem tab5Item = detailsTabs.add(new ActionGoToDetails("Constraints"));
    tab5Item.setAccelerator(alt5);
    JMenuItem tab6Item = detailsTabs.add(new ActionGoToDetails("Tagged Values"));
    tab6Item.setAccelerator(alt6);
    JMenuItem tab7Item = detailsTabs.add(new ActionGoToDetails("Checklist"));
    tab7Item.setAccelerator(alt7);
    JMenuItem tab8Item = detailsTabs.add(new ActionGoToDetails("History"));
    tab8Item.setAccelerator(alt8);
    //JMenuItem tab9Item = detailsTabs.add(new ActionGoToDetails(""));
    //tab9Item.setAccelerator(alt9);
    //JMenuItem tab0Item = detailsTabs.add(new ActionGoToDetails(""));
    //tab0Item.setAccelerator(alt0);
  }
} /* end class InitMenusLater */

