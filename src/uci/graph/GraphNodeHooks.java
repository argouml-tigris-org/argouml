package uci.graph;

import java.util.*;
import java.beans.*;
import uci.gef.Editor;

public interface GraphNodeHooks {

  /** Do some application specific action just after this node is
   *  connected to another node. the arguments contain some info about
   *  what ports were connected. */
  void postConnect(GraphModel gm, Object anotherNode,
		   Object myPort, Object otherPort);

  /** Do some application specific action just after this node is
   *  disconnected from another node. the arguments contain some info
   *  about what ports were connected. */
  void postDisconnect(GraphModel gm, Object anotherNode,
		      Object myPort, Object otherPort);

  ////////////////////////////////////////////////////////////////
  // net-level constraints

  /** Allow foir application specific rules about which nodes can be
   *  connected to which other nodes. This is called from the NetPort,
   *  so the port has first say as to whether it can be connected to
   *  some other port. NetPort.canConnectTo() just calls
   *  NetNode.canConnectTo(). By default anything can be connected to
   *  anything. */
  boolean canConnectTo(GraphModel gm, Object otherNode,
		       Object otherPort, Object myPort);

  ////////////////////////////////////////////////////////////////
  // diagram-level hooks

  /** Do some application specific actions after the node is placed in
   *  a drawing area. */
  void postPlacement(Editor ed);

  void addPropertyChangeListener(PropertyChangeListener l);
  void removePropertyChangeListener(PropertyChangeListener l);

  void initialize(Hashtable props);
  void setHighlight(boolean b);
  void dispose();
  
} /* end interface GraphNodeHooks */
