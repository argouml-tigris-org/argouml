package uci.uml.ui;

import java.util.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.util.*;
import uci.argo.kernel.*;


public class ToDoByGoal extends ToDoPerspective {

  protected void computePseudoNodes() {
    super.computePseudoNodes();
    ToDoList list = Designer.TheDesigner.getToDoList();
    Vector goals = list.getGoals();     // should be from decision model
    _pseudoNodes = new Vector(goals.size());
    java.util.Enumeration enum = goals.elements();
    while (enum.hasMoreElements()) {
      Predicate pred = new PredicateGoal((Goal)enum.nextElement());
      _pseudoNodes.addElement(new ToDoPseudoNode(list, pred));
    }
  }
  
} /* end class ToDoByGoal */


