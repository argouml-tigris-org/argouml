package uci.uml.ui;

import java.util.*;

import uci.argo.kernel.*;

public class PriorityNode {

  ////////////////////////////////////////////////////////////////
  // static variables and methods
  protected static Vector _PRIORITIES = null;

  public static Vector getPriorities() {
    if (_PRIORITIES == null) {
      _PRIORITIES = new Vector();
      _PRIORITIES.addElement(new PriorityNode("High", ToDoItem.HIGH_PRIORITY));
      _PRIORITIES.addElement(new PriorityNode("Medium", ToDoItem.MED_PRIORITY));
      _PRIORITIES.addElement(new PriorityNode("Low", ToDoItem.LOW_PRIORITY));
    }
    return _PRIORITIES;
  }


  ////////////////////////////////////////////////////////////////
  // instance variables

  protected String _name;
  protected int _priority;
  
  ////////////////////////////////////////////////////////////////
  // contrsuctors

  public PriorityNode(String name, int pri) {
    _name = name;
    _priority = pri;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public String getName() { return _name; }
  public int getPriority() { return _priority; }

  public String toString() { return getName(); }
  
} /* end class PriorityNode */
