package uci.graph;

import java.beans.*;

public interface GraphPortHooks {

  /** Reply true if this port can legally be connected to the given
   *  port. Subclasses may implement this to reflect application
   *  specific connection constraints. By default, each port just
   *  defers that decision to its parent NetNode. By convention, your
   *  implementation should return false if super.canConnectTo() would
   *  return false (i.e., deeper subclasses get more constrained). I
   *  don't know if that is a good convention. */
  boolean canConnectTo(GraphModel gm, Object anotherPort);


  /** Application specific hook that is called after a successful
   *  connection. */
  void postConnect(GraphModel gm, Object edge);

  /** Application specific hook that is called after a
   *  disconnection. (for now, all disconnections are assumed
   *  legal). */
  void postDisconnect(GraphModel gm, Object edge);

  void addPropertyChangeListener(PropertyChangeListener l);
  void removePropertyChangeListener(PropertyChangeListener l);
  void setHighlight(boolean b);
  void dispose();


} /* end interface GraphPortHooks */
