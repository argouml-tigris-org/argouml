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

// File: CrNonAggDataType.java.java
// Classes: CrNonAggDataType.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;

/** Well-formedness rule [1] for DataType. See page 28 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrNonAggDataType extends CrUML {

  public CrNonAggDataType() {
    setHeadline("Wrap DataType");
    sd("DataTypes are not full classes and cannot be associated with "+
       "classes, unless the DataType is part of a composite (black diamond) "+
       "aggregation. \n\n"+
       "Good OO design depends on careful choices about which entities to "+
       "represent as full objects and which to represent as attributes of "+
       "objects.\n\n"+
       "To fix this, use the FixIt button, or manually replace the DataType "+
       "with a full class or change the association aggregation to containment"+
       "by a full class.\n\n");

    addSupportedDecision(CrUML.decCONTAINMENT);
    addSupportedDecision(CrUML.decCLASS_SELECTION);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    // needs-more-work: not implemented
    return NO_PROBLEM;
  }

} /* end class CrNonAggDataType.java */

