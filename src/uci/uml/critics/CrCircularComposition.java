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

// File: CrCircularComposition.java.java
// Classes: CrCircularComposition.java
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;

import uci.util.*;
import uci.argo.kernel.*;
import uci.uml.Foundation.Core.*;
import uci.uml.util.*;

/**  */

public class CrCircularComposition extends CrUML {

  public CrCircularComposition() {
    setHeadline("Remove Circular Composition");
    sd("Composition relationships (black diamonds) cannot have cycles. \n\n"+
       "A legal aggregation inheritance hierarchy is needed for code \n"+
       "generation and the correctness of the design. \n\n"+
       "To fix this, use the FixIt button, or manually select one of the  \n"+
       "associations in the cycle and remove it or change its aggregation \n"+
       "to something other than composite.");

    addSupportedDecision(CrUML.decCONTAINMENT);
  }

  protected void sd(String s) { setDescription(s); }
  
  public boolean predicate(Object dm, Designer dsgr) {
    if (!(dm instanceof Classifier)) return NO_PROBLEM;
    Classifier cls = (Classifier) dm;
    Set reach = (new Set(cls)).reachable(new GenCompositeClasses());
    if (reach.contains(cls)) return PROBLEM_FOUND;
    return NO_PROBLEM;
  }

  public ToDoItem toDoItem(Object dm, Designer dsgr) {
    Classifier cls = (Classifier) dm;
    Set offs = computeOffenders(cls);
    return new ToDoItem(this, offs, dsgr);
  }

  protected Set computeOffenders(Classifier dm) {
    Set offs = new Set(dm);
    Set above = offs.reachable(new GenCompositeClasses());
    Enumeration enum = above.elements();
    while (enum.hasMoreElements()) {
      Classifier cls2 = (Classifier) enum.nextElement();
      Set trans = (new Set(cls2)).reachable(new GenCompositeClasses());
      if (trans.contains(dm)) offs.addElement(cls2);
    }
    return offs;
  }

  public boolean stillValid(ToDoItem i, Designer dsgr) {
    if (!isActive()) return false;
    Set offs = i.getOffenders();
    Classifier dm = (Classifier) offs.firstElement();
    if (!predicate(dm, dsgr)) return false;
    Set newOffs = computeOffenders(dm);
    boolean res = offs.equals(newOffs);
//      System.out.println("offs="+ offs.toString() +
//  		       " newOffs="+ newOffs.toString() +
//  		       " res = " + res);
    return res;
  }
  
} /* end class CrCircularComposition.java */

