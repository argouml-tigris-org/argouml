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

// File: CrOppEndConflict.java.java
// Classes: CrOppEndConflict.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** Well-formedness rule [2] for Classifier. See page 29 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

//needs-more-work: split into an inherited attr critic and a local
//attr critic

public class CrOppEndConflict extends CrUML {

  public CrOppEndConflict() {
    setHeadline("Rename Association Roles");
    sd("Two roles of {name} have the same name. "+
       "Roles must have distinct names.  This may because of an inherited "+
       "attribute. \n\n"+
       "Clear and unambiguous names are key to code generation and producing "+
       "an understandable and maintainable design.\n\n"+
       "To fix this, use the FixIt button, or manually select the one of the "+
       "conflicting roles at the far end of an association from this class "+
       "and change its name.");

    addSupportedDecision(CrUML.decINHERITANCE);
    addSupportedDecision(CrUML.decRELATIONSHIPS);
    addSupportedDecision(CrUML.decNAMING);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof Classifier)) return NO_PROBLEM;
    Classifier cls = (Classifier) dm;
    Vector namesSeen = new Vector();
    Vector assocEnds = cls.getAssociationEnd();
    if (assocEnds == null) return NO_PROBLEM;
    java.util.Enumeration enum = assocEnds.elements();
    // warn about inheritied name conflicts, different critic?
    while (enum.hasMoreElements()) {
      AssociationEnd myAe = (AssociationEnd) enum.nextElement();
      Association asc = (Association) myAe.getAssociation();
      Vector conn = asc.getConnection();
      if (conn == null) continue;
      java.util.Enumeration enum2 = conn.elements();
      while (enum2.hasMoreElements()) {
	AssociationEnd ae = (AssociationEnd) enum2.nextElement();
	if (ae.getType() == cls) continue;
	Name aeName = ae.getName();
	if (aeName == Name.UNSPEC) continue;
	String aeNameStr = aeName.getBody();
	if (namesSeen.contains(aeNameStr)) return PROBLEM_FOUND;
	namesSeen.addElement(aeNameStr);
      }
    }
    return NO_PROBLEM;
  }

} /* end class CrOppEndConflict.java */

