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

import uci.argo.kernel.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
// import javax.swing.text.*;
// import javax.swing.border.*;

import uci.util.*;
import uci.ui.*;
import uci.gef.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;
import uci.uml.ui.nav.NavPerspective;
import uci.uml.visual.UMLDiagram;


/** The main window of the Argo/UML application. */

public class ProjectBrowser extends JFrame
implements IStatusBar {
  ////////////////////////////////////////////////////////////////
  // constants

//   public static int WIDTH = 800;
//   public static int HEIGHT = 600;
//   public static int INITIAL_WIDTH = 400; // for showing progress bar
//   public static int INITIAL_HEIGHT = 200;

  ////////////////////////////////////////////////////////////////
  // class variables

  public static ProjectBrowser TheInstance;
  
  // Actions
  // file menu
  //-protected static Action _actionNew =  Actions.New;
  //protected static Action _actionOpen = Actions.Open;
  //protected static Action _actionOpenXMI = Actions.OpenXMI;
  //-protected static Action _actionOpenProject = Actions.OpenProject;
  //protected static Action _actionSave = Actions.Save;
  //protected static Action _actionSaveAs = Actions.SaveAs;
  //protected static Action _actionSaveAsXMI = Actions.SaveAsXMI;
  //-protected static Action _actionSaveProject = Actions.SaveProject;
  //-protected static Action _actionSaveProjectAs = Actions.SaveProjectAs;
  // -----
  //protected static Action _actionAddToProj = Actions.AddToProj;
  // -----
  //-protected static Action _actionPrint = Actions.Print;
  // -----
  //-protected static Action _actionExit = Actions.Exit;

  // edit menu
  //-protected static Action _actionUndo = Actions.Undo;
  //-protected static Action _actionRedo = Actions.Redo;
  //-protected static Action _actionCut = Actions.Cut;
  //-protected static Action _actionCopy = Actions.Copy;
  //-protected static Action _actionPaste = Actions.Paste;
  //-protected static Action _actionDelete = Actions.DeleteFromDiagram;
  //-protected static Action _actionRemove = Actions.RemoveFromModel;
  //-protected static Action _actionEmpty = Actions.EmptyTrash;

  // view menu
//   protected static Action _actionNavUp = Actions.NavUp;
//   protected static Action _actionNavDown = Actions.NavDown;
  //-protected static Action _actionNavBack = Actions.NavBack;
  //-protected static Action _actionNavForw = Actions.NavForw;
  //-protected static Action _actionFind = Actions.Find;
  //-protected static Action _actionGotoDiagram = Actions.GotoDiagram;
  //-protected static Action _actionNextEditTab = Actions.NextEditTab;
  //protected static Action _actionAddToFavs = Actions.AddToFavs;
  //-protected static Action _actionNextDetailsTab = Actions.NextDetailsTab;

  // create menu
  protected static Action _actionCreateMultiple = Actions.CreateMultiple;
  // ----- diagrams
  protected static Action _actionClassDiagram = Actions.ClassDiagram;
  protected static Action _actionUseCaseDiagram = Actions.UseCaseDiagram;
  protected static Action _actionStateDiagram = Actions.StateDiagram;
  protected static Action _actionActivityDiagram = Actions.ActivityDiagram;
  protected static Action _actionCollaborationDiagram = Actions.CollaborationDiagram;

  // ----- model elements
  //protected static Action _actionModel = Actions.Model;
  protected static Action _actionAddTopLevelPackage = Actions.AddTopLevelPackage;
  //protected static Action _actionClass = Actions.Class;
  //protected static Action _actionInterface = Actions.Interface;
  //protected static Action _actionActor = Actions.Actor;
  //protected static Action _actionUseCase = Actions.UseCase;
  //protected static Action _actionState = Actions.State;
  //protected static Action _actionPseudostate = Actions.Pseudostate;
  //protected static Action _actionAttr = Actions.Attr;
  //protected static Action _actionOper = Actions.Oper;
  // -----  shapes
//   protected static Action _actionRectangle = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigRect.class, "Rectangle");
//   protected static Action _actionRRectangle = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigRRect.class, "RRect");
//   protected static Action _actionCircle = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigCircle.class, "Circle");
//   protected static Action _actionLine = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigLine.class, "Line");
//   protected static Action _actionText = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigText.class, "Text");
//   protected static Action _actionPoly = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigPoly.class, "Polygon");
//   protected static Action _actionInk = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigInk.class, "Ink");

  // actions menu
  //-protected static Action _actionGenerateOne = Actions.GenerateOne;
  //-protected static Action _actionGenerateAll = Actions.GenerateAll;
  //protected static Action _actionGenerateWeb = Actions.GenerateWeb;

  // critique menu
  //-protected static Action _actionAutoCritique = Actions.AutoCritique;
  //-protected static Action _actionOpenDecisions = Actions.OpenDecisions;
  //-protected static Action _actionOpenGoals = Actions.OpenGoals;
  //-protected static Action _actionOpenCritics = Actions.OpenCritics;


  // Help menu
  //-protected static Action _actionAboutArgoUML = Actions.AboutArgoUML;

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected String _appName = "ProjectBrowser";
  protected Project _project = null;

  protected NavigatorPane _navPane;
  public ToDoPane _toDoPane;
  protected MultiEditorPane _multiPane;
  protected DetailsPane _detailsPane;
  protected JMenuBar _menuBar = new JMenuBar();
  protected StatusBar _statusBar = new StatusBar();
  //protected JToolBar _toolBar = new JToolBar();


  public Font defaultFont = new Font("Dialog", Font.PLAIN, 10);
  //  public static JFrame _Frame;

  protected JSplitPane _mainSplit, _topSplit, _botSplit;


  ////////////////////////////////////////////////////////////////
  // constructors

  public ProjectBrowser(String appName, StatusBar sb) {
    super(appName);
    sb.showStatus("Making Project Browser: Navigator Pane");
    sb.incProgress(5);
    _navPane = new NavigatorPane();
    sb.showStatus("Making Project Browser: To Do Pane");
    sb.incProgress(5);
    _toDoPane = new ToDoPane();
    _multiPane = new MultiEditorPane(sb);
    _detailsPane = new DetailsPane(sb);
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
    _toDoPane.setRoot(uci.argo.kernel.Designer.TheDesigner.getToDoList());

    // allows me to ask "Do you want to save first?"
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowCloser());
  }


