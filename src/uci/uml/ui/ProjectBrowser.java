// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


package uci.uml.ui;

import uci.argo.kernel.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
// import com.sun.java.swing.text.*;
// import com.sun.java.swing.border.*;

import uci.util.*;
import uci.ui.*;
import uci.gef.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;


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
  protected static Action _actionNew =  Actions.New;
  protected static Action _actionOpen = Actions.Open;
  protected static Action _actionSave = Actions.Save;
  protected static Action _actionSaveAs = Actions.SaveAs;
  // -----
  protected static Action _actionAddToProj = Actions.AddToProj;
  // -----
  protected static Action _actionPrint = Actions.Print;
  // -----
  protected static Action _actionExit = Actions.Exit;

  // edit menu
  protected static Action _actionUndo = Actions.Undo;
  protected static Action _actionRedo = Actions.Redo;
  protected static Action _actionCut = Actions.Cut;
  protected static Action _actionCopy = Actions.Copy;
  protected static Action _actionPaste = Actions.Paste;
  protected static Action _actionDelete = new CmdDelete();
  protected static Action _actionDispose = new CmdDispose();

  // view menu
  protected static Action _actionNavUp = Actions.NavUp;
  protected static Action _actionNavDown = Actions.NavDown;
  protected static Action _actionNextTab = Actions.NextTab;
  protected static Action _actionPrevTab = Actions.PrevTab;
  protected static Action _actionShowDiagramTab = Actions.ShowDiagramTab;
  protected static Action _actionShowTableTab = Actions.ShowTableTab;
  protected static Action _actionShowTextTab = Actions.ShowTextTab;
  protected static Action _actionAddToFavs = Actions.AddToFavs;

  // create menu
  protected static Action _actionCreateMultiple = Actions.CreateMultiple;
  // ----- diagrams
  protected static Action _actionClassDiagram = Actions.ClassDiagram;
  protected static Action _actionUseCaseDiagram = Actions.UseCaseDiagram;
  protected static Action _actionStateDiagram = Actions.StateDiagram;
  // ----- model elements
  protected static Action _actionModel = Actions.Model;
  protected static Action _actionClass = Actions.Class;
  protected static Action _actionInterface = Actions.Interface;
  protected static Action _actionActor = Actions.Actor;
  protected static Action _actionUseCase = Actions.UseCase;
  protected static Action _actionState = Actions.State;
  protected static Action _actionPseudostate = Actions.Pseudostate;
  protected static Action _actionAttr = Actions.Attr;
  protected static Action _actionOper = Actions.Oper;
  // -----  shapes
  protected static Action _actionRectangle = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigRect.class, "Rectangle");
  protected static Action _actionRRectangle = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigRRect.class, "RRect");
  protected static Action _actionCircle = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigCircle.class, "Circle");
  protected static Action _actionLine = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigLine.class, "Line");
  protected static Action _actionText = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigText.class, "Text");
  protected static Action _actionPoly = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigPoly.class, "Polygon");
  protected static Action _actionInk = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigInk.class, "Ink");

  // critique menu
  protected static Action _actionAutoCritique = Actions.AutoCritique;
  protected static Action _actionOpenDecisions = Actions.OpenDecisions;
  protected static Action _actionOpenGoals = Actions.OpenGoals;
  protected static Action _actionOpenCritics = Actions.OpenCritics;


  // Help menu
  protected static Action _actionAboutArgoUML = Actions.AboutArgoUML;

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected String _appName = "ProjectBrowser";
  protected Project _project = null;

  protected NavigatorPane _navPane = new NavigatorPane();
  public ToDoPane _toDoPane = new ToDoPane();
  protected MultiEditorPane _multiPane = new MultiEditorPane();
  protected DetailsPane _detailsPane = new DetailsPane();
  protected JMenuBar _menuBar = new JMenuBar();
  protected StatusBar _statusBar = new StatusBar();
  //protected JToolBar _toolBar = new JToolBar();


  public Font defaultFont = new Font("Dialog", Font.PLAIN, 10);
  //  public static JFrame _Frame;

  protected JSplitPane _mainSplit, _topSplit, _botSplit;


  ////////////////////////////////////////////////////////////////
  // constructors

  public ProjectBrowser(String appName) {
    super(appName);
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
    KeyStroke altF4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK);

    JMenuItem mi;
    // File Menu
    JMenu file = (JMenu) _menuBar.add(new JMenu("File"));
    file.setMnemonic('F');
    JMenuItem newItem = file.add(_actionNew);
    newItem.setMnemonic('N');
    newItem.setAccelerator(ctrlN);
    JMenuItem openItem = file.add(_actionOpen);
    openItem.setMnemonic('O');
    openItem.setAccelerator(ctrlO);
    JMenuItem saveItem = file.add(_actionSave);
    saveItem.setMnemonic('S');
    saveItem.setAccelerator(ctrlS);
    file.add(_actionSaveAs);
    file.addSeparator();
    JMenuItem printItem = file.add(_actionPrint);
    printItem.setMnemonic('P');
    printItem.setAccelerator(ctrlP);
    //file.addSeparator();
    //file.add(_actionProjectInfo);
    file.addSeparator();
    JMenuItem exitItem = file.add(_actionExit);
    exitItem.setMnemonic('x');
    exitItem.setAccelerator(altF4);

    JMenu edit = (JMenu) _menuBar.add(new JMenu("Edit"));
    edit.setMnemonic('E');
    edit.add(_actionUndo);
    edit.add(_actionRedo);
    edit.addSeparator();
    edit.add(_actionCut);
    edit.add(_actionCopy);
    edit.add(_actionPaste);
    edit.addSeparator();
    edit.add(_actionDelete);
    edit.add(_actionDispose);

    JMenu view = (JMenu) _menuBar.add(new JMenu("View"));
    // maybe should be Navigate instead of view
    view.setMnemonic('V');
    view.add(_actionNavDown);
    view.add(_actionNavUp);
    view.addSeparator();
    view.add(_actionNextTab);
    view.add(_actionPrevTab);
    view.addSeparator();
    view.add(_actionShowDiagramTab);
    view.add(_actionShowTableTab);
    view.add(_actionShowTextTab);
    //view.addSeparator();
    //view.add(_actionAddToFavorites);

    JMenu create = (JMenu) _menuBar.add(new JMenu("Create"));
    create.setMnemonic('C');
    create.add(_actionCreateMultiple);
    create.addSeparator();

    JMenu createDiagrams = (JMenu) create.add(new JMenu("Diagrams"));
    createDiagrams.add(_actionClassDiagram);
    createDiagrams.add(_actionUseCaseDiagram);
    createDiagrams.add(_actionStateDiagram);

    JMenu createModelElements = (JMenu) create.add(new JMenu("Model Elements"));
    createModelElements.add(_actionModel);
    createModelElements.add(_actionClass);
    createModelElements.add(_actionInterface);
    createModelElements.add(_actionActor);
    createModelElements.add(_actionUseCase);
    createModelElements.add(_actionState);
    createModelElements.add(_actionPseudostate);
    createModelElements.add(_actionAttr);
    createModelElements.add(_actionOper);

    JMenu createFig = (JMenu) create.add(new JMenu("Shapes"));
    createFig.add(_actionRectangle);
    createFig.add(_actionRRectangle);
    createFig.add(_actionCircle);
    createFig.add(_actionLine);
    createFig.add(_actionText);
    createFig.add(_actionPoly);
    createFig.add(_actionInk);

    Menu critique = (Menu) _menuBar.add(new Menu("Critique"));
    critique.setMnemonic('R');
    critique.addCheckItem(_actionAutoCritique);
    critique.addSeparator();
    critique.add(_actionOpenDecisions);
    critique.add(_actionOpenGoals);
    critique.add(_actionOpenCritics);

    // Help Menu
    JMenu help = new JMenu("Help");
    help.setMnemonic('H');
    help.add(_actionAboutArgoUML);
    //_menuBar.setHelpMenu(help);
    _menuBar.add(help);    
  }


  protected Component createPanels() {
    _topSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, _navPane, _multiPane);
    _botSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			       _toDoPane, _detailsPane);
    _mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, _topSplit, _botSplit);
    _topSplit.setDividerSize(2);
    _topSplit.setDividerLocation(250);
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
    setTitle(getAppName() + " - " + _project.getName());
    Actions.updateAllEnabled();
    // update all panes
  }
  public Project getProject() { return _project; }

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
  }
  
  public void setTarget(Object o) {
    _multiPane.setTarget(o);
    _detailsPane.setTarget(o);
    if (o instanceof Model) _project.setCurrentModel((Model)o);
    if (o instanceof ModelElement) {
      ElementOwnership eo = ((ModelElement)o).getElementOwnership();
      if (eo == null) { System.out.println("no path to model"); return; }
      while (!(eo.getNamespace() instanceof Model)) {
	eo = eo.getNamespace().getElementOwnership();
      }
      _project.setCurrentModel((Model)eo.getNamespace());
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

  public void setDetalsTarget(Object o) {
    _detailsPane.setTarget(o);
  }

  public StatusBar getStatusBar() { return _statusBar; }
  

  public ToDoPane getToDoPane() { return _toDoPane; }
  public NavigatorPane getNavPane() { return _navPane; }


//   ////////////////////////////////////////////////////////////////
//   // window operations
  public void setVisible(boolean b) {
    super.setVisible(b);
    if (b) uci.gef.Globals.setStatusBar(this);
  }

//   public void panelHack() {
//     _topSplit.setDividerLocation(251);
//     _botSplit.setDividerLocation(151);
//     _toDoPane.validate();
//   }

  
  ////////////////////////////////////////////////////////////////
  // IStatusBar
  public void showStatus(String s) { _statusBar.showStatus(s); }
  
  
  ////////////////////////////////////////////////////////////////
  // event handlers


//  public void actionPerformed(ActionEvent e) {
//    if (e,getSource() == _quitItem)
//      System.exit(0);
//  }

  ////////////////////////////////////////////////////////////////
  // notifications and updates


  ////////////////////////////////////////////////////////////////
  // testing

//   public static void main(String args[]) {
//     _Frame = new JFrame("ProjectBrowser");
//     _Frame.addWindowListener(new WindowCloser());
//     JOptionPane.setRootFrame(_Frame);
//     _Frame.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
//     Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
//     _Frame.setLocation(scrSize.width/2 - INITIAL_WIDTH/2,
// 		      scrSize.height/2 - INITIAL_HEIGHT/2);
//     _Frame.setVisble(true);
//     _Frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//     ProjectBrowser pb = new ProjectBrowser("ProjectBrowser");
//     _Frame.getContentPane().removeAll();
//     _Frame.getContentPane().setLayout(new BorderLayout());
//     _Frame.getContentPane().add(pb, BorderLayout.CENTER);
//     _Frame.setLocation(scrSize.width/2 - WIDTH/2,
// 		       scrSize.height/2 - HEIGHT/2);
//     _Frame.setSize(WIDTH, HEIGHT);
//     _Frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//     _Frame.validate();
//     _Frame.repaint();
//     pb.requestDefaultFocus();
//   }


} /* end class ProjectBrowser */





////////////////////////////////////////////////////////////////


// class DelayedPanelHack implements Runnable {
//   ProjectBrowser _pb;

//   public DelayedPanelHack(ProjectBrowser pb) { _pb = pb; }

//   public void run() { _pb.panelHack(); }

// } /* end class DelayedPanelHack */
