package uci.uml.ui;

import java.util.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.util.*;
import uci.argo.kernel.*;


//implements ToDoListListener

public class ToDoByPriority extends ToDoPerspective
implements ToDoListListener {

  public ToDoByPriority() {
    addSubTreeModel(new GoListToPriorityToItem());
  }
  
  public String toString() { return "Priority"; }

  ////////////////////////////////////////////////////////////////
  // ToDoListListener implementation

  public void toDoItemAdded(ToDoListEvent tde) {
    System.out.println("toDoItemAdded");
    ToDoItem item = tde.getToDoItem();
    Object path[] = new Object[2];
    path[0] = Designer.TheDesigner.getToDoList();
    int childIndices[] = new int[1];
    Object children[] = new Object[1];

    int pri = item.getPriority();
    java.util.Enumeration enum = PriorityNode.getPriorities().elements();
    while (enum.hasMoreElements()) {
      PriorityNode pn = (PriorityNode) enum.nextElement();
      if (pri != pn.getPriority()) continue;
      path[1] = pn;
      System.out.println("toDoItemAdded firing new item!");
      childIndices[0] = getIndexOfChild(pn, item);
      children[0] = item;
      fireTreeNodesInserted(this, path, childIndices, children);
    }
  }

  public void toDoItemRemoved(ToDoListEvent tde) {
    System.out.println("toDoItemAdded");
    ToDoList list = Designer.TheDesigner.getToDoList(); //source?
    ToDoItem item = tde.getToDoItem();
    Object path[] = new Object[2];
    path[0] = Designer.TheDesigner.getToDoList();

    int pri = item.getPriority();
    java.util.Enumeration enum = PriorityNode.getPriorities().elements();
    while (enum.hasMoreElements()) {
      PriorityNode pn = (PriorityNode) enum.nextElement();
      if (pri != pn.getPriority()) continue;      
      System.out.println("toDoItemRemoved updating PriorityNode");
      path[1] = pn;
      //fireTreeNodesChanged(this, path, childIndices, children);
      fireTreeStructureChanged(path);
    }
  }

  public void toDoListChanged(ToDoListEvent tde) { }


  
//   protected void computePseudoNodes() {
//     super.computePseudoNodes();
//     ToDoList list = Designer.TheDesigner.getToDoList();
//     Predicate predHigh = new PredicatePriority(ToDoItem.HIGH_PRIORITY);
//     Predicate predMed = new PredicatePriority(ToDoItem.MED_PRIORITY);
//     Predicate predLow = new PredicatePriority(ToDoItem.LOW_PRIORITY);
//     _pseudoNodes.addElement(new ToDoPseudoNode(list, predHigh));
//     _pseudoNodes.addElement(new ToDoPseudoNode(list, predMed));
//     _pseudoNodes.addElement(new ToDoPseudoNode(list, predLow));
//   }
  

} /* end class ToDoByPriority */


