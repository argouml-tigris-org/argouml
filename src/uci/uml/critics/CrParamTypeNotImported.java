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

// File: CrParamTypeNotImported.java.java
// Classes: CrParamTypeNotImported.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;

/** Well-formedness rule [2] for BehavioralFeature. See page 28 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrParamTypeNotImported extends CrUML {

  public CrParamTypeNotImported() {
    setHeadline("Import Parameter Type into Class");
    sd("The type of each operation parameter must be visible and imported "+
       "into the class that owns the operation.\n\n"+
       "Importing classes is needed for code generation. Good modularization "+
       "of classes into packages is key to an understandable design.\n\n"+
       "To fix this, use the FixIt button, or manually add in import to the "+
       "class that owns this operation.");

    addSupportedDecision(CrUML.decCONTAINMENT);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof BehavioralFeature)) return NO_PROBLEM;
    // needs-more-work
    return NO_PROBLEM;
  }

} /* end class CrParamTypeNotImported.java */

