package uci.uml.visual;

import java.util.*;
import java.awt.*;
import com.sun.java.swing.*;

import uci.gef.*;
import uci.graph.*;
import uci.ui.ToolBar;
import uci.ui.*;
import uci.uml.ui.*;
import uci.uml.Model_Management.*;


public class UMLClassDiagram extends UMLDiagram {

  
  ////////////////
  // actions
  // needs-more-work: should these be static?


  protected static Action _actionSelect =
  new CmdSetMode(ModeSelect.class, "Select");
  
  protected static Action _actionClass = 
  new CmdCreateNode(uci.uml.Foundation.Core.Class.class, "Class");

  protected static Action _actionInterface =
  new CmdCreateNode(uci.uml.Foundation.Core.Interface.class, "Interface");

  protected static Action _actionAssoc =
  new CmdSetMode(ModeCreateEdge.class,
		 "edgeClass", uci.uml.Foundation.Core.Association.class,
		 "Association");
  //,
  //loadIconResource(imageName("Association"), "Association"));

  protected static Action _actionGeneralize =
  new CmdSetMode(ModeCreateEdge.class,
		 "edgeClass", uci.uml.Foundation.Core.Generalization.class,
		 "Generalization");

  protected static Action _actionRectangle =
  new CmdSetMode(ModeCreateFigRect.class, "Rectangle");

  protected static Action _actionRRectangle =
  new CmdSetMode(ModeCreateFigRRect.class, "RRect");

  protected static Action _actionCircle =
  new CmdSetMode(ModeCreateFigCircle.class, "Circle");

  protected static Action _actionLine =
  new CmdSetMode(ModeCreateFigLine.class, "Line");

  protected static Action _actionText =
  new CmdSetMode(ModeCreateFigText.class, "Text");

  protected static Action _actionPoly =
  new CmdSetMode(ModeCreateFigPoly.class, "Polygon");

  protected static Action _actionInk =
  new CmdSetMode(ModeCreateFigInk.class, "Ink");


  ////////////////////////////////////////////////////////////////
  // contructors

  public UMLClassDiagram(Model m) {
    super(m);
    ClassDiagramGraphModel gm = new ClassDiagramGraphModel();
    gm.setModel(m);
    setGraphModel(gm);
    LayerPerspective lay = new LayerPerspective(m.getName().getBody(), gm);
    setLayer(lay);
    ClassDiagramRenderer rend = new ClassDiagramRenderer(); // singleton
    lay.setGraphNodeRenderer(rend);
    lay.setGraphEdgeRenderer(rend);
  }

  
  protected void initToolBar() {
    _toolBar = new ToolBar();
    _toolBar.add(Actions.Cut);
    _toolBar.add(Actions.Copy);
    _toolBar.add(Actions.Paste);
    _toolBar.addSeparator();

    _toolBar.add(_actionSelect);
    _toolBar.addSeparator();

    _toolBar.add(_actionClass);
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
  
} /* end class UMLClassDiagram */
