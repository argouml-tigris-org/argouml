
package uci.argo.kernel;

import java.util.*;

// needs-more-work: how can this possibly be persistent?
// needs-more-work: provide accessors
// needs-more-work: define subclasses for: modification, criticism

public class HistoryItemManipulation extends HistoryItem {
  
  ////////////////////////////////////////////////////////////////
  // constructors

  public HistoryItemManipulation(String desc) {
    super(desc);
  }

  public HistoryItemManipulation(String desc, Object target,
		     Object oldValue, Object newValue) {
    super(desc, target, oldValue, newValue);
  }


  ////////////////////////////////////////////////////////////////
  // debugging

  public String toString() {
    if (_desc == null) return "HIM: (null)";
    return "HIM: " + _desc;
  }
  
} /* end class HistoryItemManipulation */
