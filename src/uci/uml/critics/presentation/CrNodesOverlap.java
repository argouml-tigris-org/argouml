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

// File: CrNodesOverlap.java
// Classes: CrNodesOverlap
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

public class CrNodesOverlap extends CrUML {

  ////////////////////////////////////////////////////////////////
  // constructor
  public CrNodesOverlap() {
    // needs-more-work: {name} is not expanded for diagram objects
    setHeadline("Clean Up Diagram");
    sd("Some of the objects in this diagram overlap and obscure each other. "+
       "This may hide important information and make it difficult for humans "+
       "to understand. A neat appearance may also make your diagrams more "+
       "influencial on other designers, implementors, and decision makers.\n\n"+
       "Constructing an understandable set of class diagrams is an important "+
       "part of your design. \n\n"+
       "To fix this, move the highlighted nodes in the digragm.");

    addSupportedDecision(CrUML.decCLASS_SELECTION);
    addSupportedDecision(CrUML.decEXPECTED_USAGE);
    addSupportedDecision(CrUML.decSTATE_MACHINES);
    setKnowledgeTypes(Critic.KT_PRESENTATION);    
  }

  ////////////////////////////////////////////////////////////////
  // critiquing API
  Rectangle nodeRects[] = new Rectangle[100];

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof Diagram)) return NO_PROBLEM;
    Diagram d = (Diagram) dm;
    Set offs = computeOffenders(d);
    if (offs == null) return NO_PROBLEM;
    return PROBLEM_FOUND;
  }


  public ToDoItem toDoItem(Object dm, Designer dsgr) {
    Diagram d = (Diagram) dm;
    Set offs = computeOffenders(d);
    return new ToDoItem(this, offs, dsgr);
  }

  public boolean stillValid(ToDoItem i, Designer dsgr) {
    if (!isActive()) return false;
    Set offs = i.getOffenders();
    Diagram d = (Diagram) offs.firstElement();
    //if (!predicate(dm, dsgr)) return false;
    Set newOffs = computeOffenders(d);
    boolean res = offs.equals(newOffs);
    return res;
  }
  
  public Set computeOffenders(Diagram d) {
    //needs-more-work: algorithm is n^2 in number of nodes
    Vector figs = d.getLayer().getContents();
    int numFigs = figs.size();
    int numRects = 0;
    Set offs = null;
    synchronized (nodeRects) {
      for (int i = 0; i < numFigs; i++) {
	Object o = figs.elementAt(i);
	if (o instanceof FigNode) {
	  FigNode fn = (FigNode) o;
	  for (int j = 0; j < numRects; j++)
	    if (fn.intersects(nodeRects[j])) {
	      if (offs == null) {
		offs = new Set();
		offs.addElement(d);
	      }
	      offs.addElement(fn);
	      break;
	  }
	  nodeRects[numRects++] = fn.getBounds();
	  if (numRects > 99) numRects = 99;
	}
      }
    }
    return offs;
  }

} /* end class CrNodesOverlap */

