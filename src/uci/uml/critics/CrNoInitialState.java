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

// File: CrNoInitialState.java
// Classes: CrNoInitialState
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

public class CrNoInitialState extends CrUML {

  public CrNoInitialState() {
    setHeadline("Place an Initial State");
    sd("There is no initial state in this machine or composite state. "+
       "Normally each state machine or composite state has one initial state. \n\n"+
       "Defining unabiguous states is needed to complete the behavioral "+
       "specification part of your design.\n\n"+
       "To fix this, press the \"Next>\" button, or add manually select "+
       "initial state from the tool bar and place it in the diagram. ");

    addSupportedDecision(CrUML.decSTATE_MACHINES);
    addTrigger("substate");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof CompositeState)) return NO_PROBLEM;
    CompositeState cs = (CompositeState) dm;
    Vector peers = cs.getSubstate();
    int initialStateCount = 0;
    if (peers == null) return PROBLEM_FOUND;
    int size = peers.size();
    for (int i =0; i < size; i++) {
      Object sv = peers.elementAt(i);
      if (sv instanceof Pseudostate &&
	  (PseudostateKind.INITIAL.equals(((Pseudostate)sv).getKind())))
	initialStateCount++;
    }
    if (initialStateCount == 0) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

} /* end class CrNoInitialState */