//   void loadImages() {
//     String s = "A blue bullet icon - to draw attention to a menu item";
//     blueDot = loadImageIcon("images/dot.gif", s);
//     s = "A red bullet icon - to draw attention to a menu item";
//     redDot = loadImageIcon("images/redDot.gif", s);
//   }


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


//     KeyStroke ctrlup =
//       KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_MASK);
//     KeyStroke ctrldown =
//       KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_MASK);
//     KeyStroke ctrlleft =
//       KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.CTRL_MASK);
//     KeyStroke ctrlright =
//       KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.CTRL_MASK);

    KeyStroke delKey =
      KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);

    JMenuItem mi;
    // File Menu
    JMenu file = (JMenu) _menuBar.add(new JMenu("File"));
    file.setMnemonic('F');
    JMenuItem newItem = file.add(Actions.New);
    newItem.setMnemonic('N');
    newItem.setAccelerator(ctrlN);
    //JMenuItem openItem = file.add(_actionOpen);
    //JMenuItem openXMIItem = file.add(_actionOpenXMI);
    JMenuItem openProjectItem = file.add(Actions.OpenProject);
    openProjectItem.setMnemonic('O');
    openProjectItem.setAccelerator(ctrlO);
    //JMenuItem saveItem = file.add(_actionSave);
    //file.add(_actionSaveAs);
    //file.add(_actionSaveAsXMI);
    JMenuItem saveProjectItem = file.add(Actions.SaveProject);
    saveProjectItem.setMnemonic('S');
    saveProjectItem.setAccelerator(ctrlS);
    JMenuItem saveProjectAsItem = file.add(Actions.SaveProjectAs);
    saveProjectAsItem.setMnemonic('A');
    file.addSeparator();
    JMenuItem printItem = file.add(Actions.Print);
    printItem.setMnemonic('P');
    printItem.setAccelerator(ctrlP);
    JMenuItem saveGIFItem = file.add(Actions.SaveGIF);
    saveGIFItem.setMnemonic('G');
    //file.addSeparator();
    //file.add(_actionProjectInfo);
    file.addSeparator();
    JMenuItem exitItem = file.add(Actions.Exit);
    exitItem.setMnemonic('x');
    exitItem.setAccelerator(altF4);

    JMenu edit = (JMenu) _menuBar.add(new JMenu("Edit"));
    edit.setMnemonic('E');

    JMenu select = new JMenu("Select");
    edit.add(select);
    JMenuItem selectAllItem = select.add(new CmdSelectAll());
    selectAllItem.setAccelerator(ctrlA);
    JMenuItem selectNextItem = select.add(new CmdSelectNext(false));
    //tab
    JMenuItem selectPrevItem = select.add(new CmdSelectNext(true));
    // shift tab
    select.add(new CmdSelectInvert());

    edit.add(Actions.Undo);
    edit.add(Actions.Redo);
    edit.addSeparator();
    JMenuItem cutItem = edit.add(Actions.Cut);
    cutItem.setMnemonic('X');
    cutItem.setAccelerator(ctrlX);
    JMenuItem copyItem = edit.add(Actions.Copy);
    copyItem.setMnemonic('C');
    copyItem.setAccelerator(ctrlC);
    JMenuItem pasteItem = edit.add(Actions.Paste);
    pasteItem.setMnemonic('V');
    pasteItem.setAccelerator(ctrlV);
    edit.addSeparator();
    // needs-more-work: confusing name change
    JMenuItem deleteItem = edit.add(Actions.DeleteFromDiagram);
    deleteItem.setMnemonic('R');
    deleteItem.setAccelerator(ctrlR);
    JMenuItem removeItem = edit.add(Actions.RemoveFromModel);
    removeItem.setMnemonic('D');
    removeItem.setAccelerator(delKey);
    JMenuItem emptyItem = edit.add(Actions.EmptyTrash);

    Menu view = (Menu) _menuBar.add(new Menu("View"));
    // maybe should be Navigate instead of view
    view.setMnemonic('V');

