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

// File: CrDupParamName.java.java
// Classes: CrDupParamName.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** Well-formedness rule [1] for BehavioralFeature. See page 28 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrDupParamName extends CrUML {

  public CrDupParamName() {
    setHeadline("Duplicate Parameter Name");
    sd("Each parameter of an operation must have a unique name. \n\n"+
       "Clean and unambigous naming is needed for code generation and to \n"+
       "achieve clear and maintainable designs.\n\n"+
       "To fix this, use the FixIt button, or manually rename one of the \n"+
       "parameters to this operation.");

    addSupportedDecision(CrUML.decCONTAINMENT);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof BehavioralFeature)) return NO_PROBLEM;
    BehavioralFeature bf = (BehavioralFeature) dm;
    Vector params = bf.getParameter();
    Vector namesSeen = new Vector();
    java.util.Enumeration enum = params.elements();
    while (enum.hasMoreElements()) {
      Parameter p = (Parameter) enum.nextElement();
      Name pName = p.getName();
      if (pName == Name.UNSPEC) continue;
      String nameStr = pName.getBody();
      if (namesSeen.contains(nameStr)) return PROBLEM_FOUND;
      namesSeen.addElement(nameStr);
    }
    return NO_PROBLEM;
  }

} /* end class CrDupParamName.java */

