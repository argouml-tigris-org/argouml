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

// File: CrZeroLengthEdge.java
// Classes: CrZeroLengthEdge
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics.presentation;

import java.util.*;
import java.awt.*;
//import javax.swing.*;

import uci.argo.kernel.*;
import uci.util.*;
import uci.gef.*;
import uci.uml.critics.*;


/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */

public class CrZeroLengthEdge extends CrUML {
  ////////////////////////////////////////////////////////////////
  // constants
  public static int THRESHOLD = 12;
  
  ////////////////////////////////////////////////////////////////
  // constructor
  public CrZeroLengthEdge() {
    // needs-more-work: {name} is not expanded for diagram objects
    setHeadline("Make Edge More Visible");
    sd("This edge is too small to see easily. "+
       "This may hide important information and make it difficult for humans "+
       "to understand. A neat appearance may also make your diagrams more "+
       "influencial on other designers, implementors, and decision makers.\n\n"+
       "Constructing an understandable set of diagrams is an important "+
       "part of your design. \n\n"+
       "To fix this, move one or more nodes so that the highlighted edges will "+
       "be longer, or click in the center of the edge and drag to "+
       "make a new vertex.");

    addSupportedDecision(CrUML.decRELATIONSHIPS);
    addSupportedDecision(CrUML.decINHERITANCE);
    addSupportedDecision(CrUML.decSTATE_MACHINES);
    setKnowledgeTypes(Critic.KT_PRESENTATION);    
  }

  ////////////////////////////////////////////////////////////////
  // critiquing API
  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof FigEdge)) return NO_PROBLEM;
    FigEdge fe = (FigEdge) dm;
    int length = fe.getPerimeterLength();
    if (length > THRESHOLD) return NO_PROBLEM;
    return PROBLEM_FOUND;
  }


} /* end class CrZeroLengthEdge */

