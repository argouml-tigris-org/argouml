package uci.uml.ui;

import java.util.*;
import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;


/** Project -> (Diagram or (Model -> Element -> Feature)) */
// needs-more-work: what about pseudo nodes for types of elements and features?
// needs-more-work: nested classes: a class should be treated as a namespace


public class NavStrictContainment extends NavPerspective {
  public NavStrictContainment() { }

  public Object getChild(Object parent, int index) {
    //System.out.println("NavM_DE_F getChild");
    if (parent instanceof Project) {
      Vector diagrams = ((Project)parent).getDiagrams();
      Vector models = ((Project)parent).getModels();
      if (index < diagrams.size()) return diagrams.elementAt(index);
      else return models.elementAt(index - diagrams.size());
    }
    else if (parent instanceof Package) {
      //pseudo nodes for types of elements?
      Vector refs = ((Package)parent).getOwnedElement();
      ElementOwnership eo = (ElementOwnership) refs.elementAt(index);
      return eo.getModelElement();
    }
    else if (parent instanceof Classifier) {
      Classifier c = (Classifier) parent;
      Vector beh = c.getBehavioralFeature();
      Vector str = c.getStructuralFeature();
      int behSize = (beh == null ? 0 : beh.size());
      int strSize = (str == null ? 0 : str.size());
      if (index < behSize) return beh.elementAt(index);
      else return str.elementAt(index - behSize);
    }
    //states
    //use cases
    else return super.getChild(parent, index);
  }
  
  public int getChildCount(Object parent) {
    //System.out.println("NavM_DE_F getChildCount");
    if (parent instanceof Project) {
      Project p = (Project) parent;
      return p.getDiagrams().size() + p.getModels().size();
    }
    else if (parent instanceof Package) {
      Vector refs = ((Package)parent).getOwnedElement();
      return (refs == null ? 0 : refs.size());
    }
    else if (parent instanceof Classifier) {
      Classifier c = (Classifier) parent;
      Vector beh = c.getBehavioralFeature();
      Vector str = c.getStructuralFeature();
      int behSize = (beh == null ? 0 : beh.size());
      int strSize = (str == null ? 0 : str.size());
      return behSize + strSize;
    }
    //states
    //use cases
    else return super.getChildCount(parent);
  }
  
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof Project) {
      Project p = (Project) parent;
      Vector diagrams = p.getDiagrams();
      Vector models = p.getModels();
      if (diagrams.contains(child)) return diagrams.indexOf(child);
      else return diagrams.size() + models.indexOf(child);
    }
    else if (parent instanceof Package) {
      Vector refs = ((Package)parent).getOwnedElement();
      return refs.indexOf(child);
    }
    else if (parent instanceof Classifier) {
      Classifier c = (Classifier) parent;
      Vector beh = c.getBehavioralFeature();
      Vector str = c.getStructuralFeature();
      int behSize = (beh == null ? 0 : beh.size());
      int strSize = (str == null ? 0 : str.size());
      if (beh.contains(child)) return beh.indexOf(child);
      else return behSize + str.indexOf(child);
    }
    //states
    //use cases
    else return super.getIndexOfChild(parent, child);
  }
  
  public boolean isLeaf(Object node) {
    //if (node == null) System.out.println("NavM_DE_F isLeaf null");
    //else System.out.println("NavM_DE_F isLeaf" + node.toString());
    if (node instanceof Project) return false;
    else if (node instanceof Package) return false;
    else if (node instanceof Classifier) return false;
    // states
    // use cases
    else return super.isLeaf(node);
    //attributes ok?
  }

  
} /* end class NavStrictContainment */
