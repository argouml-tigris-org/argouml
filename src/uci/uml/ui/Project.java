// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
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

import java.util.*;
import java.beans.*;

import uci.gef.Layer;
import uci.gef.Diagram;
import uci.gef.Fig;
import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.uml.generate.*;

/** A datastructure that represents the designer's current project.  A
 *  Project consists of diagrams and UML models. */

public class Project implements java.io.Serializable {
  ////////////////////////////////////////////////////////////////
  // instance variables

  public String _pathname = "";
  public String _filename = "untitled.argo";
  public Vector _models = new Vector(); //instances of Model
  public Vector _diagrams = new Vector(); // instances of LayerDiagram
  public boolean _needsSave = false;
  public Model _curModel = null;
  public Hashtable _definedTypes = new Hashtable(80);
  public GenerationPreferences _cgPrefs = new GenerationPreferences();
  public transient VetoableChangeSupport _vetoSupport = null;

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

    defineType(JavaUML.STRING_CLASS);
    defineType(JavaUML.VOID_TYPE);
    defineType(JavaUML.CHAR_TYPE);
    defineType(JavaUML.INT_TYPE);
    defineType(JavaUML.BOOLEAN_TYPE);
    defineType(JavaUML.BYTE_TYPE);
    defineType(JavaUML.LONG_TYPE);
    defineType(JavaUML.FLOAT_TYPE);
    defineType(JavaUML.DOUBLE_TYPE);

    defineType(JavaUML.CHAR_CLASS);
    defineType(JavaUML.INT_CLASS);
    defineType(JavaUML.BOOLEAN_CLASS);
    defineType(JavaUML.BYTE_CLASS);
    defineType(JavaUML.LONG_CLASS);
    defineType(JavaUML.FLOAT_CLASS);
    defineType(JavaUML.DOUBLE_CLASS);

    defineType(JavaUML.RECTANGLE_CLASS);
    defineType(JavaUML.POINT_CLASS);
    defineType(JavaUML.COLOR_CLASS);

    defineType(JavaUML.VECTOR_CLASS);
    defineType(JavaUML.HASHTABLE_CLASS);
    defineType(JavaUML.STACK_CLASS);
  }
  ////////////////////////////////////////////////////////////////
  // accessors
  // needs-more-work 

  public String getName() {
    // needs-more-work: maybe separate name
    return _filename;
  }
  public void setName(String n) throws PropertyVetoException {
    getVetoSupport().fireVetoableChange("Name", _filename, n);
    _filename = n;
  }

  public String getFilename() { return _filename; }
  public void setFilename(String n) throws PropertyVetoException {
    getVetoSupport().fireVetoableChange("Filename", _filename, n);
    _filename = n;
  }

  public String getPathname() { return _pathname; }
  public void setPathname(String n) throws PropertyVetoException {
    getVetoSupport().fireVetoableChange("Pathname", _pathname, n);    
    _pathname = n;
  }

  public boolean getNeedsSave() { return _needsSave; }
  public void setNeedsSave(boolean ns) { _needsSave = ns; }
  public void needsSave() { setNeedsSave(true); }

  public Vector getModels() { return _models; }
  public void addModel(Model m) throws PropertyVetoException {
    getVetoSupport().fireVetoableChange("Models", _models, m);
    _models.addElement(m);
    setCurrentModel(m);
    _needsSave = true;
  }

  public Hashtable getDefinedTypes() { return _definedTypes; }
  public void setDefinedTypes(Hashtable h) { _definedTypes = h; }
  public void defineType(Classifier cls) {
    //needs-more-work: should take namespaces into account!
    _definedTypes.put(cls.getName().getBody(), cls);
  }
  public Classifier findType(String s) {
    Classifier cls = (Classifier) _definedTypes.get(s);
    if (cls == null) {
      cls = new MMClass(s);
      _definedTypes.put(s, cls);
    }
    return cls;
  }


  public void setCurrentModel(Model m) { _curModel = m; }
  public Model getCurrentModel() { return _curModel; }

  public Vector getDiagrams() { return _diagrams; }
  public void addDiagram(Diagram d) throws PropertyVetoException {
    getVetoSupport().fireVetoableChange("Diagrams", _diagrams, d);
    _diagrams.addElement(d);
    _needsSave = true;
  }

  public Object getInitialTarget() {
    if (_diagrams.size() > 0) return _diagrams.elementAt(0);
    if (_models.size() > 0) return _models.elementAt(0);
    return null;
  }

  public void setGenerationPrefs(GenerationPreferences cgp) { _cgPrefs = cgp; }
  public GenerationPreferences getGenerationPrefs() { return _cgPrefs; }

  ////////////////////////////////////////////////////////////////
  // event handling

  public void addVetoableChangeListener(VetoableChangeListener l) {
    getVetoSupport().removeVetoableChangeListener(l);
    getVetoSupport().addVetoableChangeListener(l);
  }

  public void removeVetoableChangeListener(VetoableChangeListener l) {
    getVetoSupport().removeVetoableChangeListener(l);
  }

  protected VetoableChangeSupport getVetoSupport() {
    if (_vetoSupport == null) _vetoSupport = new VetoableChangeSupport(this);
    return _vetoSupport;
  }

  public void preSave() {
    for (int i = 0; i < _diagrams.size(); i++)
      ((Diagram)_diagrams.elementAt(i)).preSave();
    // needs-more-work: is preSave needed for models?
  }

  public void postSave() {
    for (int i = 0; i < _diagrams.size(); i++)
      ((Diagram)_diagrams.elementAt(i)).postSave();
    // needs-more-work: is postSave needed for models?
  }

  public void postLoad() {
    for (int i = 0; i < _diagrams.size(); i++)
      ((Diagram)_diagrams.elementAt(i)).postLoad();
    // needs-more-work: is postLoad needed for models?
  }

  ////////////////////////////////////////////////////////////////
  // trash related methos
  public void moveToTrash(Object obj) {
    System.out.println("trashing: " + obj);
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
	  //System.out.println("trashing model element from diagram");
	  d.getLayer().remove(f);
	}
      } /* end while */
    }
  }

  public void moveFromTrash(Object obj) {
    System.out.println("needs-more-work: not restoring " + obj);
  }

  public boolean isInTrash(Object dm) {
    return (dm instanceof ModelElement) &&
      ((ModelElement)dm).getElementOwnership() == null;
  }

} /* end class Project */
