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

// File: CrInvalidInitial.java
// Classes: CrInvalidInitial
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.state_machines.*;

import org.argouml.cognitive.*;

/** A critic to detect when an initial state has more than one
 *  outgoing transitions.  Implements a constraint from the UML
 *  1.1 standard: page 10, MPseudostate [1]. */

public class CrInvalidInitial extends CrUML {

  public CrInvalidInitial() {
    setHeadline("Remove Extra Outgoing Transitions");
    addSupportedDecision(CrUML.decSTATE_MACHINES);
    addTrigger("outgoing");
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof MPseudostate)) return NO_PROBLEM;
    MPseudostate ps = (MPseudostate) dm;
    MPseudostateKind k = ps.getKind();
    if (!MPseudostateKind.INITIAL.equals(k)) return NO_PROBLEM;
    Collection outgoing = ps.getOutgoings();
    //Vector incoming = ps.getIncoming();
    int nOutgoing = outgoing == null ? 0 : outgoing.size();
    //int nIncoming = incoming == null ? 0 : incoming.size();
    if (nOutgoing > 1) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

} /* end class CrInvalidInitial */

