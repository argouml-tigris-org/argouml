// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.




package uci.graph;

import java.util.*;
import java.beans.*;
import uci.gef.Editor;

/** A set of methods that nodes in a GraphModel may implement.  If the
 *  objects you use to represent nodes implement this interface, they
 *  will get the appropriate calls. NetNode implements these.
 *
 * @see uci.gef.NetNode */
public interface GraphNodeHooks extends java.io.Serializable{

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
  
  static final long serialVersionUID = 6249493911977021522L;
} /* end interface GraphNodeHooks */
