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

// File: CrNoGuard.java
// Classes: CrNoGuard.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.behavior.state_machines.*;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;
import org.argouml.model.ModelFacade;

public class CrNoGuard extends CrUML {

  public CrNoGuard() {
    setHeadline("Add MGuard to Transistion");
    addSupportedDecision(CrUML.decSTATE_MACHINES);
    setKnowledgeTypes(Critic.KT_COMPLETENESS);
    addTrigger("guard");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(ModelFacade.isATransition(dm))) return NO_PROBLEM;
    Object sv = ModelFacade.getSource(dm);
    if (!(ModelFacade.isAPseudostate(sv))) return NO_PROBLEM;
    if (!ModelFacade.
        equalsPseudostateKind(ModelFacade.getPseudostateKind(sv),
                              ModelFacade.BRANCH_PSEUDOSTATEKIND))
        return NO_PROBLEM;
    MGuard g = (MGuard) ModelFacade.getGuard(dm);
    boolean noGuard = (g == null || g.getExpression() == null ||
			g.getExpression().getBody() == null ||
			g.getExpression().getBody() == null ||
			g.getExpression().getBody().length() == 0);
    if (noGuard) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

} /* end class CrNoGuard */

