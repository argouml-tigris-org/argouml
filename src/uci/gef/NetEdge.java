// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: NetEdge.java
// Classes: NetEdge
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.io.*;
import java.util.*;

/** This class models an arc in our underlying connected graph model.
 *  <A HREF="../features.html#graph_representation_arcs">
 *  <TT>FEATURE: graph_representation_arcs</TT></A>
 */

public abstract class NetEdge extends NetPrimitive {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The start and end ports of this edge. */
  private NetPort _sourcePort;
  private NetPort _destPort;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new NetEdge */
  public NetEdge() { }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void sourcePort(NetPort s) { _sourcePort = s; }
  public NetPort sourcePort() { return _sourcePort; }
  public void destPort(NetPort d) { _destPort = d; }
  public NetPort destPort() { return _destPort; }

  public NetPort otherEnd(NetPort oneEnd) {
    NetPort sp = sourcePort();
    if (sp == oneEnd) { return destPort(); }
    else { return sp; }
  }

  ////////////////////////////////////////////////////////////////
  // net-level hooks

  /** Connect the source and destination ports, iff they agree to
   * being connected (i.e., canConnectTo() returns true). Reply true
   * on success. This method is noramlly called after a new edge
   * instance is made. Maybe this behavior should be in a constructor,
   * but I want to use Class#newInstancel so constructors do not get
   * any arguments. */
  public boolean connect(NetPort s, NetPort d) {
    if (s.canConnectTo(d) && d.canConnectTo(s)) {
      sourcePort(s);		destPort(d);
      s.addEdge(this); 		d.addEdge(this);
      s.postConnect(d); 	d.postConnect(s);
      return true;
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////
  // Editor API

  /** Remove this NetEdge from the underlying connected graph model. */
  public void dispose() {
    System.out.println("disposing: " + toString());
    if (sourcePort() != null && destPort() != null) {
      _sourcePort.removeEdge(this);
      _destPort.removeEdge(this);
      // needs-more-work: assumes no parallel edges!
      _sourcePort.postDisconnect(destPort());
      _destPort.postDisconnect(sourcePort());
      Vector v = new Vector(2);
      v.addElement(Globals.REMOVE);
      v.addElement(this);
      setChanged();
      System.out.println("NetEdge notifying:" + toString());
      notifyObservers(v);
    }
  }

  ////////////////////////////////////////////////////////////////
  // diagram-level operations

  /** The Fig to use in views of a given type */
  public FigEdge presentationFor(Layer lay) {
    FigEdge fe = (FigEdge) lay.presentationFor(this);
    if (fe != null) return fe;
    NetNode sourceNode = _sourcePort.parentNode();
    NetNode destNode = _destPort.parentNode();
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

