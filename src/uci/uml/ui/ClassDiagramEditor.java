package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import uci.util.*;
import uci.gef.JGraph;
import com.sun.java.swing.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.event.*;
// import com.sun.java.swing.text.*;
// import com.sun.java.swing.border.*;

public class ClassDiagramEditor extends TabSpawnable
implements TabModelTarget {
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Object _target;
  protected JGraph _graph;
  protected ToolBar _toolBar;
  protected ButtonGroup _lineModeBG;

  ////////////////
  // actions
  // needs-more-work: should these be static?
  protected static Action _actionCut = new ActionCut();
  protected static Action _actionCopy = new ActionCopy();
  protected static Action _actionPaste = new ActionPaste();

  protected static Action _actionSelect = new ActionSelect();
  protected static Action _actionClassWizard = new ActionClassWizard();
  protected static Action _actionClass = new ActionClass();
  protected static Action _actionRectangle = new ActionRectangle();
  protected static Action _actionRRectangle = new ActionRRectangle();
  protected static Action _actionCircle = new ActionCircle();
  protected static Action _actionLine = new ActionLine();
  protected static Action _actionText = new ActionText();
  protected static Action _actionPoly = new ActionPoly();
  protected static Action _actionInk = new ActionInk();


  ////////////////////////////////////////////////////////////////
  // constructor
  
  public ClassDiagramEditor() { this(new JGraph()); }

  public ClassDiagramEditor(JGraph jg) {
    super("Diagram");
    setLayout(new BorderLayout());
    _graph = jg;
    uci.gef.Globals.setStatusBar(ProjectBrowser.TheInstance);
    _toolBar = new ToolBar();
    initToolBar();
    add(_toolBar, BorderLayout.NORTH);
    JPanel p = new JPanel();
    p.setLayout(new BorderLayout());
    p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
    p.add(_graph, BorderLayout.CENTER);
    add(p, BorderLayout.CENTER);
  }

  
  
//   public ImageIcon loadImageIcon(String filename, String description) {
//     // needs-more-work: use class resources
//     String imageDir = "f:/jr/dev06/uci/uml/ui/images/";
//     return new ImageIcon(imageDir + filename, description);  
//   }
  

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
    _toolBar.add(_actionCut);
    _toolBar.add(_actionCopy);
    _toolBar.add(_actionPaste);
    _toolBar.addSeparator();
    _toolBar.add(_actionSelect);
    _toolBar.addSeparator();
    _lineModeBG = _toolBar.addRadioGroup("Association", "Inheritance");
    _toolBar.addSeparator();
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

  

}
