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




package uci.graph;

import java.beans.*;

/** A set of methods that ports in a GraphModel may implement.  If the
 *  objects you use to represent ports implement this interface, they
 *  will get the appropriate calls. NetPort implements these.
 *
 * @see uci.gef.NetPort */

public interface GraphPortHooks extends java.io.Serializable {

  /** Reply true if this port can legally be connected to the given
   *  port. Subclasses may implement this to reflect application
   *  specific connection constraints. By default, each port just
   *  defers that decision to its parent NetNode. By convention, your
   *  implementation should return false if super.canConnectTo() would
   *  return false (i.e., deeper subclasses get more constrained). I
   *  don't know if that is a good convention. */
  boolean canConnectTo(GraphModel gm, Object anotherPort);


  /** Application specific hook that is called after a successful
   *  connection. */
  void postConnect(GraphModel gm, Object edge);

  /** Application specific hook that is called after a
   *  disconnection. (for now, all disconnections are assumed
   *  legal). */
  void postDisconnect(GraphModel gm, Object edge);

  void addPropertyChangeListener(PropertyChangeListener l);
  void removePropertyChangeListener(PropertyChangeListener l);
  void setHighlight(boolean b);
  void dispose();


  static final long serialVersionUID = -1522217911769251409L;
} /* end interface GraphPortHooks */
