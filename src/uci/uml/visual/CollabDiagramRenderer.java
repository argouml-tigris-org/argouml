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



// File: CollabDiagramRenderer.java
// Classes: CollabDiagramRenderer
// Original Author: agauthie@ics.uci.edu
// $Id$

package uci.uml.visual;

import java.util.*;

import uci.graph.*;
import uci.gef.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Behavioral_Elements.Collaborations.*;


public class CollabDiagramRenderer
implements GraphNodeRenderer, GraphEdgeRenderer {

  /** Return a Fig that can be used to represent the given node */
  public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node) {
    if (node instanceof ClassifierRole) return new FigClassifierRole(gm, node);
    if (node instanceof Message) return new FigMessage(gm, node);
    System.out.println("needs-more-work CollabDiagramRenderer getFigNodeFor");
    return null;
  }

  /** Return a Fig that can be used to represent the given edge */
  /** Generally the same code as for the ClassDiagram, since its
      very related to it. */
  public FigEdge getFigEdgeFor(GraphModel gm, Layer lay, Object edge) {
    if (edge instanceof AssociationRole) {
      AssociationRole asr = (AssociationRole) edge;
      FigAssociationRole asrFig = new FigAssociationRole(asr);
      Vector connections = asr.getAssociationEndRole();
      if (connections == null) System.out.println("null connections....");
      AssociationEndRole fromEnd = (AssociationEndRole) connections.elementAt(0);
      Classifier fromCls = (Classifier) fromEnd.getType();
      AssociationEndRole toEnd = (AssociationEndRole) connections.elementAt(1);
      Classifier toCls = (Classifier) toEnd.getType();
      FigNode fromFN = (FigNode) lay.presentationFor(fromCls);
      FigNode toFN = (FigNode) lay.presentationFor(toCls);
      asrFig.setSourcePortFig(fromFN);
      asrFig.setSourceFigNode(fromFN);
      asrFig.setDestPortFig(toFN);
      asrFig.setDestFigNode(toFN);
      return asrFig;
    }

    System.out.println("needs-more-work CollabDiagramRenderer getFigEdgeFor");
    return null;
  }

} /* end class CollabDiagramRenderer */
