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

// File: CrFinalSubclassed.java.java
// Classes: CrFinalSubclassed.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;

/** Well-formedness rule [2] for GeneralizableElement. See page 31 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrFinalSubclassed extends CrUML {

  public CrFinalSubclassed() {
    setHeadline("This class is subclassed from a class that is marked final");
    sd("In Java, the keyword 'final' indicates that a class is not intended \n"+
       "to have subclasses. \n\n"+
       "A well thought-out class inheritance hierarchy that conveys and \n"+
       "supports intended extensions is an important part of achieving \n"+
       "an understandable and maintainable design.\n\n"+
       "To fix this, use the FixIt button, or manually select the class and \n"+
       "change its base class, or select the base class and use the properties \n"+
       "tab to remove the 'final' keyword.");

    addSupportedDecision(CrUML.decINHERITANCE);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof GeneralizableElement)) return NO_PROBLEM;
    GeneralizableElement ge = (GeneralizableElement) dm;
    Vector bases = ge.getGeneralization();
    if (bases == null) return NO_PROBLEM;
    java.util.Enumeration enum = bases.elements();
    while (enum.hasMoreElements()) {
      Generalization g = (Generalization) enum.nextElement();
      GeneralizableElement base = g.getSupertype();
      if (base.getIsLeaf()) return PROBLEM_FOUND;
    }
    return NO_PROBLEM;
  }

} /* end class CrFinalSubclassed.java */

