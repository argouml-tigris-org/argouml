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

// File: CrMultiComposite.java.java
// Classes: CrMultiComposite.java
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

public class CrMultiComposite extends CrUML {

  public CrMultiComposite() {
    setHeadline("Composite Role with Multiplicity > 1");
    sd("A composite (black diamond) role of an association indicates that \n"+
       "instances of that class contain instances of the associated classes. \n"+
       "Since each instance can only be contained in one other object, the \n"+
       "multiplicity of a composite role must be 0..1 or 1..1.\n\n"+
       "Good OO design depends on building good is-part-of relationships.\n\n"+
       "To fix this, use the FixIt button, or manually set the multiplicity \n"+
       "to 0..1 or 1..1, or change the composite aggregation into another kind \n"+
       "of aggregation (e.g., a white diamond is less strict).");

    addSupportedDecision(CrUML.decCONTAINMENT);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof AssociationEnd)) return NO_PROBLEM;
    AssociationEnd ae = (AssociationEnd) dm;
    AggregationKind ak = ae.getAggregation();
    Multiplicity m = ae.getMultiplicity();
    if (ak != AggregationKind.COMPOSITE) return NO_PROBLEM;
    if (m.max() <= 1) return NO_PROBLEM;
    return PROBLEM_FOUND;
  }

} /* end class CrMultiComposite.java */

