package uci.uml.ui;

import java.util.*;
import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;


/** Project -> (Diagram or (Model -> Element -> Feature)) */
// needs-more-work: what about pseudo nodes for types of elements and features?
// needs-more-work: nested classes: a class should be treated as a namespace


public class NavDiagramCentric extends NavPerspective {
  public NavDiagramCentric() {
    addSubTreeModel(new GoProjectDiagram());
    addSubTreeModel(new GoDiagramToNode());
    addSubTreeModel(new GoDiagramToEdge());
    addSubTreeModel(new GoClassifierToBeh());
    addSubTreeModel(new GoClassifierToStr());
  }

  public String toString() { return "Diagram-centric"; }

} /* end class NavDiagramCentric */
