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
  protected static Action _actionRectangle = new CmdSetMode(ModeCreateFigRect.class, "Rectangle");
  protected static Action _actionRRectangle = new CmdSetMode(ModeCreateFigRRect.class, "RRect");
  protected static Action _actionCircle = new CmdSetMode(ModeCreateFigCircle.class, "Circle");
  protected static Action _actionLine = new CmdSetMode(ModeCreateFigLine.class, "Line");
  protected static Action _actionText = new CmdSetMode(ModeCreateFigText.class, "Text");
  protected static Action _actionPoly = new CmdSetMode(ModeCreateFigPoly.class, "Polygon");
  protected static Action _actionInk = new CmdSetMode(ModeCreateFigInk.class, "Ink");


  ////////////////////////////////////////////////////////////////
  // constructor
  
  public ClassDiagramEditor() { this(new JGraph()); }

  public ClassDiagramEditor(JGraph jg) {
    super("Diagram");
    setLayout(new BorderLayout());
    _graph = jg;
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

  
  
  protected static ImageIcon loadIconResource(String imgName, String desc) {
    ImageIcon res = null;
    try {
      java.net.URL imgURL = ClassDiagramEditor.class.getResource(imgName);
      return new ImageIcon(imgURL, desc);
    }
    catch (Exception ex) {
      return new ImageIcon(desc);
    }
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

    ImageIcon assocUp = loadIconResource("Association.gif", "");
    ImageIcon assocDown = loadIconResource("AssociationInverse.gif", "");
    ImageIcon inherUp = loadIconResource("Inheritance.gif", "");
    ImageIcon inherDown = loadIconResource("InheritanceInverse.gif", "");
    _lineModeBG = _toolBar.addRadioGroup("Association", assocUp, assocDown,
					 "Inheritance", inherUp, inherDown);

    _toolBar.add(_actionClass);
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


}
