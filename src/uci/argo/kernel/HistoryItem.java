
package uci.argo.kernel;

import java.util.*;

// needs-more-work: how can this possibly be persistent?
// needs-more-work: provide accessors
// needs-more-work: define subclasses for: modification, criticism

public class HistoryItem {
  
  ////////////////////////////////////////////////////////////////
  // instance variables
  public Date _when;
  public Designer _who;
  public String _desc;
  public Object _target;
  public Object _oldValue;
  public Object _newValue;
  public Vector _relatedItems;

  ////////////////////////////////////////////////////////////////
  // constructors

  public HistoryItem(String desc) {
    _when = new Date(); // right now
    _who = Designer.TheDesigner;
    _desc = desc;    
  }
  
  public HistoryItem(String desc, Object target,
		     Object oldValue, Object newValue) {
    _when = new Date(); // right now
    _who = Designer.TheDesigner;
    _desc = desc;
    _target = target;
    _oldValue = oldValue;
    _newValue = newValue;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public Vector getRelatedItems() { return _relatedItems; }
  public void setRelatedItems(Vector v) { _relatedItems = v; }

  ////////////////////////////////////////////////////////////////
  // debugging

  public String toString() {
    if (_desc == null) return "HI: (null)";
    return "HI: " + _desc;
  }
  
} /* end class HistoryItem */
