
package uci.uml.ui;

import java.util.*;
//import com.sun.java.swinq.ImageIcon;

import uci.argo.kernel.*;
import uci.util.*;


class ToDoPseudoNode {
  protected ToDoList _list;
  protected Vector _filteredItems = null;
  protected Predicate _pred;
  protected String _label; //needs-more-work 
  //protected ImageIcon _icon; //needs-more-work

  public ToDoPseudoNode(ToDoList list, Predicate p) {
    _list = list;
    _pred = p;
  }
  
  public ToDoList getToDoList() { return _list; }

  public Vector getToDoItems() {
    if (_filteredItems == null) computeItems();
    return _filteredItems;
  }

  public void computeItems() {
    Vector rawItems = _list.getToDoItems();
    if (_filteredItems == null) _filteredItems = new Vector(rawItems.size());
    else _filteredItems.removeAllElements();
    Enumeration enum = rawItems.elements();
    while (enum.hasMoreElements()) {
      ToDoItem item = (ToDoItem) enum.nextElement();
      if (_pred.predicate(item)) _filteredItems.addElement(item);
    }
  }

  //display logic
  
} /* end class ToDoPseudoNode */


class PredicatePriority implements uci.util.Predicate {
  protected int _priority;
  
  public PredicatePriority(int pri) { _priority = pri; }
  
  public boolean predicate(Object o) {
    if (!(o instanceof ToDoItem)) return false;
    ToDoItem item = (ToDoItem) o;
    return item.getPriority() == _priority;
  }
} /* end class PredicatePriority */



class PredicateDecision implements uci.util.Predicate {
  protected Decision _decision;
  
  public PredicateDecision(Decision d) { _decision = d; }
  
  public boolean predicate(Object o) {
    if (!(o instanceof ToDoItem)) return false;
    ToDoItem item = (ToDoItem) o;
    return item.getPoster().supports(_decision);
  }
} /* end class PredicateDecision */



class PredicateGoal implements uci.util.Predicate {
  protected Goal _goal;
  
  public PredicateGoal(Goal g) { _goal = g; }
  
  public boolean predicate(Object o) {
    if (!(o instanceof ToDoItem)) return false;
    ToDoItem item = (ToDoItem) o;
    return item.getPoster().supports(_goal);
  }
} /* end class PredicateGoal */



class PredicateOffender implements uci.util.Predicate {
  protected Object _offender;
  
  public PredicateOffender(Object offender) { _offender = offender; }
  
  public boolean predicate(Object o) {
    if (!(o instanceof ToDoItem)) return false;
    ToDoItem item = (ToDoItem) o;
    return item.getOffenders().contains(_offender);
  }
} /* end class PredicateOffender */



class PredicateType implements uci.util.Predicate {
  protected int _knowledgeType;
  
  public PredicateType(int type) { _knowledgeType = type; }
  public PredicateType(Integer type) { _knowledgeType = type.intValue(); }
  
  public boolean predicate(Object o) {
    if (!(o instanceof ToDoItem)) return false;
    ToDoItem item = (ToDoItem) o;
    return item.getPoster().includesKnowledgeType(_knowledgeType);
  }
} /* end class PredicateType */



class PredicatePoster implements uci.util.Predicate {
  protected Poster _poster;
  
  public PredicatePoster(Poster p) { _poster = p; }
  
  public boolean predicate(Object o) {
    if (!(o instanceof ToDoItem)) return false;
    ToDoItem item = (ToDoItem) o;
    return item.getPoster() == _poster;
  }
} /* end class PredicatePoster */



