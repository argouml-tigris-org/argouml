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

// File: CrUnconventionalOperName.java.java
// Classes: CrUnconventionalOperName.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;


public class CrUnconventionalOperName extends CrUML {

  public CrUnconventionalOperName() {
    setHeadline("Choose a better name");
    sd("Normally operation names begin with a lowercase letter. \n\n"+
       "Following good naming conventions help to improve \n"+
       "the understandability and maintainability of the design. \n\n"+
       "To fix this, use the FixIt button, or manually select the \n"+
       "attribute and use the Properties tab to give it a name.");
    addSupportedDecision(CrUML.decNAMING);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof Operation)) return NO_PROBLEM;
    Operation oper = (Operation) dm;
    Name myName = oper.getName();
    if (myName == null || myName.equals(Name.UNSPEC)) return NO_PROBLEM;
    String nameStr = myName.getBody();
    if (nameStr.length() == 0) return NO_PROBLEM;
    char initalChar = nameStr.charAt(0);
    if (!Character.isLowerCase(initalChar)) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

} /* end class CrUnconventionalOperName */

