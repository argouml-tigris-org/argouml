package uci.uml.ui;

import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;

import uci.gef.Diagram;
import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;

public class GoDiagramToEdge implements TreeModelPrereqs {

  public Object getRoot() {
    System.out.println("getRoot should never be called");
    return null;
  } 
  
  public Object getChild(Object parent, int index) {
    if (parent instanceof Diagram) {
      Vector edges = ((Diagram)parent).getGraphModel().getEdges();
      return edges.elementAt(index);
    }
    System.out.println("getChild should never be get here GoModelToElements");
    return null;
  }
  
  public int getChildCount(Object parent) {
    if (parent instanceof Diagram) {
      Vector edges = ((Diagram) parent).getGraphModel().getEdges();
      return (edges == null) ? 0 : edges.size();
    }
    return 0;
  }
  
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof Diagram) {
      Vector edges = ((Diagram)child).getGraphModel().getEdges();
      if (edges.contains(child)) return edges.indexOf(child);
    }
    return -1;
  }

  public boolean isLeaf(Object node) {
    return !(node instanceof Diagram && getChildCount(node) > 0);
  }
  
  public void valueForPathChanged(TreePath path, Object newValue) { }
  public void addTreeModelListener(TreeModelListener l) { }
  public void removeTreeModelListener(TreeModelListener l) { }

  public Vector getPrereqs() {
    Vector pros = new Vector();
    pros.addElement(uci.gef.Diagram.class);
    return pros;
  }
  public Vector getProvidedTypes() {
    Vector pros = new Vector();
    pros.addElement(uci.uml.Foundation.Core.ModelElement.class);
    return pros;
  }


}
