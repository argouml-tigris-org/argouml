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

// File: CrIllegalName.java.java
// Classes: CrIllegalName.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;


public class CrIllegalName extends CrUML {

  public CrIllegalName() {
    setHeadline("Choose a Legal Name");
    sd("The names of model elements must be sequences of letters, \n"+
       "numbers, and underscores.  They cannot contain punctuation.\n\n"+
       "useing legal names is needed for code generation. \n\n"+
       "To fix this, use the FixIt button, or manually select the \n"+
       "highlighted element and use the Properties tab to give it \n"+
       "a different name.");
    addSupportedDecision(CrUML.decNAMING);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof ModelElement)) return NO_PROBLEM;
    ModelElement me = (ModelElement) dm;
    Name meName = me.getName();
    if (meName == null || meName.equals(Name.UNSPEC)) return NO_PROBLEM;
    String nameStr = meName.getBody();
    int len = nameStr.length();
    for (int i = 0; i < len; i++) {
      char c = nameStr.charAt(i);
      if (!Character.isLetterOrDigit(c) && c != '_')
	return PROBLEM_FOUND;
    }
    return NO_PROBLEM;
  }

} /* end class CrIllegalName */

