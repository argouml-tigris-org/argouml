package uci.gef.event;

import java.util.*;

public interface GraphSelectionListener extends EventListener {

  void selectionChanged(GraphSelectionEvent gse);
  //? void selectionAdded(GraphSelectionEvent gse);
  //? void selectionRemoved(GraphSelectionEvent gse);

} /* end class GraphSelectionListener */
