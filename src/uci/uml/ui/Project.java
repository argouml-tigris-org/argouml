package uci.uml.ui;

import java.util.*;
import java.beans.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.gef.Layer;
import uci.gef.Diagram;
import uci.gef.Fig;

public class Project {
  ////////////////////////////////////////////////////////////////
  // instance variables

  public String _pathname = "";
  public String _filename = "untitled.argo";
  public Vector _models = new Vector(); //instances of Model
  public Vector _diagrams = new Vector(); // instances of LayerDiagram
  public boolean _needsSave = false;
  public Model _curModel = null;
  public VetoableChangeSupport _vetoSupport = new VetoableChangeSupport(this);

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
  public void setName(String n) throws PropertyVetoException {
    _vetoSupport.fireVetoableChange("Name", _filename, n);
    _filename = n;
  }

  public String getFilename() { return _filename; }
  public void setFilename(String n) throws PropertyVetoException {
    _vetoSupport.fireVetoableChange("Filename", _filename, n);
    _filename = n;
  }

  public String getPathname() { return _pathname; }
  public void setPathname(String n) throws PropertyVetoException {
    _vetoSupport.fireVetoableChange("Pathname", _pathname, n);    
    _pathname = n;
  }

  public boolean getNeedsSave() { return _needsSave; }
  public void setNeedsSave(boolean ns) { _needsSave = ns; }
  public void needsSave() { setNeedsSave(true); }

  
  public Vector getModels() { return _models; }
  public void addModel(Model m) throws PropertyVetoException {
    _vetoSupport.fireVetoableChange("Models", _models, m);
    _models.addElement(m);
    setCurrentModel(m);
    _needsSave = true;
  }

  public void setCurrentModel(Model m) { _curModel = m; }
  public Model getCurrentModel() { return _curModel; }

  public Vector getDiagrams() { return _diagrams; }
  public void addDiagram(Diagram d) throws PropertyVetoException {
    _vetoSupport.fireVetoableChange("Diagrams", _diagrams, d);
    _diagrams.addElement(d);
    _needsSave = true;
  }

  public void addVetoableChangeListener(VetoableChangeListener l) {
    _vetoSupport.removeVetoableChangeListener(l);
    _vetoSupport.addVetoableChangeListener(l);
  }

  public void removeVetoableChangeListener(VetoableChangeListener l) {
    _vetoSupport.removeVetoableChangeListener(l);
  }

  ////////////////////////////////////////////////////////////////
  // trash related methos
  public void moveToTrash(Object obj) {
    System.out.println("needs-more-work: trashing " + obj);
    if (obj instanceof ModelElement) {
      ModelElement me = (ModelElement) obj;
      try { me.setElementOwnership(null); }
      catch (PropertyVetoException pve) {
	System.out.println("Project got a PropertyVetoException");
      }
      Enumeration diagramEnum = _diagrams.elements();
      while (diagramEnum.hasMoreElements()) {
	Diagram d = (Diagram) diagramEnum.nextElement();
	Fig f = d.getLayer().presentationFor(me);
	if (f != null) {
	  System.out.println("trashing model element from diagram");
	  d.getLayer().remove(f);
	}
      } /* end while */
    }
  }

  public void moveFromTrash(Object obj) {
    System.out.println("needs-more-work: not restoring " + obj);
  }

} /* end class Project */
