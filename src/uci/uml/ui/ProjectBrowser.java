package uci.uml.ui;

import uci.argo.kernel.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import uci.util.*;
import uci.ui.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
// import com.sun.java.swing.text.*;
// import com.sun.java.swing.border.*;

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
  protected static Action _actionNew = new ActionNew();
  protected static Action _actionOpen = new ActionOpen();
  protected static Action _actionSave = new ActionSave();
  protected static Action _actionSaveAs = new ActionSaveAs();
  // -----
  protected static Action _actionAddToProj = new ActionAddToProj();
  // -----
  protected static Action _actionPrint = new ActionPrint();
  // -----
  protected static Action _actionExit = new ActionExit();

  // edit menu
  protected static Action _actionUndo = new ActionUndo();
  protected static Action _actionRedo = new ActionRedo();
  protected static Action _actionCut = new ActionCut();
  protected static Action _actionCopy = new ActionCopy();
  protected static Action _actionPaste = new ActionPaste();
  protected static Action _actionDelete = new ActionDelete();

  // view menu
  protected static Action _actionNavUp = new ActionNavUp();
  protected static Action _actionNavDown = new ActionNavDown();
  protected static Action _actionNextTab = new ActionNextTab();
  protected static Action _actionPrevTab = new ActionPrevTab();
  protected static Action _actionShowDiagramTab = new ActionShowDiagramTab();
  protected static Action _actionShowTableTab = new ActionShowTableTab();
  protected static Action _actionShowTextTab = new ActionShowTextTab();
  protected static Action _actionAddToFavs = new ActionAddToFavs();

  // create menu
  protected static Action _actionCreateMultiple = new ActionCreateMultiple();
  // -----
  protected static Action _actionClass = new ActionClass();
  protected static Action _actionRectangle = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigRect.class, "Rectangle");
  protected static Action _actionRRectangle = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigRRect.class, "RRect");
  protected static Action _actionCircle = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigCircle.class, "Circle");
  protected static Action _actionLine = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigLine.class, "Line");
  protected static Action _actionText = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigText.class, "Text");
  protected static Action _actionPoly = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigPoly.class, "Polygon");
  protected static Action _actionInk = new uci.gef.CmdSetMode(uci.gef.ModeCreateFigInk.class, "Ink");

  // critique menu
  protected static Action _actionAutoCritique = new ActionAutoCritique();
  protected static Action _actionOpenDecisions = new ActionOpenDecisions();
  protected static Action _actionOpenGoals = new ActionOpenGoals();
  protected static Action _actionOpenCritics = new ActionOpenCritics();

  
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
    create.add(_actionClass);
    create.add(_actionRectangle);
    create.add(_actionRRectangle);
    create.add(_actionCircle);
    create.add(_actionLine);
    create.add(_actionText);
    create.add(_actionPoly);
    create.add(_actionInk);

    Menu critique = (Menu) _menuBar.add(new Menu("Critique"));
    critique.setMnemonic('R');
    critique.addCheckItem(_actionAutoCritique);
    critique.addSeparator();
    critique.add(_actionOpenDecisions);
    critique.add(_actionOpenGoals);
    critique.add(_actionOpenCritics); 
  }



  protected Component createPanels() {
    JSplitPane top =
      new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, _navPane, _multiPane);
    JSplitPane bot =
      new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, _toDoPane, _detailsPane);
    JSplitPane splitPane =
      new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bot);
    top.setDividerSize(2);
    top.setDividerLocation(250);
    bot.setDividerSize(2);
    splitPane.setDividerSize(2);
    //bot.setOneTouchExpandable(true);
    return splitPane;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setProject(Project p) {
    _project = p;
    _navPane.setRoot(_project);
    setTitle(getAppName() + " - " + _project.getName());
    UMLAction.updateAllEnabled();
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


  public void setTarget(Object o) {
    _multiPane.setTarget(o);
  }

  public void setToDoItem(Object o) {
    _detailsPane.setToDoItem(o);
  }

  public void setDetalsTarget(Object o) {
    _detailsPane.setTarget(o);
  }

  public StatusBar getStatusBar() { return _statusBar; }
  

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
//     _Frame.show();
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

// class WindowCloser extends WindowAdapter {
//   public WindowCloser() { }
//   public void windowClosing(WindowEvent e) { System.exit(0); }
// };

