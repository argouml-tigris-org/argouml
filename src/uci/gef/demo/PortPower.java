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

// File: PortPower.java
// Classes: PortPower
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
 *  SampleNode's and only connect to PortPower's. Needs-More-Work:
 *  There should be a way to constrain the type of NetEdge that is
 *  used...
 *
 * @see Example */

public class PortPower extends NetPort {

  ////////////////////////////////////////////////////////////////
  // constants

  public final static int SOCKET = 1;
  public final static int RECEPTICAL = 2;

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected int _type;

  ////////////////////////////////////////////////////////////////
  // constructor

  public PortPower(NetNode parent, int type) {
    super(parent);
    type(type);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public int type() { return _type; }
  public void type(int t) { if (t==SOCKET || t == RECEPTICAL) _type = t; }

  protected Class defaultEdgeClass(NetPort otherPort) {
    try { return Class.forName("uci.gef.demo.EdgePower"); }
    catch (java.lang.ClassNotFoundException ignore) { return null; }
  }

  /** Add the constraint that PortPower's can only be connected to
   *  other ports of the same type. And SOCKETs can only be connected
   *  to RECEPTICALs. */
  public boolean canConnectTo(GraphModel gm, Object otherPort) {
    return (super.canConnectTo(gm, otherPort)) &&
      (otherPort.getClass() == this.getClass()) &&
      (this.type() != ((PortPower)otherPort).type())  &&
      _edges.size() == 0;
    // needs-more-work: should work with subclasses too. This is
    // really a java.lang.Class method that is missing: isSubclass()
  }

} /* end class PortPower */
