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



// File: GraphModel.java
// Interfaces: GraphModel
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.graph;

import java.util.*;

/** This interface provides a facade to a net-level
 *  representation. Similiar in concept to the Swing class
 *  TreeModel.<p>
 *
 * The idea is not to have a widget (like JGraph) storing all the
 * information that it should display, and the programmer having to
 * keep the widget's data in synch with the application's data.
 * Instead, the programmer defines a Model class that gives the widget
 * access to the application data.  That way there is only one copy of
 * the data and nothing can get out of synch.  If you don't have your
 * own application data objects, there is a Default implementation of
 * the Model that will store it for you.<p>
 *
 * Instead of asking application programmers to subclass their data
 * objects from some predefined base class (like NetNode), this
 * interface allows the use of any application object as a node, port,
 * or edge.  This makes it much easier to add a visualization to an
 * existing application.
 *
 * @see DefaultGraphModel
 * @see AdjacencyMatrixGraphModel
 * @see uci.graph.demo.WordTransforms */

public interface GraphModel extends java.io.Serializable {
  /** Return all nodes in the graph */
  Vector getNodes();

  /** Return all nodes in the graph */
  Vector getEdges();

  /** Return all ports on node or edge */
  Vector getPorts(Object nodeOrEdge);

  /** Return the node or edge that owns the given port */
  Object getOwner(Object port);

  /** Return all edges going to given port */
  Vector getInEdges(Object port);

  /** Return all edges going from given port */
  Vector getOutEdges(Object port);

  /** Return one end of an edge */
  Object getSourcePort(Object edge);

  /** Return  the other end of an edge */
  Object getDestPort(Object edge);

  void addGraphEventListener(GraphListener listener);
  void removeGraphEventListener(GraphListener listener);

  static final long serialVersionUID = -110420686972437523L;
} /* end interface GraphModel */
