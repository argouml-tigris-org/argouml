package uci.uml.ui;

import java.util.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.util.*;
import uci.argo.kernel.*;


//implements ToDoListListener

public class ToDoByPriority extends ToDoPerspective {

  protected void computePseudoNodes() {
    super.computePseudoNodes();
    ToDoList list = Designer.TheDesigner.getToDoList();
    Predicate predHigh = new PredicatePriority(ToDoItem.HIGH_PRIORITY);
    Predicate predMed = new PredicatePriority(ToDoItem.MED_PRIORITY);
    Predicate predLow = new PredicatePriority(ToDoItem.LOW_PRIORITY);
    _pseudoNodes.addElement(new ToDoPseudoNode(list, predHigh));
    _pseudoNodes.addElement(new ToDoPseudoNode(list, predMed));
    _pseudoNodes.addElement(new ToDoPseudoNode(list, predLow));
  }
  
} /* end class ToDoByPriority */


