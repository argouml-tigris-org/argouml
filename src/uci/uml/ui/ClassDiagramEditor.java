package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;

import uci.ui.*;
import uci.util.*;
import uci.gef.*;
import uci.gef.event.*;
import uci.uml.visual.*;


public class ClassDiagramEditor extends TabSpawnable
implements TabModelTarget, GraphSelectionListener {
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Object _target;
  protected JGraph _graph;
  protected ToolBar _toolBar;
  protected ButtonGroup _lineModeBG;
  protected boolean _shouldBeEnabled = true;

  ////////////////
  // actions
  // needs-more-work: should these be static?
  protected static Action _actionCut = new ActionCut();
  protected static Action _actionCopy = new ActionCopy();
  protected static Action _actionPaste = new ActionPaste();

  protected static Action _actionSelect = new CmdSetMode(ModeSelect.class, "Select");
  protected static Action _actionClassWizard = new ActionClassWizard();
  protected static Action _actionClass = new ActionClass();
  
  protected static Action _actionClass2 = new
    CmdCreateNode(uci.uml.Foundation.Core.Class.class, "Class");

  protected static Action _actionInterface = new
    CmdCreateNode(uci.uml.Foundation.Core.Interface.class, "Interface");

  protected static Action _actionAssoc = new
    CmdSetMode(ModeCreateArc.class,
	     "edgeClass", uci.uml.Foundation.Core.Association.class,
	     "Association");
  //,
  //loadIconResource(imageName("Association"), "Association"));

  protected static Action _actionGeneralize = new
    CmdSetMode(ModeCreateArc.class,
	     "edgeClass", uci.uml.Foundation.Core.Generalization.class,
	     "Generalization");

  protected static Action _actionRectangle = new CmdSetMode(ModeCreateFigRect.class, "Rectangle");
  protected static Action _actionRRectangle = new CmdSetMode(ModeCreateFigRRect.class, "RRect");
  protected static Action _actionCircle = new CmdSetMode(ModeCreateFigCircle.class, "Circle");
  protected static Action _actionLine = new CmdSetMode(ModeCreateFigLine.class, "Line");
  protected static Action _actionText = new CmdSetMode(ModeCreateFigText.class, "Text");
  protected static Action _actionPoly = new CmdSetMode(ModeCreateFigPoly.class, "Polygon");
  protected static Action _actionInk = new CmdSetMode(ModeCreateFigInk.class, "Ink");


  ////////////////////////////////////////////////////////////////
  // constructor
  
  public ClassDiagramEditor() {
    this(new JGraph(new ClassDiagramGraphModel()));
    // needs-more-work: pass project as a constructor arg?
    // or should I get the graph model from the Diagram object?
  }

  public ClassDiagramEditor(JGraph jg) {
    super("Diagram");
    setLayout(new BorderLayout());
    _graph = jg;
    ClassDiagramRenderer rend = new ClassDiagramRenderer(); // singleton
    _graph.setGraphNodeRenderer(rend);
    _graph.setGraphEdgeRenderer(rend);
    uci.gef.Globals.setStatusBar(ProjectBrowser.TheInstance);
    initToolBar();
    //_graph.setToolBar(_toolBar); //I wish this had worked...
    add(_toolBar, BorderLayout.NORTH);
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
    p.add(_graph, BorderLayout.CENTER);
    add(p, BorderLayout.CENTER);
    _graph.addGraphSelectionListener(this);
  }

    

  public Object clone() {
    try {
      ClassDiagramEditor newPanel =
	new ClassDiagramEditor((JGraph)_graph.clone());
      return newPanel;
    }
    catch (Exception ex) {
      System.out.println("exception in ClassDiagramEditor clone()");
    }
    return null;
  }
  
  protected void initToolBar() {
    _toolBar = new ToolBar();
    _toolBar.add(_actionCut);
    _toolBar.add(_actionCopy);
    _toolBar.add(_actionPaste);
    _toolBar.addSeparator();
    _toolBar.add(_actionSelect);

//     ImageIcon assocUp = loadIconResource("Association", "");
//     ImageIcon assocDown = loadIconResource("AssociationInverse", "");
//     ImageIcon inherUp = loadIconResource("Inheritance", "");
//     ImageIcon inherDown = loadIconResource("InheritanceInverse", "");
//     _lineModeBG = _toolBar.addRadioGroup("Association", assocUp, assocDown,
// 					 "Inheritance", inherUp, inherDown);

    //needs-more-work: these form an mutually exclusive sticky group
    // ToolBar should support that
    _toolBar.add(_actionClass2);
    _toolBar.add(_actionAssoc);
    _toolBar.add(_actionGeneralize);
    _toolBar.addSeparator();
    _toolBar.add(_actionRectangle);
    _toolBar.add(_actionRRectangle);
    _toolBar.add(_actionCircle);
    _toolBar.add(_actionLine);
    _toolBar.add(_actionText);
    _toolBar.add(_actionPoly);
    _toolBar.add(_actionInk);
  }

  ////////////////////////////////////////////////////////////////
  // accessors
  public void setTarget(Object t) {
    _target = t;
    // needs-more-work
  }
  public Object getTarget() { return _target; }

  public boolean shouldBeEnabled() { return _shouldBeEnabled; }

  ////////////////////////////////////////////////////////////////
  // events

  public void selectionChanged(GraphSelectionEvent gse) {
    System.out.println("ClassDiagramEditor got editor selection event");
    Vector sels = gse.getSelections();
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    if (sels.size() == 1) pb.setDetalsTarget(sels.elementAt(0));
    else pb.setDetalsTarget(null);
  }
  
  public void removeGraphSelectionListener(GraphSelectionListener listener) {
    _graph.removeGraphSelectionListener(listener);
  }


  ////////////////////////////////////////////////////////////////
  // utility methods

  protected static ImageIcon loadIconResource(String imgName, String desc) {
    ImageIcon res = null;
    try {
      java.net.URL imgURL = ClassDiagramEditor.class.getResource(imgName);
      return new ImageIcon(imgURL, desc);
    }
    catch (Exception ex) { return new ImageIcon(desc); }
  }


  
  protected static String imageName(String name) {
    return "/Images/" + stripJunk(name) + ".gif";
  }

  protected static String stripJunk(String s) {
    String res = "";
    int len = s.length();
    for (int i = 0; i < len; i++) {
      char c = s.charAt(i);
      if (Character.isJavaLetterOrDigit(c)) res += c;
    }
    return res;
  }

}
