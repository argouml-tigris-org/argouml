package uci.uml.ui;

import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;

public class GoModelToBaseElements implements TreeModelPrereqs {

  public Object getRoot() {
    System.out.println("getRoot should never be called");
    return null;
  } 
  public void setRoot(Object r) { }

  public Object getChild(Object parent, int index) {
    if (parent instanceof Package) {
      Vector eos = ((Package)parent).getOwnedElement();
      java.util.Enumeration eoEnum = eos.elements();
      while (eoEnum.hasMoreElements()) {
	ElementOwnership eo = (ElementOwnership) eoEnum.nextElement();
	ModelElement me = eo.getModelElement();
	if (me instanceof GeneralizableElement) {
	  Vector gens = ((GeneralizableElement)me).getGeneralization();
	  if (gens == null || gens.size() == 0) index--;
	  if (index == -1) return me;
	}
      }
      System.out.println("getChild not enough base elements found!");
    }
    System.out.println("getChild shouldnt get here GoModelToBaseElements");
    return null;
  }
  
  public int getChildCount(Object parent) {
    if (parent instanceof Package) {
      int count = 0;
      Vector eos = ((Package)parent).getOwnedElement();
      java.util.Enumeration eoEnum = eos.elements();
      while (eoEnum.hasMoreElements()) {
	ElementOwnership eo = (ElementOwnership) eoEnum.nextElement();
	ModelElement me = eo.getModelElement();
	if (me instanceof GeneralizableElement) {
	  Vector gens = ((GeneralizableElement)me).getGeneralization();
	  if (gens == null || gens.size() == 0) count++;
	}
      }
      return count;
    }
    return 0;
  }
  
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof Package) {
      int count = 0;
      Vector eos = ((Package)parent).getOwnedElement();
      java.util.Enumeration eoEnum = eos.elements();
      while (eoEnum.hasMoreElements()) {
	ElementOwnership eo = (ElementOwnership) eoEnum.nextElement();
	ModelElement me = eo.getModelElement();
	if (me instanceof GeneralizableElement) {
	  Vector gens = ((GeneralizableElement)me).getGeneralization();
	  if (gens == null || gens.size() == 0) return count;
	  count++;
	}
      }
      return count;
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
