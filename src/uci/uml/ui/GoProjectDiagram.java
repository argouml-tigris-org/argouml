package uci.uml.ui;

import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;

public class GoProjectDiagram implements TreeModelPrereqs {

  public Object getRoot() {
    System.out.println("getRoot should never be called");
    return null;
  } 

  public Object getChild(Object parent, int index) {
    if (parent instanceof Project) {
      return ((Project)parent).getDiagrams().elementAt(index);
    }
    System.out.println("getChild should never get here GoProjectDiagram");
    return null;
  }
  
  public int getChildCount(Object parent) {
    if (parent instanceof Project) {
      return ((Project) parent).getDiagrams().size();
    }
    return 0;
  }
  
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof Project) {
      Vector diagrams = ((Project)parent).getDiagrams();
      if (diagrams.contains(child)) return diagrams.indexOf(child);
    }
    return -1;
  }


  public boolean isLeaf(Object node) {
    // only for now
    return !(node instanceof Project && getChildCount(node) > 0);
  }

  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }

  public Vector getPrereqs() { return new Vector(); }
  public Vector getProvidedTypes() {
    Vector pros = new Vector();
    pros.addElement(uci.gef.Diagram.class);
    return pros;
  }

}
