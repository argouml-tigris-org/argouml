package uci.uml.ui;

import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;

public class GoModelToElements implements TreeModelPrereqs {

  public Object getRoot() {
    System.out.println("getRoot should never be called");
    return null;
  } 
  public void setRoot(Object r) { }

  public Object getChild(Object parent, int index) {
    if (parent instanceof Package) {
      ElementOwnership eo = (ElementOwnership)
	((Package)parent).getOwnedElement().elementAt(index);
      return eo.getModelElement();
    }
    System.out.println("getChild should never be get here GoModelToElements");
    return null;
  }
  
  public int getChildCount(Object parent) {
    if (parent instanceof Package) {
      Vector oes = ((Package) parent).getOwnedElement();
      return (oes == null) ? 0 : oes.size();
    }
    return 0;
  }
  
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof Package) {
      Vector oes = ((Package)parent).getOwnedElement();
      if (oes.contains(child)) return oes.indexOf(child);
    }
    return -1;
  }

  public boolean isLeaf(Object node) {
    return !(node instanceof Package && getChildCount(node) > 0);
  }
  
  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }

  public Vector getPrereqs() {
    Vector pros = new Vector();
    pros.addElement(uci.uml.Model_Management.Model.class);
    return pros;
  }
  public Vector getProvidedTypes() {
    Vector pros = new Vector();
    pros.addElement(uci.uml.Foundation.Core.ModelElement.class);
    return pros;
  }

}
