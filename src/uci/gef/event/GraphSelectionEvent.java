package uci.gef.event;

import java.util.*;

public class GraphSelectionEvent extends EventObject {

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Vector _selections;

  ////////////////////////////////////////////////////////////////
  // constructor
  public GraphSelectionEvent(Object src, Vector selections) {
    super(src);
    _selections = selections;
  }

  ////////////////////////////////////////////////////////////////
  // accessors
  public Vector getSelections() { return _selections; }
  
} /* end class GraphSelectionEvent */
