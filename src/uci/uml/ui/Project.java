// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
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
