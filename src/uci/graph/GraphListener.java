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



// File: GraphListener.java
// Interfaces: GraphListener
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.graph;

import java.util.EventObject;
import java.util.EventListener;

/** This defines a set of event notifications that objects can
 *  register for if they are interested in changes to the connected
 *  graph.  For example, LayerPerspective implements this interface to
 *  update the Figs it contains whenever a node or edge is added or
 *  removed from the GraphModel.
 *
 * @see uci.gef.LayerPerspective */

public interface GraphListener extends EventListener {
  void nodeAdded(GraphEvent e);
  void edgeAdded(GraphEvent e);
  void nodeRemoved(GraphEvent e);
  void edgeRemoved(GraphEvent e);
  void graphChanged(GraphEvent e);
} /* end interface GraphListener */
