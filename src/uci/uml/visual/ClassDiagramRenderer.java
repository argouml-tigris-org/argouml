// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.

// File: ClassDiagramRenderer.java
// Classes: ClassDiagramRenderer
// Original jrobbins@ics.uci.edu
// $Id$

package uci.uml.visual;

import java.util.*;

import uci.graph.*;
import uci.gef.*;
import uci.uml.Foundation.Core.*;

// could be singleton


/** This class defines a renderer object for UML Class Diagrams. In a
 *  Class Diagram the following UML objects are displayed with the
 *  following Figs: <p>
 * <pre>
 *  UML Object      ---  Fig
 *  ---------------------------------------
 *  MMClass         ---  FigClass
 *  Interface       ---  FigClass (needs-more-work?)
 *  Generalization  ---  FigGeneralization
 *  Realization     ---  FigGeneralization (needs-more-work)
 *  Association     ---  FigAssociation
 *  </pre>
 */

public class ClassDiagramRenderer
implements GraphNodeRenderer, GraphEdgeRenderer {

  /** Return a Fig that can be used to represent the given node */
  public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node) {
    if (node instanceof MMClass) return new FigClass(gm, node);
    else if (node instanceof Interface) return new FigClass(gm, node);
    System.out.println("needs-more-work ClassDiagramRenderer getFigNodeFor");
    return null;
  }

  /** Return a Fig that can be used to represent the given edge */
  public FigEdge getFigEdgeFor(GraphModel gm, Layer lay, Object edge) {
    System.out.println("making figedge for " + edge);
    if (edge instanceof Association) {
      Association asc = (Association) edge;
      FigAssociation ascFig = new FigAssociation(asc);
      Vector connections = asc.getConnection();
      if (connections == null) System.out.println("null connections....");
      AssociationEnd fromEnd = (AssociationEnd) connections.elementAt(0);
      Classifier fromCls = (Classifier) fromEnd.getType();
      AssociationEnd toEnd = (AssociationEnd) connections.elementAt(1);
      Classifier toCls = (Classifier) toEnd.getType();
      FigNode fromFN = (FigNode) lay.presentationFor(fromCls);
      FigNode toFN = (FigNode) lay.presentationFor(toCls);
      ascFig.sourcePortFig(fromFN);
      ascFig.sourceFigNode(fromFN);
      ascFig.destPortFig(toFN);
      ascFig.destFigNode(toFN);
      return ascFig;
    }
    if (edge instanceof Generalization) {
      Generalization gen = (Generalization) edge;
      FigGeneralization genFig = new FigGeneralization(gen);
      GeneralizableElement subType = gen.getSubtype();
      GeneralizableElement superType = gen.getSupertype();
      FigNode subTypeFN = (FigNode) lay.presentationFor(subType);
      FigNode superTypeFN = (FigNode) lay.presentationFor(superType);
      genFig.sourcePortFig(subTypeFN);
      genFig.sourceFigNode(subTypeFN);
      genFig.destPortFig(superTypeFN);
      genFig.destFigNode(superTypeFN);
      return genFig;
    }
    // what about realizations? They are not distince objects in my UML model
    // maybe they should be, just as an implementation issue, dont
    // remove any of the methods that are there now.

    System.out.println("needs-more-work ClassDiagramRenderer getFigEdgeFor");
    return null;
  }


} /* end class ClassDiagramRenderer */
