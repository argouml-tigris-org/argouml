
package uci.argo.kernel;

import java.util.*;

// needs-more-work: how can this possibly be persistent?
// needs-more-work: provide accessors
// needs-more-work: define subclasses for: modification, criticism

public class HistoryItemResolve extends HistoryItem {

  ////////////////////////////////////////////////////////////////
  // instance variables
  String _reason;
  
  ////////////////////////////////////////////////////////////////
  // constructors

  public HistoryItemResolve(ToDoItem item) {
    super(item, "Criticism resolved: ");
  }

  public HistoryItemResolve(ToDoItem item, String reason) {
    super(item, "Criticism resolved: ");
    _reason = reason;
  }


  ////////////////////////////////////////////////////////////////
  // debugging

  public String toString() {
    if (_desc == null) return "HIC: (null)";
    return "HIR: " + _desc;
  }
  
} /* end class HistoryItemResolve */
