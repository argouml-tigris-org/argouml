// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


package uci.uml.ui;

import java.util.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;
import uci.util.*;
import uci.argo.kernel.*;


public class ToDoByDecision extends ToDoPerspective
implements ToDoListListener {


  public ToDoByDecision() {
    super("By Decision");
    addSubTreeModel(new GoListToDecisionsToItems());
  }

  //public String toString() { return "Decision"; }

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


