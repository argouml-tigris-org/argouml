package uci.uml.ui;

import java.util.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.util.*;
import uci.argo.kernel.*;


public class ToDoByOffender extends ToDoPerspective {
  protected void computePseudoNodes() {
    super.computePseudoNodes();
    ToDoList list = Designer.TheDesigner.getToDoList();
    Vector offenders = list.getOffenders();
    java.util.Enumeration offEnum = offenders.elements();
    while (offEnum.hasMoreElements()) {
      Predicate predOff = new PredicateOffender(offEnum.nextElement());
      ToDoPseudoNode pn = new ToDoPseudoNode(list, predOff);
      _pseudoNodes.addElement(pn);
    }
  }
  
} /* end class ToDoByOffender */