//     JMenu nav = (JMenu) view.add(new JMenu("Navigate"));
//     JMenuItem downItem = nav.add(_actionNavDown);
//     downItem.setAccelerator(ctrldown);
//     JMenuItem upItem = nav.add(_actionNavUp);
//     upItem.setAccelerator(ctrlup);
//     JMenuItem backItem = nav.add(_actionNavBack);
//     backItem.setAccelerator(ctrlleft);
//     JMenuItem forwItem = nav.add(_actionNavForw);
//     forwItem.setAccelerator(ctrlright);

    view.add(Actions.GotoDiagram);
    JMenuItem findItem =  view.add(Actions.Find);
    findItem.setAccelerator(F3);
    view.addSeparator();

    JMenu editTabs = (JMenu) view.add(new JMenu("Editor Tabs"));

    //view.addSeparator();
    //view.add(_actionAddToFavorites);
    JMenu detailsTabs = (JMenu) view.add(new JMenu("Details Tabs"));

    view.addSeparator();
    view.add(new CmdAdjustGrid());
    view.add(new CmdAdjustGuide());
    view.add(new CmdAdjustPageBreaks());
    view.addCheckItem(Actions.ShowRapidButtons);


    JMenu create = (JMenu) _menuBar.add(new JMenu("Create"));
    create.setMnemonic('C');
    create.add(Actions.CreateMultiple);
    create.addSeparator();

    JMenu createDiagrams = (JMenu) create.add(new JMenu("Diagrams"));
    createDiagrams.add(Actions.ClassDiagram);
    createDiagrams.add(Actions.UseCaseDiagram);
    createDiagrams.add(Actions.StateDiagram);
    createDiagrams.add(Actions.ActivityDiagram);
    createDiagrams.add(Actions.CollaborationDiagram);

    JMenu createModelElements = (JMenu) create.add(new JMenu("Model Elements"));
    createModelElements.add(Actions.AddTopLevelPackage);
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

    JMenu arrange = (JMenu) _menuBar.add(new JMenu("Arrange"));
    arrange.setMnemonic('A');

    JMenu align = (JMenu) arrange.add(new JMenu("Align"));
    JMenu distribute = (JMenu) arrange.add(new JMenu("Distribute"));
    JMenu reorder = (JMenu) arrange.add(new JMenu("Reorder"));
    JMenu nudge = (JMenu) arrange.add(new JMenu("Nudge"));

    Runnable initLater = new
      InitMenusLater(align, distribute, reorder, nudge, editTabs, detailsTabs);
    uci.uml.Main.addPostLoadAction(initLater);

    JMenu generate = (JMenu) _menuBar.add(new JMenu("Generation"));
    generate.setMnemonic('G');
    generate.add(Actions.GenerateOne);
    JMenuItem genAllItem = generate.add(Actions.GenerateAll);
    genAllItem.setAccelerator(F7);
    //generate.add(Actions.GenerateWeb);

    Menu critique = (Menu) _menuBar.add(new Menu("Critique"));
    critique.setMnemonic('R');
    critique.addCheckItem(Actions.AutoCritique);
    critique.addSeparator();
    critique.add(Actions.OpenDecisions);
    critique.add(Actions.OpenGoals);
    critique.add(Actions.OpenCritics);

    // Help Menu
    JMenu help = new JMenu("Help");
    help.setMnemonic('H');
    help.add(Actions.AboutArgoUML);
    //_menuBar.setHelpMenu(help);
    _menuBar.add(help);
  }


  protected Component createPanels() {
    _topSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, _navPane, _multiPane);
    _botSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			       _toDoPane, _detailsPane);
    _mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, _topSplit, _botSplit);
    _topSplit.setDividerSize(2);
    _topSplit.setDividerLocation(270);
    _botSplit.setDividerSize(2);
    _mainSplit.setDividerSize(2);
    //_botSplit.setOneTouchExpandable(true);
    return _mainSplit;
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
  public Project getProject() { return _project; }

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
    _multiPane.select(o);
    _detailsPane.setTarget(o);
    Actions.updateAllEnabled();
  }

  public void setTarget(Object o) {
    _multiPane.setTarget(o);
    _detailsPane.setTarget(o);
    if (o instanceof Namespace) _project.setCurrentNamespace((Namespace)o);
    if (o instanceof UMLDiagram) {
      Namespace m = ((UMLDiagram)o).getNamespace();
      if (m != null) _project.setCurrentNamespace(m);
    }
    if (o instanceof ModelElement) {
      ElementOwnership eo = ((ModelElement)o).getElementOwnership();
      if (eo == null) { System.out.println("no path to model"); return; }
      while (!(eo.getNamespace() instanceof Model)) {
	if (eo.getNamespace() == null) break;
	eo = eo.getNamespace().getElementOwnership();
      }
      _project.setCurrentNamespace(eo.getNamespace());
    }
    Actions.updateAllEnabled();
  }

  public Object getTarget() {
    if (_multiPane == null) return null;
    return _multiPane.getTarget();
  }

  public void setToDoItem(Object o) {
    _detailsPane.setToDoItem(o);
  }

  public void setDetailsTarget(Object o) {
    _detailsPane.setTarget(o);
    Actions.updateAllEnabled();
  }

  public Object getDetailsTarget() {
    return _detailsPane.getTarget();
  }

  public StatusBar getStatusBar() { return _statusBar; }


  public ToDoPane getToDoPane() { return _toDoPane; }
  public NavigatorPane getNavPane() { return _navPane; }
  public MultiEditorPane getEditorPane() { return _multiPane; }
  public DetailsPane getDetailsPane() { return _detailsPane; }

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
    Object target = _multiPane.getTarget();
    if ((target instanceof Diagram) &&
	((Diagram)target).countContained(dms) == dms.size()) {
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
    if (b) uci.gef.Globals.setStatusBar(this);
  }

  ////////////////////////////////////////////////////////////////
  // IStatusBar
  public void showStatus(String s) { _statusBar.showStatus(s); }


} /* end class ProjectBrowser */




