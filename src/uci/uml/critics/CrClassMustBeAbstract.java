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

// File: CrClassMustBeAbstract.java.java
// Classes: CrClassMustBeAbstract.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;

/** Well-formedness rules [1] and [3] for Class. See page 29 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrClassMustBeAbstract extends CrUML {

  public CrClassMustBeAbstract() {
    setHeadline("Class Must be Abstract");
    sd("Classes that include or inherit abstract methods from base classes or \n"+
       "interfaces must be marked Abstract.\n\n"+
       "Deciding which classes are abstract or concrete is a key part of class \n"+
       "hierarchy design.\n\n"+
       "To fix this, use the FixIt button, or manually select the class and use the \n"+
       "properties tab to add the Abstract keyword, or manually override each abstract \n"+
       "operation that is inherited from a base class or interface.");

    addSupportedDecision(CrUML.decINHERITANCE);
    addSupportedDecision(CrUML.decMETHODS);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof uci.uml.Foundation.Core.Class)) return NO_PROBLEM;
    uci.uml.Foundation.Core.Class cls = (uci.uml.Foundation.Core.Class) dm;
    if (!cls.getIsAbstract().booleanValue()) return NO_PROBLEM;
    // needs-more-work: check inheritied methods and interfaces
    Vector beh = cls.getBehavioralFeature();
    if (beh == null) return NO_PROBLEM;
    java.util.Enumeration enum = beh.elements();
    while (enum.hasMoreElements()) {
      BehavioralFeature bf = (BehavioralFeature) enum.nextElement();
      // needs-more-work
      //if (bf.getIsAbstract().booleanValue()) return PROBLEM_FOUND;
    }
    return NO_PROBLEM;
  }

} /* end class CrClassMustBeAbstract.java */

