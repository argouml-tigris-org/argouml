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

package org.argouml.cognitive.critics;

import java.util.*;
import java.awt.*;

import org.tigris.gef.presentation.*;

import org.argouml.cognitive.*;
import org.argouml.uml.cognitive.critics.*;

/** A critic to detect when an edge is very short in order to suggest to
 *  improve the layout of the diagram.
 */
public class CrZeroLengthEdge extends CrUML {
  ////////////////////////////////////////////////////////////////
  // constants
  public static int THRESHOLD = 20;
  
  ////////////////////////////////////////////////////////////////
  // constructor
  public CrZeroLengthEdge() {
    // TODO: {name} is not expanded for diagram objects
    setHeadline("Make Edge More Visible");
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

