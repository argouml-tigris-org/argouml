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




// File: SamplePort2.java
// Classes: SamplePort2
// Original Author: ics125b spring 1996
// $Id$

package uci.gef.demo;

import java.awt.*;
import java.util.Vector;
import java.io.*;

import uci.gef.*;
import uci.graph.*;

/** An example subclass of NetPort for the Example application. 
 *
 * @see BasicApplication */

public class SamplePort2 extends SamplePort implements Serializable {

   /** Construct a new SamplePort2 as a port of the given NetNode. This
    * example includes the constraint that SamplePort2's can only be
    * part of SampleNode's. */
  public SamplePort2(NetNode parent) {
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
  public boolean canConnectTo(GraphModel gm, Object anotherPort) {
    return (super.canConnectTo(gm, anotherPort)) &&
      (anotherPort.getClass() == this.getClass());
    // needs-more-work: should work with subclasses too. This is
    // really a java.lang.Class method that is missing: isSubclass()
  }

  static final long serialVersionUID = -1531712489556040622L;
} /* end class SamplePort2 */
