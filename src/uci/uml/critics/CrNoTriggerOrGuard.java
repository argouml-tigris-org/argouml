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



// File: CrNoTriggerOrGuard.java
// Classes: CrNoTriggerOrGuard.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Behavioral_Elements.State_Machines.*;

public class CrNoTriggerOrGuard extends CrUML {

  public CrNoTriggerOrGuard() {
    setHeadline("Add Trigger or Guard to Transistion");
    sd("The highlighted Transisition is incomplete because it has no "+
       "trigger or guard condition.  Triggers are events that cause a "+
       "transition to be taken.  Guard conditions must be true for the "+
       "transition to be taken.  If only a guard is used, the transition "+
       "is taken when the condition becomes true.\n\n" +
       "This problem must be resolved to complete the state machine.\n\n"+
       "To fix this, select the Transition and use the \"Properties\" "+
       "tab, or select the Transition and type some text of the form:\n"+
       "TRIGGER [GUARD] / ACTION\n"+
       "Where TRIGGER is an event name, GUARD is a boolean expression, "+
       "and ACTION is an action to be performed when the Transition is "+
       "taken.  All three parts are optional.");

    addSupportedDecision(CrUML.decSTATE_MACHINES);
    setKnowledgeTypes(Critic.KT_COMPLETENESS);
    addTrigger("trigger");
    addTrigger("guard");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof Transition)) return NO_PROBLEM;
    Transition tr = (Transition) dm;
    Event t = tr.getTrigger();
    Guard g = tr.getGuard();
    StateVertex sv = tr.getSource();
    if (!(sv instanceof State)) return NO_PROBLEM;
    if (sv instanceof ActionState) return NO_PROBLEM;
    boolean hasTrigger = (t != null && t.getName().getBody().length() > 0);
    if (hasTrigger) return NO_PROBLEM;
    boolean noGuard = (g == null || g.getExpression() == null ||
			g.getExpression().getBody() == null ||
			g.getExpression().getBody().getBody() == null ||
			g.getExpression().getBody().getBody().length() == 0);
    if (noGuard) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

} /* end class CrNoTriggerOrGuard */

