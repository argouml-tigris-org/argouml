package uci.uml.ui;

import java.util.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.util.*;
import uci.argo.kernel.*;


public class ToDoByGoal extends ToDoPerspective
implements ToDoListListener {

//   protected void computePseudoNodes() {
//     super.computePseudoNodes();
//     ToDoList list = Designer.TheDesigner.getToDoList();
//     Vector goals = list.getGoals();     // should be from decision model
//     _pseudoNodes = new Vector(goals.size());
//     java.util.Enumeration enum = goals.elements();
//     while (enum.hasMoreElements()) {
//       Predicate pred = new PredicateGoal((Goal)enum.nextElement());
//       _pseudoNodes.addElement(new ToDoPseudoNode(list, pred));
//     }
//   }

  public ToDoByGoal() {
    addSubTreeModel(new GoListToGoalsToItems());
  }

  public String toString() { return "Goal"; }

  ////////////////////////////////////////////////////////////////
  // ToDoListListener implementation

  public void toDoItemAdded(ToDoListEvent tde) {
    System.out.println("toDoItemAdded");
    ToDoItem item = tde.getToDoItem();
    Object path[] = new Object[2];
    path[0] = Designer.TheDesigner.getToDoList();
    int childIndices[] = new int[1];
    Object children[] = new Object[1];

    Poster post = item.getPoster();
    java.util.Enumeration enum = post.getSupportedGoals().elements();
    while (enum.hasMoreElements()) {
      Goal g = (Goal) enum.nextElement();
      path[1] = g;
      System.out.println("toDoItemAdded firing new item!");
      childIndices[0] = getIndexOfChild(g, item);
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

    Poster post = item.getPoster();
    java.util.Enumeration enum = post.getSupportedGoals().elements();
    while (enum.hasMoreElements()) {
      Goal g = (Goal) enum.nextElement();
      System.out.println("toDoItemRemoved updating decision node!");
      path[1] = g;
      //fireTreeNodesChanged(this, path, childIndices, children);
      fireTreeStructureChanged(path);
    }
  }

  public void toDoListChanged(ToDoListEvent tde) { }


} /* end class ToDoByGoal */


