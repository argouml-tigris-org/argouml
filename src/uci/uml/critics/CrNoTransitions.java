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

// File: CrNoTransitions.java
// Classes: CrNoTransitions
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

public class CrNoTransitions extends CrUML {

  public CrNoTransitions() {
    setHeadline("Add Transitions to <ocl>self</ocl>");
    sd("State <ocl>self</ocl> has no Incoming or Outgoing transitions. "+
       "Normally states have both incoming and outgoing transitions. \n\n"+
       "Defining complete state transitions is needed to complete the behavioral "+
       "specification part of your design.  \n\n"+
       "To fix this, press the \"Next>\" button, or add transitions manually "+
       "by clicking on transition tool in the tool bar and dragging from "+
       "another state to <ocl>self</ocl> or from <ocl>self</ocl> to another state. ");

    addSupportedDecision(CrUML.decSTATE_MACHINES);
    setKnowledgeTypes(Critic.KT_COMPLETENESS);
    addTrigger("incoming");
    addTrigger("outgoing");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof StateVertex)) return NO_PROBLEM;
    StateVertex sv = (StateVertex) dm;
    if (sv instanceof State) {
      StateMachine sm = ((State)sv).getStateMachine();
      if (sm != null && sm.getTop() == sv) return NO_PROBLEM;
    }
    Vector outgoing = sv.getOutgoing();
    Vector incoming = sv.getIncoming();
    boolean needsOutgoing = outgoing == null || outgoing.size() == 0;
    boolean needsIncoming = incoming == null || incoming.size() == 0;
    if (sv instanceof Pseudostate) {
      PseudostateKind k = ((Pseudostate)sv).getKind();
      if (k.equals(PseudostateKind.INITIAL)) needsIncoming = false;
      if (k.equals(PseudostateKind.FINAL)) needsOutgoing = false;
    }
    if (needsIncoming && needsOutgoing) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

} /* end class CrNoTransitions */

