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

/** An example subclass of NetPort for the EquipmentApplet demo. As
 *  part of the example I constrain the ports to only connect to
 *  PortDatas.
 *
 * @see EquipmentApplet */

public class PortData extends NetPort implements Serializable {

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

  /** If the user starts draggin on this port, he will create a new
   *  EdgeData. */
  protected Class defaultEdgeClass(NetPort otherPort) {
    try { return Class.forName("uci.gef.demo.EdgeData"); }
    catch (java.lang.ClassNotFoundException ignore) { return null; }
  }

  static final long serialVersionUID = 7364448827419124632L;

} /* end class PortData */
