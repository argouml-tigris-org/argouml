// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: CrConflictingComposites.java.java
// Classes: CrConflictingComposites.java
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
    sd("A composite (black diamond) role of an association indicates that \n"+
       "instances of that class contain instances of the associated classes. \n"+
       "Since each instance can only be contained in one other object, each \n"+
       "object can be the 'part' in at most one is-part-of relationship.\n\n"+
       "Good OO design depends on building good is-part-of relationships.\n\n"+
       "To fix this, use the FixIt button, or manually change one association \n"+
       "to have multiplicity to 0..1 or 1..1, or another kind \n"+
       "of aggregation (e.g., a white diamond is less strict), \n"+
       "or remove one of the associations");

    addSupportedDecision(CrUML.decCONTAINMENT);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
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
      if (asc.hasCompositeEnd()) compositeCount++;
    }
    if (compositeCount > 1) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

} /* end class CrConflictingComposites.java */

