package uci.uml.ui;

import java.util.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.util.*;
import uci.argo.kernel.*;


public class ToDoByDecision extends ToDoPerspective {

  protected void computePseudoNodes() {
    super.computePseudoNodes();
    ToDoList list = Designer.TheDesigner.getToDoList();
    Vector decs = list.getDecisions();     // should be from decision model
    _pseudoNodes = new Vector(decs.size());
    java.util.Enumeration enum = decs.elements();
    while (enum.hasMoreElements()) {
      Predicate pred = new PredicateDecision((Decision)enum.nextElement());
      _pseudoNodes.addElement(new ToDoPseudoNode(list, pred));
    }
  }
  
} /* end class ToDoByDecision */


