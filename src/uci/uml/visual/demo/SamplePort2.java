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

// File: SamplePort2.java
// Classes: SamplePort2
// Original Author: ics125b spring 1996
// $Id$

package uci.uml.visual.demo;

import java.awt.*;
import java.util.Vector;
import java.io.*;
import uci.gef.*;
import uci.gef.demo.*;
import uci.uml.*;

/** An example subclass of NetPort for the Example application. As
 * part of the example I constrain the ports to only be on
 * SampleNode's and only connect to SamplePort2's. Needs-More-Work:
 * There should be a way to constrain the type of NetEdge that is
 * used...
 *
 * @see Example */

public class SamplePort2 extends SamplePort {

   /** Construct a new SamplePort2 as a port of the given NetNode. This
    * example includes the constraint that SamplePort2's can only be
    * part of SampleNode's. */
   SamplePort2(NetNode parent) {
     super(parent);
     if (!(parent instanceof SampleNode)) {
       // throw an exception
       System.out.println("SamplePort2s are only to be used on SampleNodes");
     }
   }

  protected Class defaultEdgeClass(NetPort otherPort) {
    try { return Class.forName("uci.gef.demo.SampleEdge2"); }
    catch (java.lang.ClassNotFoundException ignore) { return null; }
  }


  /** Add the constraint that SamplePort2's can only be connected to
   * other ports of the same type. */
  public boolean canConnectTo(NetPort anotherPort) {
    return (super.canConnectTo(anotherPort)) &&
      (anotherPort.getClass() == this.getClass());
    // needs-more-work: should work with subclasses too. This is
    // really a java.lang.Class method that is missing: isSubclass()
  }

} /* end class SamplePort2 */
