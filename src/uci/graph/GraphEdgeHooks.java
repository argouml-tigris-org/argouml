package uci.graph;

import java.beans.*;

public interface GraphEdgeHooks {
  
  void addPropertyChangeListener(PropertyChangeListener l);
  void removePropertyChangeListener(PropertyChangeListener l);
  void setHighlight(boolean b);
  void dispose();
  
} /* end interface GraphEdgeHooks */
