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




// File: DefaultGraphModel.java
// Interfaces: DefaultGraphModel
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.graph;

import java.util.*;
import uci.gef.NetList;
import uci.gef.NetNode;
import uci.gef.NetPort;
import uci.gef.NetEdge;

/** An abstract class that makes it easier to implement your own
 *  version of MutableGraphModel. This class basically includes the
 *  code for event notifications, so that you don't have to write
 *  that.  It also provides a few utility methods.
 *
 * @see AdjacencyMatrixGraphModel
 * @see uci.graph.demo.WordTransforms */

public abstract class MutableGraphSupport
implements MutableGraphModel, java.io.Serializable {

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected Vector _graphListeners;

  ////////////////////////////////////////////////////////////////
  // constructors
  public MutableGraphSupport() { }

  ////////////////////////////////////////////////////////////////
  // accessors

  public Vector getGraphListeners() { return _graphListeners; }


  ////////////////////////////////////////////////////////////////
  // MutableGraphModel implementation

  /** Return true if the two given ports can be connected by the given
   *  kind of edge. By default ignore edgeClass and call
   *  canConnect(port,port). */
  public boolean canConnect(Object fromPort, Object toPort, Class edgeClass) {
    return canConnect(fromPort, toPort);
  }

  /** Contruct and add a new edge of the given kind. By default ignore
   *  edgeClass and call connect(port,port). */
  public Object connect(Object fromPort, Object toPort, Class edgeClass) {
    return connect(fromPort, toPort);
  }

  ////////////////////////////////////////////////////////////////
  // utility methods

  public boolean containsNode(Object node) {
    Vector nodes = getNodes();
    return nodes.contains(node);
  }

  public boolean containsEdge(Object edge) {
    Vector edges = getEdges();
    return edges.contains(edge);
  }

  public boolean containsNodePort(Object port) {
    Vector nodes = getNodes();
    if (nodes == null) return false;
    Enumeration nodeEnum = nodes.elements();
    while (nodeEnum.hasMoreElements()) {
      Object node = nodeEnum.nextElement();
      Vector ports = getPorts(node);
      if (ports != null && ports.contains(port)) return true;
    }
    return false;
  }

  public boolean containsEdgePort(Object port) {
    Vector edges = getNodes();
    if (edges == null) return false;
    Enumeration edgeEnum = edges.elements();
    while (edgeEnum.hasMoreElements()) {
      Object edge = edgeEnum.nextElement();
      Vector ports = getPorts(edge);
      if (ports != null && ports.contains(port)) return true;
    }
    return false;
  }

  public boolean containsPort(Object port) {
    return containsNodePort(port) || containsEdgePort(port);
  }

  ////////////////////////////////////////////////////////////////
  // listener registration

  public void addGraphEventListener(GraphListener listener) {
    if (_graphListeners == null) _graphListeners = new Vector();
    _graphListeners.addElement(listener);
  }
  public void removeGraphEventListener(GraphListener listener) {
    if (_graphListeners == null) return;
    _graphListeners.removeElement(listener);
  }

  ////////////////////////////////////////////////////////////////
  // event notifications

  public void fireNodeAdded(Object node) {
    if (_graphListeners == null) return;
    GraphEvent ge = new GraphEvent(this, node);
    Enumeration listeners = _graphListeners.elements();
    while (listeners.hasMoreElements()) {
      GraphListener listen = (GraphListener) listeners.nextElement();
      listen.nodeAdded(ge);
    }
  }

  public void fireNodeRemoved(Object node) {
    if (_graphListeners == null) return;
    GraphEvent ge = new GraphEvent(this, node);
    Enumeration listeners = _graphListeners.elements();
    while (listeners.hasMoreElements()) {
      GraphListener listen = (GraphListener) listeners.nextElement();
      listen.nodeRemoved(ge);
    }
  }

  public void fireEdgeAdded(Object edge) {
    if (_graphListeners == null) return;
    GraphEvent ge = new GraphEvent(this, edge);
    Enumeration listeners = _graphListeners.elements();
    while (listeners.hasMoreElements()) {
      GraphListener listen = (GraphListener) listeners.nextElement();
      listen.edgeAdded(ge);
    }
  }

  public void fireEdgeRemoved(Object edge) {
    if (_graphListeners == null) return;
    GraphEvent ge = new GraphEvent(this, edge);
    Enumeration listeners = _graphListeners.elements();
    while (listeners.hasMoreElements()) {
      GraphListener listen = (GraphListener) listeners.nextElement();
      listen.edgeRemoved(ge);
    }
  }

  public void fireGraphChanged() {
    if (_graphListeners == null) return;
    GraphEvent ge = new GraphEvent(this, null);
    Enumeration listeners = _graphListeners.elements();
    while (listeners.hasMoreElements()) {
      GraphListener listen = (GraphListener) listeners.nextElement();
      listen.graphChanged(ge);
    }
  }

  static final long serialVersionUID = -6282336117485054663L;
} /* end class MutableGraphSupport */
