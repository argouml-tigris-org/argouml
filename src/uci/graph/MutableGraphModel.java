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



// File: MutableGraphModel.java
// Interfaces: MutableGraphModel
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.graph;

import java.util.*;

/** This interface provides a facade to a net-level
 *  representation. Similiar in concept to the Swing class
 *  TreeModel.<p>
 *
 *  This interface goes beyond GraphModel in that it allows
 *  modifications to the graph, instead of just access.
 *
 * @see DefaultGraphModel
 * @see AdjacencyMatrixGraphModel
 * @see uci.graph.demo.WordTransforms */

public interface MutableGraphModel extends GraphModel {
  /** Return true if the given object is a valid node in this graph */
  boolean canAddNode(Object node);

  /** Return true if the given object is a valid edge in this graph */
  boolean canAddEdge(Object edge);

  /** Remove the given node from the graph. Sends a notification. */
  void removeNode(Object node);

  /** Add the given node to the graph, if valid. Sends a notification. */
  void addNode(Object node);

  /** Add the given edge to the graph, if valid. Sends a notification. */
  void addEdge(Object edge);

  /** Remove the given edge from the graph. Sends a notification. */
  void removeEdge(Object edge);

  /** Return true if the two given ports can be connected by a 
   * kind of edge to be determined by the ports. */
  boolean canConnect(Object fromP, Object toP);

  /** Return true if the two given ports can be connected by the given
   * kind of edge. */
  boolean canConnect(Object fromP, Object toP, Class edgeClass);

  /** Contruct and add a new edge of a kind determined by the
   *  ports. Sends a notification.  */
  Object connect(Object fromPort, Object toPort);

  /** Contruct and add a new edge of the given kind. Sends a notification. */
  Object connect(Object fromPort, Object toPort, Class edgeClass);

  static final long serialVersionUID = -752649940921643399L;
} /* end interface MutableGraphModel */
