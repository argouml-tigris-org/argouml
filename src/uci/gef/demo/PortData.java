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

// File: PortData.java
// Classes: PortData
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef.demo;

import java.awt.*;
import java.util.Vector;
import java.io.*;

import uci.gef.*;
import uci.graph.*;

/** An example subclass of NetPort for the Example application. As
 *  part of the example I constrain the ports to only be on
 *  SampleNode's and only connect to PortData's. Needs-More-Work:
 *  There should be a way to constrain the type of NetEdge that is
 *  used...
 *
 * @see Example */

public class PortData extends NetPort {

  /** Construct a new NetPort with the given parent node and no arcs. */
  public PortData(NetNode parent) { super(parent); }


  /** Add the constraint that PortData's can only be connected to
   *  other ports of the same type. */
  public boolean canConnectTo(GraphModel gm, NetPort otherPort) {
    return (super.canConnectTo(gm, otherPort)) &&
      (otherPort.getClass() == this.getClass()) &&
      _edges.size() == 0;
    // needs-more-work: should work with subclasses too. This is
    // really a java.lang.Class method that is missing: isSubclass()
  }

  protected Class defaultEdgeClass(NetPort otherPort) {
    try { return Class.forName("uci.gef.demo.EdgeData"); }
    catch (java.lang.ClassNotFoundException ignore) { return null; }
  }

} /* end class PortData */
