package uci.uml.ui;

import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.uml.visual.UMLDiagram;

public class GoModelToDiagram implements TreeModelPrereqs {

  public Object getRoot() {
    System.out.println("getRoot should never be called");
    return null;
  } 

  public Object getChild(Object parent, int index) {
    if (parent instanceof Model) {
      Model m = (Model) parent;
      Project proj = ProjectBrowser.TheInstance.getProject();
      Vector diags = proj.getDiagrams();
      java.util.Enumeration diagEnum = diags.elements();
      while (diagEnum.hasMoreElements()) {
	UMLDiagram d = (UMLDiagram) diagEnum.nextElement();
	if (d.getModel() == m) index--;
	if (index == -1) return d;
      }
    }
    System.out.println("getChild should never be get here GoModelToDiagram");
    return null;
  }
  
  public int getChildCount(Object parent) {
    if (parent instanceof Model) {
      int count = 0;
      Model m = (Model) parent;
      Project proj = ProjectBrowser.TheInstance.getProject();
      Vector diags = proj.getDiagrams();
      java.util.Enumeration diagEnum = diags.elements();
      while (diagEnum.hasMoreElements()) {
	UMLDiagram d = (UMLDiagram) diagEnum.nextElement();
	if (d.getModel() == m) count++;
      }
      return count;
    }
    return 0;
  }
  
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof Model) {
      int count = 0;
      Model m = (Model) parent;
      Project proj = ProjectBrowser.TheInstance.getProject();
      Vector diags = proj.getDiagrams();
      java.util.Enumeration diagEnum = diags.elements();
      while (diagEnum.hasMoreElements()) {
	UMLDiagram d = (UMLDiagram) diagEnum.nextElement();
	if (d.getModel() != m) continue;
	if (d == child) return count;
	count++;
      }
      return count;
    }
    return -1;
  }

  public boolean isLeaf(Object node) {
    return !(node instanceof Model && getChildCount(node) > 0);
  }
  
  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }

  public Vector getPrereqs() {
    Vector pres = new Vector();
    pres.addElement(uci.uml.Model_Management.Model.class);
    return pres;
  }
  public Vector getProvidedTypes() {
    Vector pros = new Vector();
    pros.addElement(uci.gef.Diagram.class);
    return pros;
  }

}
