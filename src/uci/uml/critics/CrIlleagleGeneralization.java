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

// File: CrIlleagleGeneralization.java.java
// Classes: CrIlleagleGeneralization.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;

/** Well-formedness rule [1] for Generalization. See page 32 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrIlleagleGeneralization extends CrUML {

  public CrIlleagleGeneralization() {
    setHeadline("Illeagle Generalization ");
    sd("Model elements can only be inherit from others of the same type. \n\n"+
       "A legal inheritance hierarchy is needed for code generation \n"+
       "and the correctness of the design. \n\n"+
       "To fix this, use the FixIt button, or manually select the  \n"+
       "generalization arrow and remove it.");

    addSupportedDecision(CrUML.decINHERITANCE);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof Generalization)) return NO_PROBLEM;
    Generalization gen = (Generalization) dm;
    java.lang.Class javaClass1 = gen.getSupertype().getClass();
    java.lang.Class javaClass2 = gen.getSubtype().getClass();
    if (javaClass1 != javaClass2) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

} /* end class CrIlleagleGeneralization.java */

