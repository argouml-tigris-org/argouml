package uci.uml.ui;

import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;

public class GoClassifierToStr implements TreeModelPrereqs {

  public Object getRoot() {
    System.out.println("getRoot should never be called");
    return null;
  } 
  public void setRoot(Object r) { }

  public Object getChild(Object parent, int index) {
    if (parent instanceof Classifier) {
      return ((Classifier)parent).getStructuralFeature().elementAt(index);
    }
    System.out.println("getChild should never be get here GoClassifierToStr");
    return null;
  }
  
  public int getChildCount(Object parent) {
    if (parent instanceof Classifier) {
      Vector str = ((Classifier) parent).getStructuralFeature();
      return (str == null) ? 0 : str.size();
    }
    return 0;
  }
  
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof Classifier) {
      Vector str = ((Classifier)parent).getStructuralFeature();
      if (str.contains(child)) return str.indexOf(child);
    }
    return -1;
  }

  public boolean isLeaf(Object node) {
    return !(node instanceof Classifier && getChildCount(node) > 0);
  }

  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }

  public Vector getPrereqs() {
    Vector pros = new Vector();
    pros.addElement(uci.uml.Foundation.Core.ModelElement.class);
    return pros;
  }
  public Vector getProvidedTypes() {
    Vector pros = new Vector();
    pros.addElement(uci.uml.Foundation.Core.StructuralFeature.class);
    return pros;
  }

  
}
