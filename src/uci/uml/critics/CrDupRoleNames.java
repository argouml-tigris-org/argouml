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

// File: CrDupRoleNames.java
// Classes: CrDupRoleNames
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** Well-formedness rule [1] for Associations. See page 27 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrDupRoleNames extends CrUML {

  public CrDupRoleNames() {
    setHeadline("Duplicate Role Names");
    sd("Each role name must be unique within a given Association.\n\n" +
       "Using clear, unambiguous names is important to code generation\n"+
       "and and OCL constraints.\n"+
       "To fix this, select the Association and edit its role names.");
       
    addSupportedDecision(CrUML.decNAMING);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof IAssociation)) return NO_PROBLEM;
    IAssociation asc = (IAssociation) dm;
    Vector conns = asc.getConnection();
    Vector namesSeen = new Vector();
    java.util.Enumeration enum = conns.elements();
    while (enum.hasMoreElements()) {
      AssociationEnd ae = (AssociationEnd) enum.nextElement();
      Name aeName = ae.getName();
      if (aeName == Name.UNSPEC) continue;
      String nameStr = aeName.getBody();
      if (namesSeen.contains(nameStr)) return PROBLEM_FOUND;
      namesSeen.addElement(nameStr);
    }
    return NO_PROBLEM;
  }

} /* end class CrDupRoleNames */

