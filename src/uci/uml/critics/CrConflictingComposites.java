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



// File: CrConflictingComposites.java
// Classes: CrConflictingComposites
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** Well-formedness rule [2] for AssociationEnd. See page 28 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrConflictingComposites extends CrUML {

  public CrConflictingComposites() {
    setHeadline("Remove Conflicting Composite Associations");
    sd("A composite (black diamond) role of an association indicates that "+
       "instances of that class contain instances of the associated classes. "+
       "Since each instance can only be contained in one other object, each "+
       "object can be the 'part' in at most one is-part-of relationship.\n\n"+
       "Good OO design depends on building good is-part-of relationships.\n\n"+
       "To fix this, use the \"Next>\" button, or manually change one association "+
       "to have multiplicity to 0..1 or 1..1, or another kind "+
       "of aggregation (e.g., a white diamond is less strict), "+
       "or remove one of the associations");

    addSupportedDecision(CrUML.decCONTAINMENT);
    setKnowledgeTypes(Critic.KT_SEMANTICS);
    // no good trigger
  }

  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof Classifier)) return NO_PROBLEM;
    Classifier cls = (Classifier) dm;
    Vector conns = cls.getAssociationEnd();
    if (conns == null) return NO_PROBLEM;
    int compositeCount = 0;
    java.util.Enumeration enum = conns.elements();
    while (enum.hasMoreElements()) {
      AssociationEnd myEnd = (AssociationEnd) enum.nextElement();
      if (AggregationKind.COMPOSITE.equals(myEnd.getAggregation()))
	continue;
      Multiplicity m = myEnd.getMultiplicity();
      if (m.min() == 0) continue;
      IAssociation asc = myEnd.getAssociation();
      if (asc != null && asc.hasCompositeEnd()) compositeCount++;
    }
    if (compositeCount > 1) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

} /* end class CrConflictingComposites.java */

