// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


// File: NetEdge.java
// Classes: NetEdge
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;
import java.beans.*;

import uci.graph.*;

/** This class models an arc in our underlying connected graph model.
 */

public abstract class NetEdge extends NetPrimitive
implements GraphEdgeHooks, java.io.Serializable {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The start and end ports of this edge. */
  protected NetPort _sourcePort;
  protected NetPort _destPort;
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
   * but I want to use Class#newInstancel so constructors do not get
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
      firePropertyChange("Disposed", false, true);
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
    fe.sourcePortFig(sourcePortFig);
    fe.destPortFig(destPortFig);
    fe.sourceFigNode(sourceFigNode);
    fe.destFigNode(destFigNode);
    fe.setOwner(this);
    return fe;
  }

  /** Override this method if you want your Edge subclasses to have a
   * different look. */
  public abstract FigEdge makePresentation(Layer lay);




} /* end class NetEdge */

