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




// File: NetNode.java
// Classes: NetNode
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.beans.*;

import uci.graph.*;

/** This class models a node in our underlying connected graph
 *  model. Nodes have ports that are their connection points to other
 *  nodes. This class is used by DefaultGraphModel, if you implement
 *  your own GraphModel, you can use your own application-specific
 *  objects as nodes. Needs-more-work: this should probably move to
 *  package uci.graph.
 *
 * @see NetEdge
 * @see NetPort */

public abstract class NetNode extends NetPrimitive
implements GraphNodeHooks, java.io.Serializable  {
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** An array of the ports on this node */
  protected Vector _ports;

  ////////////////////////////////////////////////////////////////
  // constructors and related methods

  /** Construct a new node from the given default node and number of
   *  ports. The attributes of the default node will be used if they
   *  are not overridden in this node (i.e., nodes have attributes and
   *  there is a virual copy relationship between some nodes). */
  public NetNode(NetNode deft, Vector ports) { _ports = ports; }

  /** Construct a new NetNode with no default attributes and no ports. */
  public NetNode() { this(null, new Vector()); }

  /** Usually when nodes are created it is deon through newInstance
   *  and there is no chance to supply a default node or to connect
   *  this node to some other application level object. So after a
   *  node is constructed initialize is called to supply that
   *  information. <p>
   *
   * Needs-More-Work: what is the class protocol design here? */
  public abstract void initialize(Hashtable args);

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Returns the attribute table of the node. */
  public Object getAttributes() { return null; }

  /** reply my NetPort with the given index. */
  public NetPort getPort(int i) { return (NetPort) _ports.elementAt(i); }

  /** reply my NetPorts. */
  public Vector getPorts() { return _ports; }
  public void setPorts(Vector ports) { _ports = ports; }
  public void addPort(NetPort p) { _ports.addElement(p); }


  /** Remove this node from the underling connected graph model. */
  public void dispose() {
    //System.out.println("disposing: " + toString());
    Enumeration ps = _ports.elements();
    while (ps.hasMoreElements()) {
      ((NetPort)ps.nextElement()).dispose();
    }
    firePropertyChange("disposed", false, true);
  }

  ////////////////////////////////////////////////////////////////
  // Visualization related methods

  /** Reply the FigNode that is appropriate for visualizing this
   *  node in the given Layer.  If no such FigNode already exists,
   *  instanciate a new one. */
  public FigNode presentationFor(Layer lay) {
    FigNode fn;
    if (lay != null) {
      fn = (FigNode) lay.presentationFor(this);
      if (fn != null) return fn;
    }
    fn = makePresentation(lay);
    return fn;
  }

  /** Construct and return a new FigNode to present this NetNode
   *  in the given Layer. A default implementation is supplied as an
   *  example, but all subclasses should override this method. NetPorts
   *  of this NetNode should be associated with individual Figs that
   *  make up the FigNode. */
  public abstract FigNode makePresentation(Layer lay);


  ////////////////////////////////////////////////////////////////
  // net-level hooks

  /** Do some application specific action just after this node is
   *  connected to another node. the arguments contain some info about
   *  what ports were connected. */
  public void postConnect(GraphModel gm, Object anotherNode,
			  Object myPort, Object otherPort) { }

  /** Do some application specific action just after this node is
   *  disconnected from another node. the arguments contain some info
   *  about what ports were connected. */
  public void postDisconnect(GraphModel gm, Object anotherNode,
			     Object myPort, Object otherPort) { }

  ////////////////////////////////////////////////////////////////
  // net-level constraints

  /** Allow foir application specific rules about which nodes can be
   *  connected to which other nodes. This is called from the NetPort,
   *  so the port has first say as to whether it can be connected to
   *  some other port. NetPort.canConnectTo() just calls
   *  NetNode.canConnectTo(). By default anything can be connected to
   *  anything. */
  public boolean canConnectTo(GraphModel gm, Object otherNode,
			      Object otherPort, Object myPort) {
    return true;
  }

  ////////////////////////////////////////////////////////////////
  // diagram-level hooks

  /** Do some application specific actions after the node is placed in
   *  a drawing area. */
  public void postPlacement(Editor ed) { }



  static final long serialVersionUID = 6562548365621157454L;
} /* end class NetNode */

