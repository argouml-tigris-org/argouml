package uci.uml.visual;

import java.util.*;
import java.awt.*;

import uci.gef.*;
import uci.graph.*;
import uci.ui.*;
import uci.uml.Model_Management.*;


public class UMLDiagram extends Diagram {

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Model _model;

  ////////////////////////////////////////////////////////////////
  // constructors

  public UMLDiagram(Model m) {
    _model = m;
  }

  public Model getModel() { return _model; }
  
} /* end class UMLDiagram */
