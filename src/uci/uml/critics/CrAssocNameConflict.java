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



// File: CrAssocNameConflict.java
// Classes: CrAssocNameConflict
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;

/** Well-formedness rule [2] for Namespace. See page 33 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrAssocNameConflict extends CrUML {

  public CrAssocNameConflict() {
    setHeadline("Resolve Assocaiation Name Conflict");
    sd("Every element of a namespace must have a unique name. \n\n"+
       "Clear and unambiguous naming is key to code generation and "+
       "the understandability and maintainability of the design. \n\n"+
       "To fix this, use the \"Next>\" button, or manually select the elements "+
       "and use the Properties tab to change their names.");
    addSupportedDecision(CrUML.decNAMING);
    setKnowledgeTypes(Critic.KT_SYNTAX);
    // no good trigger
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof Namespace)) return NO_PROBLEM;
    Namespace ns = (Namespace) dm;
    Vector oes = ns.getOwnedElement();
    if (oes == null) return NO_PROBLEM;
    Vector namesSeen = new Vector();
    java.util.Enumeration enum = oes.elements();
    while (enum.hasMoreElements()) {
      ElementOwnership eo = (ElementOwnership) enum.nextElement();
      ModelElement me = (ModelElement) eo.getModelElement();
      if (!(me instanceof Association)) continue;
      // needs-more-work: not implemented yet
    }
    return NO_PROBLEM;
  }

} /* end class CrAssocNameConflict.java */

