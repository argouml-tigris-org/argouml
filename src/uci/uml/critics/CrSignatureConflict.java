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

// File: CrSignatureConflict.java.java
// Classes: CrSignatureConflict.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;

/** Well-formedness rule [1] for Classifier. See page 29 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrSignatureConflict extends CrUML {

  public CrSignatureConflict() {
    setHeadline("Resolve Signature Conflict");
    sd("Two operations of {name} have same signature.  A signature "+
       "consists of the operation's name and the number and types of "+
       "its parameters.\n\n"+
       "Operations must have distinct signatures for code generation to "+
       "produce code that will compile.\n\n" +
       "To fix this, use the FixIt button, or manually double click on one "+
       "of the conflicting operations in the navigator pane and use the "+
       "Properties tab to change this name or parameters.");

    addSupportedDecision(CrUML.decMETHODS);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof uci.uml.Foundation.Core.Classifier)) return NO_PROBLEM;
    Classifier cls = (Classifier) dm;
    Vector str = cls.getBehavioralFeature();
    if (str == null) return NO_PROBLEM;
    int size = str.size();
    for (int i = 0; i < size; i++) {
      BehavioralFeature bf_i = (BehavioralFeature) str.elementAt(i);
      for (int j = i+1; j < size; j++) {
	BehavioralFeature bf_j = (BehavioralFeature) str.elementAt(j);
	if (bf_i.equals(bf_j)) return PROBLEM_FOUND;
      }
    }
    return NO_PROBLEM;
  }

} /* end class CrSignatureConflict.java */

