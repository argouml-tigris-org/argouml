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

// File: CrMultipleInheritance.java.java
// Classes: CrMultipleInheritance.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics.java;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.critics.*;

/** Well-formedness rule [2] for AssociationEnd. See page 28 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrMultipleInheritance extends CrUML {

  public CrMultipleInheritance() {
    setHeadline("Change Multiple Inheritance to Interfaces");
    sd("{name} has multiple base classes, but Java does not support "+
       "mutiple inheritance.  You must use interfaces instead. \n\n"+
       "This change is required before you can generate Java code.\n\n"+
       "To fix this, use the FixIt button, or manually (1) remove one of "+
       "the base classes and then (2) optionally define a new interface "+
       "with the same method declarations and (3) add it as an "+
       "interface of {name}, and (4) move the method bodies from the "+
       "old base class down into {name}.");

    addSupportedDecision(CrUML.decINHERITANCE);
    addSupportedDecision(CrUML.decCODE_GEN);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof Classifier)) return NO_PROBLEM;
    Classifier cls = (Classifier) dm;
    Vector gen = cls.getGeneralization();
    if (gen != null && gen.size() > 1)
      return PROBLEM_FOUND;
    else
      return NO_PROBLEM;
  }

} /* end class CrMultipleInheritance.java */

