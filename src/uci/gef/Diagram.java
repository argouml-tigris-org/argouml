package uci.gef;

import java.util.*;
import java.awt.*;

import uci.gef.*;
import uci.graph.*;
import uci.ui.*;


public class Diagram {

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected String _name = "no title set";
  protected String _comments = "(no comments given)";
  protected LayerPerspective _lay;
  protected ToolBar _toolBar;

  ////////////////////////////////////////////////////////////////
  // constructors

  public Diagram() {
    this("untitled");
  }

  public Diagram(String name) {
    this(name, new DefaultGraphModel());
  }

  public Diagram(String name, GraphModel gm) {
    this(name, gm, new LayerPerspective(name, gm));
  }

  public Diagram(String name, GraphModel gm, LayerPerspective lay) {
    _name = name;
    _lay = lay;
    setGraphModel(gm);
    initToolBar();
  }

  protected void initToolBar() {
    _toolBar = new PaletteFig();
  }
  
  ////////////////////////////////////////////////////////////////
  // accessors

  public ToolBar getToolBar() { return _toolBar; }
  public void setToolBar(ToolBar tb) { _toolBar = tb; }

  public String getComments() { return _comments; }
  public void setComments(String c) { _comments = c; }

  public String getName() { return _name; }
  public void setName(String n) { _name = n; }

  public void setGraphModel(GraphModel gm) { getLayer().setGraphModel(gm); }
  public GraphModel getGraphModel() { return getLayer().getGraphModel(); }

  public LayerPerspective getLayer() { return _lay; }
  public void setLayer(LayerPerspective lay) { _lay = lay; }

  
} /* end class Diagram */
