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

// File: CrCircularInheritance.java.java
// Classes: CrCircularInheritance.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;

/** Well-formedness rule [2] for GeneralizableElement. See page 31 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrCircularInheritance extends CrUML {

  public CrCircularInheritance() {
    setHeadline("Circular Inheritance Detected ");
    sd("Inheritances relationships cannot have cycles. \n\n"+
       "A legal class inheritance hierarchy is needed for code generation \n"+
       "and the correctness of the design. \n\n"+
       "To fix this, use the FixIt button, or manually select one of the  \n"+
       "generalization arrows in the cycle and remove it.");

    addSupportedDecision(CrUML.decINHERITANCE);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof GeneralizableElement)) return NO_PROBLEM;
    GeneralizableElement ge = (GeneralizableElement) dm;
    // needs-more-work: not implemented
    return NO_PROBLEM;
  }

} /* end class CrCircularInheritance.java */

