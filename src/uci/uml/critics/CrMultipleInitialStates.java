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

// File: CrMultipleInitialStates.java
// Classes: CrMultipleInitialStates
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Behavioral_Elements.State_Machines.*;


/** A critic to detect when a state has no outgoing transitions. */

public class CrMultipleInitialStates extends CrUML {

  public CrMultipleInitialStates() {
    setHeadline("Remove Extra Initial States");
    sd("There are multiple, ambiguous initial states in this machine. "+
       "Normally each state machine or composite state has one initial state. \n\n"+
       "Defining unabiguous states is needed to complete the behavioral "+
       "specification part of your design.\n\n"+
       "To fix this, press the \"Next>\" button, or add manually select "+
       "one of the extra initial states and remove it. ");

    addSupportedDecision(CrUML.decSTATE_MACHINES);
    addTrigger("parent");
    addTrigger("kind");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof Pseudostate)) return NO_PROBLEM;
    Pseudostate ps = (Pseudostate) dm;
    if (ps.getKind() != PseudostateKind.INITIAL) return NO_PROBLEM;
    CompositeState cs = ps.getParent();
    if (cs == null) { System.out.println("null parent state"); return NO_PROBLEM; }
    Vector peers = cs.getSubstate();
    int initialStateCount = 0;
    int size = peers.size();
    for (int i =0; i < size; i++) {
      Object sv = peers.elementAt(i);
      if (sv instanceof Pseudostate &&
	  (PseudostateKind.INITIAL.equals(((Pseudostate)sv).getKind())))
	initialStateCount++;
    }
    if (initialStateCount > 1) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

  public ToDoItem toDoItem(Object dm, Designer dsgr) {
    Pseudostate ps = (Pseudostate) dm;
    VectorSet offs = computeOffenders(ps);
    return new ToDoItem(this, offs, dsgr);
  }

  protected VectorSet computeOffenders(Pseudostate ps) {
    VectorSet offs = new VectorSet(ps);
    CompositeState cs = ps.getParent();
    if (cs == null) { System.out.println("null parent in still valid"); return offs; }
    Vector peers = cs.getSubstate();
    int size = peers.size();
    for (int i =0; i < size; i++) {
      Object sv = peers.elementAt(i);
      if (sv instanceof Pseudostate &&
	  ((Pseudostate)sv).getKind().equals(PseudostateKind.INITIAL))
	offs.addElement(sv);
    }
    return offs;
  }

  public boolean stillValid(ToDoItem i, Designer dsgr) {
    if (!isActive()) return false;
    VectorSet offs = i.getOffenders();
    Pseudostate dm = (Pseudostate) offs.firstElement();
    //if (!predicate(dm, dsgr)) return false;
    VectorSet newOffs = computeOffenders(dm);
    boolean res = offs.equals(newOffs);
//      System.out.println("offs="+ offs.toString() +
//  		       " newOffs="+ newOffs.toString() +
//  		       " res = " + res);
    return res;
  }

} /* end class CrMultipleInitialStates */

