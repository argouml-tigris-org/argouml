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

package org.argouml.uml.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.state_machines.*;

import org.argouml.cognitive.*;

/** A critic to detect when a state has no outgoing transitions. */

public class CrNoInitialState extends CrUML {

  public CrNoInitialState() {
    setHeadline("Place an Initial MState");
    addSupportedDecision(CrUML.decSTATE_MACHINES);
    addTrigger("substate");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof MCompositeState)) return NO_PROBLEM;
    MCompositeState cs = (MCompositeState) dm;
    Collection peers = cs.getSubvertices();
    int initialStateCount = 0;
    if (peers == null) return PROBLEM_FOUND;
    int size = peers.size();
    for (Iterator iter = peers.iterator(); iter.hasNext();) {
      Object sv = iter.next();
      if (sv instanceof MPseudostate &&
	  (MPseudostateKind.INITIAL.equals(((MPseudostate)sv).getKind())))
	initialStateCount++;
    }
    if (initialStateCount == 0) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

} /* end class CrNoInitialState */

