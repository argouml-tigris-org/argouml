
package uci.argo.kernel;

import java.util.*;

// needs-more-work: how can this possibly be persistent?
// needs-more-work: provide accessors
// needs-more-work: define subclasses for: modification, criticism

public class HistoryItemCritique extends HistoryItem {
  
  ////////////////////////////////////////////////////////////////
  // constructors

  public HistoryItemCritique(ToDoItem item) {
    super(item, "Criticism raised: ");
  }


  ////////////////////////////////////////////////////////////////
  // debugging

  public String toString() {
    if (_desc == null) return "HIC: (null)";
    return "HIC: " + _desc;
  }
  
} /* end class HistoryItemCritique */
