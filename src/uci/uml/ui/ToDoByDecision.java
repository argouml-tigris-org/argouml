package uci.uml.ui;

import java.util.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.util.*;
import uci.argo.kernel.*;


public class ToDoByDecision extends ToDoPerspective
implements ToDoListListener {


  public ToDoByDecision() {
    addSubTreeModel(new GoListToDecisionsToItems());
  }

  public String toString() { return "Decision"; }

  ////////////////////////////////////////////////////////////////
  // DecisionModelListener implementation

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
    java.util.Enumeration enum = post.getSupportedDecisions().elements();
    while (enum.hasMoreElements()) {
      Decision dec = (Decision) enum.nextElement();
      path[1] = dec;
      System.out.println("toDoItemAdded firing new item!");
      childIndices[0] = getIndexOfChild(dec, item);
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
    java.util.Enumeration enum = post.getSupportedDecisions().elements();
    while (enum.hasMoreElements()) {
      Decision dec = (Decision) enum.nextElement();
      System.out.println("toDoItemRemoved updating decision node!");
      path[1] = dec;
      //fireTreeNodesChanged(this, path, childIndices, children);
      fireTreeStructureChanged(path);
    }
  }

  public void toDoListChanged(ToDoListEvent tde) { }

  
//   public static Decision decisionUNCATEGORIZED =
//   new Decision("Uncategorized", 1);
  
//   protected boolean isNeeded(ToDoPseudoNode node) {
//     PredicateDecision pd = (PredicateDecision) node.getPredicate();
//     Decision d = pd.getDecision();
//     java.util.Enumeration items = _root.elements();
//     while (items.hasMoreElements()) {
//       ToDoItem item = (ToDoItem) items.nextElement();
//       if (item.getPoster().supports(d)) return true;
//     }
//     return false;
//   }

  
//   protected Vector addNewPseudoNodes(ToDoItem item) {
//     Vector newNodes = new Vector();
//     Vector decs = item.getPoster().getSupportedDecisions();
//     if (decs == null) {
//       addNodeIfNeeded(Decision.UNSPEC, newNodes);
//     }
//     else {
//       java.util.Enumeration enum = decs.elements();
//       while (enum.hasMoreElements()) {
// 	Decision itemDec = (Decision) enum.nextElement();
// 	addNodeIfNeeded(itemDec, newNodes);
//       }
//     }
//     return newNodes;
//   }


//   protected void addNodeIfNeeded(Decision itemDec, Vector newNodes) {
//     java.util.Enumeration enum2 = _pseudoNodes.elements();
//     while (enum2.hasMoreElements()) {
//       ToDoPseudoNode node = (ToDoPseudoNode) enum2.nextElement();
//       PredicateDecision pd = (PredicateDecision) node.getPredicate();
//       Decision nodeDec = pd.getDecision();
//       //if (nodeDec.getName().equals(itemDec.getName())) return;
//       if (nodeDec == itemDec) return;
//     }
//     PredicateDecision pred = new PredicateDecision(itemDec);
//     ToDoPseudoNode newNode = new ToDoPseudoNode(_root, pred);
//     newNode.setLabel(itemDec.getName());
//     _pseudoNodes.addElement(newNode);
//     newNodes.addElement(newNode);
//   }

} /* end class ToDoByDecision */


