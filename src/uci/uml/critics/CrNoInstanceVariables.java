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

// File: CrNoInstanceVariables.java
// Classes: CrNoInstanceVariables
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;

/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */

public class CrNoInstanceVariables extends CrUML {

  public CrNoInstanceVariables() {
    setHeadline("Add Instance Variables");
    sd("You have not yet specified instance variables for this class. \n"+
       "Normally classes have intstance variables that store state \n"+
       "information for each instance. \n\n"+
       "Defining instance variables needed to complete the information \n"+
       "representation part of your design \n\n"+
       "To fix this, press the FixIt icon (to left), or add instance \n"+
       "variables by selecting the class and typing them into the the \n"+
       "Src tab, Props tab, or Attrs tab");
       
    addSupportedDecision(CrUML.decINHERITANCE);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof uci.uml.Foundation.Core.Class)) return NO_PROBLEM;
    uci.uml.Foundation.Core.Class cls = (uci.uml.Foundation.Core.Class) dm;
    Vector str = cls.getStructuralFeature();
    if (str == null || str.size() == 0) return PROBLEM_FOUND;
    else return NO_PROBLEM;
  }

} /* end class CrNoInstanceVariables */

