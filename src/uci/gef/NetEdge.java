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




// File: NetEdge.java
// Classes: NetEdge
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;
import java.beans.*;

import uci.graph.*;

/** This class models an edge in our underlying connected graph
 *  model. This class is used by the DefaultGraphModel.  If you define
 *  your own GraphModel, you can user your own application-specific
 *  objects as edges. Needs-more-work: this should probably move to
 *  package uci.graph. */

public abstract class NetEdge extends NetPrimitive
implements GraphEdgeHooks, java.io.Serializable {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The start and end ports of this edge. */
  protected NetPort _sourcePort;
  protected NetPort _destPort;

  /** The ports that are part of this edge. Most of the time Edges do
   *  not have any ports. However, in some connected graph notations,
   *  users are allowed to attach notes to edges, or something that
   *  requrires edges to go from an edge to a node, or an edge to an
   *  edge. */
  protected Vector _ports;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new NetEdge */
  public NetEdge() { }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setSourcePort(NetPort s) { _sourcePort = s; }
  public NetPort getSourcePort() { return _sourcePort; }
  public void setDestPort(NetPort d) { _destPort = d; }
  public NetPort getDestPort() { return _destPort; }

  /** Given one port (source or destination), reply the other port
   *  (destination or source). */
  public NetPort otherEnd(NetPort oneEnd) {
    NetPort sp = getSourcePort();
    if (sp == oneEnd) { return getDestPort(); }
    else { return sp; }
  }

  public Vector getPorts() { return _ports; }
  public void setPorts(Vector v) { _ports = v; }
  
  ////////////////////////////////////////////////////////////////
  // net-level hooks

  /** Connect the source and destination ports, iff they agree to
   * being connected (i.e., canConnectTo() returns true). Reply true
   * on success. This method is noramlly called after a new edge
   * instance is made. Maybe this behavior should be in a constructor,
   * but I want to use Class#newInstance so constructors do not get
   * any arguments. */
  public boolean connect(GraphModel gm, Object srcPort, Object destPort) {
    NetPort srcNetPort = (NetPort) srcPort;
    NetPort destNetPort = (NetPort) destPort;
    if (!srcNetPort.canConnectTo(gm, destPort)) return false;
    if (!destNetPort.canConnectTo(gm, srcPort)) return false;

    setSourcePort(srcNetPort);
    setDestPort(destNetPort);

    srcNetPort.addEdge(this);
    destNetPort.addEdge(this);
    
    srcNetPort.postConnect(gm, destPort);
    destNetPort.postConnect(gm, srcPort);
    return true;
  }

  ////////////////////////////////////////////////////////////////
  // Editor API

  /** Remove this NetEdge from the underlying connected graph model. */
  public void dispose() {
    if (getSourcePort() != null && getDestPort() != null) {
      _sourcePort.removeEdge(this);
      _destPort.removeEdge(this);
      
      // needs-more-work: assumes no parallel edges!
      // needs-more-work: these nulls should be GraphModels
      _sourcePort.postDisconnect(null, getDestPort());
      _destPort.postDisconnect(null, getSourcePort());
      firePropertyChange("disposed", false, true);
    }
  }

  ////////////////////////////////////////////////////////////////
  // diagram-level operations

  /** The Fig to use in views of a given type */
  public FigEdge presentationFor(Layer lay) {
    FigEdge fe;
    if (lay != null) {
      fe = (FigEdge) lay.presentationFor(this);
      if (fe != null) return fe;
    }
    NetNode sourceNode = _sourcePort.getParentNode();
    NetNode destNode = _destPort.getParentNode();
    FigNode sourceFigNode = sourceNode.presentationFor(lay);
    FigNode destFigNode = destNode.presentationFor(lay);
    Fig sourcePortFig = sourceFigNode.getPortFig(_sourcePort);
    Fig destPortFig = destFigNode.getPortFig(_destPort);
    fe = makePresentation(lay);
    fe.setSourcePortFig(sourcePortFig);
    fe.setDestPortFig(destPortFig);
    fe.setSourceFigNode(sourceFigNode);
    fe.setDestFigNode(destFigNode);
    fe.setOwner(this);
    return fe;
  }

  /** Abstract method that returns a FigEdge to represent this edge in
   *  a given Layer.  This is just a quick and simple way to do it if
   *  you use a DefaultGraphModel because DefaultgraphEdgeRenderer
   *  calls this. Override this method if you want your Edge
   *  subclasses to have a different look.  The better way to do it is
   *  to implement your own GraphEdgeRenderer. */
  public abstract FigEdge makePresentation(Layer lay);




  static final long serialVersionUID = -1750647124723651686L;
} /* end class NetEdge */

