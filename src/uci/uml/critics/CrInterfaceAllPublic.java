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

// File: CrInterfaceAllPublic.java.java
// Classes: CrInterfaceAllPublic.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** Well-formedness rule [3] for Interface. See page 32 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrInterfaceAllPublic extends CrUML {

  public CrInterfaceAllPublic() {
    setHeadline("Operations in Interfaces must be public");
    sd("Interfaces are intended to specify the set of operations that \n"+
       "other classes must implement.  The must be public. \n\n"+
       "A well-designed set of interfaces is a good way to define the \n"+
       "possible extensions of a class framework. \n\n"+
       "To fix this, use the FixIt button, or manually select the operations \n"+
       "of the interface and use the Properties tab add them public.");
    addSupportedDecision(CrUML.decPLANNED_EXTENSIONS);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof Interface)) return NO_PROBLEM;
    Interface inf = (Interface) dm;
    Vector bf = inf.getBehavioralFeature();
    if (bf == null) return NO_PROBLEM;
    java.util.Enumeration enum = bf.elements();
    while (enum.hasMoreElements()) {
      Feature f = (Feature) enum.nextElement();
      if (!f.getVisibility().equals(VisibilityKind.PUBLIC))
	return PROBLEM_FOUND;
    }
    return NO_PROBLEM;
  }

} /* end class CrInterfaceAllPublic.java */

