package uci.uml.ui;

import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;

public class GoGenElementToDerived implements TreeModel {

  public Object getRoot() {
    System.out.println("getRoot should never be called");
    return null;
  } 

  public Object getChild(Object parent, int index) {
    if (parent instanceof GeneralizableElement) {
      GeneralizableElement p = (GeneralizableElement) parent;
      Generalization g = (Generalization) p.getSpecialization().elementAt(index);
      return g.getSubtype();
    }
    System.out.println("getChild should never be get here GoClassifierToStr");
    return null;
  }
  
  public int getChildCount(Object parent) {
    if (parent instanceof GeneralizableElement) {
      GeneralizableElement p = (GeneralizableElement) parent;
      Vector specs = p.getSpecialization();
      return (specs == null) ? 0 : specs.size();
    }
    return 0;
  }
  
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof GeneralizableElement) {
      GeneralizableElement p = (GeneralizableElement) parent;
      Vector specs = p.getSpecialization();
      Vector derived = new Vector();
      java.util.Enumeration specEnum = specs.elements();
      while (specEnum.hasMoreElements()) {
	Generalization g = (Generalization) specEnum.nextElement();
	derived.addElement(g.getSubtype());
	// needs-more-work: it would be better to just count and
	// return on first match
      }
      if (derived.contains(child)) return derived.indexOf(child);
    }
    return -1;
  }

  public boolean isLeaf(Object node) {
    return !(node instanceof GeneralizableElement && getChildCount(node) > 0);
  }

  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }

}