class WindowCloser extends WindowAdapter {
  public WindowCloser() { }
  public void windowClosing(WindowEvent e) {
    Actions.Exit.actionPerformed(null);
  }
} /* end class WindowCloser */

class InitMenusLater implements Runnable {
  JMenu align, distribute, reorder, nudge;
  JMenu editTabs, detailsTabs;

  public InitMenusLater(JMenu a, JMenu d, JMenu r, JMenu n,
			JMenu et, JMenu dt) {
    align = a;
    distribute = d;
    reorder = r;
    nudge = n;
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

    reorder.add(new CmdReorder(CmdReorder.SEND_TO_BACK));
    reorder.add(new CmdReorder(CmdReorder.BRING_TO_FRONT));
    reorder.add(new CmdReorder(CmdReorder.SEND_BACKWARD));
    reorder.add(new CmdReorder(CmdReorder.BRING_FORWARD));

    nudge.add(new CmdNudge(CmdNudge.LEFT));
    nudge.add(new CmdNudge(CmdNudge.RIGHT));
    nudge.add(new CmdNudge(CmdNudge.UP));
    nudge.add(new CmdNudge(CmdNudge.DOWN));

    JMenuItem nextEditItem = editTabs.add(Actions.NextEditTab);
    nextEditItem.setAccelerator(F6);
    editTabs.addSeparator();

    JMenuItem tabe1Item = editTabs.add(new ActionGoToEdit("As Diagram"));
    tabe1Item.setAccelerator(altshift1);
    JMenuItem tabe2Item = editTabs.add(new ActionGoToEdit("As Table"));
    tabe2Item.setAccelerator(altshift2);
    JMenuItem tabe3Item = editTabs.add(new ActionGoToEdit("As Metrics"));
    tabe3Item.setAccelerator(altshift3);

    JMenuItem nextDetailsItem = detailsTabs.add(Actions.NextDetailsTab);
    nextDetailsItem.setAccelerator(F5);
    detailsTabs.addSeparator();

    JMenuItem tab1Item = detailsTabs.add(new ActionGoToDetails("ToDoItem"));
    tab1Item.setAccelerator(alt1);
    JMenuItem tab2Item = detailsTabs.add(new ActionGoToDetails("Properties"));
    tab2Item.setAccelerator(alt2);
    JMenuItem tab3Item = detailsTabs.add(new ActionGoToDetails("Javadocs"));
    tab3Item.setAccelerator(alt3);
    JMenuItem tab4Item = detailsTabs.add(new ActionGoToDetails("Source"));
    tab4Item.setAccelerator(alt4);
    JMenuItem tab5Item = detailsTabs.add(new ActionGoToDetails("Constraints"));
    tab5Item.setAccelerator(alt5);
    JMenuItem tab6Item = detailsTabs.add(new ActionGoToDetails("TaggedValues"));
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

