package uci.uml.ui;

import java.util.*;
import uci.uml.Model_Management.Model;
import uci.gef.Layer;

public class Project {
  ////////////////////////////////////////////////////////////////
  // instance variables

  public String _pathname = "";
  public String _filename = "untitled.argo";
  public Vector _models = new Vector(); //instances of Model
  public Vector _diagrams = new Vector(); // instances of LayerDiagram
  public boolean _needsSave = false;
  public Vector _trash = new Vector(); // instances of ModelElement


  ////////////////////////////////////////////////////////////////
  // constructor

  public Project(String name) {
    _filename = name;
    if (!_filename.endsWith(".argo")) _filename += ".argo";
    initProject();
  }

  // needs-more-work: project setup wizard?
  protected void initProject() {
    //_models.addElement(new Model("Object Model"));
    //_diagrams.addElement(new LayerDiagram("Untitled Diagram"));
  }
  
  ////////////////////////////////////////////////////////////////
  // accessors
  // needs-more-work 

  public String getName() {
    // needs-more-work: maybe separate name
    return _filename;
  }
  public void setName(String n) {_filename = n; }

  public String getFilename() { return _filename; }
  public void setFilename(String n) {_filename = n; }

  public String getPathname() { return _pathname; }
  public void setPathname(String n) { _pathname = n; }

  public boolean getNeedsSave() { return _needsSave; }
  public void setNeedsSave(boolean ns) { _needsSave = ns; }
  public void needsSave() { setNeedsSave(true); }

  
  public Vector getModels() { return _models; }
  public void addModel(Model m) {
    _models.addElement(m);
    _needsSave = true;
  }

  public Vector getDiagrams() { return _diagrams; }
  public void addDiagram(Layer lay) {
    _diagrams.addElement(lay);
    _needsSave = true;
  }
  
  
} /* end class Project */
