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

/** An example subclass of NetPort for the EquipmentApplet demo. As
 *  part of the example I constrain the ports to only connect to
 *  PortPowers.
 *
 * @see EquipmentApplet */

public class PortPower extends NetPort implements Serializable {

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

  /** Add the constraint that PortPowers can only be connected to
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

  static final long serialVersionUID = 3420759475537576464L;

} /* end class PortPower */
