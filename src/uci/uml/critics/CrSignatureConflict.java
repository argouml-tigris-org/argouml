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
    setHeadline("Two Operations have same Signature");
    sd("Operations must have distinct signatures.\n\n");

    addSupportedDecision(CrUML.decINHERITANCE);
    addSupportedDecision(CrUML.decMETHODS);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof uci.uml.Foundation.Core.Classifier)) return NO_PROBLEM;
    // needs-more-work 
    return NO_PROBLEM;
  }

} /* end class CrSignatureConflict.java */

